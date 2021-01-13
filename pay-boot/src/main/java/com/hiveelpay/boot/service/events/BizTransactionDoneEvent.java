package com.hiveelpay.boot.service.events;

import org.springframework.context.ApplicationEvent;
import com.hiveelpay.dal.dao.model.BizOrder;

import java.io.Serializable;

/**
 * 业务订单处理完成 事件
 */
public class BizTransactionDoneEvent extends ApplicationEvent implements Serializable {

    private BizOrder bizOrder;
    private STATUS status;


    public BizTransactionDoneEvent(BizOrder bizOrder, STATUS status) {
        super(bizOrder);
        this.bizOrder = bizOrder;
        this.status = status;
    }

    public static enum STATUS {
        SUCCESS, FAILED, FAILED_SAVED_TRANSACTION
    }

    public BizOrder getBizOrder() {
        return bizOrder;
    }

    public STATUS getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "BizTransactionDoneEvent{" +
                "bizOrder=" + bizOrder +
                ", status=" + status +
                ", source=" + source +
                '}';
    }
}
