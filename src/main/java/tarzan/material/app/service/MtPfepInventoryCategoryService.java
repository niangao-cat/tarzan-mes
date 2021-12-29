package tarzan.material.app.service;

import tarzan.material.api.dto.MtPfepInventoryDTO2;
import tarzan.material.api.dto.MtPfepInventoryDTO3;
import tarzan.material.api.dto.MtPfepInventoryDTO4;

/**
 * 物料类别存储属性应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepInventoryCategoryService {
    /**
     * 物料类别pfep维护
     * @param tenantId
     * @param dto
     * @return
     */
    MtPfepInventoryDTO4 pfepInventoryCategoryUpdate(Long tenantId, MtPfepInventoryDTO2 dto);

    /**
     * 物料类别pfep复制
     * @param tenantId
     * @param dto
     * @return
     */
    MtPfepInventoryDTO4 copyMtPfepInventoryCategory(Long tenantId, MtPfepInventoryDTO3 dto);
}
