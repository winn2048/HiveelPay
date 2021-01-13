package com.hiveelpay.dal.dao.mapper;

import com.hiveelpay.common.enumm.CustomerServiceStatusEnum;
import com.hiveelpay.common.enumm.PayProductTypeEnum;
import com.hiveelpay.dal.dao.model.BizOrder;
import com.hiveelpay.dal.dao.model.CustomerValidServices;
import com.hiveelpay.dal.dao.model.PayProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface CustomerValidServicesMapper {
    /**
     * @param cvs
     * @return
     */
    int save(@Param("cvs") CustomerValidServices cvs);

    /**
     * @param cvss
     * @return
     */
    int saveMore(@Param("cvss") List<CustomerValidServices> cvss);

    /**
     * @param serviceEndBizOrderNoList
     * @return
     */
    int deleteValidService(@Param("serviceEndBizOrderNoList") List<String> serviceEndBizOrderNoList);

    /**
     * @param customerId
     * @param serviceType
     * @param statusEnumSet
     * @return
     */
    List<CustomerValidServices> findByCustomerId(@Param("customerId") String customerId, @Param("serviceType") PayProductTypeEnum serviceType, @Param("statusEnumSet") Set<CustomerServiceStatusEnum> statusEnumSet);

    /**
     * @param bizOrder
     * @param payProduct
     * @param serviceStatus
     * @return
     */
    int updateStatus(@Param("bizOrder") BizOrder bizOrder, @Param("payProduct") PayProduct payProduct, @Param("serviceStatus") CustomerServiceStatusEnum serviceStatus);

    /**
     * @param bizOrderNo
     * @return
     */
    List<CustomerValidServices> findByBizOrderNo(@Param("bizOrderNo") String bizOrderNo);

    /**
     * 查找即将开始服务的 用户服务
     *
     * @param serviceStatus
     * @return
     */
    List<CustomerValidServices> findWillStartServices(@Param("serviceStatus") CustomerServiceStatusEnum serviceStatus);

    /**
     * @param serviceId
     * @param fromStatus
     * @param toStatus
     * @return
     */
    int updateStatusByServiceId(@Param("serviceId") String serviceId, @Param("fromStatus") CustomerServiceStatusEnum fromStatus, @Param("toStatus") CustomerServiceStatusEnum toStatus);

    /**
     * @param serviceStatus
     * @return
     */
    List<CustomerValidServices> findWillEndServices(@Param("serviceStatus") CustomerServiceStatusEnum serviceStatus);

    /**
     * @param bizOrderIdSet
     * @param serviceStatus
     * @return
     */
    List<CustomerValidServices> findValidServices(@Param("bizOrderIdSet") Set<String> bizOrderIdSet, @Param("serviceStatus") CustomerServiceStatusEnum serviceStatus);

    /**
     * @param bizOrderNo
     * @param now
     * @return
     */
    int updateServiceEndTime(@Param("bizOrderNo") String bizOrderNo, @Param("now") Date now);

    /**
     * @param bizOrderNo
     * @param fromStatusSet
     * @param toStatus
     * @return
     */
    int cancelServiceByBizOrderNo(@Param("bizOrderNo") String bizOrderNo, @Param("fromStatusSet") Set<CustomerServiceStatusEnum> fromStatusSet, @Param("toStatus") CustomerServiceStatusEnum toStatus);

    /**
     * @param statusEnumSet
     * @param types
     * @return
     */
    List<CustomerValidServices> findByStatusAndPayProductTypes(@Param("statusEnumSet") Set<CustomerServiceStatusEnum> statusEnumSet, @Param("types") Set<PayProductTypeEnum> types);

    /**
     * find latest created customer product
     *
     * @param bizOrderNo
     * @param productType
     * @return
     */
    CustomerValidServices findByBizOrderNoAndServiceType(@Param("bizOrderNo") String bizOrderNo, @Param("productType") PayProductTypeEnum productType);
}
