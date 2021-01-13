package com.hiveelpay.boot.ctrl.converters;

import com.google.common.base.Strings;
import com.hiveelpay.common.enumm.AppointmentStatus;
import com.hiveelpay.common.enumm.AppointmentType;
import com.hiveelpay.common.enumm.EngineType;
import com.hiveelpay.common.model.vo.AppointmentDocVo;
import com.hiveelpay.common.util.AmountUtil;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.model.AppointmentDoc;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.hiveelpay.common.util.DateUtil.FORMAT_MMddyyyyHHmm;

@Component
public class AppointmentDocVoToAppointmentDocConverter implements Converter<AppointmentDocVo, AppointmentDoc> {
    private static final MyLog _log = MyLog.getLog(AppointmentDocVoToAppointmentDocConverter.class);

    @Override
    public AppointmentDoc convert(AppointmentDocVo source) {
        AppointmentDoc d = new AppointmentDoc();
        if (!Strings.isNullOrEmpty(source.getAppointmentId())) {
            d.setAppointmentId(source.getAppointmentId());
        }
        d.setToMchId(source.getToMchId());
        d.setToStoreId(source.getToStoreId());
        d.setFirstName(source.getFirstName());
        d.setLastName(source.getLastName());

        try {
            d.setAppointmentStatus(AppointmentStatus.valueOf(source.getAppointmentStatus()));
        } catch (Exception ignore) {
        }
        d.setAppointmentTime(DateUtil.strToDate(source.getAppointmentDateTimeStr(), FORMAT_MMddyyyyHHmm));
        d.setEmail(source.getEmail());
        d.setPhoneNumber(source.getPhoneNumber());
        d.setVin(source.getVin());
        d.setPlateState(source.getPlateState());
        d.setYear(source.getYear());
        d.setMake(source.getMake());
        d.setModel(source.getModel());
        if (!Strings.isNullOrEmpty(source.getPreviousOfferPrice())) {
            d.setPreviousOfferPrice(AmountUtil.convertDollar2Cent(source.getPreviousOfferPrice()));
        }
        if (!Strings.isNullOrEmpty(source.getEngineType())) {
            d.setEngineType(EngineType.valueOf(source.getEngineType()));
        }
        d.setTitleStatus(source.getTitleStatus());
        d.setBusinessType(source.getBusinessType());
        d.setCustomerId(source.getCustomerId());
        d.setBizOrderNo(source.getBizOrderNo());
        d.setRemark(source.getRemark());
        d.setWhereCarAndKey(Strings.emptyToNull(source.getWhereCarAndKey()));
        d.setCompanyName(Strings.emptyToNull(source.getCompanyName()));
        d.setStreet(source.getStreet());
        d.setApt(source.getApt());
        d.setCity(source.getCity());
        d.setState(source.getState());
        d.setZipcode(source.getZipcode());
        d.setRemark(Strings.emptyToNull(source.getRemark()));
        d.setMileage(source.getMileage());
        d.setCarMaxImgUrl(Strings.emptyToNull(source.getCarMaxImgUrl()));
        if (!Strings.isNullOrEmpty(source.getQuotePrice())) {
            d.setQuotePrice(Long.valueOf(AmountUtil.convertDollar2Cent(source.getQuotePrice())));
        }

        if(!Strings.isNullOrEmpty(source.getAppointmentType())){
            d.setAppointmentType(AppointmentType.valueOf(source.getAppointmentType()));
        }else {
            d.setAppointmentType(AppointmentType.REGULAR);
        }

        return d;
    }
}
