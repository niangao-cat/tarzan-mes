package tarzan.order.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import io.tarzan.common.domain.vo.MtNumrangeVO8;
import tarzan.method.domain.vo.MtBomComponentVO18;
import tarzan.method.domain.vo.MtBomComponentVO19;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.vo.*;

/**
 * 生产指令资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:34:08
 */
public interface MtWorkOrderRepository extends BaseRepository<MtWorkOrder>, AopProxy<MtWorkOrderRepository> {

    /**
     * woPropertyGet-根据生产指令ID获取生产指令信息
     *
     * @param tenantId
     * @param workOrderId
     * @return
     */
    MtWorkOrder woPropertyGet(Long tenantId, String workOrderId);

    /**
     * woPropertyBatchGet-根据生产指令ID批量获取生产指令信息
     *
     * @param tenantId
     * @param workOrderIds
     * @return
     */
    List<MtWorkOrder> woPropertyBatchGet(Long tenantId, List<String> workOrderIds);

    /**
     * propertyLimitWoQuery-获取满足指定属性限制的生产指令
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> propertyLimitWoQuery(Long tenantId, MtWorkOrderVO21 dto);

    /**
     * numberLimitWoGet-根据生产指令编码获取生产指令ID
     *
     * @param tenantId
     * @param workOrderNum
     * @return
     */
    String numberLimitWoGet(Long tenantId, String workOrderNum);

    /**
     * bomRouterLimitWoQuery -获取使用指定装配清单&工艺路线限定的所有的生产指令
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> bomRouterLimitWoQuery(Long tenantId, MtWorkOrderVO17 dto);

    /**
     * planTimeLimitWoQuery-获取指定时间内计划生产的生产指令
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> planTimeLimitWoQuery(Long tenantId, MtWorkOrderVO20 dto);

    /**
     * woDefaultLocationGet-获取生产指令的默认完工位置
     *
     * @param tenantId
     * @param workOrderId
     * @return
     */
    String woDefaultLocationGet(Long tenantId, String workOrderId);

    /**
     * woCompleteControlGet-获取生产指令完工控制
     *
     * @param tenantId
     * @param workOrderId
     * @return
     */
    MtWorkOrderVO19 woCompleteControlGet(Long tenantId, String workOrderId);

    /**
     * woMaterialValidate-验证生产指令物料是否满足使用条件
     *
     * @param tenantId
     * @param workOrderId
     * @return
     */
    void woMaterialValidate(Long tenantId, String workOrderId);

    /**
     * woBomValidate-验证生产指令装配清单是否满足使用条件
     *
     * @param tenantId
     * @param workOrderId
     * @return
     */
    void woBomValidate(Long tenantId, String workOrderId);

    /**
     * woRouterValidate-验证生产指令工艺路线是否满足使用条件
     *
     * @param tenantId
     * @param workOrderId
     * @return
     */
    void woRouterValidate(Long tenantId, String workOrderId);

    /**
     * woRouterBomMatchValidate-验证验证生产指令工艺路线与装配清单是否一致
     *
     * @param tenantId
     * @param workOrderId
     * @return
     */
    void woRouterBomMatchValidate(Long tenantId, String workOrderId);

    /**
     * woStatusUpdate-更新生产指令状态
     *
     * update remarks
     * <ul>
     * <li>2019-09-18 benjamin 添加返回参数</li>
     * </ul>
     *
     * @param tenantId 租户Id
     * @param dto MtWorkOrderVO25
     * @return MtWorkOrderVO28
     */
    MtWorkOrderVO28 woStatusUpdate(Long tenantId, MtWorkOrderVO25 dto);

    /**
     * woReleaseVerify-生产指令下达验证
     *
     * @param tenantId
     * @param workOrderId
     */
    void woReleaseVerify(Long tenantId, String workOrderId);

    /**
     * woRelease-生产指令下达
     *
     * @param tenantId
     * @param dto
     */
    void woRelease(Long tenantId, MtWorkOrderVO23 dto);

    /**
     * woStatusVerify-验证生产指令状态是否在指定状态列表中
     *
     * @param tenantId
     * @param workOrderId
     * @param demandStatusList
     */
    boolean woStatusVerify(Long tenantId, String workOrderId, List<String> demandStatusList);

