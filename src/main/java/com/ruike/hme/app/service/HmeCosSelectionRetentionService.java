package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosSelectionRetentionDTO;
import com.ruike.hme.domain.vo.HmeCosSelectionRetentionVO;
import com.ruike.hme.domain.vo.HmeProcessReportVo;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * COS筛选滞留表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/02/24 17:51
 */
public interface HmeCosSelectionRetentionService {

    /**
     * COS筛选滞留表查询
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-02-25 9:07
     * @return com.ruike.hme.domain.vo.HmeCosSelectionRetentionVO;
     */
    Page<HmeCosSelectionRetentionVO> queryList(Long tenantId, HmeCosSelectionRetentionDTO dto, PageRequest pageRequest);

    /**
     * COS筛选滞留表查询 - 导出
     *
     * @param tenantId
     * @param dto
     * @param response
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-02-25 9:07
     * @return void
     */
    List<HmeCosSelectionRetentionVO> queryRetentionExport(Long tenantId, HmeCosSelectionRetentionDTO dto, HttpServletResponse response);
}
