package tarzan.actual.domain.repository;

import java.util.Date;
import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtEoStepActual;
import tarzan.actual.domain.entity.MtEoStepActualHis;
import tarzan.actual.domain.vo.*;

/**
 * 执行作业-工艺路线步骤执行实绩资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoStepActualRepository extends BaseRepository<MtEoStepActual>, AopProxy<MtEoStepActualRepository> {
    String saveHis(Long tenantId, MtEoStepActual t, Date nowtime, String eventId);

    /**
     * 验证执行作业是否松散模式
     *
     * @param tenantId
     * @param eoStepActualId
     * @return
     */
    String eoStepRelaxedFlowValidate(Long tenantId, String eoStepActualId);

    /**
     * eoStepRelaxedFlowBatchValidate-批量验证执行作业是否松散模式
     *
     * @author chuang.yang
     * @date 2020/10/23
     * @param tenantId
     * @param eoStepActualIds
     */
    List<MtEoStepActualVO41> eoStepRelaxedFlowBatchValidate(Long tenantId, List<String> eoStepActualIds);

    /**
     * 执行作业步骤生产实绩更新
     *
     * @param tenantId
     * @param dto
     */
    String eoStepProductionResultAndHisUpdate(Long tenantId, MtEoStepActualHis dto);

    /**
     * 执行作业步骤作业次数更新
     *
     * @param tenantId
     * @param dto
     */
    MtEoStepActualVO28 eoStepActualProcessedTimesUpdate(Long tenantId, MtEoStepActualVO15 dto);

    /**
     * 执行作业步骤保留次数更新
     *
     * @param tenantId
     * @param dto
     */
    MtEoStepActualVO28 eoStepActualHoldTimesUpdate(Long tenantId, MtEoStepActualVO9 dto);

    /**
     * 执行作业步骤实绩生成
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtEoStepActualVO28 eoStepActualAndHisCreate(Long tenantId, MtEoStepActualHis dto);

    /**
     * stepActualLimitEoAndRouterGet-根据执行作业步骤实绩获取执行作业和工艺路线信息
     *
     * @param tenantId
     * @param eoStepActualId
     * @return
     */
    MtEoStepActualVO1 stepActualLimitEoAndRouterGet(Long tenantId, String eoStepActualId);

    /**
     * 执行作业含工作单元及库存明细步骤实绩更新
     *
     * @param tenantId
     * @param dto
     */
    MtEoStepActualVO29 eoWkcAndStepWipUpdate(Long tenantId, MtEoStepActualVO2 dto);

    /**
     * operationLimitEoStepActualQuery-根据执行作业和工艺限制获取执行作业步骤实绩
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoStepActualVO4> operationLimitEoStepActualQuery(Long tenantId, MtEoStepActualVO3 dto);

    /**
     * eoStepActualProcessedGet-获取执行作业步骤实绩生产进度
     *
     * @param tenantId
     * @param eoStepActualId
     * @return
     */
    MtEoStepActualVO5 eoStepActualProcessedGet(Long tenantId, String eoStepActualId);

    /**
     * 获取执行作业步骤加工周期
     *
     * @param tenantId
     * @param eoStepActualId
     * @return
     */
    String eoStepPeriodGet(Long tenantId, String eoStepActualId);

    /**
     * 验证执行作业是否存在放行未完成步骤
     *
     * @param tenantId
     * @param eoId
     */
    boolean eoAllStepActualIsBypassedValidate(Long tenantId, String eoId);

    /**
     * 验证是来源步骤是否为目标步骤任意前序
     *
     * @param tenantId
     * @param condition
     * @return
     */
    boolean eoStepIsAnyPreVerify(Long tenantId, MtEoStepActualVO6 condition);

    /**
     * 获取执行作业步骤前道步骤
     *
     * @param tenantId
     * @param eoStepActualId
     * @return
     */
    MtEoStepActualVO7 eoPreviousStepQuery(Long tenantId, String eoStepActualId);

    /**
     * 获取执行作业步骤保留次数
     *
     * @param tenantId
     * @param eoStepActualId
     * @return
     */
    MtEoStepActualVO8 eoStepActualHoldTimesGet(Long tenantId, String eoStepActualId);

    /**
     * 获取执行作业步骤实绩属性
     *
     * @param tenantId
     * @param eoStepActualId
     * @return
     */
    MtEoStepActual eoStepPropertyGet(Long tenantId, String eoStepActualId);


    /**
     * 获取执行作业步骤实绩属性
     *
     * @param tenantId
     * @param eoStepActualIds
     * @return
     */
    List<MtEoStepActual> eoStepPropertyBatchGet(Long tenantId, List<String> eoStepActualIds);

    /**
     * 获取执行作业步骤生产实绩
     *
     * @param tenantId
     * @param eoStepActualId
     * @return
     */
    MtEoStepActualVO5 eoStepProductionResultGet(Long tenantId, String eoStepActualId);

    /**
     * eoStepBypassedFlagUpdate-执行作业步骤放行标识更新
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/6
     */
    MtEoStepActualVO28 eoStepBypassedFlagUpdate(Long tenantId, MtEoStepActualVO9 dto);

    /**
     * eoSubRouterReturnTypeGet-获取执行作业返回步骤类型
     *
     * @param tenantId
     * @param dto
     * @return hmes.eo_step_actual.view.MtEoStepActualVO10
     * @author chuang.yang
     * @date 2019/3/6
     */
    MtEoStepActualVO11 eoSubRouterReturnTypeGet(Long tenantId, MtEoStepActualVO10 dto);

    /**
     * eoStepActualStatusGenerate-判定执行作业步骤状态
     *
     * @param tenantId
     * @param eoStepActualId
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/7
     */
    String eoStepActualStatusGenerate(Long tenantId, String eoStepActualId);

    /**
     * eoStepActualExcessMaxProcessTimesValidate-验证执行作业是否达到最大加工次数
     *
     * @param tenantId
     * @param eoStepActualId
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/11
     */
    String eoStepActualExcessMaxProcessTimesValidate(Long tenantId, String eoStepActualId);

    /**
     * eoLimitStepActualQuery-获取特定执行作业步骤实绩
     *
     * @param tenantId
     * @param eoId
     * @return java.util.List<java.lang.String>
     * @author chuang.yang
     * @date 2019/3/11
     */
    List<String> eoLimitStepActualQuery(Long tenantId, String eoId);

    /**
     * movingEventCreate-移动事件创建
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/11
     */
    String movingEventCreate(Long tenantId, MtEoStepActualVO16 dto);

    /**
     * eoAllStepActualIsCompletedValidate-验证执行作业是否全部完成
     *
     * @param tenantId
     * @param eoId
     * @return hmes.eo_step_actual.view.MtEoStepActualVO17
     * @author chuang.yang
     * @date 2019/3/11
     */
    MtEoStepActualVO17 eoAllStepActualIsCompletedValidate(Long tenantId, String eoId);

    /**
     * eoStepMoveIn-执行作业步骤移入
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/12
     */
    String eoStepMoveIn(Long tenantId, MtEoStepActualVO20 dto);

    /**
     * eoStepMoveInCancel-执行作业步骤移入取消
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/12
     */
    void eoStepMoveInCancel(Long tenantId, MtEoStepActualVO20 dto);

    /**
     * eoStepQueue-执行作业工艺步骤排队
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/12
     */
    String eoStepQueue(Long tenantId, MtEoStepActualVO19 dto);

    /**
     * eoStepQueueCancel-执行作业工艺步骤排队取消
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/12
     */
    void eoStepQueueCancel(Long tenantId, MtEoStepActualVO20 dto);

    /**
     * eoQueueProcess-执行作业步骤排队
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/12
     */
    void eoQueueProcess(Long tenantId, MtEoStepActualVO21 dto);

    /**
     * eoQueueProcessCancel-执行作业步骤排队取消
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/12
     */
    void eoQueueProcessCancel(Long tenantId, MtEoStepActualVO21 dto);

    /**
     * eoStepMoveOut-执行作业步骤移出
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/13
     */
    void eoStepMoveOut(Long tenantId, MtEoStepActualVO22 dto);

    /**
     * eoStepMoveOutCancel-执行作业步骤移出取消
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/13
     */
    void eoStepMoveOutCancel(Long tenantId, MtEoStepActualVO22 dto);

    /**
     * eoNextStepMoveInProcess-执行作业下一步骤移入
     *
     * @param tenantId
     * @param dto
     * @author lxs
     * @date 2019.3.14
     */
    void eoNextStepMoveInProcess(Long tenantId, MtEoRouterActualVO15 dto);

    /**
     * eoNextStepMoveInProcessCancel-执行作业下一步骤移入取消
     *
     * @param tenantId
     * @param dto
     * @author lxs
     * @date 2019.3.18
     */
    void eoNextStepMoveInProcessCancel(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * eoStepActualProcessedTimesGet-获取执行作业步骤加工次数
     *
     * @param tenantId
     * @param eoStepActualId
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/18
     */
    Long eoStepActualProcessedTimesGet(Long tenantId, String eoStepActualId);

    /**
     * propertyLimitEoStepActualQuery-根据属性查询执行作业步骤
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/18
     */
    List<String> propertyLimitEoStepActualQuery(Long tenantId, MtEoStepActual dto);

    /**
     * eoStepCompletedValidate-根据数量验证执行作业步骤是否完成
     *
     * @param tenantId
     * @param eoStepActualId
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/20
     */
    String eoStepCompletedValidate(Long tenantId, String eoStepActualId);

    /**
     * eoStepGroupCompletedValidate-根据数量验证执行作业步骤组是否完成
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/20
     */
    String eoStepGroupCompletedValidate(Long tenantId, MtEoStepActualVO10 dto);

    /**
     * eoStepGroupCompletedBatchValidate-根据数量验证执行作业步骤组是否完成(批量)
     *
     * @Param: [tenantId, dtos]
     * @Return: tarzan.actual.domain.vo.MtEoStepActualVO54
     * @Author: 常卜
     * @Date: 2020/11/4 9:16
     */
    List<MtEoStepActualVO64> eoStepGroupCompletedBatchValidate(Long tenantId,List<MtEoStepActualVO10> dtos);

    /**
     * eoStepCompletedBatchValidate-根据数量批量验证执行作业步骤是否完成
     *
     * @author benjamin
     * @date 2020/10/27 10:19 AM
     * @param tenantId
     * @param eoStepActualIds
     * @return list
     */
    List<MtEoStepActualVO54> eoStepCompletedBatchValidate(Long tenantId, List<String> eoStepActualIds);

    /**
     * eoStepBypassed-执行作业步骤放行
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/20
     */
    void eoStepBypassed(Long tenantId, MtEoStepActualVO24 dto);

    /**
     * eoStepBypassedCancel-执行作业步骤放行取消
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/20
     */
    void eoStepBypassedCancel(Long tenantId, MtEoStepActualVO24 dto);

    /**
     * relaxedFlowEoRouterAllStepCompletedVerify-根据数量验证松散执行作业工艺路线是否完成
     *
     * @param tenantId
     * @param eoRouterActualId
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/20
     */
    String relaxedFlowEoRouterAllStepCompletedVerify(Long tenantId, String eoRouterActualId);

    /**
     * eoOperationAndNcLimitCurrentStepGet-获取执行作业当前所在步骤实绩
     *
     * @param tenantId
     * @param dto
     * @return hmes.eo_step_actual.view.MtEoStepActualVO26
     * @author chuang.yang
     * @date 2019/4/2
     */
    MtEoStepActualVO26 eoOperationAndNcLimitCurrentStepGet(Long tenantId, MtEoStepActualVO25 dto);

    /**
     * eoStepAndWkcQueue-执行作业工艺步骤及工作单元排队
     *
     * @param tenantId
     * @param dto
     * @return hmes.eo_step_actual.view.MtEoStepActualVO28
     * @author guichuan.li
     * @date 2019/4/10
     */
    String eoStepAndWkcQueue(Long tenantId, MtEoStepActualVO19 dto);

    /**
     * eoStepAndWkcQueueCancel-执行作业工艺步骤及工作单元排队取消
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/10
     */
    void eoStepAndWkcQueueCancel(Long tenantId, MtEoStepActualVO20 dto);

    /**
     * operationLimitEoForNonDispatchGet-非调度模式下根据工艺
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/10
     */
    List<String> operationLimitEoForNonDispatchGet(Long tenantId, MtEoStepActualVO27 dto);

    /**
     *
     * propertyLimitEoStepActualPropertyQuery-根据属性获取执行作业步骤实绩信息
     *
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtEoStepActualVO31> propertyLimitEoStepActualPropertyQuery(Long tenantId, MtEoStepActualVO30 vo);

    /**
     * eoWkcAndStepActualBatchSplit-执行作业移动实绩批量拆分
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/11/27
     */
    void eoWkcAndStepActualBatchSplit(Long tenantId, MtEoStepActualVO32 dto);

    /**
     * eoLimitStepActualBatchQuery-批量获取特定执行作业步骤实绩
     *
     * @Author Xie.yiyang
     * @Date 2019/12/17 10:43
     * @param tenantId
     * @param eoIdList
     * @return java.util.List<tarzan.actual.domain.vo.MtEoStepActualVO34>
     */
    List<MtEoStepActualVO34> eoLimitStepActualBatchQuery(Long tenantId, List<String> eoIdList);

    /**
     * eoStepActualProcessedBatchGet-批量获取执行作业步骤实绩生产进度
     *
     * @Author Xie.yiyang
     * @Date 2019/12/17 15:22
     * @param tenantId
     * @param eoStepActualIdList
     * @return java.util.List<tarzan.actual.domain.vo.MtEoStepActualVO35>
     */
    List<MtEoStepActualVO35> eoStepActualProcessedBatchGet(Long tenantId, List<String> eoStepActualIdList);

    /**
     * eoAndStepLimitStepActualBatchQuery-批量获取执行作业指定步骤的步骤实绩
     *
     * @author chuang.yang
     * @date 2019/12/19
     * @param tenantId
     * @param eoMessageList
     * @return java.util.List<tarzan.actual.domain.vo.MtEoStepActualVO36>
     */
    List<MtEoStepActualVO36> eoAndStepLimitStepActualBatchQuery(Long tenantId, List<MtEoStepActualVO37> eoMessageList);

    /**
     * stepActualLimitEoAndRouterBatchGet-根据执行作业步骤实绩批量获取执行作业和工艺路线信息
     *
     * @param tenantId
     * @param eoStepActualIds
     * @return
     */
    List<MtEoStepActualVO1> stepActualLimitEoAndRouterBatchGet(Long tenantId, List<String> eoStepActualIds);

    /**
     * eoRouterLimitStepActualBatchQuery-根据执行作业及工艺路线批量获取步骤实绩信息
     * 
     * @param tenantId
     * @param eoRouterList
     * @return
     */
    List<MtEoStepActualVO38> eoRouterLimitStepActualBatchQuery(Long tenantId, List<MtEoStepActualVO14> eoRouterList);

    /**
     * eoNextStepMoveInBatchProcess-执行作业下一步骤移入(批量)
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2020/10/26
     */
    void eoNextStepMoveInBatchProcess(Long tenantId, MtEoRouterActualVO42 dto);

    /**
     * eoStepBatchMoveIn-执行作业步骤批量移入
     *
     * @author chuang.yang
     * @date 2020/10/26
     * @param tenantId
     * @param dto
     */
    List<MtEoStepActualVO52> eoStepBatchMoveIn(Long tenantId, MtEoStepActualVO45 dto);

    /**
     * eoStepBatchQueue-执行作业工艺步骤排队(批量)
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2020/10/26
     */
    List<MtEoStepActualVO52> eoStepBatchQueue(Long tenantId, MtEoStepActualVO51 dto);

    /**
     * eoStepBatchComplete-执行作业步骤批量完成
     *
     * @author benjamin
     * @date 2020/10/27 3:12 PM
     * @param tenantId
     * @param vo
     */
    void eoStepBatchComplete(Long tenantId, MtEoStepActualVO61 vo);

    /**
     * 执行作业工作单元或库存明细步骤实绩批量更新
     *
     * @param tenantId
     * @param dto
     */
    List<MtEoStepActualVO49> eoWkcOrStepWipBatchUpdate(Long tenantId, MtEoStepActualVO48 dto);

    /**
     * eoStepBatchMoveOut - 执行作业步骤批量移出
     *
     * @author chuang.yang
     * @date 2020/10/26
     * @param tenantId
     * @param dto
     */
    void eoStepBatchMoveOut(Long tenantId, MtEoStepActualVO53 dto);

    /**
     * eoQueueBatchProcess-执行作业步骤排队(批量)
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2020/10/26
     */
    void eoQueueBatchProcess(Long tenantId, MtEoStepActualVO50 dto);

    /**
     * eoStepQueueBatchCancel-执行作业步骤排队取消(批量)
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2020/10/26
     */
    void eoStepQueueBatchCancel(Long tenantId, MtEoStepActualVO56 dto);

    /**
     * eoStepActualExcessMaxProcessTimesBatchValidate-批量验证执行作业是否达到最大加工次数
     *
     * @param tenantId
     * @param eoStepActualIds
     * @return
     */
    List<MtEoStepActualVO60> eoStepActualExcessMaxProcessTimesBatchValidate(Long tenantId,
                    List<String> eoStepActualIds);

    /**
     * eoStepProductionActualBatchUpdate-执行作业步骤生产实绩批量更新
     *
     * @author chuang.yang
     * @date 2020/10/23
     * @param tenantId
     * @param dto
     */
    List<MtEoStepActualVO46> eoStepProductionActualBatchUpdate(Long tenantId, MtEoStepActualVO40 dto);
}
