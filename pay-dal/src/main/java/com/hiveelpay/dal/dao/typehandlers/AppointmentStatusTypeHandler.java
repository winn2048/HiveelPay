package com.hiveelpay.dal.dao.typehandlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import com.hiveelpay.common.enumm.AppointmentStatus;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(AppointmentStatus.class)
public class AppointmentStatusTypeHandler extends BaseTypeHandler<AppointmentStatus> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, AppointmentStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getVal());
    }

    @Override
    public AppointmentStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return AppointmentStatus.byValue(rs.getInt(columnName));
    }

    @Override
    public AppointmentStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return AppointmentStatus.byValue(rs.getInt(columnIndex));
    }

    @Override
    public AppointmentStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return AppointmentStatus.byValue(cs.getInt(columnIndex));
    }
}
