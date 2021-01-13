package com.hiveelpay.dal.dao.mapper;

import com.hiveelpay.dal.dao.model.ServiceTime;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface ServiceTimeMapper {

    int save(@Param("serviceTime") ServiceTime serviceTime);

    /**
     * @param bizOrderNo
     * @return
     */
    List<ServiceTime> queryByBizOrderNo(String bizOrderNo);

    /**
     * Query biz orders
     *
     * @param bizOrderNoSet
     * @return
     */
    List<ServiceTime> queryByBizOrderNos(@Param("bizOrderNoSet") Set<String> bizOrderNoSet);

    int saveMore(@Param("serviceTimes") List<ServiceTime> serviceTimes);

    /**
     * update service end-time to now
     *
     * @param bizOrderNo
     */
    int updateServiceEndTimeToNow(@Param("bizOrderNo") String bizOrderNo);


    /**
     *
     * @param bizOrderNo
     * @param now
     * @return
     */
    int updateServiceEndTime(@Param("bizOrderNo") String bizOrderNo, @Param("now") Date now);
}
