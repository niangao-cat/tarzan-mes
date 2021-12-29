package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.api.dto.MtRouterSubstepComponentDTO;
import tarzan.method.domain.entity.MtRouterSubstepComponent;

/**
 * 子步骤组件Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterSubstepComponentMapper extends BaseMapper<MtRouterSubstepComponent> {

    List<MtRouterSubstepComponent> selectBySubStepIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "subStepIds") List<String> subStepIds);

    List<MtRouterSubstepComponentDTO> queryRouterSubstepComponentForUi(@Param(value = "tenantId") Long tenantId,
                                                                       @Param(value = "subStepIds") List<String> subStepIds);
}
