package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsStocktakeDocQueryDTO;
import com.ruike.wms.api.dto.WmsStocktakeDocSelectQueryDTO;
import com.ruike.wms.api.dto.WmsStocktakeMaterialDetailQueryDTO;
import com.ruike.wms.domain.vo.*;
import org.apache.ibatis.annotations.Param;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * 库存盘点单据 Mapper
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 09:40
 */
public interface WmsStocktakeDocMapper {

    /**
     * 根据条件查询列表
     *
     * @param dto 查询条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 09:47:14
     */
    List<WmsStocktakeDocVO> selectListByCondition(WmsStocktakeDocQueryDTO dto);

    /**
     * 查询不能被下达的盘点单号
     *
     * @param tenantId 租户
     * @param idList   盘点单Id列表
     * @return java.util.List<java.lang.String>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 10:03:36
     */
    List<String> selectNotNewStocktakeNums(@Param("tenantId") Long tenantId, @Param("idList") List<String> idList);

    /**
     * 查询不能被下达的盘点单ID
     *
     * @param tenantId 租户
     * @param idList   盘点单Id列表
     * @return java.util.List<java.lang.String>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 10:03:36
     */
    List<String> selectNotNewStocktakeIds(@Param("tenantId") Long tenantId, @Param("idList") List<String> idList);

    /**
     * 盘点单选择LOV
     *
     * @param dto 查询条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeDocSelectVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 04:39:28
     */
    List<WmsStocktakeDocSelectVO> stocktakeSelectLov(WmsStocktakeDocSelectQueryDTO dto);

    /**
     * 通过扫描盘点单号获取选择信息
     *
     * @param tenantId     租户
     * @param userId       用户
     * @param stocktakeNum 盘点单
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeDocSelectVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 04:39:28
     */
    WmsStocktakeDocSelectVO selectStocktakeDocByScan(@Param("tenantId") Long tenantId,
                                                     @Param("userId") Long userId,
                                                     @Param("stocktakeNum") String stocktakeNum);

    /**
     * 通过id查询成本中心
     *
     * @param tenantId    租户
     * @param stocktakeId id列表
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeCostCenterVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/14 11:10:05
     */
    List<WmsStocktakeCostCenterVO> selectCostCenterByIds(@Param("tenantId") Long tenantId, @Param("stocktakeId") String stocktakeId);

    /**
     * 查询物料明细
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeMaterialDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/10 03:29:02
     */
    List<WmsStocktakeMaterialDetailVO> selectMaterialDetailByIds(@Param("tenantId") Long tenantId, @Param("dto") WmsStocktakeMaterialDetailQueryDTO dto);

    /**
     * 查询盘点货位
     *
     * @param tenantId    租户
     * @param warehouseId 仓库ID
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/10 08:01:13
     */
    List<MtModLocator> selectStocktakeLocator(@Param("tenantId") Long tenantId,
                                              @Param("warehouseId") String warehouseId);

    /**
     * 查询盘点物料
     *
     * @param tenantId    租户
     * @param warehouseId 仓库ID
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/10 08:08:50
     */
    List<MtMaterial> selectStocktakeMaterial(@Param("tenantId") Long tenantId,
                                             @Param("warehouseId") String warehouseId);


    /**
     * 查询是否有漏盘
     *
     * @param tenantId    租户
     * @param stocktakeId 盘点单
     * @return java.lang.Integer
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/11 01:50:30
     */
    Integer selectLeakByDoc(@Param("tenantId") Long tenantId, @Param("stocktakeId") String stocktakeId);
}
