package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeCosTestMonitorRecord;

import com.ruike.hme.domain.vo.HmeCosTestMonitorRecordVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
/**
 * COS测试良率监控记录表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021-09-16 14:29:14
 */
public interface HmeCosTestMonitorRecordService {

    /**
     * COS测试良率监控记录表 历史数据查询
     * @param tenantId 租户id
     * @param cosMonitorHeaderId 头id
     * @param pageRequest 分页参数
     * @return
     */
    Page<HmeCosTestMonitorRecordVO> queryCosTestMonitorRecord(Long tenantId, String cosMonitorHeaderId, PageRequest pageRequest);

}
