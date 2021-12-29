package tarzan.material.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.material.api.dto.MtPfepInventoryDTO2;
import tarzan.material.api.dto.MtPfepInventoryDTO3;
import tarzan.material.api.dto.MtPfepInventoryDTO4;
import tarzan.material.domain.vo.MtPfepInventoryVO4;

/**
 * 物料存储属性应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepInventoryService {

    /**
     * 获取详细PFEP
     *
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @return
     */
    Page<MtPfepInventoryVO4> pfepInventoryDetialQuery(Long tenantId, PageRequest pageRequest, MtPfepInventoryDTO4 dto);

    /**
     * 保存物料PFEP
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    MtPfepInventoryDTO4 pfepInventorySave(Long tenantId, MtPfepInventoryDTO2 dto);

    /**
     * 物料扩展属性复制
     * @param tenantId
     * @param dto
     * @return
     */
    MtPfepInventoryDTO4 copyMtPfepInventory(Long tenantId, MtPfepInventoryDTO3 dto);
    
    MtPfepInventoryDTO2 setPfepInventorySaveParam(Long tenantId, MtPfepInventoryDTO3 dto,
                                                  MtPfepInventoryDTO2 dto2, String kid);
}
