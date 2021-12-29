package com.ruike.qms.app.service.impl;

import com.ruike.qms.api.dto.QmsIqcExamineReportDTO;
import com.ruike.qms.app.service.QmsIqcExamineReportService;
import com.ruike.qms.domain.repository.QmsIqcExamineReportRepository;
import com.ruike.qms.domain.vo.QmsIqcExamineReportVO;
import com.ruike.qms.domain.vo.QmsIqcExamineReportVO2;
import com.ruike.qms.domain.vo.QmsIqcExamineReportVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * ICQ检验报表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020-12-10 09:56:23
 */
@Service
public class QmsIqcExamineReportServiceImpl implements QmsIqcExamineReportService {

    @Autowired
    private QmsIqcExamineReportRepository qmsIqcExamineReportRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public Page<QmsIqcExamineReportVO> iqcExamineReportQuery(Long tenantId, QmsIqcExamineReportDTO dto, PageRequest pageRequest) {
        //日期转换
//        if(StringUtils.isNotEmpty(dto.getInspectionStartDateFromStr())){
//            String inspectionStartDateFromStr = dto.getInspectionStartDateFromStr() + " 00:00:00";
//            Date inspectionStartDateFrom = DateUtil.string2Date(inspectionStartDateFromStr, "yyyy-MM-dd HH:mm:ss");
//            dto.setInspectionStartDateFrom(inspectionStartDateFrom);
//        }
//        if(StringUtils.isNotEmpty(dto.getInspectionStartDateToStr())){
//            String inspectionStartDateToStr = dto.getInspectionStartDateToStr() + " 23:59:59";
//            Date inspectionStartDateTo = DateUtil.string2Date(inspectionStartDateToStr, "yyyy-MM-dd HH:mm:ss");
//            dto.setInspectionStartDateTo(inspectionStartDateTo);
//        }
        if(StringUtils.isNotEmpty(dto.getInspectionFinishDateFromStr())){
            String inspectionFinishDateFromStr = dto.getInspectionFinishDateFromStr() + " 00:00:00";
            Date inspectionFinishDateFrom = DateUtil.string2Date(inspectionFinishDateFromStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishDateFrom(inspectionFinishDateFrom);
        }
        if(StringUtils.isNotEmpty(dto.getInspectionFinishDateToStr())){
            String inspectionFinishDateToStr = dto.getInspectionFinishDateToStr() + " 23:59:59";
            Date inspectionFinishDateTo = DateUtil.string2Date(inspectionFinishDateToStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishDateTo(inspectionFinishDateTo);
        }
        return qmsIqcExamineReportRepository.iqcExamineReportQuery(tenantId, dto, pageRequest);
    }

    @Override
    public QmsIqcExamineReportVO2 iqcExaminePieChartQuery(Long tenantId, QmsIqcExamineReportDTO dto) {
        //日期转换
        if(StringUtils.isNotEmpty(dto.getInspectionFinishDateFromStr())){
            String inspectionFinishDateFromStr = dto.getInspectionFinishDateFromStr() + " 00:00:00";
            Date inspectionFinishDateFrom = DateUtil.string2Date(inspectionFinishDateFromStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishDateFrom(inspectionFinishDateFrom);
        }
        if(StringUtils.isNotEmpty(dto.getInspectionFinishDateToStr())){
            String inspectionFinishDateToStr = dto.getInspectionFinishDateToStr() + " 23:59:59";
            Date inspectionFinishDateTo = DateUtil.string2Date(inspectionFinishDateToStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishDateTo(inspectionFinishDateTo);
        }
        return qmsIqcExamineReportRepository.iqcExaminePieChartQuery(tenantId, dto);
    }

    @Override
    public QmsIqcExamineReportVO3 iqcExamineLineChartQuery(Long tenantId, QmsIqcExamineReportDTO dto) {
        if(StringUtils.isEmpty(dto.getInspectionFinishDateFromStr())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "检验时间从"));
        }
        if(StringUtils.isEmpty(dto.getInspectionFinishDateToStr())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "检验时间至"));
        }
        Date inspectionFinishDateFrom = DateUtil.string2Date(dto.getInspectionFinishDateFromStr(), "yyyy-MM-dd");
        Date inspectionFinishDateTo = DateUtil.string2Date(dto.getInspectionFinishDateToStr(), "yyyy-MM-dd");
        dto.setInspectionFinishDateFrom(inspectionFinishDateFrom);
        dto.setInspectionFinishDateTo(inspectionFinishDateTo);
        return qmsIqcExamineReportRepository.iqcExamineLineChartQuery(tenantId, dto);
    }
}
