package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobPumpComb;
import com.ruike.hme.domain.vo.HmeEoJobPumpCombVO2;
import com.ruike.hme.domain.vo.HmeEoJobPumpCombVO3;
import com.ruike.hme.domain.vo.HmeEoJobPumpCombVO4;
import com.ruike.hme.domain.vo.HmeEoJobPumpCombVO6;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 泵浦源组合关系表Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-08-23 10:34:03
 */
public interface HmeEoJobPumpCombMapper extends BaseMapper<HmeEoJobPumpComb> {

    /**
     * 根据物料查询在表hme_pump_filter_rule_header中是否存在
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/23 01:49:48
     * @return java.lang.Long
     */
    Long countPumpFilterRuleHeaderByMaterial(@Param("tenantId") Long tenantId, @Param("materialId") String materialId);

    /**
     * 查询该工单在此工位是否存在在制品
     *
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param workOrderId 工单ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/23 02:08:20
     * @return java.lang.String
     */
    String getWipMaterialLotCodeByWo(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId,
                                     @Param("workOrderId") String workOrderId);

    /**
     * 根据工单查询EO清单中未进过本作业平台的SN
     *
     * @param tenantId 租户ID
     * @param workOrderId 工单ID
     * @param siteId 站点ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/23 02:19:55
     * @return java.lang.String
     */
    String getEoIdentificationByWo(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId,
                                   @Param("siteId") String siteId);

