package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsLibraryAgeReportDTO;
import com.ruike.wms.api.dto.WmsLibraryAgeReportDTO2;
import com.ruike.wms.app.service.WmsLibraryAgeReportService;
import com.ruike.wms.domain.repository.WmsLibraryAgeReportRepository;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO2;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO4;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 库龄报表应用服务默认实现
 *
 * @author: chaonan.hu@hand-china.com 2020-11-18 14:24:34
 **/
@Service
public class WmsLibraryAgeReportServiceImpl implements WmsLibraryAgeReportService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private WmsLibraryAgeReportRepository wmsLibraryAgeReportRepository;

    @Override
    public Page<WmsLibraryAgeReportVO> libraryAgeReportQuery(Long tenantId, WmsLibraryAgeReportDTO dto, PageRequest pageRequest) {
        if(StringUtils.isEmpty(dto.getSiteId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工厂"));
        }
        return wmsLibraryAgeReportRepository.libraryAgeReportQuery(tenantId, dto, pageRequest);
    }

    @Override
    public Page<WmsLibraryAgeReportVO2> libraryAgeGroupQuery(Long tenantId, WmsLibraryAgeReportDTO2 dto, PageRequest pageRequest) {
        if(StringUtils.isEmpty(dto.getSiteId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工厂"));
        }
        if(CollectionUtils.isEmpty(dto.getLibraryAgeList())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "库龄区间"));
        }
        return wmsLibraryAgeReportRepository.libraryAgeGroupQuery(tenantId, dto, pageRequest);
    }

    @Override
    public List<WmsLibraryAgeReportVO4> libraryAgeExport(Long tenantId, WmsLibraryAgeReportDTO dto) {
        if(StringUtils.isEmpty(dto.getSiteId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工厂"));
        }
        return wmsLibraryAgeReportRepository.libraryAgeExport(tenantId, dto);
    }
}
