package com.hiveelpay.boot.service;

import com.hiveelpay.common.enumm.BusinessTypeEnum;
import com.hiveelpay.dal.dao.model.MchBlockedAppointmentTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface MerchantService {
    boolean saveBlockAppointmentTime(ArrayList<MchBlockedAppointmentTime> list);

    /**
     * @param mchId        must not null
     * @param storeId      must not null
     * @param dateStr      must not null
     * @param dayNum
     * @param businessType
     * @return
     */
    List<MchBlockedAppointmentTime> findBlockedTimes(String mchId, String storeId, String dateStr, Integer dayNum, BusinessTypeEnum businessType);

    boolean cleanBlockAppointmentTime(ArrayList<MchBlockedAppointmentTime> list);

    /**
     * @param storeId
     * @return
     */
    boolean isBlocked(String storeId, BusinessTypeEnum businessType, Date date);

    boolean isBlocked(String storeId, BusinessTypeEnum businessType, String dateStr);

    Boolean canBlock(String storeId, BusinessTypeEnum businessType, Date date);

    List<String> getMchCanNotBlocktimes(String dayStr, String storeId, BusinessTypeEnum businessType);
}
