package com.hiveelpay.dal.dao.typehandlers;

import com.hiveelpay.common.enumm.TradeInTypeEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(TradeInTypeEnum.class)
public class TradeInTypeEnumTypeHandler extends BaseTypeHandler<TradeInTypeEnum> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, TradeInTypeEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getVal());
    }

    @Override
    public TradeInTypeEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return TradeInTypeEnum.byValue(rs.getInt(columnName));
    }

    @Override
    public TradeInTypeEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return TradeInTypeEnum.byValue(rs.getInt(columnIndex));
    }

    @Override
    public TradeInTypeEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return TradeInTypeEnum.byValue(cs.getInt(columnIndex));
    }
}
