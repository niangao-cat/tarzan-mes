package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.api.dto.WmsSoTransferDTO;
import com.ruike.wms.api.dto.WmsSoTransferReturnDTO;
import com.ruike.wms.domain.repository.WmsSoTransferRepository;
import com.ruike.wms.infra.mapper.WmsSoTransferMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName WmsSoTransferRepositoryImpl
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/22 11:28
 * @Version 1.0
 **/
@Component
public class WmsSoTransferRepositoryImpl implements WmsSoTransferRepository {
    @Autowired
    private WmsSoTransferMapper wmsSoTransferMapper;

    @Override
    public List<WmsSoTransferReturnDTO> querySo(Long tenantId, WmsSoTransferDTO dto) {
        List<WmsSoTransferReturnDTO> wmsSoTransferReturnDTOS = wmsSoTransferMapper.querySo(tenantId, dto);
        for (WmsSoTransferReturnDTO temp:
        wmsSoTransferReturnDTOS) {
            temp.setQty(temp.getQty() == null ? "" : new BigDecimal(temp.getQty()).stripTrailingZeros().toPlainString());
        }
        return wmsSoTransferReturnDTOS;
    }
}
