package tarzan.actual.domain.trans.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import tarzan.actual.domain.entity.MtWorkOrderCompActualHis;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.trans.MtWoComponentActualTransMapper;
import tarzan.actual.domain.vo.MtWoComponentActualTupleVO;
import tarzan.actual.domain.vo.MtWoComponentActualVO30;


@Component
public class MtWoComponentActualTranMapperImpl implements MtWoComponentActualTransMapper {

    @Override
    public MtWoComponentActualTupleVO woComponentActualVO30ToActualTupleVO(MtWoComponentActualVO30 dto) {
        if (dto == null) {
            return null;
        }

        MtWoComponentActualTupleVO mtWoComponentActualTupleVO = new MtWoComponentActualTupleVO();

        mtWoComponentActualTupleVO.setMaterialId(dto.getMaterialId());
        mtWoComponentActualTupleVO.setComponentType(dto.getComponentType());
        mtWoComponentActualTupleVO.setBomComponentId(dto.getBomComponentId());
        mtWoComponentActualTupleVO.setBomId(dto.getBomId());
        mtWoComponentActualTupleVO.setRouterStepId(dto.getRouterStepId());
        mtWoComponentActualTupleVO.setWorkOrderId(dto.getWorkOrderId());

        return mtWoComponentActualTupleVO;
    }

    @Override
    public MtWorkOrderCompActualHis woComponentActualToCompActualHis(MtWorkOrderComponentActual dto) {
        if (dto == null) {
            return null;
        }

        MtWorkOrderCompActualHis mtWorkOrderCompActualHis = new MtWorkOrderCompActualHis();

        mtWorkOrderCompActualHis.setCreationDate(dto.getCreationDate());
        mtWorkOrderCompActualHis.setCreatedBy(dto.getCreatedBy());
        mtWorkOrderCompActualHis.setLastUpdateDate(dto.getLastUpdateDate());
        mtWorkOrderCompActualHis.setLastUpdatedBy(dto.getLastUpdatedBy());
        mtWorkOrderCompActualHis.setObjectVersionNumber(dto.getObjectVersionNumber());
        mtWorkOrderCompActualHis.setTableId(dto.getTableId());
        mtWorkOrderCompActualHis.set_token(dto.get_token());
        Map<String, Object> map = dto.getFlex();
        if (map != null) {
            mtWorkOrderCompActualHis.setFlex(new HashMap<String, Object>(map));
        }
        mtWorkOrderCompActualHis.setTenantId(dto.getTenantId());
        mtWorkOrderCompActualHis.setWorkOrderComponentActualId(dto.getWorkOrderComponentActualId());
        mtWorkOrderCompActualHis.setWorkOrderId(dto.getWorkOrderId());
        mtWorkOrderCompActualHis.setMaterialId(dto.getMaterialId());
        mtWorkOrderCompActualHis.setOperationId(dto.getOperationId());
        mtWorkOrderCompActualHis.setAssembleQty(dto.getAssembleQty());
        mtWorkOrderCompActualHis.setScrappedQty(dto.getScrappedQty());
        mtWorkOrderCompActualHis.setComponentType(dto.getComponentType());
        mtWorkOrderCompActualHis.setBomComponentId(dto.getBomComponentId());
        mtWorkOrderCompActualHis.setBomId(dto.getBomId());
        mtWorkOrderCompActualHis.setRouterStepId(dto.getRouterStepId());
        mtWorkOrderCompActualHis.setAssembleExcessFlag(dto.getAssembleExcessFlag());
        mtWorkOrderCompActualHis.setAssembleRouterType(dto.getAssembleRouterType());
        mtWorkOrderCompActualHis.setSubstituteFlag(dto.getSubstituteFlag());
        mtWorkOrderCompActualHis.setActualFirstTime(dto.getActualFirstTime());
        mtWorkOrderCompActualHis.setActualLastTime(dto.getActualLastTime());
        mtWorkOrderCompActualHis.setCid(dto.getCid());
        if (mtWorkOrderCompActualHis.get_innerMap() != null) {
            Map<String, Object> map1 = dto.get_innerMap();
            if (map1 != null) {
                mtWorkOrderCompActualHis.get_innerMap().putAll(map1);
            }
        }
        if (mtWorkOrderCompActualHis.get_tls() != null) {
            Map<String, Map<String, String>> map2 = dto.get_tls();
            if (map2 != null) {
                mtWorkOrderCompActualHis.get_tls().putAll(map2);
            }
        }

        return mtWorkOrderCompActualHis;
    }
}
