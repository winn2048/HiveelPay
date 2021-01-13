package com.hiveelpay.dal.dao.typehandlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import com.hiveelpay.common.enumm.PayProductTypeEnum;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(PayProductTypeEnum.class)
public class PayProductTypeEnumTypeHandler extends BaseTypeHandler<PayProductTypeEnum> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, PayProductTypeEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getVal());
    }

    @Override
    public PayProductTypeEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return PayProductTypeEnum.byValue(rs.getInt(columnName));
    }

    @Override
    public PayProductTypeEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return PayProductTypeEnum.byValue(rs.getInt(columnIndex));
    }

    @Override
    public PayProductTypeEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return PayProductTypeEnum.byValue(cs.getInt(columnIndex));
    }
}
