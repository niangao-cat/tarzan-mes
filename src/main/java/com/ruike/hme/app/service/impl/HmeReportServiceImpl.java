package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeProcessGatherResultReportDto;
import com.ruike.hme.app.service.HmeReportService;
import com.ruike.hme.domain.repository.HmeReportRepository;
import com.ruike.hme.domain.vo.HmeProcessGatherResultReportVO;
import com.ruike.hme.domain.vo.HmeProcessGatherResultReportVO2;
import com.ruike.hme.infra.mapper.HmeReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * HmeReportServiceImpl
 *
 * @author: chaonan.hu@hand-china.com 2021-03-22 10:04:11
 **/
@Service
public class HmeReportServiceImpl implements HmeReportService {

    @Autowired
    private HmeReportRepository hmeReportRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeReportMapper hmeReportMapper;

    @Override
    public Page<HmeProcessGatherResultReportVO> processGatherResultReportQuery(Long tenantId, HmeProcessGatherResultReportDto dto, PageRequest pageRequest) {
        if(StringUtils.isEmpty(dto.getSiteId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "站点"));
        }
        if(Objects.isNull(dto.getGatherDateFrom())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "采集时间从"));
        }
        if(Objects.isNull(dto.getGatherDateTo())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "采集时间至"));
        }
        return hmeReportRepository.processGatherResultReportQuery(tenantId, dto, pageRequest);
    }

    @Override
    public List<HmeProcessGatherResultReportVO2> processGatherResultReportExport(Long tenantId, HmeProcessGatherResultReportDto dto) {
        if(StringUtils.isEmpty(dto.getSiteId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "站点"));
        }
        if(Objects.isNull(dto.getGatherDateFrom())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "采集时间从"));
        }
        if(Objects.isNull(dto.getGatherDateTo())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "采集时间至"));
        }
        return hmeReportMapper.processGatherResultReportExport(tenantId, dto);
    }
}
