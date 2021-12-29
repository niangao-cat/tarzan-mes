package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.vo.MtInstructionActualVO2;
import tarzan.actual.domain.vo.MtInstructionActualVO3;

/**
 * 指令实绩表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtInstructionActualMapper extends BaseMapper<MtInstructionActual> {
    /**
     * select by instruction id list
     *
     * @author benjamin
     * @date 2019-07-08 20:17
     * @param instructionIdList 指令Id集合
     * @return List
     */
    List<MtInstructionActual> selectByInstructionIdList(@Param(value = "tenantId") Long tenantId,
                                                        @Param(value = "instructionIdList") List<String> instructionIdList);

    List<MtInstructionActualVO2> selectByIdList(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "instructionIdList") List<String> instructionIdList);

    List<MtInstructionActualVO3> selectByActualIdList(@Param(value = "tenantId") Long tenantId, @Param(value = "actualIdList") List<String> actualIdList);

    void updateActualQty(@Param(value = "tenantId")Long tenantId,
                         @Param(value = "actualQty")double actualQty,
                         @Param(value = "instructionId")String instructionId);
}
