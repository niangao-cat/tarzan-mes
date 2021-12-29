package com.ruike.qms.app.service;

import com.ruike.qms.api.dto.QmsCheckScrapBarCodeDTO;
import com.ruike.qms.api.dto.QmsCheckScrapDocLineDTO2;
import com.ruike.qms.api.dto.QmsCheckScrapParamsDTO;
import org.hzero.core.base.AopProxy;

import java.util.List;

/**
 * 检验报废应用服务
 *
 * @author jiangling.zheng@hand-china.com  2020-08-26 09:37:14
 */
public interface QmsCheckScrapService extends AopProxy<QmsCheckScrapService> {

    /**
     * 条码扫描
     * @author jiangling.zheng@hand-china.com 2020-08-26 09:50:00
     * @param tenantId
     * @param dto
     * @return
     */
    QmsCheckScrapBarCodeDTO barCodeQuery(Long tenantId, QmsCheckScrapParamsDTO dto);

    /**
     * 提交
     * @author jiangling.zheng@hand-china.com 2020-08-26 09:50:00
     * @param tenantId
     * @param dto
     * @return
     */
    QmsCheckScrapDocLineDTO2 scrapSubmit(Long tenantId, QmsCheckScrapDocLineDTO2 dto);

}
