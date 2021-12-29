package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeWoTrialCalculateReportQueryDTO;
import com.ruike.hme.domain.entity.HmeWoTrialCalculate;
import com.ruike.hme.domain.repository.HmeWoTrialCalculateRepository;
import com.ruike.hme.domain.vo.HmeWoCalendarShiftVO3;
import com.ruike.hme.domain.vo.HmeWoTrialCalculateReportWoVO;
import com.ruike.hme.infra.mapper.HmeWoTrialCalculateMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.*;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 工单入库日期试算表 资源库实现
 *
 * @author yuchao.wang@hand-china.com 2020-08-25 21:54:56
 */
@Component
public class HmeWoTrialCalculateRepositoryImpl extends BaseRepositoryImpl<HmeWoTrialCalculate> implements HmeWoTrialCalculateRepository {

    @Autowired
    private HmeWoTrialCalculateMapper hmeWoTrialCalculateMapper;

    /**
     *
     * @Description 查询试算报表基础数据
     *
     * @author yuchao.wang
     * @date 2020/8/27 10:10
     * @param tenantId 租户ID
     * @param queryDTO 参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWoTrialCalculateReportWoVO>
     *
     */
    @Override
    public List<HmeWoTrialCalculateReportWoVO> queryTrialCalculateReport(Long tenantId, HmeWoTrialCalculateReportQueryDTO queryDTO) {
        return hmeWoTrialCalculateMapper.queryTrialCalculateReport(tenantId, queryDTO);
    }

    /**
     *
     * @Description 查询时间区间内的工作日历天
     *
     * @author yuchao.wang
     * @date 2020/8/27 10:10
     * @param tenantId 租户ID
     * @param vo 参数
     * @return java.util.List<java.time.LocalDate>
     *
     */
    @Override
    public List<LocalDate> queryCalendarShiftByTime(Long tenantId, HmeWoCalendarShiftVO3 vo) {
        return hmeWoTrialCalculateMapper.queryCalendarShiftByTime(tenantId, vo);
    }

    /**
     *
     * @Description 根据工单ID删除数据
     *
     * @author yuchao.wang
     * @date 2020/8/27 13:47
     * @param tenantId 租户ID
     * @param workOrderId 工单ID
     * @return int
     *
     */
    @Override
    public int deleteByWorkOrderId(Long tenantId, String workOrderId) {
        return hmeWoTrialCalculateMapper.deleteByWorkOrderId(tenantId, workOrderId);
    }

    /**
     *
     * @Description 是否有该工单ID下的数据
     *
     * @author yuchao.wang
     * @date 2020/8/27 13:52
     * @param tenantId 租户ID
     * @param workOrderId 工单ID
     * @return boolean
     *
     */
    @Override
    public boolean hasTrialCalculateDataByWoId(Long tenantId, String workOrderId) {
        Integer hasData = hmeWoTrialCalculateMapper.hasTrialCalculateDataByWoId(tenantId, workOrderId);
        return !Objects.isNull(hasData) && 1 == hasData;
    }

    /**
     *
     * @Description 批量新增
     *
     * @author yuchao.wang
     * @date 2020/8/27 16:49
     * @param insertList 新增数据列表
     * @return void
     *
     */
    @Override
    public void myBatchInsert(List<HmeWoTrialCalculate> insertList) {
        if (CollectionUtils.isNotEmpty(insertList)) {
            List<List<HmeWoTrialCalculate>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeWoTrialCalculate> domains : splitSqlList) {
                hmeWoTrialCalculateMapper.batchInsert(domains);
            }
        }
    }
}
