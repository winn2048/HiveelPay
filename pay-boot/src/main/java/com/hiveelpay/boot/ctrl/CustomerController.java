package com.hiveelpay.boot.ctrl;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hiveelpay.boot.service.*;
import com.hiveelpay.boot.service.events.NotificationEvent;
import com.hiveelpay.boot.service.events.TradeInSkipPaymentEvent;
import com.hiveelpay.common.domain.RestAPIResult;
import com.hiveelpay.common.enumm.AppointmentType;
import com.hiveelpay.common.enumm.BusinessTypeEnum;
import com.hiveelpay.common.enumm.CustomerServiceStatusEnum;
import com.hiveelpay.common.enumm.PayProductTypeEnum;
import com.hiveelpay.common.exceptions.HiveelPayException;
import com.hiveelpay.common.model.HiveelPage;
import com.hiveelpay.common.model.vo.*;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.common.util.MySeq;
import com.hiveelpay.dal.dao.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.hiveelpay.common.domain.ResultStatus.FAILED;
import static com.hiveelpay.common.domain.ResultStatus.SUCCESS;
import static com.hiveelpay.common.util.DateUtil.FORMAT_MMddyyyy;
import static com.hiveelpay.common.util.DateUtil.FORMAT_MMddyyyyHHmm;
import static java.util.stream.Collectors.groupingBy;

/**
 *
 */
@RestController
@RequestMapping("/api/biz/customer")
public class CustomerController extends BaseController {
    private static final MyLog _log = MyLog.getLog(CustomerController.class);
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private CustomerService customerServiceImpl;
    @Autowired
    private CustomerValidServiceService customerValidServiceServiceImpl;
    @Autowired
    private AppointmentDocService appointmentDocServiceImpl;

    @Autowired
    private MerchantService merchantServiceImpl;

