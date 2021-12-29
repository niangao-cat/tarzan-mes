package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeAreaThroughRateDetails;
import com.ruike.hme.domain.entity.HmeMaterialLotNcRecord;
import com.ruike.hme.domain.repository.HmeAreaThroughRateDetailsRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeAreaThroughRateDetailsMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.util.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 制造部直通率看板 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2021-06-24 21:17:01
 */
@Component
public class HmeAreaThroughRateDetailsRepositoryImpl extends BaseRepositoryImpl<HmeAreaThroughRateDetails> implements HmeAreaThroughRateDetailsRepository {

    @Autowired
    private HmeAreaThroughRateDetailsMapper hmeAreaThroughRateDetailsMapper;

    @Autowired
    private HmeAreaThroughRateDetailsRepository hmeAreaThroughRateDetailsRepository;

    @Autowired
    private WmsSiteRepository wmsSiteRepository;

    @Autowired
    private ThreadPoolTaskExecutor poolExecutor;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createAreaThroughRate(Long tenantId) {
        // 获取制造部
        List<String> areaIdList = hmeAreaThroughRateDetailsMapper.queryKanbanAreaList(tenantId);
        List<HmeAreaThroughRateDetails> hmeAreaThroughRateDetailsList = new ArrayList<>();
        for (String areaId : areaIdList) {
            List<HmeAreaThroughRateDetails> detailsList = this.queryThroughRateByAreaId(tenantId, areaId);
            if (CollectionUtils.isNotEmpty(detailsList)) {
                hmeAreaThroughRateDetailsList.addAll(detailsList);
            }
        }
        // 清空当天数据
        this.batchDeleteThroughRate(tenantId);
        // 批量新增
        this.batchInsertThroughRate(tenantId, hmeAreaThroughRateDetailsList);
    }

    private void batchDeleteThroughRate (Long tenantId) {
        hmeAreaThroughRateDetailsMapper.batchDeleteThroughRate(tenantId);
    }

    private void batchInsertThroughRate(Long tenantId, List<HmeAreaThroughRateDetails> hmeAreaThroughRateDetailsList) {
        if(CollectionUtils.isNotEmpty(hmeAreaThroughRateDetailsList)) {
            List<String> idList = customDbRepository.getNextKeys("hme_area_through_rate_details_s", hmeAreaThroughRateDetailsList.size());
            List<String> cidList = customDbRepository.getNextKeys("hme_area_through_rate_details_cid_s", hmeAreaThroughRateDetailsList.size());
            Integer indexNum = 0;
            Date now = CommonUtils.currentTimeGet();
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            for (HmeAreaThroughRateDetails hmeAreaThroughRateDetails : hmeAreaThroughRateDetailsList) {
                hmeAreaThroughRateDetails.setTenantId(tenantId);
                hmeAreaThroughRateDetails.setThroughRateDetailsId(idList.get(indexNum));
                hmeAreaThroughRateDetails.setCid(Long.valueOf(cidList.get(indexNum++)));
                hmeAreaThroughRateDetails.setCreatedBy(userId);
                hmeAreaThroughRateDetails.setCreationDate(now);
                hmeAreaThroughRateDetails.setLastUpdatedBy(userId);
                hmeAreaThroughRateDetails.setLastUpdateDate(now);
                hmeAreaThroughRateDetails.setObjectVersionNumber(1L);
            }
            if (CollectionUtils.isNotEmpty(hmeAreaThroughRateDetailsList)) {
                List<List<HmeAreaThroughRateDetails>> splitSqlList = InterfaceUtils.splitSqlList(hmeAreaThroughRateDetailsList, SQL_ITEM_COUNT_LIMIT);
                for (List<HmeAreaThroughRateDetails> domains : splitSqlList) {
                    hmeAreaThroughRateDetailsRepository.batchInsert(domains);
                }
            }
        }
    }

