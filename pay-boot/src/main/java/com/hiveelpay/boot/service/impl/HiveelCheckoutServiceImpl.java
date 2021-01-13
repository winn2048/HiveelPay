package com.hiveelpay.boot.service.impl;

import com.braintreegateway.*;
import com.google.common.base.Strings;
import com.hiveelpay.boot.ctrl.exceptions.HiveelPayErrorCode;
import com.hiveelpay.boot.model.CustomerBindCardResult;
import com.hiveelpay.boot.service.IHiveelCheckoutService;
import com.hiveelpay.boot.service.exceptions.HiveelPayServiceErrorCode;
import com.hiveelpay.common.enumm.AddressType;
import com.hiveelpay.common.enumm.PaymentMethodTypeEnum;
import com.hiveelpay.common.enumm.SyncStatus;
import com.hiveelpay.common.exceptions.HiveelPayException;
import com.hiveelpay.common.model.vo.PayRequestVo;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.common.util.MySeq;
import com.hiveelpay.dal.dao.mapper.AddressMapper;
import com.hiveelpay.dal.dao.mapper.CreditCardMapper;
import com.hiveelpay.dal.dao.mapper.CustomerAccountMapper;
import com.hiveelpay.dal.dao.mapper.PaymentMethodMapper;
import com.hiveelpay.dal.dao.model.Address;
import com.hiveelpay.dal.dao.model.CustomerAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class HiveelCheckoutServiceImpl implements IHiveelCheckoutService {
    private static final MyLog _log = MyLog.getLog(HiveelCheckoutServiceImpl.class);

    @Autowired
    private CustomerAccountMapper customerAccountMapper;
    @Autowired
    private BraintreeGateway gateway;
    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;
    @Autowired
    private CreditCardMapper creditCardMapper;
    @Autowired
    private ConversionService conversionService;

    @Override
    public CustomerAccount findCustomerByUserId(String userId) {
        if (Strings.isNullOrEmpty(userId)) {
            return null;
        }
        return customerAccountMapper.findByUserId(userId);
    }

    @Transactional
    @Override
    public CustomerBindCardResult createCustomerAccountAndCardBinding(PayRequestVo payRequestVo) {
        CustomerAccount customerAccount = createCustomerAccount(payRequestVo);
        Address address = createAddress(customerAccount.getCustomerId(), customerAccount.getTargetId(), payRequestVo);
        com.hiveelpay.dal.dao.model.PaymentMethod paymentMethod = createPaymentMethod(customerAccount.getCustomerId(), customerAccount.getTargetId(), address.getAddressId(), address.getTargetId(), payRequestVo);
        return new CustomerBindCardResult(customerAccount, address, paymentMethod);
    }

    /**
     * Create Payment Method
     *
     * @param customerId
     * @param customerTargetId
     * @param addressId
     * @param addressTargetId
     * @param payRequestVo
     * @return
     */
    private com.hiveelpay.dal.dao.model.PaymentMethod createPaymentMethod(String customerId, String customerTargetId, String addressId, String addressTargetId, PayRequestVo payRequestVo) {
        checkNotNull(customerId);
        checkNotNull(customerTargetId);
        checkNotNull(addressId);
        checkNotNull(addressTargetId);

        final String token = MySeq.getPay();

        try {
            PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest()
                    .paymentMethodNonce(payRequestVo.getNonce())
                    .customerId(customerTargetId)
                    .billingAddressId(addressTargetId)
                    .token(token);

            Result<? extends PaymentMethod> paymentMethodResult = gateway.paymentMethod().create(paymentMethodRequest);
            if (paymentMethodResult.isSuccess()) {
                Object obj = paymentMethodResult.getTarget();

                com.hiveelpay.dal.dao.model.PaymentMethod paymentMethod = new com.hiveelpay.dal.dao.model.PaymentMethod();
                paymentMethod.setToken(token);
                paymentMethod.setCustomerId(customerId);
                paymentMethod.setCustomerTargetId(customerTargetId);
                paymentMethod.setImageUrl(paymentMethodResult.getTarget().getImageUrl());
                paymentMethod.setDefault(paymentMethodResult.getTarget().isDefault());

                if (obj instanceof CreditCard) {// save creditcard
                    paymentMethod.setPaymentMethodType(PaymentMethodTypeEnum.CREDITCARD);

                    CreditCard creditCard = (CreditCard) obj;
                    com.hiveelpay.dal.dao.model.CreditCard cc = conversionService.convert(creditCard, com.hiveelpay.dal.dao.model.CreditCard.class);

                    cc.setCustomerId(customerId);
                    cc.setAddressId(addressId);
                    cc.setBillingAddressId(addressTargetId);
                    creditCardMapper.save(cc);
                    paymentMethodMapper.save(paymentMethod);

                    return paymentMethod;
                }
            }

            StringBuilder sb = new StringBuilder();
            paymentMethodResult.getErrors().getAllValidationErrors().forEach(i -> {
                sb.append("code:");
                sb.append(i.getCode());
                sb.append("message:");
                sb.append(i.getMessage());
            });
            _log.error("create address result message:{},detail:{}", paymentMethodResult.getMessage(), sb.toString());
        } catch (Exception e) {
            _log.error("", e);
            throw new HiveelPayException(HiveelPayServiceErrorCode.DATA_SAVE_PAYMENT_METHOD_ERROR, e);
        }
        throw new HiveelPayException(HiveelPayErrorCode.CREATE_PAYMENT_METHOD_ERROR);
    }

    /**
     * Create user Address
     *
     * @param customerId
     * @param targetId
     * @param payRequestVo
     * @return
     */
    private Address createAddress(String customerId, String targetId, PayRequestVo payRequestVo) {
        _log.debug("HiveelCheckoutServiceImpl#createAddress(),customerId:{},targetId:{},payRequestVo:{}", customerId, targetId, payRequestVo);
        checkNotNull(customerId);
        checkNotNull(targetId);

        try {
            Address address = conversionService.convert(payRequestVo, Address.class);
            address.setCustomerId(customerId);

            List<Address> savedAddressList = addressMapper.findSaveAddress(address, AddressType.billing);
            if (savedAddressList != null && !savedAddressList.isEmpty()) {
                _log.warn("Use the saved address!");

                for (Address item : savedAddressList) {
                    try {
                        com.braintreegateway.Address addr = gateway.address().find(targetId, item.getTargetId());
                        if (addr != null) {
                            return item;
                        }
                    } catch (Exception e) {
                        _log.error("", e);
                    }
                }
            }

            AddressRequest request = new AddressRequest()
                    .firstName(address.getFirstName())
                    .lastName(address.getLastName())
                    .locality(address.getLocality())
                    .countryName(address.getCountryName())
                    .countryCodeAlpha2(address.getCountryCodeAlpha2())
                    .countryCodeAlpha3(address.getCountryCodeAlpha3());
            Result<com.braintreegateway.Address> result = gateway.address().create(targetId, request);
            if (result.isSuccess()) {
                String addrTargetId = result.getTarget().getId();
                address.setTargetId(addrTargetId);
                address.setSyncStatus(SyncStatus.SYNC_SUCCESSED);
                addressMapper.save(address);
                return address;
            }
            StringBuilder sb = new StringBuilder();
            result.getErrors().getAllValidationErrors().forEach(i -> {
                sb.append("code:");
                sb.append(i.getCode());
                sb.append("message:");
                sb.append(i.getMessage());
            });
            _log.error("create address result message:{},detail:{}", result.getMessage(), sb.toString());
        } catch (Exception e) {
            _log.error("HiveelCheckoutServiceImpl#createAddress()", e);
            throw new HiveelPayException(HiveelPayServiceErrorCode.DATA_SAVE_ADDRESS_ERROR, e);
        }
        throw new HiveelPayException(HiveelPayErrorCode.CREATE_CUSTOMER_ADDRESS_ERROR);
    }

    /**
     * CREATE CUSTOMER ACCOUNT
     *
     * @param payRequestVo
     * @return
     */
    private CustomerAccount createCustomerAccount(PayRequestVo payRequestVo) {
        _log.debug("HiveelCheckoutServiceImpl#createCustomerAccount(),payRequestVo:{}", payRequestVo);
        try {
            CustomerAccount customerAccount = new CustomerAccount();
            customerAccount.setUserId(payRequestVo.getUserId());

            final String customerId = MySeq.getCustomerId();
            customerAccount.setCustomerId(customerId);//new Customer

            customerAccount.setFirstName(payRequestVo.getFirstName().trim());
            customerAccount.setLastName(payRequestVo.getLastName().trim());
            customerAccount.setAccountType(payRequestVo.getAccountType());

            CustomerRequest request = new CustomerRequest()
                    .firstName(customerAccount.getFirstName())
                    .lastName(customerAccount.getLastName())
                    .customerId(customerAccount.getCustomerId())
                    .deviceData("WEB");

            Result<Customer> result = gateway.customer().create(request);

            if (result.isSuccess()) {//success
                String targetId = result.getTarget().getId();

                customerAccount.setTargetId(targetId);
                customerAccount.setSyncStatus(SyncStatus.SYNC_SUCCESSED);
                customerAccountMapper.save(customerAccount);
                return customerAccount;
            }

            StringBuilder sb = new StringBuilder();
            result.getErrors().getAllValidationErrors().forEach(i -> {
                sb.append("code:");
                sb.append(i.getCode());
                sb.append("message:");
                sb.append(i.getMessage());
            });
            _log.error("create address result message:{},detail:{}", result.getMessage(), sb.toString());
        } catch (Exception e) {
            _log.error("HiveelCheckoutServiceImpl#createCustomerAccount()", e);
            throw new HiveelPayException(HiveelPayServiceErrorCode.DATA_SAVE_CUSTOMER_ERROR, e);
        }
        throw new HiveelPayException(HiveelPayErrorCode.CREATE_CUSTOMER_ERROR);
    }

    @Transactional
    @Override
    public CustomerBindCardResult updateCustomerAccountAndCardBinding(CustomerAccount customerAccount, PayRequestVo payRequestVo) {
        _log.debug("HiveelCheckoutServiceImpl#updateCustomerAccountAndCardBinding(),customerAccount:{},customerAccount:{}", customerAccount, payRequestVo);
        checkNotNull(customerAccount);
        checkNotNull(payRequestVo);
        if (!Strings.isNullOrEmpty(payRequestVo.getPayMethodId())) {
            com.hiveelpay.dal.dao.model.PaymentMethod paymentMethod = paymentMethodMapper.findByToken(payRequestVo.getPayMethodId(), customerAccount.getCustomerId());
            if (paymentMethod == null) {
                Address address = createAddress(customerAccount.getCustomerId(), customerAccount.getTargetId(), payRequestVo);
                paymentMethod = createPaymentMethod(customerAccount.getCustomerId(), customerAccount.getTargetId(), address.getAddressId(), address.getTargetId(), payRequestVo);
                return new CustomerBindCardResult(customerAccount, address, paymentMethod);
            }
            switch (paymentMethod.getPaymentMethodType()) {
                case CREDITCARD:
                    com.hiveelpay.dal.dao.model.CreditCard creditCard = creditCardMapper.findByToken(paymentMethod.getToken(), customerAccount.getCustomerId());
                    if (creditCard == null) {
                        throw new HiveelPayException(HiveelPayErrorCode.PAYMENT_METHOD_NOT_SUPPORT);
                    }
                    String addressId = creditCard.getAddressId();
                    Address address = addressMapper.findAddressByAddressId(addressId);
                    if (address == null) {
                        throw new HiveelPayException(HiveelPayErrorCode.PAYMENT_METHOD_NO_ADDRESS_ERROR);
                    }
                    return new CustomerBindCardResult(customerAccount, address, paymentMethod);
                case PAYPAL://
                    break;
                default:
                    break;
            }
        }

        Address address = createAddress(customerAccount.getCustomerId(), customerAccount.getTargetId(), payRequestVo);
        com.hiveelpay.dal.dao.model.PaymentMethod paymentMethod = createPaymentMethod(customerAccount.getCustomerId(), customerAccount.getTargetId(), address.getAddressId(), address.getTargetId(), payRequestVo);
        return new CustomerBindCardResult(customerAccount, address, paymentMethod);
    }


}
