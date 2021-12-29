package tarzan.order.domain.trans.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.entity.MtWorkOrderHis;
import tarzan.order.domain.trans.MtWorkOrderTransMapper;
import tarzan.order.domain.vo.MtWorkOrderVO36;
import tarzan.order.domain.vo.MtWorkOrderVO66;

@Component
public class MtWorkOrderTransMapperImpl implements MtWorkOrderTransMapper {

    @Override
    public MtWorkOrderVO36 mtWorkOrderToVO36(MtWorkOrder dto) {
        if (dto == null) {
            return null;
        }

        MtWorkOrderVO36 mtWorkOrderVO36 = new MtWorkOrderVO36();

        mtWorkOrderVO36.setWorkOrderId(dto.getWorkOrderId());
        mtWorkOrderVO36.setWorkOrderNum(dto.getWorkOrderNum());
        mtWorkOrderVO36.setWorkOrderType(dto.getWorkOrderType());
        mtWorkOrderVO36.setSiteId(dto.getSiteId());
        mtWorkOrderVO36.setProductionLineId(dto.getProductionLineId());
        mtWorkOrderVO36.setWorkcellId(dto.getWorkcellId());
        // mtWorkOrderVO36.setMakeOrderNum( dto.getMakeOrderNum() );
        mtWorkOrderVO36.setProductionVersion(dto.getProductionVersion());
        mtWorkOrderVO36.setMaterialId(dto.getMaterialId());
        mtWorkOrderVO36.setQty(dto.getQty());
        mtWorkOrderVO36.setUomId(dto.getUomId());
        mtWorkOrderVO36.setPriority(dto.getPriority());
        mtWorkOrderVO36.setStatus(dto.getStatus());
        mtWorkOrderVO36.setLastWoStatus(dto.getLastWoStatus());
        mtWorkOrderVO36.setPlanStartTime(dto.getPlanStartTime());
        mtWorkOrderVO36.setPlanEndTime(dto.getPlanEndTime());
        mtWorkOrderVO36.setLocatorId(dto.getLocatorId());
        mtWorkOrderVO36.setBomId(dto.getBomId());
        mtWorkOrderVO36.setRouterId(dto.getRouterId());
        mtWorkOrderVO36.setValidateFlag(dto.getValidateFlag());
        mtWorkOrderVO36.setRemark(dto.getRemark());
        mtWorkOrderVO36.setOpportunityId(dto.getOpportunityId());
        mtWorkOrderVO36.setCustomerId(dto.getCustomerId());
        mtWorkOrderVO36.setCompleteControlType(dto.getCompleteControlType());
        mtWorkOrderVO36.setCompleteControlQty(String.valueOf(dto.getCompleteControlQty()));
        mtWorkOrderVO36.setSourceIdentificationId(dto.getSourceIdentificationId());

        return mtWorkOrderVO36;
    }

    @Override
    public MtWorkOrder mtWorkOrderVO66ToMtWorkOrder(MtWorkOrderVO66 dto) {
        if (dto == null) {
            return null;
        }

        MtWorkOrder mtWorkOrder = new MtWorkOrder();

        mtWorkOrder.setWorkOrderId(dto.getWorkOrderId());
        mtWorkOrder.setWorkOrderNum(dto.getWorkOrderNum());
        mtWorkOrder.setWorkOrderType(dto.getWorkOrderType());
        mtWorkOrder.setSiteId(dto.getSiteId());
        mtWorkOrder.setProductionLineId(dto.getProductionLineId());
        mtWorkOrder.setWorkcellId(dto.getWorkcellId());
        // mtWorkOrder.setMakeOrderNum( dto.getMakeOrderNum() );
        mtWorkOrder.setProductionVersion(dto.getProductionVersion());
        mtWorkOrder.setMaterialId(dto.getMaterialId());
        mtWorkOrder.setQty(dto.getQty());
        mtWorkOrder.setUomId(dto.getUomId());
        mtWorkOrder.setPriority(dto.getPriority());
        mtWorkOrder.setStatus(dto.getStatus());
        mtWorkOrder.setLastWoStatus(dto.getLastWoStatus());
        mtWorkOrder.setPlanStartTime(dto.getPlanStartTime());
        mtWorkOrder.setPlanEndTime(dto.getPlanEndTime());
        mtWorkOrder.setLocatorId(dto.getLocatorId());
        mtWorkOrder.setBomId(dto.getBomId());
        mtWorkOrder.setRouterId(dto.getRouterId());
        mtWorkOrder.setValidateFlag(dto.getValidateFlag());
        mtWorkOrder.setRemark(dto.getRemark());
        mtWorkOrder.setOpportunityId(dto.getOpportunityId());
        mtWorkOrder.setCustomerId(dto.getCustomerId());
        mtWorkOrder.setCompleteControlType(dto.getCompleteControlType());
        mtWorkOrder.setCompleteControlQty(dto.getCompleteControlQty());
        mtWorkOrder.setSourceIdentificationId(dto.getSourceIdentificationId());

        return mtWorkOrder;
    }

