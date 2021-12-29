package tarzan.actual.domain.trans.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import tarzan.actual.domain.entity.MtEoStepActual;
import tarzan.actual.domain.entity.MtEoStepActualHis;
import tarzan.actual.domain.trans.MtEoStepActualTransMapper;


@Component
public class MtEoStepActualTranMapperImpl implements MtEoStepActualTransMapper {

    @Override
    public MtEoStepActualHis mtEoStepActualTransToHis(MtEoStepActual dto) {
        if (dto == null) {
            return null;
        }

        MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();

        mtEoStepActualHis.setCreationDate(dto.getCreationDate());
        mtEoStepActualHis.setCreatedBy(dto.getCreatedBy());
        mtEoStepActualHis.setLastUpdateDate(dto.getLastUpdateDate());
        mtEoStepActualHis.setLastUpdatedBy(dto.getLastUpdatedBy());
        mtEoStepActualHis.setObjectVersionNumber(dto.getObjectVersionNumber());
        mtEoStepActualHis.setTableId(dto.getTableId());
        mtEoStepActualHis.set_token(dto.get_token());
        Map<String, Object> map = dto.getFlex();
        if (map != null) {
            mtEoStepActualHis.setFlex(new HashMap<String, Object>(map));
        }
        mtEoStepActualHis.setTenantId(dto.getTenantId());
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoStepActualHis.setSequence(dto.getSequence());
        mtEoStepActualHis.setRouterStepId(dto.getRouterStepId());
        mtEoStepActualHis.setOperationId(dto.getOperationId());
        mtEoStepActualHis.setStepName(dto.getStepName());
        mtEoStepActualHis.setQueueQty(dto.getQueueQty());
        mtEoStepActualHis.setWorkingQty(dto.getWorkingQty());
        mtEoStepActualHis.setCompletePendingQty(dto.getCompletePendingQty());
        mtEoStepActualHis.setCompletedQty(dto.getCompletedQty());
        mtEoStepActualHis.setScrappedQty(dto.getScrappedQty());
        mtEoStepActualHis.setHoldQty(dto.getHoldQty());
        mtEoStepActualHis.setBypassedFlag(dto.getBypassedFlag());
        mtEoStepActualHis.setReworkStepFlag(dto.getReworkStepFlag());
        mtEoStepActualHis.setLocalReworkFlag(dto.getLocalReworkFlag());
        mtEoStepActualHis.setMaxProcessTimes(dto.getMaxProcessTimes());
        mtEoStepActualHis.setTimesProcessed(dto.getTimesProcessed());
        mtEoStepActualHis.setPreviousStepId(dto.getPreviousStepId());
        mtEoStepActualHis.setQueueDate(dto.getQueueDate());
        mtEoStepActualHis.setWorkingDate(dto.getWorkingDate());
        mtEoStepActualHis.setCompletedDate(dto.getCompletedDate());
        mtEoStepActualHis.setCompletePendingDate(dto.getCompletePendingDate());
        mtEoStepActualHis.setStatus(dto.getStatus());
        mtEoStepActualHis.setSpecialInstruction(dto.getSpecialInstruction());
        mtEoStepActualHis.setHoldCount(dto.getHoldCount());
        mtEoStepActualHis.setCid(dto.getCid());
        if (mtEoStepActualHis.get_innerMap() != null) {
            Map<String, Object> map1 = dto.get_innerMap();
            if (map1 != null) {
                mtEoStepActualHis.get_innerMap().putAll(map1);
            }
        }
        if (mtEoStepActualHis.get_tls() != null) {
            Map<String, Map<String, String>> map2 = dto.get_tls();
            if (map2 != null) {
                mtEoStepActualHis.get_tls().putAll(map2);
            }
        }

        return mtEoStepActualHis;
    }
}
