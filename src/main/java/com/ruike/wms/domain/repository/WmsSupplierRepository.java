package com.ruike.wms.domain.repository;

import java.util.List;

import com.ruike.wms.domain.vo.WmsSupplierVO;
import tarzan.modeling.domain.entity.MtSupplier;

/**
 * WmsSupplierRepository
 *
 * @author liyuan.lv@hand-china.com 2020/05/06 17:01
 */
public interface WmsSupplierRepository {
    /**
     * 根据条件获取供应商
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtSupplier> selectByCondition(Long tenantId, WmsSupplierVO dto);
}
