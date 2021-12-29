package com.ruike.wms.domain.repository;

import java.util.List;

import com.ruike.wms.api.dto.WmsSiteDTO;

/**
 * WmsSiteRepository
 *
 * @author liyuan.lv@hand-china.com 2020/06/17 15:50
 */
public interface WmsSiteRepository {

    /**
     * 获取当前用户默认的工厂
     * @param tenantId 租户Id
     * @return 工厂Id
     */
    String userDefaultSite(Long tenantId);
    /**
     * 获取当前用户可访问的工厂列表
     *
     * @param tenantId 租户Id
     * @return 工厂
     */
    List<WmsSiteDTO> getSite(Long tenantId);
}
