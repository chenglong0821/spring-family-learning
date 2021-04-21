package com.ebaotech.longcheng;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class BatchFooDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void batchInsert(){
        jdbcTemplate.batchUpdate("insert into FOO(BAR) values (?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1,"b-" + i);
                    }

                    @Override
                    public int getBatchSize() {
                        return 1000;
                    }
                });
        List<Foo> list = new ArrayList<>();
        list.add(Foo.builder().id(2001L).bar("b-1001").build());
        list.add(Foo.builder().id(2002L).bar("b-1002").build());
        namedParameterJdbcTemplate.batchUpdate("insert into FOO (ID,BAR) values(:id,:bar)",
                SqlParameterSourceUtils.createBatch(list));
    }
}
