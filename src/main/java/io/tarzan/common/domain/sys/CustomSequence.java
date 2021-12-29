package io.tarzan.common.domain.sys;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MrZ
 */
@Component
public class CustomSequence {

    private static final String VALUE_SQL = "select last_insert_id()";
    private static final String TABLE_NAME = "mt_sys_sequence";
    private static final String COLUMN_NAME = "current_value";

    @Value("${hwms.system.suffix:.1}")
    private String suffix;

    private Long increment = 1L;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CustomSequence() {}

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public String getNextKey(String seqName) throws DataAccessException {
        if (StringUtils.isEmpty(seqName)) {
            throw new DataAccessResourceFailureException("sequence name must be set.");
        }

        int row = this.jdbcTemplate.update("update " + TABLE_NAME + " set " + COLUMN_NAME + " = last_insert_id("
                        + COLUMN_NAME + " + " + this.increment + ") where name='" + seqName + "'");

        if (row == 0) {
            return null;
        }

        Long nextId = this.jdbcTemplate.query(VALUE_SQL, rs -> {
            if (!rs.next()) {
                throw new DataAccessResourceFailureException("last_insert_id() failed after executing an update");
            }
            return rs.getLong(1);
        });

        if (seqName.contains("cid")) {
            return nextId + "";
        } else {
            return nextId + suffix;
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<String> getNextKeys(String seqName, int count) throws DataAccessException {
        if (StringUtils.isEmpty(seqName)) {
            throw new DataAccessResourceFailureException("sequence name must be set.");
        }

        int row = this.jdbcTemplate.update("update " + TABLE_NAME + " set " + COLUMN_NAME + " = last_insert_id("
                + COLUMN_NAME + " + " + this.increment * count + ") where name='" + seqName + "'");
        if (row == 0) {
            return null;
        }

        Long nextId = this.jdbcTemplate.query(VALUE_SQL, new ResultSetExtractor<Long>() {

            @Override
            public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (!rs.next()) {
                    throw new DataAccessResourceFailureException("last_insert_id() failed after executing an update");
                }
                return rs.getLong(1);
            }
        });

        if (nextId == null) {
            return null;
        }

        Long start = nextId - (this.increment * count);
        List<String> result = new ArrayList<String>(count);
        for (int i = 0; i < count; i++) {
            start = start + this.increment;
            if (seqName.contains("cid")) {
                result.add(start + "");
            } else {
                result.add(start + suffix);
            }
        }
        return result;
    }
}
