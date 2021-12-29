package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeEmployeeOutputSummary;
import com.ruike.hme.infra.mapper.HmeEmployeeOutputSummaryTimeMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeEmployeeOutputSummaryTime;
import com.ruike.hme.domain.repository.HmeEmployeeOutputSummaryTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 员工产量汇总时间表 资源库实现
 *
 * @author penglin.sui@hand-china.com 2021-07-28 15:31:45
 */
@Component
public class HmeEmployeeOutputSummaryTimeRepositoryImpl extends BaseRepositoryImpl<HmeEmployeeOutputSummaryTime> implements HmeEmployeeOutputSummaryTimeRepository {

    @Autowired
    private HmeEmployeeOutputSummaryTimeMapper hmeEmployeeOutputSummaryTimeMapper;

    @Override
    public Date selectMaxJobTime(Long tenantId) {
        Date preMaxJobTime = hmeEmployeeOutputSummaryTimeMapper.selectMaxJobTime(tenantId);
        return preMaxJobTime;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void myBatchInsert(List<HmeEmployeeOutputSummaryTime> insertList) {
        if (CollectionUtils.isNotEmpty(insertList)) {
            List<List<HmeEmployeeOutputSummaryTime>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeEmployeeOutputSummaryTime> domains : splitSqlList) {
                hmeEmployeeOutputSummaryTimeMapper.batchInsert(domains);
            }
        }
    }
}
