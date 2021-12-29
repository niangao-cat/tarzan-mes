package tarzan.dispatch.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.vo.MtEoStepActualVO37;
import tarzan.dispatch.domain.entity.MtEoDispatchAction;
import tarzan.dispatch.domain.vo.*;


/**
 * 调度结果执行表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:54:56
 */
public interface MtEoDispatchActionRepository
                extends BaseRepository<MtEoDispatchAction>, AopProxy<MtEoDispatchActionRepository> {

    /**
     * wkcShiftLimitDispatchedPublishedEoQuery-获取指定班次已调度发布的执行作业
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoDispatchAction> wkcShiftLimitDispatchedPublishedEoQuery(Long tenantId, MtEoDispatchActionVO1 dto);

    /**
     * dispatchedEoRevocableQtyGet-获取已调度的执行作业的可修改数量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    Double dispatchedEoRevocableQtyGet(Long tenantId, MtEoDispatchActionVO2 dto);

    /**
     * wkcShiftLimitDispatchedEoQuery-获取工艺或工作单元上所有已经调度的执行作业
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoDispatchActionVO3> wkcShiftLimitDispatchedEoQuery(Long tenantId, MtEoDispatchActionVO1 dto);

    /**
     * wkcShiftLimitDispatchedOngoingEoQuery-获取指定工作单元下所有已调度且正在运行的执行作业
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoDispatchActionVO3> wkcShiftLimitDispatchedOngoingEoQuery(Long tenantId, MtEoDispatchActionVO1 dto);

    /**
     * wkcShiftPeriodLimitDispatchedPublishedEoQuery-获取指定班次预设范围内已调度发布的执行作业
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoDispatchAction> wkcShiftPeriodLimitDispatchedPublishedEoQuery(Long tenantId, MtEoDispatchActionVO4 dto);

    /**
     * eoOnStandbyQuery-获取调度完成待生产的执行作业
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoDispatchActionVO5> eoOnStandbyQuery(Long tenantId, MtEoDispatchActionVO1 dto);

    /**
     * dispatchedEoAssignQtyGet-获取执行作业已调度数量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtEoDispatchActionVO7 dispatchedEoAssignQtyGet(Long tenantId, MtEoDispatchActionVO2 dto);

    /**
     * eoDispatchVerify-执行作业调度验证
     *
     * @param tenantId
     * @param dto
     */
    void eoDispatchVerify(Long tenantId, MtEoDispatchActionVO8 dto);

    /**
     * eoDispatch-执行作业调度
     *
     * @param tenantId
     * @param dto
     */
    String eoDispatch(Long tenantId, MtEoDispatchActionVO9 dto);

    /**
     * eoDispatchCancelVerify-调度取消验证
     *
     * @param tenantId
     * @param dto
     */
    void eoDispatchCancelVerify(Long tenantId, MtEoDispatchActionVO6 dto);

    /**
     * toBeDispatchedEoUnassignQtyGet-获取需调度执行作业未调度数量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtEoDispatchActionVO10 toBeDispatchedEoUnassignQtyGet(Long tenantId, MtEoDispatchActionVO16 dto);

    /**
     * dispatchedEoPriorityGenerate-执行作业调度过程优先级生成
     * 
     * @Author lxs
     * @param tenantId
     * @param vo
     * @return
     */
    Long dispatchedEoPriorityGenerate(Long tenantId, MtEoDispatchActionVO1 vo);

    /***
     * processLimitDispatchActionAndHistoryUpdate-根据调度过程更新执行作业调度结果并记录调度历史
     * 
     * @Author: L.X.S
     * @Date: 2019/3/15 14:31
     * @param tenantId
     * @param eoDispatchProcessId
     ***/
    MtEoDispatchActionVO17 processLimitDispatchActionAndHistoryUpdate(Long tenantId, String eoDispatchProcessId);


    /**
     * eoDispatchPublish-调度结果发布
     * 
     * @author lxs
     * @date 2019.3.18
     * @param tenantId
     * @param dto
     */
    void eoDispatchPublish(Long tenantId, MtEoDispatchActionVO20 dto);

    /**
     * eoDispatchCancel-调度取消
     * 
     * @author lxs
     * @date 2019.3.18
     * @param tenantId
     * @param dto
     */
    String eoDispatchCancel(Long tenantId, MtEoDispatchActionVO11 dto);

    /**
     * sequenceLimitDispatchedEoPriorityAdjust-根据已调度执行作业顺序调整调度优先级
     * 
     * @author lxs
     * @date 2019.4.10
     * @param tenantId
     * @param dto
     */
    void sequenceLimitDispatchedEoPriorityAdjust(Long tenantId, List<MtEoDispatchActionVO15> dto);

    /**
     * sequenceLimitPriorityAdjust-根据顺序调整优先级
     * 
     * @Author lxs
     * @Date 2019/4/10
     * @Params [tenantId, dto]
     * @Return java.util.List<hmes.eo_dispatch_action.view.MtEoDispatchActionVO12>
     */
    List<MtEoDispatchActionVO12> sequenceLimitPriorityAdjust(Long tenantId, List<MtEoDispatchActionVO12> dto);

    /**
     * eoLimitDispatchStrategyGet-根据执行作业获取生产线调度策略
     *
     * @param tenantId
     * @param eoId
     * @return
     */
    MtEoDispatchActionVO24 eoLimitDispatchStrategyGet(Long tenantId, String eoId);

    /**
     * propertyLimitDispatchedActionPropertyQuery-根据获取调度结果信息
     * 
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtEoDispatchActionVO19> propertyLimitDispatchedActionPropertyQuery(Long tenantId, MtEoDispatchActionVO18 vo);

    /**
     * propertyLimitDispatchedActionPropertyBatchQuery-根据属性获取调度结果信息
     * 
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtEoDispatchActionVO19> propertyLimitDispatchedActionPropertyBatchQuery(Long tenantId,
                                                                                 MtEoDispatchActionVO21 vo);

    /**
     * eoLimitDispatchStrategyBatchGet-批量获取执行作业生产线调度策略
     *
     * @param tenantId
     * @param eoIds
     * @return
     */
    MtEoDispatchActionVO24 eoLimitDispatchStrategyBatchGet(Long tenantId, List<String> eoIds);

    /**
     * toBeDispatchedEoUnassignQtyBatchGet-批量获取需调度执行作业未调度分配数量
     *
     * @param tenantId
     * @author guichuan.li
     * @date 2019/12/19
     */
    List<MtEoDispatchActionVO23> toBeDispatchedEoUnassignQtyBatchGet(Long tenantId, List<MtEoStepActualVO37> dto);


    /**
     * productionLineLimitDispatchStrategyGet-获取生产线调度策略
     * 
     * @param tenantId
     * @param productionLineId
     * @return
     */
    MtEoDispatchActionVO24 productionLineLimitDispatchStrategyGet(Long tenantId, String productionLineId);

    /**
     * dispatchActionDelete-调度结果数据删除
     * 
     * @param tenantId
     * @param dto
     */
    void dispatchActionDelete(Long tenantId, MtEoDispatchActionVO20 dto);
}
