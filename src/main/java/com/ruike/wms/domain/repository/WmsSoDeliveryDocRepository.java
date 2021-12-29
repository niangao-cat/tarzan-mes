package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsProductPrepareDocQueryDTO;
import com.ruike.wms.api.dto.WmsSoDeliveryQueryDTO;
import com.ruike.wms.domain.vo.WmsProductPrepareDocVO;
import com.ruike.wms.domain.vo.WmsSoDeliveryDocVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 出货单单据 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 15:12
 */
public interface WmsSoDeliveryDocRepository {
    /**
     * 根据查询条件返回列表
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsSoDeliveryDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 02:22:24
     */
    List<WmsSoDeliveryDocVO> selectListByQueryCondition(Long tenantId,
                                                        WmsSoDeliveryQueryDTO dto);

    /**
     * 根据条件查询待备货的出货单
     *
     * @param tenantId 租户
     * @param dto      条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 09:32:46
     */
    List<WmsProductPrepareDocVO> selectPrepareListByCondition(Long tenantId,
                                                              WmsProductPrepareDocQueryDTO dto);

    /**
     * 根据备货单号准备查询待备货的出货单
     *
     * @param tenantId          租户
     * @param instructionDocNum 备货单号
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 09:32:46
     */
    WmsProductPrepareDocVO selectPrepareDocByNum(Long tenantId,
                                                 String instructionDocNum);

    /**
     * 通过单据ID获取其下所有的物料批ID
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @return java.util.List<java.lang.String>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 11:31:05
     */
    Set<String> selectMaterialLotIdByDocId(Long tenantId,
                                           String instructionDocId);

    /**
     * 通过单据ID获取其下所有的物料批ID
     *
     * @param tenantId          租户
     * @param instructionDocId  单据ID
     * @param materialLotStatus 条码状态
     * @return java.util.List<java.lang.String>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 11:31:05
     */
    Set<String> selectMaterialLotIdByDocId(Long tenantId,
                                           String instructionDocId,
                                           String materialLotStatus);

    /**
     * 查询备料目标货位
     *
     * @param tenantId     租户
     * @param warehouseIds 仓库
     * @return java.util.Map<java.lang.String, java.util.List < java.lang.String>>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/16 10:56:32
     */
    Map<String, List<String>> selectPrepareTargetLocators(Long tenantId,
                                                          List<String> warehouseIds);

    /**
     * 查询备料目标工厂
     *
     * @param tenantId     租户
     * @param warehouseIds 仓库
     * @return java.util.Map<java.lang.String, java.util.List < java.lang.String>>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/16 10:56:32
     */
    Map<String, List<String>> selectPrepareTargetSites(Long tenantId,
                                                       List<String> warehouseIds);
}
