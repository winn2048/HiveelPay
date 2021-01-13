package com.hiveelpay.shop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * hiveel.config.payCenterServerUrl=http://localhost:3020
 * hiveel.config.pay.product.type.member=10
 * hiveel.config.pay.product.type.searchResult=20
 * hiveel.config.apiLoadMemberProducts=/api/product/${hiveel.config.pay.product.type.member}/list
 * hiveel.config.apiLoadSearchResultProducts=/api/product/${hiveel.config.pay.product.type.searchResult}/list
 */
@Component
@ConfigurationProperties(prefix = "hiveel.config")
public class HiveelConfig {

    private String payCenterServerUrl;
    private String apiLoadMemberProducts;
    private String apiLoadSearchResultProducts;
    private String apiProductShow;
    private String apiPayToken;// get token

    private String apiPayCheckout;//

    private String apiAdvancingProducts;
    private String apiAarOfDayProducts;
    private String apiLoadhighlightingProducts;//

    private String apiLoadAllProducts;// all


    public String getPayCenterServerUrl() {
        return payCenterServerUrl;
    }

    public void setPayCenterServerUrl(String payCenterServerUrl) {
        this.payCenterServerUrl = payCenterServerUrl;
    }

    public String getApiLoadMemberProducts() {
        return payCenterServerUrl + apiLoadMemberProducts;
    }

    public void setApiLoadMemberProducts(String apiLoadMemberProducts) {
        this.apiLoadMemberProducts = apiLoadMemberProducts;
    }

    public String getApiLoadSearchResultProducts() {
        return payCenterServerUrl + apiLoadSearchResultProducts;
    }

    public void setApiLoadSearchResultProducts(String apiLoadSearchResultProducts) {
        this.apiLoadSearchResultProducts = apiLoadSearchResultProducts;
    }

    public String getApiProductShow() {
        return payCenterServerUrl + apiProductShow;
    }

    public void setApiProductShow(String apiProductShow) {
        this.apiProductShow = apiProductShow;
    }

    public void setApiPayToken(String apiPayToken) {
        this.apiPayToken = apiPayToken;
    }

    public String getApiPayToken() {
        return payCenterServerUrl + apiPayToken;
    }

    public void setApiPayCheckout(String apiPayCheckout) {
        this.apiPayCheckout = apiPayCheckout;
    }

    public String getApiPayCheckout() {
        return payCenterServerUrl + apiPayCheckout;
    }

    public String getApiAdvancingProducts() {
        return payCenterServerUrl + apiAdvancingProducts;
    }

    public void setApiAdvancingProducts(String apiAdvancingProducts) {
        this.apiAdvancingProducts = apiAdvancingProducts;
    }

    public String getApiAarOfDayProducts() {
        return payCenterServerUrl + apiAarOfDayProducts;
    }

    public void setApiAarOfDayProducts(String apiAarOfDayProducts) {
        this.apiAarOfDayProducts = apiAarOfDayProducts;
    }

    public String getApiLoadhighlightingProducts() {
        return payCenterServerUrl + apiLoadhighlightingProducts;
    }

    public void setApiLoadhighlightingProducts(String apiLoadhighlightingProducts) {
        this.apiLoadhighlightingProducts = apiLoadhighlightingProducts;
    }

    public String getApiLoadAllProducts() {
        return payCenterServerUrl + apiLoadAllProducts;
    }

    public void setApiLoadAllProducts(String apiLoadAllProducts) {
        this.apiLoadAllProducts = apiLoadAllProducts;
    }
}
