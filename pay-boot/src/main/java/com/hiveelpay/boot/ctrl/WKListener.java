package com.hiveelpay.boot.ctrl;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.WebhookNotification;
import com.google.common.base.Strings;
import com.hiveelpay.boot.service.PaySubscriptionService;
import com.hiveelpay.boot.service.channel.hiveel.HiveelConfig;
import com.hiveelpay.common.util.HiveelPayUtil;
import com.hiveelpay.common.util.MyLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/wk")
public class WKListener {
    private final MyLog _log = MyLog.getLog(WKListener.class);

    @Autowired
    private PaySubscriptionService paySubscriptionServiceImpl;

    @Autowired
    private BraintreeGateway gateway;

    @Autowired
    private HiveelConfig hiveelConfig;

    @GetMapping("/test/{kind}/{id}")
    public String test(@PathVariable("kind") WebhookNotification.Kind kind, @PathVariable("id") String id) {
        HashMap<String, String> notificationMap = gateway.webhookTesting().sampleNotification(kind, id);
        String url = hiveelConfig.getPayServerBaseUrl() + "/api/wk/subscription?bt_signature=" + notificationMap.get("bt_signature") + "&bt_payload=" + notificationMap.get("bt_payload");
        return HiveelPayUtil.call4Post(url);
    }

    @PostMapping("/subscription")
    public String listenSubscription(HttpServletRequest request, String bt_signature, String bt_payload) {
        _log.info("WK-----{}", request.getRequestURI());
        Map<String, String[]> parameters = request.getParameterMap();
        if (parameters != null && !parameters.isEmpty()) {
            for (Map.Entry<String, String[]> m : parameters.entrySet()) {
                _log.info("key:{},value:{}", Strings.nullToEmpty(m.getKey()), m.getValue());
            }
        }

        WebhookNotification notification = gateway.webhookNotification().parse(bt_signature, bt_payload);
        switch (notification.getKind()) {
            case CHECK:
                return "success";
            case SUBSCRIPTION_CHARGED_SUCCESSFULLY:
                paySubscriptionServiceImpl.doChargedSuccessfully(notification);
                break;
            case SUBSCRIPTION_CHARGED_UNSUCCESSFULLY:
                paySubscriptionServiceImpl.doChargedUnSuccessfully(notification);
                break;
            case SUBSCRIPTION_CANCELED:
                paySubscriptionServiceImpl.doCanceled(notification);
                break;
            case SUBSCRIPTION_WENT_ACTIVE:
                paySubscriptionServiceImpl.doWentActive(notification);
                break;
            case SUBSCRIPTION_EXPIRED:
                break;
            case SUBSCRIPTION_WENT_PAST_DUE:
                break;
            case SUBSCRIPTION_TRIAL_ENDED:
                break;
            default:
                break;
        }

        return "";
    }


}
