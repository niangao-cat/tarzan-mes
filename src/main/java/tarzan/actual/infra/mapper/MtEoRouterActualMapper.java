package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtEoRouterActual;
import tarzan.actual.domain.vo.*;

/**
 * EO工艺路线实绩Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoRouterActualMapper extends BaseMapper<MtEoRouterActual> {

    List<MtEoRouterActualVO5> eoOperationLimitCurrentRouterStepGet(@Param(value = "tenantId") Long tenantId,
                                                                   @Param(value = "dto") MtEoRouterActualVO2 dto);

    List<MtEoRouterActualVO4> eoWkcLimitCurrentStepWkcActualGet(@Param(value = "tenantId") Long tenantId,
                                                                @Param(value = "dto") MtEoRouterActualVO3 dto);

    Long getMaxSequence(@Param(value = "tenantId") Long tenantId, @Param(value = "eoId") String eoId);

    List<MtEoRouterActual> propertyLimitEoRouterActualQuery(@Param(value = "tenantId") Long tenantId,
                                                            @Param(value = "dto") MtEoRouterActualVO20 dto);

    List<MtEoRouterActual> propertyLimitEoRouterActualBatchQuery(@Param(value = "tenantId") Long tenantId,
                                                                 @Param(value = "dtoList") List<MtEoRouterActualVO20> dtoList);

    MtEoRouterActual selectEoRouterActualByCondition(@Param(value = "tenantId") Long tenantId,
                                                     @Param(value = "eoRouterActualId") String eoRouterActualId);

    MtEoRouterActual selectEoRouterActualByCondition2(@Param(value = "tenantId") Long tenantId,
                                                      @Param(value = "condition") MtEoRouterActual condition);

    List<MtEoRouterActual> propertyLimitEoRouterActualPropertyQuery(@Param(value = "tenantId") Long tenantId,
                                                                    @Param(value = "dto") MtEoRouterActualVO29 dto);

    /**
     * 根据oStepActualIdList 查找数据
     *
     * @Author peng.yuan
     * @Date 2019/11/25 14:37
     * @param tenantId :
     * @param sourceEoStepActualIdList :
     * @return java.util.List<tarzan.actual.domain.entity.MtEoRouterActual>
     */
    List<MtEoRouterActualVO35> selectAllByStepActualId(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "sourceEoStepActualIdList") List<String> sourceEoStepActualIdList);

    List<MtEoStepActualVO43> getMaxSequenceBatch(@Param(value = "tenantId") Long tenantId,
                                                 @Param(value = "eoIdInSql") String eoIdInSql);
}
