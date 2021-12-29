package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsIqcInspectionDetailsDTO;
import com.ruike.wms.domain.vo.WmsIqcInspectionDetailsVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/09 10:40
 */
public interface WmsIqcInspectionDetailsService {

    /**
     * IQC检验明细查询
     *
     * @author li.zhang 2021/09/09 9:51
     */
    Page<WmsIqcInspectionDetailsVO> queryList(Long tenantId, WmsIqcInspectionDetailsDTO dto, PageRequest pageRequest);

    /**
     * IQC检验明细导出
     *
     * @author li.zhang 2021/09/09 9:51
     */
    List<WmsIqcInspectionDetailsVO> export(Long tenantId, WmsIqcInspectionDetailsDTO dto, ExportParam exportParam);
}
