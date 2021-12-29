package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsMaterialExchangeDocDTO;
import com.ruike.wms.api.dto.WmsMaterialExchangeParamDTO;
import com.ruike.wms.domain.vo.WmsMaterialExchangeVO;
import org.hzero.core.base.AopProxy;

import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-19 11:34
 */
public interface WmsMaterialExchangeRepository  extends AopProxy<WmsMaterialExchangeRepository> {

    /**
     * 获取头列表信息
     * @author jiangling.zheng@hand-china.com
     * @param dto WmsMaterialExchangeParamDTO
     * @return Page<WmsMaterialExchangeDocDTO>
     */
    List<WmsMaterialExchangeDocDTO> listDocForUi(WmsMaterialExchangeParamDTO dto);

    /**
     * 料废调拨-库存调拨
     *
     * @param tenantId                  租户id
     * @param wmsMaterialExchangeVO     参数
     * @author sanfeng.zhang@hand-china.com 2020/9/2 10:09
     * @return com.ruike.wms.domain.vo.WmsMaterialExchangeVO
     */
    WmsMaterialExchangeVO lineStockTransfer(Long tenantId, WmsMaterialExchangeVO wmsMaterialExchangeVO);
}