    /**
     * woValidateVerifyUpdate-生产指令验证标识验证更新
     *
     * update remarks
     * <ul>
     * <li>2019-09-18 benjamin 添加返回参数</li>
     * </ul>
     *
     * @param tenantId 租户Id
     * @param workOrderId 生产指令Id
     * @param eventId 事件Id
     * @return MtWorkOrderVO28
     */
    MtWorkOrderVO28 woValidateVerifyUpdate(Long tenantId, String workOrderId, String eventId);

    /**
     * woBomComponentGet-生产指令组件获取
     *
     * @param tenantId
     * @param dto
     */
    List<MtBomComponentVO19> attritionLimitWoComponentQtyQuery(Long tenantId, MtBomComponentVO18 dto);

    /**
     * woBomUpdate-更新生产指令装配清单
     *
     * @param tenantId
     * @param workOrderId
     * @param bomId
     * @param eventId
     */
    String woBomUpdate(Long tenantId, String workOrderId, String bomId, String eventId);

    /**
     * woRouterUpdate-更新生产指令工艺路线
     *
     * update remarks
     * <ul>
     * <li>2019-09-18 benjamin 添加返回参数</li>
     * </ul>
     *
     * @param tenantId 租户Id
     * @param workOrderId 生产指令Id
     * @param routerId 工艺路线Id
     * @param eventId 事件Id
     * @return MtWorkOrderVO28
     */
    MtWorkOrderVO28 woRouterUpdate(Long tenantId, String workOrderId, String routerId, String eventId);

    /**
     * woQtyUpdate-更新生产指令数量
     *
     * update remarks
     * <ul>
     * <li>2019-09-18 benjamin 添加返回参数</li>
     * </ul>
     *
     * @param tenantId 租户Id
     * @param dto MtWorkOrderVO22
     * @return MtWorkOrderVO28
     */
    MtWorkOrderVO28 woQtyUpdate(Long tenantId, MtWorkOrderVO22 dto);

    /**
     * woUpdate-更新生产指令属性
     * 
     * update remarks
     * <ul>
     * <li>2019-09-18 benjamin 添加sourceIdentificationId处理逻辑</li>
     * </ul>
     * 
     * @param tenantId 租户Id
     * @param dto MtWorkOrderVO
     * @param fullUpdate 全量更新标识
     * @return MtWorkOrderVO28
     */
    MtWorkOrderVO28 woUpdate(Long tenantId, MtWorkOrderVO dto, String fullUpdate);

    /**
     * 批量更新生产指令属性
     * 
     * @Author peng.yuan
     * @Date 2019/11/27 10:42
     * @param tenantId :
     * @param dto :
     * @param fullUpdate :
     * @return <tarzan.order.domain.vo.MtWorkOrderVO28>
     */
    List<MtWorkOrderVO28> woBatchUpdate(Long tenantId, MtWorkOrderVO37 dto, String fullUpdate);

    /**
     * woSort-根据指定属性对一批生产指令进行排序
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<String> woSort(Long tenantId, MtWorkOrderVO1 condition);

    /**
     * woStatusUpdateVerify-生产指令状态切换校验
     *
     * @param tenantId
     * @param workOrderId
     * @param targetStatus
     */
    void woStatusUpdateVerify(Long tenantId, String workOrderId, String targetStatus);

    /**
     * woHoldVerify-生产指令保留验证
     *
     * @param tenantId
     * @param workOrderId
     */
    void woHoldVerify(Long tenantId, String workOrderId);

    /**
     * woCloseVerify-生产指令关闭验证
     *
     * @param tenantId
     * @param workOrderId
     */
    void woCloseVerify(Long tenantId, String workOrderId);

    /**
     * woStatusComplete-生产指令完成
     *
     * @param tenantId
     * @param vo
     */
    void woStatusComplete(Long tenantId, MtWorkOrderVO2 vo);

    /**
     * woClose-生产指令关闭
     *
     * @param tenantId
     * @param vo
     */
    void woClose(Long tenantId, MtWorkOrderVO2 vo);

    /**
     * woCloseCancel-生产指令关闭取消
     *
     * @param tenantId
     * @param vo
     */
    void woCloseCancel(Long tenantId, MtWorkOrderVO2 vo);

