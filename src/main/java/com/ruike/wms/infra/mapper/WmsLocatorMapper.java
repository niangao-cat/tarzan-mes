package com.ruike.wms.infra.mapper;

import org.apache.ibatis.annotations.Param;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * <p>
 * 库位 mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/12 14:17
 */
public interface WmsLocatorMapper {

    /**
     * 根据库位类型获取仓库下的库位列表
     *
     * @param tenantId    租户
     * @param warehouseId 仓库ID
     * @param locatorType 库位类型
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/12 02:22:27
     */
    List<MtModLocator> selectListByType(@Param("tenantId") Long tenantId,
                                        @Param("warehouseId") String warehouseId,
                                        @Param("locatorType") String locatorType);
}
