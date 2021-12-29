package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 成本中心领料单执行Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-04-15 15:28
 */
public interface WmsCostCtrMaterialMapper {

    /**
     * 单据扫码查询
     * @author jiangling.zheng@hand-china.com 2020-04-15 15:28
     * @param tenantId
     * @param instructionDocNum
     * @return
     */
    WmsCostCtrMaterialDTO selectDocCondition(@Param("tenantId") Long tenantId,
                                                   @Param("instructionDocNum") String instructionDocNum);

    /**
     * 单据行查询
     * @author jiangling.zheng@hand-china.com 2020-04-15 15:28
     * @param tenantId
     * @param sourceDocId
     * @param instructionId
     * @return
     */
    List<WmsCostCtrMaterialDTO2> selectDocLineCondition(@Param("tenantId") Long tenantId,
                                                        @Param("sourceDocId") String sourceDocId,
                                                        @Param("instructionId") String instructionId);

    /**
     * 物料批查询
     * @author jiangling.zheng@hand-china.com 2020-04-15 15:28
     * @param tenantId
     * @param materialLotIds
     * @return
     */
    List<WmsCostCtrMaterialDTO3> selectMaterialLotCondition(@Param("tenantId") Long tenantId,
                                                            @Param("materialLotIds") List<String> materialLotIds);
    /**
     * 获取超发设置
     * @param tenantId
     * @param instructionId
     * @author wsg guijie.wu@hand-china.com 2020/7/21 20:30
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    Map<String, String> selectExcess(@Param("tenantId") Long tenantId, @Param("instructionId") String instructionId);

}
