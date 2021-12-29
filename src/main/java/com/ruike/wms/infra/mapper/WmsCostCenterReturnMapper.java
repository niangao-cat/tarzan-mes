package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsMaterialReturnScanTypeDTO;
import feign.Param;
import tarzan.modeling.domain.vo.MtModLocatorVO7;
import tarzan.modeling.domain.vo.MtModLocatorVO8;

import java.util.List;

public interface WmsCostCenterReturnMapper {
    /**
     * 单据扫码查询
     * @author jiangling.zheng@hand-china.com 2020-04-15 15:28
     * @param tenantId
     * @param instructionDocNum
     * @return
     */
    WmsMaterialReturnScanTypeDTO selectDocType(@Param("tenantId") Long tenantId,
                                                      @Param("instructionDocNum") String instructionDocNum);

    List<MtModLocatorVO8> getLocatorQuery(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "dto") MtModLocatorVO7 dto);

    /**
     *
     * @Description 根据事务编码查询移动类型
     *
     * @author yuchao.wang
     * @date 2020/9/10 17:27
     * @param tenantId 租户ID
     * @param transactionTypeCode 事务编码
     * @return java.lang.String
     *
     */
    String queryMoveTypeByTransactionTypeCode(@Param("tenantId") Long tenantId,
                                              @Param("transactionTypeCode") String transactionTypeCode);
}
