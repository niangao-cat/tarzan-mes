package tarzan.instruction.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.instruction.domain.entity.MtInstructionHis;
import tarzan.instruction.domain.vo.MtInstructionHisVO;
import tarzan.instruction.domain.vo.MtInstructionHisVO1;
import tarzan.instruction.domain.vo.MtInstructionHisVO2;

/**
 * 仓储物流指令内容历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
public interface MtInstructionHisMapper extends BaseMapper<MtInstructionHis> {

    /**
     * select by event id list
     *
     * @author benjamin
     * @date 2019-07-08 15:41
     * @param tenantId
     * @param eventIdList 事件Id集合
     * @return List
     */
    List<MtInstructionHisVO> selectByEventIdList(@Param(value = "tenantId") Long tenantId,
                                                 @Param(value = "eventIdList") List<String> eventIdList);

    /**
     * select by instruction id
     * 
     * @author benjamin
     * @date 2019-07-08 19:33
     * @param tenantId
     * @param instructionId 指令Id
     * @return List
     */
    List<MtInstructionHisVO> selectByInstructionId(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "instructionId") String instructionId);



    List<MtInstructionHis> selectPropertyLimit(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "property") MtInstructionHisVO1 property);

    MtInstructionHisVO2 selectRecent(Long tenantId, String instructionId);
}
