package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.vo.HmeCalendarShiftVO;
import com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO2;
import com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO3;
import com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO4;
import com.ruike.hme.infra.mapper.HmeAreaDayPlanReachRateMapper;
import com.ruike.hme.infra.mapper.HmeAreaThroughRateDetailsMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.choerodon.core.oauth.DetailsHelper;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeAreaDayPlanReachRate;
import com.ruike.hme.domain.repository.HmeAreaDayPlanReachRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 制造部日计划达成率看板 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2021-07-02 14:31:13
 */
@Component
public class HmeAreaDayPlanReachRateRepositoryImpl extends BaseRepositoryImpl<HmeAreaDayPlanReachRate> implements HmeAreaDayPlanReachRateRepository {

    @Autowired
    private HmeAreaThroughRateDetailsMapper hmeAreaThroughRateDetailsMapper;
    @Autowired
    private HmeAreaDayPlanReachRateMapper hmeAreaDayPlanReachRateMapper;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Override
    public void createDayPlanReachRate(Long tenantId) {
        // 获取制造部
        List<String> areaIdList = hmeAreaThroughRateDetailsMapper.queryKanbanAreaList(tenantId);
        // 获取用户默认站点
        String defaultSiteId = wmsSiteRepository.userDefaultSite(tenantId);
        List<HmeAreaDayPlanReachRate> hmeAreaDayPlanReachRateList = new ArrayList<>();
        for (String areaId : areaIdList) {
            List<HmeAreaDayPlanReachRate> reachRateList = this.queryDayPlanReachRate(tenantId, defaultSiteId, areaId);
            if (CollectionUtils.isNotEmpty(reachRateList)) {
                hmeAreaDayPlanReachRateList.addAll(reachRateList);
            }
        }
        // 清空当天数据
        this.batchDeleteDayPlanReachRate(tenantId);
        // 根据物料排序
        hmeAreaDayPlanReachRateList = hmeAreaDayPlanReachRateList.stream().sorted(Comparator.comparing(HmeAreaDayPlanReachRate::getMaterialCode)).collect(Collectors.toList());
        // 批量新增
        this.batchInsertDayPlanReachRate(tenantId, hmeAreaDayPlanReachRateList);
    }

