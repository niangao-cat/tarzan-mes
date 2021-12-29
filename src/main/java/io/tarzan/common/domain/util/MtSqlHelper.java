package io.tarzan.common.domain.util;

import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.domain.EntityColumn;
import io.choerodon.mybatis.domain.EntityTable;
import io.choerodon.mybatis.helper.EntityHelper;
import io.choerodon.mybatis.helper.LanguageHelper;
import io.tarzan.common.domain.sys.SequenceInfo;
import io.tarzan.common.domain.vo.MtMultiLanguageVO;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.util.FieldNameUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.domian.Language;

import javax.persistence.Table;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MtSqlHelper {

    /**
     * 全量更新的时候，创建人和创建时间不能更新，需要过滤
     */
    private static List<String> createFields = Arrays.asList("CREATED_BY", "CREATION_DATE");

    public static List<String> getDeleteSql(AuditDomain dto) {
        final StringBuilder sql = new StringBuilder();
        List<String> sqlList = new ArrayList<String>();
        Class<?> entityClass = dto.getClass();
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);

        sql.append("DELETE FROM ").append(entityTable.getName()).append(" ").append("WHERE").append(" ");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder pkSql = new StringBuilder();
        for (EntityColumn field : pkColumns) {
            try {
                Object obj = PropertyUtils.getProperty(dto, field.getProperty());
                if (obj != null) {
                    if (obj instanceof String) {
                        pkSql.append(field.getColumn()).append("=").append("'").append(obj.toString()).append("'")
                                        .append(" AND ");
                    } else if (obj instanceof Date) {
                        pkSql.append(field.getColumn()).append("=").append("'").append(format.format(obj)).append("'")
                                        .append(" AND ");
                    } else {
                        pkSql.append(field.getColumn()).append("=").append(obj).append(" AND ");
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
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

    public static List<String> getUpdateSql(AuditDomain dto) {
        final StringBuilder sql = new StringBuilder();
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                            sql.append(column.getColumn()).append("=").append("'").append(value.toString()).append("'")
                                            .append(",");
                        } else if (value instanceof Date) {
                            sql.append(column.getColumn()).append("=").append("'").append(format.format(value))
                                            .append("'").append(",");
                        } else {
                            sql.append(column.getColumn()).append("=").append(value).append(",");
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
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
                        sql.append(t.getColumn()).append("=").append("'").append(format.format(value)).append("'")
                                        .append(" AND ");
                    } else {
                        sql.append(t.getColumn()).append("=").append(value).append(" AND ");
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });

        sqlList.add(sql.substring(0, sql.length() - " AND ".length()));
        sqlList.addAll(getUpdateMultiLanguage(dto, Collections.emptyList()));

        return sqlList;
    }

    public static List<String> getUpdateOptionsSql(AuditDomain dto, Criteria criteria) {
        List<String> updateFields = null;
        if (criteria == null || CollectionUtils.isEmpty(criteria.getUpdateFields())) {
            return Collections.emptyList();
        }

        updateFields = criteria.getUpdateFields().stream().distinct().collect(toList());

        final StringBuilder sql = new StringBuilder();
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

            if (column.isUpdatable() && updateFields.stream().anyMatch(t -> t.equalsIgnoreCase(column.getProperty()))) {
                try {
                    Object obj = PropertyUtils.getProperty(dto, column.getProperty());
                    if (obj == null) {
                        sql.append(column.getColumn()).append("=").append("NULL,");
                    } else {
                        if (obj instanceof String) {
                            sql.append(column.getColumn()).append("=").append("'").append(obj.toString()).append("'")
                                            .append(",");
                        } else if (obj instanceof Date) {
                            sql.append(column.getColumn()).append("=").append("'").append(format.format(obj))
                                            .append("'").append(",");
                        } else {
                            sql.append(column.getColumn()).append("=").append(obj).append(",");
                        }
                    }

                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
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
                        sql.append(t.getColumn()).append("=").append("'").append(format.format(value)).append("'")
                                        .append(" AND ");
                    } else {
                        sql.append(t.getColumn()).append("=").append(value).append(" AND ");
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });

        sqlList.add(sql.substring(0, sql.length() - " AND ".length()));
        sqlList.addAll(getUpdateMultiLanguage(dto, updateFields));

        return sqlList;
    }

    private static List<String> getUpdateMultiLanguage(AuditDomain dto, List<String> updateFields) {
        Class<?> entityClass = dto.getClass();
        MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
        Set<EntityColumn> allFields = EntityHelper.getColumns(entityClass);
        Set<EntityColumn> multiFields =
                        allFields.stream().filter(EntityColumn::isMultiLanguage).collect(Collectors.toSet());
        if (multiLanguageTable == null || multiFields == null || multiFields.size() == 0) {
            return Collections.emptyList();
        }

        List<String> sqlList = new ArrayList<String>();
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        String tableName = entityTable.getMultiLanguageTableName();

        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                        keySql.append(field.getColumn()).append("=").append("'").append(format.format(value))
                                        .append("'").append(" AND ");
                    } else {
                        keySql.append(field.getColumn()).append("=").append(value).append(" AND ");
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
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
                        setSql.append(column.getColumn()).append("=").append("'").append(value.toString()).append("'")
                                        .append(",");
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            if (setSql.length() == 0) {
                return Collections.emptyList();
            }
            // 删除末尾逗号
            setSql.deleteCharAt(setSql.length() - 1);

            setSql.append(" WHERE ");
            // setSql.append("LANG='").append(DetailsHelper.getUserDetails().getLanguage()).append("' AND ");
            setSql.append("LANG='").append(org.hzero.core.helper.LanguageHelper.language()).append("' AND ");
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
                    setSql.append(ever.getColoumName()).append("=").append("'").append(ever.getValues()).append("'")
                                    .append(",");
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

    public static List<String> getInsertSql(AuditDomain dto) {
        final StringBuilder sql = new StringBuilder();
        final StringBuilder columnSql = new StringBuilder();
        final StringBuilder valueSql = new StringBuilder();
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
                            valueSql.append("'").append(obj.toString()).append("'").append(",");
                        } else if (obj instanceof Date) {
                            valueSql.append("'").append(format.format(obj)).append("'").append(",");
                        } else {
                            valueSql.append(obj).append(",");
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
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
                    e.printStackTrace();
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
                    /**
                     * if multi language value not exists in __tls, then use value on current field
                     */
                    try {
                        objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    continue;
                } else {
                    Map<String, String> tls = _tls.get(field.getColumn());
                    if (tls == null) {
                        /**
                         * if multi language value not exists in __tls, then use value on current field
                         */
                        try {
                            objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            e.printStackTrace();
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

    public static List<String> getFullUpdateSql(AuditDomain dto) {
        final StringBuilder sql = new StringBuilder();
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                            sql.append(column.getColumn()).append("=").append("'").append(format.format(value))
                                            .append("'").append(",");
                        } else {
                            sql.append(column.getColumn()).append("=").append(value).append(",");
                        }
                    } else {
                        if (!"java.lang.String".equals(column.getJavaType().getName())
                                        && !createFields.contains(column.getColumn().toUpperCase())) {
                            sql.append(column.getColumn()).append("=").append("null").append(",");
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
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
                        sql.append(t.getColumn()).append("=").append("'").append(format.format(value)).append("'")
                                        .append(" AND ");
                    } else {
                        sql.append(t.getColumn()).append("=").append(value).append(" AND ");
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });

        sqlList.add(sql.substring(0, sql.length() - " AND ".length()));
        sqlList.addAll(getUpdateMultiLanguage(dto, Collections.emptyList()));

        return sqlList;
    }

    /**
     * 获取实体的序列信息
     *
     * @param classz
     * @return
     */
    public static SequenceInfo getSequenceInfo(Class<?> classz) {
        SequenceInfo result = new SequenceInfo(false, "", "");
        Table table = classz.getAnnotation(Table.class);
        if (table == null) {
            return result;
        }

        String tableName = table.name();
        if (StringUtils.isEmpty(tableName)) {
            return result;
        }

        boolean isCustomPrimary;
        CustomPrimary customPrimary = classz.getAnnotation(CustomPrimary.class);
        isCustomPrimary = null != customPrimary;

        return new SequenceInfo(isCustomPrimary, tableName + MtBaseConstants.PK_SUFFIX,
                        tableName + MtBaseConstants.CID_SUFFIX);
    }
}
