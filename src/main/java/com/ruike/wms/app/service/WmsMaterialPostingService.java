package com.ruike.wms.app.service;

import java.util.List;

import com.ruike.wms.domain.vo.WmsDeliveryPoRelVo;
import com.ruike.wms.domain.vo.WmsInstructionLineVO;
import com.ruike.wms.domain.vo.WmsMaterialLotLineVO;
import com.ruike.wms.domain.vo.WmsMaterialPostingVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * WmsMaterialPostingService
 *
 * @author liyuan.lv@hand-china.com 2020/06/14 22:03
 */
public interface WmsMaterialPostingService {
    /**
     * 物料过账查询
     *
     * @param tenantId    租户ID
     * @param dto         查询参数
     * @param pageRequest 分页参数
     * @return Page<WmsInstructionLineVO>
     */
    Page<WmsInstructionLineVO> uiQuery(Long tenantId, WmsMaterialPostingVO dto, PageRequest pageRequest);

    /**
     * 物料过账明细查询
     *
     * @param tenantId      租户ID
     * @param instructionId 单据ID
     * @param pageRequest 分页参数
     * @return Page<WmsInstructionLineVO>
     */
    Page<WmsMaterialLotLineVO> detailUiQuery(Long tenantId, String instructionId, PageRequest pageRequest);

    /**
     * 物料过账
     *
     * @param tenantId 租户ID
     * @param dtoList  过账数据
     * @return List<WmsInstructionLineVO>
     */
    List<WmsInstructionLineVO> executePosting(Long tenantId, List<WmsInstructionLineVO> dtoList);

    /**
     * 根据送货单行获取采购订单信息
     *
     * @param tenantId   租户
     * @param deliveryId 送货单行
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDeliveryPoRelVo>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 10:01:10
     */
    Page<WmsDeliveryPoRelVo> poListQuery(Long tenantId, String deliveryId, PageRequest pageRequest);
}
