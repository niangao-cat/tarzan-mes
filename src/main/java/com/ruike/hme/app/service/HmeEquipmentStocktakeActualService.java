package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeActualModifyCommand;

/**
 * 设备盘点实际应用服务
 *
 * @author yonghui.zhu@hand-china.com 2021-03-31 09:32:46
 */
public interface HmeEquipmentStocktakeActualService {

    /**
     * 更新
     *
     * @param command 更新命令
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/1 03:16:30
     */
    void update(HmeEquipmentStocktakeActualModifyCommand command);
}
