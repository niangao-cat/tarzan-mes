package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.*;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface WmsMiscOutHipsMapper {
    /**查询成本中心lov
     * @Description 查询成本中心lov
     * @param tenantId
     * @param costCenterCode
     * @param finalSiteId
     * @return java.util.List<CostCenterLovResponseDTO>
     * @Date 2019/9/26 8:57
     * @Created by {HuangYuBin}
     */
    List<WmsCostCenterLovResponseDTO> costCenterLovQuery(@Param("tenantId") Long tenantId, @Param("costCenterCode") String costCenterCode, @Param(value = "finalSiteId") String finalSiteId);

    /**根据扫描到的条码查询Container
     * @Description 根据扫描到的条码查询Container
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @Date 2019/9/26 9:00
     * @Created by {HuangYuBin}
     */
    WmsMiscOutBarcodeHipsResponseDTO getContainerByCode(@Param("tenantId") Long tenantId, @Param("dto") WmsMiscOutBarcodeHipsRequestDTO dto);

    /**判断用户是否有对应工厂的权限
     * @Description 判断用户是否有对应工厂的权限，返回值大于0代表有权限
     * @param userId
     * @param siteId
     * @return java.lang.Integer
     * @Date 2019/9/26 10:02
     * @Created by {HuangYuBin}
     */
    Integer getByUserIdSiteId(@Param("userId") Long userId,@Param("siteId") String siteId);

    /**根据扫描到的条码查询MaterialLot
     * @Description 根据扫描到的条码查询MaterialLot
     * @param tenantId
     * @param dto
     * @return WmsMiscOutBarcodeHipsResponseDTO
     * @Date 2019/9/26 11:51
     * @Created by {HuangYuBin}
     */
    WmsMiscOutBarcodeHipsResponseDTO getMaterialLotByCode(@Param("tenantId") Long tenantId,@Param("dto") WmsMiscOutBarcodeHipsRequestDTO dto);

    /**根据物料批ID获取对应数据
     * @Description 根据物料批ID获取对应数据
     * @param materialLotList
     * @return java.util.List<tarzan.instruction.domain.vo.MtInstructionVO>
     * @Date 2019/9/26 16:37
     * @Created by {HuangYuBin}
     */
    List<WmsInstructionCreationDTO> materialLotQuery(@Param("materialLotList") List<String> materialLotList);

    /**
     * 根据物料批ID获取对应数据
     *
     * @param materialLotId
     * @return
     */
    WmsMaterialLotByStatusCodeDTO getMaterialLotByStatusCode(@Param("materialLotId") String materialLotId);
}
