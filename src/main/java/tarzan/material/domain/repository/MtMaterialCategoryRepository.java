package tarzan.material.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.vo.MtMaterialCategoryVO;
import tarzan.material.domain.vo.MtMaterialCategoryVO4;
import tarzan.material.domain.vo.MtMaterialCategoryVO5;
import tarzan.material.domain.vo.MtMaterialCategoryVO6;

/**
 * 物料类别资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialCategoryRepository
                extends BaseRepository<MtMaterialCategory>, AopProxy<MtMaterialCategoryRepository> {

    /**
     * materialCategoryGet-获取物料类别对应物料类别集
     *
     * @param tenantId
     * @param materialCategoryId
     * @return
     */
    MtMaterialCategory materialCategoryGet(Long tenantId, String materialCategoryId);

    /**
     * materialCategoryEnableValidate-验证物料类别是否有效
     *
     * @param tenantId
     * @param materialCategoryId
     * @return
     */
    String materialCategoryEnableValidate(Long tenantId, String materialCategoryId);

    /**
     * setLimitMaterialCategoryQuery-获取类别集下所有有效的物料类别
     *
     * @param tenantId
     * @param materialCategorySetId
     * @return
     */
    List<String> setLimitMaterialCategoryQuery(Long tenantId, String materialCategorySetId);

    /***
     * codeLimitMaterialCategoryQuery-根据编码描述获取有效的物料类别
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> codeLimitMaterialCategoryQuery(Long tenantId, MtMaterialCategory dto);

    /**
     * materialCategoryPropertyBatchGet-批量获取物料类别属性
     *
     * @param tenantId
     * @param materialCategoryIds
     * @return
     */
    List<MtMaterialCategory> materialCategoryPropertyBatchGet(Long tenantId, List<String> materialCategoryIds);

    /**
     * materialCategoryCodeGet-获取物料类别编码描述
     *
     * @param tenantId
     * @param materialCategoryId
     * @return
     */
    MtMaterialCategoryVO materialCategoryCodeGet(Long tenantId, String materialCategoryId);

    /**
     * materialCategorySetGet-获取物料类别对应的物料类别集
     *
     * @param tenantId
     * @param materialCategoryId
     * @return
     */
    String materialCategorySetGet(Long tenantId, String materialCategoryId);

    /**
     * 根据物料类别集Id集合批量查询物料类别
     *
     * @author benjamin
     * @date 2019-09-03 10:59
     * @param tenantId 租户Id
     * @param materialCategorySetIdList 物料类别集Id集合
     * @return list
     */
    List<MtMaterialCategory> queryMaterialCategoryBySetId(Long tenantId, List<String> materialCategorySetIdList);

    /**
     * 根据物料类别编码集合查询物料类别
     *
     * @author benjamin
     * @date 2019-09-03 15:23
     * @param tenantId 租户Id
     * @param materialCategoryCodeList 物料类别编码集合
     * @return list
     */
    List<MtMaterialCategory> queryMaterialCategoryByCode(Long tenantId, List<String> materialCategoryCodeList);

    /**
     * materialCategoryUpdate-物料类别新增&更新
     * 
     * @Author peng.yuan
     * @Date 2019/9/16 17:53
     * @param tenantId : 租户Id
     * @param mtMaterialCategoryVO4 : 物料类别VO
     * @return java.lang.String
     */
    String materialCategoryUpdate(Long tenantId, MtMaterialCategoryVO4 mtMaterialCategoryVO4);

    /**
     * propertyLimitMaterialCategoryPropertyQuery-根据属性获取物料类别信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtMaterialCategoryVO6> propertyLimitMaterialCategoryPropertyQuery(Long tenantId, MtMaterialCategoryVO5 dto);

    /**
     * materialCategoryAttrPropertyUpdate-物料类别新增&更新扩展表属性
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/11/19
     */
    void materialCategoryAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);

    /**
     * materialCategorySetBatchGet-批量获取物料类别对应物料类别集
     *
     * @author benjamin
     * @date 2020/4/8 11:16 AM
     * @param tenantId 租户Id
     * @param materialCategoryIdList 物料类别Id集合
     * @return list
     */
    List<MtMaterialCategoryVO6> materialCategorySetBatchGet(Long tenantId, List<String> materialCategoryIdList);
}
