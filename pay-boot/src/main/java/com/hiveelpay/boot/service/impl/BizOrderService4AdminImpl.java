package com.hiveelpay.boot.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.hiveelpay.boot.ctrl.exceptions.HiveelCheckoutException;
import com.hiveelpay.boot.ctrl.exceptions.HiveelPayErrorCode;
import com.hiveelpay.boot.model.CustomerBindCardResult;
import com.hiveelpay.boot.service.BizOrderService4Admin;
import com.hiveelpay.boot.service.NotifySearchEngineComponent;
import com.hiveelpay.boot.service.events.NotificationEvent;
import com.hiveelpay.boot.service.exceptions.HiveelPayServiceErrorCode;
import com.hiveelpay.common.enumm.*;
import com.hiveelpay.common.exceptions.HiveelBizException;
import com.hiveelpay.common.exceptions.HiveelPayException;
import com.hiveelpay.common.model.HiveelPage;
import com.hiveelpay.common.model.requests.BizOrderSearchRequest;
import com.hiveelpay.common.model.vo.PayRequestVo;
import com.hiveelpay.common.util.*;
import com.hiveelpay.dal.dao.mapper.*;
import com.hiveelpay.dal.dao.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.hiveelpay.common.util.DateUtil.FORMAT_MMddyyyyHHmm;
import static java.util.stream.Collectors.groupingBy;

@Service
public class BizOrderService4AdminImpl implements BizOrderService4Admin {
    private static final MyLog _log = MyLog.getLog(BizOrderService4AdminImpl.class);
    @Autowired
    private BizOrderMapper bizOrderMapper;

    @Autowired
    private CustomerAccountMapper customerAccountMapper;

    @Autowired
    private CustomerValidServicesMapper customerValidServicesMapper;

    @Autowired
    private PayProductMapper payProductMapper;

    @Autowired
    private PayOrderMapper payOrderMapper;
    @Autowired
    private PaySubscriptionMapper paySubscriptionMapper;

    @Autowired
    private AppointmentDocMapper appointmentDocMapper;

    @Autowired
    private BizCarMapper bizCarMapper;

    @Autowired
    private ServiceTimeMapper serviceTimeMapper;

    @Autowired
    private NotifySearchEngineComponent notifySearchEngineComponent;

    @Autowired
    private ApplicationEventPublisher publisher;



    private void notifyUserRefunded(BizOrder bizOrder) {
        final String productType = bizOrder.getProductType();
        if (productType.equalsIgnoreCase(PayProductTypeEnum.OIL_CHANGE.name())) {
            publisher.publishEvent(new NotificationEvent(NotificationEvent.ActionPoint.APPOINTMENT_REFUNDED_OIL_CHANGE, NotificationEvent.NoticeChannel.EMAIL, bizOrder.getDocId()));
        } else if (productType.equalsIgnoreCase(PayProductTypeEnum.PRE_INSPECTION.name())) {
            publisher.publishEvent(new NotificationEvent(NotificationEvent.ActionPoint.APPOINTMENT_REFUNDED_PRE_INSPECTION, NotificationEvent.NoticeChannel.EMAIL, bizOrder.getDocId()));
        } else if (productType.equalsIgnoreCase(PayProductTypeEnum.TRADE_IN.name())) {
            publisher.publishEvent(new NotificationEvent(NotificationEvent.ActionPoint.APPOINTMENT_REFUNDED_TRADE_IN, NotificationEvent.NoticeChannel.EMAIL, bizOrder.getDocId()));
        }
    }

    @Override
    public void updatePayFailed(String bizOrderNo) {
        BizOrder bizOrder = bizOrderMapper.selectByBizOrderNo(bizOrderNo);
        if (bizOrder == null || bizOrder.getOrderStatus().getVal() > 3) {
            return;
        }
        fillBizOrderServiceTime(bizOrder);
        int size = bizOrderMapper.updatePayFailed(bizOrderNo, BizOrderStatus.SAVED, BizOrderStatus.PAY_FAILED);
        if (size != 1) {
            return;
        }
        appointmentDocMapper.updateStatus(bizOrder.getDocId(), AppointmentStatus.SAVED, AppointmentStatus.INVALID);
    }

