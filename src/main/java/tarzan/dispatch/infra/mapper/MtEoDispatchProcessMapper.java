package tarzan.dispatch.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.vo.MtEoStepActualVO36;
import tarzan.dispatch.api.dto.MtEoDispatchPlatformDTO;
import tarzan.dispatch.domain.entity.MtEoDispatchProcess;
import tarzan.dispatch.domain.vo.MtEoDispatchPlatformVO1;
import tarzan.dispatch.domain.vo.MtEoDispatchProcessVO13;
import tarzan.dispatch.domain.vo.MtEoDispatchProcessVO9;

/**
 * 调度过程处理表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:54:58
 */
public interface MtEoDispatchProcessMapper extends BaseMapper<MtEoDispatchProcess> {

    /**
     * select dispatch process
     * <p>
     * condition: EO_DISPATCH_PROCESS_ID
     *
     * @param tenantId
     * @param processIds
     * @return list
     * @author benjamin
     * @date 2019-06-12 16:07
     */
    List<MtEoDispatchProcess> eoDispatchProcessQueryById(@Param(value = "tenantId") Long tenantId,
                                                         @Param(value = "processIds") List<String> processIds);

    List<MtEoDispatchProcess> propertyLimitDispatchedProcessPropertyQuery(@Param(value = "tenantId") Long tenantId,
                                                                          @Param(value = "vo") MtEoDispatchProcessVO9 vo);

    List<MtEoDispatchProcess> propertyLimitDispatchedProcessPropertyBatchQuery(@Param(value = "tenantId") Long tenantId,
                                                                               @Param(value = "vo") MtEoDispatchProcessVO13 vo);

    /**
     * 前台分页使用
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoDispatchPlatformVO1> dispatchPlatformEoQueryUi(@Param(value = "tenantId") Long tenantId,
                                                            @Param(value = "dto") MtEoDispatchPlatformDTO dto);

    List<MtEoDispatchProcess> eoAndRouterStepLimitQuery(@Param(value = "tenantId") Long tenantId,
                                                        @Param(value = "dtos") List<MtEoStepActualVO36> dtos,
                                                        @Param(value = "workcellId") String workcellId);
}
