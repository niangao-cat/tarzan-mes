package tarzan.calendar.app.service.impl;

import static io.tarzan.common.domain.util.StringHelper.getWhereInValuesSql;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import tarzan.calendar.api.dto.*;
import tarzan.calendar.app.service.MtCalendarShiftService;
import tarzan.calendar.domain.entity.MtCalendarShift;
import tarzan.calendar.domain.entity.MtShift;
import tarzan.calendar.domain.repository.MtCalendarRepository;
import tarzan.calendar.domain.repository.MtCalendarShiftRepository;
import tarzan.calendar.domain.repository.MtShiftRepository;
import tarzan.calendar.domain.vo.MtCalendarShiftVO7;
import tarzan.calendar.domain.vo.MtCalendarVO3;
import tarzan.calendar.infra.mapper.MtCalendarShiftMapper;

/**
 * 工作日历班次应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:15
 */
@Service
public class MtCalendarShiftServiceImpl implements MtCalendarShiftService {

    private static final int MILLS_PER_DAY = 86400000;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtCalendarRepository mtCalendarRepo;

    @Autowired
    private MtCalendarShiftRepository mtCalendarShiftRepo;

    @Autowired
    private MtShiftRepository mtShiftRepo;

    @Autowired
    private MtCalendarShiftMapper mtCalendarShiftMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public List<MtCalendarShiftDTO2> queryCalendarShiftGridForUi(Long tenantId, MtCalendarShiftDTO3 dto) {
        SimpleDateFormat simpleMonthFormat = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat shiftDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat shiftTimeFormat = new SimpleDateFormat("HH:mm:ss");

        Date calendarDate = new Date();
        try {
            calendarDate = simpleMonthFormat.parse(dto.getCalendarDate());
        }catch (Exception e){

        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendarDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // 将小时至0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        // 将分钟至0
        calendar.set(Calendar.MINUTE, 0);
        // 将秒至0
        calendar.set(Calendar.SECOND, 0);
        // 将毫秒至0
        calendar.set(Calendar.MILLISECOND, 0);
        // 获得当前月第一天
        dto.setCalendarDateStartTime(calendar.getTime());
        // 将当前月加1；
        calendar.add(Calendar.MONTH, 1);
        // 在当前月的下一月基础上减去1毫秒
        calendar.add(Calendar.MILLISECOND, -1);
        // 获得当前月最后一天
        dto.setCalendarDateEndTime(calendar.getTime());


        List<MtCalendarShiftDTO2> resultList = new ArrayList<>();

        List<MtCalendarShift> calendarShiftList = mtCalendarShiftRepo.selectByCondition(Condition
                .builder(MtCalendarShift.class)
                .andWhere(Sqls.custom().andEqualTo(MtCalendarShift.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtCalendarShift.FIELD_CALENDAR_ID, dto.getCalendarId())
                        .andGreaterThanOrEqualTo(MtCalendarShift.FIELD_SHIFT_DATE,
                                dto.getCalendarDateStartTime().toInstant()
                                        .atZone(ZoneId.systemDefault()).toLocalDate())
                        .andLessThanOrEqualTo(MtCalendarShift.FIELD_SHIFT_DATE,
                                dto.getCalendarDateEndTime().toInstant()
                                        .atZone(ZoneId.systemDefault()).toLocalDate()))
                .orderByAsc(MtCalendarShift.FIELD_SEQUENCE).build());

        if (CollectionUtils.isEmpty(calendarShiftList)) {
            return resultList;
        }

        Map<String, List<MtCalendarShift>> calendarShiftPerDate = calendarShiftList.stream()
                        .collect(Collectors.groupingBy(c -> shiftDateFormat.format(c.getShiftDate())));
        MtCalendarShiftDTO2 result;
        for (Map.Entry<String, List<MtCalendarShift>> entry : calendarShiftPerDate.entrySet()) {
            result = new MtCalendarShiftDTO2();
            result.setShiftDate(entry.getKey());

            List<MtCalendarShiftDTO> shiftList = new ArrayList<>(entry.getValue().size());
            MtCalendarShiftDTO shift;
            for (MtCalendarShift calendarShift : entry.getValue()) {
                shift = new MtCalendarShiftDTO();
                shift.setShiftCode(calendarShift.getShiftCode());
                shift.setShiftStartTime(shiftTimeFormat.format(calendarShift.getShiftStartTime()));
                shift.setShiftEndTime(shiftTimeFormat.format(calendarShift.getShiftEndTime()));
                shift.setShiftTimePeriod("[" + shift.getShiftStartTime() + "-" + shift.getShiftEndTime() + "]");
                shiftList.add(shift);
            }
            result.setCalendarShiftList(shiftList);
            resultList.add(result);
        }

        return resultList;
    }

    @Override
    public Page<MtCalendarShiftDTO4> queryCalendarShiftListForUi(Long tenantId, MtCalendarShiftDTO4 vo,
                    PageRequest pageRequest) {
        Page<MtCalendarShiftDTO4> result = new Page<MtCalendarShiftDTO4>();

        // 1. 查询日历班次信息
        Page<MtCalendarShift> base = PageHelper.doPage(pageRequest,
                        () -> mtCalendarShiftMapper.queryCalendarShiftListForUi(tenantId, vo));
        if (CollectionUtils.isEmpty(base.getContent())) {
            return result;
        }

        // 2. 查询所有日历
        List<String> calendarIdList =
                        base.stream().map(MtCalendarShift::getCalendarId).distinct().collect(Collectors.toList());
        List<MtCalendarVO3> mtCalendarVO3List = mtCalendarRepo.calendarBatchGet(tenantId, calendarIdList);

        // 转为Map数据
        Map<String, MtCalendarVO3> mtCalendarVO3Map = null;
        if (CollectionUtils.isNotEmpty(mtCalendarVO3List)) {
            mtCalendarVO3Map = mtCalendarVO3List.stream().collect(Collectors.toMap(t -> t.getCalendarId(), t -> t));
        }

        // 整理返回数据
        List<MtCalendarShiftDTO4> calendarShiftList = new ArrayList<MtCalendarShiftDTO4>(base.size());
        result.setTotalPages(base.getTotalPages());
        result.setTotalElements(base.getTotalElements());
        result.setNumberOfElements(base.getNumberOfElements());
        result.setSize(base.getSize());
        result.setNumber(base.getNumber());

        for (MtCalendarShift calendarShift : base) {
            MtCalendarShiftDTO4 resultDto = new MtCalendarShiftDTO4();
            resultDto.setCalendarShiftId(calendarShift.getCalendarShiftId());
            resultDto.setCalendarId(calendarShift.getCalendarId());

            if (MapUtils.isNotEmpty(mtCalendarVO3Map)) {
                MtCalendarVO3 mtCalendarVO3 = mtCalendarVO3Map.get(calendarShift.getCalendarId());
                if (mtCalendarVO3 != null) {
                    resultDto.setCalendarCode(mtCalendarVO3.getCalendarCode());
                }
            }

            resultDto.setShiftDate(calendarShift.getShiftDate());
            resultDto.setShiftCode(calendarShift.getShiftCode());
            resultDto.setEnableFlag(calendarShift.getEnableFlag());
            resultDto.setShiftStartTime(calendarShift.getShiftStartTime());
            resultDto.setShiftEndTime(calendarShift.getShiftEndTime());
            resultDto.setRestTime(calendarShift.getRestTime());
            resultDto.setUtilizationRate(calendarShift.getUtilizationRate());
            resultDto.setBorrowingAbility(calendarShift.getBorrowingAbility());
            resultDto.setCapacityUnit(calendarShift.getCapacityUnit());
            resultDto.setStandardCapacity(calendarShift.getStandardCapacity());
            resultDto.setSequence(calendarShift.getSequence());

            // calculate week of year & day of week
            LocalDate localDate = calendarShift.getShiftDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            resultDto.setWeekOfYear(localDate.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfYear()));
            resultDto.setDayOfWeek(Integer.valueOf(localDate.getDayOfWeek().getValue()));

            calendarShiftList.add(resultDto);
        }
        result.setContent(calendarShiftList);

        return result;
    }

