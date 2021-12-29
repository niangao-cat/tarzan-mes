package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeRepairSnBindVO;
import tarzan.order.api.dto.MtEoDTO4;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/8/26 10:37
 */
public interface HmeRepairSnBindRepository {

    /**
     * 导出返修SN
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeRepairSnBindVO>
     * @author sanfeng.zhang@hand-china.com 2021/8/26
     */
    List<HmeRepairSnBindVO> repairSnExport(Long tenantId, MtEoDTO4 dto);

    /**
     * 校验返修条码
     *
     * @param tenantId
     * @param reworkMaterialLot
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/11/25
     */
    void validateReworkMaterialLot(Long tenantId, String eoId, String reworkMaterialLot);
}
