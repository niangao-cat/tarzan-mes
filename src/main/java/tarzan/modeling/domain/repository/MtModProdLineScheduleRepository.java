package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtModProdLineSchedule;
import tarzan.modeling.domain.vo.MtModProdLineScheduleVO;

/**
 * 生产线计划属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModProdLineScheduleRepository
                extends BaseRepository<MtModProdLineSchedule>, AopProxy<MtModProdLineScheduleRepository> {

    /**
     * prodLineSchedulePropertyGet获取生产线计划属性
     * 
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param prodLineId
     * @return
     */
    MtModProdLineSchedule prodLineSchedulePropertyGet(Long tenantId, String prodLineId);

    /**
     * prodLineSchedulePropertyBatchGet-批量获取生产线计划属性
     * 
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param prodLineIds
     * @return
     */
    List<MtModProdLineSchedule> prodLineSchedulePropertyBatchGet(Long tenantId, List<String> prodLineIds);

    /**
     * prodLineSchedulePropertyUpdate-新增更新生产线计划属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     * @return void
     */
    void prodLineSchedulePropertyUpdate(Long tenantId, MtModProdLineScheduleVO dto, String fullUpdate);

}
