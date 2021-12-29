package com.ruike.reports.infra.repository.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.vo.HmeEoJobTimeSnVO4;
import com.ruike.hme.domain.vo.HmeOrganizationVO;
import com.ruike.hme.infra.mapper.HmeEqManageTaskDocMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.hme.infra.util.ExcellUtils;
import com.ruike.reports.api.dto.HmeEquipmentWorkingDTO;
import com.ruike.reports.domain.repository.HmeEquipmentWorkingRepository;
import com.ruike.reports.domain.vo.*;
import com.ruike.reports.infra.mapper.HmeEquipmentWorkingMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author li.zhang 2021/01/15 10:23
 */
@Component
@Slf4j
public class HmeEquipmentWorkingReportRepositoryImpl implements HmeEquipmentWorkingRepository {

    private HmeEquipmentWorkingMapper hmeEquipmentWorkingMapper;
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    private HmeEqManageTaskDocMapper hmeEqManageTaskDocMapper;

    public HmeEquipmentWorkingReportRepositoryImpl(HmeEquipmentWorkingMapper hmeEquipmentWorkingMapper, HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper, HmeEqManageTaskDocMapper hmeEqManageTaskDocMapper) {
        this.hmeEquipmentWorkingMapper = hmeEquipmentWorkingMapper;
        this.hmeWorkOrderManagementMapper = hmeWorkOrderManagementMapper;
        this.hmeEqManageTaskDocMapper = hmeEqManageTaskDocMapper;
    }

    @Override
    public HmeEquipmentWorkingVO4 selectHmeEquipmentWorking(PageRequest pageRequest, String tenantId, HmeEquipmentWorkingDTO dto){
        // 根据设备找到 绑定的工位
        List<HmeEquipmentWorkingVO3> hmeEquipmentList = hmeEquipmentWorkingMapper.selectEquipmentList(tenantId, dto);
        List<String> workcellIdList = new ArrayList<>();
        // 取设备的第一个工位找对应的车间
        List<String> firstWorkcellList = new ArrayList<>();
        hmeEquipmentList.forEach(he -> {
            if (CollectionUtils.isNotEmpty(he.getWorkcellIdList())) {
                firstWorkcellList.add(he.getWorkcellIdList().get(0));
                workcellIdList.addAll(he.getWorkcellIdList());
            }
        });
        List<String> finalWorkcellIdList = workcellIdList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<HmeEquipmentWorkingVO3> hmeEquipmentWorkingVO3List = new ArrayList<>();
        //用户默认站点
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        // 根据工位查询车间信息
        Map<String, List<HmeOrganizationVO>> hmeOrganizationMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(firstWorkcellList)) {
            List<HmeOrganizationVO> hmeOrganizationList = hmeEqManageTaskDocMapper.queryOrganizationByWorkcellIds(Long.valueOf(tenantId), defaultSiteId, firstWorkcellList);
            hmeOrganizationMap = hmeOrganizationList.stream().collect(Collectors.groupingBy(HmeOrganizationVO::getWorkcellId));
        }
        // 再根据工位查询进展数据
        if (CollectionUtils.isNotEmpty(finalWorkcellIdList)) {
            // 如果选择了车间 根据车间找工位 限制绑定工位在车间内
            List<String> filterWorkcellIds = finalWorkcellIdList;
            if (StringUtils.isNotBlank(dto.getWorkShopId())) {
                List<String> workcellIds = hmeEqManageTaskDocMapper.queryWorkcellIdByWorkShopId(Long.valueOf(tenantId), defaultSiteId, dto.getWorkShopId());
                if (CollectionUtils.isNotEmpty(workcellIds)) {
                    filterWorkcellIds = finalWorkcellIdList.stream().filter(wkc -> workcellIds.contains(wkc)).collect(Collectors.toList());
                } else {
                    filterWorkcellIds = new ArrayList<>();
                }
            }
            if (CollectionUtils.isNotEmpty(filterWorkcellIds)) {
                dto.setWorkcellIdList(filterWorkcellIds);
                hmeEquipmentWorkingVO3List = hmeEquipmentWorkingMapper.selectList(tenantId, dto, defaultSiteId);
            }
        }

