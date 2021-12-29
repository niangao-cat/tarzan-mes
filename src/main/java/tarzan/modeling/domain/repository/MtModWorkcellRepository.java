package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.vo.MtModWorkcellVO1;

/**
 * 工作单元资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModWorkcellRepository extends BaseRepository<MtModWorkcell>, AopProxy<MtModWorkcellRepository> {
    /**
     * workcellBasicPropertyGet获取工作单元基础属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param workcellId
     * @return
     */
    MtModWorkcell workcellBasicPropertyGet(Long tenantId, String workcellId);

    /**
     * workcellBasicPropertyBatchGet-批量获取工作单元基础属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param workcellIds
     * @return
     */
    List<MtModWorkcell> workcellBasicPropertyBatchGet(Long tenantId, List<String> workcellIds);

    /**
     * propertyLimitWorkcellQuery根据工作单元属性获取工作单元
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param condition
     * @return
     */
    List<String> propertyLimitWorkcellQuery(Long tenantId, MtModWorkcellVO1 condition);

    /**
     * workcellBasicPropertyUpdate 新增更新工作单元及基础属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     * @return
     */
    String workcellBasicPropertyUpdate(Long tenantId, MtModWorkcell dto, String fullUpdate);

    /**
     * workcellForWkcCodeQuery-批量获取工作单元
     *
     * @return
     */
    List<MtModWorkcell> workcellForWkcCodeQuery(Long tenantId, List<String> workcellCodes);

    /**
     * modWorkcellAttrPropertyUpdate-工作单元新增&更新扩展表属性
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/11/20
     */
    void modWorkcellAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);
}
