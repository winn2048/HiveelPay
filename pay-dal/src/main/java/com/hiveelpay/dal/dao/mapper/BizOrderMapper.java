package com.hiveelpay.dal.dao.mapper;

import com.hiveelpay.common.enumm.BizOrderStatus;
import com.hiveelpay.common.model.HiveelPage;
import com.hiveelpay.common.model.requests.BizOrderSearchRequest;
import com.hiveelpay.dal.dao.model.BizOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;


@Repository
public interface BizOrderMapper {
    /**
     * @param order
     * @return
     */
    int save(@Param("order") BizOrder order);

    /**
     * @param bizOrderNo
     * @param fromStatus
     * @param toStatus
     * @return
     */
    int updatePaySuccess(@Param("bizOrderNo") String bizOrderNo, @Param("fromStatus") BizOrderStatus fromStatus, @Param("toStatus") BizOrderStatus toStatus);

    /**
     * @param bizOrderNo
     * @param fromStatus
     * @param toStatus
     * @return
     */
    int updatePayFailed(@Param("bizOrderNo") String bizOrderNo, @Param("fromStatus") BizOrderStatus fromStatus, @Param("toStatus") BizOrderStatus toStatus);

    /**
     * @param customerId
     * @param kindNameSet
     * @return
     */
    int countTotal(@Param("customerId") String customerId, @Param("kindNameSet") Set<String> kindNameSet);

    /**
     * @param customerId
     * @param kindNameSet
     * @param hiveelPage
     * @return
     */
    List<BizOrder> loadHistoryOrder(@Param("customerId") String customerId, @Param("kindNameSet") Set<String> kindNameSet, @Param("hiveelPage") HiveelPage hiveelPage);

    /**
     * 终止服务
     *
     * @param serviceEnd
     * @return
     */
    int checkOrderServiceEnd(@Param("serviceEnd") BizOrderStatus serviceEnd);

    /**
     * @param bizOrderNo
     * @return
     */
    BizOrder selectByBizOrderNo(@Param("bizOrderNo") String bizOrderNo);

    List<BizOrder> searchBizOrder(@Param("offset") int offset, @Param("limit") int limit, @Param("bizOrder") BizOrder bizOrder);

    /**
     * @param bizOrder
     * @return
     */
    Integer searchBizOrderCount(@Param("bizOrder") BizOrder bizOrder);

    /**
     * @param willInServiceBizOrderIds
     * @param fromStatus
     * @param toStatus
     * @return
     */
    int checkInserviceOrders(@Param("willInServiceBizOrderIds") List<String> willInServiceBizOrderIds, @Param("fromStatus") BizOrderStatus fromStatus, @Param("toStatus") BizOrderStatus toStatus);

    /**
     * @param customerId
     * @param status
     * @return
     */
    List<BizOrder> findByStatus(@Param("customerId") String customerId, @Param("status") BizOrderStatus status);

    /**
     * @param customerId
     * @param payProductTypeName
     * @return
     */
    BizOrder findCustomerValidMembershipOrder(@Param("customerId") String customerId, @Param("payProductTypeName") String payProductTypeName);

    /**
     * @param customerId
     * @param bizOrderId
     * @return
     */
    BizOrder findCustomerBizOrder(@Param("customerId") String customerId, @Param("bizOrderId") String bizOrderId);

    /**
     * @param currentStatus
     * @return
     */
    List<BizOrder> findByBizOrderStatus(@Param("currentStatus") BizOrderStatus currentStatus);

    /**
     * @param serviceEndBizOrderNoList collection of bizOrderNo
     * @param fromStatus
     * @param toStatus
     * @return
     */
    int updateBizOrdersStatus(@Param("serviceEndBizOrderNoList") List<String> serviceEndBizOrderNoList, @Param("fromStatus") BizOrderStatus fromStatus, @Param("toStatus") BizOrderStatus toStatus);


    int updateEmptyInvalidBizOrderNo(@Param("validBizOrderNo") String validBizOrderNo);

    List<BizOrder> findHasInvalidBizOrderNos();


    /**
     * @param bizOrderNo
     * @return
     */
    int updateEmptyInvalidBizOrderNoByBizOrderNo(@Param("bizOrderNo") String bizOrderNo);

    /**
     * @param paySuccessed
     * @return
     */
    List<BizOrder> findWillInServiceBizOrderIds(@Param("paySuccessed") BizOrderStatus paySuccessed);

    /**
     * @param invalidBizOrderNo
     * @return
     */
    int updateServiceEndTime(@Param("invalidBizOrderNo") String invalidBizOrderNo);

    /*
     * 统计merchant订单总数
     */
    int countMerchantOrders(@Param("mchId") String mchId);

    /**
     * 查询某个商户的订单
     *
     * @param mchId
     * @param hiveelPage
     * @return
     */
    List<BizOrder> loadMerchantOrders(@Param("mchId") String mchId, @Param("hiveelPage") HiveelPage hiveelPage);

    /**
     * @param searchRequest
     * @param statusSet
     * @return
     */
    int searchBizOrdersCount(@Param("searchRequest") BizOrderSearchRequest searchRequest, @Param("statusSet") Set<BizOrderStatus> statusSet);

    /**
     * @param searchRequest
     * @param statusSet
     * @param page
     * @return
     */
    List<BizOrder> searchBizOrders(@Param("searchRequest") BizOrderSearchRequest searchRequest, @Param("statusSet") Set<BizOrderStatus> statusSet, @Param("page") HiveelPage page);

    /**
     * @param bizOrderNoList
     * @return
     */
    List<BizOrder> selectByBizOrderNoList(@Param("bizOrderNoList") List<String> bizOrderNoList);

    /**
     * @param bizOrderSets
     * @return
     */
    List<BizOrder> findBizOrders(@Param("bizOrderSets") Set<String> bizOrderSets);

    /**
     * @param carId
     * @param statusSet
     * @return
     */
    List<BizOrder> findByCarId(@Param("carId") String carId, @Param("statusSet") Set<BizOrderStatus> statusSet);

    List<BizOrder> searchBizOrdersByCustomerId(@Param("customerId") String customerId, @Param("statusSet") Set<BizOrderStatus> statusSet, @Param("productTypeName") String productTypeName);

    List<BizOrder> queryValidBizOrders(@Param("serviceTypes") List<String> serviceTypes, @Param("statusSet") Set<BizOrderStatus> statusSet);

    Long queryBizOrders(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("productType") String productType, @Param("statusSet") Set<BizOrderStatus> statusSet);

    /**
     *
     * @param carId
     * @param productType
     * @return
     */
    BizOrder findLatestByProductName(@Param("carId") String carId, @Param("productType") String productType);
}
