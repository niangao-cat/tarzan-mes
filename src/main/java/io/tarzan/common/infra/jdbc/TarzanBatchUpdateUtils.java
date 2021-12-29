package io.tarzan.common.infra.jdbc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author : MrZ
 * @date : 2020-10-15 17:32
 **/
public class TarzanBatchUpdateUtils extends BatchUpdateUtils {
    protected static final Log logger = LogFactory.getLog(TarzanBatchUpdateUtils.class);

    public static int[] executeBatchUpdateWithBatchSize(String sql, final List<Object[]> batchValues,
                    final int[] columnTypes, final int batchSize, JdbcOperations jdbcOperations) {
        if (batchValues.size() <= batchSize) {
            return jdbcOperations.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Object[] values = batchValues.get(i);
                    setStatementParameters(values, ps, columnTypes);
                }

                @Override
                public int getBatchSize() {
                    return batchValues.size();
                }
            });
        } else {
            int[] result;
            int[][] all = jdbcOperations.batchUpdate(sql, batchValues, batchSize,
                            (ps, argument) -> setStatementParameters(argument, ps, columnTypes));
            if (all.length > 0) {
                int len = 0;
                for (int[] element : all) {
                    len += element.length;
                }
                result = new int[len];
                int index = 0;
                for (int[] array : all) {
                    for (int element : array) {
                        result[index++] = element;
                    }
                }
            } else {
                result = new int[0];
            }
            return result;
        }
    }


    public static long[] executeBatchUpdateWithBatchSize(String sql, final List<Object[]> batchValues,
                    final int[] columnTypes, final int batchSize, JdbcOperations jdbcOperations, boolean returnIdFlag) {
        return batchUpdate(sql, jdbcOperations, returnIdFlag, batchValues, batchSize,
                        (ps, argument) -> setStatementParameters(argument, ps, columnTypes));
    }

    private static <T> long[] batchUpdate(String sql, JdbcOperations jdbcOperations, boolean returnIdFlag,
                    final Collection<T> batchArgs, final int batchSize,
                    final ParameterizedPreparedStatementSetter<T> pss) {

        if (logger.isDebugEnabled()) {
            logger.debug("Executing SQL batch update [" + sql + "] with a batch size of " + batchSize);
        }
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sql);
        if (returnIdFlag) {
            pscf.setReturnGeneratedKeys(true);
        }

        long[] result = jdbcOperations.execute(pscf.newPreparedStatementCreator(Collections.emptyList()), ps -> {
            try {
                boolean batchSupported = true;
                if (!JdbcUtils.supportsBatchUpdates(ps.getConnection())) {
                    batchSupported = false;
                    logger.warn("JDBC Driver does not support Batch updates; resorting to single statement execution");
                }
                int n = 0;
                List<Long> ids = new ArrayList<>();
                for (T obj : batchArgs) {
                    pss.setValues(ps, obj);
                    n++;
                    if (batchSupported) {
                        ps.addBatch();
                        if (n % batchSize == 0 || n == batchArgs.size()) {
                            if (logger.isDebugEnabled()) {
                                int batchIdx = (n % batchSize == 0) ? n / batchSize : (n / batchSize) + 1;
                                int items = n - ((n % batchSize == 0) ? n / batchSize - 1 : (n / batchSize))
                                                * batchSize;
                                logger.debug("Sending SQL batch update #" + batchIdx + " with " + items + " items");
                            }
                            ps.executeBatch();
                            if (returnIdFlag) {
                                // 增加获取主键的逻辑
                                ResultSet rs = null;
                                try {
                                    rs = ps.getGeneratedKeys();

                                    while (rs.next()) {
                                        ids.add(rs.getLong(1));
                                    }
                                } finally {
                                    JdbcUtils.closeResultSet(rs);
                                }
                            }
                        }
                    } else {
                        ps.executeUpdate();
                        // 增加获取主键的逻辑
                        if (returnIdFlag) {
                            ResultSet rs = null;
                            try {
                                rs = ps.getGeneratedKeys();

                                while (rs.next()) {
                                    ids.add(rs.getLong(1));
                                }
                            } finally {
                                JdbcUtils.closeResultSet(rs);
                            }
                        }
                    }
                }

                return ids.stream().filter(Objects::nonNull).mapToLong(Long::longValue).toArray();
            } finally {
                if (pss instanceof ParameterDisposer) {
                    ((ParameterDisposer) pss).cleanupParameters();
                }
            }
        });

        Assert.state(result != null, "No result array");

        return result;
    }

}
