package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.vo.ItfDeleteTableDataVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItfDeleteTableDataMapper {

    /**
     * 传入SQL删除表数据
     *
     * @param deleteSql
     * @return
     * @author jiangling.zheng@hand-china.com 2020/9/8 15:09
     */
    void deleteTableData(@Param("deleteSql") String deleteSql);

    /**
     * 传入SQL查询表数据
     *
     * @param selectSql
     * @return java.util.List<java.lang.String>
     * @author penglin.sui@hand-china.com 2021/7/24 13:14
     */
    List<String> selectTableData(@Param("selectSql") String selectSql);

    /**
     * 传入SQL查询表数据
     *
     * @param selectSql
     * @return java.util.List<com.ruike.itf.domain.vo.ItfDeleteTableDataVO2>
     * @author penglin.sui@hand-china.com 2021/7/25 16:56
     */
    List<ItfDeleteTableDataVO2> selectTableData2(@Param("selectSql") String selectSql);
}
