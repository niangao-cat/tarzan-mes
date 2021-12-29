package com.ruike.reports.app.service;

import com.ruike.reports.api.dto.HmeCosWorkcellSummaryQueryDTO;
import com.ruike.reports.domain.vo.HmeCosWorkcellSummaryVO;
import org.hzero.export.vo.ExportParam;

import java.util.List;

/**
 * <p>
 * COS工位加工汇总 服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/1 16:30
 */
public interface HmeCosWorkcellSummaryService {

    /**
     * 根据条件查询列表
     *
     * @param tenantId    租户
     * @param dto         参数
     * @param exportParam 导出参数
     * @return java.util.List<com.ruike.reports.domain.vo.HmeCosWorkcellSummaryVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/1 03:58:52
     */
    List<HmeCosWorkcellSummaryVO> export(Long tenantId,
                                         HmeCosWorkcellSummaryQueryDTO dto,
                                         ExportParam exportParam);
}
