package com.ruike.wms.infra.repository.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.api.dto.WmsDistDemandQueryDTO;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.mapper.WmsDistributionDemandDetailMapper;
import com.ruike.wms.infra.mapper.WmsDistributionDemandMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.helper.OptionalHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.util.MtBaseConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.wms.domain.entity.WmsDistributionDemand;
import com.ruike.wms.domain.repository.WmsDistributionDemandRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import tarzan.calendar.domain.repository.MtCalendarRepository;
import tarzan.calendar.domain.vo.MtCalendarVO2;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 配送需求表 资源库实现
 *
 * @author yonghui.zhu@hand-china.com 2020-08-31 11:09:51
 */
@Component
public class WmsDistributionDemandRepositoryImpl extends BaseRepositoryImpl<WmsDistributionDemand> implements WmsDistributionDemandRepository {
    private final WmsDistributionDemandMapper distributionDemandMapper;
    private final WmsDistributionDemandDetailMapper distributionDemandDetailMapper;
    private final MtCalendarRepository calendarRepository;
    private final MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;
    private final MtModLocatorRepository mtModLocatorRepository;

    public WmsDistributionDemandRepositoryImpl(WmsDistributionDemandMapper distributionDemandMapper, WmsDistributionDemandDetailMapper distributionDemandDetailMapper, MtCalendarRepository calendarRepository, MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository, MtModLocatorRepository mtModLocatorRepository) {
        this.distributionDemandMapper = distributionDemandMapper;
        this.distributionDemandDetailMapper = distributionDemandDetailMapper;
        this.calendarRepository = calendarRepository;
        this.mtModLocatorOrgRelRepository = mtModLocatorOrgRelRepository;
        this.mtModLocatorRepository = mtModLocatorRepository;
    }

    private final static List<String> SHIFT_SEQUENCE = Arrays.asList("A", "B", "C");

