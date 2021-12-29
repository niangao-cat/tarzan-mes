package tarzan.instruction.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.entity.MtInstructionDocHis;
import tarzan.instruction.domain.vo.MtInstructionDocHIsVO1;
import tarzan.instruction.domain.vo.MtInstructionDocHisVO;
import tarzan.instruction.domain.vo.MtInstructionDocHisVO2;

/**
 * 指令单据头历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
public interface MtInstructionDocHisRepository
                extends BaseRepository<MtInstructionDocHis>, AopProxy<MtInstructionDocHisRepository> {

    /**
     * 保存DOC历史
     *
     * @author benjamin
     * @date 2019-06-18 16:38
     * @param tenantId
     * @param mtInstructionDoc MtLogisticInstructionDoc
     * @param eventId 事件Id
     * @return instructionDocHisId
     */
    String saveLogisticInstructionDocHistory(Long tenantId, MtInstructionDoc mtInstructionDoc, String eventId);

    /**
     * eventLimitInstructionDocHisQuery-指定事件的单据历史
     *
     * @author benjamin
     * @date 2019-06-24 11:46
     * @param tenantId
     * @param eventId 事件Id
     * @return List
     */
    List<MtInstructionDocHisVO> eventLimitInstructionDocHisQuery(Long tenantId, String eventId);

    /**
     * eventLimitInstructionDocHisBatchQuery-获取批量事件的指令单据历史
     *
     * @author benjamin
     * @date 2019-06-24 11:09
     * @param tenantId
     * @param eventIdList 事件Id集合
     * @return List
     */
    List<MtInstructionDocHisVO> eventLimitInstructionDocHisBatchQuery(Long tenantId, List<String> eventIdList);

    /**
     * instructionDocHisPropertyQuery-获取指令单据历史
     *
     * @author benjamin
     * @date 2019-06-24 14:08
     * @param tenantId
     * @param mtInstructionDocHis MtLogisticInstructionDocHis
     * @return List
     */
    List<MtInstructionDocHisVO> instructionDocHisPropertyQuery(Long tenantId, MtInstructionDocHis mtInstructionDocHis);

    /**
     * instructionDocLimitHisQuery-指定单据的单据变化历史
     *
     * @author benjamin
     * @date 2019-07-08 19:57
     * @param tenantId
     * @param mtInstructionDocHis MtLogisticInstructionDocHis
     * @return List
     */
    List<MtInstructionDocHisVO> instructionDocLimitHisQuery(Long tenantId, MtInstructionDocHis mtInstructionDocHis);

    /**
     * propertyLimitInstructionDocHisQuery-根据单据属性获取单据变更历史
     *
     * @author benjamin
     * @date 2019-07-08 20:06
     * @param tenantId
     * @param mtInstructionDocHIsVO1 MtLogisticInstructionDocHis
     * @return List
     */
    List<MtInstructionDocHis> propertyLimitInstructionDocHisQuery(Long tenantId,
                                                                  MtInstructionDocHIsVO1 mtInstructionDocHIsVO1);

    /**
     * 获取指令单据最新历史
     * @Author peng.yuan
     * @Date 2019/9/28 18:05
     * @param tenantId :
     * @param instructionDocId : 指令单据ID
     * @return tarzan.instruction.domain.vo.MtInstructionDocHisVO2
     */
    MtInstructionDocHisVO2 instructionDocLatestHisGet(Long tenantId, String instructionDocId);
}
