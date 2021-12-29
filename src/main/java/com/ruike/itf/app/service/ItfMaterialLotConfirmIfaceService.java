package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfMaterialLotConfirmIfaceDTO;
import com.ruike.itf.domain.entity.ItfMaterialLotConfirmIface;
import com.ruike.itf.domain.vo.ItfMaterialLotConfirmIfaceVO4;

import java.util.List;

/**
 * 立库入库复核接口表应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-07-13 19:40:25
 */
public interface ItfMaterialLotConfirmIfaceService {

    /**
     * 立库入库复核接口
     *
     * @param tenantId 租户ID
     * @param dto 传参 条码号或容器号
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/13 07:50:46
     * @return java.util.List<com.ruike.itf.domain.entity.ItfMaterialLotConfirmIface>
     */
    List<ItfMaterialLotConfirmIfaceVO4> itfMaterialLotConfirmOrChangeIface(Long tenantId, ItfMaterialLotConfirmIfaceDTO dto);
}
