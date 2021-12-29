package tarzan.material.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.material.domain.entity.MtMaterialCategoryAssign;
import tarzan.material.domain.vo.*;

/**
 * 物料类别分配资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialCategoryAssignRepository
                extends BaseRepository<MtMaterialCategoryAssign>, AopProxy<MtMaterialCategoryAssignRepository> {

    /**
     * setLimitMaterialAssignCategoryGet-获取物料指定站点下指定类别集的物料类别
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String setLimitMaterialAssignCategoryGet(Long tenantId, MtMaterialCategoryAssignVO dto);

    /**
     * defaultSetMaterialAssignCategoryGet-获取物料指定站点下默认类别集的物料类别
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String defaultSetMaterialAssignCategoryGet(Long tenantId, MtMaterialCategoryAssignVO dto);

    /**
     * categorySiteLimitMaterialQuery-获取指定站点类别下的有效物料
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> categorySiteLimitMaterialQuery(Long tenantId, MtMaterialCategoryAssignVO dto);

    /**
     * materialCategoryAssignValidate-获取指定站点下物料是否分配了指定类别
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String materialCategoryAssignValidate(Long tenantId, MtMaterialCategoryAssignVO dto);

    /**
     * materialCategoryAssignUniqueValidate-检验物料在指定站点指定类别集下物料类别唯一
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String materialCategoryAssignUniqueValidate(Long tenantId, MtMaterialCategoryAssignVO dto);

    /**
     * materialCategoryAssign-为指定站点下的物料分配物料类别
     * 
     * @author benjamin
     * @date 2019/9/16 4:32 PM
     * @param tenantId 租户Id
     * @param assignVO MtMaterialCategoryAssignVO2
     * @return String
     */
    String materialCategoryAssign(Long tenantId, MtMaterialCategoryAssignVO2 assignVO);

    /**
     * 根据物料类别批量获取物料类别分配数据
     * 
     * @param tenantId
     * @param categoryIds
     * @return
     */
    List<MtMaterialCategoryAssign> materialCategoryAssignByCategoryIds(Long tenantId, List<String> categoryIds);

    /**
     * 根据物料类别批量获取物料类别分配数据
     *
     * @param tenantId
     * @param materialSiteIds
     * @return
     */
    List<MtMaterialCategoryAssign> materialCategoryAssignByMaterilSiteIds(Long tenantId, List<String> materialSiteIds);

    /**
     * defaultSetMaterialAssignCategoryBatchGet-批量获取物料指定站点下默认类别集的物料类别
     *
     * @Author Xie.yiyang
     * @Date 2020/4/8 13:48
     * @param tenantId
     * @param dto
     * @return java.util.List<tarzan.material.domain.vo.MtMaterialCategoryAssignVO6>
     */
    List<MtMaterialCategoryAssignVO6> defaultSetMaterialAssignCategoryBatchGet(Long tenantId,
                                                                               List<MtMaterialCategoryAssignVO5> dto);

    /**
     * setLimitMaterialAssignCategoryBatchGet-批量获取物料指定站点下指定类别集的物料类别
     *
     * @Author Xie.yiyang
     * @Date 2020/4/8 10:36
     * @param tenantId
     * @param dto
     * @return java.util.List<tarzan.material.domain.vo.MtMaterialCategoryAssignVO4>
     */
    List<MtMaterialCategoryAssignVO4> setLimitMaterialAssignCategoryBatchGet(Long tenantId,
                                                                             List<MtMaterialCategoryAssignVO3> dto);


}
