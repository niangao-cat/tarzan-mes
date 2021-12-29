package com.ruike.wms.domain.repository;

import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * <p>
 * 库位 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/12 14:32
 */
public interface WmsLocatorRepository {

    /**
     * 根据库位类型获取仓库下的库位列表
     *
     * @param tenantId    租户
     * @param warehouseId 仓库ID
     * @param locatorType 库位类型
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/12 02:22:27
     */
    List<MtModLocator> selectListByType(Long tenantId,
                                        String warehouseId,
                                        String locatorType);
}
