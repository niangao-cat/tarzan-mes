package com.ruike.wms.app.service;

import com.ruike.wms.domain.vo.WmsMaterialVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * WmsMaterialService
 *
 * @author liyuan.lv@hand-china.com 2020/05/06 16:08
 */
public interface WmsMaterialService {

    /**
     * 根据条件获取物料
     * @param tenantId 租户ID
     * @param dto 物料参数
     * @return 物料
     */
    Page<WmsMaterialVO> siteLimitMaterialQuery(Long tenantId, WmsMaterialVO dto, PageRequest pageRequest);

    /**
     * 获取用户可访问站点下的物料
     * @param tenantId 租户ID
     * @param dto 物料参数
     * @return 物料
     */
    Page<WmsMaterialVO> userPermissionMaterialQuery(Long tenantId, WmsMaterialVO dto, PageRequest pageRequest);
}
