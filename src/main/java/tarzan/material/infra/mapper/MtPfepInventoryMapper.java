package tarzan.material.infra.mapper;

import org.apache.ibatis.annotations.Param;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.common.BaseMapper;
import tarzan.material.api.dto.MtPfepInventoryDTO4;
import tarzan.material.domain.entity.MtPfepInventory;

/**
 * 物料存储属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepInventoryMapper extends BaseMapper<MtPfepInventory> {

    MtPfepInventory mySelectOne(@Param(value = "tenantId") Long tenantId, @Param("dto") MtPfepInventory dto);

    Page<MtPfepInventory> mtPfepInventoryDetialQuery(@Param(value = "tenantId") Long tenantId,
                                                     @Param("dto") MtPfepInventoryDTO4 dto);
}
