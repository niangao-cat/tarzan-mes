package com.ruike.wms.app.service.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.itf.api.dto.ItfLightTaskIfaceDTO;
import com.ruike.itf.app.service.ItfLightTaskIfaceService;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.domain.entity.ItfLightTaskIface;
import com.ruike.itf.domain.repository.ItfLightTaskIfaceRepository;
import com.ruike.itf.domain.vo.ItfLightTaskIfaceVO;
import com.ruike.itf.infra.mapper.ItfLightTaskIfaceMapper;
import com.ruike.qms.domain.entity.QmsIqcHeader;
import com.ruike.qms.domain.entity.QmsMaterialInspExempt;
import com.ruike.qms.domain.repository.QmsIqcHeaderRepository;
import com.ruike.qms.domain.repository.QmsMaterialInspExemptRepository;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsCommonApiService;
import com.ruike.wms.app.service.WmsCostCenterReturnService;
import com.ruike.wms.domain.entity.WmsPfepInertiaLocator;
import com.ruike.wms.domain.entity.WmsPutInStorageTask;
import com.ruike.wms.domain.repository.*;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsCostCenterReturnMapper;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.helper.OptionalHelper;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.message.MessageClient;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.repository.MtCostcenterRepository;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionDocVO4;
import tarzan.instruction.domain.vo.MtInstructionVO10;
import tarzan.instruction.domain.vo.MtInstructionVO11;
import tarzan.instruction.domain.vo.MtInstructionVO3;
import tarzan.instruction.infra.mapper.MtInstructionDocMapper;
import tarzan.instruction.infra.mapper.MtInstructionMapper;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtPfepInventory;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtPfepInventoryRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtSupplierRepository;
import tarzan.modeling.domain.vo.MtModLocatorVO7;
import tarzan.modeling.domain.vo.MtModLocatorVO8;
import tarzan.modeling.domain.vo.MtModSiteVO6;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.WMS;
import static java.util.stream.Collectors.toList;


