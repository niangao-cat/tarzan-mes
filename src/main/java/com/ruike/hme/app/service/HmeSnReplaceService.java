package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeSnReplaceDTO;
import com.ruike.hme.api.dto.HmeSnReplaceDTO2;
import com.ruike.hme.domain.vo.HmeSnReplaceVO;

import java.util.List;

/**
 * HmeSnReplaceService
 * SN替换应用服务
 * @author: chaonan.hu@hand-china.com 2020-11-03 22:29:34
 **/
public interface HmeSnReplaceService {

    /**
     * SN替换1
     *
     * @param tenantId 租户ID
     * @param dto2 替换信息以及用户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/4 09:52:06
     * @return java.util.List<com.ruike.hme.api.dto.HmeSnReplaceDTO>
     */
    HmeSnReplaceDTO2 cmsSnReplace(Long tenantId, HmeSnReplaceDTO2 dto2);

    /**
     * SN替换
     *
     * @param tenantId 租户ID
     * @param dtoList 替换信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/4 09:52:06
     * @return java.util.List<com.ruike.hme.api.dto.HmeSnReplaceDTO>
     */
    List<HmeSnReplaceDTO> snReplace(Long tenantId, List<HmeSnReplaceDTO> dtoList);

    /**
     * 数据校验
     *
     * @param tenantId 租户ID
     * @param dtoList 替换信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/4 10:10:35
     * @return void
     */
    HmeSnReplaceVO checkSnReplaceData(Long tenantId, List<HmeSnReplaceDTO> dtoList);
}
