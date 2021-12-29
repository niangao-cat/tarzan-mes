package tarzan.order.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.order.domain.entity.MtEoHis;
import tarzan.order.domain.vo.MtEoHisVO;
import tarzan.order.domain.vo.MtEoHisVO2;

/**
 * 执行作业历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
public interface MtEoHisMapper extends BaseMapper<MtEoHis> {
    List<MtEoHis> selectByConditionCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "condition") MtEoHisVO condition);

    List<MtEoHis> selectByEventIds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "eventIds") List<String> eventIds);

    MtEoHisVO2 selectRecent(@Param(value = "tenantId") Long tenantId, @Param(value = "eoId") String eoId);
}
