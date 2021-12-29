package com.ruike.hme.app.service.impl;

import com.google.common.base.Joiner;
import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeWorkcellEquipmentSwitchService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.HmeWkcEquSwitchVO;
import com.ruike.hme.domain.vo.HmeWkcEquSwitchVO2;
import com.ruike.hme.domain.vo.HmeWkcEquSwitchVO3;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEqManageTaskDocMapper;
import com.ruike.hme.infra.mapper.HmeEquipmentWkcRelMapper;
import com.ruike.hme.infra.mapper.HmeWorkcellEquipmentSwitchMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.vo.MtWkcShiftVO3;
import tarzan.calendar.domain.entity.MtCalendarShift;
import tarzan.calendar.domain.repository.MtCalendarRepository;
import tarzan.calendar.domain.repository.MtCalendarShiftRepository;
import tarzan.calendar.domain.vo.MtCalendarShiftVO2;
import tarzan.calendar.domain.vo.MtCalendarVO2;
import tarzan.dispatch.domain.repository.MtOperationWkcDispatchRelRepository;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO6;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO7;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.modeling.domain.vo.MtModWorkcellVO1;
import tarzan.modeling.domain.vo.MtModWorkcellVO2;
import tarzan.modeling.infra.mapper.MtModWorkcellMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * @program: tarzan-mes->HmeWorkcellEquipmentSwitchServiceImpl
 * @description: 工位设备切换应用服务默认实现
 * @author: chaonan.hu@hand-china.com 2020-06-23 09:35:47
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class HmeWorkcellEquipmentSwitchServiceImpl implements HmeWorkcellEquipmentSwitchService {

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModWorkcellMapper mtModWorkcellMapper;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtOperationWkcDispatchRelRepository mtOperationWkcDispatchRelRepository;
    @Autowired
    private HmeOpEqRelRepository hmeOpEqRelRepository;
    @Autowired
    private HmeEquipmentWkcRelRepository hmeEquipmentWkcRelRepository;
    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeEquipmentWkcRelHisRepository hmeEquipmentWkcRelHisRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtCalendarRepository mtCalendarRepository;
    @Autowired
    private MtCalendarShiftRepository mtCalendarShiftRepository;
    @Autowired
    private HmeEqManageTaskDocRepository hmeEqManageTaskDocRepository;
    @Autowired
    private HmeEqManageTaskDocMapper hmeEqManageTaskDocMapper;
    @Autowired
    private HmeEquipmentWkcRelMapper hmeEquipmentWkcRelMapper;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private HmeWorkcellEquipmentSwitchMapper hmeWorkcellEquipmentSwitchMapper;

    @Override
    @ProcessLovValue
    public HmeWkcEquSwitchVO2 getEquCategoryAndAssetEncoding(Long tenantId, HmeWkcEquSwitchDTO dto) {
        //根据当前登陆工位找到对应工艺
        String operationId = null;
        MtModWorkcellVO1 mtModWorkcellVO1 = new MtModWorkcellVO1();
        mtModWorkcellVO1.setWorkcellCode(dto.getWorkcellCode());
        mtModWorkcellVO1.setWorkcellType("STATION");
        mtModWorkcellVO1.setEnableFlag(YES);
        List<String> workcellIds = mtModWorkcellRepository.propertyLimitWorkcellQuery(tenantId, mtModWorkcellVO1);

        if (CollectionUtils.isEmpty(workcellIds)) {
            throw new MtException("HME_EO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_001", "HME"));
        }

        //获取当前班次
        Date shiftDate = null;
        String shiftCode = null;
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, new MtModOrganizationVO2() {{
            setTopSiteId(dto.getSiteId());
            setOrganizationType("WORKCELL");
            setOrganizationId(workcellIds.get(0));
            setParentOrganizationType("WORKCELL");
            setQueryType("TOP");
        }});
        if(CollectionUtils.isNotEmpty(mtModOrganizationItemVOS)){
            MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, mtModOrganizationItemVOS.get(0).getOrganizationId());
            if(mtWkcShiftVO3 != null){
                shiftCode = mtWkcShiftVO3.getShiftCode();
                shiftDate = mtWkcShiftVO3.getShiftDate();
            }
        }

        MtModWorkcellVO2 workcellVO = mtModWorkcellMapper.selectWorkcellById(tenantId, workcellIds.get(0));
        // 获取工序
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
        mtModOrganizationVO2.setOrganizationId(workcellVO.getWorkcellId());
        mtModOrganizationVO2.setOrganizationType("WORKCELL");
        mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
        mtModOrganizationVO2.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOList =
                mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        if (CollectionUtils.isEmpty(mtModOrganizationItemVOList)) {
            // 请先维护Wkc工序工位关系
            throw new MtException("HME_EO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_002", "HME"));
        } else {
            // 获取工艺路线
            MtOpWkcDispatchRelVO7 mtOpWkcDispatchRelVO7 = new MtOpWkcDispatchRelVO7();
            mtOpWkcDispatchRelVO7.setWorkcellId(mtModOrganizationItemVOList.get(0).getOrganizationId());
            List<MtOpWkcDispatchRelVO6> opWkcDispatchRelVO6List =
                    mtOperationWkcDispatchRelRepository.wkcLimitAvailableOperationQuery(tenantId, mtOpWkcDispatchRelVO7);
            if (CollectionUtils.isNotEmpty(opWkcDispatchRelVO6List)) {
                // 获取当前工位对应的工艺信息
                operationId = opWkcDispatchRelVO6List.get(0).getOperationId();
            } else {
                // 请先维护Wkc工序工艺关系
                throw new MtException("HME_EO_JOB_SN_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_021", "HME"));
            }
        }
        if (StringUtils.isBlank(operationId)) {
            throw new MtException("HME_WKC_EQUIPMENT_SWITCH_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WKC_EQUIPMENT_SWITCH_001", "HME"));
        }
        //根据工艺Id查询设备类
        List<HmeOpEqRel> hmeOpEqRels = hmeWorkcellEquipmentSwitchMapper.equipmentCategoryQuery(tenantId, operationId);
