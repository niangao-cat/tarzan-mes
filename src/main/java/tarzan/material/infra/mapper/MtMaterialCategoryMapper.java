package tarzan.material.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.vo.MtMaterialCategoryVO2;
import tarzan.material.domain.vo.MtMaterialCategoryVO3;
import tarzan.material.domain.vo.MtMaterialCategoryVO5;
import tarzan.material.domain.vo.MtMaterialCategoryVO6;

/**
 * 物料类别Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
public interface MtMaterialCategoryMapper extends BaseMapper<MtMaterialCategory> {

    List<MtMaterialCategory> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "materialCategoryIds") List<String> materialCategoryIds);

    List<MtMaterialCategoryVO3> selectByConditionForUi(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "condition") MtMaterialCategoryVO2 condition);

    List<MtMaterialCategory> queryMaterialCategoryBySetId(@Param(value = "tenantId") Long tenantId,
                                                          @Param(value = "materialCategorySetIdList") String materialCategorySetIdList);

    List<MtMaterialCategory> queryMaterialCategoryByCode(@Param(value = "tenantId") Long tenantId,
                                                         @Param(value = "materialCategoryCodeList") String materialCategoryCodeList);

    List<MtMaterialCategoryVO6> selectCondition(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "dto") MtMaterialCategoryVO5 dto);
}
