package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosFunctionHeadDTO;
import com.ruike.hme.api.dto.HmeFunctionExportDTO;
import com.ruike.hme.api.dto.HmeFunctionReportDTO;
import com.ruike.hme.domain.entity.HmeCosFunction;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

import java.util.List;

/**
 * 芯片性能表应用服务
 *
 * @author wenzhang.yu@hand-china.com 2020-08-07 15:08:16
 */
public interface HmeCosFunctionService {


    Page<HmeCosFunctionHeadDTO> cosFunctionHeadQuery(Long tenantId, HmeCosFunctionHeadDTO dto, PageRequest pageRequest);


    Page<HmeCosFunction> cosFunctionQuery(Long tenantId, String loadSequence, PageRequest pageRequest);

    Page<HmeFunctionReportDTO> cosFunctionReport(Long tenantId, HmeCosFunctionHeadDTO dto, PageRequest pageRequest);


    List<HmeFunctionExportDTO> exportDetail(Long tenantId, ExportParam exportParam, HmeCosFunctionHeadDTO dto);
}
