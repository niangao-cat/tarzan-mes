package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.api.dto.MtRouterStepGroupStepDTO;
import tarzan.method.domain.entity.MtRouterStepGroupStep;
import tarzan.method.domain.vo.MtRouterStepGroupStepVO;

/**
 * 工艺路线步骤组行步骤Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterStepGroupStepMapper extends BaseMapper<MtRouterStepGroupStep> {

    List<MtRouterStepGroupStep> selectRouterStepByGroup(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepId") String routerStepId);

    MtRouterStepGroupStepVO selectRouterStepGroupStep(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepId") String routerStepId);

    List<MtRouterStepGroupStepVO> selectRouterStepGroupStepByIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepIds") List<String> routerStepIds);

    List<MtRouterStepGroupStep> selectByGroupIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "stepGroupIds") List<String> stepGroupIds);

    List<MtRouterStepGroupStepDTO> queryRouterStepGroupStepForUi(@Param(value = "tenantId") Long tenantId,
                                                                 @Param(value = "stepGroupIds") List<String> stepGroupIds);
}
