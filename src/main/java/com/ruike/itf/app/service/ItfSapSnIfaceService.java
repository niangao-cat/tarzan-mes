package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfCommonReturnDTO;
import com.ruike.itf.api.dto.ItfSapSnIfaceDto;
import com.ruike.itf.api.dto.ItfSapSnReturnDto;
import com.ruike.itf.domain.entity.ItfSapSnIface;
import tarzan.inventory.domain.vo.MtMaterialLotVO13;
import tarzan.inventory.domain.vo.MtMaterialLotVO19;

import java.util.List;
import java.util.Map;

/**
 * 成品SN同步接口表应用服务
 *
 * @author li.zhang13@hand-china.com 2021-07-01 11:05:34
 */
public interface ItfSapSnIfaceService {

    /**
     * 客户数据同步接口
     *
     * @param itfSapSnIfaceDtoList
     * @return List <ItfSapSnReturnDto>
     * @author li.zhang13@hand-china.com
     */
    ItfSapSnReturnDto invoke(List<ItfSapSnIfaceDto> itfSapSnIfaceDtoList);

    /**
     * 更新条码表与条码扩展属性表
     *
     * @param tenantId
     * @param eventId
     * @param itfSapSnIface
     * @return List<MtMaterialLotVO19>
     * @author li.zhang13@hand-china.com
     */
    void insertSapAndSapAttr(Long tenantId, String eventId, ItfSapSnIface itfSapSnIface);

    /**
     * 更新条码表、条码扩展属性表、增加库存
     *
     * @param tenantId
     * @param eventId
     * @param itfSapSnIface
     * @return
     * @author li.zhang13@hand-china.com
     */
    void insertSapAndSapAttrAndQuantity(Long tenantId, String eventId, ItfSapSnIface itfSapSnIface);

}
