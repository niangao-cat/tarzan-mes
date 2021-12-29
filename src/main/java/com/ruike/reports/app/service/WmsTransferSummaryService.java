package com.ruike.reports.app.service;

import com.ruike.reports.api.dto.WmsTransferSummaryQueryDTO;
import com.ruike.reports.domain.vo.WmsTransferSummaryVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

/**
 * <p>
 * 调拨汇总报表 服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/23 14:27
 */
public interface WmsTransferSummaryService {

    /**
     * 查询分页列表
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return java.util.List<com.ruike.reports.domain.vo.WmsTransferSummaryVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/23 02:51:08
     */
    Page<WmsTransferSummaryVO> export(Long tenantId,
                                      WmsTransferSummaryQueryDTO dto,
                                      ExportParam exportParam,
                                      PageRequest pageRequest);
}
