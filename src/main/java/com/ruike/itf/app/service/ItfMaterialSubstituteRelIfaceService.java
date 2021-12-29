package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfCommonReturnDTO;
import com.ruike.itf.api.dto.ItfMaterialSubstituteRelDTO;

import java.util.List;

/**
 * 物料全局替代关系表应用服务
 *
 * @author yapeng.yao@hand-china.com 2020-08-18 14:40:53
 */
public interface ItfMaterialSubstituteRelIfaceService {

    /**
     * 物料全局替代关系同步接口
     * @param itfMaterialSubstituteRelDTOList
     * @return
     */
    List<ItfCommonReturnDTO> invoke(List<ItfMaterialSubstituteRelDTO> itfMaterialSubstituteRelDTOList);
}
