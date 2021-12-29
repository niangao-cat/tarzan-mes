package com.ruike.itf.infra.mapper;

import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.entity.HmeEquipmentStocktakeDoc;
import com.ruike.itf.domain.vo.ItfStocktakeVO;
import com.ruike.itf.domain.vo.ItfStocktakeVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/6 19:02
 */
public interface ItfStocktakeIfaceMapper {

    /**
     * 盘点单-查询
     *
     * @author sanfeng.zhang@hand-china.com 2021/7/6 19:38
     * @return java.util.List<com.ruike.itf.domain.vo.ItfStocktakeVO>
     */
    List<ItfStocktakeVO> queryStocktakeList(@Param("tenantId") Long tenantId);

    /**
     * 批量获取设备信息
     *
     * @param tenantId
     * @param assetEncodingList
     * @author sanfeng.zhang@hand-china.com 2021/7/7 9:13
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEquipment>
     */
    List<HmeEquipment> batchQueryEquipmentByAssetEncodings(@Param("tenantId") Long tenantId, @Param("assetEncodingList") List<String> assetEncodingList);

    /**
     * 批量获取设备盘点单
     * 
     * @param tenantId
     * @param stocktakeNumList
     * @author sanfeng.zhang@hand-china.com 2021/7/7 9:15 
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEquipmentStocktakeDoc>
     */
    List<HmeEquipmentStocktakeDoc> batchQueryStocktakeDocByStocktakeNums(@Param("tenantId") Long tenantId, @Param("stocktakeNumList") List<String> stocktakeNumList);

    /**
     * 批量更新设备
     *
     * @param tenantId
     * @param userId
     * @param equipmentList
     * @author sanfeng.zhang@hand-china.com 2021/7/7 9:59
     * @return void
     */
    void batchUpdateEquipment(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("equipmentList") List<ItfStocktakeVO2> equipmentList);

    /**
     * 批量更新盘点单
     *
     * @param tenantId
     * @param userId
     * @param equipmentList
     * @author sanfeng.zhang@hand-china.com 2021/7/7 10:01
     * @return void
     */
    void batchUpdateStocktake(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("equipmentList") List<ItfStocktakeVO2> equipmentList);

    /**
     * 批量更新设备盘点单
     *
     * @param tenantId
     * @param userId
     * @param equipmentStocktakeDocList
     * @author sanfeng.zhang@hand-china.com 2021/7/7 10:21
     * @return void
     */
    void batchUpdateEquipmentStocktakeDoc(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("equipmentStocktakeDocList") List<HmeEquipmentStocktakeDoc> equipmentStocktakeDocList);
}
