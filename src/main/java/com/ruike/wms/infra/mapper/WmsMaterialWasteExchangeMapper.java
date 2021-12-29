package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsMaterialWasteExchangeDTO2;
import org.apache.ibatis.annotations.Param;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * @Description
 * @Author tong.li
 * @Date 2020/5/7 17:13
 * @Version 1.0
 */
public interface WmsMaterialWasteExchangeMapper {

    /**
    * @param tenantId 1
    * @param materialLotIds 2
    * @return : java.util.List<com.ruike.wms.api.dto.WmsMaterialWasteExchangeDTO2>
    * @Description: 根据物料批信息查询 物料
    * @author: tong.li
    * @date 2020/5/8 11:29
    * @version 1.0
    */
    List<WmsMaterialWasteExchangeDTO2> selectMaterialLotCondition(@Param("tenantId") Long tenantId,
                                                                  @Param("materialLotIds") List<String> materialLotIds);

    /**
    * @param tenantId 1
    * @param siteId 2
    * @return : java.lang.String
    * @Description: 根据siteId从mt_site_plant_releation表中查询plantCode
    * @author: tong.li
    * @date 2020/5/9 14:05
    * @version 1.0
    */
    String queryPlantCode(@Param("tenantId") Long tenantId, @Param("siteId") String siteId);

    /**
     * 查询站点下指定类型货位
     *
     * @param tenantId
     * @param siteId
     * @param locatorType
     * @author sanfeng.zhang@hand-china.com 2020/8/31 17:27
     * @return tarzan.modeling.domain.entity.MtModLocator
     */
    List<MtModLocator> queryLocatorIdBySiteAndCode(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("locatorType") String locatorType);

    /**
     * 仓库找货位
     *
     * @param tenantId
     * @param locatorId
     * @param locatorCode
     * @author sanfeng.zhang@hand-china.com 2020/8/31 18:27
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     */
    List<MtModLocator> queryParentLocatorByLocatorId(@Param("tenantId") Long tenantId, @Param("locatorId") String locatorId, @Param("locatorCode") String locatorCode);
}