    @Override
    public List<MtCalendarShiftDTO4> queryCalendarShiftListNoPageForUi(Long tenantId, MtCalendarShiftDTO4 vo,
                    PageRequest pageRequest) {
        List<MtCalendarShiftDTO4> result = new ArrayList<>();

        // 1. 查询日历班次信息
        List<MtCalendarShift> mtCalendarShiftList = mtCalendarShiftMapper.queryCalendarShiftListForUi(tenantId, vo);
        if (CollectionUtils.isEmpty(mtCalendarShiftList)) {
            return result;
        }

        // 2. 查询所有日历
        List<String> calendarIdList = mtCalendarShiftList.stream().map(MtCalendarShift::getCalendarId).distinct()
                        .collect(Collectors.toList());
        List<MtCalendarVO3> mtCalendarVO3List = mtCalendarRepo.calendarBatchGet(tenantId, calendarIdList);

        // 转为Map数据
        Map<String, MtCalendarVO3> mtCalendarVO3Map = null;
        if (CollectionUtils.isNotEmpty(mtCalendarVO3List)) {
            mtCalendarVO3Map = mtCalendarVO3List.stream().collect(Collectors.toMap(t -> t.getCalendarId(), t -> t));
        }

        // 整理返回数据
        for (MtCalendarShift calendarShift : mtCalendarShiftList) {
            MtCalendarShiftDTO4 resultDto = new MtCalendarShiftDTO4();
            resultDto.setCalendarShiftId(calendarShift.getCalendarShiftId());
            resultDto.setCalendarId(calendarShift.getCalendarId());

            if (MapUtils.isNotEmpty(mtCalendarVO3Map)) {
                MtCalendarVO3 mtCalendarVO3 = mtCalendarVO3Map.get(calendarShift.getCalendarId());
                if (mtCalendarVO3 != null) {
                    resultDto.setCalendarCode(mtCalendarVO3.getCalendarCode());
                }
            }

            resultDto.setShiftDate(calendarShift.getShiftDate());
            resultDto.setShiftCode(calendarShift.getShiftCode());
            resultDto.setEnableFlag(calendarShift.getEnableFlag());
            resultDto.setShiftStartTime(calendarShift.getShiftStartTime());
            resultDto.setShiftEndTime(calendarShift.getShiftEndTime());
            resultDto.setRestTime(calendarShift.getRestTime());
            resultDto.setUtilizationRate(calendarShift.getUtilizationRate());
            resultDto.setBorrowingAbility(calendarShift.getBorrowingAbility());
            resultDto.setCapacityUnit(calendarShift.getCapacityUnit());
            resultDto.setStandardCapacity(calendarShift.getStandardCapacity());
            resultDto.setSequence(calendarShift.getSequence());

            // calculate week of year & day of week
            LocalDate localDate = calendarShift.getShiftDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            resultDto.setWeekOfYear(localDate.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfYear()));
            resultDto.setDayOfWeek(Integer.valueOf(localDate.getDayOfWeek().getValue()));

            result.add(resultDto);
        }

