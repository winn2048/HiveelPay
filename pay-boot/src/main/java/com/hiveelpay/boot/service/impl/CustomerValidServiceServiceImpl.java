package com.hiveelpay.boot.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hiveelpay.boot.ctrl.exceptions.HiveelPayErrorCode;
import com.hiveelpay.boot.service.CustomerValidServiceService;
import com.hiveelpay.common.enumm.BizOrderStatus;
import com.hiveelpay.common.enumm.CustomerServiceStatusEnum;
import com.hiveelpay.common.enumm.PayProductTypeEnum;
import com.hiveelpay.common.exceptions.HiveelPayException;
import com.hiveelpay.common.util.HiveelID;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.mapper.BizOrderMapper;
import com.hiveelpay.dal.dao.mapper.CustomerAccountMapper;
import com.hiveelpay.dal.dao.mapper.CustomerValidServicesMapper;
import com.hiveelpay.dal.dao.mapper.PayProductMapper;
import com.hiveelpay.dal.dao.model.BizOrder;
import com.hiveelpay.dal.dao.model.CustomerValidServices;
import com.hiveelpay.dal.dao.model.PayProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.groupingBy;

/**
 *
 */
@Service
public class CustomerValidServiceServiceImpl implements CustomerValidServiceService {
    private static final MyLog _log = MyLog.getLog(CustomerValidServiceServiceImpl.class);

    @Autowired
    private CustomerAccountMapper customerAccountMapper;

    @Autowired
    private BizOrderMapper bizOrderMapper;

    @Autowired
    private CustomerValidServicesMapper customerValidServicesMapper;

    @Autowired
    private PayProductMapper payProductMapper;


    /**
     * @param customerId
     */
    @Override
    public void mergeService(String customerId) {
        if (Strings.isNullOrEmpty(customerId)) {
            _log.warn("CustomerValidServiceServiceImpl#mergeService(null)");
            return;
        }

        List<BizOrder> bizOrderList = bizOrderMapper.findByStatus(customerId, BizOrderStatus.SERVICE_ING);
        if (bizOrderList == null || bizOrderList.isEmpty()) {
            return;
        }

        /**
         *     MEMBERSHIP(10, "MEMBERSHIP"),
         *     ADVANCING(12, "Advancing"),
         *     CAR_OF_DAY(14, "每日一车"),
         *     SEARCH_RESULT(20, "搜索结果排名靠前服务"),
         *     HIGHLIGHTING(22, "Highlighting"),
         */
        Map<String, List<BizOrder>> map = bizOrderList.stream().collect(groupingBy(BizOrder::getProductType));
        List<CustomerValidServices> validServicesList = Lists.newArrayList();
        map.forEach((k, v) -> {
            PayProductTypeEnum type = PayProductTypeEnum.byName(k);
            switch (type) {
                case MEMBERSHIP: {
                    validServicesList.add(processValidMemberShip(v));
                    break;
                }
                case ADVANCING: {
                    validServicesList.add(processValidAdvancing(v));
                    break;
                }
                case CAR_OF_DAY: {
                    validServicesList.add(processValidCarOfDay(v));
                    break;
                }
                case SEARCH_RESULT: {
                    validServicesList.add(processValidSearchResult(v));
                    break;
                }
                case HIGHLIGHTING: {
                    validServicesList.add(processValidHighlighting(v));
                    break;
                }
                default:
                    break;
            }

        });
    }

    /**
     * 查找当前用户已购买的会员信息
     *
     * @param customerId
     * @return
     */
    @Override
    public CustomerValidServices findCurrentMemberShipService(String customerId) {
        checkArgument(!Strings.isNullOrEmpty(customerId), "customerId must not be null!");

        Set<CustomerServiceStatusEnum> statusEnumSet = Sets.newHashSet();
//        statusEnumSet.add(CustomerServiceStatusEnum.INIT);
        statusEnumSet.add(CustomerServiceStatusEnum.IN_SERVICE);

        List<CustomerValidServices> list = customerValidServicesMapper.findByCustomerId(customerId, PayProductTypeEnum.MEMBERSHIP, statusEnumSet);
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            _log.error("Customer has more than one valid membership service at the same time. Wrong, Please check!");
            throw new HiveelPayException(HiveelPayErrorCode.MULTI_VALID_MEMBERSHIP_SERVICE);
        }
        CustomerValidServices rs = list.get(0);
        final String productId = rs.getProductId();
        rs.setPayProduct(payProductMapper.findByProductId(productId));

        return rs;
    }

    @Override
    public List<CustomerValidServices> findByCustomerId(String customerId, PayProductTypeEnum payProductType, Set<CustomerServiceStatusEnum> statusEnumSet) {
        return customerValidServicesMapper.findByCustomerId(customerId, payProductType, statusEnumSet);
    }

    @Override
    public List<CustomerValidServices> findByCustomerIdIncludeProduct(String customerId, PayProductTypeEnum productType, Set<CustomerServiceStatusEnum> statusEnumSet) {
        List<CustomerValidServices> list = findByCustomerId(customerId, productType, statusEnumSet);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        list.forEach(i -> i.setPayProduct(payProductMapper.findByProductId(i.getProductId())));
        return list;
    }

    @Override
    public List<CustomerValidServices> createCustomerService(BizOrder bizOrder, PayProduct payProduct) {
        List<CustomerValidServices> list = Lists.newArrayList();
        bizOrder.getServiceTimes().forEach(i -> {
            CustomerValidServices cvs = new CustomerValidServices();
            cvs.setCustomerId(bizOrder.getCustomerId());
            cvs.setServiceId(HiveelID.getInstance().getRandomId("S"));
            cvs.setProductId(bizOrder.getProductId());
            cvs.setBizOrderNo(bizOrder.getBizOrderNo());
            cvs.setServiceName(payProduct.getProductName());
            cvs.setServiceType(payProduct.getProductType());
            cvs.setServiceStatus(CustomerServiceStatusEnum.INIT);
            cvs.setStartTime(i.getServiceStartTime());
            cvs.setEndTime(i.getServiceEndTime());
            list.add(cvs);
        });

        customerValidServicesMapper.saveMore(list);
        return list;
    }

    private CustomerValidServices processValidHighlighting(List<BizOrder> list) {
        return null;
    }

    private CustomerValidServices processValidSearchResult(List<BizOrder> list) {
        return null;
    }

    private CustomerValidServices processValidCarOfDay(List<BizOrder> list) {
        return null;
    }

    private CustomerValidServices processValidAdvancing(List<BizOrder> list) {
        return null;
    }

    private CustomerValidServices processValidMemberShip(List<BizOrder> list) {

        return null;
    }
}
