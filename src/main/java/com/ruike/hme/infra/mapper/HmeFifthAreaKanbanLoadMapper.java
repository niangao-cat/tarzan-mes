package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeFifthAreaKanban;
import com.ruike.hme.domain.entity.HmeFifthAreaKanbanLoad;
import com.ruike.itf.domain.entity.ItfFacCollectIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 五部看板装载信息Mapper
 *
 * @author penglin.sui@hand-china.com 2021-06-08 16:01:49
 */
public interface HmeFifthAreaKanbanLoadMapper extends BaseMapper<HmeFifthAreaKanbanLoad> {
    /**
     * 五部看板装载信息查询
     * @param tenantId 租户ID
     * @param eoIdList 执行作业集合
     * @return
     * @author penglin.sui@hand-china.com 2021/6/8 16:42
     */
    List<HmeFifthAreaKanbanLoad> selectFifthAreaLoad(@Param("tenantId")Long tenantId,
                                                     @Param("eoIdList")List<String> eoIdList);

    /**
     * 五部看板装载主键ID
     * @param tenantId 租户ID
     * @param eoIdList 执行作业集合
     * @return
     * @author penglin.sui@hand-china.com 2021/6/8 16:42
     */
    List<String> selectFifthAreaLoadId(@Param("tenantId")Long tenantId,
                                       @Param("eoIdList")List<String> eoIdList);

    /**
     * 批量新增五部看板装载信息
     * @param domains 数据集合
     * @return
     * @author penglin.sui@hand-china.com 2021/6/8 17:06
     */
    void batchInsertLoad(@Param("domains")List<HmeFifthAreaKanbanLoad> domains);

    /**
     * 批量新增五部看板装载信息
     * @param fifthAreaLoadIdList 主键ID集合
     * @return
     * @author penglin.sui@hand-china.com 2021/6/8 17:06
     */
    void batchDeleteByPrimary(@Param("fifthAreaLoadIdList")List<String> fifthAreaLoadIdList);
}
