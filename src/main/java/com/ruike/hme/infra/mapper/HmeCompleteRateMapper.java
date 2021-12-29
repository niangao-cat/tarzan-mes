package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeMonthlyPlanDTO;
import com.ruike.hme.domain.entity.HmeCompleteRate;
import com.ruike.hme.domain.vo.HmeMonthlyPlanVO;
import com.ruike.hme.domain.vo.HmeMonthlyPlanVO2;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 月度计划表Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-07-16 14:36:03
 */
public interface HmeCompleteRateMapper extends BaseMapper<HmeCompleteRate> {

    List<HmeMonthlyPlanVO> monthlyPlanQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeMonthlyPlanDTO dto);

    /**
     * 根据月份 部门 查询月度计划
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMonthlyPlanVO2>
     * @author sanfeng.zhang@hand-china.com 2021/6/15
     */
    List<HmeMonthlyPlanVO2> queryMonthPlanByAreaId(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeMonthlyPlanDTO dto);

    /**
     * 查询COS物料的完工数
     *
     * @param tenantId
     * @param cosMaterialIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMonthlyPlanVO>
     * @author sanfeng.zhang@hand-china.com 2021/6/17
     */
    List<HmeMonthlyPlanVO> queryFinishQtyByCosMaterialIds(@Param(value = "tenantId") Long tenantId, @Param(value = "cosMaterialIdList") List<String> cosMaterialIdList, @Param(value = "dto") HmeMonthlyPlanDTO dto);

    /**
     * 根据月份删除中间表数据
     *
     * @param tenantId 租户ID
     * @param month 月份
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/16 03:21:56
     * @return void
     */
    void batchDelateCompleteRate(@Param(value = "tenantId") Long tenantId, @Param(value = "month") String month);
}
