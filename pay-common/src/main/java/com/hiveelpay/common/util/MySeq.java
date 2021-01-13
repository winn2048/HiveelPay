package com.hiveelpay.common.util;


import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wilson  wilson@hiveel.com
 * @version V1.0
 * @Description: 生成全局唯一序列号工具类
 * @date 2017-07-05
 * @Copyright: pay.hiveel.com
 */
public class MySeq {

    private static AtomicLong pay_seq = new AtomicLong(0L);
    private static String pay_seq_prefix = "P";
    private static AtomicLong trans_seq = new AtomicLong(0L);
    private static String trans_seq_prefix = "T";
    private static AtomicLong refund_seq = new AtomicLong(0L);
    private static String refund_seq_prefix = "R";

    private static AtomicLong appointment_seq = new AtomicLong(0L);
    private static String appointment_seq_prefix = "A";

    private static AtomicLong bizOrder_seq = new AtomicLong(0L);
    private static String bizOrder_seq_prefix = "B";

    private static AtomicLong customer_seq = new AtomicLong(0L);
    private static String customer_seq_prefix = "C";

    private static AtomicLong invoice_seq = new AtomicLong(0L);
    private static String invoice_seq_prefix = "H";
    private static AtomicLong block_seq = new AtomicLong(0L);
    private static String block_seq_prefix = "TB";

    private static String node = "00";

    static {
        try {
            //URL url = Thread.currentThread().getContextClassLoader().getResource("config" + File.separator + "system.properties");
            //Properties properties = new Properties();
            //properties.load(url.openStream());
            //node = properties.getProperty(ConfigEnum.SERVER_NAME.getKey());
            InetAddress ip = InetAddress.getLocalHost();
            String hostAddress = ip.getHostAddress();
            if (hostAddress != null && hostAddress.trim().length() > 0) {
                node = hostAddress.substring(hostAddress.lastIndexOf(".") + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            node = String.valueOf(new Random().nextInt(255));
        }

    }

    public static String getPay() {
        return getSeqNoNode(pay_seq_prefix, pay_seq).toUpperCase();
    }

    public static String getTrans() {
        return getSeqNoNode(trans_seq_prefix, trans_seq);
    }

    public static String getRefund() {
        return getSeqNoNode(refund_seq_prefix, refund_seq);
    }

    public static String getAppointmentId() {
        return getSeqNoNode(appointment_seq_prefix, appointment_seq);
    }

    public static String getTimeBlockId() {
        return getSeqNoNode(block_seq_prefix, block_seq);
    }

    public static String getBizOrderId() {
        return getSeqNoNode(bizOrder_seq_prefix, bizOrder_seq);
    }

    public static String getCustomerId() {
        return getSeqNoNode(customer_seq_prefix, customer_seq);
    }

    public static String getInvoiceId() {
        return getSeqNoNode(invoice_seq_prefix, invoice_seq);
    }

    private static String getSeq(String prefix, AtomicLong seq) {
        prefix += node;
        return String.format("%s%s%06d", prefix, DateUtil.getSeqString(), (int) seq.getAndIncrement() % 1000000).toUpperCase();
    }

    private static String getSeqNoNode(String prefix, AtomicLong seq) {
        return String.format("%s%s%s%s%s",
                prefix,
                Integer.toHexString(new Random().nextInt(99)).toUpperCase(),
                Integer.toHexString(Integer.valueOf(node)),
                Long.toHexString(new Long(DateUtil.getSeqStringShort())).toUpperCase().substring(6),
                Integer.toHexString((int) seq.getAndIncrement() % 1000).toUpperCase()).toUpperCase();
        //        return String.format("%s%02d%03d%s%04d", prefix, new Random().nextInt(99), Integer.valueOf(node), DateUtil.getSeqStringShort(), (int) seq.getAndIncrement() % 10000);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println("pay=" + getBizOrderId());
//            System.out.println("trans=" + getTrans());
//            System.out.println("refund=" + getRefund());
//            System.out.println("appointmentId=" + getAppointmentId());
//            System.out.println("------------------" + new Random().nextInt(254));
        }

//        try {
//            InetAddress ip = InetAddress.getLocalHost();
//
//            System.out.println(ip.getHostAddress());
//            System.out.println(ip.getHostName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i - 6);
        Date start = c.getTime();

        c.add(Calendar.DATE, 7);
        Date end = c.getTime();
        System.out.println(start + " - " + end);
    }


}