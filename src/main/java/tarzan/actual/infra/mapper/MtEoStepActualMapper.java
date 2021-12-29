package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.api.dto.MtProcessWorkingDTO6;
import tarzan.actual.domain.entity.MtEoRouterActual;
import tarzan.actual.domain.entity.MtEoStepActual;
import tarzan.actual.domain.vo.*;
import tarzan.order.api.dto.MtEoRouterDTO7;

/**
 * 执行作业-工艺路线步骤执行实绩Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoStepActualMapper extends BaseMapper<MtEoStepActual> {

    String getRouterId(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eoStepActualId") String eoStepActualId);

    List<MtEoStepActualVO41> routerRelaxedFlowBatchQuery(@Param(value = "tenantId") Long tenantId,
                                                         @Param(value = "eoStepActualIdInSql") String eoStepActualIdInSql);

    Long getMaxSequence(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eoRouterActualId") String eoRouterActualId);

    List<MtEoStepActualVO13> selectByEoRouterActual(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtEoStepActualVO12 dto);

    List<String> selectEoStepActualIdByEoIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eoIds") List<String> eoIds);

    int eoAllStepActualIsBypassed(@Param(value = "tenantId") Long tenantId, @Param(value = "eoId") String eoId);

    MtEoStepActual selectSourcePre(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eoStepActualId") String eoStepActualId);

    List<MtEoStepActual> selectAnyPre(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eoStepActualId") String eoStepActualId);

    List<MtEoStepActual> selectUnCompletedByEoId(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eoId") String eoId);

    List<MtEoStepActual> selectByRouterStepIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eoRouterActualId") String eoRouterActualId,
                    @Param(value = "routerStepIds") List<String> routerStepIds,
                    @Param(value = "status") List<String> status);

    MtEoStepActual selectByRouterStepIdAndEoRouterActualId(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtEoStepActualVO20 dto);

    List<MtEoRouterActual> selectByOperationId(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtEoStepActualVO27 dto);

    MtEoStepActual selectPrevious(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eoStepActualId") String eoStepActualId);

    List<MtEoStepActual> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "eoStepActualIds") List<String> eoStepActualIds);

    List<MtEoStepActual> propertyLimitEoStepActualPropertyQuery(@Param(value = "tenantId") Long tenantId,
                                                                @Param(value = "vo") MtEoStepActualVO30 vo);

    /**
     * 根据eoid列表批量查询
     *
     * @Author peng.yuan
     * @Date 2019/11/25 16:38
     * @param tenantId :
     * @param sourceEoStepActualIdList :
     * @return java.util.List<tarzan.actual.domain.entity.MtEoStepActual>
     */
    List<MtEoStepActual> selectByStepActualIdList(@Param(value = "tenantId") Long tenantId,
                                                  @Param(value = "sourceEoStepActualIdList") List<String> sourceEoStepActualIdList);

    /**
     * 根据eoid列表批量查询StepActualId
     *
     * @Author Xie.yiyang
     * @Date 2019/12/17 14:15
     * @param tenantId
     * @param eoIdList
     * @return java.util.List<tarzan.actual.domain.vo.MtEoStepActualVO4>
     */
    List<MtEoStepActualVO4> eoLimitStepActualBatchQuery(@Param(value = "tenantId") Long tenantId,
                                                        @Param(value = "eoIdList") List<String> eoIdList);


    List<MtEoStepActualVO35> eoStepActualProcessedBatchGet(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "eoStepActualIdList") List<String> eoStepActualIdList);

    List<MtEoStepActualVO36> eoAndStepLimitStepActualBatchQuery(@Param(value = "tenantId") Long tenantId,
                                                                @Param(value = "eoMessageList") List<MtEoStepActualVO37> eoMessageList);

    List<MtEoStepActual> eoStepActualListForUi(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "dto") MtEoRouterDTO7 dto);

    List<MtProcessWorkingVO2> selectByOperationCustom(@Param(value = "tenantId") Long tenantId,
                                                      @Param(value = "dto") MtProcessWorkingDTO6 dto);

    MtEoStepActualVO38 selectByEoAndRouter(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "eoRouter") MtEoStepActualVO14 eoRouter);


    List<MtEoStepActualVO43> getMaxSequenceBatch(@Param(value = "tenantId") Long tenantId,
                                                 @Param(value = "eoIdInSql") String eoIdInSql);
}
