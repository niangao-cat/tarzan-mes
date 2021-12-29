package tarzan.calendar.infra.repository.impl;

import static io.tarzan.common.domain.util.StringHelper.getWhereInValuesSql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.vo.MtWkcShiftVO3;
import tarzan.calendar.domain.entity.MtCalendar;
import tarzan.calendar.domain.entity.MtCalendarShift;
import tarzan.calendar.domain.repository.MtCalendarRepository;
import tarzan.calendar.domain.repository.MtCalendarShiftRepository;
import tarzan.calendar.domain.vo.*;
import tarzan.calendar.infra.mapper.MtCalendarMapper;
import tarzan.calendar.infra.mapper.MtCalendarShiftMapper;

/**
 * 工作日历班次 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:15
 */
@Component
public class MtCalendarShiftRepositoryImpl extends BaseRepositoryImpl<MtCalendarShift>
                implements MtCalendarShiftRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtCalendarRepository mtCalendarRepository;

    @Autowired
    private MtCalendarShiftMapper mtCalendarShiftMapper;

    @Autowired
    private MtCalendarMapper mtCalendarMapper;

    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;

    @Autowired
    private MtCalendarShiftRepository mtCalendarShiftRepository;

    @Override
    public MtCalendarShift calendarShiftGet(Long tenantId, String calendarShiftId) {
        if (StringUtils.isEmpty(calendarShiftId)) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "calendarShiftId", "【API：calendarShiftGet】"));
        }

        MtCalendarShift mtCalendarShift = new MtCalendarShift();
        mtCalendarShift.setTenantId(tenantId);
        mtCalendarShift.setCalendarShiftId(calendarShiftId);
        return mtCalendarShiftMapper.selectOne(mtCalendarShift);
    }

    @Override
    public List<MtCalendarShift> calendarShiftBatchGet(Long tenantId, List<String> calendarShiftIdList) {

        if (CollectionUtils.isEmpty(calendarShiftIdList)) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "calendarShiftIdList", "【API：calendarShiftBatchGet】"));
        }
        return mtCalendarShiftMapper.calendarShiftBatchGet(tenantId, calendarShiftIdList);
    }

    @Override
    public List<String> calendarLimitShiftQuery(Long tenantId, MtCalendarShiftVO2 vo) {
        if (StringUtils.isEmpty(vo.getCalendarId())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "calendarId", "【API：calendarLimitShiftQuery】"));
        }
        return mtCalendarShiftMapper.calendarLimitShiftQuery(tenantId, vo);
    }

    @Override
    public String calendarShiftAvailableValidate(Long tenantId, String calendarShiftId) {

        if (StringUtils.isEmpty(calendarShiftId)) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "calendarShiftId", "【API：calendarShiftAvailableValidate】"));
        }

        MtCalendarShift mtCalendarShift = new MtCalendarShift();
        mtCalendarShift.setTenantId(tenantId);
        mtCalendarShift.setCalendarShiftId(calendarShiftId);
        mtCalendarShift = mtCalendarShiftMapper.selectOne(mtCalendarShift);

        if (null == mtCalendarShift) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "calendarShiftId", "【API：calendarShiftAvailableValidate】"));
        }
        return mtCalendarShift.getEnableFlag();
    }

    @Override
    public List<String> timeLimitCalendarShiftQuery(Long tenantId, MtCalendarShiftVO7 vo) {
        if (StringUtils.isEmpty(vo.getCalendarId())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "calendarId", "【API：timeLimitCalendarShiftQuery】"));
        }
        return mtCalendarShiftMapper.timeLimitCalendarShiftQuery(tenantId, vo);
    }

    @Override
    public List<String> timeLimitAvailableCalendarShiftQuery(Long tenantId, MtCalendarShiftVO7 vo) {
        if (StringUtils.isEmpty(vo.getCalendarId())) {
            throw new MtException("MT_CALENDAR_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0001", "CALENDAR",
                                            "calendarId", "【API：timeLimitAvailableCalendarShiftQuery】"));
        }
        return mtCalendarShiftMapper.timeLimitAvailableCalendarShiftQuery(tenantId, vo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void calendarShiftCopy(Long tenantId, MtCalendarShiftVO1 vo) {
        if (StringUtils.isEmpty(vo.getCalendarId())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "calendarId", "【API：calendarShiftCopy】"));
        }
        if (StringUtils.isEmpty(vo.getTargetCalendarId())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "targetCalendarId", "【API：calendarShiftCopy】"));
        }

        // 获取复制：源日历班次信息
        MtCalendarShiftVO7 limitShiftDateVO = new MtCalendarShiftVO7();
        limitShiftDateVO.setCalendarId(vo.getCalendarId());
        limitShiftDateVO.setShiftDateFrom(vo.getShiftDateFrom());
        limitShiftDateVO.setShiftDateTo(vo.getShiftDateTo());

        List<MtCalendarShift> sourceCalendarShiftList =
                        mtCalendarShiftMapper.limitShiftDateQuery(tenantId, limitShiftDateVO);
        if (CollectionUtils.isEmpty(sourceCalendarShiftList)) {
            throw new MtException("MT_CALENDAR_0002", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0002", "CALENDAR", "calendarId", "【API：calendarShiftCopy】"));
        }

        // 获取复制：目标日历班次信息
        limitShiftDateVO = new MtCalendarShiftVO7();
        limitShiftDateVO.setCalendarId(vo.getTargetCalendarId());
        limitShiftDateVO.setShiftDateFrom(vo.getShiftDateFrom());
        limitShiftDateVO.setShiftDateTo(vo.getShiftDateTo());
        List<MtCalendarShift> targetCalandarShiftList =
                        mtCalendarShiftMapper.limitShiftDateQuery(tenantId, limitShiftDateVO);
        if (CollectionUtils.isEmpty(targetCalandarShiftList)) {
            throw new MtException("MT_CALENDAR_0002", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0002", "CALENDAR", "targetCalendarId", "【API：calendarShiftCopy】"));
        }

        // 删除目标日历班次信息
        self().batchDelete(targetCalandarShiftList);

        // 复制源数据，替换calendarId为targetCalendarId
        for (MtCalendarShift mtCalendarShift : sourceCalendarShiftList) {
            mtCalendarShift.setTenantId(tenantId);
            mtCalendarShift.setCalendarId(vo.getTargetCalendarId());
            mtCalendarShift.setCalendarShiftId(null);

            self().insertSelective(mtCalendarShift);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void calendarShiftBatchCopy(Long tenantId, MtCalendarShiftVO6 dto) {
        if (StringUtils.isEmpty(dto.getCalendarId())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "calendarId", "【API:calendarShiftBatchCopy】"));
        }
        if (CollectionUtils.isEmpty(dto.getTargetCalendarIds())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "targetCalendarId", "【API:calendarShiftBatchCopy】"));
        }

        MtCalendar mtCalendar = new MtCalendar();
        mtCalendar.setTenantId(tenantId);
        mtCalendar.setCalendarId(dto.getCalendarId());
        mtCalendar = this.mtCalendarMapper.selectOne(mtCalendar);
        if (null == mtCalendar) {
            throw new MtException("MT_CALENDAR_0002", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0002", "CALENDAR", dto.getCalendarId(), "【API:calendarShiftBatchCopy】"));
        }

        List<MtCalendar> mtCalendars = this.mtCalendarMapper.selectByIdsCustom(tenantId, dto.getTargetCalendarIds());
        if (CollectionUtils.isEmpty(mtCalendars)) {
            throw new MtException("MT_CALENDAR_0002",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0002", "CALENDAR",
                                            dto.getTargetCalendarIds().toString(), "【API:calendarShiftBatchCopy】"));
        }

        MtCalendarShiftVO7 limitShiftDateVO = new MtCalendarShiftVO7();
        limitShiftDateVO.setCalendarId(dto.getCalendarId());
        limitShiftDateVO.setShiftDateFrom(dto.getDateFrom());
        limitShiftDateVO.setShiftDateTo(dto.getDateTo());
        List<MtCalendarShift> mtCalendarShifts =
                        this.mtCalendarShiftMapper.limitShiftDateQuery(tenantId, limitShiftDateVO);
        if (CollectionUtils.isEmpty(mtCalendarShifts)) {
            throw new MtException("MT_CALENDAR_0020", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0020", "CALENDAR", dto.getCalendarId(), "【API:calendarShiftBatchCopy】"));
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateFrom = null == dto.getDateFrom() ? null : format.format(dto.getDateFrom());
        String dateTo = null == dto.getDateTo() ? null : format.format(dto.getDateTo());

        String whereInValuesSql = getWhereInValuesSql("cs.CALENDAR_ID", dto.getTargetCalendarIds(), 1000);
        List<String> calendarShiftIds = this.mtCalendarShiftMapper.limitShiftDateBatchQuery(tenantId, whereInValuesSql,
                        dateFrom, dateTo);
        if (CollectionUtils.isNotEmpty(calendarShiftIds)) {
            this.mtCalendarShiftMapper.deleteByIdsCustom(tenantId, calendarShiftIds);
        }

        Date now = new Date();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        List<String> sqlList = new ArrayList<String>(mtCalendarShifts.size());
        for (String targetCalendarId : dto.getTargetCalendarIds()) {
            for (MtCalendarShift mtCalendarShift : mtCalendarShifts) {
                MtCalendarShift tmpMtCalendarShift = new MtCalendarShift();
                BeanUtils.copyProperties(mtCalendarShift, tmpMtCalendarShift);
                tmpMtCalendarShift.setTenantId(tenantId);
                tmpMtCalendarShift.setCalendarShiftId(customDbRepository.getNextKey("mt_calendar_shift_s"));
                tmpMtCalendarShift.setCid(Long.valueOf(customDbRepository.getNextKey("mt_calendar_shift_cid_s")));
                tmpMtCalendarShift.setCalendarId(targetCalendarId);
                tmpMtCalendarShift.setCreationDate(now);
                tmpMtCalendarShift.setCreatedBy(userId);
                tmpMtCalendarShift.setLastUpdateDate(now);
                tmpMtCalendarShift.setLastUpdatedBy(userId);
                tmpMtCalendarShift.setObjectVersionNumber(1L);
                sqlList.addAll(customDbRepository.getInsertSql(tmpMtCalendarShift));
            }
        }

        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
    }

    @Override
    public MtCalendarShift calendarPreviousShiftGet(Long tenantId, MtCalendarShiftVO dto) {
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "workcellId", "【API：calendarPreviousShiftGet】"));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "shiftDate", "【API：calendarPreviousShiftGet】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "shiftCode", "【API：calendarPreviousShiftGet】"));
        }

        // 获取日历ID
        MtCalendarVO2 orgSiteCalendarVO = new MtCalendarVO2();
        orgSiteCalendarVO.setOrganizationId(dto.getWorkcellId());
        orgSiteCalendarVO.setOrganizationType("WORKCELL");
        orgSiteCalendarVO.setSiteType("MANUFACTURING");
        if (StringUtils.isNotEmpty(dto.getCalendarType())) {
            orgSiteCalendarVO.setCalendarType(dto.getCalendarType());
        } else {
            orgSiteCalendarVO.setCalendarType("STANDARD");
        }

        String calendarId = mtCalendarRepository.organizationLimitOnlyCalendarGet(tenantId, orgSiteCalendarVO);

        if (StringUtils.isEmpty(calendarId)) {
            throw new MtException("MT_CALENDAR_0004", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0004", "CALENDAR", "【API：calendarPreviousShiftGet】"));
        }

        // 校验当前班次是否有值
        MtCalendarShift calendarShift = new MtCalendarShift();
        calendarShift.setTenantId(tenantId);
        calendarShift.setCalendarId(calendarId);
        calendarShift.setShiftDate(dto.getShiftDate());
        calendarShift.setShiftCode(dto.getShiftCode());
        calendarShift.setEnableFlag("Y");
        calendarShift = mtCalendarShiftMapper.selectOne(calendarShift);

        if (calendarShift == null || StringUtils.isEmpty(calendarShift.getCalendarShiftId())) {
            throw new MtException("MT_CALENDAR_0005", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0005", "CALENDAR", "【API：calendarPreviousShiftGet】"));
        }

        // 获取sequence
        Long sequence = calendarShift.getSequence();

        // 获取小于当前日历下所有有效班次
        calendarShift = new MtCalendarShift();
        calendarShift.setTenantId(tenantId);
        calendarShift.setCalendarId(calendarId);
        calendarShift.setEnableFlag("Y");
        calendarShift.setShiftDate(dto.getShiftDate());

        Criteria criteria = new Criteria(calendarShift);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtCalendarShift.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtCalendarShift.FIELD_CALENDAR_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtCalendarShift.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        whereFields.add(new WhereField(MtCalendarShift.FIELD_SHIFT_DATE, Comparison.LESS_THAN_OR_EQUALTO));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        List<MtCalendarShift> calendarShiftList = mtCalendarShiftMapper.selectOptional(calendarShift, criteria);

        // 1. 在当前班次找：筛选当前班次内小于当前班次sequence的班次信息
        List<MtCalendarShift> currentShiftDate = calendarShiftList.stream()
                        .filter(p -> p.getShiftDate().equals(dto.getShiftDate()) && p.getSequence() < sequence)
                        .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(currentShiftDate)) {
            // 如果存在，则返回sequence最大值的数据
            currentShiftDate.sort(Comparator.comparingLong(MtCalendarShift::getSequence).reversed());
            return currentShiftDate.get(0);
        }

        // 2. 在之前班次找：筛选时间小于当前班次时间的班次信息
        List<MtCalendarShift> beforeShiftDate = calendarShiftList.stream()
                        .filter(p -> p.getShiftDate().before(dto.getShiftDate())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(beforeShiftDate)) {
            // 如果存在，则返回班次时间最大，sequence最大的数据
            beforeShiftDate.sort(Comparator.comparing(MtCalendarShift::getShiftDate)
                            .thenComparing(MtCalendarShift::getSequence).reversed());

            return beforeShiftDate.get(0);
        } else {
            throw new MtException("MT_CALENDAR_0006", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0006", "CALENDAR", "【API：calendarPreviousShiftGet】"));
        }
    }

    @Override
    public MtCalendarShift calendarNextShiftGet(Long tenantId, MtCalendarShiftVO dto) {
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "workcellId", "【API：calendarNextShiftGet】"));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "shiftDate", "【API：calendarNextShiftGet】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "shiftCode", "【API：calendarNextShiftGet】"));
        }
        MtCalendarVO2 orgSiteCalendarVO = new MtCalendarVO2();
        orgSiteCalendarVO.setOrganizationId(dto.getWorkcellId());
        orgSiteCalendarVO.setOrganizationType("WORKCELL");
        orgSiteCalendarVO.setSiteType("MANUFACTURING");
        if (StringUtils.isNotEmpty(dto.getCalendarType())) {
            orgSiteCalendarVO.setCalendarType(dto.getCalendarType());
        } else {
            orgSiteCalendarVO.setCalendarType("STANDARD");
        }

        String calendarId = this.mtCalendarRepository.organizationLimitOnlyCalendarGet(tenantId, orgSiteCalendarVO);
        if (StringUtils.isEmpty(calendarId)) {
            throw new MtException("MT_CALENDAR_0004", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0004", "CALENDAR", "【API：calendarNextShiftGet】"));
        }

        // 校验当前班次是否有值
        MtCalendarShift calendarShift = new MtCalendarShift();
        calendarShift.setTenantId(tenantId);
        calendarShift.setCalendarId(calendarId);
        calendarShift.setShiftDate(dto.getShiftDate());
        calendarShift.setShiftCode(dto.getShiftCode());
        calendarShift.setEnableFlag("Y");
        calendarShift = mtCalendarShiftMapper.selectOne(calendarShift);

        if (calendarShift == null || StringUtils.isEmpty(calendarShift.getCalendarShiftId())) {
            throw new MtException("MT_CALENDAR_0005", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0005", "CALENDAR", "【API：calendarNextShiftGet】"));
        }

        // 获取sequence
        Long sequence = calendarShift.getSequence();
        calendarShift = new MtCalendarShift();
        calendarShift.setTenantId(tenantId);
        calendarShift.setCalendarId(calendarId);
        calendarShift.setEnableFlag("Y");
        calendarShift.setShiftDate(dto.getShiftDate());

        Criteria criteria = new Criteria(calendarShift);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtCalendarShift.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtCalendarShift.FIELD_CALENDAR_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtCalendarShift.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        whereFields.add(new WhereField(MtCalendarShift.FIELD_SHIFT_DATE, Comparison.GREATER_THAN_OR_EQUALTO));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        List<MtCalendarShift> calendarShiftList = mtCalendarShiftMapper.selectOptional(calendarShift, criteria);

        List<MtCalendarShift> currentShiftDate = calendarShiftList.stream()
                        .filter(p -> p.getShiftDate().equals(dto.getShiftDate()) && p.getSequence() > sequence)
                        .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(currentShiftDate)) {
            currentShiftDate.sort(Comparator.comparingLong(MtCalendarShift::getSequence));
            return currentShiftDate.get(0);
        }

        List<MtCalendarShift> afterShiftDate = calendarShiftList.stream()
                        .filter(p -> p.getShiftDate().after(dto.getShiftDate())).collect(Collectors.toList());
        // 逻辑修改没有数据则返回为空
        if (CollectionUtils.isEmpty(afterShiftDate)) {
            return null;
        }

        afterShiftDate.sort(Comparator.comparing(MtCalendarShift::getShiftDate)
                        .thenComparing(MtCalendarShift::getSequence));
        return afterShiftDate.get(0);
    }

    @Override
    public List<MtCalendarShiftVO4> calendarShiftLimitNearShiftQuery(Long tenantId, MtCalendarShiftVO3 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "workcellId", "【API:calendarShiftLimitNearShiftQuery】"));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "shiftDate", "【API:calendarShiftLimitNearShiftQuery】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "shiftCode", "【API:calendarShiftLimitNearShiftQuery】"));
        }
        if (dto.getForwardPeriod() == null && dto.getBackwardPeriod() == null) {
            throw new MtException("MT_CALENDAR_0011", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0011", "CALENDAR", "【API:calendarShiftLimitNearShiftQuery】"));
        }
        if (StringUtils.isEmpty(dto.getCalendarType())) {
            // 未输入时默认约束为“STANDARD”类型
            dto.setCalendarType("STANDARD");
        }

        List<MtCalendarShiftVO4> resultList = new ArrayList<>();

        // 2. 获取当前班次
        MtCalendarVO2 orgSiteCalendarVO = new MtCalendarVO2();
        orgSiteCalendarVO.setOrganizationId(dto.getWorkcellId());
        orgSiteCalendarVO.setOrganizationType("WORKCELL");
        orgSiteCalendarVO.setSiteType("MANUFACTURING");
        orgSiteCalendarVO.setCalendarType(dto.getCalendarType());
        String calendarId = mtCalendarRepository.organizationLimitOnlyCalendarGet(tenantId, orgSiteCalendarVO);

        MtCalendarShift currentCalendarShift = new MtCalendarShift();
        currentCalendarShift.setTenantId(tenantId);
        currentCalendarShift.setShiftDate(dto.getShiftDate());
        currentCalendarShift.setShiftCode(dto.getShiftCode());
        currentCalendarShift.setCalendarId(calendarId == null ? "" : calendarId);
        currentCalendarShift.setEnableFlag("Y");
        currentCalendarShift = mtCalendarShiftMapper.selectOne(currentCalendarShift);
        if (currentCalendarShift == null || StringUtils.isEmpty(currentCalendarShift.getCalendarShiftId())) {
            throw new MtException("MT_CALENDAR_0005", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0005", "CALENDAR", "【API:calendarShiftLimitNearShiftQuery】"));
        }

        MtCalendarShiftVO4 curResult = new MtCalendarShiftVO4();
        curResult.setCalendarShiftId(currentCalendarShift.getCalendarShiftId());
        curResult.setShiftCode(currentCalendarShift.getShiftCode());
        curResult.setShiftDate(currentCalendarShift.getShiftDate());
        resultList.add(curResult);

        // 3. 如果 ForwardPeriod 有值，获取指定班次前n个班次
        if (dto.getForwardPeriod() != null) {
            MtCalendarShiftVO tempVo = new MtCalendarShiftVO();
            tempVo.setWorkcellId(dto.getWorkcellId());
            tempVo.setShiftDate(dto.getShiftDate());
            tempVo.setShiftCode(dto.getShiftCode());
            tempVo.setCalendarType(dto.getCalendarType());

            for (int i = 0; i < dto.getForwardPeriod(); i++) {
                MtCalendarShift mtCalendarShift = calendarPreviousShiftGet(tenantId, tempVo);

                if (mtCalendarShift == null || StringUtils.isEmpty(mtCalendarShift.getCalendarShiftId())) {
                    break;
                } else {
                    MtCalendarShiftVO4 result = new MtCalendarShiftVO4();
                    result.setCalendarShiftId(mtCalendarShift.getCalendarShiftId());
                    result.setShiftCode(mtCalendarShift.getShiftCode());
                    result.setShiftDate(mtCalendarShift.getShiftDate());
                    resultList.add(result);

                    // 赋值为上一班次，继续向上查询
                    tempVo.setShiftDate(mtCalendarShift.getShiftDate());
                    tempVo.setShiftCode(mtCalendarShift.getShiftCode());
                }
            }
        }

        // 4. 如果 BackwardPeriod 有值，获取指定班次后n个班次
        if (dto.getBackwardPeriod() != null) {
            MtCalendarShiftVO tempVo = new MtCalendarShiftVO();
            tempVo.setWorkcellId(dto.getWorkcellId());
            tempVo.setShiftDate(dto.getShiftDate());
            tempVo.setShiftCode(dto.getShiftCode());
            tempVo.setCalendarType(dto.getCalendarType());

            for (int i = 0; i < dto.getBackwardPeriod(); i++) {
                MtCalendarShift mtCalendarShift = calendarNextShiftGet(tenantId, tempVo);

                if (mtCalendarShift == null) {
                    break;
                } else {
                    MtCalendarShiftVO4 result = new MtCalendarShiftVO4();
                    result.setCalendarShiftId(mtCalendarShift.getCalendarShiftId());
                    result.setShiftCode(mtCalendarShift.getShiftCode());
                    result.setShiftDate(mtCalendarShift.getShiftDate());
                    resultList.add(result);

                    // 赋值为下一班次，继续向下查询
                    tempVo.setShiftDate(mtCalendarShift.getShiftDate());
                    tempVo.setShiftCode(mtCalendarShift.getShiftCode());
                }
            }
        }

        return resultList;
    }

    @Override
    public String organizationAndShiftLimitCalendarShiftGet(Long tenantId, MtCalendarShiftVO5 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_CALENDAR_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0001", "CALENDAR",
                                            "organizationType", "【API:organizationAndShiftLimitCalendarShiftGet】"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationId())) {
            throw new MtException("MT_CALENDAR_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0001", "CALENDAR",
                                            "organizationId", "【API:organizationAndShiftLimitCalendarShiftGet】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_CALENDAR_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0001", "CALENDAR",
                                            "shiftCode", "【API:organizationAndShiftLimitCalendarShiftGet】"));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_CALENDAR_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0001", "CALENDAR",
                                            "shiftDate", "【API:organizationAndShiftLimitCalendarShiftGet】"));
        }
        // 设置默认值
        if (StringUtils.isEmpty(dto.getCalendarType())) {
            dto.setCalendarType("STANDARD");
        }
        if (StringUtils.isEmpty(dto.getSiteType())) {
            dto.setSiteType("MANUFACTURING");
        }

        // 2. 获取当前组织下的日历ID
        MtCalendarVO2 orgSiteCalendarVO = new MtCalendarVO2();
        orgSiteCalendarVO.setOrganizationType(dto.getOrganizationType());
        orgSiteCalendarVO.setOrganizationId(dto.getOrganizationId());
        orgSiteCalendarVO.setSiteType(dto.getSiteType());
        orgSiteCalendarVO.setCalendarType(dto.getCalendarType());

        String calendarId = mtCalendarRepository.organizationLimitOnlyCalendarGet(tenantId, orgSiteCalendarVO);

        // 逻辑变更1.3(未取到数据则报错)
        if (StringUtils.isEmpty(calendarId)) {
            throw new MtException("MT_CALENDAR_0004", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0004", "CALENDAR", "【API:organizationAndShiftLimitCalendarShiftGet】"));

        }
        // 3. 获取日历班次数据
        MtCalendarShift mtCalendarShift = new MtCalendarShift();
        mtCalendarShift.setTenantId(tenantId);
        mtCalendarShift.setCalendarId(calendarId);
        mtCalendarShift.setShiftCode(dto.getShiftCode());
        mtCalendarShift.setShiftDate(dto.getShiftDate());
        if (StringUtils.isEmpty(dto.getIsEnableFlag()) || "Y".equals(dto.getIsEnableFlag())) {
            mtCalendarShift.setEnableFlag("Y");
        }
        mtCalendarShift = mtCalendarShiftMapper.selectOne(mtCalendarShift);
        if (mtCalendarShift != null && StringUtils.isNotEmpty(mtCalendarShift.getCalendarShiftId())) {
            return mtCalendarShift.getCalendarShiftId();
        }
        return null;
    }

    @Override
    public MtCalendarShiftVO8 currentShiftLimitNextCalendarShiftGet(Long tenantId, String workcellId) {
        // 1.参数有效性校验
        if (StringUtils.isEmpty(workcellId)) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "workcellId", "【API:currentShiftLimitNextCalendarShiftGet】"));
        }

        // 2.获取当前工作单元班次
        MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, workcellId);
        if (null == mtWkcShiftVO3) {
            throw new MtException("MT_SHIFT_0011", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0011", "SHIFT", "【API:currentShiftLimitNextCalendarShiftGet】"));
        }
        // 3.获取工作日历下一班次
        MtCalendarShiftVO mtCalendarShiftVO = new MtCalendarShiftVO();
        mtCalendarShiftVO.setShiftDate(mtWkcShiftVO3.getShiftDate());
        mtCalendarShiftVO.setShiftCode(mtWkcShiftVO3.getShiftCode());
        mtCalendarShiftVO.setWorkcellId(workcellId);
        MtCalendarShift mtCalendarShift = mtCalendarShiftRepository.calendarNextShiftGet(tenantId, mtCalendarShiftVO);
        if (null == mtCalendarShift) {
            return null;
        }
        MtCalendarShiftVO8 result = new MtCalendarShiftVO8();
        result.setCalendarShiftId(mtCalendarShift.getCalendarShiftId());
        result.setShiftDate(mtCalendarShift.getShiftDate());
        result.setShiftCode(mtCalendarShift.getShiftCode());
        return result;
    }

    @Override
    public List<MtCalendarShiftVO10> propertyLimitCalendarShiftPropertyQuery(Long tenantId, MtCalendarShiftVO9 vo) {
        return mtCalendarShiftMapper.propertyLimitCalendarShiftPropertyQuery(tenantId, vo);
    }

    @Override
    public List<MtCalendarShiftVO12> calendarShiftLimitTargetShiftQuery(Long tenantId, MtCalendarShiftVO11 vo) {
        if (StringUtils.isEmpty(vo.getCalendarShiftId())) {
            throw new MtException("MT_CALENDAR_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0001", "CALENDAR",
                                            "calendarShiftId", "【API：calendarShiftLimitTargetShiftQuery】"));
        }
        // 根据输入参数calendarShiftId在表mt_calendar_shift中查询数据
        MtCalendarShift mtCalendarShift = new MtCalendarShift();
        mtCalendarShift.setTenantId(tenantId);
        mtCalendarShift.setCalendarShiftId(vo.getCalendarShiftId());
        mtCalendarShift = mtCalendarShiftMapper.selectOne(mtCalendarShift);
        if (null != mtCalendarShift) {
            // 获取输出参数
            Date shiftStartTime = mtCalendarShift.getShiftStartTime();
            Date shiftEndTime = mtCalendarShift.getShiftEndTime();
            // 根据输入参数organizationType、organizationId（若未输入则不限制）在表mt_calendar_org_rel中查询数据
            List<MtCalendarShiftVO12> shifts = mtCalendarShiftMapper.calendarShiftLimitTargetShiftQuery(tenantId, vo,
                            shiftStartTime, shiftEndTime);
            if (CollectionUtils.isNotEmpty(shifts)) {
                Long sequence = 1L;
                shifts = shifts.stream().filter(t -> !t.getTargetCalendarShiftId().equals(vo.getCalendarShiftId()))
                                .distinct().collect(Collectors.toList());
                for (MtCalendarShiftVO12 vo12 : shifts) {
                    vo12.setSequence(sequence++);
                }
                return shifts;
            }
        }
        return Collections.emptyList();
    }

    /**
     * calendarLimitShiftBatchQuery-批量获取特定日历班次
     *
     * @author chuang.yang
     * @date 2019/12/12
     * @param tenantId
     * @param dto
     * @return java.util.List<java.lang.String>
     */
    @Override
    public List<String> calendarLimitShiftBatchQuery(Long tenantId, List<MtCalendarShiftVO2> dto) {
        if (CollectionUtils.isEmpty(dto) || dto.size() == 0) {
            return Collections.emptyList();
        }

        return mtCalendarShiftMapper.calendarLimitShiftBatchQuery(tenantId, dto);
    }


    @Override
    public List<MtCalendarShiftVO14> dateLimitCalendarShiftGet(Long tenantId, MtCalendarShiftVO13 dto) {

        List<MtCalendarShiftVO14> resultList = new ArrayList<>();
        // 参数合规性校验
        if (StringUtils.isEmpty(dto.getOrganizationId())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "organizationId", "【API：dateLimitCalendarShiftGet】"));
        }

        if (StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "organizationType", "【API：dateLimitCalendarShiftGet】"));
        }

        if (dto.getDate() == null) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "date", "【API：dateLimitCalendarShiftGet】"));
        }

        // 获取组织工作日历班次
        MtCalendarVO2 orgSiteCalendarVO = new MtCalendarVO2();
        orgSiteCalendarVO.setOrganizationId(dto.getOrganizationId());
        orgSiteCalendarVO.setOrganizationType(dto.getOrganizationType());
        orgSiteCalendarVO.setSiteType("MANUFACTURING");
        orgSiteCalendarVO.setCalendarType("STANDARD");
        // 获取组织唯一可工作日历，调用API{organizationLimitOnlyCalendarGet}传入参数
        String calendarIds = mtCalendarRepository.organizationLimitOnlyCalendarGet(tenantId, orgSiteCalendarVO);
        String calendarId = "";
        if (StringUtils.isNotEmpty(calendarIds)) {
            calendarId = calendarIds;
        }
        String date1 = new SimpleDateFormat("yyyy-MM-dd").format(dto.getDate());

        Calendar cale = Calendar.getInstance();
        cale.setTime(dto.getDate());
        cale.add(Calendar.DATE, -1);
        String date2 = new SimpleDateFormat("yyyy-MM-dd").format(cale.getTime());

        // 获取日历班次，调用API{calendarLimitShiftBatchQuery}传入参数
        List<MtCalendarShiftVO2> condition = new ArrayList<>();
        try {
            MtCalendarShiftVO2 shift = new MtCalendarShiftVO2();
            shift.setCalendarId(calendarId);
            shift.setShiftDate(new SimpleDateFormat("yyyy-MM-dd").parse(date1));
            condition.add(shift);
            shift = new MtCalendarShiftVO2();
            shift.setCalendarId(calendarId);
            shift.setShiftDate(new SimpleDateFormat("yyyy-MM-dd").parse(date2));
            condition.add(shift);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<String> calendarShiftIds = calendarLimitShiftBatchQuery(tenantId, condition);

        // 获取班次信息，调用API{calendarShiftBatchGet}传入参数
        List<MtCalendarShift> calendarShifts = calendarShiftBatchGet(tenantId, calendarShiftIds);

        if (CollectionUtils.isNotEmpty(calendarShifts)) {
            resultList = calendarShifts.stream().filter(t -> t.getShiftStartTime() != null
                            && t.getShiftStartTime().getTime() <= dto.getDate().getTime() && t.getShiftEndTime() != null
                            && t.getShiftEndTime().getTime() >= dto.getDate().getTime()).map(t -> {
                                MtCalendarShiftVO14 shiftVO14 = new MtCalendarShiftVO14();
                                shiftVO14.setCalendarShiftId(t.getCalendarShiftId());
                                shiftVO14.setShiftCode(t.getShiftCode());
                                shiftVO14.setShiftDate(t.getShiftDate());
                                return shiftVO14;
                            }).collect(Collectors.toList());
        }

        if (CollectionUtils.isEmpty(resultList)) {
            throw new MtException("MT_CALENDAR_0005", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0005", "CALENDAR", "【API：dateLimitCalendarShiftGet】"));
        }
        return resultList;
    }
}
