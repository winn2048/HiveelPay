package com.hiveelpay.dal.dao.mapper;

import com.hiveelpay.common.enumm.AppointmentStatus;
import com.hiveelpay.common.enumm.BusinessTypeEnum;
import com.hiveelpay.common.enumm.TradeInTypeEnum;
import com.hiveelpay.common.model.HiveelPage;
import com.hiveelpay.common.model.requests.AppointmentSearchRequest;
import com.hiveelpay.dal.dao.model.AppointmentDoc;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AppointmentDocMapper {
    /**
     * @param ad
     * @return
     */
    int save(@Param("ad") AppointmentDoc ad);

    /**
     * 分配bizOrderNo给预约单，预约单必须是新建状态
     *
     * @param docId
     * @param bizOrderNo
     * @return
     */
    int updateAndAssociateBizOrderNo(@Param("docId") String docId, @Param("bizOrderNo") String bizOrderNo, @Param("status") AppointmentStatus status);

    /**
     * @param docId
     * @param fromStatus
     * @param toStatus
     * @return
     */
    int updateStatus(@Param("docId") String docId, @Param("fromStatus") AppointmentStatus fromStatus, @Param("toStatus") AppointmentStatus toStatus);

    /**
     * @param userId
     * @param appointmentStatus
     * @return
     */
    int countUserAppointments(@Param("userId") String userId, @Param("appointmentStatus") AppointmentStatus appointmentStatus);

    /**
     * @param userId
     * @param appointmentStatus
     * @param page
     * @return
     */
    List<AppointmentDoc> findUserAppointments(@Param("userId") String userId, @Param("appointmentStatus") AppointmentStatus appointmentStatus, @Param("page") HiveelPage page);

    /**
     * @param userId
     * @param appointmentId
     * @return
     */
    AppointmentDoc findUserAppointment(@Param("userId") String userId, @Param("appointmentId") String appointmentId);

    /**
     * @param mchId
     * @param storeId
     * @param businessType
     * @return
     */
    Integer countMchAppointment(@Param("mchId") String mchId,
                                @Param("storeId") String storeId,
                                @Param("businessType") BusinessTypeEnum businessType,
                                @Param("tradeinType") TradeInTypeEnum tradeinType,
                                @Param("statusSet") Set<AppointmentStatus> statusSet
    );

    /**
     * @param mchId
     * @param storeId
     * @param businessType
     * @param page
     * @return
     */
    List<AppointmentDoc> findMchAppointments(@Param("mchId") String mchId,
                                             @Param("storeId") String storeId,
                                             @Param("businessType") BusinessTypeEnum businessType,
                                             @Param("tradeinType") TradeInTypeEnum tradeinType,
                                             @Param("page") HiveelPage page,
                                             @Param("statusSet") Set<AppointmentStatus> statusSet
    );

    /**
     * @param mchId
     * @param appointmentId
     * @return
     */
    AppointmentDoc findAppointmentForMch(@Param("mchId") String mchId, @Param("appointmentId") String appointmentId);

    /**
     * 统计该天 是否有有效的预约单
     *
     * @param storeId
     * @param businessType
     * @param dateStr
     * @return
     */
    int countAppointmentsWithDateStr(@Param("storeId") String storeId, @Param("businessType") BusinessTypeEnum businessType, @Param("dateStr") String dateStr, @Param("statusSet") Set<AppointmentStatus> statusSet);

    /**
     * @param dayStr
     * @param storeId
     * @param businessType
     * @param statusSet
     * @return
     */
    List<AppointmentDoc> findHaveAppointmentTimes(@Param("dayStr") String dayStr, @Param("storeId") String storeId, @Param("businessType") BusinessTypeEnum businessType, @Param("statusSet") Set<AppointmentStatus> statusSet);

    /**
     * @param appointmentId
     * @return
     */
    AppointmentDoc findByAppointmentId(@Param("appointmentId") String appointmentId);

    /**
     * @param searchRequest
     * @param statusSet
     * @return
     */
    int appointmentSearchCount(@Param("searchRequest") AppointmentSearchRequest searchRequest, @Param("statusSet") Set<AppointmentStatus> statusSet);

    /**
     * @param searchRequest
     * @param statusSet
     * @param page
     * @return
     */
    List<AppointmentDoc> appointmentSearch(@Param("searchRequest") AppointmentSearchRequest searchRequest, @Param("statusSet") Set<AppointmentStatus> statusSet, @Param("page") HiveelPage page);

    /**
     * 分配发票ID
     *
     * @param appointmentIds
     * @param invoiceId
     */
    int associateInvoiceId(@Param("appointmentIds") Set<String> appointmentIds, @Param("invoiceId") String invoiceId);

    /**
     * @param invoiceId
     * @return
     */
    List<AppointmentDoc> findAppointmentsByInvoiceId(String invoiceId);

    /**
     * 查询某天 某些状态的预约单
     *
     * @param dateStr   格式是：'MMyyyy'
     * @param dayStr    格式是'dd'
     * @param statusSet
     * @return
     */
    List<AppointmentDoc> findOneDayAppointments(@Param("mchId") String mchId, @Param("storeId") String storeId, @Param("dateStr") String dateStr, @Param("dayStr") String dayStr, @Param("statusSet") Set<AppointmentStatus> statusSet);

    /**
     * @param appointmentDoc
     * @return
     */
    int updateAppointment(@Param("appointmentDoc") AppointmentDoc appointmentDoc);

    /**
     * @param days      距离预约时间到达的天数
     * @param statusSet 状态集
     * @return
     */
    List<AppointmentDoc> searchByDaysLeave(@Param("days") int days, @Param("statusSet") Set<AppointmentStatus> statusSet);


    List<AppointmentDoc> searchByHoursLeave(@Param("hours") int hours, @Param("statusSet") Set<AppointmentStatus> statusSet);

    /**
     * @param invoiceId
     * @return total records
     */
    Integer countWithInvoiceId(@Param("invoiceId") String invoiceId);

    /**
     * 更新 quotePrice
     *
     * @param appointmentId 预约ID
     * @param quotePrice    报价
     * @return
     */
    int quoteAppt(@Param("appointmentId") String appointmentId, @Param("quotePrice") Long quotePrice);
}
