package tarzan.order.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.order.domain.entity.MtWorkOrderHis;
import tarzan.order.domain.vo.MtWorkOrderHisVO;
import tarzan.order.domain.vo.MtWorkOrderHisVO2;

/**
 * 生产指令历史Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:34:08
 */
public interface MtWorkOrderHisMapper extends BaseMapper<MtWorkOrderHis> {

    List<MtWorkOrderHis> selectByConditionCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "condition") MtWorkOrderHisVO condition);

    List<MtWorkOrderHis> selectByEventIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventIds") List<String> eventIds);

    MtWorkOrderHisVO2 selectLatestHis(@Param(value = "tenantId") Long tenantId,
                                      @Param(value = "workOrderId") String workOrderId);
}
