package com.hiveelpay.boot.service;

import com.hiveelpay.common.enumm.AppointmentStatus;
import com.hiveelpay.common.enumm.BusinessTypeEnum;
import com.hiveelpay.common.enumm.TradeInTypeEnum;
import com.hiveelpay.common.model.HiveelPage;
import com.hiveelpay.common.model.requests.AppointmentSearchRequest;
import com.hiveelpay.dal.dao.model.AppointmentDoc;

import java.util.List;

public interface AppointmentDocService {
    boolean save(AppointmentDoc appointmentDoc);

    List<AppointmentDoc> findUserAppointments(String userId, AppointmentStatus appointmentStatus, HiveelPage page);

    AppointmentDoc findUserAppointment(String userId, String appointmentId);

    List<AppointmentDoc> findAppointmentsForMch(String mchId, String storeId, BusinessTypeEnum businessType, TradeInTypeEnum tradeInType, HiveelPage page);

    AppointmentDoc findAppointmentForMch(String mchId, String appointmentId);

    Boolean cancelAppointment(AppointmentDoc appointmentDoc);

    List<AppointmentDoc> searchAppointment(AppointmentSearchRequest searchRequest, HiveelPage page);

    void invalidAppointment();

    /**
     * @param livingDays 存活天数
     */
    void serviceDone(int livingDays);

    List<AppointmentDoc> findAppointmentsByInvoiceId(String invoiceId);

    /**
     * 查询某天的所有预约单
     *
     * @param mchId
     * @param storeId
     * @param dateStr 'MMyyyy'
     * @param dayNum  day of month
     * @return
     */
    List<AppointmentDoc> findOneDayAppointments(String mchId, String storeId, String dateStr, Integer dayNum);

    AppointmentDoc findByDocId(String docId);

    boolean update(AppointmentDoc appointmentDoc);

    AppointmentDoc findAppointmentsById(String appointmentId);

    void notifyUserServiceStart();

    void sayGoodByToUser();

    /**
     * Dealer 报价
     *
     * @param appointmentId
     * @param price
     * @return
     */
    boolean quoteAppt(String appointmentId, String price);

    /**
     * pending 审核 通过
     * @param appointmentDoc
     * @return
     */
    Boolean passPendingAppt(AppointmentDoc appointmentDoc);
}
