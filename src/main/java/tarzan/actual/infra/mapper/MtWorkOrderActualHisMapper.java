package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtWorkOrderActualHis;
import tarzan.actual.domain.vo.MtWorkOrderActualHisVO;

/**
 * 生产指令实绩历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtWorkOrderActualHisMapper extends BaseMapper<MtWorkOrderActualHis> {

    /**
     * select work order actual history
     *
     * @param tenantId
     * @param dto
     * @return list
     * @author benjamin
     * @date 2019-06-13 16:41
     */
    List<MtWorkOrderActualHis> queryHis(@Param(value = "tenantId") Long tenantId,
                    @Param("condition") MtWorkOrderActualHisVO dto);

    /**
     * select work order actual history
     * <p>
     * condition: event id
     *
     * @param tenantId
     * @param eventIds
     * @return list
     * @author benjamin
     * @date 2019-06-13 16:42
     */
    List<MtWorkOrderActualHis> selectByEventIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventIds") List<String> eventIds);
}
