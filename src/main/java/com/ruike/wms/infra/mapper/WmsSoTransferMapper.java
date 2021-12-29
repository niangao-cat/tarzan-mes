package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsSoTransferDTO;
import com.ruike.wms.api.dto.WmsSoTransferReturnDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName WmsSoTransferMapper
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/22 11:36
 * @Version 1.0
 **/
public interface WmsSoTransferMapper {

    List<WmsSoTransferReturnDTO> querySo(@Param("tenantId") Long tenantId, @Param("dto")WmsSoTransferDTO dto);
}
