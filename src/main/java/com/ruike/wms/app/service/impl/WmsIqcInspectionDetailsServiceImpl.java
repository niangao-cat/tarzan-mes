package com.ruike.wms.app.service.impl;

import com.ruike.reports.domain.vo.HmeCosWorkcellSummaryVO;
import com.ruike.wms.api.dto.WmsIqcInspectionDetailsDTO;
import com.ruike.wms.app.service.WmsIqcInspectionDetailsService;
import com.ruike.wms.domain.vo.WmsIqcInspectionDetailsVO;
import com.ruike.wms.domain.vo.WmsPickReturnReceiveVO;
import com.ruike.wms.infra.mapper.WmsIqcInspectionDetailsMapper;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/09 10:41
 */
@Service
public class WmsIqcInspectionDetailsServiceImpl implements WmsIqcInspectionDetailsService {

    @Autowired
    private WmsIqcInspectionDetailsMapper wmsIqcInspectionDetailsMapper;
    @Override
    @ProcessLovValue
    public Page<WmsIqcInspectionDetailsVO> queryList(Long tenantId, WmsIqcInspectionDetailsDTO dto, PageRequest pageRequest) {
        Page<WmsIqcInspectionDetailsVO> page =
                PageHelper.doPage(pageRequest, () -> wmsIqcInspectionDetailsMapper.queryList(tenantId, dto));
        return page;
    }

    @Override
    @ProcessLovValue
    public List<WmsIqcInspectionDetailsVO> export(Long tenantId, WmsIqcInspectionDetailsDTO dto, ExportParam exportParam) {
        return wmsIqcInspectionDetailsMapper.queryList(tenantId, dto);
    }
}
