package com.hiveelpay.boot.service.events;

import com.hiveelpay.boot.ctrl.exceptions.HiveelPayErrorCode;
import com.hiveelpay.boot.service.BizOrderService;
import com.hiveelpay.common.exceptions.HiveelPayException;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.model.BizOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 业务订单处理完成 事件
 */
@Component
public class BizTransactionDoneEventListener {

    private static final MyLog _log = MyLog.getLog(BizTransactionDoneEventListener.class);
    @Autowired
    private BizOrderService bizOrderServiceImpl;

    @EventListener
    public void listen(BizTransactionDoneEvent bizTransactionDoneEvent) {
        final BizOrder bizOrder = bizTransactionDoneEvent.getBizOrder();
        if (bizOrder == null) {
            throw new HiveelPayException(HiveelPayErrorCode.NULL_BIZ_ORDER);
        }
        switch (bizTransactionDoneEvent.getStatus()) {
            case SUCCESS:// 业务订单支付成功
                _log.info("Biz Transaction success!");
                bizOrderServiceImpl.updatePaySuccess(bizOrder.getBizOrderNo());
                bizOrderServiceImpl.checkAndStartServices();
                break;
            case FAILED:
                _log.info("Biz Transaction failed!");
                bizOrderServiceImpl.updatePayFailed(bizOrder.getBizOrderNo());
                break;
            default:
                break;
        }

    }
}
