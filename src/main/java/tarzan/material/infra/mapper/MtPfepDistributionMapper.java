package tarzan.material.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.material.api.dto.MtPfepDistributionAllDTO1;
import tarzan.material.domain.entity.MtPfepDistribution;
import tarzan.material.domain.vo.MtPfepDistributionVO3;

/**
 * 物料配送属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepDistributionMapper extends BaseMapper<MtPfepDistribution> {

    List<MtPfepDistribution> materialDistributionPfepQuery(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "dto") MtPfepDistributionVO3 dto);

    List<MtPfepDistributionAllDTO1> materialDistributionPfepAllQuery(@Param(value = "tenantId") Long tenantId,
                                                                     @Param(value = "dto") MtPfepDistributionAllDTO1 dto);

    List<MtPfepDistribution> materialDistributionPfepQueryByUnique(@Param(value = "tenantId") Long tenantId,
                                                                   @Param(value = "dto") MtPfepDistribution dto);

    List<MtPfepDistribution> materialDistributionPfepQueryByCopy(@Param(value = "tenantId") Long tenantId,
                                                                 @Param(value = "dto") MtPfepDistribution dto);

}
