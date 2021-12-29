package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeTagPassRateLineHis;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
/**
 * 偏振度和发散角良率维护行历史表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021-09-14 10:10:38
 */
public interface HmeTagPassRateLineHisService {

    /**
    * 偏振度和发散角良率维护行历史表查询参数
    *
    * @param tenantId 租户ID
    * @param hmeTagPassRateLineHis 偏振度和发散角良率维护行历史表
    * @param pageRequest 分页
    * @return 偏振度和发散角良率维护行历史表列表
    */
    Page<HmeTagPassRateLineHis> list(Long tenantId, HmeTagPassRateLineHis hmeTagPassRateLineHis, PageRequest pageRequest);

    /**
     * 偏振度和发散角良率维护行历史表详情
     *
     * @param tenantId 租户ID
     * @param lineHisId 主键
     * @return 偏振度和发散角良率维护行历史表列表
     */
    HmeTagPassRateLineHis detail(Long tenantId, Long lineHisId);

    /**
     * 创建偏振度和发散角良率维护行历史表
     *
     * @param tenantId 租户ID
     * @param hmeTagPassRateLineHis 偏振度和发散角良率维护行历史表
     * @return 偏振度和发散角良率维护行历史表
     */
    HmeTagPassRateLineHis create(Long tenantId, HmeTagPassRateLineHis hmeTagPassRateLineHis);

    /**
     * 更新偏振度和发散角良率维护行历史表
     *
     * @param tenantId 租户ID
     * @param hmeTagPassRateLineHis 偏振度和发散角良率维护行历史表
     * @return 偏振度和发散角良率维护行历史表
     */
    HmeTagPassRateLineHis update(Long tenantId, HmeTagPassRateLineHis hmeTagPassRateLineHis);

    /**
     * 删除偏振度和发散角良率维护行历史表
     *
     * @param hmeTagPassRateLineHis 偏振度和发散角良率维护行历史表
     */
    void remove(HmeTagPassRateLineHis hmeTagPassRateLineHis);
}
