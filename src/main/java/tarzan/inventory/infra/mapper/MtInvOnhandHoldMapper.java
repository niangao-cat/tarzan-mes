package tarzan.inventory.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.inventory.domain.entity.MtInvOnhandHold;
import tarzan.inventory.domain.vo.MtInvOnhandHoldVO3;

/**
 * 库存保留量Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
public interface MtInvOnhandHoldMapper extends BaseMapper<MtInvOnhandHold> {

    /**
     * onhandReserveBatchGet-批量获取库存保留明细
     *
     * @param onhandHoldIds
     * @return
     */
    List<MtInvOnhandHold> onhandHoldBatchGet(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "onhandHoldIds") List<String> onhandHoldIds);

    /**
     * propertyLimitOnhandReserveQuery-根据属性获取库存保留ID
     *
     * @param dto
     * @return
     */
    List<String> propertyLimitOnhandReserveQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtInvOnhandHoldVO3 dto);

    MtInvOnhandHold selectInvOnhandHold(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtInvOnhandHold dto);

    List<MtInvOnhandHold> selectInvOnhandHolds(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtInvOnhandHold dto);

}
