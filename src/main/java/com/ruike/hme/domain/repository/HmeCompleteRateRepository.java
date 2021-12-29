package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeMonthlyPlanVO;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeCompleteRate;

import java.util.List;

/**
 * 月度计划表资源库
 *
 * @author chaonan.hu@hand-china.com 2021-07-16 14:36:03
 */
public interface HmeCompleteRateRepository extends BaseRepository<HmeCompleteRate> {

    /**
     * 根据月份删除中间表数据
     *
     * @param tenantId 租户ID
     * @param month 月份
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/16 03:25:40
     * @return void
     */
    void batchDelateCompleteRate(Long tenantId, String month);

    /**
     * 批量新增数据到中间表中
     *
     * @param tenantId 租户ID
     * @param resultList 新增数据
     * @param month 月份
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/16 03:13:56
     * @return void
     */
    void batchInsertCompleteRate(Long tenantId, List<HmeMonthlyPlanVO> resultList, String month);
}
