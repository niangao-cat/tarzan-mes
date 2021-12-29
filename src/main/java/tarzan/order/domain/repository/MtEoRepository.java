package tarzan.order.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import io.tarzan.common.domain.vo.MtNumrangeVO8;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.vo.*;

/**
 * 执行作业【执行作业需求和实绩拆分开】资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
public interface MtEoRepository extends BaseRepository<MtEo>, AopProxy<MtEoRepository> {
    /**
     * eoUpdate-更新执行作业属性
     *
     * @param tenantId
     * @param vo
     */
    MtEoVO29 eoUpdate(Long tenantId, MtEoVO vo, String fullUpdate);

    /**
     * 根据执行作业ID获取执行作业信息
     *
     * @param eoId
     * @return
     */
    MtEo eoPropertyGet(Long tenantId, String eoId);

    /**
     * 批量根据执行作业ID获取执行作业信息
     *
     * @param tenantId
     * @param eoIds
     * @return
     */
    List<MtEo> eoPropertyBatchGet(Long tenantId, List<String> eoIds);

    /**
     * eoLimitWoGet-根据执行作业获取生产指令
     *
     * @param tenantId
     * @param eoId
     * @return
     */
    String eoLimitWoGet(Long tenantId, String eoId);

    /**
     * woLimitEoQuery-获取生产指令下指定状态和类型的执行作业
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> woLimitEoQuery(Long tenantId, MtEoVO2 dto);

    /**
     * numberLimitEoGet-根据执行作业编码获取执行作业
     *
     * @param tenantId
     * @param eoNum
     * @return
     */
    String numberLimitEoGet(Long tenantId, String eoNum);

    /**
     * eoStatusUpdate-更新执行作业状态.docx
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String eoStatusUpdate(Long tenantId, MtEoVO3 dto);

    /**
     * eoStatusValidate-验证执行作业状态是否在指定状态列表中
     *
     * @param tenantId
     * @param dto
     * @return
     */
    boolean eoStatusValidate(Long tenantId, MtEoVO4 dto);

    /**
     * eoStatusAvailableValidate-验证执行作业状态是否为允许属性修改的状态
     *
     * @param eoId
     * @param tenantId
     * @return
     */
    boolean eoStatusAvailableValidate(Long tenantId, String eoId);

    /**
     * eoMaterialVerify-验证执行作业物料是否满足使用条件
     *
     * @param tenantId
     * @param eoId
     * @return
     */
    void eoMaterialVerify(Long tenantId, String eoId);

    /**
     * bomRouterLimitEoQuery-获取使用指定装配清单&工艺路线限定的所有的执行作业
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> bomRouterLimitEoQuery(Long tenantId, MtEoVO5 dto);


    /**
     * eoNextNumberGet-获取执行作业编码
     *
     * @param tenantId
     * @param dto
     * @return io.tarzan.common.domain.vo.MtNumrangeVO5
     * @Author Xie.yiyang
     * @Date 2019/11/21 10:39
     */
    MtNumrangeVO5 eoNextNumberGet(Long tenantId, MtEoVO32 dto);

    /**
     * eoValidateVerifyUpdate-执行作业验证标识验证更新
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String eoValidateVerifyUpdate(Long tenantId, MtEoVO16 dto);

    /**
     * eoValidateVerifyUpdateForRk-执行作业验证标识验证更新
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoVO29> eoValidateVerifyUpdateForRk(Long tenantId, List<MtEoVO16> dto);

    /**
     * woLimitEoCreate-根据生产指令生成执行作业
     *
     * @param tenantId
     * @param vo
     * @return
     */
    String woLimitEoCreate(Long tenantId, MtEoVO6 vo);

    /**
     * planTimeLimitEoQuery-获取指定时间内计划生产的执行作业
     *
     * @param dto
     * @return
     */
    List<String> planTimeLimitEoQuery(Long tenantId, MtEoVO7 dto);

    /**
     * releaseEoLimitWoQtyUpdateVerify-根据生产指令对应执行作业的下达数量验证生产指令数量变更
     *
     * @param tenantId
     * @param vo
     */
    void releaseEoLimitWoQtyUpdateVerify(Long tenantId, MtEoVO8 vo);

    /**
     * eoSort-根据指定属性对一批执行作业进行排序
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<String> eoSort(Long tenantId, MtEoVO9 condition);

    /**
     * eoStatusUpdateVerify-执行作业状态切换校验
     *
     * @param tenantId
     * @param targetStatus
     */
    void eoStatusUpdateVerify(Long tenantId, String eoId, String targetStatus);

    /**
     * eoHoldVerify-执行作业保留验证
     *
     * @param tenantId
     * @param eoId
     */
    void eoHoldVerify(Long tenantId, String eoId);

    /**
     * eoCloseVerify-执行作业关闭验证
     *
     * @param tenantId
     * @param eoId
     */
    void eoCloseVerify(Long tenantId, String eoId);

    /**
     * eoReleaseVerify-执行作业下达验证
     *
     * @param tenantId
     * @param eoId
     */
    void eoReleaseVerify(Long tenantId, String eoId);

    /**
     * eoReleaseVerify-执行作业下达批量验证
     *
     * @param tenantId
     * @param eoIds
     */
    void eoReleaseBatchVerifyForRk(Long tenantId, List<String> eoIds);

    /**
     * eoPreProductValidate-检查执行作业指示生产信息是否达到生产要求
     *
     * @param tenantId
     * @param eoId
     */
    void eoPreProductValidate(Long tenantId, String eoId);

    /**
     * eoHold-执行作业保留
     *
     * @param tenantId
     * @param vo
     */
    void eoHold(Long tenantId, MtEoVO10 vo);

    /**
     * eoHoldCancel-执行作业保留取消
     *
     * @param tenantId
     * @param dto
     */
    void eoHoldCancel(Long tenantId, MtEoVO28 dto);

    /**
     * eoClose-执行作业关闭
     *
     * @param tenantId
     * @param vo
     */
    void eoClose(Long tenantId, MtEoVO10 vo);

    /**
     * eoCloseCancel-执行作业关闭取消
     *
     * @param tenantId
     * @param vo
     */
    void eoCloseCancel(Long tenantId, MtEoVO10 vo);

    /**
     * eoComplete-执行作业完成
     *
     * @param tenantId
     * @param vo
     */
    void eoStatusComplete(Long tenantId, MtEoVO10 vo);

    /**
     * eoCompleteVerify-执行作业完成验证
     *
     * @param tenantId
     * @param vo
     */
    void eoCompleteVerify(Long tenantId, MtEoVO11 vo);

    /**
     * eoStatusCompleteVerify-执行作业状态完成验证
     *
     * @param tenantId
     * @param eoId
     */
    void eoStatusCompleteVerify(Long tenantId, String eoId);

    /**
     * eoQtyUpdateVerify-执行作业数量调整验证
     *
     * @param tenantId
     * @param vo
     */
    void eoQtyUpdateVerify(Long tenantId, MtEoVO12 vo);

    /**
     * eoAbandonVerify-执行作业作废验证
     *
     * @param tenantId
     * @param eoId
     */
    void eoAbandonVerify(Long tenantId, String eoId);

    /**
     * eoAbandon-执行作业作废
     *
     * @param tenantId
     * @param dto
     */
    void eoAbandon(Long tenantId, MtEoVO10 dto);

    /**
     * eoQtyUpdate-执行作业数量调整
     *
     * @param tenantId
     * @param dto
     */
    void eoQtyUpdate(Long tenantId, MtEoVO13 dto);

    /**
     * bomRouterLimitUniqueEoValidate-检查引用指定装配清单或者工艺路线的执行作业是否唯一
     *
     * @param tenantId
     * @param dto
     */
    void bomRouterLimitUniqueEoValidate(Long tenantId, MtEoVO17 dto);

    /**
     * woLimitEoBatchCreate-根据生产指令批量生成执行作业
     * <p>
     * update by chuang.yang 2019.11.28 增加复制BOM及ROUTER的逻辑，以及增加输入参数COPYFLAG及相关逻辑，修改部分用蓝色标记
     *
     * @param tenantId
     * @param dto
     */
    List<String> woLimitEoBatchCreate(Long tenantId, MtEoVO14 dto);

    /**
     * eoComplete-执行作业完成
     *
     * @param tenantId
     * @param dto
     */
    void eoComplete(Long tenantId, MtEoVO15 dto);

    /**
     * eoCompleteCancel-执行作业完成取消
     *
     * @param tenantId
     * @param dto
     */
    void eoCompleteCancel(Long tenantId, MtEoVO15 dto);

    /**
     * eoRelease-执行作业下达
     *
     * @param tenantId
     * @param dto
     */
    void eoRelease(Long tenantId, MtEoVO18 dto);

    /**
     * eoComponentQtyQuery-查询执行作业指定工艺或步骤的组件和需求用量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoVO20> eoComponentQtyQuery(Long tenantId, MtEoVO19 dto);

    /**
     * eoReleaseAndStepQueue-执行作业下达同时首道步骤排队
     *
     * @param tenantId
     * @param dto
     */
    void eoReleaseAndStepQueue(Long tenantId, MtEoVO18 dto);

    /**
     * propertyLimitEoComponentAssembleActualQuery-根据指定属性获取执行作业组件装配实绩
     *
     * @param tenantId
     * @param eoId
     * @return java.lang.String
     * @author guichuan.li
     * @date 2019/3/14
     */
    String eoOperationAssembleFlagGet(Long tenantId, String eoId);

    /**
     * eoMerge-执行作业合并
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/19
     */
    String eoMerge(Long tenantId, MtEoVO22 dto);

    /**
     * eoMergeVerify-执行作业合并验证
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/20
     */
    void eoMergeVerify(Long tenantId, MtEoVO22 dto);

    /**
     * eoSplitVerify-执行作业拆分验证
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/20
     */
    void eoSplitVerify(Long tenantId, MtEoVO23 dto);

    /**
     * eoSplit-执行作业拆分
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/20
     */
    String eoSplit(Long tenantId, MtEoVO24 dto);

    /**
     * coproductEoCreate-根据生产指令工艺路线组件分配情况创建联产品执行作业
     * <p>
     * update remarks
     * <ul>
     * <li>2019-09-18 benjamin 修改返回参数</li>
     * </ul>
     *
     * @param tenantId 租户Id
     * @param dto MtEoVO25
     * @return MtEoVO29
     * @author guichuan.li
     * @date 2019/3/20
     */
    MtEoVO29 coproductEoCreate(Long tenantId, MtEoVO25 dto);

    /**
     * eoIdentify-执行作业识别
     *
     * @param tenantId
     * @param identification
     * @author guichuan.li
     * @date 2019/3/20
     */
    List<String> eoIdentify(Long tenantId, String identification);

    /**
     * eoStatusCompleteCancel-执行作业状态完成取消
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/7/2
     */
    void eoStatusCompleteCancel(Long tenantId, MtEoVO18 dto);

    /**
     * attrLimitEoQuery-根据拓展属性获取执行作业
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/16
     */
    List<String> attrLimitEoQuery(Long tenantId, MtEoAttrVO1 dto);

    /**
     * eoLimitAttrQuery-获取执行作业拓展属性
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/16
     */
    List<MtExtendAttrVO> eoLimitAttrQuery(Long tenantId, MtEoAttrVO2 dto);

    /**
     * eoLimitAttrUpdate-新增&更新执行作业拓展属性同时记录执行作业拓展属性历
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/17
     */
    List<String> eoLimitAttrUpdate(Long tenantId, MtEoAttrVO3 dto);

    /**
     * eventLimitEoAttrHisBatchQuery-根据事件批量获取执行作业拓展
     *
     * @param tenantId
     * @param eventIds
     * @author guichuan.li
     * @date 2019/4/16
     */
    List<MtEoAttrHisVO1> eventLimitEoAttrHisBatchQuery(Long tenantId, List<String> eventIds);

    /**
     * eoAttrHisQuery-获取执行作业拓展属性变更历史
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/16
     */
    List<MtEoAttrHisVO1> eoAttrHisQuery(Long tenantId, MtEoAttrHisVO2 dto);

    /**
     * propertyLimitEoPropertyQuery-根据属性获取执行作业信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoVO31> propertyLimitEoPropertyQuery(Long tenantId, MtEoVO30 dto);

    /**
     * numberLimitEoQuery-根据执行作业编码批量获取获取执行作业
     *
     * @param tenantId
     * @param eoNum
     * @return
     */
    List<MtEo> numberLimitEoQuery(Long tenantId, List<String> eoNum);

    /**
     * eoAttrPropertyUpdate-执行指令新增&更新扩展表属性
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/11/20
     */
    void eoAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);

    /**
     * eoComponentBatchSplit-执行作业组件实绩批量拆分
     *
     * @param tenantId
     * @param dto
     * @return void
     * @Author Xie.yiyang
     * @Date 2019/11/22 10:44
     */
    void eoComponentBatchSplit(Long tenantId, MtEoVO34 dto);

    /**
     * eoBatchNumberGet-批量获取执行指令编码
     *
     * @param tenantId
     * @param dto
     * @return io.tarzan.common.domain.vo.MtNumrangeVO8
     * @Author Xie.yiyang
     * @Date 2019/11/26 15:19
     */
    MtNumrangeVO8 eoBatchNumberGet(Long tenantId, MtEoVO36 dto);

    /**
     * eoBatchUpdate-批量更新执行作业属性
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<tarzan.order.domain.vo.MtEoVO29>
     * @author chuang.yang
     * @date 2019/11/26
     */
    List<MtEoVO29> eoBatchUpdate(Long tenantId, MtEoVO39 dto, String fullUpdate);

    /**
     * eoAverageSplit-执行作业平均拆分
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<java.lang.String>
     * @Author Xie.yiyang
     * @Date 2019/11/27 10:58
     */
    List<String> eoAverageSplit(Long tenantId, MtEoVO40 dto);

    /**
     * eoKittingQtyCalculate-计算执行作业齐套数量
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/1/6
     */
    Double eoKittingQtyCalculate(Long tenantId, MtEoVO42 dto);

    /**
     * EO当前整体数量查询
     *
     * @param tenantId :
     * @param eoId :
     * @return java.lang.Double
     * @Author peng.yuan
     * @Date 2020/2/20 16:20
     */
    Double eoCurrentQuantityGet(Long tenantId, String eoId);

    /**
     * eoStatusBatchUpdate-更新执行作业状态（批量）
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoVO29> eoStatusBatchUpdate(Long tenantId, MtEoVO44 dto);

    /**
     * eoOperationAssembleFlagBatchGet-批量获取执行作业是否按照装配清单进行装配
     *
     * @param tenantId
     * @param eoIds
     * @return
     */
    List<MtEoVO49> eoOperationAssembleFlagBatchGet(Long tenantId, List<String> eoIds);

    /**
     * eoBatchRelease-执行作业批量下达
     *
     * @author benjamin
     * @date 2020/11/3 2:53 PM
     * @param tenantId 租户Id
     * @param dto MtEoVO50
     */
    void eoBatchRelease(Long tenantId, MtEoVO50 dto);

    /**
     * eoBasicBatchUpdate-批量更新执行作业属性-(仅更新不做校验查询)
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2020/10/29
     */
    List<MtEoVO29> eoBasicBatchUpdate(Long tenantId, MtEoVO46 dto);

    /**
     * eoReleaseAndStepBatchQueue-执行作业批量下达同时首道步骤排队
     *
     * @author benjamin
     * @date 2020/11/3 3:50 PM
     * @param tenantId 租户Id
     * @param dto MtEoVO50
     */
    void eoReleaseAndStepBatchQueue(Long tenantId, MtEoVO50 dto);

    /**
     * 批量查询wo下的eo信息
     *
     * @author chuang.yang
     * @date 2020/10/30
     * @param tenantId
     * @param workOrderIds
     */
    List<MtEoVO48> woLimitEoCountBatchGet(Long tenantId, List<String> workOrderIds);

    /**
     * eoBatchComplete-执行作业批量完成
     *
     * @param tenantId
     * @param dto
     */
    void eoBatchComplete(Long tenantId, MtEoVO47 dto);
}
