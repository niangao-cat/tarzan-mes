package tarzan.order.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.order.domain.entity.MtEoRouterHis;
import tarzan.order.domain.vo.MtEoRouterHisVO;

/**
 * EO工艺路线历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
public interface MtEoRouterHisMapper extends BaseMapper<MtEoRouterHis> {
    List<MtEoRouterHis> selectByConditionCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "condition") MtEoRouterHisVO condition);
}
