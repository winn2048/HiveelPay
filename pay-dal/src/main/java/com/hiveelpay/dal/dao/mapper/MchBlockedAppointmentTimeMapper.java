package com.hiveelpay.dal.dao.mapper;

import com.hiveelpay.common.enumm.BusinessTypeEnum;
import com.hiveelpay.dal.dao.model.MchBlockedAppointmentTime;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository
public interface MchBlockedAppointmentTimeMapper {
    /**
     * @param blockedAppointmentTime
     * @return
     */
    int saveOne(@Param("blockedAppointmentTime") MchBlockedAppointmentTime blockedAppointmentTime);

    /**
     * @param list
     * @return
     */
    int saveMore(@Param("list") List<MchBlockedAppointmentTime> list);

    /**
     * @param accountId
     * @param dateStr
     * @return
     */
    int deleteByDate(@Param("accountId") String accountId, @Param("storeId") String storeId, @Param("dateStr") String dateStr);

    /**
     * @param accountId
     * @param storeId
     * @param dateStr
     * @param dayStr
     * @param businessType
     * @return
     */
    List<MchBlockedAppointmentTime> findBlockedTimes(@Param("accountId") String accountId, @Param("storeId") String storeId, @Param("dateStr") String dateStr, @Param("dayStr") String dayStr, @Param("businessType") BusinessTypeEnum businessType);

    /**
     * @param item
     * @return
     */
    int deleteBlockedDate(@Param("item") MchBlockedAppointmentTime item);

    /**
     * @param storeId
     * @param dateStr
     * @param businessType
     * @return
     */
    MchBlockedAppointmentTime findBlockedTime(@Param("storeId") String storeId, @Param("dateStr") String dateStr, @Param("businessType") BusinessTypeEnum businessType);
}
