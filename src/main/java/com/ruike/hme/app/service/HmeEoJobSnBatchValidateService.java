package com.ruike.hme.app.service;

import java.util.Map;

import com.ruike.hme.api.dto.HmeEoJobSnBatchDTO2;
import com.ruike.hme.api.dto.HmeEoJobSnBatchDTO4;
import com.ruike.hme.domain.vo.*;

/**
 * 批量工序作业平台-SN作业校验
 *
 * @author penglin.sui@hand-china.com 2020-11-19 16:47:00
 */
public interface HmeEoJobSnBatchValidateService {
    /**
     * 批量工序作业平台-进站校验
     *
     * @author penglin.sui@hand-china.com 2020-11-19 16:48:10
     */
    HmeEoJobSnBatchVO inSiteScanValidate(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     * 批量工序作业平台-投料校验
     *
     * @author penglin.sui@hand-china.com 2020-11-19 14:00:00
     */
    HmeEoJobSnBatchVO12 releaseValidate(Long tenantId, HmeEoJobSnBatchDTO4 dto);

    /**
     * 批量工序作业平台-条码绑定校验
     *
     * @param tenantId 租户Id
     * @param dto 参数
     * @return HmeEoJobSnBatchVO8
     */
    HmeEoJobSnBatchVO8 releaseScanValidate(Long tenantId, HmeEoJobSnBatchDTO2 dto);

    /**
     * 批量工序作业平台-投料退回校验
     *
     * @param tenantId 租户Id
     * @param dto 参数
     * @return HmeEoJobSnBatchVO20
     */
    HmeEoJobSnBatchVO20 releaseBackValidate(Long tenantId, HmeEoJobSnVO9 dto);
}
