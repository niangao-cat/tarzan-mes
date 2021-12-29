package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsInstructionExecuteDTO;
import com.ruike.wms.domain.vo.WmsInstructionExecuteVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/09 17:54
 */
public interface WmsInstructionExecuteService {

    /**
     * 单据执行统计报表查询
     *
     * @author li.zhang 2021/09/09 17:50
     */
    Page<WmsInstructionExecuteVO> queryList(Long tenantId, WmsInstructionExecuteDTO dto, PageRequest pageRequest);

    /**
     * 单据执行统计报表查询导出
     *
     * @author li.zhang 2021/09/09 17:50
     */
    List<WmsInstructionExecuteVO> export(Long tenantId, WmsInstructionExecuteDTO dto, ExportParam exportParam);
}
