package com.ruike.hme.app.service.impl;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeEmployeeAttendanceExportService;
import com.ruike.hme.domain.entity.HmeSignInOutRecord;
import com.ruike.hme.domain.repository.HmeSignInOutRecordRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import com.ruike.hme.infra.mapper.HmeNcCheckMapper;
import com.ruike.hme.infra.mapper.HmeOpenEndShiftMapper;
import com.ruike.hme.infra.mapper.HmeSignInOutRecordMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.PageUtil;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tarzan.actual.domain.entity.MtNcRecord;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.calendar.domain.repository.MtCalendarRepository;
import tarzan.calendar.domain.vo.MtCalendarVO2;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.method.domain.entity.MtNcCode;
import tarzan.method.domain.repository.MtNcCodeRepository;
import tarzan.modeling.domain.entity.*;
import tarzan.modeling.domain.repository.*;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.DATE_FORMAT;

@Service
public class HmeEmployeeAttendanceExportServiceImpl implements HmeEmployeeAttendanceExportService {
    @Autowired
    private HmeSignInOutRecordRepository hmeSignInOutRecordRepository;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtModAreaRepository mtModAreaRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtCalendarRepository calendarRepository;
    @Autowired
    private HmeSignInOutRecordMapper hmeSignInOutRecordMapper;
    @Autowired
    private HmeOpenEndShiftMapper hmeOpenEndShiftMapper;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtUserRepository mtUserRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private HmeNcCheckMapper hmeNcCheckMapper;
    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    public static final Long HOUR = 3600000L;
    public static final Long RECOUND = 60000L;
    public static final Long MISS = 1000L;
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    public static DateTimeFormatter dteTime = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static SimpleDateFormat returnformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Page<HmeEmployeeAttendanceDto> headDataQuery(Long tenantId, HmeEmployeeAttendanceDto1 dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "站点"));
        }
        if (dto.getStartTime() == null) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "起始日期"));
        }
        if (dto.getEndTime() == null) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "截止日期"));
        }
        if(StringUtils.isNotEmpty(dto.getUserId())){
            List<String> userIdList = Arrays.asList(dto.getUserId().split(","));
            dto.setUserIdList(userIdList);
        }
        if(StringUtils.isNotEmpty(dto.getProductCodeId())){
            List<String> productCodeIdList = Arrays.asList(dto.getProductCodeId().split(","));
            dto.setProductCodeIdList(productCodeIdList);
        }
        if(StringUtils.isNotEmpty(dto.getBomVersion())){
            List<String> bomVersionList = Arrays.asList(dto.getBomVersion().split(","));
            dto.setBomVersionList(bomVersionList);
        }
        List<String> workcellIdList = new ArrayList<>();
        List<String> workcellIds = new ArrayList();
        //当未选择产线和工段时，则查询车间下的所有工段
        if (StringUtils.isEmpty(dto.getProductionLineId()) && StringUtils.isEmpty(dto.getLineWorkcellId())) {
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
            mtModOrganizationVO2.setParentOrganizationType("AREA");
            mtModOrganizationVO2.setParentOrganizationId(dto.getWorkshopId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("ALL");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            workcellIdList = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
        }
        //当选择了产线，未选择工段时，则查询产线下的工段
        if (StringUtils.isNotEmpty(dto.getProductionLineId()) && StringUtils.isEmpty(dto.getLineWorkcellId())) {
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
            mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
            mtModOrganizationVO2.setParentOrganizationId(dto.getProductionLineId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("ALL");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            workcellIdList = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
        }
        //当选择了工段后，则只查询该工段下数据
        if (StringUtils.isNotEmpty(dto.getLineWorkcellId())) {
            List<String> lineWorkcellIdList = Arrays.asList(StringUtils.split(dto.getLineWorkcellId(), ","));
            workcellIds.addAll(lineWorkcellIdList);
        }
        //工段筛选
        for (String workcellId : workcellIdList) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(workcellId);
            if ("LINE".equals(mtModWorkcell.getWorkcellType())) {
                workcellIds.add(workcellId);
            }
        }
        Date nowDate = new Date();
        //头数据查询的最小维度就是工段+班次，这里先确认好要分页展示的最小维度，其他数据的显示再根据最小维护去查询
        Page<HmeEmployeeAttendanceDto> result = PageHelper.doPageAndSort(pageRequest,
                () -> hmeSignInOutRecordMapper.headDataQuery2(tenantId, dto, workcellIds));
        for (HmeEmployeeAttendanceDto hmeEmployeeAttendanceDto : result) {
            //查询工段下的所有工位
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, new MtModOrganizationVO2() {{
                setTopSiteId(dto.getSiteId());
                setParentOrganizationType("WORKCELL");
                setParentOrganizationId(hmeEmployeeAttendanceDto.getWorkId());
                setOrganizationType("WORKCELL");
                setQueryType("BOTTOM");
            }});
            List<String> workcellIdList2 = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
            //生产线  调用API{parentOrganizationRelQuery}
            mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, new MtModOrganizationVO2() {{
                setTopSiteId(dto.getSiteId());
                setOrganizationType("WORKCELL");
                setOrganizationId(hmeEmployeeAttendanceDto.getWorkId());
                setParentOrganizationType("PROD_LINE");
                setQueryType("TOP");
            }});
            if (CollectionUtils.isNotEmpty(mtModOrganizationItemVOS)) {
                MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(mtModOrganizationItemVOS.get(0).getOrganizationId());
                if (mtModProductionLine != null) {
                    hmeEmployeeAttendanceDto.setProdLineName(mtModProductionLine.getProdLineName());
                }
            }
            if (hmeEmployeeAttendanceDto.getUnitId() != null) {
                //班组
                HmeSignInOutRecordDTO2 hmeSignInOutRecordDTO2 = hmeSignInOutRecordRepository.getUnitById(tenantId, hmeEmployeeAttendanceDto.getUnitId());
                if (hmeSignInOutRecordDTO2 != null) {
                    hmeEmployeeAttendanceDto.setUnitName(hmeSignInOutRecordDTO2.getUnitName());
                }
                //标准人数
                Integer employNumber = hmeSignInOutRecordRepository.findEmployNumberCount(tenantId, hmeEmployeeAttendanceDto.getUnitId());
                hmeEmployeeAttendanceDto.setEmployNumber(employNumber);
                //出勤数
                String shiftDateStr = DateUtil.format(hmeEmployeeAttendanceDto.getDate(), DATE_FORMAT);
                Long actualAttendance = hmeOpenEndShiftMapper.actualAttendanceQuery(tenantId, shiftDateStr,
                        hmeEmployeeAttendanceDto.getShiftCode(), String.valueOf(hmeEmployeeAttendanceDto.getUnitId()));
                hmeEmployeeAttendanceDto.setAttendanceNumber(actualAttendance.intValue());
                //缺勤数
                hmeEmployeeAttendanceDto.setNoWorkNumber(employNumber - actualAttendance.intValue());
                //标准总工时
                MtCalendarVO2 calendarVO = new MtCalendarVO2();
                calendarVO.setCalendarType(HmeConstants.ApiConstantValue.STANDARD);
                calendarVO.setSiteType(HmeConstants.ApiConstantValue.MANUFACTURING);
                calendarVO.setOrganizationType(HmeConstants.ApiConstantValue.WORKCELL);
                calendarVO.setOrganizationId(hmeEmployeeAttendanceDto.getWorkId());
                String calendarId = calendarRepository.organizationLimitOnlyCalendarGet(tenantId, calendarVO);
                if (StringUtils.isNotEmpty(calendarId)) {
                    List<HmeSignInOutRecordDTO7> mtCalendarShiftList = hmeSignInOutRecordMapper.findShiftSodeList2(tenantId, calendarId, shiftDateStr, hmeEmployeeAttendanceDto.getShiftCode());
                    if (CollectionUtils.isNotEmpty(mtCalendarShiftList)) {
                        Date startTime = mtCalendarShiftList.get(0).getShiftStartTime();
                        Date endTime = mtCalendarShiftList.get(0).getShiftEndTime();
                        if (mtCalendarShiftList.get(0).getRestTime() != null) {
                            Long restTime = mtCalendarShiftList.get(0).getRestTime().intValue() * HOUR;
                            Long time = (endTime.getTime() - startTime.getTime() - restTime) * employNumber;
                            hmeEmployeeAttendanceDto.setCountTime(time / HOUR);
                        } else {
                            Long time = (endTime.getTime() - startTime.getTime()) * employNumber;
                            hmeEmployeeAttendanceDto.setCountTime(time / HOUR);
                        }
                    }
                }
                //实际总工时
                Long countWorkTime = Long.valueOf(0);
                //查询工段下的所有工位下的relId集合,进而取到每个工位的实际工时求和
                for (String worlcellId : workcellIdList2) {
                    List<String> relIdList = hmeSignInOutRecordMapper.getRelId(tenantId, worlcellId, hmeEmployeeAttendanceDto.getUnitId(),
                            hmeEmployeeAttendanceDto.getShiftCode(), hmeEmployeeAttendanceDto.getDate());
                    if (CollectionUtils.isNotEmpty(relIdList)) {
                        for (String relId : relIdList) {
                            //查询relId下是否有OPERATION = CLOSE的数据，如果有，则累加duration
                            List<HmeSignInOutRecord> hmeSignInOutRecords = hmeSignInOutRecordRepository.select(new HmeSignInOutRecord() {{
                                setTenantId(tenantId);
                                setRelId(relId);
                                setOperation("CLOSE");
                            }});
                            if (CollectionUtils.isNotEmpty(hmeSignInOutRecords)) {
                                String duration = hmeSignInOutRecords.get(0).getDuration();
                                String[] hours = duration.split(":");
                                countWorkTime += Long.valueOf(hours[0]) * HOUR + Long.valueOf(hours[1]) * RECOUND + Long.valueOf(hours[2]) * MISS;
                            } else {
                                //如果没有，则根据relId找到OPERATION_DATE最大的那笔数据
                                HmeSignInOutRecord hmeSignInOutRecord = hmeSignInOutRecordMapper.maxOperationDateQuery(tenantId, relId);
                                if ("OPEN".equals(hmeSignInOutRecord.getOperation())) {
                                    //如果OPERATION = OPEN，则累加当前时间 - 该数据的DATE时间
                                    countWorkTime += (nowDate.getTime() - hmeSignInOutRecord.getDate().getTime());
                                } else if ("OFF".equals(hmeSignInOutRecord.getOperation())) {
                                    //如果OPERATION = OFF，则累加duration
                                    String duration = hmeSignInOutRecord.getDuration();
                                    String[] hours = duration.split(":");
                                    countWorkTime += Long.valueOf(hours[0]) * HOUR + Long.valueOf(hours[1]) * RECOUND + Long.valueOf(hours[2]) * MISS;
                                } else if ("ON".equals(hmeSignInOutRecord.getOperation())) {
                                    //如果OPERATION = ON，则累加duration + 当前时间 - 该数据的DATE时间
                                    Long time1 = nowDate.getTime() - hmeSignInOutRecord.getDate().getTime();
                                    String duration = hmeSignInOutRecord.getDuration();
                                    String[] hours = duration.split(":");
                                    Long time2 = Long.valueOf(hours[0]) * HOUR + Long.valueOf(hours[1]) * RECOUND + Long.valueOf(hours[2]) * MISS;
                                    countWorkTime += (time1 + time2);
                                }
                            }
                        }
                    }
                }
                hmeEmployeeAttendanceDto.setCountWorkTime(countWorkTime / HOUR);
                //偏差
                if (hmeEmployeeAttendanceDto.getCountTime() != null && hmeEmployeeAttendanceDto.getCountWorkTime() != null) {
                    hmeEmployeeAttendanceDto.setNoWorkTime(hmeEmployeeAttendanceDto.getCountTime() - hmeEmployeeAttendanceDto.getCountWorkTime());
                }
            }
