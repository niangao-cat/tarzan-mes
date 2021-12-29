package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsCheckScrapDocLineDTO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Mapper
 *
 * @author jiangling.zheng 2020/08/26 14:51
 */
public interface QmsCheckScrapMapper {

    /**
     * 根据物料批查询送货单单据
     * @author jiangling.zheng@hand-china.com 2020-08-26 14:59
     * @param tenantId
     * @param materialLotId
     * @return
     */
    List<QmsCheckScrapDocLineDTO> selectDocCondition(@Param("tenantId") Long tenantId,
                                                     @Param("materialLotId") String materialLotId);
    /**
     * 根据物料批查询送货单单据
     * @author jiangling.zheng@hand-china.com 2020-08-26 14:59
     * @param tenantId
     * @param instructionId
     * @return
     */
    BigDecimal sumScrapQtyGet(@Param("tenantId") Long tenantId,
                              @Param("instructionId") String instructionId);
}
