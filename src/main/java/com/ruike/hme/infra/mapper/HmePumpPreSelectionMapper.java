package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmePumpPreSelectionDTO2;
import com.ruike.hme.api.dto.HmePumpPreSelectionDTO3;
import com.ruike.hme.api.dto.HmePumpPreSelectionDTO5;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmePumpPreSelection;
import com.ruike.hme.domain.vo.*;
import com.ruike.wms.domain.entity.WmsMaterialSubstituteRel;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.modeling.domain.entity.MtModWorkcell;

import java.util.List;

/**
 * 泵浦源预筛选基础表Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-08-30 10:59:48
 */
public interface HmePumpPreSelectionMapper extends BaseMapper<HmePumpPreSelection> {

    /**
     * 根据物料查询泵浦源筛选规则行数据
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/30 01:59:48
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpPreSelectionVO>
     */
    List<HmePumpPreSelectionVO> queryTagInfoByMaterial(@Param("tenantId") Long tenantId, @Param("materialId") String materialId);

    /**
     * 根据工位查询工段ID
     *
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/30 02:36:28
     * @return java.lang.String
     */
    String getLineWorkcellIdByWorkcell(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId);

    /**
     * 根据条码ID查询条码相关信息
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 条码ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/30 04:22:03
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpPreSelectionVO4>
     */
    List<HmePumpPreSelectionVO4> materialLotInfoQuery(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 条码是否已装载查询
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 条码ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/30 04:40:25
     * @return tarzan.inventory.domain.entity.MtMaterialLot
     */
    String materialLotLoadedQuery(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 泵浦源筛选批次LOV
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/31 09:49:06
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpPreSelectionVO2>
     */
    List<HmePumpPreSelectionVO2> pumpSelectionLotLovQuery(@Param("tenantId") Long tenantId, @Param("dto") HmePumpPreSelectionDTO2 dto);
    
    /**
     * 泵浦源预挑选撤回界面查询
     * 
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/31 11:02:23 
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpPreSelectionVO5>
     */
    List<HmePumpPreSelectionVO5> pumpPreSelectionRecallQuery(@Param("tenantId") Long tenantId, @Param("dto") HmePumpPreSelectionDTO3 dto);

    /**
     * 根据容器ID查询编码
     *
     * @param tenantId 租户ID
     * @param containerIdList 容器ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/31 11:19:49
     * @return java.util.List<tarzan.inventory.domain.entity.MtContainer>
     */
    List<MtContainer> containerInfoQuery(@Param("tenantId") Long tenantId, @Param("containerIdList") List<String> containerIdList);

    /**
     * 根据主键批量删除泵浦源预筛选明细
     *
     * @param tenantId 租户ID
     * @param pumpSelectionDetailsIdList 泵浦源预筛选明细ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/31 02:12:23
     * @return void
     */
    void batchDeletePumpSelectionDetailsByPrimary(@Param("tenantId") Long tenantId, @Param("pumpSelectionDetailsIdList") List<String> pumpSelectionDetailsIdList);

    /**
     * 批量根据物料批编码查询物料批相关信息
     *
     * @param tenantId 租户ID
     * @param materialLotCodeList 物料批编码集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/31 04:04:00
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpPreSelectionVO4>
     */
    List<HmePumpPreSelectionVO4> materialLotInfoQueryByCode(@Param("tenantId") Long tenantId, @Param("materialLotCodeList") List<String> materialLotCodeList);

    /**
     * 条码是否已装载查询
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 条码ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/30 04:40:25
     * @return tarzan.inventory.domain.entity.MtMaterialLot
     */
    String materialLotLoadedQuery2(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 根据物料编码、版本号查询唯一的bomId
     *
     * @param tenantId 租户ID
     * @param dto 物料编码、版本号
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/1 11:23:17
     * @return java.lang.String
     */
    String getBomIdByMaterialRevision(@Param("tenantId") Long tenantId, @Param("dto") HmePumpPreSelectionDTO5 dto);

    /**
     * 根据bomId查询当前时间在生效时间和失效时间之内的Bom组件物料
     *
     * @param tenantId 租户ID
     * @param bomId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/1 11:36:59
     * @return java.util.List<java.lang.String>
     */
    List<String> getBomComponentMaterialByBomId(@Param("tenantId") Long tenantId, @Param("bomId") String bomId);

    /**
     * 根据Bom组件物料集合查询替代组
     *
     * @param tenantId 租户ID
     * @param mainMaterialIdList Bom组件物料集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/1 11:55:32
     * @return java.util.List<com.ruike.wms.domain.entity.WmsMaterialSubstituteRel>
     */
    List<WmsMaterialSubstituteRel> getSubStituteGroupByMainMaterial(@Param("tenantId") Long tenantId, @Param("mainMaterialIdList") List<String> mainMaterialIdList);

    /**
     * 根据全局替代组查询其下的全局替代料
     *
     * @param tenantId 租户ID
     * @param subStituteGroupList 全局替代组集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/1 02:06:59
     * @return java.util.List<java.lang.String>
     */
    List<String> getMaterialBySubStituteGroup(@Param("tenantId") Long tenantId, @Param("subStituteGroupList") List<String> subStituteGroupList);

    /**
     * 根据物料批Id、物料Id查询物料批Id  实际上是通过物料筛选物料批
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 物料批ID集合
     * @param materialIdList 物料ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/1 02:26:49
     * @return java.util.List<java.lang.String>
     */
    List<String> getMaterialLotByMaterialLotAndMaterial(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList,
                                                        @Param("materialIdList") List<String> materialIdList);

    /**
     * 根据物料查询有效的泵浦源筛选规则行
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/1 02:42:33
     * @return com.ruike.hme.domain.vo.HmePumpPreSelectionVO7
     */
    List<HmePumpPreSelectionVO7> getPumpFilterRuleLineByMaterial(@Param("tenantId") Long tenantId, @Param("materialId") String materialId);

    /**
     * 根据多个工艺查询其下的工位集合
     *
     * @param tenantId 租户ID
     * @param operationCodeList 工艺编码集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/1 02:58:18
     * @return java.util.List<java.lang.String>
     */
    List<String> getWorkcellByOperation(@Param("tenantId") Long tenantId, @Param("operationCodeList") List<String> operationCodeList);

    /**
     * 根据物料批、工位查询已出站数据
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 物料批ID集合
     * @param workcellIdList 工位ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/1 03:13:49
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     */
    List<HmeEoJobSn> eoJobSnDataQuery(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList,
                                      @Param("workcellIdList") List<String> workcellIdList);

    /**
     * 根据jobId、tagId查询结果不为空的数据采集项
     *
     * @param tenantId 租户ID
     * @param jobIdList jobID集合
     * @param tagIdList 数据项ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/1 03:54:45
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobDataRecord>
     */
    List<HmeEoJobDataRecord> eoJobDataRecordQuery(@Param("tenantId") Long tenantId, @Param("jobIdList") List<String> jobIdList,
                                                  @Param("tagIdList") List<String> tagIdList);

    /**
     * 根据泵浦源筛选头ID查询NEW状态筛选明细数据
     *
     * @param tenantId 租户ID
     * @param pumpPreSelectionId 泵浦源筛选头ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/9 09:40:37
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpPreSelectionVO4>
     */
    List<HmePumpPreSelectionVO4> newPumpSelectionDetailsQueryBySelectionLot(@Param("tenantId") Long tenantId, @Param("pumpPreSelectionId") String pumpPreSelectionId);

    /**
     * 根据泵浦源预挑选明细ID查询已存在的个数
     * 
     * @param tenantId 租户ID
     * @param pumpSelectionDetailsIdList 泵浦源预挑选明细ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/16 10:17:55 
     * @return java.lang.Long
     */
    Long getCountBySelectionDetailsId(@Param("tenantId") Long tenantId, @Param("pumpSelectionDetailsIdList") List<String> pumpSelectionDetailsIdList);

    /**
     * 根据筛选批次查询套数
     * 
     * @param tenantId 租户ID
     * @param selectionLot 挑选批次
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/28 11:10:11 
     * @return java.lang.Long
     */
    HmePumpPreSelection getSetsNumBySelectionLot(@Param("tenantId") Long tenantId, @Param("selectionLot") String selectionLot);

    /**
     * 根据筛选批次查询已挑选套数
     *
     * @param tenantId 租户ID
     * @param pumpPreSelectionId 挑选批次ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/28 11:14:04
     * @return java.lang.Long
     */
    List<String> getAlreadySetsNumBySelectionLot(@Param("tenantId") Long tenantId, @Param("pumpPreSelectionId") String pumpPreSelectionId);
}
