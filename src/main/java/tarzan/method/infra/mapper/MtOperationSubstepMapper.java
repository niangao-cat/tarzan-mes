package tarzan.method.infra.mapper;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.api.dto.MtOperationSubstepDTO;
import tarzan.method.domain.entity.MtOperationSubstep;

import org.apache.ibatis.annotations.Param;

/**
 * 工艺子步骤Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:19:27
 */
public interface MtOperationSubstepMapper extends BaseMapper<MtOperationSubstep> {
    /**
     * 查询工艺下子步骤列表
     *
     * @author benjamin
     * @date 2019/9/9 11:33 AM
     * @param tenantId 租户Id
     * @param operationId 工艺Id
     * @return list
     */
    List<MtOperationSubstepDTO> queryOperationSubstepForUi(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "operationId") String operationId);
}
