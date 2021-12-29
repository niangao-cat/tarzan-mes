package tarzan.material.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtMaterialCategorySet;
import tarzan.material.domain.vo.MtMaterialCategorySetVO;
import tarzan.material.domain.vo.MtMaterialCategorySetVO2;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO5;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO6;

/**
 * 物料类别集资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialCategorySetRepository
                extends BaseRepository<MtMaterialCategorySet>, AopProxy<MtMaterialCategorySetRepository> {
    /**
     * defaultCategorySetGet-获取指定类型的默认类别集
     *
     * @param tenantId
     * @param type
     * @return
     */
    List<MtMaterialCategorySet> defaultCategorySetGet(Long tenantId, String type);

    /**
     * materialCategorySetPropertyGet-获取物料类别集属性
     *
     * @param tenantId
     * @param materialCategorySetId
     * @return
     */
    MtMaterialCategorySet materialCategorySetPropertyGet(Long tenantId, String materialCategorySetId);

    /**
     * defaultCategorySetValidate-验证物料类别集是否为默认类别集
     *
     * @param tenantId
     * @param materialCategorySetId
     * @return
     */
    String defaultCategorySetValidate(Long tenantId, String materialCategorySetId);

    /**
     * materialCategorySetPropertyBatchGet-批量获取物料类别集的属性
     *
     * @param tenantId
     * @param materialCategorySetIds
     * @return
     */
    List<MtMaterialCategorySet> materialCategorySetPropertyBatchGet(Long tenantId, List<String> materialCategorySetIds);

    /**
     * 根据物料类别集编码批量查询
     *
     * @author benjamin
     * @date 2019-09-02 17:17
     * @param tenantId 租户Id
     * @param categorySetCodeList 物料类别集编码集合
     * @return list
     */
    List<MtMaterialCategorySet> queryMaterialCategorySetByCode(Long tenantId, List<String> categorySetCodeList);


    /**
     * 物料类别集新增&更新
     * 
     * @Author peng.yuan
     * @Date 2019/9/16 15:11
     * @param tenantId : 租户Id
     * @param mtMaterialCategorySetVo : 物料类别集新增集合参数
     * @return java.lang.String
     */
    String materialCategorySetUpdate(Long tenantId, MtMaterialCategorySetVO mtMaterialCategorySetVo);

    /**
     * propertyLimitMaterialCategorySetPropertyQuery-根据属性获取物料类别集信息
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtMaterialCategorySiteVO6> propertyLimitMaterialCategorySetPropertyQuery(Long tenantId,
                                                                                  MtMaterialCategorySiteVO5 dto);

    /**
     * 物料类别集新增&更新扩展表属性
     * 
     * @Author peng.yuan
     * @Date 2019/11/19 10:43
     * @param tenantId :
     * @param mtExtendVO10 :
     * @return void
     */
    void materialCategorySetAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10);

    /**
     * defaultCategorySetBatchGet-批量获取指定类型的默认类别集
     *
     * @author benjamin
     * @date 2020/4/8 10:26 AM
     * @param tenantId 租户Id
     * @param defaultTypes 默认类型列表
     * @return list
     */
    List<MtMaterialCategorySetVO2> defaultCategorySetBatchGet(Long tenantId, List<String> defaultTypes);
}
