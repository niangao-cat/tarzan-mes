package io.tarzan.common.infra.repository.impl;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.Id;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.jackson.serializer.DateSerializer;
import org.hzero.core.util.FieldNameUtils;
import org.hzero.mybatis.domian.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.domain.EntityColumn;
import io.choerodon.mybatis.domain.EntityTable;
import io.choerodon.mybatis.helper.EntityHelper;
import io.choerodon.mybatis.helper.LanguageHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.sys.SequenceInfo;
import io.tarzan.common.domain.util.MtLanguageHelper;
import io.tarzan.common.domain.util.MtSqlHelper;
import io.tarzan.common.domain.vo.MtMultiLanguageVO;
import io.tarzan.common.infra.jdbc.TarzanBatchUpdateUtils;

/**
 * 注意该类不允许加Component注解
 * 
 * @author : MrZ
 * @date : 2020-02-25 18:01
 **/
public class MtCustomMysqlRepositoryImpl implements MtCustomDbRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateSerializer.class);
    private static final String VALUE_SQL = "select last_insert_id()";
    private static final String TABLE_NAME = "mt_sys_sequence";
    private static final String COLUMN_NAME = "current_value";
    private static final List<String> CREATE_FIELDS = Arrays.asList("CREATED_BY", "CREATION_DATE");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Value("${hwms.system.suffix:.1}")
    private String suffix;
    private Long increment = 1L;
    private JdbcTemplate jdbcTemplate;

    public MtCustomMysqlRepositoryImpl() {}

    @Override
    public boolean needPaddingId() {
        return false;
    }

    @SuppressWarnings("unchecked")
    private MtCustomDbRepository self() {
        return (MtCustomDbRepository) AopContext.currentProxy();
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
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

    @Override
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

        Long nextId = this.jdbcTemplate.query(VALUE_SQL, rs -> {
            if (!rs.next()) {
                throw new DataAccessResourceFailureException("last_insert_id() failed after executing an update");
            }
            return rs.getLong(1);
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
                        pkSql.append(field.getColumn()).append("=").append("'").append(DATE_FORMAT.format(obj))
                                        .append("'").append(" AND ");
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
                sql.append(column.getColumn()).append("=").append("CURRENT_TIMESTAMP,");
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
                            sql.append(column.getColumn()).append("=").append("'").append(DATE_FORMAT.format(value))
                                            .append("'").append(",");
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
                        sql.append(t.getColumn()).append("=").append("'").append(DATE_FORMAT.format(value)).append("'")
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
                sql.append(column.getColumn()).append("=").append("CURRENT_TIMESTAMP,");
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
                            sql.append(column.getColumn()).append("=").append("'").append(DATE_FORMAT.format(value))
                                            .append("'").append(",");
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
                        sql.append(t.getColumn()).append("=").append("'").append(DATE_FORMAT.format(value)).append("'")
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
                            valueSql.append("'").append(DATE_FORMAT.format(obj)).append("'").append(",");
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

        // Table Name
        sql.append("REPLACE INTO ");
        sql.append(entityTable.getName());
        sql.append("(");

        // Table Column
        columnList.forEach(t -> {
            if (t.isInsertable()) {
                columnSql.append(t.getColumn()).append(",");
            }
        });
        columnSql.deleteCharAt(columnSql.length() - 1);
        sql.append(columnSql).append(") ").append("VALUES");

        // Value
        for (AuditDomain auditDomain : list) {
            valueSql.append("(");
            for (EntityColumn t : columnList) {
                if (t.isInsertable()) {
                    try {
                        Object obj = PropertyUtils.getProperty(auditDomain, t.getProperty());
                        if (obj != null) {
                            if (obj instanceof String) {
                                obj = ((String) obj).replace("'", "''");
                                valueSql.append("'").append(obj.toString()).append("'").append(",");
                            } else if (obj instanceof Date) {
                                valueSql.append("'").append(DATE_FORMAT.format(obj)).append("'").append(",");
                            } else {
                                valueSql.append(obj).append(",");
                            }
                        } else {
                            if ("java.lang.String".equalsIgnoreCase(t.getJavaType().getName())) {
                                valueSql.append("''").append(",");
                            } else {
                                valueSql.append("null").append(",");
                            }

                        }
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        LOGGER.info(e.getMessage());
                    }
                }
            }
            valueSql.deleteCharAt(valueSql.length() - 1);
            valueSql.append("),");
        }
        valueSql.deleteCharAt(valueSql.length() - 1);
        sql.append(valueSql);
        sqlList.add(sql.toString());

        MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
        if (multiLanguageTable != null) {
            List<String> keys = new ArrayList<String>();
            List<Object> objs = new ArrayList<Object>();

            String tableName = entityTable.getMultiLanguageTableName();

            StringBuilder languageSql = new StringBuilder("REPLACE  INTO " + tableName + "(");
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
            languageSql.append(") VALUES ");
            StringBuilder tmpSql = new StringBuilder();
            List<Language> languages = LanguageHelper.languages();
            for (Language language : languages) {
                objs.set(objs.size() - multiFields.size() - 1, language.getCode());
                for (AuditDomain auditDomain : list) {
                    tmpSql.append("(");
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
                    for (Object obj : objs) {
                        if (obj == null) {
                            tmpSql.append("''").append(",");
                        } else {
                            obj = ((String) obj).replace("'", "''");
                            tmpSql.append("'").append(obj).append("'").append(",");
                        }

                    }
                    tmpSql.deleteCharAt(tmpSql.length() - 1);
                    tmpSql.append("),");
                }

            }
            tmpSql.deleteCharAt(tmpSql.length() - 1);
            sqlList.add(languageSql.append(tmpSql).toString());
        }
        return sqlList;
    }

    @Override
    public String getDateSerializerSql(String input, Boolean isColumn) {
        if (StringUtils.isEmpty(input) || isColumn == null) {
            return "";
        }
        if (isColumn) {
            return " DATE_FORMAT(" + input + ",'%Y-%m-%d %H:%i:%S') ";
        } else {
            return " DATE_FORMAT('" + input + "','%Y-%m-%d %H:%i:%S') ";
        }
    }

    @Override
    public String getDateDeserializerSql(String columnName) {
        if (StringUtils.isEmpty(columnName)) {
            return "";
        }
        return " DATE_FORMAT(" + columnName + ",'%Y-%m-%d %H:%i:%S') ";
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
                        keySql.append(field.getColumn()).append("=").append("'").append(DATE_FORMAT.format(value))
                                        .append("'").append(" AND ");
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

    /**
     * 使用预编译方式的批量新增
     *
     * @param list 需要新增的数据
     * @param <T> 泛型
     * @throws DataAccessException 数据库异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends AuditDomain> int[] batchInsertTarzan(List<T> list) throws DataAccessException {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return batchInsertTarzan(list, list.size());
    }

    /**
     * 使用预编译方式的批量新增(分批)
     *
     * @param list 需要新增的数据
     * @param <T> 泛型
     * @param batchSize 分批大小
     * @throws DataAccessException 数据库异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends AuditDomain> int[] batchInsertTarzan(List<T> list, int batchSize) throws DataAccessException {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        return batchInsertBasic(list, batchSize, false);
    }

    /**
     * 使用预编译方式的批量新增(分批)-外部传入主键
     *
     * @param list 需要新增的数据
     * @param <T> 泛型
     * @param batchSize 分批大小
     * @throws DataAccessException 数据库异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends AuditDomain> int[] batchInsertTarzanWithPrimaryKey(List<T> list, int batchSize)
                    throws DataAccessException {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        return batchInsertBasic(list, batchSize, true);
    }

    private <T extends AuditDomain> int[] batchInsertBasic(List<T> list, int batchSize, boolean isOutsideId) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        // 填充主键和CID信息
        paddingListField(list, true, isOutsideId);

        Class<?> entityClass = list.get(0).getClass();

        // 获取表信息
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        // 获取字段信息
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        // 如果没有列，直接返回结果
        if (CollectionUtils.isEmpty(columns)) {
            return null;
        }

        final StringBuilder sql = new StringBuilder();
        final StringBuilder columnSql = new StringBuilder();
        final StringBuilder valueSql = new StringBuilder();

        // Table Name
        sql.append("INSERT INTO ");
        sql.append(entityTable.getName());
        sql.append(" (");

        List<Integer> paramTypeList = new ArrayList<>(columns.size());
        for (EntityColumn entityColumn : columns) {
            // Table Column
            columnSql.append(entityColumn.getColumn()).append(",");
            valueSql.append("?").append(",");
            // paramTypes
            paramTypeList.add(getDbTypeByJavaType(entityColumn));
        }

        // delete last point ","
        columnSql.deleteCharAt(columnSql.length() - 1);
        valueSql.deleteCharAt(valueSql.length() - 1);
        sql.append(columnSql).append(") ").append("VALUES(").append(valueSql).append(")");

        // paramList
        List<Object[]> paramList = new ArrayList<>(list.size());
        for (T entity : list) {
            paddingDefaultValue(entity);
            List<Object> values = new ArrayList<>(columns.size());
            for (EntityColumn entityColumn : columns) {
                try {
                    values.add(PropertyUtils.getProperty(entity, entityColumn.getProperty()));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    LOGGER.error(e.getMessage());
                }
            }

            paramList.add(values.toArray());
        }

        int[] paramTypes = paramTypeList.stream().mapToInt(Integer::valueOf).toArray();

        int[] result = executeBatchUpdate(sql.toString(), paramList, paramTypes, batchSize);
        // 插入多语言
        batchInsertMultiLanguageTarzan(list, batchSize);
        return result;
    }

    private <T extends AuditDomain> void batchInsertMultiLanguageTarzan(List<T> list, int batchSize)
                    throws DataAccessException {
        Class<?> entityClass = list.get(0).getClass();
        // 获取表信息
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        // 获取字段信息
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);

        // deal MultiLanguage
        MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
        if (multiLanguageTable != null) {
            String tableName = entityTable.getMultiLanguageTableName();

            // pk default only one
            EntityColumn pkEntityColumn = new ArrayList<>(EntityHelper.getPKColumns(entityClass)).get(0);

            StringBuilder languageSql = new StringBuilder("INSERT INTO " + tableName + "(");

            List<String> keys = new ArrayList<>();
            List<String> values = new ArrayList<>();

            keys.add(pkEntityColumn.getColumn());
            values.add("?");
            keys.add("LANG");
            values.add("?");

            List<EntityColumn> multiColumns =
                            columns.stream().filter(EntityColumn::isMultiLanguage).collect(Collectors.toList());

            List<Integer> paramTypeList = new ArrayList<>(multiColumns.size() + 2);
            paramTypeList.add(Types.BIGINT);
            paramTypeList.add(Types.VARCHAR);
            for (EntityColumn entityColumn : multiColumns) {
                keys.add(entityColumn.getColumn());
                values.add("?");
                paramTypeList.add(Types.VARCHAR);
            }

            languageSql.append(StringUtils.join(keys, ","));
            languageSql.append(") VALUES (");
            languageSql.append(StringUtils.join(values, ","));
            languageSql.append(")");

            // paramList
            List<Language> languages = MtLanguageHelper.languages();
            List<Object[]> paramList = new ArrayList<>(list.size() * languages.size());
            for (T entity : list) {
                Map<String, Map<String, String>> tlsMap = entity.get_tls();

                try {
                    for (Language language : languages) {
                        List<Object> multiValues = new ArrayList<>();
                        // pkColumn value
                        multiValues.add(PropertyUtils.getProperty(entity, pkEntityColumn.getProperty()));
                        // Lang value
                        multiValues.add(language.getCode());

                        for (EntityColumn multiColumn : multiColumns) {
                            if (MapUtils.isEmpty(tlsMap)) {
                                // multiColumn value
                                multiValues.add(PropertyUtils.getProperty(entity, multiColumn.getProperty()));
                            } else {
                                Map<String, String> tlMap = tlsMap.get(multiColumn.getProperty());

                                if (MapUtils.isEmpty(tlMap) || StringUtils.isEmpty(tlMap.get(language.getCode()))) {
                                    // multiColumn value
                                    multiValues.add(PropertyUtils.getProperty(entity, multiColumn.getProperty()));
                                } else {
                                    // multiColumn value
                                    multiValues.add(tlMap.get(language.getCode()));
                                }
                            }
                        }

                        paramList.add(multiValues.toArray());
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    LOGGER.error(e.getMessage());
                }
            }

            int[] paramTypes = paramTypeList.stream().mapToInt(Integer::valueOf).toArray();
            executeBatchUpdate(languageSql.toString(), paramList, paramTypes, batchSize);
        }
    }

    /**
     * 使用预编译方式的批量删除
     *
     * @param idList 需要删除数据的主键
     * @param entityClass 实体的类
     * @throws DataAccessException 数据库异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int[] batchDeleteTarzan(List<String> idList, Class<?> entityClass) throws DataAccessException {
        if (CollectionUtils.isEmpty(idList)) {
            return null;
        }
        return batchDeleteTarzan(idList, entityClass, idList.size());
    }

    /**
     * 使用预编译方式的批量删除(分批)
     *
     * @param idList 需要删除数据的主键
     * @param entityClass 实体的类
     * @param batchSize 分批大小
     * @throws DataAccessException 数据库异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int[] batchDeleteTarzan(List<String> idList, Class<?> entityClass, int batchSize)
                    throws DataAccessException {
        if (CollectionUtils.isEmpty(idList)) {
            return null;
        }

        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);

        // pk default only one
        List<EntityColumn> pkColumnList = new ArrayList<>(EntityHelper.getPKColumns(entityClass));
        String pkColumn = pkColumnList.get(0).getColumn();

        // paramList
        List<Object[]> paramList = new ArrayList<>(idList.size());
        for (String deleteId : idList) {
            paramList.add(new Object[] {deleteId});
        }

        String deleteSql = "DELETE FROM " + entityTable.getName() + " WHERE " + pkColumn + " = ?";
        int[] result = executeBatchUpdate(deleteSql, paramList, new int[] {Types.VARCHAR}, batchSize);

        // deal MultiLanguage
        MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
        if (multiLanguageTable != null) {
            String deleteLanguageSql =
                            "DELETE FROM " + entityTable.getMultiLanguageTableName() + " WHERE " + pkColumn + " = ?";
            executeBatchUpdate(deleteLanguageSql, paramList, new int[] {Types.VARCHAR}, batchSize);
        }
        return result;
    }


    public static void paddingDefaultValue(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] fs = clazz.getDeclaredFields();
        for (Field f : fs) {
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            // 设置属性是可以访问的(私有的也可以)
            ReflectionUtils.makeAccessible(f);
            if (f.getType().isAssignableFrom(java.lang.String.class)) {
                // 得到此属性的值
                Object val;
                try {
                    val = f.get(obj);
                    if (val == null) {
                        ReflectionUtils.setField(f, obj, "");
                    }
                } catch (Exception e) {
                    LOGGER.debug(e.getMessage());
                }
            }
        }
    }

    /**
     * 新增or更新：都会处理cid，所以外部无论新增还是更新都无需传入cid 新增：insertFlag = Y，会判断
     * isOutsideId，若为Y，则用传入的主键id，否则自动该方法自动设置主键id
     *
     * @date 2021/2/8
     */
    private <T extends AuditDomain> void paddingListField(List<T> list, boolean insertFlag, boolean isOutsideId) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        Class<?> entityClass = list.get(0).getClass();
        Date now = new Date();
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = userDetails == null ? -2 : userDetails.getUserId();

        // 申请主键和CID
        SequenceInfo sequenceInfo = MtSqlHelper.getSequenceInfo(entityClass);
        // 获取特殊主键和CID
        List<String> pkIds = new ArrayList<>();
        List<String> cidIds = new ArrayList<>();
        Field idField = null;
        Field cidField = null;

        if (sequenceInfo.isCustomPrimary() || needPaddingId()) {
            // 先遍历一次获取需要设置的列
            Field[] fields = entityClass.getDeclaredFields();
            for (Field field : fields) {
                // 如果外界传入主键或者更新,这两种情况下不需要进行主键设置
                if (insertFlag && !isOutsideId && field.getAnnotation(Id.class) != null) {
                    idField = field;
                } else if (field.getAnnotation(Cid.class) != null) {
                    cidField = field;
                }
            }
            // 注意这里需要使用新事务，不能直接同类调用
            if (null != idField) {
                // 注意外界传入主键或者更新，根本没有查询主键字段，所以这里不会执行
                pkIds = self().getNextKeys(sequenceInfo.getPrimarySequence(), list.size());
            }
            if (null != cidField) {
                cidIds = self().getNextKeys(sequenceInfo.getCidSequence(), list.size());
            }
        }

        // 修改数据
        for (int i = 0; i < list.size(); i++) {
            T ever = list.get(i);
            // 关闭乐观锁
            ever.setObjectVersionNumber(1L);
            // 设置基础字段
            if (insertFlag) {
                ever.setCreatedBy(userId);
                ever.setCreationDate(now);
            }
            ever.setLastUpdatedBy(userId);
            ever.setLastUpdateDate(now);
            // 产品自制主键生成方式,需要额外设置主键和CID
            if (sequenceInfo.isCustomPrimary()) {
                try {
                    // 设置主键值,这里更新时是没有找主键字段的，所以可能为空
                    if (null != idField) {
                        ReflectionUtils.makeAccessible(idField);
                        if (String.class.isAssignableFrom(idField.getType())) {
                            if (null == idField.get(ever) || StringUtils.isEmpty(idField.get(ever).toString())) {
                                String pid = pkIds.get(i);
                                ReflectionUtils.setField(idField, ever, String.valueOf(pid));
                            }
                        } else if (Long.class.isAssignableFrom(idField.getType())) {
                            if (null == idField.get(ever) || (long) idField.get(ever) == 0) {
                                String pid = pkIds.get(i);
                                ReflectionUtils.setField(idField, ever, Long.valueOf(pid));
                            }
                        } else {
                            if (null == idField.get(ever)) {
                                String pid = pkIds.get(i);
                                ReflectionUtils.setField(idField, ever, pid);
                            }
                        }
                    }
                    // 设置CID值,这里存在表结构没有cid字段
                    if (null != cidField) {
                        ReflectionUtils.makeAccessible(cidField);
                        String nextCid = cidIds.get(i);
                        ReflectionUtils.setField(cidField, ever, Long.parseLong(nextCid));
                    }
                } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    LOGGER.error("set pid/cid field error: " + e.getMessage());
                }
            }
        }
    }

    /**
     * 使用预编译方式的批量更新,全量更新,暂时无法支持局部更新
     *
     * @param list 需要更新的数据
     * @param <T> 泛型
     * @throws DataAccessException 数据库异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends AuditDomain> int[] batchUpdateTarzan(List<T> list) throws DataAccessException {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return batchUpdateTarzan(list, list.size());
    }

    /**
     * 使用预编译方式的批量更新,全量更新,暂时无法支持局部更新(分批)
     *
     * @param list 需要更新的数据
     * @param batchSize 分批大小
     * @param <T> 泛型
     * @throws DataAccessException 数据库异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends AuditDomain> int[] batchUpdateTarzan(List<T> list, int batchSize) throws DataAccessException {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 填充主键和CID信息
        paddingListField(list, false, true);

        Class<?> entityClass = list.get(0).getClass();

        // 获取表信息
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        // 获取字段信息
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        // 新增的字段是不应该修改的
        List<String> createInfo = Arrays.asList(AuditDomain.FIELD_CREATED_BY, AuditDomain.FIELD_CREATION_DATE);
        columns = columns.stream().filter(t -> !createInfo.contains(t.getProperty())).collect(Collectors.toSet());
        // 如果没有列，直接返回结果
        if (CollectionUtils.isEmpty(columns)) {
            return null;
        }

        final StringBuilder sql = new StringBuilder();
        final StringBuilder columnSql = new StringBuilder();

        EntityColumn pkColumn = null;

        // Table Name
        sql.append("UPDATE ");
        sql.append(entityTable.getName());
        sql.append(" SET ");

        List<Integer> paramTypeList = new ArrayList<>(columns.size());
        for (EntityColumn entityColumn : columns) {
            if (entityColumn.isId()) {
                // 主键需要最后拼接
                pkColumn = entityColumn;
                continue;
            }
            // Table Column
            columnSql.append(entityColumn.getColumn()).append("=?,");
            // paramTypes
            paramTypeList.add(getDbTypeByJavaType(entityColumn));
        }

        // delete last point ","
        columnSql.deleteCharAt(columnSql.length() - 1);

        if (null == pkColumn) {
            return null;
        }
        // 最后拼接主键
        paramTypeList.add(getDbTypeByJavaType(pkColumn));

        // 拼接主键
        sql.append(columnSql).append(" WHERE ").append(pkColumn.getColumn()).append("=?");

        // paramList
        List<Object[]> paramList = new ArrayList<>(list.size());
        for (T entity : list) {
            paddingDefaultValue(entity);
            List<Object> values = new ArrayList<>(columns.size());
            for (EntityColumn entityColumn : columns) {
                // 主键需要最后拼接
                if (entityColumn.isId()) {
                    continue;
                }
                try {
                    values.add(PropertyUtils.getProperty(entity, entityColumn.getProperty()));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    LOGGER.error(e.getMessage());
                }
            }
            try {
                values.add(PropertyUtils.getProperty(entity, pkColumn.getProperty()));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                LOGGER.error(e.getMessage());
            }

            paramList.add(values.toArray());
        }

        int[] paramTypes = paramTypeList.stream().mapToInt(Integer::valueOf).toArray();

        int[] result = executeBatchUpdate(sql.toString(), paramList, paramTypes, batchSize);
        // 更新多语言
        batchUpdateMultiLanguageTarzan(list, batchSize);
        return result;
    }

    private int[] executeBatchUpdate(String sql, List<Object[]> batchArgs, int[] argTypes, int batchSize) {
        return TarzanBatchUpdateUtils.executeBatchUpdateWithBatchSize(sql, batchArgs, argTypes, batchSize,
                        jdbcTemplate);
    }

    private <T extends AuditDomain> void batchUpdateMultiLanguageTarzan(List<T> list, int batchSize)
                    throws DataAccessException {
        Class<?> entityClass = list.get(0).getClass();
        // 获取表信息
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        // 获取字段信息
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);

        // deal MultiLanguage
        MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
        if (multiLanguageTable != null) {
            String tableName = entityTable.getMultiLanguageTableName();

            // pk default only one
            EntityColumn pkEntityColumn = new ArrayList<>(EntityHelper.getPKColumns(entityClass)).get(0);

            // 获取当前语境
            String language = MtLanguageHelper.language();

            // 获取多语言字段
            List<EntityColumn> multiColumns =
                            columns.stream().filter(EntityColumn::isMultiLanguage).collect(Collectors.toList());
            // 当前语境全量更新
            final StringBuilder sql = new StringBuilder();
            sql.append("UPDATE ");
            sql.append(tableName);
            sql.append(" SET ");
            // paramType
            List<Integer> paramTypeList = new ArrayList<>(multiColumns.size() + 2);
            for (EntityColumn multiColumn : multiColumns) {
                sql.append(multiColumn.getColumn()).append("=?,");
                paramTypeList.add(Types.VARCHAR);
            }
            sql.deleteCharAt(sql.length() - 1);
            paramTypeList.add(Types.BIGINT);
            paramTypeList.add(Types.VARCHAR);
            sql.append(" WHERE ").append(pkEntityColumn.getColumn()).append("=? AND LANG=?");

            // paramList
            List<Object[]> paramList = new ArrayList<>();
            for (T entity : list) {
                // 这里只更新当前语境，因为使用全量更新时,当前语境一定是全量更新的
                List<Object> multiValues = new ArrayList<>();
                for (EntityColumn multiColumn : multiColumns) {
                    boolean existTls = entity.get_tls() != null
                                    && entity.get_tls().containsKey(multiColumn.getProperty())
                                    && entity.get_tls().get(multiColumn.getProperty()).containsKey(language);
                    Object multiValue = null;

                    if (existTls) {
                        multiValue = entity.get_tls().get(multiColumn.getProperty()).get(language);
                    } else {
                        try {
                            multiValue = PropertyUtils.getProperty(entity, multiColumn.getProperty());
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            LOGGER.error(e.getMessage());
                        }
                    }

                    multiValues.add(multiValue);
                }

                // pkColumn value
                try {
                    multiValues.add(PropertyUtils.getProperty(entity, pkEntityColumn.getProperty()));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    LOGGER.error(e.getMessage());
                }

                // Lang value
                multiValues.add(language);

                paramList.add(multiValues.toArray());
            }

            // 执行更新当前语境
            int[] paramTypes = paramTypeList.stream().mapToInt(Integer::valueOf).toArray();
            executeBatchUpdate(sql.toString(), paramList, paramTypes, batchSize);

            // 其他语境需要根据传入tls进行更新,预编译方式无法进行局部更新，所以选择一个一个列去更新,这里不会写了
            Map<String, List<Object[]>> langInfo = new HashMap<>(multiColumns.size());
            for (T entity : list) {
                if (entity.get_tls() != null) {
                    // pkColumn value
                    Object pkValue = null;
                    try {
                        pkValue = PropertyUtils.getProperty(entity, pkEntityColumn.getProperty());
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        LOGGER.error(e.getMessage());
                    }
                    for (Map.Entry<String, Map<String, String>> outer : entity.get_tls().entrySet()) {
                        // 初始对象
                        if (!langInfo.containsKey(outer.getKey())) {
                            langInfo.put(outer.getKey(), new ArrayList<>());
                        }

                        for (Map.Entry<String, String> inner : outer.getValue().entrySet()) {
                            // 当前语境的不需要改了
                            if (!language.equals(inner.getKey())) {
                                langInfo.get(outer.getKey())
                                                .add(new Object[] {inner.getValue(), pkValue, inner.getKey()});
                            }
                        }
                    }
                }
            }

            // 根据数据拼接多个SQL语句
            for (EntityColumn multiColumn : multiColumns) {
                if (langInfo.containsKey(multiColumn.getProperty())) {
                    String langSql = "UPDATE " + tableName + " SET " + multiColumn.getColumn() + "=?" + " WHERE "
                                    + pkEntityColumn.getColumn() + "=? AND LANG=?";
                    // 一个多语言列执行一次SQL
                    executeBatchUpdate(langSql, langInfo.get(multiColumn.getProperty()),
                                    new int[] {Types.VARCHAR, Types.BIGINT, Types.VARCHAR}, batchSize);
                }

            }

        }
    }

    private int getDbTypeByJavaType(EntityColumn entityColumn) {
        if (entityColumn.getJavaType().isAssignableFrom(java.lang.String.class)) {
            return Types.VARCHAR;
        } else if (entityColumn.getJavaType().isAssignableFrom(java.util.Date.class)) {
            return Types.TIMESTAMP;
        } else if (entityColumn.getJavaType().isAssignableFrom(java.time.LocalDate.class)) {
            return Types.DATE;
        } else if (entityColumn.getJavaType().isAssignableFrom(java.lang.Double.class)) {
            return Types.DECIMAL;
        } else if (entityColumn.getJavaType().isAssignableFrom(java.lang.Long.class)) {
            return Types.BIGINT;
        } else {
            return Types.VARCHAR;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends AuditDomain> void batchUpdateSelective(List<T> list, int batchSize) throws DataAccessException {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 填充CID信息
        paddingListField(list, false, true);

        Class<?> entityClass = list.get(0).getClass();

        // 获取表信息
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        // 获取字段信息
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        // 新增的字段是不应该修改的
        List<String> createInfo = Arrays.asList(AuditDomain.FIELD_CREATED_BY, AuditDomain.FIELD_CREATION_DATE);
        columns = columns.stream().filter(t -> !createInfo.contains(t.getProperty())).collect(Collectors.toSet());
        // 如果没有列，直接返回结果
        if (CollectionUtils.isEmpty(columns)) {
            return;
        }

        // 必需设置主键
        Optional<EntityColumn> pkColumnOp = columns.stream().filter(EntityColumn::isId).findFirst();
        if (!pkColumnOp.isPresent()) {
            return;
        }
        EntityColumn pkColumn = pkColumnOp.get();

        // 存储sql和参数列的关系
        Map<String, List<Integer>> paramTypeMap = new HashMap<>(columns.size());
        // 存储sql和参数值的关系
        Map<String, List<Object[]>> paramListMap = new HashMap<>(columns.size());

        final StringBuilder baseSql = new StringBuilder();
        // Table Name
        baseSql.append("UPDATE ");
        baseSql.append(entityTable.getName());
        baseSql.append(" SET ");

        // 循环值
        for (T ever : list) {
            final StringBuilder columnSql = new StringBuilder();
            List<Integer> paramTypeList = new ArrayList<>(columns.size());
            List<Object> values = new ArrayList<>(columns.size());
            for (EntityColumn entityColumn : columns) {
                if (entityColumn.isId()) {
                    // 主键需要最后拼接
                    continue;
                }

                // 如果属性值为空，表示不更新该字段
                try {
                    Object value = PropertyUtils.getProperty(ever, entityColumn.getProperty());
                    if (null == value) {
                        continue;
                    } else {
                        values.add(value);
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new DataAccessResourceFailureException(e.getMessage());
                }

                // Table Column
                columnSql.append(entityColumn.getColumn()).append("=?,");
                // paramTypes
                paramTypeList.add(getDbTypeByJavaType(entityColumn));
            }
            // delete last point ","
            columnSql.deleteCharAt(columnSql.length() - 1);
            // 最后拼接主键
            paramTypeList.add(getDbTypeByJavaType(pkColumn));
            // 拼接主键
            final StringBuilder sql = new StringBuilder();
            sql.append(baseSql).append(columnSql).append(" WHERE ").append(pkColumn.getColumn()).append("=?");
            // 补充主键值
            try {
                values.add(PropertyUtils.getProperty(ever, pkColumn.getProperty()));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new DataAccessResourceFailureException(e.getMessage());
            }
            // 记录值
            if (paramTypeMap.containsKey(sql.toString()) && paramListMap.containsKey(sql.toString())) {
                paramListMap.get(sql.toString()).add(values.toArray());
            } else {
                paramTypeMap.put(sql.toString(), paramTypeList);
                List<Object[]> paramValues = new ArrayList<>();
                paramValues.add(values.toArray());
                paramListMap.put(sql.toString(), paramValues);
            }
        }

        for (Map.Entry<String, List<Integer>> ever : paramTypeMap.entrySet()) {
            int[] paramTypes = ever.getValue().stream().mapToInt(Integer::valueOf).toArray();
            if (!paramListMap.containsKey(ever.getKey())) {
                throw new DataAccessResourceFailureException("Type and number of numbers do not match");
            }
            List<Object[]> valueList = paramListMap.get(ever.getKey());
            TarzanBatchUpdateUtils.executeBatchUpdateWithBatchSize(ever.getKey(), valueList, paramTypes, batchSize,
                            jdbcTemplate, false);
        }

        // 更新多语言
        batchUpdateSelectiveMultiLanguage(list, batchSize);
    }

    private <T extends AuditDomain> void paddingIdValue(Field idField, T original, String idValue) {
        ReflectionUtils.makeAccessible(idField);
        if (String.class.isAssignableFrom(idField.getType())) {
            ReflectionUtils.setField(idField, original, idValue);
        } else if (Long.class.isAssignableFrom(idField.getType())) {
            ReflectionUtils.setField(idField, original, idValue);
        } else {
            ReflectionUtils.setField(idField, original, idValue);
        }
    }

    private <T extends AuditDomain> void batchUpdateSelectiveMultiLanguage(List<T> list, int batchSize)
                    throws DataAccessException {
        Class<?> entityClass = list.get(0).getClass();
        // 获取表信息
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        // 获取字段信息
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);

        // deal MultiLanguage
        MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
        if (multiLanguageTable != null) {
            String tableName = entityTable.getMultiLanguageTableName();

            // pk default only one
            EntityColumn pkEntityColumn = new ArrayList<>(EntityHelper.getPKColumns(entityClass)).get(0);

            // 获取当前语境
            String language = MtLanguageHelper.language();

            // 获取多语言字段
            List<EntityColumn> multiColumns =
                            columns.stream().filter(EntityColumn::isMultiLanguage).collect(Collectors.toList());

            // 当前语境主表有值或者传入TLS都需要更新,其他语境需要根据传入tls进行更新,预编译方式无法进行局部更新，所以选择一个一个列去更新
            Map<String, List<Object[]>> langInfo = new HashMap<>(multiColumns.size());

            // 处理主数据中的多语言
            for (EntityColumn multiColumn : multiColumns) {
                // 初始对象
                List<Object[]> objects = new ArrayList<>();
                langInfo.put(multiColumn.getProperty(), objects);
            }

            for (T entity : list) {
                // pkColumn value
                Object pkValue;
                try {
                    pkValue = PropertyUtils.getProperty(entity, pkEntityColumn.getProperty());
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new DataAccessResourceFailureException(e.getMessage());
                }
                // 处理TLS中的多语言
                if (entity.get_tls() != null) {
                    for (Map.Entry<String, Map<String, String>> outer : entity.get_tls().entrySet()) {
                        for (Map.Entry<String, String> inner : outer.getValue().entrySet()) {
                            langInfo.get(outer.getKey()).add(new Object[] {inner.getValue(), pkValue, inner.getKey()});
                        }
                    }
                } else {
                    // 更新当前语境
                    for (EntityColumn multiColumn : multiColumns) {
                        Object multiValue;
                        try {
                            multiValue = PropertyUtils.getProperty(entity, multiColumn.getProperty());
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            throw new DataAccessResourceFailureException(e.getMessage());
                        }
                        if (multiValue != null) {
                            langInfo.get(multiColumn.getProperty()).add(new Object[] {multiValue, pkValue, language});
                        }
                    }
                }

            }

            // 根据数据拼接多个SQL语句
            for (EntityColumn multiColumn : multiColumns) {
                if (langInfo.containsKey(multiColumn.getProperty())) {
                    String langSql = "UPDATE " + tableName + " SET " + multiColumn.getColumn() + "=?" + " WHERE "
                                    + pkEntityColumn.getColumn() + "=? AND LANG=?";
                    // 一个多语言列执行一次SQL
                    TarzanBatchUpdateUtils.executeBatchUpdateWithBatchSize(langSql,
                                    langInfo.get(multiColumn.getProperty()),
                                    new int[] {Types.VARCHAR, Types.BIGINT, Types.VARCHAR}, batchSize, jdbcTemplate,
                                    false);
                }
            }
        }
    }
}
