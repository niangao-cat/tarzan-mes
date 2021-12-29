package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.*;
import org.hzero.core.base.AopProxy;

import java.util.List;

/**
 * 库存调拨发出执行应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-04-27 09:50:00
 */
public interface WmsInvTransferIssueService extends AopProxy<WmsInvTransferIssueService> {

    /**
     * 单据扫码查询
     * @author jiangling.zheng@hand-china.com 2020-04-27 09:50:00
     * @param tenantId
     * @param docBarCode
     * @return
     */
    WmsInvTransferDTO docQuery(Long tenantId, String docBarCode);

    /**
     * 条码查询
     * @author jiangling.zheng@hand-china.com 2020-04-27 09:50:00
     * @param tenantId
     * @param dto
     * @return
     */
    List<WmsCostCtrMaterialDTO3> containerOrMaterialLotQuery(Long tenantId, WmsInvTransferDTO3 dto);

    /**
     * 执行
     * @author jiangling.zheng@hand-china.com 2020-04-27 09:50:00
     * @param tenantId
     * @param docDto
     * @return
     */
    WmsInvTransferDTO execute(Long tenantId, WmsInvTransferDTO5 docDto);

    /**
     * 明细查询
     * @author jiangling.zheng@hand-china.com 2020-04-27 09:50:00
     * @param tenantId
     * @param dto
     * @return
     */
    WmsInvTransferDTO7 docDetailQuery(Long tenantId, WmsInvTransferDTO6 dto);

    /**
     * 删除
     * @author jiangling.zheng@hand-china.com 2020-04-27 09:50:00
     * @param tenantId
     * @param dto
     * @return
     */
    WmsInvTransferDTO7 deleteMaterialLot(Long tenantId, WmsInvTransferDTO6 dto);

}