    /**
     * woHold-生产指令保留
     *
     * @param tenantId
     * @param vo
     */
    void woHold(Long tenantId, MtWorkOrderVO2 vo);

    /**
     * woHoldCancel-生产指令保留取消
     *
     * @param tenantId
     * @param dto
     */
    void woHoldCancel(Long tenantId, MtWorkOrderVO15 dto);

    /**
     * woPreProductValidate-检查生产指令指示生产信息是否达到生产要求
     *
     * @param tenantId
     * @param workOrderId
     */
    void woPreProductValidate(Long tenantId, String workOrderId);

    /**
     * statusLimitWoQtyUpdateVerify-验证需变更数量的生产指令状态是否满足要求
     *
     * @param tenantId
     * @param workOrderId
     */
    void statusLimitWoQtyUpdateVerify(Long tenantId, String workOrderId);

    /**
     * woCompleteControlLimitEoCreateVerify-验证生产指令创建的执行作业是否满足生产指令完工控制要求
     *
     * @param tenantId
     * @param vo
     */
    void woCompleteControlLimitEoCreateVerify(Long tenantId, MtWorkOrderVO3 vo);

    /**
     * woStatusCompleteVerify-生产指令状态完成验证
     *
     * @param tenantId
     * @param workOrderId
     */
    void woStatusCompleteVerify(Long tenantId, String workOrderId);

    /**
     * woCompleteVerify-生产指令完成验证
     * 
     * update remarks
     * <ul>
     * <li>2019-09-23 benjamin 修改传入参数</li>
     * </ul>
     * 
     * @param tenantId 租户Id
     * @param vo MtWorkOrderVO29
     */
    void woCompleteVerify(Long tenantId, MtWorkOrderVO29 vo);

    /**
     * woLimitEoCreateVerify-生产指令创建执行作业验证
     *
     * @param tenantId
     * @param vo
     */
    void woLimitEoCreateVerify(Long tenantId, MtWorkOrderVO3 vo);

    /**
     * woCompleteCancel-生产指令完成撤销
     *
     * @param tenantId
     * @param dto
     */
    void woCompleteCancel(Long tenantId, MtWorkOrderVO4 dto);

    /**
     * woComplete-生产指令完成
     *
     * @param tenantId
     * @param dto
     */
    void woComplete(Long tenantId, MtWorkOrderVO4 dto);

    /**
     * woAndEoClose-生产指令关闭同时关闭执行作业
     *
     * @param tenantId
     * @param dto
     */
    void woAndEoClose(Long tenantId, MtWorkOrderVO5 dto);

    /**
     * woAndEoCloseCancel-生产指令关闭取消同时关闭取消执行作业
     *
     * @param tenantId
     * @param dto
     */
    void woAndEoCloseCancel(Long tenantId, MtWorkOrderVO5 dto);

    /**
     * releaseWoLimitWoQtyUpdateVerify-根据已下达EO数量验证生产指令数量变更合规性
     *
     * @param tenantId
     * @param dto
     */
    void releaseWoLimitWoQtyUpdateVerify(Long tenantId, MtWorkOrderVO6 dto);

    /**
     * woAndEoQtyUpdate-生产指令数量变更同时变更执行作业数量
     *
     * @param tenantId
     * @param dto
     */
    void woAndEoQtyUpdate(Long tenantId, MtWorkOrderVO22 dto);

    /**
     * 检查引用指定装配清单或者工艺路线的生产指令是否唯一
     *
     * @param tenantId
     * @param dto
     * @return
     */
    void bomRouterLimitUniqueWoValidate(Long tenantId, MtWorkOrderVO17 dto);

    /**
     * 根据上层生产指令数量更新指定类型的下层生产指令及生产指令对应执行作业的数量
     *
     * @param tenantId
     * @param dto
     */
    void woRelLimitSubWoAndEoQtyUpdate(Long tenantId, MtWorkOrderVO24 dto);

    /**
     * bomLimitWoCreate-根据生产指令装配清单创建组件生产指令
     *
     * @param tenantId
     * @param workOrderId
     * @return
     */
    List<String> bomLimitWoCreate(Long tenantId, String workOrderId);

