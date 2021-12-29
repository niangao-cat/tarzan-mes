package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtModAreaPurchase;
import tarzan.modeling.domain.vo.MtModAreaPurchaseVO;

/**
 * 区域采购属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModAreaPurchaseRepository
                extends BaseRepository<MtModAreaPurchase>, AopProxy<MtModAreaPurchaseRepository> {

    /**
     * areaPurchasePropertyGet获取区域采购属性
     * 
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param areaId
     * @return
     */
    MtModAreaPurchase areaPurchasePropertyGet(Long tenantId, String areaId);

    /**
     * areaPurchasePropertyBatchGet-批量获取区域采购属性
     * 
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param areaIds
     * @return
     */
    List<MtModAreaPurchase> areaPurchasePropertyBatchGet(Long tenantId, List<String> areaIds);

    /**
     * areaPurchasePropertyUpdate-新增更新区域采购属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     * @return void
     */
    void areaPurchasePropertyUpdate(Long tenantId, MtModAreaPurchaseVO dto, String fullUpdate);

}
