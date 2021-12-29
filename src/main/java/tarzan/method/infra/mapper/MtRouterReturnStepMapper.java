package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.api.dto.MtRouterReturnStepDTO;
import tarzan.method.domain.entity.MtRouterReturnStep;
import tarzan.method.domain.vo.MtRouterReturnStepVO;

/**
 * 返回步骤Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
public interface MtRouterReturnStepMapper extends BaseMapper<MtRouterReturnStep> {

    List<MtRouterReturnStep> selectRouterReturnStepByIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepIds") List<String> routerStepIds);

    MtRouterReturnStepVO selectReturnStepOperation(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepId") String routerStepId);

    List<MtRouterReturnStepDTO> queryRouterReturnStepForUi(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "routerStepIds") List<String> routerStepIds);
}
