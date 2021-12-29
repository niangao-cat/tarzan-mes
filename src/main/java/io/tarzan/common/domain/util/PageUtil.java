package io.tarzan.common.domain.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 手动分页工具类
 *
 * @author liyuan.lv@hand-china.com 2019/11/21 10:11
 */
public class PageUtil {
    /**
     * 手动分页
     * 默认第一页为0
     * @param nowPage 当前页
     * @param pageSize 每页数量
     * @param data 分页数据
     * @return <T> List<T>
     */
    public static <T> List<T> pagedList(int nowPage, int pageSize, List<T> data) {
        nowPage = nowPage + 1;
        int fromIndex = (nowPage - 1) * pageSize;
        if (fromIndex >= data.size()) {
            //空数组
            return Collections.emptyList();
        }
        if (fromIndex < 0) {
            //空数组
            return Collections.emptyList();
        }
        int toIndex = nowPage * pageSize;
        if (toIndex >= data.size()) {
            toIndex = data.size();
        }
        return data.subList(fromIndex, toIndex);
    }
}
