package com.ruike.qms.infra.repository.impl;

import com.ruike.qms.api.dto.QmsIqcExamineReportDTO;
import com.ruike.qms.domain.repository.QmsIqcExamineReportRepository;
import com.ruike.qms.domain.vo.QmsIqcExamineReportVO;
import com.ruike.qms.domain.vo.QmsIqcExamineReportVO2;
import com.ruike.qms.domain.vo.QmsIqcExamineReportVO3;
import com.ruike.qms.infra.mapper.QmsIqcExamineReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * ICQ检验报表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2020-12-10 09:56:23
 */
@Component
public class QmsIqcExamineReportRepositoryImpl implements QmsIqcExamineReportRepository {

    @Autowired
    private QmsIqcExamineReportMapper qmsIqcExamineReportMapper;
    @Autowired
    private MtUserRepository mtUserRepository;

    @Override
    @ProcessLovValue
    public Page<QmsIqcExamineReportVO> iqcExamineReportQuery(Long tenantId, QmsIqcExamineReportDTO dto, PageRequest pageRequest) {
        Page<QmsIqcExamineReportVO> resultPage = PageHelper.doPage(pageRequest, () -> qmsIqcExamineReportMapper.iqcExamineReportQuery(tenantId, dto));
        for (QmsIqcExamineReportVO qmsIqcExamineReportVO:resultPage.getContent()) {
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, qmsIqcExamineReportVO.getLastUpdatedBy());
            qmsIqcExamineReportVO.setLastUpdatedByName(mtUserInfo.getRealName());
        }
        return resultPage;
    }

    @Override
    public QmsIqcExamineReportVO2 iqcExaminePieChartQuery(Long tenantId, QmsIqcExamineReportDTO dto) {
        QmsIqcExamineReportVO2 qmsIqcExamineReportVO2 = new QmsIqcExamineReportVO2();
        BigDecimal totalNum = qmsIqcExamineReportMapper.totalNumQuery(tenantId, dto);
        BigDecimal okNum = BigDecimal.ZERO;
        BigDecimal ngNum = BigDecimal.ZERO;
        if(StringUtils.isBlank(dto.getInspectionResult())){
            okNum = qmsIqcExamineReportMapper.okNumQuery(tenantId, dto);
            ngNum = qmsIqcExamineReportMapper.ngNumQuery(tenantId, dto);
        }else if("OK".equals(dto.getInspectionResult())){
            okNum = qmsIqcExamineReportMapper.okNumQuery(tenantId, dto);
        }else if("NG".equals(dto.getInspectionResult())){
            ngNum = qmsIqcExamineReportMapper.ngNumQuery(tenantId, dto);
        }
        qmsIqcExamineReportVO2.setTotalNum(totalNum);
        qmsIqcExamineReportVO2.setOkNum(okNum);
        qmsIqcExamineReportVO2.setNgNum(ngNum);
        if(totalNum.compareTo(BigDecimal.ZERO) == 1){
            BigDecimal okProbability = okNum.divide(totalNum, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            qmsIqcExamineReportVO2.setOkProbability(okProbability);
            BigDecimal ngProbability = ngNum.divide(totalNum, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            qmsIqcExamineReportVO2.setNgProbability(ngProbability);
        }else{
            qmsIqcExamineReportVO2.setOkProbability(BigDecimal.ZERO);
            qmsIqcExamineReportVO2.setNgProbability(BigDecimal.ZERO);
        }
        return qmsIqcExamineReportVO2;
    }

    @Override
    public QmsIqcExamineReportVO3 iqcExamineLineChartQuery(Long tenantId, QmsIqcExamineReportDTO dto) {
        QmsIqcExamineReportVO3 result = new QmsIqcExamineReportVO3();
        List<String> xDataList = new ArrayList<>();
        List<BigDecimal> yDataList = new ArrayList<>();
        Date inspectionFinishDateFrom = dto.getInspectionFinishDateFrom();
        Date inspectionFinishDateTo = dto.getInspectionFinishDateTo();
        do{
            //x轴数据
            String xData = DateUtil.date2String(inspectionFinishDateFrom, "yyyy/MM/dd");
            xDataList.add(xData);
            //本次循环起始时间
            dto.setInspectionFinishDateFrom(inspectionFinishDateFrom);
            //本次循环截止时间
            String dateStr = DateUtil.date2String(inspectionFinishDateFrom, "yyyy-MM-dd");
            Date date = DateUtil.string2Date(dateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            dto.setInspectionFinishDateTo(date);
            //y轴数据
            BigDecimal yData = qmsIqcExamineReportMapper.totalNumQuery(tenantId, dto);
            yDataList.add(yData);
            //循环遍历+1天
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(inspectionFinishDateFrom);
            calendar.add(Calendar.DATE, 1);
            inspectionFinishDateFrom = calendar.getTime();
        }while (inspectionFinishDateFrom.compareTo(inspectionFinishDateTo) <= 0);
        result.setXDataList(xDataList);
        result.setYDataList(yDataList);
        return result;
    }
}