    @Override
    public MtWorkOrderVO66 mtWorkOrderToMtWorkOrderVO66(MtWorkOrder dto) {
        if (dto == null) {
            return null;
        }

        MtWorkOrderVO66 mtWorkOrderVO66 = new MtWorkOrderVO66();

        mtWorkOrderVO66.setWorkOrderId(dto.getWorkOrderId());
        mtWorkOrderVO66.setWorkOrderNum(dto.getWorkOrderNum());
        mtWorkOrderVO66.setWorkOrderType(dto.getWorkOrderType());
        mtWorkOrderVO66.setSiteId(dto.getSiteId());
        mtWorkOrderVO66.setProductionLineId(dto.getProductionLineId());
        mtWorkOrderVO66.setWorkcellId(dto.getWorkcellId());
        // mtWorkOrderVO66.setMakeOrderNum( dto.getMakeOrderNum() );
        mtWorkOrderVO66.setProductionVersion(dto.getProductionVersion());
        mtWorkOrderVO66.setMaterialId(dto.getMaterialId());
        mtWorkOrderVO66.setQty(dto.getQty());
        mtWorkOrderVO66.setUomId(dto.getUomId());
        mtWorkOrderVO66.setPriority(dto.getPriority());
        mtWorkOrderVO66.setStatus(dto.getStatus());
        mtWorkOrderVO66.setLastWoStatus(dto.getLastWoStatus());
        mtWorkOrderVO66.setPlanStartTime(dto.getPlanStartTime());
        mtWorkOrderVO66.setPlanEndTime(dto.getPlanEndTime());
        mtWorkOrderVO66.setLocatorId(dto.getLocatorId());
        mtWorkOrderVO66.setBomId(dto.getBomId());
        mtWorkOrderVO66.setRouterId(dto.getRouterId());
        mtWorkOrderVO66.setValidateFlag(dto.getValidateFlag());
        mtWorkOrderVO66.setRemark(dto.getRemark());
        mtWorkOrderVO66.setOpportunityId(dto.getOpportunityId());
        mtWorkOrderVO66.setCustomerId(dto.getCustomerId());
        mtWorkOrderVO66.setCompleteControlType(dto.getCompleteControlType());
        mtWorkOrderVO66.setCompleteControlQty(dto.getCompleteControlQty());
        mtWorkOrderVO66.setSourceIdentificationId(dto.getSourceIdentificationId());

        return mtWorkOrderVO66;
    }

    @Override
    public MtWorkOrderHis mtWorkOrderToMtWorkOrderHis(MtWorkOrder dto) {
        if (dto == null) {
            return null;
        }

        MtWorkOrderHis mtWorkOrderHis = new MtWorkOrderHis();

        mtWorkOrderHis.setCreationDate(dto.getCreationDate());
        mtWorkOrderHis.setCreatedBy(dto.getCreatedBy());
        mtWorkOrderHis.setLastUpdateDate(dto.getLastUpdateDate());
        mtWorkOrderHis.setLastUpdatedBy(dto.getLastUpdatedBy());
        mtWorkOrderHis.setObjectVersionNumber(dto.getObjectVersionNumber());
        mtWorkOrderHis.setTableId(dto.getTableId());
        mtWorkOrderHis.set_token(dto.get_token());
        Map<String, Object> map = dto.getFlex();
        if (map != null) {
            mtWorkOrderHis.setFlex(new HashMap<String, Object>(map));
        }
        mtWorkOrderHis.setTenantId(dto.getTenantId());
        mtWorkOrderHis.setWorkOrderId(dto.getWorkOrderId());
        mtWorkOrderHis.setWorkOrderNum(dto.getWorkOrderNum());
        mtWorkOrderHis.setWorkOrderType(dto.getWorkOrderType());
        mtWorkOrderHis.setSiteId(dto.getSiteId());
        mtWorkOrderHis.setProductionLineId(dto.getProductionLineId());
        mtWorkOrderHis.setWorkcellId(dto.getWorkcellId());
        // mtWorkOrderHis.setMakeOrderNum( dto.getMakeOrderNum() );
        mtWorkOrderHis.setProductionVersion(dto.getProductionVersion());
        mtWorkOrderHis.setMaterialId(dto.getMaterialId());
        mtWorkOrderHis.setQty(dto.getQty());
        mtWorkOrderHis.setUomId(dto.getUomId());
        mtWorkOrderHis.setPriority(dto.getPriority());
        mtWorkOrderHis.setStatus(dto.getStatus());
        mtWorkOrderHis.setLastWoStatus(dto.getLastWoStatus());
        mtWorkOrderHis.setPlanStartTime(dto.getPlanStartTime());
        mtWorkOrderHis.setPlanEndTime(dto.getPlanEndTime());
        mtWorkOrderHis.setLocatorId(dto.getLocatorId());
        mtWorkOrderHis.setBomId(dto.getBomId());
        mtWorkOrderHis.setRouterId(dto.getRouterId());
        mtWorkOrderHis.setValidateFlag(dto.getValidateFlag());
        mtWorkOrderHis.setRemark(dto.getRemark());
        mtWorkOrderHis.setOpportunityId(dto.getOpportunityId());
        mtWorkOrderHis.setCustomerId(dto.getCustomerId());
        mtWorkOrderHis.setCompleteControlType(dto.getCompleteControlType());
        mtWorkOrderHis.setCompleteControlQty(dto.getCompleteControlQty());
        mtWorkOrderHis.setSourceIdentificationId(dto.getSourceIdentificationId());
        mtWorkOrderHis.setCid(dto.getCid());
        if (mtWorkOrderHis.get_innerMap() != null) {
            Map<String, Object> map1 = dto.get_innerMap();
            if (map1 != null) {
                mtWorkOrderHis.get_innerMap().putAll(map1);
            }
        }
        if (mtWorkOrderHis.get_tls() != null) {
            Map<String, Map<String, String>> map2 = dto.get_tls();
            if (map2 != null) {
                mtWorkOrderHis.get_tls().putAll(map2);
            }
        }

        return mtWorkOrderHis;
    }
}
