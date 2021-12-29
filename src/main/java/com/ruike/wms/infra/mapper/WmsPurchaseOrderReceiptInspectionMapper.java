package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsPurchaseOrderReceiptInspectionDTO;
import com.ruike.wms.domain.vo.WmsPurchaseOrderReceiptInspectionVO;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/09 13:49
 */
public interface WmsPurchaseOrderReceiptInspectionMapper {

    List<WmsPurchaseOrderReceiptInspectionVO> queryList(@Param("tenantId") Long tenantId,
                                                        @Param("dto")WmsPurchaseOrderReceiptInspectionDTO dto);
}