//        if(CollectionUtils.isEmpty(hmeOpEqRels)){
//            throw new MtException("HME_WKC_EQUIPMENT_SWITCH_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_WKC_EQUIPMENT_SWITCH_002", "HME"));
//        }
        List<HmeWkcEquSwitchVO> hmeWkcEquSwitchVOS = new ArrayList<>();
        //已点检完成的设备类数量
        Long completeQty = 0L;
        for (HmeOpEqRel hmeOpEqRelDb : hmeOpEqRels) {
            HmeWkcEquSwitchVO hmeWkcEquSwitchVO = new HmeWkcEquSwitchVO();
            //2020-09-01 add by chaonan.hu for tianyang.xie 增加attribute1返回前台并排序、给颜色
            hmeWkcEquSwitchVO.setAttribute1(hmeOpEqRelDb.getAttribute1());
            hmeWkcEquSwitchVO.setEquipmentCategory(hmeOpEqRelDb.getEquipmentCategory());
            hmeWkcEquSwitchVO.setEquipmentCategoryDesc(lovAdapter.queryLovMeaning("HME.EQUIPMENT_CATEGORY", tenantId, hmeOpEqRelDb.getEquipmentCategory()));
            String equipmentCategory = hmeWkcEquSwitchVO.getEquipmentCategory();
            String equipmentCategoryDesc = hmeWkcEquSwitchVO.getEquipmentCategoryDesc();
            //根据设备类查询设备
            HmeEquipmentWkcRel hmeEquipmentWkcRel = new HmeEquipmentWkcRel();
            hmeEquipmentWkcRel.setStationId(workcellVO.getWorkcellId());
            hmeEquipmentWkcRel.setEnableFlag(HmeConstants.ConstantValue.YES);
            List<HmeEquipmentWkcRel> hmeEquipmentWkcRels = hmeEquipmentWkcRelRepository.select(hmeEquipmentWkcRel);
            for (HmeEquipmentWkcRel HmeEquipmentWkcRel : hmeEquipmentWkcRels) {
                HmeEquipment hmeEquipment = hmeEquipmentRepository.selectByPrimaryKey(HmeEquipmentWkcRel.getEquipmentId());
                if (hmeOpEqRelDb.getEquipmentCategory().equals(hmeEquipment.getEquipmentCategory())) {
                    hmeWkcEquSwitchVO = new HmeWkcEquSwitchVO();
                    hmeWkcEquSwitchVO.setAttribute1(hmeOpEqRelDb.getAttribute1());
                    hmeWkcEquSwitchVO.setEquipmentCategory(equipmentCategory);
                    hmeWkcEquSwitchVO.setEquipmentCategoryDesc(equipmentCategoryDesc);
                    hmeWkcEquSwitchVO.setEquipmentId(hmeEquipment.getEquipmentId());
                    hmeWkcEquSwitchVO.setAssetEncoding(hmeEquipment.getAssetEncoding());
                    hmeWkcEquSwitchVO.setDescriptions(hmeEquipment.getAssetName());
                    hmeWkcEquSwitchVO.setColor("BLUE");
                    //判断设备类下的已绑定设备是否已设备点检完成，如果已完成，则更新颜色为绿色
                    //设备当天所有点检任务是否完成标识
                    Boolean completeFlag = false;
                    //设备当天所有点检任务是否合格标识
                    Boolean qualifiedFlag = true;
                    //获取设备当天所有点检任务
                    List<HmeEqManageTaskDoc> hmeEqManageTaskDocs = new ArrayList<>();
                    if (shiftDate != null && StringUtils.isNotBlank(shiftCode)) {
                        //第一种情况 按班
                        HmeEqManageTaskDoc hmeEqManageTaskDoc = new HmeEqManageTaskDoc();
                        hmeEqManageTaskDoc.setTenantId(tenantId);
                        hmeEqManageTaskDoc.setEquipmentId(hmeWkcEquSwitchVO.getEquipmentId());
                        hmeEqManageTaskDoc.setDocType("C");
                        hmeEqManageTaskDoc.setTaskCycle("0.5");
                        hmeEqManageTaskDoc.setShiftDate(shiftDate);
                        hmeEqManageTaskDoc.setShiftCode(shiftCode);
                        hmeEqManageTaskDocs.addAll(hmeEqManageTaskDocRepository.select(hmeEqManageTaskDoc));
                        //第二种情况 非按班
                        hmeEqManageTaskDocs.addAll(hmeEqManageTaskDocMapper.queryTaskDocList2(tenantId, hmeWkcEquSwitchVO.getEquipmentId()));
                        if (CollectionUtils.isNotEmpty(hmeEqManageTaskDocs)) {
                            completeFlag = true;
                            for (HmeEqManageTaskDoc hmeEqManageTaskDocDb : hmeEqManageTaskDocs) {
                                if (!HmeConstants.ConstantValue.COMPLETED.equals(hmeEqManageTaskDocDb.getDocStatus())) {
                                    completeFlag = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (completeFlag) {
                        completeQty++;
                        //2020-09-21 add by chaonan.hu for tianyang.xie
                        // 已点检完的，判断是否全部任务合格，如果有一个或多个任务不合格则返回红色，否则绿色
                        long count = hmeEqManageTaskDocs.stream().filter(c -> "NG".equals(c.getCheckResult())).count();
                        if(count == 0){
                            hmeWkcEquSwitchVO.setColor("GREEN");
                        }else{
                            hmeWkcEquSwitchVO.setColor("RED");
                        }
                    }
                    hmeWkcEquSwitchVOS.add(hmeWkcEquSwitchVO);
                }
            }
            if (StringUtils.isBlank(hmeWkcEquSwitchVO.getAssetEncoding())) {
                if("Y".equals(hmeWkcEquSwitchVO.getAttribute1())){
                    hmeWkcEquSwitchVO.setColor("GREY");
                    hmeWkcEquSwitchVOS.add(hmeWkcEquSwitchVO);
                }else{
                    hmeWkcEquSwitchVO.setColor("LIGHTGREEN");
                    hmeWkcEquSwitchVOS.add(hmeWkcEquSwitchVO);
                }
            }
        }

        //封装返回数据对象
        HmeWkcEquSwitchVO2 hmeWkcEquSwitchVO2 = new HmeWkcEquSwitchVO2();

        //V20210114 modify by penglin.sui for jian.zhang 选择工位下的设备生成了点检/保养的任务，需要前台提示
        if(CollectionUtils.isNotEmpty(hmeWkcEquSwitchVOS)){
            List<String> equipmentIdList = hmeWkcEquSwitchVOS.stream().map(HmeWkcEquSwitchVO::getEquipmentId).distinct()
                    .collect(Collectors.toList());
            List<HmeEqManageTaskDoc> hmeEqManageTaskDocList = hmeEqManageTaskDocMapper.queryTaskDocList3(tenantId,equipmentIdList,shiftCode,shiftDate);
            if(CollectionUtils.isNotEmpty(hmeEqManageTaskDocList)) {
                equipmentIdList.clear();
                equipmentIdList = hmeEqManageTaskDocList.stream().map(HmeEqManageTaskDoc::getEquipmentId).distinct().collect(Collectors.toList());
                StringBuilder errorEquipmentCodes = new StringBuilder();
                StringBuilder exceptioinEquipmentCodes = new StringBuilder();
                Date nowDate = CommonUtils.currentTimeGet();
                List<LovValueDTO> taskDocTypes = lovAdapter.queryLovValue("HME.TASK_DOC_TYPE", tenantId);
                for (HmeWkcEquSwitchVO hmeWkcEquSwitchVO: hmeWkcEquSwitchVOS
                     ) {
                    if (!equipmentIdList.contains(hmeWkcEquSwitchVO.getEquipmentId())) {
                        continue;
                    }
                    // V20210705 modify by sanfeng.zhang for peng.zhao 提示分任务点检和保养
                    //V20210309 modify by sanfeng.zhang for peng.zhao 查询设备当天的任务 强校验时 周期为0.5和1 和超过长周期最后一天才提示
                    List<HmeEqManageTaskDoc> taskDocList = hmeEqManageTaskDocList.stream().filter(doc -> StringUtils.equals(doc.getEquipmentId(), hmeWkcEquSwitchVO.getEquipmentId())).collect(Collectors.toList());
                    Set<String> taskDocTypeList = new HashSet<>();
                    for (HmeEqManageTaskDoc hmeEqManageTaskDoc : taskDocList) {
                        if (StringUtils.equals(hmeEqManageTaskDoc.getTaskCycle(), "0.5") || StringUtils.equals(hmeEqManageTaskDoc.getTaskCycle(), "1") || StringUtils.isBlank(hmeEqManageTaskDoc.getTaskCycle())) {
                            Optional<LovValueDTO> firstOpt = taskDocTypes.stream().filter(lov -> StringUtils.equals(lov.getValue(), hmeEqManageTaskDoc.getDocType())).findFirst();
                            if (firstOpt.isPresent()) {
                                taskDocTypeList.add(firstOpt.get().getMeaning());
                            }
                        } else {
                            // 长周期 在create_date + task_cycle - 1 的日期内 通过 否则需前台提示
                            Long days = CommonUtils.betweenDays(hmeEqManageTaskDoc.getCreationDate(), nowDate);
                            Long subDays = Long.valueOf(hmeEqManageTaskDoc.getTaskCycle()) - 1;
                            if (days.compareTo(subDays) >= 0) {
                                Optional<LovValueDTO> firstOpt = taskDocTypes.stream().filter(lov -> StringUtils.equals(lov.getValue(), hmeEqManageTaskDoc.getDocType())).findFirst();
                                if (firstOpt.isPresent()) {
                                    taskDocTypeList.add(firstOpt.get().getMeaning());
                                }
                            }
                        }
                    }
                    if (HmeConstants.ConstantValue.YES.equals(hmeWkcEquSwitchVO.getAttribute1())) {
                        if (CollectionUtils.isNotEmpty(taskDocTypeList)) {
                            if (errorEquipmentCodes.length() == 0) {
                                errorEquipmentCodes.append(hmeWkcEquSwitchVO.getAssetEncoding());
                            } else {
                                errorEquipmentCodes.append("," + hmeWkcEquSwitchVO.getAssetEncoding());
                            }
                            errorEquipmentCodes.append(taskDocTypeList.toString());
                        }
                    } else {
                        // 长周期 在周期内的 不提醒
                        if (CollectionUtils.isNotEmpty(taskDocTypeList)) {
                            if(exceptioinEquipmentCodes.length() == 0){
                                exceptioinEquipmentCodes.append(hmeWkcEquSwitchVO.getAssetEncoding());
                            }else{
                                exceptioinEquipmentCodes.append("," + hmeWkcEquSwitchVO.getAssetEncoding());
                            }
                            exceptioinEquipmentCodes.append(taskDocTypeList.toString());
                        }
                    }
                }
                if(errorEquipmentCodes.length() > 0){
                    hmeWkcEquSwitchVO2.setErrorEquipmentCodes(errorEquipmentCodes.toString());
                }else {
                    if (exceptioinEquipmentCodes.length() > 0) {
                        hmeWkcEquSwitchVO2.setExceptionEquipmentCodes(exceptioinEquipmentCodes.toString());
                    }
                }
            }
        }

        HmeWkcEquSwitchVO3 hmeWkcEquSwitchVO3 = new HmeWkcEquSwitchVO3();
        hmeWkcEquSwitchVO3.setWorkCellId(workcellIds.get(0));
        hmeWkcEquSwitchVO3.setTotalQty(Long.parseLong(hmeWkcEquSwitchVOS.size() + ""));
        hmeWkcEquSwitchVO3.setCompleteQty(completeQty);
        hmeWkcEquSwitchVO2.setHmeWkcEquSwitchVO3(hmeWkcEquSwitchVO3);
        hmeWkcEquSwitchVO2.setHmeWkcEquSwitchVOS(hmeWkcEquSwitchVOS);
        return hmeWkcEquSwitchVO2;
    }

    @Override
    public HmeWkcEquSwitchDTO7 bandingStationAndEquipment(Long tenantId, HmeWkcEquSwitchDTO2 dto) {
        HmeWkcEquSwitchDTO7 result = new HmeWkcEquSwitchDTO7();
        //根据扫描的设备编码查询设备类
        HmeEquipment hmeEquipment = new HmeEquipment();
        hmeEquipment.setTenantId(tenantId);
        hmeEquipment.setAssetEncoding(dto.getScanAssetEncoding());
        hmeEquipment = hmeEquipmentRepository.selectOne(hmeEquipment);
        if (hmeEquipment == null) {
            throw new MtException("HME_WKC_EQUIPMENT_SWITCH_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WKC_EQUIPMENT_SWITCH_004", "HME"));
        }
        //校验设备类
        if (!hmeEquipment.getEquipmentCategory().equals(dto.getEquipmentCategory())) {
            String scanEquCateDesc = lovAdapter.queryLovMeaning("HME.EQUIPMENT_CATEGORY", tenantId, hmeEquipment.getEquipmentCategory());
            throw new MtException("HME_WKC_EQUIPMENT_SWITCH_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WKC_EQUIPMENT_SWITCH_003", "HME", dto.getScanAssetEncoding(), scanEquCateDesc, dto.getEquipmentCategoryDesc()));
        }
        //2020-06-29 add by chaonan.hu
        //在记录工位设备关系前，先判断要记录的设备在关系表中是否存在数据，如果存在，则前台提示是否删除存在数据
        HmeEquipmentWkcRel hmeEquipmentWkcRel1 = new HmeEquipmentWkcRel();
        hmeEquipmentWkcRel1.setTenantId(tenantId);
        hmeEquipmentWkcRel1.setEquipmentId(hmeEquipment.getEquipmentId());
        List<HmeEquipmentWkcRel> hmeEquipmentWkcRels = hmeEquipmentWkcRelRepository.select(hmeEquipmentWkcRel1);
        if (CollectionUtils.isNotEmpty(hmeEquipmentWkcRels) && ("2".equals(hmeEquipment.getApplyType()) || StringUtils.isEmpty(hmeEquipment.getApplyType()))) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeEquipmentWkcRels.get(0).getStationId());
            //如果是绑定在当前工位，则提示报错
            if(mtModWorkcell.getWorkcellId().equals(dto.getWorkcellId())){
                throw new MtException("HME_WKC_EQUIPMENT_SWITCH_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WKC_EQUIPMENT_SWITCH_005", "HME", dto.getScanAssetEncoding()));
            }
            //封装返回结果
            result.setSuccess(false);
            BeanUtils.copyProperties(dto, result);
            result.setOldWorkcellName(mtModWorkcell.getWorkcellName());
            return result;
        }

        //记录工位设备关系
        HmeEquipmentWkcRel hmeEquipmentWkcRel = new HmeEquipmentWkcRel();
        hmeEquipmentWkcRel.setTenantId(tenantId);
        hmeEquipmentWkcRel.setSiteId(dto.getSiteId());
        hmeEquipmentWkcRel.setEquipmentId(hmeEquipment.getEquipmentId());
        hmeEquipmentWkcRel.setStationId(dto.getWorkcellId());
        hmeEquipmentWkcRel.setEnableFlag(HmeConstants.ConstantValue.YES);
        //校验当前工位是否已绑定过这个设备
        List<HmeEquipmentWkcRel> hmeEquipmentWkcRelList = hmeEquipmentWkcRelRepository.select(hmeEquipmentWkcRel);
        if (CollectionUtils.isNotEmpty(hmeEquipmentWkcRelList)) {
            throw new MtException("HME_WKC_EQUIPMENT_SWITCH_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WKC_EQUIPMENT_SWITCH_005", "HME", dto.getScanAssetEncoding()));
        }
        hmeEquipmentWkcRelRepository.insertSelective(hmeEquipmentWkcRel);
        //记录历史
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("STATION_EQUIPMENT_LOAD");
        }});
        HmeEquipmentWkcRelHis hmeEquipmentWkcRelHis = new HmeEquipmentWkcRelHis();
        hmeEquipmentWkcRelHis.setEventId(eventId);
        BeanUtils.copyProperties(hmeEquipmentWkcRel, hmeEquipmentWkcRelHis);
        hmeEquipmentWkcRelHisRepository.insertSelective(hmeEquipmentWkcRelHis);
        result.setSuccess(true);
        return result;
    }

    @Override
    public void confirmBandingRel(Long tenantId, HmeWkcEquSwitchDTO2 dto) {
        //根据扫描的设备编码查询设备类
        HmeEquipment hmeEquipment = new HmeEquipment();
        hmeEquipment.setTenantId(tenantId);
        hmeEquipment.setAssetEncoding(dto.getScanAssetEncoding());
        hmeEquipment = hmeEquipmentRepository.selectOne(hmeEquipment);
        //在记录工位设备关系前，先删除要记录的设备在关系表中的已存在数据，并记录历史
        HmeEquipmentWkcRel hmeEquipmentWkcRel1 = new HmeEquipmentWkcRel();
        hmeEquipmentWkcRel1.setTenantId(tenantId);
        hmeEquipmentWkcRel1.setEquipmentId(hmeEquipment.getEquipmentId());
        List<HmeEquipmentWkcRel> hmeEquipmentWkcRels = hmeEquipmentWkcRelRepository.select(hmeEquipmentWkcRel1);
        if (CollectionUtils.isNotEmpty(hmeEquipmentWkcRels)) {
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "STATION_EQUIPMENT_UPDATE");
            //删除数据
            hmeEquipmentWkcRelRepository.batchDeleteByPrimaryKey(hmeEquipmentWkcRels);
            //记录历史
            String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("STATION_EQUIPMENT_UNLOAD");
                setEventRequestId(eventRequestId);
            }});
            for (HmeEquipmentWkcRel hmeEquipmentWkcRelDb : hmeEquipmentWkcRels) {
                HmeEquipmentWkcRelHis hmeEquipmentWkcRelHis = new HmeEquipmentWkcRelHis();
                hmeEquipmentWkcRelHis.setEventId(eventId);
                BeanUtils.copyProperties(hmeEquipmentWkcRelDb, hmeEquipmentWkcRelHis);
                hmeEquipmentWkcRelHisRepository.insertSelective(hmeEquipmentWkcRelHis);
            }
        }
        //记录工位设备关系
        HmeEquipmentWkcRel hmeEquipmentWkcRel = new HmeEquipmentWkcRel();
        hmeEquipmentWkcRel.setTenantId(tenantId);
        hmeEquipmentWkcRel.setSiteId(dto.getSiteId());
        hmeEquipmentWkcRel.setEquipmentId(hmeEquipment.getEquipmentId());
        hmeEquipmentWkcRel.setStationId(dto.getWorkcellId());
        hmeEquipmentWkcRel.setEnableFlag(HmeConstants.ConstantValue.YES);
        //校验当前工位是否已绑定过这个设备
        List<HmeEquipmentWkcRel> hmeEquipmentWkcRelList = hmeEquipmentWkcRelRepository.select(hmeEquipmentWkcRel);
        if (CollectionUtils.isNotEmpty(hmeEquipmentWkcRelList)) {
            throw new MtException("HME_WKC_EQUIPMENT_SWITCH_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WKC_EQUIPMENT_SWITCH_005", "HME", dto.getScanAssetEncoding()));
        }
        hmeEquipmentWkcRelRepository.insertSelective(hmeEquipmentWkcRel);
        //记录历史
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("STATION_EQUIPMENT_LOAD");
        }});
        HmeEquipmentWkcRelHis hmeEquipmentWkcRelHis = new HmeEquipmentWkcRelHis();
        hmeEquipmentWkcRelHis.setEventId(eventId);
        BeanUtils.copyProperties(hmeEquipmentWkcRel, hmeEquipmentWkcRelHis);
        hmeEquipmentWkcRelHisRepository.insertSelective(hmeEquipmentWkcRelHis);
    }

    @Override
    public void deleteStationAndEquipment(Long tenantId, HmeWkcEquSwitchDTO3 dto) {
        //根据设备编码查询设备ID
        HmeEquipment hmeEquipment = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
            setTenantId(tenantId);
            setAssetEncoding(dto.getAssetEncoding());
        }});
        //删除工位设备关系表数据
        HmeEquipmentWkcRel hmeEquipmentWkcRel = new HmeEquipmentWkcRel();
        hmeEquipmentWkcRel.setEquipmentId(hmeEquipment.getEquipmentId());
        hmeEquipmentWkcRel.setStationId(dto.getWorkcellId());
        hmeEquipmentWkcRel.setEnableFlag(HmeConstants.ConstantValue.YES);
        List<HmeEquipmentWkcRel> hmeEquipmentWkcRels = hmeEquipmentWkcRelRepository.select(hmeEquipmentWkcRel);
        if (CollectionUtils.isNotEmpty(hmeEquipmentWkcRels) && hmeEquipmentWkcRels.size() == 1) {
            hmeEquipmentWkcRelRepository.deleteByPrimaryKey(hmeEquipmentWkcRels.get(0));
        }
        //记录历史
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("STATION_EQUIPMENT_UNLOAD");
        }});
        HmeEquipmentWkcRelHis hmeEquipmentWkcRelHis = new HmeEquipmentWkcRelHis();
        hmeEquipmentWkcRelHis.setEventId(eventId);
        BeanUtils.copyProperties(hmeEquipmentWkcRels.get(0), hmeEquipmentWkcRelHis);
        hmeEquipmentWkcRelHisRepository.insertSelective(hmeEquipmentWkcRelHis);
    }

    @Override
    public String getEquipmentDesc(Long tenantId, String assetEncoding) {
        HmeEquipment hmeEquipment = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
            setTenantId(tenantId);
            setAssetEncoding(assetEncoding);
        }});
        return hmeEquipment.getAssetName();
    }

    @Override
    public HmeWkcEquSwitchDTO8 replaceStationAndEquipment(Long tenantId, HmeWkcEquSwitchDTO4 dto) {
        HmeWkcEquSwitchDTO8 result = new HmeWkcEquSwitchDTO8();
        //根据新的设备编码查询设备信息
        HmeEquipment hmeEquipment = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
            setTenantId(tenantId);
            setAssetEncoding(dto.getAssetEncodingLast());
        }});
        if (hmeEquipment == null) {
            throw new MtException("HME_WKC_EQUIPMENT_SWITCH_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WKC_EQUIPMENT_SWITCH_004", "HME"));
        }
        //校验设备类
        if (!hmeEquipment.getEquipmentCategory().equals(dto.getEquipmentCategory())) {
            String scanEquCateDesc = lovAdapter.queryLovMeaning("HME.EQUIPMENT_CATEGORY", tenantId, hmeEquipment.getEquipmentCategory());
            throw new MtException("HME_WKC_EQUIPMENT_SWITCH_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WKC_EQUIPMENT_SWITCH_003", "HME", dto.getAssetEncodingLast(), scanEquCateDesc, dto.getEquipmentCategoryDesc()));
        }
        //2020-06-29 add by chaonan.hu
        // 在更新设备工位关系前，判断要更新的设备在关系表中是否存在数据，如果存在，则前台提示
        HmeEquipmentWkcRel hmeEquipmentWkcRel1 = new HmeEquipmentWkcRel();
        hmeEquipmentWkcRel1.setTenantId(tenantId);
        hmeEquipmentWkcRel1.setEquipmentId(hmeEquipment.getEquipmentId());
        List<HmeEquipmentWkcRel> hmeEquipmentWkcRels = hmeEquipmentWkcRelRepository.select(hmeEquipmentWkcRel1);
        if (CollectionUtils.isNotEmpty(hmeEquipmentWkcRels) && ("2".equals(hmeEquipment.getApplyType()) || StringUtils.isEmpty(hmeEquipment.getApplyType()))) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeEquipmentWkcRels.get(0).getStationId());
            //如果是绑定在当前工位，则提示报错
            if(mtModWorkcell.getWorkcellId().equals(dto.getWorkcellId())){
                throw new MtException("HME_WKC_EQUIPMENT_SWITCH_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WKC_EQUIPMENT_SWITCH_005", "HME", dto.getAssetEncodingLast()));
            }
            //封装返回结果
            result.setSuccess(false);
            BeanUtils.copyProperties(dto, result);
            result.setOldWorkcellName(mtModWorkcell.getWorkcellName());
            return result;
