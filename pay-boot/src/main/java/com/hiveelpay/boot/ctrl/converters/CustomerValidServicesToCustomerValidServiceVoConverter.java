package com.hiveelpay.boot.ctrl.converters;

import com.hiveelpay.common.model.vo.CustomerValidServiceVo;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.dal.dao.model.CustomerValidServices;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.hiveelpay.common.util.DateUtil.FORMAT_MMddyyyyHHmm;

@Component
public class CustomerValidServicesToCustomerValidServiceVoConverter implements Converter<CustomerValidServices, CustomerValidServiceVo> {
    @Override
    public CustomerValidServiceVo convert(CustomerValidServices source) {
        CustomerValidServiceVo cvsv = new CustomerValidServiceVo();
        cvsv.setCustomerId(source.getCustomerId());
        cvsv.setServiceId(source.getServiceId());
        cvsv.setServiceName(source.getServiceName());
        cvsv.setBizOrderNo(source.getBizOrderNo());
        cvsv.setProductId(source.getProductId());
        cvsv.setServiceType(source.getServiceType().getVal());
        cvsv.setServiceStatus(source.getServiceStatus().getVal());
        cvsv.setStartTime(DateUtil.date2Str(source.getStartTime(), FORMAT_MMddyyyyHHmm));
        cvsv.setEndTime(DateUtil.date2Str(source.getEndTime(), FORMAT_MMddyyyyHHmm));
        cvsv.setCreateAt(DateUtil.date2Str(source.getCreateAt(), FORMAT_MMddyyyyHHmm));
        cvsv.setLastUpdateAt(DateUtil.date2Str(source.getLastUpdateAt(), FORMAT_MMddyyyyHHmm));
        return cvsv;
    }
}
