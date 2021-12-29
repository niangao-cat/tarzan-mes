package com.ruike.wms.app.service;

import com.ruike.wms.domain.entity.WmsDistributionDemandDetail;

import java.util.List;

/**
 * 配送平台明细 服务
 *
 * @author penglin.sui@hand-china.com 2020-07-22 15:15
 */
public interface WmsDistributionDemandDetailService {
    /**
     * 通过配送需求ID查询配送明细
     *
     * @param tenantId     租户
     * @param distDemandId 配送需求ID
     * @return java.util.List<com.ruike.wms.domain.entity.WmsDistributionDemandDetail>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 04:10:42
     */
    List<WmsDistributionDemandDetail> selectListByDemandId(Long tenantId, String distDemandId);

    /**
     * 新增或更新替代行
     *
     * @param tenantId     租户
     * @param substitution 明细数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 08:34:03
     */
    void insertOrUpdateSubstitution(Long tenantId, WmsDistributionDemandDetail substitution);

    /**
     * 删除替代料行
     *
     * @param tenantId     租户
     * @param substitution 明细数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 08:34:03
     */
    void removeSubstitution(Long tenantId, WmsDistributionDemandDetail substitution);
}
