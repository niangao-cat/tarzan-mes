package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.api.dto.MtRouterNextStepDTO;
import tarzan.method.domain.entity.MtRouterNextStep;
import tarzan.method.domain.vo.MtRouterNextStepVO;

/**
 * 下一步骤Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterNextStepMapper extends BaseMapper<MtRouterNextStep> {

    List<MtRouterNextStep> selectRouterNextStepByStepIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepIds") List<String> routerStepIds);

    List<MtRouterNextStepVO> selectKeyStepFlagByStepIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepIds") List<String> routerStepIds);

    List<MtRouterNextStepDTO> queryRouterNextStepForUi(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "routerStepIds") List<String> routerStepIds);
}
