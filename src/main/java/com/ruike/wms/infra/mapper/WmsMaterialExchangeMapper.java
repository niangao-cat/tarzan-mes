package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsMaterialExchangeDocDTO;
import com.ruike.wms.api.dto.WmsMaterialExchangeDocLineDTO;
import com.ruike.wms.api.dto.WmsMaterialExchangeParamDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 料废调换查询Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-05-18 12:55
 */
public interface WmsMaterialExchangeMapper {

    /**
     * 单据查询
     * @author jiangling.zheng@hand-china.com 2020-05-18 12:55
     * @param dto
     * @return List<com.ruike.wms.api.dto.WmsMaterialExchangeDocDTO>
     */
    List<WmsMaterialExchangeDocDTO> selectDocByConditionForUi(WmsMaterialExchangeParamDTO dto);

    /**
     * 单据行查询
     * @author jiangling.zheng@hand-china.com 2020-05-18 12:55
     * @param tenantId
     * @param instructionDocId
     * @return List<com.ruike.wms.api.dto.WmsMaterialExchangeDocLineDTO>
     */
    List<WmsMaterialExchangeDocLineDTO> selectLineByConditionForUi(@Param("tenantId") Long tenantId,
                                                                   @Param("instructionDocId") String instructionDocId);
}
