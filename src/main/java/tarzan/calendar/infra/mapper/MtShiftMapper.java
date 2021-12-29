package tarzan.calendar.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.calendar.api.dto.MtShiftDTO1;
import tarzan.calendar.domain.entity.MtShift;
import tarzan.calendar.domain.vo.MtShiftVO;
import tarzan.calendar.domain.vo.MtShiftVO1;

/**
 * 班次信息Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:39
 */
public interface MtShiftMapper extends BaseMapper<MtShift> {

    List<String> typeLimitShiftTempletQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "shiftType") String shiftType, @Param(value = "enableFlag") String enableFlag);

    /**
     * 根据自定义条件查询
     * 
     * @Author peng.yuan
     * @Date 2019/10/10 18:50
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.calendar.domain.vo.MtShiftVO1>
     */
    List<MtShiftVO1> selectCondition(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtShiftVO dto);

    List<MtShiftDTO1> selectShiftTypes(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtShiftDTO1 dto);
}
