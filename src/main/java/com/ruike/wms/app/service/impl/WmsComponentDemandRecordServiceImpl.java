package com.ruike.wms.app.service.impl;

import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;
import com.ruike.hme.domain.repository.HmeObjectRecordLockRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.app.service.WmsComponentDemandRecordService;
import com.ruike.wms.domain.entity.WmsComponentDemandRecord;
import com.ruike.wms.domain.entity.WmsDistributionDemand;
import com.ruike.wms.domain.entity.WmsDistributionDemandDetail;
import com.ruike.wms.domain.repository.WmsComponentDemandRecordRepository;
import com.ruike.wms.domain.repository.WmsDistributionDemandDetailRepository;
import com.ruike.wms.domain.repository.WmsDistributionDemandRepository;
import com.ruike.wms.domain.vo.WmsComponentDemandDateVO;
import com.ruike.wms.domain.vo.WmsComponentDemandSumVO;
import com.ruike.wms.domain.vo.WmsDistDemandDispatchRelVO;
import com.ruike.wms.domain.vo.WmsDistributionDemandRelSumVO;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.util.DatetimeUtils;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ?????????????????????????????????????????????
 *
 * @author yonghui.zhu@hand-china.com 2020-08-24 14:00:05
 */
@Service
public class WmsComponentDemandRecordServiceImpl extends BaseServiceImpl<WmsComponentDemandRecord> implements WmsComponentDemandRecordService {
    private final WmsComponentDemandRecordRepository wmsComponentDemandRecordRepository;
    private final WmsDistributionDemandRepository wmsDistributionDemandRepository;
    private final WmsDistributionDemandDetailRepository wmsDistributionDemandDetailRepository;
    private final LovAdapter lovAdapter;
    private final HmeObjectRecordLockService hmeObjectRecordLockService;
    private final HmeObjectRecordLockRepository hmeObjectRecordLockRepository;
    private final MtWorkOrderRepository workOrderRepository;