//            //删除数据
//            hmeEquipmentWkcRelRepository.batchDeleteByPrimaryKey(hmeEquipmentWkcRels);
//            //记录历史
//            String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
//                setEventTypeCode("STATION_EQUIPMENT_UNLOAD");
//                setEventRequestId(eventRequestId);
//            }});
//            for (HmeEquipmentWkcRel hmeEquipmentWkcRelDb:hmeEquipmentWkcRels) {
//                HmeEquipmentWkcRelHis hmeEquipmentWkcRelHis = new HmeEquipmentWkcRelHis();
//                hmeEquipmentWkcRelHis.setEventId(eventId);
//                BeanUtils.copyProperties(hmeEquipmentWkcRelDb, hmeEquipmentWkcRelHis);
//                hmeEquipmentWkcRelHisRepository.insertSelective(hmeEquipmentWkcRelHis);
//            }
        }
        //校验当前工位是否已绑定过这个设备
        HmeEquipmentWkcRel hmeEquipmentWkcRel2 = new HmeEquipmentWkcRel();
        hmeEquipmentWkcRel2.setTenantId(tenantId);
        hmeEquipmentWkcRel2.setEquipmentId(hmeEquipment.getEquipmentId());
        hmeEquipmentWkcRel2.setStationId(dto.getWorkcellId());
        hmeEquipmentWkcRel2.setEnableFlag(HmeConstants.ConstantValue.YES);
        List<HmeEquipmentWkcRel> hmeEquipmentWkcRelList = hmeEquipmentWkcRelRepository.select(hmeEquipmentWkcRel2);
        if (CollectionUtils.isNotEmpty(hmeEquipmentWkcRelList)) {
            throw new MtException("HME_WKC_EQUIPMENT_SWITCH_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WKC_EQUIPMENT_SWITCH_005", "HME", dto.getAssetEncodingLast()));
        }
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "STATION_EQUIPMENT_UPDATE");
        //更新设备工位关系
        HmeEquipment hmeEquipmentFirst = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
            setTenantId(tenantId);
            setAssetEncoding(dto.getAssetEncodingFirst());
        }});
        HmeEquipmentWkcRel hmeEquipmentWkcRel = new HmeEquipmentWkcRel();
        hmeEquipmentWkcRel.setTenantId(tenantId);
        hmeEquipmentWkcRel.setEquipmentId(hmeEquipmentFirst.getEquipmentId());
        hmeEquipmentWkcRel.setStationId(dto.getWorkcellId());
        HmeEquipmentWkcRel hmeEquipmentWkcRelDb = hmeEquipmentWkcRelRepository.selectOne(hmeEquipmentWkcRel);
        //记录历史 更新前历史
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("STATION_EQUIPMENT_UNLOAD");
            setEventRequestId(eventRequestId);
        }});
        HmeEquipmentWkcRelHis hmeEquipmentWkcRelHis = new HmeEquipmentWkcRelHis();
        hmeEquipmentWkcRelHis.setEventId(eventId);
        BeanUtils.copyProperties(hmeEquipmentWkcRelDb, hmeEquipmentWkcRelHis);
        hmeEquipmentWkcRelHisRepository.insertSelective(hmeEquipmentWkcRelHis);
        //更新数据
        hmeEquipmentWkcRelDb.setEquipmentId(hmeEquipment.getEquipmentId());
        hmeEquipmentWkcRelDb.setObjectVersionNumber(hmeEquipmentWkcRelDb.getObjectVersionNumber());
        hmeEquipmentWkcRelMapper.updateByPrimaryKeySelective(hmeEquipmentWkcRelDb);
        //更新后历史
        eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("STATION_EQUIPMENT_LOAD");
            setEventRequestId(eventRequestId);
        }});
        hmeEquipmentWkcRelHis = new HmeEquipmentWkcRelHis();
        hmeEquipmentWkcRelHis.setEventId(eventId);
        BeanUtils.copyProperties(hmeEquipmentWkcRelDb, hmeEquipmentWkcRelHis);
        hmeEquipmentWkcRelHisRepository.insertSelective(hmeEquipmentWkcRelHis);
        result.setSuccess(true);
        return result;
    }

    @Override
    public void confirmReplaceRel(Long tenantId, HmeWkcEquSwitchDTO4 dto) {
        //根据新的设备编码查询设备信息
        HmeEquipment hmeEquipment = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
            setTenantId(tenantId);
            setAssetEncoding(dto.getAssetEncodingLast());
        }});
        // 在更新设备工位关系前，先删除要更新的设备在关系表中已存在的数据
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "STATION_EQUIPMENT_UPDATE");
        HmeEquipmentWkcRel hmeEquipmentWkcRel1 = new HmeEquipmentWkcRel();
        hmeEquipmentWkcRel1.setTenantId(tenantId);
        hmeEquipmentWkcRel1.setEquipmentId(hmeEquipment.getEquipmentId());
        List<HmeEquipmentWkcRel> hmeEquipmentWkcRels = hmeEquipmentWkcRelRepository.select(hmeEquipmentWkcRel1);
        if (CollectionUtils.isNotEmpty(hmeEquipmentWkcRels)) {
            //删除数据
            hmeEquipmentWkcRelRepository.batchDeleteByPrimaryKey(hmeEquipmentWkcRels);
            //记录历史
            String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("STATION_EQUIPMENT_UNLOAD");
                setEventRequestId(eventRequestId);
            }});
            for (HmeEquipmentWkcRel hmeEquipmentWkcRelDb : hmeEquipmentWkcRels) {
                HmeEquipmentWkcRelHis hmeEquipmentWkcRelHis = new HmeEquipmentWkcRelHis();
                hmeEquipmentWkcRelHis.setEventId(eventId);
                BeanUtils.copyProperties(hmeEquipmentWkcRelDb, hmeEquipmentWkcRelHis);
                hmeEquipmentWkcRelHisRepository.insertSelective(hmeEquipmentWkcRelHis);
            }
        }
        //校验当前工位是否已绑定过这个设备
        HmeEquipmentWkcRel hmeEquipmentWkcRel2 = new HmeEquipmentWkcRel();
        hmeEquipmentWkcRel2.setTenantId(tenantId);
        hmeEquipmentWkcRel2.setEquipmentId(hmeEquipment.getEquipmentId());
        hmeEquipmentWkcRel2.setStationId(dto.getWorkcellId());
        hmeEquipmentWkcRel2.setEnableFlag(HmeConstants.ConstantValue.YES);
        List<HmeEquipmentWkcRel> hmeEquipmentWkcRelList = hmeEquipmentWkcRelRepository.select(hmeEquipmentWkcRel2);
        if (CollectionUtils.isNotEmpty(hmeEquipmentWkcRelList)) {
            throw new MtException("HME_WKC_EQUIPMENT_SWITCH_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WKC_EQUIPMENT_SWITCH_005", "HME", dto.getAssetEncodingLast()));
        }
        //更新设备工位关系
        HmeEquipment hmeEquipmentFirst = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
            setTenantId(tenantId);
            setAssetEncoding(dto.getAssetEncodingFirst());
        }});
        HmeEquipmentWkcRel hmeEquipmentWkcRel = new HmeEquipmentWkcRel();
        hmeEquipmentWkcRel.setTenantId(tenantId);
        hmeEquipmentWkcRel.setEquipmentId(hmeEquipmentFirst.getEquipmentId());
        hmeEquipmentWkcRel.setStationId(dto.getWorkcellId());
        HmeEquipmentWkcRel hmeEquipmentWkcRelDb = hmeEquipmentWkcRelRepository.selectOne(hmeEquipmentWkcRel);
        //记录历史 更新前历史
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("STATION_EQUIPMENT_UNLOAD");
            setEventRequestId(eventRequestId);
        }});
        HmeEquipmentWkcRelHis hmeEquipmentWkcRelHis = new HmeEquipmentWkcRelHis();
        hmeEquipmentWkcRelHis.setEventId(eventId);
        BeanUtils.copyProperties(hmeEquipmentWkcRelDb, hmeEquipmentWkcRelHis);
        hmeEquipmentWkcRelHisRepository.insertSelective(hmeEquipmentWkcRelHis);
        //更新数据
        hmeEquipmentWkcRelDb.setEquipmentId(hmeEquipment.getEquipmentId());
        hmeEquipmentWkcRelDb.setObjectVersionNumber(hmeEquipmentWkcRelDb.getObjectVersionNumber());
        hmeEquipmentWkcRelMapper.updateByPrimaryKeySelective(hmeEquipmentWkcRelDb);
        //更新后历史
        eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("STATION_EQUIPMENT_LOAD");
            setEventRequestId(eventRequestId);
        }});
        hmeEquipmentWkcRelHis = new HmeEquipmentWkcRelHis();
        hmeEquipmentWkcRelHis.setEventId(eventId);
        BeanUtils.copyProperties(hmeEquipmentWkcRelDb, hmeEquipmentWkcRelHis);
        hmeEquipmentWkcRelHisRepository.insertSelective(hmeEquipmentWkcRelHis);
    }
}
