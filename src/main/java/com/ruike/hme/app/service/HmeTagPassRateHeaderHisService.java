package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeTagPassRateHeaderHis;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
/**
 * 偏振度和发散角良率维护头历史表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021-09-14 10:10:36
 */
public interface HmeTagPassRateHeaderHisService {

    /**
    * 偏振度和发散角良率维护头历史表查询参数
    *
    * @param tenantId 租户ID
    * @param hmeTagPassRateHeaderHis 偏振度和发散角良率维护头历史表
    * @param pageRequest 分页
    * @return 偏振度和发散角良率维护头历史表列表
    */
    Page<HmeTagPassRateHeaderHis> list(Long tenantId, HmeTagPassRateHeaderHis hmeTagPassRateHeaderHis, PageRequest pageRequest);

    /**
     * 偏振度和发散角良率维护头历史表详情
     *
     * @param tenantId 租户ID
     * @param headerHisId 主键
     * @return 偏振度和发散角良率维护头历史表列表
     */
    HmeTagPassRateHeaderHis detail(Long tenantId, Long headerHisId);

    /**
     * 创建偏振度和发散角良率维护头历史表
     *
     * @param tenantId 租户ID
     * @param hmeTagPassRateHeaderHis 偏振度和发散角良率维护头历史表
     * @return 偏振度和发散角良率维护头历史表
     */
    HmeTagPassRateHeaderHis create(Long tenantId, HmeTagPassRateHeaderHis hmeTagPassRateHeaderHis);

    /**
     * 更新偏振度和发散角良率维护头历史表
     *
     * @param tenantId 租户ID
     * @param hmeTagPassRateHeaderHis 偏振度和发散角良率维护头历史表
     * @return 偏振度和发散角良率维护头历史表
     */
    HmeTagPassRateHeaderHis update(Long tenantId, HmeTagPassRateHeaderHis hmeTagPassRateHeaderHis);

    /**
     * 删除偏振度和发散角良率维护头历史表
     *
     * @param hmeTagPassRateHeaderHis 偏振度和发散角良率维护头历史表
     */
    void remove(HmeTagPassRateHeaderHis hmeTagPassRateHeaderHis);
}
