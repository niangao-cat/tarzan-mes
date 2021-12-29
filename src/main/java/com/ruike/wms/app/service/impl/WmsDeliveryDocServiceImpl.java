package com.ruike.wms.app.service.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsDeliveryDocService;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.CommonQRCodeUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.feign.WmsHzeroIamFeignClient;
import com.ruike.wms.infra.mapper.WmsDeliveryDocMapper;
import com.ruike.wms.infra.mapper.WmsDistributionListQueryMapper;
import com.ruike.wms.infra.mapper.WmsPoDeliveryRelMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.NumberHelper;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO;
import io.tarzan.common.domain.vo.MtExtendVO1;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.file.FileClient;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionVO10;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @Classname DeliveryDocServiceImpl
 * @Description 送货单查询Service实现
 * @Date 2019/9/19 19:45
 * @Author by {HuangYuBin}
 */
@Slf4j
@Service
public class WmsDeliveryDocServiceImpl implements WmsDeliveryDocService {

	@Autowired
	private MtInstructionRepository mtInstructionRepository;

	@Autowired
	private MtInstructionActualRepository mtInstructionActualRepository;

	@Autowired
	private MtInstructionActualDetailRepository mtInstructionActualDetailRepository;

	@Autowired
	private MtExtendSettingsRepository mtExtendSettingsRepository;

	@Autowired
	private WmsDeliveryDocMapper deliveryDocMapper;
	@Autowired
	private WmsPoDeliveryRelMapper wmsPoDeliveryRelMapper;
	@Autowired
	private WmsHzeroIamFeignClient hzeroIamFeignClient;
	@Autowired
	private WmsSiteRepository wmsSiteRepository;
	@Autowired
	private LovAdapter lovAdapter;
	@Autowired
	private MtUserRepository mtUserRepository;

	@Override
	public Page<WmsInstructionDocResponseDTO> instructionDocQuery(Long tenantId, WmsInstructionDocRequestDTO dto, PageRequest pageRequest) {
		Page<WmsInstructionDocResponseDTO> instructionDocResponsePage = new Page<WmsInstructionDocResponseDTO>();
		List<String> siteIdList = new ArrayList<>();
		//如果工厂不选，把用户有权限的工厂全都作为条件
		if (StringUtils.isEmpty(dto.getSiteId())){
			siteIdList = wmsSiteRepository.getSite(tenantId).stream().map(WmsSiteDTO::getSiteId).collect(Collectors.toList());
		}
		//判断属性是否全部为空
		if(checkObjAllFieldsIsNull(dto)){
			dto.setIsAllNull(MtBaseConstants.YES);
		}
		List<String> typeList = new ArrayList<>();
		List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WMS.DELIVERY_TICKET_QUERY", tenantId);
		List<String> valueList = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(valueList)){
			typeList.add("DELIVERY_DOC");
		}else {
			typeList.addAll(valueList);
		}

		//根据查询条件查询instruction_doc_id_list
		List<String> instructionDocIdList = deliveryDocMapper.instructionDocQuery(dto,siteIdList,typeList);

