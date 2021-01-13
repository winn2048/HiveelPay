package com.hiveelpay.dal.dao.mapper;

import com.hiveelpay.common.enumm.InvoiceStatusEnum;
import com.hiveelpay.common.model.HiveelPage;
import com.hiveelpay.common.model.requests.InvoiceSearchRequest;
import com.hiveelpay.dal.dao.model.Invoice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceMapper {
    /**
     * 保存发票
     *
     * @param invoice
     * @return
     */
    int saveOne(@Param("invoice") Invoice invoice);

    /**
     * @param mchId
     * @param startDateStr
     * @param endDateStr
     * @return
     */
    int countInvoice(@Param("mchId") String mchId, @Param("startDateStr") String startDateStr, @Param("endDateStr") String endDateStr);

    int searchCount(@Param("searchRequest") InvoiceSearchRequest searchRequest);

    List<Invoice> searchInvoice(@Param("searchRequest") InvoiceSearchRequest searchRequest, @Param("page") HiveelPage page);

    Invoice queryByInvoiceId(@Param("invoiceId") String invoiceId);

    /**
     *
     * @param invoice
     * @param preStatus
     * @return
     */
    int updateInvoice(@Param("invoice") Invoice invoice, @Param("preStatus") InvoiceStatusEnum preStatus);
}
