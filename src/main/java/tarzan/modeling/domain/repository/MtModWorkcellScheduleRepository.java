package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtModWorkcellSchedule;

/**
 * 工作单元计划属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModWorkcellScheduleRepository
                extends BaseRepository<MtModWorkcellSchedule>, AopProxy<MtModWorkcellScheduleRepository> {

    /**
     * workcellSchedulePropertyGet获取工作单元计划属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param workcellId
     * @return
     */
    MtModWorkcellSchedule workcellSchedulePropertyGet(Long tenantId, String workcellId);

    /**
     * workcellSchedulePropertyBatchGet-批量获取工作单元计划属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param workcellIds
     * @return
     */
    List<MtModWorkcellSchedule> workcellSchedulePropertyBatchGet(Long tenantId, List<String> workcellIds);

    /**
     * workcellSchedulePropertyUpdate 新增更新工作单元计划属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     */
    void workcellSchedulePropertyUpdate(Long tenantId, MtModWorkcellSchedule dto, String fullUpdate);

}
