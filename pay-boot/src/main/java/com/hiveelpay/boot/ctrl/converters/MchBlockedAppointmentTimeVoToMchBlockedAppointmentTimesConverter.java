package com.hiveelpay.boot.ctrl.converters;

import com.google.common.collect.Lists;
import com.hiveelpay.boot.ctrl.exceptions.HiveelPayErrorCode;
import com.hiveelpay.common.exceptions.HiveelBizException;
import com.hiveelpay.common.model.vo.MchBlockedAppointmentTimeVo;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.common.util.HiveelID;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.model.MchBlockedAppointmentTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static com.hiveelpay.common.util.DateUtil.FORMAT_MMddyyyyHHmm;

@Component
public class MchBlockedAppointmentTimeVoToMchBlockedAppointmentTimesConverter implements Converter<MchBlockedAppointmentTimeVo, ArrayList<MchBlockedAppointmentTime>> {
    private static final MyLog _log = MyLog.getLog(BizOrderToBizOrderVoConverter.class);

    @Override
    public ArrayList<MchBlockedAppointmentTime> convert(MchBlockedAppointmentTimeVo mchBlockedAppointmentTimeVo) {
        ArrayList<MchBlockedAppointmentTime> list = Lists.newArrayList();
        for (String time : mchBlockedAppointmentTimeVo.getTimes()) {
            try {
                MchBlockedAppointmentTime t = new MchBlockedAppointmentTime();
                t.setAccountId(mchBlockedAppointmentTimeVo.getAccountId());
                t.setBlockedId(HiveelID.getInstance().getRandomId("T"));
                t.setBusinessType(mchBlockedAppointmentTimeVo.getBusinessType());
                t.setStoreId(mchBlockedAppointmentTimeVo.getStoreId());

                String dateTimeStr = mchBlockedAppointmentTimeVo.getDateStr().trim() + time.trim();
                t.setBlockedDateTime(DateUtil.strToDate(dateTimeStr, FORMAT_MMddyyyyHHmm));//MMddyyyyHHmm
                list.add(t);
            } catch (Exception e) {
                e.printStackTrace();
                _log.error("", e);
                throw new HiveelBizException(HiveelPayErrorCode.CONVERTER_ERROR, e);
            }
        }
        return list;
    }
}
