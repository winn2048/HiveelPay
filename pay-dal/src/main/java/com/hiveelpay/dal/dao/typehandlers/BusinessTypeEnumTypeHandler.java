package com.hiveelpay.dal.dao.typehandlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import com.hiveelpay.common.enumm.BusinessTypeEnum;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(BusinessTypeEnum.class)
public class BusinessTypeEnumTypeHandler extends BaseTypeHandler<BusinessTypeEnum> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BusinessTypeEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getVal());
    }

    @Override
    public BusinessTypeEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return BusinessTypeEnum.byValue(rs.getInt(columnName));
    }

    @Override
    public BusinessTypeEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return BusinessTypeEnum.byValue(rs.getInt(columnIndex));
    }

    @Override
    public BusinessTypeEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return BusinessTypeEnum.byValue(cs.getInt(columnIndex));
    }
}
