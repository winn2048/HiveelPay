package com.hiveelpay.boot.service.events;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Customer;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.Result;
import com.google.common.base.Strings;
import com.hiveelpay.boot.service.SystemHasExceptionEvent;
import com.hiveelpay.common.enumm.*;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.common.util.HiveelID;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.common.util.MySeq;
import com.hiveelpay.dal.dao.mapper.*;
import com.hiveelpay.dal.dao.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TradeInSkipPaymentEventListener {
    private static final MyLog _log = MyLog.getLog(TradeInSkipPaymentEventListener.class);

    @Autowired
    private BizOrderMapper bizOrderMapper;
    @Autowired
    private AppointmentDocMapper appointmentDocMapper;
    @Autowired
    private CustomerValidServicesMapper customerValidServicesMapper;
    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private ServiceTimeMapper serviceTimeMapper;
    @Autowired
    private BraintreeGateway gateway;
    @Autowired
    private CustomerAccountMapper customerAccountMapper;
    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * Trade-in 补单
     *
     * @param tradeInSkipPaymentEvent
     */
    @Async
    @EventListener
    public void fillMockOrders(TradeInSkipPaymentEvent tradeInSkipPaymentEvent) {
        _log.info("TradInSkipPayment:{}", tradeInSkipPaymentEvent);

        try {
            String appointmentId = generateMockOrders(tradeInSkipPaymentEvent);
            if (!Strings.isNullOrEmpty(appointmentId)) {
                publisher.publishEvent(new NotificationEvent(NotificationEvent.ActionPoint.APPOINTMENT_BOOKED_TRADE_IN, NotificationEvent.NoticeChannel.EMAIL_AND_SMS, appointmentId));
            }
        } catch (Exception e) {
            _log.error("", e);
            publisher.publishEvent(new SystemHasExceptionEvent("#HIVEELPAY", e, "Trade-in 补单逻辑发生异常！"));
        }
    }

    public String generateMockOrders(TradeInSkipPaymentEvent tradeInSkipPaymentEvent) {
        AppointmentDoc appointmentDoc = appointmentDocMapper.findByAppointmentId(tradeInSkipPaymentEvent.getAppointmentDoc().getAppointmentId());
        if (appointmentDoc == null) {
            return null;
        }
        CustomerAccount customerAccount = createCustomer(appointmentDoc);
        BizOrder bizOrder = buildBizOrder(appointmentDoc, customerAccount);
        bizOrderMapper.save(bizOrder);

        ServiceTime st = new ServiceTime();
        st.setBizOrderNo(bizOrder.getBizOrderNo());
        st.setServiceStartTime(appointmentDoc.getAppointmentTime());
        st.setServiceEndTime(DateUtil.addDays(appointmentDoc.getAppointmentTime(), 1));
        serviceTimeMapper.save(st);

        String appointmentId = appointmentDoc.getAppointmentId();

        appointmentDocMapper.updateAndAssociateBizOrderNo(appointmentId, bizOrder.getBizOrderNo(), appointmentDoc.getAppointmentStatus());
        appointmentDocMapper.updateStatus(appointmentId, appointmentDoc.getAppointmentStatus(), AppointmentStatus.VALID);

        CustomerValidServices customerValidServices = buildCustomerValidService(appointmentDoc, bizOrder);
        customerValidServicesMapper.save(customerValidServices);

        PayOrder payOrder = buildPayOrder(bizOrder);
        payOrderMapper.insert(payOrder);

        return appointmentId;
    }

    private PayOrder buildPayOrder(BizOrder bizOrder) {
        PayOrder po = new PayOrder();
        po.setPayOrderId(MySeq.getPay());
        po.setMchId(bizOrder.getMchId());
        po.setMchOrderNo(bizOrder.getBizOrderNo());
        po.setChannelOrderNo("H_ONCE");
        po.setAmount(0L);
        po.setCurrency("USD");
        po.setStatus(Byte.valueOf("2"));
        po.setClientIp("127.0.0.1");
        po.setDevice("MOCK");
        po.setSubject("Instant Sell");
        po.setBody(String.format("TRADE_IN,%s,%s", DateUtil.date2Str(new Date(), DateUtil.FORMAT_YYYYMMDDHHMMSS), DateUtil.date2Str(DateUtil.addDays(new Date(), 1), DateUtil.FORMAT_YYYYMMDDHHMMSS)));
        po.setExtra("{\"payProductId\":\"Pf2f81a39eda74785b81a39eda7c785f3\",\"paymentMethodId\":\"\",\"btPlanId\":\"\",\"addressTargetId\":\"\",\"nonce\":\"\",\"invalidBizOrderNo\":\"\",\"cTargetId\":\"\"}");
        po.setChannelId("H_ONCE");
        po.setChannelMchId("H_ONCE"+bizOrder.getMchId());
        po.setChannelOrderNo(null);
        po.setParam1(bizOrder.getCustomerId());
        po.setParam2(bizOrder.getBizOrderNo());
        po.setCreateTime(new Date());
        po.setNotifyCount(Byte.valueOf("0"));
        po.setNotifyUrl("http://");
        po.setPaySuccTime(new Date().getTime());
        return po;
    }

    private CustomerValidServices buildCustomerValidService(AppointmentDoc appointmentDoc, BizOrder bizOrder) {
        CustomerValidServices cvs = new CustomerValidServices();
        cvs.setCustomerId(bizOrder.getCustomerId());
        cvs.setServiceId(HiveelID.getInstance().getRandomId("S"));
        cvs.setProductId("Pf2f81a39eda74785b81a39eda7c785f3");
        cvs.setBizOrderNo(bizOrder.getBizOrderNo());
        cvs.setServiceName("Instant Sell");
        cvs.setServiceType(PayProductTypeEnum.TRADE_IN);
        cvs.setServiceStatus(CustomerServiceStatusEnum.INIT);
        cvs.setStartTime(appointmentDoc.getAppointmentTime());
        cvs.setEndTime(DateUtil.addDays(appointmentDoc.getAppointmentTime(), 1));
        return cvs;
    }

    private BizOrder buildBizOrder(AppointmentDoc appointmentDoc, CustomerAccount customerAccount) {
        BizOrder bo = new BizOrder();
        final String appointmentId = appointmentDoc.getAppointmentId();
        bo.setCustomerId(customerAccount.getCustomerId());
        bo.setBizOrderNo(MySeq.getBizOrderId());
        bo.setProductId("Pf2f81a39eda74785b81a39eda7c785f3");
        bo.setProductName("Instant Sell");
        bo.setDocId(appointmentId);
        bo.setAmount(0L);
        bo.setCommissionAmount(0L);
        bo.setProductType(PayProductTypeEnum.TRADE_IN.name());
        bo.setServiceLength(1);
        bo.setServiceLengthUnit(ServiceLengthUnitEnum.DAY);
        bo.setOrderStatus(BizOrderStatus.PAY_SUCCESSED);
        bo.setFirstBillDate(new Date());
        bo.setPayAmount(0L);
        bo.setCurrency(CurrencyEnum.USD);
        bo.setMchId(appointmentDoc.getToMchId());
        bo.setChannelId("H_ONCE");
        bo.setPaySuccessTime(new Date());
        bo.setRemark("MockBizOrder");
        bo.setInvalidBizOrderNo(null);
        return bo;
    }

    private CustomerAccount createCustomer(AppointmentDoc appointmentDoc) {
        CustomerAccount customerAccount = customerAccountMapper.findByUserId(appointmentDoc.getCustomerId());
        if (customerAccount != null) {
            return customerAccount;
        }

        customerAccount = new CustomerAccount();
        customerAccount.setUserId(appointmentDoc.getCustomerId());

        final String customerId = MySeq.getCustomerId();
        customerAccount.setCustomerId(customerId);//new Customer

        customerAccount.setFirstName(appointmentDoc.getFirstName().trim());
        customerAccount.setLastName(appointmentDoc.getLastName().trim());
        customerAccount.setAccountType(AccountType.CUSTOMER);

        CustomerRequest request = new CustomerRequest()
                .firstName(customerAccount.getFirstName())
                .lastName(customerAccount.getLastName())
                .customerId(customerAccount.getCustomerId())
                .deviceData("WEB");

        try {
            Result<Customer> result = gateway.customer().create(request);
            if (result.isSuccess()) {//success
                String targetId = result.getTarget().getId();

                customerAccount.setTargetId(targetId);
                customerAccount.setSyncStatus(SyncStatus.SYNC_SUCCESSED);
                customerAccountMapper.save(customerAccount);
            }
        } catch (Exception e) {
            _log.error("调用Braintree创建账户失败！", e);
            publisher.publishEvent(new SystemHasExceptionEvent("#HIVEELPAY", e, "补单逻辑中调用Braintree创建账户失败！"));
        }
        return customerAccount;
    }
}
