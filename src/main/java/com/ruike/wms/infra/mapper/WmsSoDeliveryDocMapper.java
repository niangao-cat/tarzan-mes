package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsProductPrepareDocQueryDTO;
import com.ruike.wms.api.dto.WmsSoDeliveryQueryDTO;
import com.ruike.wms.domain.vo.WmsProdPrepareExecVO;
import com.ruike.wms.domain.vo.WmsProductPrepareDocVO;
import com.ruike.wms.domain.vo.WmsSoDeliveryDocVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 出货单单据 Mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 13:49
 */
public interface WmsSoDeliveryDocMapper {

    /**
     * 根据查询条件返回列表
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @param userId   用户ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsSoDeliveryDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 02:22:24
     */
    List<WmsSoDeliveryDocVO> selectListByQueryCondition(@Param("tenantId") Long tenantId,
                                                        @Param("userId") Long userId,
                                                        @Param("dto") WmsSoDeliveryQueryDTO dto);

    /**
     * 根据条件查询待备货的出货单
     *
     * @param tenantId 租户
     * @param dto      条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 09:32:46
     */
    List<WmsProductPrepareDocVO> selectPrepareListByCondition(@Param("tenantId") Long tenantId,
                                                              @Param("dto") WmsProductPrepareDocQueryDTO dto);

    /**
     * 根据备货单号准备查询待备货的出货单
     *
     * @param tenantId          租户
     * @param instructionDocNum 备货单号
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 09:32:46
     */
    WmsProductPrepareDocVO selectPrepareDocByNum(@Param("tenantId") Long tenantId,
                                                 @Param("instructionDocNum") String instructionDocNum);

    /**
     * 通过单据ID获取其下所有的物料批ID
     *
     * @param tenantId          租户
     * @param instructionDocId  单据ID
     * @param materialLotStatus 条码状态
     * @return java.util.List<java.lang.String>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 11:31:05
     */
    List<String> selectMaterialLotIdByDocId(@Param("tenantId") Long tenantId,
                                            @Param("instructionDocId") String instructionDocId,
                                            @Param("materialLotStatus") String materialLotStatus);

    /**
     * 查询备料目标货位
     *
     * @param tenantId     租户
     * @param warehouseIds 仓库
     * @return java.util.Map<java.lang.String, java.util.List < java.lang.String>>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/16 10:56:32
     */
    List<WmsProdPrepareExecVO> selectPrepareTargetLocators(@Param("tenantId") Long tenantId,
                                                           @Param("warehouseIds") List<String> warehouseIds);

    /**
     * 查询备料目标工厂
     *
     * @param tenantId     租户
     * @param warehouseIds 仓库
     * @return java.util.Map<java.lang.String, java.util.List < java.lang.String>>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/16 10:56:32
     */
    List<WmsProdPrepareExecVO> selectPrepareTargetSites(@Param("tenantId") Long tenantId,
                                                        @Param("warehouseIds") List<String> warehouseIds);
}
