package com.ruike.wms.domain.service;

import com.ruike.wms.api.dto.WmsStocktakeMaterialDetailQueryDTO;
import com.ruike.wms.api.dto.WmsStocktakeValidationDTO;
import com.ruike.wms.api.dto.WmsStocktakeScanDTO;
import com.ruike.wms.api.dto.WmsStocktakeSubmitDTO;
import com.ruike.wms.domain.vo.*;

import java.util.List;

/**
 * 库存盘点 业务层服务
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 11:46
 */
public interface WmsStocktakeService {

    /**
     * 条码扫描
     * 执行硬校验，并返回条码详情
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return com.ruike.wms.domain.vo.WmsStocktakeActualImplVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/14 09:42:21
     */
    WmsStocktakeBarcodeScanVO barcodeScan(Long tenantId, WmsStocktakeScanDTO dto);

    /**
     * 物料校验
     *
     * @param tenantId 租户
     * @param dto      参数
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/17 05:28:52
     */
    List<WmsMaterialVO> materialValidation(Long tenantId, WmsStocktakeValidationDTO dto);

    /**
     * 库存快照校验
     *
     * @param tenantId 租户
     * @param dto      参数
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/17 05:29:07
     */
    List<WmsMaterialLotAttrVO> actualValidation(Long tenantId, WmsStocktakeValidationDTO dto);

    /**
     * 是否被盘点过校验
     *
     * @param tenantId      租户
     * @param dto           参数
     * @param stocktakeType 盘点类型
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/17 05:29:07
     */
    List<WmsMaterialLotAttrVO> countedValidation(Long tenantId, String stocktakeType, WmsStocktakeValidationDTO dto);

    /**
     * 盘点提交
     *
     * @param tenantId 租户
     * @param dto      提交参数
     * @return com.ruike.wms.domain.vo.WmsStocktakeDocSelectVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/16 08:56:57
     */
    Boolean stocktakeSubmit(Long tenantId, WmsStocktakeSubmitDTO dto);

    /**
     * 查询物料明细
     *
     * @param tenantId          租户
     * @param materialCode      物料编码
     * @param stocktakeId       盘点单ID
     * @param stocktakeTypeCode 盘点类型
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeMaterialDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/10 03:29:02
     */
    List<WmsStocktakeMaterialDetailVO> stockDetailGet(Long tenantId, String stocktakeId, String stocktakeTypeCode,
                                                      String materialCode);

    /**
     * 库存快照校验
     *
     * @param tenantId          租户
     * @param stocktakeTypeCode 盘点类型
     * @param stocktakeId       盘点单ID
     * @param containerId       容器ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/17 05:29:07
     */
    List<WmsStocktakeMaterialLotVO> materialLotInContainerGet(Long tenantId, String stocktakeTypeCode, String stocktakeId, String containerId);

    /**
     * 插入已下达状态的盘点实际
     *
     * @param tenantId 租户
     * @param dto      条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeActualVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/17 07:24:45
     */
    List<WmsStocktakeActualVO> insertReleasedActual(Long tenantId, WmsStocktakeValidationDTO dto);
}
