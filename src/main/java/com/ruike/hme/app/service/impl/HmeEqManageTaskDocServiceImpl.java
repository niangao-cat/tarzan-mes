package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeEqManageTaskDocService;
import com.ruike.hme.domain.entity.HmeEqManageTaskDoc;
import com.ruike.hme.domain.repository.HmeEqManageTaskDocLineRepository;
import com.ruike.hme.domain.repository.HmeEqManageTaskDocRepository;
import com.ruike.hme.domain.vo.HmeEqManageTaskCreateVO;
import com.ruike.hme.domain.vo.HmeEqTaskDocAndLineExportVO;
import com.ruike.hme.domain.vo.HmeEqTaskHisVO;
import com.ruike.hme.domain.vo.HmeOrganizationVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEqManageTaskDocMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.calendar.domain.repository.MtCalendarRepository;
import tarzan.calendar.domain.repository.MtCalendarShiftRepository;
import tarzan.calendar.domain.vo.MtCalendarShiftVO2;
import tarzan.calendar.domain.vo.MtCalendarVO2;
import tarzan.modeling.domain.entity.MtModOrganizationRel;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * ????????????????????????????????????????????????
 * ??????????????? 2020-08-13
 * ????????????????????????????????????????????????????????????*??? jiangling.zheng@hand-china.com 2020-08-13 14:40:33
 *
 * @author jiangling.zheng@hand-china.com 2020-06-16 16:06:11
 */
@Service
@Slf4j
public class HmeEqManageTaskDocServiceImpl implements HmeEqManageTaskDocService {

    @Autowired
    private HmeEqManageTaskDocMapper hmeEqManageTaskDocMapper;

    @Autowired
    private HmeEqManageTaskDocRepository hmeEqManageTaskDocRepository;

    @Autowired
    private MtCalendarRepository calendarRepository;

    @Autowired
    private MtCalendarShiftRepository calendarShiftRepository;

    @Autowired
    private HmeEqManageTaskDocLineRepository hmeEqManageTaskDocLineRepository;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private WmsSiteRepository wmsSiteRepository;

    @Autowired
    private MtUserClient userClient;

    @Override
    public void createEqTaskDoc(Long tenantId, String cycle) {
        // ???????????????????????????
        List<HmeEqTaskDocCreateDTO> tagGroupList = hmeEqManageTaskDocMapper.selectEqTagGroup(tenantId);
        if (CollectionUtils.isEmpty(tagGroupList)) {
            return;
        }
        // ????????????
        for (HmeEqTaskDocCreateDTO tagGroup : tagGroupList) {
            // ??????????????????????????????
            List<HmeEquipmentDTO> eqDtoList = hmeEqManageTaskDocMapper.selectEquipmentByCategory(tenantId, tagGroup.getEquipmentCategory());
            if (CollectionUtils.isEmpty(eqDtoList)) {
                continue;
            }
            // ??????????????????????????????????????????
            for (HmeEquipmentDTO eqDto : eqDtoList) {
                // ??????????????????????????????????????????
                // ???????????????????????????
                if (HmeConstants.Cycle.SHIFT.equals(cycle)) {
                    // ??????????????????
                    List<String> calendarShiftIds = getCalendarShiftId(tenantId, eqDto);
                    if (CollectionUtils.isEmpty(calendarShiftIds)) {
                        continue;
                    }
                    // ?????????????????????
                    for (String calendarShiftId : calendarShiftIds) {
                        HmeEqManageTaskCreateVO createVO = new HmeEqManageTaskCreateVO();
                        createVO.setCalendarShiftId(calendarShiftId);
                        createVO.setManageTagGroupId(tagGroup.getManageTagGroupId());
                        createVO.setCycle(cycle);
                        hmeEqManageTaskDocRepository.createTaskDoc(tenantId, createVO, eqDto);
                    }
                } else {
                    HmeEqManageTaskCreateVO createVO = new HmeEqManageTaskCreateVO();
                    createVO.setManageTagGroupId(tagGroup.getManageTagGroupId());
                    createVO.setCycle(cycle);
                    hmeEqManageTaskDocRepository.createTaskDoc(tenantId, createVO, eqDto);
                }
            }

        }
    }

