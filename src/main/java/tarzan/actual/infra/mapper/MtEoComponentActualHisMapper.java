package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtEoComponentActualHis;

/**
 * 执行作业组件装配实绩历史，记录执行作业物料和组件实际装配情况变更历史Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoComponentActualHisMapper extends BaseMapper<MtEoComponentActualHis> {

    List<MtEoComponentActualHis> queryEoComponentHis(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtEoComponentActualHis dto);

    /**
     * eventLimitEoComponentActualHisBatchQuery-批量获取指定事件的执行作业组件实绩历史记录
     *
     * @param eventIds
     * @return
     */
    List<MtEoComponentActualHis> selectByEventIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventIds") List<String> eventIds);

}
