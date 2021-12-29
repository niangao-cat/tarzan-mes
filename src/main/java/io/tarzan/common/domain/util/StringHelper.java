package io.tarzan.common.domain.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.choerodon.mybatis.domain.AuditDomain;

import org.apache.commons.collections4.CollectionUtils;

/**
 * @author MrZ
 */
public class StringHelper {

    /**
     * 根据表名获取包路径 规则：mt_bom -> bom, 仅用于Mt系统
     *
     * @author chuang.yang
     */
    public static String tableNameToPackageName(String tableName) {
        String lowCaseStr = tableName.toLowerCase();

        String[] strings = lowCaseStr.split("_");

        StringBuilder stringBuilder = new StringBuilder("hmes.");
        for (int i = 1; i < strings.length; i++) {
            String strTemp = strings[i];
            if ("b".equals(strTemp.toLowerCase()) || "tl".equals(strTemp.toLowerCase())) {
                continue;
            }
            stringBuilder.append(strTemp).append("_");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(".dto.");
        return stringBuilder.toString();
    }

    /**
     * 根据数据库表名转化为dto名称 规则：mt_bom -> MtBom, 仅用于Mt系统
     *
     * @author chuang.yang
     */
    public static String tableNameToDtoName(String tableName) {
        String lowCaseStr = tableName.toLowerCase();

        String[] strings = lowCaseStr.split("_");

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            String strTemp = strings[i];
            if ("b".equals(strTemp.toLowerCase()) || "tl".equals(strTemp.toLowerCase())) {
                continue;
            }
            stringBuilder.append(toUpperFirstCharOnly(strTemp));
        }

        return stringBuilder.toString();
    }

    /**
     * 字符传首字母大写
     *
     * @author chuang.yang
     * @date 2019/4/17
     * @return java.lang.String
     */
    public static String toUpperFirstCharOnly(String string) {
        char[] charArray = string.toCharArray();
        // 首字母小写时，转化为大写
        if (Character.isLowerCase(charArray[0])) {
            // 小写转大写
            charArray[0] -= 32;
        }
        return String.valueOf(charArray);
    }

    /**
     * 判断两个字符串是否相同
     *
     * @param o1
     * @param o2
     * @return
     */
    public static boolean isSame(String o1, String o2) {
        if (null == o1 && null == o2) {
            return true;
        }

        if (null == o1 && null != o2) {
            return false;
        } else if (null != o1 && null == o2) {
            return false;
        } else if (null != o1 && null != o2) {
            return o1.equals(o2);
        } else {
            return false;
        }
    }

    public static  List<List<AuditDomain>> splitSqlList(List<AuditDomain> sqlList, int splitNum) {

        List<List<AuditDomain>> returnList = new ArrayList<>();
        if (sqlList.size() <= splitNum) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / splitNum;
            int splitRest = sqlList.size() % splitNum;

            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * splitNum, (i + 1) * splitNum));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * splitNum, sqlList.size()));
            }
        }
        return returnList;
    }

    /**
     * Oracle数据库In 的数据量不能超过一千条
     *
     * @param column
     * @param values
     * @param num
     * @return
     */
    public static String getWhereInValuesSql(String column, List<String> values, int num) {

        if (CollectionUtils.isEmpty(values)) {
            return null;
        }

        // 值的个数
        int valueSize = values.size();
        // 批次数
        int batchSize = valueSize / num + (valueSize % num == 0 ? 0 : 1);

        // sql语句
        StringBuilder sql = new StringBuilder();
        if (batchSize > 1) {
            sql.append("(");
        }

        for (int i = 0; i < batchSize; i++) {
            if (i > 0) {
                sql.append(") or ");
            }
            sql.append(column).append(" in (");
            for (int j = i * num; (j < (i + 1) * num) && j < valueSize; j++) {
                if (j > i * num) {
                    sql.append(",");
                }
                sql.append("'").append(values.get(j)).append("'");
            }
        }

        sql.append(")");

        if (batchSize > 1) {
            sql.append(")");
        }

        return sql.toString();
    }

}
