package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.domain.entity.WmsPoDeliveryRel;
import com.ruike.wms.domain.repository.WmsPoDeliveryRelRepository;
import com.ruike.wms.domain.vo.WmsInstructionAttrVO;
import com.ruike.wms.infra.mapper.WmsPoDeliveryRelMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;

/**
 * 采购送货单关系 资源库实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/17 12:56
 */
@Component
public class WmsPoDeliveryRelRepositoryImpl extends BaseRepositoryImpl<WmsPoDeliveryRel> implements WmsPoDeliveryRelRepository {
    private WmsPoDeliveryRelMapper wmsPoDeliveryRelMapper;

    public WmsPoDeliveryRelRepositoryImpl(WmsPoDeliveryRelMapper wmsPoDeliveryRelMapper) {
        this.wmsPoDeliveryRelMapper = wmsPoDeliveryRelMapper;
    }

    @Override
    public WmsInstructionAttrVO selectLineByBarcodeAndDocNum(Long tenantId, String instructionDocNum, String materialLotId) {
        return wmsPoDeliveryRelMapper.selectLineByBarcodeAndDocNum(tenantId, instructionDocNum, materialLotId);
    }
}