//            Date shiftStartTime = hmeEmployeeAttendanceDto.getShiftStartDate();
//            Date shiftEndTime = hmeEmployeeAttendanceDto.getShiftEndDate() == null ? nowDate : hmeEmployeeAttendanceDto.getShiftEndDate();
            //2020-09-22 edit by chaonan.hu for fang.pan 修改总产量的取值逻辑
            //2021-03-11 edit by chaonan.hu for tianyang.xie 修改总产量的取值逻辑
            BigDecimal countNumber = hmeSignInOutRecordMapper.getCountNumberNew(tenantId, hmeEmployeeAttendanceDto.getWkcShiftId(),
                    hmeEmployeeAttendanceDto.getWorkId(), dto);
            hmeEmployeeAttendanceDto.setCountNumber(countNumber);
            //2021-03-11 add by chaonan.hu for tianyang.xie 增加实际产出的取值逻辑
            BigDecimal actualOutputNumber = hmeSignInOutRecordMapper.getActualOutputNumber(tenantId, hmeEmployeeAttendanceDto.getWkcShiftId(),
                    hmeEmployeeAttendanceDto.getWorkId(), dto);
            hmeEmployeeAttendanceDto.setActualOutputNumber(actualOutputNumber);
            // 总产量 查询工段下的末道工序的产量
//            List<MtModOrganizationRel> mtModOrganizationRelList = new ArrayList<>();
//            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
//            mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
//            mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
//            mtModOrganizationVO2.setParentOrganizationId(hmeEmployeeAttendanceDto.getWorkId());
//            mtModOrganizationVO2.setOrganizationType("WORKCELL");
//            mtModOrganizationVO2.setQueryType("ALL");
//            mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
//            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
//                String workcellId = mtModOrganizationItemVO.getOrganizationId();
//                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(workcellId);
//                if ("PROCESS".equals(mtModWorkcell.getWorkcellType())) {
//                    List<MtModOrganizationRel> mtModOrganizationRelS = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
//                        setTenantId(tenantId);
//                        setOrganizationType("WORKCELL");
//                        setOrganizationId(workcellId);
//                    }});
//                    if (CollectionUtils.isNotEmpty(mtModOrganizationRelS)) {
//                        mtModOrganizationRelList.addAll(mtModOrganizationRelS);
//                    }
//                }
//            }
////            List<String> firstWorkcellIdList = new ArrayList<>();
//            List<String> endWorkcellIdList = new ArrayList<>();
//            if (CollectionUtils.isNotEmpty(mtModOrganizationRelList)) {
//                mtModOrganizationRelList.removeIf(c -> c.getSequence() == null);
//                mtModOrganizationRelList = mtModOrganizationRelList.stream().sorted(Comparator.comparing(MtModOrganizationRel::getSequence)).collect(Collectors.toList());
//                //查询首道工序下的工位
////                mtModOrganizationVO2.setParentOrganizationId(mtModOrganizationRelList.get(0).getOrganizationId());
////                mtModOrganizationVO2.setQueryType("BOTTOM");
////                List<MtModOrganizationItemVO> mtModOrganizationItemVOS1 = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
////                for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS1) {
////                    firstWorkcellIdList.add(mtModOrganizationItemVO.getOrganizationId());
////                }
//                //查询末道工序下的工位
//                mtModOrganizationVO2.setParentOrganizationId(mtModOrganizationRelList.get((mtModOrganizationRelList.size() - 1)).getOrganizationId());
//                List<MtModOrganizationItemVO> mtModOrganizationItemVOS2 = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
//                for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS2) {
//                    endWorkcellIdList.add(mtModOrganizationItemVO.getOrganizationId());
//                }
//            }
//            BigDecimal countNumber = BigDecimal.ZERO;
//            if (CollectionUtils.isNotEmpty(endWorkcellIdList)) {
//                List<String> materialLotIdList = hmeSignInOutRecordMapper.getMaterialLotId4(tenantId, endWorkcellIdList, shiftStartTime, shiftEndTime);
//                materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
//                for (String materialLotId : materialLotIdList) {
//                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
//                    countNumber = countNumber.add(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
//                }
//            }
//            hmeEmployeeAttendanceDto.setCountNumber(countNumber);
            //2021-03-11 edit by chaonan.hu for tianyang.xie 修改不良数的取值逻辑
            BigDecimal defectsNumber = hmeSignInOutRecordMapper.defectNumberNew(tenantId, hmeEmployeeAttendanceDto.getWkcShiftId(),
                    hmeEmployeeAttendanceDto.getWorkId(), dto, hmeEmployeeAttendanceDto.getShiftStartDate(), hmeEmployeeAttendanceDto.getShiftEndDate());
            hmeEmployeeAttendanceDto.setDefectsNumber(defectsNumber);
//            Integer defectsNumber = Integer.valueOf(0);
//            MtMaterialLot mtMaterialLot = null;
//            if (CollectionUtils.isNotEmpty(workcellIdList2)) {
//                List<String> materialLotIdList = hmeSignInOutRecordMapper.getMaterialLot(tenantId, workcellIdList2, shiftStartTime, shiftEndTime);
//                for (String materialLotId : materialLotIdList) {
//                    mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
//                    defectsNumber = defectsNumber + mtMaterialLot.getPrimaryUomQty().intValue();
//                }
//                List<HmeShiftVO5> hmeShiftVO5List = hmeOpenEndShiftMapper.eoJobSnDataQuery(tenantId, shiftStartTime, shiftEndTime, workcellIdList3);
//                for (HmeShiftVO5 hmeShiftVO5:hmeShiftVO5List) {
//                    //查询WO下的EO
//                    List<MtEo> mtEoList = mtEoRepository.select(new MtEo() {{
//                        setTenantId(tenantId);
//                        setWorkOrderId(hmeShiftVO5.getWorkOrderId());
//                    }});
//                    if (CollectionUtils.isNotEmpty(mtEoList) && CollectionUtils.isNotEmpty(workcellIdList2)) {
//                        List<String> eoIds = mtEoList.stream().map(MtEo::getEoId).collect(Collectors.toList());
//                        List<String> materialLotIdList = hmeOpenEndShiftMapper.getMaterialLotId5(tenantId, shiftStartTime, shiftEndTime, workcellIdList2, eoIds);
//                        materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
//                        for (String materialLotId : materialLotIdList) {
//                            mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
//                            defectsNumber = defectsNumber + mtMaterialLot.getPrimaryUomQty().intValue();
//                        }
//                    }
//                }
//            }

