package com.hiveelpay.boot.jobs;

import com.google.common.collect.Sets;
import com.hiveelpay.boot.service.BizOrderService;
import com.hiveelpay.boot.service.NotifySearchEngineComponent;
import com.hiveelpay.common.enumm.CustomerServiceStatusEnum;
import com.hiveelpay.common.enumm.PayProductTypeEnum;
import com.hiveelpay.common.enumm.SyncStatus;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.mapper.BizCarMapper;
import com.hiveelpay.dal.dao.mapper.CustomerValidServicesMapper;
import com.hiveelpay.dal.dao.model.BizCar;
import com.hiveelpay.dal.dao.model.CustomerValidServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomerServiceJobs {
    private static final MyLog _log = MyLog.getLog(CustomerServiceJobs.class);

    @Autowired
    private BizOrderService bizOrderServiceImpl;
    @Autowired
    private BizCarMapper bizCarMapper;
    @Autowired
    private NotifySearchEngineComponent notifySearchEngineComponent;

    @Autowired
    private CustomerValidServicesMapper customerValidServicesMapper;

    /**
     * 支付成功以后，业务订单的服务初始化，补漏。
     */
    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 60)//一小时一次
    public void initBizOrderToService() {
        _log.info("=====================================================CustomerServiceJobs#initBizOrderToService()");
        bizOrderServiceImpl.initBizOrderToService();
    }

    /**
     * 10分钟一次检测服务 ，到达服务开始时间的 开启服务
     */
    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 1)//10分钟一次
    public void checkAndStartServices() {
        _log.info("=====================================================CustomerServiceJobs#checkAndStartServices()");
        bizOrderServiceImpl.checkAndStartServices();
    }


    /**
     * 检查付费服务是否到期，到期后修改服务状态
     */
    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 1)//10分钟
    public void checkAndEndService() {
        _log.info("=====================================================CustomerServiceJobs#checkAndEndService()");
        bizOrderServiceImpl.checkAndEndService();
    }

    /**
     *
     */
    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 10)//10分钟
    public void checkBizOrderServiceDone() {
        _log.info("=====================================================CustomerServiceJobs#checkBizOrderServiceDone()");
        bizOrderServiceImpl.checkBizOrderServiceDone();
    }


    /**
     * 重试-通知搜索引擎失败的订单
     */
    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 3)//3分钟
    public void reSyncServiceToSearchEngine() {
        _log.info("=====================================================CustomerServiceJobs#reSyncServiceToSearchEngine()");

        List<BizCar> list = bizCarMapper.findByStatus(SyncStatus.SYNC_FAILED, 8);
        if (list == null || list.isEmpty()) {
            return;
        }
        list.forEach(i -> {
            try {
                boolean result = notifySearchEngineComponent.notifySearchEngine(i.getCarId());
                bizCarMapper.increaceNotifyCount(i.getBizOrderNo(), i.getCarId(), result ? SyncStatus.SYNC_SUCCESSED : SyncStatus.SYNC_FAILED);
            } catch (Exception e) {
                _log.error("This exception can be ignored.", e);
            }
        });
    }

    @Async
    @Scheduled(cron = "0 5,15,35,45 * * * ?")// 在5,15,35,45分的时候启动执行
    public void syncValidServiceToSearchEngine() {
        Set<CustomerServiceStatusEnum> statusEnumSet = Sets.newHashSet();
        statusEnumSet.add(CustomerServiceStatusEnum.IN_SERVICE);
        statusEnumSet.add(CustomerServiceStatusEnum.USED);

        Set<PayProductTypeEnum> types = Sets.newHashSet();
        types.add(PayProductTypeEnum.CAR_OF_DAY);
        types.add(PayProductTypeEnum.ADVANCING);
        types.add(PayProductTypeEnum.SEARCH_RESULT);
        types.add(PayProductTypeEnum.HIGHLIGHTING);

        List<CustomerValidServices> list = customerValidServicesMapper.findByStatusAndPayProductTypes(statusEnumSet, types);
        if (list == null || list.isEmpty()) {
            return;
        }
        Set<String> bizOrders = list.stream().filter(Objects::nonNull).map(CustomerValidServices::getBizOrderNo).collect(Collectors.toSet());
        if (bizOrders.isEmpty()) {
            return;
        }
        bizOrderServiceImpl.syncToSearchEngine(bizOrders);
    }
}
