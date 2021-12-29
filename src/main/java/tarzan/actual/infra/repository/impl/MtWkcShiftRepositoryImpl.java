package tarzan.actual.infra.repository.impl;

import static java.util.stream.Collectors.toList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.vo.MtWkcShiftVO;
import tarzan.actual.domain.vo.MtWkcShiftVO2;
import tarzan.actual.domain.vo.MtWkcShiftVO3;
import tarzan.actual.domain.vo.MtWkcShiftVO4;
import tarzan.actual.domain.vo.MtWkcShiftVO5;
import tarzan.actual.domain.vo.MtWkcShiftVO6;
import tarzan.actual.domain.vo.MtWkcShiftVO7;
import tarzan.actual.domain.vo.MtWkcShiftVO8;
import tarzan.actual.domain.vo.MtWkcShiftVO9;
import tarzan.actual.infra.mapper.MtWkcShiftMapper;
import tarzan.calendar.domain.entity.MtCalendarShift;
import tarzan.calendar.domain.repository.MtCalendarShiftRepository;
import tarzan.calendar.domain.vo.MtCalendarShiftVO;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

/**
 * 开班实绩数据表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:00:23
 */
@Component
public class MtWkcShiftRepositoryImpl extends BaseRepositoryImpl<MtWkcShift> implements MtWkcShiftRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtWkcShiftMapper mtWkcShiftMapper;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtCalendarShiftRepository mtCalendarShiftRepository;

    @Override
    public List<String> nextShiftGet(Long tenantId, String wkcShiftId) {
        if (StringUtils.isEmpty(wkcShiftId)) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "wkcShiftId", "【API:nextShiftGet】"));
        }

        MtWkcShift tmp = new MtWkcShift();
        tmp.setTenantId(tenantId);
        tmp.setWkcShiftId(wkcShiftId);
        tmp = mtWkcShiftMapper.selectOne(tmp);
        if (tmp == null) {
            throw new MtException("MT_SHIFT_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0005", "SHIFT", wkcShiftId, "【API:nextShiftGet】"));
        }

        List<String> ls = new ArrayList<String>();
        List<MtWkcShift> all = mtWkcShiftMapper.nextShift(tenantId, tmp);

        if (all.size() > 0) {
            Date sd = all.get(0).getShiftStartTime();
            for (int i = 0; i < all.size(); i++) {
                if (sd.compareTo(all.get(i).getShiftStartTime()) == 0) {
                    ls.add(all.get(i).getWkcShiftId());
                }
            }
        } else {
            throw new MtException("MT_SHIFT_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0007", "SHIFT", "【API:nextShiftGet】"));

        }
        return ls;
    }

    @Override
    public List<String> timePeriodLimitShiftQuery(Long tenantId, MtWkcShiftVO5 dto) {
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "workcellId", "【API:timePeriodLimitShiftQuery】"));
        }

        if (dto.getShiftEndTimeFrom() == null && dto.getShiftEndTimeTo() == null && dto.getShiftStartTimeFrom() == null
                        && dto.getShiftStartTimeTo() == null) {
            throw new MtException("MT_SHIFT_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0008", "SHIFT", "【API:timePeriodLimitShiftQuery】"));
        }

        MtModWorkcell wkc = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
        if (wkc == null) {
            throw new MtException("MT_SHIFT_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0009", "SHIFT", "workcell", "【API:timePeriodLimitShiftQuery】"));
        }

        MtWkcShiftVO tmp = new MtWkcShiftVO();
        tmp.setWorkcellId(dto.getWorkcellId());
        tmp.setShiftStartTimeFrom(dto.getShiftStartTimeFrom());
        tmp.setShiftEndTimeTo(dto.getShiftEndTimeTo());
        tmp.setShiftStartTimeTo(dto.getShiftStartTimeTo());
        tmp.setShiftEndTimeFrom(dto.getShiftEndTimeFrom());
        List<MtWkcShift> ls = mtWkcShiftMapper.timeLimitShift(tenantId, tmp);

        List<String> rs = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(ls)) {
            rs = ls.stream().map(MtWkcShift::getWkcShiftId).collect(toList());
        }
        return rs;
    }

    @Override
    public List<String> timePeriodShiftCodeLimitShiftQuery(Long tenantId, MtWkcShiftVO dto) {
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "workcellId", "【API:timePeriodShiftCodeLimitShiftQuery】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "shiftCode", "【API:timePeriodShiftCodeLimitShiftQuery】"));
        }

        if (dto.getShiftEndTimeFrom() == null && dto.getShiftEndTimeTo() == null && dto.getShiftStartTimeFrom() == null
                        && dto.getShiftStartTimeTo() == null) {
            throw new MtException("MT_SHIFT_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0008", "SHIFT", "【API:timePeriodShiftCodeLimitShiftQuery】"));
        }

        MtModWorkcell wkc = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());

        if (wkc == null) {
            throw new MtException("MT_SHIFT_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0009", "SHIFT", "workcell", "【API:timePeriodShiftCodeLimitShiftQuery】"));
        }

        MtWkcShiftVO tmp = new MtWkcShiftVO();
        tmp.setWorkcellId(dto.getWorkcellId());
        tmp.setShiftStartTimeFrom(dto.getShiftStartTimeFrom());
        tmp.setShiftEndTimeTo(dto.getShiftEndTimeTo());
        tmp.setShiftStartTimeTo(dto.getShiftStartTimeTo());
        tmp.setShiftEndTimeFrom(dto.getShiftEndTimeFrom());
        tmp.setShiftCode(dto.getShiftCode());
        List<MtWkcShift> ls = mtWkcShiftMapper.timeLimitShift(tenantId, tmp);

        List<String> rs = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(ls)) {
            rs = ls.stream().map(MtWkcShift::getWkcShiftId).collect(toList());
        }
        return rs;
    }

    @Override
    public List<String> previousShiftGet(Long tenantId, String wkcShiftId) {
        if (StringUtils.isEmpty(wkcShiftId)) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "wkcShiftId", "【API:previousShiftGet】"));
        }

        MtWkcShift tmp = new MtWkcShift();
        tmp.setTenantId(tenantId);
        tmp.setWkcShiftId(wkcShiftId);
        tmp = mtWkcShiftMapper.selectOne(tmp);
        if (tmp == null) {
            throw new MtException("MT_SHIFT_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0005", "SHIFT", wkcShiftId, "【API:previousShiftGet】"));
        }

        List<String> ls = new ArrayList<String>();
        List<MtWkcShift> all = mtWkcShiftMapper.previousShift(tenantId, tmp);
        if (all.size() > 0) {
            // all =
            // all.stream().sorted(Comparator.comparing(MtWkcShift::getShiftEndTime)).collect(toList());
            Date sd = all.get(all.size() - 1).getShiftEndTime();
            for (int i = 0; i < all.size(); i++) {
                if (sd.equals(all.get(i).getShiftEndTime())) {
                    ls.add(all.get(i).getWkcShiftId());
                }
            }
        } else {
            throw new MtException("MT_SHIFT_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0006", "SHIFT", "【API:previousShiftGet】"));
        }
        return ls;
    }

    @Override
    public MtWkcShiftVO3 wkcCurrentShiftQuery(Long tenantId, String workcellId) {
        if (StringUtils.isEmpty(workcellId)) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "workcellId", "【API:wkcCurrentShiftQuery】"));
        }

        MtModWorkcell wkc = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, workcellId);
        if (wkc == null) {
            throw new MtException("MT_SHIFT_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0009", "SHIFT", "workcell", "【API:wkcCurrentShiftQuery】"));
        }

        MtWkcShift tmp = new MtWkcShift();
        tmp.setWorkcellId(workcellId);
        tmp.setShiftStartTime(new Date());
        List<MtWkcShift> all = mtWkcShiftMapper.currentShift(tenantId, tmp);

        if (all.size() > 0) {
            if (all.size() > 1) {
                throw new MtException("MT_SHIFT_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_SHIFT_0002", "SHIFT", wkc.getWorkcellCode(), "【API:wkcCurrentShiftQuery】"));
            } else {

                MtWkcShiftVO3 tmp3 = new MtWkcShiftVO3();
                tmp3.setShiftCode(all.get(0).getShiftCode());
                tmp3.setShiftDate(all.get(0).getShiftDate());
                tmp3.setWkcShiftId(all.get(0).getWkcShiftId());
                return tmp3;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<String> wkcLimitLastShiftGet(Long tenantId, String workcellId) {
        List<String> ls = new ArrayList<String>();
        if (StringUtils.isEmpty(workcellId)) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "workcellId", "【API:wkcLimitLastShiftGet】"));
        }

        MtModWorkcell wkc = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, workcellId);
        if (wkc == null) {
            throw new MtException("MT_SHIFT_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0009", "SHIFT", "workcell", "【API:wkcLimitLastShiftGet】"));
        }

        try {
            ls.add(wkcCurrentShiftQuery(tenantId, workcellId).getWkcShiftId());
            return ls;
        } catch (Exception e) {
            e.printStackTrace();
        }

        MtWkcShift tmp = new MtWkcShift();
        tmp.setTenantId(tenantId);
        tmp.setWorkcellId(workcellId);
        List<MtWkcShift> all = mtWkcShiftMapper.select(tmp);
        if (all.size() > 0) {
            // 日期为空的情况转换为1970年
            all = all.stream().sorted(Comparator.comparing(
                            (MtWkcShift c) -> c.getShiftEndTime() == null ? new Date(0) : c.getShiftEndTime()))
                            .collect(toList());

            Date sd = all.get(all.size() - 1).getShiftEndTime();
            for (int i = 0; i < all.size(); i++) {
                if (sd.equals(all.get(i).getShiftEndTime())) {
                    ls.add(all.get(i).getWkcShiftId());
                }
            }
        }
        return ls;
    }

    @Override
    public Long timePeriodShiftTimeSum(Long tenantId, MtWkcShiftVO2 dto) {
        Long ts = 0L;
        Date nowtime = new Date();
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "workcellId", "【API:timePeriodShiftTimeSum】"));
        }

        if (dto.getShiftTimeFrom() == null && dto.getShiftTimeTo() == null) {
            throw new MtException("MT_SHIFT_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0008", "SHIFT", "【API:timePeriodShiftTimeSum】"));
        }

        if ((dto.getShiftTimeFrom() != null && nowtime.before(dto.getShiftTimeFrom()))
                        || (dto.getShiftTimeTo() != null && nowtime.before(dto.getShiftTimeTo()))) {
            throw new MtException("MT_SHIFT_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0010", "SHIFT", "【API:timePeriodShiftTimeSum】"));
        }

        if (dto.getShiftTimeTo() == null) {
            dto.setShiftTimeTo(nowtime);
        }

        if (dto.getShiftTimeFrom() == null) {
            // 如果只输入了一个结束时间
            MtWkcShiftVO5 tmp0 = new MtWkcShiftVO5();
            tmp0.setWorkcellId(dto.getWorkcellId());
            tmp0.setShiftEndTimeFrom(dto.getShiftTimeTo());
            List<String> ls = timePeriodLimitShiftQuery(tenantId, tmp0);
            return Math.abs(mtWkcShiftMapper.gettimes(tenantId, ls) / 1000);
        } else {

            // 第一种情况全部包含
            MtWkcShiftVO5 tmp0 = new MtWkcShiftVO5();
            tmp0.setWorkcellId(dto.getWorkcellId());
            tmp0.setShiftStartTimeTo(dto.getShiftTimeFrom());
            tmp0.setShiftEndTimeFrom(dto.getShiftTimeTo());
            List<String> ls = timePeriodLimitShiftQuery(tenantId, tmp0);
            if (CollectionUtils.isNotEmpty(ls)) {
                // return Math.abs(mtWkcShiftMapper.gettimes(ls)/1000);
                return (dto.getShiftTimeTo().getTime() - dto.getShiftTimeFrom().getTime()) / 1000;

            }
            // 第二种情况 三段

            // 计算出中间值
            MtWkcShiftVO5 tmp = new MtWkcShiftVO5();
            tmp.setWorkcellId(dto.getWorkcellId());
            tmp.setShiftStartTimeFrom(dto.getShiftTimeFrom());
            tmp.setShiftEndTimeTo(dto.getShiftTimeTo());

            // 计算中间值的时候要剔除掉相等的时间段
            ls = timePeriodLimitShiftQuery(tenantId, tmp);

            if (CollectionUtils.isNotEmpty(ls)) {
                Long times = mtWkcShiftMapper.gettimes(tenantId, ls);
                if (times != null) {
                    ts += Math.abs(times);
                }
            }

            // 计算出starttime所在的班次

            tmp = new MtWkcShiftVO5();
            tmp.setWorkcellId(dto.getWorkcellId());
            tmp.setShiftEndTimeFrom(dto.getShiftTimeFrom());
            tmp.setShiftStartTimeTo(dto.getShiftTimeFrom());
            List<String> nls = timePeriodLimitShiftQuery(tenantId, tmp);

            if (CollectionUtils.isNotEmpty(nls) && !ls.contains(nls.get(0))) {
                MtWkcShift dn = new MtWkcShift();
                dn.setWkcShiftId(nls.get(0));
                dn = mtWkcShiftMapper.selectOne(dn);
                ts += dn.getShiftEndTime().getTime() - dto.getShiftTimeFrom().getTime();
            }

            // 计算出endtime所在的班次
            tmp = new MtWkcShiftVO5();
            tmp.setWorkcellId(dto.getWorkcellId());
            tmp.setShiftEndTimeFrom(dto.getShiftTimeTo());
            tmp.setShiftStartTimeTo(dto.getShiftTimeTo());
            nls = timePeriodLimitShiftQuery(tenantId, tmp);

            if (CollectionUtils.isNotEmpty(nls) && !ls.contains(nls.get(0))) {
                MtWkcShift dn = new MtWkcShift();
                dn.setWkcShiftId(nls.get(0));
                dn = mtWkcShiftMapper.selectOne(dn);
                ts += dto.getShiftTimeTo().getTime() - dn.getShiftStartTime().getTime();
            }

        }
        return ts / 1000;
    }

    @Override
    public List<String> wkcLimitShiftQuery(Long tenantId, String workcellId) {
        if (StringUtils.isEmpty(workcellId)) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "workcellId", "【API:wkcLimitShiftQuery】"));
        }

        MtModWorkcell wkc = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, workcellId);
        if (wkc == null) {
            throw new MtException("MT_SHIFT_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0009", "SHIFT", "workcell", "【API:wkcLimitShiftQuery】"));
        }

        MtWkcShift tmp = new MtWkcShift();
        tmp.setTenantId(tenantId);
        tmp.setWorkcellId(workcellId);
        List<MtWkcShift> ls = mtWkcShiftMapper.select(tmp);
        if (CollectionUtils.isNotEmpty(ls)) {
            return ls.stream().map(MtWkcShift::getWkcShiftId).collect(toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> wkcAndDateLimitShiftQuery(Long tenantId, MtWkcShiftVO6 dto) {
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "workcellId", "【API:wkcAndDateLimitShiftQuery】"));
        }

        if (dto.getShiftDate() == null) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "shiftDate", "【API:wkcAndDateLimitShiftQuery】"));
        }

        MtModWorkcell wkc = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
        if (wkc == null) {
            throw new MtException("MT_SHIFT_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0009", "SHIFT", "workcell", "【API:wkcAndDateLimitShiftQuery】"));
        }

        MtWkcShift tmp = new MtWkcShift();
        tmp.setTenantId(tenantId);
        tmp.setWorkcellId(dto.getWorkcellId());
        tmp.setShiftDate(dto.getShiftDate());
        List<MtWkcShift> ls = mtWkcShiftMapper.select(tmp);
        if (CollectionUtils.isNotEmpty(ls)) {
            return ls.stream().map(MtWkcShift::getWkcShiftId).collect(toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void wkcShiftEnd(Long tenantId, String workcellId) {
        if (StringUtils.isEmpty(workcellId)) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "workcellId", "【API:wkcShiftEnd】"));
        }

        MtWkcShiftVO3 mtWkcShiftVO3 = wkcCurrentShiftQuery(tenantId, workcellId);
        String wkcShiftId = null;
        if (null != mtWkcShiftVO3) {
            wkcShiftId = mtWkcShiftVO3.getWkcShiftId();
        }

        MtWkcShift tmp = new MtWkcShift();
        tmp.setTenantId(tenantId);
        tmp.setWkcShiftId(wkcShiftId);
        tmp.setShiftEndTime(new Date());
        self().updateByPrimaryKeySelective(tmp);
    }

    @Override
    public MtWkcShift wkcShiftGet(Long tenantId, String wkcShiftId) {
        if (StringUtils.isEmpty(wkcShiftId)) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "wkcShiftId", "【API:wkcShiftGet】"));
        }

        MtWkcShift mtWkcShift = new MtWkcShift();
        mtWkcShift.setTenantId(tenantId);
        mtWkcShift.setWkcShiftId(wkcShiftId);
        return mtWkcShiftMapper.selectOne(mtWkcShift);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void wkcShiftBack(Long tenantId, MtWkcShiftVO7 dto) {
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "workcellId", "【API:wkcShiftBack】"));
        }

        if (dto.getShiftDate() == null) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "shiftDate", "【API:wkcShiftBack】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "shiftCode", "【API:wkcShiftBack】"));
        }

        MtModWorkcell wkc = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
        if (wkc == null) {
            throw new MtException("MT_SHIFT_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0009", "SHIFT", "workcell", "【API:wkcShiftBack】"));
        }

        MtWkcShift tmp = new MtWkcShift();
        tmp.setTenantId(tenantId);
        tmp.setWorkcellId(dto.getWorkcellId());
        tmp.setShiftDate(dto.getShiftDate());
        tmp.setShiftCode(dto.getShiftCode());
        tmp = mtWkcShiftMapper.selectOne(tmp);
        if (tmp == null) {
            throw new MtException("MT_SHIFT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0004", "SHIFT", wkc.getWorkcellCode(), "【API:wkcShiftBack】"));
        }

        // 删除数据
        self().deleteByPrimaryKey(tmp);
    }

    @Override
    public Long wkcShiftTimeCalculate(Long tenantId, MtWkcShiftVO4 dto) {
        Date nowtime = new Date();
        if (StringUtils.isEmpty(dto.getWkcShiftId())) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "wkcShiftId", "【API:wkcShiftTimeCalculate】"));
        }

        MtWkcShift tmp = wkcShiftGet(tenantId, dto.getWkcShiftId());
        if (tmp == null) {
            throw new MtException("MT_SHIFT_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0005", "SHIFT", dto.getWkcShiftId(), "【API:wkcShiftTimeCalculate】"));
        }

        Date d1 = tmp.getShiftStartTime();
        Date d2 = tmp.getShiftEndTime();

        if ((dto.getShiftTimeFrom() != null && nowtime.before(dto.getShiftTimeFrom()))
                        || (dto.getShiftTimeTo() != null && nowtime.before(dto.getShiftTimeTo()))) {
            throw new MtException("MT_SHIFT_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0010", "SHIFT", "【API:wkcShiftTimeCalculate】"));
        }
        if (d2 == null) {
            d2 = nowtime;
        }

        // 输入的时间区域和班次时间没有交集
        if ((dto.getShiftTimeFrom() != null && d2.before(dto.getShiftTimeFrom()))
                        || dto.getShiftTimeTo() != null && dto.getShiftTimeTo().before(d1)) {
            return 0L;
        }

        if (dto.getShiftTimeFrom() != null && d1.before(dto.getShiftTimeFrom())) {
            d1 = dto.getShiftTimeFrom();
        }
        if (dto.getShiftTimeTo() != null && dto.getShiftTimeTo().before(d2)) {
            d2 = dto.getShiftTimeTo();
        }

        if (d2 != null && d1 != null) {
            return (d2.getTime() - d1.getTime()) / 1000;
        } else {
            return null;
        }
    }

    @Override
    public String dateAndShiftCodeLimitShiftQuery(Long tenantId, MtWkcShiftVO7 dto) {
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "workcellId", "【API:dateAndShiftCodeLimitShiftQuery】"));
        }

        if (dto.getShiftDate() == null) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "shiftDate", "【API:dateAndShiftCodeLimitShiftQuery】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "shiftCode", "【API:dateAndShiftCodeLimitShiftQuery】"));
        }

        MtModWorkcell wkc = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
        if (wkc == null) {
            throw new MtException("MT_SHIFT_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0009", "SHIFT", "workcell", "【API:dateAndShiftCodeLimitShiftQuery】"));
        }

        MtWkcShift tmp = new MtWkcShift();
        tmp.setTenantId(tenantId);
        tmp.setWorkcellId(dto.getWorkcellId());
        tmp.setShiftDate(dto.getShiftDate());
        tmp.setShiftCode(dto.getShiftCode());
        tmp = mtWkcShiftMapper.selectOne(tmp);
        if (tmp != null) {
            return tmp.getWkcShiftId();
        }
        return null;
    }

    @Override
    public List<String> dateLimitShiftQuery(Long tenantId, String shiftDate) {
        MtWkcShift tmp = new MtWkcShift();
        tmp.setTenantId(tenantId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            tmp.setShiftDate(sdf.parse(shiftDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<MtWkcShift> ls = mtWkcShiftMapper.select(tmp);
        if (CollectionUtils.isNotEmpty(ls)) {
            return ls.stream().map(MtWkcShift::getWkcShiftId).collect(toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String wkcShiftStart(Long tenantId, MtWkcShiftVO7 dto) {
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "workcellId", "【API:wkcShiftStart】"));
        }

        if (dto.getShiftDate() == null) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "shiftDate", "【API:wkcShiftStart】"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "shiftCode", "【API:wkcShiftStart】"));
        }

        MtModWorkcell wkc = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
        if (wkc == null) {
            throw new MtException("MT_SHIFT_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0009", "SHIFT", "workcell", "【API:wkcShiftStart】"));
        }

        MtWkcShiftVO3 mtWkcShiftVO3 = wkcCurrentShiftQuery(tenantId, dto.getWorkcellId());
        String wkcShiftId = null;
        if (null != mtWkcShiftVO3) {
            wkcShiftId = mtWkcShiftVO3.getWkcShiftId();
        }

        if (wkcShiftId != null) {
            throw new MtException("MT_SHIFT_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0003", "SHIFT", wkc.getWorkcellCode(), "【API:wkcShiftStart】"));
        }

        MtWkcShift tmp = new MtWkcShift();
        tmp.setTenantId(tenantId);
        tmp.setWorkcellId(dto.getWorkcellId());
        tmp.setShiftCode(dto.getShiftCode());
        tmp.setShiftDate(dto.getShiftDate());
        tmp.setShiftStartTime(new Date());

        // 新增数据
        self().insertSelective(tmp);
        return tmp.getWkcShiftId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String wkcShiftHandover(Long tenantId, String workcellId) {
        if (StringUtils.isEmpty(workcellId)) {
            throw new MtException("MT_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0001", "SHIFT", "workcellId", "【API:wkcShiftHandover】"));
        }

        // 查询当前班次
        MtWkcShiftVO3 mtWkcShiftVO3 = wkcCurrentShiftQuery(tenantId, workcellId);
        if (null == mtWkcShiftVO3) {
            throw new MtException("MT_SHIFT_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_SHIFT_0002", "SHIFT", workcellId, "【API:wkcShiftHandover】"));
        }

        String wkcShiftId = mtWkcShiftVO3.getWkcShiftId();

        // 停班
        wkcShiftEnd(tenantId, workcellId);

        // 查询下一班次
        MtWkcShift tmp = new MtWkcShift();
        tmp.setTenantId(tenantId);
        tmp.setWkcShiftId(wkcShiftId);
        tmp = mtWkcShiftMapper.selectOne(tmp);

        MtCalendarShiftVO v = new MtCalendarShiftVO();
        v.setWorkcellId(workcellId);
        v.setShiftDate(tmp.getShiftDate());
        v.setShiftCode(tmp.getShiftCode());
        MtCalendarShift dtmp = mtCalendarShiftRepository.calendarNextShiftGet(tenantId, v);

        // 开班
        MtWkcShiftVO7 v2 = new MtWkcShiftVO7();
        v2.setWorkcellId(workcellId);
        v2.setShiftCode(dtmp.getShiftCode());
        v2.setShiftDate(dtmp.getShiftDate());
        return wkcShiftStart(tenantId, v2);

    }

    @Override
    public List<MtWkcShiftVO9> propertyLimitWkcShiftPropertyQuery(Long tenantId, MtWkcShiftVO8 dto) {
        List<MtWkcShiftVO9> shiftVO9List = mtWkcShiftMapper.selectCondition(tenantId, dto);
        if (CollectionUtils.isEmpty(shiftVO9List)) {
            return Collections.emptyList();
        }
        // 获取到的工作单元 workcellId列表
        List<String> workcellIds = shiftVO9List.stream().map(MtWkcShiftVO9::getWorkcellId)
                .filter(StringUtils::isNotEmpty).distinct().collect(toList());
        Map<String, MtModWorkcell> workcellMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(workcellIds)) {
            List<MtModWorkcell> mtModWorkcells =
                    mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workcellIds);
            // 获取工作单元短描述、工作单元长描述和工作单元编码：
            if (CollectionUtils.isNotEmpty(mtModWorkcells)) {
                workcellMap = mtModWorkcells.stream().collect(Collectors.toMap(t -> t.getWorkcellId(), t -> t));
            }
        }

        for (MtWkcShiftVO9 shiftVO9 : shiftVO9List) {
            shiftVO9.setWorkcellCode(null == workcellMap.get(shiftVO9.getWorkcellId()) ? null
                    : workcellMap.get(shiftVO9.getWorkcellId()).getWorkcellCode());
            shiftVO9.setWorkcellName(null == workcellMap.get(shiftVO9.getWorkcellId()) ? null
                    : workcellMap.get(shiftVO9.getWorkcellId()).getWorkcellName());
            shiftVO9.setWorkcellDescription(null == workcellMap.get(shiftVO9.getWorkcellId()) ? null
                    : workcellMap.get(shiftVO9.getWorkcellId()).getDescription());
        }
        return shiftVO9List;
    }

}
