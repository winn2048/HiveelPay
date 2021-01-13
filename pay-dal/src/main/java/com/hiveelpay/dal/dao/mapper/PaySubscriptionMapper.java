package com.hiveelpay.dal.dao.mapper;

import com.hiveelpay.dal.dao.model.PaySubscription;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaySubscriptionMapper {
    int save(@Param("paySubscription") PaySubscription paySubscription);

    int updateStatusBySubscriptionId(@Param("subscriptionId") String subscriptionId, @Param("statusName") String statusName);

    int updateErrorMsg(@Param("subscriptionId") String subscriptionId, @Param("statusName") String statusName, @Param("errorMsg") String errorMsg);

    List<PaySubscription> getAfter10MinutesSubReqs(@Param("statusName") String statusName, @Param("subReqCount") int subReqCount);

    PaySubscription findBySubscriptionId(@Param("sId") String sId);

    PaySubscription findByBizOrderNo(@Param("bizOrderNo") String bizOrderNo);
}
