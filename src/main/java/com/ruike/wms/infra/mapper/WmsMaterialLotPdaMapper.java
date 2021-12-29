package com.ruike.wms.infra.mapper;

import org.apache.ibatis.annotations.Param;
import com.ruike.wms.api.dto.WmsMaterialLotDTO3;
import com.ruike.wms.api.dto.WmsMaterialLotDTO4;

import java.util.List;

/**
 * PDA条码管理Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-04-03 11:15:27
 */
public interface WmsMaterialLotPdaMapper {

    /**
     * 工单撤销
     * @author jiangling.zheng@hand-china.com 2020-04-03 11:15:27
     * @param tenantId
     * @param dto
     * @return
     */
    List<WmsMaterialLotDTO3> selectBarCodeCondition(@Param("tenantId") Long tenantId, @Param("dto") WmsMaterialLotDTO4 dto);

}