    public WmsComponentDemandRecordServiceImpl(WmsComponentDemandRecordRepository wmsComponentDemandRecordRepository, WmsDistributionDemandRepository wmsDistributionDemandRepository, WmsDistributionDemandDetailRepository wmsDistributionDemandDetailRepository, LovAdapter lovAdapter, HmeObjectRecordLockService hmeObjectRecordLockService, HmeObjectRecordLockRepository hmeObjectRecordLockRepository, MtWorkOrderRepository workOrderRepository) {
        this.wmsComponentDemandRecordRepository = wmsComponentDemandRecordRepository;
        this.wmsDistributionDemandRepository = wmsDistributionDemandRepository;
        this.wmsDistributionDemandDetailRepository = wmsDistributionDemandDetailRepository;
        this.lovAdapter = lovAdapter;
        this.hmeObjectRecordLockService = hmeObjectRecordLockService;
        this.hmeObjectRecordLockRepository = hmeObjectRecordLockRepository;
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void insertDemandRecord(Long tenantId, String shiftCode, String woDispatchId, BigDecimal newDispatchQty) {
        List<WmsComponentDemandRecord> list = wmsComponentDemandRecordRepository.selectListFromDispatch(tenantId, woDispatchId);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(rec -> {
            rec.setShiftCode(shiftCode);
            this.insertSelective(rec);
        });
    }

    /**
     * ??????????????????
     *
     * @param tenantId     ??????ID
     * @param woDispatchId ??????ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/10 12:57:19
     */
    private void deleteComponentRecord(Long tenantId, String woDispatchId, List<WmsDistDemandDispatchRelVO> list) {
        // ???????????????????????????????????????????????????????????????????????????
        Set<String> recordIdSet = list.stream().map(WmsDistDemandDispatchRelVO::getDemandRecordId).collect(Collectors.toSet());
        Set<String> demandIdSet = list.stream().filter(rec -> StringUtils.isNotBlank(rec.getDistDemandId())).map(WmsDistDemandDispatchRelVO::getDistDemandId).collect(Collectors.toSet());
        Set<String> detailIdSet = list.stream().filter(rec -> StringUtils.isNotBlank(rec.getDemandDetailId())).map(WmsDistDemandDispatchRelVO::getDemandDetailId).collect(Collectors.toSet());

        List<WmsComponentDemandRecord> recordList = recordIdSet.stream().map(id -> {
            WmsComponentDemandRecord record = new WmsComponentDemandRecord();
            record.setDemandRecordId(id);
            return record;
        }).collect(Collectors.toList());
        List<WmsDistributionDemandDetail> detailList = detailIdSet.stream().map(id -> {
            WmsDistributionDemandDetail record = new WmsDistributionDemandDetail();
            record.setDemandDetailId(id);
            return record;
        }).collect(Collectors.toList());

        // ????????????
        wmsComponentDemandRecordRepository.batchDeleteByPrimaryKey(recordList);
        if (CollectionUtils.isNotEmpty(detailList)) {
            wmsDistributionDemandDetailRepository.batchDeleteByPrimaryKey(detailList);
        }

        // ?????????????????????????????????????????????????????????????????????????????????????????????demand??????
        demandIdSet.forEach(id -> {
            WmsDistributionDemandDetail countQuery = new WmsDistributionDemandDetail();
            countQuery.setDistDemandId(id);
            List<WmsDistributionDemandDetail> remainDetailList = wmsDistributionDemandDetailRepository.select(countQuery);
            if (remainDetailList.size() == 0) {
                wmsDistributionDemandRepository.deleteByPrimaryKey(id);
            } else {
                WmsDistributionDemand demand = wmsDistributionDemandRepository.selectByPrimaryKey(id);
                BigDecimal requirementQty = remainDetailList.stream().map(WmsDistributionDemandDetail::getRequirementQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                demand.setRequirementQty(requirementQty);
                wmsDistributionDemandRepository.updateByPrimaryKey(demand);
            }
        });
    }

    /**
     * ??????????????????
     *
     * @param tenantId ??????ID
     * @param list     ????????????
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/10 12:57:19
     */
    private void updateComponentRecord(Long tenantId, List<WmsDistDemandDispatchRelVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // ???????????????????????????
        Map<String, BigDecimal> recordMap = list.stream().collect(Collectors.toMap(WmsDistDemandDispatchRelVO::getDemandRecordId, WmsDistDemandDispatchRelVO::getDispatchQty, (key1, key2) -> (key1)));
        Map<String, BigDecimal> detailMap = list.stream().collect(Collectors.toMap(WmsDistDemandDispatchRelVO::getDemandDetailId, WmsDistDemandDispatchRelVO::getDispatchQty, (key1, key2) -> (key1)));
        // ?????????????????????
        Set<String> demandIdSet = list.stream().map(WmsDistDemandDispatchRelVO::getDistDemandId).collect(Collectors.toSet());

        // ????????????
        // ?????????????????????????????????????????????
        List<WmsComponentDemandRecord> recordList = wmsComponentDemandRecordRepository.selectListByIds(tenantId, new ArrayList<>(recordMap.keySet()));
        recordList.forEach(record -> {
            BigDecimal dispatchQty = recordMap.get(record.getDemandRecordId());
            record.setDispatchQuantity(dispatchQty);
            record.setRequirementQuantity(dispatchQty.multiply(record.getUsageQuantity()).multiply(record.getAttritionRate()));
        });
        wmsComponentDemandRecordRepository.batchUpdateByPrimaryKey(recordList);

        List<WmsDistributionDemandDetail> demandDetailList = wmsDistributionDemandDetailRepository.selectAttritionListByDetailIdList(tenantId, new ArrayList<>(detailMap.keySet()));
        demandDetailList.forEach(record -> {
            BigDecimal dispatchQty = detailMap.get(record.getDemandDetailId());
            // ?????????????????????
            BigDecimal attritionRate = record.getAttritionChance().divide(BigDecimal.valueOf(100), 6, BigDecimal.ROUND_HALF_UP).add(BigDecimal.ONE);
            record.setDispatchQty(dispatchQty);
            record.setRequirementQty(dispatchQty.multiply(record.getUsageQty()).multiply(attritionRate));
        });
        wmsDistributionDemandDetailRepository.batchUpdateByPrimaryKey(demandDetailList);

        // ??????????????????????????????
        List<WmsDistributionDemand> demandList = wmsDistributionDemandRepository.selectByCondition(Condition.builder(WmsDistributionDemand.class).andWhere(Sqls.custom().andIn(WmsDistributionDemand.FIELD_DIST_DEMAND_ID, demandIdSet)).build());
        demandList.forEach(demand -> {
            BigDecimal requirementQty = wmsDistributionDemandDetailRepository.selectListByDemandId(tenantId, demand.getDistDemandId()).stream().map(WmsDistributionDemandDetail::getRequirementQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            demand.setRequirementQty(requirementQty);
            wmsDistributionDemandRepository.updateByPrimaryKey(demand);
        });
    }

    /**
     * ??????????????????
     *
     * @param tenantId     ??????
     * @param woDispatchId ??????ID
     * @param relList      ????????????
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/18 03:55:01
     */
    private void insertComponentRecord(Long tenantId, String shiftCode, String woDispatchId, List<WmsDistDemandDispatchRelVO> relList) {
        if (CollectionUtils.isEmpty(relList)) {
            return;
        }
        // ???????????????????????????????????????
        List<WmsComponentDemandRecord> list = wmsComponentDemandRecordRepository.selectListFromDispatch(tenantId, woDispatchId);
        if (CollectionUtils.isEmpty(list)) {
            throw new CommonException("???????????????????????????");
        }
        list = list.stream().filter(rec -> relList.stream().anyMatch(rel -> rel.getMaterialId().equals(rec.getMaterialId()) && rel.getProcessId().equals(rec.getProcessId()))).collect(Collectors.toList());
        Map<WmsDistributionDemandRelSumVO, List<WmsDistDemandDispatchRelVO>> sumMap = relList.stream().collect(Collectors.groupingBy(WmsDistDemandDispatchRelVO::getSummary));
        list.forEach(rec -> {
            WmsDistributionDemandRelSumVO sum = new WmsDistributionDemandRelSumVO();
            sum.setMaterialId(rec.getMaterialId()).setProcessId(rec.getProcessId()).setMaterialVersion(rec.getMaterialVersion()).setWoDispatchId(rec.getWoDispatchId());
            WmsDistDemandDispatchRelVO rel = sumMap.get(sum).get(0);
            rec.setDispatchQuantity(rel.getDispatchQty());
            rec.setRequirementQuantity(rel.getDispatchQty().multiply(rec.getUsageQuantity()).multiply(rec.getAttritionRate()));
            rec.setShiftCode(shiftCode);
            this.insertSelective(rec);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateDemandRecord(Long tenantId, String shiftCode, String woDispatchId, BigDecimal newDispatchQty, BigDecimal oldDispatchQty) {
        // ?????????????????????????????????
        List<WmsDistDemandDispatchRelVO> list = wmsComponentDemandRecordRepository.selectRelListByDispatchId(tenantId, woDispatchId);
        if (oldDispatchQty.compareTo(newDispatchQty) > 0) {
            list.forEach(rec -> WmsCommonUtils.processValidateMessage(tenantId, Objects.nonNull(rec.getInstructionDocId()),
                    "WMS_DISTRIBUTION_001", "WMS", rec.getWorkOrderNum(), rec.getInstructionDocNum()));
        }
        // ?????????????????????????????????
        WmsCommonUtils.processValidateMessage(tenantId, list.stream().anyMatch(WmsDistDemandDispatchRelVO::getSubstituteExists),
                "WMS_DISTRIBUTION_002", "WMS");
        // ??????????????????????????????????????????????????????????????????????????????>0?????????????????????
        if (list.stream().noneMatch(rec -> Objects.nonNull(rec.getDemandRecordId()))) {
            if (newDispatchQty.compareTo(BigDecimal.ZERO) > 0) {
                this.insertDemandRecord(tenantId, shiftCode, woDispatchId, newDispatchQty);
            }
        } else {
            // ?????????????????????????????????????????????=0 ??????????????? ??????????????????????????????????????????????????????????????????
            if (newDispatchQty.compareTo(BigDecimal.ZERO) == 0) {
                this.deleteComponentRecord(tenantId, woDispatchId, list);
            } else {
                // ?????????????????????????????????>0 ??? ???????????????
                if (newDispatchQty.compareTo(oldDispatchQty) != 0) {
                    // ????????????????????????????????????????????????
                    Map<WmsDistributionDemandRelSumVO, List<WmsDistDemandDispatchRelVO>> sumMap = list.stream().collect(Collectors.groupingBy(WmsDistDemandDispatchRelVO::getSummary));
                    List<WmsDistDemandDispatchRelVO> insertList = new ArrayList<>();
                    List<WmsDistDemandDispatchRelVO> updateList = new ArrayList<>();
                    for (Map.Entry<WmsDistributionDemandRelSumVO, List<WmsDistDemandDispatchRelVO>> entry : sumMap.entrySet()) {
                        WmsDistributionDemandRelSumVO sum = entry.getKey();
                        List<WmsDistDemandDispatchRelVO> relList = entry.getValue();
                        // ???????????????
                        BigDecimal docDispatchQty = BigDecimal.ZERO;
                        if (CollectionUtils.isNotEmpty(relList)) {
                            // ???????????????????????????
                            docDispatchQty = relList.stream().filter(rec -> Objects.nonNull(rec.getInstructionDocId())).map(WmsDistDemandDispatchRelVO::getDispatchQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                        }
                        // ??????????????????????????????????????? = ???????????? - ????????????????????????
                        BigDecimal changeQty = newDispatchQty.subtract(docDispatchQty);
                        // ?????????????????????????????????????????????????????????????????????????????????????????????
                        if (CollectionUtils.isEmpty(relList) || relList.stream().noneMatch(rec -> Objects.isNull(rec.getInstructionDocId()))) {
                            // ??????????????????
                            WmsDistDemandDispatchRelVO insertRel = new WmsDistDemandDispatchRelVO();
                            insertRel.setWoDispatchId(woDispatchId);
                            insertRel.setMaterialId(sum.getMaterialId());
                            insertRel.setProcessId(sum.getProcessId());
                            insertRel.setDispatchQty(changeQty);
                            insertRel.setMaterialVersion(sum.getMaterialVersion());
                            insertList.add(insertRel);
                        } else {
                            // ???????????????????????????????????????????????????
                            WmsDistDemandDispatchRelVO rel = relList.stream().filter(rec -> Objects.isNull(rec.getInstructionDocId())).collect(Collectors.toList()).get(0);
                            rel.setDispatchQty(changeQty);
                            updateList.add(rel);
                        }
                    }
                    // ????????????
                    this.insertComponentRecord(tenantId, shiftCode, woDispatchId, insertList);
                    // ????????????
                    this.updateComponentRecord(tenantId, updateList);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void createDistributionDemand(Long tenantId) {
        Date startDate = DatetimeUtils.getBeginOfDate(new Date());
        List<LovValueDTO> distributionDateList = lovAdapter.queryLovValue("WMS.DISTRIBUTION_DATE", tenantId);
        int days = 0;
        if (CollectionUtils.isNotEmpty(distributionDateList)) {
            days = Integer.parseInt(distributionDateList.get(0).getValue());
        }
        Date endDate = DateUtils.addDays(startDate, days);
        // ??????????????????????????????????????????????????????????????????
        List<WmsComponentDemandRecord> recordList = wmsComponentDemandRecordRepository.selectNonCreatedListByDateRange(tenantId, startDate, endDate);
        if (CollectionUtils.isEmpty(recordList)) {
            return;
        }
        // 20210827 add by sanfeng.zhang for peng.zhao ?????????????????????
        List<String> workOrderIdList = recordList.stream().map(WmsComponentDemandRecord::getWorkOrderId).distinct().collect(Collectors.toList());
        List<MtWorkOrder> mtWorkOrderList = workOrderRepository.woPropertyBatchGet(tenantId, workOrderIdList);
        Map<String, MtWorkOrder> mtWorkOrderMap = mtWorkOrderList.stream().collect(Collectors.toMap(MtWorkOrder::getWorkOrderId, Function.identity()));
        List<HmeObjectRecordLock> recordLockList = new ArrayList();
        for (String workOrderId : workOrderIdList) {
            //??????
            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
            hmeObjectRecordLockDTO.setFunctionName("??????????????????");
            hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
            hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.WO);
            hmeObjectRecordLockDTO.setObjectRecordId(workOrderId);
            hmeObjectRecordLockDTO.setObjectRecordCode(mtWorkOrderMap.getOrDefault(workOrderId, new MtWorkOrder()).getWorkOrderNum());
            HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
            recordLockList.add(hmeObjectRecordLock);
        }
        if(CollectionUtils.isNotEmpty(recordLockList)) {
            // ??????
            hmeObjectRecordLockRepository.batchCommonLockObject2(tenantId, recordLockList);
        }
        try {
            // ??????????????????
            Map<WmsDistributionDemand, List<WmsComponentDemandRecord>> demandListMap = recordList.stream().collect(Collectors.groupingBy(WmsComponentDemandRecord::summaryDistributionDemand));
            demandListMap.forEach((demand, list) -> {
                // ????????????????????????????????????
                List<WmsDistributionDemand> existsList = wmsDistributionDemandRepository.selectListByUniqueCondition(demand);
                String distDemandId;
                BigDecimal requirementQty = list.stream().map(WmsComponentDemandRecord::getRequirementQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
                // ?????????????????????????????????
                Optional<WmsComponentDemandRecord> firstOpt = list.stream().filter(demandRecord -> StringUtils.isNotBlank(demandRecord.getDistributionBasicId())).findFirst();
                if (firstOpt.isPresent()) {
                    demand.setDistributionBasicId(firstOpt.get().getDistributionBasicId());
                    demand.setDistributionType(firstOpt.get().getDistributionType());
                }
                if (CollectionUtils.isEmpty(existsList)) {
                    demand.setRequirementQty(requirementQty);
                    demand.setStatus(WmsConstant.InstructionStatus.NEW);
                    wmsDistributionDemandRepository.insertSelective(demand);
                    distDemandId = demand.getDistDemandId();
                } else {
                    WmsDistributionDemand existsDemand = existsList.get(0);
                    distDemandId = existsDemand.getDistDemandId();
                    requirementQty = requirementQty.add(existsDemand.getRequirementQty());
                    existsDemand.setRequirementQty(requirementQty);
                    wmsDistributionDemandRepository.updateByPrimaryKey(existsDemand);
                }
                // ????????????
                list.forEach(record -> {
                            // ?????????????????????
                            WmsDistributionDemandDetail detail = new WmsDistributionDemandDetail();
                            detail.setTenantId(tenantId);
                            detail.setDistDemandId(distDemandId);
                            detail.setMaterialId(record.getMaterialId());
                            detail.setMaterialVersion(record.getMaterialVersion());
                            detail.setWorkOrderId(record.getWorkOrderId());
                            detail.setWorkcellId(record.getWorkcellId());
                            detail.setDispatchQty(record.getDispatchQuantity());
                            detail.setUsageQty(record.getUsageQuantity());
                            detail.setRequirementQty(record.getRequirementQuantity());
                            detail.setSubstituteFlag(WmsConstant.CONSTANT_N);
                            detail.setDemandRecordId(record.getDemandRecordId());
                            wmsDistributionDemandDetailRepository.insertSelective(detail);
                            // ??????????????????ID
                            record.setDistDemandId(distDemandId);
                            wmsComponentDemandRecordRepository.updateByPrimaryKey(record);
                        }
                );
            });
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        } finally {
            hmeObjectRecordLockRepository.batchReleaseLock(tenantId , recordLockList , HmeConstants.ConstantValue.YES);
        }
    }

    @Override
    public Page<WmsComponentDemandSumVO> pagedRequirement(Long tenantId, String workOrderId, Date startDate, PageRequest pageRequest) {
        Date endDate = DateUtils.addDays(startDate, 14);
        List<WmsComponentDemandSumVO> list = wmsComponentDemandRecordRepository.selectListByDateRange(tenantId, workOrderId, startDate, endDate);
        Map<WmsComponentDemandSumVO, Map<Date, List<WmsComponentDemandSumVO>>> sumMap = list.stream().collect(Collectors.groupingBy(WmsComponentDemandSumVO::summaryDemand, Collectors.groupingBy(WmsComponentDemandSumVO::getShiftDate)));
        List<WmsComponentDemandSumVO> subList = new ArrayList<>(sumMap.keySet());
        if (subList.size() > 0) {
            Page<WmsComponentDemandSumVO> page = WmsCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), subList);
            List<Date> dateList = DatetimeUtils.getDateListInRange(startDate, endDate);
            // ??????????????????????????????????????????
            page.forEach(rec -> {
                Map<Date, List<WmsComponentDemandSumVO>> dateMap = sumMap.get(rec);
                List<WmsComponentDemandDateVO> demandDateList = new ArrayList<>();
                dateList.forEach(date -> {
                    BigDecimal requirementQty = BigDecimal.ZERO, attritionQty = BigDecimal.ZERO;
                    if (dateMap.containsKey(date)) {
                        List<WmsComponentDemandSumVO> requirementList = dateMap.get(date);
                        requirementQty = requirementList.stream().map(WmsComponentDemandSumVO::getRequirementQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                        attritionQty = requirementList.stream().map(WmsComponentDemandSumVO::getAttritionQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                    }
                    WmsComponentDemandDateVO detail = new WmsComponentDemandDateVO();
                    detail.setRequirementDate(date);
                    detail.setAttritionQty(attritionQty);
                    detail.setRequirementQty(requirementQty);
                    demandDateList.add(detail);
                });
                BigDecimal requirementQty = demandDateList.stream().map(WmsComponentDemandDateVO::getRequirementQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal attritionQty = demandDateList.stream().map(WmsComponentDemandDateVO::getAttritionQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                rec.setRequirementQty(requirementQty);
                rec.setAttritionQty(attritionQty);
                rec.setRequirementList(demandDateList);
            });
            return page;
        }
        return new Page<>(new ArrayList<>(), new PageInfo(pageRequest.getPage(), pageRequest.getSize()), 0L);
    }
}
