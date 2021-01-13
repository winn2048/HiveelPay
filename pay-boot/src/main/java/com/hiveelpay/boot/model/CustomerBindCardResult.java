package com.hiveelpay.boot.model;

import com.hiveelpay.dal.dao.model.Address;
import com.hiveelpay.dal.dao.model.CustomerAccount;
import com.hiveelpay.dal.dao.model.PaymentMethod;

import java.io.Serializable;

public class CustomerBindCardResult implements Serializable {
    private CustomerAccount customerAccount;
    private PaymentMethod paymentMethod;
    private Address address;

    public CustomerBindCardResult(CustomerAccount customerAccount, Address address, PaymentMethod paymentMethod) {
        this.customerAccount = customerAccount;
        this.paymentMethod = paymentMethod;
        this.address = address;
    }

    public CustomerAccount getCustomerAccount() {
        return customerAccount;
    }

    public Address getAddress() {
        return address;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    @Override
    public String toString() {
        return "CustomerBindCardResult{" +
                "customerAccount=" + customerAccount +
                ", address=" + address +
                ", paymentMethod=" + paymentMethod +
                '}';
    }
}
