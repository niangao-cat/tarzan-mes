package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsMaterialOnShelfBarCodeDTO;
import com.ruike.wms.api.dto.WmsMaterialOnShelfDocLineDTO;
import com.ruike.wms.api.dto.WmsMaterialOnShelfInsActualDTO;
import com.ruike.wms.api.dto.WmsMaterialOnShelfMatLotDTO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 物料上架Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-06-09 14:59
 */
public interface WmsMaterialOnShelfMapper {

    /**
     * 物料批查询
     * @author jiangling.zheng@hand-china.com 2020-06-09 14:59
     * @param tenantId
     * @param materialLotIds
     * @return
     */
    List<WmsMaterialOnShelfBarCodeDTO> selectMaterialLotCondition(@Param("tenantId") Long tenantId,
                                                                  @Param("materialLotIds") List<String> materialLotIds);

    /**
     * 根据物料批查询送货单单据
     * @author jiangling.zheng@hand-china.com 2020-06-09 14:59
     * @param tenantId
     * @param materialLotIds
     * @return
     */
    List<WmsMaterialOnShelfDocLineDTO> selectDocCondition(@Param("tenantId") Long tenantId,
                                                          @Param("materialLotIds") List<String> materialLotIds);

    /**
     * 根据物料批查询入库单单据
     *
     * @param tenantId
     * @param materialLotIds
     * @author jiangling.zheng@hand-china.com 2020/8/18 19:50
     * @return java.util.List<com.ruike.wms.api.dto.WmsMaterialOnShelfDocLineDTO>
     */

    List<WmsMaterialOnShelfDocLineDTO> selectInDocCondition(@Param("tenantId") Long tenantId,
                                                            @Param("materialLotIds") List<String> materialLotIds);

    /**
     * 获取执行数
     *
     * @param tenantId
     * @param instructionIds
     * @author jiangling.zheng@hand-china.com 2021/1/4 19:42
     * @return java.util.List<com.ruike.wms.api.dto.WmsMaterialOnShelfInsActualDTO>
     */

    List<WmsMaterialOnShelfInsActualDTO> executeQtyQuery(@Param("tenantId") Long tenantId,
                                                         @Param("instructionIds") List<String> instructionIds);

    /**
     * 获取物料批
     *
     * @param tenantId
     * @param instructionIds
     * @author jiangling.zheng@hand-china.com 2021/1/4 19:42
     * @return java.util.List<com.ruike.wms.api.dto.WmsMaterialOnShelfMatLotDTO>
     */

    List<WmsMaterialOnShelfMatLotDTO> materialLotQuery(@Param("tenantId") Long tenantId,
                                                       @Param("instructionIds") List<String> instructionIds);
}
