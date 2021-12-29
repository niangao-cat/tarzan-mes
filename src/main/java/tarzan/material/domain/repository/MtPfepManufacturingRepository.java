package tarzan.material.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtPfepManufacturing;
import tarzan.material.domain.vo.*;

/**
 * 物料生产属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepManufacturingRepository
                extends BaseRepository<MtPfepManufacturing>, AopProxy<MtPfepManufacturingRepository> {

    /**
     * pfepDefaultBomGet-获取物料默认装配清单
     *
     * @param tenantId
     * @param dto
     * @return String
     */
    String pfepDefaultBomGet(Long tenantId, MtPfepInventoryVO dto);

    /**
     * pfepDefaultRouterGet-获取物料默认工艺路线
     *
     * @param tenantId
     * @param dto
     * @return String
     */
    String pfepDefaultRouterGet(Long tenantId, MtPfepInventoryVO dto);

    /**
     * pfepManufacturingIssueControlGet-获取物料生产投料控制属性
     *
     * @param tenantId
     * @param dto
     * @return MtPfepManufacturing
     */
    MtPfepManufacturing pfepManufacturingIssueControlGet(Long tenantId, MtPfepInventoryVO dto);

    /**
     * pfepManufacturingCompleteControlGet-获取物料生产完工控制属性
     *
     * @param tenantId
     * @param dto
     * @return MtPfepManufacturing
     */
    MtPfepManufacturing pfepManufacturingCompleteControlGet(Long tenantId, MtPfepInventoryVO dto);

    /**
     * pfepManufacturingAttritionControlGet-获取物料生产损耗控制属性
     *
     * @param tenantId
     * @param dto
     * @return MtPfepManufacturing
     */
    MtPfepManufacturing pfepManufacturingAttritionControlGet(Long tenantId, MtPfepInventoryVO dto);

    /**
     * pfepRouterLimitMaterialQuery-获取使用指定工艺路线的物料组织列表
     *
     * @param tenantId
     * @param defaultRoutingId
     * @return List<MtPfepVO>
     */
    List<MtPfepInventoryVO> pfepRouterLimitMaterialQuery(Long tenantId, String defaultRoutingId);

    /**
     * pfepRouterLimitMaterialCategoryQuery-获取使用指定工艺路线的有效物料类别组织列表
     *
     * @param tenantId
     * @param defaultRoutingId
     * @return
     */
    List<MtPfepInventoryVO2> pfepRouterLimitMaterialCategoryQuery(Long tenantId, String defaultRoutingId);

    /**
     * pfepBomLimitMaterialQuery-获取使用指定装配清单生产的物料组织列表
     *
     * @param tenantId
     * @param defaultBomId
     * @return List<MtPfepVO>
     */
    List<MtPfepInventoryVO> pfepBomLimitMaterialQuery(Long tenantId, String defaultBomId);

    /**
     * pfepBomLimitMaterialCategoryQuery-获取使用指定装配清单生产的物料类别组织列表
     *
     * @param tenantId
     * @param defaultBomId
     * @return
     */
    List<MtPfepInventoryVO2> pfepBomLimitMaterialCategoryQuery(Long tenantId, String defaultBomId);

    /**
     * pfepOperationAssembleFlagGet-获取物料是否按工序装配标识
     *
     * @param tenantId
     * @param condtion
     * @return String
     */
    String pfepOperationAssembleFlagGet(Long tenantId, MtPfepInventoryVO condtion);

    /**
     * 物料生产属性新增&更新
     * 
     * @param tenantId
     * @param dto
     * @param fullUpdate
     * @return
     */
    String materialPfepManufacturingUpdate(Long tenantId, MtPfepManufacturingVO3 dto, String fullUpdate);


    /**
     * 获取物料pfep生产属性主键
     * 
     * @Author peng.yuan
     * @Date 2019/10/11 19:02
     * @param tenantId :
     * @param dto :
     * @return tarzan.material.domain.vo.MtPfepManufacturingVO5
     */
    MtPfepManufacturingVO5 propertyLimitPfepManufacturingGet(Long tenantId, MtPfepManufacturingVO4 dto);

    /**
     * 物料制造PFEP属性新增&更新扩展表属性
     * 
     * @Author peng.yuan
     * @Date 2019/11/19 11:24
     * @param tenantId :
     * @param mtExtendVO10 :
     * @return void
     */
    void pfepManufacturingAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10);

    /**
     * 根据materialSiteId批量获取物料制造属性
     * 
     * @param tenantId
     * @param materialSIteIds
     * @return
     */
    List<MtPfepManufacturing> selectpfepManufacturingByMaterialSiteId(Long tenantId, List<String> materialSIteIds);

    /**
     * pfepManufacturingBatchGet-批量获取物料生产属性
     *
     * @param tenantId
     * @param voList
     * @param fields
     * @return
     */
    List<MtPfepManufacturingVO11> pfepManufacturingBatchGet(Long tenantId, List<MtPfepManufacturingVO1> voList,
                                                            List<String> fields);

}
