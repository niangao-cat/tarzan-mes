package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsMaterialLotDTO3;
import com.ruike.wms.api.dto.WmsMaterialLotDTO4;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * PDA条码管理应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-04-03 11:15:27
 */
public interface WmsMaterialLotPdaService {

    /**
     * PDA条件查询条码
     *
     * @param tenantId    租户ID
     * @param dto         查询条件
     * @param pageRequest 分页条件
     * @return Page<WmsMaterialLotDTO3>
     * @author jiangling.zheng@hand-china.com 2020-04-03 11:15:27
     */
    Page<WmsMaterialLotDTO3> selectBarCodeCondition(Long tenantId, WmsMaterialLotDTO4 dto, PageRequest pageRequest);

}
