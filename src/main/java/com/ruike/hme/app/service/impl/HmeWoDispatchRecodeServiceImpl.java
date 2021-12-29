package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeWoDispatchCompSuiteQueryDTO;
import com.ruike.hme.api.dto.HmeWoDispatchDTO;
import com.ruike.hme.api.dto.HmeWoDispatchSuiteQueryDTO;
import com.ruike.hme.app.service.HmeWoDispatchRecodeService;
import com.ruike.hme.domain.entity.HmeWoDispatchRecode;
import com.ruike.hme.domain.repository.HmeWoDispatchRecodeRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeWoDispatchRecodeMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.hme.infra.util.CollectionCommonUtils;
import com.ruike.wms.app.service.WmsComponentDemandRecordService;
import com.ruike.wms.domain.repository.WmsComponentDemandRecordRepository;
import com.ruike.wms.domain.repository.WmsDistributionDemandRepository;
import com.ruike.wms.domain.vo.WmsDistributionQtyVO;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.util.DatetimeUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.helper.OptionalHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.profile.ProfileClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.calendar.domain.repository.MtCalendarRepository;
import tarzan.calendar.domain.vo.MtCalendarVO2;
import tarzan.modeling.domain.entity.MtModWorkcellSchedule;
import tarzan.modeling.domain.repository.MtModWorkcellScheduleRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ErrorCode.HME_WO_DISPATCH_0003;
import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY;

/**
 * 工单派工记录表应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */
@Service
@Slf4j
public class HmeWoDispatchRecodeServiceImpl extends BaseServiceImpl<HmeWoDispatchRecode> implements HmeWoDispatchRecodeService {

    private final HmeWoDispatchRecodeMapper hmeWoDispatchRecodeMapper;
    private final HmeWoDispatchRecodeRepository hmeWoDispatchRecodeRepository;
    private final HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    private final MtCalendarRepository calendarRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final MtModWorkcellScheduleRepository mtModWorkcellScheduleRepository;
    private final WmsComponentDemandRecordService wmsComponentDemandRecordService;
    private final ProfileClient profileClient;
    private final WmsDistributionDemandRepository distributionDemandRepository;
    private final WmsComponentDemandRecordRepository wmsComponentDemandRecordRepository;

    private final List<String> SHIFT_SEQUENCE = Arrays.asList("A", "B", "C");
    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##################.##########");

    public HmeWoDispatchRecodeServiceImpl(HmeWoDispatchRecodeMapper hmeWoDispatchRecodeMapper, HmeWoDispatchRecodeRepository hmeWoDispatchRecodeRepository, HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper, MtCalendarRepository calendarRepository, MtErrorMessageRepository mtErrorMessageRepository, MtModWorkcellScheduleRepository mtModWorkcellScheduleRepository, WmsComponentDemandRecordService wmsComponentDemandRecordService, ProfileClient profileClient, WmsDistributionDemandRepository distributionDemandRepository, WmsComponentDemandRecordRepository wmsComponentDemandRecordRepository) {
        this.hmeWoDispatchRecodeMapper = hmeWoDispatchRecodeMapper;
        this.hmeWoDispatchRecodeRepository = hmeWoDispatchRecodeRepository;
        this.hmeWorkOrderManagementMapper = hmeWorkOrderManagementMapper;
        this.calendarRepository = calendarRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.mtModWorkcellScheduleRepository = mtModWorkcellScheduleRepository;
        this.wmsComponentDemandRecordService = wmsComponentDemandRecordService;
        this.profileClient = profileClient;
        this.distributionDemandRepository = distributionDemandRepository;
        this.wmsComponentDemandRecordRepository = wmsComponentDemandRecordRepository;
    }

