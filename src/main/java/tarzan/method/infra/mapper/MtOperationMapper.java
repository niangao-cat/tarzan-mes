package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.api.dto.MtOperationDTO3;
import tarzan.method.api.dto.MtOperationDTO4;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.vo.MtOperationVO;
import tarzan.method.domain.vo.MtOperationVO1;
import tarzan.method.domain.vo.MtOperationVO2;
import tarzan.method.domain.vo.MtOperationVO3;

/**
 * 工序Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:19:27
 */
public interface MtOperationMapper extends BaseMapper<MtOperation> {
    int selectOperAvailability(@Param(value = "tenantId") Long tenantId,
                               @Param(value = "operationId") String operationId);

    List<MtOperation> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                                        @Param(value = "operationIds") List<String> operationIds);

    List<MtOperationDTO4> queryOperationForUi(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "dto") MtOperationDTO3 dto);

    List<MtOperationVO1> selectCondition(@Param(value = "tenantId") Long tenantId,
                                         @Param(value = "dto") MtOperationVO dto);

    List<MtOperation> selectByNameCustom(@Param(value = "tenantId") Long tenantId,
                                         @Param(value = "siteId") String siteId,
                                         @Param(value = "operationNames") List<String> operationNames);


    List<MtOperation> propertyLimitOperationQuery(@Param(value = "tenantId") Long tenantId,
                                                  @Param(value = "vo") MtOperationVO2 vo);

    List<MtOperation> propertyLimitOperationBatchQuery(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "vo") MtOperationVO3 vo);

    List<MtOperation> selectByOperationName(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "operationNames") String operationNames);
}
