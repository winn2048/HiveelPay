package com.hiveelpay.dal.dao.mapper;

import com.hiveelpay.common.enumm.SyncStatus;
import com.hiveelpay.dal.dao.model.BizCar;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BizCarMapper {
    int save(@Param("bizCar") BizCar bizCar);

    /**
     * @param carId
     * @return
     */
    List<BizCar> findByCarId(@Param("carId") String carId);

    /**
     * @param bizOrderNo
     * @param carId
     * @param syncStatus
     * @return
     */
    int increaceNotifyCount(@Param("bizOrderNo") String bizOrderNo, @Param("carId") String carId, @Param("syncStatus") SyncStatus syncStatus);

    /**
     * @param syncStatus
     * @param maxNotifyCount
     * @return
     */
    List<BizCar> findByStatus(@Param("syncStatus") SyncStatus syncStatus, @Param("maxNotifyCount") int maxNotifyCount);

    List<BizCar> findByBizOrderIds(@Param("bizOrderNoSet") Set<String> bizOrderNoSet);
}