//            for (String workcellId : endWorkcellIdList) {
//                List<String> materialLotIdList = hmeSignInOutRecordMapper.getMaterialLotId(tenantId, shiftStartTime, shiftEndTime, workcellId);
//                materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
//                for (String materialLotId : materialLotIdList) {
//                    mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
//                    defectsNumber += mtMaterialLot.getPrimaryUomQty().intValue();
//                }
//            }
            //班长
            List<String> groupLeaderList = new ArrayList<>();
            if (hmeEmployeeAttendanceDto.getUnitId() != null) {
                //根据部门查询岗位
                String positionId = null;
                ResponseEntity<List<HmeShiftVO2>> positionResponse = hmeHzeroPlatformFeignClient.queryPositionByUnit(tenantId,
                        String.valueOf(hmeEmployeeAttendanceDto.getUnitId()));
                if (CollectionUtils.isNotEmpty(positionResponse.getBody())) {
                    for (HmeShiftVO2 position : positionResponse.getBody()) {
                        if ("1".equals(position.getSupervisorFlag())) {
                            positionId = position.getPositionId();
                        }
                    }
                }
                if (StringUtils.isNotEmpty(positionId)) {
                    //根据岗位查询员工
                    ResponseEntity<String> headers = hmeHzeroPlatformFeignClient.queryEmployeeIdByPositionId(tenantId,
                            String.valueOf(hmeEmployeeAttendanceDto.getUnitId()), positionId, "0", "999999");
                    Page<Map<String, Object>> page = new Page<Map<String, Object>>();
                    try {
                        page = objectMapper.readValue(headers.getBody(), page.getClass());
                    } catch (IOException e) {

                    }
                    if (CollectionUtils.isNotEmpty(page.getContent())) {
                        List<Map<String, Object>> content = page.getContent();
                        for (Map<String, Object> employeeMap : content) {
                            String employeeId = String.valueOf(employeeMap.get("employeeId"));
                            //根据员工查询姓名
                            ResponseEntity<HmeShiftVO3> employee = hmeHzeroPlatformFeignClient.queryEmployeeNameById(tenantId, employeeId);
                            if (employee.getBody() != null) {
                                groupLeaderList.add(employee.getBody().getName());
                            }
                        }
                    }
                }
            }
            hmeEmployeeAttendanceDto.setGroupLeaderList(groupLeaderList);
        }
        return result;
    }

    @Override
    public Page<HmeEmployeeAttendanceRecordDto> lineDataQuery(Long tenantId, HmeEmployeeAttendanceDto5 dto, PageRequest pageRequest) {
        Page<HmeEmployeeAttendanceRecordDto> result = new Page<>();
        List<HmeEmployeeAttendanceRecordDto> resultList = new ArrayList<>();
        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String shiftDateStr = simpleDateFormat.format(dto.getShiftStartDate());
        if(StringUtils.isNotEmpty(dto.getUserId())){
            List<String> userIdList = Arrays.asList(dto.getUserId().split(","));
            dto.setUserIdList(userIdList);
        }
        if(StringUtils.isNotEmpty(dto.getProductCodeId())){
            List<String> productCodeIdList = Arrays.asList(dto.getProductCodeId().split(","));
            dto.setProductCodeIdList(productCodeIdList);
        }
        if(StringUtils.isNotEmpty(dto.getBomVersion())){
            List<String> bomVersionList = Arrays.asList(dto.getBomVersion().split(","));
            dto.setBomVersionList(bomVersionList);
        }
//        //查询工段下的末道工序
//        List<MtModOrganizationRel> mtModOrganizationRelList = new ArrayList<>();
//        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
//        mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
//        mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
//        mtModOrganizationVO2.setParentOrganizationId(dto.getWorkId());
//        mtModOrganizationVO2.setOrganizationType("WORKCELL");
//        mtModOrganizationVO2.setQueryType("ALL");
//        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
//        for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
//            String workcellId = mtModOrganizationItemVO.getOrganizationId();
//            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(workcellId);
//            if ("PROCESS".equals(mtModWorkcell.getWorkcellType())) {
//                List<MtModOrganizationRel> mtModOrganizationRelS = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
//                    setTenantId(tenantId);
//                    setOrganizationType("WORKCELL");
//                    setOrganizationId(workcellId);
//                }});
//                if (CollectionUtils.isNotEmpty(mtModOrganizationRelS)) {
//                    mtModOrganizationRelList.addAll(mtModOrganizationRelS);
//                }
//            }
//        }
//        List<String> endWorkcellIdList = new ArrayList<>();
//        if (CollectionUtils.isNotEmpty(mtModOrganizationRelList)) {
//            mtModOrganizationRelList.removeIf(c -> c.getSequence() == null);
//            mtModOrganizationRelList = mtModOrganizationRelList.stream().sorted(Comparator.comparing(MtModOrganizationRel::getSequence)).collect(Collectors.toList());
//
//            //查询末道工序下的工位
//            mtModOrganizationVO2.setParentOrganizationId(mtModOrganizationRelList.get((mtModOrganizationRelList.size() - 1)).getOrganizationId());
//            List<MtModOrganizationItemVO> mtModOrganizationItemVOS2 = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
//            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS2) {
//                endWorkcellIdList.add(mtModOrganizationItemVO.getOrganizationId());
//            }
//            //查询末道工序名称
//            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(mtModOrganizationVO2.getParentOrganizationId());
//            workName = mtModWorkcell.getWorkcellName();
//        }
        //查询工段下的所有工位
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, new MtModOrganizationVO2() {{
            setTopSiteId(dto.getSiteId());
            setParentOrganizationType("WORKCELL");
            setParentOrganizationId(dto.getWorkId());
            setOrganizationType("WORKCELL");
            setQueryType("BOTTOM");
        }});
        List<String> workcellIdList2 = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
        List<String> finalWorkcellIdList = new ArrayList<>();
        //2020-11-18 add by chaonan.hu for can.wang 行上查询条件增加工位
        List<String> queryWorkcellIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(dto.getWorkcellName())){
            queryWorkcellIdList = hmeSignInOutRecordMapper.workcellNameLikeQuery(tenantId, dto.getWorkcellName());
            if(CollectionUtils.isNotEmpty(queryWorkcellIdList)){
                for (String workcellId:queryWorkcellIdList) {
                    if(workcellIdList2.contains(workcellId)){
                        finalWorkcellIdList.add(workcellId);
                    }
                }
                if(CollectionUtils.isEmpty(finalWorkcellIdList)){
                    throw new MtException("HME_EO_JOB_SN_124", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_124", "HME"));
                }
            }else{
                throw new MtException("HME_EO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_001", "HME"));
            }
        }else{
            finalWorkcellIdList = workcellIdList2;
        }
        if (CollectionUtils.isNotEmpty(finalWorkcellIdList)) {
            List<String> workcellList = new ArrayList<>();
            workcellList.addAll(finalWorkcellIdList);
//            Date shiftEndDate = dto.getShiftEndDate() == null ? nowDate : dto.getShiftEndDate();
//            //行数据展示的最小维度是工位+员工，这里也是先确认好展示的最下维度，其他数据再根据最小维度去查询
//            resultList = hmeSignInOutRecordMapper.shiftDataQuery(tenantId, finalWorkcellIdList, dto.getWkcShiftId(), dto.getShiftStartDate(), shiftEndDate, dto.getSiteId(), dto.getWorkId());
            //2021-03-12 edit by chaonan.hu for tianyang.xie 行数据展示的最小维度更改为是工位+员工+物料
            result = PageHelper.doPage(pageRequest, ()->hmeSignInOutRecordMapper.shiftDataQueryNew(tenantId, workcellList, dto.getWkcShiftId(), dto));
            if (CollectionUtils.isEmpty(result)) {
                return result;
//                List<HmeEmployeeAttendanceRecordDto> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
//                result = new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), resultList.size());
            } else {
                //查询结果根据工位+员工去重, 根据工序的组织顺序排序
//                resultList = resultList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
//                        -> new TreeSet<>(Comparator.comparing(o -> o.getWorkcellId() + ";" + o.getEmployeeId()))), ArrayList::new));
//                resultList = resultList.stream().sorted(Comparator.comparing(HmeEmployeeAttendanceRecordDto::getOrderBy)).collect(Collectors.toList());
//                List<HmeEmployeeAttendanceRecordDto> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
//                result = new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), resultList.size());
                MtModWorkcell work = mtModWorkcellRepository.selectByPrimaryKey(dto.getWorkId());
                for (HmeEmployeeAttendanceRecordDto employeeAttendanceRecordDto : result) {
                    employeeAttendanceRecordDto.setWorkName(work.getWorkcellName());
                    //根据工位查询工序
                    MtModWorkcell process = hmeSignInOutRecordMapper.getProcessByWorkcell(tenantId, employeeAttendanceRecordDto.getWorkcellId());
                    if(Objects.nonNull(process)){
                        employeeAttendanceRecordDto.setWorkName(process.getWorkcellName());
                    }
                    //员工
                    MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.parseLong(employeeAttendanceRecordDto.getEmployeeId()));
                    employeeAttendanceRecordDto.setEmployName(mtUserInfo.getRealName());
                    //工号
                    employeeAttendanceRecordDto.setEmployeeNum(mtUserInfo.getLoginName());
                    //产量
                    BigDecimal makeNum = hmeSignInOutRecordMapper.lineMakeNumQuery(tenantId, dto.getWkcShiftId(), employeeAttendanceRecordDto, dto);
                    employeeAttendanceRecordDto.setMakeNum(makeNum);
                    List<String> jobIdList = hmeSignInOutRecordMapper.lineMakeNumDetailQuery(tenantId, dto.getWkcShiftId(), employeeAttendanceRecordDto, dto);
                    employeeAttendanceRecordDto.setJobIdList(jobIdList);
                    //在制
                    BigDecimal inMakeNum = hmeSignInOutRecordMapper.inMakeNumQuery(tenantId, dto.getWkcShiftId(), employeeAttendanceRecordDto, dto);
                    employeeAttendanceRecordDto.setInMakeNum(inMakeNum);
                    List<String> inMakeJobIdList = hmeSignInOutRecordMapper.inMakeNumDetailQuery(tenantId, dto.getWkcShiftId(), employeeAttendanceRecordDto, dto);
                    employeeAttendanceRecordDto.setInMakeJobIdList(inMakeJobIdList);
                    //不良数
                    List<String> defectsNumbList = hmeSignInOutRecordMapper.defectsNumbQuery(tenantId, dto.getWkcShiftId(), employeeAttendanceRecordDto, dto);
                    employeeAttendanceRecordDto.setDefectsNumb(new BigDecimal(defectsNumbList.size()));
                    employeeAttendanceRecordDto.setNcRecordIdList(defectsNumbList);
                    //返修
                    BigDecimal repairNum = hmeSignInOutRecordMapper.repairNumQuery(tenantId, dto.getWkcShiftId(), employeeAttendanceRecordDto, dto);
                    employeeAttendanceRecordDto.setRepairNum(repairNum);
                    List<String> repairNumjobIdList = hmeSignInOutRecordMapper.repairNumDetailQuery(tenantId, dto.getWkcShiftId(), employeeAttendanceRecordDto, dto);
                    employeeAttendanceRecordDto.setRepairJobIdList(repairNumjobIdList);
                    //合格率
                    BigDecimal eoWorkcellNum = hmeSignInOutRecordMapper.eoWorkcellGroupQuery(tenantId, dto.getWkcShiftId(), employeeAttendanceRecordDto, dto);
                    if (eoWorkcellNum.compareTo(BigDecimal.ZERO) == 0) {
                        employeeAttendanceRecordDto.setFirstPassRate("--");
                    } else {
                        List<String> reworkFlagNEoList = hmeSignInOutRecordMapper.eoWorkcellReworkFlagNGroupQuery(tenantId, dto.getWkcShiftId(), employeeAttendanceRecordDto, dto);
                        List<String> reworkFlagYEoList = hmeSignInOutRecordMapper.eoWorkcellReworkFlagYGroupQuery(tenantId, dto.getWkcShiftId(), employeeAttendanceRecordDto, dto);
                        BigDecimal eoWorkcellReworkFlagNum = BigDecimal.ZERO;
                        if(CollectionUtils.isEmpty(reworkFlagNEoList)){
                            eoWorkcellReworkFlagNum = BigDecimal.ZERO;
                        }else{
                            reworkFlagNEoList = reworkFlagNEoList.stream().distinct().collect(Collectors.toList());
                        }
                        if(CollectionUtils.isEmpty(reworkFlagYEoList)){
                            eoWorkcellReworkFlagNum = new BigDecimal(reworkFlagNEoList.size());
                        }else{
                            reworkFlagYEoList = reworkFlagYEoList.stream().distinct().collect(Collectors.toList());
                        }
                        if(CollectionUtils.isNotEmpty(reworkFlagNEoList) && CollectionUtils.isNotEmpty(reworkFlagYEoList)){
                            reworkFlagNEoList.removeAll(reworkFlagYEoList);
                            eoWorkcellReworkFlagNum = new BigDecimal(reworkFlagNEoList.size());
                        }
                        BigDecimal passPercentB = (eoWorkcellReworkFlagNum.divide(eoWorkcellNum, 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                        employeeAttendanceRecordDto.setFirstPassRate(passPercentB.toString() + "%");
                    }
                    if (StringUtils.isNotEmpty(dto.getUnitId())) {
                        //计划上下岗时间
                        Date shiftStartTime = null;
                        Date shiftEndTime = null;
                        MtCalendarVO2 calendarVO = new MtCalendarVO2();
                        calendarVO.setCalendarType(HmeConstants.ApiConstantValue.STANDARD);
                        calendarVO.setSiteType(HmeConstants.ApiConstantValue.MANUFACTURING);
                        calendarVO.setOrganizationType(HmeConstants.ApiConstantValue.WORKCELL);
                        calendarVO.setOrganizationId(dto.getWorkId());
                        String calendarId = calendarRepository.organizationLimitOnlyCalendarGet(tenantId, calendarVO);
                        if (StringUtils.isNotEmpty(calendarId)) {
                            List<HmeSignInOutRecordDTO7> mtCalendarShiftList = hmeSignInOutRecordMapper.findShiftSodeList2(tenantId, calendarId, shiftDateStr, dto.getShiftCode());
                            if (CollectionUtils.isNotEmpty(mtCalendarShiftList)) {
                                shiftStartTime = mtCalendarShiftList.get(0).getShiftStartTime();
                                shiftEndTime = mtCalendarShiftList.get(0).getShiftEndTime();
                            }
                        }
                        //上岗时间
                        Date mountGuardDate = hmeOpenEndShiftMapper.mountLaidDateQuery(tenantId, employeeAttendanceRecordDto.getEmployeeId(),
                                dto.getUnitId(), employeeAttendanceRecordDto.getWorkcellId(),
                                dto.getShiftCode(), shiftDateStr, "OPEN");
                        employeeAttendanceRecordDto.setEndOperationDate(mountGuardDate);
                        //偏差
                        if (mountGuardDate != null && shiftStartTime != null) {
                            employeeAttendanceRecordDto.setShiftStartTime(timeDifference(mountGuardDate, shiftStartTime));
                        }
                        //下岗时间
                        Date laidOffDate = hmeOpenEndShiftMapper.mountLaidDateQuery(tenantId, employeeAttendanceRecordDto.getEmployeeId(),
                                dto.getUnitId(), employeeAttendanceRecordDto.getWorkcellId(),
                                dto.getShiftCode(), shiftDateStr, "CLOSE");
                        employeeAttendanceRecordDto.setEndOperationDate(laidOffDate);
                        //偏差
                        if (laidOffDate != null && shiftEndTime != null) {
                            employeeAttendanceRecordDto.setShiftEndTime(timeDifference(laidOffDate, shiftEndTime));
                        }
                        //总时长
                        if (mountGuardDate != null && laidOffDate != null) {
                            employeeAttendanceRecordDto.setCountDate((laidOffDate.getTime() - mountGuardDate.getTime() / HOUR) + "h");
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Page<HmeEmployeeAttendanceDto8> prodLineLovQuery(Long tenantId, HmeEmployeeAttendanceDto7 dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "站点"));
        }
        if (StringUtils.isEmpty(dto.getWorkshopId())) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "车间"));
        }
        Page<HmeEmployeeAttendanceDto8> resultPage = new Page<>();
        List<HmeEmployeeAttendanceDto8> resultList = new ArrayList<>();
        //调用API{subOrganizationRelQuery} 根据车间查询产线
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
        mtModOrganizationVO2.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.AREA);
        mtModOrganizationVO2.setParentOrganizationId(dto.getWorkshopId());
        mtModOrganizationVO2.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);
        mtModOrganizationVO2.setQueryType("TOP");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        HmeEmployeeAttendanceDto8 hmeEmployeeAttendanceDto8 = null;
        for (MtModOrganizationItemVO vo : mtModOrganizationItemVOS) {
            boolean flag = true;
            hmeEmployeeAttendanceDto8 = new HmeEmployeeAttendanceDto8();
            hmeEmployeeAttendanceDto8.setProdLineId(vo.getOrganizationId());
            MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(vo.getOrganizationId());
            hmeEmployeeAttendanceDto8.setProdLineCode(mtModProductionLine.getProdLineCode());
            hmeEmployeeAttendanceDto8.setProdLineName(mtModProductionLine.getProdLineName());
            if (StringUtils.isNotEmpty(dto.getProdLineCode()) && !hmeEmployeeAttendanceDto8.getProdLineCode().contains(dto.getProdLineCode())) {
                flag = false;
            }
            if (StringUtils.isNotEmpty(dto.getProdLineName()) && !hmeEmployeeAttendanceDto8.getProdLineName().contains(dto.getProdLineName())) {
                flag = false;
            }
            if (flag) {
                resultList.add(hmeEmployeeAttendanceDto8);
            }
        }
        if (CollectionUtils.isNotEmpty(resultList)) {
            List<HmeEmployeeAttendanceDto8> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
            resultPage = new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), resultList.size());
        }
        return resultPage;
    }

    @Override
    public Page<HmeEmployeeAttendanceDto10> processLovQuery(Long tenantId, HmeEmployeeAttendanceDto9 dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "站点"));
        }
        if (StringUtils.isEmpty(dto.getProdLineId())) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "产线"));
        }
        Page<HmeEmployeeAttendanceDto10> resultPage = new Page<>();
        List<HmeEmployeeAttendanceDto10> resultList = new ArrayList<>();
        //调用API {subOrganizationRelQuery}根据产线找工段
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
        mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
        mtModOrganizationVO2.setParentOrganizationId(dto.getProdLineId());
        mtModOrganizationVO2.setOrganizationType("WORKCELL");
        mtModOrganizationVO2.setQueryType("TOP");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        HmeEmployeeAttendanceDto10 hmeEmployeeAttendanceDto10 = null;
        for (MtModOrganizationItemVO vo : mtModOrganizationItemVOS) {
            boolean flag = true;
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(vo.getOrganizationId());
            hmeEmployeeAttendanceDto10 = new HmeEmployeeAttendanceDto10();
            hmeEmployeeAttendanceDto10.setProcessId(vo.getOrganizationId());
            hmeEmployeeAttendanceDto10.setProcessCode(mtModWorkcell.getWorkcellCode());
            hmeEmployeeAttendanceDto10.setProcessName(mtModWorkcell.getWorkcellName());
            if (StringUtils.isNotEmpty(dto.getLineWorkcellCode()) && !mtModWorkcell.getWorkcellCode().contains(dto.getLineWorkcellCode())) {
                flag = false;
            }
            if (StringUtils.isNotEmpty(dto.getLineWorkcellName()) && !mtModWorkcell.getWorkcellName().contains(dto.getLineWorkcellName())) {
                flag = false;
            }
            if (flag) {
                resultList.add(hmeEmployeeAttendanceDto10);
            }
        }
        if (CollectionUtils.isNotEmpty(resultList)) {
            List<HmeEmployeeAttendanceDto10> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
            resultPage = new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), resultList.size());
        }
        return resultPage;
    }

    @Override
    public Page<HmeEmployeeAttendanceExportVO3> jobDetailInfoQuery(Long tenantId, List<String> jobIdList, PageRequest pageRequest) {
        if(CollectionUtils.isEmpty(jobIdList)){
            return new Page<>();
        }
        List<String> jobIds = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), jobIdList);
        List<HmeEmployeeAttendanceExportVO3> resultList = hmeSignInOutRecordMapper.jobDetailInfoQuery(jobIds);
        for (HmeEmployeeAttendanceExportVO3 vo:resultList) {
            if(vo.getSiteInDate() != null &&vo.getSiteOutDate() != null){
                //加工时长
                if(vo.getSiteInDate() != null && vo.getSiteOutDate() != null){
                    long time = vo.getSiteOutDate().getTime() - vo.getSiteInDate().getTime();
                    long min = 1000*60;
                    BigDecimal processTime = BigDecimal.valueOf(time).divide(BigDecimal.valueOf(min), 2, BigDecimal.ROUND_HALF_UP);
                    vo.setProcessTime(processTime);
                }
            }
        }
        return new Page<>(resultList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), jobIdList.size());
    }

    @Override
    public Page<HmeEmployeeAttendanceExportVO4> ncRecordInfoQuery(Long tenantId, List<String> ncRecordIdList, PageRequest pageRequest) {
        if(CollectionUtils.isEmpty(ncRecordIdList)){
            return new Page<>();
        }
        List<String> ncRecords = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), ncRecordIdList);
        List<HmeEmployeeAttendanceExportVO4> result = hmeSignInOutRecordMapper.ncRecordInfoQuery(ncRecords);
        for (HmeEmployeeAttendanceExportVO4 vo:result) {
            //提交人
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.parseLong(vo.getUserId()));
            vo.setUserName(mtUserInfo.getRealName());
            //不良代码
            List<MtNcRecord> mtNcRecordList = hmeNcCheckMapper.childNcRecordQuery(tenantId, vo.getNcRecordId());
            List<String> ncCodeIdList = new ArrayList<>();
            List<String> ncDescriptionList = new ArrayList<>();
            for (MtNcRecord mtNcRecord : mtNcRecordList) {
                if (StringUtils.isNotEmpty(mtNcRecord.getNcCodeId())) {
                    //根据NC_CODE_ID关联表mt_nc_code查询nc_code
                    MtNcCode mtNcCode = mtNcCodeRepository.selectByPrimaryKey(mtNcRecord.getNcCodeId());
                    ncCodeIdList.add(mtNcCode.getNcCodeId());
                    ncDescriptionList.add(mtNcCode.getDescription());
                }
            }
            vo.setNcCodeIdList(ncCodeIdList);
            vo.setNcCodeDescriptionList(ncDescriptionList);
        }

        return new Page<>(result, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), ncRecordIdList.size());
    }

    @Override
    public Page<HmeEmployeeAttendanceExportVO5> sumQuery(Long tenantId, HmeEmployeeAttendanceDTO13 dto, PageRequest pageRequest) {
        if(StringUtils.isEmpty(dto.getProdLineId()) && StringUtils.isEmpty(dto.getLineWorkcellId())
            && StringUtils.isEmpty(dto.getProcessId()) && StringUtils.isEmpty(dto.getUserId()) && StringUtils.isEmpty(dto.getMaterialId())){
            throw new MtException("HME_PRO_REPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_004", "HME"));
        }
        if(Objects.isNull(dto.getDateFrom())){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "起始时间"));
        }
        if(Objects.isNull(dto.getDateTo())){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "截止时间"));
        }
        Page<HmeEmployeeAttendanceExportVO5> resultPage = new Page<>();
        if(StringUtils.isNotEmpty(dto.getUserId())){
            List<String> userIdList = Arrays.asList(dto.getUserId().split(","));
            dto.setUserIdList(userIdList);
        }
        if(StringUtils.isNotEmpty(dto.getMaterialId())){
            List<String> productCodeIdList = Arrays.asList(dto.getMaterialId().split(","));
            dto.setMaterialIdList(productCodeIdList);
        }
        if(StringUtils.isNotEmpty(dto.getMaterialVersion())){
            List<String> materialVersionList = Arrays.asList(dto.getMaterialVersion().split(","));
            dto.setMaterialVersionList(materialVersionList);
        }
        List<String> workcellIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(dto.getProcessId())){
            //如果选择工序，则取工序下所有工位
            workcellIdList = hmeSignInOutRecordMapper.getWorkcellByProcess(tenantId, dto.getProcessId());
        }
        if(StringUtils.isEmpty(dto.getProcessId()) && StringUtils.isNotEmpty(dto.getLineWorkcellId())){
            //如果未选工序，则取工段下所有工序下工位
            workcellIdList = hmeSignInOutRecordMapper.getWorkcellByLineWorkcellId(tenantId, dto.getLineWorkcellId());
        }
        if(StringUtils.isEmpty(dto.getProcessId()) && StringUtils.isEmpty(dto.getLineWorkcellId()) && StringUtils.isNotEmpty(dto.getProdLineId())){
            //如果未选工段，取产线下所有工段下所有工序下工位
            workcellIdList = hmeSignInOutRecordMapper.getWorkcellByProdLine(tenantId, dto.getProdLineId());
        }
        dto.setWorkcellIdList(workcellIdList);
        resultPage = PageHelper.doPage(pageRequest, ()->hmeSignInOutRecordMapper.sumQuery(tenantId, dto));
        for (HmeEmployeeAttendanceExportVO5 result:resultPage) {
            result.setDateFrom(dto.getDateFrom());
            result.setDateTo(dto.getDateTo());
            //员工姓名
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.parseLong(result.getUserId()));
            result.setUserName(mtUserInfo.getRealName());
            //工号
            result.setUserNum(mtUserInfo.getLoginName());
            //工段
            MtModWorkcell lineWorkcell = hmeSignInOutRecordMapper.getLineWorkcellByProcess(tenantId, result.getProcessId());
            if(Objects.nonNull(lineWorkcell)){
                result.setLineWorkcellId(lineWorkcell.getWorkcellId());
                result.setLineWorkcerllName(lineWorkcell.getWorkcellName());
                //产线
                MtModProductionLine mtModProductionLine = hmeSignInOutRecordMapper.getProdLineByLineWorkcell(tenantId, lineWorkcell.getWorkcellId());
                result.setProdLineId(mtModProductionLine.getProdLineId());
                result.setProdLineName(mtModProductionLine.getProdLineName());
            }
            //实际产出
            BigDecimal actualOutputNumber = BigDecimal.ZERO;
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setTableName("mt_mod_workcell_attr");
            mtExtendVO.setKeyId(result.getProcessId());
            mtExtendVO.setAttrName("OUTPUT_FLAG");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && "Y".equals(mtExtendAttrVOS.get(0).getAttrValue())) {
                actualOutputNumber = hmeSignInOutRecordMapper.getSumActualOutputNumber(tenantId, result);
            }
            result.setActualOutputNumber(actualOutputNumber);
            //产量
            BigDecimal countNumber = hmeSignInOutRecordMapper.getSumCountNumber(tenantId, result);
            result.setCountNumber(countNumber);
            //在制数
            BigDecimal inMakeNum = hmeSignInOutRecordMapper.getSumInMakeNum(tenantId, result);
            result.setInMakeNum(inMakeNum);
            //不良数
            BigDecimal defectsNumber = hmeSignInOutRecordMapper.getSumDefectsNumb(tenantId, result, dto.getDateFrom(), dto.getDateTo());
            result.setDefectsNumber(defectsNumber);
            //返修数
            BigDecimal repairNum = hmeSignInOutRecordMapper.getSumRepairNum(tenantId, result);
            result.setRepairNum(repairNum);
            //一次合格率
            BigDecimal eoWorkcellNum = hmeSignInOutRecordMapper.getSumEoWorkcellGroup(tenantId, result);
            if (eoWorkcellNum.compareTo(BigDecimal.ZERO) == 0) {
                result.setFirstPassRate("--");
            } else {
                List<String> reworkFlagNEoList = hmeSignInOutRecordMapper.sumEoWorkcellReworkFlagNGroupQuery(tenantId, result);
                List<String> reworkFlagYEoList = hmeSignInOutRecordMapper.sumEoWorkcellReworkFlagYGroupQuery(tenantId, result);
                BigDecimal eoWorkcellReworkFlagNum = BigDecimal.ZERO;
                if(CollectionUtils.isEmpty(reworkFlagNEoList)){
                    eoWorkcellReworkFlagNum = BigDecimal.ZERO;
                }else{
                    reworkFlagNEoList = reworkFlagNEoList.stream().distinct().collect(Collectors.toList());
                }
                if(CollectionUtils.isEmpty(reworkFlagYEoList)){
                    eoWorkcellReworkFlagNum = new BigDecimal(reworkFlagNEoList.size());
                }else{
                    reworkFlagYEoList = reworkFlagYEoList.stream().distinct().collect(Collectors.toList());
                }
                if(CollectionUtils.isNotEmpty(reworkFlagNEoList) && CollectionUtils.isNotEmpty(reworkFlagYEoList)){
                    reworkFlagNEoList.removeAll(reworkFlagYEoList);
                    eoWorkcellReworkFlagNum = new BigDecimal(reworkFlagNEoList.size());
                }
                BigDecimal passPercentB = (eoWorkcellReworkFlagNum.divide(eoWorkcellNum, 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                result.setFirstPassRate(passPercentB.toString() + "%");
                //生产总时长
                BigDecimal totalProductionTime = hmeSignInOutRecordMapper.getTotalProductionTime(tenantId, result);
                result.setTotalProductionTime(totalProductionTime.divide(BigDecimal.valueOf(3600), 2,BigDecimal.ROUND_HALF_UP) + "h");
            }
        }
        return resultPage;
    }

    @Override
    public Page<HmeEmployeeAttendanceExportVO3> sumNumberDeatilQuery(Long tenantId, HmeEmployeeAttendanceDTO14 dto, PageRequest pageRequest) {
        Page<HmeEmployeeAttendanceExportVO3> resultPage = new Page<>();
        if(StringUtils.isEmpty(dto.getMaterialVersion())){
            dto.setMaterialVersion("");
        }
        if("ACTUALOUTPUT".equals(dto.getType())){
            //实际产出
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setTableName("mt_mod_workcell_attr");
            mtExtendVO.setKeyId(dto.getProcessId());
            mtExtendVO.setAttrName("OUTPUT_FLAG");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && "Y".equals(mtExtendAttrVOS.get(0).getAttrValue())) {
                resultPage = PageHelper.doPage(pageRequest, ()->hmeSignInOutRecordMapper.sumActualOutputNumberQuery(tenantId, dto));
            }
        }else if("COUNTNUMBER".equals(dto.getType())){
            //产量
            resultPage = PageHelper.doPage(pageRequest, ()->hmeSignInOutRecordMapper.sumCountNumberQuery(tenantId, dto));
        }else if("INMAKENUM".equals(dto.getType())){
            //在制数
            resultPage = PageHelper.doPage(pageRequest, ()->hmeSignInOutRecordMapper.sumInMakeNumQuery(tenantId, dto));
        }else if("REPAIRNUM".equals(dto.getType())){
            //返修数
            resultPage = PageHelper.doPage(pageRequest, ()->hmeSignInOutRecordMapper.sumRepairNumQuery(tenantId, dto));
        }
        for (HmeEmployeeAttendanceExportVO3 vo:resultPage) {
            //加工时长
            if(vo.getSiteInDate() != null && vo.getSiteOutDate() != null){
                long time = vo.getSiteOutDate().getTime() - vo.getSiteInDate().getTime();
                long min = 1000*60;
                BigDecimal processTime = BigDecimal.valueOf(time).divide(BigDecimal.valueOf(min), 2, BigDecimal.ROUND_HALF_UP);
                vo.setProcessTime(processTime);
            }
        }
        return resultPage;
    }

    @Override
    public Page<HmeEmployeeAttendanceExportVO4> sumDefectsNumQuery(Long tenantId, HmeEmployeeAttendanceDTO14 dto, PageRequest pageRequest) {
        if(StringUtils.isEmpty(dto.getMaterialVersion())){
            dto.setMaterialVersion("");
        }
        Page<HmeEmployeeAttendanceExportVO4> resultPage = PageHelper.doPage(pageRequest, ()->hmeSignInOutRecordMapper.sumDefectsNumQuery(tenantId, dto));
        for (HmeEmployeeAttendanceExportVO4 vo:resultPage) {
            //提交人
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.parseLong(vo.getUserId()));
            vo.setUserName(mtUserInfo.getRealName());
            //不良代码
            List<MtNcRecord> mtNcRecordList = hmeNcCheckMapper.childNcRecordQuery(tenantId, vo.getNcRecordId());
            List<String> ncCodeIdList = new ArrayList<>();
            List<String> ncDescriptionList = new ArrayList<>();
            for (MtNcRecord mtNcRecord : mtNcRecordList) {
                if (StringUtils.isNotEmpty(mtNcRecord.getNcCodeId())) {
                    //根据NC_CODE_ID关联表mt_nc_code查询nc_code
                    MtNcCode mtNcCode = mtNcCodeRepository.selectByPrimaryKey(mtNcRecord.getNcCodeId());
                    ncCodeIdList.add(mtNcCode.getNcCodeId());
                    ncDescriptionList.add(mtNcCode.getDescription());
                }
            }
            vo.setNcCodeIdList(ncCodeIdList);
            vo.setNcCodeDescriptionList(ncDescriptionList);
        }
        return resultPage;
    }

    @Override
    public List<HmeEmployeeAttendanceExportVO5> sumExport(Long tenantId, HmeEmployeeAttendanceDTO13 dto) {
        List<HmeEmployeeAttendanceExportVO5> resultList = new ArrayList<>();
        if(StringUtils.isEmpty(dto.getProdLineId()) && StringUtils.isEmpty(dto.getLineWorkcellId())
                && StringUtils.isEmpty(dto.getProcessId()) && StringUtils.isEmpty(dto.getUserId()) && StringUtils.isEmpty(dto.getMaterialId())){
            return resultList;
        }
        if(Objects.isNull(dto.getDateFrom())){
            return resultList;
        }
        if(Objects.isNull(dto.getDateTo())){
            return resultList;
        }
        if(StringUtils.isNotEmpty(dto.getUserId())){
            List<String> userIdList = Arrays.asList(dto.getUserId().split(","));
            dto.setUserIdList(userIdList);
        }
        if(StringUtils.isNotEmpty(dto.getMaterialId())){
            List<String> productCodeIdList = Arrays.asList(dto.getMaterialId().split(","));
            dto.setMaterialIdList(productCodeIdList);
        }
        if(StringUtils.isNotEmpty(dto.getMaterialVersion())){
            List<String> materialVersionList = Arrays.asList(dto.getMaterialVersion().split(","));
            dto.setMaterialVersionList(materialVersionList);
        }
        List<String> workcellIdList = new ArrayList<>();
        if(StringUtils.isNotEmpty(dto.getProcessId())){
            //如果选择工序，则取工序下所有工位
            workcellIdList = hmeSignInOutRecordMapper.getWorkcellByProcess(tenantId, dto.getProcessId());
        }
        if(StringUtils.isEmpty(dto.getProcessId()) && StringUtils.isNotEmpty(dto.getLineWorkcellId())){
            //如果未选工序，则取工段下所有工序下工位
            workcellIdList = hmeSignInOutRecordMapper.getWorkcellByLineWorkcellId(tenantId, dto.getLineWorkcellId());
        }
        if(StringUtils.isEmpty(dto.getProcessId()) && StringUtils.isEmpty(dto.getLineWorkcellId()) && StringUtils.isNotEmpty(dto.getProdLineId())){
            //如果未选工段，取产线下所有工段下所有工序下工位
            workcellIdList = hmeSignInOutRecordMapper.getWorkcellByProdLine(tenantId, dto.getProdLineId());
        }
        dto.setWorkcellIdList(workcellIdList);
        resultList = hmeSignInOutRecordMapper.sumQuery(tenantId, dto);
        for (HmeEmployeeAttendanceExportVO5 result:resultList) {
            result.setDateFrom(dto.getDateFrom());
            result.setDateTo(dto.getDateTo());
            //员工姓名
            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.parseLong(result.getUserId()));
            result.setUserName(mtUserInfo.getRealName());
            //工号
            result.setUserNum(mtUserInfo.getLoginName());
            //工段
            MtModWorkcell lineWorkcell = hmeSignInOutRecordMapper.getLineWorkcellByProcess(tenantId, result.getProcessId());
            if(Objects.nonNull(lineWorkcell)){
                result.setLineWorkcellId(lineWorkcell.getWorkcellId());
                result.setLineWorkcerllName(lineWorkcell.getWorkcellName());
                //产线
                MtModProductionLine mtModProductionLine = hmeSignInOutRecordMapper.getProdLineByLineWorkcell(tenantId, lineWorkcell.getWorkcellId());
                result.setProdLineId(mtModProductionLine.getProdLineId());
                result.setProdLineName(mtModProductionLine.getProdLineName());
            }
            //实际产出
            BigDecimal actualOutputNumber = BigDecimal.ZERO;
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setTableName("mt_mod_workcell_attr");
            mtExtendVO.setKeyId(result.getProcessId());
            mtExtendVO.setAttrName("OUTPUT_FLAG");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && "Y".equals(mtExtendAttrVOS.get(0).getAttrValue())) {
                actualOutputNumber = hmeSignInOutRecordMapper.getSumActualOutputNumber(tenantId, result);
            }
            result.setActualOutputNumber(actualOutputNumber);
            //产量
            BigDecimal countNumber = hmeSignInOutRecordMapper.getSumCountNumber(tenantId, result);
            result.setCountNumber(countNumber);
            //在制数
            BigDecimal inMakeNum = hmeSignInOutRecordMapper.getSumInMakeNum(tenantId, result);
            result.setInMakeNum(inMakeNum);
            //不良数
            BigDecimal defectsNumber = hmeSignInOutRecordMapper.getSumDefectsNumb(tenantId, result, dto.getDateFrom(), dto.getDateTo());
            result.setDefectsNumber(defectsNumber);
            //返修数
            BigDecimal repairNum = hmeSignInOutRecordMapper.getSumRepairNum(tenantId, result);
            result.setRepairNum(repairNum);
            //一次合格率
            BigDecimal eoWorkcellNum = hmeSignInOutRecordMapper.getSumEoWorkcellGroup(tenantId, result);
            if (eoWorkcellNum.compareTo(BigDecimal.ZERO) == 0) {
                result.setFirstPassRate("--");
            } else {
                List<String> reworkFlagNEoList = hmeSignInOutRecordMapper.sumEoWorkcellReworkFlagNGroupQuery(tenantId, result);
                List<String> reworkFlagYEoList = hmeSignInOutRecordMapper.sumEoWorkcellReworkFlagYGroupQuery(tenantId, result);
                BigDecimal eoWorkcellReworkFlagNum = BigDecimal.ZERO;
                if(CollectionUtils.isEmpty(reworkFlagNEoList)){
                    eoWorkcellReworkFlagNum = BigDecimal.ZERO;
                }else{
                    reworkFlagNEoList = reworkFlagNEoList.stream().distinct().collect(Collectors.toList());
                }
                if(CollectionUtils.isEmpty(reworkFlagYEoList)){
                    eoWorkcellReworkFlagNum = new BigDecimal(reworkFlagNEoList.size());
                }else{
                    reworkFlagYEoList = reworkFlagYEoList.stream().distinct().collect(Collectors.toList());
                }
                if(CollectionUtils.isNotEmpty(reworkFlagNEoList) && CollectionUtils.isNotEmpty(reworkFlagYEoList)){
                    reworkFlagNEoList.removeAll(reworkFlagYEoList);
                    eoWorkcellReworkFlagNum = new BigDecimal(reworkFlagNEoList.size());
                }
                BigDecimal passPercentB = (eoWorkcellReworkFlagNum.divide(eoWorkcellNum, 2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
                result.setFirstPassRate(passPercentB.toString() + "%");
                //生产总时长
                BigDecimal totalProductionTime = hmeSignInOutRecordMapper.getTotalProductionTime(tenantId, result);
                result.setTotalProductionTime(totalProductionTime.divide(BigDecimal.valueOf(3600), 2,BigDecimal.ROUND_HALF_UP) + "h");
            }
        }
        return resultList;
    }

    @Override
    public List<HmeEmployeeAttendanceDto> lineWorkcellProductExport(Long tenantId, HmeEmployeeAttendanceDto1 dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "站点"));
        }
        if (dto.getStartTime() == null) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "起始日期"));
        }
        if (dto.getEndTime() == null) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "截止日期"));
        }
        if(StringUtils.isNotEmpty(dto.getUserId())){
            List<String> userIdList = Arrays.asList(dto.getUserId().split(","));
            dto.setUserIdList(userIdList);
        }
        if(StringUtils.isNotEmpty(dto.getProductCodeId())){
            List<String> productCodeIdList = Arrays.asList(dto.getProductCodeId().split(","));
            dto.setProductCodeIdList(productCodeIdList);
        }
        if(StringUtils.isNotEmpty(dto.getBomVersion())){
            List<String> bomVersionList = Arrays.asList(dto.getBomVersion().split(","));
            dto.setBomVersionList(bomVersionList);
        }
        List<String> workcellIdList = new ArrayList<>();
        List<String> workcellIds = new ArrayList();
        //当未选择产线和工段时，则查询车间下的所有工段
        if (StringUtils.isEmpty(dto.getProductionLineId()) && StringUtils.isEmpty(dto.getLineWorkcellId())) {
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
            mtModOrganizationVO2.setParentOrganizationType("AREA");
            mtModOrganizationVO2.setParentOrganizationId(dto.getWorkshopId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("ALL");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            workcellIdList = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
        }
        //当选择了产线，未选择工段时，则查询产线下的工段
        if (StringUtils.isNotEmpty(dto.getProductionLineId()) && StringUtils.isEmpty(dto.getLineWorkcellId())) {
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
            mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
            mtModOrganizationVO2.setParentOrganizationId(dto.getProductionLineId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("ALL");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            workcellIdList = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
        }
        //当选择了工段后，则只查询该工段下数据
        if (StringUtils.isNotEmpty(dto.getLineWorkcellId())) {
            List<String> lineWorkcellIdList = Arrays.asList(StringUtils.split(dto.getLineWorkcellId(), ","));
            workcellIds.addAll(lineWorkcellIdList);
        }
        //工段筛选
        for (String workcellId : workcellIdList) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(workcellId);
            if ("LINE".equals(mtModWorkcell.getWorkcellType())) {
                workcellIds.add(workcellId);
            }
        }
        Date nowDate = new Date();
        //头数据查询的最小维度就是工段+班次，这里先确认好要分页展示的最小维度，其他数据的显示再根据最小维护去查询
        List<HmeEmployeeAttendanceDto> result = hmeSignInOutRecordMapper.headDataQuery2(tenantId, dto, workcellIds);
        for (HmeEmployeeAttendanceDto hmeEmployeeAttendanceDto : result) {
            //查询工段下的所有工位
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, new MtModOrganizationVO2() {{
                setTopSiteId(dto.getSiteId());
                setParentOrganizationType("WORKCELL");
                setParentOrganizationId(hmeEmployeeAttendanceDto.getWorkId());
                setOrganizationType("WORKCELL");
                setQueryType("BOTTOM");
            }});
            List<String> workcellIdList2 = mtModOrganizationItemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList());
            //生产线  调用API{parentOrganizationRelQuery}
            mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, new MtModOrganizationVO2() {{
                setTopSiteId(dto.getSiteId());
                setOrganizationType("WORKCELL");
                setOrganizationId(hmeEmployeeAttendanceDto.getWorkId());
                setParentOrganizationType("PROD_LINE");
                setQueryType("TOP");
            }});
            if (CollectionUtils.isNotEmpty(mtModOrganizationItemVOS)) {
                MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(mtModOrganizationItemVOS.get(0).getOrganizationId());
                if (mtModProductionLine != null) {
                    hmeEmployeeAttendanceDto.setProdLineName(mtModProductionLine.getProdLineName());
                }
            }
            if (hmeEmployeeAttendanceDto.getUnitId() != null) {
                //班组
                HmeSignInOutRecordDTO2 hmeSignInOutRecordDTO2 = hmeSignInOutRecordRepository.getUnitById(tenantId, hmeEmployeeAttendanceDto.getUnitId());
                if (hmeSignInOutRecordDTO2 != null) {
                    hmeEmployeeAttendanceDto.setUnitName(hmeSignInOutRecordDTO2.getUnitName());
                }
                //标准人数
                Integer employNumber = hmeSignInOutRecordRepository.findEmployNumberCount(tenantId, hmeEmployeeAttendanceDto.getUnitId());
                hmeEmployeeAttendanceDto.setEmployNumber(employNumber);
                //出勤数
                String shiftDateStr = DateUtil.format(hmeEmployeeAttendanceDto.getDate(), DATE_FORMAT);
                Long actualAttendance = hmeOpenEndShiftMapper.actualAttendanceQuery(tenantId, shiftDateStr,
                        hmeEmployeeAttendanceDto.getShiftCode(), String.valueOf(hmeEmployeeAttendanceDto.getUnitId()));
                hmeEmployeeAttendanceDto.setAttendanceNumber(actualAttendance.intValue());
                //缺勤数
                hmeEmployeeAttendanceDto.setNoWorkNumber(employNumber - actualAttendance.intValue());
                //标准总工时
                MtCalendarVO2 calendarVO = new MtCalendarVO2();
                calendarVO.setCalendarType(HmeConstants.ApiConstantValue.STANDARD);
                calendarVO.setSiteType(HmeConstants.ApiConstantValue.MANUFACTURING);
                calendarVO.setOrganizationType(HmeConstants.ApiConstantValue.WORKCELL);
                calendarVO.setOrganizationId(hmeEmployeeAttendanceDto.getWorkId());
                String calendarId = calendarRepository.organizationLimitOnlyCalendarGet(tenantId, calendarVO);
                if (StringUtils.isNotEmpty(calendarId)) {
                    List<HmeSignInOutRecordDTO7> mtCalendarShiftList = hmeSignInOutRecordMapper.findShiftSodeList2(tenantId, calendarId, shiftDateStr, hmeEmployeeAttendanceDto.getShiftCode());
                    if (CollectionUtils.isNotEmpty(mtCalendarShiftList)) {
                        Date startTime = mtCalendarShiftList.get(0).getShiftStartTime();
                        Date endTime = mtCalendarShiftList.get(0).getShiftEndTime();
                        if (mtCalendarShiftList.get(0).getRestTime() != null) {
                            Long restTime = mtCalendarShiftList.get(0).getRestTime().intValue() * HOUR;
                            Long time = (endTime.getTime() - startTime.getTime() - restTime) * employNumber;
                            hmeEmployeeAttendanceDto.setCountTime(time / HOUR);
                        } else {
                            Long time = (endTime.getTime() - startTime.getTime()) * employNumber;
                            hmeEmployeeAttendanceDto.setCountTime(time / HOUR);
                        }
                    }
                }
                //实际总工时
                Long countWorkTime = Long.valueOf(0);
                //查询工段下的所有工位下的relId集合,进而取到每个工位的实际工时求和
                for (String worlcellId : workcellIdList2) {
                    List<String> relIdList = hmeSignInOutRecordMapper.getRelId(tenantId, worlcellId, hmeEmployeeAttendanceDto.getUnitId(),
                            hmeEmployeeAttendanceDto.getShiftCode(), hmeEmployeeAttendanceDto.getDate());
                    if (CollectionUtils.isNotEmpty(relIdList)) {
                        for (String relId : relIdList) {
                            //查询relId下是否有OPERATION = CLOSE的数据，如果有，则累加duration
                            List<HmeSignInOutRecord> hmeSignInOutRecords = hmeSignInOutRecordRepository.select(new HmeSignInOutRecord() {{
                                setTenantId(tenantId);
                                setRelId(relId);
                                setOperation("CLOSE");
                            }});
                            if (CollectionUtils.isNotEmpty(hmeSignInOutRecords)) {
                                String duration = hmeSignInOutRecords.get(0).getDuration();
                                String[] hours = duration.split(":");
                                countWorkTime += Long.valueOf(hours[0]) * HOUR + Long.valueOf(hours[1]) * RECOUND + Long.valueOf(hours[2]) * MISS;
                            } else {
                                //如果没有，则根据relId找到OPERATION_DATE最大的那笔数据
                                HmeSignInOutRecord hmeSignInOutRecord = hmeSignInOutRecordMapper.maxOperationDateQuery(tenantId, relId);
                                if ("OPEN".equals(hmeSignInOutRecord.getOperation())) {
                                    //如果OPERATION = OPEN，则累加当前时间 - 该数据的DATE时间
                                    countWorkTime += (nowDate.getTime() - hmeSignInOutRecord.getDate().getTime());
                                } else if ("OFF".equals(hmeSignInOutRecord.getOperation())) {
                                    //如果OPERATION = OFF，则累加duration
                                    String duration = hmeSignInOutRecord.getDuration();
                                    String[] hours = duration.split(":");
                                    countWorkTime += Long.valueOf(hours[0]) * HOUR + Long.valueOf(hours[1]) * RECOUND + Long.valueOf(hours[2]) * MISS;
                                } else if ("ON".equals(hmeSignInOutRecord.getOperation())) {
                                    //如果OPERATION = ON，则累加duration + 当前时间 - 该数据的DATE时间
                                    Long time1 = nowDate.getTime() - hmeSignInOutRecord.getDate().getTime();
                                    String duration = hmeSignInOutRecord.getDuration();
                                    String[] hours = duration.split(":");
                                    Long time2 = Long.valueOf(hours[0]) * HOUR + Long.valueOf(hours[1]) * RECOUND + Long.valueOf(hours[2]) * MISS;
                                    countWorkTime += (time1 + time2);
                                }
                            }
                        }
                    }
                }
                hmeEmployeeAttendanceDto.setCountWorkTime(countWorkTime / HOUR);
                //偏差
                if (hmeEmployeeAttendanceDto.getCountTime() != null && hmeEmployeeAttendanceDto.getCountWorkTime() != null) {
                    hmeEmployeeAttendanceDto.setNoWorkTime(hmeEmployeeAttendanceDto.getCountTime() - hmeEmployeeAttendanceDto.getCountWorkTime());
                }
            }
