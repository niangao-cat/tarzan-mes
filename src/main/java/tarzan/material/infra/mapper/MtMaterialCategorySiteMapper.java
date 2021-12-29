package tarzan.material.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.material.domain.entity.MtMaterialCategorySite;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO2;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO3;

/**
 * 物料类别站点分配Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialCategorySiteMapper extends BaseMapper<MtMaterialCategorySite> {

    MtMaterialCategorySite selectPfepItem(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "dto") MtMaterialCategorySite dto);

    List<MtMaterialCategorySiteVO> selectMaterialCategorySiteByIdForUi(@Param(value = "tenantId") Long tenantId,
                                                                       @Param(value = "categoryId") String categoryId);

    List<MtMaterialCategorySiteVO2> materialCategorySiteNotExistForLov(@Param(value = "tenantId") Long tenantId,
                                                                       @Param(value = "condition") MtMaterialCategorySiteVO3 condition);

    List<MtMaterialCategorySite> selectByMaterialCategorySiteIds(@Param(value = "tenantId") Long tenantId,
                                                                 @Param(value = "materialCategorySiteIds") List<String> materialCategorySiteIds);

    List<MtMaterialCategorySite> selectByMaterialCategoryIds(@Param(value = "tenantId") Long tenantId,
                                                             @Param(value = "materialCategoryIds") String materialCategoryIds);
}
