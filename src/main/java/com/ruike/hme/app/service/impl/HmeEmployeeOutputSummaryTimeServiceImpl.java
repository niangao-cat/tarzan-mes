package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeEmployeeOutputSummaryTimeService;
import com.ruike.hme.domain.entity.HmeEmployeeOutputSummaryTime;
import com.ruike.hme.domain.repository.HmeEmployeeOutputSummaryTimeRepository;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.sys.CustomSequence;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import java.util.*;

/**
 * 员工产量汇总时间表应用服务默认实现
 *
 * @author penglin.sui@hand-china.com 2021-07-28 15:31:45
 */
@Service
@Slf4j
public class HmeEmployeeOutputSummaryTimeServiceImpl implements HmeEmployeeOutputSummaryTimeService {

    @Autowired
    private HmeEmployeeOutputSummaryTimeRepository hmeEmployeeOutputSummaryTimeRepository;

    @Autowired
    private CustomSequence customSequence;

    @Override
    public Date selectMaxJobTime(Long tenantId) {
        return hmeEmployeeOutputSummaryTimeRepository.selectMaxJobTime(tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInserData(Long tenantId, Date startTime, Date endTime) {
        List<HmeEmployeeOutputSummaryTime> employeeOutputSummaryTimeList = new ArrayList<>();

        int diff = CommonUtils.differentByMillisecond(startTime , endTime , Calendar.HOUR_OF_DAY,true);

        List<String> outputSummaryTimeIdList = customSequence.getNextKeys("hme_employee_output_summary_time_s", diff);
        List<String> outputSummaryTimeCidList = customSequence.getNextKeys("hme_employee_output_summary_time_cid_s", 1);
        Long cid = Long.valueOf(outputSummaryTimeCidList.get(0));

        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }

        //获取当前时间
        Date now = CommonUtils.currentTimeGet();

        for(int i = 0 ; i < diff ; i++){
            HmeEmployeeOutputSummaryTime employeeOutputSummaryTime = new HmeEmployeeOutputSummaryTime();
            //租户ID
            employeeOutputSummaryTime.setTenantId(tenantId);

            //主键
            employeeOutputSummaryTime.setOutputSummaryTimeId(outputSummaryTimeIdList.get(i));

            //时间
            employeeOutputSummaryTime.setJobTime(CommonUtils.calculateDate(startTime , i , Calendar.HOUR_OF_DAY , "yyyy-MM-dd HH"));

            log.info("<=========HmeEmployeeOutputSummaryTimeServiceImpl.batchInserData=========>" + CommonUtils.dateToString(employeeOutputSummaryTime.getJobTime() , "yyyy-MM-dd HH"));

            //CID
            employeeOutputSummaryTime.setCid(cid);

            //版本
            employeeOutputSummaryTime.setObjectVersionNumber(1L);

            //WHO字段
            employeeOutputSummaryTime.setCreatedBy(userId);
            employeeOutputSummaryTime.setCreationDate(now);
            employeeOutputSummaryTime.setLastUpdatedBy(userId);
            employeeOutputSummaryTime.setLastUpdateDate(now);

            employeeOutputSummaryTimeList.add(employeeOutputSummaryTime);
        }
        if(CollectionUtils.isEmpty(employeeOutputSummaryTimeList)){
            return;
        }

        hmeEmployeeOutputSummaryTimeRepository.myBatchInsert(employeeOutputSummaryTimeList);
    }
}