    private List<HmeAreaDayPlanReachRate> queryDayPlanReachRate(Long tenantId, String defaultSiteId, String areaId) {
        List<HmeAreaDayPlanReachRate> resultList = new ArrayList<>();
        // 获取制造部下的产线
        List<String> prodLineIdList = hmeAreaThroughRateDetailsMapper.queryProdLineByAreaId(tenantId, areaId, defaultSiteId);
        // 获取计划班次
        List<String> calendarShiftIdList = this.queryCurrentCalendarShiftIdList(tenantId, defaultSiteId, prodLineIdList);
        if (CollectionUtils.isNotEmpty(calendarShiftIdList)) {
            // 获取实绩班次
            List<String> shiftIdList = hmeAreaDayPlanReachRateMapper.queryShiftIdList(tenantId, calendarShiftIdList);
            // 取COS报表作业类型
            List<LovValueDTO> cosJobTypeLovList = lovAdapter.queryLovValue("HME.REPORT_JOB_TYPE", tenantId);
            List<String> cosJobTypeList = cosJobTypeLovList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            // 取产线当班所做所有工单
            List<String> workOrderIdList = hmeAreaDayPlanReachRateMapper.queryWorkOrderByShiftIdAndProdLineId(tenantId, shiftIdList, prodLineIdList, cosJobTypeList);
            // 分成派工记录的工单和eoJobSn的工单
            List<String> dispatchWorkOrderIdList = hmeAreaDayPlanReachRateMapper.queryDispatchWorkOrderByShiftIdAndProdLineId(tenantId, prodLineIdList, calendarShiftIdList);
            List<String> allWorkOrderIdList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(workOrderIdList)) {
                allWorkOrderIdList.addAll(workOrderIdList);
            }
            if (CollectionUtils.isNotEmpty(dispatchWorkOrderIdList)) {
                allWorkOrderIdList.addAll(dispatchWorkOrderIdList);
            }
            // 去重
            allWorkOrderIdList = allWorkOrderIdList.stream().distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(allWorkOrderIdList)) {
                // 取工单工艺路线工序所属工段
                List<HmeMakeCenterProduceBoardVO3> boardVO3List = hmeAreaDayPlanReachRateMapper.queryLineWorkcellAndProcess(tenantId, allWorkOrderIdList, defaultSiteId);
                if (CollectionUtils.isNotEmpty(boardVO3List)) {
                    // 按工单找到最大的工段 再找该工段下末道序
                    LinkedHashMap<String, List<HmeMakeCenterProduceBoardVO3>> lineWorkcellMap = boardVO3List.stream().collect(Collectors.groupingBy(dto -> dto.getWorkOrderId(), LinkedHashMap::new, Collectors.toList()));
                    List<HmeMakeCenterProduceBoardVO4> endProcessList = new ArrayList<>();
                    lineWorkcellMap.entrySet().forEach(map -> {
                        HmeMakeCenterProduceBoardVO4 vo4 = new HmeMakeCenterProduceBoardVO4();
                        List<HmeMakeCenterProduceBoardVO3> valueList = map.getValue();
                        LinkedHashMap<String, List<HmeMakeCenterProduceBoardVO3>> valueMap = valueList.stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO3::getLineWorkcellId, LinkedHashMap::new, Collectors.toList()));
                        // 取最后一个工段
                        List<HmeMakeCenterProduceBoardVO3> lastWorkcellList = valueMap.get(valueMap.keySet().toArray()[valueMap.size() - 1]);
                        vo4.setWorkOrderId(lastWorkcellList.get(0).getWorkOrderId());
                        vo4.setLineWorkcellId(lastWorkcellList.get(0).getLineWorkcellId());
                        vo4.setEndProcessId(lastWorkcellList.get(lastWorkcellList.size() - 1).getProcessId());
                        endProcessList.add(vo4);
                    });
                    List<String> endProcessIdList = endProcessList.stream().map(HmeMakeCenterProduceBoardVO4::getEndProcessId).distinct().collect(Collectors.toList());
                    // 实际交付
                    List<HmeMakeCenterProduceBoardVO2> actualDeliverQtyList = hmeAreaDayPlanReachRateMapper.queryActualDeliverQty(tenantId, shiftIdList, allWorkOrderIdList, endProcessIdList, defaultSiteId);
                    List<String> lineWorkcellIdList = endProcessList.stream().map(HmeMakeCenterProduceBoardVO4::getLineWorkcellId).distinct().collect(Collectors.toList());
                    // 派工数量
                    List<HmeMakeCenterProduceBoardVO2> dispatchQtyList = hmeAreaDayPlanReachRateMapper.queryDispatchQty(tenantId, calendarShiftIdList, allWorkOrderIdList, lineWorkcellIdList);
                    List<HmeMakeCenterProduceBoardVO2> workingList = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(actualDeliverQtyList)) {
                        Map<String, List<HmeMakeCenterProduceBoardVO4>> processMap = endProcessList.stream().collect(Collectors.groupingBy(line -> line.getWorkOrderId() + "_" + line.getLineWorkcellId()));
                        // 根据工单及对应的末序 取对应的交付数
                        // 过滤数组(过滤掉 不是工单对应工段末序交付的数据)
                        List<HmeMakeCenterProduceBoardVO2> filterBoardList = new ArrayList<>();
                        for (HmeMakeCenterProduceBoardVO2 boardVO2 : actualDeliverQtyList) {
                            List<HmeMakeCenterProduceBoardVO4> boardVO4List = processMap.get(boardVO2.getWorkOrderId() + "_" + boardVO2.getLineWorkcellId());
                            if (CollectionUtils.isNotEmpty(boardVO4List)) {
                                if (boardVO2.getActualDeliverQty() != null) {
                                    if (StringUtils.equals(boardVO2.getProcessId(), boardVO4List.get(0).getEndProcessId())) {
                                        filterBoardList.add(boardVO2);
                                    }
                                }
                            }
                        }
                        // 根据物料 取对应的实际交付
                        Map<String, List<HmeMakeCenterProduceBoardVO2>> listMap = filterBoardList.stream().collect(Collectors.groupingBy(vo -> vo.getSnMaterialId()));
                        listMap.entrySet().forEach(lm -> {
                            HmeMakeCenterProduceBoardVO2 boardVO2 = new HmeMakeCenterProduceBoardVO2();
                            List<HmeMakeCenterProduceBoardVO2> valueList = lm.getValue();
                            boardVO2.setSnMaterialId(valueList.get(0).getSnMaterialId());
                            boardVO2.setMaterialName(valueList.get(0).getMaterialName());
                            boardVO2.setMaterialCode(valueList.get(0).getMaterialCode());
                            Double actualDeliverQty = valueList.stream().map(HmeMakeCenterProduceBoardVO2::getActualDeliverQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                            boardVO2.setActualDeliverQty(actualDeliverQty != null ? BigDecimal.valueOf(actualDeliverQty) : BigDecimal.ZERO);
                            workingList.add(boardVO2);
                        });
                    }
                    if (CollectionUtils.isNotEmpty(dispatchWorkOrderIdList)) {
                        // 派工记录的 取派工工单的物料 及通过工单找的物料 进行汇总
                        List<HmeMakeCenterProduceBoardVO4> dispatchMaterialList = hmeAreaDayPlanReachRateMapper.queryDispatchMaterialList(tenantId, dispatchWorkOrderIdList, defaultSiteId);
                        for (HmeMakeCenterProduceBoardVO4 hmeMakeCenterProduceBoardVO4 : dispatchMaterialList) {
                            HmeMakeCenterProduceBoardVO2 boardVO2 = new HmeMakeCenterProduceBoardVO2();
                            boardVO2.setSnMaterialId(hmeMakeCenterProduceBoardVO4.getMaterialId());
                            boardVO2.setMaterialName(hmeMakeCenterProduceBoardVO4.getMaterialName());
                            boardVO2.setMaterialCode(hmeMakeCenterProduceBoardVO4.getMaterialCode());
                            boardVO2.setActualDeliverQty(BigDecimal.ZERO);
                            workingList.add(boardVO2);
                        }
                    }
                    // 根据物料 对数据进行汇总
                    Map<String, List<HmeMakeCenterProduceBoardVO2>> resultListMap = workingList.stream().collect(Collectors.groupingBy(vo -> vo.getSnMaterialId(), LinkedHashMap::new, Collectors.toList()));
                    Date nowDate = CommonUtils.currentTimeGet();
                    resultListMap.entrySet().forEach(rs -> {
                        List<HmeMakeCenterProduceBoardVO2> valueList = rs.getValue();
                        HmeAreaDayPlanReachRate reachRate = new HmeAreaDayPlanReachRate();
                        reachRate.setMaterialName(valueList.get(0).getMaterialName());
                        reachRate.setMaterialCode(valueList.get(0).getMaterialCode());
                        Double actualDeliverQty = valueList.stream().map(HmeMakeCenterProduceBoardVO2::getActualDeliverQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                        reachRate.setActualDeliverQty(actualDeliverQty != null ? BigDecimal.valueOf(actualDeliverQty) : BigDecimal.ZERO);
                        List<HmeMakeCenterProduceBoardVO2> dispatchQtys = dispatchQtyList.stream().filter(dpq -> StringUtils.equals(dpq.getSnMaterialId(), valueList.get(0).getSnMaterialId())).collect(Collectors.toList());
                        Double dispatchQty = dispatchQtys.stream().map(HmeMakeCenterProduceBoardVO2::getDispatchQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                        reachRate.setDispatchQty(dispatchQty != null ? BigDecimal.valueOf(dispatchQty) : BigDecimal.ZERO);
                        // 计划达成率 实际交付/派工数量*100%，保留整数，四舍五入
                        BigDecimal planReachRate = BigDecimal.ZERO.setScale(2);
                        if (BigDecimal.ZERO.compareTo(reachRate.getDispatchQty()) != 0) {
                            planReachRate = reachRate.getActualDeliverQty().divide(reachRate.getDispatchQty(), 6, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                        }
                        reachRate.setPlanReachRate(planReachRate + "%");
                        reachRate.setChartDate(nowDate);
                        reachRate.setSiteId(defaultSiteId);
                        reachRate.setAreaId(areaId);
                        // 过滤掉派工 实际交付 实际投产都为0的数据
                        if (BigDecimal.ZERO.compareTo(reachRate.getActualDeliverQty()) != 0 || BigDecimal.ZERO.compareTo(reachRate.getDispatchQty()) != 0) {
                            resultList.add(reachRate);
                        }
                    });
                }
            }
        }
        return resultList;
    }

    private List<String> queryCurrentCalendarShiftIdList(Long tenantId, String siteId, List<String> prodLineIdList) {
        List<String> calendarShiftIdList = new ArrayList<>();
        // 获取当天产线对应工段下的班次(考虑跨天的情况 班次日期往后推一天)
        List<HmeCalendarShiftVO> calendarShiftList = hmeAreaDayPlanReachRateMapper.queryCalendarShiftList(tenantId, siteId, prodLineIdList);
        // 根据当前时间取最近的班次日历
        Map<String, List<HmeCalendarShiftVO>> calendarShiftMap = calendarShiftList.stream().collect(Collectors.groupingBy(HmeCalendarShiftVO::getCalendarId));
        for (Map.Entry<String, List<HmeCalendarShiftVO>> calendarShiftEntry : calendarShiftMap.entrySet()) {
            List<HmeCalendarShiftVO> valueList = calendarShiftEntry.getValue();
            List<HmeCalendarShiftVO> sortValueList = valueList.stream().sorted(Comparator.comparing(HmeCalendarShiftVO::getShiftStartTime).reversed()).collect(Collectors.toList());
            calendarShiftIdList.add(sortValueList.get(0).getCalendarShiftId());
        }
        return calendarShiftIdList;
    }

    private void batchDeleteDayPlanReachRate(Long tenantId) {
        hmeAreaDayPlanReachRateMapper.batchDeleteDayPlanReachRate(tenantId);
    }

    private void batchInsertDayPlanReachRate(Long tenantId, List<HmeAreaDayPlanReachRate> hmeAreaDayPlanReachRateList) {
        if(CollectionUtils.isNotEmpty(hmeAreaDayPlanReachRateList)) {
            List<String> idList = customDbRepository.getNextKeys("hme_area_day_plan_reach_rate_s", hmeAreaDayPlanReachRateList.size());
            List<String> cidList = customDbRepository.getNextKeys("hme_area_day_plan_reach_rate_cid_s", hmeAreaDayPlanReachRateList.size());
            Integer indexNum = 0;
            Date now = CommonUtils.currentTimeGet();
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            for (HmeAreaDayPlanReachRate hmeAreaDayPlanReachRate : hmeAreaDayPlanReachRateList) {
                hmeAreaDayPlanReachRate.setTenantId(tenantId);
                hmeAreaDayPlanReachRate.setDayPlanReachRateId(idList.get(indexNum));
                hmeAreaDayPlanReachRate.setCid(Long.valueOf(cidList.get(indexNum++)));
                hmeAreaDayPlanReachRate.setCreatedBy(userId);
                hmeAreaDayPlanReachRate.setCreationDate(now);
                hmeAreaDayPlanReachRate.setLastUpdatedBy(userId);
                hmeAreaDayPlanReachRate.setLastUpdateDate(now);
                hmeAreaDayPlanReachRate.setObjectVersionNumber(1L);
            }
            if (CollectionUtils.isNotEmpty(hmeAreaDayPlanReachRateList)) {
                List<List<HmeAreaDayPlanReachRate>> splitSqlList = InterfaceUtils.splitSqlList(hmeAreaDayPlanReachRateList, SQL_ITEM_COUNT_LIMIT);
                for (List<HmeAreaDayPlanReachRate> domains : splitSqlList) {
                    self().batchInsert(domains);
                }
            }
        }
    }
}
