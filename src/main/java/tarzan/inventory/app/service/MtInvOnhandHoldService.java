package tarzan.inventory.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.inventory.api.dto.MtInvOnhandQuantityDTO;
import tarzan.inventory.domain.vo.MtInvOnhandHoldVO3;

/**
 * 库存保留量应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
public interface MtInvOnhandHoldService {

    /**
     * 库存查询 查询库存预留信息
     * 
     * @author benjamin
     * @date 2019-08-15 16:32
     * @param tenantId 租户Id
     * @param holdVO MtInvOnhandHoldVO3
     * @param pageRequest PageRequest
     * @return page
     */
    Page<MtInvOnhandQuantityDTO> queryInventoryHoldQuantityForUi(Long tenantId, MtInvOnhandHoldVO3 holdVO,
                                                                 PageRequest pageRequest);
}
