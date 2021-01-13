package com.hiveelpay.dal.dao.typehandlers;

import com.hiveelpay.common.enumm.InvoiceStatusEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(InvoiceStatusEnum.class)
public class InvoiceStatusEnumTypeHandler extends BaseTypeHandler<InvoiceStatusEnum> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, InvoiceStatusEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getVal());
    }

    @Override
    public InvoiceStatusEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return InvoiceStatusEnum.byValue(rs.getInt(columnName));
    }

    @Override
    public InvoiceStatusEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return InvoiceStatusEnum.byValue(rs.getInt(columnIndex));
    }

    @Override
    public InvoiceStatusEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return InvoiceStatusEnum.byValue(cs.getInt(columnIndex));
    }
}
