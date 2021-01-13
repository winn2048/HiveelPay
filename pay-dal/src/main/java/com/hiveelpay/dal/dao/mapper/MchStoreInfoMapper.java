package com.hiveelpay.dal.dao.mapper;

import com.hiveelpay.dal.dao.model.MchStoreInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MchStoreInfoMapper {
    int save(@Param("mchStoreInfo") MchStoreInfo mchStoreInfo);

    MchStoreInfo findByMchIdAndStoreId(@Param("mchId") String mchId, @Param("storeId") String storeId);

    List<MchStoreInfo> findByMchId(@Param("mchId") String mchId);
}
