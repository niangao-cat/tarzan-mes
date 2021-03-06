package com.ruike.hme.infra.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import com.ruike.hme.infra.mapper.HmeEqManageTaskDocMapper;
import com.ruike.hme.infra.mapper.HmeOpenEndShiftMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.vo.MtWkcShiftVO3;
import tarzan.actual.domain.vo.MtWkcShiftVO7;
import tarzan.calendar.domain.entity.MtCalendarShift;
import tarzan.calendar.domain.repository.MtCalendarRepository;
import tarzan.calendar.domain.repository.MtCalendarShiftRepository;
import tarzan.calendar.domain.vo.*;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModOrganizationRel;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * HmeOpenEndShiftRepositoryImpl
 *
 * @author: chaonan.hu@hand-china.com 2020-07-07 09:51:28
 **/
@Component
public class HmeOpenEndShiftRepositoryImpl implements HmeOpenEndShiftRepository {

    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtCalendarRepository mtCalendarRepository;
    @Autowired
    private MtCalendarShiftRepository mtCalendarShiftRepository;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private HmeOpenEndShiftMapper hmeOpenEndShiftMapper;
    @Autowired
    private HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;
    @Autowired
    private HmeSignInOutRecordRepository hmeSignInOutRecordRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtUserRepository mtUserRepository;
    @Autowired
    private HmeWkcShiftAttrRepository hmeWkcShiftAttrRepository;
    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;
    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;
    @Autowired
    private HmeEqManageTaskDocMapper hmeEqManageTaskDocMapper;

