package io.tarzan.common.infra.repository.impl;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.jackson.serializer.DateSerializer;
import org.hzero.core.util.FieldNameUtils;
import org.hzero.mybatis.domian.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.domain.EntityColumn;
import io.choerodon.mybatis.domain.EntityTable;
import io.choerodon.mybatis.helper.EntityHelper;
import io.choerodon.mybatis.helper.LanguageHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.util.MtLanguageHelper;
import io.tarzan.common.domain.vo.MtMultiLanguageVO;

/**
 * 注意该类不允许加Component注解
 * 
 * @author : MrZ
 * @date : 2020-02-25 18:02
 **/
public class MtCustomOracleRepositoryImpl implements MtCustomDbRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateSerializer.class);
    private static final String TABLE_NAME = "mt_sys_sequence";
    private static final String COLUMN_NAME = "seq_name";
    private static final List<String> CREATE_FIELDS = Arrays.asList("CREATED_BY", "CREATION_DATE");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Value("${hwms.system.suffix:.1}")
    private String suffix;
    private JdbcTemplate jdbcTemplate;

    public MtCustomOracleRepositoryImpl() {}

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean needPaddingId() {
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public String getNextKey(String seqName) throws DataAccessException {
        if (StringUtils.isEmpty(seqName)) {
            throw new DataAccessResourceFailureException("sequence name must be set.");
        }

        String sequence = this.jdbcTemplate.query(
                        "select " + COLUMN_NAME + " from " + TABLE_NAME + " where name = '" + seqName + "'", rs -> {
                            if (!rs.next()) {
                                throw new DataAccessResourceFailureException(
                                                "Get sequence failed after executing an update");
                            }
                            return rs.getString(1);
                        });

        Long nextId = this.jdbcTemplate.query("select " + sequence + ".nextval from dual", rs -> {
            if (!rs.next()) {
                throw new DataAccessResourceFailureException("Get sequence name failed after executing an update");
            }
            return rs.getLong(1);
        });

        if (seqName.contains("cid")) {
            return nextId + "";
        } else {
            return nextId + suffix;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<String> getNextKeys(String seqName, int count) throws DataAccessException {
        if (StringUtils.isEmpty(seqName)) {
            throw new DataAccessResourceFailureException("sequence name must be set.");
        }
        List<Long> sequenceIdList = this.jdbcTemplate
                        .query("select " + seqName + ".nextval from dual connect by rownum <= " + count, rs -> {
                            if (!rs.next()) {
                                throw new DataAccessResourceFailureException(
                                                "Get sequence name failed after executing an update");
                            }

                            List<Long> result = new ArrayList<>();

                            // 先添加第一个
                            result.add(rs.getLong(1));

                            // 之后添加后面的
                            while (rs.next()) {
                                result.add(rs.getLong(1));
                            }

                            return result;
                        });

        List<String> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sequenceIdList)) {
            for (Long aLong : sequenceIdList) {
                if (seqName.contains("cid")) {
                    result.add(aLong + "");
                } else {
                    result.add(aLong + suffix);
                }
            }
        }

        return result;
    }

    @Override
    public List<String> getDeleteSql(AuditDomain dto) {
        final StringBuilder sql = new StringBuilder();
        List<String> sqlList = new ArrayList<String>();
        Class<?> entityClass = dto.getClass();
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);

        sql.append("DELETE FROM ").append(entityTable.getName()).append(" ").append("WHERE").append(" ");

        StringBuilder pkSql = new StringBuilder();
        for (EntityColumn field : pkColumns) {
            try {
                Object obj = PropertyUtils.getProperty(dto, field.getProperty());
                if (obj != null) {
                    if (obj instanceof String) {
                        pkSql.append(field.getColumn()).append("=").append("'").append(obj.toString()).append("'")
                                        .append(" AND ");
                    } else if (obj instanceof Date) {
                        pkSql.append(field.getColumn()).append("=").append("to_date(").append("'")
                                        .append(DATE_FORMAT.format(obj)).append("','yyyy-MM-dd hh24:mi:ss')");
                    } else {
                        pkSql.append(field.getColumn()).append("=").append(obj).append(" AND ");
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                LOGGER.info(e.getMessage());
            }
        }

        if (pkSql.length() != 0) {
            sql.append(pkSql);
            sql.delete(sql.length() - " AND ".length(), sql.length() - 1);
            sqlList.add(sql.toString());
        }

        MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
        if (multiLanguageTable != null) {
            String tableName = entityTable.getMultiLanguageTableName();
            StringBuilder languageSql = new StringBuilder("DELETE FROM " + tableName + " WHERE ");

            if (pkSql.length() != 0) {
                languageSql.append(pkSql);
                languageSql.delete(languageSql.length() - " AND ".length(), languageSql.length() - 1);
                sqlList.add(languageSql.toString());
            }
        }
        return sqlList;
    }

    @Override
    public List<String> getUpdateSql(AuditDomain dto) {
        final StringBuilder sql = new StringBuilder();
        List<EntityColumn> pkColumns = new ArrayList<EntityColumn>();
        List<String> sqlList = new ArrayList<String>();
        Class<?> entityClass = dto.getClass();
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);

        sql.append("UPDATE ").append(entityTable.getName()).append(" ").append("set").append(" ");

        for (EntityColumn column : columnList) {
            if (column.isId()) {
                pkColumns.add(column);
                continue;
            }
            if ("last_update_date".equalsIgnoreCase(column.getColumn())) {
                sql.append(column.getColumn()).append("=").append("SYSDATE,");
                continue;
            }
            if ("object_version_number".equalsIgnoreCase(column.getColumn())) {
                sql.append(column.getColumn()).append("=").append(column.getColumn()).append("+1,");
                continue;
            }

            if (column.isUpdatable()) {
                try {
                    Object value = PropertyUtils.getProperty(dto, column.getProperty());
                    if (value != null) {
                        if (value instanceof String) {
                            value = ((String) value).replace("'", "''");
                            sql.append(column.getColumn()).append("=").append("'").append(value.toString()).append("'")
                                            .append(",");
                        } else if (value instanceof Date) {
                            sql.append(column.getColumn()).append("=").append("to_date(").append("'")
                                            .append(DATE_FORMAT.format(value)).append("','yyyy-MM-dd hh24:mi:ss')")
                                            .append(",");
                        } else {
                            sql.append(column.getColumn()).append("=").append(value).append(",");
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    LOGGER.info(e.getMessage());
                }
            }
        }

        sql.deleteCharAt(sql.length() - 1);
        sql.append(" WHERE ");

        if (CollectionUtils.isEmpty(pkColumns)) {
            return Collections.emptyList();
        }

        pkColumns.forEach(t -> {
            try {
                Object value = PropertyUtils.getProperty(dto, t.getProperty());
                if (value == null) {
                    sql.append(t.getColumn()).append("=").append("''").append(" AND ");
                } else {
                    if (value instanceof String) {
                        sql.append(t.getColumn()).append("=").append("'").append(value.toString()).append("'")
                                        .append(" AND ");
                    } else if (value instanceof Date) {
                        sql.append(t.getColumn()).append("=").append("to_date(").append("'")
                                        .append(DATE_FORMAT.format(value)).append("','yyyy-MM-dd hh24:mi:ss')")
                                        .append(" AND ");
                    } else {
                        sql.append(t.getColumn()).append("=").append(value).append(" AND ");
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                LOGGER.info(e.getMessage());
            }
        });

        sqlList.add(sql.substring(0, sql.length() - " AND ".length()));
        sqlList.addAll(getUpdateMultiLanguage(dto, Collections.emptyList()));

        return sqlList;
    }

    @Override
    public List<String> getFullUpdateSql(AuditDomain dto) {
        final StringBuilder sql = new StringBuilder();
        List<EntityColumn> pkColumns = new ArrayList<EntityColumn>();
        List<String> sqlList = new ArrayList<String>();
        Class<?> entityClass = dto.getClass();
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);

        sql.append("UPDATE ").append(entityTable.getName()).append(" ").append("set").append(" ");

        for (EntityColumn column : columnList) {
            if (column.isId()) {
                pkColumns.add(column);
                continue;
            }
            if ("last_update_date".equalsIgnoreCase(column.getColumn())) {
                sql.append(column.getColumn()).append("=").append("SYSDATE,");
                continue;
            }
            if ("object_version_number".equalsIgnoreCase(column.getColumn())) {
                sql.append(column.getColumn()).append("=").append(column.getColumn()).append("+1,");
                continue;
            }

            if (column.isUpdatable()) {
                try {
                    Object value = PropertyUtils.getProperty(dto, column.getProperty());
                    if (value != null) {
                        if (value instanceof String) {
                            value = ((String) value).replace("'", "''");
                            sql.append(column.getColumn()).append("=").append("'").append(value.toString()).append("'")
                                            .append(",");
                        } else if (value instanceof Date) {
                            sql.append(column.getColumn()).append("=").append("to_date(").append("'")
                                            .append(DATE_FORMAT.format(value)).append("','yyyy-MM-dd hh24:mi:ss')")
                                            .append(",");
                        } else {
                            sql.append(column.getColumn()).append("=").append(value).append(",");
                        }
                    } else {
                        if (!"java.lang.String".equals(column.getJavaType().getName())
                                        && !CREATE_FIELDS.contains(column.getColumn().toUpperCase())) {
                            sql.append(column.getColumn()).append("=").append("null").append(",");
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    LOGGER.info(e.getMessage());
                }
            }
        }

        sql.deleteCharAt(sql.length() - 1);
        sql.append(" WHERE ");

        if (CollectionUtils.isEmpty(pkColumns)) {
            return Collections.emptyList();
        }

        pkColumns.forEach(t -> {
            try {
                Object value = PropertyUtils.getProperty(dto, t.getProperty());
                if (value == null) {
                    sql.append(t.getColumn()).append("=").append("''").append(" AND ");
                } else {
                    if (value instanceof String) {
                        sql.append(t.getColumn()).append("=").append("'").append(value.toString()).append("'")
                                        .append(" AND ");
                    } else if (value instanceof Date) {
                        sql.append(t.getColumn()).append("=").append("to_date(").append("'")
                                        .append(DATE_FORMAT.format(value)).append("','yyyy-MM-dd hh24:mi:ss')")
                                        .append(" AND ");
                    } else {
                        sql.append(t.getColumn()).append("=").append(value).append(" AND ");
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                LOGGER.info(e.getMessage());
            }
        });

        sqlList.add(sql.substring(0, sql.length() - " AND ".length()));
        sqlList.addAll(getUpdateMultiLanguage(dto, Collections.emptyList()));

        return sqlList;
    }

    @Override
    public List<String> getInsertSql(AuditDomain dto) {
        final StringBuilder sql = new StringBuilder();
        final StringBuilder columnSql = new StringBuilder();
        final StringBuilder valueSql = new StringBuilder();

        List<String> sqlList = new ArrayList<String>();
        Class<?> entityClass = dto.getClass();
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);

        // Table Name
        sql.append("INSERT INTO ");
        sql.append(entityTable.getName());
        sql.append(" ");
        sql.append("(");

        // Table Column&Value
        columnList.forEach(t -> {
            if (t.isInsertable()) {
                try {
                    Object obj = PropertyUtils.getProperty(dto, t.getProperty());
                    if (obj != null) {
                        columnSql.append(t.getColumn()).append(",");
                        if (obj instanceof String) {
                            obj = ((String) obj).replace("'", "''");
                            valueSql.append("'").append(obj.toString()).append("'").append(",");
                        } else if (obj instanceof Date) {
                            valueSql.append("to_date(").append("'").append(DATE_FORMAT.format(obj))
                                            .append("','yyyy-MM-dd hh24:mi:ss')").append(",");
                        } else {
                            valueSql.append(obj).append(",");
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    LOGGER.info(e.getMessage());
                }
            }
        });

        columnSql.deleteCharAt(columnSql.length() - 1);
        valueSql.deleteCharAt(valueSql.length() - 1);
        sql.append(columnSql).append(") ").append("VALUES(").append(valueSql).append(")");
        sqlList.add(sql.toString());

        MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
        if (multiLanguageTable != null) {
            List<String> keys = new ArrayList<String>();
            List<Object> objs = new ArrayList<Object>();

            String tableName = entityTable.getMultiLanguageTableName();

            StringBuilder languageSql = new StringBuilder("INSERT INTO " + tableName + "(");
            for (EntityColumn field : EntityHelper.getPKColumns(entityClass)) {
                String columnName = field.getColumn();
                keys.add(columnName);
                try {
                    objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    LOGGER.info(e.getMessage());
                }
            }
            keys.add("LANG");
            // 占位符
            objs.add(null);

            Set<EntityColumn> allFields = EntityHelper.getColumns(entityClass);
            List<EntityColumn> multiFields = allFields.stream().filter(EntityColumn::isMultiLanguage).collect(toList());
            for (EntityColumn field : multiFields) {
                keys.add(field.getColumn());

                Map<String, Map<String, String>> _tls = dto.get_tls();
                if (_tls == null) {
                    // if multi language value not exists in __tls, then use value on current field
                    try {
                        objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        LOGGER.info(e.getMessage());
                    }
                    continue;
                } else {
                    Map<String, String> tls = _tls.get(field.getColumn());
                    if (tls == null) {
                        // if multi language value not exists in __tls, then use value on current field
                        try {
                            objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            LOGGER.info(e.getMessage());
                        }
                        continue;
                    }
                }

                // 占位符
                objs.add(null);
            }

            languageSql.append(StringUtils.join(keys, ","));
            languageSql.append(") VALUES (");

            List<Language> languages = LanguageHelper.languages();
            for (Language language : languages) {
                objs.set(objs.size() - multiFields.size() - 1, language.getCode());
                for (int i = 0; i < multiFields.size(); i++) {
                    int idx = objs.size() - multiFields.size() + i;
                    Map<String, Map<String, String>> _tls = dto.get_tls();
                    if (null != _tls) {
                        Map<String, String> tls = _tls.get(multiFields.get(i).getProperty());
                        if (tls != null) {
                            objs.set(idx, tls.get(language.getCode()));
                        }
                        // 当tls为null时,不设置值(使用field的值,旧模式)
                    }
                }

                StringBuilder tmpSql = new StringBuilder(languageSql);
                for (Object obj : objs) {
                    if (obj == null) {
                        tmpSql.append("''").append(",");
                    } else {
                        obj = ((String) obj).replace("'", "''");
                        tmpSql.append("'").append(obj).append("'").append(",");
                    }

                }
                tmpSql.deleteCharAt(tmpSql.length() - 1);

                tmpSql.append(")");
                sqlList.add(tmpSql.toString());
            }
        }
        return sqlList;
    }

    @Override
    public List<String> getReplaceSql(List<AuditDomain> list) {
        final StringBuilder sql = new StringBuilder();
        final StringBuilder columnSql = new StringBuilder();
        final StringBuilder valueSql = new StringBuilder();

        AuditDomain dto = list.get(0);
        List<String> sqlList = new ArrayList<String>();
        Class<?> entityClass = dto.getClass();
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        // Table Column
        columnList.forEach(t -> {
            if (t.isInsertable()) {
                columnSql.append(t.getColumn()).append(",");
            }
        });

        // Table Name
        Optional<EntityColumn> first = columnList.stream().filter(EntityColumn::isId).findFirst();
        if (!first.isPresent()) {
            throw new RuntimeException("object has no primary key set!");
        }
        EntityColumn pkColumn = first.get();
        sql.append("MERGE INTO ");
        sql.append(entityTable.getName());
        sql.append(" M USING");
        sql.append("(");
        // Value
        for (AuditDomain auditDomain : list) {
            valueSql.append(" SELECT ");
            for (EntityColumn t : columnList) {
                if (t.isInsertable()) {
                    try {
                        Object obj = PropertyUtils.getProperty(auditDomain, t.getProperty());
                        if (obj != null) {
                            if (obj instanceof String) {
                                obj = ((String) obj).replace("'", "''");
                                valueSql.append("'").append(obj.toString()).append("' ").append(t.getColumn())
                                                .append(" ,");
                            } else if (obj instanceof Date) {
                                valueSql.append("to_date(").append("'").append(DATE_FORMAT.format(obj))
                                                .append("','yyyy-MM-dd hh24:mi:ss')").append(" ").append(t.getColumn())
                                                .append(" ,");
                            } else {
                                valueSql.append(obj).append(" ").append(t.getColumn()).append(" ,");
                            }
                        } else {
                            if ("java.lang.String".equalsIgnoreCase(t.getJavaType().getName())) {
                                valueSql.append("''").append(t.getColumn()).append(" ,");
                            } else {
                                valueSql.append("null").append(" ").append(t.getColumn()).append(",");
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        LOGGER.info(e.getMessage());
                    }
                }
            }
            valueSql.deleteCharAt(valueSql.length() - 1);
            valueSql.append(" FROM DUAL ");
            valueSql.append("UNION");
        }
        valueSql.delete(valueSql.length() - 5, valueSql.length());
        sql.append(valueSql).append(") T ").append(" ON ( M.").append(pkColumn.getColumn()).append("= T.")
                        .append(pkColumn.getColumn()).append(")");
        sql.append(" WHEN MATCHED THEN UPDATE SET ");
        for (EntityColumn t : columnList) {
            if (t.isUpdatable() && !t.isId()) {
                sql.append(t.getColumn()).append("=").append("T.").append(t.getColumn()).append(",");
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        columnSql.deleteCharAt(columnSql.length() - 1);
        sql.append(" WHEN NOT MATCHED THEN INSERT (").append(columnSql).append(") VALUES (");
        for (EntityColumn t : columnList) {
            if (t.isInsertable()) {
                sql.append("T.").append(t.getColumn()).append(",");
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        sqlList.add(sql.toString());
        MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
        if (multiLanguageTable != null) {
            List<String> keys = new ArrayList<String>();
            List<Object> objs = new ArrayList<Object>();

            String tableName = entityTable.getMultiLanguageTableName();
            StringBuilder languageSql = new StringBuilder("MERGE INTO  " + tableName + " M USING (");
            for (EntityColumn field : EntityHelper.getPKColumns(entityClass)) {
                String columnName = field.getColumn();
                keys.add(columnName.toUpperCase());
                try {
                    objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    LOGGER.info(e.getMessage());
                }

            }
            keys.add("LANG");
            // 占位符
            objs.add(null);
            Set<EntityColumn> allFields = EntityHelper.getColumns(entityClass);
            List<EntityColumn> multiFields = allFields.stream().filter(EntityColumn::isMultiLanguage).collect(toList());
            for (EntityColumn field : multiFields) {
                keys.add(field.getColumn());
                Map<String, Map<String, String>> _tls = dto.get_tls();
                if (_tls == null) {
                    // if multi language value not exists in __tls, then use value on current field
                    try {
                        objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        LOGGER.info(e.getMessage());
                    }
                    continue;
                } else {
                    Map<String, String> tls = _tls.get(field.getColumn());
                    if (tls == null) {
                        // if multi language value not exists in __tls, then use value on current field
                        try {
                            objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            LOGGER.info(e.getMessage());
                        }
                        continue;
                    }
                }

                // 占位符
                objs.add(null);
            }

            StringBuilder tmpSql = new StringBuilder();
            List<Language> languages = LanguageHelper.languages();
            for (Language language : languages) {
                objs.set(objs.size() - multiFields.size() - 1, language.getCode());
                for (AuditDomain auditDomain : list) {
                    tmpSql.append(" SELECT ");
                    for (int i = 0; i < multiFields.size(); i++) {
                        int idx = objs.size() - multiFields.size() + i;
                        String property = multiFields.get(i).getProperty();
                        Map<String, Map<String, String>> _tls = auditDomain.get_tls();
                        if (null != _tls) {
                            Map<String, String> tls = _tls.get(property);
                            if (tls != null) {
                                objs.set(idx, tls.get(language.getCode()));
                            } else {
                                try {
                                    objs.set(idx, PropertyUtils.getProperty(auditDomain, property));
                                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                    LOGGER.info(e.getMessage());
                                }
                            }
                        } else {
                            try {
                                objs.set(idx, PropertyUtils.getProperty(auditDomain, property));
                            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                LOGGER.info(e.getMessage());
                            }
                        }
                    }
                    // 更换kid
                    for (EntityColumn field : EntityHelper.getPKColumns(entityClass)) {
                        try {
                            objs.set(0, PropertyUtils.getProperty(auditDomain, field.getProperty()));
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            LOGGER.info(e.getMessage());
                        }
                    }
                    for (int i = 0; i < keys.size(); i++) {
                        Object obj = objs.get(i);
                        obj = ((String) obj).replace("'", "''");
                        tmpSql.append("'").append(obj).append("' ").append(keys.get(i)).append(" ,");
                    }
                    tmpSql.deleteCharAt(tmpSql.length() - 1);
                    tmpSql.append(" FROM DUAL ");
                    tmpSql.append("UNION");
                }
            }
            tmpSql.delete(tmpSql.length() - 5, tmpSql.length());

            languageSql.append(tmpSql).append(") T ON (").append(" M.").append(keys.get(0)).append("=T.")
                            .append(keys.get(0)).append(" AND ").append("M.LANG=T.LANG )");
            languageSql.append(" WHEN MATCHED THEN UPDATE SET ");
            for (int i = 2; i < keys.size(); i++) {
                languageSql.append(keys.get(i)).append(" = ").append("T.").append(keys.get(i)).append(",");
            }
            languageSql.deleteCharAt(languageSql.length() - 1);
            languageSql.append(" WHEN NOT MATCHED THEN INSERT (");
            for (String key : keys) {
                languageSql.append(key).append(",");
            }
            languageSql.deleteCharAt(languageSql.length() - 1);
            languageSql.append(") VALUES (");
            for (String key : keys) {
                languageSql.append("T.").append(key).append(",");
            }
            languageSql.deleteCharAt(languageSql.length() - 1);
            languageSql.append(")");
            sqlList.add(languageSql.toString());
        }
        return sqlList;
    }

    @Override
    public String getDateSerializerSql(String input, Boolean isColumn) {
        if (StringUtils.isEmpty(input) || isColumn == null) {
            return "";
        }
        if (isColumn) {
            return " to_date(" + input + ",'yyyy-MM-dd hh24:mi:ss') ";
        } else {
            return " to_date('" + input + "','yyyy-MM-dd hh24:mi:ss') ";
        }
    }

    @Override
    public String getDateDeserializerSql(String columnName) {
        if (StringUtils.isEmpty(columnName)) {
            return "";
        }
        return " to_char(" + columnName + ",'yyyy-MM-dd hh24:mi:ss') ";
    }

    /**
     * 私有方法，更新时更新多语言表
     * 
     * @param dto
     * @param updateFields
     * @return
     */
    private List<String> getUpdateMultiLanguage(AuditDomain dto, List<String> updateFields) {
        Class<?> entityClass = dto.getClass();
        MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
        Set<EntityColumn> allFields = EntityHelper.getColumns(entityClass);
        Set<EntityColumn> multiFields =
                        allFields.stream().filter(EntityColumn::isMultiLanguage).collect(Collectors.toSet());
        if (multiLanguageTable == null || multiFields.size() == 0) {
            return Collections.emptyList();
        }

        List<String> sqlList = new ArrayList<String>();
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        String tableName = entityTable.getMultiLanguageTableName();

        final StringBuilder firstSql = new StringBuilder("UPDATE " + tableName + " SET ");
        final StringBuilder setSql = new StringBuilder();
        final StringBuilder keySql = new StringBuilder();

        // 获取主键拼接信息
        for (EntityColumn field : EntityHelper.getPKColumns(entityClass)) {
            try {
                Object value = PropertyUtils.getProperty(dto, field.getProperty());
                if (value != null) {
                    if (value instanceof String) {
                        keySql.append(field.getColumn()).append("=").append("'").append(value.toString()).append("'")
                                        .append(" AND ");
                    } else if (value instanceof Date) {
                        keySql.append(field.getColumn()).append("=").append("to_date(").append("'")
                                        .append(DATE_FORMAT.format(value)).append("','yyyy-MM-dd hh24:mi:ss')")
                                        .append(" AND ");
                    } else {
                        keySql.append(field.getColumn()).append("=").append(value).append(" AND ");
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                LOGGER.info(e.getMessage());
            }
        }

        if (keySql.length() == 0) {
            return Collections.emptyList();
        }

        // 没有特殊备注TLS信息时，默认只更新当前语言环境的数据
        if (null == dto.get_tls() || dto.get_tls().isEmpty()) {
            for (EntityColumn column : multiFields) {
                if (CollectionUtils.isNotEmpty(updateFields)
                                && updateFields.stream().noneMatch(t -> t.equalsIgnoreCase(column.getProperty()))) {
                    continue;
                }

                try {
                    Object value = PropertyUtils.getProperty(dto, column.getProperty());
                    if (value != null) {
                        value = ((String) value).replace("'", "''");
                        setSql.append(column.getColumn()).append("=").append("'").append(value.toString()).append("'")
                                        .append(",");
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    LOGGER.info(e.getMessage());
                }
            }

            if (setSql.length() == 0) {
                return Collections.emptyList();
            }
            // 删除末尾逗号
            setSql.deleteCharAt(setSql.length() - 1);

            setSql.append(" WHERE ");
            setSql.append("LANG='").append(MtLanguageHelper.language()).append("' AND ");
            // 拼接主键信息
            sqlList.add(String.valueOf(firstSql) + setSql + keySql.substring(0, keySql.length() - " AND ".length()));
        }
        // 特殊备注TLS信息时，根据TLS信息进行多语言环境的数据修改
        else {
            List<MtMultiLanguageVO> infos = new ArrayList<MtMultiLanguageVO>();
            for (Map.Entry<String, Map<String, String>> columns : dto.get_tls().entrySet()) {
                for (Map.Entry<String, String> tls : columns.getValue().entrySet()) {
                    // 获取需要修改的语言环境
                    infos.add(new MtMultiLanguageVO(FieldNameUtils.camel2Underline(columns.getKey(), true),
                                    tls.getKey(), tls.getValue()));
                }
            }
            List<String> langs = infos.stream().map(MtMultiLanguageVO::getLang).distinct().collect(toList());
            // 拼接不同语言环境的数据
            for (String lang : langs) {
                // 获取不同语言环境的数据
                List<MtMultiLanguageVO> langInfo =
                                infos.stream().filter(t -> lang.equals(t.getLang())).collect(toList());
                // 拼接修改的值
                setSql.delete(0, setSql.length());
                for (MtMultiLanguageVO ever : langInfo) {
                    String value = ever.getValues().replace("'", "''");
                    setSql.append(ever.getColoumName()).append("=").append("'").append(value).append("'").append(",");
                }
                // 删除末尾逗号
                setSql.deleteCharAt(setSql.length() - 1);

                setSql.append(" WHERE ");
                setSql.append("LANG='").append(lang).append("' AND ");
                // 拼接主键信息
                sqlList.add(String.valueOf(firstSql) + setSql
                                + keySql.substring(0, keySql.length() - " AND ".length()));
            }
        }
        return sqlList;
    }

    @Override
    public <T extends AuditDomain> int[] batchInsertTarzan(List<T> list) {
        throw new CommonException("Unsupport ORACLE database;");
    }

    @Override
    public <T extends AuditDomain> int[] batchInsertTarzan(List<T> list, int batchSize) {
        throw new CommonException("Unsupport ORACLE database;");
    }

    @Override
    public <T extends AuditDomain> int[] batchInsertTarzanWithPrimaryKey(List<T> list, int batchSize) {
        throw new CommonException("Unsupport ORACLE database;");
    }

    @Override
    public int[] batchDeleteTarzan(List<String> idList, Class<?> entityClass) {
        throw new CommonException("Unsupport ORACLE database;");
    }

    @Override
    public int[] batchDeleteTarzan(List<String> idList, Class<?> entityClass, int batchSize) {
        throw new CommonException("Unsupport ORACLE database;");
    }

    @Override
    public <T extends AuditDomain> int[] batchUpdateTarzan(List<T> list) {
        throw new CommonException("Unsupport ORACLE database;");
    }

    @Override
    public <T extends AuditDomain> int[] batchUpdateTarzan(List<T> list, int batchSize) {
        throw new CommonException("Unsupport ORACLE database;");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends AuditDomain> void batchUpdateSelective(List<T> list, int batchSize) throws DataAccessException {
        throw new CommonException("Unsupport ORACLE database;");
    }
}