    /**
     * woComponentQtyQuery-查询生产指令指定工艺或步骤的组件和需求用量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWorkOrderVO8> woComponentQtyQuery(Long tenantId, MtWorkOrderVO7 dto);


    /**
     * 根据生产指令和物料获取生产指令组件行
     * 
     * @param tenantId
     * @param condition
     * @return
     */
    List<String> woMaterialLimitComponentQuery(Long tenantId, MtWorkOrderVO27 condition);

    /**
     * 获取生产指令是否按照工序装配清单进行装配
     * 
     * @param tenantId
     * @param workOrderId
     * @return
     */
    String woOperationAssembleFlagGet(Long tenantId, String workOrderId);

    /**
     * 获取新的生产指令编码/sen.luo 2018-03-18 add param mtWorkOrderVO33 by pengyuan 2019-11-21
     * 
     * @param tenantId
     * @return
     */
    MtNumrangeVO5 woNextNumberGet(Long tenantId, MtWorkOrderVO33 mtWorkOrderVO33);

    /**
     * wo批量获取生产指令编码
     * 
     * @Author peng.yuan
     * @Date 2019/11/22 11:32
     * @param tenantId :
     * @param mtWorkOrderVO35 :
     * @return io.tarzan.common.domain.vo.MtNumrangeVO8
     */
    MtNumrangeVO8 woBatchNumberGet(Long tenantId, MtWorkOrderVO35 mtWorkOrderVO35);

    /**
     * 生产指令合并/sen.luo 2018-03-19
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    String woMerge(Long tenantId, MtWorkOrderVO9 dto);

    /**
     * 生产指令合并验证/sen.luo 2018-03-19
     * 
     * @param tenantId
     * @param dto
     */
    void woMergeVerify(Long tenantId, MtWorkOrderVO10 dto);

    /**
     * 生产指令拆分验证/sen.luo 2018-03-19
     * 
     * @param tenantId
     * @param dto
     */
    void woSplitVerify(Long tenantId, MtWorkOrderVO11 dto);

    /**
     * 验证生产指令是否允许变更工艺路线/sen.luo 2018-03-19
     * 
     * @param tenantId
     */
    void woRouterUpdateVerify(Long tenantId, MtWorkOrderVO12 dto);

    /**
     * 生产指令拆分/sen.luo 2018-03-19
     * 
     * @param tenantId
     * @param dto
     */
    String woSplit(Long tenantId, MtWorkOrderVO13 dto);

    /**
     * 验证生产指令是否允许变更装配清单/sen.luo 2018-03-20
     * 
     * @param tenantId
     * @param workOrderId
     * @param bomId
     */
    void wobomUpdateValidate(Long tenantId, String workOrderId, String bomId);

    /**
     * 生产指令装配清单变更同时更新执行作业装配清单/sen.luo 2018-03-20
     * 
     * @param tenantId
     * @param dto
     */
    void woAndEoBomUpdate(Long tenantId, MtWorkOrderVO14 dto);

    /**
     * woAbandon-生产指令作废
     *
     * @author chuang.yang
     * @date 2019/6/4
     * @param tenantId
     * @param dto
     * @return void
     */
    void woAbandon(Long tenantId, MtWorkOrderVO5 dto);

    /**
     * woAndEoHold-生产指令保留同时保留执行作业
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/6/17
     */
    void woAndEoHold(Long tenantId, MtWorkOrderVO2 dto);

    /**
     * woAndEoHoldCancel-根据生产指令对执行作业进行保留取消
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/6/17
     */
    void woAndEoHoldCancel(Long tenantId, MtWorkOrderVO15 dto);

    /**
     * woStatusCompleteCancel-生产指令状态完成取消
     *
     * @author chuang.yang
     * @date 2019/7/2
     * @param tenantId
     * @param dto
     * @return void
     */
    void woStatusCompleteCancel(Long tenantId, MtWorkOrderVO5 dto);

    /**
     * materialLimitWoUpdate-根据物料PFEP更新生产指令信息
     *
     * @author chuang.yang
     * @date 2019/7/2
     * @param tenantId
     * @param dto
     * @return void
     */
    void materialLimitWoUpdate(Long tenantId, MtWorkOrderEventVO dto);

