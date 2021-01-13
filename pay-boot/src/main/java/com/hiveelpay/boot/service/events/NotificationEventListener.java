package com.hiveelpay.boot.service.events;

import com.google.common.base.Strings;
import com.hiveel.core.util.ShortMessageUtil;
import com.hiveelpay.boot.service.channel.hiveel.AdminConfig;
import com.hiveelpay.boot.service.channel.hiveel.EmailConfig;
import com.hiveelpay.boot.service.impl.EmailComponent;
import com.hiveelpay.common.util.AmountUtil;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.mapper.AppointmentDocMapper;
import com.hiveelpay.dal.dao.mapper.BizOrderMapper;
import com.hiveelpay.dal.dao.mapper.MchStoreInfoMapper;
import com.hiveelpay.dal.dao.model.AppointmentDoc;
import com.hiveelpay.dal.dao.model.BizOrder;
import com.hiveelpay.dal.dao.model.MchStoreInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;

/**
 * 消息通知事件
 * <p>
 * EMAIL_APPOINTMENT_IS_AT_TOMORROW_CAR_INSPECTION(26, "预约时间到达(oil_change)", "appoint_is_at_tomorrow_car_inspection.html", "Your Appointment is at Tomorrow", false),
 * EMAIL_APPOINTMENT_IS_AT_TOMORROW_OIL_CHANGE(27, "预约时Ø间到达(oil_change)", "appoint_is_at_tomorrow_oil_change.html", "Your Appointment is at Tomorrow", false),
 * EMAIL_APPOINTMENT_IS_AT_TOMORROW_TRADE_IN(28, "预约时间到达(trade_in)", "appoint_is_at_tomorrow_trade_in.html", "Your Appointment is at Tomorrow", false),
 * <p>
 * EMAIL_APPOINTMENT_IS_BOOKED_INSTANT_SELL(29, "预约成功", "appoint_is_booked_instant_sell.html", "Your Appointment Is Booked", false),
 * EMAIL_APPOINTMENT_IS_BOOKED_OIL_CHANGE(30, "预约成功", "appoint_is_booked_oil_change.html", "Your Appointment Is Booked", false),
 * EMAIL_APPOINTMENT_IS_BOOKED_CAR_INSPECTION(31, "预约成功", "appoint_is_booked_used_car_inspection.html", "Your Appointment Is Booked", false),
 * <p>
 * EMAIL_APPOINTMENT_IS_CANCELED_CAR_INSPECTION(32, "预约取消", "appoint_is_canceled_car_inspection.html", "Your Appointment Is Canceled", false),
 * EMAIL_APPOINTMENT_IS_CANCELED_OIL_CHANGE(33, "预约取消", "appoint_is_canceled_oil_change.html", "Your Appointment Is Canceled", false),
 * EMAIL_APPOINTMENT_IS_CANCELED_USED_CAR(34, "预约取消", "appoint_is_canceled_used_car.html", "Your Appointment Is Canceled", false),
 * <p>
 * EMAIL_THANK_YOU_CHOOSE_HIVEEL(35, "感谢选择hiveel", "thank_you_choose_hiveel.html", "Thank You for Choosing Hiveel", false),
 * <p>
 * EMAIL_YOUR_REFUND_HAS_BEEN_SENT(36, "已退款", "your_refund_has_been_sent.html", "Your Refund Has Been Sent", false),
 */
@Component
public class NotificationEventListener {
    private static final MyLog _log = MyLog.getLog(NotificationEventListener.class);
    public static final String PRE_INSPECTION = "pre-inspection";
    public static final String TRADE_IN = "trade-in";
    public static final String OIL_CHANGE = "oil-change";

    public static final String ADDRESS_001 = "1050 Lakes Dr. Suite 206, West Covina, CA 91790";
    public static final String ADDRESS_002 = "11400 W Olympic Blvd. Suite 650, Los Angeles, CA 90064";

    public static final String KEY_APPOINTMENT_ID = "_appointmentId_";
    public static final String KEY_BIZ_TYPE = "_bizType_";

