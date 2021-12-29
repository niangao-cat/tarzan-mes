package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsSoTransferDTO;
import com.ruike.wms.api.dto.WmsSoTransferDTO2;
import com.ruike.wms.api.dto.WmsSoTransferReturnDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

public interface WmsSoTransferService {

    Page<WmsSoTransferReturnDTO> querySo(Long tenantId, PageRequest pageRequest, WmsSoTransferDTO dto);


    void confirmSo(Long tenantId, List<WmsSoTransferDTO2> dtos);
}
