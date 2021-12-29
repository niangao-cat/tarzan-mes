package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeProcessGatherResultReportDto;
import com.ruike.hme.domain.repository.HmeReportRepository;
import com.ruike.hme.domain.vo.HmeProcessGatherResultReportVO;
import com.ruike.hme.infra.mapper.HmeReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * HmeReportRepositoryImpl
 *
 * @author: chaonan.hu@hand-china.com 2021-03-22 10:06:12
 **/
@Component
public class HmeReportRepositoryImpl implements HmeReportRepository {

    @Autowired
    private HmeReportMapper hmeReportMapper;

    @Override
    public Page<HmeProcessGatherResultReportVO> processGatherResultReportQuery(Long tenantId, HmeProcessGatherResultReportDto dto, PageRequest pageRequest) {
        Page<HmeProcessGatherResultReportVO> resultPage = PageHelper.doPage(pageRequest, ()->hmeReportMapper.processGatherResultReportQuery(tenantId, dto));
        return resultPage;
    }
}
