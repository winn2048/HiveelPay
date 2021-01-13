package com.hiveelpay.mgr.service.events;

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
        NOTIFICATION, EMAIL, NOTIFICATION_AND_EMAIL, SMS, EMAIL_AND_SMS, ALL
    }

    public static enum ActionPoint implements Serializable {
        NEW_INVOICE_GENERATED,
        SYSTEM_EXCEPTION

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

    public boolean isInvoiceReady() {
        return actionPoint != null && actionPoint.equals(ActionPoint.NEW_INVOICE_GENERATED);
    }

    public boolean isSystemException() {
        return actionPoint != null && actionPoint.equals(ActionPoint.SYSTEM_EXCEPTION);
    }


}
