package com.ruike.wms.domain.service;

import com.ruike.wms.api.dto.WmsBarcodeDTO;
import com.ruike.wms.api.dto.WmsPrepareExecScanDTO;
import com.ruike.wms.api.dto.WmsPrepareExecScannedDTO;
import com.ruike.wms.api.dto.WmsPrepareExecSplitDTO;
import com.ruike.wms.domain.vo.*;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * 备料执行 服务
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 17:01
 */
public interface WmsPrepareExecuteService {
    /**
     * 查询配送单
     *
     * @param tenantId          租户
     * @param instructionDocNum 配送单号
     * @return com.ruike.wms.domain.vo.WmsDistDemandInsDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/24 08:27:20
     */
    WmsPrepareExecInsDocVO instructionDocQuery(Long tenantId, String instructionDocNum);

    /**
     * 查询配送单行
     *
     * @param tenantId         租户
     * @param instructionDocId 配送单ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistDemandInsVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/24 09:41:54
     */
    List<WmsPrepareExecInsVO> instructionQuery(Long tenantId, String instructionDocId);

    /**
     * 条码扫描
     *
     * @param tenantId 租户ID
     * @param scan     扫描参数
     * @return java.math.BigDecimal
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 02:54:31
     */
    WmsPrepareExecuteBarcodeVO barcodeScan(Long tenantId, WmsPrepareExecScanDTO scan);

    /**
     * 查询指令实际
     *
     * @param tenantId      租户
     * @param instructionId 指令行ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInstructionActualVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 02:56:17
     */
    List<WmsInstructionActualDetailVO> actualDetailQuery(Long tenantId, String instructionId);

    /**
     * 提交已扫描数据
     *
     * @param tenantId 租户
     * @param dto      提交参数
     * @return Boolean
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 11:17:08
     */
    WmsPrepareExecuteBarcodeVO submitScanned(Long tenantId, WmsPrepareExecScannedDTO dto);

    /**
     * 删除已扫描数据
     *
     * @param tenantId      租户
     * @param instructionId 提交参数
     * @param barcodeList   条码列表
     * @return Boolean
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 11:17:08
     */
    WmsPrepareExecInsVO removeScanned(Long tenantId, String instructionId, List<WmsBarcodeDTO> barcodeList);

    /**
     * 拆分
     *
     * @param tenantId 租户
     * @param dto      提交参数
     * @return Boolean
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 11:17:08
     */
    String split(Long tenantId, WmsPrepareExecSplitDTO dto);

    /**
     * 执行前验证
     *
     * @param tenantId         租户
     * @param instructionDocId 配送单ID
     * @return java.lang.Boolean
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 09:13:16
     */
    Boolean validateBeforeExecute(Long tenantId, String instructionDocId);

    /**
     * 执行
     *
     * @param tenantId         租户
     * @param instructionDocId 提交参数
     * @return Boolean
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 11:17:08
     */
    Boolean execute(Long tenantId, String instructionDocId);

    /**
     * 根据单据行ID查询推荐货位
     *
     * @param tenantId      租户
     * @param instructionId 单据行ID
     * @return tarzan.modeling.domain.entity.MtModLocator
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/22 02:45:37
     */
    MtModLocator getRecommendLocator(Long tenantId, String instructionId);
}
