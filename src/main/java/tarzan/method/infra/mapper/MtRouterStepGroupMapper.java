package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtRouterStepGroup;

/**
 * 工艺路线步骤组Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterStepGroupMapper extends BaseMapper<MtRouterStepGroup> {

    List<MtRouterStepGroup> selectRouterStepGroupByIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "routerStepIds") List<String> routerStepIds);
}
