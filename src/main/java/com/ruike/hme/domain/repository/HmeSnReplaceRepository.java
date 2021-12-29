package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeSnReplaceDTO;
import com.ruike.hme.domain.vo.HmeSnReplaceVO;

import java.util.List;

/**
 * HmeSnReplaceRepository
 *
 * @author: chaonan.hu@hand-china.com 2020-11-03 22:29:34
 **/
public interface HmeSnReplaceRepository {

    /**
     * 数据更新
     *
     * @param tenantId 租户ID
     * @param dto 更新数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/4 11:47:26
     * @return void
     */
    void updateSnReplaceData(Long tenantId, HmeSnReplaceVO dto);
}
