package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.vo.MtModProductionLineVO1;

/**
 * 生产线资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModProductionLineRepository
                extends BaseRepository<MtModProductionLine>, AopProxy<MtModProductionLineRepository> {

    /**
     * prodLineBasicPropertyGet获取生产线基础属性
     * 
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param prodLineId
     * @return
     */
    MtModProductionLine prodLineBasicPropertyGet(Long tenantId, String prodLineId);

    /**
     * prodLineBasicPropertyBatchGet-批量获取生产线基础属性
     * 
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param prodLineIds
     * @return
     */
    List<MtModProductionLine> prodLineBasicPropertyBatchGet(Long tenantId, List<String> prodLineIds);

    /**
     * propertyLimitProdLineQuery根据生产线属性获取生产线
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param condition
     * @return
     */
    List<String> propertyLimitProdLineQuery(Long tenantId, MtModProductionLineVO1 condition);


    /**
     * prodLineBasicPropertyUpdate 新增更新生产线及基础属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     * @return
     */
    String prodLineBasicPropertyUpdate(Long tenantId, MtModProductionLine dto, String fullUpdate);

    /**
     * 自定义查询-根据Code批量获取数据
     */
    List<MtModProductionLine> prodLineByproLineCodes(Long tenantId, List<String> prodLineCodes);

}
