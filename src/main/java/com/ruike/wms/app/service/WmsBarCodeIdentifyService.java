package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsCodeIdentifyDTO;

/**
 * @Classname CodeIdentifyService
 * @Description 条码识别service
 * @Date 2019/10/4 16:37
 * @Author zhihao.sang
 */
public interface WmsBarCodeIdentifyService {

    String CONTAINER = "CONTAINER";

    String MATERIAL_LOT = "MATERIAL_LOT";

    /**
     * @Description 识别扫描的条码为物料批或容器 (通用)
     * @param tenantId
     * @param code
     * @return com.ruike.wms.api.dto.api.controller.dto.CodeIdentifyDTO
     * @Date 2019/10/4 16:34
     * @author zhihao.sang
     */
    WmsCodeIdentifyDTO codeIdentify(Long tenantId, String code);


    /**
     * @Description 识别扫描的条码为物料批或容器 (不通用)
     * @param tenantId
     * @param code
     * @return com.ruike.wms.api.dto.api.controller.dto.CodeIdentifyDTO
     * @Date 2019/10/4 16:34
     * @author zhihao.sang
     */
    WmsCodeIdentifyDTO codeIdentifyPro(Long tenantId, String code);
}
