package tarzan.instruction.infra.mapper;

import java.util.List;

import com.ruike.itf.api.dto.ItfSendOutReturnDTO2;
import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;

import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.vo.MtInstructionVO10;

/**
 * 仓储物流指令内容表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
public interface MtInstructionMapper extends BaseMapper<MtInstruction> {

    Integer selectMaxNum(@Param(value = "tenantId") Long tenantId, @Param(value = "siteDateStr") String siteDateStr);

    List<MtInstruction> selectByIdList(@Param(value = "tenantId") Long tenantId,
                                       @Param(value = "instructionIdList") List<String> instructionIdList);

    /**
     * 适应Oracle查询空字符串
     */
    List<MtInstruction> selectForEmptyString(@Param(value = "tenantId") Long tenantId,
                                             @Param(value = "dto") MtInstructionVO10 dto);

    List<MtInstruction> selectByUnique(@Param(value = "tenantId") Long tenantId,
                                       @Param(value = "instruction") MtInstruction instruction);

    void deleteByDocId(@Param("instructionDocId") String instructionDocId);

    List<MtInstruction> selectByDocId(@Param("instructionId")String instructionId);

    String getMode(@Param(value = "tenantId")Long tenantId, @Param(value = "siteId")String siteId,
                   @Param(value = "materialId")String materialId, @Param(value = "toLocatorId")String toLocatorId);

    /**
     * 查询单据和行号，用作判断更新或新增
     * @param tenantId
     * @param instructionDocNumList
     * @param lineNumber
     * @return
     */
    List<ItfSendOutReturnDTO2> selectByDocCodeForJudge(@Param("tenantId") Long tenantId,
                                                       @Param("instructionDocNumList") String instructionDocNumList,
                                                       @Param("lineNumber") String lineNumber);

    String selectAttrValue(@Param("tenantId")Long tenantId,
                           @Param("attrName")String attrName,
                           @Param("instructionId")String instructionId);

    List<MtInstruction> selectByMaterial(@Param("tenantId")Long tenantId,
                                         @Param("instructionDocId")String instructionDocId,
                                         @Param("materialId")String materialId,
                                         @Param("siteId")String siteId,
                                         @Param("instructionType")String instructionType,
                                         @Param("materialVersion")String materialVersion);

    List<MtInstruction> selectBySourceDocId(@Param("tenantId")Long tenantId,
                                            @Param("deliveryInstructionDocIdList")List<String> deliveryInstructionDocIdList);

    List<ItfSendOutReturnDTO2> selectByDocCodeForJudgeList(@Param("tenantId") Long tenantId,
                                                           @Param("instructionDocNum") String instructionDocNum,
                                                           @Param("lineNumber") List<String> lineNumber);
}
