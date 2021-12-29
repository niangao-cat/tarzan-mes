package tarzan.instruction.domain.repository;

import java.util.List;
import java.util.Map;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.vo.*;

/**
 * 仓储物流指令内容表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
public interface MtInstructionRepository extends BaseRepository<MtInstruction>, AopProxy<MtInstructionRepository> {

    /**
     * propertyLimitInstructionQuery-根据指定的指令属性获取指令
     * 
     * @author xiao tang
     * @date 2019-08-06 11:00
     * @param tenantId
     * @param mtInstruction MtLogisticInstructionVO
     * @return List
     */
    List<String> propertyLimitInstructionQuery(Long tenantId, MtInstructionVO10 mtInstruction);

    /**
     * instructionPropertyGet-根据指令Id获取指令属性
     * 
     * @author xiao tang
     * @date 2019-08-06 10:41
     * @param tenantId
     * @param instructionId 指令Id
     * @return MtLogisticInstructionVO
     */
    MtInstruction instructionPropertyGet(Long tenantId, String instructionId);

    /**
     * instructionPropertyBatchGet-根据批量指令id获取属性
     * 
     * @author xiao tang
     * @date 2019-08-06 11:12
     * @param tenantId
     * @param instructionIdList 指令Id列表
     * @return List
     */
    List<MtInstruction> instructionPropertyBatchGet(Long tenantId, List<String> instructionIdList);

    /**
     * instructionNextNumGet-获取下一个指令编码
     *
     * @author xiao tang
     * @date 2019-08-06 14:46
     * @param tenantId
     * @param siteId 站点Id
     * @return String
     */
    String instructionNextNumGet(Long tenantId, String siteId);

    /**
     * instructionUpdate-指令更新
     *
     * @author xiao tang
     * @date 2019-08-06 09:54
     * @param tenantId
     * @param dto MtInstructionVO
     * @return
     */
    MtInstructionVO6 instructionUpdate(Long tenantId, MtInstructionVO dto, String fullUpdate);

    /**
     * instructionExecute-指令执行
     * 
     * @author xiao tang
     * @date 2019-08-06 09:39
     * @param tenantId
     * @param executeVO MtLogisticInstructionVO3
     * @return MtLogisticInstructionVO4
     */
    MtInstructionVO4 instructionExecute(Long tenantId, MtInstructionVO3 executeVO);

    /**
     * noInstructionExecute-无指令执行
     *
     * @date 2020/1/10
     * @param tenantId
     * @param executeVO
     * @return tarzan.instruction.domain.vo.MtInstructionVO15
     */
    MtInstructionVO15 noInstructionExecute(Long tenantId, MtInstructionVO13 executeVO);

    /**
     * instructionExecutedCancel-指令执行取消
     * 
     * @author xiao tang
     * @date 2019-08-06 09:39
     * @param tenantId
     * @param executeVO
     * @return
     */
    MtInstructionVO9 instructionExecutedCancel(Long tenantId, MtInstructionVO12 executeVO);

    /**
     * instructionExecuteVerify-指令执行验证
     * 
     * @author xiao tang
     * @date 2019-08-06 11:34
     * @param tenantId
     * @param vo 指令Id
     */
    void instructionExecuteVerify(Long tenantId, MtInstructionVO8 vo);

    /**
     * instructionExecutedBack-指令执行撤销
     * 
     * @author xiao tang
     * @date 2019-08-06 10:56
     * @param tenantId
     * @param instructionId 指令Id
     * @return List
     */
    List<MtInstructionVO5> instructionExecutedBack(Long tenantId, String instructionId, String eventRequestId);

    /**
     * instructionRelease-指令下达
     * 
     * @author xiao tang
     * @date 2019-08-06 12:43
     * @param tenantId
     * @param instructionId 指令Id
     * @param eventRequestId 事件请求ID
     */
    void instructionRelease(Long tenantId, String instructionId, String eventRequestId);

    /**
     * instructionReleaseVerify-指令下达验证
     * 
     * @author xiao tang
     * @date 2019-08-06 11:36
     * @param tenantId
     * @param instructionId 指令Id
     */
    void instructionReleaseVerify(Long tenantId, String instructionId);

    /**
     * instructionCancel-指令取消
     * 
     * @author xiao tang
     * @date 2019-08-06 14:24
     * @param tenantId
     * @param instructionId 指令Id
     */
    void instructionCancel(Long tenantId, String instructionId, String eventRequestId);

    /**
     * instructionCancelVerify-指令取消验证
     * 
     * @author xiao tang
     * @date 2019-08-06 14:24
     * @param tenantId
     * @param instructionId 指令Id
     */
    void instructionCancelVerify(Long tenantId, String instructionId);

    /**
     * instructionComplete-指令完成
     * 
     * @author xiao tang
     * @date 2019-08-06 11:41
     * @param tenantId
     * @param instructionId 指令Id
     */
    void instructionComplete(Long tenantId, String instructionId, String eventRequestId);

    /**
     * instructionCompleteVerify-指令完成验证
     * 
     * @author xiao tang
     * @date 2019-08-06 11:30
     * @param tenantId
     * @param instructionId 指令Id
     */
    void instructionCompleteVerify(Long tenantId, String instructionId);

    /**
     * instructionCompletedCancel-指令完成取消
     * 
     * @author xiao tang
     * @date 2019-08-06 14:05
     * @param tenantId
     * @param instructionId 指令Id
     */
    void instructionCompletedCancel(Long tenantId, String instructionId, String eventRequestId);

    /**
     * instructionCompletedCancelVerify-指令完成取消验证
     * 
     * @author xiao tang
     * @date 2019-08-06 13:44
     * @param tenantId
     * @param instructionId 指令Id
     */
    void instructionCompletedCancelVerify(Long tenantId, String instructionId);

    /**
     * solveEo-根据EO创建实绩 返回实绩Id
     *
     * @author xiao tang
     * @date 2019-08-06 16:30
     * @param tenantId tenantId
     * @param mtInstruction MtLogisticInstruction
     * @param eventId 事件Id
     * @return String
     */
    String solveEo(Long tenantId, MtInstruction mtInstruction, String eventId);

    /**
     * solveMaterial-根据物料创建实绩 返回实绩Id
     *
     * @author xiao tang
     * @date 2019-08-06 16:30
     * @param tenantId tenantId
     * @param executeVO MtLogisticInstructionVO3
     * @param mtInstruction MtLogisticInstruction
     * @param eventId 事件Id
     * @return String
     */
    Map<String, List<String>> solveMaterial(Long tenantId, MtInstructionVO3 executeVO, MtInstruction mtInstruction,
                                            String eventId);

    /**
     * 根据指令ID获取指令及指令实绩
     * 
     * @param tenantId
     * @param instructionId
     * @return
     */
    MtInstructionVO11 instructionLimitInstructionAndActualQuery(Long tenantId, String instructionId);

    /**
     * instructionAttrPropertyUpdate-指令新增&更新扩展表属性
     *
     * @Author Xie.yiyang
     * @Date 2019/11/19 16:15
     * @param tenantId
     * @param dto
     * @return void
     */
    void instructionAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);

    /**
     * instructionExecuteStatusVerify-指令执行状态验证
     *
     * @Author Xie.yiyang
     * @Date 2019/12/18 9:51
     * @param tenantId
     * @param instructionId
     * @return void
     */
    void instructionExecuteStatusVerify(Long tenantId, String instructionId);

    /**
     * instructionExecuteMaterialLotVerify-指令执行物料批验证
     *
     * @Author Xie.yiyang
     * @Date 2019/12/18 10:35
     * @param tenantId
     * @param vo
     * @return void
     */
    void instructionExecuteMaterialLotVerify(Long tenantId, MtInstructionVO16 vo);

    /**
     * instructionExecuteMaterialVerify-指令执行物料验证
     *
     * @Author Xie.yiyang
     * @Date 2019/12/18 11:08
     * @param tenantId
     * @param vo
     * @return void
     */
    void instructionExecuteMaterialVerify(Long tenantId, MtInstructionVO17 vo);

    /**
     * 指令执行库位验证
     * 
     * @Author peng.yuan
     * @Date 2019/12/18 14:40
     * @param tenantId :
     * @param mtInstructionVO18 :
     * @return void
     */
    void instructionExecuteLocatorVerify(Long tenantId, MtInstructionVO18 mtInstructionVO18);

    /**
     * 指令执行库位验证
     * 
     * @Author peng.yuan
     * @Date 2019/12/18 15:38
     * @param tenantId :
     * @param mtInstructionVO19 :
     * @return void
     */
    void instructionExecuteQtyVerify(Long tenantId, MtInstructionVO19 mtInstructionVO19);


    /**
     * 指令执行库位批量验证
     * 
     * @Author peng.yuan
     * @Date 2020/1/7 10:53
     * @param tenantId :
     * @param mtInstructionVO18s :
     * @return void
     */
    void instructionExecuteLocatorBatchVerify(Long tenantId, List<MtInstructionVO18> mtInstructionVO18s);

    /**
     * 指令执行物料批量验证
     * 
     * @Author peng.yuan
     * @Date 2020/1/7 13:57
     * @param tenantId :
     * @param mtInstructionVO17 :
     * @return void
     */
    void instructionExecuteMaterialBatchVerify(Long tenantId, List<MtInstructionVO17> mtInstructionVO17);

    /**
     * 指令执行数量批量验证
     * 
     * @Author peng.yuan
     * @Date 2020/1/7 14:30
     * @param tenantId :
     * @param mtInstructionVO19s :
     * @return void
     */
    void instructionExecuteQtyBatchVerify(Long tenantId, List<MtInstructionVO19> mtInstructionVO19s);

    /**
     * 指令执行状态批量验证
     * 
     * @Author peng.yuan
     * @Date 2020/1/7 16:01
     * @param tenantId :
     * @param instructionIdList :
     * @return void
     */
    void instructionExecuteStatusBatchVerify(Long tenantId, List<String> instructionIdList);

    /**
     * 指令执行物料批批量验证
     * 
     * @Author peng.yuan
     * @Date 2020/1/8 9:52
     * @param tenantId :
     * @param mtInstructionVO16s :
     * @return void
     */
    void instructionExecuteMaterialLotBatchVerify(Long tenantId, List<MtInstructionVO16> mtInstructionVO16s);

    /**
     * 指令执行批量验证
     * 
     * @Author peng.yuan
     * @Date 2020/1/9 10:55
     * @param tenantId :
     * @param mtInstructionVO8s :
     * @return void
     */
    void instructionExecuteBatchVerify(Long tenantId, List<MtInstructionVO8> mtInstructionVO8s);

    /**
     * instructionBatchExecute-指令批量执行
     *
     * @author chuang.yang
     * @date 2020/1/10
     * @param tenantId
     * @param dto
     * @return java.util.List<tarzan.instruction.domain.vo.MtInstructionVO22>
     */
    List<MtInstructionVO22> instructionBatchExecute(Long tenantId, MtInstructionVO21 dto);
}
