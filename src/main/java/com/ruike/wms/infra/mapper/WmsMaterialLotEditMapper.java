package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsMaterialLotEditDTO;
import com.ruike.wms.api.dto.WmsMaterialLotEditResponseDTO;
import com.ruike.wms.api.dto.WmsMaterialLotEditUomDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Classname MaterialLotEditMapper
 * @Description 条码调整Mapper
 * @Date 2020/03/17 09:29
 * @Author kun.zhou
 */
public interface WmsMaterialLotEditMapper {

    /**
     * 查询条码调整数据
     *
     * @param tenantId
     * @param dto
     * @return io.choerodon.core.domain.Page(com.superlighting.hwms.api.controller.dto.MaterialLotEditResponseDTO)
     * @Description 查询条码调整数据
     * @date 2020/03/17 09:44
     * @auther kun.zhou
     */
    List<WmsMaterialLotEditResponseDTO> queryMaterialLotEdit(@Param("tenantId") Long tenantId,
                                                             @Param("dto") WmsMaterialLotEditDTO dto);

    /**
     * 查询UOM数据
     *
     * @param tenantId
     * @return io.choerodon.core.domain.Page(com.superlighting.hwms.api.controller.dto.MaterialLotEditUomDTO)
     * @Description 查询UOM数据
     * @date 2020/03/17 09:44
     * @auther kun.zhou
     */
    List<WmsMaterialLotEditUomDTO> queryUom(@Param("tenantId") Long tenantId);
}
