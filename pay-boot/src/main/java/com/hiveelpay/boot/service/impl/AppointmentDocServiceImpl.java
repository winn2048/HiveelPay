package com.hiveelpay.boot.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hiveelpay.boot.ctrl.exceptions.HiveelPayErrorCode;
import com.hiveelpay.boot.service.AppointmentDocService;
import com.hiveelpay.boot.service.events.NotificationEvent;
import com.hiveelpay.boot.service.events.TradeInSkipPaymentEvent;
import com.hiveelpay.boot.service.exceptions.HiveelPayServiceErrorCode;
import com.hiveelpay.common.enumm.*;
import com.hiveelpay.common.exceptions.HiveelBizException;
import com.hiveelpay.common.model.HiveelPage;
import com.hiveelpay.common.model.requests.AppointmentSearchRequest;
import com.hiveelpay.common.util.AmountUtil;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.mapper.AppointmentDocMapper;
import com.hiveelpay.dal.dao.mapper.BizOrderMapper;
import com.hiveelpay.dal.dao.mapper.CustomerValidServicesMapper;
import com.hiveelpay.dal.dao.model.AppointmentDoc;
import com.hiveelpay.dal.dao.model.BizOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.hiveelpay.common.enumm.AppointmentStatus.*;
import static com.hiveelpay.common.util.DateUtil.FORMAT_MMddyyyyHHmm;

@Service
public class AppointmentDocServiceImpl implements AppointmentDocService {
    private static final MyLog _log = MyLog.getLog(BizOrderServiceImpl.class);

    @Autowired
    private AppointmentDocMapper appointmentDocMapper;
    @Autowired
    private BizOrderMapper bizOrderMapper;
    @Autowired
    private CustomerValidServicesMapper customerValidServicesMapper;
    @Autowired
    private ApplicationEventPublisher publisher;


    @Override
    public boolean save(AppointmentDoc appointmentDoc) {
        if (appointmentDoc == null) {
            return false;
        }
        try {
            if (appointmentDoc.getAppointmentType().equals(AppointmentType.INSTANT)) {// 快速申请需要审核
                appointmentDoc.setAppointmentStatus(AppointmentStatus.PENDING);
            } else if (appointmentDoc.getAppointmentType().equals(AppointmentType.REGULAR)) {
                appointmentDoc.setAppointmentStatus(AppointmentStatus.SAVED);
            }

            return appointmentDocMapper.save(appointmentDoc) == 1;
        } catch (Exception e) {
            e.printStackTrace();
            _log.error("", e);
        }
        return false;
    }

    @Override
    public List<AppointmentDoc> findUserAppointments(String userId, AppointmentStatus appointmentStatus, HiveelPage page) {
        if (page == null) {
            page = new HiveelPage();
        }
        if (page.getCurrentPage() == 1) {
            page.setTotal(appointmentDocMapper.countUserAppointments(userId, appointmentStatus));
        }
        return appointmentDocMapper.findUserAppointments(userId, appointmentStatus, page);
    }

    @Override
    public AppointmentDoc findUserAppointment(String userId, String appointmentId) {
        return appointmentDocMapper.findUserAppointment(userId, appointmentId);
    }

    @Override
    public List<AppointmentDoc> findAppointmentsForMch(String mchId, String storeId, BusinessTypeEnum businessType, TradeInTypeEnum tradeInType, HiveelPage page) {
        if (page == null) {
            page = new HiveelPage();
        }
        if (tradeInType == null) {
            tradeInType = TradeInTypeEnum.ILLEGAL;
        }
        Set<AppointmentStatus> statusSet = Sets.newHashSet();
        statusSet.add(VALID);
        statusSet.add(SERVICE_DONE);
        statusSet.add(CANCELED);

        if (page.getCurrentPage() == 1) {
            page.setTotal(appointmentDocMapper.countMchAppointment(mchId, storeId, businessType, tradeInType, statusSet));
        }
        return appointmentDocMapper.findMchAppointments(mchId, storeId, businessType, tradeInType, page, statusSet);
    }

    @Override
    public AppointmentDoc findAppointmentForMch(String mchId, String appointmentId) {
        if (Strings.isNullOrEmpty(mchId) || Strings.isNullOrEmpty(appointmentId)) {
            return null;
        }
        return appointmentDocMapper.findAppointmentForMch(mchId, appointmentId);
    }

