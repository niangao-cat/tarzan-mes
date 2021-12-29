package com.ruike.reports.app.service;

import com.ruike.reports.api.dto.HmeCosInProductionDTO;
import com.ruike.reports.domain.vo.HmeCosInProductionVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

/**
 * description
 *
 * @author wenqiang.yin@hand-china.com 2021/01/27 13:26
 */
public interface HmeCosInProductionService {

    /**
     * COS在制报表 导出
     *
     * @param tenantId
     * @param dto
     * @param exportParam
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.reports.domain.vo.HmeCosInProductionVO>
     * @auther wenqiang.yin@hand-china.com 2021/1/27 16:07
    */
    Page<HmeCosInProductionVO> export(Long tenantId,
                                      HmeCosInProductionDTO dto,
                                      ExportParam exportParam,
                                      PageRequest pageRequest);
}
