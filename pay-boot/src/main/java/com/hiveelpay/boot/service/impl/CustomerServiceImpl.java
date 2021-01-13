package com.hiveelpay.boot.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.hiveelpay.boot.ctrl.exceptions.HiveelUserNotExistsException;
import com.hiveelpay.boot.service.CustomerService;
import com.hiveelpay.common.enumm.BizOrderStatus;
import com.hiveelpay.common.enumm.CustomerServiceStatusEnum;
import com.hiveelpay.common.enumm.PayProductTypeEnum;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.mapper.*;
import com.hiveelpay.dal.dao.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final MyLog _log = MyLog.getLog(CustomerServiceImpl.class);

    @Autowired
    private CustomerAccountMapper customerAccountMapper;

    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    @Autowired
    private CreditCardMapper creditCardMapper;

    @Autowired
    private CustomerValidServicesMapper customerValidServicesMapper;

    @Autowired
    private PayProductMapper payProductMapper;

    @Autowired
    private BizOrderMapper bizOrderMapper;

    /**
     * 创建账户
     *
     * @param account
     * @param address
     */

    @Override
    public void createAccount(CustomerAccount account, Address address) {

    }

    @Override
    public CustomerAccount findCustomerByUserId(String userId) {
        try {
            CustomerAccount customerAccount = customerAccountMapper.findByUserId(userId);
            if (customerAccount != null) {
                return customerAccount;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new HiveelUserNotExistsException();
    }

    @Override
    public List<PaymentMethod> findPaymentMethods(String customerId) {
        if (Strings.isNullOrEmpty(customerId)) {
            return Collections.emptyList();
        }
        List<PaymentMethod> list = paymentMethodMapper.findCustomerPaymentMethods(customerId);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        list.forEach(i -> {
            switch (i.getPaymentMethodType()) {
                case CREDITCARD:
                    CreditCard creditCard = creditCardMapper.findByToken(i.getToken(), i.getCustomerId());
                    String addressId = creditCard.getAddressId();
//                    Address address = addressMapper.findAddressByAddressId(addressId);
                    creditCard.setAddress(addressMapper.findAddressByAddressId(addressId));
                    i.setCreditCard(creditCard);
                    break;
                default:
                    break;
            }
        });
        return list;
    }

    @Override
    public List<CustomerValidServices> findValidServices(String customerId) {
        Set<CustomerServiceStatusEnum> statusEnumSet = Sets.newHashSet();
        statusEnumSet.add(CustomerServiceStatusEnum.INIT);
        statusEnumSet.add(CustomerServiceStatusEnum.IN_SERVICE);

        List<CustomerValidServices> list = customerValidServicesMapper.findByCustomerId(customerId, null, statusEnumSet);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public Boolean checkUserBuy(String productId, String userId) {
        CustomerAccount customerAccount = customerAccountMapper.findByUserId(userId);
        if (customerAccount == null) {
            return Boolean.FALSE;
        }
        PayProduct payProduct = payProductMapper.findByProductId(productId);
        if (payProduct == null) {
            return Boolean.FALSE;
        }
        final String customerId = customerAccount.getCustomerId();
        switch (payProduct.getProductType()) {
            case MEMBERSHIP: {
                return checkUserBuyMembership(productId, payProduct, customerId);
            }
            case ADVANCING: {
                return Boolean.TRUE;
            }
            case SEARCH_RESULT: {
                return Boolean.TRUE;
            }
            case HIGHLIGHTING: {
                return Boolean.TRUE;
            }
            case CAR_OF_DAY: {
                return Boolean.TRUE;
            }
            case PRE_INSPECTION: {
                return Boolean.TRUE;
            }
            case OIL_CHANGE: {
                return Boolean.TRUE;
            }
            case TRADE_IN: {
                return Boolean.TRUE;
            }

            default:
                return Boolean.FALSE;
        }


    }

    private Boolean checkUserBuyMembership(String productId, PayProduct payProduct, String customerId) {
        Set<CustomerServiceStatusEnum> statusEnumSet = Sets.newHashSet();
        statusEnumSet.add(CustomerServiceStatusEnum.INIT);
        statusEnumSet.add(CustomerServiceStatusEnum.IN_SERVICE);

        List<CustomerValidServices> servicesList = customerValidServicesMapper.findByCustomerId(customerId, PayProductTypeEnum.MEMBERSHIP, statusEnumSet);
        if (servicesList == null || servicesList.isEmpty()) {
            return Boolean.TRUE;
        }
        if (servicesList.size() > 1) {
            _log.error("customerId={}, has more than one valid membership service!", customerId);
            return Boolean.FALSE;
        }
        CustomerValidServices membershipService = servicesList.get(0);
        PayProduct currentPayProduct = payProductMapper.findByProductId(membershipService.getProductId());
        if (currentPayProduct.getProductId().equalsIgnoreCase(productId)) {
            return Boolean.FALSE;
        }
        if (currentPayProduct.getAmount() > payProduct.getAmount()) {// level high to low
            List<BizOrder> list = bizOrderMapper.findWillInServiceBizOrderIds(BizOrderStatus.PAY_SUCCESSED);
            long count = list.stream().filter(i -> i.getProductType().equalsIgnoreCase(PayProductTypeEnum.MEMBERSHIP.name())).count();
            if (count > 0) {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
