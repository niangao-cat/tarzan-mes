package tarzan.actual.domain.repository;

import java.util.List;
import java.util.Map;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtEoRouterActual;
import tarzan.actual.domain.vo.*;

/**
 * EO工艺路线实绩资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoRouterActualRepository
                extends BaseRepository<MtEoRouterActual>, AopProxy<MtEoRouterActualRepository> {
    /**
     * eoOperationLimitCurrentRouterStepGet-获取当前操作步骤
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtEoRouterActualVO1 eoOperationLimitCurrentRouterStepGet(Long tenantId, MtEoRouterActualVO2 dto);

    /**
     * eoWkcLimitCurrentStepWkcActualGet-获取工作单元实绩行
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtEoRouterActualVO4 eoWkcLimitCurrentStepWkcActualGet(Long tenantId, MtEoRouterActualVO3 dto);

    /**
     * eoRouterProductionResultAndHisUpdate-执行作业工艺路线生产实绩更新
     *
     * @param tenantId
     * @param dto
     */
    MtEoRouterActualVO28 eoRouterProductionResultAndHisUpdate(Long tenantId, MtEoRouterActualVO6 dto);

    /**
     * eoRouterActualAndHisCreate-执行作业工艺路线实绩生成
     *
     * update remarks
     * <ul>
     * <li>2019-09-18 benjamin 修改返回参数</li>
     * </ul>
     *
     * @param tenantId 租户Id
     * @param dto MtEoRouterActualVO7
     * @return MtEoRouterActualVO28
     */
    MtEoRouterActualVO28 eoRouterActualAndHisCreate(Long tenantId, MtEoRouterActualVO7 dto);

    /**
     * 获取执行作业工艺路线实绩属性
     *
     * @param tenantId
     * @param eoRouterActualId
     * @return
     */
    MtEoRouterActual eoRouterPropertyGet(Long tenantId, String eoRouterActualId);

    /**
     * 获取执行作业工艺路线生产实绩
     *
     * @param tenantId
     * @param eoRouterActualId
     * @return
     */
    MtEoRouterActualVO8 eoRouterProductionResultGet(Long tenantId, String eoRouterActualId);

    /**
     * 批量获取执行作业工艺路线生产实绩
     *
     * @param tenantId
     * @param eoRouterActualIdList
     * @return
     */
    Map<String,MtEoRouterActualVO8> eoRouterProductionResultBatchGet(Long tenantId, List<String> eoRouterActualIdList);

    /**
     * eoStepTypeValidate-验证执行作业步骤类型
     *
     * @author chuang.yang
     * @date 2019/3/7
     * @param tenantId
     * @param dto
     * @return hmes.eo_router_actual.view.MtEoRouterActualVO10
     */
    MtEoRouterActualVO10 eoStepTypeValidate(Long tenantId, MtEoRouterActualVO9 dto);

    /**
     * eoStepTypeBatchValidate-批量验证执行作业步骤类型
     *
     * @author benjamin
     * @date 2020/11/3 9:33 AM
     * @param tenantId 租户Id
     * @param dtoList 参数列表
     * @return List
     */
    List<MtEoRouterActualVO49> eoStepTypeBatchValidate(Long tenantId, List<MtEoRouterActualVO9> dtoList);

    /**
     * eoReturnStepGet-获取执行作业返回步骤
     *
     * @author chuang.yang
     * @date 2019/3/7
     * @param tenantId
     * @param dto
     * @return hmes.eo_router_actual.view.MtEoRouterActualVO11
     */
    MtEoRouterActualVO11 eoReturnStepGet(Long tenantId, MtEoRouterActualVO9 dto);

    /**
     * eoRouterActualStatusGenerate-判定执行作业工艺路线状态
     *
     * @author chuang.yang
     * @date 2019/3/7
     * @param tenantId
     * @param eoRouterActualId
     * @return java.lang.String
     */
    String eoRouterActualStatusGenerate(Long tenantId, String eoRouterActualId);

    /**
     * eoRouterIsSubRouterValidate-验证是否子工艺路线
     *
     * @author chuang.yang
     * @date 2019/3/11
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    String eoRouterIsSubRouterValidate(Long tenantId, MtEoRouterActualVO12 dto);

    /**
     * eoRouterMoveIn-执行作业工艺路线移入
     *
     * @author chuang.yang
     * @date 2019/3/12
     * @param tenantId
     * @param dto
     * @return hmes.eo_router_actual.view.MtEoRouterActualVO14
     */
    MtEoRouterActualVO14 eoRouterMoveIn(Long tenantId, MtEoRouterActualVO13 dto);

    MtEoRouterActualVO14 eoRouterMoveInRecursion(Long tenantId, MtEoRouterActualVO13 dto, String subRouterFlag,
                    String eventId);

    /**
     * eoRouterMoveInCancel-执行作业工艺路线移入取消
     *
     * @author chuang.yang
     * @date 2019/3/12
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoRouterMoveInCancel(Long tenantId, MtEoRouterActualVO16 dto);

    void eoRouterMoveInCancelRecursion(Long tenantId, String eoId, String eoRouterActualId, Double qty, String eventId,
                    String eoStepActualId);

    /**
     * queueProcess-订单排队
     *
     * @author chuang.yang
     * @date 2019/3/12
     * @param tenantId
     * @param dto
     * @return void
     */
    void queueProcess(Long tenantId, MtEoRouterActualVO15 dto);

    /**
     * queueProcessCancel-订单排队取消
     *
     * @author chuang.yang
     * @date 2019/3/12
     * @param tenantId
     * @param dto
     * @return void
     */
    void queueProcessCancel(Long tenantId, MtEoRouterActualVO16 dto);

    /**
     * eoWkcAndStepActualCopy-执行作业移动实绩复制
     *
     * @author chuang.yang
     * @date 2019/3/21
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    String eoWkcAndStepActualCopy(Long tenantId, MtEoRouterActualVO25 dto);

    /**
     * eoWkcAndStepActualBatchCopy-执行作业移动实绩批量复制
     *
     * @author chuang.yang
     * @date 2019/3/13
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    List<String> eoWkcAndStepActualBatchCopy(Long tenantId, MtEoRouterActualVO17 dto);

    /**
     * eoLimitWkcAndStepActualBatchCopy-执行作业移动实绩批量复制
     *
     * @Author peng.yuan
     * @Date 2019/11/25 14:14
     * @param tenantId :
     * @param dto :
     * @return void
     */
    void eoLimitWkcAndStepActualBatchCopy(Long tenantId, MtEoRouterActualVO33 dto);

    /**
     * eoWkcAndStepActualSplitVerify-验证执行作业是否允许拆分实绩
     *
     * @author chuang.yang
     * @date 2019/3/13
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoWkcAndStepActualSplitVerify(Long tenantId, MtEoRouterActualVO18 dto);

    /**
     * eoWkcAndStepActualMergeVerify-验证执行作业是否允许合并实绩
     *
     * @author chuang.yang
     * @date 2019/3/13
     * @param tenantId
     * @param eoId
     * @return void
     */
    void eoWkcAndStepActualMergeVerify(Long tenantId, String eoId);

    /**
     * propertyLimitEoRouterActualQuery-根据属性查询执行作业步骤
     *
     * @author chuang.yang
     * @date 2019/3/18
     * @param tenantId
     * @param dto
     * @return java.util.List<java.lang.String>
     */
    List<String> propertyLimitEoRouterActualQuery(Long tenantId, MtEoRouterActualVO20 dto);

    /**
     * propertyLimitEoRouterActualBatchQuery-根据属性查询执行作业步骤
     *
     * @author penglin.sui
     * @date 2021/12/3
     * @param tenantId
     * @param dtoList
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    Map<String,List<String>> propertyLimitEoRouterActualBatchQuery(Long tenantId, List<MtEoRouterActualVO20> dtoList);

    /**
     * eoWkcAndStepActualSplit-执行作业移动实绩拆分
     *
     * @author chuang.yang
     * @date 2019/3/19
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoWkcAndStepActualSplit(Long tenantId, MtEoRouterActualVO21 dto);

    /**
     * eoWkcAndStepActualMerge-执行作业移动实绩合并
     *
     * @author chuang.yang
     * @date 2019/3/19
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoWkcAndStepActualMerge(Long tenantId, MtEoRouterActualVO22 dto);

    /**
     * eoWkcAndStepScrapped-执行作业工作单元步骤报废
     *
     * @author chuang.yang
     * @date 2019/3/19
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoWkcAndStepScrapped(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * eoWkcAndStepScrappedCancel-执行作业步骤工作单元报废取消
     *
     * @author chuang.yang
     * @date 2019/3/19
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoWkcAndStepScrappedCancel(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * scrappedProcess-订单报废
     *
     * update remarks - 2019/7/16 benjamin 调用API(eoScrappedConfirm)添加EoStepActualId字段
     *
     * @author chuang.yang
     * @date 2019/3/19
     * @param tenantId
     * @param dto
     */
    void scrappedProcess(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * scrappedProcessCancel-订单报废取消
     *
     * @author chuang.yang
     * @date 2019/3/19
     * @param tenantId
     * @param dto
     * @return void
     */
    void scrappedProcessCancel(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * coproductEoActualCreate-联产品移动实绩创建
     *
     * @author chuang.yang
     * @date 2019/3/20
     * @param tenantId
     * @param dto
     * @return void
     */
    void coproductEoActualCreate(Long tenantId, MtEoRouterActualVO21 dto);

    /**
     * eoWkcAndStepWorking-执行作业步骤运行
     *
     * @author chuang.yang
     * @date 2019/3/20
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoWkcAndStepWorking(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * eoWkcAndStepMoving-执行作业步骤移动
     *
     * @author benjamin
     * @date 2019-07-29 16:07
     * @param tenantId IRequest
     * @param moveVO MtEoRouterActualVO27
     */
    void eoWkcAndStepMoving(Long tenantId, MtEoRouterActualVO27 moveVO);

    /**
     * eoWkcAndStepWorkingCancel-执行作业步骤运行取消
     *
     * @author chuang.yang
     * @date 2019/3/20
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoWkcAndStepWorkingCancel(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * eoWorkingProcess-执行作业步骤运行
     *
     * @author chuang.yang
     * @date 2019/3/20
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoWorkingProcess(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * eoWorkingProcessCancel-执行作业步骤运行取消
     *
     * @author chuang.yang
     * @date 2019/3/20
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoWorkingProcessCancel(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * eoWkcAndStepCompletePending-执行作业工作单元步骤完成暂存
     *
     * @author chuang.yang
     * @date 2019/3/20
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoWkcAndStepCompletePending(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * eoWkcAndStepCompletePendingCancel-执行作业步骤工作单元完成暂存取消
     *
     * @author chuang.yang
     * @date 2019/3/20
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoWkcAndStepCompletePendingCancel(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * eoWkcAndStepComplete-执行作业步骤完成
     *
     * @author chuang.yang
     * @date 2019/3/20
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoWkcAndStepComplete(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * eoWkcAndStepCompleteCancel-执行作业步骤完成取消
     *
     * @author chuang.yang
     * @date 2019/3/20
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoWkcAndStepCompleteCancel(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * eoRouterCompletedValidate-验证执行作业工艺路线是否完成
     *
     * @author chuang.yang
     * @date 2019/3/21
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    String eoRouterCompletedValidate(Long tenantId, MtEoRouterActualVO14 dto);

    /**
     * eoRouterComplete-执行作业工艺路线完成
     *
     * @author chuang.yang
     * @date 2019/3/21
     * @param tenantId
     * @param dto
     * @return hmes.eo_router_actual.view.MtEoRouterActualVO24
     */
    MtEoRouterActualVO24 eoRouterComplete(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * 递归处理分支工艺路线 -> 分支工艺路线继续向上找
     *
     * @author chuang.yang
     * @date 2019/3/21
     */
    MtEoRouterActualVO24 eoRouterCompleteRecursion(Long tenantId, MtEoRouterActualVO19 dto,
                    MtEoStepActualVO1 mtEoStepActualVO1, boolean isNeedVerify);

    /**
     * eoRouterCompleteCancel-执行作业工艺路线完成取消
     *
     * @author chuang.yang
     * @date 2019/3/21
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    MtEoRouterActualVO26 eoRouterCompleteCancel(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * 递归处理分支工艺路线 -> 分支工艺路线继续向上找
     *
     * @author chuang.yang
     * @date 2019/3/21
     */
    MtEoRouterActualVO26 eoRouterCompleteCancelRecursion(Long tenantId, MtEoRouterActualVO19 dto,
                    MtEoStepActualVO1 mtEoStepActualVO1, boolean isNeedVerify);

    /**
     * completeProcess-订单完成
     *
     * @author chuang.yang
     * @date 2019/3/21
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    MtEoRouterActualVO32 completeProcess(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * completeProcessCancel-订单完成取消
     *
     * @author chuang.yang
     * @date 2019/3/21
     * @param tenantId
     * @param dto
     * @return void
     */
    void completeProcessCancel(Long tenantId, MtEoRouterActualVO19 dto);

    /**
     * propertyLimitEoRouterActualPropertyQuery-根据属性获取EO工艺路线实绩信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoRouterActualVO30> propertyLimitEoRouterActualPropertyQuery(Long tenantId, MtEoRouterActualVO29 dto);

    /**
     * 验证执行作业步骤类型
     *
     * @Author peng.yuan
     * @Date 2019/10/21 10:21
     * @param tenantId :
     * @param routerStepId :
     * @return tarzan.actual.domain.vo.MtEoRouterActualVO31
     */
    MtEoRouterActualVO31 routerStepTypeGet(Long tenantId, String routerStepId);

    /**
     * eoRouterPropertyBatchGet-批量获取执行作业工艺路线实绩属性
     *
     * @param tenantId
     * @param eoRouterActualIds
     * @author guichuan.li
     * @date 2020/2/12
     */
    List<MtEoRouterActual> eoRouterPropertyBatchGet(Long tenantId, List<String> eoRouterActualIds);

    /**
     * eoCompleteProcess-执行作业订单完成-暂时仅供工序作业平台交付功能按钮
     *
     * @author: chuang.yang
     * @date: 2020/02/12 18:31
     * @param tenantId :
     * @param dto :
     * @return tarzan.actual.domain.vo.MtEoRouterActualVO32
     */
    MtEoRouterActualVO32 eoCompleteProcess(Long tenantId, MtEoRouterActualVO36 dto);

    /**
     * eoRouterProductionActualBatchUpdate-执行作业工艺路线生产实绩批量更新
     *
     * @author chuang.yang
     * @date 2020/10/23
     * @param tenantId
     * @param dto
     */
    List<MtEoRouterActualVO43> eoRouterProductionActualBatchUpdate(Long tenantId, MtEoRouterActualVO38 dto);

    /**
     * eoWkcAndStepBatchWorking-执行作业步骤批量运行
     *
     * @param tenantId
     * @param dto
     */
    void eoWkcAndStepBatchWorking(Long tenantId, MtEoRouterActualVO52 dto);

    /**
     * queueBatchProcess-订单排队(批量)
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2020/10/26
     */
    void queueBatchProcess(Long tenantId, MtEoRouterActualVO41 dto);

    /**
     * completeBatchProcess-订单批量完成
     *
     * @author chuang.yang
     * @date 2020/10/27
     * @param tenantId
     * @param dto
     */
    List<MtEoRouterActualVO55> completeBatchProcess(Long tenantId, MtEoRouterActualVO39 dto);

}
