package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeProcessNcDetail;
import com.ruike.hme.domain.vo.HmeProcessNcDetailVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 工序不良明细表应用服务
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
public interface HmeProcessNcDetailService {

    /**
     * 创建工序不良行明细表
     *
     * @param tenantId
     * @param hmeProcessNcDetail
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */
    void createDetail(Long tenantId, HmeProcessNcDetail hmeProcessNcDetail);

    /**
     * 修改工序不良行明细表
     *
     * @param tenantId
     * @param hmeProcessNcDetail
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */
    void updateDetail(Long tenantId, HmeProcessNcDetail hmeProcessNcDetail);
}
