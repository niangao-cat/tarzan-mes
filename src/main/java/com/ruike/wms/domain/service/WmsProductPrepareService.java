package com.ruike.wms.domain.service;

import com.ruike.wms.api.dto.WmsBarcodeDTO;
import com.ruike.wms.api.dto.WmsProductPrepareDocQueryDTO;
import com.ruike.wms.domain.vo.*;

import java.util.List;

/**
 * <p>
 * 成品备料 服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/12 16:19
 */
public interface WmsProductPrepareService {
    /**
     * 根据条件查询待备货的出货单
     *
     * @param tenantId 租户
     * @param dto      条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 09:32:46
     */
    List<WmsProductPrepareDocVO> deliveryDocLovGet(Long tenantId,
                                                   WmsProductPrepareDocQueryDTO dto);

    /**
     * 根据备货单号准备查询待备货的出货单
     *
     * @param tenantId          租户
     * @param instructionDocNum 备货单号
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 09:32:46
     */
    WmsProductPrepareDocVO deliveryDocScan(Long tenantId,
                                           String instructionDocNum);

    /**
     * 根据单据ID查询备货行列表
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 10:31:04
     */
    List<WmsProductPrepareLineVO> prepareListGet(Long tenantId,
                                                 String instructionDocId);

    /**
     * 容器扫描
     *
     * @param tenantId         租户
     * @param containerCode    容器编码
     * @param instructionDocId 单据Id
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 09:32:46
     */
    WmsContainerVO containerScan(Long tenantId, String containerCode, String instructionDocId);

    /**
     * 条码扫描
     *
     * @param tenantId         租户
     * @param barcode          条码
     * @param instructionDocId 单据Id
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 09:32:46
     */
    WmsProdPrepareScanVO barcodeScan(Long tenantId, String barcode, String instructionDocId,String unBundingFlag);

    /**
     * 条码匹配结果提交
     *
     * @param tenantId         租户
     * @param instructionDocId 单据Id
     * @param vo               匹配结果
     * @param containerId      容器ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 09:32:46
     */
    List<WmsProductPrepareLineVO> barcodeMatchSubmit(Long tenantId, String instructionDocId, WmsProdPrepareScanVO vo, String containerId);

    /**
     * 根据行Id查询备货明细列表
     *
     * @param tenantId      租户
     * @param instructionId 行ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/15 05:20:50
     */
    List<WmsProductPrepareDetailVO> detailListQuery(Long tenantId,
                                                    String instructionId);

    /**
     * 条码撤销
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @param vo      条码勾选列表
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/15 06:35:40
     */
    List<WmsProductPrepareLineVO> barcodeCancel(Long tenantId, String instructionDocId,String instructionId,WmsProdPrepareScanVO vo);

    /**
     * 执行
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @return com.ruike.wms.domain.vo.WmsProductPrepareDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/15 07:17:52
     */
    WmsProductPrepareDocVO execute(Long tenantId, String instructionDocId);
}
