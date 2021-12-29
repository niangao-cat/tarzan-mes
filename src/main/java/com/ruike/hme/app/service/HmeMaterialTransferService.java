package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.*;
import org.hzero.core.base.AopProxy;

import java.util.List;

/**
 * 物料转移应用服务层
 *
 * @author jiangling.zheng@hand-china.com 2020-05-09 10:00
 */
public interface HmeMaterialTransferService extends AopProxy<HmeMaterialTransferService> {

    /**
     * 待转移条码查询
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @param dtoList
     * @return
     */
    HmeMaterialTransferDTO2 transferMaterialLotGet(Long tenantId, List<HmeMaterialTransferDTO> dtoList);

    /**
     * 目标条码查询
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @param dtoList
     * @return
     */
    HmeMaterialTransferDTO4 targetMaterialLotGet(Long tenantId, List<HmeMaterialTransferDTO> dtoList);

    /**
     * 目标条码查询
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @param targetDto
     * @return
     */
    HmeMaterialTransferDTO2 targetMaterialLotConfirmForUi(Long tenantId, HmeMaterialTransferDTO3 targetDto);

    /**
     * 锁定
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @param materialLotCode
     * @return
     */
    void getLock(Long tenantId, String materialLotCode);

    /**
     * 解除锁定
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @param materialLotCode
     * @return
     */
    void releaseLock(Long tenantId, String materialLotCode);

    /**
     * 查询目标条码供应商批次
     * 
     * @param tenantId              租户id
     * @param materialLotCode       条码
     * @author sanfeng.zhang@hand-china.com 2020/10/10 14:53 
     * @return com.ruike.hme.api.dto.HmeMaterialTransferDTO4
     */
    HmeMaterialTransferDTO4 targetMaterialLotInfoGet(Long tenantId, String materialLotCode);
}