    @Override
    public List<HmeOpenEndShiftVO> lineWorkellDataQuery(Long tenantId) {
        List<HmeOpenEndShiftVO> resultList = new ArrayList<>();

        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        //???????????????????????????????????????????????????
        List<MtUserOrganization> mtUserOrganizationList = mtUserOrganizationRepository.select(new MtUserOrganization() {{
            setTenantId(tenantId);
            setUserId(userId);
            setOrganizationType("WORKCELL");
            setEnableFlag(HmeConstants.ConstantValue.YES);
        }});
        if (CollectionUtils.isEmpty(mtUserOrganizationList)) {
            //??????????????????????????????????????????????????????????????????????????????????????????????????????
            throw new MtException("HME_SHIFT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SHIFT_001", "HME"));
        }
        for (MtUserOrganization mtUserOrganization : mtUserOrganizationList) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectOne(new MtModWorkcell() {{
                setTenantId(tenantId);
                setWorkcellId(mtUserOrganization.getOrganizationId());
                setWorkcellType("LINE");
                setEnableFlag(HmeConstants.ConstantValue.YES);
            }});
            if (mtModWorkcell != null) {
                HmeOpenEndShiftVO hmeOpenEndShiftVO = new HmeOpenEndShiftVO();
                hmeOpenEndShiftVO.setLineWorkcellId(mtModWorkcell.getWorkcellId());
                hmeOpenEndShiftVO.setLineWorkcellName(mtModWorkcell.getWorkcellName());
                resultList.add(hmeOpenEndShiftVO);
            }
        }
        return resultList;
    }

    @Override
    public List<HmeOpenEndShiftVO2> shiftQuery(Long tenantId, HmeOpenEndShiftDTO dto) {
        List<HmeOpenEndShiftVO2> resultList = new ArrayList<>();
        MtCalendarVO2 mtCalendarVO2 = new MtCalendarVO2();
        mtCalendarVO2.setOrganizationType("WORKCELL");
        mtCalendarVO2.setOrganizationId(dto.getLineWorkcellId());
        mtCalendarVO2.setSiteType("MANUFACTURING");
        mtCalendarVO2.setCalendarType("STANDARD");
        String calendarId = mtCalendarRepository.organizationLimitOnlyCalendarGet(tenantId, mtCalendarVO2);
        if (StringUtils.isEmpty(calendarId)) {
            throw new MtException("HME_SHIFT_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SHIFT_003", "HME"));
        }
        //??????api???calendarLimitShiftQuery???
        MtCalendarShiftVO2 mtCalendarShiftVO2 = new MtCalendarShiftVO2();
        mtCalendarShiftVO2.setCalendarId(calendarId);
        ZonedDateTime zonedDateTime = dto.getDate().atStartOfDay(ZoneId.systemDefault());
        mtCalendarShiftVO2.setShiftDate(Date.from(zonedDateTime.toInstant()));
        List<String> calenderShiftIdList = mtCalendarShiftRepository.calendarLimitShiftQuery(tenantId, mtCalendarShiftVO2);
        //??????api???calendarShiftBatchGet???
        List<MtCalendarShift> mtCalendarShiftList = mtCalendarShiftRepository.calendarShiftBatchGet(tenantId, calenderShiftIdList);
        for (MtCalendarShift mtCalendarShift : mtCalendarShiftList) {
            //??????????????????
            HmeOpenEndShiftVO2 hmeOpenEndShiftVO2 = new HmeOpenEndShiftVO2();
            hmeOpenEndShiftVO2.setCalendarShiftId(mtCalendarShift.getCalendarShiftId());
            hmeOpenEndShiftVO2.setShiftCode(mtCalendarShift.getShiftCode());
            hmeOpenEndShiftVO2.setShiftStartTime(mtCalendarShift.getShiftStartTime());
            hmeOpenEndShiftVO2.setShiftEndTime(mtCalendarShift.getShiftEndTime());
            hmeOpenEndShiftVO2.setShiftDate(mtCalendarShift.getShiftDate());
            resultList.add(hmeOpenEndShiftVO2);
        }
        return resultList;
    }

    @Override
    public HmeOpenEndShiftVO4 shiftDateAndCodeQuery(Long tenantId, String lineWorkcellId) {
        HmeOpenEndShiftVO4 hmeOpenEndShiftVO4 = new HmeOpenEndShiftVO4();
        try {
            MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, lineWorkcellId);
            if (mtWkcShiftVO3 != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String shiftDate = simpleDateFormat.format(mtWkcShiftVO3.getShiftDate());
                hmeOpenEndShiftVO4.setShiftDateAndCode(shiftDate + " " + mtWkcShiftVO3.getShiftCode());
                hmeOpenEndShiftVO4.setShiftDate(shiftDate);
                hmeOpenEndShiftVO4.setShiftCode(mtWkcShiftVO3.getShiftCode());
            } else {
                hmeOpenEndShiftVO4.setShiftDateAndCode("????????????????????????");
            }
        } catch (Exception ex) {
            hmeOpenEndShiftVO4.setShiftDateAndCode("????????????????????????");
        }
        return hmeOpenEndShiftVO4;
    }

    @Override
    public HmeOpenEndShiftVO3 dateTimeQuery(Long tenantId, HmeOpenEndShiftDTO2 dto) {
        HmeOpenEndShiftVO3 result = new HmeOpenEndShiftVO3();
        ZonedDateTime zonedDateTime = dto.getShiftDate().atStartOfDay(ZoneId.systemDefault());
        //???????????????????????????
        MtWkcShift mtWkcShift = mtWkcShiftRepository.selectOne(new MtWkcShift() {{
            setTenantId(tenantId);
            setWorkcellId(dto.getLineWorkcellId());
            setShiftDate(Date.from(zonedDateTime.toInstant()));
            setShiftCode(dto.getShiftCode());
        }});
        //??????????????????
        result.setShiftStartTime(dto.getShiftStartTime());
        result.setShiftEndTime(dto.getShiftEndTime());
        if (mtWkcShift != null) {
            result.setWkcShiftId(mtWkcShift.getWkcShiftId());
            if (mtWkcShift.getShiftStartTime() != null) {
                result.setShiftActualStartTime(mtWkcShift.getShiftStartTime());
            }
            if (mtWkcShift.getShiftEndTime() != null) {
                result.setShiftActualEndTime(mtWkcShift.getShiftEndTime());
            }
        }
        return result;
    }

    @Override
    public Date openShiftActualDate(Long tenantId, HmeOpenEndShiftDTO2 dto) {
        //??????api???wkcShiftStart??? ??????wkcShiftId
        MtWkcShiftVO7 mtWkcShiftVO7 = new MtWkcShiftVO7();
        mtWkcShiftVO7.setWorkcellId(dto.getLineWorkcellId());
        ZonedDateTime zonedDateTime = dto.getShiftDate().atStartOfDay(ZoneId.systemDefault());
        mtWkcShiftVO7.setShiftDate(Date.from(zonedDateTime.toInstant()));
        mtWkcShiftVO7.setShiftCode(dto.getShiftCode());
        String wkcShiftId = mtWkcShiftRepository.wkcShiftStart(tenantId, mtWkcShiftVO7);
        return mtWkcShiftRepository.selectByPrimaryKey(wkcShiftId).getShiftStartTime();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Date endShiftActualDate(Long tenantId, HmeOpenEndShiftDTO2 dto) {
        //??????api???wkcShiftEnd??? ??????
        mtWkcShiftRepository.wkcShiftEnd(tenantId, dto.getLineWorkcellId());
        ZonedDateTime zonedDateTime = dto.getShiftDate().atStartOfDay(ZoneId.systemDefault());
        //????????????????????????
        MtWkcShift mtWkcShift = mtWkcShiftRepository.selectOne(new MtWkcShift() {{
            setTenantId(tenantId);
            setWorkcellId(dto.getLineWorkcellId());
            setShiftDate(Date.from(zonedDateTime.toInstant()));
            setShiftCode(dto.getShiftCode());
        }});
        // ??????????????????????????????
        // ???????????????????????????
        List<MtUserOrganization> mtUserOrganizationList = mtUserOrganizationRepository.selectByCondition(Condition.builder(MtUserOrganization.class).andWhere(Sqls.custom()
                .andEqualTo(MtUserOrganization.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtUserOrganization.FIELD_ORGANIZATION_ID, mtWkcShift.getWorkcellId())
                .andEqualTo(MtUserOrganization.FIELD_ENABLE_FLAG, HmeConstants.ConstantValue.YES)).build());
        if (CollectionUtils.isNotEmpty(mtUserOrganizationList)) {
            Date curDate = CommonUtils.currentTimeGet();
            for (MtUserOrganization mtUserOrganization : mtUserOrganizationList) {
                // ??????????????????????????? ?????? ?????????????????????
                List<HmeSignInOutRecord> recordList = hmeSignInOutRecordRepository.selectByCondition(Condition.builder(HmeSignInOutRecord.class).andWhere(Sqls.custom()
                        .andEqualTo(HmeSignInOutRecord.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeSignInOutRecord.FIELD_WORKCELL_ID, mtWkcShift.getWorkcellId())
                        .andEqualTo(HmeSignInOutRecord.FIELD_USER_ID, mtUserOrganization.getUserId())
                        .andEqualTo(HmeSignInOutRecord.FIELD_SHIFT_CODE, mtWkcShift.getShiftCode())
                        .andEqualTo(HmeSignInOutRecord.FIELD_DATE, Date.from(zonedDateTime.toInstant())))
                        .orderByDesc(HmeSignInOutRecord.FIELD_CREATION_DATE).build());
                if (CollectionUtils.isNotEmpty(recordList)) {
                    // ?????????????????????
                    Optional<HmeSignInOutRecord> closeOpt = recordList.stream().filter(record -> StringUtils.equals(record.getOperation(), HmeConstants.ApiSignInOutRecordValue.OPERATION_CLOSE)).findFirst();
                    if (!closeOpt.isPresent()) {
                        // ????????? ???????????????
                        Optional<HmeSignInOutRecord> openOpt = recordList.stream().filter(rd -> HmeConstants.ApiSignInOutRecordValue.OPERATION_OPEN.equals(rd.getOperation())).findFirst();
                        HmeSignInOutRecord record = new HmeSignInOutRecord();
                        record.setTenantId(tenantId);
                        record.setRelId(openOpt.get().getRecordId());
                        record.setUserId(mtUserOrganization.getUserId());
                        // ????????????
                        HmeSignInOutRecordDTO1 hmeSignInOutRecordDTO1 = hmeSignInOutRecordRepository.getEemployeeQuery(tenantId, mtUserOrganization.getUserId());
                        if (hmeSignInOutRecordDTO1 == null) {
                            throw new MtException("HME_STAFF_0001", mtErrorMessageRepository
                                    .getErrorMessageWithModule(tenantId, "HME_STAFF_0001", "HME"));
                        }
                        record.setEmployeeId(hmeSignInOutRecordDTO1.getEmployeeId());
                        record.setDate(Date.from(zonedDateTime.toInstant()));
                        record.setShiftCode(mtWkcShift.getShiftCode());
                        record.setOperationDate(curDate);
                        record.setOperation(HmeConstants.ApiSignInOutRecordValue.OPERATION_CLOSE);
                        record.setWorkcellId(mtWkcShift.getWorkcellId());
                        // ??????????????????  ????????????????????? ????????????????????????????????? ????????? ??????????????????
                        // ???????????????????????????
                        HmeSignInOutRecord signInOutRecord = recordList.get(0);
                        if (HmeConstants.ApiSignInOutRecordValue.OPERATION_OFF.equals(signInOutRecord.getOperation())) {
                            record.setDuration(signInOutRecord.getDuration());
                        } else if (HmeConstants.ApiSignInOutRecordValue.OPERATION_ON.equals(signInOutRecord.getOperation()) || HmeConstants.ApiSignInOutRecordValue.OPERATION_OPEN.equals(signInOutRecord.getOperation())) {
                            // ???????????????????????????????????? ??????????????????
                            Long addTime = strFormatToTime(signInOutRecord.getDuration());
                            long diffTime = curDate.getTime() - signInOutRecord.getOperationDate().getTime();
                            record.setDuration(dateFormatToDuration(diffTime + addTime));
                        }
                        hmeSignInOutRecordRepository.insertSelective(record);
                    }
                }
            }
        }
        // ??????????????????????????????????????????doc_status??????off
        // 20210524 add by sanfeng.zhang for peng.zhao ?????????????????????  ???????????????????????? ??????????????????
        List<HmeEqManageTaskDoc> taskDocList = hmeEqManageTaskDocMapper.select(new HmeEqManageTaskDoc() {{
            setTenantId(tenantId);
            setShiftCode(mtWkcShift.getShiftCode());
            setShiftDate(mtWkcShift.getShiftDate());
            setDocStatus(HmeConstants.ConstantValue.WAITING);
        }});
        if (CollectionUtils.isNotEmpty(taskDocList)) {
            List<String> taskDocIdList = taskDocList.stream().map(HmeEqManageTaskDoc::getTaskDocId).distinct().collect(Collectors.toList());
            List<String> undoneTaskDocIdList = hmeEqManageTaskDocMapper.queryUndoneTaskDocByWkcShift(tenantId, taskDocIdList, mtWkcShift.getShiftStartTime(), mtWkcShift.getShiftEndTime());
            List<HmeEqManageTaskDoc> manageTaskDocList = taskDocList.stream().map(doc -> {
                if (undoneTaskDocIdList.contains(doc.getTaskDocId())) {
                    doc.setDocStatus(HmeConstants.ConstantValue.UNDONE);
                } else {
                    doc.setDocStatus(HmeConstants.ConstantValue.OFF);
                }
                return doc;
            }).collect(Collectors.toList());
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            hmeEqManageTaskDocMapper.batchUpdateManageTaskDoc(tenantId, userId, manageTaskDocList);
        }
        return mtWkcShift.getShiftEndTime();
    }

    private Long strFormatToTime(String during) {
        // ?????? 00:00:00
        List<String> timeList = Arrays.asList(during.split(":"));
        long hour = Long.valueOf(timeList.get(0));
        long min = Long.valueOf(timeList.get(1));
        long sec = Long.valueOf(timeList.get(2));
        return (hour * 24 * 3600 + min * 60 + sec) * 1000;
    }

    private String dateFormatToDuration(long diffTime) {
        // ??????
        long hour = (diffTime / 3600 / 1000) % 24;
        // ??????
        long min = (diffTime / 1000 / 60) % 60;
        // ???
        long sec = diffTime / 1000 % 60;
        StringBuffer sb = new StringBuffer();
        sb.append(hour >= 10 ? hour : "0" + hour);
        sb.append(":");
        sb.append(min >= 10 ? min : "0" + min);
        sb.append(":");
        sb.append(sec >= 10 ? sec : "0" + sec);
        return sb.toString();
    }


    @Override
    public HmeShiftVO shiftDataQuery(Long tenantId, HmeShiftDTO dto) {
        HmeShiftVO hmeShiftVO = new HmeShiftVO();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ZonedDateTime zonedDateTime = dto.getShiftDate().atStartOfDay(ZoneId.systemDefault());
        String shiftDateStr = simpleDateFormat.format(Date.from(zonedDateTime.toInstant()));
        //??????API {subOrganizationRelQuery}????????????????????????????????????
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
        mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
        mtModOrganizationVO2.setParentOrganizationId(dto.getLineWorkcellId());
        mtModOrganizationVO2.setOrganizationType("WORKCELL");
        mtModOrganizationVO2.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        List<String> workcellIds = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
        //??????
        String unitId = hmeOpenEndShiftMapper.getUnitByLineWorkcellId(dto.getLineWorkcellId());
        if(CollectionUtils.isNotEmpty(workcellIds)){
            Date nowDate = new Date();
            if (dto.getShiftActualStartTime() != null && CollectionUtils.isNotEmpty(workcellIds)) {
                Date shiftActualEndTime = dto.getShiftActualEndTime();
                if (shiftActualEndTime == null) {
                    shiftActualEndTime = nowDate;
                }
                List<HmeShiftVO4> hmeShiftVO4List = hmeOpenEndShiftMapper.shiftDataQuery(tenantId, workcellIds, dto.getWkcShiftId(), dto.getShiftActualStartTime(), shiftActualEndTime);
                if(CollectionUtils.isEmpty(hmeShiftVO4List)){
                    for (String workcellId:workcellIds) {
                        HmeShiftVO4 hmeShiftVO4 = new HmeShiftVO4();
                        hmeShiftVO4.setWorkcellId(workcellId);
                        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(workcellId);
                        hmeShiftVO4.setWorkcellName(mtModWorkcell.getWorkcellName());
                        hmeShiftVO4List.add(hmeShiftVO4);
                    }
                }else{
                    //????????????????????????+????????????
                    hmeShiftVO4List = hmeShiftVO4List.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
                            -> new TreeSet<>(Comparator.comparing(o -> o.getWorkcellId() + ";" + o.getEmployeeId()))), ArrayList::new));
                    for (HmeShiftVO4 hmeShiftVO4 : hmeShiftVO4List) {
                        //????????????
                        MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.valueOf(hmeShiftVO4.getEmployeeId()));
                        hmeShiftVO4.setEmpolyeeName(mtUserInfo.getRealName());
                        //???????????????
                        if(StringUtils.isNotEmpty(unitId)){
                            Date mountGuardDate = hmeOpenEndShiftMapper.mountLaidDateQuery(tenantId, hmeShiftVO4.getEmployeeId(), unitId, hmeShiftVO4.getWorkcellId(),
                                    dto.getShiftCode(), shiftDateStr, "OPEN");
                            hmeShiftVO4.setMountGuardDate(mountGuardDate);
                            Date laidOffDate = hmeOpenEndShiftMapper.mountLaidDateQuery(tenantId, hmeShiftVO4.getEmployeeId(), unitId, hmeShiftVO4.getWorkcellId(),
                                    dto.getShiftCode(), shiftDateStr, "CLOSE");
                            hmeShiftVO4.setLaidOffDate(laidOffDate);
                        }
                        //??????
                        BigDecimal production = BigDecimal.ZERO;
                        List<String> materialLotIdList = hmeOpenEndShiftMapper.getMaterialLotId(tenantId, dto.getShiftActualStartTime(),
                                shiftActualEndTime, hmeShiftVO4.getEmployeeId(), hmeShiftVO4.getWorkcellId());
                        materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
                        MtMaterialLot mtMaterialLot = null;
                        for (String materialLotId : materialLotIdList) {
                            mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
                            production = production.add(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                        }
                        //?????????
                        BigDecimal ncNumber = BigDecimal.ZERO;
                        materialLotIdList = hmeOpenEndShiftMapper.getMaterialLotId2(tenantId, dto.getShiftActualStartTime(),
                                shiftActualEndTime, hmeShiftVO4.getEmployeeId(), hmeShiftVO4.getWorkcellId());
                        materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
                        for (String materialLotId : materialLotIdList) {
                            mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
                            ncNumber = ncNumber.add(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                        }
                        //?????????
                        if (production.compareTo(BigDecimal.ZERO) == 0) {
                            hmeShiftVO4.setPassPercent("--");
                        } else {
                            BigDecimal passPercentB = ((production.subtract(ncNumber)).divide(production, 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                            hmeShiftVO4.setPassPercent(passPercentB.toString() + "%");
                        }
                        //?????????
                        BigDecimal repairNumber = BigDecimal.ZERO;
                        materialLotIdList = hmeOpenEndShiftMapper.getMaterialLotId3(tenantId, dto.getShiftActualStartTime(),
                                shiftActualEndTime, hmeShiftVO4.getEmployeeId(), hmeShiftVO4.getWorkcellId());
//                        materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
//                        for (String materialLotId : materialLotIdList) {
//                            mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
//                            repairNumber = repairNumber.add(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
//                        }
                        hmeShiftVO4.setProduction(production);
                        hmeShiftVO4.setNcNumber(ncNumber);
                        hmeShiftVO4.setRepairNumber(BigDecimal.valueOf(materialLotIdList.size()));
                    }
                }
                hmeShiftVO.setShiftDataList(hmeShiftVO4List);
            }
        }
        //??????
        if (StringUtils.isEmpty(unitId)) {
            return hmeShiftVO;
        }
        ResponseEntity<HmeHzeroPlatformUnitDTO> unitsInfo = hmeHzeroPlatformFeignClient.getUnitsInfo(tenantId, unitId);
        if (unitsInfo.getBody() != null) {
            hmeShiftVO.setUnitName(unitsInfo.getBody().getUnitName());
        }
        //??????
        List<String> groupLeaderList = new ArrayList<>();
        //????????????????????????
        String positionId = null;
        //?????????????????????????????????????????????????????????????????????
        List<String> positionIdList = new ArrayList<>();
        ResponseEntity<List<HmeShiftVO2>> positionResponse = hmeHzeroPlatformFeignClient.queryPositionByUnit(tenantId, unitId);
        if (CollectionUtils.isNotEmpty(positionResponse.getBody())) {
            for (HmeShiftVO2 position : positionResponse.getBody()) {
                if ("1".equals(position.getSupervisorFlag())) {
                    positionId = position.getPositionId();
                }
                positionIdList.add(position.getPositionId());
            }
        }
        if (StringUtils.isNotEmpty(positionId)) {
            //????????????????????????
            ResponseEntity<String> headers = hmeHzeroPlatformFeignClient.queryEmployeeIdByPositionId(tenantId, unitId, positionId, "0", "999999");
            Page<Map<String, Object>> page = new Page<Map<String, Object>>();
            try {
                page = objectMapper.readValue(headers.getBody(), page.getClass());
            } catch (IOException e) {

            }
            if (CollectionUtils.isNotEmpty(page.getContent())) {
                List<Map<String, Object>> content = page.getContent();
                for (Map<String, Object> employeeMap : content) {
                    String employeeId = String.valueOf(employeeMap.get("employeeId"));
                    //????????????????????????
                    ResponseEntity<HmeShiftVO3> employee = hmeHzeroPlatformFeignClient.queryEmployeeNameById(tenantId, employeeId);
                    if (employee.getBody() != null) {
                        groupLeaderList.add(employee.getBody().getName());
                    }
                }
            }
        }
        hmeShiftVO.setGroupLeaderList(groupLeaderList);
        //????????????
        Long planAttendance = 0L;
        for (String positionId2 : positionIdList) {
            ResponseEntity<String> headers = hmeHzeroPlatformFeignClient.queryEmployeeIdByPositionId(tenantId, unitId, positionId2, "0", "999999");
            Page<Map<String, Object>> page = new Page<Map<String, Object>>();
            try {
                page = objectMapper.readValue(headers.getBody(), page.getClass());
            } catch (IOException e) {

            }
            planAttendance = planAttendance + page.getTotalElements();
        }
        hmeShiftVO.setPlanAttendance(planAttendance);
        //????????????
        Long actualAttendance = hmeOpenEndShiftMapper.actualAttendanceQuery(tenantId, shiftDateStr, dto.getShiftCode(), unitId);
        hmeShiftVO.setActualAttendance(actualAttendance);
        return hmeShiftVO;
    }

    @Override
    public Page<HmeShiftVO5> completeStatistical(Long tenantId, HmeShiftDTO dto, PageRequest pageRequest) {
        Page<HmeShiftVO5> resultPage = new Page<>();
        //??????API {subOrganizationRelQuery}????????????????????????????????????
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
        mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
        mtModOrganizationVO2.setParentOrganizationId(dto.getLineWorkcellId());
        mtModOrganizationVO2.setOrganizationType("WORKCELL");
        mtModOrganizationVO2.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        List<String> workcellIds = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
        //??????API {subOrganizationRelQuery}??????????????????????????????????????????
        List<MtModOrganizationRel> mtModOrganizationRelList = new ArrayList<>();
        mtModOrganizationVO2.setQueryType("ALL");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOList = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOList) {
            String workcellId = mtModOrganizationItemVO.getOrganizationId();
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(workcellId);
            if ("PROCESS".equals(mtModWorkcell.getWorkcellType())) {
                List<MtModOrganizationRel> mtModOrganizationRelS = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
                    setTenantId(tenantId);
                    setOrganizationType("WORKCELL");
                    setOrganizationId(workcellId);
                }});
                if (CollectionUtils.isNotEmpty(mtModOrganizationRelS)) {
                    mtModOrganizationRelList.addAll(mtModOrganizationRelS);
                }
            }
        }
        List<String> firstWorkcellIdList = new ArrayList<>();
        List<String> endWorkcellIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtModOrganizationRelList)) {
            mtModOrganizationRelList.removeIf(c -> c.getSequence()== null);
            mtModOrganizationRelList = mtModOrganizationRelList.stream().sorted(Comparator.comparing(MtModOrganizationRel::getSequence)).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(mtModOrganizationRelList)){
                throw new MtException("HME_SHIFT_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SHIFT_005", "HME"));
            }
            //??????????????????????????????
            mtModOrganizationVO2.setParentOrganizationId(mtModOrganizationRelList.get(0).getOrganizationId());
            mtModOrganizationVO2.setQueryType("BOTTOM");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS1 = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS1) {
                firstWorkcellIdList.add(mtModOrganizationItemVO.getOrganizationId());
            }
            //??????????????????????????????
            mtModOrganizationVO2.setParentOrganizationId(mtModOrganizationRelList.get((mtModOrganizationRelList.size() - 1)).getOrganizationId());
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS2 = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS2) {
                endWorkcellIdList.add(mtModOrganizationItemVO.getOrganizationId());
            }
        }
        Date shiftActualEndTime = (dto.getShiftActualEndTime() == null ? new Date() : dto.getShiftActualEndTime());
        List<String> workcellIdList = new ArrayList<>();
        if (CollectionUtils.isEmpty(firstWorkcellIdList) || CollectionUtils.isEmpty(endWorkcellIdList)) {
            return resultPage;
        } else {
            workcellIdList.addAll(firstWorkcellIdList);
            workcellIdList.addAll(endWorkcellIdList);
        }
        resultPage = PageHelper.doPageAndSort(pageRequest, () -> hmeOpenEndShiftMapper.eoJobSnDataQuery(tenantId, dto.getShiftActualStartTime(), shiftActualEndTime, workcellIdList));
        for (HmeShiftVO5 hmeShiftVO5 : resultPage) {
            String key = hmeShiftVO5.getMaterialId();
            String key2 = hmeShiftVO5.getWorkOrderId();
            //????????????
            BigDecimal shiftProduction = BigDecimal.ZERO;
            //????????????ID?????????ID?????????????????????ID????????????????????????ID,?????????????????????????????????
            if (CollectionUtils.isNotEmpty(firstWorkcellIdList)) {
                List<String> materialLotIdList = hmeOpenEndShiftMapper.getMaterialLotId4(tenantId, key, key2,
                        firstWorkcellIdList, dto.getShiftActualStartTime(), shiftActualEndTime);
                materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
                for (String materialLotId : materialLotIdList) {
                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
                    shiftProduction = shiftProduction.add(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                }
            }
            hmeShiftVO5.setShiftProduction(shiftProduction);
            //2020-09-22 15:03 add by chaonan.hu fou fang.pan ??????????????????
            BigDecimal dispatchNumber = BigDecimal.ZERO;
            String dispatchNumberStr = hmeOpenEndShiftMapper.getShiftProduction(tenantId, hmeShiftVO5.getWorkOrderId(), dto.getLineWorkcellId(), dto.getCalendarShiftId());
            if(StringUtils.isNotEmpty(dispatchNumberStr)){
                dispatchNumber = new BigDecimal(dispatchNumberStr);
            }
            hmeShiftVO5.setDispatchNumber(dispatchNumber);
            //????????????
            //2020-09-22 15:03 edit by chaonan.hu fou fang.pan ??????????????????????????????
            BigDecimal shiftComplete = hmeOpenEndShiftMapper.getShiftComplete(tenantId, hmeShiftVO5.getWorkOrderId(), dto.getWkcShiftId(), dto.getLineWorkcellId());
//            //????????????ID?????????ID?????????????????????ID????????????????????????ID,?????????????????????????????????
//            BigDecimal shiftComplete = BigDecimal.ZERO;
//            if (CollectionUtils.isNotEmpty(endWorkcellIdList)) {
//                List<String> materialLotIdList = hmeOpenEndShiftMapper.getMaterialLotId4(tenantId, key, key2,
//                        endWorkcellIdList, dto.getShiftActualStartTime(), shiftActualEndTime);
//                materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
//                for (String materialLotId : materialLotIdList) {
//                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
//                    shiftComplete = shiftComplete.add(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
//                }
//            }
            hmeShiftVO5.setShiftComplete(shiftComplete);
            //?????????
            BigDecimal ncNumber = BigDecimal.ZERO;
            //??????WO??????EO
            List<MtEo> mtEoList = mtEoRepository.select(new MtEo() {{
                setTenantId(tenantId);
                setWorkOrderId(key2);
            }});
            if (CollectionUtils.isNotEmpty(mtEoList) && CollectionUtils.isNotEmpty(workcellIds)) {
                List<String> eoIds = mtEoList.stream().map(MtEo::getEoId).collect(Collectors.toList());
                List<String> materialLotIdList = hmeOpenEndShiftMapper.getMaterialLotId5(tenantId, dto.getShiftActualStartTime(), shiftActualEndTime, workcellIds, eoIds);
                materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
                for (String materialLotId : materialLotIdList) {
                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
                    ncNumber = ncNumber.add(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                }
            }
            hmeShiftVO5.setNcNumber(ncNumber);
        }
        return resultPage;
    }

    @Override
    public HmeShiftVO7 productBeat(Long tenantId, HmeShiftDTO dto) {
        HmeShiftVO7 result = new HmeShiftVO7();
        List<String> xDataList = new ArrayList<>();
        List<BigDecimal> yDataList = new ArrayList<>();
        //??????API {subOrganizationRelQuery}??????????????????????????????????????????
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
        mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
        mtModOrganizationVO2.setParentOrganizationId(dto.getLineWorkcellId());
        mtModOrganizationVO2.setOrganizationType("WORKCELL");
        List<MtModOrganizationRel> mtModOrganizationRelList = new ArrayList<>();
        mtModOrganizationVO2.setQueryType("ALL");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOList = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOList) {
            String workcellId = mtModOrganizationItemVO.getOrganizationId();
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(workcellId);
            if ("PROCESS".equals(mtModWorkcell.getWorkcellType())) {
                List<MtModOrganizationRel> mtModOrganizationRelS = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
                    setTenantId(tenantId);
                    setOrganizationType("WORKCELL");
                    setOrganizationId(workcellId);
                }});
                if (CollectionUtils.isNotEmpty(mtModOrganizationRelS)) {
                    mtModOrganizationRelList.addAll(mtModOrganizationRelS);
                }
            }
        }
        List<String> endWorkcellIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtModOrganizationRelList)) {
            mtModOrganizationRelList.removeIf(c -> c.getSequence()== null);
            mtModOrganizationRelList = mtModOrganizationRelList.stream().sorted(Comparator.comparing(MtModOrganizationRel::getSequence)).collect(Collectors.toList());
            //??????????????????????????????
            mtModOrganizationVO2.setParentOrganizationId(mtModOrganizationRelList.get((mtModOrganizationRelList.size() - 1)).getOrganizationId());
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS2 = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS2) {
                endWorkcellIdList.add(mtModOrganizationItemVO.getOrganizationId());
            }
        }
        if (CollectionUtils.isNotEmpty(endWorkcellIdList)) {
            Date nowDate = new Date();
            Date shiftActualEndTime = dto.getShiftActualEndTime();
            Date shiftActualStartTime = dto.getShiftActualStartTime();
            if (shiftActualEndTime == null) {
                shiftActualEndTime = nowDate;
            }
            Calendar calendar = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            BigDecimal yData = BigDecimal.ZERO;
            do {
                //???30??????????????????
                calendar = Calendar.getInstance();
                calendar.setTime(shiftActualStartTime);
                calendar.add(Calendar.MINUTE, 30);
                Date siteInDateTo = calendar.getTime();
                if (siteInDateTo.compareTo(shiftActualEndTime) == -1) {
                    xDataList.add(simpleDateFormat.format(siteInDateTo));
                } else {
                    xDataList.add(simpleDateFormat.format(shiftActualEndTime));
                }
                List<String> materialLotIdList = hmeOpenEndShiftMapper.getMaterialLotId6(tenantId, shiftActualStartTime, siteInDateTo, endWorkcellIdList);
                materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
                for (String materialLotId : materialLotIdList) {
                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
                    yData = yData.add(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                }
                yDataList.add(yData);
                shiftActualStartTime = siteInDateTo;
            } while (shiftActualStartTime.compareTo(shiftActualEndTime) == -1);
        }
        result.setXDataList(xDataList);
        result.setYDataList(yDataList);
        return result;
    }

    @Override
    public HmeShiftVO8 handoverMattersQuery(Long tenantId, String wkcShiftId) {
        HmeShiftVO8 hmeShiftVO8 = new HmeShiftVO8();
        HmeWkcShiftAttr hmeWkcShiftAttr = hmeWkcShiftAttrRepository.selectOne(new HmeWkcShiftAttr() {{
            setTenantId(tenantId);
            setWkcShiftId(wkcShiftId);
        }});
        if(hmeWkcShiftAttr != null){
            hmeShiftVO8.setHandoverMatter(hmeWkcShiftAttr.getRemark());
        }
        return hmeShiftVO8;
    }

    @Override
    public List<HmeShiftVO9> monthSecurityCalendar(Long tenantId, String siteId, String lineWorkcellId) {
        List<HmeShiftVO9> resultList = new ArrayList<>();
        //??????API {subOrganizationRelQuery}????????????????????????????????????
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(siteId);
        mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
        mtModOrganizationVO2.setParentOrganizationId(lineWorkcellId);
        mtModOrganizationVO2.setOrganizationType("WORKCELL");
        mtModOrganizationVO2.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        List<String> workcellIds = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(workcellIds)){
            return resultList;
        }
        //????????????????????????????????????
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date minDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date maxDate = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        HmeShiftVO9 hmeShiftVO9 = null;
        do{
            //??????
            hmeShiftVO9 = new HmeShiftVO9();
            hmeShiftVO9.setSecurityDate(minDate);
            String minDateStr = simpleDateFormat.format(minDate);
            //???
            String[] split = minDateStr.split("-");
            hmeShiftVO9.setDay(split[2]);
            //??????ID??????
            List<String> exceptionIdList = hmeOpenEndShiftMapper.getExceptionIdList(tenantId, minDateStr, workcellIds);
            hmeShiftVO9.setExceptionIdList(exceptionIdList);
            //?????????
            hmeShiftVO9.setExceptionNumber(BigDecimal.valueOf(exceptionIdList.size()));
            resultList.add(hmeShiftVO9);
            calendar = Calendar.getInstance();
            calendar.setTime(minDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            minDate = calendar.getTime();
        }while (minDate.compareTo(maxDate) <= 0);
        return resultList;
    }

    @Override
    public HmeShiftVO7 operationQuality(Long tenantId, HmeShiftDTO dto) {
        HmeShiftVO7 hmeShiftVO7 = new HmeShiftVO7();
        List<String> xDataList = new ArrayList<>();
        List<BigDecimal> yDataList = new ArrayList<>();
        //??????API {subOrganizationRelQuery}????????????????????????????????????
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
        mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
        mtModOrganizationVO2.setParentOrganizationId(dto.getLineWorkcellId());
        mtModOrganizationVO2.setOrganizationType("WORKCELL");
        mtModOrganizationVO2.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        List<String> workcellIds = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(workcellIds)){
            Date nowDate = new Date();
            Date shiftActualEndTime = dto.getShiftActualEndTime();
            Date shiftActualStartTime = dto.getShiftActualStartTime();
            if (shiftActualEndTime == null) {
                shiftActualEndTime = nowDate;
            }
            Calendar calendar = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            do{
                //????????????????????????
                calendar = Calendar.getInstance();
                calendar.setTime(shiftActualStartTime);
                calendar.add(Calendar.HOUR, 1);
                Date siteInDateTo = calendar.getTime();
                if (siteInDateTo.compareTo(shiftActualEndTime) == -1) {
                    xDataList.add(simpleDateFormat.format(siteInDateTo));
                } else {
                    xDataList.add(simpleDateFormat.format(shiftActualEndTime));
                }
                List<String> exceptionIdList = hmeOpenEndShiftMapper.getExceptionIdList2(tenantId, shiftActualStartTime, shiftActualEndTime, workcellIds);
                BigDecimal yData = BigDecimal.ZERO;
                if(CollectionUtils.isNotEmpty(exceptionIdList)){
                    yData = BigDecimal.valueOf(exceptionIdList.size());
                }
                yDataList.add(yData);
                shiftActualStartTime = siteInDateTo;
            }while (shiftActualStartTime.compareTo(shiftActualEndTime) == -1);
            hmeShiftVO7.setXDataList(xDataList);
            hmeShiftVO7.setYDataList(yDataList);
        }
        return hmeShiftVO7;
    }

    @Override
    public HmeShiftVO7 equipmentManage(Long tenantId, HmeShiftDTO dto) {
        HmeShiftVO7 hmeShiftVO7 = new HmeShiftVO7();
        List<String> xDataList = new ArrayList<>();
        List<BigDecimal> yDataList = new ArrayList<>();
        //??????API {subOrganizationRelQuery}????????????????????????????????????
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
        mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
        mtModOrganizationVO2.setParentOrganizationId(dto.getLineWorkcellId());
        mtModOrganizationVO2.setOrganizationType("WORKCELL");
        mtModOrganizationVO2.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        List<String> workcellIds = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(workcellIds)){
            Date nowDate = new Date();
            Date shiftActualEndTime = dto.getShiftActualEndTime();
            Date shiftActualStartTime = dto.getShiftActualStartTime();
            if (shiftActualEndTime == null) {
                shiftActualEndTime = nowDate;
            }
            List<HmeShiftVO10> hmeShiftVO10s = hmeOpenEndShiftMapper.equipmenteExceptionQuery(tenantId, shiftActualStartTime, shiftActualEndTime, workcellIds);
            if(CollectionUtils.isNotEmpty(hmeShiftVO10s)){
                Map<String, Long> map = hmeShiftVO10s.stream().collect(Collectors.groupingBy(HmeShiftVO10::getEquipmentId, Collectors.counting()));
                for (Map.Entry<String, Long> entry:map.entrySet()) {
                    String key = entry.getKey();
                    Long value = entry.getValue();
                    HmeEquipment hmeEquipment = hmeEquipmentRepository.selectByPrimaryKey(key);
                    xDataList.add(hmeEquipment.getAssetName());
                    yDataList.add(BigDecimal.valueOf(value));
                }
            }
        }
        hmeShiftVO7.setXDataList(xDataList);
        hmeShiftVO7.setYDataList(yDataList);
        return hmeShiftVO7;
    }

    @Override
    public Page<HmeShiftVO11> otherException(Long tenantId, HmeShiftDTO dto, PageRequest pageRequest) {
        Page<HmeShiftVO11> resultPage = new Page<>();
        //??????API {subOrganizationRelQuery}????????????????????????????????????
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
        mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
        mtModOrganizationVO2.setParentOrganizationId(dto.getLineWorkcellId());
        mtModOrganizationVO2.setOrganizationType("WORKCELL");
        mtModOrganizationVO2.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        List<String> workcellIds = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(workcellIds)){
            Date nowDate = new Date();
            Date shiftActualEndTime = dto.getShiftActualEndTime() == null ? nowDate:dto.getShiftActualEndTime();
            Date shiftActualStartTime = dto.getShiftActualStartTime();
            resultPage = PageHelper.doPageAndSort(pageRequest, () -> hmeOpenEndShiftMapper.otherExceptionQuery(tenantId, shiftActualStartTime, shiftActualEndTime, workcellIds));
            List<MtGenType> mtGenTypes = new ArrayList<>();
            for (HmeShiftVO11 hmeShiftVO11:resultPage) {
                String exceptionType = hmeShiftVO11.getExceptionType();
                mtGenTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, new MtGenTypeVO2() {{
                    setModule("HME");
                    setTypeGroup("EXCEPTION_TYPE");
                    setTypeCode(exceptionType);
                }});
                if(CollectionUtils.isNotEmpty(mtGenTypes)){
                    hmeShiftVO11.setExceptionTypeMeaning(mtGenTypes.get(0).getDescription());
                }
            }
        }
        return resultPage;
    }
}
