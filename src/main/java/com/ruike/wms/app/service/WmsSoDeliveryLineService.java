package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsSoDeliverySubmitDTO;

import java.util.List;

/**
 * <p>
 * 出货单行 服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 21:21
 */
public interface WmsSoDeliveryLineService {

    /**
     * 批量新增
     *
     * @param tenantId 租户
     * @param dto      批量保存数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 09:24:07
     */
    void batchInsertOrUpdate(Long tenantId, WmsSoDeliverySubmitDTO dto);

    /**
     * 批量更新状态
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @param eventId          事件ID
     * @param status           更新状态
     * @param idList           行ID列表
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 09:24:07
     */
    void batchUpdateStatus(Long tenantId, String instructionDocId, List<String> idList, String eventId, String status);

    /**
     * 取消
     *
     * @param tenantId      租户
     * @param instructionId 行ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/10 11:27:06
     */
    void cancel(Long tenantId, String instructionId);
}
