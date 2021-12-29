package com.ruike.wms.app.service.impl;

import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;
import com.ruike.hme.domain.repository.HmeObjectRecordLockRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.api.dto.ItfLightTaskIfaceDTO;
import com.ruike.itf.app.service.ItfLightTaskIfaceService;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.domain.entity.ItfLightTaskIface;
import com.ruike.itf.domain.vo.ItfLightTaskIfaceVO;
import com.ruike.itf.infra.mapper.ItfLightTaskIfaceMapper;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsMaterialOnShelfService;
import com.ruike.wms.domain.entity.WmsPfepInertiaLocator;
import com.ruike.wms.domain.repository.*;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsMaterialOnShelfMapper;
import com.ruike.wms.infra.mapper.WmsPfepInertiaLocatorMapper;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtCommonExtendVO5;
import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.MtInstructionActualVO2;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDetail;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDetailRepository;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionVO10;
import tarzan.instruction.domain.vo.MtInstructionVO3;
import tarzan.instruction.infra.mapper.MtInstructionMapper;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContainerVO7;
import tarzan.inventory.domain.vo.MtMaterialLotVO14;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtPfepInventory;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtPfepInventoryRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.material.infra.mapper.MtPfepInventoryMapper;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO2;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO3;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.NO;

/**
 * 物料上架功能 应用服务实现
 * 迭代日期：2020-08-15
 * 迭代内容：新增入库单类型业务逻辑处理
 *
 * @author jiangling.zheng@hand-china.com 2020-06-09 14:57
 */
@Service
public class WmsMaterialOnShelfServiceImpl implements WmsMaterialOnShelfService {

    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;

    @Autowired
    private MtInstructionActualDetailRepository mtInstructionActualDetailRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private WmsInvTransferIssueRepository wmsInvTransferIssueRepository;

    @Autowired
    private WmsMaterialOnShelfRepository wmsMaterialOnShelfRepository;

    @Autowired
    private WmsMaterialOnShelfMapper wmsMaterialOnShelfMapper;

    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;

    @Autowired
    private MtInstructionDetailRepository mtInstructionDetailRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;

    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;

    @Autowired
    private MtInstructionRepository mtLogisticInstructionService;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;

    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;

    @Autowired
    private MtPfepInventoryMapper mtPfepInventoryMapper;

    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;

    @Autowired
    private WmsPfepInertiaLocatorRepository wmsPfepInertiaLocatorRepository;

    @Autowired
    private WmsPfepInertiaLocatorMapper wmsPfepInertiaLocatorMapper;

    @Autowired
    private WmsMaterialLotRepository materialLotRepository;

    @Autowired
    private WmsDocPrivilegeRepository wmsDocPrivilegeRepository;

    @Autowired
    private MtPfepInventoryRepository mtPfepInventoryRepository;

    @Autowired
    private MtInstructionMapper mtInstructionMapper;

    @Autowired
    private ItfLightTaskIfaceService itfLightTaskIfaceService;

    @Autowired
    private ItfLightTaskIfaceMapper itfLightTaskIfaceMapper;

    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;

    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;


