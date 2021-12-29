package tarzan.dispatch.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.vo.MtEoStepActualVO36;
import tarzan.calendar.domain.vo.MtCalendarShiftVO4;
import tarzan.dispatch.domain.entity.MtEoDispatchAction;
import tarzan.dispatch.domain.vo.MtEoDispatchActionVO1;
import tarzan.dispatch.domain.vo.MtEoDispatchActionVO18;
import tarzan.dispatch.domain.vo.MtEoDispatchActionVO21;
import tarzan.dispatch.domain.vo.MtEoDispatchActionVO4;

/**
 * 调度结果执行表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:54:56
 */
public interface MtEoDispatchActionMapper extends BaseMapper<MtEoDispatchAction> {

    /**
     * select eo dispatch action
     * <p>
     * condition: SHIFT_DATE & SHIFT_CODE
     *
     * @param tenantId
     * @param shiftCodeAndDates
     * @param condition
     * @return list
     * @author benjamin
     * @date 2019-06-12 15:12
     */
    List<MtEoDispatchAction> selectByShiftCodesAndDates(@Param(value = "tenantId") Long tenantId,
                                                        @Param(value = "shiftCodeAndDates") List<MtCalendarShiftVO4> shiftCodeAndDates,
                                                        @Param(value = "condition") MtEoDispatchActionVO4 condition);

    /**
     * 根据参数获取最大版本
     *
     * @param condition
     * @return
     */
    Long getMaxRevision(@Param(value = "tenantId") Long tenantId,
                        @Param(value = "condition") MtEoDispatchActionVO1 condition);

    /**
     * 根据主键获取数据
     *
     * @param actionIds
     * @return
     */
    List<MtEoDispatchAction> eoDispatchActionQueryById(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "actionIds") List<String> actionIds);

    /**
     * 根据获取调度结果信息
     *
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtEoDispatchAction> propertyLimitDispatchedActionPropertyQuery(@Param(value = "tenantId") Long tenantId,
                                                                        @Param(value = "vo") MtEoDispatchActionVO18 vo);

    List<MtEoDispatchAction> propertyLimitDispatchedActionPropertyBatchQuery(@Param(value = "tenantId") Long tenantId,
                                                                             @Param(value = "vo") MtEoDispatchActionVO21 vo);

    List<MtEoDispatchAction> eoAndRouterStepLimitQuery(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "dtos") List<MtEoStepActualVO36> dtos,
                                                       @Param(value = "workcellId") String workcellId);
}
