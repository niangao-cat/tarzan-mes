package com.ruike.wms.domain.repository;

import com.ruike.wms.domain.entity.WmsPoDeliveryRel;
import com.ruike.wms.domain.vo.WmsInstructionAttrVO;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

/**
 * 采购送货单关系 资源库
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/17 12:55
 */
public interface WmsPoDeliveryRelRepository extends BaseRepository<WmsPoDeliveryRel> {

    /**
     * 根据条码和单据号查询送货单详情
     *
     * @param tenantId          租户
     * @param instructionDocNum 送货单号
     * @param materialLotId     条码ID
     * @return com.ruike.wms.domain.vo.WmsInstructionAttrVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/2 11:25:23
     */
    WmsInstructionAttrVO selectLineByBarcodeAndDocNum(Long tenantId, String instructionDocNum, String materialLotId);
}
