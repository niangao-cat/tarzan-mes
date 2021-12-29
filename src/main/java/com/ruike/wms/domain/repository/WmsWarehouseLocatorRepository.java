package com.ruike.wms.domain.repository;

import java.util.List;

import com.ruike.wms.api.dto.WmsLocatorDTO;
import com.ruike.wms.api.dto.WmsWarehouseDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.modeling.domain.entity.MtModLocator;

import org.hzero.mybatis.base.BaseRepository;

/**
 * WmsWarehouseLocatorRepository
 *
 * @author liyuan.lv@hand-china.com 2020/04/30 18:23
 */
public interface WmsWarehouseLocatorRepository extends BaseRepository<MtModLocator> {
    /**
     * 根据指定条件获取仓库
     * @param tenantId 租户Id
     * @param dto dto
     * @return WmsWarehouseDTO
     */
    List<WmsWarehouseDTO> getWarehouse(Long tenantId, WmsWarehouseDTO dto);

    /**
     * 根据指定条件获取库位
     * @param tenantId 租户Id
     * @param dto dto
     * @return WmsLocatorDTO
     */
    List<WmsLocatorDTO> getLocator(Long tenantId, WmsLocatorDTO dto);
}
