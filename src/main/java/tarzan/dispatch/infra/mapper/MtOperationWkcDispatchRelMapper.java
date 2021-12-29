package tarzan.dispatch.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.dispatch.domain.entity.MtOperationWkcDispatchRel;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO5;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO8;

/**
 * 工艺和工作单元调度关系表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:55:05
 */
public interface MtOperationWkcDispatchRelMapper extends BaseMapper<MtOperationWkcDispatchRel> {

    Long getMaxPriority(@Param(value = "tenantId") Long tenantId, @Param("operationId") String operationId,
                        @Param("stepName") String stepName);

    List<MtOpWkcDispatchRelVO8> propertyLimitOperationWkcQuery(@Param(value = "tenantId") Long tenantId,
                                                               @Param("dto") MtOpWkcDispatchRelVO5 dto, @Param("fuzzyQueryFlag") String fuzzyQueryFlag);
}
