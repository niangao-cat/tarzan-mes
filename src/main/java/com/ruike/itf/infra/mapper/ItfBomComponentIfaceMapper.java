package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfBomComponentIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * BOM接口表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
public interface ItfBomComponentIfaceMapper extends BaseMapper<ItfBomComponentIface> {


    /**
     * 批量 BOM 导入Iface
     * @author jiangling.zheng@hand-china.com 2020-07-09 15:28
     * @param tableName
     * @param bomIfaceList
     * @return
     */
    void batchInsertBomIface(@Param(value = "tableName") String tableName,
                        @Param(value = "bomIfaceList") List<ItfBomComponentIface> bomIfaceList);
    /**
     * 批量 BOM 导入
     * @author jiangling.zheng@hand-china.com 2020-07-09 15:28
     * @param tableName
     * @param bomIfaceList
     * @return
     */
    void batchInsertBom(@Param(value = "tableName") String tableName,
                        @Param(value = "bomIfaceList") List<ItfBomComponentIface> bomIfaceList);

    /**
     * 根据类型和批次时间删除
     * @author kejin.liu01@hand-china.com 2020-07-09 15:28
     * @param type
     * @param batchDate
     * @return
     */
    void deleteByTypeAndBatchDate(@Param("type") String type, @Param("batchDate") String batchDate);
}