    @Override
    public List<BizOrder> findHistoryOrder(String userId, Set<PayProductTypeEnum> kindSet, HiveelPage hiveelPage) {
        checkNotNull(userId);
        checkNotNull(hiveelPage);

        final String customerId = customerAccountMapper.findCustomerIdbyUserId(userId);
        if (Strings.isNullOrEmpty(customerId)) {
            return Collections.emptyList();
        }

        Set<String> kindNameSet = null;
        if (!kindSet.isEmpty()) {
            kindNameSet = kindSet.stream().filter(i -> i != null && !i.equals(PayProductTypeEnum.ILLEGAL)).map(Enum::name).collect(Collectors.toSet());
        }


        int total = bizOrderMapper.countTotal(customerId, kindNameSet);
        hiveelPage.setTotal(total);

        if (total <= 0) {
            return Collections.emptyList();
        }

        List<BizOrder> list = bizOrderMapper.loadHistoryOrder(customerId, kindNameSet, hiveelPage);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        fillBizOrderServiceTime(list);
        fillMoreInfo(list);
        return list;
    }

    @Override
    public List<BizOrder> findMerchantOrders(String mchId, HiveelPage hiveelPage) {
        checkNotNull(mchId);
        checkNotNull(hiveelPage);

        int total = bizOrderMapper.countMerchantOrders(mchId);
        if (total <= 0) {
            return Collections.emptyList();
        }
        hiveelPage.setTotal(total);

        List<BizOrder> list = bizOrderMapper.loadMerchantOrders(mchId, hiveelPage);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        fillBizOrderServiceTime(list);
        fillMoreInfo(list);
        return list;
    }

    @Override
    public List<BizOrder> searchBizOrders(BizOrderSearchRequest searchRequest, HiveelPage hiveelPage) {
        checkNotNull(searchRequest);
        checkNotNull(hiveelPage);

        Set<BizOrderStatus> statusSet = Sets.newHashSet();
        if (searchRequest.getOrderStatus() == null) {
            statusSet.add(BizOrderStatus.SAVED);
            statusSet.add(BizOrderStatus.EXPIRED);

            statusSet.add(BizOrderStatus.PAY_START);
            statusSet.add(BizOrderStatus.PAY_SUCCESSED);
            statusSet.add(BizOrderStatus.PAY_FAILED);

            statusSet.add(BizOrderStatus.SERVICE_SETUPED);
            statusSet.add(BizOrderStatus.CANCELED);
            statusSet.add(BizOrderStatus.SERVICE_ING);
            statusSet.add(BizOrderStatus.REFUNDED);
            statusSet.add(BizOrderStatus.SERVICE_END);
        } else {
            statusSet.add(searchRequest.getOrderStatus());
        }

        int total = bizOrderMapper.searchBizOrdersCount(searchRequest, statusSet);
        if (total <= 0) {
            return Collections.emptyList();
        }
        hiveelPage.setTotal(total);

        List<BizOrder> list = bizOrderMapper.searchBizOrders(searchRequest, statusSet, hiveelPage);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        fillBizOrderServiceTime(list);
        fillMoreInfo(list);
        return list;
    }

