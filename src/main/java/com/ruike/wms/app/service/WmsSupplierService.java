package com.ruike.wms.app.service;

import com.ruike.wms.domain.vo.WmsSupplierVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.modeling.domain.entity.MtSupplier;

/**
 * WmsSupplierService
 *
 * @author liyuan.lv@hand-china.com 2020/05/06 16:47
 */
public interface WmsSupplierService {
    /**
     * 界面查询
     *
     * @param tenantId    租户ID
     * @param dto         查询参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<tarzan.modeling.domain.entity.MtSupplier>
     */
    Page<MtSupplier> uiQuery(Long tenantId, WmsSupplierVO dto, PageRequest pageRequest);
}
