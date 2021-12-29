package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtWorkOrderCompActualHis;
import tarzan.actual.domain.vo.MtWoComponentActualVO7;

/**
 * 生产订单组件装配实绩历史，记录生产订单物料和组件实际装配情况变更历史Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtWorkOrderCompActualHisMapper extends BaseMapper<MtWorkOrderCompActualHis> {

    List<MtWoComponentActualVO7> selectByActualId(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "workOrderComponentActualId") String workOrderComponentActualId);

    List<MtWorkOrderCompActualHis> selectByEventIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventIds") List<String> eventIds);

    List<MtWorkOrderCompActualHis> selectByEventId(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventId") String eventId);
}
