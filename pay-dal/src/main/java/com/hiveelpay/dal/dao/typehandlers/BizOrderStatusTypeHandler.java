package com.hiveelpay.dal.dao.typehandlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import com.hiveelpay.common.enumm.BizOrderStatus;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(BizOrderStatus.class)
public class BizOrderStatusTypeHandler extends BaseTypeHandler<BizOrderStatus> {


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BizOrderStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getVal());
    }


    @Override
    public BizOrderStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return BizOrderStatus.byValue(rs.getInt(columnName));
    }

    @Override
    public BizOrderStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return BizOrderStatus.byValue(rs.getInt(columnIndex));
    }

    @Override
    public BizOrderStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return BizOrderStatus.byValue(cs.getInt(columnIndex));
    }
}
