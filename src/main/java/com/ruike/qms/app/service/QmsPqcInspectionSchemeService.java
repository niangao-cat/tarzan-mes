package com.ruike.qms.app.service;

import com.ruike.qms.api.dto.QmsPqcInspectionSchemeDTO;

/**
 * 巡检检验计划应用服务
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-12 16:41:12
 */
public interface QmsPqcInspectionSchemeService {

    /**
     * 巡检检验复制按钮
     *
     * @param tenantId
     * @param qmsPqcInspectionSchemeDTO
     * @return java.lang.String
     * @auther wenqiang.yin@hand-china.com 2021/2/5 15:30
    */
    String copy(Long tenantId, QmsPqcInspectionSchemeDTO qmsPqcInspectionSchemeDTO);
}