    /**
     * woLimitAttrQuery -获取生产指令拓展属性
     *
     * @Author lxs
     * @Date 2019/4/16
     * @Params [tenantId, dto]
     * @Return java.util.List<hmes.work_order.view.MtWorkOrderVO16>
     */
    List<MtExtendAttrVO> woLimitAttrQuery(Long tenantId, MtWorkOrderAttrVO2 dto);

    /**
     * attrLimitWoQuery -根据拓展属性获取生产指令
     *
     * @Author lxs
     * @Date 2019/4/16
     * @Params [tenantId, dto]
     * @Return java.util.List<java.lang.String>
     */
    List<String> attrLimitWoQuery(Long tenantId, MtWorkOrderAttrVO1 dto);

    /**
     * woLimitAttrUpdate-新增&更新生产指令拓展属性同时记录生产指令拓展属性历史
     * 
     * @Author lxs
     * @Date 2019/4/17
     * @Params [tenantId, dto]
     * @Return void
     */
    void woLimitAttrUpdate(Long tenantId, MtWorkOrderAttrVO3 dto);

    /**
     * woAttrHisQuery -获取生产指令拓展属性变更历史
     *
     * @Author lxs
     * @Date 2019/4/16
     * @Params [tenantId, dto]
     * @Return java.util.List<hmes.work_order.view.MtWorkOrderAttrHisVO2>
     */
    List<MtWorkOrderAttrHisVO2> woAttrHisQuery(Long tenantId, MtWorkOrderAttrHisVO dto);

    /**
     * eventLimitWoAttrHisBatchQuery-根据事件批量获取生产指令拓展属性变更历史
     *
     * @Author lxs
     * @Date 2019/4/16
     * @Params [tenantId, eventIds]
     * @Return java.util.List<hmes.work_order.view.MtWorkOrderAttrHisVO2>
     */
    List<MtWorkOrderAttrHisVO2> eventLimitWoAttrHisBatchQuery(Long tenantId, List<String> eventIds);

    /**
     * propertyLimitWoPropertyQuery-根据属性获取生产指令信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWorkOrderVO31> propertyLimitWoPropertyQuery(Long tenantId, MtWorkOrderVO30 dto);

    /**
     * woPriorityLimitNextWoQuery-根据生产指令优先级获取下一生产指令信息
     *
     * @param tenantId
     * @param workOrderId
     * @return
     */
    List<String> woPriorityLimitNextWoQuery(Long tenantId, String workOrderId);

    /**
     * woAttrPropertyUpdate-生产指令新增&更新扩展表属性
     *
     * @param tenantId
     * @param vo
     * @return
     */
    void woAttrPropertyUpdate(Long tenantId, MtWorkOrderVO32 vo);

    /**
     * 根据workOrderNum 批量获取WO
     */
    List<MtWorkOrder> woLimitWorkNUmQuery(Long tenantId, List<String> workOrderNum);

    /**
     * woKittingQtyCalculate-计算生产指令齐套数量
     *
     * @Author Xie.yiyang
     * @Date 2019/12/19 17:04
     * @param tenantId
     * @param vo
     * @return java.lang.Long
     */
    Double woKittingQtyCalculate(Long tenantId, MtWorkOrderVO45 vo);


    List<MtWorkOrder> selectByWipEntityId(Long tenantId, List<String> wipEntityIds);

    /**
     * woMaterialLimitComponentBatchQuery-根据生产指令和物料获取生产指令组件行(批量)
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtWorkOrderVO68> woMaterialLimitComponentBatchQuery(Long tenantId, List<MtWorkOrderVO27> condition);

    /**
     * woBatchComplete-生产指令批量完成
     *
     * @author chuang.yang
     * @date 2020/10/30
     * @param tenantId
     * @param dto
     */
    void woBatchComplete(Long tenantId, MtWorkOrderVO65 dto);

    /**
     * woBasicBatchUpdate-wo信息基础数据批量更新
     *
     * @author chuang.yang
     * @date 2020/10/30
     */
    List<MtWorkOrderVO28> woBasicBatchUpdate(Long tenantId, MtWorkOrderVO67 dto);
}
