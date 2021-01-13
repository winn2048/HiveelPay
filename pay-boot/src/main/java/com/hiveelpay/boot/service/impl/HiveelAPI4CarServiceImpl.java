package com.hiveelpay.boot.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hiveelpay.boot.service.HiveelAPI4CarService;
import com.hiveelpay.common.enumm.BizOrderStatus;
import com.hiveelpay.common.enumm.CustomerServiceStatusEnum;
import com.hiveelpay.common.enumm.PayProductTypeEnum;
import com.hiveelpay.common.model.response.CarServicesCarOfDayProperty;
import com.hiveelpay.common.model.response.CarServicesProperty;
import com.hiveelpay.common.model.response.CarServicesResponse;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.mapper.BizCarMapper;
import com.hiveelpay.dal.dao.mapper.BizOrderMapper;
import com.hiveelpay.dal.dao.mapper.CustomerValidServicesMapper;
import com.hiveelpay.dal.dao.mapper.PayProductMapper;
import com.hiveelpay.dal.dao.model.BizCar;
import com.hiveelpay.dal.dao.model.BizOrder;
import com.hiveelpay.dal.dao.model.CustomerValidServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.groupingBy;

@Service
public class HiveelAPI4CarServiceImpl implements HiveelAPI4CarService {
    private static final MyLog _log = MyLog.getLog(HiveelAPI4CarServiceImpl.class);
    @Autowired
    private BizCarMapper bizCarMapper;
    @Autowired
    private BizOrderMapper bizOrderMapper;

    @Autowired
    private CustomerValidServicesMapper customerValidServicesMapper;
    @Autowired
    private PayProductMapper payProductMapper;


    @Override
    public CarServicesResponse queryCarServices(String carId) {
        checkNotNull(carId, "carId can't null!");

        CarServicesResponse response = new CarServicesResponse();
        response.setCarId(carId);

        List<BizCar> bizCarList = bizCarMapper.findByCarId(carId);
        if (bizCarList == null || bizCarList.isEmpty()) {
            return response;
        }

        Set<BizOrderStatus> statusSet = Sets.newHashSet();
        statusSet.add(BizOrderStatus.SERVICE_ING);//服务中的订单
        List<BizOrder> bizOrderList = bizOrderMapper.findByCarId(carId, statusSet);
        if (bizOrderList == null) {
            bizCarList = Lists.newArrayList();
        }
        BizOrder latestAdvancingBizOrder = bizOrderMapper.findLatestByProductName(carId, PayProductTypeEnum.ADVANCING.name());
        bizOrderList.add(latestAdvancingBizOrder);

        if (bizOrderList.isEmpty()) {
            return response;
        }

        Set<String> bizOrderNos = bizOrderList.stream().filter(Objects::nonNull).map(BizOrder::getBizOrderNo).collect(Collectors.toSet());
        if (bizOrderNos.isEmpty()) {
            return response;
        }

        List<CustomerValidServices> customerValidServicesList = customerValidServicesMapper.findValidServices(bizOrderNos, CustomerServiceStatusEnum.IN_SERVICE);
        if (customerValidServicesList == null) {
            customerValidServicesList = Lists.newArrayList();
        }
        if (latestAdvancingBizOrder != null) {
            customerValidServicesList.add(customerValidServicesMapper.findByBizOrderNoAndServiceType(latestAdvancingBizOrder.getBizOrderNo(), PayProductTypeEnum.ADVANCING));
        }


        customerValidServicesList.forEach(i -> i.setPayProduct(payProductMapper.findByProductId(i.getProductId())));
        Map<PayProductTypeEnum, List<CustomerValidServices>> map = customerValidServicesList.stream().filter(Objects::nonNull).collect(groupingBy(CustomerValidServices::getServiceType));
        List<BizCar> finalBizCarList = bizCarList;
        map.forEach((k, v) -> {
            v.sort(Comparator.comparing(CustomerValidServices::getStartTime));
            CustomerValidServices validServices = v.get(0);

            switch (k) {
                case CAR_OF_DAY: {
                    CarServicesCarOfDayProperty carServicesProperty = new CarServicesCarOfDayProperty(validServices.getStartTime(), validServices.getEndTime());
//                    carServicesProperty.setStartDate(validServices.getStartTime());
//                    carServicesProperty.setEndDate(validServices.getEndTime());
                    BizCar bc = finalBizCarList.stream().filter(i -> i.getBizOrderNo().equalsIgnoreCase(validServices.getBizOrderNo())).findFirst().orElse(null);
                    carServicesProperty.setZipcode(bc == null ? null : bc.getZipcode());

                    response.setCarOfDay(carServicesProperty);
                    break;
                }
                case HIGHLIGHTING: {
//                    CarServicesProperty carServicesProperty = new CarServicesProperty();
//                    carServicesProperty.setStartDate(validServices.getStartTime());
//                    carServicesProperty.setEndDate(validServices.getEndTime());

                    response.setHighlighting(new CarServicesProperty(validServices.getStartTime(), validServices.getEndTime()));
                    break;
                }
                case ADVANCING: {
//                    CarServicesProperty carServicesProperty = new CarServicesProperty();
//                    carServicesProperty.setStartDate(validServices.getStartTime());
//                    carServicesProperty.setEndDate(validServices.getEndTime());
                    response.setAdvancing(new CarServicesProperty(validServices.getStartTime(), validServices.getEndTime()));
                    break;
                }
                case SEARCH_RESULT: {
//                    CarServicesProperty carServicesProperty = new CarServicesProperty();
//                    carServicesProperty.setStartDate(validServices.getStartTime());
//                    carServicesProperty.setEndDate(validServices.getEndTime());

                    response.setSearchResult(new CarServicesProperty(validServices.getStartTime(), validServices.getEndTime()));
                    break;
                }
                default:
                    break;
            }
        });
        return response;
    }

}
