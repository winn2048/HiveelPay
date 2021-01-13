package com.hiveelpay.boot.ctrl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hiveelpay.boot.service.AppointmentDocService;
import com.hiveelpay.boot.service.BizOrderService;
import com.hiveelpay.boot.service.InvoiceService;
import com.hiveelpay.boot.service.MerchantService;
import com.hiveelpay.common.domain.RestAPIResult;
import com.hiveelpay.common.domain.ResultStatus;
import com.hiveelpay.common.enumm.AppointmentStatus;
import com.hiveelpay.common.enumm.BusinessTypeEnum;
import com.hiveelpay.common.enumm.TradeInTypeEnum;
import com.hiveelpay.common.model.HiveelPage;
import com.hiveelpay.common.model.requests.AppointmentSearchRequest;
import com.hiveelpay.common.model.requests.BizOrderSearchRequest;
import com.hiveelpay.common.model.requests.ISearchRequest;
import com.hiveelpay.common.model.requests.InvoiceSearchRequest;
import com.hiveelpay.common.model.vo.*;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.hiveelpay.common.domain.ResultStatus.SUCCESS;
import static com.hiveelpay.common.util.DateUtil.FORMAT_HHmm;
import static com.hiveelpay.common.util.DateUtil.FORMAT_MMddyyyy;
import static java.util.stream.Collectors.groupingBy;

/**
 * provide interface for merchant
 */
@RestController
@RequestMapping("/api/biz/merchant")
public class MerchantController extends BaseController {
    private static final MyLog _log = MyLog.getLog(MerchantController.class);

    @Autowired
    private ConversionService conversionService;
    @Autowired
    private BizOrderService bizOrderServiceImpl;
    @Autowired
    private MerchantService merchantServiceImpl;
    @Autowired
    private AppointmentDocService appointmentDocServiceImpl;
    @Autowired
    private InvoiceService invoiceServiceImpl;


    /**
     * 商家锁定营业时间（客户可预约时间）
     *
     * @return
     */
    @PostMapping("/block/time")
    public RestAPIResult<Boolean> blockAppointmentTime(MchBlockedAppointmentTimeVo mchBlockedAppointmentTimeVo) {
        checkArgument(!Strings.isNullOrEmpty(mchBlockedAppointmentTimeVo.getAccountId()), "Please set accountId");
        checkArgument(!Strings.isNullOrEmpty(mchBlockedAppointmentTimeVo.getStoreId()), "Please set storeId.");
        checkArgument(!Strings.isNullOrEmpty(mchBlockedAppointmentTimeVo.getDateStr()), "Please set date.");
        checkArgument(mchBlockedAppointmentTimeVo.getTimes() != null && mchBlockedAppointmentTimeVo.getTimes().length > 0, "Please set block time.");

        ArrayList<MchBlockedAppointmentTime> list = conversionService.convert(mchBlockedAppointmentTimeVo, ArrayList.class);
        boolean rs = merchantServiceImpl.saveBlockAppointmentTime(list);
        return new RestAPIResult<>(rs ? SUCCESS : ResultStatus.FAILED);
    }


    /**
     * 商家-check某天是否可以block
     *
     * @param dateStr
     * @param storeId
     * @return
     */
    @GetMapping(value = "/block/time/check/{dateStr}/{storeId}/{businessType}")
    public RestAPIResult<Boolean> blocktimeCheck(@PathVariable("dateStr") String dateStr,
                                                 @PathVariable("storeId") String storeId,
                                                 @PathVariable("businessType") BusinessTypeEnum businessType) {
        checkArgument(!Strings.isNullOrEmpty(storeId), "You must give me a storeId.");
        checkArgument(businessType != null && !businessType.equals(BusinessTypeEnum.ILLEGAL), "Please set a valid business type.");

        Date date = null;
        try {
            date = DateUtil.strToDate(dateStr, DateUtil.FORMAT_MMddyyyyHHmm);
        } catch (Exception ignored) {
        }
        checkArgument(date != null, "You must set dateStr format as 'MMddyyyyHHmm'");
        return new RestAPIResult<>(SUCCESS, merchantServiceImpl.canBlock(storeId, businessType, date));
    }

