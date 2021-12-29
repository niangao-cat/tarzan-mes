package tarzan.instruction.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionHis;
import tarzan.instruction.domain.vo.MtInstructionHisVO;
import tarzan.instruction.domain.vo.MtInstructionHisVO1;
import tarzan.instruction.domain.vo.MtInstructionHisVO2;

/**
 * 仓储物流指令内容历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
public interface MtInstructionHisRepository
                extends BaseRepository<MtInstructionHis>, AopProxy<MtInstructionHisRepository> {

    /**
     * 保存历史
     *
     * @author benjamin
     * @date 2019-06-18 16:38
     * @param tenantId
     * @param mtInstruction MtLogisticInstruction
     * @param eventId 事件Id
     * @return instructionHisId
     */
    String saveLogisticInstructionHistory(Long tenantId, MtInstruction mtInstruction, String eventId);

    /**
     * eventLimitInstructionHisQuery-指定事件的指令历史
     *
     * @author benjamin
     * @date 2019-07-08 15:35
     * @param tenantId
     * @param eventId 事件Id
     * @return List
     */
    List<MtInstructionHisVO> eventLimitInstructionHisQuery(Long tenantId, String eventId);

    /**
     * eventLimitInstructionHisBatchQuery-获取一批事件的指令变更历史
     *
     * @author benjamin
     * @date 2019-07-08 15:40
     * @param tenantId
     * @param eventIdList 事件Id集合
     * @return List
     */
    List<MtInstructionHisVO> eventLimitInstructionHisBatchQuery(Long tenantId, List<String> eventIdList);

    /**
     * instructionLimitHisQuery-根据指令获取变化历史
     *
     * @author benjamin
     * @date 2019-07-08 19:31
     * @param tenantId
     * @param instructionId 指令Id
     * @return List
     */
    List<MtInstructionHisVO> instructionLimitHisQuery(Long tenantId, String instructionId);

    /**
     * propertyLimitInstructionHisQuery-根据指令属性获取指令历史
     *
     * @author benjamin
     * @date 2019-07-08 20:09
     * @param tenantId
     * @param mtInstructionHis MtLogisticInstructionHis
     * @return List
     */
    List<MtInstructionHis> propertyLimitInstructionHisQuery(Long tenantId, MtInstructionHisVO1 mtInstructionHis);

    /**
     * 获取指令最新历史
     * @Author peng.yuan
     * @Date 2019/9/28 14:37
     * @param tenantId :
     * @param instructionId : 指令id
     * @return tarzan.instruction.domain.vo.MtInstructionHisVO2
     */
    MtInstructionHisVO2 instructionLatestHisGet(Long tenantId, String instructionId);
}
