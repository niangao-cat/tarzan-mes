package tarzan.calendar.infra.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.calendar.domain.entity.MtShift;
import tarzan.calendar.domain.repository.MtShiftRepository;
import tarzan.calendar.domain.vo.MtShiftVO;
import tarzan.calendar.domain.vo.MtShiftVO1;
import tarzan.calendar.infra.mapper.MtShiftMapper;

/**
 * 班次信息 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:39
 */
@Component
public class MtShiftRepositoryImpl extends BaseRepositoryImpl<MtShift> implements MtShiftRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtShiftMapper mtShiftMapper;

    @Override
    public MtShift shiftTempletGet(Long tenantId, String shiftId) {
        if (StringUtils.isEmpty(shiftId)) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "shiftId", "【API：shiftTempletGet】"));
        }
        MtShift shift = new MtShift();
        shift.setTenantId(tenantId);
        shift.setShiftId(shiftId);
        return mtShiftMapper.selectOne(shift);
    }

    @Override
    public List<String> typeLimitShiftTempletQuery(Long tenantId, String shiftType) {
        if (StringUtils.isEmpty(shiftType)) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "shiftType", "【API：typeLimitShiftTempletQuery】"));
        }
        return mtShiftMapper.typeLimitShiftTempletQuery(tenantId, shiftType, null);
    }

    @Override
    public List<String> availableShiftTempletQuery(Long tenantId, String shiftType) {
        if (StringUtils.isEmpty(shiftType)) {
            throw new MtException("MT_CALENDAR_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0001", "CALENDAR", "shiftType", "【API：availableShiftTempletQuery】"));
        }
        return mtShiftMapper.typeLimitShiftTempletQuery(tenantId, shiftType, "Y");
    }

    @Override
    public List<MtShiftVO1> propertyLimitshiftTempletPropertyQuery(Long tenantId, MtShiftVO dto) {
        return mtShiftMapper.selectCondition(tenantId, dto);
    }
}
