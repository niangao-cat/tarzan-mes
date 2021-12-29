package com.ruike.reports.infra.mapper;

import com.ruike.hme.domain.entity.HmeCosNcRecord;
import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO;
import com.ruike.reports.api.dto.HmeCosWorkcellSummaryQueryDTO;
import com.ruike.reports.domain.vo.HmeCosWorkcellSummaryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * COS工位加工汇总 mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/1 15:57
 */
public interface HmeCosWorkcellSummaryMapper {

    /**
     * 根据条件查询列表
     *
     * @param tenantId 租户
     * @param dto      参数
     * @return java.util.List<com.ruike.reports.domain.vo.HmeCosWorkcellSummaryVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/1 03:58:52
     */
    List<HmeCosWorkcellSummaryVO> selectListByCondition(@Param("tenantId") Long tenantId,
                                                        @Param("dto") HmeCosWorkcellSummaryQueryDTO dto);

    /**
     * 根据条件查询设备
     *
     * @param tenantId 租户
     * @param jobIdList 参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO>
     * @author penglin.sui@hand-china.com 2021/4/7 19:08
     */
    List<HmeCosBarCodeExceptionVO> selectEquipmentList(@Param("tenantId") Long tenantId,
                                                       @Param("jobIdList") List<String> jobIdList);

    /**
     * 根据条件查询芯片数
     *
     * @param tenantId 租户
     * @param waferList 参数
     * @return java.util.List<com.ruike.hme.domain.entity.HmeCosOperationRecord>
     * @author penglin.sui@hand-china.com 2021/4/7 19:18
     */
    List<HmeCosOperationRecord> selectCosNumList(@Param("tenantId") Long tenantId,
                                                    @Param("waferList") List<String> waferList,
                                                    @Param("workOrderList") List<String> workOrderList);

    /**
     * 根据条件查询芯片数
     *
     * @param tenantId 租户
     * @param waferList 参数
     * @param workOrderList 参数
     * @param operationIdList 参数
     * @param workcellIdList 参数
     * @param createdByList 参数
     * @return java.util.List<com.ruike.hme.domain.entity.HmeCosNcRecord>
     * @author penglin.sui@hand-china.com 2021/4/7 19:42
     */
    List<HmeCosNcRecord> selectNgQtyList(@Param("tenantId") Long tenantId,
                                         @Param("waferList") List<String> waferList,
                                         @Param("workOrderList") List<String> workOrderList,
                                         @Param("operationIdList") List<String> operationIdList,
                                         @Param("workcellIdList") List<String> workcellIdList,
                                         @Param("createdByList") List<Long> createdByList);

    /**
     * 根据条件查询芯片数
     *
     * @param tenantId 租户
     * @param sourceJobIdList 参数
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     * @author penglin.sui@hand-china.com 2021/4/7 21:22
     */
    List<HmeEoJobSn> selectEoJobSnList(@Param("tenantId") Long tenantId,
                                       @Param("sourceJobIdList") List<String> sourceJobIdList);
}
