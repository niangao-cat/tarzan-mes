package com.ruike.wms.domain.repository;

import com.ruike.wms.domain.entity.WmsPoDeliveryRel;
import com.ruike.wms.domain.vo.WmsPoDeliveryVO;
import com.ruike.wms.domain.vo.WmsPoDeliveryVO3;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 送货单行与采购订单行关系表资源库
 *
 * @author han.zhang03@hand-china.com 2020-03-27 18:46:38
 */
public interface WmsPoDeliveryRepository extends BaseRepository<WmsPoDeliveryRel> {
    /**
     * propertyLimitPoDeliveryQuery-根据属性获取送货单
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/26
     */
    List<WmsPoDeliveryVO3> propertyLimitPoDeliveryQuery(Long tenantId, WmsPoDeliveryVO dto);
}
