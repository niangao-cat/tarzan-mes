package com.ruike.reports.app.service;

import com.ruike.reports.api.dto.WmsDistributionGeneralQueryDTO;
import com.ruike.reports.domain.vo.WmsDistributionGeneralVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

/**
 * <p>
 * 配送综合查询报表 服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 17:16
 */
public interface WmsDistributionGeneralService {

    /**
     * 导出分页列表
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param exportParam 导出参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.reports.domain.vo.WmsDistributionGeneralVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 05:03:23
     */
    Page<WmsDistributionGeneralVO> export(Long tenantId,
                                          WmsDistributionGeneralQueryDTO dto,
                                          ExportParam exportParam,
                                          PageRequest pageRequest);
}
