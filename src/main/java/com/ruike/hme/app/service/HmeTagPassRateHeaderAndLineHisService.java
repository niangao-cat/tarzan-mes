package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeTagPassRateHeaderAndLineHisVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 偏振度和发散角良率维护头历史表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021/09/14 14:04
 */
public interface HmeTagPassRateHeaderAndLineHisService {
    /**
     * 头行历史数据查询
     *
     * @param tenantId    租户id
     * @param pageRequest 分页参数
     * @param heardId     头id
     * @return
     */
    Page<HmeTagPassRateHeaderAndLineHisVO> queryTagPassRateHeaderAndLineHis(Long tenantId, PageRequest pageRequest, String heardId);
}
