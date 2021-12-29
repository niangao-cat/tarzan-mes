package tarzan.material.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.material.api.dto.MtMaterialCategorySetDTO;
import tarzan.material.domain.entity.MtMaterialCategorySet;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO5;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO6;

/**
 * 物料类别集Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialCategorySetMapper extends BaseMapper<MtMaterialCategorySet> {

    List<MtMaterialCategorySet> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                                                  @Param(value = "materialCategorySetIds") List<String> materialCategorySetIds);

    List<MtMaterialCategorySet> selectList(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "dto") MtMaterialCategorySetDTO dto);

    List<MtMaterialCategorySet>  queryMaterialCategorySetByCode(@Param(value = "tenantId") Long tenantId,
                                                                @Param(value = "categorySetCodeList") String categorySetCodeList);

    List<MtMaterialCategorySiteVO6> selectCondition(@Param(value = "tenantId") Long tenantId,
                                                    @Param(value = "dto") MtMaterialCategorySiteVO5 dto);
}

