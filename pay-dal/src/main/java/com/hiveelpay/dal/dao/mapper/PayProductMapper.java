package com.hiveelpay.dal.dao.mapper;


import com.hiveelpay.common.enumm.PayProductStatusEnum;
import com.hiveelpay.common.enumm.PayProductTypeEnum;
import com.hiveelpay.dal.dao.model.PayProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayProductMapper {
    int save(@Param("product") PayProduct product);

    List<PayProduct> findList(@Param("offset") int offset, @Param("limit") int limit);

    PayProduct findByProductId(@Param("payProductId") String payProductId);

    int count(@Param("payProduct") PayProduct payProduct);

    int updatePryProduct(@Param("product") PayProduct product);

    int deleteByProductId(@Param("productId") String productId);

    List<PayProduct> findByType(@Param("payProductType") PayProductTypeEnum payProductType, @Param("payProductStatus") PayProductStatusEnum payProductStatus);

    List<PayProduct> findAllSellingProducts(@Param("selling") PayProductStatusEnum selling);
}