//            Date shiftStartTime = hmeEmployeeAttendanceDto.getShiftStartDate();
//            Date shiftEndTime = hmeEmployeeAttendanceDto.getShiftEndDate() == null ? nowDate : hmeEmployeeAttendanceDto.getShiftEndDate();
            //2020-09-22 edit by chaonan.hu for fang.pan 修改总产量的取值逻辑
            //2021-03-11 edit by chaonan.hu for tianyang.xie 修改总产量的取值逻辑
            BigDecimal countNumber = hmeSignInOutRecordMapper.getCountNumberNew(tenantId, hmeEmployeeAttendanceDto.getWkcShiftId(),
                    hmeEmployeeAttendanceDto.getWorkId(), dto);
            hmeEmployeeAttendanceDto.setCountNumber(countNumber);
            //2021-03-11 add by chaonan.hu for tianyang.xie 增加实际产出的取值逻辑
            BigDecimal actualOutputNumber = hmeSignInOutRecordMapper.getActualOutputNumber(tenantId, hmeEmployeeAttendanceDto.getWkcShiftId(),
                    hmeEmployeeAttendanceDto.getWorkId(), dto);
            hmeEmployeeAttendanceDto.setActualOutputNumber(actualOutputNumber);
            //2021-03-11 edit by chaonan.hu for tianyang.xie 修改不良数的取值逻辑
            BigDecimal defectsNumber = hmeSignInOutRecordMapper.defectNumberNew(tenantId, hmeEmployeeAttendanceDto.getWkcShiftId(),
                    hmeEmployeeAttendanceDto.getWorkId(), dto, hmeEmployeeAttendanceDto.getShiftStartDate(), hmeEmployeeAttendanceDto.getShiftEndDate());
            hmeEmployeeAttendanceDto.setDefectsNumber(defectsNumber);
            //班长
            List<String> groupLeaderList = new ArrayList<>();
            if (hmeEmployeeAttendanceDto.getUnitId() != null) {
                //根据部门查询岗位
                String positionId = null;
                ResponseEntity<List<HmeShiftVO2>> positionResponse = hmeHzeroPlatformFeignClient.queryPositionByUnit(tenantId,
                        String.valueOf(hmeEmployeeAttendanceDto.getUnitId()));
                if (CollectionUtils.isNotEmpty(positionResponse.getBody())) {
                    for (HmeShiftVO2 position : positionResponse.getBody()) {
                        if ("1".equals(position.getSupervisorFlag())) {
                            positionId = position.getPositionId();
                        }
                    }
                }
                if (StringUtils.isNotEmpty(positionId)) {
                    //根据岗位查询员工
                    ResponseEntity<String> headers = hmeHzeroPlatformFeignClient.queryEmployeeIdByPositionId(tenantId,
                            String.valueOf(hmeEmployeeAttendanceDto.getUnitId()), positionId, "0", "999999");
                    Page<Map<String, Object>> page = new Page<Map<String, Object>>();
                    try {
                        page = objectMapper.readValue(headers.getBody(), page.getClass());
                    } catch (IOException e) {

                    }
                    if (CollectionUtils.isNotEmpty(page.getContent())) {
                        List<Map<String, Object>> content = page.getContent();
                        for (Map<String, Object> employeeMap : content) {
                            String employeeId = String.valueOf(employeeMap.get("employeeId"));
                            //根据员工查询姓名
                            ResponseEntity<HmeShiftVO3> employee = hmeHzeroPlatformFeignClient.queryEmployeeNameById(tenantId, employeeId);
                            if (employee.getBody() != null) {
                                groupLeaderList.add(employee.getBody().getName());
                            }
                        }
                    }
                }
            }
            hmeEmployeeAttendanceDto.setGroupLeaderList(groupLeaderList);
        }
        return result;
    }

    @Override
    public Page<HmeEmployeeAttendanceExportVO3> lineWorkcellProductDetails(Long tenantId, HmeEmployeeAttendanceDTO15 dto, PageRequest pageRequest) {
        Page<HmeEmployeeAttendanceExportVO3> pageObj = PageHelper.doPage(pageRequest, () -> hmeSignInOutRecordMapper.lineWorkcellProductDetails(tenantId, dto));
        for (HmeEmployeeAttendanceExportVO3 hmeEmployeeAttendanceExportVO3 : pageObj.getContent()) {
            //加工时长
            if(hmeEmployeeAttendanceExportVO3.getSiteInDate() != null && hmeEmployeeAttendanceExportVO3.getSiteOutDate() != null){
                long time = hmeEmployeeAttendanceExportVO3.getSiteOutDate().getTime() - hmeEmployeeAttendanceExportVO3.getSiteInDate().getTime();
                long min = 1000*60;
                BigDecimal processTime = BigDecimal.valueOf(time).divide(BigDecimal.valueOf(min), 2, BigDecimal.ROUND_HALF_UP);
                hmeEmployeeAttendanceExportVO3.setProcessTime(processTime);
            }
        }
        return pageObj;
    }

    @Override
    public Page<HmeEmployeeAttendanceExportVO4> lineWorkcellNcDetails(Long tenantId, HmeEmployeeAttendanceDTO15 dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeSignInOutRecordMapper.lineWorkcellNcDetails(tenantId, dto));    }

    public String timeDifference(Date date1, Date date2) {
        String hourStr = null;
        String minuteStr = null;
        String sStr = null;

        long diff = date1.getTime() - date2.getTime();
        //时
        long hour = diff / (1000 * 60 * 60);
        //分
        long minute = (diff - hour * (1000 * 60 * 60)) / (1000 * 60);
        //秒
        long s = (diff / 1000 - hour * (60 * 60) - minute * 60);
        //补0
        if (String.valueOf(hour).length() == 1) {
            hourStr = "0" + hour;
        } else {
            hourStr = String.valueOf(hour);
        }
        if (String.valueOf(minute).length() == 1) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = String.valueOf(minute);
        }
        if (String.valueOf(s).length() == 1) {
            sStr = "0" + s;
        } else {
            sStr = String.valueOf(s);
        }
        return hourStr + minuteStr + sStr;
    }
}
