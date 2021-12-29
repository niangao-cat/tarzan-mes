package com.ruike.reports.app.service.impl;

import com.ruike.reports.api.dto.WmsDistributionGeneralQueryDTO;
import com.ruike.reports.app.service.WmsDistributionGeneralService;
import com.ruike.reports.domain.repository.WmsDistributionGeneralRepository;
import com.ruike.reports.domain.vo.WmsDistributionGeneralVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 配送综合查询报表 服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 17:16
 */
@Service
public class WmsDistributionGeneralServiceImpl implements WmsDistributionGeneralService {

    private final WmsDistributionGeneralRepository wmsDistributionGeneralRepository;

    public WmsDistributionGeneralServiceImpl(WmsDistributionGeneralRepository wmsDistributionGeneralRepository) {
        this.wmsDistributionGeneralRepository = wmsDistributionGeneralRepository;
    }

    @Override
    public Page<WmsDistributionGeneralVO> export(Long tenantId, WmsDistributionGeneralQueryDTO dto, ExportParam exportParam, PageRequest pageRequest) {
        return wmsDistributionGeneralRepository.pageList(tenantId, dto, pageRequest);
    }
}
