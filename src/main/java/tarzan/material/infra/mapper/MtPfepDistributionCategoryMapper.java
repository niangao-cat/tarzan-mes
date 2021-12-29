package tarzan.material.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.material.domain.entity.MtPfepDistributionCategory;
import tarzan.material.domain.vo.MtPfepInventoryCategoryVO3;

/**
 * 物料类别配送属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepDistributionCategoryMapper extends BaseMapper<MtPfepDistributionCategory> {

    List<MtPfepDistributionCategory> materialCategoryDistributionPfepQuery(@Param(value = "tenantId") Long tenantId,
                                                                           @Param(value = "dto") MtPfepInventoryCategoryVO3 dto);

    List<MtPfepDistributionCategory> materialCategoryDistributionPfepQueryByUnique(
            @Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtPfepDistributionCategory dto);

    List<MtPfepDistributionCategory> materialCategoryDistributionPfepQueryByCopy(
            @Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtPfepDistributionCategory dto);

}
