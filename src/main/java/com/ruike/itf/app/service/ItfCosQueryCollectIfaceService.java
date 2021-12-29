package com.ruike.itf.app.service;


import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.itf.api.dto.CosQueryCollectItfReturnDTO;
import com.ruike.itf.api.dto.CosQueryCollectItfReturnDTO2;
import com.ruike.itf.api.dto.CosQueryCollectItfReturnDTO3;

import java.util.List;

public interface ItfCosQueryCollectIfaceService {

    CosQueryCollectItfReturnDTO invoke(Long tenantId, String materialLotCode);

    /**
     * 根据盒子信息查询是否测试偏振度标识和是否测试发散角标识
     *
     * @param tenantId 租户ID
     * @param dto 盒子信息
     * @param hmeMaterialLotLoadList 盒子装载信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/11 10:34:11
     * @return com.ruike.itf.api.dto.CosQueryCollectItfReturnDTO3
     */
    CosQueryCollectItfReturnDTO3 polarizationAndVolatilizationQuery(Long tenantId, CosQueryCollectItfReturnDTO2 dto,
                                                                    List<HmeMaterialLotLoad> hmeMaterialLotLoadList);
}
