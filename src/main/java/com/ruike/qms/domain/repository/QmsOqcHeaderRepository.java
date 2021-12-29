package com.ruike.qms.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.qms.domain.entity.QmsOqcHeader;

/**
 * 出库检头表资源库
 *
 * @author yuchao.wang@hand-china.com 2020-08-28 14:18:10
 */
public interface QmsOqcHeaderRepository extends BaseRepository<QmsOqcHeader> {

    /**
     *
     * @Description 查询是否有进行中的检验单
     *
     * @author yuchao.wang
     * @date 2020/8/29 11:22
     * @param tenantId 租户ID
     * @param materialLotCode 物料批条码
     * @return boolean
     *
     */
    boolean checkProcessing(Long tenantId, String materialLotCode);
}