    /**
     * 获取制造的直通率信息
     * @param tenantId
     * @param areaId
     * @return java.util.List<com.ruike.hme.domain.entity.HmeAreaThroughRateDetails>
     * @author sanfeng.zhang@hand-china.com 2021/6/24
     */
    private List<HmeAreaThroughRateDetails> queryThroughRateByAreaId(Long tenantId, String areaId) {
        List<HmeAreaThroughRateDetails> resultList = new ArrayList<>();
        // 获取用户默认站点
        String defaultSiteId = wmsSiteRepository.userDefaultSite(tenantId);
        List<String> prodLineIdList = hmeAreaThroughRateDetailsMapper.queryProdLineByAreaId(tenantId, areaId, defaultSiteId);
        // 根据产线取产品组及工单
        List<HmeMakeCenterProduceBoardVO10> productionGroupList = hmeAreaThroughRateDetailsMapper.queryProductionGroupByProdLineId(tenantId, prodLineIdList, defaultSiteId);
        if (CollectionUtils.isNotEmpty(productionGroupList)) {
            List<HmeMakeCenterProduceBoardVO10> sortProductionGroupList = productionGroupList.stream().sorted(Comparator.comparing(HmeMakeCenterProduceBoardVO10::getProdLineOrder, Comparator.nullsLast(Long::compareTo))).collect(Collectors.toList());
            // 获取今天在做的工单(eoJobSn 现在进站时间为今天)
            Date currentDate = CommonUtils.currentTimeGet();
            List<String> currentWorkOrderIdList = hmeAreaThroughRateDetailsMapper.queryCurrentWorkOrderIdList(tenantId, DateUtil.date2String(currentDate, "yyyy-MM-dd 00:00:00"), DateUtil.date2String(currentDate, "yyyy-MM-dd 23:59:59"));
            // 去重
            List<String> distinctCurrentWorkOrderIdList = currentWorkOrderIdList.stream().distinct().collect(Collectors.toList());
            List<String> centerKanbanLineByHeaderIds = sortProductionGroupList.stream().map(HmeMakeCenterProduceBoardVO10::getCenterKanbanHeaderId).collect(Collectors.toList());
            List<HmeMakeCenterProduceBoardVO12> kanbanLineByHeaderList = hmeAreaThroughRateDetailsMapper.batchQueryCenterKanbanLineByHeaderIds(tenantId, centerKanbanLineByHeaderIds);
            Map<String, List<HmeMakeCenterProduceBoardVO12>> kanbanLineByHeaderMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(kanbanLineByHeaderList)) {
                kanbanLineByHeaderMap = kanbanLineByHeaderList.stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO12::getCenterKanbanHeaderId));
            }
            List<String> firstWorkOrderIdList = new ArrayList<>();
            List<String> nonCosWorkOrderIdList = new ArrayList<>();
            List<String> cosWorkOrderIdList = new ArrayList<>();
            sortProductionGroupList.forEach(ptg -> {
                // 获取产品组下所有工单
                List<String> workOrderIdList = ptg.getWorkOrderIdList();
                // 取两者的交集
                List<String> filterWorkOrderIdList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(workOrderIdList)) {
                    filterWorkOrderIdList = distinctCurrentWorkOrderIdList.stream().filter(vo -> workOrderIdList.contains(vo)).collect(Collectors.toList());
                    if (HmeConstants.ConstantValue.YES.equals(ptg.getCosFlag())) {
                        cosWorkOrderIdList.addAll(workOrderIdList);
                    } else {
                        nonCosWorkOrderIdList.addAll(workOrderIdList);
                    }
                }
                if (CollectionUtils.isNotEmpty(filterWorkOrderIdList)) {
                    firstWorkOrderIdList.add(filterWorkOrderIdList.get(0));
                }
            });
            Map<String, List<HmeMakeCenterProduceBoardVO11>> firstWorkOrderMap = new HashMap<>();
            List<HmeMakeCenterProduceBoardVO19> boardVO19List = new ArrayList<>();
            List<HmeMakeCenterProduceBoardVO19> reworkEoList = new ArrayList<>();
            List<HmeMakeCenterProduceBoardVO19> cosBoardList = new ArrayList<>();
            List<HmeMakeCenterProduceBoardVO19> cosTestBoardList = new ArrayList<>();
            List<HmeMakeCenterProduceBoardVO20> reInspectionOkQtyList = new ArrayList<>();
            Integer ncRecordNum = 0;
            if (CollectionUtils.isNotEmpty(firstWorkOrderIdList)) {
                // 查询工单对应工序及工位信息
                List<HmeMakeCenterProduceBoardVO11> boardVO11List = hmeAreaThroughRateDetailsMapper.batchQueryProcessByWorkOrders(tenantId, defaultSiteId, firstWorkOrderIdList);
                if (CollectionUtils.isNotEmpty(boardVO11List)) {
                    firstWorkOrderMap = boardVO11List.stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO11::getWorkOrderId, LinkedHashMap::new, Collectors.toList()));
                }
                List<String> processIds = boardVO11List.stream().map(HmeMakeCenterProduceBoardVO11::getProcessId).distinct().collect(Collectors.toList());
                // 更据工单和工位查询当天作业的eo
                if (CollectionUtils.isNotEmpty(processIds) && CollectionUtils.isNotEmpty(nonCosWorkOrderIdList)) {
                    boardVO19List = hmeAreaThroughRateDetailsMapper.queryEoListByWorkOrderAndWorkcell(tenantId, nonCosWorkOrderIdList, processIds, defaultSiteId, DateUtil.date2String(currentDate, "yyyy-MM-dd 00:00:00"), DateUtil.date2String(currentDate, "yyyy-MM-dd 23:59:59"));
                    List<String> allEoIdList = boardVO19List.stream().map(HmeMakeCenterProduceBoardVO19::getEoId).distinct().collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(allEoIdList)) {
                        // 多线程获取返修的eo
                        // 根据eoId查询返修的eo
                        List<List<String>> eoIdList = this.splitSqlList(allEoIdList, 4000, 10);
                        List<Future<List<HmeMakeCenterProduceBoardVO19>>> BoardVO19FutureList = new ArrayList<>();
                        // 最多开启10个进程 判断数据拆分数是否大于10
                        for (List<String> eoIds : eoIdList) {
                            Future<List<HmeMakeCenterProduceBoardVO19>> BoardVO19Future = poolExecutor.submit(() -> {
                                SecurityTokenHelper.close();
                                List<HmeMakeCenterProduceBoardVO19> makeCenterProduceBoardVO19s = hmeAreaThroughRateDetailsMapper.batchQueryReworkRecordEoList(tenantId, eoIds, processIds, defaultSiteId);
                                return makeCenterProduceBoardVO19s;
                            });
                            BoardVO19FutureList.add(BoardVO19Future);
                        }
                        if (CollectionUtils.isNotEmpty(BoardVO19FutureList)) {
                            //将异步计算结果加载到返回值中
                            BoardVO19FutureList.forEach(bf -> {
                                try {
                                    List<HmeMakeCenterProduceBoardVO19> boardVO19s = bf.get();
                                    if (CollectionUtils.isNotEmpty(boardVO19s)) {
                                        reworkEoList.addAll(boardVO19s);
                                    }
                                } catch (InterruptedException | ExecutionException e) {
                                    bf.cancel(true);
                                    Thread.currentThread().interrupt();
                                }
                            });
                        }
                    }
                }
                // COS 汇总SN数量和出站数量
                if (CollectionUtils.isNotEmpty(cosWorkOrderIdList)) {
                    if (CollectionUtils.isNotEmpty(processIds)) {
                        cosBoardList = hmeAreaThroughRateDetailsMapper.queryCosQtyByWorkOrderAndProcess(tenantId, cosWorkOrderIdList, processIds, defaultSiteId);
                        List<String> materialLotIdList = cosBoardList.stream().map(HmeMakeCenterProduceBoardVO19::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(materialLotIdList)) {
                            // 复检合格数
                            reInspectionOkQtyList = hmeAreaThroughRateDetailsMapper.queryReInspectionOkQty(tenantId, materialLotIdList);
                        }
                    }
                    // 查询当天COS的不良数
                    ncRecordNum = hmeAreaThroughRateDetailsMapper.queryCosNcRecordNum(tenantId);
                    cosTestBoardList = hmeAreaThroughRateDetailsMapper.queryCos015SnQty(tenantId, cosWorkOrderIdList);
                }
            }
            Map<String, List<HmeMakeCenterProduceBoardVO19>> processGroupBoardMap = boardVO19List.stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO19::getProcessId));
            Map<String, List<HmeMakeCenterProduceBoardVO19>> reworkEoGroupByProcessMap = reworkEoList.stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO19::getProcessId));
            Map<String, List<HmeMakeCenterProduceBoardVO19>> cosBoardMap = cosBoardList.stream().collect(Collectors.groupingBy(HmeMakeCenterProduceBoardVO19::getProcessId));

            for (HmeMakeCenterProduceBoardVO10 boardVO10 : sortProductionGroupList) {
                List<HmeMakeCenterProduceBoardVO12> boardVO12List = new ArrayList<>();
                BigDecimal throughRate = BigDecimal.ONE;
                // 获取产品组下所有工单
                List<String> workOrderIdList = boardVO10.getWorkOrderIdList();
                // 取两者的交集
                List<String> filterWorkOrderIdList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(workOrderIdList)) {
                    filterWorkOrderIdList = distinctCurrentWorkOrderIdList.stream().filter(vo -> workOrderIdList.contains(vo)).collect(Collectors.toList());
                }
                // 根据第一个工单 获取对应工序及工位
                if (CollectionUtils.isNotEmpty(filterWorkOrderIdList)) {
                    List<HmeMakeCenterProduceBoardVO11> boardVO11List = firstWorkOrderMap.getOrDefault(filterWorkOrderIdList.get(0), new ArrayList<>());
                    // 按工序计算直通率
                    for (HmeMakeCenterProduceBoardVO11 boardVO11 : boardVO11List) {
                        HmeMakeCenterProduceBoardVO12 boardVO12 = new HmeMakeCenterProduceBoardVO12();
                        boardVO12.setProcessId(boardVO11.getProcessId());
                        boardVO12.setProcessName(boardVO11.getProcessName());
                        BigDecimal processThroughRate = BigDecimal.ONE;
                        if (HmeConstants.ConstantValue.YES.equals(boardVO10.getCosFlag())) {
                            // COS 工序直通率 产出数/投产数  取片工序 = 产出数+复检合格数/投产数
                            List<HmeMakeCenterProduceBoardVO19> processCosBoardList = cosBoardMap.getOrDefault(boardVO11.getProcessId(), new ArrayList<>());
                            List<HmeMakeCenterProduceBoardVO19> cosBoardVO19s = processCosBoardList.stream().filter(vo -> workOrderIdList.contains(vo.getWorkOrderId())).collect(Collectors.toList());
                            Double siteOutQty = cosBoardVO19s.stream().map(HmeMakeCenterProduceBoardVO19::getSiteOutQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                            Double snQty = cosBoardVO19s.stream().map(HmeMakeCenterProduceBoardVO19::getSnQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                            Double reInspectionOkQty = 0.0D;
                            if (HmeConstants.ConstantValue.YES.equals(boardVO11.getQpFlag())) {
                                List<String> materialLotIds = cosBoardVO19s.stream().map(HmeMakeCenterProduceBoardVO19::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                                reInspectionOkQty = reInspectionOkQtyList.stream().filter(vo -> materialLotIds.contains(vo.getMaterialLotId())).map(HmeMakeCenterProduceBoardVO20::getReInspectionOkQty).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                            }
                            if (snQty.compareTo(Double.valueOf(0)) != 0) {
                                processThroughRate = BigDecimal.valueOf(siteOutQty).add(BigDecimal.valueOf(reInspectionOkQty)).divide(BigDecimal.valueOf(snQty), 2, BigDecimal.ROUND_HALF_DOWN);
                            }
                        } else {
                            // 获取所有的EO
                            List<HmeMakeCenterProduceBoardVO19> boardVO19s = processGroupBoardMap.getOrDefault(boardVO11.getProcessId(), new ArrayList<>());
                            List<String> eoList = boardVO19s.stream().filter(vo -> workOrderIdList.contains(vo.getWorkOrderId())).map(HmeMakeCenterProduceBoardVO19::getEoId).distinct().collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(eoList)) {
                                // 获取返修记录的EO
                                List<HmeMakeCenterProduceBoardVO19> allReworkList = reworkEoGroupByProcessMap.getOrDefault(boardVO11.getProcessId(), new ArrayList<>());
                                List<String> reworkEoIdList = allReworkList.stream().filter(rework -> eoList.contains(rework.getEoId())).map(HmeMakeCenterProduceBoardVO19::getEoId).distinct().collect(Collectors.toList());
                                // 非返修的 为总eo数 - 返修数
                                Integer nonReworkEoCount = eoList.size() - reworkEoIdList.size();
                                processThroughRate = BigDecimal.valueOf(nonReworkEoCount).divide(BigDecimal.valueOf(eoList.size()), 2, BigDecimal.ROUND_HALF_DOWN);
                            }
                        }
                        boardVO12.setProcessThroughRate(processThroughRate);
                        boardVO12List.add(boardVO12);
                        if (BigDecimal.ZERO.compareTo(processThroughRate) != 0) {
                            throughRate = throughRate.multiply(processThroughRate).setScale(6, BigDecimal.ROUND_HALF_DOWN);
                        }
                    }

                    // 查询横轴对应的工序
                    List<HmeMakeCenterProduceBoardVO12> centerProduceBoardVO12List = kanbanLineByHeaderMap.get(boardVO10.getCenterKanbanHeaderId());
                    // COS产线 要加入COS测试工序
                    HmeMakeCenterProduceBoardVO12 boardVO12 = new HmeMakeCenterProduceBoardVO12();
                    if (HmeConstants.ConstantValue.YES.equals(boardVO10.getCosFlag())) {
                        // COS测试工序 直通率为 SN数/(SN数+不良数)
                        List<HmeMakeCenterProduceBoardVO19> cosTestBoards = cosTestBoardList.stream().filter(vo -> workOrderIdList.contains(vo.getWorkOrderId())).collect(Collectors.toList());
                        Double snQty = cosTestBoards.stream().map(HmeMakeCenterProduceBoardVO19::getSnQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                        boardVO12.setProcessThroughRate(BigDecimal.ONE);
                        if (snQty.compareTo(Double.valueOf(0)) != 0 && ncRecordNum.compareTo(Integer.valueOf(0)) != 0) {
                            boardVO12.setProcessThroughRate(BigDecimal.valueOf(snQty).divide(BigDecimal.valueOf(snQty + ncRecordNum), 2, BigDecimal.ROUND_HALF_DOWN));
                        }
                        throughRate = throughRate.multiply(boardVO12.getProcessThroughRate()).setScale(6, BigDecimal.ROUND_HALF_DOWN);
                        boardVO12.setThroughRate(throughRate);
                    }
                    BigDecimal finalThroughRate = throughRate;
                    centerProduceBoardVO12List.stream().forEach(cpb -> {
                        //  为COS测试工序 即cosTestFlag = Y 另外取值
                        if (HmeConstants.ConstantValue.YES.equals(cpb.getCosTestFlag())) {
                            cpb.setProcessThroughRate(boardVO12.getProcessThroughRate());
                        } else {
                            Optional<HmeMakeCenterProduceBoardVO12> firstOpt = boardVO12List.stream().filter(vo -> StringUtils.equals(vo.getProcessId(), cpb.getProcessId())).findFirst();
                            if (firstOpt.isPresent()) {
                                cpb.setProcessThroughRate(firstOpt.get().getProcessThroughRate());
                            } else {
                                cpb.setProcessThroughRate(BigDecimal.ONE);
                            }
                        }
                        cpb.setThroughRate(finalThroughRate);
                    });
                    // 组装数据返回
                    HmeAreaThroughRateDetails hmeAreaThroughRateDetails = new HmeAreaThroughRateDetails();
                    hmeAreaThroughRateDetails.setChartTitle(boardVO10.getProdLineName() + "-" + boardVO10.getDescription());
                    hmeAreaThroughRateDetails.setTargetHeaderThroughRate(boardVO10.getTargetThroughRate());
                    // 目标直通率为空 默认1 小于目标直通率 则红色显示 即N 大于等于则Y
                    BigDecimal targetThroughRate = boardVO10.getTargetThroughRate() != null ? boardVO10.getTargetThroughRate() : BigDecimal.ONE;
                    if (finalThroughRate.compareTo(targetThroughRate) >= 0) {
                        hmeAreaThroughRateDetails.setOverFlag(HmeConstants.ConstantValue.YES);
                    } else {
                        hmeAreaThroughRateDetails.setOverFlag(HmeConstants.ConstantValue.NO);
                    }

                    for (HmeMakeCenterProduceBoardVO12 hmeMakeCenterProduceBoardVO12 : centerProduceBoardVO12List) {
                        HmeAreaThroughRateDetails throughRateDetails = new HmeAreaThroughRateDetails();
                        throughRateDetails.setChartTitle(hmeAreaThroughRateDetails.getChartTitle());
                        throughRateDetails.setTargetHeaderThroughRate(hmeAreaThroughRateDetails.getTargetHeaderThroughRate());
                        throughRateDetails.setOverFlag(hmeAreaThroughRateDetails.getOverFlag());
                        throughRateDetails.setAreaId(areaId);
                        throughRateDetails.setChartDate(currentDate);
                        throughRateDetails.setProcessName(hmeMakeCenterProduceBoardVO12.getProcessName());
                        throughRateDetails.setProcessThroughRate(hmeMakeCenterProduceBoardVO12.getProcessThroughRate());
                        throughRateDetails.setTargetLineThroughRate(hmeMakeCenterProduceBoardVO12.getTargetThroughRate());
                        throughRateDetails.setThroughRate(hmeMakeCenterProduceBoardVO12.getThroughRate());
                        throughRateDetails.setAttribute1(boardVO10.getProdLineId());
                        throughRateDetails.setAttribute2(boardVO10.getProductionGroupId());
                        if (boardVO10.getProdLineOrder() != null) {
                            throughRateDetails.setAttribute3(boardVO10.getProdLineOrder().toString());
                        }
                        resultList.add(throughRateDetails);
                    }
                }
            }
        }
        return resultList;
    }
    /***
     * 拆分数据 拆分个数大于指定数 则将剩余数据放到最后一列
     * @param sqlList
     * @param splitNum
     * @return java.util.List<java.util.List<T>>
     * @author sanfeng.zhang@hand-china.com 2021/6/21
     */
    private static <T> List<List<T>> splitSqlList(List<T> sqlList, Integer splitNum, Integer maxNum) {
        if (maxNum == null) {
            maxNum = 10;
        }
        List<List<T>> returnList = new ArrayList<>();
        if (sqlList.size() <= splitNum) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / splitNum;
            if (splitCount > maxNum - 1) {
                splitCount = maxNum - 1;
            }
            int splitRest = sqlList.size() - splitCount * splitNum;
            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * splitNum, (i + 1) * splitNum));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * splitNum, sqlList.size()));
            }
        }
        return returnList;
    }
}
