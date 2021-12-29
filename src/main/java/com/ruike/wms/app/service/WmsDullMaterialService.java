package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsDullMaterialImportQueryRequestDTO;
import com.ruike.wms.api.dto.WmsDullMaterialImportQueryResponseDTO;
import com.ruike.wms.api.dto.WmsDullMaterialQueryRequestDTO;
import com.ruike.wms.api.dto.WmsDullMaterialQueryResponseDTO;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * @Classname DullMaterialService
 * @Description 呆滞物料报表
 * @Date 2019/10/29 17:55
 * @Author by {HuangYuBin}
 */

public interface WmsDullMaterialService {
	String NOMOVE = "无异动呆滞";
	String OVERDUE = "超期呆滞";
	String CHANGE = "变更呆滞";

	/**
	 * 呆滞物料报表查询
	 *
	 * @param tenantId
	 * @param pageRequest
	 * @param dto
	 * @return io.choerodon.core.domain.Page<com.superlighting.hwms.api.controller.dto.DullMaterialQueryResponseDTO>
	 * @Description 呆滞物料报表查询
	 * @Date 2019/10/29 17:59
	 * @Created by {HuangYuBin}
	 */
	Page<WmsDullMaterialQueryResponseDTO> queryList(Long tenantId, PageRequest pageRequest, WmsDullMaterialQueryRequestDTO dto);

	/**
	 * 呆滞物料报表导出
	 *
	 * @param tenantId 租户ID
	 * @param dto 查询条件
	 * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/17 04:03:34
	 * @return java.util.List<com.ruike.wms.api.dto.WmsDullMaterialQueryResponseDTO>
	 */
	List<WmsDullMaterialQueryResponseDTO> exportList(Long tenantId, WmsDullMaterialQueryRequestDTO dto);

	/**
	 * 更新条码质量状态
	 *
	 * @param tenantId
	 * @return void
	 * @Description 更新条码质量状态
	 * @Date 2019/10/31 9:14
	 * @Created by {HuangYuBin}
	 */
	List<String> dellMaterialJob(Long tenantId);

	/**
	 * 呆滞物料导入查询
	 *
	 * @param tenantId
	 * @param pageRequest
	 * @param dto
	 * @return io.choerodon.core.domain.Page<com.superlighting.hwms.api.controller.dto.DullMaterialImportQueryResponseDTO>
	 * @Description 呆滞物料导入查询
	 * @Date 2019/10/31 13:32
	 * @Created by {HuangYuBin}
	 */
	Page<WmsDullMaterialImportQueryResponseDTO> importQuery(Long tenantId, PageRequest pageRequest, WmsDullMaterialImportQueryRequestDTO dto);

	/**
	 * 呆滞物料导入保存
	 *
	 * @param tenantId
	 * @param dto
	 * @return void
	 * @Description 呆滞物料导入保存
	 * @Date 2019/10/31 18:02
	 * @Created by {HuangYuBin}
	 */
	void importSave(Long tenantId, List<WmsDullMaterialImportQueryResponseDTO> dto);

}