    @Override
    public List<BizOrder> findBizOrders(Set<String> bizOrderSets) {
        List<BizOrder> list = bizOrderMapper.findBizOrders(bizOrderSets);
        fillBizOrderServiceTime(list);
        if (list != null && !list.isEmpty()) {
            fillMoreInfo(list);
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 针对支付成功的订单 但是 初始化服务失败的情况，补漏
     */
    @Override
    public void initBizOrderToService() {
        List<BizOrder> paySuccessedBizOrders = bizOrderMapper.findByBizOrderStatus(BizOrderStatus.PAY_SUCCESSED);
        if (paySuccessedBizOrders == null || paySuccessedBizOrders.isEmpty()) {
            return;
        }

        fillBizOrderServiceTime(paySuccessedBizOrders);
        paySuccessedBizOrders.forEach(i -> {
            List<CustomerValidServices> list = customerValidServicesMapper.findByBizOrderNo(i.getBizOrderNo());
            if (list == null || list.isEmpty()) {
                addServiceAndUpdateBizOrder(i);
            }
        });
    }

    /**
     *
     */
    @Override
    public void checkAndStartServices() {
        _log.info("===========================check and start service========================================");
        List<CustomerValidServices> willStartValidServiceList = customerValidServicesMapper.findWillStartServices(CustomerServiceStatusEnum.INIT);
        if (willStartValidServiceList == null || willStartValidServiceList.isEmpty()) {
            _log.info("There is no services will be start.");
            return;
        }
        willStartValidServiceList.forEach(i -> {
            try {
                startService(i);
            } catch (Exception e) {
                e.printStackTrace();
                _log.error("Start service error!", e);
            }
        });
    }

    /**
     *
     */
    @Override
    public void checkBizOrderServiceDone() {
        List<BizOrder> list = bizOrderMapper.findByBizOrderStatus(BizOrderStatus.SERVICE_ING);
        if (list == null || list.isEmpty()) {
            return;
        }
        fillBizOrderServiceTime(list);
        list.forEach(i -> {
            try {
                if (isServiceDone(i)) {
                    endBizOrder(i);
                }
            } catch (Exception e) {
                _log.error("", e);
            }
        });
    }

    @Override
    public BizOrder findBizOrder(String bizOrderId) {
        if (Strings.isNullOrEmpty(bizOrderId)) {
            return null;
        }
        return bizOrderMapper.selectByBizOrderNo(bizOrderId);
    }

    @Override
    @Transactional
    public void refundOrder(BizOrder bizOrder) {
        if (bizOrder == null) {
            return;
        }

        bizOrderMapper.updateBizOrdersStatus(Collections.singletonList(bizOrder.getBizOrderNo()), BizOrderStatus.CANCELED, BizOrderStatus.REFUNDED);
        if (isSpecialBusiness(bizOrder.getProductType(), PayProductTypeEnum.TRADE_IN, PayProductTypeEnum.OIL_CHANGE, PayProductTypeEnum.PRE_INSPECTION)) {
            appointmentDocMapper.updateStatus(bizOrder.getDocId(), AppointmentStatus.CANCELED, AppointmentStatus.CANCEL_AND_REFUNDED);
            notifyUserRefunded(bizOrder);
        }
    }

    @Override
    public List<BizOrder> queryValidBizOrders(List<String> serviceTypes, String zipcode) {
        if (serviceTypes == null || serviceTypes.isEmpty()) {
            return Collections.emptyList();
        }
        Set<BizOrderStatus> statusSet = Sets.newHashSet();
        statusSet.add(BizOrderStatus.PAY_SUCCESSED);
        statusSet.add(BizOrderStatus.SERVICE_SETUPED);
        statusSet.add(BizOrderStatus.SERVICE_ING);

        List<BizOrder> list = bizOrderMapper.queryValidBizOrders(serviceTypes, statusSet);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        fillBizOrderServiceTime(list);

        if (!Strings.isNullOrEmpty(zipcode)) {
            list = list.stream().filter(i -> i != null && i.getBizCar() != null && i.getBizCar().getZipcode() != null && i.getBizCar().getZipcode().equalsIgnoreCase(zipcode)).collect(Collectors.toList());
        }

        return list;
    }

    @Override
    public Map<String, String> countIncome(Date startDate, Date endDate, PayProductTypeEnum productType) {
        Map<String, String> result = Maps.newHashMap();

        Set<BizOrderStatus> orderStatusSet = Sets.newHashSet();
        orderStatusSet.add(BizOrderStatus.PAY_SUCCESSED);
        orderStatusSet.add(BizOrderStatus.SERVICE_SETUPED);
        orderStatusSet.add(BizOrderStatus.SERVICE_ING);
        orderStatusSet.add(BizOrderStatus.SERVICE_END);

        if (productType.equals(PayProductTypeEnum.ILLEGAL)) {
            for (PayProductTypeEnum pType : PayProductTypeEnum.values()) {
                if (pType.equals(PayProductTypeEnum.ILLEGAL)) {
                    continue;
                }
                countByType(startDate, endDate, result, pType, orderStatusSet);
            }
        } else {
            countByType(startDate, endDate, result, productType, orderStatusSet);
        }
        return result;
    }

    private void countByType(Date startDate, Date endDate, Map<String, String> result, PayProductTypeEnum pType, Set<BizOrderStatus> orderStatusSet) {
        Long total = bizOrderMapper.queryBizOrders(startDate, endDate, pType.name(), orderStatusSet);
        if (total == null) {
            result.put(pType.name(), "0.00");
        } else {
            result.put(pType.name(), AmountUtil.convertCent2Dollar(String.valueOf(total)));
        }
    }

    @Transactional
    public void endBizOrder(BizOrder bizOrder) {
        bizOrderMapper.updateBizOrdersStatus(Collections.singletonList(bizOrder.getBizOrderNo()), BizOrderStatus.SERVICE_ING, BizOrderStatus.SERVICE_END);
        if (isSpecialBusiness(bizOrder.getProductType(), PayProductTypeEnum.TRADE_IN, PayProductTypeEnum.OIL_CHANGE, PayProductTypeEnum.PRE_INSPECTION)) {
            appointmentDocMapper.updateStatus(bizOrder.getDocId(), AppointmentStatus.VALID, AppointmentStatus.SERVICE_DONE);
        }
    }

    private boolean isSpecialBusiness(String productType, PayProductTypeEnum... payProductTypeEnums) {
        for (PayProductTypeEnum payProductTypeEnum : payProductTypeEnums) {
            if (productType.equalsIgnoreCase(payProductTypeEnum.name())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 开启服务
     *
     * @param customerValidServices
     */
    @Transactional
    public void startService(CustomerValidServices customerValidServices) {
        if (customerValidServices == null) {
            return;
        }
        BizOrder bizOrder = bizOrderMapper.selectByBizOrderNo(customerValidServices.getBizOrderNo());
        if (bizOrder == null) {
            _log.warn("Check and start service, can't find bizOrder:{},customerValidServiceId:{}", customerValidServices.getBizOrderNo(), customerValidServices.getServiceId());
            return;
        }
        fillBizOrderServiceTime(bizOrder);

        PayProduct payProduct = payProductMapper.findByProductId(customerValidServices.getProductId());

        /**
         * step 1 update valid service status
         * step 2 sync to search-engine
         */

        customerValidServicesMapper.updateStatusByServiceId(customerValidServices.getServiceId(), CustomerServiceStatusEnum.INIT, CustomerServiceStatusEnum.IN_SERVICE);
        bizOrderMapper.updateBizOrdersStatus(Lists.newArrayList(customerValidServices.getBizOrderNo()), bizOrder.getOrderStatus(), BizOrderStatus.SERVICE_ING);


        if (payProduct.getProductType().equals(PayProductTypeEnum.PRE_INSPECTION) ||
                payProduct.getProductType().equals(PayProductTypeEnum.OIL_CHANGE) ||
                payProduct.getProductType().equals(PayProductTypeEnum.TRADE_IN)
        ) {
            appointmentDocMapper.updateStatus(bizOrder.getDocId(), AppointmentStatus.SAVED, AppointmentStatus.VALID);
        }

        notifySearchEngine(customerValidServices, bizOrder, payProduct);
    }

    private boolean notifySearchEngine(CustomerValidServices customerValidServices, BizOrder bizOrder, PayProduct payProduct) {
        if (payProduct == null) {
            payProduct = payProductMapper.findByProductId(customerValidServices.getProductId());
        }
        _log.info("Notify search-engine, service start, service:{}", customerValidServices);
        final String docId = bizOrder.getDocId();
        boolean rs = false;
        switch (payProduct.getProductType()) {
            case ADVANCING: {
                rs = notifySearchEngineComponent.notifySearchEngine(docId);
                increaceSyncCount(bizOrder, rs);
                break;
            }
            case SEARCH_RESULT: {
                rs = notifySearchEngineComponent.notifySearchEngine(docId);
                increaceSyncCount(bizOrder, rs);
                break;
            }
            case CAR_OF_DAY: {
                rs = notifySearchEngineComponent.notifySearchEngine(docId);
                increaceSyncCount(bizOrder, rs);
                break;
            }
            case HIGHLIGHTING: {
                rs = notifySearchEngineComponent.notifySearchEngine(docId);
                increaceSyncCount(bizOrder, rs);
                break;
            }
            default:
                break;
        }
        return rs;
    }

    private void increaceSyncCount(BizOrder bizOrder, boolean rs) {
        bizCarMapper.increaceNotifyCount(bizOrder.getBizOrderNo(), bizOrder.getDocId(), rs ? SyncStatus.SYNC_SUCCESSED : SyncStatus.SYNC_FAILED);
    }

    private void fillMoreInfo(List<BizOrder> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        list.forEach(i -> {
            final String bizOrderNo = i.getBizOrderNo();
            List<PayOrder> payOrders = payOrderMapper.findPayOrdersByBizOrderNo(bizOrderNo);
            if (payOrders != null && !payOrders.isEmpty()) {
                i.setPayOrders(payOrders);
            }

            PaySubscription paySubscription = paySubscriptionMapper.findByBizOrderNo(bizOrderNo);
            if (paySubscription != null) {
                i.setPaySubscription(paySubscription);
            }
        });
    }

    @Override
    public void checkAndEndService() {
        _log.info("===============check and end service=======================");
        List<CustomerValidServices> willEndServiceList = customerValidServicesMapper.findWillEndServices(CustomerServiceStatusEnum.IN_SERVICE);
        if (willEndServiceList == null || willEndServiceList.isEmpty()) {
            _log.info("There is no services will be end.");
            return;
        }
        willEndServiceList.forEach(i -> {
            try {
                endService(i);
            } catch (Exception e) {
                _log.error("End service error!", e);
            }
        });
    }

    @Transactional
    public void endService(CustomerValidServices customerValidServices) {
        if (customerValidServices == null) {
            return;
        }
        BizOrder bizOrder = bizOrderMapper.selectByBizOrderNo(customerValidServices.getBizOrderNo());
        if (bizOrder == null) {
            _log.warn("Check and end service, can't find bizOrder:{},customerValidServiceId:{}", customerValidServices.getBizOrderNo(), customerValidServices.getServiceId());
            return;
        }
        fillBizOrderServiceTime(bizOrder);

        PayProduct payProduct = payProductMapper.findByProductId(customerValidServices.getProductId());
        /**
         * Do not need notify search-engine
         */
        int i = customerValidServicesMapper.updateStatusByServiceId(customerValidServices.getServiceId(), CustomerServiceStatusEnum.IN_SERVICE, CustomerServiceStatusEnum.EXPIRED);
        bizOrderMapper.updateBizOrdersStatus(Lists.newArrayList(customerValidServices.getBizOrderNo()), bizOrder.getOrderStatus(), BizOrderStatus.SERVICE_END);
//        if (!Strings.isNullOrEmpty(bizOrder.getInvalidBizOrderNo())) {
//            bizOrderMapper.updateEmptyInvalidBizOrderNo(bizOrder.getBizOrderNo());
//        }
        notifySearchEngine(customerValidServices, bizOrder, payProduct);
    }


    private boolean isServiceDone(BizOrder bizOrder) {
        if (bizOrder.getServiceTimes() == null || bizOrder.getServiceTimes().isEmpty()) {
            throw new HiveelPayException(HiveelPayErrorCode.ILLEGAL_PARAMETERS, "There is no service time for bizOrder:" + bizOrder.getBizOrderNo());
        }
        List<ServiceTime> list = bizOrder.getServiceTimes().stream().filter(i -> i.getServiceEndTime().after(new Date())).collect(Collectors.toList());
        return list.isEmpty();
    }

    @Transactional
    public void addServiceAndUpdateBizOrder(BizOrder bizOrder) {
        _log.info("BizOrder pay success, generate valid customer service!", new Gson().toJson(bizOrder.getServiceTimes()));
        try {
            PayProduct payProduct = payProductMapper.findByProductId(bizOrder.getProductId());
            if (payProduct == null) {
                throw new HiveelPayException(HiveelPayErrorCode.INCORRECT_PAY_PRODUCT);
            }
            List<CustomerValidServices> list = Lists.newArrayList();
            bizOrder.getServiceTimes().forEach(i -> {
                CustomerValidServices cvs = new CustomerValidServices();
                cvs.setCustomerId(bizOrder.getCustomerId());
                cvs.setServiceId(HiveelID.getInstance().getRandomId("S"));
                cvs.setProductId(bizOrder.getProductId());
                cvs.setBizOrderNo(bizOrder.getBizOrderNo());
                cvs.setServiceName(payProduct.getProductName());
                cvs.setServiceType(payProduct.getProductType());
                cvs.setServiceStatus(CustomerServiceStatusEnum.INIT);
                cvs.setStartTime(i.getServiceStartTime());
                cvs.setEndTime(i.getServiceEndTime());
                _log.info("=================Customer has new service:{}", cvs);
                list.add(cvs);
            });

            int saved = customerValidServicesMapper.saveMore(list);
            int updated = bizOrderMapper.checkInserviceOrders(Lists.newArrayList(bizOrder.getBizOrderNo()), BizOrderStatus.PAY_SUCCESSED, BizOrderStatus.SERVICE_SETUPED);
            if (saved < 1 || updated < 1) {
                throw new HiveelPayException(HiveelPayErrorCode.CUSTOMER_SERVICE_ADD_ERROR);
            }
            _log.info("Service generated!,bizOrderNo:{},generated services:{}", bizOrder.getBizOrderNo(), saved);
        } catch (Exception e) {
            _log.error("Customer service add error!", e);
            throw new HiveelPayException(HiveelPayErrorCode.CUSTOMER_SERVICE_ADD_ERROR, e);
        }
    }

    @Override
    public BizOrder findCustomerValidMembershipOrder(String customerId) {
        try {
            BizOrder bizOrder = bizOrderMapper.findCustomerValidMembershipOrder(customerId, PayProductTypeEnum.MEMBERSHIP.name());
            fillBizOrderServiceTime(bizOrder);
            return bizOrder;
        } catch (Exception e) {
            _log.error("", e);
            throw new HiveelPayException(HiveelPayServiceErrorCode.MULTI_VALID_BIZ_ORDER_ERROR);
        }
    }

    @Override
    public BizOrder findByBizOrderNo(String customerId, String bizOrderId) {
        BizOrder bizOrder = bizOrderMapper.findCustomerBizOrder(customerId, bizOrderId);
        fillBizOrderServiceTime(bizOrder);
        return bizOrder;
    }


    @Override
    public List<BizOrder> findHasInvalidBizOrderNos() {
        List<BizOrder> list = bizOrderMapper.findHasInvalidBizOrderNos();
        fillBizOrderServiceTime(list);
        return list;
    }

    @Override
    public List<BizOrder> findServiceNotStartMembershipBizOrders(String customerId) {
        checkArgument(!Strings.isNullOrEmpty(customerId), "Must give me a customerId");

        Set<CustomerServiceStatusEnum> statusEnums = Sets.newHashSet();
        statusEnums.add(CustomerServiceStatusEnum.INIT);
//        statusEnums.add(CustomerServiceStatusEnum.IN_SERVICE);

        List<CustomerValidServices> customerValidServicesList = customerValidServicesMapper.findByCustomerId(customerId, PayProductTypeEnum.MEMBERSHIP, statusEnums);
        if (customerValidServicesList != null && !customerValidServicesList.isEmpty()) {
            List<String> bizOrderNoList = customerValidServicesList.stream().filter(Objects::nonNull).map(CustomerValidServices::getBizOrderNo).collect(Collectors.toList());
            if (bizOrderNoList == null || bizOrderNoList.isEmpty()) {
                return Collections.emptyList();
            }
            return bizOrderMapper.selectByBizOrderNoList(bizOrderNoList);
        }
        return Collections.emptyList();
    }

    private void fillBizOrderServiceTime(BizOrder bizOrder) {
        if (bizOrder == null) {
            return;
        }
        List<ServiceTime> list = serviceTimeMapper.queryByBizOrderNo(bizOrder.getBizOrderNo());
        if (list == null || list.isEmpty()) {
            return;
        }
        bizOrder.setServiceTimes(list);
    }

    private void fillBizOrderServiceTime(List<BizOrder> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Set<String> bizOrderNoSet = list.stream().map(BizOrder::getBizOrderNo).collect(Collectors.toSet());
        List<ServiceTime> serviceTimeList = serviceTimeMapper.queryByBizOrderNos(bizOrderNoSet);
        if (serviceTimeList == null || serviceTimeList.isEmpty()) {
            return;
        }
        List<BizCar> bizCarList = bizCarMapper.findByBizOrderIds(bizOrderNoSet);
        if (bizCarList == null) {
            bizCarList = Collections.emptyList();
        }
        Map<String, List<BizCar>> m = bizCarList.stream().filter(Objects::nonNull).collect(groupingBy(BizCar::getBizOrderNo));

        Map<String, List<ServiceTime>> map = serviceTimeList.stream().collect(groupingBy(ServiceTime::getBizOrderNo));
        list.forEach(i -> {
            final String bizOrderNo = i.getBizOrderNo();
            i.setServiceTimes(map.get(bizOrderNo));
            List<BizCar> l = m.get(bizOrderNo);
            if (l != null && l.size() > 0) {
                i.setBizCar(l.get(0));
            }
        });
    }


}