    @Autowired
    private EmailComponent emailComponent;

    @Autowired
    private AppointmentDocMapper appointmentDocMapper;
    @Autowired
    private BizOrderMapper bizOrderMapper;
    @Autowired
    private EmailConfig emailConfig;

    @Autowired
    private AdminConfig adminConfig;

    @Autowired
    private MchStoreInfoMapper mchStoreInfoMapper;

    /**
     * 快速提交的trade-in预约申请，通知管理员审核
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isThisTradeInNeedAdminCheck()")
    public void tradeinNeedAdminCheck(NotificationEvent notificationEvent) {
        _log.info("SMS-NOTIFICATION-admin need to check,appointment:{}", notificationEvent.getDocId());
        AppointmentDoc appointmentDoc = getAppointmentDoc(notificationEvent);
        if (appointmentDoc == null) return;

        if (Strings.isNullOrEmpty(adminConfig.getContacts())) {
            _log.warn("Please set the propertity key: config.admin.contacts");
            return;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("Appt ID: ").append(String.valueOf(appointmentDoc.getAppointmentId()));
        sb.append(",   Time: ").append(DateUtil.date2Str(appointmentDoc.getAppointmentTime(), "MM/dd/YYYY hh:mm a"));
        sb.append(",   First name: ").append(Strings.nullToEmpty(appointmentDoc.getFirstName()));
        sb.append(",   Last name: ").append(Strings.nullToEmpty(appointmentDoc.getLastName()));
        sb.append(",   Phone: ").append(Strings.nullToEmpty(appointmentDoc.getPhoneNumber()));
        sb.append(",   Email: ").append(Strings.nullToEmpty(appointmentDoc.getEmail()));
        sb.append(",   Car Info: Year: ").append(appointmentDoc.getYear() == null ? "" : appointmentDoc.getYear());
        sb.append(",   Make: ").append(Strings.nullToEmpty(appointmentDoc.getMake()));
        sb.append(",   Model: ").append(Strings.nullToEmpty(appointmentDoc.getModel()));
        sb.append(",   Mileage: ").append(appointmentDoc.getMileage() == null ? "" : appointmentDoc.getMileage());

        Arrays.stream(adminConfig.getContacts().split(",")).filter(k -> k != null && k.trim().length() > 0).forEach(i -> {
            String[] contact = i.split(":");

            String name = contact[0];
            String phoneNumber = contact[1];
            sendSMSMessage(String.format("Hi %s, we have a new trade-in appointment need to check. %s", name, sb.toString()), phoneNumber);
        });

    }


    /**
     * booked (oil-change)
     * 30
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isBookedOilChange()")
    public void listenBookedOilChange(NotificationEvent notificationEvent) {
        _log.info("EMAIL-NOTIFICATION-OIL_CHANGE-booked,appointment:{}", notificationEvent.getDocId());
        MultiValueMap<String, String> map = getStringStringMultiValueMap(notificationEvent);
        if (map == null) return;
        map.set("id", "30");
        map.set("HIVEEL_VIEW_URL", emailConfig.getAppointmentViewUrl().replace(KEY_BIZ_TYPE, OIL_CHANGE).replace(KEY_APPOINTMENT_ID, notificationEvent.getDocId()));
        emailComponent.sendEmail(map);

        map.set("id", "38");// email to merchant
        map.set("toMemberId", "5592");
        map.set("toArr", "info@on-siteautomotiveservices.com");
        emailComponent.sendEmail(map);

        sendSMS(notificationEvent);
    }

    private MultiValueMap<String, String> getStringStringMultiValueMap(NotificationEvent notificationEvent) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        AppointmentDoc appointmentDoc = getAppointmentDoc(notificationEvent);
        if (appointmentDoc == null) return map;

        map.add("toArr", appointmentDoc.getEmail());
        map.add("toMemberId", Strings.nullToEmpty(appointmentDoc.getCustomerId()));
        map.add("HIVEEL_APPOINTMENT_ID", Strings.nullToEmpty(appointmentDoc.getAppointmentId()));
        map.add("HIVEEL_APPOINTMENT_TIME", DateUtil.date2Str(appointmentDoc.getAppointmentTime(), DateUtil.FORMAT_4_EMAIL));
        map.add("HIVEEL_APPOINTMENT_TIME_TITLE", DateUtil.date2Str(appointmentDoc.getAppointmentTime(), "hh:mm a"));
        map.add("HIVEEL_FIRST_NAME", Strings.nullToEmpty(appointmentDoc.getFirstName()));
        map.add("HIVEEL_LAST_NAME", Strings.nullToEmpty(appointmentDoc.getLastName()));
        map.add("HIVEEL_PHONE", Strings.nullToEmpty(appointmentDoc.getPhoneNumber()));
        map.add("HIVEEL_EMAIL", appointmentDoc.getEmail());
        map.add("HIVEEL_VIN", appointmentDoc.getVin());
        map.add("HIVEEL_YEAR", Strings.nullToEmpty(String.valueOf(appointmentDoc.getYear() == null ? 0 : appointmentDoc.getYear())));
        map.add("HIVEEL_MAKE", Strings.nullToEmpty(appointmentDoc.getMake()));
        map.add("HIVEEL_MODEL", Strings.nullToEmpty(appointmentDoc.getModel()));
        map.add("HIVEEL_ADDRESS", toAddress(appointmentDoc));
        map.add("HIVEEL_ITEM", appointmentDoc.getBusinessType().name());

        BizOrder bizOrder = bizOrderMapper.selectByBizOrderNo(appointmentDoc.getBizOrderNo());
        if (bizOrder != null) {
            if (bizOrder.getAmount() != null) {
                map.add("HIVEEL_PRICE", AmountUtil.convertCent2Dollar(String.valueOf(bizOrder.getAmount())));
            }
            map.add("HIVEEL_TOTAL_PRICE", AmountUtil.convertCent2Dollar(String.valueOf(bizOrder.getPayAmount())));
        }
        String storeAddress = "";
        if (appointmentDoc.getToStoreId().equalsIgnoreCase("store001")) {
            storeAddress = ADDRESS_001;
        } else if (appointmentDoc.getToStoreId().equalsIgnoreCase("store002")) {
            storeAddress = ADDRESS_002;
        }

        map.add("HIVEEL_STORE_ADDRESS", storeAddress);
        map.add("HIVEEL_MILEAGE", appointmentDoc.getMileage() == null ? "0" : String.valueOf(appointmentDoc.getMileage()));
        map.add("HIVEEL_TITLE_STATUS", Strings.nullToEmpty(appointmentDoc.getTitleStatus()));
        map.add("HIVEEL_PREVIOUS_OFFER", Strings.nullToEmpty(appointmentDoc.getPreviousOfferPrice()));
        map.add("HIVEEL_NOTE", Strings.nullToEmpty(appointmentDoc.getRemark()));
        map.add("HIVEEL_COMPANY_NAME", Strings.nullToEmpty(appointmentDoc.getCompanyName()));
        map.add("HIVEEL_WHERE_CAR_AND_KEY", Strings.nullToEmpty(appointmentDoc.getWhereCarAndKey()));

        return map;
    }

    private String toAddress(AppointmentDoc appointmentDoc) {

        StringBuffer sb = new StringBuffer();
        sb.append(Strings.nullToEmpty(appointmentDoc.getStreet()));
        if (!Strings.isNullOrEmpty(appointmentDoc.getApt())) {
            sb.append("#");
            sb.append(appointmentDoc.getApt());
        }
        sb.append(",");
        sb.append(Strings.nullToEmpty(appointmentDoc.getCity()));
        sb.append(",");
        sb.append(Strings.nullToEmpty(appointmentDoc.getState()));
        sb.append(",");
        sb.append(Strings.nullToEmpty(appointmentDoc.getZipcode()));
        return sb.toString();
    }

    /**
     * booked (pre-inspection)
     * 31
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isBookedPreInspection()")
    public void listenBookedPreInspection(NotificationEvent notificationEvent) {
        _log.info("EMAIL-NOTIFICATION-PRE-INSPECTION-booked,appointment:{}", notificationEvent.getDocId());
        MultiValueMap<String, String> map = getStringStringMultiValueMap(notificationEvent);
        if (map == null) return;
        map.set("id", "31");
        map.set("HIVEEL_VIEW_URL", emailConfig.getAppointmentViewUrl().replace(KEY_BIZ_TYPE, PRE_INSPECTION).replace(KEY_APPOINTMENT_ID, notificationEvent.getDocId()));
        emailComponent.sendEmail(map);

        map.set("id", "37");// email to merchant
        sendEmailToMerchant(map, getAppointmentDoc(notificationEvent));

        sendSMS(notificationEvent);
    }

    /**
     * booked (trade-in)
     * 29
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isBookedTradeIn()")
    public void listenBookedTradeIn(NotificationEvent notificationEvent) {
        _log.info("EMAIL-NOTIFICATION-TRADE-IN-booked,appointment:{}", notificationEvent.getDocId());
        MultiValueMap<String, String> map = getStringStringMultiValueMap(notificationEvent);
        if (map == null) return;
        AppointmentDoc appointmentDoc = getAppointmentDoc(notificationEvent);
        if (appointmentDoc == null) {
            return;
        }
        // 邮件提醒dealder of trade-in to do quote.
        if (Strings.isNullOrEmpty(appointmentDoc.getCarMaxImgUrl())) {
            map.set("id", "29");
            map.set("HIVEEL_VIEW_URL", emailConfig.getAppointmentViewUrl().replace(KEY_BIZ_TYPE, TRADE_IN).replace(KEY_APPOINTMENT_ID, notificationEvent.getDocId()));
            emailComponent.sendEmail(map);

            map.set("id", "39");// email to merchant
            sendEmailToMerchant(map, appointmentDoc);
            sendSMS(notificationEvent);
        } else {
            map.set("id", "46");
            sendEmailToMerchant(map, appointmentDoc);
            /**
             * trade-in预定成功给dealer发送短信提醒
             */
            String content = " Hi, you have received a new offer request from a customer, please login to your Hiveel account and send offer back to the customer ASAP. Thank you for your cooperation.";

