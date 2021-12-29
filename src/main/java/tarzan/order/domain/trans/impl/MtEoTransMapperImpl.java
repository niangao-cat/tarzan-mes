package tarzan.order.domain.trans.impl;

import org.springframework.stereotype.Component;

import tarzan.actual.domain.entity.MtEoActual;
import tarzan.actual.domain.entity.MtWorkOrderActual;
import tarzan.actual.domain.vo.MtEoActualVO10;
import tarzan.actual.domain.vo.MtWorkOrderActualVO8.ActualInfo;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtEoHis;
import tarzan.order.domain.trans.MtEoTransMapper;
import tarzan.order.domain.vo.MtEoVO38;
import tarzan.order.domain.vo.MtEoVO45;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Component
public class MtEoTransMapperImpl implements MtEoTransMapper {

    @Override
    public MtEoVO38 mtEoToMtEoVO38(MtEo dto) {
        if (dto == null) {
            return null;
        }

        MtEoVO38 mtEoVO38 = new MtEoVO38();

        mtEoVO38.setEoId(dto.getEoId());
        mtEoVO38.setEoNum(dto.getEoNum());
        mtEoVO38.setSiteId(dto.getSiteId());
        mtEoVO38.setWorkOrderId(dto.getWorkOrderId());
        mtEoVO38.setStatus(dto.getStatus());
        mtEoVO38.setLastEoStatus(dto.getLastEoStatus());
        mtEoVO38.setProductionLineId(dto.getProductionLineId());
        mtEoVO38.setWorkcellId(dto.getWorkcellId());
        mtEoVO38.setPlanStartTime(dto.getPlanStartTime());
        mtEoVO38.setPlanEndTime(dto.getPlanEndTime());
        mtEoVO38.setQty(dto.getQty());
        mtEoVO38.setUomId(dto.getUomId());
        mtEoVO38.setEoType(dto.getEoType());
        mtEoVO38.setValidateFlag(dto.getValidateFlag());
        mtEoVO38.setIdentification(dto.getIdentification());
        mtEoVO38.setMaterialId(dto.getMaterialId());

        return mtEoVO38;
    }

