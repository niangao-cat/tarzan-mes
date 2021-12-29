package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsPutInStorageDTO;
import com.ruike.wms.domain.vo.WmsInstructionVO;

/**
 * 入库上架功能 服务
 *
 * @author liyuan.lv@hand-china.com 2020/04/03 18:13
 */
public interface WmsPutInStorageService {

    /**
     * 扫描送货单校验
     *
     * @param tenantId
     * @param instructionDocNum
     * @return
     */
    WmsInstructionVO queryInstructionDocByNum(Long tenantId, String instructionDocNum);
    /**
     * 扫描单据条码(查询)
     *
     * @param tenantId
     * @param dto
     * @return WmsInstructionVO
     */
    WmsInstructionVO queryBarcode(Long tenantId, WmsPutInStorageDTO dto);

}
