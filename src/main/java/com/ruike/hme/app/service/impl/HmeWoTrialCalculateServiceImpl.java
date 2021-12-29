package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeWoTrialCalculateReportQueryDTO;
import com.ruike.hme.api.dto.HmeWoTrialCalculateReportSaveDTO;
import com.ruike.hme.app.service.HmeWoTrialCalculateService;
import com.ruike.hme.domain.entity.HmeWoTrialCalculate;
import com.ruike.hme.domain.repository.HmeWoTrialCalculateRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.infra.util.DatetimeUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.calendar.domain.repository.MtCalendarRepository;
import tarzan.calendar.domain.vo.MtCalendarVO2;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

/**
 * 工单入库日期试算表应用服务默认实现
 *
 * @author yuchao.wang@hand-china.com 2020-08-25 21:54:56
 */
@Slf4j
@Service
public class HmeWoTrialCalculateServiceImpl implements HmeWoTrialCalculateService {

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtCalendarRepository calendarRepository;

    @Autowired
    private HmeWoTrialCalculateRepository hmeWoTrialCalculateRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    /**
     * 默认报表时间跨度
     */
    private static final int DEFAULT_TIME_SPAN = 45;


    /**
     *
     * @Description 查询试算报表
     *
     * @author yuchao.wang
     * @date 2020/8/27 10:15
     * @param tenantId 租户ID
     * @param dto 参数
     * @return com.ruike.hme.domain.vo.HmeWoTrialCalculateReportVO
     *
     */
    @Override
    public HmeWoTrialCalculateReportVO queryReport(Long tenantId, HmeWoTrialCalculateReportQueryDTO dto) {
        //非空校验
        if(StringUtils.isEmpty(dto.getProductionLineId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "产线"));
        }