    /**
     * 整理查询结果
     * 修剪每个日期下超过的3个班次，如果不足则补充到3个
     *
     * @param list 查询结果
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/18 10:39:41
     */
    private List<HmeWoCalendarShiftVO> processQueryResult(List<HmeWoCalendarShiftVO> list) {
        List<HmeWoCalendarShiftVO> newList = new ArrayList<>();
        Map<Date, List<HmeWoCalendarShiftVO>> shiftMap = list.stream().collect(Collectors.groupingBy(HmeWoCalendarShiftVO::getShiftDate));
        shiftMap.keySet().stream().sorted().forEach(shiftDate -> {
            List<HmeWoCalendarShiftVO> shiftList = shiftMap.get(shiftDate);
            if (shiftList.size() > SHIFT_SEQUENCE.size()) {
                // 修剪
                List<HmeWoCalendarShiftVO> subList = list.subList(0, SHIFT_SEQUENCE.size());
                shiftMap.put(shiftDate, subList);
            } else if (shiftList.size() < SHIFT_SEQUENCE.size()) {
                // 填充
                int startIndex = shiftList.size();
                List<String> subList = SHIFT_SEQUENCE.subList(startIndex, SHIFT_SEQUENCE.size());
                long preSequence = Optional.ofNullable(shiftList.get(startIndex - 1).getSequence()).orElse(0L);
                for (String shiftCode : subList) {
                    HmeWoCalendarShiftVO calendarShift = new HmeWoCalendarShiftVO();
                    preSequence += 1;
                    calendarShift.setShiftDate(shiftDate);
                    calendarShift.setEditableFlag(0);
                    calendarShift.setShiftCode(shiftCode);
                    calendarShift.setSequence(preSequence);
                    shiftList.add(calendarShift);
                }
            }
            shiftList.get(0).setShiftCode(SHIFT_SEQUENCE.get(0));
            shiftList.get(1).setShiftCode(SHIFT_SEQUENCE.get(1));
            shiftList.get(2).setShiftCode(SHIFT_SEQUENCE.get(2));
            newList.addAll(shiftList);
        });
        return newList;
    }

    /**
     * 工段与日历关系获取
     *
     * @param tenantId      租户
     * @param workcellIdSet 工段
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/20 02:09:03
     */
    private Map<String, String> shiftMapGet(Long tenantId, Set<String> workcellIdSet) {
        Map<String, String> wkcCalMap = new HashMap<>(MAP_DEFAULT_CAPACITY);
        for (String workcellId : workcellIdSet) {
            // 获取车间日历信息
            MtCalendarVO2 calendarVO = new MtCalendarVO2();
            calendarVO.setCalendarType(HmeConstants.ApiConstantValue.STANDARD);
            calendarVO.setSiteType(HmeConstants.ApiConstantValue.MANUFACTURING);
            calendarVO.setOrganizationType(HmeConstants.ApiConstantValue.WORKCELL);
            calendarVO.setOrganizationId(workcellId);
            String calendarId = calendarRepository.organizationLimitOnlyCalendarGet(tenantId, calendarVO);
            wkcCalMap.put(workcellId, calendarId);
        }
        return wkcCalMap;
    }

