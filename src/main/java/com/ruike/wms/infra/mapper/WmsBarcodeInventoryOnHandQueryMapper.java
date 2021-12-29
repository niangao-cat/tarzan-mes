package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsBarcodeInventoryOnHandDetailQueryDTO;
import com.ruike.wms.api.dto.WmsBarcodeInventoryOnHandQueryDTO;
import com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandDetailVO;
import com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandQueryExportVO;
import com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description 条码库存现有量查询
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/13
 * @time 11:11
 * @version 0.0.1
 * @return
 */
public interface WmsBarcodeInventoryOnHandQueryMapper {

    /**
     * @description 头信息查询
     * @param tenantId
     * @param dto
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/13
     * @time 11:45
     * @version 0.0.1
     * @return java.util.List<com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandVO>
     */
    List<WmsBarcodeInventoryOnHandVO> headList(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") WmsBarcodeInventoryOnHandQueryDTO dto);


    /**
     * @description 明细信息查询
     * @param tenantId
     * @param dto
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/13
     * @time 11:45
     * @version 0.0.1
     * @return java.util.List<com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandDetailVO>
     */
    List<WmsBarcodeInventoryOnHandDetailVO> detailList(@Param(value = "tenantId") Long tenantId, @Param(value = "dto")WmsBarcodeInventoryOnHandDetailQueryDTO dto);

    /**
     * @description 头信息导出数据查询
     * @param tenantId
     * @param dto
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/13
     * @time 11:45
     * @version 0.0.1
     * @return java.util.List<com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandVO>
     */
    List<WmsBarcodeInventoryOnHandQueryExportVO> excelExport(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") WmsBarcodeInventoryOnHandQueryDTO dto);

}