    private List<WmsDistributionDemandQtyVO> refactorShiftList(List<WmsDistributionDemandQtyVO> list) {
        List<WmsDistributionDemandQtyVO> newList = new ArrayList<>();
        Map<Date, List<WmsDistributionDemandQtyVO>> shiftMap = list.stream().collect(Collectors.groupingBy(WmsDistributionDemandQtyVO::getShiftDate));
        shiftMap.keySet().stream().sorted().forEach(shiftDate -> {
            List<WmsDistributionDemandQtyVO> shiftList = shiftMap.get(shiftDate);
            if (shiftList.size() > SHIFT_SEQUENCE.size()) {
                // 修剪
                List<WmsDistributionDemandQtyVO> subList = list.subList(0, SHIFT_SEQUENCE.size());
                shiftMap.put(shiftDate, subList);
            } else if (shiftList.size() < SHIFT_SEQUENCE.size()) {
                // 填充
                int startIndex = shiftList.size();
                List<String> subList = SHIFT_SEQUENCE.subList(startIndex, SHIFT_SEQUENCE.size());
                long preSequence = Optional.ofNullable(shiftList.get(startIndex - 1).getSequence()).orElse(0L);
                for (String shiftCode : subList) {
                    WmsDistributionDemandQtyVO calendarShift = new WmsDistributionDemandQtyVO();
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

    @Override
    public Page<WmsDistributionDemandVO> selectListByCondition(Long tenantId, WmsDistDemandQueryDTO dto, PageRequest pageRequest) {
        Page<WmsDistributionDemandVO> page = PageHelper.doPage(pageRequest, () -> distributionDemandMapper.selectListByCondition(tenantId, dto));
        if (page.size() != 0) {
            for (WmsDistributionDemandVO rec : page) {
                // 将汇总维度取hash值
                rec.setKeyId(rec.hashCode());
                // 获取车间日历信息
                MtCalendarVO2 calendarVO = new MtCalendarVO2();
                calendarVO.setCalendarType(HmeConstants.ApiConstantValue.STANDARD);
                calendarVO.setSiteType(HmeConstants.ApiConstantValue.MANUFACTURING);
                calendarVO.setOrganizationType(HmeConstants.ApiConstantValue.WORKCELL);
                calendarVO.setOrganizationId(rec.getWorkcellId());
                String calendarId = calendarRepository.organizationLimitOnlyCalendarGet(tenantId, calendarVO);
                // 计算配送货位
                MtModLocatorOrgRelVO3 locatorOrgRel = new MtModLocatorOrgRelVO3();
                locatorOrgRel.setOrganizationId(rec.getWorkcellId());
                locatorOrgRel.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
                List<String> locatorIdList = mtModLocatorOrgRelRepository.organizationLimitLocatorAllQuery(tenantId, locatorOrgRel);
                String locators = "";
                if (CollectionUtils.isNotEmpty(locatorIdList)) {
                    List<MtModLocator> locatorList = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, locatorIdList);
//                    locators = StringUtils.join(locatorList.stream().map(MtModLocator::getLocatorCode).collect(Collectors.toList()), ",");
                    //根据优先级，优先配送库位，没有配送库位的使用默认存储库位
                    Map<String,MtModLocator> locatorMap = locatorList.stream().collect(Collectors.toMap(MtModLocator::getLocatorType,c->c));
                    MtModLocator locatorX = locatorMap.get("27");
                    if(locatorX != null){
                        locators = locatorX.getLocatorCode();
                    }else{
                        MtModLocator locatorY = locatorMap.get("DEFAULT_STORAGE");
                        if(locatorY != null){
                            locators = locatorY.getLocatorCode();
                        }
                    }
                }

                rec.setDistributionLocator(locators);
                // 构建查询条件
                WmsDistributionDemandQueryVO shiftQuery = new WmsDistributionDemandQueryVO();
                BeanUtils.copyProperties(rec, shiftQuery);
                shiftQuery.setCalendarId(calendarId);
                shiftQuery.setStartDate(dto.getStartDate());
                shiftQuery.setEndDate(dto.getEndDate());

                // 查询班次数量列表
                List<WmsDistributionDemandQtyVO> shiftList = distributionDemandMapper.selectQtyByShift(tenantId, shiftQuery);
                // modify by sanfeng.zhang 优化查询 分两段 汇总数量的单独查 根据配送需求汇总 配送明细的已配送数量和库存数量
                List<String> demandIdList = shiftList.stream().map(WmsDistributionDemandQtyVO::getDistDemandId).distinct().collect(Collectors.toList());
                Map<String, List<WmsDistributionDemandQtyVO>> demandQtyMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(demandIdList)) {
                    List<WmsDistributionDemandQtyVO> demandQtyList = distributionDemandMapper.selectQtyByDemandId(tenantId, demandIdList);
                    if (CollectionUtils.isNotEmpty(demandQtyList)) {
                        demandQtyMap = demandQtyList.stream().collect(Collectors.groupingBy(WmsDistributionDemandQtyVO::getDistDemandId));
                    }
                }
                Map<String, List<WmsDistributionDemandQtyVO>> finalDemandQtyMap = demandQtyMap;
                shiftList.forEach(shift -> {
                    List<WmsDistributionDemandQtyVO> qtyVOList = finalDemandQtyMap.get(shift.getDistDemandId());
                    if (CollectionUtils.isNotEmpty(qtyVOList)) {
                        shift.setDeliveredQty(qtyVOList.get(0).getDeliveredQty());
                        shift.setDistributionQty(qtyVOList.get(0).getDistributionQty());
                    }
                    BigDecimal remainQty = shift.getRequestQty().subtract(shift.getDeliveredQty() != null ? shift.getDeliveredQty() : BigDecimal.ZERO);
                    shift.setRemainQty(remainQty);
                });
                shiftList = this.refactorShiftList(shiftList);
                rec.setShiftQtyList(shiftList);

                // 查询替代料，替代数量
                if (CollectionUtils.isNotEmpty(demandIdList)) {
                    List<WmsDistributeSubstitutionVO> substituteList = distributionDemandDetailMapper.selectSubstituteByDemandId(tenantId, demandIdList);
                    if (CollectionUtils.isNotEmpty(substituteList)) {
                        Map<String, WmsDistributionDemandQtyVO> shiftMap = shiftList.stream().filter(shift -> Objects.nonNull(shift.getCalendarShiftId())).collect(Collectors.toMap(WmsDistributionDemandQtyVO::getCalendarShiftId, shift -> shift, (key1, key2) -> key1));
                        String substituteShift = substituteList.stream().map(shift -> {
                            WmsDistributionDemandQtyVO demand = shiftMap.get(shift.getCalendarShiftId());
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(demand.getShiftDate());
                            return cal.get(Calendar.DAY_OF_MONTH) + "-" + demand.getShiftCode();
                        }).collect(Collectors.joining(","));
                        BigDecimal substituteQty = substituteList.stream().map(WmsDistributeSubstitutionVO::getSubstituteQty).reduce(BigDecimal.ZERO, BigDecimal::add);

                        // 设置返回值
                        rec.setSubstituteShift(substituteShift);
                        rec.setSubstituteQty(substituteQty);
                    }
                }
            }
        }
        return page;
    }

    @Override
    public List<WmsDistributionDemand> selectListByUniqueCondition(WmsDistributionDemand demand) {
        return distributionDemandMapper.selectListByUniqueCondition(demand);
    }

    @Override
    public List<WmsDistributionQtyVO> selectWorkcellQtyBatch(Long tenantId,
                                                             String siteId,
                                                             List<String> workcellIdList) {
        return distributionDemandMapper.selectWorkcellQtyBatch(tenantId, siteId, workcellIdList);
    }

    @Override
    public List<WmsDistributionDemandExportVO> selectExportListByDateRange(Long tenantId, LocalDate startDate, LocalDate demandDate, LocalDate endDate, WmsDistDemandQueryDTO dto) {
        return distributionDemandMapper.selectExportListByDateRange(tenantId, startDate, demandDate, endDate, dto);
    }

    @Override
    public List<WmsDistributionQtyVO> selectBatchInventoryQty(Long tenantId, String siteId) {
        return distributionDemandMapper.selectBatchInventoryQty(tenantId, siteId);
    }

    @Override
    public int updateByPrimaryKey(WmsDistributionDemand demand) {
        return distributionDemandMapper.updateByPrimaryKey(demand);
    }

    @Override
    public List<WmsDistributionDemand> batchUpdateByPrimaryKey(List<WmsDistributionDemand> list) {
        list.forEach(distributionDemandMapper::updateByPrimaryKey);
        return list;
    }

    @Override
    public int updateOptional(WmsDistributionDemand record, String... optionals) {
        if (optionals.length == 0) {
            return 0;
        }
        OptionalHelper.optional(Arrays.asList(optionals));
        return distributionDemandMapper.updateOptional(record);
    }
}
