package com.ruike.hme.domain.repository;

import org.hzero.core.base.AopProxy;
import tarzan.inventory.domain.entity.MtMaterialLot;

/**
 * 物料转移资源库
 *
 * @author jiangling.zheng@hand-china.com 2020-05-09 11:07
 */
public interface HmeMaterialTransferRepository extends AopProxy<HmeMaterialTransferRepository> {

    /**
     * 根据物料批编码获取物料批
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @param materialLotCode
     * @return
     */
    MtMaterialLot materialLotPropertyGet(Long tenantId, String materialLotCode);

    /**
     * 锁定/解除锁定
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @param materialLotCode
     * @param lockFlag
     * @return
     */
    void operationLock(Long tenantId, String materialLotCode, String lockFlag);

}
