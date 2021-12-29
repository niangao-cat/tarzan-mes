package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsMaterialLotEditDTO;
import com.ruike.wms.api.dto.WmsMaterialLotEditResponseDTO;
import com.ruike.wms.api.dto.WmsMaterialLotEditUomDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * @author kun.zhou
 * @Classname MaterialLotEditService
 * @Description 条码调整SERVICE
 * @Date 2020-03-17 10:37
 */
public interface WmsMaterialLotEditService {

    /**
     * 查询条码数据queryMaterialLotEdit
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page(com.superlighting.hwms.api.controller.dto.MaterialLotEditResponseDTO)
     * @Description 查询条码数据queryMaterialLotEdit
     * @date 2020/03/17 10:40
     * @auther kun.zhou
     */
    Page<WmsMaterialLotEditResponseDTO> queryMaterialLotEdit(Long tenantId, WmsMaterialLotEditDTO dto, PageRequest pageRequest);

    /**
     * 更新条码数据
     *
     * @param tenantId
     * @param dtoList
     * @return io.choerodon.core.domain.Page(com.superlighting.hwms.api.controller.dto.MaterialLotEditDTO)
     * @Description 更新条码数据
     * @date 2020/03/17 16:48
     * @auther kun.zhou
     */
    Page<WmsMaterialLotEditDTO> updateMaterialLotData(Long tenantId, List<WmsMaterialLotEditResponseDTO> dtoList);

    /**
     * 查询单位数据queryMaterialLotEdit
     *
     * @param tenantId
     * @return io.choerodon.core.domain.Page(com.superlighting.hwms.api.controller.dto.MaterialLotEditUomDTO)
     * @Description 查询单位数据queryMaterialLotEdit
     * @date 2020/03/17 10:40
     * @auther kun.zhou
     */
    Page<WmsMaterialLotEditUomDTO> queryUom(Long tenantId, PageRequest pageRequest);


}
