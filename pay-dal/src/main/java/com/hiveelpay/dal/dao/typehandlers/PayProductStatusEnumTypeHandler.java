package com.hiveelpay.dal.dao.typehandlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import com.hiveelpay.common.enumm.PayProductStatusEnum;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(PayProductStatusEnum.class)
public class PayProductStatusEnumTypeHandler extends BaseTypeHandler<PayProductStatusEnum> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, PayProductStatusEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getVal());
    }

    @Override
    public PayProductStatusEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return PayProductStatusEnum.byValue(rs.getInt(columnName));
    }

    @Override
    public PayProductStatusEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return PayProductStatusEnum.byValue(rs.getInt(columnIndex));
    }

    @Override
    public PayProductStatusEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return PayProductStatusEnum.byValue(cs.getInt(columnIndex));
    }
}
