package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfRoutingOperationIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工艺路线接口表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
public interface ItfRoutingOperationIfaceMapper extends BaseMapper<ItfRoutingOperationIface> {


    /**
     * 批量工艺路线导入Iface
     *
     * @param tableName
     * @param operaIfaceList
     * @return
     * @author jiangling.zheng@hand-china.com 2020-07-09 15:28
     */
    void batchInsertRoutingOperaIface(@Param(value = "tableName") String tableName,
                                      @Param(value = "operaIfaceList") List<ItfRoutingOperationIface> operaIfaceList);

    /**
     * 批量工艺路线导入
     *
     * @param tableName
     * @param operaIfaceList
     * @return
     * @author jiangling.zheng@hand-china.com 2020-07-09 15:28
     */
    void batchInsertRoutingOpera(@Param(value = "tableName") String tableName,
                                 @Param(value = "operaIfaceList") List<ItfRoutingOperationIface> operaIfaceList);

    /**
     * 删除今天的批次，重新记录
     *
     * @param batchDate
     * @return
     * @author kejin.liu01@hand-china.com 2020-07-09 15:28
     */
    void deleteDataByTypeAndBatchDate(@Param(value = "type") String type,
                                      @Param(value = "batchDate") String batchDate);

    /**
     * 查询未处理的批次
     *
     * @param tenantId
     * @author sanfeng.zhang@hand-china.com 2021/11/23 14:51
     * @return java.util.List<java.lang.Long>
     */
    List<Long> selectBatchId(@Param("tenantId") Long tenantId);

    /**
     * 接口数据
     *
     * @param tenantId
     * @param batchId
     * @return java.util.List<com.ruike.itf.domain.entity.ItfRoutingOperationIface>
     * @author sanfeng.zhang@hand-china.com 2021/11/24
     */
    List<ItfRoutingOperationIface> queryRoutingOperationIfaces(@Param("tenantId") Long tenantId, @Param("batchId") Double batchId);
}
