package com.ruike.wms.infra.mapper;

import com.ruike.wms.domain.vo.*;
import org.apache.ibatis.annotations.Param;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.vo.MtModLocatorVO16;

import java.util.List;

public interface WmsOutSourceMapper {

    /**
     * @Description 外协订单查询
     * @param tenantId
     * @param instructionDocNum
     * @return java.util.List<com.ruike.wms.domain.vo.WmsOutSourceOrderQueryVO>
     * @Date 2020-06-18 11:10
     * @Author han.zhang
     */
    List<WmsOutSourceOrderQueryVO> selectOutSourceOrder(@Param("tenantId") Long tenantId,
                                                        @Param("instructionDocNum") String instructionDocNum);

    /**
     * @Description 外协订单非模糊查询
     * @param tenantId
     * @param instructionDocNum
     * @return java.util.List<com.ruike.wms.domain.vo.WmsOutSourceOrderQueryVO>
     * @Date 2020-06-18 11:10
     * @Author han.zhang
     */
    List<WmsOutSourceOrderQueryVO> selectOutSourceOrderNotLike(@Param("tenantId") Long tenantId,
                                                        @Param("instructionDocNum") String instructionDocNum);

    /**
     * @Description 查询行数据
     * @param tenantId
     * @param instructionDocId
     * @return java.util.List<com.ruike.wms.domain.vo.WmsOutSourceLineVO>
     * @Date 2020-06-22 19:18
     * @Author han.zhang
     */
    List<WmsOutSourceLineVO> selectOutSourceLine(@Param("tenantId") Long tenantId,
                                                 @Param("instructionDocId") String instructionDocId);

    /**
     * @Description 物料批条码扫描查询
     * @param tenantId
     * @param materialLotCode
     * @return java.util.List<com.ruike.wms.domain.vo.WmsOutSourceMaterialReturnVO>
     * @Date 2020-06-22 11:35
     * @Author han.zhang
     */
    List<WmsOutSourceMaterialReturnVO> selectMaterialLotData(@Param("tenantId") Long tenantId,
                                                            @Param("materialLotCode") String materialLotCode);

    /**
     * @Description 查询明细
     * @param tenantId
     * @param instructionId
     * @return java.util.List<com.ruike.wms.domain.vo.WmsOsDetailVO>
     * @Date 2020-06-24 11:51
     * @Author han.zhang
     */
    List<WmsOsDetailVO> selectDetail(@Param("tenantId") Long tenantId,
                                     @Param("instructionId") String instructionId);

    /**
     * 外协仓库位查询
     *
     * @param tenantId
     * @param siteId
     * @author jiangling.zheng@hand-china.com 2020/9/10 14:29
     * @return java.util.List<java.lang.String>
     */

    List<String> selectLocator(@Param("tenantId") Long tenantId,
                               @Param("siteId") String siteId);

    /**
     * 根据物料+站点查询取料货位
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param siteId 站点ID
     * @param locatorId 库位ID
     * @param materialVersion 物料版本
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/20 08:12:54
     * @return tarzan.modeling.domain.entity.MtModLocator
     */
    MtModLocator getMaterialLocator(@Param(value = "tenantId") Long tenantId
            , @Param(value = "materialId") String materialId
            , @Param(value = "siteId") String siteId
            , @Param(value = "locatorId") String locatorId
            , @Param(value = "materialVersion") String materialVersion);

    /**
     * 查询条码是否存在单据行的实际明细表中
     *
     * @param tenantId 租户ID
     * @param instructionId 单据行ID
     * @param materialLotId 条码ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/21 17:01:10
     * @return long
     */
    long getActialDetailTotal(@Param(value = "tenantId") Long tenantId, @Param(value = "instructionId") String instructionId,
                              @Param(value = "materialLotId") String materialLotId);

    /**
     * 根据物料+站点查询取料货位
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param siteId 站点ID
     * @param locatorId 库位ID
     * @param materialVersion 物料版本
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/20 08:12:54
     * @return tarzan.modeling.domain.entity.MtModLocator
     */
    MtModLocatorVO16 getMaterialLocatorCode(@Param(value = "tenantId") Long tenantId
            , @Param(value = "materialId") String materialId
            , @Param(value = "siteId") String siteId
            , @Param(value = "locatorId") String locatorId
            , @Param(value = "materialVersion") String materialVersion);

    MtModLocatorVO16 getMaterialLocatorCodeByType(@Param(value = "tenantId") Long tenantId
            , @Param(value = "materialId") String materialId
            , @Param(value = "siteId") String siteId
            , @Param(value = "locatorId") String locatorId
            , @Param(value = "materialVersion") String materialVersion
            , @Param(value = "locatorTypeList")List<String> locatorTypeList);
}
