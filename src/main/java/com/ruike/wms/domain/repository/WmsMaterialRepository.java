package com.ruike.wms.domain.repository;

import java.util.List;

import com.ruike.wms.domain.vo.WmsMaterialVO;

/**
 * WmsMaterialRepository
 *
 * @author liyuan.lv@hand-china.com 2020/05/06 15:49
 */
public interface WmsMaterialRepository {
    /**
     * 根据条件获取物料
     *
     * @param tenantId 租户ID
     * @param dto      物料参数
     * @return 物料
     */
    List<WmsMaterialVO> siteLimitMaterialQuery(Long tenantId, WmsMaterialVO dto);

    /**
     * 获取用户可访问站点下的物料
     *
     * @param tenantId 租户ID
     * @param dto      物料参数
     * @return 物料
     */
    List<WmsMaterialVO> userPermissionMaterialQuery(Long tenantId, WmsMaterialVO dto);

    /**
     * 根据条件获取物料
     *
     * @param tenantId      租户ID
     * @param siteId        站点
     * @param itemGroupCode 物料类别编码
     * @param warehouseId   仓库ID
     * @return 物料
     */
    List<WmsMaterialVO> listGetByItemGroup(Long tenantId, String siteId, String itemGroupCode, String warehouseId);
}