        result = result.stream().sorted(Comparator.comparing(MtCalendarShiftDTO4::getShiftStartTime))
                        .collect(Collectors.toList());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveCalendarShiftForUi(Long tenantId, MtCalendarShiftDTO4 dto) {
        MtCalendarShiftDTO4 vo = new MtCalendarShiftDTO4();
        vo.setCalendarId(dto.getCalendarId());
        vo.setShiftDate(dto.getShiftDate());
        List<MtCalendarShift> calendarShiftList = mtCalendarShiftMapper.queryCalendarShiftListForUi(tenantId, vo);

        MtCalendarShift calendarShift = new MtCalendarShift();
        calendarShift.setCalendarShiftId(dto.getCalendarShiftId());
        calendarShift.setTenantId(tenantId);
        calendarShift.setCalendarId(dto.getCalendarId());
        calendarShift.setShiftDate(dto.getShiftDate());
        calendarShift.setShiftCode(dto.getShiftCode());
        calendarShift.setEnableFlag(dto.getEnableFlag());
        calendarShift.setShiftStartTime(dto.getShiftStartTime());
        calendarShift.setShiftEndTime(dto.getShiftEndTime());
        calendarShift.setRestTime(dto.getRestTime());
        calendarShift.setUtilizationRate(dto.getUtilizationRate());
        calendarShift.setBorrowingAbility(dto.getBorrowingAbility());
        calendarShift.setCapacityUnit(dto.getCapacityUnit());
        calendarShift.setStandardCapacity(dto.getStandardCapacity());
        calendarShift.setSequence(dto.getSequence());

        if (CollectionUtils.isEmpty(calendarShiftList)) {
            mtCalendarShiftRepo.insertSelective(calendarShift);
        } else {
            if (calendarShiftList.stream().anyMatch(s -> s.getShiftCode().equals(dto.getShiftCode())
                            && !s.getCalendarShiftId().equals(dto.getCalendarShiftId()))) {
                throw new MtException("MT_SHIFT_0012", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_SHIFT_0012", "SHIFT", calendarShift.getShiftCode()));
            }

            if (calendarShiftList.stream().anyMatch(s -> s.getSequence().equals(dto.getSequence())
                            && !s.getCalendarShiftId().equals(dto.getCalendarShiftId()))) {
                throw new MtException("MT_SHIFT_0013", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_SHIFT_0013", "SHIFT", String.valueOf(calendarShift.getSequence())));
            }

            // 如果是对已有数据修改，先删除旧数据
            Optional<MtCalendarShift> any = calendarShiftList.stream()
                            .filter(t -> t.getCalendarShiftId().equals(dto.getCalendarShiftId())).findAny();
            if (any.isPresent()) {
                calendarShiftList.remove(any.get());
            }

            // 填充新数据
            calendarShiftList.add(calendarShift);

            List<MtCalendarShift> sortedShiftList =
                            calendarShiftList.stream().sorted(Comparator.comparingLong(MtCalendarShift::getSequence))
                                            .collect(Collectors.toList());

            long duration = 0L;
            for (int i = 0; i < sortedShiftList.size(); i++) {
                Time startTime;
                Time endTime;
                startTime = new Time(sortedShiftList.get(i).getShiftStartTime().getTime());
                endTime = new Time(sortedShiftList.get(i).getShiftEndTime().getTime());

                // 后一个班次的开始时间不能大于前一个班次的结束时间
                if (i < sortedShiftList.size() - 1) {
                    Time nextStartTime = new Time(sortedShiftList.get(i + 1).getShiftStartTime().getTime());
                    if (nextStartTime.compareTo(endTime) <= 0) {
                        throw new MtException("MT_CALENDAR_0026", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                        "MT_CALENDAR_0026", "CALENDAR"));
                    }
                }

                if (startTime.compareTo(endTime) > 0) {
                    duration += new Date(endTime.getTime() + MILLS_PER_DAY).getTime() - startTime.getTime();
                } else {
                    duration += endTime.getTime() - startTime.getTime();
                }
            }

            if (duration > MILLS_PER_DAY) {
                throw new MtException("MT_SHIFT_0014",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_SHIFT_0014", "SHIFT"));
            }

            if (StringUtils.isEmpty(calendarShift.getCalendarShiftId())) {
                mtCalendarShiftRepo.insertSelective(calendarShift);
            } else {
                mtCalendarShiftRepo.updateByPrimaryKeySelective(calendarShift);
            }
        }

        return calendarShift.getCalendarShiftId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeCalendarShiftForUi(Long tenantId, List<String> calendarShiftIdList) {
        if (CollectionUtils.isEmpty(calendarShiftIdList)) {
            return;
        }

        List<String> sqlList = new ArrayList<>();
        MtCalendarShift mtCalendarShift;
        for (String id : calendarShiftIdList) {
            mtCalendarShift = new MtCalendarShift();
            mtCalendarShift.setTenantId(tenantId);
            mtCalendarShift.setCalendarShiftId(id);

            sqlList.addAll(customDbRepository.getDeleteSql(mtCalendarShift));
        }
        try {
            if (CollectionUtils.isNotEmpty(sqlList)) {
                jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }
        } catch (Exception ex) {
            throw new MtException("数据删除失败.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initCalendarShiftForUi(Long tenantId, MtCalendarShiftDTO5 dto) {
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDate calendarShiftStartDate = LocalDate.from(dto.getShiftStartDate().toInstant().atZone(zoneId));
        LocalDate calendarShiftEndDate = LocalDate.from(dto.getShiftEndDate().toInstant().atZone(zoneId));

        int period = (int) (calendarShiftEndDate.toEpochDay() - calendarShiftStartDate.toEpochDay()) + 1;

        // check date
        checkCalendarShiftDatePeriod(tenantId, dto.getShiftStartDate(), dto.getShiftEndDate());

        // get shift
        MtShift queryShift = new MtShift();
        queryShift.setTenantId(tenantId);
        queryShift.setShiftType(dto.getShiftType());
        List<MtShift> shiftList = mtShiftRepo.select(queryShift);
        if (CollectionUtils.isEmpty(shiftList)) {
            return;
        }

        // delete origin calendar shift info
        MtCalendarShiftVO7 calendarShiftVO = new MtCalendarShiftVO7();
        calendarShiftVO.setCalendarId(dto.getCalendarId());
        calendarShiftVO.setShiftDateFrom(dto.getShiftStartDate());
        calendarShiftVO.setShiftDateTo(dto.getShiftEndDate());
        List<String> originCalendarShiftIdList =
                        mtCalendarShiftRepo.timeLimitCalendarShiftQuery(tenantId, calendarShiftVO);
        if (CollectionUtils.isNotEmpty(originCalendarShiftIdList)) {
            mtCalendarShiftMapper.deleteByIdsCustom(tenantId, originCalendarShiftIdList);
        }

        // 批量获取新增数据ID、CID
        List<String> calendarShiftIdList = customDbRepository.getNextKeys("mt_calendar_shift_s", period * shiftList.size());
        List<String> calendarShiftCidList =
                        customDbRepository.getNextKeys("mt_calendar_shift_cid_s", period * shiftList.size());

        // 通用变量
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date();

        // init calendar shift
        List<String> calendarShiftList = new ArrayList<>();
        MtCalendarShift calendarShift;
        int count = 0;
        for (int i = 0; i < period; i++) {
            String date = calendarShiftStartDate.format(DATE_FORMATTER);
            for (MtShift shift : shiftList) {
                calendarShift = new MtCalendarShift();
                calendarShift.setCalendarShiftId(calendarShiftIdList.get(count));
                calendarShift.setCid(Long.valueOf(calendarShiftCidList.get(count)));
                calendarShift.setTenantId(tenantId);
                calendarShift.setCalendarId(dto.getCalendarId());
                calendarShift.setShiftDate(Date.from(calendarShiftStartDate.atStartOfDay().atZone(zoneId).toInstant()));
                calendarShift.setEnableFlag("Y");
                calendarShift.setShiftCode(shift.getShiftCode());
                calendarShift.setShiftStartTime(transformCalendarShiftDate(date, shift.getShiftStartTime(),null, zoneId));
                calendarShift.setShiftEndTime(transformCalendarShiftDate(date, shift.getShiftEndTime(),shift.getShiftStartTime(), zoneId));
                calendarShift.setRestTime(shift.getRestTime());
                calendarShift.setUtilizationRate(shift.getUtilizationRate());
                calendarShift.setBorrowingAbility(shift.getBorrowingAbility());
                calendarShift.setCapacityUnit(shift.getCapacityUnit());
                calendarShift.setStandardCapacity(shift.getStandardCapacity());
                calendarShift.setSequence(shift.getSequence());
                calendarShift.setCreatedBy(userId);
                calendarShift.setCreationDate(now);
                calendarShift.setLastUpdatedBy(userId);
                calendarShift.setLastUpdateDate(now);

                calendarShiftList.addAll(customDbRepository.getInsertSql(calendarShift));

                count++;
            }
            calendarShiftStartDate = calendarShiftStartDate.plusDays(1L);
        }

        if (CollectionUtils.isNotEmpty(calendarShiftList)) {
            jdbcTemplate.batchUpdate(calendarShiftList.toArray(new String[calendarShiftList.size()]));
        }
    }

    /**
     * UI复制日历班次信息-前校验
     *
     * @author chuang.yang
     * @date 2019/12/23
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    @Override
    public String copyCalendarShiftCheckForUi(Long tenantId, MtCalendarShiftDTO6 dto) {
        // 参数有效性校验
        if (StringUtils.isEmpty(dto.getTargetCalendarId())) {
            return MtBaseConstants.NO;
        }

        // delete origin calendar shift info
        String whereInValuesSql = getWhereInValuesSql("cs.CALENDAR_ID", Arrays.asList(dto.getTargetCalendarId()), 1000);
        List<String> originCalendarIdList = mtCalendarShiftMapper.limitShiftDateBatchQuery(tenantId, whereInValuesSql,
                DATE_FORMATTER.format(dto.getTargetShiftStartDate()),
                DATE_FORMATTER.format(dto.getTargetShiftEndDate()));
        if (CollectionUtils.isNotEmpty(originCalendarIdList)) {
            throw new MtException("MT_CALENDAR_0025",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0025", "CALENDAR"));
        }
        return MtBaseConstants.YES;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyCalendarShiftForUi(Long tenantId, MtCalendarShiftDTO6 dto) {
        if (StringUtils.isEmpty(dto.getTargetCalendarId())) {
            return;
        }
        // check date
        checkCalendarShiftDatePeriod(tenantId,Date.from(dto.getSourceShiftStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(dto.getSourceShiftEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        checkCalendarShiftDatePeriod(tenantId,Date.from(dto.getTargetShiftStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(dto.getTargetShiftEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        checkCalendarShiftDateInteraction(tenantId,Date.from(dto.getSourceShiftStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),Date.from(dto.getSourceShiftEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(dto.getTargetShiftStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),Date.from(dto.getTargetShiftEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // check standard type shift
        if ("STANDARD".equals(dto.getSourceCalendarType())) {
            if(dto.getTargetShiftStartDate().isBefore(LocalDate.now())){
                throw new MtException("MT_CALENDAR_0014",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0014","CALENDAR"));
            }
        }

        // check source shift
        Sqls sqls = Sqls.custom().andEqualTo(MtCalendarShift.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtCalendarShift.FIELD_CALENDAR_ID, dto.getSourceCalendarId());
        if (dto.getSourceShiftStartDate() != null) {
            sqls.andGreaterThanOrEqualTo(MtCalendarShift.FIELD_SHIFT_DATE, dto.getSourceShiftStartDate());
        }
        if (dto.getSourceShiftEndDate() != null) {
            sqls.andLessThanOrEqualTo(MtCalendarShift.FIELD_SHIFT_DATE, dto.getSourceShiftEndDate());
        }

        List<MtCalendarShift> sourceCalendarShiftList = mtCalendarShiftRepo
                .selectByCondition(Condition.builder(MtCalendarShift.class).andWhere(sqls).build());

        if (CollectionUtils.isEmpty(sourceCalendarShiftList)) {
            throw new MtException("MT_CALENDAR_0013",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0013","CALENDAR"));
        }


        // get source calendar shift
        Map<Date, List<MtCalendarShift>> calendarShiftPerDate = sourceCalendarShiftList.stream().collect(
                        Collectors.groupingBy(MtCalendarShift::getShiftDate, TreeMap::new, Collectors.toList()));

        // delete origin calendar shift info
        String whereInValuesSql = getWhereInValuesSql("cs.CALENDAR_ID", Arrays.asList(dto.getTargetCalendarId()), 1000);

        List<String> originCalendarIdList = mtCalendarShiftMapper.limitShiftDateBatchQuery(tenantId, whereInValuesSql,
                DATE_FORMATTER.format(dto.getTargetShiftStartDate()),DATE_FORMATTER.format(dto.getTargetShiftEndDate()));
        if (CollectionUtils.isNotEmpty(originCalendarIdList)) {
            mtCalendarShiftMapper.deleteByIdsCustom(tenantId, originCalendarIdList);
        }

        LocalDate srcCalendarShiftStartDate = dto.getSourceShiftStartDate();
        LocalDate srcCalendarShiftEndDate = dto.getSourceShiftEndDate();
        LocalDate tgtCalendarShiftStartDate = dto.getTargetShiftStartDate();
        LocalDate tgtCalendarShiftEndDate = dto.getTargetShiftEndDate();

        // 复制区间
        int srcPeriod = (int)Math.abs((srcCalendarShiftEndDate.toEpochDay() - srcCalendarShiftStartDate.toEpochDay())) + 1;
        int tgtPeriod = (int)Math.abs((tgtCalendarShiftEndDate.toEpochDay() - tgtCalendarShiftStartDate.toEpochDay())) + 1;


        List<String> calendarShiftList = new ArrayList<>();
        MtCalendarShift calendarShift;

        // 批量获取新增数据ID、CID
        List<String> calendarShiftIdList =
                        customDbRepository.getNextKeys("mt_calendar_shift_s", tgtPeriod * sourceCalendarShiftList.size());
        List<String> calendarShiftCidList = customDbRepository.getNextKeys("mt_calendar_shift_cid_s",
                        tgtPeriod * sourceCalendarShiftList.size());

        // 通用变量
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date();
        int count = 0;

        // 记录当前处理日期
        Calendar curTargetDate = Calendar.getInstance();
        curTargetDate.setTime(Date.from(dto.getTargetShiftStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Calendar curSourceDate = Calendar.getInstance();
        curSourceDate.setTime(Date.from(dto.getSourceShiftStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        for (int i = 0; i < tgtPeriod; i++) {
            // 如果目标天数大于来源天数，循环复制
            if (tgtPeriod > srcPeriod && i % srcPeriod == 0) {
                // 当前达到来源天数时，重置来源日期为来源起始日期
                curSourceDate.setTime(Date.from(dto.getSourceShiftStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }

            // 查询当前目标日期是否有日历班次信息，如果有则复制给目标日期
            List<MtCalendarShift> mtCalendarShifts = calendarShiftPerDate.get(curSourceDate.getTime());

            LocalDate curSourceLocalDate = curSourceDate.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate curTargetLocalDate = curTargetDate.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            //计算来源日期和目标日期的天数偏移量
            long offset = curTargetLocalDate.toEpochDay() - curSourceLocalDate.toEpochDay();
//            long offset = Period.between(curSourceLocalDate,curTargetLocalDate).getDays();
            if (CollectionUtils.isNotEmpty(mtCalendarShifts)) {
                for (MtCalendarShift originCalendarShift : mtCalendarShifts) {
                    //计算目标的日期的班次开始时间和结束时间
                    Calendar shiftTimeCalendar = Calendar.getInstance();
                    shiftTimeCalendar.setTime(originCalendarShift.getShiftStartTime());
                    shiftTimeCalendar.add(Calendar.DAY_OF_MONTH,(int) offset);
                    Date targetShiftStartTime = shiftTimeCalendar.getTime();
                    shiftTimeCalendar.setTime(originCalendarShift.getShiftEndTime());
                    shiftTimeCalendar.add(Calendar.DAY_OF_MONTH,(int) offset);
                    Date targetShiftEndTime = shiftTimeCalendar.getTime();

                    calendarShift = new MtCalendarShift();
                    calendarShift.setCalendarShiftId(calendarShiftIdList.get(count));
                    calendarShift.setCid(Long.valueOf(calendarShiftCidList.get(count)));
                    calendarShift.setTenantId(tenantId);
                    calendarShift.setCalendarId(dto.getTargetCalendarId());
                    calendarShift.setShiftDate(curTargetDate.getTime());
                    calendarShift.setEnableFlag("Y");
                    calendarShift.setShiftCode(originCalendarShift.getShiftCode());
                    calendarShift.setShiftStartTime(targetShiftStartTime);
                    calendarShift.setShiftEndTime(targetShiftEndTime);
                    calendarShift.setRestTime(originCalendarShift.getRestTime());
                    calendarShift.setBorrowingAbility(originCalendarShift.getBorrowingAbility());
                    calendarShift.setSequence(originCalendarShift.getSequence());
                    calendarShift.setCreatedBy(userId);
                    calendarShift.setCreationDate(now);
                    calendarShift.setLastUpdatedBy(userId);
                    calendarShift.setLastUpdateDate(now);

                    calendarShiftList.addAll(customDbRepository.getInsertSql(calendarShift));

                    count++;
                }
            }

            // 加一天
            curTargetDate.add(Calendar.DAY_OF_MONTH, 1);
            curSourceDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        if (CollectionUtils.isNotEmpty(calendarShiftList)) {
            jdbcTemplate.batchUpdate(calendarShiftList.toArray(new String[calendarShiftList.size()]));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void newInitCalendarShiftForUi(Long tenantId, MtCalendarShiftDTO5 dto) {
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDate calendarShiftStartDate = LocalDate.from(dto.getShiftStartDate().toInstant().atZone(zoneId));
        LocalDate calendarShiftEndDate = LocalDate.from(dto.getShiftEndDate().toInstant().atZone(zoneId));

        int period = (int) (calendarShiftEndDate.toEpochDay() - calendarShiftStartDate.toEpochDay()) + 1;

        // check date
        checkCalendarShiftDatePeriod(tenantId, dto.getShiftStartDate(), dto.getShiftEndDate());

        // get shift
        MtShift queryShift = new MtShift();
        queryShift.setTenantId(tenantId);
        queryShift.setShiftType(dto.getShiftType());
        List<MtShift> shiftList = mtShiftRepo.select(queryShift);
        if (CollectionUtils.isEmpty(shiftList)) {
            return;
        }

        // delete origin calendar shift info
        MtCalendarShiftVO7 calendarShiftVO = new MtCalendarShiftVO7();
        calendarShiftVO.setCalendarId(dto.getCalendarId());
        calendarShiftVO.setShiftDateFrom(dto.getShiftStartDate());
        calendarShiftVO.setShiftDateTo(dto.getShiftEndDate());
        List<String> originCalendarShiftIdList = mtCalendarShiftRepo.timeLimitCalendarShiftQuery(tenantId, calendarShiftVO);
        if (CollectionUtils.isNotEmpty(originCalendarShiftIdList)) {
            //校验派工数量
            List<MtCalendarShift> mtCalendarShiftList = mtCalendarShiftMapper.queryCalendarShiftDispatchQty(tenantId, originCalendarShiftIdList);
            Map<String, List<MtCalendarShift>> mtCalendarShiftMap = mtCalendarShiftList.stream().collect(Collectors.groupingBy(shift -> shift.getCalendarShiftId()));
            SimpleDateFormat shiftDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (String calendarShiftId : originCalendarShiftIdList) {
                List<MtCalendarShift> mtCalendarShifts = mtCalendarShiftMap.get(calendarShiftId);
                if(CollectionUtils.isNotEmpty(mtCalendarShifts)){
                    throw new MtException("HME_WO_DISPATCH_0004", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "HME_WO_DISPATCH_0004", "HME", shiftDateFormat.format(mtCalendarShifts.get(0).getShiftDate()), mtCalendarShifts.get(0).getShiftCode()));
                }
            }
            mtCalendarShiftMapper.deleteByIdsCustom(tenantId, originCalendarShiftIdList);
        }

        // 批量获取新增数据ID、CID
        List<String> calendarShiftIdList = customDbRepository.getNextKeys("mt_calendar_shift_s", period * shiftList.size());
        List<String> calendarShiftCidList =
                customDbRepository.getNextKeys("mt_calendar_shift_cid_s", period * shiftList.size());

        // 通用变量
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date();

        // init calendar shift
        List<String> calendarShiftList = new ArrayList<>();
        MtCalendarShift calendarShift;
        int count = 0;
        for (int i = 0; i < period; i++) {
            String date = calendarShiftStartDate.format(DATE_FORMATTER);
            for (MtShift shift : shiftList) {
                calendarShift = new MtCalendarShift();
                calendarShift.setCalendarShiftId(calendarShiftIdList.get(count));
                calendarShift.setCid(Long.valueOf(calendarShiftCidList.get(count)));
                calendarShift.setTenantId(tenantId);
                calendarShift.setCalendarId(dto.getCalendarId());
                calendarShift.setShiftDate(Date.from(calendarShiftStartDate.atStartOfDay().atZone(zoneId).toInstant()));
                calendarShift.setEnableFlag("Y");
                calendarShift.setShiftCode(shift.getShiftCode());
                calendarShift.setShiftStartTime(transformCalendarShiftDate(date, shift.getShiftStartTime(),null, zoneId));
                calendarShift.setShiftEndTime(transformCalendarShiftDate(date, shift.getShiftEndTime(),shift.getShiftStartTime(), zoneId));
                calendarShift.setRestTime(shift.getRestTime());
                calendarShift.setUtilizationRate(shift.getUtilizationRate());
                calendarShift.setBorrowingAbility(shift.getBorrowingAbility());
                calendarShift.setCapacityUnit(shift.getCapacityUnit());
                calendarShift.setStandardCapacity(shift.getStandardCapacity());
                calendarShift.setSequence(shift.getSequence());
                calendarShift.setCreatedBy(userId);
                calendarShift.setCreationDate(now);
                calendarShift.setLastUpdatedBy(userId);
                calendarShift.setLastUpdateDate(now);

                calendarShiftList.addAll(customDbRepository.getInsertSql(calendarShift));

                count++;
            }
            calendarShiftStartDate = calendarShiftStartDate.plusDays(1L);
        }

        if (CollectionUtils.isNotEmpty(calendarShiftList)) {
            jdbcTemplate.batchUpdate(calendarShiftList.toArray(new String[calendarShiftList.size()]));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void newRemoveCalendarShiftForUi(Long tenantId, List<String> calendarShiftList) {
        if (CollectionUtils.isEmpty(calendarShiftList)) {
            return;
        }
        List<MtCalendarShift> mtCalendarShiftList = mtCalendarShiftMapper.queryCalendarShiftDispatchQty(tenantId, calendarShiftList);
        Map<String, List<MtCalendarShift>> mtCalendarShiftMap = mtCalendarShiftList.stream().collect(Collectors.groupingBy(dto -> dto.getCalendarShiftId()));
        SimpleDateFormat shiftDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<String> sqlList = new ArrayList<>();
        MtCalendarShift mtCalendarShift;
        for (String id : calendarShiftList) {
            //校验派工数量
            List<MtCalendarShift> mtCalendarShifts = mtCalendarShiftMap.get(id);
            if(CollectionUtils.isNotEmpty(mtCalendarShifts)){
                throw new MtException("HME_WO_DISPATCH_0004", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                        "HME_WO_DISPATCH_0004", "HME", shiftDateFormat.format(mtCalendarShifts.get(0).getShiftDate()), mtCalendarShifts.get(0).getShiftCode()));
            }

            mtCalendarShift = new MtCalendarShift();
            mtCalendarShift.setTenantId(tenantId);
            mtCalendarShift.setCalendarShiftId(id);

            sqlList.addAll(customDbRepository.getDeleteSql(mtCalendarShift));
        }
        try {
            if (CollectionUtils.isNotEmpty(sqlList)) {
                jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }
        } catch (Exception ex) {
            throw new MtException("数据删除失败.");
        }
    }

    /**
     * 校验工作日历时间间隔
     * 
     * 时间间隔不能大于1年
     * 
     * @author benjamin
     * @date 2019/9/29 1:09 PM
     * @param tenantId 租户Id
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    private void checkCalendarShiftDatePeriod(Long tenantId, Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startDate);
        end.setTime(endDate);
        start.add(Calendar.YEAR, 1);
        if (end.compareTo(start) > 0) {
            throw new MtException("MT_CALENDAR_0017",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0017", "CALENDAR"));
        }
    }

    /**
     * 校验工作日历时间重叠
     * 
     * @author benjamin
     * @date 2019/9/29 2:37 PM
     * @param tenantId 租户Id
     * @param srcDateStart 来源开始日期
     * @param srcDateEnd 来源结束日期
     * @param tgtDateStart 目标开始日期
     * @param tgtDateEnd 目标结束日期
     */
    private void checkCalendarShiftDateInteraction(Long tenantId, Date srcDateStart, Date srcDateEnd, Date tgtDateStart,
                    Date tgtDateEnd) {
        if (tgtDateStart.after(srcDateStart) && tgtDateStart.before(srcDateEnd)) {
            throw new MtException("MT_CALENDAR_0012",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0012", "CALENDAR"));
        } else if (tgtDateEnd.after(srcDateStart) && tgtDateEnd.before(srcDateEnd)) {
            throw new MtException("MT_CALENDAR_0012",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0012", "CALENDAR"));
        } else if (tgtDateStart.before(srcDateStart) && tgtDateEnd.after(srcDateEnd)) {
            throw new MtException("MT_CALENDAR_0012",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0012", "CALENDAR"));
        }
    }

    /**
     * 工作日历班次时间转换
     * 
     * @author benjamin
     * @date 2019/9/29 11:26 AM
     * @param date 日期
     * @param time 时间
     * @param zoneId ZoneId
     * @return Date
     */
    private Date transformCalendarShiftDate(String date, String time, ZoneId zoneId) {
        String dateTimeStr = date + " " + time;
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);

        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    /**
     * 工作日历班次时间转换（修改）
     *
     * @param relativeTime 相对时间
     * @param zoneId ZoneId
     * @author sanfeng.zhang@hand-china.com 2020/8/11 13:53
     * @return java.util.Date
     */
    private Date transformCalendarShiftDate(String date, String time, String relativeTime, ZoneId zoneId) {
        //如果没传相对时间则只是拼接时间
        if (StringUtils.isEmpty(relativeTime)) {
            String dateTimeStr = date + " " + time;
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
            return Date.from(localDateTime.atZone(zoneId).toInstant());
        }

        //如果传了相对时间则要当前时间与相对时间的偏移量。若相对时间小于结束时间，则日期没有变化，否则日期+1
        LocalTime localTime = LocalTime.parse(time, TIME_FORMATTER);
        LocalTime relativeLocalTime = LocalTime.parse(relativeTime, TIME_FORMATTER);
        long offset = localTime.compareTo(relativeLocalTime) > 0 ? 0 : 1;
        String dateTimeStr = date + " " + time;
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
        localDateTime = localDateTime.plusDays(offset);
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

}
