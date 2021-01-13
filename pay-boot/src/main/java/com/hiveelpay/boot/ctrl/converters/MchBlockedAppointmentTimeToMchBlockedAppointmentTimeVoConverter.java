package com.hiveelpay.boot.ctrl.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.hiveelpay.common.model.vo.MchBlockedAppointmentTimeVo;
import com.hiveelpay.dal.dao.model.MchBlockedAppointmentTime;

@Component
public class MchBlockedAppointmentTimeToMchBlockedAppointmentTimeVoConverter implements Converter<MchBlockedAppointmentTime, MchBlockedAppointmentTimeVo> {
    @Override
    public MchBlockedAppointmentTimeVo convert(MchBlockedAppointmentTime source) {
        MchBlockedAppointmentTimeVo mat = new MchBlockedAppointmentTimeVo();
        mat.setAccountId(source.getAccountId());
        mat.setBusinessType(source.getBusinessType());
        return mat;
    }
}
