package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.vo.WmsPoDeliveryVO5;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.util.List;

/**
 * 送货单行与采购订单行关系表应用服务
 *
 * @author han.zhang03@hand-china.com 2020-03-27 18:46:38
 */
public interface WmsPoDeliveryRelService {
    /**
     * 新增送货单
     *
     * @param tenantId 租户ID
     * @param dto      送货单数据
     * @return 送货单
     */
    MtInstructionDocVO3 createOrder(Long tenantId, WmsPoDeliveryDTO dto);

    /**
     * 生成送货单号
     *
     * @param tenantId  租户id
     * @param createDTO 数据
     * @return java.lang.String
     * @author han.zhang 2020-03-30 15:08
     */
    WmsDliveryNumReturnDTO createOrderNumber(Long tenantId, WmsDeliveryNumberCreateDTO createDTO);

    /**
     * 获取行列表
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @return java.util.List<com.ruike.wms.api.dto.WmsPoDeliveryScanLineReturnDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/2 09:17:01
     */
    List<WmsPoDeliveryScanLineReturnDTO> getLineList(Long tenantId, String instructionDocId);

    /**
     * 送货单扫描
     *
     * @param tenantId 租户id
     * @param dto      参数
     * @return java.lang.Object
     * @author han.zhang 2020-04-07 10:53
     */
    WmsPoDeliveryScanReturnDTO poDeliveryScan(Long tenantId, WmsPoDeliveryScanDTO dto);

    /**
     * 条码创建
     *
     * @param tenantId 租户ID
     * @param dto      创建数据
     * @return java.util.List<com.ruike.wms.domain.vo.MtPoDeliveryVO3>
     * @author han.zhang 2020-04-08 17:35
     */
    List<MtMaterialLot> createBarcode(Long tenantId, WmsCreateBarcodeDTO dto);

    /**
     * 取消送货单
     *
     * @param tenantId         租户id
     * @param instructionDocId 取消的送货单参数
     * @author han.zhang 2020-04-08 21:16
     */
    void cancelPoDelivery(Long tenantId, String instructionDocId);

    /**
     * 实物条码扫描
     *
     * @param tenantId 租户ID
     * @param dto      扫描数据
     * @return java.lang.Object
     * @author wenzhang.yu 2020/4/10 13:45
     **/
    WmsPoDeliveryScanReturnDTO2 materialLotCodeScan(Long tenantId, WmsPoDeliveryScanDTO2 dto);

    /**
     * 校验是否全部接收
     *
     * @param tenantId          租户ID
     * @param instructionDocNum 单据号
     * @author wenzhang.yu 2020/4/17 13:57
     **/
    WmsPoDeliveryVO5 iscompleted(Long tenantId, String instructionDocNum);

    /**
     * 过账：点击确认接收按钮
     *
     * @param tenantId 租户ID
     * @param dto      确认数据
     * @return com.ruike.wms.api.dto.WmsPoDeliveryConfirmDTO
     * @author wenzhang.yu 2020/4/14 9:43
     **/
    WmsPoDeliveryConfirmDTO confirmAcceptance(Long tenantId, WmsPoDeliveryConfirmDTO dto);

    /**
     * 明細界数据查询
     *
     * @param tenantId      租户ID
     * @param instructionId 单据行ID
     * @return WmsPoDeliveryDetailReturnDTO
     * @author wenzhang.yu 2020/4/14 14:32
     **/
    WmsPoDeliveryDetailReturnDTO queryDetail(Long tenantId, String instructionId);

    /**
     * 明細页面删除
     *
     * @param tenantId 租户ID
     * @param dtos     删除数据
     * @return WmsPoDeliveryScanReturnDTO.WmsPoDeliveryScanLineReturnDTO
     * @author wenzhang.yu 2020/4/14 18:06
     **/
    WmsPoDeliveryScanLineReturnDTO deleteDetail(Long tenantId, List<WmsPoDeliveryDetailDTO> dtos);

}
