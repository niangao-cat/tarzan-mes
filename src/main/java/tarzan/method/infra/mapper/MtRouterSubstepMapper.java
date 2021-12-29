package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.api.dto.MtRouterSubstepDTO;
import tarzan.method.domain.entity.MtRouterSubstep;

/**
 * 工艺路线子步骤Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterSubstepMapper extends BaseMapper<MtRouterSubstep> {

    List<MtRouterSubstep> selectByStepIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepIds") List<String> routerStepIds);

    List<MtRouterSubstepDTO> queryRouterSubstepForUi(@Param(value = "tenantId") Long tenantId,
                                                     @Param(value = "routerStepIds") List<String> routerStepIds);

}