    /**
     * 根据物料查询在表hme_pump_filter_rule_header中维护的泵浦源个数
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/23 01:49:48
     * @return com.ruike.hme.domain.vo.HmeEoJobPumpCombVO2
     */
    HmeEoJobPumpCombVO2 pumpFilterRuleHeaderByMaterial(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 根据jobId+materialLotId为空查询表hme_eo_job_pump_comb数据
     *
     * @param tenantId
     * @param jobId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/23 08:03:24
     * @return com.ruike.hme.domain.entity.HmeEoJobPumpComb
     */
    HmeEoJobPumpComb getHmeEoJobPumpComb(@Param("tenantId") Long tenantId, @Param("jobId") String jobId);

    /**
     * 根据jobId、泵浦源物料批ID查询最大的子条码顺序
     *
     * @param tenantId 租户ID
     * @param jobId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/23 08:19:55
     * @return java.lang.Long
     */
    Long getMaxSubBarcodeSeqByJobId(@Param("tenantId") Long tenantId, @Param("jobId") String jobId);

    /**
     * 根据物料查询有效的泵浦源筛选规则行
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/25 03:06:42
     * @return com.ruike.hme.domain.vo.HmeEoJobPumpCombVO3
     */
    List<HmeEoJobPumpCombVO3> qureyPumpFilterRuleLineByMaterial(@Param("tenantId") Long tenantId, @Param("materialId") String materialId);

    /**
     * 根据出站的jobId查询泵浦源组合关系表中泵浦源物料批Id
     *
     * @param tenantId 租户ID
     * @param jobId jobId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/25 03:13:45
     * @return java.util.List<java.lang.String>
     */
    List<String> getPumpMaterialLotIdByJobId(@Param("tenantId") Long tenantId, @Param("jobId") String jobId);

    /**
     * 根据工艺编码查询其下有效的工位ID
     *
     * @param tenantId 租户ID
     * @param operationCodeList 工艺编码集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/25 03:39:58
     * @return java.util.List<java.lang.String>
     */
    List<String> getWorkcellByOperation(@Param("tenantId") Long tenantId, @Param("operationCodeList") List<String> operationCodeList);

    /**
     * 根据workcell_id+泵浦源物料批查询hme_eo_job_sn的workcell_id+泵浦源物料批+jobId+工位的上层工序+最后更新时间
     *
     * @param tenantId 租户ID
     * @param workcellIdList 工位ID集合
     * @param materialLotIdList 泵浦源物料批集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/25 04:42:16
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobPumpCombVO4>
     */
    List<HmeEoJobPumpCombVO4> getJobId(@Param("tenantId") Long tenantId, @Param("workcellIdList") List<String> workcellIdList,
                                       @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 根据jobId、tagId查询数据采集项
     *
     * @param tenantId 租户ID
     * @param jobIdList jobId集合
     * @param tagIdList tagId集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/25 05:10:49
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobDataRecord>
     */
    List<HmeEoJobDataRecord> queryDataRecordByJobTag(@Param("tenantId") Long tenantId, @Param("jobIdList") List<String> jobIdList,
                                                     @Param("tagIdList") List<String> tagIdList);

    /**
     * 根据jobId、组合物料批ID查询泵浦源组合关系数据
     *
     * @param tenantId 租户ID
     * @param jobId jobId
     * @param combMaterialLotId 组合物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/26 07:12:04
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobPumpComb>
     */
    List<HmeEoJobPumpComb> queryPumbCombByJobCombMaterialLot(@Param("tenantId") Long tenantId, @Param("jobId") String jobId,
                                                             @Param("combMaterialLotId") String combMaterialLotId);

    /**
     * 根据组合物料批、且泵浦源物料批不为空查找hme_eo_job_pump_comb数据，任取一条
     * 
     * @param tenantId 租户ID
     * @param combMaterialLotId 组合物料批
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/7 03:50:35 
     * @return com.ruike.hme.domain.entity.HmeEoJobPumpComb
     */
    HmeEoJobPumpComb getPumpCombByCombMaterialLot(@Param("tenantId") Long tenantId, @Param("combMaterialLotId") String combMaterialLotId);

    /**
     * 根据扫描的条码ID、状态为LOADED查询表hme_pump_pre_selection的组合物料
     *
     * @param tenantId 租户ID
     * @param materialLotId 扫描的条码ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/7 04:11:57
     * @return java.lang.String
     */
    String getCombMaterialBySelectionDetailsMaterialLot(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 根据条码ID、状态=LOADED查询表hme_pump_pre_selection的数据
     * 
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/7 04:31:34 
     * @return com.ruike.hme.domain.vo.HmeEoJobPumpCombVO6
     */
    HmeEoJobPumpCombVO6 getPumpSelectionDetailsByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 根据泵浦源物料批查询同一筛选批次同一筛选顺序下的筛选明细ID
     * 
     * @param tenantId 租户ID
     * @param materialLotId 泵浦源物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/7 05:25:41 
     * @return java.util.List<java.lang.String>
     */
    List<String> getSameSelectionOrderDetailsIdByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 根据主键更新泵浦源预筛选明细数据的状态
     *
     * @param tenantId 租户ID
     * @param pumpSelectionDetailsIdList 主键集合
     * @param userId 更新人
     * @param status 状态
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/7 05:30:25
     * @return void
     */
    void updatePumbSelectionDetailsStatus(@Param("tenantId") Long tenantId, @Param("pumpSelectionDetailsIdList") List<String> pumpSelectionDetailsIdList,
                                          @Param("userId") Long userId, @Param("status") String status);

    /**
     * 根据组合物料批查询泵浦源物料批
     * 
     * @param tenantId
     * @param combMaterialLotId
     * @author sanfeng.zhang@hand-china.com 2021/9/16 23:30 
     * @return java.util.List<java.lang.String>
     */
    List<String> getPumpMaterialLotIdByCombBarcodeId(@Param("tenantId") Long tenantId, @Param("combMaterialLotId") String combMaterialLotId);

    /**
     * 根据组合物料批查询泵浦组合关系
     *
     * @param tenantId
     * @param combMaterialLotId
     * @author sanfeng.zhang@hand-china.com 2021/9/17 0:05
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobPumpComb>
     */
    List<HmeEoJobPumpComb> queryPumbCombByCombMaterialLot(@Param("tenantId") Long tenantId, @Param("combMaterialLotId") String combMaterialLotId);

    /**
     * 根据物料批ID在表hme_eo_job_sn取最新的进站时间（site_in_date）的进站记录的workcell_id
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/26 11:26:17
     * @return java.lang.String
     */
    String getLastSiteInDateWorkcellIdBySn(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 根据物料批ID查询表hme_eo_job_pump_comb中的数据
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/9 02:22:55
     * @return com.ruike.hme.domain.entity.HmeEoJobPumpComb
     */
    HmeEoJobPumpComb eoJobPumpCombQueryByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 投料记录
     *
     * @param tenantId
     * @param materialLotId
     * @author sanfeng.zhang@hand-china.com 2021/12/6 19:29
     * @return java.lang.Long
     */
    Long queryEoJobMaterial(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);
}
