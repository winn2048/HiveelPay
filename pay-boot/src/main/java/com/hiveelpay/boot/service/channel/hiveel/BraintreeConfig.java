package com.hiveelpay.boot.service.channel.hiveel;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: wilson
 * @date: 17/8/21
 * @description:
 */
@Component
@ConfigurationProperties(prefix = "config.braintree")
public class BraintreeConfig {
    /**
     * btEnvironment: "sandbox"
     * btMerchantId: "hdkfsz2tnsx7tq9s"
     * btPublicKey: "4mw23fj89g9n3z3h"
     * btPrivateKey: "25d93abdec90d31701b6d3edd00d5b0b"
     */

    private String btEnvironment;
    private String btMerchantId;
    private String btPublicKey;
    private String btPrivateKey;

    public String getBtEnvironment() {
        return btEnvironment;
    }

    public void setBtEnvironment(String btEnvironment) {
        this.btEnvironment = btEnvironment;
    }

    public String getBtMerchantId() {
        return btMerchantId;
    }

    public void setBtMerchantId(String btMerchantId) {
        this.btMerchantId = btMerchantId;
    }

    public String getBtPublicKey() {
        return btPublicKey;
    }

    public void setBtPublicKey(String btPublicKey) {
        this.btPublicKey = btPublicKey;
    }

    public String getBtPrivateKey() {
        return btPrivateKey;
    }

    public void setBtPrivateKey(String btPrivateKey) {
        this.btPrivateKey = btPrivateKey;
    }
}