    @Autowired
    private BizOrderService bizOrderServiceImpl;

    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * 保存客户的预约申请
     *
     * @param appointmentDocVo
     * @return
     */
    @PostMapping("/appointment")
    public RestAPIResult<Map<String, Object>> saveAppointment(AppointmentDocVo appointmentDocVo) {
        _log.debug("appointment#save:{}", appointmentDocVo);
        preCheckAppointment(appointmentDocVo);
        AppointmentDoc appointmentDoc = conversionService.convert(appointmentDocVo, AppointmentDoc.class);

        final String appointmentId = MySeq.getAppointmentId();
        appointmentDoc.setAppointmentId(appointmentId);
        boolean rs = appointmentDocServiceImpl.save(appointmentDoc);
        if (rs) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("appointmentId", appointmentId);

            // trade-in 发起补单动作
            if (appointmentDoc.getBusinessType().equals(BusinessTypeEnum.TRADE_IN)
                    && appointmentDoc.getAppointmentType().equals(AppointmentType.REGULAR)) {//正常网站流程预约，无需支付，直接补上其他数据
                publisher.publishEvent(new TradeInSkipPaymentEvent(appointmentDoc));
            }

            if(appointmentDoc.getBusinessType().equals(BusinessTypeEnum.TRADE_IN) &&
            appointmentDoc.getAppointmentType().equals(AppointmentType.INSTANT)){// 网站上 快速预约的 trade-in 需要短信通知管理员去审核一下
                publisher.publishEvent(new NotificationEvent(NotificationEvent.ActionPoint.NEW_APPOINTMENT_NEED_ADMIN_TO_CHECK, NotificationEvent.NoticeChannel.SMS,appointmentId));
            }
            return new RestAPIResult<>(SUCCESS, map);
        }
        return new RestAPIResult<>(FAILED);
    }

    /**
     * 更新预约单接口
     *
     * @param appointmentDocVo
     * @return
     */
    @PostMapping("/appointment/update")
    public RestAPIResult<Boolean> updateAppointment(AppointmentDocVo appointmentDocVo) {
        checkArgument(!isNullOrEmpty(appointmentDocVo.getAppointmentId()), "Please set appointmentId");
        preCheckAppointmentForUpdate(appointmentDocVo);
        AppointmentDoc appointmentDoc = conversionService.convert(appointmentDocVo, AppointmentDoc.class);
        boolean rs = appointmentDocServiceImpl.update(appointmentDoc);
        return new RestAPIResult<>(SUCCESS, rs);
    }

    /**
     * 分页读取自己的预约申请
     *
     * @param userId
     * @param page
     * @return
     */
    @GetMapping("/appointment/{userId}")
    public RestAPIResult<Map<String, Object>> myAppointments(@PathVariable("userId") String userId, HiveelPage page) {
        List<AppointmentDocVo> result = Lists.newArrayList();
        if (page == null) {
            page = new HiveelPage();
        }
        List<AppointmentDoc> list = appointmentDocServiceImpl.findUserAppointments(userId, null, page);
        if (list != null && !list.isEmpty()) {
            list.forEach(i -> result.add(conversionService.convert(i, AppointmentDocVo.class)));
        }

        Map<String, Object> map = Maps.newHashMap();
        map.put("page", page);
        map.put("data", result);
        return new RestAPIResult<>(SUCCESS, map);
    }

    /**
     * @param userId
     * @param appointmentId
     * @return
     */
    @GetMapping("/appointment/{userId}/{appointmentId}")
    public RestAPIResult<AppointmentDocVo> myAppointments(@PathVariable("userId") String userId,
                                                          @PathVariable("appointmentId") String appointmentId) {
        AppointmentDoc appointment = appointmentDocServiceImpl.findUserAppointment(userId, appointmentId);
        AppointmentDocVo adv = conversionService.convert(appointment, AppointmentDocVo.class);
        return new RestAPIResult<>(SUCCESS, adv);
    }

    private void preCheckAppointment(AppointmentDocVo appointmentDocVo) {
        if(Strings.isNullOrEmpty(appointmentDocVo.getAppointmentType())){
            appointmentDocVo.setAppointmentType(AppointmentType.REGULAR.name());
        }
//        checkArgument(!isNullOrEmpty(appointmentDocVo.getAppointmentType()), "Please set appointment type in one of the value[REGULAR,INSTANT]");

        checkArgument(!isNullOrEmpty(appointmentDocVo.getFirstName()), "Must fill firstName;");
        checkArgument(!isNullOrEmpty(appointmentDocVo.getLastName()), "Must fill lastName;");
        checkArgument(!isNullOrEmpty(appointmentDocVo.getPhoneNumber()), "Must fill phoneNumber;");
        checkArgument(!isNullOrEmpty(appointmentDocVo.getEmail()), "Must fill email;");
//        checkArgument(!Strings.isNullOrEmpty(appointmentDocVo.getVin()), "Must have Vin number!");
        checkArgument(!isNullOrEmpty(appointmentDocVo.getToMchId()), "must set mchId");

        checkArgument(!isNullOrEmpty(appointmentDocVo.getToStoreId()), "must set storeId");
        if (appointmentDocVo.getMileage() != null) {
            checkArgument(appointmentDocVo.getMileage() > 0 && appointmentDocVo.getMileage() < 800000, "Mileage must be >0 and <800000.");
        }
        checkArgument(!isNullOrEmpty(appointmentDocVo.getAppointmentDateTimeStr()), "Must have appointment time!");
        checkArgument(appointmentDocVo.getBusinessType() != null && !appointmentDocVo.getBusinessType().equals(BusinessTypeEnum.ILLEGAL), "Illegal business type!");
        Date date = null;
        try {
            date = DateUtil.strToDate(appointmentDocVo.getAppointmentDateTimeStr(), FORMAT_MMddyyyyHHmm);
        } catch (Exception ignored) {
        }
        checkArgument(!merchantServiceImpl.isBlocked(appointmentDocVo.getToStoreId(), appointmentDocVo.getBusinessType(), date), "appointment time is blocked, please change another appointment time.");

    }

    private void preCheckAppointmentForUpdate(AppointmentDocVo appointmentDocVo) {
        checkArgument(!isNullOrEmpty(appointmentDocVo.getFirstName()), "Must fill firstName;");
        checkArgument(!isNullOrEmpty(appointmentDocVo.getLastName()), "Must fill lastName;");
        checkArgument(!isNullOrEmpty(appointmentDocVo.getPhoneNumber()), "Must fill phoneNumber;");
        checkArgument(!isNullOrEmpty(appointmentDocVo.getEmail()), "Must fill email;");

        if (appointmentDocVo.getMileage() != null) {
            checkArgument(appointmentDocVo.getMileage() > 0 && appointmentDocVo.getMileage() < 800000, "Mileage must >0 and <800000.");
        }
        checkArgument(!isNullOrEmpty(appointmentDocVo.getAppointmentDateTimeStr()), "Must have appointment time!");
        checkArgument(appointmentDocVo.getBusinessType() != null && !appointmentDocVo.getBusinessType().equals(BusinessTypeEnum.ILLEGAL), "Illegal business type!");
        checkArgument(!isNullOrEmpty(appointmentDocVo.getAppointmentStatus()), "Illegal appointmentStatus!");

        Date date = null;
        try {
            date = DateUtil.strToDate(appointmentDocVo.getAppointmentDateTimeStr(), FORMAT_MMddyyyyHHmm);
        } catch (Exception ignored) {
        }
        checkArgument(date != null && date.after(new Date()), "appointment date must after right now");

        if (!isNullOrEmpty(appointmentDocVo.getZipcode())) {
            checkArgument(appointmentDocVo.getZipcode().length() <= 6, "zipcode length <=6");
        }
    }

    @GetMapping(value = {
            "/info/{userId}",
            "/info/{userId}/{serviceType}"
    })
    public RestAPIResult<CustomerAccountVo> customerInfo(HttpServletRequest request,
                                                         @PathVariable(value = "userId") String userId,
                                                         @PathVariable(value = "serviceType", required = false) PayProductTypeEnum serviceType) {
        CustomerAccount customerAccount = customerServiceImpl.findCustomerByUserId(userId);
        final String customerId = customerAccount.getCustomerId();
        CustomerAccountVo customerAccountVo = conversionService.convert(customerAccount, CustomerAccountVo.class);

        List<PaymentMethod> paymentMethodList = customerServiceImpl.findPaymentMethods(customerId);
        List<PaymentMethodVo> list = Lists.newArrayList();
        paymentMethodList.forEach(i -> list.add(conversionService.convert(i, PaymentMethodVo.class)));
        customerAccountVo.setPaymentMethodList(list);

        List<CustomerValidServiceVo> validServiceVoList = Lists.newArrayList();
        initValidServiceList(customerId, validServiceVoList, serviceType);
        customerAccountVo.setValidServiceList(validServiceVoList);
        return new RestAPIResult<>(SUCCESS, customerAccountVo);
    }

    /**
     * @param kind   产品类别
     * @param userId
     * @return
     */
    @GetMapping(value = "/{kind}/{userId}")
    public RestAPIResult<List<CustomerValidServiceVo>> membershipInfo(@PathVariable("kind") Integer kind,
                                                                      @PathVariable("userId") String userId) {
        PayProductTypeEnum payProductType = PayProductTypeEnum.byValue(kind);
        if (payProductType.equals(PayProductTypeEnum.ILLEGAL)) {
            return new RestAPIResult<>(FAILED, Collections.emptyList(), "Wrong Kind");
        }

        CustomerAccount customerAccount = customerServiceImpl.findCustomerByUserId(userId);
        final String customerId = customerAccount.getCustomerId();
        try {
            List<CustomerValidServices> cvss = customerValidServiceServiceImpl.findByCustomerIdIncludeProduct(customerId, payProductType, null);
            if (!cvss.isEmpty()) {
                List<CustomerValidServiceVo> result = Lists.newArrayList();
                cvss.forEach(cvs -> {
                    CustomerValidServiceVo cvsv = conversionService.convert(cvs, CustomerValidServiceVo.class);
                    fillPayProduct(cvs, cvsv);
                    result.add(cvsv);
                });
                return new RestAPIResult<>(SUCCESS, result);
            }
            return new RestAPIResult<>(SUCCESS, null);
        } catch (HiveelPayException ex) {
            _log.error("", ex);
            return new RestAPIResult<>(FAILED, null, ex.getMsg());
        } catch (Exception e) {
            _log.error("", e);
            throw e;
        }
    }

    private void fillPayProduct(CustomerValidServices cvs, CustomerValidServiceVo cvsv) {
        if (cvs != null && cvs.getPayProduct() != null) {
            cvsv.setProduct(conversionService.convert(cvs.getPayProduct(), ProductVo.class));
        }
    }


    /**
     * @param userId
     * @return
     */
    @GetMapping(value = {
            "/current/membership/{userId}"
    })
    public RestAPIResult<CustomerValidServiceVo> currentMembershipInfo(@PathVariable("userId") String userId) {
        CustomerAccount customerAccount = customerServiceImpl.findCustomerByUserId(userId);
        if (customerAccount == null) {
            return new RestAPIResult<>(FAILED, null, "No such customer!");
        }
        final String customerId = customerAccount.getCustomerId();
        try {
            CustomerValidServices cvs = customerValidServiceServiceImpl.findCurrentMemberShipService(customerId);
            CustomerValidServiceVo cvsv = conversionService.convert(cvs, CustomerValidServiceVo.class);
            fillPayProduct(cvs, cvsv);
            return new RestAPIResult<>(SUCCESS, cvsv);
        } catch (HiveelPayException ex) {
            _log.error("", ex);
            return new RestAPIResult<>(FAILED, null, ex.getMsg());
        } catch (Exception e) {
            _log.error("", e);
            throw e;
        }
    }

    private void initValidServiceList(String customerId, List<CustomerValidServiceVo> validServiceVoList, PayProductTypeEnum serviceType) {
        List<CustomerValidServices> validServices = customerServiceImpl.findValidServices(customerId);
        if (!validServices.isEmpty()) {
            if (serviceType == null) {
                validServices.stream().filter(i -> i != null
                        && (i.getServiceStatus().equals(CustomerServiceStatusEnum.IN_SERVICE) || i.getServiceStatus().equals(CustomerServiceStatusEnum.INIT)))
                        .forEach(i -> validServiceVoList.add(conversionService.convert(i, CustomerValidServiceVo.class)));
            } else {
                validServices.stream().filter(i -> i != null && i.getServiceType().equals(serviceType) && (i.getServiceStatus().equals(CustomerServiceStatusEnum.IN_SERVICE) || i.getServiceStatus().equals(CustomerServiceStatusEnum.INIT)))
                        .forEach(i -> validServiceVoList.add(conversionService.convert(i, CustomerValidServiceVo.class)));
            }
        }
    }

    /**
     * @param request
     * @param userId
     * @return
     */
    @GetMapping(value = {
            "/services/{userId}",
            "/services/{userId}/{serviceType}"
    })
    public RestAPIResult<List<CustomerValidServiceVo>> customerValidService(HttpServletRequest request,
                                                                            @PathVariable(value = "userId") String userId,
                                                                            @PathVariable(value = "serviceType", required = false) PayProductTypeEnum serviceType) {
        CustomerAccount customerAccount = customerServiceImpl.findCustomerByUserId(userId);
        final String customerId = customerAccount.getCustomerId();

        List<CustomerValidServiceVo> validServiceVoList = Lists.newArrayList();
        initValidServiceList(customerId, validServiceVoList, serviceType);
        return new RestAPIResult<>(SUCCESS, validServiceVoList);
    }


    /**
     * @param productId
     * @param userId
     * @return
     */
    @GetMapping(value = "/buy/check/{productId}/{userId}")
    public RestAPIResult<Boolean> buyCheck(@PathVariable("productId") String productId,
                                           @PathVariable("userId") String userId) {
        return new RestAPIResult<>(SUCCESS, customerServiceImpl.checkUserBuy(productId, userId));
    }

    /**
     * @param zipcode
     * @return
     */
    @GetMapping(value = "/buy/check/count/{zipcode}")
    public RestAPIResult<Map<String, Map<String, Integer>>> buyCheckService(@PathVariable(value = "zipcode") String zipcode) {
        Map<String, Map<String, Integer>> rs = Maps.newHashMap();
        List<BizOrder> bizOrderList = bizOrderServiceImpl.queryValidBizOrders(Collections.singletonList(PayProductTypeEnum.CAR_OF_DAY.name()), zipcode);
        if (bizOrderList == null || bizOrderList.isEmpty()) {
            Map<String, Integer> map = Maps.newTreeMap();
            for (int i = 0; i < 10; i++) {
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_YEAR, i);
                map.put(DateUtil.date2Str(c.getTime(), FORMAT_MMddyyyy), 5);
            }
            rs.put(PayProductTypeEnum.CAR_OF_DAY.name(), map);
            return new RestAPIResult<>(SUCCESS, rs);
        }


        Calendar c = Calendar.getInstance();
        c.setTime(DateUtil.strToDate(DateUtil.date2Str(new Date(), FORMAT_MMddyyyy), FORMAT_MMddyyyy));

        final Date today = c.getTime();

        c.add(Calendar.DAY_OF_YEAR, 10);
        final Date endDay = c.getTime();

        Map<String, List<BizOrder>> map = bizOrderList.stream().filter(i -> {
            if (i == null) {
                return false;
            }
            for (ServiceTime d : i.getServiceTimes()) {
                if (d.getServiceStartTime().getTime() >= today.getTime() && d.getServiceStartTime().getTime() <= endDay.getTime()) {
                    return true;
                }
            }
            return false;
        }).collect(groupingBy(BizOrder::getProductType));

        if (map.isEmpty()) {
            Map<String, Integer> mm = Maps.newTreeMap();
            for (int i = 0; i < 10; i++) {
                Calendar cc = Calendar.getInstance();
                cc.setTime(new Date());
                cc.add(Calendar.DAY_OF_YEAR, i);
                mm.put(DateUtil.date2Str(cc.getTime(), FORMAT_MMddyyyy), 5);
            }
            rs.put(PayProductTypeEnum.CAR_OF_DAY.name(), mm);
            return new RestAPIResult<>(SUCCESS, rs);
        }


        map.forEach((k, v) -> {
            Map<String, Integer> mm = Maps.newTreeMap();
            for (int i = 0; i < 10; i++) {
                Calendar cc = Calendar.getInstance();
                cc.setTime(new Date());
                cc.add(Calendar.DAY_OF_YEAR, i);
                final String key = DateUtil.date2Str(cc.getTime(), FORMAT_MMddyyyy);

                int count = v.stream().filter(item -> {
                    for (ServiceTime st : item.getServiceTimes()) {
                        if (DateUtil.date2Str(st.getServiceStartTime(), FORMAT_MMddyyyy).equals(key)) {
                            return true;
                        }
                    }
                    return false;
                }).collect(Collectors.toList()).size();
                mm.put(key, Math.max(5 - count, 0));
            }
            rs.put(k, mm);
        });
        return new RestAPIResult<>(SUCCESS, rs);
    }


}
