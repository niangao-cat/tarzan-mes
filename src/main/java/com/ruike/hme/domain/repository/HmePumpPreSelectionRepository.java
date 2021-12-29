package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmePumpPreSelectionDTO4;
import com.ruike.hme.api.dto.HmePumpPreSelectionDTO5;
import com.ruike.hme.domain.vo.*;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmePumpPreSelection;
import tarzan.inventory.domain.entity.MtContainer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 泵浦源预筛选基础表资源库
 *
 * @author chaonan.hu@hand-china.com 2021-08-30 10:59:48
 */
public interface HmePumpPreSelectionRepository extends BaseRepository<HmePumpPreSelection> {

    /**
     * 根据容器获取物料批
     *
     * @param tenantId 租户ID
     * @param mtContainer 容器
     * @param defaultStorageLocatorId 工段下的默认存储库位
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/30 03:50:26
     * @return java.util.List<java.lang.String>
     */
    List<String> getMaterialLotByContainer(Long tenantId, MtContainer mtContainer, String defaultStorageLocatorId);

    /**
     * 泵浦源预挑选确认时校验条码
     *
     * @param tenantId 租户ID
     * @param dto 确认信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/31 04:09:37
     * @return com.ruike.hme.domain.vo.HmePumpPreSelectionVO6
     */
    HmePumpPreSelectionVO6 pumpPreSelectionConfirmBarcodeVerify(Long tenantId, HmePumpPreSelectionDTO4 dto);

    /**
     * 泵浦源预挑选确认时校验目标容器
     *
     * @param tenantId 租户ID
     * @param dto 确认信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/31 05:25:11
     * @return tarzan.inventory.domain.entity.MtContainer
     */
    MtContainer pumpPreSelectionConfirmContainerVerify(Long tenantId, HmePumpPreSelectionDTO4 dto,
                                                       List<String> materialLotIdList);

    /**
     * 根据主物料查询组件物料及其全局替代料，得到二者合一之后去重的总物料集合
     *
     * @param tenantId 租户ID
     * @param dto 主物料
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/1 11:21:11
     * @return java.util.List<java.lang.String>
     */
    HmePumpPreSelectionVO14 getAllMaterialByMainMaterial(Long tenantId, HmePumpPreSelectionDTO5 dto);

    /**
     * 对筛选池中的条码进行数据采集项校验
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 物料批ID集合
     * @param pumpFilterRuleLineList 筛选规则行集合
     * @param qty 泵浦源个数
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/1 04:39:08
     * @return com.ruike.hme.domain.vo.HmePumpPreSelectionVO8
     */
    HmePumpPreSelectionVO8 eoJobDataRecordVerify(Long tenantId, List<String> materialLotIdList, List<HmePumpPreSelectionVO7> pumpFilterRuleLineList,
                                                 long qty);

    /**
     * 根据物料ID查询有效的筛选规则行
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/1 07:37:33
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpPreSelectionVO7>
     */
    HmePumpPreSelectionVO13 pumpFilterRuleLineQuery(Long tenantId, String materialId);

    /**
     * 筛选池中去除掉不符合SINGLE、MULTIPLE的数据采集项上限、下限的条码
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 筛选池中的条码
     * @param pumpFilterRuleLineList 筛选规则行数据
     * @param materialLotJobDataRecordMap 物料批及其对应的数据采集项
     * @param qty 筛选规则头上的泵浦源个数
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/1 09:12:13
     * @return com.ruike.hme.domain.vo.HmePumpPreSelectionVO8
     */
    HmePumpPreSelectionVO8 multipleSinglePumpFilterRuleLineVerify(Long tenantId, List<String> materialLotIdList,
                                                                  List<HmePumpPreSelectionVO7> pumpFilterRuleLineList,
                                                                  Map<String, List<HmePumpPreSelectionVO9>> materialLotJobDataRecordMap,
                                                                  Long qty);

    /**
     * 从小到大优先消耗
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 筛选池中的条码集合
     * @param sumList SUM类型的筛选规则行集合
     * @param qty 泵浦源个数
     * @param qtyLong 泵浦源个数
     * @param materialLotTagResultList 筛选池中所有条码所有tagId下的result
     * @param materialLotGroupList 多个条码子集
     * @param letterTagIdMap 计算类型筛选规则行公式中的字母与tag的关系map
     * @param wpeLine wpe筛选规则行
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/15 09:45:51
     * @return java.util.List<java.util.List<java.lang.String>>
     */
    List<List<String>> pumpFilterRuleSumLineAscend(Long tenantId, List<String> materialLotIdList, List<HmePumpPreSelectionVO7> sumList,
                                     BigDecimal qty, long qtyLong, List<HmePumpPreSelectionVO11> materialLotTagResultList,
                                     List<List<String>> materialLotGroupList, Map<String, String> letterTagIdMap, HmePumpPreSelectionVO7 wpeLine);

