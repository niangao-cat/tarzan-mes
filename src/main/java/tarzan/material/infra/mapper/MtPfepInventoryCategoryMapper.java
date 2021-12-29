package tarzan.material.infra.mapper;

import org.apache.ibatis.annotations.Param;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.common.BaseMapper;
import tarzan.material.api.dto.MtPfepInventoryDTO4;
import tarzan.material.domain.entity.MtPfepInventoryCategory;

/**
 * 物料类别存储属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepInventoryCategoryMapper extends BaseMapper<MtPfepInventoryCategory> {

    MtPfepInventoryCategory mySelectOne(@Param(value = "tenantId") Long tenantId,
                                        @Param("dto") MtPfepInventoryCategory dto);

    Page<MtPfepInventoryCategory> mtPfepInventoryCategoryDetialQuery(@Param(value = "tenantId") Long tenantId,
                                                                     @Param(value = "dto") MtPfepInventoryDTO4 dto);
}
