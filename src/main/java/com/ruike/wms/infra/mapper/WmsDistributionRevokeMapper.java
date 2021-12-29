package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO3;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WmsDistributionRevokeMapper {

    /**
     * @param instructionDocId
     * @return com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO3
     * @description 查询产线
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/8 21:08
     **/
    WmsDistributionRevokeReturnDTO3 prodLineQuery(@Param("instructionDocId") String instructionDocId);

    /**
     * @param instructionDocId
     * @return com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO3
     * @description 查询工段
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/8 21:13
     **/
    WmsDistributionRevokeReturnDTO3 WorkcellQuery(@Param("instructionDocId") String instructionDocId);


    String qtyquery(@Param(value = "materialLotIds") List<String> materialLotIds);


    String instructionIdQuery(@Param(value = "instructionDocId") String instructionDocId,
                              @Param(value = "materialLotId") String materialLotId);
}
