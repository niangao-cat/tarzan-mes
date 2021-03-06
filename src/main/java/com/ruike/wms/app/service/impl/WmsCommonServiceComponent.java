package com.ruike.wms.app.service.impl;

import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.profile.ProfileClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.inventory.domain.entity.MtContainerLoadDetail;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.entity.MtMaterialLotChangeHistory;
import tarzan.inventory.domain.repository.MtMaterialLotChangeHistoryRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO13;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.inventory.infra.mapper.MtContainerLoadDetailMapper;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtMaterialVO3;
import tarzan.material.domain.vo.MtUomVO1;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Classname CommonServiceComponent
 * @Description ??????????????????
 * @Date 2019/9/24 13:58
 * @Created by admin
 */
@Component
@Slf4j
public class WmsCommonServiceComponent {

	private static final String STATUS_RELEASED = "RELEASED";
	private static final String STATUS_COMPLETED_CANCEL = "COMPLETED_CANCEL";

	@Autowired
	private MtNumrangeRepository numrangeRepository;

	@Autowired
	private MtUserOrganizationRepository organizationRepository;

	@Autowired
	MtEventRepository eventRepository;

	@Autowired
	MtEventRequestRepository eventRequestRepository;

	@Autowired
	MtExtendSettingsRepository mtExtendSettingsRepository;

	@Autowired
	MtErrorMessageRepository mtErrorMessageRepository;

	@Autowired
	MtEventRepository mtEventRepository;

	@Autowired
	MtMaterialLotRepository mtMaterialLotRepository;

	@Autowired
	MtNumrangeRepository mtNumrangeRepository;

	@Autowired
	MtMaterialLotChangeHistoryRepository mtMaterialLotChangeHistoryRepository;

	@Autowired
	MtInstructionRepository mtInstructionRepository;

	@Autowired
	MtInstructionActualRepository mtInstructionActualRepository;

	@Autowired
	MtUomRepository mtUomRepository;

	@Autowired
	ProfileClient profileClient;

	@Autowired
	MtContainerLoadDetailMapper mtContainerLoadDetailMapper;

	@Autowired
	MtMaterialRepository mtMaterialRepository;

	/**
	 * @param tenantId
	 * @param eventCode
	 * @return java.lang.String
	 * @Description ??????????????????
	 * @Date 2019/9/24 14:05
	 * @Created by admin
	 */
	public String generateEvent(Long tenantId, String eventCode) {
		String eventRequestId = generateEventRequest(tenantId, eventCode);

		MtEventCreateVO eventDto = new MtEventCreateVO();
		eventDto.setEventTypeCode(eventCode);
		eventDto.setEventRequestId(eventRequestId);
		String eventId = eventRepository.eventCreate(tenantId, eventDto);
		log.debug("<==== get evnetId: {}", eventId);

		return eventId;
	}

	/**
	 * @Description ????????????
	  * @param tenantId
	 * @param eventCode
	 * @param eventRequestId
	 * @return java.lang.String
	 * @Date 2019/12/31 10:01
	 * @Author weihua.liao
	 */
	public String generateEvent(Long tenantId, String eventCode, String eventRequestId) {
		if (StringUtils.isEmpty(eventRequestId)) {
			eventRequestId = generateEventRequest(tenantId, eventCode);
		}
		MtEventCreateVO eventDto = new MtEventCreateVO();
		eventDto.setEventTypeCode(eventCode);
		eventDto.setEventRequestId(eventRequestId);
		String eventId = eventRepository.eventCreate(tenantId, eventDto);
		log.debug("<==== get evnetId: {}", eventId);

		return eventId;
	}

	/**
	 * @Description ??????????????????
	  * @param tenantId
	 * @param eventCode
	 * @return java.lang.String
	 * @Date 2019/12/31 10:02
	 * @Author weihua.liao
	 */
	public String generateEventRequest(Long tenantId, String eventCode) {
		String eventRequestId = eventRequestRepository.eventRequestCreate(tenantId, eventCode);
		log.debug("<==== get eventRequestId: {}", eventRequestId);

		return eventRequestId;
	}