		if (instructionDocIdList.size() > 0) {
			//根据instruction_doc_id_list查询对应的具体属性
//			pageRequest.setSort(new Sort(Sort.Direction.DESC, WmsInstructionDocResponseDTO.FIELD_CREATION_DATE));
			instructionDocResponsePage = PageHelper
					.doPageAndSort(pageRequest, () -> deliveryDocMapper.instructionDocPropertyBatchGet(instructionDocIdList));
		}
		List<WmsInstructionDocResponseDTO>  dtos = instructionDocResponsePage.stream()
				.sorted(Comparator.comparing(WmsInstructionDocResponseDTO::getCreationDate).reversed()).collect(Collectors.toList());
		instructionDocResponsePage.setContent(dtos);
		return instructionDocResponsePage;
	}

	@Override
	@ProcessLovValue
	public Page<WmsInstructionDTO> 	instructionQuery(Long tenantId, String instructionDocId, PageRequest pageRequest) {
		MtInstructionVO10 mtInstruction = new MtInstructionVO10();
		Page<WmsInstructionDTO> result = new Page<>();
//		mtInstruction.setSourceDocId(instructionDocId);
//		mtInstruction.setInstructionType(WmsConstant.InspectionType.RECEIVE_FROM_SUPPLIER);
		//根据指令单据ID获取对应的指令Id列表
//		Page<String> instructionDocIdPages = PageHelper
//				.doPage(pageRequest, () -> mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstruction));


//		List<String> instructionDocIds = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstruction);

		List<Object> queryList = new ArrayList<>();
		queryList.add("");
		queryList.add(null);
		Condition build = Condition.builder(MtInstruction.class).andWhere(Sqls.custom()
				.andEqualTo(MtInstruction.FIELD_TENANT_ID, tenantId)
				.andEqualTo(MtInstruction.FIELD_INSTRUCTION_TYPE, WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER)
				.andEqualTo(MtInstruction.FIELD_SOURCE_DOC_ID,instructionDocId)
//				.andIn(MtInstruction.FIELD_SOURCE_INSTRUCTION_ID, queryList, false)
		).build();
//		List<MtInstruction> mtInstructions1 = mtInstructionRepository.selectByCondition(build);
		Page<MtInstruction> instructions = PageHelper.doPage(pageRequest, () -> mtInstructionRepository.selectByCondition(build));

		List<WmsInstructionDTO> wmsInstructionDTOSPages = null;
		if(CollectionUtils.isNotEmpty(instructions.getContent())){
//			wmsInstructionDTOSPages = PageHelper.doPage(pageRequest, () -> deliveryDocMapper
//					.instructionPropertyBatchGet(mtInstructions1.stream().map(MtInstruction::getInstructionId).collect(Collectors.toList())));
			wmsInstructionDTOSPages = deliveryDocMapper
					.instructionPropertyBatchGet(instructions.getContent().stream().map(MtInstruction::getInstructionId).collect(Collectors.toList()));
		}else {
			wmsInstructionDTOSPages = new Page<>();
		}
		//根据指令Id列表获取对应的指令属性和指令实绩属性
//		List<WmsInstructionDTO> wmsInstructionDTOS = wmsInstructionDTOSPages.getContent();


		//循环查询扩展属性和指令实绩
		for (WmsInstructionDTO wmsInstructionDto :
				wmsInstructionDTOSPages) {

            //查询扩展属性 modify by yuchao.wang for kang.wang at 2020.8.31
            List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
					new MtExtendVO1("mt_instruction_attr", Collections.singletonList(wmsInstructionDto.getInstructionId()), "INSTRUCTION_LINE_NUM", "MATERIAL_VERSION", HmeConstants.ExtendAttr.UAI_FLAG, "EXCHANGE_QTY", "SO_NUM", "SO_LINE_NUM", "IQC_VERSION"));
			Map<String, String> extendAttrMap = new HashMap<>();
            mtExtendAttrVO1s.forEach(item -> extendAttrMap.put(item.getAttrName(), item.getAttrValue()));
            //查询行号
            wmsInstructionDto.setInstructionLineNum(extendAttrMap.get("INSTRUCTION_LINE_NUM"));
            //查询物料版本
            wmsInstructionDto.setMaterialVersion(extendAttrMap.get("MATERIAL_VERSION"));
            //特采标识
            wmsInstructionDto.setUaiFlag(extendAttrMap.get(HmeConstants.ExtendAttr.UAI_FLAG));
            //查询料废调换数量
			wmsInstructionDto.setExchangeQty(extendAttrMap.get("EXCHANGE_QTY"));
			//SoNum/SoLineNum
			wmsInstructionDto.setSoNum(extendAttrMap.get("SO_NUM"));
			wmsInstructionDto.setSoLineNum(extendAttrMap.get("SO_LINE_NUM"));
			//IQC版本
			wmsInstructionDto.setIqcVersion(extendAttrMap.get("IQC_VERSION"));

			//如果查询头下类型为TRANSFER_OVER_LOCATOR的单据状态为NEW则不作处理，如果不为NEW需要匹配另一个值集
			MtInstruction mtInstruction1 = new MtInstruction();
			mtInstruction1.setSourceDocId(instructionDocId);
			mtInstruction1.setInstructionType(WmsConstant.InstructionType.TRANSFER_OVER_LOCATOR);

			List<MtInstruction> mtInstructions = mtInstructionRepository.select(mtInstruction1);

			if(CollectionUtils.isNotEmpty(mtInstructions)) {
				MtInstruction mtInstruction2 = mtInstructions.stream().filter(item -> {
					List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId,
							new MtExtendVO("mt_instruction_attr", item.getInstructionId(), "INSTRUCTION_LINE_NUM"));
					return CollectionUtils.isNotEmpty(mtExtendAttrVOS) && mtExtendAttrVOS.get(0).getAttrValue().equals(wmsInstructionDto.getInstructionLineNum());
				}).findAny().orElse(null);

				if (mtInstruction2 != null) {
					String instructionStatus = mtInstruction2.getInstructionStatus();
					if (!WmsConstant.InstructionStatus.RELEASED.equals(instructionStatus)) {
						wmsInstructionDto.setInstructionStatus2(instructionStatus);
					}
				}
			}

			//查询采购订单号
			WmsPoDeliveryVO wmsPoDeliveryVO = new WmsPoDeliveryVO();
			wmsPoDeliveryVO.setDeliveryDocId(instructionDocId);
			wmsPoDeliveryVO.setDeliveryDocLineId(wmsInstructionDto.getInstructionId());
			List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WMS.PO.TYPE", tenantId);
			List<String> typeList = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
			if(CollectionUtils.isEmpty(typeList)){
				typeList.add("PO");
			}
			wmsPoDeliveryVO.setTypeList(typeList);
			List<String> wmsPoDeliveryVOS = wmsPoDeliveryRelMapper.selectPoNumbers(tenantId,wmsPoDeliveryVO);
			//处理数据，将行号合并
			String join = StringUtils.join(wmsPoDeliveryVOS,",");
			wmsInstructionDto.setPoLineNum(join);

			/**
			 * 修改已接收数量和已制单数量的取值逻辑 by han.zhang 2020-06-02
			 */
			//已料废调换数量
			AtomicReference<Double> exchangedQty = new AtomicReference<>(0D);
			//已接收数量
			AtomicReference<Double> actualQty = new AtomicReference<>(0D);
			//查询指令实绩表和实绩扩展表
			MtInstructionActual mtInstructionActual = new MtInstructionActual();
			mtInstructionActual.setInstructionId(wmsInstructionDto.getInstructionId());
			List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId,
					mtInstructionActual);
			if(org.apache.commons.collections.CollectionUtils.isNotEmpty(actualIdList)){
				actualIdList.forEach(id -> {
					//制单数量
					MtInstructionActual mtInstructionActual1 = mtInstructionActualRepository.instructionActualPropertyGet(tenantId, id);
					actualQty.updateAndGet(v -> v + mtInstructionActual1.getActualQty());
					//已接收数量
					MtExtendVO extendVO = new MtExtendVO();
					extendVO.setTableName("mt_instruction_actual_attr");
					extendVO.setKeyId(id);
					extendVO.setAttrName("EXCHANGED_QTY");
					List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
					if(org.apache.commons.collections.CollectionUtils.isNotEmpty(mtExtendAttrVOS)){
						mtExtendAttrVOS.forEach(item -> {
							if(!NumberHelper.isDouble(item.getAttrValue())){
								throw new MtException("指令实绩id为"+id +"的扩展表已接收数量格式错误");
							}
							Double v = Double.valueOf(item.getAttrValue());
							exchangedQty.updateAndGet(v1 -> v1 + v);
						});
					}
				});
			}
			wmsInstructionDto.setExchangedQty(String.valueOf(exchangedQty.get()));
			wmsInstructionDto.setCoverQty(actualQty.get());

			//查询已入库数量
			Condition anotherBuild = Condition.builder(MtInstruction.class).andWhere(Sqls.custom()
					.andEqualTo(MtInstruction.FIELD_TENANT_ID, tenantId)
					.andEqualTo(MtInstruction.FIELD_INSTRUCTION_TYPE, WmsConstant.InstructionType.TRANSFER_OVER_LOCATOR)
					.andEqualTo(MtInstruction.FIELD_SOURCE_DOC_ID,instructionDocId)
			).build();
			List<MtInstruction> anotherInstructions = mtInstructionRepository.selectByCondition(anotherBuild);
