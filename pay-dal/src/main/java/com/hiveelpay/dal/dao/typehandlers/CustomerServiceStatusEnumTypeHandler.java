package com.hiveelpay.dal.dao.typehandlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import com.hiveelpay.common.enumm.CustomerServiceStatusEnum;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(CustomerServiceStatusEnum.class)
public class CustomerServiceStatusEnumTypeHandler extends BaseTypeHandler<CustomerServiceStatusEnum> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CustomerServiceStatusEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getVal());
    }

    @Override
    public CustomerServiceStatusEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return CustomerServiceStatusEnum.byValue(rs.getInt(columnName));
    }

    @Override
    public CustomerServiceStatusEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return CustomerServiceStatusEnum.byValue(rs.getInt(columnIndex));
    }

    @Override
    public CustomerServiceStatusEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return CustomerServiceStatusEnum.byValue(cs.getInt(columnIndex));
    }
}
