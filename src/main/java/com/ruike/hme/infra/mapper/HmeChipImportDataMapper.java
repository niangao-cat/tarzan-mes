package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeChipImportDTO;
import com.ruike.hme.api.dto.HmeChipImportDTO2;
import com.ruike.hme.api.dto.HmeChipImportDTO3;
import com.ruike.hme.domain.entity.HmeChipImportData;
import com.ruike.hme.domain.vo.HmeChipImportVO;
import com.ruike.hme.domain.vo.HmeChipImportVO2;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 六型芯片导入临时表Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-01-25 13:56:00
 */
public interface HmeChipImportDataMapper extends BaseMapper<HmeChipImportData> {

    /**
     * 头部数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/25 14:39:57
     * @return java.util.List<com.ruike.hme.domain.entity.HmeChipImportData>
     */
    List<HmeChipImportVO> headDataQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeChipImportDTO dto);

    /**
     * 行数据查询
     *
     * @param tenantId 租户ID
     * @param dto 头部数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/25 15:08:31
     * @return java.util.List<com.ruike.hme.domain.entity.HmeChipImportData>
     */
    List<HmeChipImportData> lineDataQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeChipImportDTO2 dto);

    /**
     * 打印数据查询
     *
     * @param tenantId 租户ID
     * @param printFlag 打印标识
     * @param workNum 工单
     * @param creationDateFrom 导入时间从
     * @param creationDateTo 导入时间至
     * @param dto 头部数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/25 15:41:15
     * @return java.util.List<com.ruike.hme.domain.vo.HmeChipImportVO2>
     */
    List<HmeChipImportVO2> printDataQuery(@Param("tenantId") Long tenantId, @Param("printFlag") String printFlag,
                                          @Param("workNum") String workNum, @Param("creationDateFrom") Date creationDateFrom,
                                          @Param("creationDateTo") Date creationDateTo, @Param("dto") HmeChipImportVO dto);
    /**
     * 更新打印标识
     *
     * @param tenantId 租户ID
     * @param kids 主键集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/25 15:57:38
     * @return void
     */
    void updatePrintFlag(@Param("tenantId") Long tenantId, @Param("kids") List<String> kids);

    /**
     * 根据物料、站点查询物料组
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param siteId 站点ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/25 17:03:22
     * @return java.lang.String
     */
    String getItemGropByMaterial(@Param("tenantId") Long tenantId, @Param("materialId") String materialId,
                                 @Param("siteId") String siteId);

    /**
     * 根据Bom获取Bom组件物料
     *
     * @param tenantId 租户ID
     * @param bomId bomId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/25 17:08:24
     * @return java.util.List<java.lang.String>
     */
    List<String> getBomMaterialByBomId(@Param("tenantId") Long tenantId, @Param("bomId") String bomId);

    /**
     *
     *
     * @param tenantId
     * @param routerId
     * @param operationId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/26 02:39:59
     * @return java.lang.String
     */
    String getRouterStepId(@Param("tenantId") Long tenantId, @Param("routerId") String routerId,
                           @Param("operationId") String operationId);

    String getRouterOperationId(@Param("tenantId") Long tenantId, @Param("routerId") String routerId,
                           @Param("operationId") String operationId);

    String getBomComponentId(@Param("tenantId") Long tenantId, @Param("bomId") String bomId,
                             @Param("materialId") String materialId, @Param("routerOperationId") String routerOperationId);

    BigDecimal getAssembleQtySum(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId,
                                 @Param("bomComponentId") String bomComponentId);

    /**
     * 查询目标条码信息
     *
     * @param tenantId
     * @param targetBarcodeList
     * @author sanfeng.zhang@hand-china.com 2021/3/30 13:59
     * @return java.util.List<com.ruike.hme.domain.vo.HmeChipImportVO>
     */
    List<HmeChipImportVO> queryTargetBarcodeInfoList(@Param("tenantId") Long tenantId, @Param("targetBarcodeList") List<String> targetBarcodeList);
}
