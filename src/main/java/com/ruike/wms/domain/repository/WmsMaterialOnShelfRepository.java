package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.*;
import org.hzero.core.base.AopProxy;

import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-06-09 14:59
 */
public interface WmsMaterialOnShelfRepository extends AopProxy<WmsMaterialOnShelfRepository> {

    /**
     * 条码查询
     * @param tenantId
     * @param materialLotIds
     * @return WmsMaterialOnShelfBarCodeDTO
     * @Date 2020-06-09 14:59
     * @author jiangling.zheng@hand-china.com
     */
    List<WmsMaterialOnShelfBarCodeDTO> selectMaterialLotCondition(Long tenantId, List<String> materialLotIds);

}
