package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @Classname MaterialGetReturnMapper
 * @Description 领退料查询 Mapper
 * @Date 2019/10/12 16:25
 * @Author by {HuangYuBin}
 */

public interface WmsMaterialGetReturnMapper {
	/**
	 * 领退料单查询
	 *
	 * @param dto
	 * @param tenantId
	 * @param finalSiteIdList
	 * @param finalTypeList
	 * @return java.util.List<com.ruike.wms.api.dto.api.controller.dto.MaterialGetReturnResponseDTO>
	 * @Description 领退料单查询
	 * @Date 2019/10/14 9:26
	 * @Created by {HuangYuBin}
	 */
	List<WmsMaterialGetReturnResponseDTO> queryInstructionDoc(@Param(value = "dto") WmsMaterialGetReturnRequestDTO dto,
															  @Param(value = "tenantId") Long tenantId,
															  @Param(value = "finalSiteIdList") List<String> finalSiteIdList,
															  @Param(value = "finalTypeList") List<String> finalTypeList);

	/**
	 * 根据指令行ID列表获取指令信息
	 *
	 * @param tenantId
	 * @param instructionIdList
	 * @return java.util.List
	 * @Description 根据指令行ID列表获取指令信息
	 * @Date 2019/10/14 13:36
	 * @Created by {HuangYuBin}
	 */
	List<WmsMaterialGetReturnLineDTO> queryInstructionByIdList(@Param(value = "tenantId") Long tenantId, @Param(value = "instructionIdList") List<String> instructionIdList);

	/**
	 * 根据送货单号和送货单行号获取送货单行ID和物料批ID
	 *
	 * @param tenantId
	 * @param instructionDocNum
	 * @param instructionLineNum
	 * @return java.lang.String
	 * @Description 根据送货单号和送货单行号获取送货单行ID和物料批ID
	 * @Date 2019/10/14 14:36
	 * @Created by {HuangYuBin}
	 */
	List<HashMap> getInstructionIdMaterialLotId(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "instructionDocNum") String instructionDocNum,
                                                @Param(value = "instructionLineNum") String instructionLineNum);

	/**
	 * 根据指令ID获取物料批ID
	 *
	 * @param tenantId
	 * @param instructionId
	 * @return java.util.List<java.lang.String>
	 * @Description 根据指令ID获取物料批ID
	 * @Date 2019/10/14 15:40
	 * @Created by {HuangYuBin}
	 */
	List<String> getMaterialLotId(@Param(value = "tenantId") Long tenantId, @Param(value = "instructionId") String instructionId);

	/**
	 * 根据id列表，模糊查询物料批code，精准查询物料批状态
	 *
	 * @param tenantId
	 * @param instructionLineNum
	 * @param materialLotIdList
	 * @param materialLotCode
	 * @param materialLotStatus
	 * @param finalInstructionId
	 * @return java.util.List<com.ruike.wms.api.dto.api.controller.dto.MaterialGetReturnLineDetailDTO>
	 * @Description 根据id列表，模糊查询物料批code，精准查询物料批状态
	 * @Date 2019/10/14 15:59
	 * @Created by {HuangYuBin}
	 */
	List<WmsMaterialGetReturnLineDetailDTO> queryInstructionDetail(@Param(value = "tenantId") Long tenantId,
																   @Param(value = "instructionLineNum") String instructionLineNum,
																   @Param(value = "materialLotIdList") List<String> materialLotIdList,
																   @Param(value = "materialLotCode") String materialLotCode,
																   @Param(value = "materialLotStatus") String materialLotStatus,
																   @Param(value = "finalInstructionId") String finalInstructionId);

	/**
	 * 根据送货单ID查询所有的送货单行的actualQty累加
	 *
	 * @param instructionDocId
	 * @return java.lang.Integer
	 * @Description 根据送货单ID查询所有的送货单行的actualQty累加
	 * @Date 2019/10/14 17:22
	 * @Created by {HuangYuBin}
	 */
	Integer queryInstructionActualQty(@Param(value = "instructionDocId") String instructionDocId);

	/**
	 * 获取当前单据下最大的单据行号
	 *
	 * @param instructionDocId
	 * @return java.lang.String
	 * @Description 获取当前单据下最大的单据行号
	 * @Date 2019/10/15 13:06
	 * @Created by {HuangYuBin}
	 */
	Integer getInstructionLineNumMax(@Param(value = "instructionDocId") String instructionDocId);
	/**
	 * @Description 查询单据行id
	 * @param materialGetReturnLineDto
	 * @return java.lang.String
	 * @Date 2019/11/28 16:30
	 * @Created by 许博思
	 */
	String getInId(WmsMaterialGetReturnLineDTO materialGetReturnLineDto);
	/**
	 * @Description 验证行唯一性
	 * @param line
	 * @return java.util.List<com.ruike.wms.api.dto.api.controller.dto.MaterialGetReturnLineSaveDTO.Line>
	 * @Date 2019/12/5 16:45
	 * @Created by 许博思
	 */
	List<WmsMaterialGetReturnLineSaveDTO.Line> queryLine(WmsMaterialGetReturnLineSaveDTO.Line line);
}