package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.vo.WmsGenTypeSiteVO;
import org.apache.ibatis.annotations.Param;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;

import java.util.List;
import java.util.Map;

/**
 * @Classname DeliveryDocMapper
 * @Description 送货单查询Mapper
 * @Date 2019/9/20 9:08
 * @Author by {HuangYuBin}
 */
public interface WmsDeliveryDocMapper {

	/**
	 * 根据查询条件查询指令单据ID
	 *
	 * @param dto 查询条件
	 * @param siteIdList
	 * @return java.util.List<java.lang.String>
	 * @Description 根据查询条件查询指令单据ID
	 * @Date 2019/9/20 10:46
	 * @Author by {HuangYuBin}
	 */
	List<String> instructionDocQuery(@Param(value = "dto") WmsInstructionDocRequestDTO dto, @Param(value = "siteIdList") List<String> siteIdList, @Param(value = "typeList") List<String> typeList);

	/**
	 * 根据指令单据ID列表查询指令单据属性
	 *
	 * @param instructionDocIdList
	 * @return java.util.List<com.ruike.wms.api.dto.api.controller.dto.InstructionDocRequestDTO>
	 * @Description 根据指令单据ID列表查询指令单据属性
	 * @Date 2019/9/20 13:23
	 * @Author by {HuangYuBin}
	 */
	List<WmsInstructionDocResponseDTO> instructionDocPropertyBatchGet(List<String> instructionDocIdList);

	/**
	 * 根据指令Id列表获取对应的指令属性和指令实绩属性
	 *
	 * @param instructionIdList
	 * @return java.util.List
	 * @Description 根据指令Id列表获取对应的指令属性和指令实绩属性
	 * @Date 2019/9/20 16:10
	 * @Author by {HuangYuBin}
	 */
	List<WmsInstructionDTO> instructionPropertyBatchGet(List<String> instructionIdList);

	/**
	 * 根据送货单头、行查询materialLot_id
	 *
	 * @param dto
	 * @return java.util.List<java.lang.String>
	 * @Description 根据送货单头、行查询materialLot_id
	 * @Date 2019/9/21 13:13
	 * @Author by {HuangYuBin}
	 */
	List<String> getMaterialLotIdByDelivery(WmsInstructionDetailRequestDTO dto);


	/**
	 * 根据页面条件查询materialLot_id
	 *
	 * @param dto
	 * @return java.util.List<java.lang.String>
	 * @Description 根据页面条件查询materialLot_id
	 * @Date 2019/9/24 20:42
	 * @Created by {HuangYuBin}
	 */
	List<String> getMaterialLotIdByCondition(WmsInstructionDetailRequestDTO dto);


	/**
	 * 根据materialLotIdList查询指令明细
	 *
	 * @param materialLotIdListOne
	 * @param instructionId
	 * @return java.util.List<com.ruike.wms.api.dto.api.controller.dto.InstructionDetailResponseDTO>
	 * @Description 根据materialLotIdList查询指令明细
	 * @Date 2019/9/21 14:32
	 * @Author by {HuangYuBin}
	 */
	List<WmsInstructionDetailResponseDTO> instructionDetailQuery(@Param("list") List<String> materialLotIdListOne, @Param("instructionId") String instructionId);

	/**
	 * 根据用户ID获取站点ID
	 *
	 * @param userId
	 * @return java.lang.String
	 * @Description 根据用户ID获取站点ID
	 * @Date 2019/9/23 10:55
	 * @Author by {HuangYuBin}
	 */
	String getSiteId(@Param("userId") Long userId);

	/**
	 * @Description 查询指定送货单ID所有行数据
	 * @param deliveryVO
	 * @return java.util.List<com.ruike.wms.api.dto.api.controller.dto.PrdDeliveryLineDTO>
	 * @Date 2019/10/14 18:52
	 * @Author admin
	 */
	List<WmsPrdDeliveryLineDTO> queryInstructionLines(WmsPrdDeliveryVO deliveryVO);

	/**
	 * @Description 查询指定送货单ID所有行数据  单据类型为PO
	 * @param deliveryVO
	 * @return java.util.List<com.ruike.wms.api.dto.api.controller.dto.PrdDeliveryLineDTO>
	 * @Date 2019/10/14 18:52
	 * @Author admin
	 */
	List<WmsPrdDeliveryLineDTO> queryInstructionLinesPo(WmsPrdDeliveryVO deliveryVO);


