package com.ruike.reports.app.service.impl;

import com.ruike.reports.api.dto.HmeCosWorkcellSummaryQueryDTO;
import com.ruike.reports.app.service.HmeCosWorkcellSummaryService;
import com.ruike.reports.domain.repository.HmeCosWorkcellSummaryRepository;
import com.ruike.reports.domain.vo.HmeCosWorkcellSummaryVO;
import org.hzero.export.vo.ExportParam;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * COS工位加工汇总 服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/1 16:30
 */
@Service
public class HmeCosWorkcellSummaryServiceImpl implements HmeCosWorkcellSummaryService {

    private final HmeCosWorkcellSummaryRepository repository;

    public HmeCosWorkcellSummaryServiceImpl(HmeCosWorkcellSummaryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<HmeCosWorkcellSummaryVO> export(Long tenantId, HmeCosWorkcellSummaryQueryDTO dto, ExportParam exportParam) {
        return repository.list(tenantId, dto);
    }
}
