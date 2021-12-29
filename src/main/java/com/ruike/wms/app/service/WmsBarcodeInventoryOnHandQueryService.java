package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsBarcodeInventoryOnHandDetailQueryDTO;
import com.ruike.wms.api.dto.WmsBarcodeInventoryOnHandQueryDTO;
import com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandDetailVO;
import com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandQueryExportVO;
import com.ruike.wms.domain.vo.WmsBarcodeInventoryOnHandVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

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
public interface WmsBarcodeInventoryOnHandQueryService {

    /**
     * @description 条码库存现有量查询
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/13
     * @time 11:34
     * @version 0.0.1
     * @return io.choerodon.core.domain.Page<com.ruike.wms.api.dto.WmsPickReturnLineReceiveVO>
     */
    Page<WmsBarcodeInventoryOnHandVO> list(Long tenantId, PageRequest pageRequest, WmsBarcodeInventoryOnHandQueryDTO dto);


    /**
     * @description 条码库存现有量明细查询
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/13
     * @time 11:34
     * @version 0.0.1
     * @return io.choerodon.core.domain.Page<com.ruike.wms.api.dto.WmsPickReturnLineReceiveVO>
     */
    Page<WmsBarcodeInventoryOnHandDetailVO> listDetail(Long tenantId, PageRequest pageRequest, WmsBarcodeInventoryOnHandDetailQueryDTO dto);

    /**
     * @description 条码库存现有量导出数据查询
     * @param tenantId
     * @param dto
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/13
     * @time 11:34
     * @version 0.0.1
     * @return io.choerodon.core.domain.Page<com.ruike.wms.api.dto.WmsBarcodeInventoryOnHandQueryExportVO>
     */
    List<WmsBarcodeInventoryOnHandQueryExportVO> excelExport(Long tenantId, WmsBarcodeInventoryOnHandQueryDTO dto);


}