	/**
	 * @Description 查询指定送货单ID所有已执行行数据
	 * @param deliveryVO
	 * @return java.util.List<com.ruike.wms.api.dto.api.controller.dto.PrdDeliveryLineDTO>
	 * @Date 2019/10/14 18:52
	 * @Author admin
	 */
	List<WmsPrdDeliveryLineDetailDTO> queryInstructionDetail(WmsPrdDeliveryVO deliveryVO);

	/**
	 * @Description 查询Instruction的目标仓库
	  * @param tenantId
	 * @param instructionIds
	 * @return java.util.List<com.ruike.wms.api.dto.api.controller.dto.PrdDeliveryLineDTO>
	 * @Date 2019/10/18 10:29
	 * @Author admin
	 */
	List<WmsPrdDeliveryLineDTO> queryLocatorByInstruction(@Param(value = "tenantId") Long tenantId,
														  @Param("instructionIds") List<String> instructionIds);

	/**
	 * @Description 查询指令行的实绩DetailId
	  * @param instructionLine
	 * @return java.lang.String
	 * @Date 2019/10/21 16:20
	 * @Author admin
	 */
	String queryActualDetailId(WmsPrdDeliveryLineDetailDTO instructionLine);

	/**
	 * @Description  模糊查询出货单号
	  * @param condition
	 * @return java.util.Map<java.lang.String,java.lang.String>
	 * @Date 2019/10/22 9:30
	 * @Author admin
	 */
	List<Map<String,String>> queryLikeDocNum(WmsPrdDeliveryQryDTO condition);

	/**
	 * @Description 查询出货单列表根据条件
	  * @param condition
	 * @return java.util.List<tarzan.instruction.domain.entity.MtInstructionDoc>
	 * @Date 2019/10/22 11:19
	 * @Author admin
	 */
	List<MtInstructionDoc> queryInstructionDocs(WmsPrdDeliveryQryDTO condition);

	/**
	 * @Description 刷新产品出货行下一个货位
	  * @param deliveryLineVO
	 * @return com.ruike.wms.api.controller.vo.PrdDeliveryLineVO
	 * @Date 2019/11/7 15:55
	 * @Author weihua.liao
	 */
	WmsPrdDeliveryLineVO queryNextLocator(WmsPrdDeliveryLineVO deliveryLineVO);

	/**
	 * @Description 查询要完成的所有事务数据
	  * @param instructionDocId
	 * @return java.util.List<com.ruike.wms.api.controller.dto.PrdDeliveryLineDetailDTO>
	 * @Date 2019/11/15 16:50
	 * @Author weihua.liao
	 */
	List<WmsPrdDeliveryLineDetailDTO> queryCompleteTran(@Param("instructionDocId") String instructionDocId);

	/**
	 * @Description 查询指定单据
	  * @param instructionDocId
	 * @param tenantId
	 * @return com.ruike.wms.api.controller.dto.PdInstructionDocDTO
	 * @Date 2019/12/13 14:59
	 * @Author weihua.liao
	 */
	WmsPdInstructionDocDTO queryInstructionDoc(@Param("instructionDocId") String instructionDocId,
											   @Param(value = "tenantId") Long tenantId);

	/**
	 * @Description 查询当前用户下所有工厂
	  * @param tenantId
	 * @param module
	 * @param typeCode
	 * @return com.ruike.wms.domain.entity.GenTypeSite
	 * @Date 2019/12/13 15:32
	 * @Author weihua.liao
	 */
	List<WmsGenTypeSiteVO> queryRelationSite(@Param(value = "tenantId") Long tenantId, @Param("module") String module,
                                           @Param("typeCode") String typeCode);

	/**
	 * @Description 查询非Base基地指令
	  * @param deliveryVO
	 * @return tarzan.instruction.domain.entity.MtInstruction
	 * @Date 2019/12/16 11:00
	 * @Author weihua.liao
	 */
	List<MtInstruction> queryNonBaseLcl(WmsPrdDeliveryVO deliveryVO);
	/**
	 * @Description 刷新产品出货行下一个货位 单据为PO
	 * @param deliveryLineVO
	 * @return com.ruike.wms.api.controller.vo.PrdDeliveryLineVO
	 * @Date 2020/2/10 16:02
	 * @Created by yubin.huang
	 */
	WmsPrdDeliveryLineVO queryNextLocatorPo(WmsPrdDeliveryLineVO deliveryLineVO);


}