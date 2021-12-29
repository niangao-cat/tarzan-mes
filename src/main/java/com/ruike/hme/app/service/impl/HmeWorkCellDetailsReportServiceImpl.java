package com.ruike.hme.app.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ruike.hme.app.service.HmeSnBindEoService;
import com.ruike.hme.app.service.HmeWorkCellDetailsReportService;
import com.ruike.hme.domain.repository.HmeSnBindEoRepository;
import com.ruike.hme.domain.repository.HmeWorkCellDetailsReportRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 工位产量明细报表
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/08 14:43
 */
@Service
public class HmeWorkCellDetailsReportServiceImpl implements HmeWorkCellDetailsReportService {

    @Autowired
    private HmeWorkCellDetailsReportRepository hmeWorkCellDetailsReportRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;

    private static final int DAY_90 = 90;
    private static final int DAY_30 = 30;

    @Override
    public Page<HmeWorkCellDetailsReportVO2> listForUi(Long tenantId, HmeWorkCellDetailsReportVO reportVO, PageRequest pageRequest) {
        //只传结束时间 提示选择开始时间
        if (StringUtils.isBlank(reportVO.getStartTime()) && StringUtils.isNotBlank(reportVO.getEndTime())) {
            throw new MtException("HME_EQUIPMENT_HIS_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_HIS_002", "HME"));
        }

        if (StringUtils.isNotBlank(reportVO.getEndTime()) && StringUtils.isNotBlank(reportVO.getStartTime())) {
            //开始时间不能大于结束时间
            DateTime startTime = DateUtil.parse(reportVO.getStartTime(), HmeConstants.ConstantValue.DATE_TIME_FORMAT);
            DateTime endTime = DateUtil.parse(reportVO.getEndTime(), HmeConstants.ConstantValue.DATE_TIME_FORMAT);
            int compare = DateUtil.compare(startTime, endTime);
            if (compare > 0) {
                throw new MtException("HME_EQUIPMENT_HIS_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_HIS_001", "HME"));
            }

            //间隔不能超过30天
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            long timeInMillis1 = calendar.getTimeInMillis();
            calendar.setTime(endTime);
            long timeInMillis2 = calendar.getTimeInMillis();

            long betweenDays = (timeInMillis2 - timeInMillis1) / (1000L * 3600L * 24L);
            if (betweenDays > DAY_30) {
                throw new MtException("HME_EQUIPMENT_HIS_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_HIS_003", "HME"));
            }
        }

        //只传开始时间 与当前时间比较 间隔不能超过30天
        if (StringUtils.isNotBlank(reportVO.getStartTime()) && StringUtils.isBlank(reportVO.getEndTime())) {
            DateTime startTime = DateUtil.parse(reportVO.getStartTime(), HmeConstants.ConstantValue.DATE_TIME_FORMAT);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            long timeInMillis1 = calendar.getTimeInMillis();
            calendar.setTime(new Date());
            long timeInMillis2 = calendar.getTimeInMillis();

            long betweenDays = (timeInMillis2 - timeInMillis1) / (1000L * 3600L * 24L);
            if (betweenDays > DAY_30) {
                throw new MtException("HME_EQUIPMENT_HIS_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_HIS_003", "HME"));
            }
        }

        //没传时间 取前一个月的数据
        if (StringUtils.isBlank(reportVO.getStartTime()) && StringUtils.isBlank(reportVO.getEndTime())) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            reportVO.setStartTime(DateUtil.format(calendar.getTime(), HmeConstants.ConstantValue.DATE_TIME_FORMAT));
        }

        return hmeWorkCellDetailsReportRepository.queryWorkCellReportList(tenantId, reportVO, pageRequest);
    }

    @Override
    public Page<HmeWorkCellVO> workCellUiQuery(Long tenantId, HmeWorkCellVO hmeWorkCellVO, PageRequest pageRequest) {
        return hmeWorkCellDetailsReportRepository.workCellUiQuery(tenantId,hmeWorkCellVO,pageRequest);
    }

    @Override
    public Page<HmeProcessReportVo2> queryProcessReportList(Long tenantId, HmeProcessReportVo reportVO, PageRequest pageRequest) {
        return hmeWorkCellDetailsReportRepository.queryProcessReportList(tenantId,reportVO,pageRequest);
    }

    @Override
    public void queryProcessReportExport(Long tenantId, HmeProcessReportVo reportVO, HttpServletResponse response) {
        hmeWorkCellDetailsReportRepository.queryProcessReportExport(tenantId, reportVO, response);
    }

    @Override
    public Page<HmeExceptionReportVO2> queryExceptionReportList(Long tenantId, HmeExceptionReportVO reportVO, PageRequest pageRequest) {
        //时间必输
        if (StringUtils.isBlank(reportVO.getStartTime()) && StringUtils.isBlank(reportVO.getEndTime())) {
            throw new MtException("HME_EXCEPTION_REP_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_REP_001", "HME"));
        }
        // 隔间不大于90天
        DateTime startTime = DateUtil.parse(reportVO.getStartTime(), HmeConstants.ConstantValue.DATE_TIME_FORMAT);
        DateTime endTime = DateUtil.parse(reportVO.getEndTime(), HmeConstants.ConstantValue.DATE_TIME_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        long timeInMillis1 = calendar.getTimeInMillis();
        calendar.setTime(endTime);
        long timeInMillis2 = calendar.getTimeInMillis();

        long betweenDays = (timeInMillis2 - timeInMillis1) / (1000L * 3600L * 24L);
        if (betweenDays > DAY_90) {
            throw new MtException("HME_EXCEPTION_REP_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_REP_002", "HME"));
        }
        return hmeWorkCellDetailsReportRepository.queryExceptionReportList(tenantId,reportVO,pageRequest);
    }
}
