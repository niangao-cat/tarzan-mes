package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.api.dto.MtRouterOperationComponentDTO;
import tarzan.method.domain.entity.MtRouterOperationComponent;
import tarzan.method.domain.vo.MtRouterOpComponentVO1;
import tarzan.method.domain.vo.MtRouterOpComponentVO4;
import tarzan.method.domain.vo.MtRouterOpComponentVO5;

/**
 * 工艺路线步骤对应工序组件Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
public interface MtRouterOperationComponentMapper extends BaseMapper<MtRouterOperationComponent> {

    List<MtRouterOperationComponent> selectRouterOperationComponent(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepId") String routerStepId);

    List<MtRouterOperationComponent> selectRouterOperationComponent2(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "condition") MtRouterOpComponentVO1 condition);

    List<MtRouterOperationComponent> selectByRouterId(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerId") String routerId);

    List<MtRouterOperationComponent> selectByOperationIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "operationIds") List<String> operationIds);

    List<MtRouterOperationComponentDTO> queryRouterOperationComponentForUi(@Param(value = "tenantId") Long tenantId,
                                                                           @Param(value = "operationIds") List<String> operationIds);

    List<MtRouterOpComponentVO4> selectByRouterIds(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "routerIds") List<String> routerIds);

    List<MtRouterOpComponentVO5> selectRouterOperationComponents(@Param(value = "tenantId") Long tenantId,
                                                                 @Param(value = "bomComponentId") String bomComponentId,
                                                                 @Param(value = "routerStepIds") List<String> routerStepIds);

}
