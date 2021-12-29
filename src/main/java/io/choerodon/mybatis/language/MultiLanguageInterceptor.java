/*
 * #{copyright}#
 */

package io.choerodon.mybatis.language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.hzero.mybatis.domian.Language;
import io.choerodon.mybatis.helper.LanguageHelper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.domain.EntityColumn;
import io.choerodon.mybatis.domain.EntityTable;
import io.choerodon.mybatis.helper.EntityHelper;
import io.choerodon.mybatis.helper.MapperTemplate;
import io.choerodon.mybatis.util.StringUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.update.Update;


/**
 * 自动数据多语言支持.
 *
 * @author superleader8@gmail.com
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MultiLanguageInterceptor implements Interceptor {
    private Logger logger = LoggerFactory.getLogger(MultiLanguageInterceptor.class);
    private static final String COLUMN_LANG = "lang";
    public static final ThreadLocal<Boolean> multiLanguageEnable = new ThreadLocal<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        if (BooleanUtils.isNotFalse(multiLanguageEnable.get()) && target instanceof Executor) {
            Executor executor = (Executor) target;
            MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
            Object parameter = invocation.getArgs()[1];
            EntityTable table = EntityHelper.getTableByMapper(MapperTemplate.getMapperClassName(statement.getId()));
            if (table != null && table.isMultiLanguage()) {
                Object obj = invocation.proceed();
                Connection connection = executor.getTransaction().getConnection();
                switch (statement.getSqlCommandType()) {
                    case INSERT:
                        insertMultiLanguage(table, parameter, connection);
                        break;
                    case UPDATE:
                        updateMultiLanguage(table, parameter, connection, getUpdateColumn(statement, parameter));
                        break;
                    case DELETE:
                        proceedDeleteMultiLanguage(table, parameter, connection);
                        break;
                    default:
                        break;
                }
                multiLanguageEnable.remove();
                return obj;
            }
        }
        multiLanguageEnable.remove();
        return invocation.proceed();
    }

    private void proceedDeleteMultiLanguage(EntityTable table, Object object, Connection connection)
                    throws SQLException {
        List<Object> objs = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        for (EntityColumn column : table.getEntityClassPkColumns()) {
            keys.add(column.getColumn() + " = ?");
            if (column.getJavaType().equals(object.getClass())) {
                // deleteByPrimaryKey 的情况
                objs.add(object);
            } else {
                objs.add(column.getField().get(object));
            }
        }
        if (!keys.isEmpty()) {
            executeSql(connection, "DELETE FROM " + table.getMultiLanguageTableName() + " WHERE "
                            + StringUtil.join(keys, " AND "), objs);
        }
    }

    private void insertMultiLanguage(EntityTable table, Object object, Connection connection) throws SQLException {
        Map<String, Object> columnMap = new HashMap<>(
                        table.getMultiLanguageColumns().size() + table.getEntityClassPkColumns().size() + 1);
        Map<String, Map<String, String>> fieldLangValueMap = getTls(object);

        String placeholders = buildPlaceholders(
                        table.getMultiLanguageColumns().size() + table.getEntityClassPkColumns().size() + 1);
        String sql = "INSERT INTO " + table.getMultiLanguageTableName() + "(";

        for (EntityColumn column : table.getEntityClassPkColumns()) {
            columnMap.put(column.getColumn(), column.getField().get(object));
        }
        for (Language language : getLanguages()) {
            StringBuilder sb = new StringBuilder(sql);
            columnMap.remove(COLUMN_LANG);
            columnMap.put(COLUMN_LANG, language.getCode());
            for (EntityColumn column : table.getMultiLanguageColumns()) {
                columnMap.remove(column.getColumn());
                columnMap.put(column.getColumn(), getValue(fieldLangValueMap, column.getField().getName(),
                                language.getCode(), () -> column.getField().get(object)));
            }
            sb.append(StringUtil.join(columnMap.keySet(), ","));
            sb.append(") VALUES (").append(placeholders).append(")");
            executeSql(connection, sb.toString(), columnMap.values());
        }
    }

    private void insertMultiLanguage(EntityTable table, String lang, Object parameter, Connection connection)
                    throws SQLException {
        StringBuilder builder = new StringBuilder();
        List<Object> values = new ArrayList<>();
        List<Object> keys = new ArrayList<>();
        List<String> placeholders = new ArrayList<>();
        for (EntityColumn column : table.getEntityClassPkColumns()) {
            values.add(column.getField().get(parameter));
            keys.add(column.getColumn());
            placeholders.add("?");
        }
        for (EntityColumn column : table.getMultiLanguageColumns()) {
            values.add(column.getField().get(parameter));
            keys.add(column.getColumn());
            placeholders.add("?");
        }
        keys.add("lang");
        values.add(lang);
        placeholders.add("?");

        builder.append("INSERT INTO ").append(table.getMultiLanguageTableName()).append(" (");
        builder.append(StringUtil.join(keys, ","));
        builder.append(") VALUES (");
        builder.append(StringUtil.join(placeholders, ","));
        builder.append(")");
        logger.debug("Insert missing multi language record: {} ,parameters: {}", builder, values);
        executeSql(connection, builder.toString(), values);
    }

    private void updateMultiLanguage(EntityTable table, Object object, Connection connection,
                    Set<String> updateColumnSet) throws SQLException {
        Map<String, Object> whereColumnMap = new LinkedHashMap<>(table.getEntityClassPkColumns().size() + 1);
        Map<String, Object> setColumnMap = new LinkedHashMap<>(
                        table.getMultiLanguageColumns().size() + table.getEntityClassPkColumns().size() + 1);
        Map<String, Map<String, String>> fieldLangValueMap = getTls(object);
        Set<String> updateLanguageSet = getUpdateLanguage(fieldLangValueMap);
        String sql = "UPDATE " + table.getMultiLanguageTableName() + " SET ";

        for (EntityColumn column : table.getEntityClassPkColumns()) {
            Object value = column.getField().get(object);
            if (value == null) {
                return;
            }
            whereColumnMap.put(column.getColumn() + " = ?", value);
        }

        String currentLanguage = LanguageHelper.language();
        if (CollectionUtils.isEmpty(fieldLangValueMap)) {
            fieldLangValueMap = Collections.emptyMap();
        }
        for (Language language : getLanguages()) {
            if (!updateLanguageSet.contains(language.getCode())) {
                continue;
            }

            setColumnMap.clear();
            whereColumnMap.remove(COLUMN_LANG + " = ?");
            whereColumnMap.put(COLUMN_LANG + " = ?", language.getCode());
            for (EntityColumn column : table.getMultiLanguageColumns()) {
                if (!updateColumnSet.contains(column.getColumn())) {
                    continue;
                }

                if (!fieldLangValueMap.containsKey(column.getField().getName())
                                && !currentLanguage.equals(language.getCode())) {
                    continue;
                }

                setColumnMap.put(column.getColumn() + " = ? ", getValue(fieldLangValueMap, column.getField().getName(),
                                language.getCode(), () -> column.getField().get(object)));
            }
            if (CollectionUtils.isEmpty(setColumnMap)) {
                continue;
            }
            String placeholderSql = sql + StringUtil.join(setColumnMap.keySet(), ",") + " WHERE "
                            + StringUtil.join(whereColumnMap.keySet(), " AND ");
            setColumnMap.putAll(whereColumnMap);
            int updateCount = executeSql(connection, placeholderSql, setColumnMap.values());
            if (updateCount < 1) {
                logger.warn("Update multi language failed. update count: {}", updateCount);
                // 讲道理，下面这个不会执行
                insertMultiLanguage(table, language.getCode(), object, connection);
            }
        }
    }

    private Set<String> getUpdateLanguage(Map<String, Map<String, String>> fieldLangValueMap) {
        Set<String> updateLanguageSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(fieldLangValueMap)) {
            fieldLangValueMap.forEach((field, langValue) -> {
                if (!CollectionUtils.isEmpty(langValue)) {
                    langValue.forEach((lang, value) -> updateLanguageSet.add(lang));
                }
            });
        }
        updateLanguageSet.add(LanguageHelper.language());
        return updateLanguageSet;
    }


    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        // no need properties
    }

    private int executeSql(Connection connection, String sql, Collection<Object> params) throws SQLException {
        logger.debug("==>  Preparing: {}", sql);
        logger.debug("==> Parameters: {}", params);
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int i = 1;
            for (Object obj : params) {
                ps.setObject(i++, obj);
            }
            ps.execute();
            int updateCount = ps.getUpdateCount();
            logger.debug("<==      Total: {}", updateCount);
            return updateCount;
        }
    }

    private List<Language> getLanguages() {
        return LanguageHelper.languages();
    }

    private Object getValue(Map<String, Map<String, String>> fieldLangValueMap, String field, String lang,
                    DefaultValue defaultValue) {
        if (fieldLangValueMap != null && fieldLangValueMap.containsKey(field)) {
            Map<String, String> fieldValue = fieldLangValueMap.get(field);
            if (fieldValue != null && fieldValue.containsKey(lang)) {
                return fieldValue.get(lang);
            }
        }
        return defaultValue.getDefaultValue();
    }

    private interface DefaultValue {
        /**
         * 获取默认值
         *
         * @return 默认值
         */
        Object getDefaultValue();
    }

    private String buildPlaceholders(int placeholderCnt) {
        StringBuilder sb = new StringBuilder("?");
        while ((--placeholderCnt) > 0) {
            sb.append(", ?");
        }
        return sb.toString();
    }

    private Map<String, Map<String, String>> getTls(Object object) {
        Map<String, Map<String, String>> fieldLangValueMap = null;
        if (object instanceof AuditDomain) {
            fieldLangValueMap = ((AuditDomain) object).get_tls();
        }
        return fieldLangValueMap;
    }

    private Set<String> getUpdateColumn(MappedStatement statement, Object parameter) {
        try {
            List<Column> columnList =
                            ((Update) CCJSqlParserUtil.parse(statement.getBoundSql(parameter).getSql())).getColumns();
            if (!CollectionUtils.isEmpty(columnList)) {
                return columnList.stream().map(column -> column.getColumnName().toLowerCase())
                                .collect(Collectors.toSet());
            }
        } catch (JSQLParserException e) {
            logger.error("Error parse sql, {}", ExceptionUtils.getStackTrace(e));
        }
        return Collections.emptySet();
    }
}
