package com.ruike.wms.infra.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ruike.wms.api.dto.WmsMaterialLotLineDetailDTO;
import com.ruike.wms.domain.entity.WmsPutInStorageTask;
import com.ruike.wms.domain.vo.WmsInstructionLineVO;
import com.ruike.wms.domain.vo.WmsInstructionVO4;
import com.ruike.wms.domain.vo.WmsPutInStorageVO;
import com.ruike.wms.domain.vo.WmsExchangeLineVO;
import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtModLocator;

/**
 * WmsPutInStorageTaskMapper
 *
 * @author liyuan.lv@hand-china.com 2020/04/11 23:19
 */
public interface WmsPutInStorageTaskMapper extends BaseMapper<WmsPutInStorageTask> {
	/**
	 * 根据物料批条码获取指令ID
	 *
	 * @param materialLotId
	 * @return
	 */
	String getInstructionIdByMaterialLotId(@Param("materialLotId") String materialLotId);

	/**
	 * 根据物料批id列表获取物料批信息
	 *
	 * @param materialLotIdList
	 * @return
	 */
	List<WmsMaterialLotLineDetailDTO> queryDetailByMaterialLotIds(@Param("list") List<String> materialLotIdList);

	/**
	 * 根据指令获取对应的物料批ID列表
	 *
	 * @param tenantId
	 * @param instructionNum
	 * @return
	 */
	List<String> queryInstruction(@Param("tenantId") Long tenantId, @Param("instructionNum") String instructionNum);

	/**
	 * 根据指令查询总计和已确认数量
	 *
	 * @param instructionNum
	 * @return
	 */
	HashMap countNum(@Param("instructionNum") String instructionNum);

	/**
	 * 查询单据lov
	 *
	 * @param paramMap
	 * @return
	 */
    List<Map<String, String>> queryInstructionDocLov(Map<String, Object> paramMap);

	/**
	 * 查询物料行by物料批id
	 *
	 * @param tenantId
	 * @param materialLotIdList
	 * @return
	 */
	List<WmsInstructionLineVO> queryMaterialLotLineByLotIds(@Param("tenantId") Long tenantId, @Param("list") List<String> materialLotIdList);

	/**
	 * 查询执令行
	 *
	 * @param tenantId
	 * @param dto
	 * @return
	 */
	List<WmsInstructionLineVO> queryInstructionLine(@Param("tenantId") Long tenantId,
													@Param("dto") WmsInstructionVO4 dto);

	/**
	 *
	 * @param tenantId
	 * @param instructionDocId
	 * @param instructionId
	 * @return
	 */
	List<WmsPutInStorageVO> queryPoLinesByInstructionId(@Param("tenantId") Long tenantId,
														@Param("instructionDocId") String instructionDocId,
														@Param("instructionId") String instructionId);

	/**
	 * 获取料废调换单
	 *
	 * @param tenantId
	 * @param supplierId
	 * @param materialId
	 * @param materialVersion
	 * @return
	 */
	List<WmsExchangeLineVO> queryExchangeInstruction(@Param("tenantId") Long tenantId,
                                                     @Param("supplierId") String supplierId,
                                                     @Param("materialId") String materialId,
                                                     @Param("materialVersion") String materialVersion);

	/**
	 * 获取当前送货单头下，是否存在未入库状态单据行
	 * @param tenantId
	 * @param instructionDocId
	 * @return
	 */
	int executeInstructionCount(@Param(value = "tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);

	/**
	 * 获取料废调换单（限制了工厂）
	 * @param tenantId
	 * @param supplierId
	 * @param materialId
	 * @param materialVersion
	 * @param siteId
	 * @return
	 */
	List<WmsExchangeLineVO> queryExchangeInstructionWithSite(@Param("tenantId") Long tenantId,
													 @Param("supplierId") String supplierId,
													 @Param("materialId") String materialId,
													 @Param("materialVersion") String materialVersion,
													 @Param("siteId") String siteId);

	List<MtModLocator> queryLocatorWithSite(@Param("tenantId") Long tenantId,
										  @Param("siteId") String siteId,
										  @Param("locatorType") String locatorType);
}