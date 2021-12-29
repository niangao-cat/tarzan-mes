package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsOutSourceDTO;
import com.ruike.wms.api.dto.WmsOutSourceSendDTO;
import com.ruike.wms.domain.vo.*;

import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 外协发货repo层
 * @author: han.zhang
 * @create: 2020/06/18 11:11
 */
public interface WmsOutSourceRepository {

    /**
     * @Description 外协发货订单查询
     * @param tenantId
     * @param instructionDocNum
     * @return java.util.List<com.ruike.wms.domain.vo.WmsOutSourceOrderQueryVO>
     * @Date 2020-06-18 11:12
     * @Author han.zhang
     */
    List<WmsOutSourceOrderQueryVO> selectOutSourceOrder(Long tenantId,
                                                        String instructionDocNum);
    /**
     * @Description 单据扫描
     * @param tenantId
     * @param instructionDocNum
     * @return java.util.List<com.ruike.wms.domain.vo.WmsOutSourceScanVO>
     * @Date 2020-06-19 16:49
     * @Author han.zhang
     */
    WmsOutSourceScanVO scanOutSourceOrder(Long tenantId, String instructionDocNum);

    /**
     * @Description 物料条码扫描
     * @param tenantId
     * @param materialQueryVO
     * @return java.util.List<com.ruike.wms.domain.vo.WmsOutSourceScanVO>
     * @Date 2020-06-22 09:18
     * @Author han.zhang
     */
    WmsOutSourceMaterialReturnVO materialLotScan(Long tenantId, WmsOutSourceScanMaterialQueryVO materialQueryVO);

    /**
     * @Description 外协发货 提交
     * @param tenantId
     * @param sendDto
     * @return com.ruike.wms.domain.vo.WmsOutSourceMaterialReturnVO
     * @Date 2020-06-22 11:49
     * @Author han.zhang
     */
    WmsOutSourceSendDTO send(Long tenantId, WmsOutSourceSendDTO sendDto);

    /**
     * @Description 明细查询
     * @param tenantId
     * @param instructionId
     * @return com.ruike.wms.domain.vo.WmsOutSourceScanVO
     * @Date 2020-06-24 11:39
     * @Author han.zhang
     */
    List<WmsOsDetailVO> queryDetail(Long tenantId, String instructionId);

    /**
     * @Description 条码撤回
     * @param tenantId
     * @param materialQueryVO
     * @return java.util.List<com.ruike.wms.domain.vo.WmsOsDetailVO>
     * @Date 2020-06-28 09:41
     * @Author han.zhang
     */
    WmsOutSourceScanMaterialQueryVO returnMaterialLot(Long tenantId, WmsOutSourceScanMaterialQueryVO materialQueryVO);

    /**
     * 条码拆分
     *
     * @param tenantId 租户ID
     * @param dto 拆分信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/21 16:28:39
     * @return com.ruike.wms.api.dto.WmsOutSourceDTO
     */
    WmsOutSourceDTO materialLotSplit(Long tenantId, WmsOutSourceDTO dto);
}