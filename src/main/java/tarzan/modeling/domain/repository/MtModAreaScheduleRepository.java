package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtModAreaSchedule;
import tarzan.modeling.domain.vo.MtModAreaScheduleVO;

/**
 * 区域计划属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModAreaScheduleRepository
                extends BaseRepository<MtModAreaSchedule>, AopProxy<MtModAreaScheduleRepository> {

    /**
     * areaSchedulePropertyGet获取区域计划属性
     * 
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param areaId
     * @return
     */
    MtModAreaSchedule areaSchedulePropertyGet(Long tenantId, String areaId);

    /**
     * areaSchedulePropertyBatchGet-批量获取区域计划属性
     * 
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param areaIds
     * @return
     */
    List<MtModAreaSchedule> areaSchedulePropertyBatchGet(Long tenantId, List<String> areaIds);

    /**
     * areaSchedulePropertyUpdate-新增更新区域计划属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     * @return void
     */
    void areaSchedulePropertyUpdate(Long tenantId, MtModAreaScheduleVO dto, String fullUpdate);

}
