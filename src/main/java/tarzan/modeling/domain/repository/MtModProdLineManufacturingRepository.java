package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtModProdLineManufacturing;
import tarzan.modeling.domain.vo.MtModProdLineManufacturingVO;

/**
 * 生产线生产属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModProdLineManufacturingRepository
                extends BaseRepository<MtModProdLineManufacturing>, AopProxy<MtModProdLineManufacturingRepository> {

    /**
     * prodLineManufacturingPropertyGet获取生产线生产属性
     * 
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param prodLineId
     * @return
     */
    MtModProdLineManufacturing prodLineManufacturingPropertyGet(Long tenantId, String prodLineId);

    /**
     * prodLineManufacturingPropertyBatchGet-批量获取生产线生产属性
     * 
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param prodLineIds
     * @return
     */
    List<MtModProdLineManufacturing> prodLineManufacturingPropertyBatchGet(Long tenantId, List<String> prodLineIds);

    /**
     * prodLineManufacturingPropertyUpdate-新增更新生产线生产属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @return void
     * @return dto
     */
    void prodLineManufacturingPropertyUpdate(Long tenantId, MtModProdLineManufacturingVO dto, String fullUpdate);
}
