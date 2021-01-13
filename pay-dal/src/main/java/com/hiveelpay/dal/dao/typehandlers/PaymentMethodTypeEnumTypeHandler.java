package com.hiveelpay.dal.dao.typehandlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import com.hiveelpay.common.enumm.PaymentMethodTypeEnum;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(PaymentMethodTypeEnum.class)
public class PaymentMethodTypeEnumTypeHandler extends BaseTypeHandler<PaymentMethodTypeEnum> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, PaymentMethodTypeEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getVal());
    }

    @Override
    public PaymentMethodTypeEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return PaymentMethodTypeEnum.byVal(rs.getInt(columnName));
    }

    @Override
    public PaymentMethodTypeEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return PaymentMethodTypeEnum.byVal(rs.getInt(columnIndex));
    }

    @Override
    public PaymentMethodTypeEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return PaymentMethodTypeEnum.byVal(cs.getInt(columnIndex));
    }
}
