package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.modeling.domain.entity.MtModLocatorGroup;
import tarzan.modeling.domain.vo.MtModLocatorGroupVO;
import tarzan.modeling.domain.vo.MtModLocatorGroupVO3;

/**
 * 库位组资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModLocatorGroupRepository
        extends BaseRepository<MtModLocatorGroup>, AopProxy<MtModLocatorGroupRepository> {


    /**
     * locatorGroupBasicPropertyGet获取库位组基础属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param locatorGroupId
     * @return
     */
    MtModLocatorGroup locatorGroupBasicPropertyGet(Long tenantId, String locatorGroupId);

    /**
     * locatorGroupBasicPropertyBatchGet-批量获取库位组基础属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param locatorGroupIds
     * @return
     */
    List<MtModLocatorGroup> locatorGroupBasicPropertyBatchGet(Long tenantId, List<String> locatorGroupIds);

    /**
     * propertyLimitLocatorGroupQuery根据库位组属性获取库位组
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param condition
     * @return
     */
    List<String> propertyLimitLocatorGroupQuery(Long tenantId, MtModLocatorGroupVO condition);

    /**
     * locatorGroupBasicPropertyUpdate新增更新库位组及基础属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     * @return
     */
    String locatorGroupBasicPropertyUpdate(Long tenantId, MtModLocatorGroup dto);

    /**
     * 根据属性获取库位组信息
     *
     * @Author peng.yuan
     * @Date 2019/9/29 14:50
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.modeling.domain.vo.MtModLocatorGroupVO3>
     */
    List<MtModLocatorGroupVO3> propertyLimitLocatorGroupPropertyQuery(Long tenantId, MtModLocatorGroupVO3 dto);

    /**
     * modLocatorGroupAttrTableUpdate-库位组新增&更新扩展表属性
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/11/20
     */
    void modLocatorGroupAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);
}