//			List<WmsInstructionDTO> anotherWmsInstructionDTOSPages = null;
//			if(CollectionUtils.isNotEmpty(anotherInstructions)){
//				anotherWmsInstructionDTOSPages = deliveryDocMapper
//						.instructionPropertyBatchGet(anotherInstructions.stream().filter(dto -> StringUtils.equals(dto.getInstructionType(), WmsConstant.InstructionType.TRANSFER_OVER_LOCATOR)).map(MtInstruction::getInstructionId).collect(Collectors.toList()));
//			}else {
//				anotherWmsInstructionDTOSPages = new Page<>();
//			}
			//已入库数量
			AtomicReference<Double> stockedQty = new AtomicReference<>(0D);
			for(MtInstruction dto : anotherInstructions) {
                //校验行号是否一致
				List<MtExtendAttrVO1> anotherMtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, new MtExtendVO1("mt_instruction_attr", Collections.singletonList(dto.getInstructionId()), "INSTRUCTION_LINE_NUM"));
				Map<String, String> anotherExtendAttrMap = new HashMap<>();
                anotherMtExtendAttrVO1s.forEach(item -> anotherExtendAttrMap.put(item.getAttrName(), item.getAttrValue()));
                if (!wmsInstructionDto.getInstructionLineNum().equals(anotherExtendAttrMap.get("INSTRUCTION_LINE_NUM"))) {
                    continue;
                }

                //查询指令实绩表和实绩扩展表
                MtInstructionActual anotherMtInstructionActual = new MtInstructionActual();
                anotherMtInstructionActual.setInstructionId(dto.getInstructionId());
                List<String> anotherActualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId,
						anotherMtInstructionActual);
				if(CollectionUtils.isNotEmpty(anotherActualIdList)){
					anotherActualIdList.forEach(id -> {
						//已入库数量
						MtInstructionActual anotherMtInstructionActual1 = mtInstructionActualRepository.instructionActualPropertyGet(tenantId, id);
						stockedQty.updateAndGet(v -> v + anotherMtInstructionActual1.getActualQty());
					});
				}
			}
			wmsInstructionDto.setStockedQty(stockedQty.get());
		}



		result.setTotalPages(instructions.getTotalPages());
		result.setTotalElements(instructions.getTotalElements());
		result.setNumberOfElements(instructions.getNumberOfElements());
		result.setSize(instructions.getSize());
		result.setNumber(instructions.getNumber());
		result.setContent(wmsInstructionDTOSPages);
		return result;
	}

	@Override
	public Page<WmsInstructionDetailResponseDTO> instructionDetailQuery(Long tenantId, WmsInstructionDetailRequestDTO dto, PageRequest pageRequest) {
		Page<WmsInstructionDetailResponseDTO> instructionDetailResponsePage = new Page<>();
		List<MtInstructionActualDetail> mtInstructionActualDetailList = new ArrayList<MtInstructionActualDetail>();
		List<String> materialLotIdListOne = new ArrayList<String>();
		List<String> materialLotIdListTwo = new ArrayList<String>();
		List<String> materialLotIdListThree = new ArrayList<String>();

		//获取输出参数materialLotId（物料批ID），并保存到ListOne
		mtInstructionActualDetailList = mtInstructionActualDetailRepository.instructionLimitActualDetailQuery(tenantId, dto.getInstructionId());
		if (mtInstructionActualDetailList != null) {
			for (MtInstructionActualDetail mtInstructionActualDetail : mtInstructionActualDetailList) {
				materialLotIdListOne.add(mtInstructionActualDetail.getMaterialLotId());
			}
		}
		//获取与送货单头、行关联的materialLotId（物料批ID），并保存到ListTwo
		materialLotIdListTwo = deliveryDocMapper.getMaterialLotIdByDelivery(dto);
		//根据页面筛选条件，并保存到ListThree
		Boolean flag = StringUtils.isEmpty(dto.getStatus()) && StringUtils.isEmpty(dto.getQualityStatus()) &&
				StringUtils.isEmpty(dto.getMaterialLotCode()) && StringUtils.isEmpty(dto.getCreationDateEnd()) &&
				StringUtils.isEmpty(dto.getCreationDateStart()) && StringUtils.isEmpty(dto.getInLocatorTimeStart())
				&& StringUtils.isEmpty(dto.getInLocatorTimeEnd());
		if (!flag) {
			materialLotIdListThree = deliveryDocMapper.getMaterialLotIdByCondition(dto);
		}

		//One和TwoIdList求交集
		materialLotIdListOne.removeAll(materialLotIdListTwo);
		materialLotIdListOne.addAll(materialLotIdListTwo);
		//再与Three求并集
		if (!flag) {
			materialLotIdListOne.retainAll(materialLotIdListThree);
		}
		//根据materialLotIdList查询指令明细
		if (materialLotIdListOne.size() > 0) {
			instructionDetailResponsePage = PageHelper
					.doPageAndSort(pageRequest, () -> deliveryDocMapper.instructionDetailQuery(materialLotIdListOne, dto.getInstructionId()));
		}
		for (WmsInstructionDetailResponseDTO instructionDetailResponseDTO : instructionDetailResponsePage) {
			if (instructionDetailResponseDTO.getCreatedBy() != null) {
				ResponseEntity<WmsUserInfoDTO> userInfo = hzeroIamFeignClient.getUserInfo(tenantId, instructionDetailResponseDTO.getCreatedBy());
				if (userInfo.getBody() != null) {
					instructionDetailResponseDTO.setCreatedByName(userInfo.getBody().getRealName());
				}
			}
			if (instructionDetailResponseDTO.getInstockBy() != null) {
				ResponseEntity<WmsUserInfoDTO> userInfo = hzeroIamFeignClient.getUserInfo(tenantId, instructionDetailResponseDTO.getInstockBy());
				if (userInfo.getBody() != null) {
					instructionDetailResponseDTO.setInstockByName(userInfo.getBody().getRealName());
				}
			}
		}

		return instructionDetailResponsePage;
	}

	@Override
	public List<WmsAvailQuantityReturnDTO> getAvailQuantity(Long tenantId, WmsAvailQuantityGetDTO wmsAvailQuantityGetDTO) {
		List<WmsAvailQuantityReturnDTO> returnDTOS = new ArrayList<>(wmsAvailQuantityGetDTO.getInstructionIdList().size());

		//单据类型
		List<String> typeList = new ArrayList<>();
		List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WMS.DELIVERY_TICKET_QUERY", tenantId);
		List<String> valueList = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(valueList)){
			typeList.add("DELIVERY_DOC");
		}else {
			typeList.addAll(valueList);
		}
		for (int i = 0; i < wmsAvailQuantityGetDTO.getInstructionIdList().size(); i++) {
			MtInstruction mtInstruction = new MtInstruction();
			mtInstruction.setInstructionId(wmsAvailQuantityGetDTO.getInstructionIdList().get(i));
			mtInstruction = mtInstructionRepository.selectByPrimaryKey(mtInstruction);
			if(Objects.isNull(mtInstruction)){
				throw new MtException("采购订单不存在");
			}

			//已制单数量
			Double executeQty = wmsPoDeliveryRelMapper.selectPoQuantityOfReceiveComplete("", mtInstruction.getInstructionId(), typeList);
			//接收完成数量
			Double completeQty = wmsPoDeliveryRelMapper.selectPoQuantityOfComplete("", mtInstruction.getInstructionId());
			//NG数量
			Double ngQty = wmsPoDeliveryRelMapper.selectNgQty(tenantId, mtInstruction.getInstructionId());
			//入库完成
			Double stockQty = wmsPoDeliveryRelMapper.selectPoQuantityOfStockInComplete("", mtInstruction.getInstructionId());

			WmsAvailQuantityReturnDTO innerDTO = new WmsAvailQuantityReturnDTO();
			innerDTO.setInstructionId(wmsAvailQuantityGetDTO.getInstructionIdList().get(i));
			//获取可制单数量 = 采购订单数量 – 已制单数量 - 已入库数量
			innerDTO.setAvailableOrderQuantity(BigDecimal.valueOf(mtInstruction.getQuantity()).subtract(BigDecimal.valueOf(executeQty)).subtract(BigDecimal.valueOf(completeQty)).subtract(BigDecimal.valueOf(stockQty)).add(BigDecimal.valueOf(ngQty)).setScale(2));
			returnDTOS.add(innerDTO);
		}

		return returnDTOS;
	}

	/**
	 * 判断所有属性是否为空
	 * @param object
	 * @return
	 */
	public static boolean checkObjAllFieldsIsNull(Object object) {
		if (null == object) {
			return true;
		}

		try {
			for (Field f : object.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				//跳过属性
				if("serialVersionUID".equals(f.getName())){
					continue;
				}

				if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
					return false;
				}

			}
		} catch (Exception e) {
            // e.printStackTrace();
            log.error("erorr!", e);
        }

		return true;
	}

	@Autowired
	private WmsDistributionListQueryMapper wmsDistributionListQueryMapper;
	@Autowired
	FileClient fileClient;
	@Autowired
	private MtUserClient mtUserClient;

	private String bucketName = "delivery-print-pdf-file";

	/**
	 * 条码打印s
	 *
	 * @author yifan.xiong@hand-china.com 2020-9-7 11:43:58
	 */
	@Override
	public void multiplePrint(Long tenantId, List<String> instructionDocIds, HttpServletResponse response){
		List<String> filePathList = new ArrayList();
		String outStream = "";
		//确定根目录
		String systemPath = System.getProperty("user.dir");
		String classUrl = this.getClass().getResource("/").getPath();
		log.info("<==== System path :: {}", systemPath);
		log.info("<==== class path :: {}", classUrl);
		String basePath = classUrl + "/templates";
		if (!new File(classUrl).exists()) {
			File file = new File(systemPath + "/templates");
			if (!file.exists()) {
				if(!file.mkdir()){
					throw new MtException("创建临时文件夹失败!");
				}
			}
			basePath = systemPath + "/templates";
		} else {
			basePath = classUrl + "/templates";
		}
		String dateTime = System.currentTimeMillis() + "";
		String docNumber = "";
		String uuid = UUID.randomUUID().toString();
		String barcodePath = "";
		String qrcodePath = "";
		String content = "";
		String pdfFileName = uuid + ".pdf";
		String pdfPath = basePath + "/" + pdfFileName;
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		List<File> barcodeImageFileList = new ArrayList<File>();
		List<File> qrcodeImageFileList = new ArrayList<File>();

		//定义每页可以打印的行上限
		Long lineCount = 10L;
		//循环需要打印的数据，单个打印
		for(String instructionDocId:instructionDocIds) {
			//获取头数据
			WmsDeliveryPrintVO headDto = wmsDistributionListQueryMapper.selectDeliveryPrintHead(tenantId, instructionDocId);
			//当前登录用户
			CustomUserDetails curUser = DetailsHelper.getUserDetails();
			String realName = curUser.getRealName();
			headDto.setCreatedBy(realName);

			//生成二维码
			String codeUuid = UUID.randomUUID().toString();
			qrcodePath = basePath + "/" + codeUuid + "_" + docNumber + "_qrcode.png";
			File qrcodeImageFile = new File(qrcodePath);
			barcodeImageFileList.add(qrcodeImageFile);
			content = headDto.getInstructionDocNum();
			try {
				CommonQRCodeUtil.encode(content, qrcodePath, qrcodePath, true);
				log.info("<====生成二维码完成！{}", qrcodePath);
			} catch (Exception e) {
				log.error("<==== WmsDeliveryDocServiceImpl.multiplePrint.encode Error", e);
				throw new MtException(e.getMessage());
			}

			//组装参数
			Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
			imgMap.put("barcodeImage", qrcodePath);
			Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);

			//获取行数据
			Boolean printflag = false;
			Long currentLine = 0L;
			Double sumQty = 0d;
			List<WmsDeliveryPrintVO1> lineList = wmsDistributionListQueryMapper.selectDeliveryPrintLine(tenantId, instructionDocId);

			if (CollectionUtils.isEmpty(lineList)) {
				continue;
			}
			//按照除原单号外的字段汇总，原单号有多个，显示在一行中 modify by yuchao.wang for shangwen.yan at 2020.10.19
			Map<WmsDeliveryPrintVO1, List<WmsDeliveryPrintVO1>> printVoMap = lineList.stream().collect(Collectors.groupingBy(WmsDeliveryPrintVO1::createSummary));
			LinkedHashMap<WmsDeliveryPrintVO1, List<WmsDeliveryPrintVO1>> printVoLinkedMap = printVoMap.entrySet().stream()
					.sorted(Comparator.comparing(item -> StringUtils.trimToEmpty(item.getKey().getLineNum())))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
							(oldValue, newValue) -> oldValue, LinkedHashMap::new));
			for (Map.Entry<WmsDeliveryPrintVO1, List<WmsDeliveryPrintVO1>> entry : printVoLinkedMap.entrySet()) {
				WmsDeliveryPrintVO1 lineDto = entry.getKey();

				//原单号有多个，显示在一行中 modify by yuchao.wang for shangwen.yan at 2020.10.19
				List<WmsDeliveryPrintVO1> values = entry.getValue();
				if (CollectionUtils.isNotEmpty(values) && values.size() > 1) {
					StringBuilder stringBuilder = new StringBuilder();
					values.forEach(item -> {
						stringBuilder.append(item.getPoNum()).append(',');
					});
					formMap.put("PoNum"+currentLine.toString(), stringBuilder.substring(0, stringBuilder.length()-1));
				} else {
					formMap.put("PoNum"+currentLine.toString(), values.get(0).getPoNum());
				}
				formMap.put("LineNum"+currentLine.toString(), lineDto.getLineNum());
				formMap.put("ItemCode"+currentLine.toString(), lineDto.getMaterialCode());
				formMap.put("ItemDesc"+currentLine.toString(), lineDto.getMaterialName());
				formMap.put("Qty"+currentLine.toString(), lineDto.getQuantity());
				formMap.put("Version"+currentLine.toString(), lineDto.getMaterialVersion());
				formMap.put("Uom"+currentLine.toString(), lineDto.getUomCode());
				formMap.put("Warehouse"+currentLine.toString(), lineDto.getLocatorCode());
				//formMap.put("OldItemCode"+currentLine.toString(), lineDto.getOldItemCode());
				formMap.put("remark"+currentLine.toString(), lineDto.getRemark());

				sumQty += lineDto.getQuantity();
				currentLine +=1L;
				printflag = false;
				if(currentLine%lineCount == 0){
					formMap.put("instructionDocNum", headDto.getInstructionDocNum());
					formMap.put("supplierName", headDto.getSupplierName());
					formMap.put("siteName", headDto.getCustomerName());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					formMap.put("date", sdf.format(new Date()));
					formMap.put("remark", headDto.getRemark());
					formMap.put("createdBy", headDto.getCreatedBy());

					formMap.put("sumQty",sumQty);
					Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
					param.put("formMap", formMap);
					param.put("imgMap", imgMap);
					dataList.add(param);
					currentLine = 0L;
					sumQty = 0D;
					formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
					printflag = true;
				}
			}
			if(!printflag) {
				formMap.put("instructionDocNum", headDto.getInstructionDocNum());
				formMap.put("supplierName", headDto.getSupplierName());
				formMap.put("siteName", headDto.getCustomerName());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				formMap.put("date", sdf.format(new Date()));
				formMap.put("remark", headDto.getRemark());
				formMap.put("createdBy", headDto.getCreatedBy());

				formMap.put("sumQty", sumQty);
				Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
				param.put("formMap", formMap);
				param.put("imgMap", imgMap);
				dataList.add(param);
			}
		}
		if(dataList.size() > 0) {
			//生成PDF
			try {
				log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, dataList.size());
				CommonPdfTemplateUtil.multiplePage(basePath + "/wms_delivery_print_template.pdf", pdfPath, dataList);
				log.info("<==== 生成PDF完成！{}", pdfPath);
			} catch (Exception e) {
				log.error("<==== WmsDeliveryDocServiceImpl.multiplePrint.generatePDFFile Error", e);
				throw new MtException(e.getMessage());
			}
		}

		//将文件转化成流进行输出
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		File pdfFile = new File(pdfPath);
		try{
			//设置相应参数
			response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
			response.setHeader("Content-Disposition","attachment;filename=" + uuid + ".pdf");
			String encoding = new GetFileCharset().guestFileEncoding(pdfFile);
			if(org.apache.commons.lang.StringUtils.isNotEmpty(encoding)){
				response.setCharacterEncoding(encoding);
			}

			//将文件转化成流进行输出
			bis = new BufferedInputStream(new FileInputStream(pdfPath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			log.error("<==== HmeDistributionListQueryServiceImpl.multiplePrint.outputPDFFile Error", e);
			throw new MtException("Exception", e.getMessage());
		} finally {
			try {
				if (bis != null){
					bis.close();
				}
				if (bos != null){
					bos.close();
				}
			} catch (IOException e) {
				log.error("<==== HmeDistributionListQueryServiceImpl.multiplePrint.closeIO Error", e);
			}
		}

		//将文件转化成流进行输出
		//String outPutStream = WmsCommonUtils.getStringByFile(pdfPath);
		/*String d = DateUtil.date2String(new Date(), "yyyyMMdd");
		String filePath = fileClient.uploadFile(tenantId, bucketName, "pages" + "/" + d,
				pdfFileName, "application/pdf", FileUtil.File2byte(pdfFile));
		filePath = fileClient.getSignedUrl(tenantId,bucketName,null,filePath);
		filePathList.add(filePath);*/

		//删除临时文件
		for (File file:barcodeImageFileList
				) {
			if(!file.delete()){
				log.info("<==== WmsDeliveryDocServiceImpl.multiplePrint.barcodeImageFileList Failed: {}", barcodePath);
			}
		}
		if(!pdfFile.delete()){
			log.info("<==== WmsDeliveryDocServiceImpl.multiplePrint.pdfFile Failed: {}", barcodePath);
		}

	}

	@Override
	@ProcessLovValue
	public Page<WmsDeliveryDocVO> detailQuery(Long tenantId, String instructionId, PageRequest pageRequest) {
		Page<WmsDeliveryDocVO> resultPage = PageHelper.doPageAndSort(pageRequest, () -> wmsPoDeliveryRelMapper.detailQuery(tenantId, instructionId));
		for (WmsDeliveryDocVO wmsDeliveryDocVO:resultPage) {
			MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, Long.parseLong(wmsDeliveryDocVO.getCreatedBy()));
			wmsDeliveryDocVO.setCreatedByName(mtUserInfo.getRealName());
		}
		return resultPage;
	}

	@Override
	@ProcessLovValue
	public Page<WmsMaterialLotLineVO> instructionMaterialLotQuery(Long tenantId, String docLineId, PageRequest pageRequest) {
		return PageHelper.doPageAndSort(pageRequest, () -> wmsPoDeliveryRelMapper.instructionMaterialLotQuery(tenantId, docLineId));
	}
}