    /**
     * 商家-获取一天不可以被block的时段
     *
     * @param dayStr  FORMAT_MMddyyyy
     * @param storeId
     * @return
     */
    @GetMapping(value = "/can/not/block/times/{dayStr}/{storeId}/{businessType}")
    public RestAPIResult<List<String>> queryMchCanNotBlocktimes(@PathVariable("dayStr") String dayStr,
                                                                @PathVariable("storeId") String storeId,
                                                                @PathVariable("businessType") BusinessTypeEnum businessType) {
        checkArgument(!Strings.isNullOrEmpty(storeId), "You must give me a storeId.");
        checkArgument(businessType != null && !businessType.equals(BusinessTypeEnum.ILLEGAL), "Please set a valid business type.");

        Date date = null;
        try {
            date = DateUtil.strToDate(dayStr, FORMAT_MMddyyyy);
        } catch (Exception ignored) {
        }
        checkArgument(date != null, "You must set dateStr format as 'MMddyyyy'");
        List<String> blockedTimes = merchantServiceImpl.getMchCanNotBlocktimes(dayStr, storeId, businessType);
        return new RestAPIResult<>(SUCCESS, blockedTimes);
    }

    /**
     * 去除某天的block时间
     *
     * @param mchBlockedAppointmentTimeVo
     * @return
     */
    @DeleteMapping("/block/time")
    public RestAPIResult<Boolean> cleanBlockAppointmentTime(MchBlockedAppointmentTimeVo mchBlockedAppointmentTimeVo) {
        checkArgument(!Strings.isNullOrEmpty(mchBlockedAppointmentTimeVo.getStoreId()), "Please set storeId.");
        checkArgument(!Strings.isNullOrEmpty(mchBlockedAppointmentTimeVo.getDateStr()), "Please set date.");

        ArrayList<MchBlockedAppointmentTime> list = conversionService.convert(mchBlockedAppointmentTimeVo, ArrayList.class);
        boolean rs = merchantServiceImpl.cleanBlockAppointmentTime(list);
        return new RestAPIResult<>(rs ? SUCCESS : ResultStatus.FAILED);
    }

    /**
     * 读取商家不可预约时间设置
     *
     * @param dateStr
     * @param dayNum
     * @param storeId
     * @param businessType
     * @return
     */
    @GetMapping(value = {
            "/blocked/time/{mchId}/{dateStr}",
            "/blocked/time/{mchId}/{dateStr}/{storeId}",
            "/blocked/time/{mchId}/{dateStr}/{storeId}/{dayNum}",
            "/blocked/time/day/{mchId}/{dateStr}/{dayNum}",
            "/blocked/time/{mchId}/{dateStr}/{storeId}/{dayNum}/{businessType}",
            "/blocked/time/bt/{mchId}/{dateStr}/{storeId}/{businessType}",
    })
    public RestAPIResult<Set<MchBlockedAppointmentTimeVo>> listBlockedAppointmentTime(
            @PathVariable("mchId") String mchId,
            @PathVariable("dateStr") String dateStr,
            @PathVariable(value = "storeId", required = false) String storeId,
            @PathVariable(value = "dayNum", required = false) Integer dayNum,
            @PathVariable(value = "businessType", required = false) BusinessTypeEnum businessType) {
        preChack(dateStr, dayNum, storeId);
        List<MchBlockedAppointmentTime> list = merchantServiceImpl.findBlockedTimes(mchId, storeId, dateStr, dayNum, businessType);
//        List<AppointmentDoc> appointmentDocList = appointmentDocServiceImpl.findOneDayAppointments(mchId, dateStr, dayNum);
        Set<MchBlockedAppointmentTimeVo> resultList = Sets.newHashSet();
        Map<String, List<MchBlockedAppointmentTime>> map = list.stream().collect(groupingBy(i -> DateUtil.date2Str(i.getBlockedDateTime(), FORMAT_MMddyyyy)));
        map.forEach((k, v) -> resultList.addAll(merge(v)));
        return new RestAPIResult<>(SUCCESS, resultList);
    }

