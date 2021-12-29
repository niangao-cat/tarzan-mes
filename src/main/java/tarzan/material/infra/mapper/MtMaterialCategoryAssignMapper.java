package tarzan.material.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.material.domain.entity.MtMaterialCategoryAssign;
import tarzan.material.domain.vo.MaterialCategoryAssignVO;
import tarzan.material.domain.vo.MtMaterialCategoryAssignVO;

/**
 * 物料类别分配Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialCategoryAssignMapper extends BaseMapper<MtMaterialCategoryAssign> {

    List<MtMaterialCategoryAssign> selectData(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "dto") MtMaterialCategoryAssignVO dto);

    List<MtMaterialCategoryAssignVO> selectMaterialBySiteId(@Param(value = "tenantId") Long tenantId,
                                                            @Param(value = "dto") MtMaterialCategoryAssignVO dto);

    List<MaterialCategoryAssignVO> selectMaterialCategoryAssiteById(@Param(value = "tenantId") Long tenantId,
                                                                    @Param(value = "materialId") String materialId);

    List<MtMaterialCategoryAssign> selectByMaterialCategoryIds(@Param(value = "tenantId") Long tenantId,
                                                               @Param(value = "categoryIds") List<String> categoryIds);

    List<MtMaterialCategoryAssign> selectByMaterilSiteIds(@Param(value = "tenantId") Long tenantId,
                                                          @Param(value = "materialSiteIds") String materialSiteIds);

}
