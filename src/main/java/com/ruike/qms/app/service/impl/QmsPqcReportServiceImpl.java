package com.ruike.qms.app.service.impl;

import com.ruike.qms.api.dto.QmsPqcReportDTO;
import com.ruike.qms.app.service.QmsPqcReportService;
import com.ruike.qms.domain.repository.QmsPqcReportRepository;
import com.ruike.qms.domain.vo.*;
import com.ruike.qms.infra.mapper.QmsPqcReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 巡检报表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020/12/11 15:13:23
 */
@Service
public class QmsPqcReportServiceImpl implements QmsPqcReportService {

    @Autowired
    private QmsPqcReportRepository qmsPqcReportRepository;
    @Autowired
    private QmsPqcReportMapper qmsPqcReportMapper;
    @Autowired
    private MtUserRepository mtUserRepository;

    @Override
    public Page<QmsPqcReportVO> pqcReportHeadDataQuery(Long tenantId, QmsPqcReportDTO dto, PageRequest pageRequest) {
        //日期转换
        if(StringUtils.isNotEmpty(dto.getInspectionFinishFromDateStr())){
            String inspectionFinishFromDateStr = dto.getInspectionFinishFromDateStr() + " 00:00:00";
            Date inspectionFinishFromDate = DateUtil.string2Date(inspectionFinishFromDateStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishFromDate(inspectionFinishFromDate);
        }
        if(StringUtils.isNotEmpty(dto.getInspectionFinishToDateStr())){
            String inspectionFinishToDateStr = dto.getInspectionFinishToDateStr() + " 23:59:59";
            Date inspectionFinishToDate = DateUtil.string2Date(inspectionFinishToDateStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishToDate(inspectionFinishToDate);
        }
        return qmsPqcReportRepository.pqcReportHeadDataQuery(tenantId, dto, pageRequest);
    }

    @Override
    public Page<QmsPqcReportVO2> pgcReportDetailDataQuery(Long tenantId, QmsPqcReportDTO dto, PageRequest pageRequest) {
        //日期转换
        if(StringUtils.isNotEmpty(dto.getInspectionFinishFromDateStr())){
            String inspectionFinishFromDateStr = dto.getInspectionFinishFromDateStr() + " 00:00:00";
            Date inspectionFinishFromDate = DateUtil.string2Date(inspectionFinishFromDateStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishFromDate(inspectionFinishFromDate);
        }
        if(StringUtils.isNotEmpty(dto.getInspectionFinishToDateStr())){
            String inspectionFinishToDateStr = dto.getInspectionFinishToDateStr() + " 23:59:59";
            Date inspectionFinishToDate = DateUtil.string2Date(inspectionFinishToDateStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishToDate(inspectionFinishToDate);
        }
        return qmsPqcReportRepository.pgcReportDetailDataQuery(tenantId, dto, pageRequest);
    }

    @Override
    public List<QmsPqcReportVO3> pqcReportHeadDataExportByDepartment(Long tenantId, QmsPqcReportDTO dto) {
        //日期转换
        if(StringUtils.isNotEmpty(dto.getInspectionFinishFromDateStr())){
            String inspectionFinishFromDateStr = dto.getInspectionFinishFromDateStr() + " 00:00:00";
            Date inspectionFinishFromDate = DateUtil.string2Date(inspectionFinishFromDateStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishFromDate(inspectionFinishFromDate);
        }
        if(StringUtils.isNotEmpty(dto.getInspectionFinishToDateStr())){
            String inspectionFinishToDateStr = dto.getInspectionFinishToDateStr() + " 23:59:59";
            Date inspectionFinishToDate = DateUtil.string2Date(inspectionFinishToDateStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishToDate(inspectionFinishToDate);
        }
        return qmsPqcReportMapper.pqcReportHeadDataExportByDepartment(tenantId, dto);
    }

    @Override
    public List<QmsPqcReportVO4> pqcReportHeadDataExportByWorkshop(Long tenantId, QmsPqcReportDTO dto) {
        //日期转换
        if(StringUtils.isNotEmpty(dto.getInspectionFinishFromDateStr())){
            String inspectionFinishFromDateStr = dto.getInspectionFinishFromDateStr() + " 00:00:00";
            Date inspectionFinishFromDate = DateUtil.string2Date(inspectionFinishFromDateStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishFromDate(inspectionFinishFromDate);
        }
        if(StringUtils.isNotEmpty(dto.getInspectionFinishToDateStr())){
            String inspectionFinishToDateStr = dto.getInspectionFinishToDateStr() + " 23:59:59";
            Date inspectionFinishToDate = DateUtil.string2Date(inspectionFinishToDateStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishToDate(inspectionFinishToDate);
        }
        return qmsPqcReportMapper.pqcReportHeadDataExportByWorkshop(tenantId, dto);
    }

    @Override
    public List<QmsPqcReportVO5> pgcReportDetailDataExport(Long tenantId, QmsPqcReportDTO dto) {
        //日期转换
        if(StringUtils.isNotEmpty(dto.getInspectionFinishFromDateStr())){
            String inspectionFinishFromDateStr = dto.getInspectionFinishFromDateStr() + " 00:00:00";
            Date inspectionFinishFromDate = DateUtil.string2Date(inspectionFinishFromDateStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishFromDate(inspectionFinishFromDate);
        }
        if(StringUtils.isNotEmpty(dto.getInspectionFinishToDateStr())){
            String inspectionFinishToDateStr = dto.getInspectionFinishToDateStr() + " 23:59:59";
            Date inspectionFinishToDate = DateUtil.string2Date(inspectionFinishToDateStr, "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishToDate(inspectionFinishToDate);
        }
        List<QmsPqcReportVO5> qmsPqcReportVO5s = qmsPqcReportMapper.pgcReportDetailDataExport(tenantId, dto);
        for (QmsPqcReportVO5 qmsPqcReportVO5:qmsPqcReportVO5s) {
            String inspectionFinishDateStr = DateUtil.date2String(qmsPqcReportVO5.getInspectionFinishDate(), "yyyy-MM-dd HH:mm:ss");
            qmsPqcReportVO5.setInspectionFinishDateStr(inspectionFinishDateStr);
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, qmsPqcReportVO5.getLastUpdatedBy());
            qmsPqcReportVO5.setLastUpdatedByName(mtUserInfo.getRealName());
        }
        return qmsPqcReportVO5s;
    }
}
