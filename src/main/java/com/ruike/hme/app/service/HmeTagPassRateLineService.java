package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeTagPassRateLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 偏振度和发散角良率维护行表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021-09-14 10:10:37
 */
public interface HmeTagPassRateLineService {

    /**
     * 偏振度和发散角良率维护行表查询参数
     *
     * @param tenantId    租户id
     * @param heardId     头表id
     * @param pageRequest 分页参数
     * @return
     */
    Page<HmeTagPassRateLineVO> queryTagPassRateLine(Long tenantId, String heardId, PageRequest pageRequest);

    /**
     * 创建偏振度和发散角良率维护行表
     *
     * @param tenantId             租户id
     * @param hmeTagPassRateLineVO 保存数据
     */
    void saveTagPassRateLine(Long tenantId, HmeTagPassRateLineVO hmeTagPassRateLineVO);

}