    /**
     * 取消预约
     *
     * @param appointmentDoc
     * @return
     */
    @Override
    @Transactional
    public Boolean cancelAppointment(AppointmentDoc appointmentDoc) {
        if (appointmentDoc == null) {
            return Boolean.FALSE;
        }
        if (appointmentDoc.getAppointmentStatus().equals(VALID)) {
            int i = appointmentDocMapper.updateStatus(appointmentDoc.getAppointmentId(), VALID, CANCELED);
            if (i != 1) {
                return Boolean.FALSE;
            }
            // todo maybe need refund money here!

            BizOrder bizOrder = bizOrderMapper.selectByBizOrderNo(appointmentDoc.getBizOrderNo());
            int canceled = bizOrderMapper.updateBizOrdersStatus(Collections.singletonList(appointmentDoc.getBizOrderNo()), bizOrder.getOrderStatus(), BizOrderStatus.CANCELED);
            if (canceled == 0) {
                throw new HiveelBizException(HiveelPayServiceErrorCode.CANCEL_APPOINTMENT_ERROR);
            }

            Set<CustomerServiceStatusEnum> statusSet = Sets.newHashSet();
            statusSet.add(CustomerServiceStatusEnum.INIT);
            statusSet.add(CustomerServiceStatusEnum.IN_SERVICE);

            int size = customerValidServicesMapper.cancelServiceByBizOrderNo(bizOrder.getBizOrderNo(), statusSet, CustomerServiceStatusEnum.CANCELED);
            if (size == 0) {
                throw new HiveelBizException(HiveelPayServiceErrorCode.CANCEL_APPOINTMENT_ERROR);
            }
            notifyUser(appointmentDoc);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private void notifyUser(AppointmentDoc appointmentDoc) {
        if (appointmentDoc == null) {
            return;
        }
        if (appointmentDoc.getBusinessType().equals(BusinessTypeEnum.TRADE_IN)) {
            publisher.publishEvent(new NotificationEvent(
                    NotificationEvent.ActionPoint.APPOINTMENT_AT_TOMORROW_TRADE_IN,
                    NotificationEvent.NoticeChannel.EMAIL,
                    appointmentDoc.getAppointmentId())
            );
            return;
        }
        if (appointmentDoc.getBusinessType().equals(BusinessTypeEnum.OIL_CHANGE)) {
            publisher.publishEvent(new NotificationEvent(
                    NotificationEvent.ActionPoint.APPOINTMENT_AT_TOMORROW_OIL_CHANGE,
                    NotificationEvent.NoticeChannel.EMAIL,
                    appointmentDoc.getAppointmentId()));
            return;
        }
        if (appointmentDoc.getBusinessType().equals(BusinessTypeEnum.PRE_INSPECTION)) {
            publisher.publishEvent(new NotificationEvent(
                    NotificationEvent.ActionPoint.APPOINTMENT_AT_TOMORROW_PRE_INSPECTION,
                    NotificationEvent.NoticeChannel.EMAIL,
                    appointmentDoc.getAppointmentId())
            );
        }

    }

    @Override
    public List<AppointmentDoc> searchAppointment(AppointmentSearchRequest searchRequest, HiveelPage page) {
        if (page == null) {
            page = new HiveelPage();
        }
        Set<AppointmentStatus> statusSet = Sets.newHashSet();
        if (searchRequest.getAppointmentStatus() == null) {
            statusSet.add(VALID);
            statusSet.add(SERVICE_DONE);
            statusSet.add(CANCELED);
            statusSet.add(CANCEL_AND_REFUNDED);
        } else {
            statusSet.add(searchRequest.getAppointmentStatus());
        }

        if (page.getCurrentPage() == 1) {
            page.setTotal(appointmentDocMapper.appointmentSearchCount(searchRequest, statusSet));
        }
        return appointmentDocMapper.appointmentSearch(searchRequest, statusSet, page);
    }

    @Override
    public void invalidAppointment() {
        Set<AppointmentStatus> statusSet = Sets.newHashSet();
        statusSet.add(SAVED);
        // 预约时间 距离现在24小时 且未付款 的预约单
        List<AppointmentDoc> list = appointmentDocMapper.searchByHoursLeave(24, statusSet);
        if (list == null || list.isEmpty()) {
            _log.info("################------No invalid appointments.");
            return;
        }
        _log.info("################------{} invalid appointments.", list.size());

        list.forEach(i -> {
            appointmentDocMapper.updateStatus(i.getAppointmentId(), AppointmentStatus.SAVED, AppointmentStatus.INVALID);
            final String bizOrderNo = i.getBizOrderNo();

            if (!Strings.isNullOrEmpty(bizOrderNo)) {
                bizOrderMapper.updateBizOrdersStatus(Lists.newArrayList(bizOrderNo), BizOrderStatus.SAVED, BizOrderStatus.EXPIRED);
            }
        });

    }

    @Override
    public void serviceDone(int livingDays) {
        if (livingDays <= 0) {
            livingDays = 0;
        }
        Set<AppointmentStatus> statusSet = Sets.newHashSet();
        statusSet.add(VALID);

        AppointmentSearchRequest searchRequest = new AppointmentSearchRequest();
        searchRequest.setKeyWords("");
        searchRequest.setStartDateStr(DateUtil.date2Str(DateUtil.addDays(new Date(), -livingDays), FORMAT_MMddyyyyHHmm));
        searchRequest.setEndDateStr(DateUtil.date2Str(DateUtil.addDays(new Date(), -2), FORMAT_MMddyyyyHHmm));
        List<AppointmentDoc> list = appointmentDocMapper.appointmentSearch(searchRequest, statusSet, null);
        if (list == null || list.isEmpty()) {
            _log.info("################------No serviceDone appointments.");
            return;
        }
        _log.info("################------{} serviceDone appointments.", list.size());
        list.forEach(i -> {
            appointmentDocMapper.updateStatus(i.getAppointmentId(), VALID, SERVICE_DONE);
            final String bizOrderNo = i.getBizOrderNo();

            if (!Strings.isNullOrEmpty(bizOrderNo)) {
                BizOrder bizOrder = bizOrderMapper.selectByBizOrderNo(bizOrderNo);
                if (bizOrder != null) {
                    bizOrderMapper.updateBizOrdersStatus(Lists.newArrayList(bizOrderNo), bizOrder.getOrderStatus(), BizOrderStatus.SERVICE_END);
                }
            }
        });
    }

    @Override
    public List<AppointmentDoc> findAppointmentsByInvoiceId(String invoiceId) {
        List<AppointmentDoc> list = appointmentDocMapper.findAppointmentsByInvoiceId(invoiceId);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public List<AppointmentDoc> findOneDayAppointments(String mchId, String storeId, String dateStr, Integer dayNum) {
        String dayStr = DateUtil.formatDayNumToDayStr(dayNum);
        Set<AppointmentStatus> statusSet = Sets.newHashSet();
        statusSet.add(VALID);
        return appointmentDocMapper.findOneDayAppointments(mchId, storeId, dateStr, dayStr, statusSet);
    }

    @Override
    public AppointmentDoc findByDocId(String docId) {
        if (Strings.isNullOrEmpty(docId)) {
            return null;
        }
        return appointmentDocMapper.findByAppointmentId(docId);
    }

    @Override
    public boolean update(AppointmentDoc appointmentDoc) {
        if (appointmentDoc == null) {
            return false;
        }
        if (Strings.isNullOrEmpty(appointmentDoc.getAppointmentId())) {
            return false;
        }
        if (appointmentDoc.getAppointmentStatus() == null || appointmentDoc.getAppointmentStatus().equals(AppointmentStatus.ILLEGAL)) {
            throw new HiveelBizException(HiveelPayErrorCode.ILLEGAL_PARAMETERS);
        }
        return appointmentDocMapper.updateAppointment(appointmentDoc) == 1;
    }

    @Override
    public AppointmentDoc findAppointmentsById(String appointmentId) {
        return appointmentDocMapper.findByAppointmentId(appointmentId);
    }

    @Override
    public void notifyUserServiceStart() {
        Set<AppointmentStatus> statusSet = Sets.newHashSet();
        statusSet.add(VALID);
        // 预约时间 距离现在24小时 且支付成功 的预约单
        List<AppointmentDoc> list = appointmentDocMapper.searchByHoursLeave(24, statusSet);
        if (list == null || list.isEmpty()) {
            _log.info("################------No   appointments.");
            return;
        }
        _log.info("################------We have {}   appointments.", list.size());

        list.forEach(i -> {
            if (i.getBusinessType().equals(BusinessTypeEnum.TRADE_IN)) {
                publisher.publishEvent(new NotificationEvent(
                        NotificationEvent.ActionPoint.APPOINTMENT_AT_TOMORROW_TRADE_IN,
                        NotificationEvent.NoticeChannel.EMAIL,
                        i.getAppointmentId())
                );
            } else if (i.getBusinessType().equals(BusinessTypeEnum.OIL_CHANGE)) {
                publisher.publishEvent(new NotificationEvent(
                        NotificationEvent.ActionPoint.APPOINTMENT_AT_TOMORROW_OIL_CHANGE,
                        NotificationEvent.NoticeChannel.EMAIL,
                        i.getAppointmentId())
                );
            } else if (i.getBusinessType().equals(BusinessTypeEnum.PRE_INSPECTION)) {
                publisher.publishEvent(new NotificationEvent(
                        NotificationEvent.ActionPoint.APPOINTMENT_AT_TOMORROW_PRE_INSPECTION,
                        NotificationEvent.NoticeChannel.EMAIL,
                        i.getAppointmentId())
                );
            }
        });
    }

    @Override
    public void sayGoodByToUser() {
        Set<AppointmentStatus> statusSet = Sets.newHashSet();
        statusSet.add(VALID);
        statusSet.add(SERVICE_DONE);

        // 预约时间已过3小时
        List<AppointmentDoc> list = appointmentDocMapper.searchByHoursLeave(-3, statusSet);
        if (list == null || list.isEmpty()) {
            _log.info("################------No  appointments.");
            return;
        }
        _log.info("################------We have {}   appointments.", list.size());

        list.forEach(i -> {
            if (i.getBusinessType().equals(BusinessTypeEnum.TRADE_IN)) {
                publisher.publishEvent(new NotificationEvent(
                        NotificationEvent.ActionPoint.APPOINTMENT_THX_TRADE_IN,
                        NotificationEvent.NoticeChannel.EMAIL,
                        i.getAppointmentId()));
            } else if (i.getBusinessType().equals(BusinessTypeEnum.OIL_CHANGE)) {
                publisher.publishEvent(new NotificationEvent(
                        NotificationEvent.ActionPoint.APPOINTMENT_THX_OIL_CHANGE,
                        NotificationEvent.NoticeChannel.EMAIL,
                        i.getAppointmentId()));
            } else if (i.getBusinessType().equals(BusinessTypeEnum.PRE_INSPECTION)) {
                publisher.publishEvent(new NotificationEvent(
                        NotificationEvent.ActionPoint.APPOINTMENT_THX_PRE_INSPECTION,
                        NotificationEvent.NoticeChannel.EMAIL,
                        i.getAppointmentId()));
            }
        });
    }

    @Override
    public boolean quoteAppt(String appointmentId, String price) {
        int count = appointmentDocMapper.quoteAppt(appointmentId, Long.valueOf(AmountUtil.convertDollar2Cent(price)));
        if (count == 1) {
            publisher.publishEvent(new NotificationEvent(NotificationEvent.ActionPoint.APPOINTMENT_QUOTE_READY, NotificationEvent.NoticeChannel.EMAIL_AND_SMS, appointmentId));
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public Boolean passPendingAppt(AppointmentDoc appointmentDoc) {
        if (appointmentDoc == null) {
            return false;
        }

        if(appointmentDoc.getAppointmentStatus().equals(AppointmentStatus.SAVED)){
            publisher.publishEvent(new TradeInSkipPaymentEvent(appointmentDoc));
            return true;
        }

        int i = appointmentDocMapper.updateStatus(appointmentDoc.getAppointmentId(), AppointmentStatus.PENDING, AppointmentStatus.SAVED);
        if (i == 1) {
            publisher.publishEvent(new TradeInSkipPaymentEvent(appointmentDoc));
        }
        return true;
    }
}
