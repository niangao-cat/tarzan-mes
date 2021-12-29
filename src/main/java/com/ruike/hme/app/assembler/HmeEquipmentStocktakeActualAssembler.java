package com.ruike.hme.app.assembler;

import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeActualModifyCommand;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.entity.HmeEquipmentStocktakeActual;
import org.springframework.stereotype.Component;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.NO;

/**
 * <p>
 * 设备盘点实际 转换器
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 14:56
 */
@Component
public class HmeEquipmentStocktakeActualAssembler {

    public HmeEquipmentStocktakeActual equipmentToEntity(String stocktakeId, HmeEquipment equipment) {
        return new HmeEquipmentStocktakeActual(stocktakeId, equipment.getEquipmentId(), NO, equipment.getTenantId());
    }

    public HmeEquipmentStocktakeActual modifyCommandToEntity(HmeEquipmentStocktakeActualModifyCommand command) {
        HmeEquipmentStocktakeActual entity = new HmeEquipmentStocktakeActual();
        entity.setStocktakeActualId(command.getStocktakeActualId());
        entity.setRemark(command.getRemark());
        return entity;
    }
}