            MchStoreInfo smi = getMchInfo(appointmentDoc);
            if (smi == null || Strings.isNullOrEmpty(smi.getStorePhone())) {
                return;
            }
            Arrays.stream(smi.getStorePhone().split(",")).filter(i -> !Strings.isNullOrEmpty(i)).forEach(i -> sendSMSMessage(content, i));
        }

        // 短信提醒用户
        String content = "You have successfully upload your previous CarMax appraisal offer, we will get back to you within 24 hours.";// Carmax
        if (Strings.isNullOrEmpty(appointmentDoc.getCarMaxImgUrl())) {
            content = String.format("You have successfully booked your FREE APPRAISAL with Hiveel at %s, %s. We look forward to seeing you.",
                    DateUtil.date2Str(appointmentDoc.getAppointmentTime(), "MM/dd/YYYY hh:mm a"),
                    getDealerAddress(appointmentDoc));

        }

        final String phoneNumber = appointmentDoc.getPhoneNumber();
        sendSMSMessage(content, phoneNumber);
    }

    private String getDealerAddress(AppointmentDoc appointmentDoc) {
        String addressStr = "";
        if (appointmentDoc.getToMchId().equalsIgnoreCase("10000002")) {
            switch (appointmentDoc.getToStoreId()) {
                case "store002": {//trade_in-LA
                    addressStr = "11400 W Olympic Blvd STE 650, Los Angeles";
                    break;
                }
                case "store001": {// trade_in-west-covina
                    addressStr = "1050 Lakes Dr STE 206, West Covina";
                    break;
                }
                default:
                    break;
            }
        }
        return addressStr;
    }

    private void sendEmailToMerchant(MultiValueMap<String, String> map, AppointmentDoc appointmentDoc) {
        MchStoreInfo mchStoreInfo = getMchInfo(appointmentDoc);
        if (mchStoreInfo == null || Strings.isNullOrEmpty(mchStoreInfo.getEmails())) {
            _log.warn("Can't send email to merchant, please set emails in db_table of  pay.t_mch_store_info. mchId={},storeId={}", appointmentDoc.getToMchId(), appointmentDoc.getToStoreId());
            return;
        }
        map.remove("toMemberId");
        map.remove("toArr");
        for (String email : mchStoreInfo.getEmails().split(",")) {
            map.add("toArr", email);
        }
        emailComponent.sendEmail(map);
    }

    private MchStoreInfo getMchInfo(AppointmentDoc appointmentDoc) {
        return mchStoreInfoMapper.findByMchIdAndStoreId(appointmentDoc.getToMchId(), appointmentDoc.getToStoreId());
    }

    /**
     * Dealer报价完成,发邮件和短信给 客户
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isTradeInQuoteReady()")
    public void listenTradeInQuoteReady(NotificationEvent notificationEvent) {
        _log.info("EMAIL-NOTIFICATION-TRADE-IN-Quote-Ready,appointment:{}", notificationEvent.getDocId());
        MultiValueMap<String, String> map = getStringStringMultiValueMap(notificationEvent);
        if (map == null) return;
        //邮件通知用户 报价完成
        AppointmentDoc appointmentDoc = getAppointmentDoc(notificationEvent);
        if (appointmentDoc == null) return;

        map.set("id", "44");
        map.set("HIVEEL_SUBJECT", "Your Hiveel appraisal offer is ready!");
        map.set("HIVEEL_FIRST_NAME", Strings.nullToEmpty(appointmentDoc.getFirstName()));
        map.set("HIVEEL_QUOTE_PRICE", AmountUtil.convertCent2Dollar(String.valueOf(appointmentDoc.getQuotePrice())));
        map.set("HIVEEL_VALID_DATE", DateUtil.date2Str(DateUtil.addDays(appointmentDoc.getLastUpdateAt(), 7), DateUtil.FORMAT_4_EMAIL_1));
        emailComponent.sendEmail(map);

        // 报价完成，短信通知客户
        sendSMSMessage(buildSMSContentForTradeInQuote(appointmentDoc), appointmentDoc.getPhoneNumber());
    }

    private void sendSMS(NotificationEvent notificationEvent) {
        // sms notify merchant
        AppointmentDoc appointmentDoc = getAppointmentDoc(notificationEvent);
        if (appointmentDoc == null) return;

        MchStoreInfo mchStoreInfo = mchStoreInfoMapper.findByMchIdAndStoreId(appointmentDoc.getToMchId(), appointmentDoc.getToStoreId());
        if (mchStoreInfo == null || Strings.isNullOrEmpty(mchStoreInfo.getStorePhone())) {
            _log.warn("Can't send SMS to merchant, please set phone number in db_table of  pay.t_mch_store_info. mchId={},storeId={}", appointmentDoc.getToMchId(), appointmentDoc.getToStoreId());
            return;
        }
        String content = buildSMSContent(appointmentDoc);

        for (String phone : mchStoreInfo.getStorePhone().split(",")) {
            sendSMSMessage(content, phone);
        }
    }

    private void sendSMSMessage(String content, String phone) {
        if (Strings.isNullOrEmpty(content) || Strings.isNullOrEmpty(phone)) {
            return;
        }
        try {
            ShortMessageUtil.send(phone, content);
            Thread.sleep(2000);
        } catch (Exception e) {
            _log.error(String.format("Send SMS to phone:%s", phone), e);
        }
    }

    private AppointmentDoc getAppointmentDoc(NotificationEvent notificationEvent) {
        AppointmentDoc appointmentDoc = appointmentDocMapper.findByAppointmentId(notificationEvent.getDocId());
        if (appointmentDoc == null) {
            return null;
        }
        return appointmentDoc;
    }

    private String buildSMSContentForTradeInQuote(AppointmentDoc appointmentDoc) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hi ")
                .append(appointmentDoc.getFirstName())
                .append(", your Hiveel trade-in offer is :")
                .append(AmountUtil.convertCent2Dollar(String.valueOf(appointmentDoc.getQuotePrice())))
                .append(" which is valid until ")
                .append(DateUtil.date2Str(DateUtil.addDays(appointmentDoc.getLastUpdateAt(), 7), DateUtil.FORMAT_4_EMAIL_1))
                .append(" If you interested in this offer or have any questions, please contact us at ")
                .append("9095339804");
        return sb.toString();
    }

    private String buildSMSContent(AppointmentDoc appointmentDoc) {
        StringBuilder sb = new StringBuilder();
        sb.append("New Appointment! ");
        sb.append("Appt ID: ").append(String.valueOf(appointmentDoc.getAppointmentId()));
        sb.append(",   Time: ").append(DateUtil.date2Str(appointmentDoc.getAppointmentTime(), "MM/dd/YYYY hh:mm a"));
        sb.append(",   First name: ").append(Strings.nullToEmpty(appointmentDoc.getFirstName()));
        sb.append(",   Last name: ").append(Strings.nullToEmpty(appointmentDoc.getLastName()));
        sb.append(",   Phone: ").append(Strings.nullToEmpty(appointmentDoc.getPhoneNumber()));
        sb.append(",   Email: ").append(Strings.nullToEmpty(appointmentDoc.getEmail()));
        sb.append(",   Car Info: Year: ").append(appointmentDoc.getYear() == null ? "" : appointmentDoc.getYear());
        sb.append(",   Make: ").append(Strings.nullToEmpty(appointmentDoc.getMake()));
        sb.append(",   Model: ").append(Strings.nullToEmpty(appointmentDoc.getModel()));
        sb.append(",   Mileage: ").append(appointmentDoc.getMileage() == null ? "" : appointmentDoc.getMileage());
        if (!Strings.isNullOrEmpty(appointmentDoc.getPreviousOfferPrice())) {
            sb.append(",   Previous Offer: $").append(Strings.nullToEmpty(AmountUtil.convertCent2Dollar(String.valueOf(appointmentDoc.getPreviousOfferPrice()))));
        }
        return sb.toString();
    }


//    public static void main(String args[]) {
//        NotificationEventListener n = new NotificationEventListener();
//        AppointmentDoc apt = new AppointmentDoc();
//        apt.setAppointmentId("A2F733AA5E13");
//        apt.setAppointmentTime(new Date());
//        apt.setFirstName("Wilson");
//        apt.setLastName("SMS_Test");
//        apt.setPhoneNumber("9099908811");
//        apt.setEmail("wilson@hiveel.com");
//        apt.setYear(2018);
//        apt.setMake("BMW");
//        apt.setModel("model");
//        apt.setMileage(212121);
//        apt.setPreviousOfferPrice("2002020");
//        String content = n.buildSMSContent(apt);
//        try {
//
//            ShortMessageUtil.send("9099908811", content);
//        } catch (Exception e) {
//            _log.error("Send SMS to phone:", e);
//        }
//    }

    /**
     * at tomorrow (oil-change)
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isAtTomorrowOilChange()")
    public void listenTomorrowOilChange(NotificationEvent notificationEvent) {
        _log.info("EMAIL-NOTIFICATION-OIL_CHANGE-at-tomorrow,appointment:{}", notificationEvent.getDocId());
        MultiValueMap<String, String> map = getStringStringMultiValueMap(notificationEvent);
        if (map == null) return;
        map.set("id", "27");
        map.set("HIVEEL_VIEW_URL", emailConfig.getAppointmentViewUrl().replace(KEY_BIZ_TYPE, OIL_CHANGE).replace(KEY_APPOINTMENT_ID, notificationEvent.getDocId()));

        emailComponent.sendEmail(map);
    }

    /**
     * at tomorrow  (pre-inspection)
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isAtTomorrowPreInspection()")
    public void listenTomorrowPreInspection(NotificationEvent notificationEvent) {
        _log.info("EMAIL-NOTIFICATION-pre-inspection-at-tomorrow,appointment:{}", notificationEvent.getDocId());
        MultiValueMap<String, String> map = getStringStringMultiValueMap(notificationEvent);
        if (map == null) return;
        map.set("id", "26");
        map.set("HIVEEL_VIEW_URL", emailConfig.getAppointmentViewUrl().replace(KEY_BIZ_TYPE, PRE_INSPECTION).replace(KEY_APPOINTMENT_ID, notificationEvent.getDocId()));

        emailComponent.sendEmail(map);
    }

    /**
     * at tomorrow  (trade-in)
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isAtTomorrowTradeIn()")
    public void listenTomorrowTradeIn(NotificationEvent notificationEvent) {
        _log.info("EMAIL-NOTIFICATION-TRADE-IN-at-tomorrow,appointment:{}", notificationEvent.getDocId());
        MultiValueMap<String, String> map = getStringStringMultiValueMap(notificationEvent);
        if (map == null) return;
        map.set("id", "28");
        map.set("HIVEEL_VIEW_URL", emailConfig.getAppointmentViewUrl().replace(KEY_BIZ_TYPE, TRADE_IN).replace(KEY_APPOINTMENT_ID, notificationEvent.getDocId()));

        emailComponent.sendEmail(map);
    }

    /**
     * canceled  (car-inspection)
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isCanceledPreInspection()")
    public void listenCanceledPreInspection(NotificationEvent notificationEvent) {
        _log.info("EMAIL-NOTIFICATION-CAR-INSPECTION-CANCELED,appointment:{}", notificationEvent.getDocId());

        MultiValueMap<String, String> map = getStringStringMultiValueMap(notificationEvent);
        if (map == null) return;
        map.set("id", "32");
        map.set("HIVEEL_VIEW_URL", emailConfig.getAppointmentViewUrl().replace(KEY_BIZ_TYPE, PRE_INSPECTION).replace(KEY_APPOINTMENT_ID, notificationEvent.getDocId()));

        emailComponent.sendEmail(map);
    }

    /**
     * canceled  (oil-change)
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isCanceledOilChange()")
    public void listenCanceledOilChange(NotificationEvent notificationEvent) {
        _log.info("EMAIL-NOTIFICATION-OIL-CHANGE-CANCELED,appointment:{}", notificationEvent.getDocId());
        MultiValueMap<String, String> map = getStringStringMultiValueMap(notificationEvent);
        if (map == null) return;
        map.set("id", "33");
        map.set("HIVEEL_VIEW_URL", emailConfig.getAppointmentViewUrl().replace(KEY_BIZ_TYPE, OIL_CHANGE).replace(KEY_APPOINTMENT_ID, notificationEvent.getDocId()));

        emailComponent.sendEmail(map);
    }

    /**
     * canceled  (traid-in)
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isCanceledTradeIn()")
    public void listenCanceledTradein(NotificationEvent notificationEvent) {
        _log.info("EMAIL-NOTIFICATION-TRADE-IN-CANCELED,appointment:{}", notificationEvent.getDocId());
        MultiValueMap<String, String> map = getStringStringMultiValueMap(notificationEvent);
        if (map == null) return;
        map.set("id", "34");
        map.set("HIVEEL_VIEW_URL", emailConfig.getAppointmentViewUrl().replace(KEY_BIZ_TYPE, TRADE_IN).replace(KEY_APPOINTMENT_ID, notificationEvent.getDocId()));

        emailComponent.sendEmail(map);
    }

    /**
     * refunded  (oil-change)
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isRefundedOilChange()")
    public void listenRefundedOilChange(NotificationEvent notificationEvent) {
        _log.info("EMAIL-NOTIFICATION-oil-change-Refunded,appointment:{}", notificationEvent.getDocId());
        MultiValueMap<String, String> map = getStringStringMultiValueMap(notificationEvent);
        if (map == null) return;
        map.set("id", "36");
        map.set("HIVEEL_VIEW_URL", emailConfig.getAppointmentViewUrl().replace(KEY_BIZ_TYPE, OIL_CHANGE).replace(KEY_APPOINTMENT_ID, notificationEvent.getDocId()));

        emailComponent.sendEmail(map);
    }

    /**
     * refunded  (trade-in)
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isRefundedTradeIn()")
    public void listenRefundedTradeIn(NotificationEvent notificationEvent) {
        _log.info("EMAIL-NOTIFICATION-trade-in-Refunded,appointment:{}", notificationEvent.getDocId());
        MultiValueMap<String, String> map = getStringStringMultiValueMap(notificationEvent);
        if (map == null) return;
        map.set("id", "36");
        map.set("HIVEEL_VIEW_URL", emailConfig.getAppointmentViewUrl().replace(KEY_BIZ_TYPE, TRADE_IN).replace(KEY_APPOINTMENT_ID, notificationEvent.getDocId()));

        emailComponent.sendEmail(map);
    }

    /**
     * refunded  (pre-inspection)
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isRefundedPreInspection()")
    public void listenRefundedPreInspection(NotificationEvent notificationEvent) {
        _log.info("EMAIL-NOTIFICATION-pre-inspection-Refunded,appointment:{}", notificationEvent.getDocId());
        MultiValueMap<String, String> map = getStringStringMultiValueMap(notificationEvent);
        if (map == null) return;
        map.set("id", "36");
        map.set("HIVEEL_VIEW_URL", emailConfig.getAppointmentViewUrl().replace(KEY_BIZ_TYPE, PRE_INSPECTION).replace(KEY_APPOINTMENT_ID, notificationEvent.getDocId()));

        emailComponent.sendEmail(map);
    }


    /**
     * THX  (oil-change)
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isThxOilChange()")
    public void listenThxOilChange(NotificationEvent notificationEvent) {
        _log.info("EMAIL-NOTIFICATION-oil-change-Thx,appointment:{}", notificationEvent.getDocId());
        MultiValueMap<String, String> map = getStringStringMultiValueMap(notificationEvent);
        if (map == null) return;
        map.set("id", "35");
        map.set("HIVEEL_VIEW_URL", emailConfig.getAppointmentViewUrl().replace(KEY_BIZ_TYPE, OIL_CHANGE).replace(KEY_APPOINTMENT_ID, notificationEvent.getDocId()));

        emailComponent.sendEmail(map);
    }

    /**
     * THX  (trade-in)
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isThxTradeIn()")
    public void listenThxTradeIn(NotificationEvent notificationEvent) {
        _log.info("EMAIL-NOTIFICATION-trade-in-Thx,appointment:{}", notificationEvent.getDocId());
        MultiValueMap<String, String> map = getStringStringMultiValueMap(notificationEvent);
        if (map == null) return;
        map.set("id", "35");
        map.set("HIVEEL_VIEW_URL", emailConfig.getAppointmentViewUrl().replace(KEY_BIZ_TYPE, TRADE_IN).replace(KEY_APPOINTMENT_ID, notificationEvent.getDocId()));

        emailComponent.sendEmail(map);
    }

    /**
     * THX  (pre-inspection)
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isThxPreInspection()")
    public void listenThxPreInspection(NotificationEvent notificationEvent) {
        _log.info("EMAIL-NOTIFICATION-pre-inspection-Thx,appointment:{}", notificationEvent.getDocId());
        MultiValueMap<String, String> map = getStringStringMultiValueMap(notificationEvent);
        if (map == null) return;
        map.set("id", "35");
        map.set("HIVEEL_VIEW_URL", emailConfig.getAppointmentViewUrl().replace(KEY_BIZ_TYPE, PRE_INSPECTION).replace(KEY_APPOINTMENT_ID, notificationEvent.getDocId()));

        emailComponent.sendEmail(map);
    }

}
