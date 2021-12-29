package tarzan.iface.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.iface.domain.entity.MtPoLine;

/**
 * 采购订单计划发运行Mapper
 *
 * @author yiyang.xie@hand-china.com 2019-10-08 19:52:47
 */
public interface MtPoLineMapper extends BaseMapper<MtPoLine> {

    List<MtPoLine> selectByPoDistributionId(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "poDistributionIds") String poDistributionIds);

    List<MtPoLine> selectByPoLineId(@Param(value = "tenantId") Long tenantId,
                                    @Param(value = "poLineIds") String poLineIds);

    List<MtPoLine> selectByPoNums(@Param(value = "tenantId") Long tenantId,
                                  @Param(value = "poLineNums") String poLineNums);
}
