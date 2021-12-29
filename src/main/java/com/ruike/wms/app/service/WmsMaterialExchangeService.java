package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsMaterialExchangeDocDTO;
import com.ruike.wms.api.dto.WmsMaterialExchangeDocLineDTO;
import com.ruike.wms.api.dto.WmsMaterialExchangeParamDTO;

import com.ruike.wms.domain.vo.WmsMaterialExchangeVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 料废查询应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-05-18 10:29
 */
public interface WmsMaterialExchangeService {

    /**
     * 获取头列表信息
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @param dto WmsMaterialExchangeParamDTO
     * @param pageRequest pageRequest
     * @return Page<WmsMaterialExchangeDocDTO>
     */
    Page<WmsMaterialExchangeDocDTO> listDocForUi (Long tenantId, WmsMaterialExchangeParamDTO dto,  PageRequest pageRequest);

    /**
     * 获取行列表信息
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @param instructionDocId 单据头ID
     * @param pageRequest pageRequest
     * @return Page<WmsMaterialExchangeDocLineDTO>
     */
    Page<WmsMaterialExchangeDocLineDTO> listDocLineForUi (Long tenantId, String instructionDocId, PageRequest pageRequest);

    /**
     * 料废调拨-库存调拨
     *
     * @param tenantId                  租户id
     * @param wmsMaterialExchangeVO     参数
     * @author sanfeng.zhang@hand-china.com 2020/9/2 10:04
     * @return com.ruike.wms.domain.vo.WmsMaterialExchangeVO
     */
    WmsMaterialExchangeVO lineStockTransfer(Long tenantId, WmsMaterialExchangeVO wmsMaterialExchangeVO);
}
