package com.ruike.reports.domain.repository;

import com.ruike.reports.api.dto.HmeSsnInspectResultDTO;
import com.ruike.reports.domain.vo.HmeSsnInspectResultHeaderLinesVO;
import com.ruike.reports.domain.vo.HmeSsnInspectResultHeaderVO;
import com.ruike.reports.domain.vo.HmeSsnInspectResultLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 标准件检验结果查询
 *
 * @author wenqiang.yin@hand-china.com 2021/02/04 8:33
 */
public interface HmeSsnInspectResultRepository {


    /**
     * 标准件检验结果汇总查询
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return HmeSsnInspectResultHeaderVO
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-16 10:23
     */
    Page<HmeSsnInspectResultHeaderLinesVO> pageHeaderLinesList(Long tenantId, HmeSsnInspectResultDTO dto, PageRequest pageRequest);

    /**
     * 标准件检验结果汇总导出
     *
     * @param tenantId
     * @param dto
     * @return HmeSsnInspectResultHeaderVO
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-16 10:23
     */
    List<HmeSsnInspectResultHeaderLinesVO> listExport(Long tenantId, HmeSsnInspectResultDTO dto);
}