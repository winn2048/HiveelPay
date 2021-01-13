package com.hiveelpay.dal.dao.typehandlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import com.hiveelpay.common.enumm.CardType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(CardType.class)
public class CardTypeTypeHandler extends BaseTypeHandler<CardType> {


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CardType parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getVal());
    }

    @Override
    public CardType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return CardType.byValue(rs.getInt(columnName));
    }

    @Override
    public CardType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return CardType.byValue(rs.getInt(columnIndex));
    }

    @Override
    public CardType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return CardType.byValue(cs.getInt(columnIndex));
    }
}
