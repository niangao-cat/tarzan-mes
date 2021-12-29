package tarzan.instruction.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.instruction.domain.entity.MtInstructionDetail;
import tarzan.instruction.domain.vo.*;

/**
 * 指令明细行Mapper
 *
 * @author yiyang.xie@hand-china.com 2019-10-16 10:19:53
 */
public interface MtInstructionDetailMapper extends BaseMapper<MtInstructionDetail> {

    List<MtInstructionDetailVO1> selectCondition(@Param(value = "tenantId") Long tenantId,
                                                 @Param(value = "dto") MtInstructionDetailVO dto);

    List<MtInstructionDetail> selectByMaterialLods(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "dto") MtInstructionDetailVO2 dto);

    List<MtInstructionDetailVO4> selectByMyCondition(@Param(value = "tenantId") Long tenantId,
                                                     @Param(value = "dto") MtInstructionDetailVO3 dto);
}
