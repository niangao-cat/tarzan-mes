package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtEoActualHis;
import tarzan.actual.domain.vo.MtEoActualHisVO1;

/**
 * 执行作业实绩历史Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoActualHisMapper extends BaseMapper<MtEoActualHis> {

    /**
     * eventLimitEoActualHisBatchQuery-获取一批事件的执行作业实绩历史记录
     *
     * @param eventIds
     * @return
     */
    List<MtEoActualHis> eventLimitEoActualHisBatchQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventIds") List<String> eventIds);

    /**
     * eoActualHisPropertyQuery-获取执行作业实绩变更历史
     *
     * @param dto
     * @return
     */
    List<MtEoActualHis> eoActualHisPropertyQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtEoActualHisVO1 dto);

}
