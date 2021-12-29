package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.vo.MtWkcShiftVO;
import tarzan.actual.domain.vo.MtWkcShiftVO8;
import tarzan.actual.domain.vo.MtWkcShiftVO9;

/**
 * 开班实绩数据表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:00:23
 */
public interface MtWkcShiftMapper extends BaseMapper<MtWkcShift> {

    List<MtWkcShift> timeLimitShift(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "condition") MtWkcShiftVO condition);

    Long gettimes(@Param(value = "tenantId") Long tenantId, @Param(value = "ids") List<String> ids);

    List<MtWkcShift> nextShift(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "condition") MtWkcShift condition);

    List<MtWkcShift> previousShift(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "condition") MtWkcShift condition);

    List<MtWkcShift> currentShift(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "condition") MtWkcShift condition);

    /**
     * 根据条件查询列表
     *
     * @Author peng.yuan
     * @Date 2019/10/10 19:09
     * @param tenantId :
     * @param condition :
     * @return java.util.List<tarzan.actual.domain.vo.MtWkcShiftVO9>
     */
    List<MtWkcShiftVO9> selectCondition(@Param(value = "tenantId") Long tenantId,
                                        @Param(value = "condition") MtWkcShiftVO8 condition);
}
