package com.hiveelpay.mgr.service.jobs;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.hiveelpay.common.enumm.AppointmentStatus;
import com.hiveelpay.common.enumm.BizOrderStatus;
import com.hiveelpay.common.enumm.InvoiceStatusEnum;
import com.hiveelpay.common.model.requests.AppointmentSearchRequest;
import com.hiveelpay.common.util.AmountUtil;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.common.util.MySeq;
import com.hiveelpay.dal.dao.mapper.AppointmentDocMapper;
import com.hiveelpay.dal.dao.mapper.BizOrderMapper;
import com.hiveelpay.dal.dao.mapper.InvoiceMapper;
import com.hiveelpay.dal.dao.model.AppointmentDoc;
import com.hiveelpay.dal.dao.model.BizOrder;
import com.hiveelpay.dal.dao.model.Invoice;
import com.hiveelpay.mgr.service.events.NotificationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.hiveelpay.common.util.DateUtil.FORMAT_MMddyyyyHHmm;

@Component
public class InvoiceJob {
    private static final MyLog _log = MyLog.getLog(InvoiceJob.class);

    @Autowired
    private BizOrderMapper bizOrderMapper;
    @Autowired
    private AppointmentDocMapper appointmentDocMapper;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private ApplicationEventPublisher publisher;

    private static final String MCHID_TRADIN = "10000002";
    private static final String MCHID_PREINSPECTION_OILCHANGE = "10000001";

