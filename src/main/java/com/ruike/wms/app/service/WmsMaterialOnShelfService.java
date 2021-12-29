package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.vo.WmsInstructionVO;
import org.hzero.core.base.AopProxy;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * 物料上架功能 应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-06-09 14:55
 */
public interface WmsMaterialOnShelfService extends AopProxy<WmsMaterialOnShelfService> {


    /**
     * 单据扫码查询
     *
     * @param tenantId
     * @param instructionDocNum
     * @return
     */
    WmsMaterialOnShelfDocDTO queryInstructionDocByNum(Long tenantId, String instructionDocNum);

    /**
     * 扫描条码(查询)
     *
     * @param tenantId
     * @param dto
     * @return
     */
    WmsMaterialOnShelfDTO queryBarcode(Long tenantId, WmsMaterialOnShelfBarCodeDTO2 dto);

    /**
     * 扫描货位条码(查询)
     *
     * @param tenantId
     * @param locatorCode
     * @param dto
     * @return
     */
    WmsMaterialOnShelfDTO queryLocatorByCode(Long tenantId, String locatorCode, WmsMaterialOnShelfDTO dto);

    /**
     * 执行
     *
     * @param tenantId
     * @param dtoList
     * @return
     */
    List<WmsMaterialOnShelfExecuteDTO3> execute(Long tenantId, List<WmsMaterialOnShelfExecuteDTO3> dtoList);

    /**
     * 明细
     *
     * @param tenantId
     * @param instructionId
     * @return
     */
    List<WmsMaterialOnShelfBarCodeDTO> detail(Long tenantId, String instructionId);
}
