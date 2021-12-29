package com.ruike.hme.domain.service;

import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeDocCreateCommand;
import com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeDocRepresentation;

/**
 * <p>
 * 设备盘点 领域服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 11:13
 */
public interface HmeEquipmentStocktakeDomainService {

    /**
     * 创建单据
     *
     * @param command 命令
     * @return com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeDocRepresentation
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 11:15:22
     */
    HmeEquipmentStocktakeDocRepresentation createDoc(HmeEquipmentStocktakeDocCreateCommand command);

    /**
     * 完成校验
     *
     * @param stocktakeId 单据ID
     * @return com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeDocRepresentation
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 11:15:22
     */
    Boolean completeValid(String stocktakeId);
}
