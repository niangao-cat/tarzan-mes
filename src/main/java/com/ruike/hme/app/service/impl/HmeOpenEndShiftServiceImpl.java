package com.ruike.hme.app.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.HmeOpenEndShiftDTO;
import com.ruike.hme.api.dto.HmeOpenEndShiftDTO2;
import com.ruike.hme.api.dto.HmeOpenEndShiftEndCancelCommandDTO;
import com.ruike.hme.app.service.HmeOpenEndShiftService;
import com.ruike.hme.domain.entity.HmeWkcShiftAttr;
import com.ruike.hme.domain.repository.HmeOpenEndShiftRepository;
import com.ruike.hme.domain.repository.HmeWkcShiftAttrRepository;
import com.ruike.hme.domain.vo.HmeOpenEndShiftVO;
import com.ruike.hme.domain.vo.HmeOpenEndShiftVO2;
import com.ruike.hme.domain.vo.HmeOpenEndShiftVO3;
import com.ruike.hme.domain.vo.HmeOpenEndShiftVO4;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.infra.mapper.MtWkcShiftMapper;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 班组工作平台-开班结班管理应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020-07-07 09:48:23
 */
@Service
public class HmeOpenEndShiftServiceImpl implements HmeOpenEndShiftService {

    private final HmeOpenEndShiftRepository hmeOpenEndShiftRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final HmeWkcShiftAttrRepository hmeWkcShiftAttrRepository;
    private final MtWkcShiftRepository wkcShiftRepository;
    private final MtWkcShiftMapper wkcShiftMapper;

    public HmeOpenEndShiftServiceImpl(HmeOpenEndShiftRepository hmeOpenEndShiftRepository, MtErrorMessageRepository mtErrorMessageRepository, HmeWkcShiftAttrRepository hmeWkcShiftAttrRepository, MtWkcShiftRepository wkcShiftRepository, MtWkcShiftMapper wkcShiftMapper) {
        this.hmeOpenEndShiftRepository = hmeOpenEndShiftRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.hmeWkcShiftAttrRepository = hmeWkcShiftAttrRepository;
        this.wkcShiftRepository = wkcShiftRepository;
        this.wkcShiftMapper = wkcShiftMapper;
    }


    @Override
    public List<HmeOpenEndShiftVO> lineWorkellDataQuery(Long tenantId) {
        return hmeOpenEndShiftRepository.lineWorkellDataQuery(tenantId);
    }

    @Override
    public List<HmeOpenEndShiftVO2> shiftQuery(Long tenantId, HmeOpenEndShiftDTO dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "默认站点", ""));
        }
        if (StringUtils.isEmpty(dto.getLineWorkcellId())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "工段", ""));
        }
        if (dto.getDate() == null) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "日期", ""));
        }
        return hmeOpenEndShiftRepository.shiftQuery(tenantId, dto);
    }

    @Override
    public HmeOpenEndShiftVO4 shiftDateAndCodeQuery(Long tenantId, String lineWorkcellId) {
        if (StringUtils.isEmpty(lineWorkcellId)) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "工段"));
        }
        return hmeOpenEndShiftRepository.shiftDateAndCodeQuery(tenantId, lineWorkcellId);
    }

    @Override
    public HmeOpenEndShiftVO3 dateTimeQuery(Long tenantId, HmeOpenEndShiftDTO2 dto) {
        if (StringUtils.isEmpty(dto.getLineWorkcellId())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "工段", ""));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "班次", ""));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "班次日期", ""));
        }
        return hmeOpenEndShiftRepository.dateTimeQuery(tenantId, dto);
    }

    @Override
    public Date openShiftActualDate(Long tenantId, HmeOpenEndShiftDTO2 dto) {
        if (StringUtils.isEmpty(dto.getLineWorkcellId())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "工段", ""));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "班次", ""));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "班次日期", ""));
        }
        return hmeOpenEndShiftRepository.openShiftActualDate(tenantId, dto);
    }

    @Override
    public Date endShiftActualDate(Long tenantId, HmeOpenEndShiftDTO2 dto) {
        if (StringUtils.isEmpty(dto.getLineWorkcellId())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "工段", ""));
        }
        if (StringUtils.isEmpty(dto.getShiftCode()) || StringUtils.isEmpty(dto.getWkcShiftId())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "班次", ""));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "班次日期", ""));
        }
        //校验结班前是否填写交接注意事项
        HmeWkcShiftAttr hmeWkcShiftAttr = hmeWkcShiftAttrRepository.selectOne(new HmeWkcShiftAttr() {{
            setTenantId(tenantId);
            setWkcShiftId(dto.getWkcShiftId());
        }});
        if (hmeWkcShiftAttr == null) {
            throw new MtException("HME_SHIFT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SHIFT_004", "HME"));
        }
        return hmeOpenEndShiftRepository.endShiftActualDate(tenantId, dto);
    }

    @Override
    public void shiftEndCancel(Long tenantId, HmeOpenEndShiftEndCancelCommandDTO command) {
        command.setTenantId(tenantId);
        List<MtWkcShift> shiftList = wkcShiftRepository.select(command.toWkcCondition());
        MtWkcShift shift = cancelValidate(tenantId, command, shiftList);
        shift.setShiftEndTime(null);
        wkcShiftMapper.updateByPrimaryKey(shift);
    }

    private MtWkcShift cancelValidate(Long tenantId, HmeOpenEndShiftEndCancelCommandDTO command, List<MtWkcShift> shiftList) {
        Optional<MtWkcShift> lastShiftOpl = shiftList.stream().min((a, b) -> b.getLastUpdateDate().compareTo(a.getLastUpdateDate()));
        if (!lastShiftOpl.isPresent()) {
            throw new MtException("HME_SHIFT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SHIFT_0001", "HME"));
        }
        Optional<MtWkcShift> notEndShiftOpl = shiftList.stream().filter(r -> Objects.isNull(r.getShiftEndTime())).findFirst();
        if (notEndShiftOpl.isPresent()) {
            throw new MtException("HME_SHIFT_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SHIFT_0002", "HME", DateUtil.formatDateTime(notEndShiftOpl.get().getShiftDate()), notEndShiftOpl.get().getShiftCode()));
        }
        Optional<MtWkcShift> selectShiftOpl = shiftList.stream().filter(r -> r.getShiftCode().equals(command.getShiftCode()) && (r.getShiftDate().compareTo(command.getShiftDate()) == 0)).findAny();
        if (!selectShiftOpl.isPresent()) {
            throw new MtException("HME_SHIFT_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SHIFT_0003", "HME", DateUtil.formatDateTime(command.getShiftDate()), command.getShiftCode()));
        }
        if (!selectShiftOpl.get().getWkcShiftId().equals(lastShiftOpl.get().getWkcShiftId())) {
            throw new MtException("HME_SHIFT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SHIFT_0004", "HME", DateUtil.formatDateTime(lastShiftOpl.get().getShiftDate()), lastShiftOpl.get().getShiftCode()));
        }

        return selectShiftOpl.get();
    }
}
