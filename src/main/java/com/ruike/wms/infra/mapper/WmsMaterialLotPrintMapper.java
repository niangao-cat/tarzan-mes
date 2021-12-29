package com.ruike.wms.infra.mapper;

import java.util.List;

import com.ruike.wms.domain.vo.WmsMaterialLotPrintVO;
import org.apache.ibatis.annotations.Param;

/**
 * 条码打印Mapper
 *
 * @author penglin.sui@hand-china.com 2020-07-29 20:58
 */
public interface WmsMaterialLotPrintMapper {
    List<WmsMaterialLotPrintVO> materialLotPrint(@Param(value = "tenantId") Long tenantId, @Param(value = "materialLotIds") List<String> materialLotIds);
}