    /**
     * 从大到小优先消耗
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 筛选池中的条码集合
     * @param sumList SUM类型的筛选规则行集合
     * @param qty 泵浦源个数
     * @param qtyLong 泵浦源个数
     * @param materialLotTagResultList 筛选池中所有条码所有tagId下的result
     * @param materialLotGroupList 多个条码子集
     * @param letterTagIdMap 计算类型筛选规则行公式中的字母与tag的关系map
     * @param wpeLine wpe筛选规则行
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/15 09:49:35
     * @return java.util.List<java.util.List<java.lang.String>>
     */
    List<List<String>> pumpFilterRuleSumLineDescend(Long tenantId, List<String> materialLotIdList, List<HmePumpPreSelectionVO7> sumList,
                                     BigDecimal qty, long qtyLong, List<HmePumpPreSelectionVO11> materialLotTagResultList,
                                     List<List<String>> materialLotGroupList, Map<String, String> letterTagIdMap, HmePumpPreSelectionVO7 wpeLine);

    /**
     * 临时条码组合是否符合其他数据项的要求，不符合则寻找最优的可置换条码，寻找不到则结束
     *
     * @param tenantId 租户ID
     * @param temporaryMaterialLotList 临时条码组合
     * @param materialLotGroupList 多个条码子集
     * @param materialLotTagResultList 筛选池中所有条码所有tagId下的result
     * @param sumList SUM类型的筛选规则行集合
     * @param currentMaterialLotIdList 筛选池中的条码集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/3 03:54:42
     * @return java.util.List<java.lang.String>
     */
    List<String> temporaryMaterialLotOtherTagDisplace(Long tenantId, List<String> temporaryMaterialLotList,
                                              List<List<String>> materialLotGroupList, List<HmePumpPreSelectionVO11> materialLotTagResultList,
                                              List<HmePumpPreSelectionVO7> sumList, List<String> currentMaterialLotIdList);

    /**
     * 根据不符合要求的临时条码组合去查询可置换条码集合，并选出最优的可置换条码
     *
     * @param tenantId 租户ID
     * @param temporaryMaterialLotList 临时条码组合（去除不符合要求条码之后的组合）
     * @param materialLotGroupList 多个条码子集
     * @param materialLotTagResultList 筛选池中所有条码所有tagId下的result
     * @param sumList SUM类型的筛选规则行集合
     * @param currentMaterialLotIdList 筛选池中的条码集合
     * @param currentSequenceMinSumLine  当前循环的SUM类型筛选规则行
     * @param removeMaterialLotId 要移除出去的条码
     * @param resultSum 未移除出去之前的条码组合在当前循环的SUM类型筛选规则行下的result之和
     * @param passTagIdList 已通过要求的tagId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/3 03:46:17
     * @return java.util.List<java.lang.String>
     */
    List<String> getDisplaceMaterialLot(Long tenantId, List<String> temporaryMaterialLotList, List<List<String>> materialLotGroupList,
                                        List<HmePumpPreSelectionVO11> materialLotTagResultList, List<HmePumpPreSelectionVO7> sumList,
                                        List<String> currentMaterialLotIdList, HmePumpPreSelectionVO7 currentSequenceMinSumLine,
                                        String removeMaterialLotId, BigDecimal resultSum, List<String> passTagIdList);

    /**
     * WPE校验
     *
     * @param tenantId 租户ID
     * @param temporaryMaterialLotList 临时条码组合
     * @param materialLotGroupList 多个条码子集
     * @param materialLotTagResultList 筛选池中所有条码所有tagId下的result
     * @param sumList SUM类型的筛选规则行集合
     * @param currentMaterialLotIdList 当前筛选池中的条码集合
     * @param wpeLine wpe筛选规则行
     * @param letterTagIdMap 计算类型筛选规则行公式中的字母与tag的关系map
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/15 09:49:54
     * @return java.util.List<java.lang.String>
     */
    List<String> temporaryMaterialLotWpeDisplace(Long tenantId, List<String> temporaryMaterialLotList, List<List<String>> materialLotGroupList,
                                                 List<HmePumpPreSelectionVO11> materialLotTagResultList, List<HmePumpPreSelectionVO7> sumList,
                                                 List<String> currentMaterialLotIdList, HmePumpPreSelectionVO7 wpeLine, Map<String, String> letterTagIdMap);

    /**
     * 插入泵浦源头行数据，并返回结果
     *
     * @param tenantId 租户ID
     * @param dto 预筛选信息
     * @param finalMaterialLotList 最终的条码组合
     * @param bomId bomID
     * @param headId 筛选规则头ID
     * @param qty 筛选规则头的泵浦源个数
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/6 03:24:31
     * @return com.ruike.hme.domain.vo.HmePumpPreSelectionVO15
     */
    HmePumpPreSelectionVO15 insertPumpPreSelectionData(Long tenantId, HmePumpPreSelectionDTO5 dto,
                                                       List<List<String>> finalMaterialLotList, String bomId,
                                                       String headId, long qty);
}
