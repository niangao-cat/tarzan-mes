package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeSignInOutRecordService;
import com.ruike.hme.domain.entity.HmeSignInOutRecord;
import com.ruike.hme.domain.repository.HmeSignInOutRecordRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeSignInOutRecordMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.calendar.domain.repository.MtCalendarRepository;
import tarzan.calendar.domain.repository.MtCalendarShiftRepository;
import tarzan.calendar.domain.vo.MtCalendarShiftVO2;
import tarzan.calendar.domain.vo.MtCalendarVO2;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ruike.hme.infra.constant.HmeConstants.ApiSignInOutRecordValue.OPERATION_ON;
import static com.ruike.hme.infra.constant.HmeConstants.ApiSignInOutRecordValue.OPERATION_OPEN;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.HME;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * 员工上下岗记录表应用服务默认实现
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-13 11:28:07
 */
@Service
@Slf4j
public class HmeSignInOutRecordServiceImpl implements HmeSignInOutRecordService {
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final HmeSignInOutRecordRepository hmeSignInOutRecordRepository;
    private final MtCalendarRepository calendarRepository;
    private final MtCalendarShiftRepository mtCalendarShiftRepository;
    private final MtModWorkcellRepository mtModWorkcellRepository;
    private final MtModAreaRepository mtModAreaRepository;
    private final MtModOrganizationRelRepository mtModOrganizationRelRepository;
    private final MtModProductionLineRepository mtModProductionLineRepository;
    private final HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    private final MtWkcShiftRepository mtWkcShiftRepository;
    private final HmeSignInOutRecordMapper hmeSignInOutRecordMapper;

    public static final Long HOUR = 3600000L;
    public static final Long RECOUND = 60000L;
    public static final Long MISS = 1000L;
    public static final String STAFF_OPERATION = "HME.STAFF_OPERATION";//集值编码
    public static final String STAFF_OFFREASON = "HME.STAFF_OFFREASON";

    public HmeSignInOutRecordServiceImpl(MtErrorMessageRepository mtErrorMessageRepository, HmeSignInOutRecordRepository hmeSignInOutRecordRepository, MtCalendarRepository calendarRepository, MtCalendarShiftRepository mtCalendarShiftRepository, MtModWorkcellRepository mtModWorkcellRepository, MtModAreaRepository mtModAreaRepository, MtModOrganizationRelRepository mtModOrganizationRelRepository, MtModProductionLineRepository mtModProductionLineRepository, HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper, MtWkcShiftRepository mtWkcShiftRepository, HmeSignInOutRecordMapper hmeSignInOutRecordMapper) {
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.hmeSignInOutRecordRepository = hmeSignInOutRecordRepository;
        this.calendarRepository = calendarRepository;
        this.mtCalendarShiftRepository = mtCalendarShiftRepository;
        this.mtModWorkcellRepository = mtModWorkcellRepository;
        this.mtModAreaRepository = mtModAreaRepository;
        this.mtModOrganizationRelRepository = mtModOrganizationRelRepository;
        this.mtModProductionLineRepository = mtModProductionLineRepository;
        this.hmeWorkOrderManagementMapper = hmeWorkOrderManagementMapper;
        this.mtWkcShiftRepository = mtWkcShiftRepository;
        this.hmeSignInOutRecordMapper = hmeSignInOutRecordMapper;
    }

