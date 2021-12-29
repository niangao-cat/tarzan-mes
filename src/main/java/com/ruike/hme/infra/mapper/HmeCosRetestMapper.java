package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.entity.HmeRetestImportData;
import com.ruike.hme.domain.vo.HmeCosRetestImportVO2;
import com.ruike.hme.domain.vo.HmeCosRetestImportVO3;
import com.ruike.hme.domain.vo.HmeCosRetestVO10;
import org.apache.ibatis.annotations.Param;
import tarzan.method.domain.entity.MtRouterOperation;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/19 14:11
 */
public interface HmeCosRetestMapper {

    /**
     * 工位找线边仓
     *
     * @param tenantId
     * @param workcellId
     * @param siteId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/1/19 16:20
     */
    List<String> queryLineSideLocator(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId, @Param("siteId") String siteId);

    /**
     * 工艺路线步骤
     *
     * @param tenantId
     * @param routerId
     * @param operationId
     * @return tarzan.method.domain.entity.MtRouterOperation
     * @author sanfeng.zhang@hand-china.com 2021/1/19 17:19
     */
    MtRouterOperation queryRouteOperation(@Param("tenantId") Long tenantId, @Param("routerId") String routerId, @Param("operationId") String operationId);

    /**
     * 查询工单组件中物料组为3101-芯片的物料
     *
     * @param tenantId
     * @param bomId
     * @param siteId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/1/25 0:19
     */
    String queryCosMaterialByBomId(@Param("tenantId") Long tenantId, @Param("bomId") String bomId, @Param("siteId") String siteId);

    /**
     * 退料条码清单
     *
     * @param tenantId
     * @param materialLotId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosRetestVO10>
     * @author sanfeng.zhang@hand-china.com 2021/1/25 0:44
     */
    List<HmeCosRetestVO10> queryReturnMaterialLotCodeList(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 投料条码清单
     *
     * @param tenantId
     * @param materialLotId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosRetestVO10>
     * @author sanfeng.zhang@hand-china.com 2021/1/25 1:03
     */
    List<HmeCosRetestVO10> queryFeelMaterialLotCodeList(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 更新目标条码相同的打印状态
     *
     * @param tenantId
     * @param targetBarcodeList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/25 11:02
     */
    void updateCosRetestPrintFlag(@Param("tenantId") Long tenantId, @Param("targetBarcodeList") List<String> targetBarcodeList);

    /**
     * COS复测导入-查询头信息
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosRetestImportVO3>
     * @author sanfeng.zhang@hand-china.com 2021/1/25 11:09
     */
    List<HmeCosRetestImportVO3> cosRetestImportHeaderDataList(@Param("tenantId") Long tenantId, @Param("dto") HmeCosRetestImportVO2 dto);

    /**
     * COS复测导入-查询行信息
     *
     * @param tenantId
     * @param targetBarcode
     * @return java.util.List<com.ruike.hme.domain.entity.HmeRetestImportData>
     * @author sanfeng.zhang@hand-china.com 2021/1/25 11:10
     */
    List<HmeRetestImportData> cosRetestImportLineList(@Param("tenantId") Long tenantId, @Param("targetBarcode") String targetBarcode);

    /**
     * 批量更新装载信息工单
     *
     * @param tenantId
     * @param userId
     * @param materialLotLoadList
     * @author sanfeng.zhang@hand-china.com 2021/3/2 16:09
     * @return void
     */
    void batchLoadUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("materialLotLoadList") List<HmeMaterialLotLoad> materialLotLoadList);

    /**
     * 取最近的装载表WAFER
     *
     * @param tenantId
     * @param materialLotId
     * @author sanfeng.zhang@hand-china.com 2021/11/17 14:29
     * @return java.lang.String
     */
    String queryLastMaterialLotLoad(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);
}
