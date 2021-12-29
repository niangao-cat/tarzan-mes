package com.ruike.reports.app.service;

import com.ruike.reports.api.dto.ReportDispatchDetailsDTO;
import com.ruike.reports.domain.vo.ReportDispatchDetailsVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName ReportDispatchDetailsService
 * @Description 派工明细报表
 * @Author lkj
 * @Date 2020/12/15
 */
public interface ReportDispatchDetailsService {
    /**
     * <strong>Title : selectDispatchDetails</strong><br/>
     * <strong>Description : 派工明细报表-查询 </strong><br/>
     * <strong>Create on : 2020/12/15 上午11:09</strong><br/>
     *
     * @param pageRequest
     * @param tenantId
     * @param dto
     * @return io.choerodon.core.domain.Page<com.ruike.reports.domain.vo.ReportDispatchDetailsVO>
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    Page<ReportDispatchDetailsVO> selectDispatchDetails(PageRequest pageRequest, String tenantId, ReportDispatchDetailsDTO dto);

    /**
     * 派工明细报表-导出
     *
     * @param tenantId
     * @param dto
     * @param response
     * @return
     * @author sanfeng.zhang@hand-china.com 2021/1/13 10:16
     */
    void reportDispatchExport(String tenantId, ReportDispatchDetailsDTO dto, HttpServletResponse response) throws IOException;
}

