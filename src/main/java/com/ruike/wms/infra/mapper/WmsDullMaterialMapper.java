package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsDullMaterialImportQueryRequestDTO;
import com.ruike.wms.api.dto.WmsDullMaterialImportQueryResponseDTO;
import com.ruike.wms.api.dto.WmsDullMaterialQueryRequestDTO;
import com.ruike.wms.api.dto.WmsDullMaterialQueryResponseDTO;
import com.ruike.wms.domain.entity.WmsDullMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * @Classname DullMaterialMapper
 * @Description 呆滞物料报表
 * @Date 2019/10/29 17:58
 * @Author by {HuangYuBin}
 */

public interface WmsDullMaterialMapper extends BaseMapper<WmsDullMaterial> {
	/**
	 * 超期呆滞查询
	 *
	 * @param tenantId
	 * @param dto
	 * @return java.util.List<com.superlighting.hwms.api.controller.dto.DullMaterialQueryResponseDTO>
	 * @Description 超期呆滞查询
	 * @Date 2019/10/29 18:37
	 * @Created by {HuangYuBin}
	 */
	List<WmsDullMaterialQueryResponseDTO> queryOverDue(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") WmsDullMaterialQueryRequestDTO dto);

	/**
	 * 变更呆滞查询
	 *
	 * @param tenantId
	 * @param dto
	 * @param dullMaterialOverDueResponse
	 * @return java.util.List<com.superlighting.hwms.api.controller.dto.DullMaterialQueryResponseDTO>
	 * @Description 变更呆滞查询（排除超期呆滞的物料批）
	 * @Date 2019/10/29 18:34
	 * @Created by {HuangYuBin}
	 */
	List<WmsDullMaterialQueryResponseDTO> queryChange(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "dto") WmsDullMaterialQueryRequestDTO dto,
                                                   @Param(value = "dullMaterialOverDueResponse") List<WmsDullMaterialQueryResponseDTO> dullMaterialOverDueResponse);

	/**
	 * 无异动呆滞查询
	 *
	 * @param tenantId
	 * @param dto
	 * @param dullMaterialOverDueResponse
	 * @param dullMaterialChangeResponse
	 * @return java.util.List<com.superlighting.hwms.api.controller.dto.DullMaterialQueryResponseDTO>
	 * @Description 无异动呆滞查询（排除超期呆滞和变更呆滞）
	 * @Date 2019/10/29 19:12
	 * @Created by {HuangYuBin}
	 */
	List<WmsDullMaterialQueryResponseDTO> queryNoMove(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "dto") WmsDullMaterialQueryRequestDTO dto,
                                                   @Param(value = "dullMaterialOverDueResponse") List<WmsDullMaterialQueryResponseDTO> dullMaterialOverDueResponse,
                                                   @Param(value = "dullMaterialChangeResponse") List<WmsDullMaterialQueryResponseDTO> dullMaterialChangeResponse);

	/**
	 * 查询到符合条件的条码ID
	 *
	 * @param
	 * @return java.util.List<java.lang.String>
	 * @Description 查询到符合条件的条码ID
	 * @Date 2019/10/31 9:20
	 * @Created by {HuangYuBin}
	 */
	List<String> getMaterialLotIdByJob();

	/**
	 * 呆滞物料导入查询
	 *
	 * @param tenantId
	 * @param dto
	 * @return java.util.List<com.superlighting.hwms.api.controller.dto.DullMaterialImportQueryResponseDTO>
	 * @Description 呆滞物料导入查询
	 * @Date 2019/10/31 14:26
	 * @Created by {HuangYuBin}
	 */
	List<WmsDullMaterialImportQueryResponseDTO> importQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") WmsDullMaterialImportQueryRequestDTO dto);

	/**
	 * 超期呆滞查询并保存
	 *
	 * @param tenantId
	 * @param dto
	 * @return java.util.List<com.superlighting.hwms.api.controller.dto.DullMaterialQueryResponseDTO>
	 * @Description 超期呆滞查询并保存
	 * @Date 2019/10/29 18:37
	 * @Created by {HuangYuBin}
	 */
	void saveOverDueTemp(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") WmsDullMaterialQueryRequestDTO dto);

	/**
	 * 变更呆滞查询并保存
	 *
	 * @param tenantId
	 * @param dto
	 * @return java.util.List<com.superlighting.hwms.api.controller.dto.DullMaterialQueryResponseDTO>
	 * @Description 变更呆滞查询并保存（排除超期呆滞的物料批）
	 * @Date 2019/10/29 18:34
	 * @Created by {HuangYuBin}
	 */
	void saveChangeTemp(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") WmsDullMaterialQueryRequestDTO dto);

	/**
	 * 无异动呆滞查询保存
	 *
	 * @param tenantId
	 * @param dto
	 * @return java.util.List<com.superlighting.hwms.api.controller.dto.DullMaterialQueryResponseDTO>
	 * @Description 无异动呆滞查询保存（排除超期呆滞和变更呆滞）
	 * @Date 2019/10/29 19:12
	 * @Created by {HuangYuBin}
	 */
	void saveQueryNoMoveTemp(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") WmsDullMaterialQueryRequestDTO dto);

	/**
	 * 呆滞物料临时表查询
	 *
	 * @param tenantId
	 * @param dto
	 * @return java.util.List<com.superlighting.hwms.api.controller.dto.DullMaterialQueryResponseDTO>
	 * @Description 呆滞物料导入查询
	 * @Date 2019/10/31 14:26
	 * @Created by {HuangYuBin}
	 */
	List<WmsDullMaterialQueryResponseDTO> tempQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") WmsDullMaterialQueryRequestDTO dto);

	/**
	 * 呆滞物料临时表导入关联查询
	 *
	 * @param tenantId
	 * @return java.util.List<com.superlighting.hwms.api.controller.dto.DullMaterialImportQueryResponseDTO>
	 * @Description 呆滞物料导入查询
	 * @Date 2019/10/31 14:26
	 * @Created by {HuangYuBin}
	 */
	List<WmsDullMaterialImportQueryResponseDTO> tempImportQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") WmsDullMaterialImportQueryRequestDTO dto);

	void clearTemp();

}