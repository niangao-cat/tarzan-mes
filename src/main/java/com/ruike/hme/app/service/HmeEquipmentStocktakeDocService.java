package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeDocActionCommand;
import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeDocModifyCommand;
import com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeDocRepresentation;
import com.ruike.hme.domain.vo.HmeEquipmentVO;

/**
 * 设备盘点单应用服务
 *
 * @author yonghui.zhu@hand-china.com 2021-03-31 09:32:46
 */
public interface HmeEquipmentStocktakeDocService {

    /**
     * 更新单据
     *
     * @param command 命令
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 04:37:22
     */
    void update(HmeEquipmentStocktakeDocModifyCommand command);

    /**
     * 完成单据
     *
     * @param command 命令
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 04:37:22
     */
    void complete(HmeEquipmentStocktakeDocActionCommand command);

    /**
     * 取消单据
     *
     * @param command 命令
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 04:37:22
     */
    void cancel(HmeEquipmentStocktakeDocActionCommand command);

    /**
     * 创建盘点单
     *
     * @param tenantId
     * @param equipmentVO
     * @author sanfeng.zhang@hand-china.com 2021/11/25 7:29
     * @return com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeDocRepresentation
     */
    HmeEquipmentStocktakeDocRepresentation createDoc(Long tenantId, HmeEquipmentVO equipmentVO);
}
