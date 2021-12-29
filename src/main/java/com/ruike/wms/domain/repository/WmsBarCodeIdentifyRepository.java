package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsCodeIdentifyDTO;

/**
 * description
 *
 * @author liyuan.lv@hand-china.com 2020/04/09 11:16
 */
public interface WmsBarCodeIdentifyRepository {
    String CONTAINER = "CONTAINER";

    String MATERIAL_LOT = "MATERIAL_LOT";

    /**
     * @Description 识别扫描的条码为物料批或容器 (通用)
     * @param tenantId
     * @param code
     * @return com.ruike.wms.api.controller.dto.CodeIdentifyDTO
     * @Date 2019/10/4 16:34
     * @author zhihao.sang
     */
    WmsCodeIdentifyDTO codeIdentify(Long tenantId, String code);


    /**
     * @Description 识别扫描的条码为物料批或容器 (不通用)
     * @param tenantId
     * @param code
     * @return com.ruike.wms.api.controller.dto.CodeIdentifyDTO
     * @Date 2019/10/4 16:34
     * @author zhihao.sang
     */
    WmsCodeIdentifyDTO codeIdentifyPro(Long tenantId, String code);
}
