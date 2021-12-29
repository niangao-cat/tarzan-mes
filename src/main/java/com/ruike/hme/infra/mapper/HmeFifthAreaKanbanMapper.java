package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeFifthAreaKanban;
import com.ruike.hme.domain.entity.HmeFifthAreaKanbanLoad;
import com.ruike.hme.domain.vo.HmeFifthAreaKanbanVO2;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 五部看板Mapper
 *
 * @author penglin.sui@hand-china.com 2021-06-08 07:53:30
 */
public interface HmeFifthAreaKanbanMapper extends BaseMapper<HmeFifthAreaKanban> {

    /**
     * 五部看板主数据查询
     * @param tenantId 租户ID
     * @param preOneStartTime 前一天的开始时间(2021-01-01 00:00:00)
     * @param preOneEndTime 前一天的结束时间(2021-01-01 23:59:59)
     * @return
     * @author penglin.sui@hand-china.com 2021/6/8 9:26
     */
    List<HmeFifthAreaKanban> selectMainData(@Param("tenantId")Long tenantId,
                                            @Param("preOneStartTime")String preOneStartTime,
                                            @Param("preOneEndTime")String preOneEndTime);

    /**
     * 不良处理方式查询
     * @param tenantId 租户ID
     * @return
     * @author penglin.sui@hand-china.com 2021/6/8 9:26
     */
    List<HmeFifthAreaKanbanVO2> selectNcProcessMethod(@Param("tenantId")Long tenantId,
                                                      @Param("dtoList") List<HmeFifthAreaKanbanVO2> dtoList);

    /**
     * 五部看板主键ID
     * @param tenantId 租户ID
     * @param preOneStartTime 前一天的开始时间(2021-01-01 00:00:00)
     * @param preOneEndTime 前一天的结束时间(2021-01-01 23:59:59)
     * @return
     * @author penglin.sui@hand-china.com 2021/6/9 9:26
     */
    List<String> selectFifthAreaKanbanId(@Param("tenantId")Long tenantId,
                                         @Param("preOneStartTime")String preOneStartTime,
                                         @Param("preOneEndTime")String preOneEndTime);

    /**
     * 批量新增五部看板数据
     * @param domains 数据集合
     * @return
     * @author penglin.sui@hand-china.com 2021/6/8 17:06
     */
    void batchInsertFifthArea(@Param("domains")List<HmeFifthAreaKanban> domains);

    /**
     * 批量删除五部看板装载信息
     * @param fifthAreaKanbanIdList 主键ID集合
     * @return
     * @author penglin.sui@hand-china.com 2021/6/8 17:06
     */
    void batchDeleteByPrimary(@Param("fifthAreaKanbanIdList")List<String> fifthAreaKanbanIdList);
}
