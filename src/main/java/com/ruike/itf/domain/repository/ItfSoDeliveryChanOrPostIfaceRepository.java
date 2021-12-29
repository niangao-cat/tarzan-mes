package com.ruike.itf.domain.repository;

import com.ruike.itf.api.dto.ItfSoDeliveryChanOrPostDTO;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanOrPostIface;

import java.util.List;

/**
 * 交货单修改过账接口头表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2021-07-09 16:58:10
 */
public interface ItfSoDeliveryChanOrPostIfaceRepository extends BaseRepository<ItfSoDeliveryChanOrPostIface>, AopProxy<ItfSoDeliveryChanOrPostIfaceRepository> {

    /**
     * 发运/退货单修改或过账
     *
     * @param tenantId
     * @param itfSoDeliveryChanOrPostList
     * @return java.util.List<com.ruike.itf.api.dto.ItfSoDeliveryChanOrPostDTO>
     * @author sanfeng.zhang@hand-china.com 2021/7/9
     */
    List<ItfSoDeliveryChanOrPostDTO> soDeliveryChangeOrPostIface(Long tenantId, List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostList);

    /**
     * 批量新增
     * @param ifaceList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/7/13
     */
    void myBatchInsertSelective(List<ItfSoDeliveryChanOrPostIface> ifaceList);

    /**
     * 批量更新
     * @param ifaceList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/7/13
     */
    void myBatchUpdateSelective(List<ItfSoDeliveryChanOrPostIface> ifaceList);

    /**
     * 定时过账发运/退货单
     * @param tenantId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/7/16
     */
    void postIfaceToESB(Long tenantId);
}