/**
 * @ClassName WmsCostCenterReturnServiceImpl
 * @Deacription TODO
 * @Author ywz
 * @Date 2020/4/22 16:06
 * @Version 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class WmsCostCenterReturnServiceImpl implements WmsCostCenterReturnService {


    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtCostcenterRepository mtCostcenterRepository;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingMapper;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtMaterialRepository materialRepository;

    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;

    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;

    @Autowired
    private WmsMaterialLotDocRelRepository wmsMaterialLotDocRelRepository;

    @Autowired
    private MtInstructionMapper mtInstructionMapper;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Autowired
    private WmsPutInStorageTaskRepository wmsPutInStorageTaskRepository;

    @Autowired
    private MtInstructionDocMapper mtInstructionDocMapper;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MessageClient messageClient;
    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private WmsCommonApiService commonApiService;

    @Autowired
    private WmsCostCenterReturnMapper wmsCostCenterReturnMapper;

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;

    @Autowired
    private MtInstructionRepository mtLogisticInstructionService;

    @Autowired
    private QmsMaterialInspExemptRepository qmsMaterialInspExemptRepository;

    @Autowired
    private MtInstructionActualDetailRepository mtInstructionActualDetailRepository;
    @Autowired
    private MtSupplierRepository supplierRepository;
    @Autowired
    private QmsIqcHeaderRepository qmsIqcHeaderRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private WmsDocPrivilegeRepository wmsDocPrivilegeRepository;

    @Autowired
    private MtPfepInventoryRepository mtPfepInventoryRepository;

    @Autowired
    private WmsPfepInertiaLocatorRepository wmsPfepInertiaLocatorRepository;

    @Autowired
    private ItfLightTaskIfaceService itfLightTaskIfaceService;

    @Autowired
    private ItfLightTaskIfaceMapper itfLightTaskIfaceMapper;

    @Autowired
    private ItfLightTaskIfaceRepository itfLightTaskIfaceRepository;

    /**
     * @param tenantId
     * @param instructionDocNum
     * @return com.ruike.wms.api.dto.WmsMaterialReturnScanDTO
     * @Description ????????????????????????????????????
     * @Author wenzhang.yu
     * @Date 2020/4/20 11:58
     * @version 1.0
     **/
    @Override
    @ProcessLovValue
    @Transactional
    public WmsMaterialReturnScanDTO returnCodeScan(Long tenantId, String instructionDocNum,String instructionDocId) {
        if(StringUtils.isNotBlank(instructionDocId)){
            ItfLightTaskIface s = new ItfLightTaskIface();
            s.setTenantId(tenantId);
            s.setDocId(instructionDocId);
            List<ItfLightTaskIface> itfLightTaskIfaceList1 = itfLightTaskIfaceRepository.select(s);
            List<ItfLightTaskIface> itfLightTaskIfaceList2 = itfLightTaskIfaceList1.stream().filter(item ->item.getTaskStatus().equals("ON")).collect(toList());
            if(CollectionUtils.isNotEmpty(itfLightTaskIfaceList2)){
                List<ItfLightTaskIfaceDTO> itfLightTaskIfaceDTOList = new ArrayList<>();
                for(ItfLightTaskIface itfLightTaskIface:itfLightTaskIfaceList2){
                    ItfLightTaskIfaceDTO lightTaskIfaceDTO = new ItfLightTaskIfaceDTO();
                    lightTaskIfaceDTO.setTaskNum(itfLightTaskIface.getTaskNum());
                    lightTaskIfaceDTO.setTaskStatus("OFF");
                    itfLightTaskIfaceDTOList.add(lightTaskIfaceDTO);
                }
                itfLightTaskIfaceService.itfLightTaskIface(tenantId,itfLightTaskIfaceDTOList);
            }
        }
        if (StringUtils.isEmpty(instructionDocNum)) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", "WMS", "instructionDocNum", ""));
        }

        //?????????
        WmsMaterialReturnScanDTO wmsMaterialReturnScanDTO = new WmsMaterialReturnScanDTO();

        //??????API?????????????????????????????????????????????????????????ID
        MtInstructionDocVO4 mtLogisticInstructionDoc = new MtInstructionDocVO4();
        mtLogisticInstructionDoc.setInstructionDocNum(instructionDocNum);
        List<String> docIds = mtInstructionDocRepository.propertyLimitInstructionDocQuery(tenantId, mtLogisticInstructionDoc);
        if (CollectionUtils.isEmpty(docIds)) {
            throw new MtException("WMS_COST_CENTER_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0002", "WMS", instructionDocNum, ""));
        }

        //??????API???????????????????????????ID???????????????????????????????????????
        List<MtInstructionDoc> mtInstructionDocs = mtInstructionDocRepository.instructionDocPropertyBatchGet(tenantId, docIds);
        MtInstructionDoc mtInstructionDoc = mtInstructionDocs.get(0);

        //???????????????????????????????????????
        if (!WmsConstant.InspectionDocType.CCA_RETURN.equals(mtInstructionDoc.getInstructionDocType())) {
            throw new MtException("WMS_COST_CENTER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0020", "WMS", instructionDocNum));
        }
        //?????????????????????NEW-????????????EXECUTE-?????????
        if (!(WmsConstant.InstructionStatus.NEW.equals(mtInstructionDoc.getInstructionDocStatus()) || WmsConstant.InstructionStatus.EXECUTE.equals(mtInstructionDoc.getInstructionDocStatus()))) {
            throw new MtException("WMS_COST_CENTER_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0004", "WMS", instructionDocNum));
        }

        wmsMaterialReturnScanDTO.setInstructionDocId(mtInstructionDoc.getInstructionDocId());

        //????????????
        wmsMaterialReturnScanDTO.setInstructionDocStatus(mtInstructionDoc.getInstructionDocStatus());

        //????????????
        wmsMaterialReturnScanDTO.setInstructionDocType(mtInstructionDoc.getInstructionDocType());

        //??????
        MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, mtInstructionDoc.getSiteId());
        wmsMaterialReturnScanDTO.setSiteId(mtModSite.getSiteId());
        wmsMaterialReturnScanDTO.setSiteCode(mtModSite.getSiteCode());
        wmsMaterialReturnScanDTO.setSiteName(mtModSite.getSiteName());

        //??????
        wmsMaterialReturnScanDTO.setRemark(mtInstructionDoc.getRemark());

        //????????????/????????????
        //??????????????????
        WmsMaterialReturnScanTypeDTO doc = wmsCostCenterReturnMapper.selectDocType(tenantId, instructionDocNum);
        if (!Objects.isNull(doc)) {
            wmsMaterialReturnScanDTO.setSettleAccounts(doc.getSettleAccounts());
            wmsMaterialReturnScanDTO.setCostCenterCode(doc.getCostCenterCode());
        }


        //?????????????????????????????????????????????????????????????????????????????????
        // ???????????????ID???????????????ID??????
        //??????????????????
        MtInstructionVO10 mtInstruction = new MtInstructionVO10();
        mtInstruction.setSourceDocId(docIds.get(0));
        List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstruction);

        //???????????????????????????
        if (CollectionUtils.isEmpty(instructionIdList)) {
            return wmsMaterialReturnScanDTO;
        }

        List<WmsMaterialReturnScanLineDTO> listTemp = new ArrayList<>();

        List<ItfLightTaskIfaceDTO> itfLightTaskIfaceDTOS = new ArrayList<>();
        for (String instructionId : instructionIdList) {
            MtInstructionVO11 mtInstructionVO11 = mtInstructionRepository.instructionLimitInstructionAndActualQuery(tenantId, instructionId);
            WmsMaterialReturnScanLineDTO lineReturnDTO = new WmsMaterialReturnScanLineDTO();
            QmsIqcHeader qmsIqcHeader = new QmsIqcHeader();
            qmsIqcHeader.setDocLineId(instructionId);
            qmsIqcHeader = qmsIqcHeaderRepository.selectOne(qmsIqcHeader);
            if (!Objects.isNull(qmsIqcHeader)) {
                if (!HmeConstants.ConstantValue.OK.equals(qmsIqcHeader.getInspectionResult())) {
                    lineReturnDTO.setNeedIqc(1);
                }
            }
            lineReturnDTO.setInstructionId(mtInstructionVO11.getInstructionId());

            //?????????
            lineReturnDTO.setInstructionStatus(mtInstructionVO11.getInstructionStatus());
            List<LovValueDTO> lovList = commonApiService.queryLovValueList(tenantId, "WMS.COST_CENTER_DOCUMENT_LINE.STATUS", mtInstructionVO11.getInstructionStatus());
            if (CollectionUtils.isNotEmpty(lovList)) {
                lineReturnDTO.setLineStatusMeaning(lovList.get(0).getMeaning());
            }
            //?????????
            lineReturnDTO.setSiteId(mtInstructionVO11.getSiteId());

            //??????????????????
            if (StringUtils.isNotEmpty(mtInstructionVO11.getMaterialId())) {
                MtMaterial mtMaterial = new MtMaterial();
                mtMaterial.setMaterialId(mtInstructionVO11.getMaterialId());
                mtMaterial = materialRepository.selectByPrimaryKey(mtMaterial);
                lineReturnDTO.setMaterialId(mtInstructionVO11.getMaterialId());
                lineReturnDTO.setMaterialCode(mtMaterial.getMaterialCode());
                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
                if (!Objects.isNull(mtUom)) {
                    lineReturnDTO.setUomCode(mtUom.getUomCode());
                }
                lineReturnDTO.setMaterialName(mtMaterial.getMaterialName());
            }


            //????????????
            lineReturnDTO.setQuantity(BigDecimal.valueOf(mtInstructionVO11.getQuantity()));

            //????????????
            if (StringUtils.isNotEmpty(mtInstructionVO11.getToLocatorId())) {
                lineReturnDTO.setToWarehouseId(mtInstructionVO11.getToLocatorId());
                MtModLocatorVO7 mtModLocatorVO7 = new MtModLocatorVO7();
                mtModLocatorVO7.setLocatorId(mtInstructionVO11.getToLocatorId());
                List<MtModLocatorVO8> mtModLocatorVO8s1 = mtModLocatorRepository.propertyLimitLocatorPropertyQuery(tenantId, mtModLocatorVO7);
                if (CollectionUtils.isNotEmpty(mtModLocatorVO8s1)) {
                    lineReturnDTO.setToWarehouseCode(mtModLocatorVO8s1.get(0).getLocatorCode());
                }
            }

            lineReturnDTO.setExecuteQty(BigDecimal.valueOf(0));
            int codeQty = 0;
            //????????????
            if (CollectionUtils.isNotEmpty(mtInstructionVO11.getInstructionActualLines())) {
                lineReturnDTO.setExecuteQty(BigDecimal.valueOf(mtInstructionVO11.getInstructionActualLines().stream().filter(x -> x.getActualQty() != null).mapToDouble(MtInstructionVO11.InstructionActualLine::getActualQty).sum()));
            }
            List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository.instructionLimitActualDetailQuery(tenantId, instructionId);
            codeQty = mtInstructionActualDetails.size();
            lineReturnDTO.setCodeQty(codeQty);
            //??????????????????
            List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
                    new MtExtendVO1("mt_instruction_attr", Collections.singletonList(mtInstructionVO11.getInstructionId()), "MATERIAL_VERSION", "TO_LOCATOR_ID", "EXCESS_SETTING", "EXCESS_VALUE"));
            for (MtExtendAttrVO1 extendAttr :
                    mtExtendAttrVO1s) {
                //????????????
                if ("MATERIAL_VERSION".equals(extendAttr.getAttrName())) {
                    lineReturnDTO.setMaterialVersion(extendAttr.getAttrValue());
                }
                if ("TO_LOCATOR_ID".equals(extendAttr.getAttrName())) {
                    if (StringUtils.isNotEmpty(extendAttr.getAttrValue())) {
                        lineReturnDTO.setToLocatorId(extendAttr.getAttrValue());
                        MtModLocatorVO7 mtModLocatorVO71 = new MtModLocatorVO7();
                        mtModLocatorVO71.setLocatorId(extendAttr.getAttrValue());
                        List<MtModLocatorVO8> mtModLocatorVO8s = mtModLocatorRepository.propertyLimitLocatorPropertyQuery(tenantId, mtModLocatorVO71);
                        if (CollectionUtils.isNotEmpty(mtModLocatorVO8s)) {
                            lineReturnDTO.setToLocatorCode(mtModLocatorVO8s.get(0).getLocatorCode());
                            lineReturnDTO.setToLocatorName(mtModLocatorVO8s.get(0).getLocatorName());
                        }
                    }
                }
                //????????????
                if ("EXCESS_SETTING".equals(extendAttr.getAttrName())) {
                    lineReturnDTO.setExcessSetting(extendAttr.getAttrValue());
                }
                //?????????
                if ("EXCESS_VALUE".equals(extendAttr.getAttrName())) {
                    lineReturnDTO.setExcessValue(extendAttr.getAttrValue());
                }
            }
            //??????????????????
            String locatorRecomMode = mtInstructionMapper.getMode(tenantId,
                    mtInstructionDoc.getSiteId(),
                    mtInstructionVO11.getMaterialId(),
                    mtInstructionVO11.getToLocatorId());
            if(StringUtils.isBlank(locatorRecomMode)){
                lineReturnDTO.setRecommendLocatorCode("");
            }else if(locatorRecomMode.equals("POSITION")){
                MtPfepInventoryVO mtPfepInventoryVO = new MtPfepInventoryVO();
                mtPfepInventoryVO.setMaterialId(mtInstructionVO11.getMaterialId());
                mtPfepInventoryVO.setSiteId(mtInstructionDoc.getSiteId());
                mtPfepInventoryVO.setOrganizationType("LOCATOR");
                mtPfepInventoryVO.setOrganizationId(mtInstructionVO11.getToLocatorId());
                MtPfepInventory mtPfepInventory = mtPfepInventoryRepository.pfepInventoryGet(tenantId,mtPfepInventoryVO);
                if(StringUtils.isBlank(mtPfepInventory.getStockLocatorId())){
                    lineReturnDTO.setRecommendLocatorCode("");
                }else{
                    lineReturnDTO.setRecommendLocatorCode(mtModLocatorRepository.selectByPrimaryKey(mtPfepInventory.getStockLocatorId()).getLocatorCode());
                }
            }else if(locatorRecomMode.equals("INERTIA")){
                WmsPfepInertiaLocator wmsPfepInertiaLocator = new WmsPfepInertiaLocator();
                wmsPfepInertiaLocator.setTenantId(tenantId);
                wmsPfepInertiaLocator.setMaterialId(mtInstructionVO11.getMaterialId());
                wmsPfepInertiaLocator.setSiteId(mtInstructionDoc.getSiteId());
                wmsPfepInertiaLocator.setWarehouseId(mtInstructionVO11.getToLocatorId());
                List<WmsPfepInertiaLocator> wmsPfepInertiaLocatorList = wmsPfepInertiaLocatorRepository.select(wmsPfepInertiaLocator);
                if(wmsPfepInertiaLocatorList.size() !=0){
                    lineReturnDTO.setRecommendLocatorCode(mtModLocatorRepository.selectByPrimaryKey(wmsPfepInertiaLocatorList.get(0).getLocatorId()).getLocatorCode());
                }else{
                    lineReturnDTO.setRecommendLocatorCode("");
                }
            }
            if(StringUtils.isNotBlank(lineReturnDTO.getRecommendLocatorCode())){
                //?????????????????????????????????ITF.LOCATOR_LABEL_ID???
                List<LovValueDTO> LovValueDTOs = lovAdapter.queryLovValue("ITF.LOCATOR_LABEL_ID", tenantId);
                List<LovValueDTO> LovValueDTOList = LovValueDTOs.stream().filter(item ->item.getValue().equals(lineReturnDTO.getRecommendLocatorCode())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(LovValueDTOList)){
                    ItfLightTaskIfaceDTO itfLightTaskIfaceDTO = new ItfLightTaskIfaceDTO();
                    itfLightTaskIfaceDTO.setInstructionDocId(wmsMaterialReturnScanDTO.getInstructionDocId());
                    itfLightTaskIfaceDTO.setInstructionId(lineReturnDTO.getInstructionId());
                    itfLightTaskIfaceDTO.setLocatorCode(lineReturnDTO.getRecommendLocatorCode());
                    itfLightTaskIfaceDTO.setTaskType("IN");
                    itfLightTaskIfaceDTOS.add(itfLightTaskIfaceDTO);
                }
            }
            listTemp.add(lineReturnDTO);
        }
        Long userId = DetailsHelper.getUserDetails().getUserId();
        List<String> ToWarehouseIdList = listTemp.stream().map(item -> item.getToWarehouseId()).collect(Collectors.toList());
        Boolean flag = wmsDocPrivilegeRepository.existsWarehousePrivileged(tenantId,WmsWarehousePrivilegeBatchQueryDTO.builder()
                        .userId(userId)
                        .locatorIdList(ToWarehouseIdList)
                        .docType(WmsConstant.DocType.CCA_RETURN)
                        .operationType(WmsConstant.OperationType.EXECUTE)
                        .build());
        if(!flag){
            MtModLocator locator = mtModLocatorRepository.selectByPrimaryKey(ToWarehouseIdList.get(0));
            String locatorCode = Objects.isNull(locator) ? "" : locator.getLocatorCode();
            WmsCommonUtils.processValidateMessage(tenantId, "WMS_COST_CENTER_0067", WMS, locatorCode);
        }
        if(CollectionUtils.isNotEmpty(itfLightTaskIfaceDTOS)){
            List<ItfLightTaskIfaceVO> itfLightTaskIfaceVOS = itfLightTaskIfaceService.itfLightTaskIface(tenantId,itfLightTaskIfaceDTOS);
            if(CollectionUtils.isNotEmpty(itfLightTaskIfaceVOS)){
                List<String> taskNumList = itfLightTaskIfaceVOS.stream().map(ItfLightTaskIfaceVO::getTaskNum).collect(Collectors.toList());
                List<ItfLightTaskIface> itfLightTaskIfaces = itfLightTaskIfaceMapper.selectByTaskNum(tenantId,taskNumList);
                for(WmsMaterialReturnScanLineDTO wmsMaterialReturnScanLineDTO:listTemp){
                    List<ItfLightTaskIface> itfLightTaskIfaceList = itfLightTaskIfaces.stream()
                            .filter(item ->item.getDocLineId().equals(wmsMaterialReturnScanLineDTO.getInstructionId())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(itfLightTaskIfaceList)){
                        wmsMaterialReturnScanLineDTO.setTaskNum(itfLightTaskIfaceList.get(0).getTaskNum());
                        wmsMaterialReturnScanLineDTO.setStatus(itfLightTaskIfaceList.get(0).getStatus());
                        wmsMaterialReturnScanLineDTO.setTaskStatus(itfLightTaskIfaceList.get(0).getTaskStatus());
                    }
                }
            }
        }
        wmsMaterialReturnScanDTO.setDocLineList(listTemp);
        return wmsMaterialReturnScanDTO;
    }

    /**
     * @param tenantId
     * @param barCode
     * @return com.ruike.wms.api.dto.WmsMaterialReturnScanDTO2
     * @Description ?????????????????????????????????
     * @Author wenzhang.yu
     * @Date 2020/4/20 16:11
     * @version 1.0
     **/
    @Override
    @ProcessLovValue
    public WmsMaterialReturnScanDTO2 returnMaterialCodeScan(Long tenantId, String barCode, String instructionDocId, String locatorCode, List<WmsMaterialReturnScanDTO2> barCodeList, List<WmsMaterialReturnScanLineDTO> docLineList) {
        if (StringUtils.isEmpty(barCode)) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", "WMS", "barCode", ""));
        }

        WmsMaterialReturnScanDTO2 wmsMaterialReturnScanDTO2 = new WmsMaterialReturnScanDTO2();
        MtMaterialLotVO3 materialLotVo3 = new MtMaterialLotVO3();
        materialLotVo3.setMaterialLotCode(barCode);
        List<String> materialLotIds =
                mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotVo3);
        if (CollectionUtils.isNotEmpty(materialLotIds)) {
            wmsMaterialReturnScanDTO2.setMaterialLotId(materialLotIds.get(0));
        } else {
            throw new MtException("WMS_COST_CENTER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0006", "WMS", barCode));
        }
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotIds.get(0));
        if (WmsConstant.CONSTANT_Y.equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("WMS_COST_CENTER_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0038", "WMS"));
        }

        // ???????????????????????????????????? ???????????????
        MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
        mtContLoadDtlVO5.setLoadObjectType("MATERIAL_LOT");
        mtContLoadDtlVO5.setLoadObjectId(mtMaterialLot.getMaterialLotId());
        mtContLoadDtlVO5.setTopLevelFlag(HmeConstants.ConstantValue.YES);
        List<String> containerIds = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, mtContLoadDtlVO5);
        if (CollectionUtils.isNotEmpty(containerIds)) {
            MtContainer mtContainer = mtContainerRepository.selectByPrimaryKey(containerIds.get(0));
            throw new MtException("WMS_COST_CENTER_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0055", WmsConstant.ConstantValue.WMS, barCode, mtContainer != null ? mtContainer.getContainerCode() : ""));
        }

        // ????????????????????????
        if (StringUtils.equals(mtMaterialLot.getStocktakeFlag(), WmsConstant.CONSTANT_Y)) {
            throw new MtException("WMS_COST_CENTER_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0034", WmsConstant.ConstantValue.WMS, barCode));
        }

        //???????????????????????????
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName(WmsConstant.AttrTable.MT_MATERIAL_LOT_ATTR);
        mtExtendVO1.setKeyIdList(Collections.singletonList(mtMaterialLot.getMaterialLotId()));
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 extend1 = new MtExtendVO5();
        extend1.setAttrName(WmsConstant.MaterialLotAttr.SO_NUM);
        attrs.add(extend1);
        MtExtendVO5 extend2 = new MtExtendVO5();
        extend2.setAttrName(WmsConstant.MaterialLotAttr.SO_LINE_NUM);
        attrs.add(extend2);
        MtExtendVO5 extend3 = new MtExtendVO5();
        extend3.setAttrName(HmeConstants.ExtendAttr.SAP_ACCOUNT_FLAG);
        attrs.add(extend3);
        MtExtendVO5 extend4 = new MtExtendVO5();
        extend4.setAttrName(HmeConstants.ExtendAttr.MF_FLAG);
        attrs.add(extend4);
        mtExtendVO1.setAttrs(attrs);
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        if (CollectionUtils.isNotEmpty(extendAttrList)) {
            extendAttrList.forEach(item -> {
                if(item.getAttrName().equals(HmeConstants.ExtendAttr.SAP_ACCOUNT_FLAG)){
                    if(HmeConstants.ConstantValue.NO.equals(item.getAttrValue())) {
                        throw new MtException("HME_MATERIAL_LOT_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_MATERIAL_LOT_0003", "HME", barCode));
                    }
                } else if (item.getAttrName().equals(HmeConstants.ExtendAttr.MF_FLAG)) {
                    if(HmeConstants.ConstantValue.YES.equals(item.getAttrValue())) {
                        // ??????${1}??????????????????,?????????!
                        throw new MtException("WMS_COST_CENTER_0100", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0100", "WMS", barCode));
                    }
                } else{
                    if (StringUtils.isNotBlank(item.getAttrValue())) {
                        throw new MtException("WMS_COST_CENTER_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0054", WmsConstant.ConstantValue.WMS));
                    }
                }
            });
        }
        //????????????
        if (StringUtils.isNotEmpty(mtMaterialLot.getPrimaryUomId())) {
            MtUomVO mtUomVO = mtUomRepository.uomPropertyGet(tenantId, mtMaterialLot.getPrimaryUomId());
            if (!Objects.isNull(mtUomVO)) {
                wmsMaterialReturnScanDTO2.setUomCode(mtUomVO.getUomCode());
            }
        }

        //??????????????????
        if (StringUtils.isNotEmpty(mtMaterialLot.getMaterialId())) {
            MtMaterial mtMaterial = new MtMaterial();
            mtMaterial.setMaterialId(mtMaterialLot.getMaterialId());
            mtMaterial = materialRepository.selectByPrimaryKey(mtMaterial);
            wmsMaterialReturnScanDTO2.setMaterialId(mtMaterial.getMaterialId());
            wmsMaterialReturnScanDTO2.setMaterialCode(mtMaterial.getMaterialCode());
        }

        //??????API????????????????????????
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2Temp = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2Temp.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        mtMaterialLotAttrVO2Temp.setAttrName("MATERIAL_VERSION");
        List<MtExtendAttrVO> materialLotAttrVOTemp = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2Temp);
        if (CollectionUtils.isNotEmpty(materialLotAttrVOTemp)) {
            wmsMaterialReturnScanDTO2.setMaterialVersion(materialLotAttrVOTemp.get(0).getAttrValue());
        }

        MtInstructionVO10 mtInstruction = new MtInstructionVO10();
        mtInstruction.setSourceDocId(instructionDocId);
        List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstruction);
        List<WmsMaterialReturnScanLineDTO> WmsMaterialReturnScanLineDTOList = new ArrayList<>();
        for (String instructionId : instructionIdList) {
            MtInstructionVO11 mtInstructionVO11 = mtInstructionRepository.instructionLimitInstructionAndActualQuery(tenantId, instructionId);
            WmsMaterialReturnScanLineDTO lineReturnDTO = new WmsMaterialReturnScanLineDTO();
            lineReturnDTO.setToWarehouseId(mtInstructionVO11.getToLocatorId());
            lineReturnDTO.setMaterialId(mtInstructionVO11.getMaterialId());
            lineReturnDTO.setUomId(mtInstructionVO11.getUomId());
            lineReturnDTO.setQuantity(new BigDecimal(mtInstructionVO11.getQuantity()));
            //??????????????????
            List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
                    new MtExtendVO1("mt_instruction_attr", Collections.singletonList(mtInstructionVO11.getInstructionId()), "MATERIAL_VERSION", "TO_LOCATOR_ID", "EXCESS_SETTING", "EXCESS_VALUE"));
            log.info("===========>mtExtendAttrVO1s:" + mtExtendAttrVO1s.toString());
            for (MtExtendAttrVO1 extendAttr :
                    mtExtendAttrVO1s) {
                //????????????
                if ("MATERIAL_VERSION".equals(extendAttr.getAttrName())) {
                    lineReturnDTO.setMaterialVersion(extendAttr.getAttrValue());
                }
                if ("TO_LOCATOR_ID".equals(extendAttr.getAttrName())) {
                    if (StringUtils.isNotBlank(extendAttr.getAttrValue())) {
                        lineReturnDTO.setToLocatorId(extendAttr.getAttrValue());
                        MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(extendAttr.getAttrValue());
                        if (mtModLocator != null) {
                            lineReturnDTO.setToLocatorCode(mtModLocator.getLocatorCode());
                        }
                    }
                }
                //????????????
                if ("EXCESS_SETTING".equals(extendAttr.getAttrName())) {
                    lineReturnDTO.setExcessSetting(extendAttr.getAttrValue());
                }
                //?????????
                if ("EXCESS_VALUE".equals(extendAttr.getAttrName())) {
                    lineReturnDTO.setExcessValue(extendAttr.getAttrValue());
                }
            }
            List<WmsMaterialReturnScanLineDTO> docLines = docLineList.stream().filter(item ->item.getInstructionId().equals(instructionId)).collect(toList());
            if(CollectionUtils.isNotEmpty(docLines)){
                lineReturnDTO.setStatus(docLines.get(0).getStatus());
                lineReturnDTO.setTaskNum(docLines.get(0).getTaskNum());
            }
            WmsMaterialReturnScanLineDTOList.add(lineReturnDTO);
        }
        log.info("===========>WmsMaterialReturnScanLineDTOList:" + WmsMaterialReturnScanLineDTOList.toString());
        List<WmsMaterialReturnScanLineDTO> collect = WmsMaterialReturnScanLineDTOList.stream()
                .filter(x -> x.getMaterialId().equals(mtMaterialLot.getMaterialId()) && ((x.getMaterialVersion().equals(wmsMaterialReturnScanDTO2.getMaterialVersion())) || StringUtils.isBlank(x.getMaterialVersion()))).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            //???????????????????????????????????????????????????
            if (1 == docLineList.stream()
                    .filter(x -> x.getMaterialId().equals(mtMaterialLot.getMaterialId()) && ((x.getMaterialVersion().equals(wmsMaterialReturnScanDTO2.getMaterialVersion())) || StringUtils.isBlank(x.getMaterialVersion()))).collect(Collectors.toList()).get(0).getNeedIqc()) {
                throw new MtException("WMS_COST_CENTER_0048", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0048", WmsConstant.ConstantValue.WMS, barCode));
            }
            // ?????????????????????????????????????????????????????????????????????
            long uomCount = collect.stream().filter(item ->
                    StringUtils.equals(mtMaterialLot.getPrimaryUomId(), item.getUomId())
            ).count();
            if (uomCount == 0) {
                throw new MtException("WMS_COST_CENTER_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0037", WmsConstant.ConstantValue.WMS, barCode));
            }

            //locatorCode??????(??????????????????,??????)
            if (StringUtils.isNotBlank(locatorCode)) {
                //???????????????????????????????????????
                if (StringUtils.isNotEmpty(collect.get(0).getToLocatorCode())) {
                    if (!collect.get(0).getToLocatorCode().equals(locatorCode)) {
                        throw new MtException("WMS_COST_CENTER_0039", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0039", WmsConstant.ConstantValue.WMS, locatorCode));
                    }
                } else {
                    //???????????????????????????,??????????????????????????????????????????
                    MtModLocatorVO7 mtModLocatorVO7 = new MtModLocatorVO7();
                    mtModLocatorVO7.setLocatorCode(locatorCode);
                    List<MtModLocatorVO8> mtModLocatorVO8s = mtModLocatorRepository.propertyLimitLocatorPropertyQuery(tenantId, mtModLocatorVO7);
                    if (mtModLocatorVO8s.size() > 0) {
                        if (!mtModLocatorVO8s.get(0).getParentLocatorId().equals(collect.get(0).getToWarehouseId())) {
                            throw new MtException("WMS_COST_CENTER_0040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_COST_CENTER_0040", WmsConstant.ConstantValue.WMS, locatorCode));
                        }
                    } else {
                        throw new MtException("WMS_COST_CENTER_0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0041", WmsConstant.ConstantValue.WMS, locatorCode));
                    }
                }
            }
            //????????????
            BigDecimal quantity = collect.get(0).getQuantity() != null ? collect.get(0).getQuantity() : BigDecimal.ZERO;
            //????????????
            BigDecimal executeQty = collect.get(0).getExecuteQty() != null ? collect.get(0).getExecuteQty() : BigDecimal.ZERO;
            //???????????????
            BigDecimal scannedMaterialQty = BigDecimal.ZERO;
            //?????????????????????List
            if (CollectionUtils.isNotEmpty(barCodeList)) {
                List<WmsMaterialReturnScanDTO2> scannedBarCodeList = barCodeList.stream().filter(m -> wmsMaterialReturnScanDTO2.getMaterialId().equals(m.getMaterialId())
                        && StringUtils.trimToEmpty(wmsMaterialReturnScanDTO2.getMaterialVersion()).equals(StringUtils.trimToEmpty(m.getMaterialVersion()))).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(scannedBarCodeList)) {
                    scannedMaterialQty = scannedMaterialQty.add(scannedBarCodeList.stream().collect(CollectorsUtil.summingBigDecimal(m -> Optional.ofNullable(m.getExecuteQty()).orElse(BigDecimal.ZERO))));
                }
                //?????????+?????????+????????????????????????????????????
                if (executeQty.add(scannedMaterialQty).add(new BigDecimal(mtMaterialLot.getPrimaryUomQty())).compareTo(quantity) > 0) {
                    //?????????+?????????????????????????????????
                    if (executeQty.add(scannedMaterialQty).compareTo(quantity) > 0) {
                        throw new MtException("WMS_COST_CENTER_0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0042", WmsConstant.ConstantValue.WMS, barCode, String.valueOf(executeQty.add(scannedMaterialQty)), String.valueOf(quantity)));
                        //?????????+?????????=????????????
                    } else if (executeQty.add(scannedMaterialQty).compareTo(quantity) == 0) {
                        //????????????????????????
                        if (!"M".equals(collect.get(0).getExcessSetting())) {
                            throw new MtException("WMS_COST_CENTER_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_COST_CENTER_0043", WmsConstant.ConstantValue.WMS, barCode, String.valueOf(executeQty.add(scannedMaterialQty)), String.valueOf(quantity)));
                        }
                    }
                }
            }
        } else {
            throw new MtException("WMS_COST_CENTER_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0044", WmsConstant.ConstantValue.WMS, barCode));
        }
        Long userId = DetailsHelper.getUserDetails().getUserId();
        //??????API?????????????????????
        wmsDocPrivilegeRepository.isWarehousePrivileged(tenantId,WmsWarehousePrivilegeQueryDTO.builder()
                .userId(userId)
                .locatorId(collect.get(0).getToWarehouseId())
                .docType(WmsConstant.DocType.CCA_RETURN)
                .operationType(WmsConstant.OperationType.EXECUTE).build());
        wmsMaterialReturnScanDTO2.setToLocatorCode(locatorCode);
        wmsMaterialReturnScanDTO2.setLot(mtMaterialLot.getLot());
        wmsMaterialReturnScanDTO2.setQualityStatus(mtMaterialLot.getQualityStatus());
        wmsMaterialReturnScanDTO2.setExecuteQty(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
        List<ItfLightTaskIfaceDTO> itfLightTaskIfaceDTOS = new ArrayList<>();
        if(StringUtils.isNotBlank(collect.get(0).getStatus()) && !"E".equals(collect.get(0).getStatus())){
            ItfLightTaskIfaceDTO itfLightTaskIfaceDTO = new ItfLightTaskIfaceDTO();
            itfLightTaskIfaceDTO.setTaskNum(collect.get(0).getTaskNum());
            itfLightTaskIfaceDTO.setTaskStatus("OFF");
            itfLightTaskIfaceDTOS.add(itfLightTaskIfaceDTO);
        }
        if(CollectionUtils.isNotEmpty(itfLightTaskIfaceDTOS)){
            List<ItfLightTaskIfaceVO> itfLightTaskIfaceVOS = itfLightTaskIfaceService.itfLightTaskIface(tenantId,itfLightTaskIfaceDTOS);
            if(CollectionUtils.isNotEmpty(itfLightTaskIfaceVOS)){
                List<String> taskNumList = itfLightTaskIfaceVOS.stream().map(ItfLightTaskIfaceVO::getTaskNum).collect(toList());
                List<ItfLightTaskIface> itfLightTaskIfaceList = itfLightTaskIfaceMapper.selectByTaskNum(tenantId,taskNumList);
                wmsMaterialReturnScanDTO2.setInstructionId(itfLightTaskIfaceList.get(0).getDocLineId());
                wmsMaterialReturnScanDTO2.setStatus(itfLightTaskIfaceList.get(0).getStatus());
//                wmsMaterialReturnScanDTO2.setTaskNum(itfLightTaskIfaceList.get(0).getTaskNum());
                wmsMaterialReturnScanDTO2.setTaskStatus(itfLightTaskIfaceList.get(0).getTaskStatus());
            }
        }
        return wmsMaterialReturnScanDTO2;
    }

    /**
     * @param tenantId
     * @param dto
     * @return void
     * @Description ??????
     * @Author wenzhang.yu
     * @Date 2020/4/22 16:54
     * @version 1.0
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public WmsMaterialReturnScanDTO returnCodeConfirm(Long tenantId, WmsMaterialReturnConfirmDTO dto) {

        WmsMaterialReturnScanDTO wmsMaterialReturnScanDTO = new WmsMaterialReturnScanDTO();
        List<WmsMaterialReturnScanLineDTO> WmsMaterialReturnScanLineDTOList = new ArrayList<>();
        //????????????????????????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "COSTCENTER_RETURN_REQUISTION_EXECUTE");
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.instructionDocPropertyGet(tenantId, dto.getInstructionDocId());

        //??????api??????????????????
        MtNumrangeVO2 mtNumrangeVO2 = new MtNumrangeVO2();
        mtNumrangeVO2.setObjectCode("RECEIPT_BATCH");
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // ??????????????????????????????
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        mtNumrangeVO2.setSiteId(siteId);
        Map<String, String> map = new HashMap<>(2);
        MtModSiteVO6 mtModSiteVO6 = new MtModSiteVO6();
        mtModSiteVO6.setSiteId(siteId);
        map.put("SITECODE", mtModSiteRepository.propertyLimitSitePropertyQuery(tenantId, mtModSiteVO6).get(0).getSiteCode());
        mtNumrangeVO2.setCallObjectCodeList(map);
        MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrangeVO2);


        List<WmsMaterialReturnScanLineDTO> lineList = dto.getLineList();
        List<WmsMaterialReturnConfirmDTO2> materialLotList = dto.getMaterialLotList();
        List<String> materialLotIdList = materialLotList.stream().map(x -> x.getMaterialLotId()).collect(Collectors.toList());

        //???????????????????????????
        List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
        List<WmsMaterialReturnConfirmDTO2> wmsMaterialReturnConfirmDTO2List = new ArrayList<>();

        //???????????????????????????
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName(WmsConstant.AttrTable.MT_MATERIAL_LOT_ATTR);
        mtExtendVO1.setKeyIdList(materialLotIdList);
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 extend1 = new MtExtendVO5();
        extend1.setAttrName(WmsConstant.MaterialLotAttr.SO_NUM);
        attrs.add(extend1);
        MtExtendVO5 extend2 = new MtExtendVO5();
        extend2.setAttrName(WmsConstant.MaterialLotAttr.SO_LINE_NUM);
        attrs.add(extend2);
        mtExtendVO1.setAttrs(attrs);
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        Map<String, List<MtExtendAttrVO1>> materialLotAttrMap =
                extendAttrList.stream().collect(Collectors.groupingBy(MtExtendAttrVO1::getKeyId));

        //???????????????????????????
        MtExtendVO moveAttr = new MtExtendVO();
        moveAttr.setKeyId(dto.getInstructionDocId());
        moveAttr.setAttrName("MOVE_TYPE");
        moveAttr.setTableName("mt_instruction_doc_attr");
        List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, moveAttr);
        String docMoveType = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";

        //??????????????????
        String transactionTypeCode = "";
        switch (docMoveType) {
            case "201":
                transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_COST_CENTER_I;
                break;
            case "Z01":
                transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_INSDID_ORDER_RD_I;
                break;
            case "Z07":
                transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_INSDID_ORDER_E_I;
                break;
            case "Z05":
                transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_INSDID_ORDER_S_I;
                break;
            case "202":
                transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_COST_CENTER_R;
                break;
            case "Z02":
                transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_INSDID_ORDER_RD_R;
                break;
            case "Z08":
                transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_INSDID_ORDER_E_R;
                break;
            case "Z06":
                transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_INSDID_ORDER_S_R;
                break;
            default:
                break;
        }
        if(StringUtils.isBlank(transactionTypeCode)){
            throw new MtException("WMS_COST_CENTER_0070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0070", "WMS",mtInstructionDoc.getInstructionDocNum()));
        }
        //??????????????????
        String moveType = wmsCostCenterReturnMapper.queryMoveTypeByTransactionTypeCode(tenantId, transactionTypeCode);

        //????????????????????????Id
        for (MtMaterialLot mtMaterialLot : mtMaterialLotList) {
            WmsMaterialReturnConfirmDTO2 wmsMaterialReturnConfirmDTO2 = new WmsMaterialReturnConfirmDTO2();
            BeanUtils.copyProperties(mtMaterialLot, wmsMaterialReturnConfirmDTO2);

            List<WmsMaterialReturnConfirmDTO2> wmsMaterialReturnConfirmDTO2Stream = materialLotList.stream().filter(x -> x.getMaterialLotId().equals(mtMaterialLot.getMaterialLotId())).collect(Collectors.toList());

            wmsMaterialReturnConfirmDTO2.setMaterialVersion(wmsMaterialReturnConfirmDTO2Stream.get(0).getMaterialVersion());
            wmsMaterialReturnConfirmDTO2.setPrimaryUomCode(wmsMaterialReturnConfirmDTO2Stream.get(0).getPrimaryUomCode());
            wmsMaterialReturnConfirmDTO2.setToLocatorCode(wmsMaterialReturnConfirmDTO2Stream.get(0).getToLocatorCode());
            String locatorId = "";
            if (StringUtils.isNotBlank(wmsMaterialReturnConfirmDTO2Stream.get(0).getLocatorId())) {
                locatorId = wmsMaterialReturnConfirmDTO2Stream.get(0).getLocatorId();
            } else {
                MtModLocator mtModLocator = new MtModLocator();
                mtModLocator.setLocatorCode(wmsMaterialReturnConfirmDTO2Stream.get(0).getToLocatorCode());
                mtModLocator = mtModLocatorRepository.selectOne(mtModLocator);
                if (!Objects.isNull(mtModLocator)) {
                    locatorId = mtModLocator.getLocatorId();
                }
            }
            wmsMaterialReturnConfirmDTO2.setLocatorId(locatorId);
            wmsMaterialReturnConfirmDTO2.setExecuteQty(wmsMaterialReturnConfirmDTO2Stream.get(0).getExecuteQty());

            wmsMaterialReturnConfirmDTO2List.add(wmsMaterialReturnConfirmDTO2);
        }
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        for (WmsMaterialReturnScanLineDTO line : lineList) {
            // ?????????????????????????????????????????????
            List<WmsMaterialReturnConfirmDTO2> lotDtoList = wmsMaterialReturnConfirmDTO2List.stream().filter(item -> line.getMaterialId().equals(item.getMaterialId()) &&
                    ((StringUtils.isEmpty(line.getMaterialVersion()) && StringUtils.isEmpty(item.getMaterialVersion()))
                            || line.getMaterialVersion().equals(item.getMaterialVersion()) || StringUtils.isEmpty(line.getMaterialVersion()))).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(lotDtoList)) {
                continue;
            }
            //??????
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventRequestId(eventRequestId);
            eventCreateVO.setEventTypeCode("COSTCENTER_RETURN_EXECUTE");
            eventCreateVO.setLocatorId(line.getToWarehouseId());
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            MtInstruction docLineTemp = mtInstructionRepository.instructionPropertyGet(tenantId, line.getInstructionId());
            //????????????(????????????)

            //4.??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            MtInstructionVO3 logisticInstructionVO3 = new MtInstructionVO3();
            logisticInstructionVO3.setInstructionId(line.getInstructionId());
            logisticInstructionVO3.setEventRequestId(eventRequestId);

            List<MtInstructionVO3.MaterialLotList> list = new ArrayList<>();


            BigDecimal materialQty = lotDtoList.stream().collect(CollectorsUtil.summingBigDecimal(m -> Optional.ofNullable(m.getExecuteQty()).orElse(BigDecimal.ZERO)));
            materialQty = materialQty != null ? materialQty : BigDecimal.ZERO;
            if (materialQty.add(line.getExecuteQty()).compareTo(line.getQuantity()) > 0) {
                if (!"M".equals(line.getExcessSetting())) {
                    throw new MtException("WMS_COST_CENTER_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0045", WmsConstant.ConstantValue.WMS, line.getMaterialName()));
                }
            }
            double executeQty = 0d;
            for (WmsMaterialReturnConfirmDTO2 lotDto : lotDtoList) {
                //1.??????????????????
                WmsObjectTransactionRequestVO wmsObjectTransactionRequestVO = new WmsObjectTransactionRequestVO();
                wmsObjectTransactionRequestVO.setEventId(eventId);
                wmsObjectTransactionRequestVO.setMaterialLotId(lotDto.getMaterialLotId());
                wmsObjectTransactionRequestVO.setMaterialId(line.getMaterialId());
                wmsObjectTransactionRequestVO.setTransactionQty(lotDto.getExecuteQty());
                wmsObjectTransactionRequestVO.setTransactionUom(lotDto.getPrimaryUomCode());
                wmsObjectTransactionRequestVO.setTransactionTime(new Date());
                wmsObjectTransactionRequestVO.setTransactionReasonCode("??????????????????");
                wmsObjectTransactionRequestVO.setPlantId(line.getSiteId());
                wmsObjectTransactionRequestVO.setWarehouseId(line.getToWarehouseId());
                wmsObjectTransactionRequestVO.setLocatorId(lotDto.getToLocatorId());
                if (WmsConstant.SettleAccounts.COST_CENTER.equals(dto.getSettleAccounts())) {
                    wmsObjectTransactionRequestVO.setCostCenterCode(dto.getCostCenterCode());
                } else if (WmsConstant.SettleAccounts.INTERNAL_ORDER.equals(dto.getSettleAccounts())) {
                    wmsObjectTransactionRequestVO.setInsideOrder(dto.getCostCenterCode());
                }
                wmsObjectTransactionRequestVO.setTransactionTypeCode(transactionTypeCode);
                wmsObjectTransactionRequestVO.setMoveType(moveType);
                wmsObjectTransactionRequestVO.setSourceDocType(dto.getInstructionDocType());
                wmsObjectTransactionRequestVO.setSourceDocId(dto.getInstructionDocId());
                wmsObjectTransactionRequestVO.setSourceDocLineId(line.getInstructionId());
                wmsObjectTransactionRequestVO.setMergeFlag(WmsConstant.CONSTANT_N);
                wmsObjectTransactionRequestVO.setMaterialCode(line.getMaterialCode());
                MtModSite ms = mtModSiteRepository.selectByPrimaryKey(line.getSiteId());
                wmsObjectTransactionRequestVO.setPlantCode(ms.getSiteCode());
                wmsObjectTransactionRequestVO.setBarcode(lotDto.getMaterialLotCode());

                wmsObjectTransactionRequestVO.setLotNumber(mtNumrangeVO5.getNumber());
                wmsObjectTransactionRequestVO.setSourceDocNum(mtInstructionDoc.getInstructionDocNum());
                wmsObjectTransactionRequestVO.setSourceDocLineNum(docLineTemp.getInstructionNum());
                wmsObjectTransactionRequestVO.setLocatorId(lotDto.getLocatorId());
                wmsObjectTransactionRequestVO.setTransactionUom(line.getUomCode());

                if (materialLotAttrMap.containsKey(lotDto.getMaterialLotId())) {
                    List<MtExtendAttrVO1> extendAttrs = materialLotAttrMap.get(lotDto.getMaterialLotId());
                    for (MtExtendAttrVO1 extendAttr : extendAttrs) {
                        if (WmsConstant.MaterialLotAttr.SO_NUM.equals(extendAttr.getAttrName())) {
                            wmsObjectTransactionRequestVO.setSoNum(extendAttr.getAttrValue());
                        } else if (WmsConstant.MaterialLotAttr.SO_LINE_NUM.equals(extendAttr.getAttrName())) {
                            wmsObjectTransactionRequestVO.setSoLineNum(extendAttr.getAttrValue());
                        }
                    }
                }
                objectTransactionRequestList.add(wmsObjectTransactionRequestVO);

                //2.????????????
                MtMaterialLotVO13 mtMaterialLotVO13 = mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                    setMaterialLotId(lotDto.getMaterialLotId());
                    setEventId(eventId);
                    setEnableFlag(HmeConstants.ConstantValue.YES);
                    setLocatorId(lotDto.getLocatorId());
                    setPrimaryUomQty(lotDto.getExecuteQty().doubleValue());
                    setLot(mtNumrangeVO5.getNumber());
                    setQualityStatus(HmeConstants.ConstantValue.OK);
                }}, WmsConstant.CONSTANT_N);
                //??????????????????
                List<MtExtendAttrDTO3> materialLotAttrList = new ArrayList<>();
                MtExtendAttrDTO3 materialLotAttr = new MtExtendAttrDTO3();
                materialLotAttr.setAttrName("STATUS");
                materialLotAttr.setAttrValue("INSTOCK");
                materialLotAttrList.add(materialLotAttr);
                mtExtendSettingsService.attrSave(tenantId, "mt_material_lot_attr", lotDto.getMaterialLotId(),
                        eventId, materialLotAttrList);

                MtMaterialLot materialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, mtMaterialLotVO13.getMaterialLotId());

                // 3.?????????????????????
                //??????????????????
                MtInvOnhandQuantityVO9 condition = new MtInvOnhandQuantityVO9();
                condition.setSiteId(line.getSiteId());
                condition.setLocatorId(lotDto.getLocatorId());
                condition.setMaterialId(materialLot.getMaterialId());
                condition.setLotCode(materialLot.getLot());
                condition.setChangeQuantity(lotDto.getExecuteQty().doubleValue());
                condition.setEventId(eventId);
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, condition);


                MtInstructionVO3.MaterialLotList materialLotList1 = new MtInstructionVO3.MaterialLotList();
                materialLotList1.setMaterialLotId(materialLot.getMaterialLotId());
                materialLotList1.setQty(lotDto.getExecuteQty().doubleValue());
                materialLotList1.setContainerId(materialLot.getCurrentContainerId());
                materialLotList1.setUomId(materialLot.getPrimaryUomId());
                materialLotList1.setToLocatorId(line.getToWarehouseId());
                list.add(materialLotList1);
                executeQty += lotDto.getExecuteQty().doubleValue();
            }

            logisticInstructionVO3.setMaterialLotMessageList(list);
            mtLogisticInstructionService.instructionExecute(tenantId, logisticInstructionVO3);

            // 5???????????????????????????????????????
            MtInstruction mtInstruction = mtInstructionMapper.selectByPrimaryKey(line.getInstructionId());
//            if (line.getQuantity().compareTo(BigDecimal.valueOf(executeQty).add(line.getExecuteQty())) == 0) {
//                mtInstruction.setInstructionStatus("COMPLETE");
//            }
            if (BigDecimal.valueOf(executeQty).add(line.getExecuteQty()).compareTo(line.getQuantity()) >= 0) {
                mtInstruction.setInstructionStatus("COMPLETE");
            } else if (BigDecimal.valueOf(executeQty).add(line.getExecuteQty()).compareTo(BigDecimal.ZERO) > 0 && BigDecimal.valueOf(executeQty).add(line.getExecuteQty()).compareTo(line.getQuantity()) < 0) {
                // ????????????????????? ?????????????????????
                mtInstruction.setInstructionStatus("EXECUTE");
            }
            OptionalHelper.optional(Arrays.asList(MtInstruction.FIELD_INSTRUCTION_STATUS));
            mtInstructionMapper.updateOptional(mtInstruction);

            //??????????????????????????????
            QmsMaterialInspExempt qmsMaterialInspExempt = new QmsMaterialInspExempt();
            qmsMaterialInspExempt.setTenantId(tenantId);
            qmsMaterialInspExempt.setMaterialId(line.getMaterialId());
            qmsMaterialInspExempt.setSiteId(line.getSiteId());
            //qmsMaterialInspExempt.setType("CCDR");
            qmsMaterialInspExempt.setEnableFlag(HmeConstants.ConstantValue.YES);
            qmsMaterialInspExempt.setExemptionFlag(HmeConstants.ConstantValue.YES);
            qmsMaterialInspExempt = qmsMaterialInspExemptRepository.selectOne(qmsMaterialInspExempt);
            if (!Objects.isNull(qmsMaterialInspExempt)) {
                WmsPutInStorageTask wmsPutInStorageTask = new WmsPutInStorageTask();
                wmsPutInStorageTask.setInstructionId(line.getInstructionId());
                WmsPutInStorageTask wmsPutInStorageTask1 = wmsPutInStorageTaskRepository.selectOne(wmsPutInStorageTask);
                if (!Objects.isNull(wmsPutInStorageTask1)) {
                    if ("COMPLETE".equals(mtInstruction.getInstructionStatus())) {
                        wmsPutInStorageTask1.setTaskStatus("STOCKING");
                    } else if (WmsConstant.InstructionStatus.EXECUTE.equals(mtInstruction.getInstructionStatus())) {
                        wmsPutInStorageTask1.setTaskStatus("STOCKED");
                    }
                    wmsPutInStorageTaskRepository.updateByPrimaryKey(wmsPutInStorageTask1);
                }

            }

            WmsMaterialReturnScanLineDTO wmsMaterialReturnScanLineDTO = new WmsMaterialReturnScanLineDTO();
            wmsMaterialReturnScanLineDTO.setMaterialId(line.getMaterialId());
            wmsMaterialReturnScanLineDTO.setMaterialVersion(line.getMaterialVersion());
            wmsMaterialReturnScanLineDTO.setInstructionStatus(mtInstruction.getInstructionStatus());
            List<LovValueDTO> lovList = commonApiService.queryLovValueList(tenantId, "WMS.COST_CENTER_DOCUMENT_LINE.STATUS", mtInstruction.getInstructionStatus());
            if (CollectionUtils.isNotEmpty(lovList)) {
                wmsMaterialReturnScanLineDTO.setLineStatusMeaning(lovList.get(0).getMeaning());
            }
            wmsMaterialReturnScanLineDTO.setExecuteQty(line.getExecuteQty());
            WmsMaterialReturnScanLineDTOList.add(wmsMaterialReturnScanLineDTO);
        }

        // ??????6???????????????????????????????????????
        MtInstructionVO10 mtInstruction = new MtInstructionVO10();
        mtInstruction.setSourceDocId(dto.getInstructionDocId());
        List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstruction);

        //?????????id?????????????????????
        List<MtInstruction> docLineTempList = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIdList);

        if (docLineTempList.stream().filter(item -> WmsConstant.InstructionStatus.COMPLETE.equals(item.getInstructionStatus())).count() == docLineTempList.size()) {
            mtInstructionDoc.setInstructionDocStatus("COMPLETE");
        } else {
            mtInstructionDoc.setInstructionDocStatus("EXECUTE");
        }
        OptionalHelper.optional(Arrays.asList(MtInstructionDoc.FIELD_INSTRUCTION_DOC_STATUS));
        mtInstructionDocMapper.updateOptional(mtInstructionDoc);

        wmsMaterialReturnScanDTO.setInstructionDocId(mtInstructionDoc.getInstructionDocId());

        //????????????
        wmsMaterialReturnScanDTO.setInstructionDocStatus(mtInstructionDoc.getInstructionDocStatus());
        wmsMaterialReturnScanDTO.setDocLineList(WmsMaterialReturnScanLineDTOList);

        /**
         * ???????????????????????????????????????????????? by han.zhang 2020-05-15
         */
        messageClient.sendToAll(WmsConstant.REVEIVED_BOARD_THIRTY_MATRIAL_QUANTITY_UPDATED, MtBaseConstants.YES);
        // ????????????????????????????????????????????????
        List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
        itfObjectTransactionIfaceService.sendSapMaterialMove(tenantId,wmsObjectTransactionResponseVOS);
        return wmsMaterialReturnScanDTO;
    }

    /**
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.wms.api.dto.WmsMaterialReturnDetailDTO2>
     * @Description ????????????
     * @Author wenzhang.yu
     * @Date 2020/4/23 9:43
     * @version 1.0
     **/
    @Override
    @ProcessLovValue
    public List<WmsMaterialReturnDetailDTO2> docDetailQuery(Long tenantId, WmsMaterialReturnDetailDTO dto) {

        List<WmsMaterialReturnDetailDTO2> rerurnList = new ArrayList<>();

        List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository.instructionLimitActualDetailQuery(tenantId, dto.getInstructionId());
        if (CollectionUtils.isNotEmpty(mtInstructionActualDetails)) {
            for (MtInstructionActualDetail line : mtInstructionActualDetails) {
                MtMaterialLot materialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, line.getMaterialLotId());
                WmsMaterialReturnDetailDTO2 returnDto = new WmsMaterialReturnDetailDTO2();
                returnDto.setScanFlag(WmsConstant.CONSTANT_N);
                returnDto.setMaterialLotCode(materialLot.getMaterialLotCode());
                //??????????????????
                if (StringUtils.isNotEmpty(materialLot.getMaterialId())) {
                    returnDto.setMaterialId(materialLot.getMaterialId());
                    MtMaterialVO mtMaterialVOS = mtMaterialRepository.materialPropertyGet(tenantId, materialLot.getMaterialId());
                    returnDto.setMaterialCode(mtMaterialVOS.getMaterialCode());
                    returnDto.setMaterialName(mtMaterialVOS.getMaterialName());
                }
                returnDto.setPrimaryUomQty(line.getActualQty());
                //????????????
                if (StringUtils.isNotEmpty(materialLot.getPrimaryUomId())) {
                    MtUomVO mtUomVO = mtUomRepository.uomPropertyGet(tenantId, materialLot.getPrimaryUomId());
                    if (!Objects.isNull(mtUomVO)) {
                        returnDto.setPrimaryUomCode(mtUomVO.getUomCode());
                    }
                }
                returnDto.setQualityStatus(materialLot.getQualityStatus());
                returnDto.setLot(materialLot.getLot());
                returnDto.setLocatorId(line.getToLocatorId());
                rerurnList.add(returnDto);
            }
        }
        if (rerurnList.size() < 1) {
            throw new MtException("WMS_COST_CENTER_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0047", WmsConstant.ConstantValue.WMS));
        }
        return rerurnList;
    }

    /**
     * @param tenantId
     * @param locatorCode
     * @return com.ruike.wms.api.dto.WmsMaterialReturnScanLocatorDTO
     * @Description ????????????
     * @Author wenzhang.yu
     * @Date 2020/6/4 10:32
     * @version 1.0
     **/
    @Override
    public WmsMaterialReturnScanLocatorDTO returnLocatorScan(Long tenantId, String locatorCode, List<WmsMaterialReturnScanDTO2> barCodeList, List<WmsMaterialReturnScanLineDTO> docLineList) {
        WmsMaterialReturnScanLocatorDTO wmsMaterialReturnScanLocatorDTO = new WmsMaterialReturnScanLocatorDTO();
        MtModLocatorVO7 mtModLocatorVO7 = new MtModLocatorVO7();
        mtModLocatorVO7.setLocatorCode(locatorCode);
        List<MtModLocatorVO8> mtModLocatorVO8s = mtModLocatorRepository.propertyLimitLocatorPropertyQuery(tenantId, mtModLocatorVO7);

        if (CollectionUtils.isNotEmpty(mtModLocatorVO8s)) {
            wmsMaterialReturnScanLocatorDTO.setLocatorCode(mtModLocatorVO8s.get(0).getLocatorCode());
            wmsMaterialReturnScanLocatorDTO.setLocatorId(mtModLocatorVO8s.get(0).getLocatorId());
            wmsMaterialReturnScanLocatorDTO.setLocatorName(mtModLocatorVO8s.get(0).getLocatorName());
            if (CollectionUtils.isNotEmpty(barCodeList)) {
                WmsMaterialReturnScanDTO2 wmsMaterialReturnScanDTO2 = barCodeList.get(barCodeList.size() - 1);
                wmsMaterialReturnScanDTO2.setToLocatorCode(mtModLocatorVO8s.get(0).getLocatorCode());
                List<WmsMaterialReturnScanLineDTO> collect = docLineList.stream()
                        .filter(x -> x.getMaterialId().equals(wmsMaterialReturnScanDTO2.getMaterialId()) && ((x.getMaterialVersion().equals(wmsMaterialReturnScanDTO2.getMaterialVersion())) || (StringUtils.isEmpty(x.getMaterialVersion()) && StringUtils.isEmpty(wmsMaterialReturnScanDTO2.getMaterialVersion())) || (StringUtils.isEmpty(x.getMaterialVersion())))).collect(Collectors.toList());
                if (collect.size() > 0) {
                    // ??????????????????  ??????????????????
                    if (StringUtils.isNotBlank(collect.get(0).getToLocatorId())) {
                        if (!mtModLocatorVO8s.get(0).getLocatorId().equals(collect.get(0).getToLocatorId())) {
                            throw new MtException("WMS_COST_CENTER_0073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_COST_CENTER_0073", WmsConstant.ConstantValue.WMS, locatorCode));
                        }
                    }
                    // ??????????????????
                    if (!mtModLocatorVO8s.get(0).getParentLocatorId().equals(collect.get(0).getToWarehouseId())) {
                        throw new MtException("WMS_COST_CENTER_0040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0040", WmsConstant.ConstantValue.WMS, locatorCode));
                    }
                }
            } else {
                throw new MtException("WMS_COST_CENTER_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0046", WmsConstant.ConstantValue.WMS));
            }

        } else {
            throw new MtException("WMS_COST_CENTER_0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0041", WmsConstant.ConstantValue.WMS, locatorCode));
        }
        List<String> ids = mtModLocatorRepository.parentLocatorQuery(tenantId, mtModLocatorVO8s.get(0).getLocatorId(), "FIRST");
        if (ids.size() == 1 && !StringUtils.isEmpty(ids.get(0))) {
            wmsMaterialReturnScanLocatorDTO.setFirstLocatorId(ids.get(0));
        }
        return wmsMaterialReturnScanLocatorDTO;
    }

}
