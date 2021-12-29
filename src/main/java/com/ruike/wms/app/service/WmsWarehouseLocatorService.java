package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.SiteDTO;
import com.ruike.wms.api.dto.WmsLocatorDTO;
import com.ruike.wms.api.dto.WmsWarehouseDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.modeling.domain.entity.MtModSite;

import java.util.List;

/**
 * WmsWarehouseLocatorService
 *
 * @author liyuan.lv@hand-china.com 2020/04/30 23:43
 */
public interface WmsWarehouseLocatorService {
    Page<WmsWarehouseDTO> getWarehouse(Long tenantId, WmsWarehouseDTO dto, PageRequest pageRequest);

    Page<WmsLocatorDTO> getLocator(Long tenantId, WmsLocatorDTO dto, PageRequest pageRequest);

    MtModSite siteBasicPropertyGet(Long tenantId);

    /**
     * 站点下拉框
     *
     * @param tenantId 租户ID
     * @return java.util.List<com.ruike.wms.api.controller.dto.SiteDTO>
     * @author Jiangling.Zheng 2020/05/21 14:12
     */
    List<SiteDTO> getSite(Long tenantId);
}