    @Override
    public MtEoActualVO10 mEoActualToMtEoActualVO10(MtEoActual dto) {
        if (dto == null) {
            return null;
        }

        MtEoActualVO10 mtEoActualVO10 = new MtEoActualVO10();

        mtEoActualVO10.setEoActualId(dto.getEoActualId());
        mtEoActualVO10.setEoId(dto.getEoId());
        mtEoActualVO10.setCompletedQty(dto.getCompletedQty());
        mtEoActualVO10.setScrappedQty(dto.getScrappedQty());
        mtEoActualVO10.setHoldQty(dto.getHoldQty());
        if (dto.getActualStartTime() != null) {
            mtEoActualVO10.setActualStartTime(
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dto.getActualStartTime()));
        }
        if (dto.getActualEndTime() != null) {
            mtEoActualVO10.setActualEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dto.getActualEndTime()));
        }

        return mtEoActualVO10;
    }

    @Override
    public ActualInfo mtWorkOrderActualToActualInfo(MtWorkOrderActual dto) {
        if (dto == null) {
            return null;
        }

        ActualInfo actualInfo = new ActualInfo();

        actualInfo.setWorkOrderActualId(dto.getWorkOrderActualId());
        actualInfo.setWorkOrderId(dto.getWorkOrderId());
        actualInfo.setReleasedQty(dto.getReleasedQty());
        actualInfo.setCompletedQty(dto.getCompletedQty());
        actualInfo.setScrappedQty(dto.getScrappedQty());
        actualInfo.setHoldQty(dto.getHoldQty());
        actualInfo.setActualStartDate(dto.getActualStartDate());
        actualInfo.setActualEndDate(dto.getActualEndDate());

        return actualInfo;
    }

    @Override
    public MtEoVO45 mtEoToMtEoVO45(MtEo dto) {
        if (dto == null) {
            return null;
        }

        MtEoVO45 mtEoVO45 = new MtEoVO45();

        mtEoVO45.setEoId(dto.getEoId());
        mtEoVO45.setEoNum(dto.getEoNum());
        mtEoVO45.setSiteId(dto.getSiteId());
        mtEoVO45.setWorkOrderId(dto.getWorkOrderId());
        mtEoVO45.setStatus(dto.getStatus());
        mtEoVO45.setLastEoStatus(dto.getLastEoStatus());
        mtEoVO45.setProductionLineId(dto.getProductionLineId());
        mtEoVO45.setWorkcellId(dto.getWorkcellId());
        mtEoVO45.setPlanStartTime(dto.getPlanStartTime());
        mtEoVO45.setPlanEndTime(dto.getPlanEndTime());
        mtEoVO45.setQty(dto.getQty());
        mtEoVO45.setUomId(dto.getUomId());
        mtEoVO45.setEoType(dto.getEoType());
        mtEoVO45.setValidateFlag(dto.getValidateFlag());
        mtEoVO45.setIdentification(dto.getIdentification());
        mtEoVO45.setMaterialId(dto.getMaterialId());

        return mtEoVO45;
    }

    @Override
    public MtEo mtEoVO45ToMtEo(MtEoVO45 dto) {
        if ( dto == null ) {
            return null;
        }

        MtEo mtEo = new MtEo();

        mtEo.setEoId( dto.getEoId() );
        mtEo.setEoNum( dto.getEoNum() );
        mtEo.setSiteId( dto.getSiteId() );
        mtEo.setWorkOrderId( dto.getWorkOrderId() );
        mtEo.setStatus( dto.getStatus() );
        mtEo.setLastEoStatus( dto.getLastEoStatus() );
        mtEo.setProductionLineId( dto.getProductionLineId() );
        mtEo.setWorkcellId( dto.getWorkcellId() );
        mtEo.setPlanStartTime( dto.getPlanStartTime() );
        mtEo.setPlanEndTime( dto.getPlanEndTime() );
        mtEo.setQty( dto.getQty() );
        mtEo.setUomId( dto.getUomId() );
        mtEo.setEoType( dto.getEoType() );
        mtEo.setValidateFlag( dto.getValidateFlag() );
        mtEo.setIdentification( dto.getIdentification() );
        mtEo.setMaterialId( dto.getMaterialId() );

        return mtEo;
    }

    @Override
    public MtEoHis mtEoToMtEoHis(MtEo dto) {
        if ( dto == null ) {
            return null;
        }

        MtEoHis mtEoHis = new MtEoHis();

        mtEoHis.setCreationDate( dto.getCreationDate() );
        mtEoHis.setCreatedBy( dto.getCreatedBy() );
        mtEoHis.setLastUpdateDate( dto.getLastUpdateDate() );
        mtEoHis.setLastUpdatedBy( dto.getLastUpdatedBy() );
        mtEoHis.setObjectVersionNumber( dto.getObjectVersionNumber() );
        mtEoHis.setTableId( dto.getTableId() );
        mtEoHis.set_token( dto.get_token() );
        Map<String, Object> map = dto.getFlex();
        if ( map != null ) {
            mtEoHis.setFlex( new HashMap<String, Object>( map ) );
        }
        mtEoHis.setTenantId( dto.getTenantId() );
        mtEoHis.setEoId( dto.getEoId() );
        mtEoHis.setEoNum( dto.getEoNum() );
        mtEoHis.setSiteId( dto.getSiteId() );
        mtEoHis.setWorkOrderId( dto.getWorkOrderId() );
        mtEoHis.setStatus( dto.getStatus() );
        mtEoHis.setLastEoStatus( dto.getLastEoStatus() );
        mtEoHis.setProductionLineId( dto.getProductionLineId() );
        mtEoHis.setWorkcellId( dto.getWorkcellId() );
        mtEoHis.setPlanStartTime( dto.getPlanStartTime() );
        mtEoHis.setPlanEndTime( dto.getPlanEndTime() );
        mtEoHis.setQty( dto.getQty() );
        mtEoHis.setUomId( dto.getUomId() );
        mtEoHis.setEoType( dto.getEoType() );
        mtEoHis.setValidateFlag( dto.getValidateFlag() );
        mtEoHis.setIdentification( dto.getIdentification() );
        mtEoHis.setMaterialId( dto.getMaterialId() );
        mtEoHis.setCid( dto.getCid() );
        if ( mtEoHis.get_innerMap() != null ) {
            Map<String, Object> map1 = dto.get_innerMap();
            if ( map1 != null ) {
                mtEoHis.get_innerMap().putAll( map1 );
            }
        }
        if ( mtEoHis.get_tls() != null ) {
            Map<String, Map<String, String>> map2 = dto.get_tls();
            if ( map2 != null ) {
                mtEoHis.get_tls().putAll( map2 );
            }
        }

        return mtEoHis;
    }
}