    @Override
    @ProcessLovValue(targetField = {"", "orderLineList", ""})
    public WmsMaterialOnShelfDocDTO queryInstructionDocByNum(Long tenantId, String instructionDocNum) {
        if (StringUtils.isEmpty(instructionDocNum)) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0003", WmsConstant.ConstantValue.WMS, "instructionDocNum"));
        }
        MtInstructionDoc doc = mtInstructionDocRepository.selectOne(new MtInstructionDoc() {{
            setInstructionDocNum(instructionDocNum);
            setTenantId(tenantId);
        }});
        if (ObjectUtils.isEmpty(doc)) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0001", WmsConstant.ConstantValue.WMS, instructionDocNum));
        }
        // 校验用户是否有入库单对应的工厂权限
        mtUserOrganizationRepository.userOrganizationPermissionValidate(tenantId, new MtUserOrganization() {{
            setUserId(DetailsHelper.getUserDetails().getUserId());
            setOrganizationType("SITE");
            setOrganizationId(doc.getSiteId());
        }});
        // 单据类型校验
        if (!StringUtils.equalsAny(doc.getInstructionDocType(),
                WmsConstant.DocType.DELIVERY_DOC, WmsConstant.DocType.PRODUCT_RECEIPT)) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0002", WmsConstant.ConstantValue.WMS, instructionDocNum));
        }
        // 入库单校验状态
        if (WmsConstant.DocType.PRODUCT_RECEIPT.equals(doc.getInstructionDocType()) && !StringUtils.equalsAny(doc.getInstructionDocStatus(),
                WmsConstant.DocStatus.RELEASED, WmsConstant.DocStatus.COMPLETED_CANCEL)) {
            String instructionStatusDesc = lovAdapter.queryLovMeaning("WMS.RECEIPT_DOC_STATUS", tenantId, doc.getInstructionDocStatus());
            throw new MtException("WMS_STOCKTAKE_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_STOCKTAKE_001", "WMS", "", instructionDocNum, instructionStatusDesc));
        }
        MtGenType type = mtGenTypeRepository.getGenType(tenantId, "INSTRUCTION", "INSTRUCTION_DOC_TYPE", doc.getInstructionDocType());
        List<String> instructionIds = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, new MtInstructionVO10() {{
            setSourceDocId(doc.getInstructionDocId());
            setTenantId(tenantId);
        }});
        List<MtInstruction> instructionList = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIds);
        if (WmsConstant.DocType.DELIVERY_DOC.equals(doc.getInstructionDocType())) {
            // 送货单行（指令类型仅为RECEIVE_FROM_SUPPLIER）
            instructionList.removeIf(item -> !WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER.equals(item.getInstructionType()));
        } else {
            // 入库单行（指令状态仅为 RELEASED，COMPLETED_CANCEL）
            instructionList.removeIf(item -> !StringUtils.equalsAny(doc.getInstructionDocStatus(),
                    WmsConstant.InstructionStatus.RELEASED, WmsConstant.InstructionStatus.COMPLETED_CANCEL));
        }
        List<WmsMaterialOnShelfDocLineDTO> orderLineList = new ArrayList<>(instructionBatchGet(tenantId, instructionList, doc));

        if(WmsConstant.DocType.PRODUCT_RECEIPT.equals(doc.getInstructionDocType())){
            Long userId = DetailsHelper.getUserDetails().getUserId();
            wmsDocPrivilegeRepository.isWarehousePrivileged(tenantId,WmsWarehousePrivilegeQueryDTO.builder()
                    .userId(userId)
                    .locatorId(orderLineList.get(0).getToLocatorId())
                    .docType(WmsConstant.DocType.PRODUCT_RECEIPT)
                    .operationType(WmsConstant.OperationType.EXECUTE).build());
        }
        //获取推荐货位
        for(WmsMaterialOnShelfDocLineDTO wmsMaterialOnShelfDocLineDTO:orderLineList){
            String locatorRecomMode = mtInstructionMapper.getMode(tenantId,
                    doc.getSiteId(),
                    wmsMaterialOnShelfDocLineDTO.getMaterialId(),
                    wmsMaterialOnShelfDocLineDTO.getToLocatorId());
            if(StringUtils.isBlank(locatorRecomMode)){
                wmsMaterialOnShelfDocLineDTO.setRecommendLocatorCode("");
            }else if(locatorRecomMode.equals("POSITION")){
                MtPfepInventoryVO mtPfepInventoryVO = new MtPfepInventoryVO();
                mtPfepInventoryVO.setMaterialId(wmsMaterialOnShelfDocLineDTO.getMaterialId());
                mtPfepInventoryVO.setSiteId(doc.getSiteId());
                mtPfepInventoryVO.setOrganizationType("LOCATOR");
                mtPfepInventoryVO.setOrganizationId(wmsMaterialOnShelfDocLineDTO.getToLocatorId());
                MtPfepInventory mtPfepInventory = mtPfepInventoryRepository.pfepInventoryGet(tenantId,mtPfepInventoryVO);
                if(StringUtils.isBlank(mtPfepInventory.getStockLocatorId())){
                    wmsMaterialOnShelfDocLineDTO.setRecommendLocatorCode("");
                }else{
                    wmsMaterialOnShelfDocLineDTO.setRecommendLocatorCode(mtModLocatorRepository.selectByPrimaryKey(mtPfepInventory.getStockLocatorId()).getLocatorCode());
                }
            }else if(locatorRecomMode.equals("INERTIA")){
                WmsPfepInertiaLocator wmsPfepInertiaLocator = new WmsPfepInertiaLocator();
                wmsPfepInertiaLocator.setTenantId(tenantId);
                wmsPfepInertiaLocator.setMaterialId(wmsMaterialOnShelfDocLineDTO.getMaterialId());
                wmsPfepInertiaLocator.setSiteId(doc.getSiteId());
                wmsPfepInertiaLocator.setWarehouseId(wmsMaterialOnShelfDocLineDTO.getToLocatorId());
                List<WmsPfepInertiaLocator> wmsPfepInertiaLocatorList = wmsPfepInertiaLocatorRepository.select(wmsPfepInertiaLocator);
                if(wmsPfepInertiaLocatorList.size() !=0){
                    wmsMaterialOnShelfDocLineDTO.setRecommendLocatorCode(mtModLocatorRepository.selectByPrimaryKey(wmsPfepInertiaLocatorList.get(0).getLocatorId()).getLocatorCode());
                }else{
                    wmsMaterialOnShelfDocLineDTO.setRecommendLocatorCode("");
                }
            }
        }
        return new WmsMaterialOnShelfDocDTO() {{
            setInstructionDocId(doc.getInstructionDocId());
            setInstructionDocNum(doc.getInstructionDocNum());
            String docStatus = WmsConstant.DocType.DELIVERY_DOC.equals(doc.getInstructionDocType()) ? "WMS.DELIVERY_DOC.STATUS" : "WMS.RECEIPT_DOC_STATUS";
            String docStatusMeaning = lovAdapter.queryLovMeaning(docStatus, tenantId, doc.getInstructionDocStatus());
            setInstructionDocStatus(doc.getInstructionDocStatus());
            setInstructionDocStatusMeaning(docStatusMeaning);
            setInstructionDocType(doc.getInstructionDocType());
            setInstructionDocTypeDesc(type.getDescription());
            setInspectionDate(null);
            setRemark(doc.getRemark());
            setOrderLineList(orderLineList);
        }};
    }

    @Override
    @ProcessLovValue(targetField = {"", "orderDto", "barCodeDtoList", "barCodeDtoList.orderLineDto"})
    public WmsMaterialOnShelfDTO queryBarcode(Long tenantId, WmsMaterialOnShelfBarCodeDTO2 dto) {
        if (StringUtils.isEmpty(dto.getBarCode())) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0003", WmsConstant.ConstantValue.WMS, "barCode"));
        }
        if(CollectionUtils.isNotEmpty(dto.getOrderLineList())){
            List<ItfLightTaskIfaceDTO> itfLightTaskIfaceDTOS = new ArrayList<>();
            for(WmsMaterialOnShelfDocLineDTO2 wmsMaterialOnShelfDocLineDTO2:dto.getOrderLineList()){
                if (StringUtils.isNotBlank(wmsMaterialOnShelfDocLineDTO2.getTaskNum()) && !"false".equals(wmsMaterialOnShelfDocLineDTO2.getTaskNum())) {
                    ItfLightTaskIfaceDTO itfLightTaskIfaceDTO = new ItfLightTaskIfaceDTO();
                    itfLightTaskIfaceDTO.setTaskStatus("OFF");
                    itfLightTaskIfaceDTO.setTaskNum(wmsMaterialOnShelfDocLineDTO2.getTaskNum());
                    itfLightTaskIfaceDTOS.add(itfLightTaskIfaceDTO);
                }
            }
            if(CollectionUtils.isNotEmpty(itfLightTaskIfaceDTOS)){
                itfLightTaskIfaceService.itfLightTaskIface(tenantId,itfLightTaskIfaceDTOS);
            }
        }
        WmsMaterialOnShelfDTO returnDto = new WmsMaterialOnShelfDTO();
        String instructionDocType = null;
        // 获取扫描的条码是物料批、容器还是不存在
        WmsCostCtrMaterialDTO6 materialLotDto = wmsInvTransferIssueRepository.materialLotInfoQuery(tenantId, dto.getBarCode());
        List<String> materialLotIds = materialLotDto.getMaterialLotIds();
        String codeType = materialLotDto.getCodeType();
        returnDto.setContainerId(materialLotDto.getContainerId());
        if (CollectionUtils.isEmpty(materialLotIds)) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0004", WmsConstant.ConstantValue.WMS, dto.getBarCode()));
        }
        // 获取条码信息
        List<WmsMaterialOnShelfBarCodeDTO> barCodeDtoList = wmsMaterialOnShelfRepository
                .selectMaterialLotCondition(tenantId, materialLotIds);
        List<WmsMaterialOnShelfDocLineDTO2> orderLineList = dto.getOrderLineList();
        List<WmsMaterialOnShelfBarCodeDTO> returnDtoList = new ArrayList<>();
        // 校验用户是否有入库单对应的工厂权限
        mtUserOrganizationRepository.userOrganizationPermissionValidate(tenantId, new MtUserOrganization() {{
            setUserId(DetailsHelper.getUserDetails().getUserId());
            setOrganizationType("SITE");
            setOrganizationId(barCodeDtoList.get(0).getSiteId());
        }});

        // 入库单数据
        List<WmsMaterialOnShelfDocLineDTO> inDocList = wmsMaterialOnShelfMapper.selectInDocCondition(tenantId, materialLotIds);
        List<LovValueDTO> insStatusList = lovAdapter.queryLovValue("WMS.RECEIPT_LINE_STATUS", tenantId);
        List<String> instructionIds = inDocList.stream().map(WmsMaterialOnShelfDocLineDTO::getInstructionId).collect(Collectors.toList());
        // 送货单数据
        List<WmsMaterialOnShelfDocLineDTO> deliveryDocList = wmsMaterialOnShelfMapper.selectDocCondition(tenantId, materialLotIds);
        List<LovValueDTO> deliveryStatusList = lovAdapter.queryLovValue("WMS.DELIVERY_DOC_LINE.STATUS", tenantId);
        instructionIds.addAll(deliveryDocList.stream().map(WmsMaterialOnShelfDocLineDTO::getInstructionId).collect(Collectors.toList()));
        List<String> insIds = instructionIds.stream().distinct().collect(Collectors.toList());
        List<WmsMaterialOnShelfInsActualDTO> actualDtoList = new ArrayList<>();
        List<WmsMaterialOnShelfMatLotDTO> matLotList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(insIds)) {
            actualDtoList = wmsMaterialOnShelfMapper.executeQtyQuery(tenantId, insIds);
            matLotList = wmsMaterialOnShelfMapper.materialLotQuery(tenantId, insIds);
        }

        List<ItfLightTaskIfaceDTO> itfLightTaskIfaceDTOS = new ArrayList<>();
        for (WmsMaterialOnShelfBarCodeDTO barDto : barCodeDtoList) {

            // 有效性校验
            if (!WmsConstant.ConstantValue.YES.equals(barDto.getEnableFlag()) && WmsConstant.MaterialLotType.CONTAINER.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0007", WmsConstant.ConstantValue.WMS, dto.getBarCode(), barDto.getMaterialLotCode()));
            } else if (!WmsConstant.CONSTANT_Y.equals(barDto.getEnableFlag()) && WmsConstant.MaterialLotType.MATERIAL_LOT.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0009", WmsConstant.ConstantValue.WMS, barDto.getMaterialLotCode()));
            }
            // 是否冻结校验
            if (WmsConstant.CONSTANT_Y.equals(barDto.getFreezeFlag()) && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0024", WmsConstant.ConstantValue.WMS, dto.getBarCode(), barDto.getMaterialLotCode()));
            } else if (WmsConstant.CONSTANT_Y.equals(barDto.getFreezeFlag()) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0025", WmsConstant.ConstantValue.WMS, barDto.getMaterialLotCode()));
            }
            //条码盘点停用标识校验
            if (WmsConstant.CONSTANT_Y.equals(barDto.getStocktakeFlag()) && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0033", WmsConstant.ConstantValue.WMS, dto.getBarCode(), barDto.getMaterialLotCode()));
            } else if (WmsConstant.CONSTANT_Y.equals(barDto.getStocktakeFlag()) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0034", WmsConstant.ConstantValue.WMS, barDto.getMaterialLotCode()));
            }
            // 校验是否为在制品
            if (WmsConstant.CONSTANT_Y.equals(barDto.getMfFlag()) && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                throw new MtException("WMS_INV_TRANSFER_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0036", WmsConstant.ConstantValue.WMS, dto.getBarCode(), barDto.getMaterialLotCode()));
            } else if (WmsConstant.CONSTANT_Y.equals(barDto.getMfFlag()) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                throw new MtException("WMS_INV_TRANSFER_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0035", WmsConstant.ConstantValue.WMS, barDto.getMaterialLotCode()));
            }
            // 匹配对应单据行
            List<WmsMaterialOnShelfDocLineDTO> orderLines = new ArrayList<>();
            instructionDocType = WmsConstant.DocType.PRODUCT_RECEIPT;
            // 不启用单据扫描
            if (!WmsConstant.ConstantValue.YES.equals(dto.getEnableDocFlag())) {
                // 先匹配入库单，未匹配到再匹配送货单
                orderLines.addAll(inDocList.stream().filter(t -> StringUtils.equals(t.getMaterialLotId(),
                        barDto.getMaterialLotId())).collect(Collectors.toList()));
                if (orderLines.size() == 0) {
                    instructionDocType = WmsConstant.DocType.DELIVERY_DOC;
                    orderLines.addAll(deliveryDocList.stream().filter(t -> StringUtils.equals(t.getMaterialLotId(),
                            barDto.getMaterialLotId())).collect(Collectors.toList()));
                }
                if(WmsConstant.DocType.PRODUCT_RECEIPT.equals(instructionDocType)){
                    Long userId = DetailsHelper.getUserDetails().getUserId();
                    wmsDocPrivilegeRepository.isWarehousePrivileged(tenantId,WmsWarehousePrivilegeQueryDTO.builder()
                            .userId(userId)
                            .locatorId(orderLines.get(0).getToLocatorId())
                            .docType(WmsConstant.DocType.PRODUCT_RECEIPT)
                            .operationType(WmsConstant.OperationType.EXECUTE).build());
                }
            } else {
                instructionDocType = dto.getInstructionDocType();
                orderLines.addAll(StringUtils.equals(WmsConstant.DocType.PRODUCT_RECEIPT, instructionDocType) ?
                        inDocList.stream().filter(t -> StringUtils.equals(t.getMaterialLotId(),
                                barDto.getMaterialLotId())).collect(Collectors.toList()) :
                        deliveryDocList.stream().filter(t -> StringUtils.equals(t.getMaterialLotId(),
                                barDto.getMaterialLotId())).collect(Collectors.toList()));
            }
            // (入库单)质量状态校验
            if (StringUtils.equals(WmsConstant.DocType.PRODUCT_RECEIPT, instructionDocType) && !WmsConstant.ConstantValue.OK.equals(barDto.getQualityStatus()) &&
                    HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0008", WmsConstant.ConstantValue.WMS, dto.getBarCode(), barDto.getMaterialLotCode()));
            } else if (WmsConstant.DocType.PRODUCT_RECEIPT.equals(instructionDocType) && !WmsConstant.ConstantValue.OK.equals(barDto.getQualityStatus()) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0010", WmsConstant.ConstantValue.WMS, barDto.getMaterialLotCode()));
            }
            // 待入库状态校验
            if (StringUtils.equals(WmsConstant.DocType.DELIVERY_DOC, instructionDocType)) {
                if (!StringUtils.equals(WmsConstant.MaterialLotStatus.TO_ACCEPT, barDto.getMaterialLotStatus()) && WmsConstant.MaterialLotType.CONTAINER.equals(codeType)) {
                    throw new MtException("WMS_MATERIAL_ON_SHELF_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_MATERIAL_ON_SHELF_0005", WmsConstant.ConstantValue.WMS, dto.getBarCode(), barDto.getMaterialLotCode()));
                } else if (!StringUtils.equals(WmsConstant.MaterialLotStatus.TO_ACCEPT, barDto.getMaterialLotStatus()) && WmsConstant.MaterialLotType.MATERIAL_LOT.equals(codeType)) {
                    throw new MtException("WMS_MATERIAL_ON_SHELF_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_MATERIAL_ON_SHELF_0006", WmsConstant.ConstantValue.WMS, barDto.getMaterialLotCode()));
                }
            }
            // 校验单据行唯一
            if (orderLines.size() == 0) {
                throw new MtException("WMS_MATERIAL_ON_SHELF_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_MATERIAL_ON_SHELF_0007", WmsConstant.ConstantValue.WMS, barDto.getMaterialLotCode()));
            } else if (orderLines.size() > 1) {
                throw new MtException("WMS_MATERIAL_ON_SHELF_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_MATERIAL_ON_SHELF_0008", WmsConstant.ConstantValue.WMS, dto.getBarCode()));
            }
            WmsMaterialOnShelfDocLineDTO orderLine = orderLines.get(0);
            // 校验匹配单据是否是扫描单据
            if (WmsConstant.ConstantValue.YES.equals(dto.getEnableDocFlag())) {
                if (!StringUtils.equals(dto.getInstructionDocNum(), orderLine.getInstructionDocNum())) {
                    throw new MtException("WMS_MATERIAL_ON_SHELF_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_MATERIAL_ON_SHELF_0011", WmsConstant.ConstantValue.WMS));
                }
            } else {
                // 当前单据类型是否与上次条码对应单据类型一致
                if (StringUtils.isNotEmpty(dto.getInstructionDocType()) && !StringUtils.equals(dto.getInstructionDocType(), instructionDocType)) {
                    throw new MtException("WMS_MATERIAL_ON_SHELF_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_MATERIAL_ON_SHELF_0018", WmsConstant.ConstantValue.WMS));
                }
            }
            if (StringUtils.equals(WmsConstant.DocType.PRODUCT_RECEIPT, instructionDocType)) {
                // 条码数量校验
                if (barDto.getPrimaryUomQty().compareTo(BigDecimal.ZERO) == 0 && WmsConstant.MaterialLotType.CONTAINER.equals(codeType)) {
                    throw new MtException("WMS_PUT_IN_STOCK_018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_PUT_IN_STOCK_018", WmsConstant.ConstantValue.WMS, dto.getBarCode(), barDto.getMaterialLotCode()));
                } else if (barDto.getPrimaryUomQty().compareTo(BigDecimal.ZERO) == 0 && WmsConstant.MaterialLotType.MATERIAL_LOT.equals(codeType)) {
                    throw new MtException("WMS_PUT_IN_STOCK_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_PUT_IN_STOCK_013", WmsConstant.ConstantValue.WMS, barDto.getMaterialLotCode()));
                }
                BigDecimal scannedMaterialQty = BigDecimal.ZERO;
                BigDecimal scannedContainerQty = BigDecimal.ZERO;
                BigDecimal putStorageQty = BigDecimal.ZERO;
                // 前台缓存
                if (CollectionUtils.isNotEmpty(orderLineList)) {
                    // 当前条码对应单据缓存扫描数量
                    List<WmsMaterialOnShelfDocLineDTO2> lineList = orderLineList.stream().filter(m -> StringUtils.equals(orderLine.getInstructionId(),
                            m.getInstructionId())).collect(Collectors.toList());
                    // 扫描条码数量
                    scannedMaterialQty = CollectionUtils.isNotEmpty(lineList) ? lineList.get(0).getPrimaryUomQty() : BigDecimal.ZERO;
                    // 已入库数量
                    putStorageQty = CollectionUtils.isNotEmpty(lineList) ? lineList.get(0).getBarCodeQty() : BigDecimal.ZERO;
                }
                // 容器条码累计
                if (CollectionUtils.isNotEmpty(returnDtoList)) {
                    // 当前条码为容器条码则当前容器内物料批累计已扫描总数
                    List<WmsMaterialOnShelfBarCodeDTO> scanLineList = returnDtoList.stream().filter(m -> StringUtils.equals(orderLine.getInstructionId(),
                            m.getOrderLineDto().getInstructionId())).collect(Collectors.toList());
                    scannedContainerQty = CollectionUtils.isNotEmpty(scanLineList) ? scanLineList.stream().
                            collect(CollectorsUtil.summingBigDecimal(m -> Optional.ofNullable(m.getPrimaryUomQty())
                                    .orElse(BigDecimal.ZERO))) : BigDecimal.ZERO;
                }
                // 已扫描条码累计数量（前代缓存数+当前容器内累计数） + 已入库数量 + 当前条码数量 < 单据行数量
                BigDecimal totalQty = scannedMaterialQty.add(scannedContainerQty).add(putStorageQty).add(barDto.getPrimaryUomQty());
                if (totalQty.compareTo(orderLine.getQuantity()) > 0) {
                    throw new MtException("WMS_PUT_IN_STOCK_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_PUT_IN_STOCK_012", WmsConstant.ConstantValue.WMS, orderLine.getInstructionDocNum()));
                }
            }
            // 不启用单据扫描
            if (!WmsConstant.ConstantValue.YES.equals(dto.getEnableDocFlag())) {
                BigDecimal executeQty = BigDecimal.ZERO;
                BigDecimal sumActualQty = BigDecimal.ZERO;
                if (CollectionUtils.isNotEmpty(actualDtoList)) {
                    List<WmsMaterialOnShelfInsActualDTO> actualDtos = actualDtoList.stream().filter(t ->
                            StringUtils.equals(t.getInstructionId(), orderLine.getInstructionId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(actualDtos)) {
                        WmsMaterialOnShelfInsActualDTO actualDto = actualDtos.get(0);
                        executeQty = actualDto.getExecuteQty() != null ? actualDto.getExecuteQty() : BigDecimal.ZERO;
                        sumActualQty = actualDto.getActualQty() != null ? actualDto.getActualQty() : BigDecimal.ZERO;
                    }
                }
                BigDecimal barCodeQty = BigDecimal.ZERO;
                List<WmsMaterialOnShelfMatLotDTO> materialLotList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(matLotList)) {
                    materialLotList = matLotList.stream().filter(t ->
                            StringUtils.equals(t.getInstructionId(), orderLine.getInstructionId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(materialLotList)) {
                        String finalInstructionDocType = instructionDocType;
                        materialLotList.removeIf(item -> StringUtils.equals(finalInstructionDocType, WmsConstant.DocType.DELIVERY_DOC) &&
                                !WmsConstant.MaterialLotStatus.MINSTOCK.equals(Optional.ofNullable(item.getStatus()).orElse("")));
                    }
                    if (CollectionUtils.isNotEmpty(materialLotList)) {
                        barCodeQty = materialLotList.stream().map(WmsMaterialOnShelfMatLotDTO::getPrimaryUomQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                    }
                }
                BigDecimal finalExecuteQty = StringUtils.equals(instructionDocType, WmsConstant.DocType.DELIVERY_DOC) ? executeQty : orderLine.getQuantity();
                BigDecimal finalBarCodeQty = StringUtils.equals(instructionDocType, WmsConstant.DocType.DELIVERY_DOC) ? barCodeQty : sumActualQty;
                String insStatusMeaning = WmsConstant.DocType.DELIVERY_DOC.equals(instructionDocType) ?
                        deliveryStatusList.stream().filter(t -> StringUtils.equals(t.getValue(), orderLine.getInstructionStatus())).collect(Collectors.toList()).get(0).getMeaning() :
                        insStatusList.stream().filter(t -> StringUtils.equals(t.getValue(), orderLine.getInstructionStatus())).collect(Collectors.toList()).get(0).getMeaning();
                orderLine.setInstructionStatusMeaning(insStatusMeaning);
                orderLine.setExecuteQty(finalExecuteQty);
                orderLine.setBarCodeQty(finalBarCodeQty);
                orderLine.setSumCount(BigDecimal.valueOf(materialLotList.size()));
                //获取推荐货位
                String locatorRecomMode = mtInstructionMapper.getMode(tenantId,
                        barCodeDtoList.get(0).getSiteId(),
                        orderLine.getMaterialId(),
                        orderLine.getToLocatorId());
                if(StringUtils.isBlank(locatorRecomMode)){
                    orderLine.setRecommendLocatorCode("");
                }else if(locatorRecomMode.equals("POSITION")){
                    MtPfepInventoryVO mtPfepInventoryVO = new MtPfepInventoryVO();
                    mtPfepInventoryVO.setMaterialId(orderLine.getMaterialId());
                    mtPfepInventoryVO.setSiteId(barCodeDtoList.get(0).getSiteId());
                    mtPfepInventoryVO.setOrganizationType("LOCATOR");
                    mtPfepInventoryVO.setOrganizationId(orderLine.getToLocatorId());
                    MtPfepInventory mtPfepInventory = mtPfepInventoryRepository.pfepInventoryGet(tenantId,mtPfepInventoryVO);
                    if(StringUtils.isBlank(mtPfepInventory.getStockLocatorId())){
                        orderLine.setRecommendLocatorCode("");
                    }else{
                        orderLine.setRecommendLocatorCode(mtModLocatorRepository.selectByPrimaryKey(mtPfepInventory.getStockLocatorId()).getLocatorCode());
                    }
                }else if(locatorRecomMode.equals("INERTIA")){
                    WmsPfepInertiaLocator wmsPfepInertiaLocator = new WmsPfepInertiaLocator();
                    wmsPfepInertiaLocator.setTenantId(tenantId);
                    wmsPfepInertiaLocator.setMaterialId(orderLine.getMaterialId());
                    wmsPfepInertiaLocator.setSiteId(barCodeDtoList.get(0).getSiteId());
                    wmsPfepInertiaLocator.setWarehouseId(orderLine.getToLocatorId());
                    List<WmsPfepInertiaLocator> wmsPfepInertiaLocatorList = wmsPfepInertiaLocatorRepository.select(wmsPfepInertiaLocator);
                    if(wmsPfepInertiaLocatorList.size() !=0){
                        orderLine.setRecommendLocatorCode(mtModLocatorRepository.selectByPrimaryKey(wmsPfepInertiaLocatorList.get(0).getLocatorId()).getLocatorCode());
                    }else{
                        orderLine.setRecommendLocatorCode("");
                    }
                }
            }
            orderLine.setPrimaryUomQty(barDto.getPrimaryUomQty());
            orderLine.setSumBarCodeCount(BigDecimal.ONE);
            if(StringUtils.isNotBlank(orderLine.getRecommendLocatorCode())){
                //判断推荐货位是否在值集ITF.LOCATOR_LABEL_ID中
                List<LovValueDTO> LovValueDTOs = lovAdapter.queryLovValue("ITF.LOCATOR_LABEL_ID", tenantId);
                List<LovValueDTO> LovValueDTOList = LovValueDTOs.stream().filter(item ->item.getValue().equals(orderLine.getRecommendLocatorCode())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(LovValueDTOList)){
                    ItfLightTaskIfaceDTO itfLightTaskIfaceDTO = new ItfLightTaskIfaceDTO();
                    itfLightTaskIfaceDTO.setInstructionDocId(orderLine.getInstructionDocId());
                    itfLightTaskIfaceDTO.setInstructionId(orderLine.getInstructionId());
                    itfLightTaskIfaceDTO.setLocatorCode(orderLine.getRecommendLocatorCode());
                    itfLightTaskIfaceDTO.setTaskType("IN");
                    itfLightTaskIfaceDTOS.add(itfLightTaskIfaceDTO);
                }
            }
            barDto.setOrderLineDto(orderLine);
            returnDtoList.add(barDto);
        }
        // 校验单据行目标仓库是否一致(针对容器条码)
        List<String> toWarehouseIds = returnDtoList.stream().map(WmsMaterialOnShelfBarCodeDTO::getOrderLineDto)
                .map(WmsMaterialOnShelfDocLineDTO::getToLocatorId).distinct().collect(Collectors.toList());
        if (toWarehouseIds.size() > 1) {
            throw new MtException("WMS_INV_TRANSFER_0033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_INV_TRANSFER_0033", WmsConstant.ConstantValue.WMS));
        }
        List<String> instructionDocIds = returnDtoList.stream().map(WmsMaterialOnShelfBarCodeDTO::getOrderLineDto)
                .map(WmsMaterialOnShelfDocLineDTO::getInstructionDocId).distinct().collect(Collectors.toList());
        if (instructionDocIds.size() > 1) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0020", WmsConstant.ConstantValue.WMS, dto.getBarCode()));
        }
        // 不启用单据扫描，为物料批时，返回对应单据头信息
        if (!WmsConstant.ConstantValue.YES.equals(dto.getEnableDocFlag())) {
            MtInstructionDoc doc = mtInstructionDocRepository.selectByPrimaryKey(returnDtoList.get(0).getOrderLineDto().getInstructionDocId());
            MtGenType type = mtGenTypeRepository.getGenType(tenantId, "INSTRUCTION", "INSTRUCTION_DOC_TYPE", instructionDocType);
            String finalInstructionDocType = instructionDocType;
            String docStatus = WmsConstant.DocType.DELIVERY_DOC.equals(doc.getInstructionDocType()) ? "WMS.DELIVERY_DOC.STATUS" : "WMS.RECEIPT_DOC_STATUS";
            String docStatusMeaning = lovAdapter.queryLovMeaning(docStatus, tenantId, doc.getInstructionDocStatus());
            returnDto.setOrderDto(new WmsMaterialOnShelfDocDTO() {{
                setInstructionDocId(doc.getInstructionDocId());
                setInstructionDocNum(doc.getInstructionDocNum());
                setInstructionDocStatus(doc.getInstructionDocStatus());
                setInstructionDocStatusMeaning(docStatusMeaning);
                setInstructionDocType(finalInstructionDocType);
                setInstructionDocTypeDesc(type.getDescription());
                setInspectionDate(null);
                setRemark(doc.getRemark());
            }});
        }
        // 判断条码类型
        if (WmsConstant.MaterialLotType.MATERIAL_LOT.equals(codeType)) {
            // 若为物料批条码，获取推荐货位
//            WmsMaterialOnShelfRecomLocDTO recomLocDto = new WmsMaterialOnShelfRecomLocDTO();
//            recomLocDto.setSiteId(returnDtoList.get(0).getSiteId());
//            recomLocDto.setMaterialId(returnDtoList.get(0).getMaterialId());
//            recomLocDto.setWarehouseId(returnDtoList.get(0).getOrderLineDto().getToLocatorId());
//            MtModLocator locator = getRecomLocator(tenantId, recomLocDto);
//            if (!ObjectUtils.isEmpty(locator)) {
//                returnDto.setLocatorId(locator.getLocatorId());
//                returnDto.setLocatorCode(locator.getLocatorCode());
//                returnDto.setLocatorName(locator.getLocatorName());
//                returnDto.setWarehouseId(locator.getParentLocatorId());
//            }
            if(CollectionUtils.isNotEmpty(returnDtoList)){
                if(StringUtils.isNotBlank(returnDtoList.get(0).getOrderLineDto().getRecommendLocatorCode())){
                    MtModLocator mtModLocator = new MtModLocator();
                    mtModLocator.setTenantId(tenantId);
                    mtModLocator.setLocatorCode(returnDtoList.get(0).getOrderLineDto().getRecommendLocatorCode());
                    List<MtModLocator> mtModLocatorList = mtModLocatorRepository.select(mtModLocator);
                    if(CollectionUtils.isNotEmpty(mtModLocatorList)){
                        returnDto.setLocatorId(mtModLocatorList.get(0).getLocatorId());
                        returnDto.setLocatorCode(mtModLocatorList.get(0).getLocatorCode());
                        returnDto.setLocatorName(mtModLocatorList.get(0).getLocatorName());
                        returnDto.setWarehouseId(mtModLocatorList.get(0).getParentLocatorId());
                    }
                }
            }
            returnDto.setCodeType(WmsConstant.MaterialLotType.MATERIAL_LOT);
        } else {
            returnDto.setCodeType(WmsConstant.MaterialLotType.CONTAINER);
        }
        if(CollectionUtils.isNotEmpty(itfLightTaskIfaceDTOS)){
            List<ItfLightTaskIfaceVO> itfLightTaskIfaceVOS = itfLightTaskIfaceService.itfLightTaskIface(tenantId,itfLightTaskIfaceDTOS);
            if(CollectionUtils.isNotEmpty(itfLightTaskIfaceVOS)){
                List<String> taskNumList = itfLightTaskIfaceVOS.stream().map(ItfLightTaskIfaceVO::getTaskNum).collect(Collectors.toList());
                List<ItfLightTaskIface> itfLightTaskIfaces = itfLightTaskIfaceMapper.selectByTaskNum(tenantId,taskNumList);
                for(WmsMaterialOnShelfBarCodeDTO wmsMaterialOnShelfBarCodeDTO:returnDtoList){
                    List<ItfLightTaskIface> itfLightTaskIfaceList = itfLightTaskIfaces.stream()
                            .filter(item ->item.getDocLineId().equals(wmsMaterialOnShelfBarCodeDTO.getOrderLineDto().getInstructionId())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(itfLightTaskIfaceList)){
                        wmsMaterialOnShelfBarCodeDTO.getOrderLineDto().setTaskNum(itfLightTaskIfaceList.get(0).getTaskNum());
                        wmsMaterialOnShelfBarCodeDTO.getOrderLineDto().setStatus(itfLightTaskIfaceList.get(0).getStatus());
                        wmsMaterialOnShelfBarCodeDTO.getOrderLineDto().setTaskStatus(itfLightTaskIfaceList.get(0).getTaskStatus());
                    }
                }
            }
        }
        returnDto.setBarCode(dto.getBarCode());
        returnDto.setBarCodeDtoList(returnDtoList);
        return returnDto;
    }

    @Override
    public WmsMaterialOnShelfDTO queryLocatorByCode(Long tenantId, String locatorCode, WmsMaterialOnShelfDTO dto) {
        if (StringUtils.isEmpty(locatorCode)) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0003", WmsConstant.ConstantValue.WMS, "locatorCode"));
        }
        MtModLocator mtModLocator = mtModLocatorRepository.selectOne(new MtModLocator() {{
            setLocatorCode(locatorCode);
            setEnableFlag(WmsConstant.CONSTANT_Y);
            setTenantId(tenantId);
        }});
        if (ObjectUtils.isEmpty(mtModLocator)) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0012", WmsConstant.ConstantValue.WMS, locatorCode));
        }
        // 货位类型校验
        if (!WmsConstant.LocatorType.TYPE_4.equals(mtModLocator.getLocatorType()) && !WmsConstant.LocatorType.DEFAULT_STORAGE.equals(mtModLocator.getLocatorType())) {
            MtGenType type = mtGenTypeRepository.getGenType(tenantId, "MODELING", "LOCATOR_TYPE", mtModLocator.getLocatorType());
            throw new MtException("WMS_PUT_IN_STOCK_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_PUT_IN_STOCK_021", WmsConstant.ConstantValue.WMS, dto.getBarCode(), type.getDescription()));
        }
        // 货位类别校验
        if (!WmsConstant.LocatorCategory.INVENTORY.equals(mtModLocator.getLocatorCategory())) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0013", WmsConstant.ConstantValue.WMS, locatorCode));
        }
        // 父层库位校验
        if (StringUtils.isEmpty(mtModLocator.getParentLocatorId())) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0014", WmsConstant.ConstantValue.WMS, locatorCode));
        }
        // 物料批条码，校验单据对应仓库与扫描货位上层库位是否一致
        // if (WmsConstant.MaterialLotType.MATERIAL_LOT.equals(dto.getCodeType())) {
        WmsMaterialOnShelfBarCodeDTO barCodeDto = dto.getBarCodeDtoList().get(0);
        if (!mtModLocator.getParentLocatorId().equals(barCodeDto.getOrderLineDto().getToLocatorId())) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0015", WmsConstant.ConstantValue.WMS, locatorCode));
        }
        //}
        dto.setLocatorId(mtModLocator.getLocatorId());
        dto.setLocatorCode(mtModLocator.getLocatorCode());
        dto.setLocatorName(mtModLocator.getLocatorName());
        dto.setWarehouseId(mtModLocator.getParentLocatorId());
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<WmsMaterialOnShelfExecuteDTO3> execute(Long tenantId, List<WmsMaterialOnShelfExecuteDTO3> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0016", WmsConstant.ConstantValue.WMS));
        }
        // 20211202 add by sanfeng.zhang for wenxin.zhang 对条码进行加锁
        List<WmsMaterialOnShelfExecuteDTO2> barCodeDtoList = new ArrayList<>();
        for (WmsMaterialOnShelfExecuteDTO3 wmsMaterialOnShelfExecuteDTO3 : dtoList) {
            if (CollectionUtils.isNotEmpty(wmsMaterialOnShelfExecuteDTO3.getDtoList())) {
                for (WmsMaterialOnShelfExecuteDTO wmsMaterialOnShelfExecuteDTO : wmsMaterialOnShelfExecuteDTO3.getDtoList()) {
                    if (CollectionUtils.isNotEmpty(wmsMaterialOnShelfExecuteDTO.getBarCodeDtoList())) {
                        barCodeDtoList.addAll(wmsMaterialOnShelfExecuteDTO.getBarCodeDtoList());
                    }
                }
            }
        }
        List<HmeObjectRecordLock> recordLockList = new ArrayList<>();
        for (WmsMaterialOnShelfExecuteDTO2 wmsMaterialOnShelfExecuteDTO2 : barCodeDtoList) {
            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
            hmeObjectRecordLockDTO.setFunctionName("物料上架");
            hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PDA);
            hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
            hmeObjectRecordLockDTO.setObjectRecordId(wmsMaterialOnShelfExecuteDTO2.getMaterialLotId());
            hmeObjectRecordLockDTO.setObjectRecordCode(wmsMaterialOnShelfExecuteDTO2.getMaterialLotCode());
            HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
            recordLockList.add(hmeObjectRecordLock);
        }
        if (CollectionUtils.isNotEmpty(recordLockList)) {
            hmeObjectRecordLockRepository.batchCommonLockObject(tenantId, recordLockList);
        }

        try {
            String instructionDocType = dtoList.get(0).getInstructionDocType();
            String eventRequestId = null;
            String eventId;
            if (StringUtils.equals(WmsConstant.DocType.DELIVERY_DOC, instructionDocType)) {
                // 创建送货单事件
                MtEventCreateVO eventCreate = new MtEventCreateVO();
                eventCreate.setEventTypeCode(WmsConstant.EVENT_MATERIAL_STOCKING);
                eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
            } else {
                // 创建入库单请求事件
                eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, WmsConstant.EVENT_PRODUCT_RECEIPT);
                // 创建入库单事件
                MtEventCreateVO eventCreate = new MtEventCreateVO();
                eventCreate.setEventRequestId(eventRequestId);
                eventCreate.setEventTypeCode(WmsConstant.EVENT_PRODUCT_RECEIPT);
                eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
            }
            List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
            Set<String> containerIdSet = new HashSet<>();
            for (WmsMaterialOnShelfExecuteDTO3 dto : dtoList) {
                MtInstruction instruction = mtInstructionRepository.selectByPrimaryKey(dto.getInstructionId());
                dto.setWarehouseId(instruction.getToLocatorId());
                for (WmsMaterialOnShelfExecuteDTO exeDto : dto.getDtoList()) {
                    //货位扫描校验
                    if (StringUtils.isBlank(exeDto.getLocatorCode()) || StringUtils.isBlank(exeDto.getLocatorId())) {
                        throw new MtException("WMS_MATERIAL_ON_SHELF_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_MATERIAL_ON_SHELF_0021", WmsConstant.ConstantValue.WMS, exeDto.getBarCode()));
                    }

                    // 送货单执行处理
                    if (StringUtils.equals(WmsConstant.DocType.DELIVERY_DOC, dto.getInstructionDocType())) {
                        // 批量更新条码
                        List<MtMaterialLotVO20> updateList = new ArrayList<>();
                        List<MtCommonExtendVO6> attrList = new ArrayList<>();
                        for (WmsMaterialOnShelfExecuteDTO2 barCodeDto : exeDto.getBarCodeDtoList()) {
                            //更新物料批 modify by yuchao.wang for kang.wang at 2020.9.18
                            MtMaterialLotVO20 materialLotVo = new MtMaterialLotVO20();
                            materialLotVo.setMaterialLotId(barCodeDto.getMaterialLotId());
                            materialLotVo.setFreezeFlag(NO);
                            updateList.add(materialLotVo);
                            // 更新/创建 惯性货位 add by jiangling.zheng 2020-09-21
                            if (!StringUtils.isBlank(instruction.getToLocatorId())) {
                                WmsMaterialOnShelfRecomLocDTO recomLocDto = new WmsMaterialOnShelfRecomLocDTO();
                                recomLocDto.setWarehouseId(instruction.getToLocatorId());
                                recomLocDto.setMaterialId(barCodeDto.getMaterialId());
                                recomLocDto.setSiteId(barCodeDto.getSiteId());
                                inertiaLocatorUpdate(tenantId, exeDto.getLocatorId(), recomLocDto);
                            }
                            // end add
                            MtCommonExtendVO6 attr = new MtCommonExtendVO6();
                            attr.setKeyId(barCodeDto.getMaterialLotId());
                            List<MtCommonExtendVO5> attrs = new ArrayList<>();
                            MtCommonExtendVO5 att;
                            att = new MtCommonExtendVO5();
                            att.setAttrName(HmeConstants.ExtendAttr.STATUS);
                            att.setAttrValue(WmsConstant.MaterialLotStatus.MINSTOCK);
                            attrs.add(att);
                            att = new MtCommonExtendVO5();
                            att.setAttrName("ACTUAL_LOCATOR");
                            att.setAttrValue(exeDto.getLocatorCode());
                            attrs.add(att);
                            attr.setAttrs(attrs);
                            attrList.add(attr);
                        }
                        // 批量更新条码和拓展属性
                        mtMaterialLotRepository.materialLotBatchUpdate(tenantId, updateList, eventId, NO);
                        mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, WmsConstant.AttrTable.MT_MATERIAL_LOT_ATTR, eventId, attrList);
                    } else {
                        //判断容器是否已经转移
                        Boolean flag = containerIdSet.contains(exeDto.getContainerId());
                        // 入库单执行处理
                        List<WmsObjectTransactionRequestVO> wmsObjectTransactionRequestVOS = proExecute(tenantId, eventRequestId, eventId, dto, exeDto, flag);
                        objectTransactionRequestList.addAll(wmsObjectTransactionRequestVOS);
                        if (StringUtils.isNotBlank(exeDto.getContainerId())) {
                            containerIdSet.add(exeDto.getContainerId());
                        }
                    }
                }
            }

            if (StringUtils.equals(WmsConstant.DocType.PRODUCT_RECEIPT, instructionDocType)) {
                // 更新指令行
                Map<String, WmsMaterialOnShelfExecuteDTO3> instructionMap = dtoList.stream().collect(Collectors.toMap(WmsMaterialOnShelfExecuteDTO3::getInstructionId, a -> a, (k1, k2) -> k1));
                String finalEventRequestId = eventRequestId;
                instructionMap.forEach((instructionId, rec) -> {
                    List<MtInstructionActual> mtInstructionActualList =
                            mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId, instructionId);
                    if (CollectionUtils.isEmpty(mtInstructionActualList)) {
                        throw new MtException("WMS_MATERIAL_ON_SHELF_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_MATERIAL_ON_SHELF_0019", WmsConstant.ConstantValue.WMS, rec.getInstructionNum()));
                    }
                    // 行数量与实绩数量相等
                    Double actualQty = mtInstructionActualList.stream().mapToDouble(MtInstructionActual::getActualQty).sum();
                    if (rec.getExecuteQty().compareTo(BigDecimal.valueOf(actualQty)) == 0) {
                        //指令完成
                        mtInstructionRepository.instructionComplete(tenantId, instructionId, finalEventRequestId);
                    }
                });

                // 更新指令头
                List<String> instructionDocIds = dtoList.stream().map(WmsMaterialOnShelfExecuteDTO3::getInstructionDocId)
                        .distinct().collect(Collectors.toList());
                for (String instructionDocId : instructionDocIds) {
                    // 获取单据下所有单据行
                    List<MtInstruction> instructionList = mtInstructionRepository.select(new MtInstruction() {{
                        setSourceDocId(instructionDocId);
                    }});
                    if (CollectionUtils.isEmpty(instructionList)) {
                        continue;
                    }
                    List<String> instructionIds = instructionList.stream().map(MtInstruction::getInstructionId).collect(Collectors.toList());
                    // 获取单据下所有指令实绩行
                    List<MtInstructionActualVO2> instructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyBatchGet(tenantId, instructionIds);
                    if (CollectionUtils.isEmpty(instructionActualList)) {
                        continue;
                    }
                    Double lineQty = instructionList.stream().mapToDouble(MtInstruction::getQuantity).sum();
                    Double actualQty = instructionActualList.stream().mapToDouble(MtInstructionActualVO2::getActualQty).sum();
                    // 相等则说明所单据行都一直执行完成
                    if (lineQty.compareTo(actualQty) == 0) {
                        mtInstructionDocRepository.instructionDocComplete(tenantId, instructionDocId, eventRequestId);
                    }
                }
            }
            // 本次执行条码记录事务
            if (CollectionUtils.isNotEmpty(objectTransactionRequestList)) {
                List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
                itfObjectTransactionIfaceService.sendSapMaterialMove(tenantId, wmsObjectTransactionResponseVOS);
            }
            List<ItfLightTaskIfaceDTO> itfLightTaskIfaceDTOS = new ArrayList<>();
            for(WmsMaterialOnShelfExecuteDTO3 wmsMaterialOnShelfExecuteDTO3:dtoList){
                if(StringUtils.isNotBlank(wmsMaterialOnShelfExecuteDTO3.getTaskNum()) && !"false".equals(wmsMaterialOnShelfExecuteDTO3.getTaskNum())){
                    ItfLightTaskIfaceDTO itfLightTaskIfaceDTO = new ItfLightTaskIfaceDTO();
                    itfLightTaskIfaceDTO.setTaskStatus("OFF");
                    itfLightTaskIfaceDTO.setTaskNum(wmsMaterialOnShelfExecuteDTO3.getTaskNum());
                    itfLightTaskIfaceDTOS.add(itfLightTaskIfaceDTO);
                }
            }
            if(CollectionUtils.isNotEmpty(itfLightTaskIfaceDTOS)){
                itfLightTaskIfaceService.itfLightTaskIface(tenantId,itfLightTaskIfaceDTOS);
            }
        } catch (Exception e){
            throw new CommonException(e.getMessage());
        }finally {
            //解锁
            hmeObjectRecordLockRepository.batchReleaseLock(tenantId , recordLockList , HmeConstants.ConstantValue.YES);
        }
        return dtoList;
    }

    @Override
    public List<WmsMaterialOnShelfBarCodeDTO> detail(Long tenantId, String instructionId) {
        List<WmsMaterialOnShelfBarCodeDTO> barCodeDtoList = new ArrayList<>();
        MtInstruction mtInstruction = mtInstructionRepository.selectByPrimaryKey(instructionId);
        // 获取单据行对应单据头信息
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(mtInstruction.getSourceDocId());
        // 获取指令实绩
        List<MtInstructionActual> mtInstructionActualList =
                mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId, instructionId);
        if (CollectionUtils.isNotEmpty(mtInstructionActualList)) {
            List<String> materialLotIds = null;
            // 获取指令实绩明细
            List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository
                    .instructionLimitActualDetailQuery(tenantId, instructionId);
            if (CollectionUtils.isNotEmpty(mtInstructionActualDetails)) {
                List<String> matLotIds = mtInstructionActualDetails.stream().map(MtInstructionActualDetail::getMaterialLotId).collect(Collectors.toList());
                if (StringUtils.equals(mtInstructionDoc.getInstructionDocType(), WmsConstant.DocType.DELIVERY_DOC)) {
                    materialLotIds = matLotIds.stream().filter(id -> WmsConstant.MaterialLotStatus.MINSTOCK.equals(
                            Optional.ofNullable(getAttrValue(tenantId, WmsConstant.AttrTable.MT_MATERIAL_LOT_ATTR, id, HmeConstants.ExtendAttr.STATUS))
                                    .orElse(""))).collect(Collectors.toList());
                } else {
                    materialLotIds = matLotIds;
                }
            }
            if (CollectionUtils.isNotEmpty(materialLotIds)) {
                List<WmsMaterialOnShelfBarCodeDTO> historyBarCodeDtoList = wmsMaterialOnShelfRepository
                        .selectMaterialLotCondition(tenantId, materialLotIds);
                // 前台缓存数据标记
                historyBarCodeDtoList.forEach(barDto -> barDto.setCacheFlag(WmsConstant.CONSTANT_N));
                barCodeDtoList.addAll(historyBarCodeDtoList);
            }
        }
        return barCodeDtoList;
    }

    /**
     * 货位推荐
     *
     * @param tenantId
     * @param recomLocDto
     * @return tarzan.modeling.domain.entity.MtModLocator
     * @author jiangling.zheng@hand-china.com 2020/8/17 11:07
     */

    private MtModLocator getRecomLocator(Long tenantId, WmsMaterialOnShelfRecomLocDTO recomLocDto) {
        // 此处代码为pfepInventoryKidGet api中抽出代码，只需通过物料站点及物料id
        // 查询MtPfepInventory 未查询到则不推荐，无需走默认类别等逻辑
        MtPfepInventory inventory = pfepInventoryGet(tenantId, recomLocDto);
        // end modify
        if (ObjectUtils.isEmpty(inventory)) {
            return null;
        }
        // 获取货位推荐模式
        MtModLocator mtModLocator;
        String locatorRecomMode = getAttrValue(tenantId, WmsConstant.AttrTable.MT_PFEP_INVENTORY_ATTR, inventory.getPfepInventoryId(), "LOCATOR_RECOM_MODE");
        if (StringUtils.isNotBlank(locatorRecomMode) && StringUtils.isNotBlank(inventory.getStockLocatorId()) &&
                WmsConstant.LocatorRecomMode.POSITION.equals(locatorRecomMode)) {
            mtModLocator = mtModLocatorRepository.locatorBasicPropertyGet(tenantId, inventory.getStockLocatorId());
        } else if (!StringUtils.isBlank(locatorRecomMode) && WmsConstant.LocatorRecomMode.INERTIA.equals(locatorRecomMode)) {
            WmsPfepInertiaLocator wmsPfepInertiaLocator = wmsPfepInertiaLocatorRepository.selectOne(new WmsPfepInertiaLocator() {{
                setTenantId(tenantId);
                setSiteId(recomLocDto.getSiteId());
                setWarehouseId(recomLocDto.getWarehouseId());
                setMaterialId(recomLocDto.getMaterialId());
            }});
            if (Objects.isNull(wmsPfepInertiaLocator)) {
                return null;
            }
            mtModLocator = mtModLocatorRepository.selectByPrimaryKey(wmsPfepInertiaLocator.getLocatorId());
        } else {
            return null;
        }
        if (ObjectUtils.isEmpty(mtModLocator) || !WmsConstant.LocatorCategory.INVENTORY.equals(mtModLocator.getLocatorCategory())) {
            return null;
        }
        // 推荐货位对应仓库与单据行目标仓库不一致则不推荐
        if (!StringUtils.equals(mtModLocator.getParentLocatorId(), recomLocDto.getWarehouseId())) {
            return null;
        }
        return mtModLocator;
    }

    /**
     * 获取扩展字段值
     *
     * @param tenantId
     * @param tableName
     * @param keyId
     * @param attrName
     * @return java.lang.String
     * @author jiangling.zheng@hand-china.com 2020/6/10 10:54
     */

    private String getAttrValue(Long tenantId, String tableName, String keyId, String attrName) {
        MtExtendVO mtExtendVO = new MtExtendVO();
        mtExtendVO.setTableName(tableName);
        mtExtendVO.setKeyId(keyId);
        mtExtendVO.setAttrName(attrName);
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
        if (CollectionUtils.isEmpty(mtExtendAttrVOList)) {
            return null;
        }
        return mtExtendAttrVOList.get(0).getAttrValue();
    }

    /**
     * 获取执行物料区域数据
     *
     * @param tenantId
     * @param instructionList
     * @param doc
     * @return java.util.List<com.ruike.wms.api.dto.WmsMaterialOnShelfDocLineDTO>
     * @author jiangling.zheng@hand-china.com 2020/6/10 10:59
     */
    private List<WmsMaterialOnShelfDocLineDTO> instructionBatchGet(Long tenantId, List<MtInstruction> instructionList, MtInstructionDoc doc) {
        List<WmsMaterialOnShelfDocLineDTO> orderLineList = new ArrayList<>();
        // 指令数据
        instructionList.forEach(instruction -> {
            List<String> materialIds = Collections.singletonList(instruction.getMaterialId());
            // 物料版本
            String materialVersion = getAttrValue(tenantId, WmsConstant.AttrTable.MT_INSTRUCTION_ATTR, instruction.getInstructionId(), "MATERIAL_VERSION");
            // 获取指令实绩
            List<MtInstructionActual> mtInstructionActualList =
                    mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId, instruction.getInstructionId());
            if (StringUtils.equals(WmsConstant.DocType.DELIVERY_DOC, doc.getInstructionDocType()) && CollectionUtils.isEmpty(mtInstructionActualList)) {
                throw new MtException("MT_INSTRUCTION_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INSTRUCTION_0024", "INSTRUCTION", ""));
            }

            BigDecimal executeQty = BigDecimal.ZERO;
            BigDecimal sumActualQty = BigDecimal.ZERO;
            List<MtMaterialLot> mtMaterialLotList = new ArrayList<>();
            // 送货单逻辑：计算对应指令实绩数量
            if (StringUtils.equals(WmsConstant.DocType.DELIVERY_DOC, doc.getInstructionDocType())) {
                List<String> actualIds = mtInstructionActualList.stream().filter(item ->
                        WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER.equals(item.getInstructionType())).map(MtInstructionActual::getActualId).collect(Collectors.toList());
                for (String actualId : actualIds) {
                    String exchangedQtyStr = getAttrValue(tenantId, WmsConstant.AttrTable.MT_INSTRUCTION_ACTUAL_ATTR, actualId, "EXCHANGED_QTY");
                    String actualReceiveQtyStr = getAttrValue(tenantId, WmsConstant.AttrTable.MT_INSTRUCTION_ACTUAL_ATTR, actualId, "ACTUAL_RECEIVE_QTY");
                    BigDecimal exchangedQty = StringUtils.isBlank(exchangedQtyStr) ? BigDecimal.ZERO : new BigDecimal(exchangedQtyStr);
                    BigDecimal actualReceiveQty = StringUtils.isBlank(actualReceiveQtyStr) ? BigDecimal.ZERO : new BigDecimal(actualReceiveQtyStr);
                    executeQty = executeQty.add(exchangedQty).add(actualReceiveQty);
                }
                // 获取指令实绩明细
                List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository
                        .instructionActualLimitDetailBatchQuery(tenantId, actualIds);
                if (CollectionUtils.isNotEmpty(mtInstructionActualDetails)) {
                    List<String> materialLotIds = mtInstructionActualDetails.stream().map(MtInstructionActualDetail::getMaterialLotId).collect(Collectors.toList());
                    mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds);
                    // 校验指令实绩明细物料批状态
                    for (String materialLotId : materialLotIds) {
                        String status = getAttrValue(tenantId, WmsConstant.AttrTable.MT_MATERIAL_LOT_ATTR, materialLotId, HmeConstants.ExtendAttr.STATUS);
                        if (!WmsConstant.MaterialLotStatus.MINSTOCK.equals(Optional.ofNullable(status).orElse(""))) {
                            mtMaterialLotList.removeIf(item -> item.getMaterialLotId().equals(materialLotId));
                        }
                    }
                }
            } else {
                // 入库单逻辑
                /*List<MtInstructionDetailVO3> mtInstructionDetailVO3s = new ArrayList<>();
                MtInstructionDetailVO3 detailVO3 = new MtInstructionDetailVO3();
                detailVO3.setInstructionId(instruction.getInstructionId());
                mtInstructionDetailVO3s.add(detailVO3);
                List<MtInstructionDetailVO4> mtInstructionDetails = mtInstructionDetailRepository
                        .propertyLimitInstructionDetailBatchQuery(tenantId, mtInstructionDetailVO3s);
                if (CollectionUtils.isNotEmpty(mtInstructionDetails)) {
                    List<String> materialLotIds = mtInstructionDetails.stream().map(MtInstructionDetailVO4::getMaterialLotId).collect(Collectors.toList());
                    mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds);
                }*/
                if (CollectionUtils.isNotEmpty(mtInstructionActualList)) {
                    List<String> actualIds = mtInstructionActualList.stream().map(MtInstructionActual::getActualId).collect(Collectors.toList());
                    // 获取指令实绩明细
                    List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository
                            .instructionActualLimitDetailBatchQuery(tenantId, actualIds);
                    if (CollectionUtils.isNotEmpty(mtInstructionActualDetails)) {
                        List<String> materialLotIds = mtInstructionActualDetails.stream().map(MtInstructionActualDetail::getMaterialLotId).collect(Collectors.toList());
                        mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds);
                    }
                    // 获取指令实绩数量
                    sumActualQty = mtInstructionActualList.stream()
                            .map(MtInstructionActual::getActualQty).collect(CollectorsUtil.summingBigDecimal(c ->
                                    BigDecimal.valueOf(null == c ? 0.0D : c)));
                }
            }

            double barCodeQty = 0.0D;
            if (CollectionUtils.isNotEmpty(mtMaterialLotList)) {
                barCodeQty = mtMaterialLotList.stream().mapToDouble(MtMaterialLot::getPrimaryUomQty).sum();
            }
            BigDecimal finalExecuteQty = StringUtils.equals(doc.getInstructionDocType(), WmsConstant.DocType.DELIVERY_DOC) ? executeQty : BigDecimal.valueOf(instruction.getQuantity());
            BigDecimal finalBarCodeQty = StringUtils.equals(doc.getInstructionDocType(), WmsConstant.DocType.DELIVERY_DOC) ? BigDecimal.valueOf(barCodeQty) : sumActualQty;
            BigDecimal sumCount = BigDecimal.valueOf(mtMaterialLotList.size());
            orderLineList.add(new WmsMaterialOnShelfDocLineDTO() {{
                setInstructionDocId(instruction.getSourceDocId());
                setInstructionDocNum(doc.getInstructionDocNum());
                setInstructionId(instruction.getInstructionId());
                setInstructionNum(instruction.getInstructionNum());
                setMaterialId(instruction.getMaterialId());
                MtMaterialVO mtMaterial = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds).get(0);
                setMaterialCode(mtMaterial.getMaterialCode());
                setMaterialName(mtMaterial.getMaterialName());
                setMaterialVersion(materialVersion);
                setExecuteQty(finalExecuteQty);
                setBarCodeQty(finalBarCodeQty);
                setSumCount(sumCount);
                setUomId(instruction.getUomId());
                MtUomVO mtUomVO = mtUomRepository.uomPropertyGet(tenantId, instruction.getUomId());
                setUomCode(mtUomVO.getUomCode());
                setUomName(mtUomVO.getUomName());
                String insStatus = StringUtils.equals(WmsConstant.DocType.DELIVERY_DOC, doc.getInstructionDocType()) ? "WMS.DELIVERY_DOC_LINE.STATUS" : "WMS.RECEIPT_LINE_STATUS";
                String insStatusMeaning = lovAdapter.queryLovMeaning(insStatus, tenantId, instruction.getInstructionStatus());
                setInstructionStatus(instruction.getInstructionStatus());
                setInstructionStatusMeaning(insStatusMeaning);
                setToLocatorId(instruction.getToLocatorId());
            }});
        });
        return orderLineList;
    }

    /**
     * 入库单类型执行逻辑处理
     *
     * @param tenantId
     * @param dto
     * @param exeDto
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<WmsObjectTransactionRequestVO> proExecute(Long tenantId, String eventRequestId, String eventId,
                                                          WmsMaterialOnShelfExecuteDTO3 dto, WmsMaterialOnShelfExecuteDTO exeDto, Boolean flag) {
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        MtModLocatorOrgRelVO2 rel = new MtModLocatorOrgRelVO2();
        rel.setLocatorId(exeDto.getLocatorId());
        rel.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.SITE);
        List<MtModLocatorOrgRelVO3> existSiteList =
                mtModLocatorOrgRelRepository.locatorLimitOrganizationQuery(tenantId, rel);
        if (existSiteList.size() != 1) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0017", WmsConstant.ConstantValue.WMS, exeDto.getLocatorCode()));
        }
        if (StringUtils.isNotEmpty(exeDto.getContainerId())) {
            if (!flag) {
                // 获取容器转移的目标货位
                MtContainerVO7 conDto = new MtContainerVO7();
                conDto.setContainerId(exeDto.getContainerId());
                conDto.setTargetSiteId(existSiteList.get(0).getOrganizationId());
                conDto.setTargetLocatorId(exeDto.getLocatorId());
                conDto.setEventRequestId(eventRequestId);
                mtContainerRepository.containerTransfer(tenantId, conDto);
            }
        }
        Map<String, WmsMaterialLotAttrVO> materialLotMap = materialLotRepository.selectListWithAttrByIds(tenantId, exeDto.getBarCodeDtoList().stream().map(WmsMaterialOnShelfExecuteDTO2::getMaterialLotId).collect(Collectors.toList())).stream().collect(Collectors.toMap(WmsMaterialLotAttrVO::getMaterialLotId, rec -> rec, (key1, key2) -> key1));
        // 批量转移条码
        if (StringUtils.isEmpty(exeDto.getContainerId())) {
            MtMaterialLotVO14 mtMaterialLotVO14 = new MtMaterialLotVO14();
            mtMaterialLotVO14.setEventRequestId(eventRequestId);
            mtMaterialLotVO14.setTargetLocatorId(exeDto.getLocatorId());
            mtMaterialLotVO14.setTargetSiteId(existSiteList.get(0).getOrganizationId());
            mtMaterialLotVO14.setMaterialLotIds(exeDto.getBarCodeDtoList().stream().map(WmsMaterialOnShelfExecuteDTO2::getMaterialLotId).collect(Collectors.toList()));
            mtMaterialLotRepository.materialLotBatchTransfer(tenantId, mtMaterialLotVO14);
        }

        // 批量更新条码状态
        List<MtCommonExtendVO6> attrList = new ArrayList<>();
        for (WmsMaterialOnShelfExecuteDTO2 barCodeDto : exeDto.getBarCodeDtoList()) {
            MtCommonExtendVO6 attr = new MtCommonExtendVO6();
            attr.setKeyId(barCodeDto.getMaterialLotId());
            List<MtCommonExtendVO5> attrs = new ArrayList<>();
            MtCommonExtendVO5 att;
            att = new MtCommonExtendVO5();
            att.setAttrName(HmeConstants.ExtendAttr.STATUS);
            att.setAttrValue(WmsConstant.MaterialLotStatus.INSTOCK);
            attrs.add(att);
            attr.setAttrs(attrs);
            attrList.add(attr);
        }
        mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, WmsConstant.AttrTable.MT_MATERIAL_LOT_ATTR, eventId, attrList);
        List<MtInstructionVO3.MaterialLotList> materialLotMessageList = new ArrayList<>();
        for (WmsMaterialOnShelfExecuteDTO2 lotDto : exeDto.getBarCodeDtoList()) {
            // 获取条码货位仓库
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(lotDto.getLocatorId());
            String warehouseId = null;
            if (!ObjectUtils.isEmpty(mtModLocator)) {
                warehouseId = mtModLocator.getParentLocatorId();
            }
            MtModLocator modLocator = mtModLocatorRepository.selectByPrimaryKey(exeDto.getLocatorId());
            String transferWarehouseId = null;
            if (!ObjectUtils.isEmpty(modLocator)) {
                transferWarehouseId = StringUtils.isNotBlank(exeDto.getWarehouseId()) ? exeDto.getWarehouseId() : modLocator.getParentLocatorId();
            }
            String transactionTypeCode = StringUtils.equals(warehouseId, exeDto.getWarehouseId()) ?
                    WmsConstant.TransactionTypeCode.WMS_LOCATOR_TRAN : WmsConstant.TransactionTypeCode.WMS_WAREHOUSE_TRAN;
            WmsTransactionTypeDTO type = wmsTransactionTypeRepository.getTransactionType(tenantId, transactionTypeCode);
            if (Objects.isNull(type)) {
                throw new MtException("WMS_MATERIAL_ON_SHELF_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_MATERIAL_ON_SHELF_0022", WmsConstant.ConstantValue.WMS, transactionTypeCode));
            }
            WmsInvTransferObjectTrxVO objectTrxVO = new WmsInvTransferObjectTrxVO();
            objectTrxVO.setEventId(eventId);
            objectTrxVO.setTransactionTypeCode(transactionTypeCode);
            objectTrxVO.setTransactionTime(new Date());
            objectTrxVO.setTransactionQty(lotDto.getPrimaryUomQty());
            objectTrxVO.setSourceDocType(dto.getInstructionDocType());
            objectTrxVO.setSourceDocId(dto.getInstructionDocId());
            objectTrxVO.setSourceDocLineId(dto.getInstructionId());
            objectTrxVO.setSourceDocNum(dto.getInstructionDocNum());
            objectTrxVO.setSourceDocLineNum(dto.getInstructionNum());
            objectTrxVO.setTransferPlantId(existSiteList.get(0).getOrganizationId());
            objectTrxVO.setTransferWarehouseId(transferWarehouseId);
            objectTrxVO.setContainerId(exeDto.getContainerId());
            objectTrxVO.setTransferLocatorId(exeDto.getLocatorId());
            objectTrxVO.setWarehouseId(warehouseId);
            objectTrxVO.setMoveType(type.getMoveType());
            objectTrxVO.setRemark("入库单执行");
            objectTrxVO.setSoNum(materialLotMap.get(lotDto.getMaterialLotId()).getSoNum());
            objectTrxVO.setSoLineNum(materialLotMap.get(lotDto.getMaterialLotId()).getSoLineNum());
            addObjectTransaction(objectTrxVO, lotDto, objectTransactionRequestList);
            //  建立条码关系
            MtInstructionVO3.MaterialLotList materialLotList = new MtInstructionVO3.MaterialLotList();
            materialLotList.setMaterialLotId(lotDto.getMaterialLotId());
            materialLotList.setQty(lotDto.getPrimaryUomQty().doubleValue());
            materialLotList.setUomId(lotDto.getPrimaryUomId());
            materialLotList.setContainerId(exeDto.getContainerId());
            materialLotList.setFromLocatorId(lotDto.getLocatorId());
            materialLotList.setToLocatorId(exeDto.getLocatorId());
            materialLotMessageList.add(materialLotList);
        }

        // 执行指令
        MtInstructionVO3 mtInstructionExecute = new MtInstructionVO3();
        mtInstructionExecute.setEventRequestId(eventRequestId);
        mtInstructionExecute.setInstructionId(dto.getInstructionId());
        mtInstructionExecute.setMaterialLotMessageList(materialLotMessageList);
        mtLogisticInstructionService.instructionExecute(tenantId, mtInstructionExecute);

        for (WmsMaterialOnShelfExecuteDTO2 lotDto : exeDto.getBarCodeDtoList()) {
            MtInstructionDetail detail = mtInstructionDetailRepository.selectOne(new MtInstructionDetail() {{
                setTenantId(tenantId);
                setMaterialLotId(lotDto.getMaterialLotId());
                setInstructionId(dto.getInstructionId());
            }});
            if (!Objects.isNull(detail)) {
                mtInstructionDetailRepository.deleteByPrimaryKey(detail.getInstructionDetailId());
            }
            // 更新/创建 惯性货位
            if (!StringUtils.isBlank(dto.getWarehouseId())) {
                WmsMaterialOnShelfRecomLocDTO recomLocDto = new WmsMaterialOnShelfRecomLocDTO();
                recomLocDto.setWarehouseId(dto.getWarehouseId());
                recomLocDto.setMaterialId(lotDto.getMaterialId());
                recomLocDto.setSiteId(lotDto.getSiteId());
                inertiaLocatorUpdate(tenantId, exeDto.getLocatorId(), recomLocDto);
            }
        }
        return objectTransactionRequestList;
    }

    private void addObjectTransaction(WmsInvTransferObjectTrxVO dto,
                                      WmsMaterialOnShelfExecuteDTO2 lotDto,
                                      List<WmsObjectTransactionRequestVO> objectTransactionList) {
        objectTransactionList.add(new WmsObjectTransactionRequestVO() {{
            setTransactionId(null);
            setTransactionTypeCode(dto.getTransactionTypeCode());
            setEventId(dto.getEventId());
            setBarcode(lotDto.getMaterialLotCode());
            setMaterialLotId(lotDto.getMaterialLotId());
            setPlantId(lotDto.getSiteId());
            setMaterialId(lotDto.getMaterialId());
            setTransactionQty(dto.getTransactionQty());
            setTransferLotNumber(lotDto.getLot());
            setLotNumber(lotDto.getLot());
            setTransactionUom(lotDto.getPrimaryUomCode());
            setTransactionTime(new Date());
            setWarehouseId(dto.getWarehouseId());
            setLocatorId(lotDto.getLocatorId());
            setTransferPlantId(dto.getTransferPlantId());
            setTransferPlantCode(dto.getTransferPlantCode());
            setTransferWarehouseId(dto.getTransferWarehouseId());
            setTransferWarehouseCode(dto.getTransferWarehouseCode());
            setTransferLocatorId(dto.getTransferLocatorId());
            setTransferLocatorCode(dto.getTransferLocatorCode());
            setContainerId(dto.getContainerId());
            setContainerCode(dto.getContainerCode());
            setSourceDocId(dto.getSourceDocId());
            setSourceDocLineId(dto.getSourceDocLineId());
            setSourceDocNum(dto.getSourceDocNum());
            setSourceDocLineNum(dto.getSourceDocLineNum());
            setSourceDocType(dto.getSourceDocType());
            setMoveType(dto.getMoveType());
            setRemark(dto.getRemark());
            setTransactionReasonCode("成品入库");
            setMergeFlag("N");
            setSoNum(dto.getSoNum());
            setSoLineNum(dto.getSoLineNum());
        }});
    }

    /**
     * 物料存储属性获取
     *
     * @param tenantId
     * @param recomLocDto
     * @return tarzan.material.domain.entity.MtPfepInventory
     * @author jiangling.zheng@hand-china.com 2020/9/21 20:02
     */

    private MtPfepInventory pfepInventoryGet(Long tenantId, WmsMaterialOnShelfRecomLocDTO recomLocDto) {
        MtMaterialSite site = new MtMaterialSite();
        site.setMaterialId(recomLocDto.getMaterialId());
        site.setSiteId(recomLocDto.getSiteId());
        String materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId, site);
        if (StringUtils.isEmpty(materialSiteId)) {
            throw new MtException("MT_MATERIAL_0074", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_0074", "MATERIAL", "【API:pfepInventoryKidGet】"));
        }
        MtPfepInventory pfepInventory = new MtPfepInventory();
        pfepInventory.setMaterialSiteId(materialSiteId);
        pfepInventory.setEnableFlag("Y");
        pfepInventory.setTenantId(tenantId);
        pfepInventory.setOrganizationType("LOCATOR");
        List<MtPfepInventory> inventoryList = mtPfepInventoryMapper.select(pfepInventory);
        if (CollectionUtils.isEmpty(inventoryList)) {
            return null;
        }
        inventoryList.removeIf(item -> !StringUtils.equals(item.getOrganizationId(), recomLocDto.getWarehouseId()));
        if (CollectionUtils.isEmpty(inventoryList)) {
            return null;
        }
        return inventoryList.get(0);
    }

    /**
     * 更新/创建惯性货位
     *
     * @param tenantId
     * @param locatorId
     * @param recomLocDto
     * @author jiangling.zheng@hand-china.com 2020/9/21 20:01
     */

    private void inertiaLocatorUpdate(Long tenantId, String locatorId, WmsMaterialOnShelfRecomLocDTO recomLocDto) {
        WmsPfepInertiaLocator wmsPfepInertiaLocator = wmsPfepInertiaLocatorRepository.selectOne(new WmsPfepInertiaLocator() {{
            setTenantId(tenantId);
            setSiteId(recomLocDto.getSiteId());
            setWarehouseId(recomLocDto.getWarehouseId());
            setMaterialId(recomLocDto.getMaterialId());
        }});
        if (Objects.isNull(wmsPfepInertiaLocator)) {
            wmsPfepInertiaLocator = new WmsPfepInertiaLocator();
            wmsPfepInertiaLocator.setTenantId(tenantId);
            wmsPfepInertiaLocator.setSiteId(recomLocDto.getSiteId());
            wmsPfepInertiaLocator.setWarehouseId(recomLocDto.getWarehouseId());
            wmsPfepInertiaLocator.setMaterialId(recomLocDto.getMaterialId());
            wmsPfepInertiaLocator.setLocatorId(locatorId);
            wmsPfepInertiaLocatorRepository.insertSelective(wmsPfepInertiaLocator);
        } else {
            wmsPfepInertiaLocator.setLocatorId(locatorId);
            wmsPfepInertiaLocatorMapper.updateByPrimaryKeySelective(wmsPfepInertiaLocator);
        }
    }
}
