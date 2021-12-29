package com.ruike.wms.app.service;

import com.ruike.wms.domain.vo.WmsStocktakeRangeVO;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * <p>
 * 盘点范围 服务管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/11 16:10
 */
public interface WmsStocktakeRangeService {

    /**
     * 批量新增
     *
     * @param tenantId        租户
     * @param stocktakeId     盘点单ID
     * @param rangeObjectType 范围类型
     * @param rangeList       范围列表@author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/11 04:30:28
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeRangeVO>
     */
    List<WmsStocktakeRangeVO> batchInsert(Long tenantId, String stocktakeId, String rangeObjectType, List<WmsStocktakeRangeVO> rangeList);

    /**
     * 盘点货位获取
     *
     * @param tenantId    租户
     * @param stocktakeId 盘点ID
     * @param locatorCode 货位编码
     * @return java.lang.String
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/16 03:13:21
     */
    MtModLocator locatorGet(Long tenantId, String stocktakeId, String locatorCode);
}
