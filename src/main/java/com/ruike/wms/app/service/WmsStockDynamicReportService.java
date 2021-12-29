package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsStockDynamicReportDTO;
import com.ruike.wms.domain.vo.WmsStockDynamicReportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/28 9:26
 */
public interface WmsStockDynamicReportService {

    /**
     * 出入库动态报表查询
     *
     * @author li.zhang 2021/09/28 9:13
     */
    Page<WmsStockDynamicReportVO> queryList(Long tenantId, WmsStockDynamicReportDTO dto, PageRequest pageRequest);

    /**
     * 出入库动态报表导出
     *
     * @author li.zhang 2021/09/28 9:13
     */
    List<WmsStockDynamicReportVO> export(Long tenantId, WmsStockDynamicReportDTO dto, ExportParam exportParam);
}