    @Override
    public List<HmeWoDispatchVO> woDispatchListQuery(Long tenantId, String prodLineId, List<String> workOrderIdList) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        List<HmeWoDispatchVO> list = new ArrayList<>();
        // 获取当前用户站点信息
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        if (StringUtils.isEmpty(siteId)) {
            return list;
        }
        // 获取工单明细到工段的信息
        List<HmeWoDispatchWkcVO> detailList = hmeWoDispatchRecodeRepository.selectDispatchDetailList(tenantId, siteId, prodLineId, userId, workOrderIdList);
        if (CollectionUtils.isEmpty(detailList)) {
            return list;
        }
        // 获取所有要查询的工段
        Set<String> workcellIdSet = detailList.stream().map(HmeWoDispatchWkcVO::getWorkcellId).collect(Collectors.toSet());
        // 查询班次日历
        Map<String, String> wkcCalMap = shiftMapGet(tenantId, workcellIdSet);
        // 查询日历下日期范围内工段对应日历的所有的班次信息
        Date today = DatetimeUtils.getBeginOfDate(new Date());
        Date dateFrom = DateUtils.addDays(today, -1);
        Date dateTo = DateUtils.addDays(today, 14);
        // 记录工段和班次日历关系
        Map<String, List<HmeWoCalendarShiftVO>> calendarShiftMap = new HashMap<>(MAP_DEFAULT_CAPACITY);
        for (Map.Entry<String, String> entry : wkcCalMap.entrySet()) {
            List<HmeWoCalendarShiftVO> calendarShiftList = hmeWoDispatchRecodeMapper.selectCalendarShiftByDateRange(tenantId, dateFrom, dateTo, entry.getValue());
            calendarShiftMap.put(entry.getKey(), calendarShiftList);
        }
        Map<String, List<HmeWoCalendarShiftVO>> wkcDispatchMap = new HashMap<>(MAP_DEFAULT_CAPACITY);
        // 查询日历下日期范围内工段对应日历的所有的派工信息
        for (Map.Entry<String, String> entry : wkcCalMap.entrySet()) {
            String workcellId = entry.getKey();
            String value = entry.getValue();
            List<HmeWoCalendarShiftVO> hmeCalendarShiftList = hmeWoDispatchRecodeMapper.dispatchShiftDateQuery(tenantId, dateFrom, dateTo, value, workcellId, workOrderIdList);
            Map<String, List<HmeWoCalendarShiftVO>> woDispatchMap = hmeCalendarShiftList.stream().collect(Collectors.groupingBy(HmeWoCalendarShiftVO::getWorkOrderId));
            for (Map.Entry<String, List<HmeWoCalendarShiftVO>> woMap : woDispatchMap.entrySet()) {
                wkcDispatchMap.put(workcellId + "-" + woMap.getKey(), woMap.getValue());
            }
        }
        // 先按照产品维度汇总工单数据
        Map<HmeWoDispatchVO, List<HmeWoDispatchWkcVO>> productMap = detailList.stream().collect(Collectors.groupingBy(HmeWoDispatchWkcVO::summaryProduct));
        for (Map.Entry<HmeWoDispatchVO, List<HmeWoDispatchWkcVO>> entry : productMap.entrySet()) {
            HmeWoDispatchVO product = entry.getKey();
            List<HmeWoDispatchWkcVO> woDetail = entry.getValue();
            BigDecimal woQty = woDetail.stream().map(HmeWoDispatchWkcVO::getWoQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal completedQty = woDetail.stream().map(HmeWoDispatchWkcVO::getCompletedQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            product.setCompleteTotalQty(DECIMAL_FORMAT.format(completedQty) + "/" + DECIMAL_FORMAT.format(woQty));
            List<HmeWoDispatchWorkOrderVO> workOrderList = new ArrayList<>();
            // 按照工单维度汇总数据
            Map<HmeWoDispatchWorkOrderVO, List<HmeWoDispatchWkcVO>> woMap = woDetail.stream().collect(Collectors.groupingBy(HmeWoDispatchWkcVO::summaryWorkOrder));
            for (Map.Entry<HmeWoDispatchWorkOrderVO, List<HmeWoDispatchWkcVO>> e : woMap.entrySet()) {
                HmeWoDispatchWorkOrderVO workOrder = e.getKey();
                List<HmeWoDispatchWkcVO> wkcList = e.getValue();
                // 按照工段和工单维度
                for (HmeWoDispatchWkcVO wkc : wkcList) {
                    List<HmeWoCalendarShiftVO> calendarShiftList = new ArrayList<>();
                    try {
                        calendarShiftList = CollectionCommonUtils.deepCopy(calendarShiftMap.get(wkc.getWorkcellId()));
                    } catch (IOException | ClassNotFoundException ex) {
                        log.info(Arrays.toString(ex.getStackTrace()));
                    }
                    // 查询当前工段和工单是否有派工数据
                    if (wkcDispatchMap.containsKey(wkc.getWorkcellId() + "-" + workOrder.getWorkOrderId())) {
                        List<HmeWoCalendarShiftVO> woCalendarShiftList = wkcDispatchMap.get(wkc.getWorkcellId() + "-" + workOrder.getWorkOrderId());
                        Map<String, List<HmeWoCalendarShiftVO>> shiftMap = woCalendarShiftList.stream().collect(Collectors.groupingBy(HmeWoCalendarShiftVO::getCalendarShiftId));
                        calendarShiftList.forEach(rec -> {
                            if (shiftMap.containsKey(rec.getCalendarShiftId())) {
                                HmeWoCalendarShiftVO dispatch = shiftMap.get(rec.getCalendarShiftId()).get(0);
                                rec.setDispatchQty(dispatch.getDispatchQty());
                                rec.setEditableFlag(dispatch.getEditableFlag());
                                rec.setDocCreatedFlag(dispatch.getDocCreatedFlag());
                            }
                        });
                    }
                    // 先按照重新排序再补全日历数据
                    calendarShiftList = calendarShiftList.stream().sorted((c1, c2) -> {
                        if (c1.getShiftDate().equals(c2.getShiftDate())) {
                            return c1.getSequence().compareTo(c2.getSequence());
                        } else {
                            return c1.getShiftDate().compareTo(c2.getShiftDate());
                        }
                    }).collect(Collectors.toList());
                    calendarShiftList = this.processQueryResult(calendarShiftList);
                    wkc.setCalendarShiftList(calendarShiftList);
                }
                workOrder.setDetailList(wkcList);
                workOrderList.add(workOrder);
            }
            product.setWorkOrderList(workOrderList);
            list.add(product);
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveDispatchRecodeBatchForUi(Long tenantId, List<HmeWoDispatchDTO> dtoList) {
        int limit;
        try {
            String dateLimit = Optional.ofNullable(profileClient.getProfileValueByOptions(WmsConstant.Profile.WMS_DISPATCH_DATE_LIMIT)).orElse("0");
            limit = Math.max(Integer.parseInt(dateLimit), 0);
        } catch (Exception e) {
            limit = 0;
        }
        Date limitDate = DatetimeUtils.getEndOfDate(DateUtils.addDays(DatetimeUtils.getBeginOfDate(new Date()), limit - 1));
        if (CollectionUtils.isNotEmpty(dtoList)) {
            if (dtoList.stream().anyMatch(item -> item.getWorkOrderId() == null || "".equals(item.getWorkOrderId()))) {
                throw new MtException(HmeConstants.ErrorCode.HME_WO_DISPATCH_0001, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        HmeConstants.ErrorCode.HME_WO_DISPATCH_0001, HmeConstants.ConstantValue.HME, HmeConstants.ParameterCode.P_WORK_ORDER_ID, ""));
            }
            if (dtoList.stream().anyMatch(item -> item.getProdLineId() == null || "".equals(item.getProdLineId()))) {
                throw new MtException(HmeConstants.ErrorCode.HME_WO_DISPATCH_0001, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        HmeConstants.ErrorCode.HME_WO_DISPATCH_0001, HmeConstants.ConstantValue.HME, HmeConstants.ParameterCode.P_PROD_LINE_ID, ""));
            }
            if (dtoList.stream().anyMatch(item -> item.getWorkcellId() == null || "".equals(item.getWorkcellId()))) {
                throw new MtException(HmeConstants.ErrorCode.HME_WO_DISPATCH_0001, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        HmeConstants.ErrorCode.HME_WO_DISPATCH_0001, HmeConstants.ConstantValue.HME, HmeConstants.ParameterCode.P_WORKCELL_ID, ""));
            }
            if (dtoList.stream().anyMatch(item -> item.getCalendarShiftId() == null || "".equals(item.getCalendarShiftId()))) {
                throw new MtException(HmeConstants.ErrorCode.HME_WO_DISPATCH_0001, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        HmeConstants.ErrorCode.HME_WO_DISPATCH_0001, HmeConstants.ConstantValue.HME, HmeConstants.ParameterCode.P_CALENDAR_SHIFT_ID, ""));
            }
            if (dtoList.stream().anyMatch(item -> item.getWoQty() == null)) {
                throw new MtException(HmeConstants.ErrorCode.HME_WO_DISPATCH_0001, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        HmeConstants.ErrorCode.HME_WO_DISPATCH_0001, HmeConstants.ConstantValue.HME, HmeConstants.ParameterCode.P_WO_QTY, ""));
            }
            if (limit > 0 && dtoList.stream().anyMatch(rec -> rec.getShiftDate().before(limitDate))) {
                throw new MtException(HME_WO_DISPATCH_0003, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        HME_WO_DISPATCH_0003, HmeConstants.ConstantValue.HME, DateUtil.date2String(limitDate, "yyyy-MM-dd")));
            }
            List<String> calendarShiftIds = dtoList.stream().map(HmeWoDispatchDTO::getCalendarShiftId).collect(Collectors.toList());
            calendarShiftIds = calendarShiftIds.stream().distinct().collect(Collectors.toList());
            for (HmeWoDispatchDTO dto : dtoList) {
                HmeWoDispatchRecode recode = new HmeWoDispatchRecode();
                // 数量校验
                List<HmeWoDispatchDTO> currDtoList = dtoList.stream().filter(item -> dto.getWorkOrderId().equals(item.getWorkOrderId()) &&
                        dto.getProdLineId().equals(item.getProdLineId()) && dto.getWorkcellId().equals(item.getWorkcellId())).collect(Collectors.toList());
                // 界面提交数量
                double sumQty = currDtoList.stream().map(HmeWoDispatchDTO::getDispatchQty)
                        .filter(Objects::nonNull).collect(Collectors.toList()).stream().mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                // 界面派工总数 + 工段完工数量 + 数据库派工总数
                Double totalQty = sumQty + hmeWoDispatchRecodeMapper.dispatchQtyGet(dto.getWorkOrderId(), dto.getProdLineId(), dto.getWorkcellId(), calendarShiftIds);
                if (totalQty.compareTo(dto.getWoQty().doubleValue()) > 0) {
                    throw new MtException(HmeConstants.ErrorCode.HME_WO_DISPATCH_0002, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            HmeConstants.ErrorCode.HME_WO_DISPATCH_0002, HmeConstants.ConstantValue.HME, totalQty.toString(), dto.getWoQty().toString()));
                }
                // 查询数据是否存在
                HmeWoDispatchRecode queryRecode = hmeWoDispatchRecodeMapper.selectOne(new HmeWoDispatchRecode(tenantId,
                        dto.getWorkOrderId(), dto.getProdLineId(), dto.getWorkcellId(), dto.getCalendarShiftId()));
                if (queryRecode == null) {
                    BeanUtils.copyProperties(dto, recode);
                    recode.setTenantId(tenantId);
                    hmeWoDispatchRecodeRepository.insertSelective(recode);
                    if (recode.getDispatchQty().compareTo(BigDecimal.ZERO) > 0) {
                        // 插入组件需求
                        wmsComponentDemandRecordService.insertDemandRecord(tenantId, dto.getShiftCode(), recode.getWoDispatchId(), dto.getDispatchQty());
                    }
                } else {
                    HmeWoDispatchRecode dispatchRecode = hmeWoDispatchRecodeMapper.selectByPrimaryKey(queryRecode.getWoDispatchId());
                    queryRecode.setDispatchQty(dto.getDispatchQty());
                    OptionalHelper.optional(Collections.singletonList(HmeWoDispatchRecode.FIELD_DISPATCH_QTY));
                    hmeWoDispatchRecodeMapper.updateOptional(queryRecode);
                    // 更新组件需求
                    wmsComponentDemandRecordService.updateDemandRecord(tenantId, dto.getShiftCode(), queryRecode.getWoDispatchId(), dto.getDispatchQty(), dispatchRecode.getDispatchQty());
                }
            }
        }
    }

    @Override
    public List<HmeWoDispatchVO6> woProdLineListQuery(Long tenantId, String prodLineId) {
        List<HmeWoDispatchVO6> list = new ArrayList<>();
        HmeWoDispatchVO6 prodLineWkc = new HmeWoDispatchVO6();
        // 获取车间对应产线及工段
        List<HmeWoDispatchVO4> prodLineWkcList = hmeWoDispatchRecodeMapper.prodLineWkcQuery(tenantId, prodLineId);
        if (CollectionUtils.isEmpty(prodLineWkcList)) {
            return new ArrayList<>();
        }
        // 获取当日 前1个工作日
        Date dateFrom = hmeWoDispatchRecodeMapper.shiftDateFromGet();
        // 获取当日 后14工作日
        Date dateTo = hmeWoDispatchRecodeMapper.shiftDateToGet();

        for (HmeWoDispatchVO4 vo : prodLineWkcList) {
            // 获取车间日历信息
            MtCalendarVO2 calendarVO = new MtCalendarVO2();
            calendarVO.setCalendarType(HmeConstants.ApiConstantValue.STANDARD);
            calendarVO.setSiteType(HmeConstants.ApiConstantValue.MANUFACTURING);
            calendarVO.setOrganizationType(HmeConstants.ApiConstantValue.WORKCELL);
            calendarVO.setOrganizationId(vo.getWorkcellId());
            String calendarId = calendarRepository.organizationLimitOnlyCalendarGet(tenantId, calendarVO);
            List<HmeWoCalendarShiftVO> wkcCalendarShiftList = hmeWoDispatchRecodeMapper.selectCalendarShiftByDateRange(tenantId, dateFrom, dateTo, calendarId);

            // 获取工单信息
            List<HmeWoDispatchVO5> woProdList = hmeWoDispatchRecodeMapper.woProdListQuery(tenantId, vo.getProdLineId(), vo.getWorkcellId());
            List<String> workOrderList = woProdList.stream().map(HmeWoDispatchVO5::getWorkOrderId).collect(Collectors.toList());
            List<HmeWoCalendarShiftVO> woCalendarShiftList = hmeWoDispatchRecodeMapper.dispatchShiftDateQuery(tenantId, dateFrom, dateTo, calendarId, vo.getWorkcellId(), workOrderList);
            Map<String, List<HmeWoCalendarShiftVO>> calMap = woCalendarShiftList.stream().collect(Collectors.groupingBy(HmeWoCalendarShiftVO::getWorkOrderId));

            HmeWoCalendarShiftVO2 calendarShiftVO = new HmeWoCalendarShiftVO2();
            calendarShiftVO.setCalendarId(calendarId);
            calendarShiftVO.setShiftDateFrom(dateFrom);
            calendarShiftVO.setShiftDateTo(dateTo);

            List<HmeWoCalendarShiftVO> totalCalShiftList = new ArrayList<>();
            for (HmeWoDispatchVO5 woProdVo : woProdList) {
                // 获取工单对应日历工班信息
                List<HmeWoCalendarShiftVO> calendarShiftList = new ArrayList<>();
                try {
                    calendarShiftList = CollectionCommonUtils.deepCopy(wkcCalendarShiftList);
                } catch (IOException | ClassNotFoundException ex) {
                    log.info(Arrays.toString(ex.getStackTrace()));
                }
                // 查询当前工段和工单是否有派工数据
                if (calMap.containsKey(woProdVo.getWorkOrderId())) {
                    List<HmeWoCalendarShiftVO> dispatchCalendarShiftList = calMap.get(woProdVo.getWorkOrderId());
                    Map<String, List<HmeWoCalendarShiftVO>> shiftMap = dispatchCalendarShiftList.stream().collect(Collectors.groupingBy(HmeWoCalendarShiftVO::getCalendarShiftId));
                    calendarShiftList.forEach(rec -> {
                        if (shiftMap.containsKey(rec.getCalendarShiftId())) {
                            HmeWoCalendarShiftVO dispatch = shiftMap.get(rec.getCalendarShiftId()).get(0);
                            rec.setDispatchQty(dispatch.getDispatchQty());
                            rec.setEditableFlag(dispatch.getEditableFlag());
                            rec.setDocCreatedFlag(dispatch.getDocCreatedFlag());
                        }
                    });
                }
                // 先按照重新排序再补全日历数据
                calendarShiftList = calendarShiftList.stream().sorted((c1, c2) -> {
                    if (c1.getShiftDate().equals(c2.getShiftDate())) {
                        return c1.getSequence().compareTo(c2.getSequence());
                    } else {
                        return c1.getShiftDate().compareTo(c2.getShiftDate());
                    }
                }).collect(Collectors.toList());
                calendarShiftList = this.processQueryResult(calendarShiftList);
                woProdVo.setHmeCalendarShiftList(calendarShiftList);
                totalCalShiftList.addAll(calendarShiftList);
            }
            // 合计列
            HmeWoDispatchVO5 totalColumn = totalColumnListGet(tenantId, totalCalShiftList, vo.getWorkcellId());
            woProdList.add(totalColumn);
            vo.setWoProdList(woProdList);
        }
        prodLineWkc.setProdLineWkcList(prodLineWkcList);
        list.add(prodLineWkc);
        return list;
    }

    @Override
    public List<HmeWoDispatchSuiteVO> suiteQuery(Long tenantId, List<HmeWoDispatchSuiteQueryDTO> queryList, String siteId) {
        List<HmeWoDispatchSuiteVO> list = new ArrayList<>(queryList.size());
        if (CollectionUtils.isNotEmpty(queryList)) {
            // 计算仓库库存,分有无物料版本分维度汇总
            List<WmsDistributionQtyVO> qtyList = wmsComponentDemandRecordRepository.selectBarcodeOnhandBySite(tenantId, siteId);
            Map<WmsDistributionQtyVO, BigDecimal> map = qtyList.stream().collect(Collectors.groupingBy(rec ->
                    new WmsDistributionQtyVO().setMaterialId(rec.getMaterialId()).setMaterialVersion(rec.getMaterialVersion()).setSoNum(rec.getSoNum()).setSoLineNum(rec.getSoLineNum()), CollectorsUtil.summingBigDecimal(WmsDistributionQtyVO::getQuantity)));
            Map<WmsDistributionQtyVO, BigDecimal> mapIgnoreVer = qtyList.stream().collect(Collectors.groupingBy(rec ->
                    new WmsDistributionQtyVO().setMaterialId(rec.getMaterialId()).setSoNum(rec.getSoNum()).setSoLineNum(rec.getSoLineNum()), CollectorsUtil.summingBigDecimal(WmsDistributionQtyVO::getQuantity)));

            // 循环获取仓库库存
            queryList.forEach(rec -> {
                // 计算组件齐套列表
                List<HmeWoDispatchComponentSuiteVO> componentList = hmeWoDispatchRecodeMapper.selectDispatchComponentSuiteList(tenantId, HmeWoDispatchCompSuiteQueryDTO.builder(rec.getWorkOrderId(), rec.getWorkcellId()));
                componentList.forEach(comp -> {
                    comp.setInStockQty(BigDecimal.ZERO);
                    WmsDistributionQtyVO key = new WmsDistributionQtyVO();
                    key.setMaterialId(comp.getMaterialId());
                    key.setSoNum(comp.getSoNum());
                    key.setSoLineNum(comp.getSoLineNum());
                    if (StringUtils.isBlank(comp.getMaterialVersion())) {
                        if (mapIgnoreVer.containsKey(key)) {
                            comp.setInStockQty(mapIgnoreVer.get(key));
                        }
                    } else {
                        key.setMaterialVersion(comp.getMaterialVersion());
                        if (map.containsKey(key)) {
                            comp.setInStockQty(map.get(key));
                        }
                    }
                    comp.setSuiteQty(comp.getInStockQty().compareTo(BigDecimal.ZERO) <= 0 ? 0L : comp.getInStockQty().divide(comp.getUsageQty(), 0, BigDecimal.ROUND_FLOOR).longValue());
                });
                // 取得最小值作为工单在工段上的齐套数量，说明齐套数量为0
                Long suiteQty = componentList.stream().map(HmeWoDispatchComponentSuiteVO::getSuiteQty).reduce(Long::min).orElse(0L);
                // 组合数据
                HmeWoDispatchSuiteVO suite = new HmeWoDispatchSuiteVO();
                suite.setWorkcellId(rec.getWorkcellId());
                suite.setWorkOrderId(rec.getWorkOrderId());
                suite.setSuiteQty(suiteQty);
                list.add(suite);
            });
        }
        return list;
    }

    @Override
    public Page<HmeWoDispatchComponentSuiteVO> suiteComponentQuery(Long tenantId, HmeWoDispatchCompSuiteQueryDTO dto, PageRequest pageRequest) {
        Page<HmeWoDispatchComponentSuiteVO> page = PageHelper.doPage(pageRequest, () -> hmeWoDispatchRecodeMapper.selectDispatchComponentSuiteList(tenantId, dto));
        // 计算线边库存和线边套数
        if (page.size() > 0) {
            // 计算仓库库存,分有无物料版本分维度汇总
            List<WmsDistributionQtyVO> qtyList = wmsComponentDemandRecordRepository.selectBarcodeOnhandBySite(tenantId, dto.getSiteId());
            Map<WmsDistributionQtyVO, BigDecimal> qtyMap = qtyList.stream().collect(Collectors.groupingBy(rec ->
                    new WmsDistributionQtyVO().setMaterialId(rec.getMaterialId()).setMaterialVersion(rec.getMaterialVersion()).setSoNum(rec.getSoNum()).setSoLineNum(rec.getSoLineNum()), CollectorsUtil.summingBigDecimal(WmsDistributionQtyVO::getQuantity)));
            Map<WmsDistributionQtyVO, BigDecimal> qtyNoVerMap = qtyList.stream().collect(Collectors.groupingBy(rec ->
                    new WmsDistributionQtyVO().setMaterialId(rec.getMaterialId()).setSoNum(rec.getSoNum()).setSoLineNum(rec.getSoLineNum()), CollectorsUtil.summingBigDecimal(WmsDistributionQtyVO::getQuantity)));

            // 计算线边库存,分有无物料版本分维度汇总
            List<String> workcellIdList = page.getContent().stream().map(HmeWoDispatchComponentSuiteVO::getWorkcellId).collect(Collectors.toList());
            List<WmsDistributionQtyVO> workcellQtyList = distributionDemandRepository.selectWorkcellQtyBatch(tenantId, dto.getSiteId(), workcellIdList);
            Map<WmsDistributionQtyVO, BigDecimal> workcellMap = workcellQtyList.stream().collect(Collectors.groupingBy(rec ->
                    new WmsDistributionQtyVO().setMaterialId(rec.getMaterialId()).setMaterialVersion(rec.getMaterialVersion()).setSoNum(rec.getSoNum()).setSoLineNum(rec.getSoLineNum()).setWorkcellId(rec.getWorkcellId()), CollectorsUtil.summingBigDecimal(WmsDistributionQtyVO::getQuantity)));
            Map<WmsDistributionQtyVO, BigDecimal> workcellNoVerMap = qtyList.stream().collect(Collectors.groupingBy(rec ->
                    new WmsDistributionQtyVO().setMaterialId(rec.getMaterialId()).setSoNum(rec.getSoNum()).setSoLineNum(rec.getSoLineNum()).setWorkcellId(rec.getWorkcellId()), CollectorsUtil.summingBigDecimal(WmsDistributionQtyVO::getQuantity)));

            page.forEach(comp -> {
                comp.setInStockQty(BigDecimal.ZERO);
                comp.setWorkcellQty(BigDecimal.ZERO);
                WmsDistributionQtyVO key = new WmsDistributionQtyVO();
                key.setMaterialId(comp.getMaterialId());
                key.setSoNum(comp.getSoNum());
                key.setSoLineNum(comp.getSoLineNum());
                if (StringUtils.isBlank(comp.getMaterialVersion())) {
                    if (qtyNoVerMap.containsKey(key)) {
                        comp.setInStockQty(qtyNoVerMap.get(key));
                    }
                } else {
                    key.setMaterialVersion(comp.getMaterialVersion());
                    if (qtyMap.containsKey(key)) {
                        comp.setInStockQty(qtyMap.get(key));
                    }
                }
                // 获取线边数量
                key.setWorkcellId(comp.getWorkcellId());
                if (StringUtils.isBlank(key.getMaterialVersion())) {
                    if (workcellNoVerMap.containsKey(key)) {
                        comp.setWorkcellQty(workcellNoVerMap.get(key));
                    }
                } else {
                    if (workcellMap.containsKey(key)) {
                        comp.setWorkcellQty(workcellMap.get(key));
                    }
                }
                // 计算套数
                comp.setSuiteQty(comp.getInStockQty().compareTo(BigDecimal.ZERO) <= 0 ? 0L : comp.getInStockQty().divide(comp.getUsageQty(), 0, BigDecimal.ROUND_FLOOR).longValue());
                comp.setWorkcellSuiteQty(comp.getWorkcellQty().compareTo(BigDecimal.ZERO) <= 0 ? 0L : comp.getWorkcellQty().divide(comp.getUsageQty(), 0, BigDecimal.ROUND_FLOOR).longValue());
            });
        }
        return page;
    }

    private HmeWoDispatchVO5 totalColumnListGet(Long tenantId, List<HmeWoCalendarShiftVO> totalCalShiftList, String workcellId) {
        HmeWoDispatchVO5 totalColumn = new HmeWoDispatchVO5();
        List<HmeWoCalendarShiftVO> totalColumnCalList = new ArrayList<>();
        totalColumn.setTotal(HmeConstants.CnConstantValue.TOTAL);
        // 获取当前WKC的计划属性
        MtModWorkcellSchedule mtModWorkcellSchedule = mtModWorkcellScheduleRepository.workcellSchedulePropertyGet(tenantId, workcellId);
        double ratio = 0;
        if (mtModWorkcellSchedule == null) {
            return totalColumn;
        }
        switch (mtModWorkcellSchedule.getRateType()) {
            case HmeConstants.RateType.SECOND:
                ratio = 3600 / mtModWorkcellSchedule.getRate() * mtModWorkcellSchedule.getActivity() / 100;
                break;
            case HmeConstants.RateType.PERHOUR:
                ratio = mtModWorkcellSchedule.getRate() * mtModWorkcellSchedule.getActivity() / 100;
                break;
            default:
                break;
        }
        Map<HmeCalendarShiftVO, List<HmeWoCalendarShiftVO>> shiftMap = totalCalShiftList.stream().collect(Collectors.groupingBy(HmeWoCalendarShiftVO::convertCalendarShift));
        for (HmeCalendarShiftVO calendarShift : shiftMap.keySet().stream().sorted().collect(Collectors.toList())) {
            List<HmeWoCalendarShiftVO> list = shiftMap.get(calendarShift);
            HmeWoCalendarShiftVO totalColumnCal = new HmeWoCalendarShiftVO();
            totalColumnCal.setCalendarShiftId(calendarShift.getCalendarShiftId());
            totalColumnCal.setShiftCode(calendarShift.getShiftCode());
            totalColumnCal.setShiftDate(calendarShift.getShiftDate());
            if (StringUtils.isNotBlank(calendarShift.getCalendarShiftId())) {
                BigDecimal sumQty = list.stream().map(HmeWoCalendarShiftVO::getDispatchQty).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                // 计算工作时长(毫秒数)
                double wtMillisecond = calendarShift.getShiftEndTime().getTime() - calendarShift.getShiftStartTime().getTime();
                // dispatchQty求和除以最大产能（百分比显示）
                String totalQty = null;
                double restTime = Optional.ofNullable(calendarShift.getRestTime()).orElse(0D);
                double v = ratio * (wtMillisecond / (1000 * 60 * 60) - restTime);
                if (v != 0) {
                    totalQty = String.format("%.2f", (sumQty.doubleValue() / (v / 100))) + "%";
                }
                totalColumnCal.setDispatchQty(sumQty);
                if (totalQty != null) {
                    totalColumnCal.setTotalQty(totalQty);
                }
            }
            totalColumnCalList.add(totalColumnCal);
        }
        totalColumn.setHmeCalendarShiftList(totalColumnCalList);
        return totalColumn;
    }
}
