package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtModWorkcellManufacturing;

/**
 * 工作单元生产属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModWorkcellManufacturingRepository
                extends BaseRepository<MtModWorkcellManufacturing>, AopProxy<MtModWorkcellManufacturingRepository> {

    /**
     * workcellManufacturingPropertyBatchGet-批量获取工作单元生产属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param workcellId
     * @return
     */
    List<MtModWorkcellManufacturing> workcellManufacturingPropertyBatchGet(Long tenantId, List<String> workcellId);

    /**
     * workcellManufacturingPropertyGet-获取工作单元生产属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param workcellId
     * @return
     */
    MtModWorkcellManufacturing workcellManufacturingPropertyGet(Long tenantId, String workcellId);

    /**
     * workcellManufacturingPropertyUpdate 新增更新工作单元生产属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     * @return
     */
    void workcellManufacturingPropertyUpdate(Long tenantId, MtModWorkcellManufacturing dto, String fullUpdate);
}
