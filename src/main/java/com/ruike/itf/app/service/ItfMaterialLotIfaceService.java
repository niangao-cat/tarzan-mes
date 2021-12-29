package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfMaterialLotReturnDTO;
import com.ruike.itf.domain.entity.ItfMaterialLotIface;

import java.util.List;

/**
 * 物料批，提供实物管理简易装载物料的结构，记录包括物料、位置、状态、数量、所有者、预留对象等数据应用服务
 *
 * @author yapeng.yao@hand-china.com 2020-09-01 09:32:35
 */
public interface ItfMaterialLotIfaceService {

    /**
     * 物料批及扩展同步接口
     *
     * @param itfMaterialLotIfaceList
     * @return
     */
    List<ItfMaterialLotReturnDTO> invoke(List<ItfMaterialLotIface> itfMaterialLotIfaceList);
}
