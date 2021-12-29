package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfOperationComponentIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口）Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
public interface ItfOperationComponentIfaceMapper extends BaseMapper<ItfOperationComponentIface> {

    /**
     * 批量工序组件关系导入Iface
     *
     * @param tableName
     * @param operaCompIfaceList
     * @return
     * @author jiangling.zheng@hand-china.com 2020-07-09 15:28
     */
    void batchInsertOperaCompIface(@Param(value = "tableName") String tableName,
                                   @Param(value = "operaCompIfaceList") List<ItfOperationComponentIface> operaCompIfaceList);

    /**
     * 批量工序组件关系导入
     *
     * @param tableName
     * @param operaCompIfaceList
     * @return
     * @author jiangling.zheng@hand-china.com 2020-07-09 15:28
     */
    void batchInsertOperaComp(@Param(value = "tableName") String tableName,
                              @Param(value = "operaCompIfaceList") List<ItfOperationComponentIface> operaCompIfaceList);

    /**
     * 删除今天的批次，重新记录
     *
     * @param batchDate
     * @return
     * @author kejin.liu01@hand-china.com 2020-07-09 15:28
     */
    void deleteDataByTypeAndBatchDate(@Param(value = "type") String type,
                                      @Param(value = "batchDate") String batchDate);
}
