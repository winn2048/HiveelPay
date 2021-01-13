package com.hiveelpay.dal.dao.mapper;

import com.hiveelpay.common.enumm.AddressType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.hiveelpay.dal.dao.model.Address;

import java.util.List;

@Repository
public interface AddressMapper {
    int save(@Param("address") Address address);

    int update(@Param("addr") Address addr);

    Address findAddressByAddressId(@Param("addressId") String addressId);

    /**
     * @param address
     * @param addressType
     * @return
     */
    List<Address> findSaveAddress(@Param("address") Address address, @Param("addressType") AddressType addressType);
}
