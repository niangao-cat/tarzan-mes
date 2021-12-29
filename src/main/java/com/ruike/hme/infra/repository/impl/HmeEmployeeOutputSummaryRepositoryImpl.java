package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeWoTrialCalculate;
import com.ruike.hme.infra.mapper.HmeEmployeeOutputSummaryMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeEmployeeOutputSummary;
import com.ruike.hme.domain.repository.HmeEmployeeOutputSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 员工产量汇总表 资源库实现
 *
 * @author penglin.sui@hand-china.com 2021-07-28 11:19:48
 */
@Component
public class HmeEmployeeOutputSummaryRepositoryImpl extends BaseRepositoryImpl<HmeEmployeeOutputSummary> implements HmeEmployeeOutputSummaryRepository {

    @Autowired
    private HmeEmployeeOutputSummaryMapper hmeEmployeeOutputSummaryMapper;

    @Override
    public List<HmeEmployeeOutputSummary> selectDataOfSignIn(Long tenantId, String startTime, String endTime) {
        List<HmeEmployeeOutputSummary> resultVOList = hmeEmployeeOutputSummaryMapper.selectDataOfSignIn(tenantId,startTime,endTime);
        return resultVOList;
    }

    @Override
    public List<HmeEmployeeOutputSummary> selectDataOfSignOut(Long tenantId, String startTime, String endTime) {
        List<HmeEmployeeOutputSummary> resultVOList = hmeEmployeeOutputSummaryMapper.selectDataOfSignOut(tenantId,startTime,endTime);
        return resultVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void myBatchInsert(List<HmeEmployeeOutputSummary> insertList) {
        if (CollectionUtils.isNotEmpty(insertList)) {
            List<List<HmeEmployeeOutputSummary>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeEmployeeOutputSummary> domains : splitSqlList) {
                hmeEmployeeOutputSummaryMapper.batchInsert(domains);
            }
        }
    }

    @Override
    public void myBatchDelete(List<String> outSummaryIdList) {
        if (CollectionUtils.isNotEmpty(outSummaryIdList)) {
            List<List<String>> splitSqlList = InterfaceUtils.splitSqlList(outSummaryIdList, SQL_ITEM_COUNT_LIMIT);
            for (List<String> domains : splitSqlList) {
                hmeEmployeeOutputSummaryMapper.batchDelete(domains);
            }
        }
    }
}
