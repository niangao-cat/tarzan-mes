package com.ruike.reports.app.service.impl;

import com.ruike.reports.api.dto.HmeCosInProductionDTO;
import com.ruike.reports.app.service.HmeCosInProductionService;
import com.ruike.reports.domain.repository.HmeCosInProductionRepository;
import com.ruike.reports.domain.vo.HmeCosInProductionVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * COS在制报表 服务实现
 *
 * @author wenqiang.yin@hand-china.com 2021/01/27 13:26
 */
@Service
public class HmeCosInProductionServiceImpl implements HmeCosInProductionService {

    @Autowired
    private HmeCosInProductionRepository hmeCosInProductionRepository;

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
    @Override
    public Page<HmeCosInProductionVO> export(Long tenantId, HmeCosInProductionDTO dto, ExportParam exportParam, PageRequest pageRequest) {
        return hmeCosInProductionRepository.pageList(tenantId, dto, pageRequest);
    }
}
