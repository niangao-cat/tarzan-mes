package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsLocatorPutInStorageDTO;
import com.ruike.wms.api.dto.WmsMiscInBarCodeDTO;
import com.ruike.wms.api.dto.WmsMiscInBarCodeDetailDTO;
import com.ruike.wms.api.dto.WmsMiscInCostCenterDTO;

import java.util.List;

/**
 * @Classname MiscInHipsService
 * @Description 杂收应用服务
 * @Date 2019/9/26 14:39
 * @Author zhihao.sang
 * @Moved by yifan.xiong@hand-china.com 2020-9-24 19:52:37
 */
public interface WmsMiscInHipsService {

    String Y = "Y";

    String N = "N";

    /**
     * @Description 成本中心查询
     * @param costCenter
     * @return java.util.List<com.superlighting.hwms.api.controller.dto.MiscInCostCenterDTO>
     * @Date 2019/9/26 14:44
     * @author zhihao.sang
     */
    List<WmsMiscInCostCenterDTO> costCenterQuery(Long tenantId, String costCenter);

    /**
     * @Description 条码扫描查询
     * @param tenantId
     * @param barCode
     * @return com.superlighting.hwms.api.controller.dto.MiscInBarCodeDTO
     * @Date 2019/9/26 19:04
     * @author zhihao.sang
     */
    WmsMiscInBarCodeDTO barCodeQuery(Long tenantId, String barCode);

    /**
     * @Description 杂收确认
     * @param dtoList 条码信息的list
     * @return void
     * @Date 2019/9/27 10:02
     * @author zhihao.sang
     */
    void miscInConfirm(Long tenantId, List<WmsMiscInBarCodeDTO> dtoList,Boolean enableFlag);

    /**
     * @Description 条码明细查询
     * @param tenantId
     * @param barCodeDetailDTO
     * @return com.superlighting.hwms.api.controller.dto.MiscInBarCodeDTO
     * @Date 2019/9/28 10:24
     * @author zhihao.sang
     */
    WmsMiscInBarCodeDTO barCodeDetailQuery(Long tenantId, WmsMiscInBarCodeDetailDTO barCodeDetailDTO);

    /**
     * @param tenantId
     * @param siteId
     * @param locatorCode
     * @return com.superlighting.hwms.api.controller.dto.LocatorPutInStorageDTO
     * @Description 扫描货位
     * @Date 2019/11/4 18:18
     * @Created by {HuangYuBin}
     */
    WmsLocatorPutInStorageDTO getLocator(Long tenantId, String siteId, String locatorCode);

    /**
     * @Description 杂收再利用条码扫描查询
     * @param tenantId
     * @param barCode
     * @return com.superlighting.hwms.api.controller.dto.MiscInBarCodeDTO
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-12 19:41
     * @return
     */
    WmsMiscInBarCodeDTO barCodeQueryReuse(Long tenantId, String barCode);


    /**
     * @Description 再利用杂收确认
     * @param dtoList 条码信息的list
     * @return void
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-02-22 15:39
     */
    void miscInConfirmReuse(Long tenantId, List<WmsMiscInBarCodeDTO> dtoList, Boolean enableFlag);
}
