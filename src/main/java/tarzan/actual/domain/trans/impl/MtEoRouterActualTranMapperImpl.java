package tarzan.actual.domain.trans.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import tarzan.actual.domain.entity.MtEoRouterActual;
import tarzan.actual.domain.entity.MtEoRouterActualHis;
import tarzan.actual.domain.trans.MtEoRouterActualTransMapper;

@Component
public class MtEoRouterActualTranMapperImpl implements MtEoRouterActualTransMapper {

    @Override
    public MtEoRouterActualHis mtEoRouterActualTransToHis(MtEoRouterActual dto) {
        if (dto == null) {
            return null;
        }

        MtEoRouterActualHis mtEoRouterActualHis = new MtEoRouterActualHis();

        mtEoRouterActualHis.setCreationDate(dto.getCreationDate());
        mtEoRouterActualHis.setCreatedBy(dto.getCreatedBy());
        mtEoRouterActualHis.setLastUpdateDate(dto.getLastUpdateDate());
        mtEoRouterActualHis.setLastUpdatedBy(dto.getLastUpdatedBy());
        mtEoRouterActualHis.setObjectVersionNumber(dto.getObjectVersionNumber());
        mtEoRouterActualHis.setTableId(dto.getTableId());
        mtEoRouterActualHis.set_token(dto.get_token());
        Map<String, Object> map = dto.getFlex();
        if (map != null) {
            mtEoRouterActualHis.setFlex(new HashMap<String, Object>(map));
        }
        mtEoRouterActualHis.setTenantId(dto.getTenantId());
        mtEoRouterActualHis.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoRouterActualHis.setEoId(dto.getEoId());
        mtEoRouterActualHis.setSequence(dto.getSequence());
        mtEoRouterActualHis.setRouterId(dto.getRouterId());
        mtEoRouterActualHis.setStatus(dto.getStatus());
        mtEoRouterActualHis.setQty(dto.getQty());
        mtEoRouterActualHis.setSubRouterFlag(dto.getSubRouterFlag());
        mtEoRouterActualHis.setSourceEoStepActualId(dto.getSourceEoStepActualId());
        mtEoRouterActualHis.setCompletedQty(dto.getCompletedQty());
        mtEoRouterActualHis.setCid(dto.getCid());
        if (mtEoRouterActualHis.get_innerMap() != null) {
            Map<String, Object> map1 = dto.get_innerMap();
            if (map1 != null) {
                mtEoRouterActualHis.get_innerMap().putAll(map1);
            }
        }
        if (mtEoRouterActualHis.get_tls() != null) {
            Map<String, Map<String, String>> map2 = dto.get_tls();
            if (map2 != null) {
                mtEoRouterActualHis.get_tls().putAll(map2);
            }
        }

        return mtEoRouterActualHis;
    }
}
