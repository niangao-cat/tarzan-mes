package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeProcessGatherResultReportDto;
import com.ruike.hme.domain.vo.HmeProcessGatherResultReportVO;
import com.ruike.hme.domain.vo.HmeProcessGatherResultReportVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * HmeReportService
 * 报表应用服务
 * @author: chaonan.hu@hand-china.com 2021-03-22 10:02:12
 **/
public interface HmeReportService {

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

    /**
     * 工序采集结果报表导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/22 01:49:22
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessGatherResultReportVO2>
     */
    List<HmeProcessGatherResultReportVO2> processGatherResultReportExport(Long tenantId, HmeProcessGatherResultReportDto dto);

}
