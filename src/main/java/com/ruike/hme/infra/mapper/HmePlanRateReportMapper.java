package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmePlanRateReportRateDTO;
import com.ruike.hme.api.dto.HmePlanRateReportRequestDTO;
import com.ruike.hme.domain.vo.HmePlanRateDetailVO;
import com.ruike.hme.domain.vo.HmePlanRateWoRouterStepVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * description
 *
 * @author quan.luo@hand-china.com 2020/11/25 20:32
 */
public interface HmePlanRateReportMapper {

    /**
     * 计划达成率报表数据查询
     *
     * @param tenantId                    租户id
     * @param hmePlanRateReportRequestDTO 参数
     * @return 数据
     */
    List<HmePlanRateReportRateDTO> planRateReportQuery(@Param("tenantId") Long tenantId,
                                                       @Param("hmePlanRateReportRequestDTO") HmePlanRateReportRequestDTO hmePlanRateReportRequestDTO);

    /**
     * 查询明细
     *
     * @param tenantId   租户
     * @param siteId     站点
     * @param shiftDate  班次日期
     * @param shiftCode  班次编码
     * @param workcellId 工段
     * @return java.util.List<com.ruike.hme.domain.vo.HmePlanRateDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/17 10:27:55
     */
    List<HmePlanRateDetailVO> selectDetailList(@Param("tenantId") Long tenantId,
                                               @Param("siteId") String siteId,
                                               @Param("shiftDate") LocalDate shiftDate,
                                               @Param("shiftCode") String shiftCode,
                                               @Param("workcellId") String workcellId);

    /**
     * 查询工单路线步骤关系列表
     *
     * @param tenantId   租户
     * @param shiftDate  班次日期
     * @param shiftCode  班次编码
     * @param workcellId 工段ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmePlanRateWoRouterStepVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/17 03:21:39
     */
    List<HmePlanRateWoRouterStepVO> selectWoRouterStepList(@Param("tenantId") Long tenantId,
                                                           @Param("shiftDate") LocalDate shiftDate,
                                                           @Param("shiftCode") String shiftCode,
                                                           @Param("workcellId") String workcellId);

    /**
     * 查询交付明细
     *
     * @param tenantId 租户
     * @param dtoList  参数列表
     * @return java.util.List<com.ruike.hme.domain.vo.HmePlanRateDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/17 10:27:55
     */
    List<HmePlanRateDetailVO> selectDeliveryDetailList(@Param("tenantId") Long tenantId,
                                                       @Param("dtoList") List<HmePlanRateWoRouterStepVO> dtoList);


    /**
     * 查询交付明细
     *
     * @param tenantId          租户
     * @param siteId            站点
     * @param shiftDate         班次日期
     * @param shiftCode         班次
     * @param workcellId        工位
     * @author sanfeng.zhang@hand-china.com 2021/1/18 11:15
     * @return java.util.List<com.ruike.hme.domain.vo.HmePlanRateDetailVO>
     */
    List<HmePlanRateDetailVO> queryDeliveryDetailList(@Param("tenantId") Long tenantId,
                                                      @Param("siteId") String siteId,
                                                      @Param("shiftDate") LocalDate shiftDate,
                                                      @Param("shiftCode") String shiftCode,
                                                      @Param("workcellId") String workcellId);

    /**
     *
     * @Description 单独查询实际投产数据
     *
     * @author yuchao.wang
     * @date 2021/1/21 10:32
     * @param tenantId 租户ID
     * @param workcellIdList 工位ID
     * @param shiftCodeList 班次编码
     * @param hmePlanRateReportRequestDTO 参数
     * @return java.util.List<com.ruike.hme.api.dto.HmePlanRateReportRateDTO>
     *
     */
    List<HmePlanRateReportRateDTO> actualProductionQuery(@Param("tenantId") Long tenantId,
                                                         @Param("workcellIdList") List<String> workcellIdList,
                                                         @Param("shiftCodeList") List<String> shiftCodeList,
                                                         @Param("dto") HmePlanRateReportRequestDTO hmePlanRateReportRequestDTO);

    /**
     *
     * @Description 单独查询实际交付数据
     *
     * @author yuchao.wang
     * @date 2021/1/21 10:32
     * @param tenantId 租户ID
     * @param workcellIdList 工位ID
     * @param shiftCodeList 班次编码
     * @param hmePlanRateReportRequestDTO 参数
     * @return java.util.List<com.ruike.hme.api.dto.HmePlanRateReportRateDTO>
     *
     */
    List<HmePlanRateReportRateDTO> actualAeliveryQuery(@Param("tenantId") Long tenantId,
                                                       @Param("workcellIdList") List<String> workcellIdList,
                                                       @Param("shiftCodeList") List<String> shiftCodeList,
                                                       @Param("dto") HmePlanRateReportRequestDTO hmePlanRateReportRequestDTO);

}
