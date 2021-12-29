package com.ruike.qms.infra.repository.impl;

import com.ruike.qms.api.dto.QmsPqcReportDTO;
import com.ruike.qms.domain.repository.QmsPqcReportRepository;
import com.ruike.qms.domain.vo.QmsPqcReportVO;
import com.ruike.qms.domain.vo.QmsPqcReportVO2;
import com.ruike.qms.infra.mapper.QmsPqcReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 巡检报表资源库默认实现
 *
 * @author chaonan.hu@hand-china.com 2020/12/11 15:13:23
 */
@Component
public class QmsPqcReportRepositoryImpl implements QmsPqcReportRepository {

    @Autowired
    private QmsPqcReportMapper qmsPqcReportMapper;
    @Autowired
    private MtUserRepository mtUserRepository;

    @Override
    public Page<QmsPqcReportVO> pqcReportHeadDataQuery(Long tenantId, QmsPqcReportDTO dto, PageRequest pageRequest) {
        Page<QmsPqcReportVO> resultPage = new Page<>();
        if(StringUtils.isEmpty(dto.getWorkshopId())){
            resultPage = PageHelper.doPage(pageRequest, () -> qmsPqcReportMapper.pqcReportHeadDataQueryByDepartment(tenantId, dto));
        }else{
            resultPage = PageHelper.doPage(pageRequest, () -> qmsPqcReportMapper.pqcReportHeadDataQueryByWorkshop(tenantId, dto));
        }
        return resultPage;
    }

    @Override
    public Page<QmsPqcReportVO2> pgcReportDetailDataQuery(Long tenantId, QmsPqcReportDTO dto, PageRequest pageRequest) {
        Page<QmsPqcReportVO2> resultPage = PageHelper.doPage(pageRequest, () -> qmsPqcReportMapper.pgcReportDetailDataQuery(tenantId, dto));
        for (QmsPqcReportVO2 qmsPqcReportVO2:resultPage.getContent()) {
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, qmsPqcReportVO2.getLastUpdatedBy());
            qmsPqcReportVO2.setLastUpdatedByName(mtUserInfo.getRealName());
        }
        return resultPage;
    }
}
