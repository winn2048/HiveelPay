package com.hiveelpay.dal.dao.typehandlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import com.hiveelpay.common.enumm.ServiceLengthUnitEnum;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(ServiceLengthUnitEnum.class)
public class ServiceLengthUnitEnumTypeHandler extends BaseTypeHandler<ServiceLengthUnitEnum> {


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ServiceLengthUnitEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getVal());
    }


    @Override
    public ServiceLengthUnitEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return ServiceLengthUnitEnum.byVal(rs.getInt(columnName));
    }

    @Override
    public ServiceLengthUnitEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return ServiceLengthUnitEnum.byVal(rs.getInt(columnIndex));
    }

    @Override
    public ServiceLengthUnitEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return ServiceLengthUnitEnum.byVal(cs.getInt(columnIndex));
    }
}
