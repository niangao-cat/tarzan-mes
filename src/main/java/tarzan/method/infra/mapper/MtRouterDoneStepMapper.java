package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtRouterDoneStep;

/**
 * 完成步骤Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterDoneStepMapper extends BaseMapper<MtRouterDoneStep> {

    List<MtRouterDoneStep> selectRouterDoneSteByIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepIds") List<String> routerStepIds);
}
