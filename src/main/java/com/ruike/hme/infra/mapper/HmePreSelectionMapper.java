package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeCosFunction;
import com.ruike.hme.domain.entity.HmeCosRuleLogic;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.entity.HmePreSelection;
import com.ruike.hme.domain.vo.HmePreSelectionVO;
import com.ruike.wms.domain.vo.WmsLocatorTransferVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * 预挑选基础表Mapper
 *
 * @author wenzhang.yu@hand-china.com 2020-08-18 15:00:33
 */
public interface HmePreSelectionMapper extends BaseMapper<HmePreSelection> {


    List<HmePreSelectionReturnDTO> workOrderQuery(@Param("tenantId") Long tenantId, @Param("dto") HmePreSelectionDTO dto);


    List<String> queryLocatorId(@Param("tenantId") Long tenantId, @Param("locatorId") String parentLocatorId);

    List<HmePreSelectionDTO4> queryMaterialLot(@Param("tenantId") Long tenantId,
                                               @Param("materialLotIdList") List<String> materialLotIdList,
                                               @Param("hmeCosRuleType") HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO1,
                                               @Param("limitRuleList") List<HmeCosRuleLogic> limitRule);

    List<HmePreSelectionDTO4> queryMaterialLotNew(@Param("tenantId") Long tenantId,
                                                  @Param("materialLotIdList") List<String> materialLotIdList,
                                                  @Param("hmeCosRuleType") HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO1,
                                                  @Param("limitRuleList") List<HmePreSelectionDTO8> limitRule);

    /**
     * 与queryMaterialLotNew方法的区别只在于将cos_function表更换为cos_function_selection表
     *
     * @param tenantId
     * @param materialLotIdList
     * @param hmeCosRuleTypeDTO1
     * @param limitRule
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/27 04:24:35
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionDTO4>
     */
    List<HmePreSelectionDTO4> queryMaterialLotNew2(@Param("tenantId") Long tenantId,
                                                  @Param("materialLotIdList") List<String> materialLotIdList,
                                                  @Param("hmeCosRuleType") HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO1,
                                                  @Param("limitRuleList") List<HmePreSelectionDTO8> limitRule);

    List<HmePreSelectionReturnDTO3> selectLot(@Param("tenantId") Long tenantId, @Param("selectLot") String selectLot);


    List<HmePreSelectionReturnDTO4> materialLot(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);