    /**
     * 商家分页读取 属于自己的预约单
     *
     * @param mchId
     * @param storeId
     * @param businessType
     * @param page
     * @return
     */
    @GetMapping(value = {
            "/appointments/{mchId}",
            "/appointments/{mchId}/{storeId}",
            "/appointments/{mchId}/{storeId}/{businessType}",
            "/appointments/{mchId}/{storeId}/{businessType}/{tradeInType}",
    })
    public RestAPIResult<Map<String, Object>> getMerchantAppointments(@PathVariable("mchId") String mchId,
                                                                      @PathVariable(value = "storeId", required = false) String storeId,
                                                                      @PathVariable(value = "businessType", required = false) BusinessTypeEnum businessType,
                                                                      @PathVariable(value = "tradeInType", required = false) TradeInTypeEnum tradeInType,
                                                                      HiveelPage page) {
        List<AppointmentDoc> list = appointmentDocServiceImpl.findAppointmentsForMch(mchId, storeId, businessType, tradeInType, page);
        List<AppointmentDocVo> result = Lists.newArrayList();
        if (list != null && !list.isEmpty()) {
            list.forEach(i -> result.add(conversionService.convert(i, AppointmentDocVo.class)));
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("data", result);
        map.put("page", page);
        return new RestAPIResult<>(ResultStatus.SUCCESS, map);
    }


    /**
     * 商家搜索预约单
     *
     * @param searchRequest
     * @param page
     * @return
     */
    @PostMapping("/appointment/search")
    public RestAPIResult<Map<String, Object>> searchAppointment(AppointmentSearchRequest searchRequest, HiveelPage page) {
        appointmentSearchPreCheck(searchRequest);

        List<AppointmentDoc> list = appointmentDocServiceImpl.searchAppointment(searchRequest, page);
        List<AppointmentDocVo> result = Lists.newArrayList();
        if (list != null && !list.isEmpty()) {
            list.forEach(i -> result.add(conversionService.convert(i, AppointmentDocVo.class)));
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("data", result);
        map.put("page", page);
        return new RestAPIResult<>(SUCCESS, map);
    }

    private void appointmentSearchPreCheck(AppointmentSearchRequest searchRequest) {
        checkArgument(searchRequest != null && !Strings.isNullOrEmpty(searchRequest.getMchId()), "mchId can't null!");
        checkDateParameter(searchRequest);
        searchRequest.setKeyWords(Strings.nullToEmpty(searchRequest.getKeyWords()));
    }

    /**
     * 商家查看某一个预约单详情
     *
     * @param mchId
     * @param appointmentId
     * @return
     */
    @GetMapping(value = {"/appointment/{mchId}/{appointmentId}"})
    public RestAPIResult<AppointmentDocVo> getMerchantAppointments(@PathVariable("mchId") String mchId,
                                                                   @PathVariable("appointmentId") String appointmentId) {
        AppointmentDoc appointment = appointmentDocServiceImpl.findAppointmentForMch(mchId, appointmentId);
        if (appointment != null) {
            return new RestAPIResult<>(SUCCESS, conversionService.convert(appointment, AppointmentDocVo.class));
        }
        return new RestAPIResult<>(ResultStatus.FAILED, null, "No such appointment!");
    }


    /**
     * 商家对 trade-in 报价
     *
     * @param appointmentId
     * @param price
     * @return
     */
    @PostMapping(value = {"/appointment/quote"})
    public RestAPIResult<Boolean> quote4Appointment(String appointmentId, String price) {
        if (Strings.isNullOrEmpty(appointmentId)) {
            return new RestAPIResult<>(SUCCESS, Boolean.FALSE, "Illegal parameter of appointmentId");
        }
        AppointmentDoc appointmentDoc = appointmentDocServiceImpl.findAppointmentsById(appointmentId);
        if (appointmentDoc == null) {
            return new RestAPIResult<>(SUCCESS, Boolean.FALSE, "No such appointment!");
        }
        if (!appointmentDoc.getBusinessType().equals(BusinessTypeEnum.TRADE_IN)) {
            return new RestAPIResult<>(SUCCESS, Boolean.FALSE, "This is not a trade-in appointment so you can't quote for it.");
        }
        if (Strings.isNullOrEmpty(price)) {
            return new RestAPIResult<>(SUCCESS, Boolean.FALSE, "Price can't  null!");
        }
        boolean result = appointmentDocServiceImpl.quoteAppt(appointmentId, price);

        return new RestAPIResult<>(SUCCESS, result, "quoted!");
    }

    /**
     * 取消预约
     *
     * @param appointmentId
     * @return
     */
    @GetMapping(value = {"/appointment/cancel/{appointmentId}"})
    public RestAPIResult<Boolean> cancelAppointment(@PathVariable("appointmentId") String appointmentId) {
        AppointmentDoc appointmentDoc = appointmentDocServiceImpl.findAppointmentsById(appointmentId);
        if (appointmentDoc == null) {
            return new RestAPIResult<>(SUCCESS, Boolean.FALSE, "No such appointment!");
        }

        if (appointmentDoc.getAppointmentStatus().equals(AppointmentStatus.CANCELED) || appointmentDoc.getAppointmentStatus().equals(AppointmentStatus.CANCEL_AND_REFUNDED)) {
            return new RestAPIResult<>(SUCCESS, Boolean.TRUE, "canceled");
        }

        if (appointmentDoc.getAppointmentTime().before(new Date())) {
            return new RestAPIResult<>(SUCCESS, Boolean.FALSE, "The appointment time is after current time, so you can not cancel the appointment!current_time=" +
                    DateUtil.date2Str(new Date(), DateUtil.FORMAT_MMddyyyyHHmm) +
                    ",appointmentTime:" + DateUtil.date2Str(appointmentDoc.getAppointmentTime(), DateUtil.FORMAT_MMddyyyyHHmm));
        }
        AppointmentStatus appointmentStatus = appointmentDoc.getAppointmentStatus();
        if (appointmentStatus == null) {
            return new RestAPIResult<>(SUCCESS, Boolean.FALSE, "Wrong DATA!");
        }
        if (appointmentStatus.equals(AppointmentStatus.INVALID) || appointmentStatus.equals(AppointmentStatus.SERVICE_DONE)) {
            return new RestAPIResult<>(SUCCESS, Boolean.FALSE, "You can not cancel this appointment!,status=" + appointmentDoc.getAppointmentStatus());
        }
        try {
            Boolean result = appointmentDocServiceImpl.cancelAppointment(appointmentDoc);
            return new RestAPIResult<>(SUCCESS, result, "canceled");
        } catch (Exception e) {
            _log.error("", e);
            return new RestAPIResult<>(ResultStatus.FAILED, Boolean.FALSE, "Canceled error!" + e.getMessage());
        }
    }


    private void preChack(String dateStr, Integer dayNum, String storeId) {
//        checkArgument(!Strings.isNullOrEmpty(storeId), "storeId can't null!");
        checkArgument(!Strings.isNullOrEmpty(dateStr), "Must give me a date!");

        if (dayNum != null) {
            checkArgument(dayNum > 0 && dayNum <= 31, "dayNum must in [1,31]");
        }
        if (!Strings.isNullOrEmpty(dateStr)) {
            Date date = null;
            try {
                date = DateUtil.strToDate(dateStr, DateUtil.FORMAT_MMyyyy);
            } catch (Exception ignored) {
            }
            checkArgument(date != null, "Wrong format of dateStr");
        }
    }

    private Collection<? extends MchBlockedAppointmentTimeVo> merge(List<MchBlockedAppointmentTime> v) {
        List<MchBlockedAppointmentTimeVo> list = Lists.newArrayList();
        Map<String, List<MchBlockedAppointmentTime>> map = v.stream().collect(groupingBy(MchBlockedAppointmentTime::getStoreId));
        map.forEach((k, j) -> mergeByBusinessType(list, j));
        return list;
    }

    private void mergeByBusinessType(List<MchBlockedAppointmentTimeVo> list, List<MchBlockedAppointmentTime> j) {

        Map<BusinessTypeEnum, List<MchBlockedAppointmentTime>> m1 = j.stream().collect(groupingBy(MchBlockedAppointmentTime::getBusinessType));

        for (Map.Entry<BusinessTypeEnum, List<MchBlockedAppointmentTime>> m : m1.entrySet()) {
            BusinessTypeEnum businessTypeEnum = m.getKey();
            List<MchBlockedAppointmentTime> v = m.getValue();
            Map<MchBlockedAppointmentTime.BlockedBy, List<MchBlockedAppointmentTime>> rs = v.stream().collect(groupingBy(MchBlockedAppointmentTime::getBlockedBy));

            rs.forEach((ik, iv) -> {
                MchBlockedAppointmentTime.BlockedBy blockedBy = ik;
                List<MchBlockedAppointmentTime> blockedAppointmentTimeList = iv;

                blockedAppointmentTimeList.forEach(il -> {
                    MchBlockedAppointmentTimeVo item = new MchBlockedAppointmentTimeVo();
//                MchBlockedAppointmentTime source = v.get(0);
                    item.setAccountId(il.getAccountId());
                    item.setBusinessType(businessTypeEnum);
                    item.setStoreId(il.getStoreId());
                    item.setDateStr(DateUtil.date2Str(il.getBlockedDateTime(), FORMAT_MMddyyyy));
                    item.setBlockedBy(il.getBlockedBy().name());
                    Set<String> times = Sets.newHashSet();
                    iv.forEach(i -> {
                        String timeStr = DateUtil.date2Str(i.getBlockedDateTime(), FORMAT_HHmm);
                        times.add(timeStr);
                    });
                    String[] timesArr = times.toArray(new String[0]);
                    Arrays.sort(timesArr, (a, b) -> {
                        Date ad = DateUtil.strToDate(a, FORMAT_HHmm);
                        Date bd = DateUtil.strToDate(b, FORMAT_HHmm);
                        return ad.compareTo(bd);
                    });
                    item.setTimes(timesArr);
                    list.add(item);

                });
            });
        }
    }


    /**
     * 获取Merchant的历史交易订单
     *
     * @param hiveelPage
     * @return
     */
    @GetMapping("/orders/{mchId}")
    public RestAPIResult<Map<String, Object>> list(HttpServletRequest request, HiveelPage hiveelPage,
                                                   @PathVariable(value = "mchId", required = false) String mchId) {

        List<BizOrder> list = bizOrderServiceImpl.findMerchantOrders(mchId, hiveelPage);
        Map<String, Object> mapResult = Maps.newHashMap();

        List<BizOrderVo> rsList = Lists.newArrayList();
        convertOrderHistoryResult(list, rsList);

        mapResult.put("list", rsList);
        mapResult.put("page", hiveelPage);

        return new RestAPIResult<>(SUCCESS, mapResult);
    }

    private void convertOrderHistoryResult(List<BizOrder> list, List<BizOrderVo> rsList) {
        if (!list.isEmpty()) {
            list.forEach(i -> {
                BizOrderVo bv = conversionService.convert(i, BizOrderVo.class);
                if (bv != null) {
                    List<PayOrder> payOrders = i.getPayOrders();
                    if (payOrders != null && !payOrders.isEmpty()) {
                        List<PayOrderVo> povs = Lists.newArrayList();
                        payOrders.forEach(k -> povs.add(conversionService.convert(k, PayOrderVo.class)));
                        bv.setPayOrderList(povs);
                    }

                    if (i.getPaySubscription() != null) {
                        bv.setPaySubscription(conversionService.convert(i.getPaySubscription(), PaySubscriptionVo.class));
                    }
                    rsList.add(bv);
                }
            });
        }
    }

    /**
     * 查询Merchant的历史交易订单
     *
     * @param hiveelPage
     * @return
     */
    @PostMapping("/orders/search")
    public RestAPIResult<Map<String, Object>> searchMerchantHistoryBizOrders(BizOrderSearchRequest searchRequest, HiveelPage hiveelPage) {

        bizOrderSearchPreCheck(searchRequest);

        List<BizOrder> list = bizOrderServiceImpl.searchBizOrders(searchRequest, hiveelPage);
        Map<String, Object> mapResult = Maps.newHashMap();

        List<BizOrderVo> rsList = Lists.newArrayList();
        convertOrderHistoryResult(list, rsList);

        mapResult.put("list", rsList);
        mapResult.put("page", hiveelPage);

        return new RestAPIResult<>(SUCCESS, mapResult);
    }

    /**
     * 查询发票
     *
     * @param searchRequest
     * @param page
     * @return
     */
    @PostMapping("/invoice/search")
    public RestAPIResult<Map<String, Object>> queryInvoice(InvoiceSearchRequest searchRequest, HiveelPage page) {
        checkArgument(!Strings.isNullOrEmpty(searchRequest.getMchId()), "Must set mchId.");
        List<Invoice> list = invoiceServiceImpl.searchInvoice(searchRequest, page);
        List<InvoiceVo> resultList = Lists.newArrayList();
        if (list != null && !list.isEmpty()) {
            list.forEach(i -> resultList.add(conversionService.convert(i, InvoiceVo.class)));
        }

        Map<String, Object> map = Maps.newHashMap();
        map.put("page", page);
        map.put("list", resultList);
        return new RestAPIResult<>(SUCCESS, map);
    }

    /**
     * @param invoiceId
     * @return
     */
    @GetMapping("/invoice/detail/{invoiceId}")
    public RestAPIResult<List<AppointmentDocVo>> showInvoiceDetail(@PathVariable("invoiceId") String invoiceId) {
        List<AppointmentDoc> list = appointmentDocServiceImpl.findAppointmentsByInvoiceId(invoiceId);
        List<AppointmentDocVo> resultList = Lists.newArrayList();
        if (!list.isEmpty()) {
            List<BizOrder> bizOrderList = bizOrderServiceImpl.findBizOrders(list.stream().filter(Objects::nonNull).map(AppointmentDoc::getBizOrderNo).collect(Collectors.toSet()));

            List<BizOrderVo> bizOrderVoList = Lists.newArrayList();
            convertOrderHistoryResult(bizOrderList, bizOrderVoList);

            Map<String, BizOrderVo> map = bizOrderVoList.stream().filter(Objects::nonNull).collect(Collectors.toMap(BizOrderVo::getBizOrderNo, c -> c));
            list.forEach(i -> {
                AppointmentDocVo adv = conversionService.convert(i, AppointmentDocVo.class);
                adv.setBizOrder(map.get(adv.getBizOrderNo()));
                resultList.add(adv);
            });
        }
        return new RestAPIResult<>(SUCCESS, resultList);
    }

    private void bizOrderSearchPreCheck(BizOrderSearchRequest searchRequest) {
        checkArgument(searchRequest != null && !Strings.isNullOrEmpty(searchRequest.getMchId()), "mchId can't null!");
        checkDateParameter(searchRequest);
        searchRequest.setKeyWord(Strings.nullToEmpty(searchRequest.getKeyWord()));
    }

    private void checkDateParameter(ISearchRequest searchRequest) {
        if (!Strings.isNullOrEmpty(searchRequest.getStartDateStr())) {
            Date startDate = null;
            try {
                startDate = DateUtil.strToDate(searchRequest.getStartDateStr(), FORMAT_MMddyyyy);
            } catch (Exception ignored) {
            }
            checkArgument(startDate != null, "Wrong format of startDateStr. Please format it like 'MMddyyyy'");
        } else {
            searchRequest.setStartDateStr(DateUtil.date2Str(DateUtil.addMonths(new Date(), -3), FORMAT_MMddyyyy));
        }

        if (!Strings.isNullOrEmpty(searchRequest.getEndDateStr())) {
            Date endDate = null;
            try {
                endDate = DateUtil.strToDate(searchRequest.getEndDateStr(), FORMAT_MMddyyyy);
            } catch (Exception ignored) {
            }
            checkArgument(endDate != null, "Wrong format of endDateStr. Please format it like 'MMddyyyy'.");
        } else {
            searchRequest.setEndDateStr(DateUtil.date2Str(DateUtil.addMonths(new Date(), 3), FORMAT_MMddyyyy));
        }
    }

}
