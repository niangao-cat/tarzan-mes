package com.ruike.wms.infra.mapper;

import java.util.List;

import com.ruike.hme.domain.entity.HmeMaterialLotLabCode;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.vo.WmsMaterialLotAttrVO;
import com.ruike.wms.domain.vo.WmsMaterialLotPntVO;
import com.ruike.wms.domain.vo.WmsMaterialLotVO;
import com.ruike.wms.domain.vo.WmsMaterialLotVO2;
import tarzan.general.api.dto.MtTagGroupObjectDTO3;
import tarzan.inventory.domain.entity.MtMaterialLot;

import org.apache.ibatis.annotations.Param;

/**
 * @Classname WmsMaterialLotMapper
 * @Description TODO
 * @Date 2019/9/18 11:41
 * @Created by admin
 */
public interface WmsMaterialLotMapper {

    /**
     * @param materialLotQryDTO
     * @return java.util.List<com.ruike.wms.api.dto.WmsMaterialLotQryResultDTO>
     * @Description 条码查询
     * @Date 2019/9/19 10:52
     * @Created by admin
     */
    List<WmsMaterialLotQryResultDTO> selectBarCodeCondition(@Param("tenantId") Long tenantId, @Param("dto") WmsMaterialLotQryDTO materialLotQryDTO);

    /**
     * @param materialLotQryDTO
     * @return java.util.List<com.ruike.wms.api.dto.WmsMaterialLotQryResultDTO>
     * @Description 条码查询
     * @Date 2019/9/19 10:52
     * @Created by admin
     */
    List<WmsMaterialLotQryExportResultDTO> selectBarCodeExportCondition(@Param("tenantId") Long tenantId, @Param("dto") WmsMaterialLotQryDTO materialLotQryDTO);

    /**
     * @param materialLotHisQryDTO
     * @param tenantId
     * @return java.util.List<com.ruike.wms.domain.entity.WmsMaterialLotHisResultDTO>
     * @Description 条码历史查询
     * @Date 2019/9/19 10:53
     * @Created by admin
     */
    List<WmsMaterialLotHisResultDTO> selectBarCodeHis(@Param(value = "materialLotHisQryDTO") WmsMaterialLotHisQryDTO materialLotHisQryDTO,
                                                      @Param(value = "tenantId") Long tenantId);

    /**
     * @param materialLotIds
     * @return java.util.List<com.ruike.wms.domain.entity.ZMaterialLotE>
     * @Description 查询打印数据
     * @Date 2019/11/25 18:52
     * @Author weihua.liao
     */
    List<WmsMaterialLotPntVO> queryPrintData(@Param("tenantId") Long tenantId, @Param("materialLotIds") List<String> materialLotIds);

    /**
     * 根据条件获取物料批
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<WmsMaterialLotVO2> selectMaterialLotByCondition(@Param(value = "tenantId") Long tenantId,
                                                         @Param(value = "dto") WmsMaterialLotVO dto);

    /**
     * 根据物料批Id列表获取物料批
     *
     * @param tenantId
     * @param materialLotIds
     * @return
     */
    List<WmsMaterialLotVO2> selectMaterialLotByIds(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "materialLotIds") List<String> materialLotIds);

    MtMaterialLot selectMaterialLotByCode(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "materialLotCode") String materialLotCode);

    List<MtMaterialLot> selectMaterialLotByCodes(@Param(value = "tenantId") Long tenantId,
                                                 @Param(value = "materialLotCodes") List<String> materialLotCodes);

    /**
     * 查询带拓展字段的列表
     *
     * @param tenantId       租户
     * @param materialLotIds 物料批
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialLotAttrVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 05:16:23
     */
    List<WmsMaterialLotAttrVO> selectListWithAttrByIds(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "materialLotIds") List<String> materialLotIds);

    /**
     * 查询带拓展字段的列表
     *
     * @param tenantId        租户
     * @param materialLotCode 物料批
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialLotAttrVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 05:16:23
     */
    WmsMaterialLotAttrVO selectWithAttrByCode(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "materialLotCode") String materialLotCode);

    /**
     * 批量获取生产版本信息
     *
     * @param tenantId
     * @param siteId
     * @param mtMaterialIdList
     * @param materialVersionList
     * @return java.util.List<tarzan.general.api.dto.MtTagGroupObjectDTO3>
     * @author sanfeng.zhang@hand-china.com 2020/9/27 10:20
     */
    List<MtTagGroupObjectDTO3> batchProductionVersionQuery(@Param(value = "tenantId") Long tenantId, @Param("siteId") String siteId, @Param("mtMaterialIdList") List<String> mtMaterialIdList, @Param("materialVersionList") List<String> materialVersionList);

    /**
     * 实验代码
     *
     * @param tenantId
     * @param materialLotIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/20 10:25
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLabCode>
     */
    List<HmeMaterialLotLabCode> queryLabCodeByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);
}
