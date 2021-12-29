package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeActualModifyCommand;
import com.ruike.hme.app.assembler.HmeEquipmentStocktakeActualAssembler;
import com.ruike.hme.app.service.HmeEquipmentStocktakeActualService;
import com.ruike.hme.domain.repository.HmeEquipmentStocktakeActualRepository;
import org.springframework.stereotype.Service;

/**
 * 设备盘点实际应用服务默认实现
 *
 * @author yonghui.zhu@hand-china.com 2021-03-31 09:32:46
 */
@Service
public class HmeEquipmentStocktakeActualServiceImpl implements HmeEquipmentStocktakeActualService {
    private final HmeEquipmentStocktakeActualRepository repository;
    private final HmeEquipmentStocktakeActualAssembler assembler;

    public HmeEquipmentStocktakeActualServiceImpl(HmeEquipmentStocktakeActualRepository repository, HmeEquipmentStocktakeActualAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @Override
    public void update(HmeEquipmentStocktakeActualModifyCommand command) {
        repository.save(assembler.modifyCommandToEntity(command));
    }
}
