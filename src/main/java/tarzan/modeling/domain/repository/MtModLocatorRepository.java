package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.vo.*;

/**
 * 库位资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModLocatorRepository extends BaseRepository<MtModLocator>, AopProxy<MtModLocatorRepository> {

    /**
     * locatorBasicPropertyGet获取库位基础属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param locatorId
     * @return
     */
    MtModLocator locatorBasicPropertyGet(Long tenantId, String locatorId);

    /**
     * locatorBasicPropertyBatchGet-批量获取库位基础属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param locatorIds
     * @return
     */
    List<MtModLocator> locatorBasicPropertyBatchGet(Long tenantId, List<String> locatorIds);

    /**
     * subLocatorQuery根据指定库位获取子层库位
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> subLocatorQuery(Long tenantId, MtModLocatorVO9 dto);

    /**
     * locatorGroupLimitLocatorQuery根据指定库位组获取库位
     *
     * @param tenantId
     * @param locatorGroupId
     * @param queryType
     * @return
     */
    List<String> locatorGroupLimitLocatorQuery(Long tenantId, String locatorGroupId, String queryType);

    /**
     * parentLocatorQuery根据指定库位获取父层库位
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param locatorId
     * @param queryType
     * @return
     */
    List<String> parentLocatorQuery(Long tenantId, String locatorId, String queryType);

    /**
     * locatorLimitLocatorGroupGet根据指定库位获取所属库位组
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param locatorId
     * @return
     */
    String locatorLimitLocatorGroupGet(Long tenantId, String locatorId);

    /**
     * locatorRelVerify校验库位结构可行性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param locatorId
     * @param parentLocatorId
     * @return
     */
    String locatorRelVerify(Long tenantId, String locatorId, String parentLocatorId);

    /**
     * propertyLimitLocatorQuery根据库位属性获取库位
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param condition
     * @return
     */
    List<String> propertyLimitLocatorQuery(Long tenantId, MtModLocatorVO1 condition);

    /**
     * 删除库位结构关系
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     */
    void locatorRelDelete(Long tenantId, MtModLocator dto);

    /**
     * locatorRecordOnhandVerify-校验库位是否允许记录库存
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param tenantId
     * @param locatorId
     */
    void locatorRecordOnhandVerify(Long tenantId, String locatorId);

    /**
     * locatorLimitInventoryCategoryLocatorGet-根据库位获取库存类别库位
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param tenantId
     * @param locatorId
     * @return
     */
    String locatorLimitInventoryCategoryLocatorGet(Long tenantId, String locatorId);

    /**
     * locatorTransferOnhandUpdateVerify-校验库位转移时是否需要更新库存
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param tenantId
     * @param sourceLocatorId
     * @param targetLocatorId
     */
    void locatorTransferOnhandUpdateVerify(Long tenantId, String sourceLocatorId, String targetLocatorId);

    /**
     * locatorBasicPropertyUpdate-新增更新库位及基础属性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     * @return void
     */
    String locatorBasicPropertyUpdate(Long tenantId, MtModLocator dto, String fullUpdate);

    /**
     * 根据属性获取库位信息
     * 
     * @Author peng.yuan
     * @Date 2019/9/29 15:50
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.modeling.domain.vo.MtModLocatorVO8>
     */
    List<MtModLocatorVO8> propertyLimitLocatorPropertyQuery(Long tenantId, MtModLocatorVO7 dto);

    /**
     * modLocatorAttrPropertyUpdate-库位新增&更新扩展表属性
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/11/19
     */
    void modLocatorAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);

    /**
     * 根据lcatorCode批量获取locator信息
     */
    List<MtModLocator> selectModLocatorForCodes(Long tenantId, List<String> locatorCodes);

    /**
     * locatorLimitInventoryCategoryLocatorBatchGet-批量获取库位上层库存类别库位
     *
     * @param tenantId
     * @param locatorIds
     * @return List<MtModLocatorVO15>
     */
    List<MtModLocatorVO15> locatorListLimitInvCategoryLocatorGet(Long tenantId, List<String> locatorIds);


    /**
     * locatorListLimitSubLocatorQuery-库位新增&根据指定库位批量获取子层库
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2020/09/04
     */
    List<MtModLocatorVO14> locatorListLimitSubLocatorQuery(Long tenantId, MtModLocatorVO13 dto);
}
