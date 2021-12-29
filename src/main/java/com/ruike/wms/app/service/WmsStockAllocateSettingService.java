package com.ruike.wms.app.service;


import com.ruike.qms.api.dto.QmsSampleSchemeDTO2;
import com.ruike.wms.api.dto.WmsStockAllocateSettingDTO;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 库存调拨审核设置表应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-08-05 17:21:32
 */
public interface WmsStockAllocateSettingService {

    /**
     * 调拨审批设置查询
     * 
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/8/5 19:58 
     * @return io.choerodon.core.domain.Page<com.ruike.wms.api.dto.WmsStockAllocateSettingDTO>
     */
   Page<WmsStockAllocateSettingDTO> listStockSettingForUi(Long tenantId, PageRequest pageRequest, WmsStockAllocateSettingDTO dto);

   /**
    * 调拨审批设置保存
    *
    * @param tenantId
    * @param dto
    * @author jiangling.zheng@hand-china.com 2020/8/5 20:04
    * @return java.lang.String
    */
   String saveStockSettingForUi(Long tenantId, WmsStockAllocateSettingDTO dto);
}
