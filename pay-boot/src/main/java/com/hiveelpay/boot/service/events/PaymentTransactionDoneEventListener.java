package com.hiveelpay.boot.service.events;

import com.hiveelpay.boot.service.BizOrderService;
import com.hiveelpay.boot.service.CustomerValidServiceService;
import com.hiveelpay.boot.service.PayProductService;
import com.hiveelpay.common.util.MyLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 支付成功
 */
@Component
public class PaymentTransactionDoneEventListener {
    private static final MyLog _log = MyLog.getLog(PaymentTransactionDoneEventListener.class);
    @Autowired
    private BizOrderService bizOrderServiceImpl;
    @Autowired
    private CustomerValidServiceService customerValidServiceServiceImpl;

//    @Autowired
//    private CustomerValidServicesMapper customerValidServicesMapper;

    @Autowired
    private PayProductService payProductServiceImpl;

    @EventListener
    public void listen(PaymentTransactionDoneEvent paymentTransactionDoneEvent) {
        _log.info("PaymentTransactionDone:{}", paymentTransactionDoneEvent);

        switch (paymentTransactionDoneEvent.getStatus()) {
            case SUCCESS://支付成功
                _log.info("Payment success!:{}", paymentTransactionDoneEvent);
                break;
            default:
                break;
        }
    }


}
