package com.hiveelpay.boot.ctrl.converters;

import com.google.common.collect.Lists;
import com.hiveelpay.common.model.vo.BizCarVo;
import com.hiveelpay.common.model.vo.BizOrderVo;
import com.hiveelpay.common.model.vo.ServiceTimeVo;
import com.hiveelpay.common.util.AmountUtil;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.model.BizCar;
import com.hiveelpay.dal.dao.model.BizOrder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.hiveelpay.common.util.DateUtil.FORMAT_MMddyyyyHHmm;

@Component
public class BizOrderToBizOrderVoConverter implements Converter<BizOrder, BizOrderVo> {
    private static final MyLog _log = MyLog.getLog(BizOrderToBizOrderVoConverter.class);

    @Override
    public BizOrderVo convert(BizOrder source) {
        BizOrderVo rs = new BizOrderVo();

        rs.setCustomerId(source.getCustomerId());
        rs.setBizOrderNo(source.getBizOrderNo());
        rs.setProductId(source.getProductId());
        rs.setProductName(source.getProductName());
        rs.setAmount(AmountUtil.convertCent2Dollar(String.valueOf(source.getAmount())));
        rs.setProductType(source.getProductType());
        rs.setServiceLength(source.getServiceLength());
        rs.setServiceLengthUnit(source.getServiceLengthUnit().getVal());
        rs.setOrderStatus(source.getOrderStatus().name());
        rs.setFirstBillDate(DateUtil.date2Str(source.getFirstBillDate(), FORMAT_MMddyyyyHHmm));

        if (source.getServiceTimes() != null && !source.getServiceTimes().isEmpty()) {
            List<ServiceTimeVo> list = Lists.newArrayList();
            source.getServiceTimes().forEach(i -> {
                ServiceTimeVo stv = new ServiceTimeVo();
                stv.setBizOrderNo(i.getBizOrderNo());
                stv.setServiceStartTime(DateUtil.date2Str(i.getServiceStartTime(), FORMAT_MMddyyyyHHmm));
                stv.setServiceEndTime(DateUtil.date2Str(i.getServiceEndTime(), FORMAT_MMddyyyyHHmm));
                list.add(stv);
            });
            rs.setServiceTimeList(list);
        }
        rs.setPayAmount(AmountUtil.convertCent2Dollar(String.valueOf(source.getPayAmount())));
        rs.setPaySuccessTime(DateUtil.date2Str(source.getPaySuccessTime(), FORMAT_MMddyyyyHHmm));
        rs.setMchId(source.getMchId());
        rs.setChannelId(source.getChannelId());
        rs.setCreateAt(DateUtil.date2Str(source.getCreateAt(), FORMAT_MMddyyyyHHmm));
        rs.setLastUpdateAt(DateUtil.date2Str(source.getLastUpdateAt(), FORMAT_MMddyyyyHHmm));
        rs.setCommissionAmount(AmountUtil.convertCent2Dollar(String.valueOf(source.getCommissionAmount())));

        BizCar bc = source.getBizCar();
        if (bc != null) {
            BizCarVo bcv = new BizCarVo();
            bcv.setBizOrderNo(bc.getBizOrderNo());
            bcv.setCarId(bc.getCarId());
            bcv.setZipcode(bc.getZipcode());

            rs.setBizCar(bcv);
        }

        return rs;
    }
}
