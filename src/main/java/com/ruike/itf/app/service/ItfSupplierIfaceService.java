package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfCommonReturnDTO;
import com.ruike.itf.api.dto.ItfSupplierDTO;

import java.util.List;

/**
 * 供应商数据接口表应用服务
 *
 * @author yapeng.yao@hand-china.com 2020-08-19 18:49:46
 */
public interface ItfSupplierIfaceService {

    /**
     * 供应商数据同步接口
     * @param itfSupplierDTOList
     * @return
     */
    List<ItfCommonReturnDTO> invoke(List<ItfSupplierDTO> itfSupplierDTOList);
}
