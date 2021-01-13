package com.hiveelpay.boot.service.events;

import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * 消息通知 事件
 */
public class NotificationEvent extends ApplicationEvent implements Serializable {

    private String docId;
    private ActionPoint actionPoint;
    private NoticeChannel noticeChannel;


    public NotificationEvent(ActionPoint actionPoint, NoticeChannel noticeChannel, String docId) {
        super(actionPoint.name() + noticeChannel.name() + docId);
        this.actionPoint = actionPoint;
        this.noticeChannel = noticeChannel;
        this.docId = docId;
    }

    public static enum NoticeChannel implements Serializable {
        NOTIFICATION, EMAIL, NOTIFICATION_AND_EMAIL, EMAIL_AND_SMS, SMS, NOTIFICATION_AND_EMAIL_SMS
    }

    public static enum ActionPoint implements Serializable {
        NEW_APPOINTMENT_NEED_ADMIN_TO_CHECK,

        APPOINTMENT_BOOKED_OIL_CHANGE,
        APPOINTMENT_BOOKED_PRE_INSPECTION,
        APPOINTMENT_BOOKED_TRADE_IN,

        APPOINTMENT_AT_TOMORROW_OIL_CHANGE,
        APPOINTMENT_AT_TOMORROW_PRE_INSPECTION,
        APPOINTMENT_AT_TOMORROW_TRADE_IN,

        APPOINTMENT_CANCELED_OIL_CHANGE,
        APPOINTMENT_CANCELED_PRE_INSPECTION,
        APPOINTMENT_CANCELED_TRADE_IN,

        APPOINTMENT_REFUNDED_OIL_CHANGE,
        APPOINTMENT_REFUNDED_PRE_INSPECTION,
        APPOINTMENT_REFUNDED_TRADE_IN,

        APPOINTMENT_THX_OIL_CHANGE,
        APPOINTMENT_THX_PRE_INSPECTION,
        APPOINTMENT_THX_TRADE_IN,

        APPOINTMENT_QUOTE_READY

    }

    public String getDocId() {
        return docId;
    }

    public ActionPoint getActionPoint() {
        return actionPoint;
    }

    public NoticeChannel getNoticeChannel() {
        return noticeChannel;
    }

    public boolean isThisTradeInNeedAdminCheck() { // 通知管理员审核
        return actionPoint != null && actionPoint.equals(ActionPoint.NEW_APPOINTMENT_NEED_ADMIN_TO_CHECK);
    }

    public boolean isBookedOilChange() {
        return actionPoint != null && actionPoint.equals(ActionPoint.APPOINTMENT_BOOKED_OIL_CHANGE);
    }

    public boolean isBookedPreInspection() {
        return actionPoint != null && actionPoint.equals(ActionPoint.APPOINTMENT_BOOKED_PRE_INSPECTION);
    }

    public boolean isBookedTradeIn() {
        return actionPoint != null && actionPoint.equals(ActionPoint.APPOINTMENT_BOOKED_TRADE_IN);
    }

    public boolean isAtTomorrowOilChange() {
        return actionPoint != null && actionPoint.equals(ActionPoint.APPOINTMENT_AT_TOMORROW_OIL_CHANGE);
    }

    public boolean isAtTomorrowPreInspection() {
        return actionPoint != null && actionPoint.equals(ActionPoint.APPOINTMENT_AT_TOMORROW_PRE_INSPECTION);
    }

    public boolean isAtTomorrowTradeIn() {
        return actionPoint != null && actionPoint.equals(ActionPoint.APPOINTMENT_AT_TOMORROW_TRADE_IN);
    }


    public boolean isCanceledOilChange() {
        return actionPoint != null && actionPoint.equals(ActionPoint.APPOINTMENT_CANCELED_OIL_CHANGE);
    }

    public boolean isCanceledPreInspection() {
        return actionPoint != null && actionPoint.equals(ActionPoint.APPOINTMENT_CANCELED_PRE_INSPECTION);
    }

    public boolean isCanceledTradeIn() {
        return actionPoint != null && actionPoint.equals(ActionPoint.APPOINTMENT_CANCELED_TRADE_IN);
    }


    public boolean isRefundedOilChange() {
        return actionPoint != null && actionPoint.equals(ActionPoint.APPOINTMENT_REFUNDED_OIL_CHANGE);
    }

    public boolean isRefundedPreInspection() {
        return actionPoint != null && actionPoint.equals(ActionPoint.APPOINTMENT_REFUNDED_PRE_INSPECTION);
    }

    public boolean isRefundedTradeIn() {
        return actionPoint != null && actionPoint.equals(ActionPoint.APPOINTMENT_REFUNDED_TRADE_IN);
    }

    public boolean isThxOilChange() {
        return actionPoint != null && actionPoint.equals(ActionPoint.APPOINTMENT_THX_OIL_CHANGE);
    }

    public boolean isThxPreInspection() {
        return actionPoint != null && actionPoint.equals(ActionPoint.APPOINTMENT_THX_PRE_INSPECTION);
    }

    public boolean isThxTradeIn() {
        return actionPoint != null && actionPoint.equals(ActionPoint.APPOINTMENT_THX_TRADE_IN);
    }

    /**
     * 报价完成事件
     *
     * @return
     */
    public boolean isTradeInQuoteReady() {
        return actionPoint != null && actionPoint.equals(ActionPoint.APPOINTMENT_QUOTE_READY);
    }
}
