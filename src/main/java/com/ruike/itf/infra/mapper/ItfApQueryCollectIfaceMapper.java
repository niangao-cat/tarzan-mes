package com.ruike.itf.infra.mapper;

import com.ruike.itf.api.dto.ApQueryCollectItfReturnDTO1;
import com.ruike.itf.api.dto.ApQueryCollectItfReturnDTO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItfApQueryCollectIfaceMapper {

    List<ApQueryCollectItfReturnDTO1> selectMaterial(@Param(value = "tenantId") Long tenantId,
                                                     @Param(value = "materialLotCodes") List<String> materialLotCodes);

    List<ApQueryCollectItfReturnDTO2> selectCurrent(@Param(value = "tenantId") Long tenantId,
                                                    @Param(value = "materialIds") List<String> materialIds);
}
