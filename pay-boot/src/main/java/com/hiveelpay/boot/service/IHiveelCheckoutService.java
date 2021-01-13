package com.hiveelpay.boot.service;

import com.hiveelpay.boot.model.CustomerBindCardResult;
import com.hiveelpay.common.model.vo.PayRequestVo;
import com.hiveelpay.dal.dao.model.CustomerAccount;

public interface IHiveelCheckoutService {
    CustomerAccount findCustomerByUserId(String userId);

    /**
     * 创建用户、绑卡和信用卡地址
     *
     * @param payRequestVo
     * @return
     */
    CustomerBindCardResult createCustomerAccountAndCardBinding(PayRequestVo payRequestVo);

    /**
     * 更新用户信息
     *
     * @param customerAccount CustomerAccount
     * @param payRequestVo    PayRequestVo
     */
    CustomerBindCardResult updateCustomerAccountAndCardBinding(CustomerAccount customerAccount, PayRequestVo payRequestVo);

}