    /**
     * 用户+员工 +岗位+工位+日期 +班次 查询上下班记录
     */
    @Override
    public Page<HmeSignInOutRecordDTO8> hmeSignInOutRecordListQuery(Long tenantId,
                                                                    HmeSignInOutRecordDTO9 hmeSignInOutRecordDTO9, PageRequest pageRequest) {
        HmeSignInOutRecordDTO hmeSignInOutRecordDTO = new HmeSignInOutRecordDTO();
        hmeSignInOutRecordDTO.setTenantId(tenantId);
        hmeSignInOutRecordDTO.setUserId(hmeSignInOutRecordDTO9.getUserId());
        hmeSignInOutRecordDTO.setEmployeeId(hmeSignInOutRecordDTO9.getEmployeeId());
        hmeSignInOutRecordDTO.setWorkcellId(hmeSignInOutRecordDTO9.getWorkcellId());
        hmeSignInOutRecordDTO.setDate(hmeSignInOutRecordDTO9.getDate());
        hmeSignInOutRecordDTO.setShiftCode(hmeSignInOutRecordDTO9.getShiftCode());
        List<HmeSignInOutRecordDTO8> list = new ArrayList<HmeSignInOutRecordDTO8>();
        Page<HmeSignInOutRecord> recordList = PageHelper
                .doPageAndSort(pageRequest, () -> hmeSignInOutRecordRepository.getHmeSignInOutRecordList(tenantId, hmeSignInOutRecordDTO));
        if (CollectionUtils.isNotEmpty(recordList.getContent())) {
            List<HmeSignInOutRecord> hmeSignInOutRecordList = recordList.getContent();
            HmeSignInOutRecordDTO1 hmeSignInOutRecordDTO1 = hmeSignInOutRecordRepository.getEemployeeQuery(tenantId, hmeSignInOutRecordList.get(0).getUserId());
            // 工位根据主键查询
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeSignInOutRecordList.get(0).getWorkcellId());
            //工厂
            List<MtModSite> modSiteList = hmeSignInOutRecordRepository.getMtModSiteList(tenantId, hmeSignInOutRecordList.get(0).getWorkcellId());
            // 车间 根据ROOT_CAUSE_WORKCELL_ID调用API{parentOrganizationRelQuery}
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
            mtModOrganizationVO2.setTopSiteId(defaultSiteId);
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setOrganizationId(hmeSignInOutRecordList.get(0).getWorkcellId());
            mtModOrganizationVO2.setParentOrganizationType("AREA");
            mtModOrganizationVO2.setQueryType("ALL");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            String areaName = "";
            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
                MtModArea mtModArea = mtModAreaRepository
                        .selectByPrimaryKey(mtModOrganizationItemVO.getOrganizationId());
                if ("CJ".equals(mtModArea.getAreaCategory())) {
                    areaName = mtModArea.getAreaName();
                    break;
                }
            }
            // 生产线
            String prodLineName = "";
            mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
            mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            if (CollectionUtils.isNotEmpty(mtModOrganizationItemVOS)) {
                MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(mtModOrganizationItemVOS.get(0).getOrganizationId());
                prodLineName = mtModProductionLine.getProdLineName();
            }
            List<HpfmLovValueDTO> values = hmeSignInOutRecordRepository.queryLovValues(tenantId, STAFF_OPERATION);
            // 离岗原因值集
            List<HpfmLovValueDTO> reasonList = hmeSignInOutRecordRepository.queryLovValues(tenantId, STAFF_OFFREASON);
            for (HmeSignInOutRecord hmeSignInOutRecord : hmeSignInOutRecordList) {
                HmeSignInOutRecordDTO8 hmeSignInOutRecordDTO8 = new HmeSignInOutRecordDTO8();
                hmeSignInOutRecordDTO8.setShiftCode(hmeSignInOutRecord.getShiftCode());
                if (hmeSignInOutRecordDTO1 != null) {
                    hmeSignInOutRecordDTO8.setEmployeeName(hmeSignInOutRecordDTO1.getEmployeeName());
                }
                if (mtModWorkcell != null) {
                    hmeSignInOutRecordDTO8.setWorkcellName(mtModWorkcell.getWorkcellName());
                }
                if (CollectionUtils.isNotEmpty(modSiteList)) {
                    hmeSignInOutRecordDTO8.setSiteName(modSiteList.get(0).getSiteName());
                }
                if (StringUtils.isNotBlank(areaName)) {
                    hmeSignInOutRecordDTO8.setAreaName(areaName);
                }
                if (StringUtils.isNotBlank(prodLineName)) {
                    hmeSignInOutRecordDTO8.setProdLineName(prodLineName);
                }

                String operation = hmeSignInOutRecord.getOperation();
                if (CollectionUtils.isNotEmpty(values)) {
                    for (HpfmLovValueDTO hpfmLovValueDTO : values) {
                        if (hpfmLovValueDTO.getValue().equals(operation)) {
                            hmeSignInOutRecordDTO8.setOperation(hpfmLovValueDTO.getMeaning());
                        }
                    }
                }
                hmeSignInOutRecordDTO8.setDuration(hmeSignInOutRecord.getDuration());
                hmeSignInOutRecordDTO8.setOperationDate(hmeSignInOutRecord.getOperationDate());
                Optional<HpfmLovValueDTO> reasonOpt = reasonList.stream().filter(reason -> StringUtils.equals(reason.getValue(), hmeSignInOutRecord.getReason())).findFirst();
                hmeSignInOutRecordDTO8.setReason(reasonOpt.isPresent() ? reasonOpt.get().getMeaning() : "");
                list.add(hmeSignInOutRecordDTO8);
            }

        }
        Page<HmeSignInOutRecordDTO8> resultList = new Page<>();
        resultList.setTotalPages(recordList.getTotalPages());
        resultList.setTotalElements(recordList.getTotalElements());
        resultList.setNumberOfElements(recordList.getNumberOfElements());
        resultList.setSize(recordList.getSize());
        resultList.setNumber(recordList.getNumber());
        resultList.setContent(list);
        return resultList;
    }

    /**
     * 用户信息
     */
    @Override
    public HmeSignInOutRecordDTO1 getUserQuery(Long tenantId, Long userId) {
        // 查找员工ID及名称
        HmeSignInOutRecordDTO1 hmeSignInOutRecordDTO1 = hmeSignInOutRecordRepository.getEemployeeQuery(tenantId, userId);
        if (hmeSignInOutRecordDTO1 == null) {
            throw new MtException("HME_STAFF_0001", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_STAFF_0001", "HME"));
        }
        hmeSignInOutRecordDTO1.setUserId(userId);
        hmeSignInOutRecordDTO1.setTenantId(tenantId);

//        List<HmeWorkCellDTO> HmeWorkCellDTOTemp=new ArrayList<>();
//        //获取不需要资质的wokecell
//        List<HmeWorkCellDTO> hmeWorkCellDTOs = hmeSignInOutRecordRepository.getNoModWorkcellList(tenantId, userId);
//        if(CollectionUtils.isNotEmpty(hmeWorkCellDTOs)) {
//            HmeWorkCellDTOTemp.addAll(hmeWorkCellDTOs);
//        }
        //获取需要资质的wokecell
        //获取岗位资质
//        List<HmeWorkCellDTO> hmeWorkCellDTOs1 = hmeSignInOutRecordRepository.getModWorkcellList(tenantId);
//        if(CollectionUtils.isNotEmpty(hmeWorkCellDTOs1)) {
//            //获取员工资质
//            List<HmeWorkCellDTO> hmeWorkCellDTOs2 = hmeSignInOutRecordRepository.getEmployeeList(tenantId, hmeSignInOutRecordDTO1.getEmployeeId());
//            Map<String,List<HmeWorkCellDTO>> HmeWorkCellDTOMap=hmeWorkCellDTOs1.stream()
//                    .collect(Collectors.groupingBy(HmeWorkCellDTO::getWorkcellId));
//            for (String key : HmeWorkCellDTOMap.keySet()) {
//                List<HmeWorkCellDTO> hmeWorkCellDTOS = HmeWorkCellDTOMap.get(key);
//                int size = hmeWorkCellDTOS.stream()
//                        .map(t -> hmeWorkCellDTOs2.stream().filter(s -> Objects.nonNull(t.getQualityId()) && Objects.nonNull(s.getQualityId()) && Objects.equals(t.getQualityId(), s.getQualityId())).findAny().orElse(null))
//                        .filter(Objects::nonNull)
//                        .collect(Collectors.toList())
//                        .size();
//                if(size==hmeWorkCellDTOS.size())
//                {
//                    HmeWorkCellDTOTemp.addAll(hmeWorkCellDTOS);
//                }
//            }
//        }

//        // 获取所有的岗位及名称
//        List<HmeSignInOutRecordDTO2> list = hmeSignInOutRecordRepository.getUnitList(tenantId, hmeSignInOutRecordDTO1.getEmployeeId());
//        if (CollectionUtils.isNotEmpty(list)) {
//            for (HmeSignInOutRecordDTO2 hmeSignInOutRecordDTO2 : list) {
//                List<HmeWorkCellDTO> collect = HmeWorkCellDTOTemp.stream().filter(s -> s.getUnitId().equals(hmeSignInOutRecordDTO2.getUnitId().toString())).collect(Collectors.toList());
//                List<HmeSignInOutRecordDTO3> HmeSignInOutRecordDTO3=new ArrayList<>();
//                for (HmeWorkCellDTO hmeWorkCellDTO:
//                collect) {
//                    HmeSignInOutRecordDTO3 hmeSignInOutRecordDTO3 = new HmeSignInOutRecordDTO3();
//                    hmeSignInOutRecordDTO3.setWorkcellId(hmeWorkCellDTO.getWorkcellId());
//                    hmeSignInOutRecordDTO3.setWorkcellName(hmeWorkCellDTO.getWorkcellName());
//                    HmeSignInOutRecordDTO3.add(hmeSignInOutRecordDTO3);
//                }
//                List<HmeSignInOutRecordDTO3> collect1 = HmeSignInOutRecordDTO3.stream().distinct().collect(Collectors.toList());
//                if (1 == hmeSignInOutRecordDTO2.getPrimaryPositionFlag()) {
//                    hmeSignInOutRecordDTO1.setUnitId(hmeSignInOutRecordDTO2.getUnitId());
//                    hmeSignInOutRecordDTO1.setUnitName(hmeSignInOutRecordDTO2.getUnitName());
//                    hmeSignInOutRecordDTO1.setWorkcellList(collect1);
//                }
//                hmeSignInOutRecordDTO2.setWorkcellList(collect1);
//            }
//            hmeSignInOutRecordDTO1.setUnitList(list);
//        } else {
//            throw new MtException("HME_STAFF_0002", mtErrorMessageRepository
//                    .getErrorMessageWithModule(tenantId, "HME_STAFF_0002", "HME"));
//        }

        // 查询可操作工段
        List<MtModWorkcell> mtModWorkcellList = hmeSignInOutRecordRepository.getMtModWorkcellList(tenantId, userId);
        List<HmeSignInOutRecordDTO3> recordDTO3List = mtModWorkcellList.stream().map(rec -> new HmeSignInOutRecordDTO3() {{
            setWorkcellId(rec.getWorkcellId());
            setWorkcellName(rec.getWorkcellName());
        }}).collect(Collectors.toList());

        //// 根据员工ID 查找具备资质
        //List<HmeEmployeeAssign> hmeEmployeeAssignList = hmeSignInOutRecordRepository.queryData(tenantId, hmeSignInOutRecordDTO1.getEmployeeId());
        ////if (CollectionUtils.isNotEmpty(mtModWorkcellList)&&CollectionUtils.isNotEmpty(hmeEmployeeAssignList)) {
        //if (CollectionUtils.isNotEmpty(mtModWorkcellList)) {
        //    // 根据员工资质以及可操作工段 找到可操作工位
        //    List<String> workcellIds = mtModWorkcellList.stream().map(MtModWorkcell::getWorkcellId).collect(Collectors.toList());
        //    List<String> qualityIds = null;
        //    if (CollectionUtils.isNotEmpty(hmeEmployeeAssignList)) {
        //        qualityIds = hmeEmployeeAssignList.stream().map(HmeEmployeeAssign::getQualityId).collect(Collectors.toList());
        //    }
        //
        //    List<HmeSignInOutRecordDTO3> workcellList2 = hmeSignInOutRecordRepository.findWorkcell(tenantId,
        //            workcellIds, qualityIds);
        //    ArrayList<HmeSignInOutRecordDTO3> workcellList = new ArrayList<>();
        //    workcellList.addAll(workcellList2);
        //    ArrayList<String> list1 = new ArrayList<>();
        //    list1.add("Processing station");
        //    if (CollectionUtils.isNotEmpty(workcellList2)) {
        //        //存在工位（存在一工位多资质的满足要求）
        //        List<String> workcellLists = workcellList2.stream().map(HmeSignInOutRecordDTO3::getWorkcellId).collect(Collectors.toList());
        //        //查询一对一满足工位所需的资质ID
        //        List<HmeSignInOutRecordDTO3> workcellQualityId = hmeSignInOutRecordRepository.findWorkcellQualityId(tenantId, workcellLists);
        //                for (int j = workcellList2.size()-1; j >=0; j--){
        //                    for (int i = 0; i < workcellQualityId.size(); i++){
        //                    if (workcellQualityId.get(i).getQualityId() != null ){
        //                    boolean contains1 = list1.contains(workcellQualityId.get(i).getWorkcellName());
        //                    if (workcellList2.get(j).getWorkcellName().equals(workcellQualityId.get(i).getWorkcellName()) && !contains1){
        //                        boolean contains = qualityIds.contains(workcellQualityId.get(i).getQualityId());
        //                        if (!contains){
        //                            list1.add(workcellQualityId.get(i).getWorkcellName());
        //                            workcellList.remove(j);
        //                            continue;
        //                        }
        //                    }
        //                }
        //            }
        //        }
        //        hmeSignInOutRecordDTO1.setWorkcellList(workcellList);
        //    }
        //}
        hmeSignInOutRecordDTO1.setWorkcellList(recordDTO3List);
        return hmeSignInOutRecordDTO1;
    }

    @Override
    public List<HmeSignInOutRecordDTO4> getUserAttendanceQuery(Long tenantId, HmeSignInOutRecordDTO10 dto) {
        int year = dto.getYear();
        int month = dto.getMonth();
        LocalDate date = LocalDate.of(year, month, 1);
        LocalDate now = LocalDate.now();
        int dayNum = date.lengthOfMonth();
        if (date.getYear() == now.getYear() && date.getMonthValue() == now.getMonthValue()) {
            dayNum = now.getDayOfMonth();
        }
        int[] lenth = new int[dayNum];
        List<HmeSignInOutRecordDTO4> list = new ArrayList<HmeSignInOutRecordDTO4>(dayNum);
        // 根据所选工段ID，找到日历班次的工作日
        // 2021-02-03 add by sanfeng.zhang for zhao.peng 工位改成工段查
        MtCalendarVO2 calendarVO = new MtCalendarVO2();
        calendarVO.setCalendarType(HmeConstants.ApiConstantValue.STANDARD);
        calendarVO.setSiteType(HmeConstants.ApiConstantValue.MANUFACTURING);
        calendarVO.setOrganizationType(HmeConstants.ApiConstantValue.WORKCELL);
        calendarVO.setOrganizationId(dto.getWorkcellId());
        String calendarId = calendarRepository.organizationLimitOnlyCalendarGet(tenantId, calendarVO);
        HmeSignInOutRecordDTO4 hmeSignInOutRecordDTO4 = null;
        for (int i = 0; i < lenth.length; i++) {
            hmeSignInOutRecordDTO4 = new HmeSignInOutRecordDTO4();
            hmeSignInOutRecordDTO4.setDay(i + 1);
            if (StringUtils.isNotEmpty(calendarId)) {
                // 根据所选工位ID，找到日历班次的工作日
                MtCalendarShiftVO2 mtCalendarShiftVO2 = new MtCalendarShiftVO2();
                mtCalendarShiftVO2.setCalendarId(calendarId);
                Date time = new GregorianCalendar(year, month - 1, i + 1).getTime();
                mtCalendarShiftVO2.setShiftDate(time);
                List<String> calenderShiftIdS = mtCalendarShiftRepository.calendarLimitShiftQuery(tenantId, mtCalendarShiftVO2);
                if (CollectionUtils.isEmpty(calenderShiftIdS)) {
                    // 如果找到，则当天为工作日，如果没有找到，则当天为休息日
                    hmeSignInOutRecordDTO4.setIsWork(null);
                } else {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    HmeSignInOutRecordDTO hmeSignInOutRecordDTO = new HmeSignInOutRecordDTO();
                    hmeSignInOutRecordDTO.setUserId(dto.getUserId());
                    hmeSignInOutRecordDTO.setEmployeeId(dto.getEmployeeId());
                    hmeSignInOutRecordDTO.setWorkcellId(dto.getWorkcellId());
                    hmeSignInOutRecordDTO.setDate(format.format(time));
                    List<HmeSignInOutRecord> recordList = hmeSignInOutRecordRepository.getHmeSignInOutRecordList(tenantId, hmeSignInOutRecordDTO);
                    if (CollectionUtils.isEmpty(recordList)) {
                        // 缺勤
                        hmeSignInOutRecordDTO4.setIsWork(false);
                    } else {
                        // 正常上班
                        hmeSignInOutRecordDTO4.setIsWork(true);
                    }
                }
            } else {
                //无日历id
                hmeSignInOutRecordDTO4.setIsWork(null);
            }
            list.add(hmeSignInOutRecordDTO4);
        }
        return list;
    }

    @Override
    public List<HmeSignInOutRecordDTO6> getUserFrequencyQuery(Long tenantId, HmeSignInOutRecordDTO5 dto) {
        List<HmeSignInOutRecordDTO6> list = new ArrayList<HmeSignInOutRecordDTO6>();
        if (dto.getYear() == null || dto.getMonth() == null || dto.getDay() == null) {
            // 1.如果未选择日期或者班次，则按钮为灰色，不可进行操作
            return list;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat returnformat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat formats = new SimpleDateFormat("HH:mm");
        Date time = new GregorianCalendar(dto.getYear(), dto.getMonth() - 1, dto.getDay()).getTime();
        String choiceTime = format.format(time);
        // 根据所选工位ID，找到日历班次的工作日
        MtCalendarVO2 calendarVO = new MtCalendarVO2();
        calendarVO.setCalendarType(HmeConstants.ApiConstantValue.STANDARD);
        calendarVO.setSiteType(HmeConstants.ApiConstantValue.MANUFACTURING);
        calendarVO.setOrganizationType(HmeConstants.ApiConstantValue.WORKCELL);
        calendarVO.setOrganizationId(dto.getWorkcellId());
        String calendarId = calendarRepository.organizationLimitOnlyCalendarGet(tenantId, calendarVO);
        if (StringUtils.isNotEmpty(calendarId)) {
            // 根据所选工位ID，找到日历班次的工作日
            MtCalendarShiftVO2 mtCalendarShiftVO2 = new MtCalendarShiftVO2();
            mtCalendarShiftVO2.setCalendarId(calendarId);
            mtCalendarShiftVO2.setShiftDate(time);
            List<String> calenderShiftIdS = mtCalendarShiftRepository.calendarLimitShiftQuery(tenantId, mtCalendarShiftVO2);
            if (CollectionUtils.isNotEmpty(calenderShiftIdS)) {
                // 班次shiftcode
                List<HmeSignInOutRecordDTO7> mtCalendarShiftList = hmeSignInOutRecordRepository.findShiftSodeList(tenantId, calendarId, choiceTime);
                if (CollectionUtils.isEmpty(mtCalendarShiftList)) {
                    throw new MtException("HME_STAFF_0003", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_STAFF_0003", "HME"));
                }
                for (HmeSignInOutRecordDTO7 mtCalendarShift : mtCalendarShiftList) {
                    HmeSignInOutRecordDTO6 hmeSignInOutRecordDTO6 = new HmeSignInOutRecordDTO6();
                    hmeSignInOutRecordDTO6.setChoiceTime(choiceTime);
                    hmeSignInOutRecordDTO6.setShiftcode(mtCalendarShift.getShiftCode());
                    Date endTime = mtCalendarShift.getShiftEndTime();
                    Date startTime = mtCalendarShift.getShiftStartTime();
                    Date nows = new Date();
                    String now = format.format(nows);
                    if (choiceTime.compareTo(now) > 0) {
                        hmeSignInOutRecordDTO6.setChoice(false);
                    } else {
                        if (choiceTime.compareTo(now) == 0) {
                            // 当天
                            if (choiceTime.compareTo(format.format(endTime)) > 0) {
                                hmeSignInOutRecordDTO6.setChoice(false);
                            } else {
                                hmeSignInOutRecordDTO6.setChoice(true);
                            }
                        } else {
                            hmeSignInOutRecordDTO6.setChoice(true);
                        }
                        // 查询起止时间
                    }
                    // 班次起止时间
                    hmeSignInOutRecordDTO6.setStartAndEndTime(formats.format(startTime) + " ~ " + formats.format(endTime));
                    // 历史班次，及所选班次的结束时间早于当前时间员工ID+工位ID+ 岗位ID+ 日期+班次
                    HmeSignInOutRecordDTO hmeSignInOutRecordDTO = new HmeSignInOutRecordDTO();
                    hmeSignInOutRecordDTO.setUserId(dto.getUserId());
                    hmeSignInOutRecordDTO.setEmployeeId(dto.getEmployeeId());
                    hmeSignInOutRecordDTO.setWorkcellId(dto.getWorkcellId());
                    hmeSignInOutRecordDTO.setShiftCode(mtCalendarShift.getShiftCode());
                    hmeSignInOutRecordDTO.setDate(choiceTime);
                    List<HmeSignInOutRecord> hmeSignInOutRecordList = hmeSignInOutRecordRepository.getHmeSignInOutRecordList(tenantId, hmeSignInOutRecordDTO);
                    //当前班次最后的信息
                    if (nows.compareTo(startTime) > 0 && nows.compareTo(endTime) < 0) {
                        if (CollectionUtils.isNotEmpty(hmeSignInOutRecordList)) {
                            List<HmeSignInOutRecord> openList = hmeSignInOutRecordList.stream().filter(
                                    s -> OPERATION_OPEN.equals(s.getOperation()))
                                    .collect(Collectors.toList());
                            List<HmeSignInOutRecord> recentRecordList = openList.stream().sorted(Comparator.comparing(HmeSignInOutRecord::getCreationDate)).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(recentRecordList)) {
                                //存入relid 取最早开班的记录id
                                hmeSignInOutRecordDTO6.setRelId(recentRecordList.get(0).getRecordId());
                            }
                            String operation = hmeSignInOutRecordList.get(0).getOperation();
                            Date operationDate = hmeSignInOutRecordList.get(0).getOperationDate();
                            String duration = hmeSignInOutRecordList.get(0).getDuration();
                            if (OPERATION_OPEN.equals(operation) || OPERATION_ON.equals(operation)) {
                                //累计时间
                                String[] hours = duration.split(":");
                                Long oldTime = Long.valueOf(hours[0]) * HOUR + Long.valueOf(hours[1]) * RECOUND + Long.valueOf(hours[2]) * MISS;
                                oldTime += nows.getTime() - operationDate.getTime();
                                StringBuffer newDuration = new StringBuffer();
                                newDuration = oldTime / HOUR < 10 ? newDuration.append("0").append(oldTime / HOUR).append(":") : newDuration.append(oldTime / HOUR).append(":");
                                newDuration = oldTime % HOUR / RECOUND < 10 ? newDuration.append("0").append(oldTime % HOUR / RECOUND).append(":") : newDuration.append(oldTime % HOUR / RECOUND).append(":");
                                newDuration = oldTime % HOUR % RECOUND / MISS < 10 ? newDuration.append("0").append(oldTime % HOUR % RECOUND / MISS) : newDuration.append(oldTime % HOUR % RECOUND / MISS);
                                hmeSignInOutRecordDTO6.setDuration(newDuration.toString());
                                hmeSignInOutRecordDTO6.setStartSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_OPEN);
                                hmeSignInOutRecordDTO6.setCloseSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_OPEN);
                                if (OPERATION_OPEN.equals(operation)) {
                                    hmeSignInOutRecordDTO6.setStartName(HmeConstants.ApiSignInOutRecordValue.START_NAME_OPEN);
                                } else {
                                    hmeSignInOutRecordDTO6.setStartName(HmeConstants.ApiSignInOutRecordValue.START_NAME_ON);
                                }
                                //累计时间
                            } else if (HmeConstants.ApiSignInOutRecordValue.OPERATION_OFF.equals(operation)) {
                                hmeSignInOutRecordDTO6.setStartSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_OPEN);
                                hmeSignInOutRecordDTO6.setCloseSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_OPEN);
                                hmeSignInOutRecordDTO6.setStartName(HmeConstants.ApiSignInOutRecordValue.START_NAME_OFF);
                                //累计时间
                                hmeSignInOutRecordDTO6.setDuration(hmeSignInOutRecordList.get(0).getDuration());
                            } else {
                                //当前班次接班判断是否可以重新的开班
                                hmeSignInOutRecordDTO6.setStartAgain(true);
                                hmeSignInOutRecordDTO6.setStartSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_CLOSE);
                                hmeSignInOutRecordDTO6.setCloseSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_CLOSE);
                                // 结班 取结班的操作时间
                                List<HmeSignInOutRecord> closeList = hmeSignInOutRecordList.stream().filter(
                                        s -> HmeConstants.ApiSignInOutRecordValue.OPERATION_CLOSE.equals(s.getOperation()))
                                        .collect(Collectors.toList());
                                if (CollectionUtils.isNotEmpty(closeList)) {
                                    hmeSignInOutRecordDTO6.setCloseTime(returnformat.format(closeList.get(0).getOperationDate()));
                                }
                                //累计时间
                                hmeSignInOutRecordDTO6.setDuration(hmeSignInOutRecordList.get(0).getDuration());
                            }
                            // 开始时间显示--
                            hmeSignInOutRecordDTO6.setStartTime(returnformat.format(openList.get(0).getOperationDate()));
                            hmeSignInOutRecordDTO6.setOperation(operation);
                        } else {
                            hmeSignInOutRecordDTO6.setStartTime(HmeConstants.ApiSignInOutRecordValue.START_TIME);
                            hmeSignInOutRecordDTO6.setCloseTime(HmeConstants.ApiSignInOutRecordValue.START_TIME);
                            hmeSignInOutRecordDTO6.setDuration(HmeConstants.ApiSignInOutRecordValue.START_TIME);
                            hmeSignInOutRecordDTO6.setStartSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_OPEN);
                            hmeSignInOutRecordDTO6.setCloseSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_OPEN);
                        }
                    } else {
                        //历史班次
                        List<HmeSignInOutRecord> openList = hmeSignInOutRecordList.stream().filter(
                                s -> OPERATION_OPEN.equals(s.getOperation()))
                                .collect(Collectors.toList());
                        List<HmeSignInOutRecord> closeList = hmeSignInOutRecordList.stream().filter(
                                s -> HmeConstants.ApiSignInOutRecordValue.OPERATION_CLOSE.equals(s.getOperation()))
                                .collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(hmeSignInOutRecordList)) {
                            List<HmeSignInOutRecord> recentRecordList = openList.stream().sorted(Comparator.comparing(HmeSignInOutRecord::getCreationDate)).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(recentRecordList)) {
                                //存入relid 取最早开班的记录id
                                hmeSignInOutRecordDTO6.setRelId(recentRecordList.get(0).getRecordId());
                            }
                            if (CollectionUtils.isEmpty(openList)) {
                                hmeSignInOutRecordDTO6.setStartSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_CLOSE);
                                hmeSignInOutRecordDTO6.setCloseSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_CLOSE);
                            }
                            if (CollectionUtils.isNotEmpty(openList) && CollectionUtils.isNotEmpty(closeList)) {
//                                //判断之后是否有开班记录
//                                HmeSignInOutRecordDTO hmeSignInOutRecordDTO3 = new HmeSignInOutRecordDTO();
//                                hmeSignInOutRecordDTO3.setUserId(dto.getUserId());
//                                hmeSignInOutRecordDTO3.setEmployeeId(dto.getEmployeeId());
//                                hmeSignInOutRecordDTO3.setUnitId(dto.getUnitId());
//                                hmeSignInOutRecordDTO3.setWorkcellId(dto.getWorkcellId());
//                                //hmeSignInOutRecordDTO.setShiftCode(mtCalendarShift.getShiftCode());
//                                //hmeSignInOutRecordDTO.setDate(choiceTime);
//                                hmeSignInOutRecordDTO3.setOperation(HmeConstants.ApiSignInOutRecordValue.OPERATION_OPEN);
//                                hmeSignInOutRecordDTO3.setStartTime(closeList.get(0).getOperationDate());
//                                List<HmeSignInOutRecord> hmeSignInOutRecordList2 = hmeSignInOutRecordRepository.getHmeSignInOutRecordList(tenantId, hmeSignInOutRecordDTO3);
//                                if (CollectionUtils.isNotEmpty(hmeSignInOutRecordList2)) {
//                                    hmeSignInOutRecordDTO6.setStartAgain(false);
//                                } else {
//                                    hmeSignInOutRecordDTO6.setStartAgain(true);
//                                }
                                hmeSignInOutRecordDTO6.setStartSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_CLOSE);
                                hmeSignInOutRecordDTO6.setCloseSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_CLOSE);
                            }
                            if (CollectionUtils.isNotEmpty(openList) && CollectionUtils.isEmpty(closeList)) {
                                hmeSignInOutRecordDTO6.setStartSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_OPEN);
                                hmeSignInOutRecordDTO6.setCloseSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_OPEN);
                            }
                            //------按键显示开始----------
                            HmeSignInOutRecord hmeSignInOutRecord = hmeSignInOutRecordList.get(0);
                            // 按键状态
                            String operation = hmeSignInOutRecord.getOperation();
                            String duration = hmeSignInOutRecord.getDuration();
                            hmeSignInOutRecordDTO6.setOperation(operation);
                            // 操作记录时间
                            Date date = hmeSignInOutRecord.getOperationDate();
                            // 累计时间
                            if (OPERATION_OPEN.equals(operation) || OPERATION_ON.equals(operation)) {
                                //超过24小时计算
                                String[] hours = duration.split(":");
                                Long oldTime = Long.valueOf(hours[0]) * HOUR + Long.valueOf(hours[1]) * RECOUND + Long.valueOf(hours[2]) * MISS;
                                oldTime += nows.getTime() - date.getTime();
                                StringBuffer newDuration = new StringBuffer();
                                newDuration = oldTime / HOUR < 10 ? newDuration.append("0").append(oldTime / HOUR).append(":") : newDuration.append(oldTime / HOUR).append(":");
                                newDuration = oldTime % HOUR / RECOUND < 10 ? newDuration.append("0").append(oldTime % HOUR / RECOUND).append(":") : newDuration.append(oldTime % HOUR / RECOUND).append(":");
                                newDuration = oldTime % HOUR % RECOUND / MISS < 10 ? newDuration.append("0").append(oldTime % HOUR % RECOUND / MISS) : newDuration.append(oldTime % HOUR % RECOUND / MISS);
                                hmeSignInOutRecordDTO6.setDuration(newDuration.toString());
                            } else {
                                hmeSignInOutRecordDTO6.setDuration(hmeSignInOutRecordList.get(0).getDuration());
                            }
                            // 开始
                            if (OPERATION_OPEN.equals(operation)
                                    || OPERATION_ON.equals(operation)) {
                                hmeSignInOutRecordDTO6.setStartName(HmeConstants.ApiSignInOutRecordValue.START_NAME_ON);
                            } else if (HmeConstants.ApiSignInOutRecordValue.OPERATION_OFF.equals(operation)) {
                                hmeSignInOutRecordDTO6.setStartName(HmeConstants.ApiSignInOutRecordValue.START_NAME_OFF);
                            } else if (HmeConstants.ApiSignInOutRecordValue.OPERATION_CLOSE.equals(operation)) {
                                hmeSignInOutRecordDTO6.setStartSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_CLOSE);
                            }
                            // 开始时间显示--
                            if (CollectionUtils.isNotEmpty(openList)) {
                                hmeSignInOutRecordDTO6.setStartTime(returnformat.format(openList.get(0).getOperationDate()));
                            }
                            // 结束时间显示---
                            if (HmeConstants.ApiSignInOutRecordValue.OPERATION_CLOSE.equals(operation)) {
                                hmeSignInOutRecordDTO6.setCloseTime(returnformat.format(date));
                            }
                            //--------按键显示结束----------
                        } else {
                            //没有历史的岗位记录
                            hmeSignInOutRecordDTO6.setStartSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_OPEN);
                            hmeSignInOutRecordDTO6.setCloseSwitch(HmeConstants.ApiSignInOutRecordValue.SWITCH_OPEN);
                            hmeSignInOutRecordDTO6.setStartTime(HmeConstants.ApiSignInOutRecordValue.START_TIME);
                            hmeSignInOutRecordDTO6.setCloseTime(HmeConstants.ApiSignInOutRecordValue.START_TIME);
                            hmeSignInOutRecordDTO6.setDuration(HmeConstants.ApiSignInOutRecordValue.START_TIME);
                        }
                    }
                    //添加进度条时间集合
                    List<HmeSignInOutRecordDTO11> timeList = new ArrayList<HmeSignInOutRecordDTO11>();
                    if (CollectionUtils.isNotEmpty(hmeSignInOutRecordList)) {
                        //当前班次最早时间
                        Date fastTime = hmeSignInOutRecordList.get(hmeSignInOutRecordList.size() - 1).getOperationDate();
                        Date temp = fastTime;
                        //当前班次最晚时间
                        Date lastTime = hmeSignInOutRecordList.get(0).getOperationDate();
                        HmeSignInOutRecordDTO11 hmeSignInOutRecordDTO11 = new HmeSignInOutRecordDTO11();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(fastTime);
                        hmeSignInOutRecordDTO11.setTimes(formatTime.format(calendar.getTime()));
                        hmeSignInOutRecordDTO11.setTime(formats.format(calendar.getTime()));
                        hmeSignInOutRecordDTO11.setTimeType(calendar.getTimeInMillis());
                        timeList.add(hmeSignInOutRecordDTO11);
                        //后面的时间
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);
                        fastTime = calendar.getTime();
                        while (fastTime.compareTo(lastTime) < 0) {
                            HmeSignInOutRecordDTO11 hmeSignInOutRecordDTO12 = new HmeSignInOutRecordDTO11();
                            hmeSignInOutRecordDTO12.setTimes(formatTime.format(calendar.getTime()));
                            hmeSignInOutRecordDTO12.setTime(formats.format(calendar.getTime()));
                            hmeSignInOutRecordDTO12.setTimeType(calendar.getTimeInMillis());
                            timeList.add(hmeSignInOutRecordDTO12);
                            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);
                            fastTime = calendar.getTime();
                        }
                        if (temp.compareTo(lastTime) != 0) {
                            HmeSignInOutRecordDTO11 hmeSignInOutRecordDTO12 = new HmeSignInOutRecordDTO11();
                            hmeSignInOutRecordDTO12.setTimes(formatTime.format(lastTime));
                            hmeSignInOutRecordDTO12.setTime(formats.format(lastTime));
                            hmeSignInOutRecordDTO12.setTimeType(lastTime.getTime());
                            timeList.add(hmeSignInOutRecordDTO12);
                        }
                        for (HmeSignInOutRecordDTO11 hmeSignInOutRecordDTO13 : timeList) {
                            try {
                                Date times = formatTime.parse(hmeSignInOutRecordDTO13.getTimes());
                                //获取前一个
                                if (CollectionUtils.isNotEmpty(hmeSignInOutRecordList)) {
                                    List<HmeSignInOutRecord> recordList = hmeSignInOutRecordList.stream().filter(e -> times.compareTo(e.getOperationDate()) >= 0).collect(Collectors.toList());
                                    if (CollectionUtils.isNotEmpty(recordList)) {
                                        HmeSignInOutRecord record = recordList.stream().findFirst().get();
                                        if (OPERATION_OPEN.equals(record.getOperation())) {
                                            hmeSignInOutRecordDTO13.setWork(true);
                                        } else if (HmeConstants.ApiSignInOutRecordValue.OPERATION_OFF.equals(record.getOperation())) {
                                            hmeSignInOutRecordDTO13.setWork(false);
                                        } else if (OPERATION_ON.equals(record.getOperation())) {
                                            hmeSignInOutRecordDTO13.setWork(true);
                                        } else {
                                            hmeSignInOutRecordDTO13.setWork(true);
                                        }
                                    }
                                }
                            } catch (ParseException e1) {
                                throw new MtException("HME_STAFF_0007", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "HME_STAFF_0007", "HME"));
                            }
                        }
                        //先放所有的打卡记录
                        hmeSignInOutRecordList.remove(hmeSignInOutRecordList.size() - 1);
                        if (CollectionUtils.isNotEmpty(hmeSignInOutRecordList)) {
                            hmeSignInOutRecordList.remove(0);
                        }
                        if (CollectionUtils.isNotEmpty(hmeSignInOutRecordList)) {
                            for (HmeSignInOutRecord record : hmeSignInOutRecordList) {
                                HmeSignInOutRecordDTO11 hmeSignInOutRecordDTO12 = new HmeSignInOutRecordDTO11();
                                hmeSignInOutRecordDTO12.setTimes(formatTime.format(record.getOperationDate()));
                                hmeSignInOutRecordDTO12.setTime(formats.format(record.getOperationDate()));
                                hmeSignInOutRecordDTO12.setTimeType(record.getOperationDate().getTime());
                                if (OPERATION_OPEN.equals(record.getOperation()) ||
                                        OPERATION_ON.equals(record.getOperation())) {
                                    hmeSignInOutRecordDTO12.setWork(true);
                                }
                                timeList.add(hmeSignInOutRecordDTO12);
                            }
                        }
                        //去重
                        timeList = timeList.stream().collect(Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(r -> r.getTimes()))), ArrayList::new));
                        timeList = timeList.stream().sorted(Comparator.comparing(r -> r.getTimeType())).collect(Collectors.toList());
                    }
                    hmeSignInOutRecordDTO6.setList(timeList);
                    //返回班次信息
                    list.add(hmeSignInOutRecordDTO6);
                }
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeSignInOutRecord creat(Long tenantId, HmeSignInOutRecord hmeSignInOutRecord) {
        hmeSignInOutRecord.setTenantId(tenantId);
        String operation = hmeSignInOutRecord.getOperation();
        if (StringUtils.isEmpty(operation)) {
            throw new MtException("HME_STAFF_0006", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_STAFF_0006", "HME"));
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (hmeSignInOutRecord.getDate() == null) {
            throw new MtException("HME_STAFF_0008", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_STAFF_0008", "HME"));
        }
        if (hmeSignInOutRecord.getOperationDate() == null) {
            hmeSignInOutRecord.setOperationDate(date);
        }
        //开始或继续
		/*if(HmeConstants.ApiSignInOutRecordValue.OPERATION_OPEN.equals(operation)||
			HmeConstants.ApiSignInOutRecordValue.OPERATION_ON.equals(operation)){*/
			/*HmeSignInOutRecordDTO hmeSignInOutRecordDTO=new HmeSignInOutRecordDTO();
			hmeSignInOutRecordDTO.setUserId(hmeSignInOutRecord.getUserId());
			hmeSignInOutRecordDTO.setDate(format.format(hmeSignInOutRecord.getDate()));
			List<HmeSignInOutRecord> recordList	=hmeSignInOutRecordRepository.getEachGroupMaxList(tenantId,hmeSignInOutRecordDTO);
			if(CollectionUtils.isNotEmpty(recordList)){
				//校验同一员工不可同时在多个岗位在岗
				for (HmeSignInOutRecord hmeSignInOutRecord2 : recordList) {
					if (HmeConstants.ApiSignInOutRecordValue.OPERATION_OPEN.equals(hmeSignInOutRecord2.getOperation())||
							HmeConstants.ApiSignInOutRecordValue.OPERATION_ON.equals(hmeSignInOutRecord2.getOperation()) ) {
						MtModWorkcell mtModWorkcell =mtModWorkcellRepository.selectByPrimaryKey(hmeSignInOutRecord2.getWorkcellId());
						throw new MtException("HME_STAFF_0004", mtErrorMessageRepository
								.getErrorMessageWithModule(tenantId, "HME_STAFF_0004", "HME",mtModWorkcell.getWorkcellCode()));
					}
				}
			}*/
        if (YES.equals(hmeSignInOutRecord.getStartAgen()) && StringUtils.isNotEmpty(hmeSignInOutRecord.getRelId())) {
            HmeSignInOutRecordDTO hmeSignInOutRecordDTO1 = new HmeSignInOutRecordDTO();
            hmeSignInOutRecordDTO1.setRelId(hmeSignInOutRecord.getRelId());
            hmeSignInOutRecordDTO1.setOperation(HmeConstants.ApiSignInOutRecordValue.OPERATION_CLOSE);
            List<HmeSignInOutRecord> hmeSignInOutRecordList1 = hmeSignInOutRecordRepository.getHmeSignInOutRecordList(tenantId, hmeSignInOutRecordDTO1);
            if (CollectionUtils.isEmpty(hmeSignInOutRecordList1)) {
                throw new MtException("HME_STAFF_0009", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_STAFF_0009", "HME"));
            }
            HmeSignInOutRecord hmeSignInOutRecord2 = hmeSignInOutRecordList1.get(0);
            hmeSignInOutRecord2.setOperation(HmeConstants.ApiSignInOutRecordValue.OPERATION_OFF);
            hmeSignInOutRecordRepository.updateByPrimaryKey(hmeSignInOutRecord2);
            hmeSignInOutRecord2.setRecordId(null);
            hmeSignInOutRecord2.setCid(null);
            hmeSignInOutRecord2.setOperation(OPERATION_ON);
            hmeSignInOutRecord2.setOperationDate(date);
            hmeSignInOutRecordRepository.insertSelective(hmeSignInOutRecord2);
            return hmeSignInOutRecord2;
        }

        List<HmeEmployeeAttendanceDTO11> attendanceDTO11List = hmeSignInOutRecordRepository.findOperation(tenantId, hmeSignInOutRecord.getEmployeeId(), hmeSignInOutRecord.getWorkcellId());
        //如果之前没有开班记录
        if (attendanceDTO11List.size() == 0 && OPERATION_OPEN.equals(operation)) {
            // 开班校验
            this.verifyStaffOpen(tenantId, hmeSignInOutRecord);
            hmeSignInOutRecordRepository.insertSelective(hmeSignInOutRecord);
            return hmeSignInOutRecord;
        }
        //开始或继续
        if (OPERATION_OPEN.equals(operation) && attendanceDTO11List.size() != 0) {
            this.verifyStaffOpen(tenantId, hmeSignInOutRecord);
            //同一个工位存在未结班数据，则不允许进行新的开班
            ArrayList<String> list = new ArrayList<>();
            Date date1 = null;
            String shiftCode = null;
            String relId2 = null;
            String recordId2 = null;
            for (HmeEmployeeAttendanceDTO11 attendanceDTO11 : attendanceDTO11List) {
                list.add(attendanceDTO11.getOperation());
                if (!attendanceDTO11.getOperation().equals(HmeConstants.ApiSignInOutRecordValue.OPERATION_CLOSE)) {
                    date1 = attendanceDTO11.getDate();
                    shiftCode = attendanceDTO11.getShiftCode();
                } else {
                    relId2 = attendanceDTO11.getRelId();
                    recordId2 = attendanceDTO11.getRecordId();
                }
            }
            boolean contains = list.contains(HmeConstants.ApiSignInOutRecordValue.OPERATION_CLOSE);
            if (!contains) {
                throw new MtException("HME_STAFF_0010", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_STAFF_0010", "HME", date1 + "" + shiftCode));
            }
            //查看是不是同一班次的重新开班
            HmeSignInOutRecord hmeSignInOutRecord1 = new HmeSignInOutRecord();
            hmeSignInOutRecord1.setOperation(HmeConstants.ApiSignInOutRecordValue.OPERATION_CLOSE);
            hmeSignInOutRecord1.setWorkcellId(hmeSignInOutRecord.getWorkcellId());
            HmeSignInOutRecord hmeSignInOutRecord2 = hmeSignInOutRecordRepository.selectOne(hmeSignInOutRecord1);
            if (hmeSignInOutRecord2.getRelId().equals(relId2)) {
                hmeSignInOutRecord2.setOperation("OFF");
                hmeSignInOutRecordRepository.updateByPrimaryKey(hmeSignInOutRecord2);
            }
            hmeSignInOutRecordRepository.insertSelective(hmeSignInOutRecord);
            //hmeSignInOutRecord.setRelId(hmeSignInOutRecord.getRecordId());
            //hmeSignInOutRecordRepository.updateByPrimaryKey(hmeSignInOutRecord);


        } else {
            //查找开班记录
            HmeSignInOutRecordDTO hmeSignInOutRecordDTO1 = new HmeSignInOutRecordDTO();
            hmeSignInOutRecordDTO1.setUserId(hmeSignInOutRecord.getUserId());
            hmeSignInOutRecordDTO1.setEmployeeId(hmeSignInOutRecord.getEmployeeId());
            hmeSignInOutRecordDTO1.setWorkcellId(hmeSignInOutRecord.getWorkcellId());
            hmeSignInOutRecordDTO1.setShiftCode(hmeSignInOutRecord.getShiftCode());
            hmeSignInOutRecordDTO1.setDate(format.format(hmeSignInOutRecord.getDate()));
            hmeSignInOutRecordDTO1.setOperation(OPERATION_OPEN);
            List<HmeSignInOutRecord> hmeSignInOutRecordList1 = hmeSignInOutRecordRepository.getHmeSignInOutRecordList(tenantId, hmeSignInOutRecordDTO1);
            if (CollectionUtils.isEmpty(hmeSignInOutRecordList1)) {
                throw new MtException("HME_STAFF_0005", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_STAFF_0005", "HME"));
            }
            // 增加上岗校验 班组是否开班
            hmeSignInOutRecord.setRelId(hmeSignInOutRecordList1.get(0).getRecordId());
            hmeSignInOutRecordRepository.insertSelective(hmeSignInOutRecord);
        }
		/*}else{
			//查找开班记录
			HmeSignInOutRecordDTO hmeSignInOutRecordDTO=new HmeSignInOutRecordDTO();
			hmeSignInOutRecordDTO.setUserId(hmeSignInOutRecord.getUserId());
			hmeSignInOutRecordDTO.setEmployeeId(hmeSignInOutRecord.getEmployeeId());
			hmeSignInOutRecordDTO.setUnitId(hmeSignInOutRecord.getUnitId());
			hmeSignInOutRecordDTO.setWorkcellId(hmeSignInOutRecord.getWorkcellId());
			hmeSignInOutRecordDTO.setShiftCode(hmeSignInOutRecord.getShiftCode());
			hmeSignInOutRecordDTO.setDate(format.format(hmeSignInOutRecord.getDate()));
			hmeSignInOutRecordDTO.setOperation(HmeConstants.ApiSignInOutRecordValue.OPERATION_OPEN);
			List<HmeSignInOutRecord> hmeSignInOutRecordList = hmeSignInOutRecordRepository.getHmeSignInOutRecordList(tenantId, hmeSignInOutRecordDTO);
			if(CollectionUtils.isEmpty(hmeSignInOutRecordList)){
				throw new MtException("HME_STAFF_0005", mtErrorMessageRepository
						.getErrorMessageWithModule(tenantId, "HME_STAFF_0005", "HME"));
			}
			hmeSignInOutRecord.setRelId(hmeSignInOutRecordList.get(0).getRelId());
			hmeSignInOutRecordRepository.insertSelective(hmeSignInOutRecord);
		}*/
        return hmeSignInOutRecord;
    }

    private void verifyStaffOpen(Long tenantId, HmeSignInOutRecord hmeSignInOutRecord) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 根据员工 + 工位查找最近一条记录，上一班次未结不允许上岗
        List<HmeSignInOutRecord> recordList = hmeSignInOutRecordRepository.selectByCondition(Condition.builder(HmeSignInOutRecord.class).andWhere(Sqls.custom()
                .andEqualTo(HmeSignInOutRecord.FIELD_TENANT_ID, tenantId)
                .andEqualTo(HmeSignInOutRecord.FIELD_USER_ID, hmeSignInOutRecord.getUserId())
                .andEqualTo(HmeSignInOutRecord.FIELD_EMPLOYEE_ID, hmeSignInOutRecord.getEmployeeId())
                .andEqualTo(HmeSignInOutRecord.FIELD_WORKCELL_ID, hmeSignInOutRecord.getWorkcellId()))
                .orderByDesc(HmeSignInOutRecord.FIELD_CREATION_DATE).build());
        if (CollectionUtils.isNotEmpty(recordList) && !HmeConstants.ApiSignInOutRecordValue.OPERATION_CLOSE.equals(recordList.get(0).getOperation())) {
            throw new MtException("HME_STAFF_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_STAFF_0011", "HME", format.format(recordList.get(0).getDate()), recordList.get(0).getShiftCode()));
        }

        // 校验上岗时已经开班
        List<MtWkcShift> shiftList = mtWkcShiftRepository.select(new MtWkcShift() {{
            setTenantId(tenantId);
            setWorkcellId(hmeSignInOutRecord.getWorkcellId());
            setShiftDate(hmeSignInOutRecord.getDate());
            setShiftCode(hmeSignInOutRecord.getShiftCode());
        }});
        if (CollectionUtils.isEmpty(shiftList)) {
            throw new MtException("HME_STAFF_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_STAFF_0012", HME));
        }
        shiftList.forEach(a -> {
            if (Objects.nonNull(a.getShiftEndTime())) {
                throw new MtException("HME_STAFF_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_STAFF_0012", HME));
            }
        });
    }

    @Override
    @ProcessLovValue
    public Page<HmeSignInOutRecordVO> listQuery(Long tenantId, HmeSignInOutRecordDTO12 dto, PageRequest pageRequest) {
        //时间必输
        if(StringUtils.isBlank(dto.getDateFrom()) || StringUtils.isBlank(dto.getDateTo())){
            throw new MtException("HME_EXCEPTION_REP_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_REP_001", "HME"));
        }
        List<HmeSignInOutRecordVO4> hmeSignInOutRecordVO4List = hmeSignInOutRecordRepository.listQuery(dto, tenantId);

        Map<HmeSignInOutRecordVO4, List<HmeSignInOutRecordVO4>> collect = hmeSignInOutRecordVO4List.stream().collect(
                Collectors.groupingBy(
                        hmeSignInOutRecordVO4 -> new HmeSignInOutRecordVO4(
                                hmeSignInOutRecordVO4.getDepartmentName(),
                                hmeSignInOutRecordVO4.getProdLineName(),
                                hmeSignInOutRecordVO4.getWorkcellName(),
                                hmeSignInOutRecordVO4.getLoginName(),
                                hmeSignInOutRecordVO4.getRealName(),
                                hmeSignInOutRecordVO4.getShiftCode()
                        )
                )
        );
        List<HmeSignInOutRecordVO> hmeSignInOutRecordVOList = new ArrayList<>();
        for (Map.Entry<HmeSignInOutRecordVO4, List<HmeSignInOutRecordVO4>> entry : collect.entrySet()) {
            HmeSignInOutRecordVO vo = new HmeSignInOutRecordVO();
            List<HmeSignInOutRecordVO2> hmeSignInOutRecordVO2List = new ArrayList<>();
            vo.setDepartmentName(entry.getKey().getDepartmentName());
            vo.setProdLineName(entry.getKey().getProdLineName());
            vo.setWorkcellName(entry.getKey().getWorkcellName());
            vo.setLoginName(entry.getKey().getLoginName());
            vo.setRealName(entry.getKey().getRealName());
            List<HmeSignInOutRecordVO4> list = entry.getValue();

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

            Map<Date, List<HmeSignInOutRecordVO4>> listMap = list.stream().collect(Collectors.groupingBy(HmeSignInOutRecordVO4::getDate));
            for (Map.Entry<Date, List<HmeSignInOutRecordVO4>> dateListEntry : listMap.entrySet()) {
                HmeSignInOutRecordVO2 vo2 = new HmeSignInOutRecordVO2();
                vo2.setDate(dateListEntry.getKey());
                vo2.setDateString(dateToString(dateListEntry.getKey()));
                List<HmeSignInOutRecordVO4> recordVO4List = dateListEntry.getValue();

                List<HmeSignInOutRecordVO4> close = recordVO4List.stream().filter(f -> "CLOSE".equals(f.getOperation())).collect(Collectors.toList());
                List<HmeSignInOutRecordVO4> open = recordVO4List.stream().filter(f -> "OPEN".equals(f.getOperation())).collect(Collectors.toList());

                if(close.size()>0 && open.size()>0) {
                    BigDecimal onTime = new BigDecimal(0);
                    try {
                        //上岗时间
                        onTime = dateTimeToBigDecimal(dateFormat.parse(close.get(0).getDuration()));
                    } catch (ParseException e) {
                        log.info("<=====HmeSignInOutRecordServiceImpl==listQuery==>" + e.getMessage() + "[" + close.get(0).getDuration() + "]");
                    }
                    BigDecimal a = new BigDecimal(0);
                    BigDecimal b = new BigDecimal(0);
                    if (close.get(0).getShiftStartTime().before(open.get(0).getOperationDate())) {
                        a = (dateToBigDecimal(open.get(0).getOperationDate()).subtract(dateToBigDecimal(close.get(0).getShiftStartTime())));
                    }
                    if (close.get(0).getShiftEndTime().after(close.get(0).getOperationDate())) {
                        b = (dateToBigDecimal(close.get(0).getShiftEndTime()).subtract(dateToBigDecimal(close.get(0).getOperationDate())));
                    }
                    //下岗时间
                    BigDecimal downTime = a.add(b);
                    //暂停时间
                    BigDecimal stopTime = dateToBigDecimal(close.get(0).getOperationDate()).subtract(dateToBigDecimal(open.get(0).getOperationDate())).subtract(onTime);

                    vo2.setOnTime(onTime.setScale(2,BigDecimal.ROUND_HALF_UP));
                    vo2.setDownTime(downTime.setScale(2,BigDecimal.ROUND_HALF_UP));
                    vo2.setStopTime(stopTime.setScale(2,BigDecimal.ROUND_HALF_UP));
                    List<HmeSignInOutRecordVO4> vo4s = recordVO4List.stream().filter(f -> "OFF".equals(f.getOperation())).sorted(Comparator.comparing(HmeSignInOutRecordVO4::getCreationDate)).collect(Collectors.toList());
                    StringBuilder reasonMeaningList = new StringBuilder();
                    for (HmeSignInOutRecordVO4 vo4 : vo4s) {
                        if (reasonMeaningList.length() == 0) {
                            if(StringUtils.isBlank(vo4.getReasonMeaning())){
                                reasonMeaningList.append("重新上岗");
                            }else {
                                reasonMeaningList.append(vo4.getReasonMeaning());
                            }
                        }else {
                            if(StringUtils.isBlank(vo4.getReasonMeaning())){
                                reasonMeaningList.append(";重新上岗");
                            }else{
                                reasonMeaningList.append(";" + vo4.getReasonMeaning());
                            }
                        }
                    }
                    vo2.setReasonMeaning(reasonMeaningList.toString());
                }
                vo2.setShiftCode(entry.getKey().getShiftCode());
                hmeSignInOutRecordVO2List.add(vo2);
            }
            vo.setNumOfMonth(BigDecimal.valueOf(hmeSignInOutRecordVO2List.size()));
            vo.setHmeSignInOutRecordVO2List(hmeSignInOutRecordVO2List);
            hmeSignInOutRecordVOList.add(vo);
        }
        Page<HmeSignInOutRecordVO> resultList = new Page<>();
        //计算获取子串开始结束位置
        int fromIndex = pageRequest.getPage() * pageRequest.getSize();
        int toIndex = Math.min(fromIndex + pageRequest.getSize(), hmeSignInOutRecordVOList.size());

        resultList.setTotalPages((int) hmeSignInOutRecordVOList.size()/pageRequest.getSize()+1);
        resultList.setTotalElements(hmeSignInOutRecordVOList.size());
        resultList.setNumberOfElements(toIndex-fromIndex);
        resultList.setSize(pageRequest.getSize());
        resultList.setNumber(pageRequest.getPage());
        resultList.setContent(hmeSignInOutRecordVOList);
        return  resultList;
    }

    private BigDecimal dateTimeToBigDecimal(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        BigDecimal h = BigDecimal.valueOf(calendar.get(Calendar.HOUR_OF_DAY));				//时（24小时制）
        BigDecimal m = BigDecimal.valueOf(calendar.get(Calendar.MINUTE));					//分
        return h.add(m.divide(BigDecimal.valueOf(60),20,BigDecimal.ROUND_HALF_UP));
    }
    private BigDecimal dateToBigDecimal(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        BigDecimal h = BigDecimal.valueOf(calendar.get(Calendar.HOUR_OF_DAY));				//时（24小时制）
        BigDecimal m = BigDecimal.valueOf(calendar.get(Calendar.MINUTE));					//分
        return BigDecimal.valueOf(d*24).add(h).add(m.divide(BigDecimal.valueOf(60),20,BigDecimal.ROUND_HALF_UP));
    }
    private String dateToString(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int y = calendar.get(Calendar.YEAR);			//年
        int m = calendar.get(Calendar.MONTH)+1;			//月
        int d = calendar.get(Calendar.DAY_OF_MONTH);	//日
        return y+"年"+m+"月"+d+"日";
    }
}

