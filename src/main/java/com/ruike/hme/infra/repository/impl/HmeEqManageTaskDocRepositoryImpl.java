package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeEqTaskDocCreateDTO;
import com.ruike.hme.api.dto.HmeEqTaskDocLineQueryDTO;
import com.ruike.hme.api.dto.HmeEqTaskDocQueryDTO;
import com.ruike.hme.api.dto.HmeEquipmentDTO;
import com.ruike.hme.domain.entity.HmeEqManageTaskDocLine;
import com.ruike.hme.domain.repository.HmeEqManageTaskDocLineRepository;
import com.ruike.hme.domain.vo.HmeEqManageTaskCheckVO;
import com.ruike.hme.domain.vo.HmeEqManageTaskCreateVO;
import com.ruike.hme.domain.vo.HmeEqTaskDocAndLineExportVO;
import com.ruike.hme.domain.vo.HmeOrganizationVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEqManageTaskDocMapper;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeEqManageTaskDoc;
import com.ruike.hme.domain.repository.HmeEqManageTaskDocRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.calendar.domain.entity.MtCalendarShift;
import tarzan.calendar.domain.repository.MtCalendarShiftRepository;
import tarzan.modeling.domain.entity.MtModOrganizationRel;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备管理任务单表 资源库实现
 *
 * @author jiangling.zheng@hand-china.com 2020-06-16 16:06:11
 */
@Component
public class HmeEqManageTaskDocRepositoryImpl extends BaseRepositoryImpl<HmeEqManageTaskDoc> implements HmeEqManageTaskDocRepository {

    @Autowired
    private MtCalendarShiftRepository mtCalendarShiftRepository;

    @Autowired
    private HmeEqManageTaskDocLineRepository eqManageTaskDocLineRepository;

    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;