        //查询交期报表数据
        List<HmeWoTrialCalculateReportWoVO> hmeWoTrialCalculateReportWoVOList = hmeWoTrialCalculateRepository.queryTrialCalculateReport(tenantId, dto);
        if(CollectionUtils.isEmpty(hmeWoTrialCalculateReportWoVOList)){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "工单信息"));
        }

        //获取当前报表时间跨度，默认45天
        List<LocalDate> reportTimeSpanList = DatetimeUtils.getDateListInRange(LocalDate.now(), LocalDate.now().plusDays(DEFAULT_TIME_SPAN));

        //初始化返回数据
        int index = 0;
        HmeWoTrialCalculateReportVO reportVO = new HmeWoTrialCalculateReportVO();
        reportVO.setReportTimeSpanList(reportTimeSpanList);
        Long[] prodLineAbilityArray = new Long[DEFAULT_TIME_SPAN];
        BigDecimal[] prodLineAbilityRateArray = new BigDecimal[DEFAULT_TIME_SPAN];

        //循环每个工单构造日期部分数据
        for(HmeWoTrialCalculateReportWoVO woVO : hmeWoTrialCalculateReportWoVOList){
            //将表中数据转换为Map
            Map<LocalDate, HmeWoTrialCalculateReportWoDetailVO> trialCalculateMap = new HashMap<>();
            woVO.getDetailList().forEach(item -> trialCalculateMap.put(item.getShiftDate(), item));

            //循环时间区间计算报表数据
            List<HmeWoTrialCalculateReportWoDetailVO> detailList = new LinkedList<>();
            index = 0;
            for(LocalDate date : reportTimeSpanList) {
                HmeWoTrialCalculateReportWoDetailVO woDetailVO = new HmeWoTrialCalculateReportWoDetailVO(date);
                if(trialCalculateMap.containsKey(date)){
                    woDetailVO = trialCalculateMap.get(date);
                }

                //计算产线负荷
                prodLineAbilityArray[index] = Objects.isNull(prodLineAbilityArray[index]) ?
                        woDetailVO.getTrialQty() : prodLineAbilityArray[index] + woDetailVO.getTrialQty();

                //计算产线负荷比例
                BigDecimal rate = new BigDecimal(woDetailVO.getTrialQty()).multiply(new BigDecimal(100))
                        .divide(woVO.getMaxAbility(), 2, BigDecimal.ROUND_HALF_UP);
                prodLineAbilityRateArray[index] = Objects.isNull(prodLineAbilityRateArray[index]) ?
                        rate : prodLineAbilityRateArray[index].add(rate);

                detailList.add(woDetailVO);
                index++;
            }
            woVO.setDetailList(detailList);
        }

        //组装产线负荷/符合比例
        List<HmeWoTrialCalculateReportAbilityVO> prodLineAbilityList = new LinkedList<>();
        List<HmeWoTrialCalculateReportAbilityRateVO> prodLineAbilityRateList = new LinkedList<>();
        index = 0;
        for(LocalDate date : reportTimeSpanList) {
            prodLineAbilityList.add(new HmeWoTrialCalculateReportAbilityVO(date, prodLineAbilityArray[index]));
            prodLineAbilityRateList.add(new HmeWoTrialCalculateReportAbilityRateVO(date, prodLineAbilityRateArray[index]));
            index++;
        }

        reportVO.setProdLineAbilityList(prodLineAbilityList);
        reportVO.setProdLineAbilityRateList(prodLineAbilityRateList);
        reportVO.setWoList(hmeWoTrialCalculateReportWoVOList);
        return reportVO;
    }


    /**
     *
     * @Description 试算报表重排
     *
     * @author yuchao.wang
     * @date 2020/8/27 11:33
     * @param tenantId 租户ID
     * @param workOrderId 工单ID
     * @return
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reschedule(Long tenantId, String workOrderId) {
        //非空校验
        if(StringUtils.isEmpty(workOrderId)){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工单ID"));
        }

        //删除工单下的数据
        if(hmeWoTrialCalculateRepository.hasTrialCalculateDataByWoId(tenantId, workOrderId)) {
            hmeWoTrialCalculateRepository.deleteByWorkOrderId(tenantId, workOrderId);
        }
    }

    /**
     *
     * @Description 试算报表保存
     *
     * @author yuchao.wang
     * @date 2020/8/27 13:58
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Long tenantId, HmeWoTrialCalculateReportSaveDTO dto) {
        //非空校验
        if(StringUtils.isEmpty(dto.getProductionLineId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "产线ID"));
        }
        if(StringUtils.isEmpty(dto.getWorkOrderId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工单ID"));
        }
        if(Objects.isNull(dto.getDateFrom())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "开始时间"));
        }
        if(Objects.isNull(dto.getDateTo())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "结束时间"));
        }
        if(dto.getDateFrom().isAfter(dto.getDateTo())) {
            throw new MtException("Exception", "开始时间不能大于结束时间");
        }
        if(dto.getDateTo().isAfter(LocalDate.now().plusDays(DEFAULT_TIME_SPAN))){
            throw new MtException("Exception", "开始时间和结束时间应在" + DEFAULT_TIME_SPAN + "天内");
        }

        //判断工单下是否有数据
        if(hmeWoTrialCalculateRepository.hasTrialCalculateDataByWoId(tenantId, dto.getWorkOrderId())) {
            log.info("<====HmeWoTrialCalculateServiceImpl.save: 工单【{}】自动重排！", dto.getWorkOrderId());
            self().reschedule(tenantId, dto.getWorkOrderId());
        }

        //获取当前产线工作日历
        MtCalendarVO2 calendarVO = new MtCalendarVO2();
        calendarVO.setOrganizationType(HmeConstants.ApiConstantValue.PROD_LINE);
        calendarVO.setOrganizationId(dto.getProductionLineId());
        calendarVO.setCalendarType(HmeConstants.ApiConstantValue.STANDARD);
        calendarVO.setSiteType(HmeConstants.ApiConstantValue.MANUFACTURING);
        String calendarId = calendarRepository.organizationLimitOnlyCalendarGet(tenantId, calendarVO);
        if(StringUtils.isEmpty(calendarId)){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "产线对应的工作日历"));
        }

        //查询期间的工作日历天
        HmeWoCalendarShiftVO3 woCalendarShiftVO3 = new HmeWoCalendarShiftVO3();
        woCalendarShiftVO3.setCalendarId(calendarId);
        woCalendarShiftVO3.setShiftDateFrom(dto.getDateFrom());
        woCalendarShiftVO3.setShiftDateTo(dto.getDateTo());
        List<LocalDate> shiftDateList = hmeWoTrialCalculateRepository.queryCalendarShiftByTime(tenantId, woCalendarShiftVO3);
        if(CollectionUtils.isEmpty(shiftDateList)){
            throw new MtException("Exception", "时间区间内没有有效的工作日历班次");
        }

        //获取工单信息
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if(Objects.isNull(mtWorkOrder) || Objects.isNull(mtWorkOrder.getQty())){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "工单信息"));
        }

        //每日预排数量和余数
        long trialQty = mtWorkOrder.getQty().intValue() / shiftDateList.size();
        long remainder = mtWorkOrder.getQty().intValue() % shiftDateList.size();

        //批量获取序列
        List<String> hmeWoTrialCalculateIdList = customSequence.getNextKeys("hme_wo_trial_calculate_s", shiftDateList.size());
        List<String> hmeWoTrialCalculateCidList = customSequence.getNextKeys("hme_wo_trial_calculate_cid_s", shiftDateList.size());

        //构造要插入的数据
        int index = 0;
        Date now = new Date();
        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        List<HmeWoTrialCalculate> hmeWoTrialCalculateList = new ArrayList<>();
        for(LocalDate date : shiftDateList) {
            HmeWoTrialCalculate woTrialCalculate = new HmeWoTrialCalculate();
            woTrialCalculate.setTenantId(tenantId);
            woTrialCalculate.setShiftDate(date);
            woTrialCalculate.setWorkOrderId(dto.getWorkOrderId());
            woTrialCalculate.setDateFrom(dto.getDateFrom());
            woTrialCalculate.setDateTo(dto.getDateTo());
            woTrialCalculate.setQty(trialQty);
            woTrialCalculate.setObjectVersionNumber(1L);
            woTrialCalculate.setCreatedBy(userId);
            woTrialCalculate.setCreationDate(now);
            woTrialCalculate.setLastUpdatedBy(userId);
            woTrialCalculate.setLastUpdateDate(now);
            woTrialCalculate.setWoTrialCalculateId(hmeWoTrialCalculateIdList.get(index));
            woTrialCalculate.setCid(Long.valueOf(hmeWoTrialCalculateCidList.get(index++)));
            hmeWoTrialCalculateList.add(woTrialCalculate);
        }

        //重新设置最后一天的数量
        hmeWoTrialCalculateList.get(index-1).setQty(trialQty+remainder);

        //批量插入
        hmeWoTrialCalculateRepository.myBatchInsert(hmeWoTrialCalculateList);
    }
}
