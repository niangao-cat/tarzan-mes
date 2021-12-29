package com.ruike.hme.app.assembler;

import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeDocActionCommand;
import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeDocCreateCommand;
import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeDocModifyCommand;
import com.ruike.hme.domain.entity.HmeEquipmentStocktakeDoc;
import com.ruike.hme.infra.util.BeanCopierUtil;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.springframework.stereotype.Component;

import static com.ruike.hme.infra.constant.HmeConstants.EquipmentStocktakeStatus.*;
import static com.ruike.hme.infra.constant.HmeConstants.ObjectCode.EQUIPMENT_STOCKTAKE_DOC;
import static com.ruike.wms.infra.constant.WmsConstant.DocStatus.CANCEL;
import static com.ruike.wms.infra.constant.WmsConstant.DocStatus.COMPLETE;

/**
 * <p>
 * 设备盘点单 转换器
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 11:30
 */
@Component
public class HmeEquipmentStocktakeDocAssembler {
    private final CodeRuleBuilder codeRuleBuilder;

    public HmeEquipmentStocktakeDocAssembler(CodeRuleBuilder codeRuleBuilder) {
        this.codeRuleBuilder = codeRuleBuilder;
    }

    public HmeEquipmentStocktakeDoc createCommandToEntity(HmeEquipmentStocktakeDocCreateCommand command) {
        HmeEquipmentStocktakeDoc entity = new HmeEquipmentStocktakeDoc();
        BeanCopierUtil.copy(command, entity);
        if (StringUtils.isBlank(entity.getStocktakeId())) {
            String docNum = codeRuleBuilder.generateCode(EQUIPMENT_STOCKTAKE_DOC, null);
            entity.setStocktakeNum(docNum);
            entity.setStocktakeStatus(NEW);
        }
        return entity;
    }

    public HmeEquipmentStocktakeDoc modifyCommandToEntity(HmeEquipmentStocktakeDocModifyCommand command) {
        HmeEquipmentStocktakeDoc entity = new HmeEquipmentStocktakeDoc();
        BeanCopierUtil.copy(command, entity);
        return entity;
    }

    public HmeEquipmentStocktakeDoc actionCommandToEntity(HmeEquipmentStocktakeDocActionCommand command) {
        HmeEquipmentStocktakeDoc entity = new HmeEquipmentStocktakeDoc();
        entity.setStocktakeId(command.getStocktakeId());
        entity.setStocktakeStatus(CANCEL.equals(command.getAction()) ? CANCELLED : (COMPLETE.equals(command.getAction()) ? DONE : NEW));
        return entity;
    }
}
