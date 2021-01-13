package com.hiveelpay.dal.dao.typehandlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import com.hiveelpay.common.enumm.SyncStatus;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
@MappedTypes(SyncStatus.class)
public class SyncStatusTypeHandler extends BaseTypeHandler<SyncStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, SyncStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getVal());
    }

    @Override
    public SyncStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return SyncStatus.valueOf(rs.getInt(columnName));
    }

    @Override
    public SyncStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return SyncStatus.valueOf(rs.getInt(columnIndex));
    }

    @Override
    public SyncStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return SyncStatus.valueOf(cs.getInt(columnIndex));
    }


}
