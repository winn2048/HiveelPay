package com.hiveelpay.boot.ctrl.converters;

import com.google.common.base.Strings;
import com.hiveelpay.common.model.vo.AppointmentDocVo;
import com.hiveelpay.common.util.AmountUtil;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.dal.dao.model.AppointmentDoc;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.hiveelpay.common.util.DateUtil.FORMAT_MMddyyyyHHmm;

@Component
public class AppointmentDocToAppointmentDocVoConverter implements Converter<AppointmentDoc, AppointmentDocVo> {
    @Override
    public AppointmentDocVo convert(AppointmentDoc source) {
        AppointmentDocVo adv = new AppointmentDocVo();
        adv.setCustomerId(source.getCustomerId());
        adv.setBizOrderNo(source.getBizOrderNo());
        adv.setAppointmentId(source.getAppointmentId());
        adv.setToMchId(source.getToMchId());
        adv.setToStoreId(source.getToStoreId());
        adv.setFirstName(source.getFirstName());
        adv.setLastName(source.getLastName());
        adv.setBusinessType(source.getBusinessType());
        adv.setAppointmentDateTimeStr(DateUtil.date2Str(source.getAppointmentTime(), FORMAT_MMddyyyyHHmm));
        adv.setAppointmentStatus(source.getAppointmentStatus().name());
        adv.setEmail(source.getEmail());
        adv.setPhoneNumber(source.getPhoneNumber());
        adv.setVin(source.getVin());
        adv.setPlateState(source.getPlateState());
        adv.setYear(source.getYear());
        adv.setMake(source.getMake());
        adv.setModel(source.getModel());
        adv.setMileage(source.getMileage());
        if (!Strings.isNullOrEmpty(source.getPreviousOfferPrice())) {
            adv.setPreviousOfferPrice(AmountUtil.convertCent2Dollar(source.getPreviousOfferPrice()));
        }
        adv.setTitleStatus(source.getTitleStatus());
        adv.setCreateAt(DateUtil.date2Str(source.getCreateAt(), FORMAT_MMddyyyyHHmm));
        adv.setLastUpdateAt(DateUtil.date2Str(source.getLastUpdateAt(), FORMAT_MMddyyyyHHmm));
        adv.setWhereCarAndKey(source.getWhereCarAndKey());
        adv.setCompanyName(source.getCompanyName());
        adv.setRemark(source.getRemark());
        adv.setStreet(source.getStreet());
        adv.setApt(source.getApt());
        adv.setCity(source.getCity());
        adv.setState(source.getState());
        adv.setZipcode(source.getZipcode());
        if (source.getEngineType() != null) {
            adv.setEngineType(source.getEngineType().name());
        }
        adv.setCarMaxImgUrl(Strings.nullToEmpty(source.getCarMaxImgUrl()));
        if (source.getQuotePrice() != null) {
            adv.setQuotePrice(AmountUtil.convertCent2Dollar(String.valueOf(source.getQuotePrice())));
        }
        adv.setAppointmentType(source.getAppointmentType().name());
        return adv;
    }
}