    /**
     * Trade-in
     * 每月1号和15号生成 结算 发票 信息
     */
    @Async
    @Scheduled(cron = "0 0 1 1,16 * ?")// 每月1号和16号的凌晨1点执行
    public void generateInvoice() {
        String startDateStr;
        String endDateStr;

        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (day >= 1 && day <= 15) {
            Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.MONTH, -1);
            c1.set(Calendar.DAY_OF_MONTH, 16);
            cleanLessHour(c1);
            startDateStr = DateUtil.date2Str(c1.getTime(), FORMAT_MMddyyyyHHmm);

            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.DAY_OF_MONTH, 1);
            cleanLessHour(c2);
            endDateStr = DateUtil.date2Str(c2.getTime(), FORMAT_MMddyyyyHHmm);
        } else {
            Calendar c1 = Calendar.getInstance();
            c1.set(Calendar.DAY_OF_MONTH, 1);
            cleanLessHour(c1);
            startDateStr = DateUtil.date2Str(c1.getTime(), FORMAT_MMddyyyyHHmm);

            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.DAY_OF_MONTH, 16);
            cleanLessHour(c2);
            endDateStr = DateUtil.date2Str(c2.getTime(), FORMAT_MMddyyyyHHmm);
        }
        _log.info("#INVOICE# Start to generate invoice, from{} to {} ", startDateStr, endDateStr);
        final AppointmentSearchRequest searchRequest = new AppointmentSearchRequest();
        searchRequest.setStartDateStr(startDateStr);
        searchRequest.setEndDateStr(endDateStr);
        searchRequest.setKeyWords("");

        Set<AppointmentStatus> statusSet = Sets.newHashSet();
        statusSet.add(AppointmentStatus.VALID);
        statusSet.add(AppointmentStatus.SERVICE_DONE);

        List<AppointmentDoc> list = appointmentDocMapper.appointmentSearch(searchRequest, statusSet, null);

        if (list == null || list.isEmpty()) {
            generateBlankInvoice(MCHID_TRADIN, startDateStr, endDateStr);
            return;
        }
        /**
         * 只有trade_in是半月结算一次
         */
        generateInvoice(MCHID_TRADIN, startDateStr, endDateStr, list);
    }


    /**
     * pre-inspection 和 oil-change 服务商的发票生成
     */
    @Async
    @Scheduled(cron = "0 0 1 ? * MON")//每周一凌晨1点执行一次
    public void generateInvoice2() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i - 6);
        cleanLessHour(c);
        String startDateStr = DateUtil.date2Str(c.getTime(), FORMAT_MMddyyyyHHmm);

        c.add(Calendar.DATE, 7);
        cleanLessHour(c);
        String endDateStr = DateUtil.date2Str(c.getTime(), FORMAT_MMddyyyyHHmm);

        _log.info("#INVOICE# Start to generate invoice, from{} to {} ", startDateStr, endDateStr);
        final AppointmentSearchRequest searchRequest = new AppointmentSearchRequest();
        searchRequest.setStartDateStr(startDateStr);
        searchRequest.setEndDateStr(endDateStr);
        searchRequest.setKeyWords("");
        searchRequest.setMchId(MCHID_PREINSPECTION_OILCHANGE);//oil-change and pre-inspection

        Set<AppointmentStatus> statusSet = Sets.newHashSet();
        statusSet.add(AppointmentStatus.VALID);
        statusSet.add(AppointmentStatus.SERVICE_DONE);

        List<AppointmentDoc> list = appointmentDocMapper.appointmentSearch(searchRequest, statusSet, null);
        if (list == null || list.isEmpty()) {
            generateBlankInvoice("10000001", startDateStr, endDateStr);
            return;
        }
        generateInvoice(MCHID_PREINSPECTION_OILCHANGE, startDateStr, endDateStr, list);
    }

    private void cleanLessHour(Calendar c1) {
        c1.setTime(DateUtil.strToDate(DateUtil.date2Str(c1.getTime(), DateUtil.FORMAT_MMddyyyy), DateUtil.FORMAT_MMddyyyy));
    }

    @Transactional
    public void generateInvoice(String mchId, String finalStartDateStr, String finalEndDateStr, List<AppointmentDoc> appointmentDocList) {
        if (isInvoiceGenerated(mchId, finalStartDateStr, finalEndDateStr)) return;
        final String invoiceId = MySeq.getInvoiceId();
        Invoice invoice = new Invoice();
        invoice.setMchId(mchId);
        invoice.setInvoiceId(invoiceId);
        invoice.setInvoiceStatus(InvoiceStatusEnum.NEW_SAVED);
        invoice.setDateFrom(DateUtil.strToDate(finalStartDateStr, FORMAT_MMddyyyyHHmm));
        invoice.setDateTo(DateUtil.strToDate(finalEndDateStr, FORMAT_MMddyyyyHHmm));

        Set<String> appointmentIds = appointmentDocList.stream().filter(Objects::nonNull).map(AppointmentDoc::getAppointmentId).collect(Collectors.toSet());
        List<String> bizOrderIdList = appointmentDocList.stream().filter(Objects::nonNull).map(AppointmentDoc::getBizOrderNo).collect(Collectors.toList());
        if (bizOrderIdList.isEmpty()) {
            invoice.setInvoiceAmount(0L);
            invoice.setSettledAmount(0L);
            invoice.setTotalRecords(0);
            invoice.setCommissionAmount(0L);
        } else {
            List<BizOrder> bizOrderList = bizOrderMapper.selectByBizOrderNoList(bizOrderIdList);
            Long sumTotal = bizOrderList.stream().filter(i -> i != null && (
                    i.getOrderStatus().equals(BizOrderStatus.PAY_SUCCESSED)
                            || i.getOrderStatus().equals(BizOrderStatus.SERVICE_ING)
                            || i.getOrderStatus().equals(BizOrderStatus.SERVICE_SETUPED)
                            || i.getOrderStatus().equals(BizOrderStatus.SERVICE_END))
            ).map(BizOrder::getPayAmount).mapToLong(Long::longValue).sum();
            invoice.setInvoiceAmount(sumTotal);
            invoice.setSettledAmount(sumTotal);

            if (mchId.equalsIgnoreCase(MCHID_TRADIN)) {// trade-in
                invoice.setCommissionAmount(Long.valueOf(AmountUtil.convertDollar2Cent(String.valueOf(appointmentIds.size() * 30))));// 每单给我们30美金的反佣
                invoice.setRemark("(This is a trad-in merchant.)Hiveel will get " + appointmentIds.size() + "(appointments)*$30(per-appointment)");
            } else if (mchId.equalsIgnoreCase(MCHID_PREINSPECTION_OILCHANGE)) {// oil-change and pre-inspection
                invoice.setCommissionAmount(-(sumTotal / 10) * 8);// 要支付出去80%， 2/8 分成，我们得2
                invoice.setRemark("(This is a oil-change and pre-inspection merchant.)Hiveel will pay-out:" + sumTotal + "(cents)*80%");
            } else {
                invoice.setCommissionAmount(0L);
                invoice.setRemark("Hiveel");
            }
            invoice.setTotalRecords(appointmentIds.size());
        }

        invoiceMapper.saveOne(invoice);
        if (!appointmentIds.isEmpty()) {
            appointmentDocMapper.associateInvoiceId(appointmentIds, invoiceId);
            publisher.publishEvent(new NotificationEvent(NotificationEvent.ActionPoint.NEW_INVOICE_GENERATED, NotificationEvent.NoticeChannel.EMAIL, invoiceId));
        }
    }

    private boolean isInvoiceGenerated(String mchId, String finalStartDateStr, String finalEndDateStr) {
        return invoiceMapper.countInvoice(mchId, finalStartDateStr, finalEndDateStr) >= 1;
    }

    private void generateBlankInvoice(String mchId, String finalStartDateStr, String finalEndDateStr) {
        if (Strings.isNullOrEmpty(mchId)) {
            return;
        }
        if (isInvoiceGenerated(mchId, finalStartDateStr, finalEndDateStr)) return;
        _log.info("#INVOICE# Start to generate blank invoice, from{} to {}, for mchId:{}", finalStartDateStr, finalEndDateStr, mchId);
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(MySeq.getInvoiceId());
        invoice.setDateFrom(DateUtil.strToDate(finalStartDateStr, FORMAT_MMddyyyyHHmm));
        invoice.setDateTo(DateUtil.strToDate(finalEndDateStr, FORMAT_MMddyyyyHHmm));
        invoice.setMchId(mchId);
        invoice.setInvoiceAmount(0L);
        invoice.setSettledAmount(0L);
        invoice.setTotalRecords(0);
        invoice.setCommissionAmount(0L);
        invoice.setInvoiceStatus(InvoiceStatusEnum.NEW_SAVED);
        invoice.setRemark("Blank invoice.");
        invoiceMapper.saveOne(invoice);
    }
}
