package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.vo.*;

/**
 * 工艺路线步骤Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterStepMapper extends BaseMapper<MtRouterStep> {

    List<String> selectOperationStep(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "operationId") String operationId, @Param(value = "routerId") String routerId);

    List<MtRouterStepVO7> selectComParentStep(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepId") String routerStepId);

    List<MtRouterStepVO7> selectNonComParentStep(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepId") String routerStepId);

    List<MtRouterStepVO5> selectRouterStepOp(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerId") String routerId);

    List<String> selectChildEntry(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepId") String routerStepId);

    List<MtRouterStepVO9> selectChild(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepId") String routerStepId);

    List<String> selectStepByGroupStep(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepId") String routerStepId);

    List<MtRouterStepVO4> selectRouterStepTL(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepId") String routerStepId);

    List<MtRouterStepVO3> routerComponentQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "condition") MtRouterStepVO2 condition);

    List<MtRouterStepVO12> selectCondition(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "dto") MtRouterStepVO11 dto);

    List<MtRouterStep> selectRouterStepByRouter(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "routerId") List<String> routerId);

    List<String> selectByWoRouter(@Param(value = "tenantId") Long tenantId,
                                  @Param(value = "routerId") String routerId);

    List<MtRouterStepVO5> selectStepByRouterIds(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "routerIds") List<String> routerIds);
}
