package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeDataCollectLine;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

/**
 * 生产数据采集行表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2020-07-16 19:35:58
 */
public interface HmeDataCollectLineRepository extends BaseRepository<HmeDataCollectLine>, AopProxy<HmeDataCollectLineRepository> {


    /**
     * 保存行表信息
     *
     * @param tenantId              租户Id
     * @param workcellId            工位Id
     * @param collectHeaderId       头表Id
     * @param operationId           工艺Id
     * @param shiftId               班次id
     * @param  defaultSiteId         站点
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/17 16:17
     * @return void
     */
    void insetDataCollectLineMsg(Long tenantId,String workcellId,String collectHeaderId,String operationId,String shiftId,String materialId,String defaultSiteId);

}
