package com.ruike.wms.domain.repository;

import java.util.List;
import java.util.Map;

import com.ruike.wms.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * WmsMaterialPostingRepository
 *
 * @author liyuan.lv@hand-china.com 2020/06/13 11:42
 */
public interface WmsMaterialPostingRepository {

    /**
     * 查询物料过账
     *
     * @param tenantId    租户Id
     * @param dto         查询参数
     * @param pageRequest 分页参数
     * @return WmsInstructionLineVO
     */
    Page<WmsInstructionLineVO> materialPostingQuery(Long tenantId, WmsMaterialPostingVO dto, PageRequest pageRequest);

    /**
     * 根据指令获取转移数据
     *
     * @param tenantId 租户
     * @param idList   指令ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInstructionLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/4 05:10:41
     */
    List<WmsInstructionLineVO> selectTransInstructionByIdList(Long tenantId,
                                                              List<String> idList);

    /**
     * 根据送货单行获取采购订单信息
     *
     * @param tenantId   租户
     * @param deliveryId 送货单行
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDeliveryPoRelVo>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 10:01:10
     */
    List<WmsDeliveryPoRelVo> selectPoByDeliveryId(Long tenantId,
                                                  String deliveryId);

    /**
     * 根据送货单行列表获取采购订单信息
     *
     * @param tenantId 租户
     * @param idList   送货单行列表
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDeliveryPoRelVo>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 10:01:10
     */
    Map<String, List<WmsDeliveryPoRelVo>> selectPoByDeliveryIdList(Long tenantId,
                                                                   List<String> idList);

    /**
     * 查询物料过账明细
     *
     * @param tenantId      租户Id
     * @param instructionId 单据Id
     * @return WmsMaterialLotLineVO
     */
    List<WmsMaterialLotLineVO> detailQuery(Long tenantId, String instructionId);

    /**
     * @param tenantId 租户ID
     * @param dtoList  过账数据
     * @return List<WmsInstructionLineVO>
     */
    List<WmsInstructionLineVO> executePosting(Long tenantId, List<WmsInstructionLineVO> dtoList);
}
