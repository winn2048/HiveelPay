package com.hiveelpay.boot.service;

import com.hiveelpay.common.model.response.CarServicesResponse;

public interface HiveelAPI4CarService {
    /**
     * 车关联的购买的服务
     * @param carId
     * @return
     */
    CarServicesResponse queryCarServices(String carId);
}