	/**
	 * @param tenantId
	 * @param numrangeVO2
	 * @return io.tarzan.common.domain.vo.MtNumrangeVO5
	 * @Description ????????????
	 * @Date 2019/9/24 14:06
	 * @Created by admin
	 */
	public MtNumrangeVO5 generateNumber(Long tenantId, MtNumrangeVO2 numrangeVO2) {

		MtNumrangeVO5 numrangeVO5 = numrangeRepository.numrangeGenerate(tenantId, numrangeVO2);
		if (null == numrangeVO5) {
			log.error("<==== materialLotCreate numrangeGenerate is null");
			return null;
		}
		log.info("<==== numrangeGenerate code: {}", numrangeVO5.getNumber());
		return numrangeVO5;
	}

	/**
	 * @param tenantId
	 * @param userId
	 * @return tarzan.general.domain.entity.MtUserOrganization
	 * @Description ????????????????????????ID
	 * @Date 2019/10/12 10:49
	 * @Created by {HuangYuBin}
	 */
	public MtUserOrganization getUserOrganization(Long tenantId, Long userId) {
		MtUserOrganization userOrganization = new MtUserOrganization();
		userOrganization.setUserId(userId);
		userOrganization.setOrganizationType("SITE");
		MtUserOrganization mtUserOrganization = organizationRepository.userDefaultOrganizationGet(tenantId, userOrganization);
		if (mtUserOrganization == null) {
			log.error("<==== materialLotCreate userDefaultOrganizationGet is null");
			return null;
		}
		log.info("<==== userDefaultOrganizationGet code: {}", mtUserOrganization.getOrganizationId());

		return mtUserOrganization;
	}

