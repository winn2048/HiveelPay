package com.hiveelpay.dal.dao.typehandlers;

import com.hiveelpay.common.enumm.AppointmentType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(AppointmentType.class)
public class AppointmentTypeTypeHandler extends BaseTypeHandler<AppointmentType> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, AppointmentType parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getVal());
    }

    @Override
    public AppointmentType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return AppointmentType.byVal(rs.getInt(columnName));
    }

    @Override
    public AppointmentType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return AppointmentType.byVal(rs.getInt(columnIndex));
    }

    @Override
    public AppointmentType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return AppointmentType.byVal(cs.getInt(columnIndex));
    }
}
