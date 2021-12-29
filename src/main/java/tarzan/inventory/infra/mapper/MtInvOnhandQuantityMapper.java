package tarzan.inventory.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.inventory.api.dto.MtInvOnhandQuantityDTO;
import tarzan.inventory.domain.entity.MtInvOnhandQuantity;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO1;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO4;

/**
 * 库存量Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
public interface MtInvOnhandQuantityMapper extends BaseMapper<MtInvOnhandQuantity> {

    Double organizationSumOnhandQtyGet(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "locatorIds") List<String> locatorIds,
                    @Param(value = "materialId") String materialId, @Param(value = "lotCode") String lotCode,
                    @Param(value = "ownerType") String ownerType, @Param(value = "ownerId") String ownerId);

    List<MtInvOnhandQuantity> selectByOrganizationDetail(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "locatorIds") List<String> locatorIds,
                    @Param(value = "materialId") String materialId, @Param(value = "lotCode") String lotCode,
                    @Param(value = "ownerType") String ownerType, @Param(value = "ownerId") String ownerId);

    List<MtInvOnhandQuantity> selectByConditions(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "condition") MtInvOnhandQuantityVO1 condition);

    MtInvOnhandQuantity selectForUpdate(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "onhandQuantityId") String onhandQuantityId);

    MtInvOnhandQuantity selectOnhandQuantity(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtInvOnhandQuantity dto);

    List<MtInvOnhandQuantityVO4> queryInventoryOnhandQuantityForUi(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtInvOnhandQuantityDTO dto,
                    @Param(value = "locatorIdList") List<String> locatorIdList,
                    @Param(value = "lotList") List<String> lotList);
}
