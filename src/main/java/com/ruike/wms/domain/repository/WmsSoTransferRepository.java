package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsSoTransferDTO;
import com.ruike.wms.api.dto.WmsSoTransferReturnDTO;

import java.util.List;

/**
 * @ClassName WmsSoTransferRepository
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/22 11:24
 * @Version 1.0
 **/
public interface WmsSoTransferRepository {

    List<WmsSoTransferReturnDTO> querySo(Long tenantId, WmsSoTransferDTO dto);

}
