package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeProcessGatherResultReportDto;
import com.ruike.hme.domain.vo.HmeProcessGatherResultReportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * HmeReportRepository
 *
 * @author: chaonan.hu@hand-china.com 2021-03-22 10:05:34
 **/
public interface HmeReportRepository {

    /**
     * 工序采集结果分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/22 10:50:46
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeProcessGatherResultReportVO>
     */
    Page<HmeProcessGatherResultReportVO> processGatherResultReportQuery(Long tenantId, HmeProcessGatherResultReportDto dto,
                                                                        PageRequest pageRequest);
}