    @Override
    public void createEqTaskDocPlus(Long tenantId) {
        // ????????????????????????
        List<HmeEqTaskDocEquipmentDTO> cEquDtoList = hmeEqManageTaskDocMapper.selectCheckEquipment(tenantId);
        // ????????????????????????
        List<HmeEqTaskDocEquipmentDTO> mEquDtoList = hmeEqManageTaskDocMapper.selectMaintainEquipment(tenantId);
        List<HmeEqTaskDocEquipmentDTO> finalDtoList = new ArrayList<>();
        List<HmeEqTaskDocEquipmentDTO> dtoList = new ArrayList<>();
        // ????????????+????????????????????????
        finalDtoList.addAll(cEquDtoList);
        finalDtoList.addAll(mEquDtoList);
        dtoList.addAll(finalDtoList);
        // ???????????????????????????????????????????????????(????????????????????????????????????level???????????????????????????)
        for (HmeEqTaskDocEquipmentDTO dto : finalDtoList) {
            dtoList.removeIf(item -> item.getDocType().equals(dto.getDocType()) &&
                    item.getEquipmentCategory().equals(dto.getEquipmentCategory()) &&
                    item.getEquipmentId().equals(dto.getEquipmentId()) &&
                    item.getEqLevel() > dto.getEqLevel());
        }
        for (HmeEqTaskDocEquipmentDTO dto : dtoList) {
            HmeEquipmentDTO eqDto = new HmeEquipmentDTO();
            eqDto.setEquipmentId(dto.getEquipmentId());
            eqDto.setSiteId(dto.getSiteId());
            if (HmeConstants.Cycle.SHIFT.equals(dto.getManageCycle())) {
                // ??????
                // ??????????????????
                List<String> calendarShiftIds = getCalendarShiftId(tenantId, eqDto);
                if (CollectionUtils.isEmpty(calendarShiftIds)) {
                    continue;
                }
                // ?????????????????????
                for (String calendarShiftId : calendarShiftIds) {
                    HmeEqManageTaskCreateVO createVO = new HmeEqManageTaskCreateVO();
                    createVO.setCalendarShiftId(calendarShiftId);
                    createVO.setManageTagGroupId(dto.getManageTagGroupId());
                    createVO.setCycle(dto.getManageCycle());
                    createVO.setDocType(dto.getDocType());
                    hmeEqManageTaskDocRepository.createTaskDoc(tenantId, createVO, eqDto);
                }
            } else {
                // ?????????
                HmeEqManageTaskCreateVO createVO = new HmeEqManageTaskCreateVO();
                createVO.setManageTagGroupId(dto.getManageTagGroupId());
                createVO.setCycle(dto.getManageCycle());
                createVO.setDocType(dto.getDocType());
                hmeEqManageTaskDocRepository.createTaskDoc(tenantId, createVO, eqDto);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEqTaskDocLineQueryDTO updateEqCheckDoc(Long tenantId, HmeEqTaskDocLineQueryDTO dto) {
        return hmeEqManageTaskDocLineRepository.updateEqCheckDoc(tenantId, dto);
    }

    @Override
    public Page<HmeEqTaskDocQueryDTO> queryTaskDocList(Long tenantId, HmeEqTaskDocQueryDTO dto, PageRequest pageRequest) {
        // ????????????????????????
        String defaultSiteId = wmsSiteRepository.userDefaultSite(tenantId);
        if (StringUtils.isBlank(dto.getSiteId())) {
            dto.setSiteId(defaultSiteId);
        }
        // ?????? ?????? ?????? ????????? ????????????????????? ??????????????????
        List<String> workcellIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getProcessId())) {
            // ???????????????
            List<MtModOrganizationRel> mtModOrganizationRelList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
                setTenantId(tenantId);
                setParentOrganizationType("WORKCELL");
                setParentOrganizationId(dto.getProcessId());
                setOrganizationType("WORKCELL");
            }});
            if (CollectionUtils.isNotEmpty(mtModOrganizationRelList)) {
                workcellIdList.addAll(mtModOrganizationRelList.stream().map(MtModOrganizationRel::getOrganizationId).collect(Collectors.toList()));
            }
        } else if (StringUtils.isNotBlank(dto.getProdLineId())) {
            // ???????????????
            List<String> workcellIds = hmeEqManageTaskDocMapper.queryWorkcellIdByProdLineId(tenantId, defaultSiteId, dto.getProdLineId());
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                workcellIdList.addAll(workcellIds);
            }
        } else if (StringUtils.isNotBlank(dto.getWorkshopId())) {
            // ???????????????
            List<String> workcellIds = hmeEqManageTaskDocMapper.queryWorkcellIdByWorkShopId(tenantId, defaultSiteId, dto.getWorkshopId());
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                workcellIdList.addAll(workcellIds);
            }
        } else if (StringUtils.isNotBlank(dto.getAreaId())) {
            // ??????????????????
            List<String> workcellIds = hmeEqManageTaskDocMapper.queryWorkcellIdByAreaId(tenantId, defaultSiteId, dto.getAreaId());
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                workcellIdList.addAll(workcellIds);
            }
        }
        dto.setWorkcellIdList(workcellIdList);
        Page<HmeEqTaskDocQueryDTO> pageObj = PageHelper.doPage(pageRequest, () -> hmeEqManageTaskDocRepository.queryTaskDocList(tenantId, dto));

        dto.setDocStatus("COMPLETED");
        int count = hmeEqManageTaskDocMapper.queryTaskDocListCount(tenantId, dto);
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(2);
        String completedRate = percent.format(count);
        if (BigDecimal.valueOf(pageObj.getTotalElements()).compareTo(BigDecimal.ZERO) != 0) {
            completedRate = percent.format(BigDecimal.valueOf(count).divide(BigDecimal.valueOf(pageObj.getTotalElements()), 20, BigDecimal.ROUND_HALF_UP));
        }
        String finalCompletedRate = completedRate;
        // ????????????????????????????????? ?????? ?????? ?????? ??????????????????
        List<String> workcellIds = pageObj.getContent().stream().map(HmeEqTaskDocQueryDTO::getWkcId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, List<HmeOrganizationVO>> hmeOrganizationMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(workcellIds)) {
            List<HmeOrganizationVO> hmeOrganizationList = hmeEqManageTaskDocMapper.queryOrganizationByWorkcellIds(tenantId, defaultSiteId, workcellIds);
            if (CollectionUtils.isNotEmpty(hmeOrganizationList)) {
                hmeOrganizationMap = hmeOrganizationList.stream().collect(Collectors.groupingBy(HmeOrganizationVO::getWorkcellId));
            }
        }
        Map<String, List<HmeOrganizationVO>> finalHmeOrganizationMap = hmeOrganizationMap;
        pageObj.getContent().forEach(item -> {
            if (StringUtils.isNotBlank(item.getWkcId())) {
                List<HmeOrganizationVO> hmeOrganizationVOS = finalHmeOrganizationMap.get(item.getWkcId());
                if (CollectionUtils.isNotEmpty(hmeOrganizationVOS)) {
                    // ?????????
                    item.setAreaName(hmeOrganizationVOS.get(0).getAreaName());
                    // ??????
                    item.setWorkshopName(hmeOrganizationVOS.get(0).getWorkshopName());
                    // ??????
                    item.setProdLineName(hmeOrganizationVOS.get(0).getProdLineName());
                    // ??????
                    item.setProcessName(hmeOrganizationVOS.get(0).getProcessName());
                }
            }
            item.setCheckByName(userClient.userInfoGet(tenantId, item.getCheckBy()).getRealName());
            item.setConfirmByName(userClient.userInfoGet(tenantId, item.getConfirmBy()).getRealName());
            item.setCompletedRate(finalCompletedRate);
            // ????????????
            if (HmeConstants.ConstantValue.UNDONE.equals(item.getDocStatus())
                    && !StringUtils.equals(item.getTaskCycle(), "0.5")
                    && !StringUtils.equals(item.getTaskCycle(), "1")) {
                item.setEditFlag(HmeConstants.ConstantValue.ONE);
            } else {
                item.setEditFlag(HmeConstants.ConstantValue.ZERO);
            }
        });
        return pageObj;
    }

    @Override
    public Page<HmeEqTaskDocLineQueryDTO> queryTaskDocLineList(Long tenantId, String taskDocId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeEqManageTaskDocLineRepository.queryTaskDocLineList(tenantId, taskDocId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEqTaskDocQueryDTO updateTaskDoc(Long tenantId, HmeEqTaskDocQueryDTO dto) {
        // ???????????????????????????????????????????????????
        HmeEqManageTaskDoc hmeEqManageTaskDoc = hmeEqManageTaskDocMapper.selectByPrimaryKey(dto.getTaskDocId());
        if (hmeEqManageTaskDoc == null) {
            throw new MtException("HME_EQUIPMENT_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_010", "HME"));
        }
        if (HmeConstants.ConstantValue.UNDONE.equals(hmeEqManageTaskDoc.getDocStatus())
                && !StringUtils.equals(hmeEqManageTaskDoc.getTaskCycle(), "0.5")
                && !StringUtils.equals(hmeEqManageTaskDoc.getTaskCycle(), "1")) {
            hmeEqManageTaskDoc.setRemark(dto.getRemark());
            hmeEqManageTaskDocMapper.updateByPrimaryKeySelective(hmeEqManageTaskDoc);
        } else {
            throw new MtException("HME_EQUIPMENT_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_011", "HME"));
        }
        return dto;
    }

    @Override
    public Page<HmeEqTaskHisVO> taskHistoryListQuery(Long tenantId, String taskDocLineId, PageRequest pageRequest) {
        return hmeEqManageTaskDocLineRepository.taskHistoryListQuery(tenantId, taskDocLineId, pageRequest);
    }

    @Override
    public void updateEqChangeStatus(Long tenantId) {
        List<HmeEqManageTaskDoc> allTaskDocList = new ArrayList<>();
        // ??????task_cycle = 1???????????????creat_date+24h??????????????????
        List<HmeEqManageTaskDoc> manageTaskDocOver24List = hmeEqManageTaskDocMapper.queryTaskDocOver24Hour(tenantId);
        // task_cycle ???=0.5 or 1???????????????creat_date+task_cycle??????????????????
        List<HmeEqManageTaskDoc> manageTaskDocOverTaskCycleList = hmeEqManageTaskDocMapper.queryTaskDocOverTaskCycle(tenantId);
        // ??????????????????task_cycle = 0.5 ?????????????????? ????????????????????? ?????????????????????
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        List<HmeEqManageTaskDoc> manageTaskDocNonShiftList = hmeEqManageTaskDocMapper.queryTaskDocNonShift(tenantId, sdf.format(new Date()));
        List<HmeEqManageTaskDoc> manageTaskDocShiftList = hmeEqManageTaskDocMapper.queryTaskDocShift(tenantId, sdf.format(new Date()));

        // ????????????????????? ?????????????????? ??????????????????
        if (CollectionUtils.isNotEmpty(manageTaskDocOver24List)) {
            allTaskDocList.addAll(manageTaskDocOver24List);
        }
        if (CollectionUtils.isNotEmpty(manageTaskDocOverTaskCycleList)) {
            allTaskDocList.addAll(manageTaskDocOverTaskCycleList);
        }
        List<String> taskDocIdList = allTaskDocList.stream().map(HmeEqManageTaskDoc::getTaskDocId).distinct().collect(Collectors.toList());
        List<String> undoneTaskDocIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(taskDocIdList)) {
            undoneTaskDocIdList = hmeEqManageTaskDocMapper.queryUndoneTaskDocList(tenantId, taskDocIdList);
        }
        if (CollectionUtils.isNotEmpty(allTaskDocList)) {
            for (HmeEqManageTaskDoc hmeEqManageTaskDoc : allTaskDocList) {
                if (undoneTaskDocIdList.contains(hmeEqManageTaskDoc.getTaskDocId())) {
                    hmeEqManageTaskDoc.setDocStatus(HmeConstants.ConstantValue.UNDONE);
                } else {
                    hmeEqManageTaskDoc.setDocStatus(HmeConstants.ConstantValue.OFF);
                }
            }
        }
        // ????????????
        if (CollectionUtils.isNotEmpty(manageTaskDocShiftList)) {
            for (HmeEqManageTaskDoc hmeEqManageTaskDoc : manageTaskDocShiftList) {
                hmeEqManageTaskDoc.setDocStatus(HmeConstants.ConstantValue.UNDONE);
            }
            allTaskDocList.addAll(manageTaskDocShiftList);
        }
        if (CollectionUtils.isNotEmpty(manageTaskDocNonShiftList)) {
            for (HmeEqManageTaskDoc hmeEqManageTaskDoc : manageTaskDocNonShiftList) {
                hmeEqManageTaskDoc.setDocStatus(HmeConstants.ConstantValue.OFF);
            }
            allTaskDocList.addAll(manageTaskDocNonShiftList);
        }
        if (CollectionUtils.isNotEmpty(allTaskDocList)) {
            // ????????????????????????
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            List<List<HmeEqManageTaskDoc>> splitSqlList = InterfaceUtils.splitSqlList(allTaskDocList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeEqManageTaskDoc> domains : splitSqlList) {
                hmeEqManageTaskDocMapper.batchUpdateManageTaskDoc(tenantId, userId, domains);
            }
        }
    }

    @Override
    public List<HmeEqTaskDocQueryDTO> listExport(Long tenantId, HmeEqTaskDocQueryDTO dto) {
        return hmeEqManageTaskDocRepository.queryExportTaskDocList(tenantId, dto);
    }

    @Override
    @ProcessLovValue
    public List<HmeEqTaskDocLineQueryDTO> listLineExport(Long tenantId, String taskDocId) {
        return hmeEqManageTaskDocLineRepository.queryTaskDocLineList(tenantId, taskDocId);
    }

    @Override
    public List<HmeEqTaskDocAndLineExportVO> listHeadAndLineExport(Long tenantId, HmeEqTaskDocQueryDTO dto) {
        return hmeEqManageTaskDocRepository.queryTaskDocListAndTaskDocLineList(tenantId, dto);
    }

    private List<String> getCalendarShiftId (Long tenantId, HmeEquipmentDTO eqDto) {
        List<String> stationIds = hmeEqManageTaskDocMapper.getStationId(tenantId, eqDto.getSiteId(), eqDto.getEquipmentId());
        if (CollectionUtils.isEmpty(stationIds)) {
            return null;
        }
        // ?????????????????????????????????????????????????????? ??????????????????
        String stationId = stationIds.get(0);
        // ????????????
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(eqDto.getSiteId());
        mtModOrganizationVO2.setOrganizationType("WORKCELL");
        mtModOrganizationVO2.setOrganizationId(stationId);
        mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
        mtModOrganizationVO2.setQueryType("TOP");
        List<MtModOrganizationItemVO> voList = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        String wkcId = voList.get(0).getOrganizationId();
        // ??????????????????????????????
        MtCalendarVO2 calendarVO = new MtCalendarVO2();
        calendarVO.setSiteType(HmeConstants.ApiConstantValue.MANUFACTURING);
        calendarVO.setOrganizationType(HmeConstants.ApiConstantValue.WORKCELL);
        calendarVO.setCalendarType(HmeConstants.ApiConstantValue.STANDARD);
        calendarVO.setOrganizationId(wkcId);
        String calendarId = calendarRepository.organizationLimitOnlyCalendarGet(tenantId, calendarVO);
        if (StringUtils.isEmpty(calendarId)) {
            return null;
        }
        // ????????????????????????????????????ID
        MtCalendarShiftVO2 calendarShiftVO = new MtCalendarShiftVO2();
        calendarShiftVO.setCalendarId(calendarId);
        calendarShiftVO.setShiftDate(new Date());
        return calendarShiftRepository.calendarLimitShiftQuery(tenantId, calendarShiftVO);
    }

}
