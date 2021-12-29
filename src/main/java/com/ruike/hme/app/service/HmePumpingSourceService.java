package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmePumpingSourceDTO;
import com.ruike.hme.domain.vo.HmePumpingSourceAllVO;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 泵浦源性能数据展示
 *
 * @author wengang.qiang@hand-china.com 2021/08/31 21:54
 */
public interface HmePumpingSourceService {
    /**
     * 泵浦源性能数据查询
     *
     * @param tenantId            租户id
     * @param hmePumpingSourceDto 查询条件sn
     * @param pageRequest         分页
     * @return
     */
    HmePumpingSourceAllVO queryHmePumpingSource(Long tenantId, HmePumpingSourceDTO hmePumpingSourceDto,
                                                PageRequest pageRequest);
}
