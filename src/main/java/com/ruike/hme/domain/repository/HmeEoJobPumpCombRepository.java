package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.*;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeEoJobPumpComb;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.order.domain.entity.MtWorkOrder;

import java.util.List;

/**
 * 泵浦源组合关系表资源库
 *
 * @author chaonan.hu@hand-china.com 2021-08-23 10:34:03
 */
public interface HmeEoJobPumpCombRepository extends BaseRepository<HmeEoJobPumpComb> {

    /**
     * 根据工单查询物料序列号
     *
     * @param tenantId 租户ID
     * @param mtWorkOrder 工单
     * @param workcellId 工位ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/23 01:59:41
     * @return java.lang.String
     */
    String getSnByWorkOrder(Long tenantId, MtWorkOrder mtWorkOrder, String workcellId);

    /**
     * 出站时校验性能数据
     *
     * @param tenantId 租户ID
     * @param dto 出站信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/25 02:07:17
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobPumpCombVO5>
     */
    List<HmeEoJobPumpCombVO5> pumpFilterRuleVerify(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     * 泵浦源工序作业平台-绑定SN物料时，对泵浦源组合关系表的操作逻辑
     *
     * @param tenantId 租户ID
     * @param hmeEoJobSnBatchVO8 前提信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/26 07:59:33
     * @return com.ruike.hme.domain.vo.HmeEoJobSnBatchVO14
     */
    HmeEoJobSnBatchVO14 releaseScan(Long tenantId, HmeEoJobSnBatchVO8 hmeEoJobSnBatchVO8);

    /**
     * 校验扫描的条码是否位于同一筛选组合
     *
     * @param tenantId 租户ID
     * @param hmeEoJobSn 进出站数据
     * @param mtMaterialLot 扫描条码信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/7 05:16:04
     * @return void
     */
    void samePumpSelectionComposeVerify(Long tenantId, HmeEoJobSn hmeEoJobSn, MtMaterialLot mtMaterialLot);
}
