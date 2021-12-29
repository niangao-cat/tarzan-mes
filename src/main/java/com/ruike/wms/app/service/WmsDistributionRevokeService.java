package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsDistributionRevokeDTO;
import com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO;
import com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO4;
import com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO5;

import java.util.List;

/**
 * @ClassName WmsDistributionRevokeService
 * @Description 配送撤销
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/8 17:24
 * @Version 1.0
 **/
public interface WmsDistributionRevokeService {

    /**
     *@description 1.	配送单扫描
     *@author wenzhang.yu@hand-china.com
     *@date 2020/9/8 17:30
     *@param tenantId
     *@param instructionDocNum
     *@return com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO
     **/
    WmsDistributionRevokeReturnDTO scanInstructionDocNum(Long tenantId, String instructionDocNum);

    /**
     *@description 扫描条码
     *@author wenzhang.yu@hand-china.com
     *@date 2020/9/9 17:33
     *@param tenantId
     *@param scanCode
     *@param instructionDocId
     *@return com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO4
     **/
    WmsDistributionRevokeReturnDTO4 scanCode(Long tenantId, String scanCode,String instructionDocId);

    /**
     *@description 货位扫描逻辑
     *@author wenzhang.yu@hand-china.com
     *@date 2020/9/9 17:36
     *@param tenantId
     *@param locatorCode
     *@return com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO4
     **/
    WmsDistributionRevokeReturnDTO4 scanLocatorCode(Long tenantId, String locatorCode);

    /**
     *@description 明细界面
     *@author wenzhang.yu@hand-china.com
     *@date 2020/9/9 19:32
     *@param tenantId
     *@param instructionId
     *@return java.util.List<com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO5>
     **/
    List<WmsDistributionRevokeReturnDTO5> instructionDetail(Long tenantId, String instructionId);

    /**
     *@description 确认
     *@author wenzhang.yu@hand-china.com
     *@date 2020/9/9 19:45
     *@param tenantId
     *@param dto
     *@return void
     **/
    void confirm(Long tenantId, WmsDistributionRevokeDTO dto);
}
