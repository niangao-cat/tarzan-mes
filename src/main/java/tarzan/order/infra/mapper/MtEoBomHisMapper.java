package tarzan.order.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.order.domain.entity.MtEoBomHis;
import tarzan.order.domain.vo.MtEoBomHisVO;

/**
 * EO装配清单历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
public interface MtEoBomHisMapper extends BaseMapper<MtEoBomHis> {
    List<MtEoBomHis> selectByConditionCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "condition") MtEoBomHisVO condition);
}