        Map<String, List<HmeEquipmentWorkingVO3>> listMap = hmeEquipmentWorkingVO3List.stream().collect(Collectors.groupingBy(HmeEquipmentWorkingVO3::getWorkcellId));
        List<HmeEquipmentWorkingVO> hmeEquipmentWorkingVOList = new ArrayList<>();
        List<HmeEquipmentWorkingVO2> allEquipmentWorkingList = new ArrayList<>();
        for (HmeEquipmentWorkingVO3 hmeEquipment : hmeEquipmentList) {
            HmeEquipmentWorkingVO hmeEquipmentWorkingVO = new HmeEquipmentWorkingVO();
            hmeEquipmentWorkingVO.setAssetEncoding(hmeEquipment.getAssetEncoding());
            hmeEquipmentWorkingVO.setAssetName(hmeEquipment.getAssetName());
            hmeEquipmentWorkingVO.setEquipmentBodyNum(hmeEquipment.getEquipmentBodyNum());
            hmeEquipmentWorkingVO.setModel(hmeEquipment.getModel());
            hmeEquipmentWorkingVO.setUser(hmeEquipment.getUser());
            hmeEquipmentWorkingVO.setDepartment(hmeEquipment.getDepartment());
            List<HmeEquipmentWorkingVO2> hmeEquipmentWorkingVO2List = new ArrayList<>();
            List<HmeEquipmentWorkingVO3> entryValue = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(hmeEquipment.getWorkcellIdList())) {
                List<HmeOrganizationVO> hmeOrganizationVOS = hmeOrganizationMap.get(hmeEquipment.getWorkcellIdList().get(0));
                if (CollectionUtils.isNotEmpty(hmeOrganizationVOS)) {
                    hmeEquipmentWorkingVO.setAreaLocation(hmeOrganizationVOS.get(0).getWorkshopName());
                }
                for (String workcellId : hmeEquipment.getWorkcellIdList()) {
                    List<HmeEquipmentWorkingVO3> hmeEquipmentWorkingVO3s = listMap.get(workcellId);
                    if (CollectionUtils.isNotEmpty(hmeEquipmentWorkingVO3s)) {
                        entryValue.addAll(hmeEquipmentWorkingVO3s);
                    }
                }
            }
            Map<String, List<HmeEquipmentWorkingVO3>> list = entryValue.stream().filter(f -> Objects.nonNull(f.getDateString())).collect(Collectors.groupingBy(HmeEquipmentWorkingVO3::getDateString));
            for (Map.Entry<String, List<HmeEquipmentWorkingVO3>> entry : list.entrySet()) {
                List<HmeEquipmentWorkingVO3> value = entry.getValue();
                List<HmeEquipmentWorkingVO3> vo3s = value.stream().sorted(Comparator.comparing(HmeEquipmentWorkingVO3::getSiteInDate)).collect(Collectors.toList());
                //时间段的开始
                Date dateStart = null;
                //时间段的结束
                Date dateEnd = null;
                Calendar calendarStart = Calendar.getInstance();
                Calendar calendarEnd = Calendar.getInstance();
                BigDecimal timeSum = new BigDecimal(0);
                // 日期为出站时间的 还要累加 跨天的 即当天23::59:59在进站时间和出站时间范围内的
                Date currentSiteOutDate = vo3s.get(0).getSiteOutDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentSiteOutDate);
                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),23,59,59);
                List<HmeEquipmentWorkingVO3> overSiteOutDateList = entryValue.stream().filter(flt -> {
                    if (calendar.getTime().after(flt.getSiteInDate()) && calendar.getTime().before(flt.getSiteOutDate())) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(overSiteOutDateList)) {
                    vo3s.addAll(overSiteOutDateList);
                }
                List<HmeEquipmentWorkingVO3> sortList = vo3s.stream().sorted(Comparator.comparing(HmeEquipmentWorkingVO3::getSiteInDate)).collect(Collectors.toList());
                //循环次数
                int i = 0;
                for (HmeEquipmentWorkingVO3 vo3 : sortList) {
                    calendarStart.setTime(vo3.getSiteInDate());
                    calendarEnd.setTime(vo3.getSiteOutDate());
                    //循环的第一次
                    if(i == 0){
                        //赋初值 如果跨天 则开始时间从零点开始 结束时间从23:59:59
                        dateStart = vo3.getSiteInDate();
                        dateEnd = vo3.getSiteOutDate();
                        if (calendarStart.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR)) {
                            calendarStart.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
                            dateStart = calendarStart.getTime();
                        }
                        if (calendarEnd.get(Calendar.DAY_OF_YEAR) > calendar.get(Calendar.DAY_OF_YEAR)) {
                            calendarEnd.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),23,59,59);
                            dateEnd = calendarEnd.getTime();
                        }
                        i++;
                        if (sortList.size() == 1) {
                            // 只有一行数据 则直接累加时间
                            timeSum = timeSum.add(dateTimeToBigDecimal(dateEnd).subtract(dateTimeToBigDecimal(dateStart)));
                        }
                        continue;
                    }
                    Date siteInDate = vo3.getSiteInDate();
                    Date siteOutDate = vo3.getSiteOutDate();
                    // 如果跨天 则开始时间从零点开始 结束时间从23:59:59
                    if (calendarStart.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR)) {
                        calendarStart.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
                        siteInDate = calendarStart.getTime();
                    }
                    if (calendarEnd.get(Calendar.DAY_OF_YEAR) > calendar.get(Calendar.DAY_OF_YEAR)) {
                        calendarEnd.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),23,59,59);
                        siteOutDate = calendarEnd.getTime();
                    }
                    if (dateEnd.before(siteInDate)) {
                        // 进站时间大于上个出站时间 则累加上个的时长
                        timeSum = timeSum.add(dateTimeToBigDecimal(dateEnd).subtract(dateTimeToBigDecimal(dateStart)));
                        dateStart = siteInDate;
                        dateEnd = siteOutDate;
                    } else if (dateEnd.before(siteOutDate)) {
                        // 不大于出站时间 则出站时间更新为最大的
                        dateEnd = siteOutDate;
                    }
                    //最后一次循环 计算结果
                    if(++i == vo3s.size()){
                        timeSum = timeSum.add(dateTimeToBigDecimal(dateEnd).subtract(dateTimeToBigDecimal(dateStart)));
                    }
                }
                BigDecimal actualDate = timeSum.divide(BigDecimal.valueOf(3600),2,BigDecimal.ROUND_HALF_UP);
                HmeEquipmentWorkingVO2 hmeEquipmentWorkingVO2 = new HmeEquipmentWorkingVO2();
                hmeEquipmentWorkingVO2.setActualDate(actualDate);
                hmeEquipmentWorkingVO2.setPlanDate(BigDecimal.valueOf(24));
                hmeEquipmentWorkingVO2.setStopDate(BigDecimal.valueOf(24).subtract(actualDate));
                hmeEquipmentWorkingVO2.setDateString(entry.getKey());
                //利用率
                NumberFormat percent = NumberFormat.getPercentInstance();
                percent.setMaximumFractionDigits(2);
                hmeEquipmentWorkingVO2.setUtilization(actualDate.divide(BigDecimal.valueOf(0.24), 2, BigDecimal.ROUND_HALF_UP));
                if (actualDate.compareTo(BigDecimal.ZERO) == 0) {
                    hmeEquipmentWorkingVO2.setBoot(BigDecimal.valueOf(0));
                } else {
                    hmeEquipmentWorkingVO2.setBoot(BigDecimal.valueOf(100));
                }
                hmeEquipmentWorkingVO2List.add(hmeEquipmentWorkingVO2);
                allEquipmentWorkingList.add(hmeEquipmentWorkingVO2);
            }
            hmeEquipmentWorkingVO.setHmeEquipmentWorkingVO2List(hmeEquipmentWorkingVO2List);
            hmeEquipmentWorkingVOList.add(hmeEquipmentWorkingVO);
        }
        // 组装数据 设备日期没数据的  给默认数据
        List<String> dateStrList = allEquipmentWorkingList.stream().sorted(Comparator.comparing(HmeEquipmentWorkingVO2::getDateString)).map(HmeEquipmentWorkingVO2::getDateString).distinct().sorted().collect(Collectors.toList());
        List<HmeEquipmentWorkingVO2> finalAllEquipmentWorkingList = new ArrayList();
        for (HmeEquipmentWorkingVO vo : hmeEquipmentWorkingVOList) {
            List<String> collect = vo.getHmeEquipmentWorkingVO2List().stream().map(HmeEquipmentWorkingVO2::getDateString).collect(Collectors.toList());
            Map<String, List<HmeEquipmentWorkingVO2>> map = vo.getHmeEquipmentWorkingVO2List().stream().collect(Collectors.groupingBy(HmeEquipmentWorkingVO2::getDateString));
            for (String dateStr : dateStrList) {
                HmeEquipmentWorkingVO2 hmeEquipmentWorkingVO2 = new HmeEquipmentWorkingVO2();
                if (collect.contains(dateStr)) {
                    BeanUtils.copyProperties(map.get(dateStr).get(0), hmeEquipmentWorkingVO2);
                } else {
                    hmeEquipmentWorkingVO2.setPlanDate(BigDecimal.valueOf(24));
                    hmeEquipmentWorkingVO2.setActualDate(BigDecimal.valueOf(0));
                    hmeEquipmentWorkingVO2.setStopDate(BigDecimal.valueOf(24));
                    hmeEquipmentWorkingVO2.setBoot(BigDecimal.valueOf(0));
                    hmeEquipmentWorkingVO2.setUtilization(BigDecimal.valueOf(0));
                    hmeEquipmentWorkingVO2.setDateString(dateStr);
                }
                finalAllEquipmentWorkingList.add(hmeEquipmentWorkingVO2);
            }
        }
        // 汇总查询的运行信息 根据日期统计数据
        List<HmeEquipmentWorkingVO5> summaryList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(finalAllEquipmentWorkingList)) {
            int size = hmeEquipmentWorkingVOList.size();
            if (size > 0) {
                Map<String, List<HmeEquipmentWorkingVO2>> allEquipmentWorkingMap = new HashMap<>();
                allEquipmentWorkingMap = finalAllEquipmentWorkingList.stream().collect(Collectors.groupingBy(HmeEquipmentWorkingVO2::getDateString));
                for (Map.Entry<String, List<HmeEquipmentWorkingVO2>> allEquipmentWorkingEntry : allEquipmentWorkingMap.entrySet()) {
                    List<HmeEquipmentWorkingVO2> value = allEquipmentWorkingEntry.getValue();
                    // 计划运行总时长
                    BigDecimal totalPlanDate = value.stream().map(HmeEquipmentWorkingVO2::getPlanDate).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                    // 实际运行总时长
                    BigDecimal totalActualDate = value.stream().map(HmeEquipmentWorkingVO2::getActualDate).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                    // 停机总时长
                    BigDecimal totalStopDate = value.stream().map(HmeEquipmentWorkingVO2::getStopDate).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                    // 平均开机率
                    BigDecimal totalBoot = value.stream().map(HmeEquipmentWorkingVO2::getBoot).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal avgBoot = totalBoot.divide(BigDecimal.valueOf(size), 2, BigDecimal.ROUND_HALF_DOWN);
                    // 平均利用率
                    BigDecimal totalUtilization = value.stream().map(HmeEquipmentWorkingVO2::getUtilization).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal avgUtilization = totalUtilization.divide(BigDecimal.valueOf(size), 2, BigDecimal.ROUND_HALF_DOWN);
                    HmeEquipmentWorkingVO5 vo5 = new HmeEquipmentWorkingVO5();
                    vo5.setDateString(allEquipmentWorkingEntry.getKey());
                    vo5.setTotalStopDate(totalStopDate);
                    vo5.setAvgUtilization(avgUtilization);
                    vo5.setAvgBoot(avgBoot);
                    vo5.setTotalActualDate(totalActualDate);
                    vo5.setTotalPlanDate(totalPlanDate);
                    summaryList.add(vo5);
                }
            }
        }
        int fromIndex = pageRequest.getPage() * pageRequest.getSize();
        int toIndex = Math.min(fromIndex + pageRequest.getSize(), hmeEquipmentWorkingVOList.size());
        HmeEquipmentWorkingVO4 resultPage = new HmeEquipmentWorkingVO4();
        resultPage.setTotalPages(hmeEquipmentWorkingVOList.size()/pageRequest.getSize()+1);
        resultPage.setTotalElements(hmeEquipmentWorkingVOList.size());
        resultPage.setNumberOfElements(toIndex-fromIndex);
        resultPage.setSize(pageRequest.getSize());
        resultPage.setNumber(pageRequest.getPage());
        resultPage.setContent(hmeEquipmentWorkingVOList);
        resultPage.setSummaryList(summaryList);
        return  resultPage;
    }

    @Override
    public void export(String tenantId, HmeEquipmentWorkingDTO dto, HttpServletResponse response) throws IOException {
        // 根据设备找到 绑定的工位
        List<HmeEquipmentWorkingVO3> hmeEquipmentList = hmeEquipmentWorkingMapper.selectEquipmentList(tenantId, dto);
        List<String> workcellIdList = new ArrayList<>();
        hmeEquipmentList.forEach(he -> {
            if (CollectionUtils.isNotEmpty(he.getWorkcellIdList())) {
                workcellIdList.addAll(he.getWorkcellIdList());
            }
        });
        List<String> finalWorkcellIdList = workcellIdList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<HmeEquipmentWorkingVO3> hmeEquipmentWorkingVO3List = new ArrayList<>();
        //用户默认站点
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        // 再根据工位查询进展数据
        if (CollectionUtils.isNotEmpty(finalWorkcellIdList)) {
            dto.setWorkcellIdList(finalWorkcellIdList);
            hmeEquipmentWorkingVO3List = hmeEquipmentWorkingMapper.selectList(tenantId, dto, defaultSiteId);
        }
        Map<String, List<HmeEquipmentWorkingVO3>> listMap = hmeEquipmentWorkingVO3List.stream().collect(Collectors.groupingBy(HmeEquipmentWorkingVO3::getWorkcellId));
        List<HmeEquipmentWorkingVO> hmeEquipmentWorkingVOList = new ArrayList<>();
        for (HmeEquipmentWorkingVO3 hmeEquipment : hmeEquipmentList) {
            HmeEquipmentWorkingVO hmeEquipmentWorkingVO = new HmeEquipmentWorkingVO();
            hmeEquipmentWorkingVO.setAssetEncoding(hmeEquipment.getAssetEncoding());
            hmeEquipmentWorkingVO.setAssetName(hmeEquipment.getAssetName());
            hmeEquipmentWorkingVO.setEquipmentBodyNum(hmeEquipment.getEquipmentBodyNum());
            hmeEquipmentWorkingVO.setModel(hmeEquipment.getModel());
            hmeEquipmentWorkingVO.setUser(hmeEquipment.getUser());
            hmeEquipmentWorkingVO.setDepartment(hmeEquipment.getDepartment());
            hmeEquipmentWorkingVO.setAreaLocation(hmeEquipment.getAreaLocation());
            List<HmeEquipmentWorkingVO2> hmeEquipmentWorkingVO2List = new ArrayList<>();
            List<HmeEquipmentWorkingVO3> entryValue = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(hmeEquipment.getWorkcellIdList())) {
                for (String workcellId : hmeEquipment.getWorkcellIdList()) {
                    List<HmeEquipmentWorkingVO3> hmeEquipmentWorkingVO3s = listMap.get(workcellId);
                    if (CollectionUtils.isNotEmpty(hmeEquipmentWorkingVO3s)) {
                        entryValue.addAll(hmeEquipmentWorkingVO3s);
                    }
                }
            }
            Map<String, List<HmeEquipmentWorkingVO3>> list = entryValue.stream().filter(f -> Objects.nonNull(f.getDateString())).collect(Collectors.groupingBy(HmeEquipmentWorkingVO3::getDateString));
            for (Map.Entry<String, List<HmeEquipmentWorkingVO3>> entry : list.entrySet()) {
                List<HmeEquipmentWorkingVO3> value = entry.getValue();
                List<HmeEquipmentWorkingVO3> vo3s = value.stream().sorted(Comparator.comparing(HmeEquipmentWorkingVO3::getSiteInDate)).collect(Collectors.toList());
                //时间段的开始
                Date dateStart = null;
                //时间段的结束
                Date dateEnd = null;
                Calendar calendarStart = Calendar.getInstance();
                Calendar calendarEnd = Calendar.getInstance();
                BigDecimal timeSum = new BigDecimal(0);
                // 日期为出站时间的 还要累加 跨天的 即当天23::59:59在进站时间和出站时间范围内的
                Date currentSiteOutDate = vo3s.get(0).getSiteOutDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentSiteOutDate);
                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),23,59,59);
                List<HmeEquipmentWorkingVO3> overSiteOutDateList = entryValue.stream().filter(flt -> {
                    if (calendar.getTime().after(flt.getSiteInDate()) && calendar.getTime().before(flt.getSiteOutDate())) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(overSiteOutDateList)) {
                    vo3s.addAll(overSiteOutDateList);
                }
                List<HmeEquipmentWorkingVO3> sortList = vo3s.stream().sorted(Comparator.comparing(HmeEquipmentWorkingVO3::getSiteInDate)).collect(Collectors.toList());
                //循环次数
                int i = 0;
                for (HmeEquipmentWorkingVO3 vo3 : sortList) {
                    calendarStart.setTime(vo3.getSiteInDate());
                    calendarEnd.setTime(vo3.getSiteOutDate());
                    //循环的第一次
                    if(i == 0){
                        //赋初值 如果跨天 则开始时间从零点开始 结束时间从23:59:59
                        dateStart = vo3.getSiteInDate();
                        dateEnd = vo3.getSiteOutDate();
                        if (calendarStart.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR)) {
                            calendarStart.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
                            dateStart = calendarStart.getTime();
                        }
                        if (calendarEnd.get(Calendar.DAY_OF_YEAR) > calendar.get(Calendar.DAY_OF_YEAR)) {
                            calendarEnd.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),23,59,59);
                            dateEnd = calendarEnd.getTime();
                        }
                        i++;
                        if (sortList.size() == 1) {
                            // 只有一行数据 则直接累加时间
                            timeSum = timeSum.add(dateTimeToBigDecimal(dateEnd).subtract(dateTimeToBigDecimal(dateStart)));
                        }
                        continue;
                    }
                    Date siteInDate = vo3.getSiteInDate();
                    Date siteOutDate = vo3.getSiteOutDate();
                    // 如果跨天 则开始时间从零点开始 结束时间从23:59:59
                    if (calendarStart.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR)) {
                        calendarStart.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
                        siteInDate = calendarStart.getTime();
                    }
                    if (calendarEnd.get(Calendar.DAY_OF_YEAR) > calendar.get(Calendar.DAY_OF_YEAR)) {
                        calendarEnd.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),23,59,59);
                        siteOutDate = calendarEnd.getTime();
                    }
                    if (dateEnd.before(siteInDate)) {
                        // 进站时间大于上个出站时间 则累加上个的时长
                        timeSum = timeSum.add(dateTimeToBigDecimal(dateEnd).subtract(dateTimeToBigDecimal(dateStart)));
                        dateStart = siteInDate;
                        dateEnd = siteOutDate;
                    } else if (dateEnd.before(siteOutDate)) {
                        // 不大于出站时间 则出站时间更新为最大的
                        dateEnd = siteOutDate;
                    }
                    //最后一次循环 计算结果
                    if(++i == vo3s.size()){
                        timeSum = timeSum.add(dateTimeToBigDecimal(dateEnd).subtract(dateTimeToBigDecimal(dateStart)));
                    }
                }
                BigDecimal actualDate = timeSum.divide(BigDecimal.valueOf(3600),2,BigDecimal.ROUND_HALF_UP);
                HmeEquipmentWorkingVO2 hmeEquipmentWorkingVO2 = new HmeEquipmentWorkingVO2();
                hmeEquipmentWorkingVO2.setActualDate(actualDate);
                hmeEquipmentWorkingVO2.setPlanDate(BigDecimal.valueOf(24));
                hmeEquipmentWorkingVO2.setStopDate(BigDecimal.valueOf(24).subtract(actualDate));
                hmeEquipmentWorkingVO2.setDateString(entry.getKey());
                //利用率
                NumberFormat percent = NumberFormat.getPercentInstance();
                percent.setMaximumFractionDigits(2);
                hmeEquipmentWorkingVO2.setUtilization(actualDate.divide(BigDecimal.valueOf(0.24), 2, BigDecimal.ROUND_HALF_UP));
                if (actualDate.compareTo(BigDecimal.ZERO) == 0) {
                    hmeEquipmentWorkingVO2.setBoot(BigDecimal.valueOf(0));
                } else {
                    hmeEquipmentWorkingVO2.setBoot(BigDecimal.valueOf(100));
                }
                hmeEquipmentWorkingVO2List.add(hmeEquipmentWorkingVO2);
            }
            hmeEquipmentWorkingVO.setHmeEquipmentWorkingVO2List(hmeEquipmentWorkingVO2List);
            hmeEquipmentWorkingVOList.add(hmeEquipmentWorkingVO);
        }

        List<HmeEquipmentWorkingVO2> hmeEquipmentWorkingVO2s = new ArrayList<>();
        for (HmeEquipmentWorkingVO vo : hmeEquipmentWorkingVOList) {
            if (CollectionUtils.isNotEmpty(vo.getHmeEquipmentWorkingVO2List())) {
                hmeEquipmentWorkingVO2s.addAll(vo.getHmeEquipmentWorkingVO2List());
            }
        }
        List<HmeEquipmentWorkingVO2> vo2List = hmeEquipmentWorkingVO2s.stream().sorted(Comparator.comparing(HmeEquipmentWorkingVO2::getDateString)).collect(Collectors.toList());
        List<String> dateStrList = vo2List.stream().map(HmeEquipmentWorkingVO2::getDateString).distinct().sorted().collect(Collectors.toList());
        List<HmeEquipmentWorkingVO> hmeEquipmentWorkingVOS = new ArrayList<>();

        List<HmeEquipmentWorkingVO2> allEquipmentWorkingList = new ArrayList();
        for (HmeEquipmentWorkingVO vo : hmeEquipmentWorkingVOList) {
            HmeEquipmentWorkingVO hmeEquipmentWorkingVO = new HmeEquipmentWorkingVO();
            List<String> collect = vo.getHmeEquipmentWorkingVO2List().stream().map(HmeEquipmentWorkingVO2::getDateString).collect(Collectors.toList());
            Map<String, List<HmeEquipmentWorkingVO2>> map = vo.getHmeEquipmentWorkingVO2List().stream().collect(Collectors.groupingBy(HmeEquipmentWorkingVO2::getDateString));
            List<HmeEquipmentWorkingVO2> vo2s = new ArrayList<>();
            for (String dateStr : dateStrList) {
                HmeEquipmentWorkingVO2 hmeEquipmentWorkingVO2 = new HmeEquipmentWorkingVO2();
                if (collect.contains(dateStr)) {
                    BeanUtils.copyProperties(map.get(dateStr).get(0), hmeEquipmentWorkingVO2);
                } else {
                    hmeEquipmentWorkingVO2.setPlanDate(BigDecimal.valueOf(24));
                    hmeEquipmentWorkingVO2.setActualDate(BigDecimal.valueOf(0));
                    hmeEquipmentWorkingVO2.setStopDate(BigDecimal.valueOf(24));
                    hmeEquipmentWorkingVO2.setBoot(BigDecimal.valueOf(0));
                    hmeEquipmentWorkingVO2.setUtilization(BigDecimal.valueOf(0));
                    hmeEquipmentWorkingVO2.setDateString(dateStr);
                }
                vo2s.add(hmeEquipmentWorkingVO2);
                allEquipmentWorkingList.add(hmeEquipmentWorkingVO2);
            }
            hmeEquipmentWorkingVO.setAssetName(vo.getAssetName());
            hmeEquipmentWorkingVO.setAssetEncoding(vo.getAssetEncoding());
            hmeEquipmentWorkingVO.setModel(vo.getModel());
            hmeEquipmentWorkingVO.setDepartment(vo.getDepartment());
            hmeEquipmentWorkingVO.setAreaLocation(vo.getAreaLocation());
            hmeEquipmentWorkingVO.setEquipmentBodyNum(vo.getEquipmentBodyNum());
            hmeEquipmentWorkingVO.setUser(vo.getUser());
            hmeEquipmentWorkingVO.setHmeEquipmentWorkingVO2List(vo2s);
            hmeEquipmentWorkingVOS.add(hmeEquipmentWorkingVO);
        }
        // 汇总查询的运行信息 根据日期统计数据
        List<HmeEquipmentWorkingVO5> summaryList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(allEquipmentWorkingList)) {
            int size = hmeEquipmentWorkingVOList.size();
            Map<String, List<HmeEquipmentWorkingVO2>> allEquipmentWorkingMap = new HashMap<>();
            allEquipmentWorkingMap = allEquipmentWorkingList.stream().collect(Collectors.groupingBy(HmeEquipmentWorkingVO2::getDateString));
            for (Map.Entry<String, List<HmeEquipmentWorkingVO2>> allEquipmentWorkingEntry : allEquipmentWorkingMap.entrySet()) {
                List<HmeEquipmentWorkingVO2> value = allEquipmentWorkingEntry.getValue();
                // 计划运行总时长
                BigDecimal totalPlanDate = value.stream().map(HmeEquipmentWorkingVO2::getPlanDate).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                // 实际运行总时长
                BigDecimal totalActualDate = value.stream().map(HmeEquipmentWorkingVO2::getActualDate).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                // 停机总时长
                BigDecimal totalStopDate = value.stream().map(HmeEquipmentWorkingVO2::getStopDate).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                // 平均开机率
                BigDecimal totalBoot = value.stream().map(HmeEquipmentWorkingVO2::getBoot).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal avgBoot = totalBoot.divide(BigDecimal.valueOf(size), 2, BigDecimal.ROUND_HALF_DOWN);
                // 平均利用率
                BigDecimal totalUtilization = value.stream().map(HmeEquipmentWorkingVO2::getUtilization).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal avgUtilization = totalUtilization.divide(BigDecimal.valueOf(size), 2, BigDecimal.ROUND_HALF_DOWN);
                HmeEquipmentWorkingVO5 vo5 = new HmeEquipmentWorkingVO5();
                vo5.setDateString(allEquipmentWorkingEntry.getKey());
                vo5.setTotalStopDate(totalStopDate);
                vo5.setAvgUtilization(avgUtilization);
                vo5.setAvgBoot(avgBoot);
                vo5.setTotalActualDate(totalActualDate);
                vo5.setTotalPlanDate(totalPlanDate);
                summaryList.add(vo5);
            }
        }
        Map<String, List<HmeEquipmentWorkingVO5>> summaryMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(summaryList)) {
            summaryMap = summaryList.stream().collect(Collectors.groupingBy(HmeEquipmentWorkingVO5::getDateString));
        }

        //导出数据
        log.info(">>>>>>>>>>>>>>>>>>>>>>开始导出设备运行利用率数据");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("设备运行利用率");
        String fileName = "设备运行利用率" + DateUtil.format(new Date(), "yyyyMMdd") + ".xls";
        OutputStream fOut = null;
        try {
            //创建excel文件对象
            fOut = response.getOutputStream();
            //新增数据行，并且设置单元格数据
            //headers表示excel表中第一行的表头
            List<String> headerList = new ArrayList<>();
            String[] headers = {"资产名称", "资产编号", "型号", "序列号", "使用部门", "车间位置", "使用人"};
            headerList.addAll(Arrays.asList(headers));
            if (CollectionUtils.isNotEmpty(dateStrList)) {
                headerList.addAll(dateStrList);
            }
            // 创建一个新的HSSFWorkbook对象
            HSSFRow row = sheet.createRow(0);
            //标题
            row.setHeightInPoints(30);
            HSSFCell headerCell1 = row.createCell(0);
            Map<String, CellStyle> styles = ExcellUtils.createStyles(workbook);
            headerCell1.setCellStyle(styles.get("title"));
            headerCell1.setCellValue("设 备 运 行 报 表");
            //存在合并 工序下有运行和库存 占两个单元格
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(),
                    row.getRowNum(), 0, dateStrList.size() * 5 + 7 - 1));
            //表头
            HSSFRow headerRow = sheet.createRow(1);
            Integer headerIndex = 0;
            for (String hd : headerList) {
                HSSFCell cell = headerRow.createCell(headerIndex);
                cell.setCellStyle(styles.get("subTitle"));
                cell.setCellValue(hd);
                if (CollectionUtils.isNotEmpty(dateStrList)) {
                    if (headerIndex.compareTo(6) > 0 && headerIndex.compareTo(6 + dateStrList.size() * 5) <= 0) {
                        sheet.addMergedRegion(new CellRangeAddress(1, 1, headerIndex, headerIndex + 4));
                        headerIndex += 5;
                    } else {
                        headerIndex++;
                    }
                } else {
                    headerIndex++;
                }
            }

            // 工序存在 需上下合并行
            if (CollectionUtils.isNotEmpty(vo2List)) {
                HSSFRow subHeaderRow = sheet.createRow(2);
                // 初始值
                Integer subIndex = 6;
                // 工序下分运行和库存
                for (String dateStr : dateStrList) {
                    HSSFCell cellList1 = subHeaderRow.createCell(subIndex + 1);
                    cellList1.setCellValue("计划运行时间（h）");
                    cellList1.setCellStyle(styles.get("subTitle"));
                    HSSFCell cellList2 = subHeaderRow.createCell(subIndex + 2);
                    cellList2.setCellValue("实际运行时间（h）");
                    cellList2.setCellStyle(styles.get("subTitle"));
                    HSSFCell cellList3 = subHeaderRow.createCell(subIndex + 3);
                    cellList3.setCellValue("停机时间（h）");
                    cellList3.setCellStyle(styles.get("subTitle"));
                    HSSFCell cellList4 = subHeaderRow.createCell(subIndex + 4);
                    cellList4.setCellValue("开机率（%）");
                    cellList4.setCellStyle(styles.get("subTitle"));
                    HSSFCell cellList5 = subHeaderRow.createCell(subIndex + 5);
                    cellList5.setCellValue("利用率（%）");
                    cellList5.setCellStyle(styles.get("subTitle"));
                    subIndex += 5;
                }
                // 合并除工序外其他标题 占两行
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 5, 5));
                sheet.addMergedRegion(new CellRangeAddress(1, 2, 6, 6));
            }
            //内容的起始行 日期不存在 起始行为2 否则为3
            Integer rowIndex = CollectionUtils.isNotEmpty(vo2List) ? 3 : 2;
            for (HmeEquipmentWorkingVO hmeEquipmentWorkingVO : hmeEquipmentWorkingVOS) {
                HSSFRow hssfRow = sheet.createRow(rowIndex++);
                HSSFCell cell0 = hssfRow.createCell(0);
                cell0.setCellStyle(styles.get("center"));
                cell0.setCellValue(hmeEquipmentWorkingVO.getAssetName());
                HSSFCell cell1 = hssfRow.createCell(1);
                cell1.setCellStyle(styles.get("center"));
                cell1.setCellValue(hmeEquipmentWorkingVO.getAssetEncoding());
                HSSFCell cell2 = hssfRow.createCell(2);
                cell2.setCellStyle(styles.get("center"));
                cell2.setCellValue(hmeEquipmentWorkingVO.getModel());
                HSSFCell cell3 = hssfRow.createCell(3);
                cell3.setCellStyle(styles.get("center"));
                cell3.setCellValue(hmeEquipmentWorkingVO.getEquipmentBodyNum());
                HSSFCell cell4 = hssfRow.createCell(4);
                cell4.setCellStyle(styles.get("center"));
                cell4.setCellValue(hmeEquipmentWorkingVO.getDepartment());
                HSSFCell cell5 = hssfRow.createCell(5);
                cell5.setCellStyle(styles.get("center"));
                cell5.setCellValue(hmeEquipmentWorkingVO.getAreaLocation());
                HSSFCell cell6 = hssfRow.createCell(6);
                cell6.setCellStyle(styles.get("center"));
                cell6.setCellValue(hmeEquipmentWorkingVO.getUser());
                Integer columnIndex = 7;
                if (CollectionUtils.isNotEmpty(hmeEquipmentWorkingVO.getHmeEquipmentWorkingVO2List())) {
                    for (HmeEquipmentWorkingVO2 hmeEquipmentWorkingVO2 : hmeEquipmentWorkingVO.getHmeEquipmentWorkingVO2List()) {
                        HSSFCell cellList1 = hssfRow.createCell(columnIndex);
                        cellList1.setCellStyle(styles.get("cellList1"));
                        cellList1.setCellValue(hmeEquipmentWorkingVO2.getPlanDate().stripTrailingZeros().toPlainString());
                        HSSFCell cellList2 = hssfRow.createCell(columnIndex + 1);
                        cellList2.setCellStyle(styles.get("cellList2"));
                        cellList2.setCellValue(hmeEquipmentWorkingVO2.getActualDate().stripTrailingZeros().toPlainString());
                        HSSFCell cellList3 = hssfRow.createCell(columnIndex + 2);
                        cellList3.setCellStyle(styles.get("cellList3"));
                        cellList3.setCellValue(hmeEquipmentWorkingVO2.getStopDate().stripTrailingZeros().toPlainString());
                        HSSFCell cellList4 = hssfRow.createCell(columnIndex +3);
                        cellList4.setCellStyle(styles.get("cellList4"));
                        cellList4.setCellValue(hmeEquipmentWorkingVO2.getBoot().stripTrailingZeros().toPlainString());
                        HSSFCell cellList5 = hssfRow.createCell(columnIndex + 4);
                        cellList5.setCellStyle(styles.get("cellList5"));
                        cellList5.setCellValue(hmeEquipmentWorkingVO2.getUtilization().stripTrailingZeros().toPlainString());
                        columnIndex += 5;
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(dateStrList)) {
                // 增加合计 从第七列开始
                HSSFRow hssfRow = sheet.createRow(rowIndex);
                Integer columnIndex = 7;
                for (String dateStr : dateStrList) {
                    List<HmeEquipmentWorkingVO5> hmeEquipmentWorkingVO5s = summaryMap.get(dateStr);
                    if (CollectionUtils.isNotEmpty(hmeEquipmentWorkingVO5s)) {
                        HSSFCell cellList1 = hssfRow.createCell(columnIndex);
                        cellList1.setCellStyle(styles.get("cellList1"));
                        cellList1.setCellValue(hmeEquipmentWorkingVO5s.get(0).getTotalPlanDate().stripTrailingZeros().toPlainString());
                        HSSFCell cellList2 = hssfRow.createCell(columnIndex + 1);
                        cellList2.setCellStyle(styles.get("cellList2"));
                        cellList2.setCellValue(hmeEquipmentWorkingVO5s.get(0).getTotalActualDate().stripTrailingZeros().toPlainString());
                        HSSFCell cellList3 = hssfRow.createCell(columnIndex + 2);
                        cellList3.setCellStyle(styles.get("cellList3"));
                        cellList3.setCellValue(hmeEquipmentWorkingVO5s.get(0).getTotalStopDate().stripTrailingZeros().toPlainString());
                        HSSFCell cellList4 = hssfRow.createCell(columnIndex +3);
                        cellList4.setCellStyle(styles.get("cellList4"));
                        cellList4.setCellValue(hmeEquipmentWorkingVO5s.get(0).getAvgBoot().stripTrailingZeros().toPlainString());
                        HSSFCell cellList5 = hssfRow.createCell(columnIndex + 4);
                        cellList5.setCellStyle(styles.get("cellList5"));
                        cellList5.setCellValue(hmeEquipmentWorkingVO5s.get(0).getAvgUtilization().stripTrailingZeros().toPlainString());
                        columnIndex += 5;
                    }
                }
            }
            ExcellUtils.setResponseHeader(response, fileName);
            workbook.write(fOut);
        } catch (IOException e) {
            throw new CommonException("在制设备运行利用率报表导出报错");
        } finally {
            //操作结束，关闭文件
            fOut.flush();
            fOut.close();
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>设备运行利用率报表导出完成");
    }

    private BigDecimal dateTimeToBigDecimal(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        BigDecimal h = BigDecimal.valueOf(calendar.get(Calendar.HOUR_OF_DAY));				//时（24小时制）
        BigDecimal m = BigDecimal.valueOf(calendar.get(Calendar.MINUTE));					//分
        BigDecimal s = BigDecimal.valueOf(calendar.get(Calendar.SECOND));
        return s.add(m.multiply(BigDecimal.valueOf(60))).add(h.multiply(BigDecimal.valueOf(3600)));
    }

}
