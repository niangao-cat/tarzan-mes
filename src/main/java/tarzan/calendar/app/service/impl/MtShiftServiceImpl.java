package tarzan.calendar.app.service.impl;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.calendar.api.dto.MtShiftDTO;
import tarzan.calendar.api.dto.MtShiftDTO1;
import tarzan.calendar.app.service.MtShiftService;
import tarzan.calendar.domain.entity.MtShift;
import tarzan.calendar.domain.repository.MtShiftRepository;
import tarzan.calendar.infra.mapper.MtShiftMapper;

/**
 * 班次信息应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:39
 */
@Service
public class MtShiftServiceImpl implements MtShiftService {

    private static final int MILLS_PER_DAY = 86400000;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtShiftRepository mtShiftRepo;

    @Autowired
    private MtShiftMapper mtShiftMapper;

    @Override
    public Page<MtShiftDTO> queryShiftForUi(Long tenantId, MtShiftDTO dto, PageRequest pageRequest) {
        MtShift queryShift = new MtShift();
        BeanUtils.copyProperties(dto, queryShift);
        queryShift.setTenantId(tenantId);

        Criteria criteria = new Criteria(queryShift);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtShift.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtShift.FIELD_SHIFT_CODE, Comparison.LIKE));
        whereFields.add(new WhereField(MtShift.FIELD_SHIFT_TYPE, Comparison.LIKE));
        whereFields.add(new WhereField(MtShift.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

        return PageHelper.doPageAndSort(pageRequest, () -> mtShiftRepo.selectOptional(queryShift, criteria));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveShiftForUi(Long tenantId, MtShiftDTO dto) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        MtShift mtShift = new MtShift();
        BeanUtils.copyProperties(dto, mtShift);
        mtShift.setTenantId(tenantId);

        MtShift queryShift = new MtShift();
        queryShift.setTenantId(tenantId);
        queryShift.setShiftType(dto.getShiftType());
        List<MtShift> shiftList = mtShiftRepo.select(queryShift);

        if (CollectionUtils.isEmpty(shiftList) && StringUtils.isEmpty(mtShift.getShiftId())) {
            mtShiftRepo.insertSelective(mtShift);
        } else {
            if (CollectionUtils.isNotEmpty(shiftList)) {
                if (shiftList.stream().anyMatch(s -> s.getShiftCode().equals(dto.getShiftCode())
                                && !s.getShiftId().equals(dto.getShiftId()))) {
                    throw new MtException("MT_CALENDAR_0022", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_CALENDAR_0022", "CALENDAR", dto.getShiftCode(), dto.getShiftType()));
                }

                if (shiftList.stream().anyMatch(s -> s.getSequence().equals(dto.getSequence())
                                && !s.getShiftId().equals(dto.getShiftId()))) {
                    throw new MtException("MT_CALENDAR_0023",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0023",
                                                    "CALENDAR", String.valueOf(dto.getSequence()), dto.getShiftType()));
                }
            }
            if (StringUtils.isEmpty(mtShift.getShiftId())) {
                shiftList.add(mtShift);
            } else {
                MtShift originShift = new MtShift();
                originShift.setTenantId(tenantId);
                originShift.setShiftId(mtShift.getShiftId());
                originShift = mtShiftRepo.selectOne(originShift);
                if (!mtShift.getShiftType().equals(originShift.getShiftType())) {
                    shiftList.add(mtShift);
                } else {
                    Optional<MtShift> shiftOptional = shiftList.stream()
                                    .filter(s -> s.getShiftId().equals(mtShift.getShiftId())).findAny();
                    shiftOptional.ifPresent(shift -> shiftList.set(shiftList.indexOf(shift), mtShift));
                }
            }

            List<MtShift> sortedShiftList;
            if (shiftList.size() == 1) {
                sortedShiftList = new ArrayList<>(shiftList);
            } else {
                sortedShiftList = shiftList.stream().sorted(Comparator.comparingLong(MtShift::getSequence))
                                .collect(Collectors.toList());
            }

            // check time total
            Time startTime, endTime, firstStartTime, lastEndTime;
            try {
                firstStartTime = new Time(timeFormat.parse(sortedShiftList.get(0).getShiftStartTime()).getTime());
            } catch (ParseException e) {
                throw new MtException("MT_CALENDAR_0010",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0010", "CALENDAR"));
            }

            long duration = 0L;
            for (int i = 0; i < sortedShiftList.size(); i++) {
                long tempDuration = 0L;
                try {
                    startTime = new Time(timeFormat.parse(sortedShiftList.get(i).getShiftStartTime()).getTime());
                    endTime = new Time(timeFormat.parse(sortedShiftList.get(i).getShiftEndTime()).getTime());
                    // next start-time is after current end-time
                    if (i < sortedShiftList.size() - 1) {
                        Time nextStartTime = new Time(
                                        timeFormat.parse(sortedShiftList.get(i + 1).getShiftStartTime()).getTime());
                        if (nextStartTime.compareTo(endTime) == 0) {
                            throw new MtException("MT_CALENDAR_0010", mtErrorMessageRepo
                                            .getErrorMessageWithModule(tenantId, "MT_CALENDAR_0010", "CALENDAR"));
                        }
                    }

                    if (i > 0) {
                        lastEndTime = new Time(
                                        timeFormat.parse(sortedShiftList.get(i - 1).getShiftEndTime()).getTime());
                        if (startTime.compareTo(lastEndTime) > 0 && startTime.compareTo(endTime) > 0
                                        && endTime.compareTo(firstStartTime) > 0) {
                            throw new MtException("MT_SHIFT_0014", mtErrorMessageRepo
                                            .getErrorMessageWithModule(tenantId, "MT_SHIFT_0014", "SHIFT"));
                        } else if (startTime.compareTo(lastEndTime) < 0 && startTime.compareTo(endTime) < 0
                                        && endTime.compareTo(firstStartTime) > 0) {
                            throw new MtException("MT_SHIFT_0015", mtErrorMessageRepo
                                            .getErrorMessageWithModule(tenantId, "MT_SHIFT_0015", "SHIFT"));
                        }

                        // 增加时间间隔
                        if (startTime.compareTo(lastEndTime) > 0) {
                            tempDuration += startTime.getTime() - lastEndTime.getTime();
                        } else {
                            tempDuration += new Date(startTime.getTime() + MILLS_PER_DAY).getTime()
                                            - lastEndTime.getTime();
                        }
                    }
                } catch (ParseException e) {
                    throw new MtException("MT_CALENDAR_0010", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_CALENDAR_0010", "CALENDAR"));
                }

                // calculate duration
                if (startTime.compareTo(endTime) > 0) {
                    duration += new Date(endTime.getTime() + MILLS_PER_DAY).getTime() - startTime.getTime();
                } else {
                    duration += endTime.getTime() - startTime.getTime();
                }
                duration += tempDuration;


            }

            if (duration > MILLS_PER_DAY) {
                throw new MtException("MT_CALENDAR_0010",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_CALENDAR_0010", "CALENDAR"));
            }

            if (StringUtils.isEmpty(mtShift.getShiftId())) {
                mtShiftRepo.insertSelective(mtShift);
            } else {
                mtShiftRepo.updateByPrimaryKeySelective(mtShift);
            }
        }

        return mtShift.getShiftId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeShiftForUi(Long tenantId, String shiftId) {
        if (StringUtils.isEmpty(shiftId)) {
            return;
        }

        MtShift mtShift = new MtShift();
        mtShift.setTenantId(tenantId);
        mtShift.setShiftId(shiftId);
        if (mtShiftRepo.delete(mtShift) == 0) {
            throw new MtException("数据删除失败.");
        }
    }

    @Override
    public List<MtShiftDTO1> queryShiftTypesForUi(Long tanentId, MtShiftDTO1 dto) {
        return mtShiftMapper.selectShiftTypes(tanentId, dto);
    }
}
