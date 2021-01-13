package com.hiveelpay.boot.service;

import com.hiveelpay.common.enumm.PayProductTypeEnum;
import com.hiveelpay.common.model.HiveelPage;
import com.hiveelpay.common.model.requests.BizOrderSearchRequest;
import com.hiveelpay.dal.dao.model.BizOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BizOrderService4Admin {


    void updatePayFailed(String bizOrderNo);

    List<BizOrder> findHistoryOrder(String userId, Set<PayProductTypeEnum> kindSet, HiveelPage hiveelPage);

    List<BizOrder> findMerchantOrders(String mchId, HiveelPage hiveelPage);

    void checkAndEndService();

    BizOrder findCustomerValidMembershipOrder(String customerId);

    BizOrder findByBizOrderNo(String customerId, String bizOrderId);

    List<BizOrder> findHasInvalidBizOrderNos();


    List<BizOrder> findServiceNotStartMembershipBizOrders(String customerId);

    /**
     * @param searchRequest
     * @param hiveelPage
     * @return
     */
    List<BizOrder> searchBizOrders(BizOrderSearchRequest searchRequest, HiveelPage hiveelPage);

    List<BizOrder> findBizOrders(Set<String> bizOrderSets);

    void initBizOrderToService();


    /**
     * 检查服务的开始时间，到时间即开启服务
     */
    void checkAndStartServices();


    /**
     * 检查bizOrder所有的服务时间是否都完成了
     */
    void checkBizOrderServiceDone();

    /**
     * @param bizOrderId
     * @return
     */
    BizOrder findBizOrder(String bizOrderId);

    /**
     * 退款，目前只修改了状态
     *
     * @param bizOrder
     */
    void refundOrder(BizOrder bizOrder);

    List<BizOrder> queryValidBizOrders(List<String> asList, String zipcode);

    Map<String, String> countIncome(Date startDate, Date endDate, PayProductTypeEnum productType);

}
