package com.hiveelpay.boot.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hiveelpay.boot.service.MerchantService;
import com.hiveelpay.common.enumm.AppointmentStatus;
import com.hiveelpay.common.enumm.BusinessTypeEnum;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.common.util.MySeq;
import com.hiveelpay.dal.dao.mapper.AppointmentDocMapper;
import com.hiveelpay.dal.dao.mapper.MchBlockedAppointmentTimeMapper;
import com.hiveelpay.dal.dao.model.AppointmentDoc;
import com.hiveelpay.dal.dao.model.MchBlockedAppointmentTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author wangqiang
 */
@Service
public class MerchantServiceImpl implements MerchantService {
    private static final MyLog _log = MyLog.getLog(MerchantServiceImpl.class);

    @Autowired
    private MchBlockedAppointmentTimeMapper mchBlockedAppointmentTimeMapper;

    @Autowired
    private AppointmentDocMapper appointmentDocMapper;

    @Transactional
    @Override
    public boolean saveBlockAppointmentTime(ArrayList<MchBlockedAppointmentTime> list) {
        MchBlockedAppointmentTime item0 = list.get(0);
        String dateStr = DateUtil.date2Str(item0.getBlockedDateTime(), DateUtil.FORMAT_MMddyyyy);
        mchBlockedAppointmentTimeMapper.deleteByDate(item0.getAccountId(), item0.getStoreId(), dateStr);
        mchBlockedAppointmentTimeMapper.saveMore(list);
        return true;
    }

    @Override
    public List<MchBlockedAppointmentTime> findBlockedTimes(String mchId, String storeId, String dateStr, Integer dayNum, BusinessTypeEnum businessType) {
        String dayStr = DateUtil.formatDayNumToDayStr(dayNum);

        final List<MchBlockedAppointmentTime> list = Lists.newArrayList();
        List<MchBlockedAppointmentTime> rs = mchBlockedAppointmentTimeMapper.findBlockedTimes(mchId, storeId, dateStr, dayStr, businessType);
        if (rs != null && !rs.isEmpty()) {
            rs.forEach(i -> i.setBlockedBy(MchBlockedAppointmentTime.BlockedBy.MERCHANT));
            list.addAll(rs);
        }

        Set<AppointmentStatus> statusSet = Sets.newHashSet();
        statusSet.add(AppointmentStatus.VALID);
        List<AppointmentDoc> appointmentDocList = appointmentDocMapper.findOneDayAppointments(mchId, storeId, dateStr, dayStr, statusSet);
        if (appointmentDocList == null || appointmentDocList.isEmpty()) {
            return list;
        }
        if (businessType != null) {
            appointmentDocList = appointmentDocList.stream().filter(i -> i.getBusinessType().equals(businessType)).collect(Collectors.toList());
        }

        appointmentDocList.forEach(i -> {
            final BusinessTypeEnum type = i.getBusinessType();
            if (type.equals(BusinessTypeEnum.PRE_INSPECTION) || type.equals(BusinessTypeEnum.OIL_CHANGE)) {
                Date appointmentTime = i.getAppointmentTime();
                Calendar cc = Calendar.getInstance();
                cc.setTime(appointmentTime);
                cc.add(Calendar.MINUTE,-90);
                for (int k = 0; k < 7; k++) {
                    MchBlockedAppointmentTime mba = new MchBlockedAppointmentTime();
                    mba.setAccountId(mchId);
                    mba.setBlockedId(MySeq.getTimeBlockId());
                    mba.setBlockedBy(MchBlockedAppointmentTime.BlockedBy.CUSTOMER);
                    Calendar c = Calendar.getInstance();
                    c.setTime(cc.getTime());
                    c.add(Calendar.MINUTE, k * 30);
                    mba.setBlockedDateTime(c.getTime());
                    mba.setStoreId(i.getToStoreId());
                    mba.setBusinessType(type);
                    list.add(mba);
                }
            }
        });
        return list;
    }

    @Transactional
    @Override
    public boolean cleanBlockAppointmentTime(ArrayList<MchBlockedAppointmentTime> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        try {
            list.forEach(i -> mchBlockedAppointmentTimeMapper.deleteBlockedDate(i));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            _log.error("", e);
        }
        return false;
    }

    @Override
    public boolean isBlocked(String storeId, BusinessTypeEnum businessType, Date date) {
        checkNotNull(storeId);
        checkNotNull(businessType);
        checkNotNull(date);

        String dateStr = DateUtil.date2Str(date, DateUtil.FORMAT_MMddyyyyHHmm);
        MchBlockedAppointmentTime blockedTime = mchBlockedAppointmentTimeMapper.findBlockedTime(storeId, dateStr, businessType);
        return blockedTime != null;
    }

    @Override
    public boolean isBlocked(String storeId, BusinessTypeEnum businessType, String dateStr) {
        checkNotNull(storeId);
        checkNotNull(businessType);
        checkNotNull(dateStr);

        MchBlockedAppointmentTime blockedTime = mchBlockedAppointmentTimeMapper.findBlockedTime(storeId, dateStr, businessType);
        return blockedTime != null;
    }

    @Override
    public Boolean canBlock(String storeId, BusinessTypeEnum businessType, Date date) {
        checkNotNull(storeId);
        checkNotNull(businessType);
        checkNotNull(date);
        String dateStr = DateUtil.date2Str(date, DateUtil.FORMAT_MMddyyyyHHmm);

        Set<AppointmentStatus> statusSet = Sets.newHashSet();
        statusSet.add(AppointmentStatus.VALID);

        int countAppoinements = appointmentDocMapper.countAppointmentsWithDateStr(storeId, businessType, dateStr, statusSet);
        if (countAppoinements > 0) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public List<String> getMchCanNotBlocktimes(String dayStr, String storeId, BusinessTypeEnum businessType) {
        Set<AppointmentStatus> statusSet = Sets.newHashSet();
        statusSet.add(AppointmentStatus.VALID);

        List<AppointmentDoc> list = appointmentDocMapper.findHaveAppointmentTimes(dayStr, storeId, businessType, statusSet);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> rs = Sets.newHashSet();
        if (businessType.equals(BusinessTypeEnum.OIL_CHANGE) || businessType.equals(BusinessTypeEnum.PRE_INSPECTION)) {
            list.forEach(i -> {
                Date appointmentTime = i.getAppointmentTime();
                for (int k = 0; k < 4; k++) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(appointmentTime);
                    c.add(Calendar.MINUTE, k * 30);
                    rs.add(DateUtil.date2Str(c.getTime(), DateUtil.FORMAT_HHmm));
                }
            });
        }
        List<String> result = Lists.newArrayList(rs);
        result.sort((a, b) -> {
            Date ad = DateUtil.strToDate(a, DateUtil.FORMAT_HHmm);
            Date bd = DateUtil.strToDate(b, DateUtil.FORMAT_HHmm);
            return ad.compareTo(bd);
        });
        return result;
    }
}
