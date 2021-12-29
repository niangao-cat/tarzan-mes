package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsMaterialTurnoverRateDTO;
import com.ruike.wms.domain.vo.WmsMaterialTurnoverRateVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/27 17:17
 */
public interface WmsMaterialTurnoverRateService {

    /**
     * 物料周转率报表查询
     *
     * @author li.zhang 2021/09/27 17:00
     */
    Page<WmsMaterialTurnoverRateVO> queryList(Long tenantId, WmsMaterialTurnoverRateDTO dto, PageRequest pageRequest);

    /**
     * 物料周转率报表导出
     *
     * @author li.zhang 2021/09/27 17:00
     */
    List<WmsMaterialTurnoverRateVO> export(Long tenantId, WmsMaterialTurnoverRateDTO dto, ExportParam exportParam);
}