    List<HmePreSelectionReturnDTO4> tomaterialLot(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    Integer selectmaterialLot(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode, @Param("selectLot") String selectLot);


    List<HmePreSelectionReturnDTO4> getOrderBy(@Param("virtualNum") String virtualNum);

    List<HmeCosFunction> selectFunction(@Param("materialLotIdList") List<String> materialLotId);


    List<HmeCosRuleLogicDTO> selectRule(@Param("tenantId") Long tenantId, @Param("ruleId") String ruleId);


    MtModLocator getWarehouse(@Param("tenantId") Long tenantId, @Param("locatorId") String locatorId);

    List<HmePreSelectionReturnDTO5> getMateriallot(@Param("tenantId") Long tenantId, @Param("locatorCode") String locatorCode);

    List<MtMaterialLot> selectMaterialLot(@Param("locatorCode") String locatorCode, @Param("materialLotNum") String materialLotNum);

    /**
     * @param tenantId
     * @param materialLotCode
     * @return com.ruike.hme.api.dto.HmePreSelectionReturnDTO5
     * @description 查询盒子信息
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/2 14:30
     **/
    HmePreSelectionReturnDTO5 materialLotQuery(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);

    List<HmePreSelectionReturnDTO5> materialLotBatchQuery(@Param("tenantId") Long tenantId, @Param("materialLotCodeList") List<String> materialLotCodeList);

    Long selectCosNumByMaterialLotCode(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);

    List<HmePreSelectionReturnDTO5> selectBatchCosNumByMaterialLotCode(@Param("tenantId") Long tenantId, @Param("materialLotCodeList") List<String> materialLotCodeList);

    List<MtMaterialLot> selectMaterialLotByContainerCode(@Param("containerCode") String containerCode);

    List<HmePreSelectionReturnDTO3> selectLotQuery(@Param("selectLot") Long tenantId, String selectLot);

    /**
     * @param tenantId
     * @param containerCode
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO5>
     * @description 根据容器获取盒子信息
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/3 15:08
     **/
    List<HmePreSelectionReturnDTO5> materialLotQueryByContainer(@Param("tenantId") Long tenantId, @Param("containerCode") String containerCode);

    /**
     * @param tenantId
     * @param productTyperesult
     * @param ruleCode
     * @param statusresult
     * @param materialCode
     * @param materialName
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO6>
     * @description 获取挑选批次
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/2 16:33
     **/
    List<HmePreSelectionReturnDTO6> selectLotQueryAll(@Param("tenantId") Long tenantId,
                                                      @Param("productTyperesult") String productTyperesult,
                                                      @Param("ruleCode") String ruleCode,
                                                      @Param("statusresult") String statusresult,
                                                      @Param("materialCode") String materialCode,
                                                      @Param("materialName") String materialName);

    /**
     * @param tenantId
     * @param containerCode
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO7>
     * @description 挑选未挑选批次查询
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/2 17:34
     **/
    List<HmePreSelectionReturnDTO7> selectLotQueryElse(@Param("tenantId") Long tenantId, @Param("containerCode") String containerCode);

    HmePreSelection selectbyDetails(@Param("selectionDetailsId") String selectionDetailsId);


    List<HmePreSelectionReturnDTO8> selectLotInformation(@Param("tenantId") Long tenantId, @Param("selectLot") String selectLot);

    String qtyQueryByContainer(@Param("tenantId") Long tenantId, @Param("containerCode") String containerCode);

    WmsLocatorTransferVO selectMaterialLotInfo(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 根据挑选批次查询电流和采集项
     *
     * @param tenantId  租户ID
     * @param selectLot 挑选批次
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO10>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/12 13:54:50
     */
    List<HmePreSelectionReturnDTO10> selectCosRuleLogicBySelectLot(@Param("tenantId") Long tenantId, @Param("selectLot") String selectLot);

    /**
     * 挑选结果撤回数据查询
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO12>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/12 15:13:11
     */
    List<HmePreSelectionReturnDTO12> recallDataQuery(@Param("tenantId") Long tenantId, @Param("dto") HmePreSelectionReturnDTO11 dto);

    /**
     * 根据虚拟号查询预挑选基础表ID
     *
     * @param tenantId   租户ID
     * @param virtualNum 虚拟号
     * @return java.lang.String
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/12 16:31:33
     */
    String getPreSelectionIdByVirtualNum(@Param("tenantId") Long tenantId, @Param("virtualNum") String virtualNum);

    /**
     * 根据虚拟号查询装载表数据
     *
     * @param tenantId   租户ID
     * @param virtualNum 虚拟号
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/12 16:31:33
     */
    List<HmeMaterialLotLoad> selectMaterialLotLoadByVirtualNum(@Param("tenantId") Long tenantId, @Param("virtualNum") String virtualNum);

    /**
     * 将物料批的顶层容器和当前容器置空
     *
     * @param tenantId   租户ID
     * @param userId     用户ID
     * @param materialLotIdList 物料批ID集合
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/12 16:31:33
     */
    void batchUpdateMaterialLotContainerId(@Param("tenantId") Long tenantId, @Param("userId") Long userId,
                                           @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 将物料批的顶层容器和当前容器置空
     *
     * @param tenantId   租户ID
     * @param materialLotId     物料批ID
     * @param attrName 扩展名
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/12 16:31:33
     */
    String getSingleAttrValueByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId,
                                             @Param("attrName") String attrName);

    /**
     * 批量插入数据到GP数据删除记录表
     *
     * @param tenantId 租户ID
     * @param gpDelRecordList 插入数据集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/2 02:34:31
     * @return void
     */
    void gpDelRecordBatchInsert(@Param("tenantId") Long tenantId, @Param("gpDelRecordList") List<HmePreSelectionVO> gpDelRecordList);

    /**
     * 根据COS类型和wafer查询COS测试良率监控头表数据
     *
     * @param tenantId 租户ID
     * @param cosType  cos类型
     * @param wafer    wafer
     * @return com.ruike.hme.api.dto.HmePreSelectionDTO10
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/22 10:45:07
     */
    HmePreSelectionDTO10 cosTestMonitorHeaderQueryByCosTypeWafer(@Param("tenantId") Long tenantId, @Param("cosType") String cosType,
                                                                 @Param("wafer") String wafer);

    /**
     * 根据COS测试良率监控头表主键和物料批ID查询物料批状态
     *
     * @param tenantId           租户ID
     * @param cosMonitorHeaderId COS测试良率监控头表主键
     * @param materialLotId      物料批ID
     * @return java.lang.String
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/22 10:57:04
     */
    String getCosTestMonitorLineMaterialLotStatus(@Param("tenantId") Long tenantId, @Param("cosMonitorHeaderId") String cosMonitorHeaderId,
                                                  @Param("materialLotId") String materialLotId);

    /**
     * 根据wafer查询其是否存在于hme_cos_test_select_cancle表中
     * 
     * @param tenantId 租户ID
     * @param wafer wafer
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/12 10:46:50 
     * @return java.lang.Long
     */
    Long testSelectCancelCountQueryByWafer(@Param("tenantId") Long tenantId, @Param("wafer") String wafer);
}
