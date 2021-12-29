package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.repository.HmeCommonReportRepository;
import com.ruike.hme.domain.vo.HmeNonStandardDetailsVO;
import com.ruike.hme.domain.vo.HmeNonStandardReportVO;
import com.ruike.hme.domain.vo.HmeNonStandardReportVO2;
import com.ruike.hme.infra.mapper.HmeCommonReportMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/14 16:51
 */
@Component
public class HmeCommonReportRepositoryImpl implements HmeCommonReportRepository {

    @Autowired
    private HmeCommonReportMapper hmeCommonReportMapper;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Override
    @ProcessLovValue
    public Page<HmeNonStandardReportVO2> nonStandardProductReportQuery(Long tenantId, HmeNonStandardReportVO dto, PageRequest pageRequest) {
        Page<HmeNonStandardReportVO2> reportPage = PageHelper.doPage(pageRequest, () -> hmeCommonReportMapper.nonStandardProductReportQuery(tenantId, dto));
        return reportPage;
    }

    @Override
    public Page<HmeNonStandardDetailsVO> waitQtyDetailsQuery(Long tenantId, String workOrderId, PageRequest pageRequest) {
        Page<HmeNonStandardDetailsVO> reportPage = PageHelper.doPage(pageRequest, () -> hmeCommonReportMapper.waitQtyDetailsQuery(tenantId, workOrderId));
        return reportPage;
    }

    @Override
    public Page<HmeNonStandardDetailsVO> onlineQtyDetailsQuery(Long tenantId, String workOrderId, PageRequest pageRequest) {
        //用户默认站点
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        Page<HmeNonStandardDetailsVO> reportPage = PageHelper.doPage(pageRequest, () -> hmeCommonReportMapper.onlineQtyDetailsQuery(tenantId, workOrderId, defaultSiteId));
        return reportPage;
    }

    @Override
    public Page<HmeNonStandardDetailsVO> completedQtyDetailsQuery(Long tenantId, String workOrderId, PageRequest pageRequest) {
        Page<HmeNonStandardDetailsVO> reportPage = PageHelper.doPage(pageRequest, () -> hmeCommonReportMapper.completedQtyDetailsQuery(tenantId, workOrderId));
        return reportPage;
    }
}
