package tarzan.instruction.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.instruction.domain.entity.MtInstructionDocHis;
import tarzan.instruction.domain.vo.MtInstructionDocHisVO;
import tarzan.instruction.domain.vo.MtInstructionDocHIsVO1;
import tarzan.instruction.domain.vo.MtInstructionDocHisVO2;

/**
 * 指令单据头历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
public interface MtInstructionDocHisMapper extends BaseMapper<MtInstructionDocHis> {

    /**
     * select by event id list
     * 
     * @author benjamin
     * @date 2019-06-26 14:13
     * @param tenantId
     * @param eventIdList event Id List
     * @return List
     */
    List<MtInstructionDocHisVO> selectByEventIdList(@Param(value = "tenantId") Long tenantId,
                                                    @Param(value = "eventIdList") List<String> eventIdList);

    /**
     * select by property
     *
     * condition instructionDocId, instructionDocHisId, eventId
     * 
     * @author benjamin
     * @date 2019-07-08 19:51
     * @param tenantId
     * @param property MtLogisticInstructionDocHis
     * @return List
     */
    List<MtInstructionDocHisVO> selectByProperty(@Param(value = "tenantId") Long tenantId,
                                                 @Param(value = "property") MtInstructionDocHis property);


    /**
     * 根据单据属性获取单据变更历史
     */
    List<MtInstructionDocHis> selectPropertyLimit(@Param(value = "tenantId") Long tenantId,
                                                  @Param(value = "property") MtInstructionDocHIsVO1 property);

    MtInstructionDocHisVO2 selectRecent(Long tenantId, String instructionDocId);
}
