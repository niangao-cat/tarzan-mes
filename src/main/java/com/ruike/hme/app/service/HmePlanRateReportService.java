package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmePlanRateReportRequestDTO;
import com.ruike.hme.api.dto.HmePlanRateReportResponseDTO;
import com.ruike.hme.domain.vo.HmePlanRateDetailVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * description 计划达成率报表
 *
 * @author quan.luo@hand-china.com 2020/11/25 20:31
 */
public interface HmePlanRateReportService {

    /**
     * 计划达成率报表数据查询
     *
     * @param tenantId                    租户id
     * @param hmePlanRateReportRequestDTO 参数
     * @return 数据
     */
    List<HmePlanRateReportResponseDTO> planRateReportQuery(Long tenantId, HmePlanRateReportRequestDTO hmePlanRateReportRequestDTO);

    /**
     * 查询投产明细
     *
     * @param tenantId    租户
     * @param siteId      站点
     * @param shiftDate   班次日期
     * @param shiftCode   班次编码
     * @param workcellId  工段
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmePlanRateDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/17 10:37:14
     */
    Page<HmePlanRateDetailVO> detailQuery(Long tenantId, String siteId, LocalDate shiftDate, String shiftCode,
                                          String workcellId, PageRequest pageRequest);

    /**
     * 查询交付明细
     *
     * @param tenantId    租户
     * @param siteId      站点
     * @param shiftDate   班次日期
     * @param shiftCode   班次编码
     * @param workcellId  工段
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmePlanRateDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/17 10:37:14
     */
    Page<HmePlanRateDetailVO> detailDeliveryQuery(Long tenantId, String siteId, LocalDate shiftDate, String shiftCode,
                                                  String workcellId, PageRequest pageRequest);
}
