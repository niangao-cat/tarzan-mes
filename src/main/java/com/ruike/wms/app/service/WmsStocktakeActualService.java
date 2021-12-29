package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsStocktakeActualExportDTO;
import com.ruike.wms.api.dto.WmsStocktakeActualQueryDTO;
import com.ruike.wms.api.dto.WmsStocktakeValidationDTO;
import com.ruike.wms.domain.vo.WmsStocktakeActualVO;
import com.ruike.wms.domain.vo.WmsStocktakeMaterialLotVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

import java.util.List;

/**
 * 库存盘点实绩 服务
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 19:15
 */
public interface WmsStocktakeActualService {

    /**
     * 调整库存盘点单
     *
     * @param tenantId              租户
     * @param stocktakeActualIdList 调整列表
     * @param stocktakeId           盘点单Id
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 09:21:32
     */
    void adjust(Long tenantId, String stocktakeId, List<String> stocktakeActualIdList);

    /**
     * 批量插入
     *
     * @param tenantId          租户ID
     * @param stocktakeId       盘点ID
     * @param eventRequestId    事件请求ID
     * @param materialLotIdList 物料批列表
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/30 10:12:27
     */
    void batchInsert(Long tenantId, String stocktakeId, String eventRequestId, List<String> materialLotIdList);

    /**
     * 根据条件查询列表
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeActualVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 05:07:48
     */
    Page<WmsStocktakeActualVO> pageAndSort(Long tenantId, WmsStocktakeActualQueryDTO dto, PageRequest pageRequest);

    /**
     * 通过装载对象插入物料批
     *
     * @param tenantId 租户
     * @param dto
     * @return java.util.List<com.ruike.wms.domain.vo.WmsStocktakeActualVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/17 07:24:45
     */
    List<WmsStocktakeActualVO> insertByLoadObject(Long tenantId, WmsStocktakeValidationDTO dto);

    /**
     * 导出
     *
     * @param tenantId    租户
     * @param exportParam 导出参数
     * @param condition   查询条件
     * @return java.util.List<com.ruike.wms.api.dto.WmsStocktakeActualExportDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/19 08:00:57
     */
    List<WmsStocktakeActualExportDTO> export(Long tenantId, ExportParam exportParam, WmsStocktakeActualQueryDTO condition);
}
