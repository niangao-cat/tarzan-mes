package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.*;
import org.hzero.core.base.AopProxy;

import java.util.List;
import java.util.Map;

/**
 * 二维码解析应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-8-24 12:29:57
 */
public interface WmsQrCodeAnalysisService extends AopProxy<WmsQrCodeAnalysisService> {

    /**
     * 二维码解析
     * @author jiangling.zheng@hand-china.com 2020-08-24 12:29:57
     * @param tenantId
     * @param dto
     * @return Map<String, String>
     */

    WmsQrCodeAnalysisDTO qrCodeAnalysis(Long tenantId, WmsQrCodeAnalysisDTO2 dto);

}
