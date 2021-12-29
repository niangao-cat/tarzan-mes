package tarzan.material.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtMaterialCategorySite;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO4;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO7;

/**
 * 物料类别站点分配资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialCategorySiteRepository
                extends BaseRepository<MtMaterialCategorySite>, AopProxy<MtMaterialCategorySiteRepository> {

    /**
     * materialCategorySitePfepExistValidate-验证物料类别是否在站点下存在PFEP属性
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String materialCategorySitePfepExistValidate(Long tenantId, MtMaterialCategorySite dto);

    /**
     * materialCategorySiteValidate-验证物料类别分配站点是否满足类别集限制
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String materialCategorySiteValidate(Long tenantId, MtMaterialCategorySite dto);

    /**
     * materialCategorySiteLimitRelationGet-根据物料类别和站点限制验证并获取物料类别站点关系
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String materialCategorySiteLimitRelationGet(Long tenantId, MtMaterialCategorySite dto);

    /**
     * relationLimitMaterialCategorySiteGet-根据物料类别站点关系获取物料类别和站点
     *
     * @param tenantId
     * @param materialCategorySiteId
     * @return
     */
    MtMaterialCategorySite relationLimitMaterialCategorySiteGet(Long tenantId, String materialCategorySiteId);

    /**
     * materialCategorySiteAssign-物料类别站点分配
     * 
     * @author benjamin
     * @date 2019/9/16 5:58 PM
     * @param tenantId 租户Id
     * @param materialCategorySiteVO MtMaterialCategorySiteVO4
     * @return String
     */
    String materialCategorySiteAssign(Long tenantId, MtMaterialCategorySiteVO4 materialCategorySiteVO);

    /**
     * 自定义根据物料类别站点Id批量查询
     *
     * @Author Xie.yiyang
     * @Date 2019/11/11 18:53
     * @param tenantId, materialCategorySiteIds
     * @return java.util.List<tarzan.material.domain.entity.MtMaterialCategorySite>
     */
    List<MtMaterialCategorySite> selectByMaterialCategorySiteIds(Long tenantId, List<String> materialCategorySiteIds);

    /**
     * 物料类别站点新增&更新扩展表属性
     * 
     * @Author peng.yuan
     * @Date 2019/11/19 10:31
     * @param tenantId :
     * @param mtExtendVO10 :
     * @return void
     */
    void materialCategorySiteAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10);

    /**
     * 自定义根据物料类别Id批量查询
     */
    List<MtMaterialCategorySite> selectByMaterialCategoryIds(Long tenantId, List<String> materialCategoryIds);

    /**
     * materialCategorySiteLimitRelationBatchGet-根据物料类别和站点限制批量获取物料类别站点关系
     *
     * @author chuang.yang
     * @date 2020/4/7
     * @param tenantId
     * @param materialCategorySiteIds
     * @return java.util.List<tarzan.material.domain.vo.MtMaterialCategorySiteVO7>
     */
    List<MtMaterialCategorySiteVO7> materialCategorySiteLimitRelationBatchGet(Long tenantId,
                                                                              List<MtMaterialCategorySiteVO4> materialCategorySiteIds);
}
