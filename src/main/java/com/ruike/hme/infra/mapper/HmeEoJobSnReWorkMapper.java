package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmePumpCombDTO;
import com.ruike.hme.api.dto.HmePumpCombDTO2;
import com.ruike.hme.domain.entity.HmeServiceSplitRecord;
import com.ruike.hme.domain.vo.HmeEoJobSnReworkVO;
import com.ruike.hme.domain.vo.HmeEoJobSnReworkVO3;
import com.ruike.hme.domain.vo.HmeWorkOrderVO2;
import org.apache.ibatis.annotations.Param;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.modeling.domain.entity.MtModLocator;

import java.math.BigDecimal;
import java.util.List;

/**
 * 返修作业平台-SN作业Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020/12/23 11:48
 */
public interface HmeEoJobSnReWorkMapper {

    /**
     * 查询拆机记录
     *
     * @param tenantId
     * @param workOrderNum
     * @author jiangling.zheng@hand-china.com 2020/12/23 11:48
     * @return com.ruike.hme.domain.entity.HmeServiceSplitRecord
     */
    HmeServiceSplitRecord lastServiceSplitRecordGet(@Param("tenantId") Long tenantId, @Param("workOrderNum") String workOrderNum);

    /**
     * 查询物料
     *
     * @param tenantId
     * @param materialId 物料ID
     * @param siteId 站点ID
     * @author penglin.sui@hand-china.com 2020/12/4 14:48
     * @return HmeEoJobSnReworkVO
     */
    HmeEoJobSnReworkVO selectMaterial(@Param("tenantId") Long tenantId, @Param("materialId") String materialId, @Param("siteId") String siteId);

    /**
     * 查询条码
     *
     * @param tenantId
     * @param materialLotCode 条码编码
     * @author penglin.sui@hand-china.com 2020/12/24 11:48
     * @return HmeEoJobSnReworkVO3
     */
    HmeEoJobSnReworkVO3 selectMaterialLot(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);

    /**
     * 获取装配数量
     * @param tenantId
     * @param eoId
     * @param eoStepId
     * @param materialId
     * @return java.math.BigDecimal
     * @author sanfeng.zhang@hand-china.com 2021/5/29
     */
    BigDecimal queryAssembleQty(@Param("tenantId") Long tenantId,  @Param("eoId") String eoId, @Param("eoStepId") String eoStepId, @Param("materialId") String materialId);

    /**
     * 查询配送货位
     *
     * @param tenantId
     * @param workcellId
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     * @author sanfeng.zhang@hand-china.com 2021/7/29
     */
    List<MtModLocator> queryDeliveryLocator(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId, @Param("siteId") String siteId);

    /**
     * 查询条码返修条码
     * @param tenantId
     * @param materialLotId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/8/12
     */
    String queryReworkMaterialLotByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 返修条码查询原条码
     * @param tenantId
     * @param materialLotId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/8/16
     */
    List<String> queryNewMaterialLotByOldMaterialLot(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 返修条码查询原条码
     * @param tenantId
     * @param materialLotCode
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/8/16
     */
    String queryNearNewMaterialLotByOldMaterialLot(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);

    /**
     * 返修条码
     *
     * @param tenantId
     * @param materialLotId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/8/13
     */
    String queryReworkMaterialLotCode(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 根据旧条码查返修条码
     * @param tenantId
     * @param materialLotCode
     * @return
     */
    String queryReworkMaterialLotCodeByOldCode(@Param("tenantId") Long tenantId,@Param("materialLotCode") String materialLotCode);

    /**
     * 根据返修旧条码查新EO
     * @param tenantId
     * @param materialLotCode
     * @return
     */
    List<String> queryReworkEoIdsByOldCode(@Param("tenantId") Long tenantId,@Param("materialLotCode") String materialLotCode);

    /**
     * 查询物料在规则有有效数据
     *
     * @param tenantId
     * @param materialId
     * @author sanfeng.zhang@hand-china.com 2021/9/13 14:09
     * @return java.lang.Long
     */
    Long queryPumpProcessFlag(@Param("tenantId") Long tenantId, @Param("materialId") String materialId);

    /**
     * 根据工艺路线名称查询工艺路线步骤
     *
     * @param tenantId   租户ID
     * @param eoId 工艺路线名称
     * @return java.util.List<java.lang.String>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/10 03:04:10
     */
    List<String> getRouterStepIdByEoId(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     * 根据工步、工艺查询Bom组件
     *
     * @param tenantId         租户ID
     * @param operationId      工艺ID
     * @param routerStepIdList 工步ID集合
     * @return java.util.List<java.lang.String>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/10 03:11:01
     */
    List<MtBomComponent> getBomComponentIdByRouterStepAndOperation(@Param("tenantId") Long tenantId, @Param("operationId") String operationId,
                                                                   @Param("routerStepIdList") List<String> routerStepIdList);

    /**
     * 查询泵浦源组合信息
     *
     * @param tenantId
     * @param materialLotId
     * @author sanfeng.zhang@hand-china.com 2021/9/22 16:26
     * @return java.util.List<com.ruike.hme.api.dto.HmePumpCombDTO>
     */
    List<HmePumpCombDTO> queryPumpCombListByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 查询泵浦源组合的电压和功率
     *
     * @param tenantId
     * @param cmbJobIds
     * @author sanfeng.zhang@hand-china.com 2021/8/31 7:28
     * @return java.util.List<com.ruike.hme.api.dto.HmePumpCombDTO2>
     */
    List<HmePumpCombDTO2> queryPumpTagRecordResult(@Param("tenantId") Long tenantId, @Param("cmbJobIds") List<String> cmbJobIds);


    /**
     * 查询退料货位
     * @param tenantId
     * @param workcellId
     * @param siteId
     * @return
     */
    List<MtModLocator> queryReleaseBackLocator(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId, @Param("siteId") String siteId);

    /**
     * 原工单销单信息
     *
     * @param tenantId
     * @param eoId
     * @author sanfeng.zhang@hand-china.com 2021/11/4 16:51
     * @return com.ruike.hme.domain.vo.HmeWorkOrderVO2
     */
    HmeWorkOrderVO2 querySourceMaterialLotSoNum(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     * 查询最近状态为运行的新条码
     *
     * @param tenantId
     * @param materialLotCode
     * @author sanfeng.zhang@hand-china.com 2021/11/19 15:29
     * @return tarzan.inventory.domain.entity.MtMaterialLot
     */
    MtMaterialLot queryLastWorkingMaterialLotCode(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);
}