	/**
	 * @param tenantId
	 * @param tableName ??????
	 * @param keyId
	 * @param eventId
	 * @return void
	 * @Description ??????????????????????????????,?????????????????????????????????????????????
	 * @Date 2019/10/12 10:54
	 * @Created by {HuangYuBin}
	 */
	@Transactional(rollbackFor = Exception.class)
	public void attrUpdate(Long tenantId, String tableName, String keyId, String eventId) {
		MtExtendVO mtExtend = new MtExtendVO();
		List<MtExtendVO5> mtExtend5List = new ArrayList<>();

		mtExtend.setTableName(tableName);
		mtExtend.setKeyId(keyId);
		//??????keyId???????????????????????????
		List<MtExtendAttrVO> mtExtendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtend);
		if (!CollectionUtils.isEmpty(mtExtendAttrList) && mtExtendAttrList.size() > 0) {
			for (MtExtendAttrVO temp : mtExtendAttrList) {
				MtExtendVO5 tempExtend5 = new MtExtendVO5();
				BeanUtils.copyProperties(temp, tempExtend5);
				mtExtend5List.add(tempExtend5);
			}
			mtExtendSettingsRepository.attrPropertyUpdate(tenantId, tableName, keyId, eventId, mtExtend5List);
		}
	}

	/**
	 * @param tenantId
	 * @param dto
	 * @return java.lang.String
	 * @Description ???????????? copy
	 * @Date 2019/10/17 15:58
	 * @Created by {HuangYuBin}
	 */
	@Transactional(rollbackFor = Exception.class)
	public String materialLotSplit(Long tenantId, MtMaterialVO3 dto) {
		// 1.?????????????????????????????????????????????
		if (StringUtils.isEmpty(dto.getSourceMaterialLotId())) {
			throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
					"MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "sourceMaterialLotId", "???API:materialLotSplit???"));
		}
		if (null == dto.getSplitPrimaryQty()) {
			throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
					"MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "splitPrimaryQty", "???API:materialLotSplit???"));
		}
		// SplitPrimaryQty<=0??????
		if (new BigDecimal(dto.getSplitPrimaryQty().toString()).compareTo(BigDecimal.ZERO) != 1) {
			throw new MtException("MT_MATERIAL_LOT_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
					"MT_MATERIAL_LOT_0007", "MATERIAL_LOT", "splitPrimaryQty", "???API:materialLotSplit???"));
		}

		// ???????????? ???????????????????????????
		MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getSourceMaterialLotId());
		if (mtMaterialLot == null) {
			throw new MtException("MT_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
					"MT_MATERIAL_LOT_0011", "MATERIAL_LOT", "???API:materialLotSplit???"));
		}
		// ??????
		// SplitPrimaryQty>=PrimaryUomQty??????
		if (new BigDecimal(dto.getSplitPrimaryQty().toString())
				.compareTo(new BigDecimal(mtMaterialLot.getPrimaryUomQty().toString())) != -1) {
			throw new MtException("MT_MATERIAL_LOT_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
					"MT_MATERIAL_LOT_0021", "MATERIAL_LOT", "???API:materialLotSplit???"));
		}
		// ??????????????????????????????????????????????????????????????????????????????
		if (StringUtils.isNotEmpty(mtMaterialLot.getSecondaryUomId()) && null != mtMaterialLot.getSecondaryUomQty()
				&& dto.getSplitSecondaryQty() == null) {
			throw new MtException("MT_MATERIAL_LOT_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
					"MT_MATERIAL_LOT_0022", "MATERIAL_LOT", "???API:materialLotSplit???"));
		}

		if (StringUtils.isNotEmpty(mtMaterialLot.getSecondaryUomId()) && null != mtMaterialLot.getSecondaryUomQty()) {
			// ?????????0??????SplitSecondaryQty<=0
			if (new BigDecimal(dto.getSplitSecondaryQty().toString()).compareTo(BigDecimal.ZERO) != 1) {
				throw new MtException("MT_MATERIAL_LOT_0007",
						mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0007",
								"MATERIAL_LOT", "splitSecondaryQty", "???API:materialLotSplit???"));
			}
			// SplitSecondaryQty>=SecondaryUomQty??????
			if (new BigDecimal(dto.getSplitSecondaryQty().toString())
					.compareTo(new BigDecimal(mtMaterialLot.getSecondaryUomQty().toString())) != -1) {
				throw new MtException("MT_MATERIAL_LOT_0021", mtErrorMessageRepository.getErrorMessageWithModule(
						tenantId, "MT_MATERIAL_LOT_0021", "MATERIAL_LOT", "???API:materialLotSplit???"));

			}
		}
		// ???????????????????????????????????????


		MtEventCreateVO eventCreateVO = new MtEventCreateVO();
		eventCreateVO.setEventTypeCode("MATERIAL_LOT_SPLIT");
		eventCreateVO.setLocatorId(mtMaterialLot.getLocatorId());
		eventCreateVO.setShiftCode(dto.getShiftCode());
		eventCreateVO.setShiftDate(dto.getShiftDate());
		eventCreateVO.setEventRequestId(dto.getEventRequestId());
		eventCreateVO.setWorkcellId(dto.getWorkcellId());
		eventCreateVO.setParentEventId(dto.getParentEventId());
		String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

		// ?????????????????????????????????????????????????????????
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();

        mtMaterialLotVO2.setMaterialLotId(dto.getSourceMaterialLotId());
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotVO2.setTrxPrimaryUomQty(-dto.getSplitPrimaryQty());

        if (StringUtils.isNotEmpty(mtMaterialLot.getSecondaryUomId())) {
            mtMaterialLotVO2.setTrxSecondaryUomQty(-dto.getSplitSecondaryQty());
        }

        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, WmsConstant.CONSTANT_N);

        mtMaterialLotVO2 = new MtMaterialLotVO2();
        // ?????????????????????????????????????????????????????????????????????????????????
        BeanUtils.copyProperties(mtMaterialLot, mtMaterialLotVO2);
        mtMaterialLotVO2.setEventId(eventId);

        //String materialLotCode = materialLotNextCodeGet(tenantId, mtMaterialLot.getSiteId());s
        MtMaterialLot mtMaterialLot2 = mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getSourceMaterialLotId());
        String lotCode = mtMaterialLot2.getLot();
        MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, mtMaterialLot2.getMaterialId());
		MtNumrangeVO2 mtNumrange = new MtNumrangeVO2();
		mtNumrange.setObjectCode("MATERIAL_LOT_CODE");
		Map<String, String> map = new HashMap<>(2);
		map.put("lotCode", lotCode);
		map.put("materialCode", mtMaterialVO.getMaterialCode());
		mtNumrange.setCallObjectCodeList(map);
		String materialLotCode = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrange).getNumber();
		mtMaterialLotVO2.setMaterialLotCode(materialLotCode);
		mtMaterialLotVO2.setMaterialLotId(null);
		mtMaterialLotVO2.setPrimaryUomQty(null);
		mtMaterialLotVO2.setTrxPrimaryUomQty(dto.getSplitPrimaryQty());
		if (StringUtils.isEmpty(mtMaterialLot.getSecondaryUomId())) {
            mtMaterialLotVO2.setSecondaryUomQty(null);
        } else {
            mtMaterialLotVO2.setSecondaryUomQty(dto.getSplitSecondaryQty());
        }

        mtMaterialLotVO2.setAssemblePointId(null);

        mtMaterialLotVO2.setCreateReason("SPLIT");
        mtMaterialLotVO2.setIdentification(null);

        MtMaterialLotVO13 mtMaterialLotVO13 = mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, WmsConstant.CONSTANT_N);
        String materialLotId = mtMaterialLotVO13.getMaterialLotId();

        // 6.???????????????????????????????????????????????????
        MtMaterialLotChangeHistory mtMaterialLotChangeHistory = new MtMaterialLotChangeHistory();
        mtMaterialLotChangeHistory.setMaterialLotId(materialLotId);
        mtMaterialLotChangeHistory.setSourceMaterialLotId(dto.getSourceMaterialLotId());
        mtMaterialLotChangeHistory.setReason("SPLIT");
        mtMaterialLotChangeHistory.setSequence(Long.valueOf(10L));
        mtMaterialLotChangeHistory.setEventId(eventId);
        mtMaterialLotChangeHistory.setTrxQty(dto.getSplitPrimaryQty());
		mtMaterialLotChangeHistory.setSourceTrxQty(-dto.getSplitPrimaryQty());
		mtMaterialLotChangeHistoryRepository.materialLotChangeHistoryCreate(tenantId, mtMaterialLotChangeHistory);
		return materialLotId;
	}

	/**
	 * @Description ????????????
	  * @param tenantId
	 * @param instructionId
	 * @return void
	 * @Date 2019/12/31 10:02
	 * @Author weihua.liao
	 */
	public void instructionCompletedVerify(Long tenantId, String instructionId) {
		if (StringUtils.isEmpty(instructionId)) {
			throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
					"MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "???API:instructionCompletedVerify???"));
		}

		MtInstruction mtInstruction = mtInstructionRepository.instructionPropertyGet(tenantId, instructionId);

		if (mtInstruction == null) {
			throw new MtException("MT_INSTRUCTION_0023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
					"MT_INSTRUCTION_0023", "INSTRUCTION", instructionId, "???API:instructionCompletedVerify???"));
		}
		if (!(STATUS_RELEASED.equals(mtInstruction.getInstructionStatus())
				|| STATUS_COMPLETED_CANCEL.equals(mtInstruction.getInstructionStatus()))) {
			throw new MtException("MT_INSTRUCTION_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
					"MT_INSTRUCTION_0032", "INSTRUCTION", "???API:instructionCompletedVerify???"));
		}

		List<MtInstructionActual> mtInstructionActualList =
				mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId, instructionId);

		if (CollectionUtils.isEmpty(mtInstructionActualList)) {
			throw new MtException("MT_INSTRUCTION_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
					"MT_INSTRUCTION_0024", "INSTRUCTION", "???API:instructionCompletedVerify???"));
		}
		MtInstructionActual mtInstructionActual = mtInstructionActualList.get(0);
		MtUomVO1 transferUomVO = new MtUomVO1();
		transferUomVO.setSourceUomId(mtInstruction.getUomId());
		transferUomVO.setSourceValue(mtInstruction.getQuantity());
		transferUomVO.setTargetUomId(mtInstructionActual.getUomId());
		transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);

		BigDecimal transferQty = BigDecimal.valueOf(transferUomVO.getTargetValue());
		BigDecimal actualQty = BigDecimal.valueOf(mtInstructionActual.getActualQty());
		if (transferQty.compareTo(actualQty) > 0) {
			throw new MtException("MT_INSTRUCTION_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
					"MT_INSTRUCTION_0025", "INSTRUCTION", "???API:instructionCompletedVerify???"));
		}
	}

	/**
	 * ??????????????????
	 *
	 * @param tenantId
	 * @param profileName
	 * @return java.lang.String
	 * @Description ??????????????????
	 * @Date 2019/11/25 13:53
	 * @Created by {HuangYuBin}
	 */
	public String getProfileValue(Long tenantId, String profileName) {
		//V20211129 modify by penglin.sui ?????????????????????????????????
		Long userId = -1L;
		Long roleId = -1L;
		CustomUserDetails customUserDetails = DetailsHelper.getUserDetails();
		if(Objects.nonNull(customUserDetails)){
			if(Objects.nonNull(customUserDetails.getUserId())) {
				userId = customUserDetails.getUserId();
			}
			if(Objects.nonNull(customUserDetails.getRoleId())) {
				roleId = customUserDetails.getRoleId();
			}
		}
		return profileClient.getProfileValueByOptions(tenantId,
				userId,
				roleId, profileName);

	}

	/**
	 * ???????????????????????????????????????
	 *
	 * @param tenantId       ??????ID
	 * @param loadObjectType ?????????????????? MATERIAL_LOT???CONTAINER
	 * @param loadObjectId   ????????????ID
	 * @return java.util.List<java.lang.String>
	 * @Description ???????????????????????????????????????
	 * @Date 2019/12/4 18:42
	 * @Created by {HuangYuBin}
	 */
	public List<String> objectLimitLoadingContainerAllQuery(Long tenantId, String loadObjectType, String loadObjectId) {
		// ??????????????????????????????????????????
		if (StringUtils.isEmpty(loadObjectId)) {
			throw new MtException("MT_MATERIAL_LOT_0001",
					mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
							"MATERIAL_LOT", "loadObjectId", "???API???objectLimitLoadingContainerAllQuery???"));
		}
		if (StringUtils.isEmpty(loadObjectType)) {
			throw new MtException("MT_MATERIAL_LOT_0001",
					mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
							"MATERIAL_LOT", "loadObjectType", "???API???objectLimitLoadingContainerAllQuery???"));
		}
		// ??????????????????????????????????????????????????????
		MtContainerLoadDetail mtContainerLoadDetail = new MtContainerLoadDetail();
		mtContainerLoadDetail.setLoadObjectType(loadObjectType);
		mtContainerLoadDetail.setLoadObjectId(loadObjectId);
		mtContainerLoadDetail.setTenantId(tenantId);
		MtContainerLoadDetail mtContainerLoadDetail1 = mtContainerLoadDetailMapper.selectOne(mtContainerLoadDetail);
		if (ObjectUtils.isEmpty(mtContainerLoadDetail1)) {
			//????????????????????????????????????????????????
			return Collections.emptyList();
		}
		// ???????????????containerId??????
		List<String> allContainerId = new ArrayList<>();
		allContainerId.add(mtContainerLoadDetail1.getContainerId());
		//??????????????????
		Boolean flag = true;
		while (flag) {
			//????????????????????????????????????????????????????????????
			mtContainerLoadDetail = new MtContainerLoadDetail();
			mtContainerLoadDetail.setLoadObjectType("CONTAINER");
			mtContainerLoadDetail.setLoadObjectId(mtContainerLoadDetail1.getContainerId());
			mtContainerLoadDetail.setTenantId(tenantId);
			mtContainerLoadDetail1 = mtContainerLoadDetailMapper.selectOne(mtContainerLoadDetail);
			if (ObjectUtils.isEmpty(mtContainerLoadDetail1)) {
				//??????????????????????????????
				flag = false;
			} else {
				allContainerId.add(mtContainerLoadDetail1.getContainerId());
			}
		}
		return allContainerId;
	}


}