    @Autowired
    private HmeEqManageTaskDocMapper hmeEqManageTaskDocMapper;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createTaskDoc(Long tenantId, HmeEqManageTaskCreateVO vo, HmeEquipmentDTO eqDto) {
        // 获取按班点检数据
        List<HmeEqTaskDocCreateDTO> dtoList =
                hmeEqManageTaskDocMapper.selectEqManageTag(tenantId, vo.getCycle(), vo.getManageTagGroupId());
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        // 点检单
//        List<HmeEqTaskDocCreateDTO> checkDtoList = dtoList.stream()
//                .filter(item -> HmeConstants.ConstantValue.YES.equals(item.getCheckFlag())).collect(Collectors.toList());
//        // 保养单
//        List<HmeEqTaskDocCreateDTO> maintainDtoList = dtoList.stream()
//                .filter(item -> HmeConstants.ConstantValue.YES.equals(item.getMaintainFlag())).collect(Collectors.toList());
//        Boolean isCreateCheckDoc = false;
//        Boolean isCreateMaintainDoc = false;
        MtCalendarShift mtCalendarShift = new MtCalendarShift();
        HmeEqManageTaskCheckVO checkVO = new HmeEqManageTaskCheckVO();
        checkVO.setSiteId(eqDto.getSiteId());
        checkVO.setEquipmentId(eqDto.getEquipmentId());
        checkVO.setTaskCycle(vo.getCycle());
        checkVO.setDocType(vo.getDocType());
        // 判断周期是否为按班
        if (HmeConstants.Cycle.SHIFT.equals(vo.getCycle())) {
            // 获取当日班次
            mtCalendarShift = mtCalendarShiftRepository.calendarShiftGet(tenantId, vo.getCalendarShiftId());
            checkVO.setShiftCode(mtCalendarShift.getShiftCode());
            checkVO.setShiftDate(mtCalendarShift.getShiftDate());
            List<Integer> diffTime = hmeEqManageTaskDocMapper.getDiffTime(tenantId, checkVO);
            if (CollectionUtils.isNotEmpty(diffTime)) {
                return;
            }
//        } else if (HmeConstants.Cycle.DAY.equals(vo.getCycle())) {
//            checkVO.setCreationDate(new Date());
//        } else if (HmeConstants.Cycle.WEEK.equals(vo.getCycle())) {
//            checkVO.setCreationWeekDate(new Date());
//        } else if (HmeConstants.Cycle.MONTH.equals(vo.getCycle())) {
//            checkVO.setCreationMonthDate(new Date());
        } else {
            List<Integer> diffTime = hmeEqManageTaskDocMapper.getDiffTime(tenantId, checkVO);
            if (CollectionUtils.isNotEmpty(diffTime)) {
                // 当前时间 - 创建时间 < 周期 标识在该周期内创建过单据，则无需创建
                diffTime.removeIf(t -> t >= Integer.parseInt(vo.getCycle()));
                if (CollectionUtils.isNotEmpty(diffTime)) {
                    return;
                }
            }
        }
        /*// 校验点检单
        if (HmeConstants.ObjectTypeCode.CHECK.equals(vo.getDocType())) {
            List<Integer> diffTime = hmeEqManageTaskDocMapper.getTaskDocId(tenantId, checkVO);
            if (diffTime.size() == 0) {
                isCreateCheckDoc = true;
            }
        }
        // 校验保养单
        if (HmeConstants.ObjectTypeCode.MAINTAIN.equals(vo.getDocType())) {
            List<Integer> diffTime = hmeEqManageTaskDocMapper.getTaskDocId(tenantId, checkVO);
            if (diffTime.size() == 0) {
                isCreateMaintainDoc = true;
            }
        }
        if (!isCreateCheckDoc && !isCreateMaintainDoc) {
            return;
        }*/
        HmeEqManageTaskDoc taskDoc = new HmeEqManageTaskDoc();
        taskDoc.setTenantId(tenantId);
        taskDoc.setSiteId(eqDto.getSiteId());
        taskDoc.setDocStatus(HmeConstants.ConstantValue.WAITING);
        taskDoc.setTaskCycle(vo.getCycle());
        taskDoc.setEquipmentId(eqDto.getEquipmentId());
        taskDoc.setAttribute1(vo.getManageTagGroupId());
        if (HmeConstants.Cycle.SHIFT.equals(vo.getCycle())) {
            taskDoc.setShiftCode(mtCalendarShift.getShiftCode());
            taskDoc.setShiftDate(mtCalendarShift.getShiftDate());
        }
        // 创建单据
        // 生成单据编号
        MtNumrangeVO2 mtNumrange = new MtNumrangeVO2();
        mtNumrange.setObjectCode(HmeConstants.ObjectCode.EQUIPMENT_DOC_NUM);
        mtNumrange.setSiteId(eqDto.getSiteId());
        HmeEqManageTaskDoc checkDoc = new HmeEqManageTaskDoc();
        mtNumrange.setObjectTypeCode(vo.getDocType());
        String docNum = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrange).getNumber();
        BeanUtils.copyProperties(taskDoc, checkDoc);
        checkDoc.setDocNum(docNum);
        checkDoc.setDocType(vo.getDocType());
        self().insertSelective(checkDoc);
        // 创建单据行
        for (HmeEqTaskDocCreateDTO dto : dtoList) {
            HmeEqManageTaskDocLine taskDocLine = new HmeEqManageTaskDocLine();
            taskDocLine.setTenantId(tenantId);
            taskDocLine.setTaskDocId(checkDoc.getTaskDocId());
            taskDocLine.setManageTagId(dto.getManageTagId());
            eqManageTaskDocLineRepository.insertSelective(taskDocLine);
        }
//        // 创建保养单
//        if (isCreateMaintainDoc) {
//            HmeEqManageTaskDoc maintainDoc = new HmeEqManageTaskDoc();
//            mtNumrange.setObjectTypeCode(HmeConstants.ObjectTypeCode.MAINTAIN);
//            String docNum = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrange).getNumber();
//            BeanUtils.copyProperties(taskDoc, maintainDoc);
//            maintainDoc.setDocNum(docNum);
//            maintainDoc.setDocType(HmeConstants.ObjectTypeCode.MAINTAIN);
//            self().insertSelective(maintainDoc);
//            // 创建单据行
//            for (HmeEqTaskDocCreateDTO dto : dtoList) {
//                HmeEqManageTaskDocLine taskDocLine = new HmeEqManageTaskDocLine();
//                taskDocLine.setTenantId(tenantId);
//                taskDocLine.setTaskDocId(maintainDoc.getTaskDocId());
//                taskDocLine.setManageTagId(dto.getManageTagId());
//                eqManageTaskDocLineRepository.insertSelective(taskDocLine);
//            }
//        }
    }

    @Override
    @ProcessLovValue
    public List<HmeEqTaskDocQueryDTO> queryTaskDocList(Long tenantId, HmeEqTaskDocQueryDTO dto) {
        return hmeEqManageTaskDocMapper.queryTaskDocList(tenantId, dto);
    }

    @Override
    @ProcessLovValue
    public List<HmeEqTaskDocQueryDTO> queryExportTaskDocList(Long tenantId, HmeEqTaskDocQueryDTO dto) {
        // 获取用户默认站点
        String defaultSiteId = wmsSiteRepository.userDefaultSite(tenantId);
        if (StringUtils.isBlank(dto.getSiteId())) {
            dto.setSiteId(defaultSiteId);
        }
        // 工序 产线 车间 制造部 优先级从大到小 找对应的工位
        List<String> workcellIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getProcessId())) {
            // 工序找工位
            List<MtModOrganizationRel> mtModOrganizationRelList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
                setTenantId(tenantId);
                setParentOrganizationType("WORKCELL");
                setParentOrganizationId(dto.getProcessId());
                setParentOrganizationType("WORKCELL");
            }});
            if (CollectionUtils.isNotEmpty(mtModOrganizationRelList)) {
                workcellIdList.addAll(mtModOrganizationRelList.stream().map(MtModOrganizationRel::getOrganizationId).collect(Collectors.toList()));
            }
        } else if (StringUtils.isNotBlank(dto.getProdLineId())) {
            // 产线找工位
            List<String> workcellIds = hmeEqManageTaskDocMapper.queryWorkcellIdByProdLineId(tenantId, defaultSiteId, dto.getProdLineId());
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                workcellIdList.addAll(workcellIds);
            }
        } else if (StringUtils.isNotBlank(dto.getWorkshopId())) {
            // 车间找工位
            List<String> workcellIds = hmeEqManageTaskDocMapper.queryWorkcellIdByWorkShopId(tenantId, defaultSiteId, dto.getWorkshopId());
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                workcellIdList.addAll(workcellIds);
            }
        } else if (StringUtils.isNotBlank(dto.getAreaId())) {
            // 制造部找工位
            List<String> workcellIds = hmeEqManageTaskDocMapper.queryWorkcellIdByAreaId(tenantId, defaultSiteId, dto.getAreaId());
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                workcellIdList.addAll(workcellIds);
            }
        }
        dto.setWorkcellIdList(workcellIdList);
        List<HmeEqTaskDocQueryDTO> result = hmeEqManageTaskDocMapper.queryTaskDocList(tenantId, dto);
        // 批量查询工位对应的部门 车间 产线 工序 工位产线信息
        List<String> workcellIds = result.stream().map(HmeEqTaskDocQueryDTO::getWkcId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, List<HmeOrganizationVO>> hmeOrganizationMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(workcellIds)) {
            List<HmeOrganizationVO> hmeOrganizationList = hmeEqManageTaskDocMapper.queryOrganizationByWorkcellIds(tenantId, defaultSiteId, workcellIds);
            if (CollectionUtils.isNotEmpty(hmeOrganizationList)) {
                hmeOrganizationMap = hmeOrganizationList.stream().collect(Collectors.groupingBy(HmeOrganizationVO::getWorkcellId));
            }
        }
        Map<String, List<HmeOrganizationVO>> finalHmeOrganizationMap = hmeOrganizationMap;
        List<HmeEqTaskDocQueryDTO> collect = result.stream().filter(f -> "COMPLETED".equals(f.getDocStatus())).collect(Collectors.toList());

        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(2);
        result.forEach(item -> {
            if (StringUtils.isNotBlank(item.getWkcId())) {
                List<HmeOrganizationVO> hmeOrganizationVOS = finalHmeOrganizationMap.get(item.getWkcId());
                if (CollectionUtils.isNotEmpty(hmeOrganizationVOS)) {
                    // 制造部
                    item.setAreaName(hmeOrganizationVOS.get(0).getAreaName());
                    // 车间
                    item.setWorkshopName(hmeOrganizationVOS.get(0).getWorkshopName());
                    // 产线
                    item.setProdLineName(hmeOrganizationVOS.get(0).getProdLineName());
                    // 工序
                    item.setProcessName(hmeOrganizationVOS.get(0).getProcessName());
                }
            }
            item.setCheckByName(userClient.userInfoGet(tenantId, item.getCheckBy()).getRealName());
            item.setConfirmByName(userClient.userInfoGet(tenantId, item.getConfirmBy()).getRealName());
            item.setCompletedRate(percent.format(BigDecimal.valueOf(collect.size()).divide(BigDecimal.valueOf(result.size()), 20, BigDecimal.ROUND_HALF_UP)));
        });
        HmeEqTaskDocQueryDTO hmeEqTaskDocQueryDTO = new HmeEqTaskDocQueryDTO();
        hmeEqTaskDocQueryDTO.setRate(result.get(0).getCompletedRate());
        result.add(hmeEqTaskDocQueryDTO);
        return result;
    }

    @Override
    @ProcessLovValue
    public List<HmeEqTaskDocAndLineExportVO> queryTaskDocListAndTaskDocLineList(Long tenantId, HmeEqTaskDocQueryDTO dto) {
        // 获取用户默认站点
        String defaultSiteId = wmsSiteRepository.userDefaultSite(tenantId);
        if (StringUtils.isBlank(dto.getSiteId())) {
            dto.setSiteId(defaultSiteId);
        }
        // 工序 产线 车间 制造部 优先级从大到小 找对应的工位
        List<String> workcellIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getProcessId())) {
            // 工序找工位
            List<MtModOrganizationRel> mtModOrganizationRelList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
                setTenantId(tenantId);
                setParentOrganizationType("WORKCELL");
                setParentOrganizationId(dto.getProcessId());
                setParentOrganizationType("WORKCELL");
            }});
            if (CollectionUtils.isNotEmpty(mtModOrganizationRelList)) {
                workcellIdList.addAll(mtModOrganizationRelList.stream().map(MtModOrganizationRel::getOrganizationId).collect(Collectors.toList()));
            }
        } else if (StringUtils.isNotBlank(dto.getProdLineId())) {
            // 产线找工位
            List<String> workcellIds = hmeEqManageTaskDocMapper.queryWorkcellIdByProdLineId(tenantId, defaultSiteId, dto.getProdLineId());
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                workcellIdList.addAll(workcellIds);
            }
        } else if (StringUtils.isNotBlank(dto.getWorkshopId())) {
            // 车间找工位
            List<String> workcellIds = hmeEqManageTaskDocMapper.queryWorkcellIdByWorkShopId(tenantId, defaultSiteId, dto.getWorkshopId());
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                workcellIdList.addAll(workcellIds);
            }
        } else if (StringUtils.isNotBlank(dto.getAreaId())) {
            // 制造部找工位
            List<String> workcellIds = hmeEqManageTaskDocMapper.queryWorkcellIdByAreaId(tenantId, defaultSiteId, dto.getAreaId());
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                workcellIdList.addAll(workcellIds);
            }
        }
        dto.setWorkcellIdList(workcellIdList);

        //查询任务单和任务行
        List<HmeEqTaskDocAndLineExportVO> eqTaskDocAndLineExportVOList = hmeEqManageTaskDocMapper.queryTaskDocAndLineList(tenantId, dto);
        if (CollectionUtils.isEmpty(eqTaskDocAndLineExportVOList)) {
            return null;
        }
        //对查询到的数据根据taskDocID分组，根据serialNumber排序
        Map<String, List<HmeEqTaskDocAndLineExportVO>> eqTaskDocAndLineMap = eqTaskDocAndLineExportVOList.stream().collect(Collectors.groupingBy(HmeEqTaskDocAndLineExportVO :: getTaskDocId));
        for (String taskDocId : eqTaskDocAndLineMap.keySet()) {
            List<HmeEqTaskDocAndLineExportVO> sortedTaskDocAndLineList = eqTaskDocAndLineMap.get(taskDocId).stream().sorted(Comparator.comparing(HmeEqTaskDocAndLineExportVO :: getSerialNumber)).collect(Collectors.toList());
            eqTaskDocAndLineMap.put(taskDocId, sortedTaskDocAndLineList);
        }
        // 批量查询工位对应的部门 车间 产线 工序 工位产线信息
        List<String> workcellIds = eqTaskDocAndLineExportVOList.stream().map(HmeEqTaskDocAndLineExportVO::getWkcId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, List<HmeOrganizationVO>> hmeOrganizationMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(workcellIds)) {
            List<HmeOrganizationVO> hmeOrganizationList = hmeEqManageTaskDocMapper.queryOrganizationByWorkcellIds(tenantId, defaultSiteId, workcellIds);
            if (CollectionUtils.isNotEmpty(hmeOrganizationList)) {
                hmeOrganizationMap = hmeOrganizationList.stream().collect(Collectors.groupingBy(HmeOrganizationVO::getWorkcellId));
            }
        }
        Map<String, List<HmeOrganizationVO>> finalHmeOrganizationMap = hmeOrganizationMap;
        List<HmeEqTaskDocAndLineExportVO> collect = eqTaskDocAndLineExportVOList.stream().filter(f -> "COMPLETED".equals(f.getDocStatus())).collect(Collectors.toList());

        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(2);
        //整合返回数据
        List<HmeEqTaskDocAndLineExportVO> resultVO = new ArrayList<>();
        for (String taskDocId : eqTaskDocAndLineMap.keySet()) {
            if (CollectionUtils.isNotEmpty(eqTaskDocAndLineMap.get(taskDocId))) {
                List<HmeEqTaskDocAndLineExportVO> sortedTaskDocAndLineList = eqTaskDocAndLineMap.get(taskDocId);
                sortedTaskDocAndLineList.forEach(item ->{
                    List<HmeOrganizationVO> hmeOrganizationVOS = finalHmeOrganizationMap.get(item.getWkcId());
                    if (CollectionUtils.isNotEmpty(hmeOrganizationVOS)) {
                        // 制造部
                        item.setAreaName(hmeOrganizationVOS.get(0).getAreaName());
                        // 车间
                        item.setWorkshopName(hmeOrganizationVOS.get(0).getWorkshopName());
                        // 产线
                        item.setProdLineName(hmeOrganizationVOS.get(0).getProdLineName());
                        // 工序
                        item.setProcessName(hmeOrganizationVOS.get(0).getProcessName());
                    }
                    item.setCheckByName(userClient.userInfoGet(tenantId, item.getCheckBy()).getRealName());
                    item.setConfirmByName(userClient.userInfoGet(tenantId, item.getConfirmBy()).getRealName());
                    item.setCompletedRate(percent.format(BigDecimal.valueOf(collect.size()).divide(BigDecimal.valueOf(eqTaskDocAndLineExportVOList.size()), 20, BigDecimal.ROUND_HALF_UP)));

                    item.setResponsibleName(userClient.userInfoGet(tenantId, item.getResponsible()).getRealName());
                    item.setItemCheckByName(userClient.userInfoGet(tenantId, item.getCheckBy()).getRealName());
                    resultVO.add(item);
                });
            }
        }
        HmeEqTaskDocAndLineExportVO eqTaskDocAndLineExportVO = new HmeEqTaskDocAndLineExportVO();
        eqTaskDocAndLineExportVO.setRate(eqTaskDocAndLineExportVOList.get(0).getCompletedRate());
        resultVO.add(eqTaskDocAndLineExportVO);
        return resultVO;
    }


}
