package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.vo.MtRouterOperationVO2;
import tarzan.method.domain.vo.MtRouterStepVO5;

/**
 * 工艺路线步骤对应工序Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterOperationMapper extends BaseMapper<MtRouterOperation> {

    List<MtRouterOperationVO2> selectRouterOperationTL(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerOperationId") String routerOperationId);

    List<MtRouterOperation> selectRouterOperationByIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepIds") List<String> routerStepIds);

    List<MtRouterStepVO5> routerOperationBatch(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "routerStepIds") List<String> routerStepIds);
}
