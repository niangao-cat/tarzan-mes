package com.ruike.itf.domain.repository;

import com.ruike.hme.domain.entity.HmeMonthlyPlan;
import com.ruike.itf.api.dto.ItfMonthlyPlanIfaceDTO2;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.itf.domain.entity.ItfMonthlyPlanIface;

import java.util.List;

/**
 * 月度计划接口表资源库
 *
 * @author chaonan.hu@hand-china.com 2021-06-01 14:21:59
 */
public interface ItfMonthlyPlanIfaceRepository extends BaseRepository<ItfMonthlyPlanIface> {

    /**
     * 批量插入接口表数据
     *
     * @param tenantId 租户ID
     * @param ifaceList 接口表数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/1 03:15:49
     * @return java.util.List<com.ruike.itf.domain.entity.ItfMonthlyPlanIface>
     */
    List<ItfMonthlyPlanIface> batchInsertIface(Long tenantId, List<ItfMonthlyPlanIface> ifaceList);

    /**
     * 批量更新接口表数据
     *
     * @param tenantId 租户ID
     * @param ifaceList 接口表数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/1 04:45:26
     * @return void
     */
    void batchUpdateIface(Long tenantId, List<ItfMonthlyPlanIface> ifaceList);

    /**
     * 批量插入业务表
     *
     * @param tenantId 租户ID
     * @param hmeMonthlyPlanList 业务表数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/1 05:05:45
     * @return void
     */
    void batchInsertMonthlyPlan(Long tenantId, List<HmeMonthlyPlan> hmeMonthlyPlanList);
}
