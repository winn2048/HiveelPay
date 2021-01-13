package com.hiveelpay.mgr.service.events;

import com.google.common.base.Strings;
import com.hiveelpay.common.util.AmountUtil;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.mapper.AppointmentDocMapper;
import com.hiveelpay.dal.dao.mapper.InvoiceMapper;
import com.hiveelpay.dal.dao.mapper.MchStoreInfoMapper;
import com.hiveelpay.dal.dao.model.AppointmentDoc;
import com.hiveelpay.dal.dao.model.Invoice;
import com.hiveelpay.dal.dao.model.MchStoreInfo;
import com.hiveelpay.mgr.config.EmailConfig;
import com.hiveelpay.mgr.service.EmailComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 消息通知事件
 */
@Component
public class NotificationEventListener {
    private static final MyLog _log = MyLog.getLog(NotificationEventListener.class);
    public static final String PRE_INSPECTION = "pre-inspection";
    public static final String TRADE_IN = "trade-in";
    public static final String OIL_CHANGE = "oil-change";

    public static final String ADDRESS_001 = "1050 Lakes Dr. Suite 206, West Covina, CA 91790";
    public static final String ADDRESS_002 = "11400 W Olympic Blvd. Suite 650, Los Angeles, CA 90064";

    public static final String KEY_APPOINTMENT_ID = "_appointmentId_";
    public static final String KEY_BIZ_TYPE = "_bizType_";

    @Autowired
    private EmailComponent emailComponent;
    @Autowired
    private AppointmentDocMapper appointmentDocMapper;
    @Autowired
    private EmailConfig emailConfig;
    @Autowired
    private MchStoreInfoMapper mchStoreInfoMapper;
    @Autowired
    private InvoiceMapper invoiceMapper;

    /**
     * 发票生成，通知Dealer
     *
     * @param notificationEvent
     */
    @Async
    @EventListener(condition = "#notificationEvent.isInvoiceReady()")
    public void notifyInvoieReady(NotificationEvent notificationEvent) {
        if (!emailConfig.isSwitchOn()) {
            _log.warn("Email switch is closed! If you want open it please set config.email.switchOn to 'true'.");
            return;
        }
        final String invoiceId = notificationEvent.getDocId();
        Invoice invoice = invoiceMapper.queryByInvoiceId(invoiceId);
        if (invoice == null) {
            return;
        }
        final String mchId = invoice.getMchId();
        List<MchStoreInfo> mchStoreInfos = mchStoreInfoMapper.findByMchId(mchId);
        if (mchStoreInfos == null || mchStoreInfos.isEmpty()) {
            return;
        }
        Set<String> receivers = new HashSet<>();
        mchStoreInfos.forEach(i -> {
            String emails = i.getEmails();
            if (emails != null && emails.trim().length() > 0) {
                receivers.addAll(Arrays.asList(emails.split(",")));
            }
        });
        if (receivers.isEmpty()) {
            return;
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id", "42");
        map.add("toArr", String.join(",", receivers));
        if (emailConfig.isOpenCC()) {
            map.add("ccArr", "iris@hiveel.com");
        }
        map.add("HIVEEL_INVOICE_ID", invoice.getInvoiceId());
        final String from = DateUtil.date2Str(invoice.getDateFrom(), DateUtil.FORMAT_4_EMAIL_1);
        final String to = DateUtil.date2Str(invoice.getDateTo(), DateUtil.FORMAT_4_EMAIL_1);
        map.add("HIVEEL_DATE_FROM", from);
        map.add("HIVEEL_DATE_TO", to);
        map.add("HIVEEL_REMARK", Strings.nullToEmpty(invoice.getRemark()));
        map.add("HIVEEL_INVOICE_AMOUNT", AmountUtil.convertCent2Dollar(String.valueOf(invoice.getInvoiceAmount())));
        map.add("HIVEEL_INVOICE_URL", "https://businessadmin.hiveel.com/main");
        List<AppointmentDoc> appointmentDocList = appointmentDocMapper.findAppointmentsByInvoiceId(notificationEvent.getDocId());
        if (appointmentDocList == null || appointmentDocList.isEmpty()) {
            map.add("HIVEEL_TOTAL_RECORDS", "0");
        } else {
            map.add("HIVEEL_TOTAL_RECORDS", String.valueOf(appointmentDocList.size()));
        }
        map.add("subject", String.format("Hiveel invoice(%s-%s)", from, to));
        emailComponent.sendEmail(map);
    }
}
