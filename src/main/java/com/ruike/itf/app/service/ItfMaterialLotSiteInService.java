package com.ruike.itf.app.service;

import com.ruike.hme.domain.vo.HmeVisualInspectionVO;
import com.ruike.hme.domain.vo.ItfMaterialLotSiteInVO;
import com.ruike.hme.domain.vo.ItfMaterialLotSiteInVO3;
import com.ruike.itf.api.dto.ItfMaterialLotSiteDTO;
import com.ruike.itf.api.dto.ItfMaterialLotSiteDTO2;

public interface ItfMaterialLotSiteInService {

    /**
     * 盒子进站接口
     *
     * @param tenantId 租户ID
     * @param dto 进站数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/12 10:45:17
     * @return com.ruike.hme.domain.vo.ItfMaterialLotSiteInVO
     */
    ItfMaterialLotSiteInVO materialLotSiteIn(Long tenantId, ItfMaterialLotSiteDTO dto);

    /**
     * 盒子出站接口
     *
     * @param tenantId 租户ID
     * @param dto 出站信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/12 03:35:10
     * @return com.ruike.hme.domain.vo.ItfMaterialLotSiteInVO3
     */
    ItfMaterialLotSiteInVO3 materialLotSiteOut(Long tenantId, ItfMaterialLotSiteDTO2 dto);

}
