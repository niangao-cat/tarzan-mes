package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsSummaryOfCosBarcodeProcessingDTO;
import com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description COS条码加工汇总表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @version 0.0.1
 * @return
 */
public interface WmsSummaryOfCosBarcodeProcessingMapper {

    List<WmsSummaryOfCosBarcodeProcessingVO> list(@Param(value = "tenantId") Long tenantId, @Param(value = "userId") Long userId, @Param(value = "dto") WmsSummaryOfCosBarcodeProcessingDTO dto);


    /**
     * 批量查询不良数
     *
     * @param tenantId
     * @param workOrderIdList
     * @param waferNumList
     * @param materialLotIdList
     * @param createByList
     * @param workcellIdList
     * @return java.util.List<com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO>
     * @author sanfeng.zhang@hand-china.com 2021/1/15 15:23
     */
    List<WmsSummaryOfCosBarcodeProcessingVO> batchListQueryNg(@Param(value = "tenantId") Long tenantId,
                                                              @Param(value = "workOrderIdList") List<String> workOrderIdList,
                                                              @Param(value = "waferNumList") List<String> waferNumList,
                                                              @Param(value = "materialLotIdList") List<String> materialLotIdList,
                                                              @Param(value = "createByList") List<String> createByList,
                                                              @Param(value = "workcellIdList") List<String> workcellIdList);

    /**
     * 批量查询设备
     * @param tenantId
     * @param jobIdList
     * @return java.util.List<com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO>
     * @author sanfeng.zhang@hand-china.com 2021/1/15 15:23
     */
    List<WmsSummaryOfCosBarcodeProcessingVO> batchListQueryAssetEncoding(@Param(value = "tenantId") Long tenantId,
                                                              @Param(value = "jobIdList") List<String> jobIdList);
}
