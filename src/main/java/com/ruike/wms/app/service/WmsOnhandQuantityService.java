package com.ruike.wms.app.service;

import org.hzero.export.vo.ExportParam;
import tarzan.inventory.api.dto.MtInvOnhandQuantityDTO;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO4;

import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/24 16:25
 */
public interface WmsOnhandQuantityService {

    /**
     * 导出库存信息
     *
     * @author zhangli
     * @date 2021-09-23 11:22
     * @param tenantId 租户Id
     * @param dto MtInvOnhandQuantityDTO
     * @param exportParam ExportParam
     * @return List<MtInvOnhandQuantityVO4>
     */
    List<MtInvOnhandQuantityVO4> export(Long tenantId, MtInvOnhandQuantityDTO dto, ExportParam exportParam);
}
