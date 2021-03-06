package com.ruike.wms.app.service.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.api.dto.ItfLightTaskIfaceDTO;
import com.ruike.itf.api.dto.ItfProductionPickingIfaceDTO;
import com.ruike.itf.app.service.ItfLightTaskIfaceService;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.app.service.ItfProductionPickingIfaceService;
import com.ruike.itf.domain.entity.ItfLightTaskIface;
import com.ruike.itf.domain.repository.ItfLightTaskIfaceRepository;
import com.ruike.itf.domain.vo.ItfLightTaskIfaceVO;
import com.ruike.itf.infra.mapper.ItfLightTaskIfaceMapper;
import com.ruike.wms.api.dto.WmsMaterialReturnScanLineDTO;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.app.service.WmsProductionReturnService;
import com.ruike.wms.domain.entity.WmsPfepInertiaLocator;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsPfepInertiaLocatorRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO2;
import tarzan.actual.domain.vo.MtInstructionActualVO;
import tarzan.actual.domain.vo.MtInstructionActualVO1;
import tarzan.actual.infra.mapper.MtInstructionActualDetailMapper;
import tarzan.actual.infra.mapper.MtInstructionActualMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.general.domain.vo.MtEventVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionDocVO4;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.instruction.domain.vo.MtInstructionVO10;
import tarzan.instruction.infra.mapper.MtInstructionDocMapper;
import tarzan.instruction.infra.mapper.MtInstructionMapper;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtPfepInventory;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtPfepInventoryRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * ??????????????????????????????
 *
 * @author li.zhang 2021/07/13 9:51
 */
@Service
public class WmsProductionReturnServiceImpl implements WmsProductionReturnService {

    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtInstructionDocMapper mtInstructionDocMapper;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtInstructionMapper mtInstructionMapper;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;
    @Autowired
    private MtInstructionActualDetailRepository mtInstructionActualDetailRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtInstructionActualMapper mtInstructionActualMapper;
    @Autowired
    private MtInstructionActualDetailMapper mtInstructionActualDetailMapper;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;
    @Autowired
    private WmsProductionReturnService wmsProductionReturnService;
    @Autowired
    private MtPfepInventoryRepository mtPfepInventoryRepository;
    @Autowired
    private WmsPfepInertiaLocatorRepository wmsPfepInertiaLocatorRepository;
    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;
    @Autowired
    private ItfProductionPickingIfaceService itfProductionPickingIfaceService;
    @Autowired
    private ItfLightTaskIfaceService itfLightTaskIfaceService;
    @Autowired
    private ItfLightTaskIfaceRepository itfLightTaskIfaceRepository;
    @Autowired
    private ItfLightTaskIfaceMapper itfLightTaskIfaceMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @ProcessLovValue
    public WmsProductionReturnVO docScan(Long tenantId, String instructionDocNum,String instructionDocId) {
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
        //????????????
        WmsProductionReturnVO wmsProductionReturnVO = new WmsProductionReturnVO();
        List<WmsProductionReturnInstructionVO> instructionList = new ArrayList<>();
        List<WmsProductionReturnInstructionVO> instructionList1 = new ArrayList<>();
        //?????????????????????????????????Id
        MtInstructionDocVO4 mtInstructionDocVO4 = new MtInstructionDocVO4();
        mtInstructionDocVO4.setInstructionDocNum(instructionDocNum);
        List<String> instructionDocIds = mtInstructionDocRepository.propertyLimitInstructionDocQuery(tenantId, mtInstructionDocVO4);
        if(CollectionUtils.isEmpty(instructionDocIds)){
            throw new MtException("WMS_MATERIAL_ON_SHELF_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0001", "WMS", instructionDocNum));
        }
        //???????????????id??????????????????
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.instructionDocPropertyGet(tenantId, instructionDocIds.get(0));
        //????????????????????????????????????
        List<LovValueDTO> docTypeMianings = lovAdapter.queryLovValue("WX.WMS.DOC_RE",tenantId);
        List<LovValueDTO> docTypeMianingList = docTypeMianings.stream().filter(item -> item.getValue().equals(mtInstructionDoc.getInstructionDocType())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(docTypeMianingList)){
            throw new MtException("WX_WMS_INSTOCK_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_INSTOCK_0002", "WMS", instructionDocNum));
        }
        if(!mtInstructionDoc.getInstructionDocStatus().equals("RELEASED") && !mtInstructionDoc.getInstructionDocStatus().equals("EXECUTE")){
            throw new MtException("WMS_C/R_DOC_STATUS_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_C/R_DOC_STATUS_0001", "WMS", instructionDocNum));
        }
        wmsProductionReturnVO.setInstructionDocNum(instructionDocNum);
        wmsProductionReturnVO.setInstructionDocId(instructionDocIds.get(0));
        wmsProductionReturnVO.setSiteId(mtInstructionDoc.getSiteId());
        MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, mtInstructionDoc.getSiteId());
        wmsProductionReturnVO.setSiteName(mtModSite.getSiteName());
        wmsProductionReturnVO.setInstructionDocType(mtInstructionDoc.getInstructionDocType());
        String instructionDocTypeMeaning = lovAdapter.queryLovMeaning("WX.WMS.WO_IO_DM_TYPE", tenantId, mtInstructionDoc.getInstructionDocType());
        wmsProductionReturnVO.setInstructionDocTypeMeaning(instructionDocTypeMeaning);
        String instructionDocStatusMeaning = lovAdapter.queryLovMeaning("WX.WMS_C/R_DOC_STATUS", tenantId, mtInstructionDoc.getInstructionDocStatus());
        wmsProductionReturnVO.setInstructionDocStatus(mtInstructionDoc.getInstructionDocStatus());
        wmsProductionReturnVO.setInstructionDocStatusMeaning(instructionDocStatusMeaning);
        //?????????????????????????????????
        String workOrderNum = mtInstructionDocMapper.selectAttrValue(tenantId,"WORK_ORDER_NUM",mtInstructionDoc.getInstructionDocId());
        wmsProductionReturnVO.setWorkOrderNum(workOrderNum);
        //?????????????????????????????????????????????
        String instructionDocTypeMeaning1 = lovAdapter.queryLovMeaning("WX.WMS_INPLAN", tenantId, mtInstructionDoc.getInstructionDocStatus());
        String instructionDocTypeMeaning2 = lovAdapter.queryLovMeaning("WX.WMS_OUTPLAN", tenantId, mtInstructionDoc.getInstructionDocStatus());
        if(!StringUtils.isBlank(instructionDocTypeMeaning1)){
            wmsProductionReturnVO.setPlant("?????????");
        }
        if(!StringUtils.isBlank(instructionDocTypeMeaning2)){
            wmsProductionReturnVO.setPlant("?????????");
        }
        //?????????????????????
        MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
        mtInstructionVO10.setSourceDocId(instructionDocIds.get(0));
        List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
        List<MtInstruction> mtInstructions = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIdList);
        List<ItfLightTaskIfaceDTO> itfLightTaskIfaceDTOS = new ArrayList<>();
        //????????????????????????
        List<MtMaterialVO> mtMaterials = mtMaterialRepository.materialPropertyBatchGet(tenantId,mtInstructions.stream().map(MtInstruction::getMaterialId).collect(Collectors.toList()));
        for(String instructionId:instructionIdList){
            WmsProductionReturnInstructionVO wmsProductionReturnInstructionV0 = new WmsProductionReturnInstructionVO();
            wmsProductionReturnInstructionV0.setInstructionId(instructionId);
            List<MtInstruction> mtInstructionList = mtInstructions.stream().filter(item ->item.getInstructionId().equals(instructionId)).collect(Collectors.toList());
            wmsProductionReturnInstructionV0.setMaterialId(mtInstructionList.get(0).getMaterialId());
            List<MtMaterialVO> mtMaterialList = mtMaterials.stream().filter(item ->item.getMaterialId().equals(mtInstructionList.get(0).getMaterialId())).collect(Collectors.toList());
            wmsProductionReturnInstructionV0.setMaterialCode(mtMaterialList.get(0).getMaterialCode());
            wmsProductionReturnInstructionV0.setMaterialName(mtMaterialList.get(0).getMaterialName());
            String materialVersion = mtInstructionMapper.selectAttrValue(tenantId,"MATERIAL_VERSION",instructionId);
            wmsProductionReturnInstructionV0.setMaterialVersion(materialVersion);
            String instructionStatusMeaning = lovAdapter.queryLovMeaning("WX.WMS_C/R_DOC_LINE_STATUS", tenantId, mtInstructionList.get(0).getInstructionStatus());
            wmsProductionReturnInstructionV0.setInstructionStatus(mtInstructionList.get(0).getInstructionStatus());
            wmsProductionReturnInstructionV0.setInstructionStatusMeaning(instructionStatusMeaning);
            wmsProductionReturnInstructionV0.setQuantity(mtInstructionList.get(0).getQuantity());
            wmsProductionReturnInstructionV0.setInstructionType(mtInstructionList.get(0).getInstructionType());
            wmsProductionReturnInstructionV0.setToSiteId(mtInstructionList.get(0).getToSiteId());
            wmsProductionReturnInstructionV0.setToLocatorId(mtInstructionList.get(0).getToLocatorId());
            wmsProductionReturnInstructionV0.setToLocatorCode(mtModLocatorRepository.locatorBasicPropertyGet(tenantId,mtInstructionList.get(0).getToLocatorId()).getLocatorCode());
            //????????????????????????,??????????????????
            MtInstructionActual mtInstructionActual = new MtInstructionActual();
            mtInstructionActual.setInstructionId(instructionId);
            List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId,
                    mtInstructionActual);
            if(CollectionUtils.isEmpty(actualIdList)){
                wmsProductionReturnInstructionV0.setActualQuantity(0d);
                wmsProductionReturnInstructionV0.setSerialAccount(0d);
            }else{
                MtInstructionActual mtInstructionActual1 = mtInstructionActualRepository.instructionActualPropertyGet(tenantId, actualIdList.get(0));
                wmsProductionReturnInstructionV0.setActualQuantity(mtInstructionActual1.getActualQty());
                MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
                mtInstructionActualDetail.setActualId(actualIdList.get(0));
                List<MtInstructionActualDetailVO> detailList = mtInstructionActualDetailRepository.propertyLimitInstructionActualDetailQuery(tenantId, mtInstructionActualDetail);
                List<String> materialLotIdList = detailList.stream().map(MtInstructionActualDetailVO::getMaterialLotId).collect(Collectors.toList());
                wmsProductionReturnInstructionV0.setSerialAccount(Double.valueOf(materialLotIdList.size()));
            }
            //?????????????????????????????????????????????????????????0
            wmsProductionReturnInstructionV0.setScanQty(0D);
            wmsProductionReturnInstructionV0.setScanSerialAccount(0D);
            //????????????
            wmsProductionReturnInstructionV0.setUomId(mtInstructionList.get(0).getUomId());
            wmsProductionReturnInstructionV0.setUomCode(mtUomRepository.uomPropertyGet(tenantId,mtInstructionList.get(0).getUomId()).getUomCode());
            String soNum = mtInstructionMapper.selectAttrValue(tenantId,"SO_NUM",instructionId);
            String soLineNum = mtInstructionMapper.selectAttrValue(tenantId,"SO_LINE_NUM",instructionId);
            wmsProductionReturnInstructionV0.setSoNum(soNum);
            wmsProductionReturnInstructionV0.setSoLineNum(soLineNum);
            if(wmsProductionReturnInstructionV0.getInstructionStatus().equals("COMPLETED")){
                wmsProductionReturnInstructionV0.setSort(1);
            }else{
                wmsProductionReturnInstructionV0.setSort(0);
            }
            //??????????????????
            String locatorRecomMode = mtInstructionMapper.getMode(tenantId,
                    mtInstructionDoc.getSiteId(),
                    mtInstructionList.get(0).getMaterialId(),
                    mtInstructionList.get(0).getToLocatorId());
            if(StringUtils.isBlank(locatorRecomMode)){
                wmsProductionReturnInstructionV0.setRecommendLocatorCode("");
            }else if(locatorRecomMode.equals("POSITION")){
                MtPfepInventoryVO mtPfepInventoryVO = new MtPfepInventoryVO();
                mtPfepInventoryVO.setMaterialId(mtInstructionList.get(0).getMaterialId());
                mtPfepInventoryVO.setSiteId(mtInstructionDoc.getSiteId());
                mtPfepInventoryVO.setOrganizationType("LOCATOR");
                mtPfepInventoryVO.setOrganizationId(mtInstructionList.get(0).getToLocatorId());
                MtPfepInventory mtPfepInventory = mtPfepInventoryRepository.pfepInventoryGet(tenantId,mtPfepInventoryVO);
                if(StringUtils.isBlank(mtPfepInventory.getStockLocatorId())){
                    wmsProductionReturnInstructionV0.setRecommendLocatorCode("");
                }else{
                    wmsProductionReturnInstructionV0.setRecommendLocatorCode(mtModLocatorRepository.selectByPrimaryKey(mtPfepInventory.getStockLocatorId()).getLocatorCode());
                }
            }else if(locatorRecomMode.equals("INERTIA")){
                WmsPfepInertiaLocator wmsPfepInertiaLocator = new WmsPfepInertiaLocator();
                wmsPfepInertiaLocator.setTenantId(tenantId);
                wmsPfepInertiaLocator.setMaterialId(mtInstructionList.get(0).getMaterialId());
                wmsPfepInertiaLocator.setSiteId(mtInstructionDoc.getSiteId());
                wmsPfepInertiaLocator.setWarehouseId(mtInstructionList.get(0).getToLocatorId());
                List<WmsPfepInertiaLocator> wmsPfepInertiaLocatorList = wmsPfepInertiaLocatorRepository.select(wmsPfepInertiaLocator);
                if(wmsPfepInertiaLocatorList.size() !=0){
                    wmsProductionReturnInstructionV0.setRecommendLocatorCode(mtModLocatorRepository.selectByPrimaryKey(wmsPfepInertiaLocatorList.get(0).getLocatorId()).getLocatorCode());
                }else{
                    wmsProductionReturnInstructionV0.setRecommendLocatorCode("");
                }
            }
            if(StringUtils.isNotBlank(wmsProductionReturnInstructionV0.getRecommendLocatorCode())){
                //?????????????????????????????????ITF.LOCATOR_LABEL_ID???
                List<LovValueDTO> LovValueDTOs = lovAdapter.queryLovValue("ITF.LOCATOR_LABEL_ID", tenantId);
                List<LovValueDTO> LovValueDTOList = LovValueDTOs.stream().filter(item ->item.getValue().equals(wmsProductionReturnInstructionV0.getRecommendLocatorCode())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(LovValueDTOList)){
                    ItfLightTaskIfaceDTO itfLightTaskIfaceDTO = new ItfLightTaskIfaceDTO();
                    itfLightTaskIfaceDTO.setInstructionDocId(wmsProductionReturnVO.getInstructionDocId());
                    itfLightTaskIfaceDTO.setInstructionId(wmsProductionReturnInstructionV0.getInstructionId());
                    itfLightTaskIfaceDTO.setLocatorCode(wmsProductionReturnInstructionV0.getRecommendLocatorCode());
                    itfLightTaskIfaceDTO.setTaskType("IN");
                    itfLightTaskIfaceDTOS.add(itfLightTaskIfaceDTO);
                }
            }
            instructionList1.add(wmsProductionReturnInstructionV0);
        }
        if(CollectionUtils.isNotEmpty(itfLightTaskIfaceDTOS)){
            List<ItfLightTaskIfaceVO> itfLightTaskIfaceVOS = itfLightTaskIfaceService.itfLightTaskIface(tenantId,itfLightTaskIfaceDTOS);
            if(CollectionUtils.isNotEmpty(itfLightTaskIfaceVOS)){
                List<String> taskNumList = itfLightTaskIfaceVOS.stream().map(ItfLightTaskIfaceVO::getTaskNum).collect(Collectors.toList());
                List<ItfLightTaskIface> itfLightTaskIfaces = itfLightTaskIfaceMapper.selectByTaskNum(tenantId,taskNumList);
                for(WmsProductionReturnInstructionVO wmsProductionReturnInstructionVO:instructionList1){
                    List<ItfLightTaskIface> itfLightTaskIfaceList = itfLightTaskIfaces.stream()
                            .filter(item ->item.getDocLineId().equals(wmsProductionReturnInstructionVO.getInstructionId())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(itfLightTaskIfaceList)){
                        wmsProductionReturnInstructionVO.setTaskNum(itfLightTaskIfaceList.get(0).getTaskNum());
                        wmsProductionReturnInstructionVO.setStatus(itfLightTaskIfaceList.get(0).getStatus());
                        wmsProductionReturnInstructionVO.setTaskStatus(itfLightTaskIfaceList.get(0).getTaskStatus());
                    }
                }
            }
        }
        //????????????????????????????????????????????????????????????
        instructionList = instructionList1.stream().sorted(Comparator.comparing(WmsProductionReturnInstructionVO::getSort)).collect(Collectors.toList());
        wmsProductionReturnVO.setInstructionList(instructionList);
        return wmsProductionReturnVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsProductionMaterialReturnVO materialDocScan(Long tenantId, String materialLotCode, String instructionDocId, List<WmsProductionReturnInstructionVO> instructionList, WmsProductionMaterialReturnVO vo,String locatorId) {
        //????????????
        WmsProductionMaterialReturnVO wmsProductionMaterialReturnVO = new WmsProductionMaterialReturnVO();
        //??????????????????????????????
        if(vo != null){
            //????????????????????????????????????
            MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
            mtInstructionActualVO.setInstructionId(vo.getWmsProductionReturnInstructionVO().getInstructionId());
            //?????????????????????????????????
            List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId,vo.getWmsProductionReturnInstructionVO().getInstructionId());
            //?????????????????????????????????????????????
            //????????????????????????
            MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
            mtInstructionActualDetail.setTenantId(tenantId);
            mtInstructionActualDetail.setActualId(mtInstructionActualList.get(0).getActualId());
            mtInstructionActualDetail.setMaterialLotId(vo.getMaterialLotId());
            List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository.select(mtInstructionActualDetail);
            //????????????????????????????????????
            if(CollectionUtils.isNotEmpty(mtInstructionActualDetails) && StringUtils.isBlank(mtInstructionActualDetails.get(0).getToLocatorId())){
                wmsProductionMaterialReturnVO.setMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WX.WMS_PRODUCTION_0020", "WMS",vo.getMaterialLotCode(),"??????"));
                wmsProductionMaterialReturnVO.setMaterialLotCode(vo.getMaterialLotCode());
                return wmsProductionMaterialReturnVO;
            }
            //?????????????????????????????????0
            if(CollectionUtils.isNotEmpty(mtInstructionActualDetails) && mtInstructionActualDetails.get(0).getActualQty().equals(Double.NaN)){
                wmsProductionMaterialReturnVO.setMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WX.WMS_PRODUCTION_0020", "WMS",vo.getMaterialLotCode(),"??????"));
                wmsProductionMaterialReturnVO.setMaterialLotCode(vo.getMaterialLotCode());
                return wmsProductionMaterialReturnVO;
            }
        }
        MtMaterialLotVO21 mtMaterialLotVO21 = new MtMaterialLotVO21();
        mtMaterialLotVO21.setMaterialLotCode(materialLotCode);
        //??????api[propertyLimitMaterialLotPropertyQuery]?????????????????????
        List<MtMaterialLotVO22> mtMaterialLotVO22List = mtMaterialLotRepository.propertyLimitMaterialLotPropertyQuery(tenantId,mtMaterialLotVO21);
        if(CollectionUtils.isEmpty(mtMaterialLotVO22List)){
            throw new MtException("WMS_COST_CENTER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0006", "WMS", materialLotCode));
        }
        //?????????????????????
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, mtMaterialLotVO22List.get(0).getMaterialLotId());
        //????????????????????????
        String materialVersion = mtMaterialLotMapper.selectAttrValue(tenantId,"MATERIAL_VERSION",mtMaterialLot.getMaterialLotId());
        String status = mtMaterialLotMapper.selectAttrValue(tenantId,"STATUS",mtMaterialLot.getMaterialLotId());
        //???????????????id??????
        MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
        mtInstructionVO10.setSourceDocId(instructionDocId);
        List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
        List<MtInstructionActualDetailVO2> mtInstructionActualDetailVO2s = mtInstructionActualDetailRepository.instructionLimitActualDetailBatchQuery(tenantId,instructionIdList);
        List<MtInstructionActualDetailVO2> mtInstructionActualDetailVO2List = mtInstructionActualDetailVO2s.stream().filter(item ->item.getMaterialLotId().equals(mtMaterialLot.getMaterialLotId())).collect(Collectors.toList());
        //?????????????????????
        if(!CollectionUtils.isEmpty(mtInstructionActualDetailVO2List)){
            throw new MtException("WX.WMS_PRODUCTION_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX.WMS_PRODUCTION_0002", "WMS"));
        }
        //?????????????????????
        if(mtMaterialLot.getEnableFlag().equals("Y")){
            throw new MtException("WX.WMS_PRODUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX.WMS_PRODUCTION_0001", "WMS"));
        }
        if("SCANNED".equals(status)){
            throw new MtException("WX.WMS_PRODUCTION_00019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX.WMS_PRODUCTION_00019", "WMS",materialLotCode));
        }
        //?????????????????????
        List<MtInstruction> mtInstructions = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIdList);
        List<MtInstruction> mtInstructions1 = mtInstructions.stream().filter(item ->!item.getInstructionStatus().equals("CANCEL")).collect(Collectors.toList());
        List<MtInstruction> mtInstructionList = mtInstructions1.stream().filter(item ->item.getMaterialId().equals(mtMaterialLot.getMaterialId())).collect(Collectors.toList());
        //????????????????????????????????????
        if(CollectionUtils.isEmpty(mtInstructionList)){
            throw new MtException("WX.WMS_PRODUCTION_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX.WMS_PRODUCTION_0003", "WMS"));
        }
//        //??????????????????
//        String instructionmaterialVersion = mtInstructionMapper.selectAttrValue(tenantId,"MATERIAL_VERSION",mtInstructionList.get(0).getInstructionId());
//        if(!StringUtils.isBlank(materialVersion) && !StringUtils.isBlank(instructionmaterialVersion)){
//            if(!instructionmaterialVersion.equals(materialVersion)){
//                throw new MtException("WX.WMS_PRODUCTION_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                        "WX.WMS_PRODUCTION_0004", "WMS"));
//            }
//        }
        //????????????
        WmsProductionReturnInstructionVO wmsProductionReturnInstructionVO = instructionList.stream().filter(item ->item.getInstructionId().equals(mtInstructionList.get(0).getInstructionId())).collect(Collectors.toList()).get(0);
        if(mtMaterialLot.getPrimaryUomQty()+wmsProductionReturnInstructionVO.getActualQuantity()> wmsProductionReturnInstructionVO.getQuantity()){
            throw new MtException("WX.WMS_PRODUCTION_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX.WMS_PRODUCTION_0005", "WMS"));
        }
        //???????????????????????????
        String soNum = mtMaterialLotMapper.selectAttrValue(tenantId,"SO_NUM",mtMaterialLot.getMaterialLotId());
        String soLineNum = mtMaterialLotMapper.selectAttrValue(tenantId,"SO_LINE_NUM",mtMaterialLot.getMaterialLotId());
        wmsProductionMaterialReturnVO.setSoNum(soNum);
        wmsProductionMaterialReturnVO.setSoLineNum(soLineNum);
        wmsProductionMaterialReturnVO.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        wmsProductionMaterialReturnVO.setMaterialLotCode(materialLotCode);
        wmsProductionMaterialReturnVO.setMaterialId(mtMaterialLot.getMaterialId());
        MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId,mtMaterialLot.getMaterialId());
        wmsProductionMaterialReturnVO.setMaterialName(mtMaterial.getMaterialName());
        wmsProductionMaterialReturnVO.setMaterialVersion(materialVersion);
        wmsProductionMaterialReturnVO.setMaterialLotQty(mtMaterialLot.getPrimaryUomQty());
        //????????????
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("MATERIALLOT_RE_SCAN");
        String eventId = mtEventRepository.eventCreate(tenantId,mtEventCreateVO);
        //????????????
        MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
        mtInstructionActualVO.setInstructionId(wmsProductionReturnInstructionVO.getInstructionId());
        //?????????????????????????????????
        List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId,wmsProductionReturnInstructionVO.getInstructionId());
        if(CollectionUtils.isEmpty(mtInstructionActualList)){
            mtInstructionActualVO.setActualQty(mtMaterialLot.getPrimaryUomQty());
        }else{
            mtInstructionActualVO.setActualQty(mtMaterialLot.getPrimaryUomQty());
        }
        mtInstructionActualVO.setToLocatorId(wmsProductionReturnInstructionVO.getToLocatorId());
        mtInstructionActualVO.setEventId(eventId);
        mtInstructionActualVO.setInstructionType(wmsProductionReturnInstructionVO.getInstructionType());
        mtInstructionActualVO.setMaterialId(wmsProductionReturnInstructionVO.getMaterialId());
        mtInstructionActualVO.setUomId(wmsProductionReturnInstructionVO.getUomId());
        mtInstructionActualVO.setToSiteId(wmsProductionReturnInstructionVO.getToSiteId());
        mtInstructionActualVO.setToLocatorId(wmsProductionReturnInstructionVO.getToLocatorId());
        MtInstructionActualVO1 mtInstructionActualVO1 = mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);
        //??????????????????
        MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
        mtInstructionActualDetail.setActualId(mtInstructionActualVO1.getActualId());
        mtInstructionActualDetail.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        mtInstructionActualDetail.setUomId(mtMaterialLot.getPrimaryUomId());
        mtInstructionActualDetail.setActualQty(mtMaterialLot.getPrimaryUomQty());
        if(!StringUtils.isBlank(locatorId)){
            mtInstructionActualDetail.setToLocatorId(locatorId);
        }
        mtInstructionActualDetailRepository.instructionActualDetailCreate(tenantId, mtInstructionActualDetail);
        //????????????
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setMaterialLotCode(materialLotCode);
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
        //????????????????????????
        MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
        mtExtendVO10.setKeyId(mtMaterialLot.getMaterialLotId());
        mtExtendVO10.setEventId(eventId);
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("STATUS");
        mtExtendVO5.setAttrValue("SCANNED");
        attrs.add(mtExtendVO5);
        mtExtendVO10.setAttrs(attrs);
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
        //??????????????????
        MtInstructionVO mtInstructionVO = new MtInstructionVO();
        mtInstructionVO.setInstructionId(wmsProductionReturnInstructionVO.getInstructionId());
        mtInstructionVO.setEventId(eventId);
        String instructionStatus = "";
        int sort = 0;
        if(CollectionUtils.isEmpty(mtInstructionActualList)){
            if(mtMaterialLot.getPrimaryUomQty() == 0){
                instructionStatus = "RELEASED";
            }else if(mtMaterialLot.getPrimaryUomQty() < wmsProductionReturnInstructionVO.getQuantity()){
                instructionStatus = "EXECUTE";
            }else if(mtMaterialLot.getPrimaryUomQty().equals(wmsProductionReturnInstructionVO.getQuantity())){
                instructionStatus = "COMPLETED";
                sort = 1;
            }
        }else{
            if(mtInstructionActualList.get(0).getActualQty()+mtMaterialLot.getPrimaryUomQty() == 0){
                instructionStatus = "RELEASED";
            }else if(mtInstructionActualList.get(0).getActualQty()+mtMaterialLot.getPrimaryUomQty() < wmsProductionReturnInstructionVO.getQuantity()){
                instructionStatus = "EXECUTE";
            }else if(mtInstructionActualList.get(0).getActualQty()+mtMaterialLot.getPrimaryUomQty() == wmsProductionReturnInstructionVO.getQuantity()){
                instructionStatus = "COMPLETED";
                sort = 1;
            }
        }
        String instructionStatusMeaning = lovAdapter.queryLovMeaning("WX.WMS_C/R_DOC_LINE_STATUS", tenantId, instructionStatus);
        mtInstructionVO.setInstructionStatus(instructionStatus);
        mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, WmsConstant.CONSTANT_N);
        //?????????????????????
        Double actualQty = instructionList.stream().filter(item ->
                item.getInstructionId().equals(mtInstructionList.get(0).getInstructionId())).collect(Collectors.toList()).get(0)
                .getActualQuantity()+mtMaterialLot.getPrimaryUomQty();
        Double serialAccount = instructionList.stream().filter(item ->
                item.getInstructionId().equals(mtInstructionList.get(0).getInstructionId())).collect(Collectors.toList()).get(0)
                .getSerialAccount()+1;
        instructionList.stream().filter(item ->
                item.getInstructionId().equals(mtInstructionList.get(0).getInstructionId())).collect(Collectors.toList()).get(0)
                .setActualQuantity(actualQty);
        instructionList.stream().filter(item ->
                item.getInstructionId().equals(mtInstructionList.get(0).getInstructionId())).collect(Collectors.toList()).get(0)
                .setSerialAccount(serialAccount);
        instructionList.stream().filter(item ->
                item.getInstructionId().equals(mtInstructionList.get(0).getInstructionId())).collect(Collectors.toList()).get(0)
                .setInstructionStatus(instructionStatus);
        instructionList.stream().filter(item ->
                item.getInstructionId().equals(mtInstructionList.get(0).getInstructionId())).collect(Collectors.toList()).get(0)
                .setInstructionStatusMeaning(instructionStatusMeaning);
        instructionList.stream().filter(item ->
                item.getInstructionId().equals(mtInstructionList.get(0).getInstructionId())).collect(Collectors.toList()).get(0)
                .setSort(sort);
        if(StringUtils.isNotBlank(wmsProductionReturnInstructionVO.getTaskNum()) && !"E".equals(wmsProductionReturnInstructionVO.getTaskNum())){
            List<ItfLightTaskIfaceDTO> itfLightTaskIfaceDTOS = new ArrayList<>();
            ItfLightTaskIfaceDTO itfLightTaskIfaceDTO = new ItfLightTaskIfaceDTO();
            itfLightTaskIfaceDTO.setTaskNum(wmsProductionReturnInstructionVO.getTaskNum());
            itfLightTaskIfaceDTO.setTaskStatus("OFF");
            itfLightTaskIfaceDTOS.add(itfLightTaskIfaceDTO);
            List<ItfLightTaskIfaceVO> itfLightTaskIfaceVOS = itfLightTaskIfaceService.itfLightTaskIface(tenantId,itfLightTaskIfaceDTOS);
            if(CollectionUtils.isNotEmpty(itfLightTaskIfaceVOS)){
                List<String> taskNumList = itfLightTaskIfaceVOS.stream().map(ItfLightTaskIfaceVO::getTaskNum).collect(toList());
                List<ItfLightTaskIface> itfLightTaskIfaceList = itfLightTaskIfaceMapper.selectByTaskNum(tenantId,taskNumList);
                instructionList.stream().filter(item ->
                        item.getInstructionId().equals(mtInstructionList.get(0).getInstructionId())).collect(Collectors.toList()).get(0)
                        .setStatus(itfLightTaskIfaceList.get(0).getStatus());
                instructionList.stream().filter(item ->
                        item.getInstructionId().equals(mtInstructionList.get(0).getInstructionId())).collect(Collectors.toList()).get(0)
                        .setTaskStatus(itfLightTaskIfaceList.get(0).getTaskStatus());
                instructionList.stream().filter(item ->
                        item.getInstructionId().equals(mtInstructionList.get(0).getInstructionId())).collect(Collectors.toList()).get(0)
                        .setTaskNum(null);
            }
        }
        //????????????????????????????????????????????????????????????
        List<WmsProductionReturnInstructionVO> instructionList1 = new ArrayList<>();
        instructionList1 = instructionList.stream().sorted(Comparator.comparing(WmsProductionReturnInstructionVO::getSort)).collect(Collectors.toList());
        wmsProductionMaterialReturnVO.setInstructionList(instructionList1);
        wmsProductionReturnInstructionVO.setActualQuantity(actualQty);
        wmsProductionReturnInstructionVO.setSerialAccount(serialAccount);
        wmsProductionReturnInstructionVO.setInstructionStatus(instructionStatus);
        wmsProductionReturnInstructionVO.setInstructionStatusMeaning(instructionStatusMeaning);
        wmsProductionMaterialReturnVO.setWmsProductionReturnInstructionVO(wmsProductionReturnInstructionVO);
        return wmsProductionMaterialReturnVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsProductionQtyChangeVO qtyChange(Long tenantId, Double changeQty, WmsProductionMaterialReturnVO wmsProductionMaterialReturnVO) {
        //????????????
        WmsProductionQtyChangeVO wmsProductionQtyChangeVO = new WmsProductionQtyChangeVO();
        wmsProductionMaterialReturnVO.setWmsProductionReturnInstructionVO(wmsProductionMaterialReturnVO.getInstructionList().stream().filter(item ->item.getInstructionId().equals(wmsProductionMaterialReturnVO.getWmsProductionReturnInstructionVO().getInstructionId())).collect(Collectors.toList()).get(0));
        //????????????
        if(wmsProductionMaterialReturnVO.getWmsProductionReturnInstructionVO().getActualQuantity() + changeQty - wmsProductionMaterialReturnVO.getMaterialLotQty() > wmsProductionMaterialReturnVO.getWmsProductionReturnInstructionVO().getQuantity()){
            throw new MtException("WX.WMS_PRODUCTION_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX.WMS_PRODUCTION_0005", "WMS"));
        }
        wmsProductionQtyChangeVO.setMaterialLotQty(changeQty);
        //?????????????????????????????????
        List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId,wmsProductionMaterialReturnVO.getWmsProductionReturnInstructionVO().getInstructionId());
        //??????????????????
        mtInstructionActualMapper.updateActualQty(tenantId,
                mtInstructionActualList.get(0).getActualQty()+changeQty-wmsProductionMaterialReturnVO.getMaterialLotQty(),
                wmsProductionMaterialReturnVO.getWmsProductionReturnInstructionVO().getInstructionId());
        //????????????????????????
        mtInstructionActualDetailMapper.updateQty(tenantId,changeQty,
                mtInstructionActualList.get(0).getActualId(),wmsProductionMaterialReturnVO.getMaterialLotId());
        //????????????
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("MATERIALLOT_SCAN");
        String eventId = mtEventRepository.eventCreate(tenantId,mtEventCreateVO);
        //??????????????????
        MtInstructionVO mtInstructionVO = new MtInstructionVO();
        mtInstructionVO.setInstructionId(wmsProductionMaterialReturnVO.getWmsProductionReturnInstructionVO().getInstructionId());
        mtInstructionVO.setEventId(eventId);
        String instructionStatus = "";
        int sort = 0;
        if(mtInstructionActualList.get(0).getActualQty()+changeQty-wmsProductionMaterialReturnVO.getMaterialLotQty() == 0){
            instructionStatus = "RELEASED";
        }else if(mtInstructionActualList.get(0).getActualQty()+changeQty-wmsProductionMaterialReturnVO.getMaterialLotQty() < wmsProductionMaterialReturnVO.getWmsProductionReturnInstructionVO().getQuantity()){
            instructionStatus = "EXECUTE";
        }else if(mtInstructionActualList.get(0).getActualQty()+changeQty-wmsProductionMaterialReturnVO.getMaterialLotQty() == wmsProductionMaterialReturnVO.getWmsProductionReturnInstructionVO().getQuantity()){
            instructionStatus = "COMPLETED";
            sort = 1;
        }
        String instructionStatusMeaning = lovAdapter.queryLovMeaning("WX.WMS_C/R_DOC_LINE_STATUS", tenantId, instructionStatus);
        mtInstructionVO.setInstructionStatus(instructionStatus);
        mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, WmsConstant.CONSTANT_N);
        //?????????????????????
        Double actualQty = wmsProductionMaterialReturnVO.getInstructionList().stream().filter(item ->
                item.getInstructionId().equals(wmsProductionMaterialReturnVO.getWmsProductionReturnInstructionVO().getInstructionId()))
                .collect(Collectors.toList()).get(0)
                .getActualQuantity()-wmsProductionMaterialReturnVO.getMaterialLotQty()+changeQty;
        wmsProductionMaterialReturnVO.getInstructionList().stream().filter(item ->
                item.getInstructionId().equals(wmsProductionMaterialReturnVO.getWmsProductionReturnInstructionVO().getInstructionId()))
                .collect(Collectors.toList()).get(0)
                .setActualQuantity(actualQty);
        wmsProductionMaterialReturnVO.getInstructionList().stream().filter(item ->
                item.getInstructionId().equals(wmsProductionMaterialReturnVO.getWmsProductionReturnInstructionVO().getInstructionId()))
                .collect(Collectors.toList()).get(0)
                .setInstructionStatus(instructionStatus);
        wmsProductionMaterialReturnVO.getInstructionList().stream().filter(item ->
                item.getInstructionId().equals(wmsProductionMaterialReturnVO.getWmsProductionReturnInstructionVO().getInstructionId()))
                .collect(Collectors.toList()).get(0)
                .setInstructionStatusMeaning(instructionStatusMeaning);
        wmsProductionMaterialReturnVO.getInstructionList().stream().filter(item ->
                item.getInstructionId().equals(wmsProductionMaterialReturnVO.getWmsProductionReturnInstructionVO().getInstructionId()))
                .collect(Collectors.toList()).get(0)
                .setSort(sort);
        //????????????????????????????????????????????????????????????
        List<WmsProductionReturnInstructionVO> instructionList1 = new ArrayList<>();
        instructionList1 = wmsProductionMaterialReturnVO.getInstructionList().stream().sorted(Comparator.comparing(WmsProductionReturnInstructionVO::getSort)).collect(Collectors.toList());
        wmsProductionQtyChangeVO.setInstructionList(instructionList1);
        return wmsProductionQtyChangeVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsProductionLocatorDocReturnVO locatorDocScan(Long tenantId, String locatorCode, WmsProductionMaterialReturnVO wmsProductionMaterialReturnVO) {
        //????????????
        WmsProductionLocatorDocReturnVO wmsProductionLocatorDocReturnVO = new WmsProductionLocatorDocReturnVO();
        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setTenantId(tenantId);
        mtModLocator.setLocatorCode(locatorCode);
        //????????????
        WmsProductionReturnInstructionVO wmsProductionReturnInstructionVO = wmsProductionMaterialReturnVO.getInstructionList().stream().filter(item ->item.getInstructionId().equals(wmsProductionMaterialReturnVO.getWmsProductionReturnInstructionVO().getInstructionId())).collect(Collectors.toList()).get(0);
        if(wmsProductionReturnInstructionVO.getActualQuantity()> wmsProductionReturnInstructionVO.getQuantity()){
            throw new MtException("WX.WMS_PRODUCTION_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX.WMS_PRODUCTION_0005", "WMS"));
        }
        //???????????????????????????
        List<MtModLocator> mtModLocators = mtModLocatorRepository.select(mtModLocator);
        if(CollectionUtils.isEmpty(mtModLocators)){
            throw new MtException("WX.WMS_PRODUCTION_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX.WMS_PRODUCTION_0008", "WMS"));
        }
        //????????????Id
        List<String> parentLocatorIdS = mtModLocatorRepository.parentLocatorQuery(tenantId, mtModLocators.get(0).getLocatorId(), "FIRST");
        if(!parentLocatorIdS.get(0).equals(wmsProductionMaterialReturnVO.getWmsProductionReturnInstructionVO().getToLocatorId())){
            throw new MtException("WX.WMS_PRODUCTION_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX.WMS_PRODUCTION_0009", "WMS"));
        }
        wmsProductionLocatorDocReturnVO.setLocatorId(mtModLocators.get(0).getLocatorId());
        wmsProductionLocatorDocReturnVO.setLocatorName(mtModLocators.get(0).getLocatorName());
        wmsProductionLocatorDocReturnVO.setLocatorCode(locatorCode);
        //????????????Id
        List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId,wmsProductionMaterialReturnVO.getWmsProductionReturnInstructionVO().getInstructionId());
        //???????????????????????????Id
        mtInstructionActualDetailMapper.updateLocatorByActualId(tenantId,mtModLocators.get(0).getLocatorId(),
                mtInstructionActualList.get(0).getActualId(),wmsProductionMaterialReturnVO.getMaterialLotId());
        return wmsProductionLocatorDocReturnVO;
    }

    @Override
    @ProcessLovValue
    @Transactional(rollbackFor = Exception.class)
    public WmsProductionDetailVO docDetailQuery(Long tenantId, WmsProductionReturnInstructionVO wmsProductionReturnInstructionVO) {
        //????????????
        WmsProductionDetailVO wmsProductionDetailVO = new WmsProductionDetailVO();
        List<WmsProductionReturnInstructionDetailVO> wmsProductionReturnInstructionDetailVOList = new ArrayList<>();
        //???????????????
        //????????????Id
        MtInstructionActual mtInstructionActual = new MtInstructionActual();
        mtInstructionActual.setInstructionId(wmsProductionReturnInstructionVO.getInstructionId());
        List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId, mtInstructionActual);
        if(CollectionUtils.isEmpty(actualIdList)){
            //???????????????
            wmsProductionReturnInstructionVO.setActualQuantity(0d);
            wmsProductionDetailVO.setWmsProductionReturnInstructionVO(wmsProductionReturnInstructionVO);
            return wmsProductionDetailVO;
        }
        wmsProductionReturnInstructionVO.setActualQuantity(mtInstructionActualRepository.instructionActualPropertyGet(tenantId,actualIdList.get(0)).getActualQty());
        //????????????????????????
        MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
        mtInstructionActualDetail.setTenantId(tenantId);
        mtInstructionActualDetail.setActualId(actualIdList.get(0));
        List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository.select(mtInstructionActualDetail);
        if(CollectionUtils.isEmpty(mtInstructionActualDetails)){
            //???????????????
            wmsProductionReturnInstructionVO.setSerialAccount(0d);
            wmsProductionDetailVO.setWmsProductionReturnInstructionVO(wmsProductionReturnInstructionVO);
            return wmsProductionDetailVO;
        }
        List<String> mtMaterialLotIdList = mtInstructionActualDetails.stream().map(MtInstructionActualDetail::getMaterialLotId).collect(Collectors.toList());
        //???????????????
        wmsProductionReturnInstructionVO.setSerialAccount(Double.valueOf(mtMaterialLotIdList.size()));
        wmsProductionDetailVO.setWmsProductionReturnInstructionVO(wmsProductionReturnInstructionVO);
        //???????????????????????????
        List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId,mtMaterialLotIdList);
        List<String> mtMaterialIdList = mtMaterialLots.stream().map(MtMaterialLot::getMaterialId).collect(Collectors.toList());
        //??????????????????????????????
        List<MtMaterialVO> mtMaterialVOS = mtMaterialRepository.materialPropertyBatchGet(tenantId,mtMaterialIdList);
        for(MtInstructionActualDetail mtInstructionActualDetail1:mtInstructionActualDetails){
            WmsProductionReturnInstructionDetailVO wmsProductionReturnInstructionDetailVO = new WmsProductionReturnInstructionDetailVO();
            List<MtMaterialLot> mtMaterialLotList = mtMaterialLots.stream().filter(item ->item.getMaterialLotId().equals(mtInstructionActualDetail1.getMaterialLotId())).collect(Collectors.toList());
            wmsProductionReturnInstructionDetailVO.setMaterialLotId(mtMaterialLotList.get(0).getMaterialLotId());
            wmsProductionReturnInstructionDetailVO.setMaterialLotCode(mtMaterialLotList.get(0).getMaterialLotCode());
            wmsProductionReturnInstructionDetailVO.setMaterialId(mtMaterialLotList.get(0).getMaterialId());
            List<MtMaterialVO> mtMaterialVOList = mtMaterialVOS.stream().filter(item ->item.getMaterialId().equals(mtMaterialLotList.get(0).getMaterialId())).collect(Collectors.toList());
            wmsProductionReturnInstructionDetailVO.setMaterialCode(mtMaterialVOList.get(0).getMaterialCode());
            wmsProductionReturnInstructionDetailVO.setMaterialName(mtMaterialVOList.get(0).getMaterialName());
            //???????????????????????????
            String materialVersion = mtMaterialLotMapper.selectAttrValue(tenantId,"MATERIAL_VERSION",mtMaterialLotList.get(0).getMaterialLotId());
            wmsProductionReturnInstructionDetailVO.setMaterialVersion(materialVersion);
            wmsProductionReturnInstructionDetailVO.setQuality(mtInstructionActualDetail1.getActualQty());
            wmsProductionReturnInstructionDetailVO.setUomId(mtMaterialLotList.get(0).getPrimaryUomId());
            wmsProductionReturnInstructionDetailVO.setUomCode(mtUomRepository.uomPropertyGet(tenantId,mtMaterialLotList.get(0).getPrimaryUomId()).getUomCode());
            wmsProductionReturnInstructionDetailVO.setLot(mtMaterialLotList.get(0).getLot());
            wmsProductionReturnInstructionDetailVO.setToLocatorId(mtInstructionActualDetail1.getToLocatorId());
            if(StringUtils.isBlank(mtInstructionActualDetail1.getToLocatorId())){
                wmsProductionReturnInstructionDetailVO.setToLocatorCode("");
            }else{
                wmsProductionReturnInstructionDetailVO.setToLocatorCode(mtModLocatorRepository.locatorBasicPropertyGet(tenantId,mtInstructionActualDetail1.getToLocatorId()).getLocatorCode());
            }
            wmsProductionReturnInstructionDetailVO.setQualityStatus(mtMaterialLotList.get(0).getQualityStatus());
            String qualityStatusMeaning = lovAdapter.queryLovMeaning("WMS.MTLOT.QUALITY_STATUS", tenantId, mtMaterialLotList.get(0).getQualityStatus());
            wmsProductionReturnInstructionDetailVO.setQualityStatusMeaning(qualityStatusMeaning);
            wmsProductionReturnInstructionDetailVO.setEnableFlag(mtMaterialLotList.get(0).getEnableFlag());
            if(mtMaterialLotList.get(0).getEnableFlag().equals("Y")){
                wmsProductionReturnInstructionDetailVO.setEnableFlagMeaning("??????");
            }
            if(mtMaterialLotList.get(0).getEnableFlag().equals("N")){
                wmsProductionReturnInstructionDetailVO.setEnableFlagMeaning("??????");
            }
            wmsProductionReturnInstructionDetailVO.setCurrentContainerId(mtMaterialLotList.get(0).getCurrentContainerId());
            if(StringUtils.isBlank(mtMaterialLotList.get(0).getCurrentContainerId())){
                wmsProductionReturnInstructionDetailVO.setCurrentContainerCode("");
            }else{
                wmsProductionReturnInstructionDetailVO.setCurrentContainerCode(mtContainerRepository.containerPropertyGet(tenantId,mtMaterialLotList.get(0).getCurrentContainerId()).getContainerCode());
            }
            String status = mtMaterialLotMapper.selectAttrValue(tenantId,"STATUS",mtMaterialLotList.get(0).getMaterialLotId());
            if(status.equals("SCANNED")){
                wmsProductionReturnInstructionDetailVO.setSelected("Y");
            }else{
                wmsProductionReturnInstructionDetailVO.setSelected("N");
            }
            wmsProductionReturnInstructionDetailVOList.add(wmsProductionReturnInstructionDetailVO);
        }
        wmsProductionDetailVO.setWmsProductionReturnInstructionDetailVOList(wmsProductionReturnInstructionDetailVOList);
        return wmsProductionDetailVO;
    }

    @Override
    public WmsProductionDetailDeleteVO docDetailDelete(Long tenantId, WmsProductionReturnInstructionVO wmsProductionReturnInstructionVO, List<WmsProductionReturnInstructionDetailVO> wmsProductionReturnInstructionDetailVOList) {
        //????????????
        WmsProductionDetailDeleteVO wmsProductionDetailDeleteVO = new WmsProductionDetailDeleteVO();
        //????????????Id
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("MATERIALLOT_RE_SCAN_CANCEL");
        String eventId = mtEventRepository.eventCreate(tenantId,mtEventCreateVO);
        //?????????????????????????????????
        List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId,wmsProductionReturnInstructionVO.getInstructionId());
        //??????????????????
        MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
        mtInstructionActualVO.setInstructionId(wmsProductionReturnInstructionVO.getInstructionId());
        Double qty = 0d;
        for(WmsProductionReturnInstructionDetailVO wmsProductionReturnInstructionDetailVO: wmsProductionReturnInstructionDetailVOList){
            qty = qty+wmsProductionReturnInstructionDetailVO.getQuality();
        }
        mtInstructionActualVO.setActualQty(-qty);
        mtInstructionActualVO.setToLocatorId(wmsProductionReturnInstructionVO.getToLocatorId());
        mtInstructionActualVO.setEventId(eventId);
        MtInstructionActualVO1 mtInstructionActualVO1 = mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);
        for(WmsProductionReturnInstructionDetailVO wmsProductionReturnInstructionDetailVO: wmsProductionReturnInstructionDetailVOList){
            //????????????????????????
            mtInstructionActualDetailMapper.deleteByMaterialLotId(tenantId,mtInstructionActualVO1.getActualId(),wmsProductionReturnInstructionDetailVO.getMaterialLotId());
            //????????????
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setMaterialLotCode(wmsProductionReturnInstructionDetailVO.getMaterialLotCode());
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
            //????????????????????????
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setKeyId(wmsProductionReturnInstructionDetailVO.getMaterialLotId());
            mtExtendVO10.setEventId(eventId);
            List<MtExtendVO5> attrs = new ArrayList<>();
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("STATUS");
            mtExtendVO5.setAttrValue("NEW");
            attrs.add(mtExtendVO5);
            mtExtendVO10.setAttrs(attrs);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
        }
        //??????????????????
        MtInstructionVO mtInstructionVO = new MtInstructionVO();
        mtInstructionVO.setInstructionId(wmsProductionReturnInstructionVO.getInstructionId());
        mtInstructionVO.setEventId(eventId);
        String instructionStatus = "";
        if(mtInstructionActualList.get(0).getActualQty()-qty == 0){
            instructionStatus = "RELEASED";
        }else if(mtInstructionActualList.get(0).getActualQty()-qty < wmsProductionReturnInstructionVO.getQuantity()){
            instructionStatus = "EXECUTE";
        }else if(mtInstructionActualList.get(0).getActualQty()-qty == wmsProductionReturnInstructionVO.getQuantity()){
            instructionStatus = "COMPLETED";
        }
        String instructionStatusMeaning = lovAdapter.queryLovMeaning("WX.WMS_C/R_DOC_LINE_STATUS", tenantId, instructionStatus);
        mtInstructionVO.setInstructionStatus(instructionStatus);
        mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, WmsConstant.CONSTANT_N);
        return wmsProductionDetailDeleteVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(Long tenantId, WmsProductionReturnVO wmsProductionReturnVO) {
        //?????????????????????
        MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
        mtInstructionVO10.setSourceDocId(wmsProductionReturnVO.getInstructionDocId());
        List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
        //????????????????????????
        List<MtInstructionActual> mtInstructionActuals = mtInstructionActualRepository.instructionLimitActualBatchGet(tenantId,instructionIdList);
        //????????????Id??????????????????????????????
        List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository.instructionActualLimitDetailBatchQuery(tenantId,
                mtInstructionActuals.stream().map(MtInstructionActual::getActualId).collect(Collectors.toList()));
//        //?????????????????????????????????
//        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
//        mtExtendVO1.setTableName("mt_material_lot_attr");
//        mtExtendVO1.setKeyIdList(mtInstructionActualDetails.stream().map(MtInstructionActualDetail::getMaterialLotId).collect(Collectors.toList()));
//        List<MtExtendVO5> attrs = new ArrayList<>();
//        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
//        mtExtendVO5.setAttrName("STATUS");
//        attrs.add(mtExtendVO5);
//        mtExtendVO1.setAttrs(attrs);
//        List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,mtExtendVO1);
        //??????????????????????????????SCANNED
        String status = "N";
        Set<WmsProductionReturnInstructionVO> instructionSet = new HashSet<WmsProductionReturnInstructionVO>();
//        for(MtExtendAttrVO1 mtExtendAttrVO1:mtExtendAttrVO1s){
//            if(mtExtendAttrVO1.getAttrValue().equals("SCANNED")){
//                status = "Y";
//            }
//        }
        List<MtInstructionActualDetail> mtInstructionActualDetailList = new ArrayList<>();
        for(MtInstructionActualDetail mtInstructionActualDetail:mtInstructionActualDetails){
            if(("SCANNED").equals(mtMaterialLotMapper.selectAttrValue(tenantId,"STATUS",mtInstructionActualDetail.getMaterialLotId()))){
                status = "Y";
                MtInstructionActual mtInstructionActual = mtInstructionActualRepository.instructionActualPropertyGet(tenantId,mtInstructionActualDetail.getActualId());
                instructionSet.add(wmsProductionReturnVO.getInstructionList().stream().filter(item ->item.getInstructionId().equals(mtInstructionActual.getInstructionId())).collect(Collectors.toList()).get(0));
                mtInstructionActualDetailList.add(mtInstructionActualDetail);
            }
        }
        List<WmsProductionReturnInstructionVO> instructionList1 = new ArrayList<>(instructionSet);
        if(status.equals("N")){
            throw new MtException("WMS_DISTRIBUTION_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_DISTRIBUTION_0013", "WMS"));
        }
        for(MtInstructionActualDetail mtInstructionActualDetail:mtInstructionActualDetails){
            //???????????????????????????????????????
            if(StringUtils.isBlank(mtInstructionActualDetail.getToLocatorId())){
                throw new MtException("WX.WMS_PRODUCTION_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WX.WMS_PRODUCTION_0010", "WMS"));
            }
            //???????????????????????????????????????
            if(mtInstructionActualDetail.getActualQty()==0){
                throw new MtException("WX.WMS_PRODUCTION_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WX.WMS_PRODUCTION_0011", "WMS"));
            }
        }
        //??????????????????Id
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "PRODUCTION_MATERIAL_RETURN");
        //????????????Id
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("PRODUCTION_MATERIAL_RETURN");
        mtEventCreateVO.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId,mtEventCreateVO);
        //???????????????????????????
        MtMaterialLotVO42 mtMaterialLotVO42 = new MtMaterialLotVO42();
        mtMaterialLotVO42.setEventId(eventId);
        List<MtMaterialLotVO41> materialLotList = new ArrayList<>();
        //????????????
        List<WmsObjectTransactionRequestVO> wmsObjectTransactionRequestVOS = new ArrayList<>();
        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
        //??????????????????????????????
        List<ItfProductionPickingIfaceDTO> dtoList = new ArrayList<>();
        //????????????????????????
        String workOrderNum = mtInstructionDocMapper.selectAttrValue(tenantId,"WORK_ORDER_NUM",wmsProductionReturnVO.getInstructionDocId());
        for(WmsProductionReturnInstructionVO wmsProductionReturnInstructionVO:instructionList1){
            ItfProductionPickingIfaceDTO itfProductionPickingIfaceDTO = new ItfProductionPickingIfaceDTO();
            itfProductionPickingIfaceDTO.setInstructionDocId(wmsProductionReturnVO.getInstructionDocId());
            itfProductionPickingIfaceDTO.setInstructionId(wmsProductionReturnInstructionVO.getInstructionId());
            //????????????????????????
            Double qty = 0d;
            //??????????????????????????????
            String flag = mtInstructionMapper.selectAttrValue(tenantId,"SPEC_STOCK_FLAG",wmsProductionReturnInstructionVO.getInstructionId());
            String bomReserveNum = mtInstructionMapper.selectAttrValue(tenantId,"BOM_RESERVE_NUM",wmsProductionReturnInstructionVO.getInstructionId());
            String bomReserveLineNum = mtInstructionMapper.selectAttrValue(tenantId,"BOM_RESERVE_LINE_NUM",wmsProductionReturnInstructionVO.getInstructionId());
            String soNum = mtInstructionMapper.selectAttrValue(tenantId,"SO_NUM",wmsProductionReturnInstructionVO.getInstructionId());
            String soLineNum = mtInstructionMapper.selectAttrValue(tenantId,"SO_LINE_NUM",wmsProductionReturnInstructionVO.getInstructionId());
            //???????????????????????????
            List<MtInstructionActual> mtInstructionActualList = mtInstructionActuals.stream()
                    .filter(item ->item.getInstructionId().equals(wmsProductionReturnInstructionVO.getInstructionId()))
                    .collect(Collectors.toList());
            //??????????????????????????????????????????
            List<MtInstructionActualDetail> mtInstructionActualDetailList1 = mtInstructionActualDetailList.stream()
                    .filter(item ->item.getActualId().equals(mtInstructionActualList.get(0).getActualId()))
                    .collect(Collectors.toList());
            for(MtInstructionActualDetail mtInstructionActualDetail:mtInstructionActualDetailList1){
                MtMaterialLotVO41 mtMaterialLotVO41 = new MtMaterialLotVO41();
                mtMaterialLotVO41.setMaterialLotId(mtInstructionActualDetail.getMaterialLotId());
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, mtInstructionActualDetail.getMaterialLotId());
                mtMaterialLotVO41.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                mtMaterialLotVO41.setSiteId(wmsProductionReturnInstructionVO.getToSiteId());
                mtMaterialLotVO41.setEnableFlag("Y");
                mtMaterialLotVO41.setQualityStatus("OK");
                mtMaterialLotVO41.setMaterialId(wmsProductionReturnInstructionVO.getMaterialId());
                mtMaterialLotVO41.setPrimaryUomId(wmsProductionReturnInstructionVO.getUomId());
                mtMaterialLotVO41.setTrxPrimaryUomQty(mtInstructionActualDetail.getActualQty());
                mtMaterialLotVO41.setLocatorId(mtInstructionActualDetail.getToLocatorId());
                mtMaterialLotVO41.setLot(mtMaterialLot.getLot());
                mtMaterialLotVO41.setSupplierId(mtMaterialLot.getSupplierId());
                mtMaterialLotVO41.setSupplierSiteId(mtMaterialLot.getSupplierSiteId());
                mtMaterialLotVO41.setCustomerId(mtMaterialLot.getCustomerId());
                mtMaterialLotVO41.setCustomerSiteId(mtMaterialLot.getCustomerSiteId());
                mtMaterialLotVO41.setCreateReason("PRODUCTION_RETURN");
                mtMaterialLotVO41.setEoId(mtMaterialLot.getEoId());
                mtMaterialLotVO41.setInSiteTime(mtMaterialLot.getInSiteTime());
                mtMaterialLotVO41.setInLocatorTime(currentTimeGet());
                materialLotList.add(mtMaterialLotVO41);
                //??????????????????????????????
                MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
                mtCommonExtendVO6.setKeyId(mtInstructionActualDetail.getMaterialLotId());
                List<MtCommonExtendVO5> mtCommonExtendVO5s = new ArrayList<>();
                MtCommonExtendVO5 mtCommonExtendVO5;
                mtCommonExtendVO5 = new MtCommonExtendVO5();
                mtCommonExtendVO5.setAttrName("STATUS");
                mtCommonExtendVO5.setAttrValue("INSTOCK");
                mtCommonExtendVO5s.add(mtCommonExtendVO5);
                if(!StringUtils.isBlank(wmsProductionReturnInstructionVO.getMaterialVersion())){
                    mtCommonExtendVO5 = new MtCommonExtendVO5();
                    mtCommonExtendVO5.setAttrName("MATERIAL_VERSION");
                    mtCommonExtendVO5.setAttrValue(wmsProductionReturnInstructionVO.getMaterialVersion());
                    mtCommonExtendVO5s.add(mtCommonExtendVO5);
                }
                //??????????????????Y?????????mtmaterialLot????????????SO_NUM???SO_LINE_NUM??????
                if("E".equals(flag)){
                    mtCommonExtendVO5 = new MtCommonExtendVO5();
                    mtCommonExtendVO5.setAttrName("SO_NUM");
                    mtCommonExtendVO5.setAttrValue(wmsProductionReturnInstructionVO.getSoNum());
                    mtCommonExtendVO5s.add(mtCommonExtendVO5);
                    mtCommonExtendVO5 = new MtCommonExtendVO5();
                    mtCommonExtendVO5.setAttrName("SO_LINE_NUM");
                    mtCommonExtendVO5.setAttrValue(wmsProductionReturnInstructionVO.getSoLineNum());
                    mtCommonExtendVO5s.add(mtCommonExtendVO5);
                }
                mtCommonExtendVO6.setAttrs(mtCommonExtendVO5s);
                attrPropertyList.add(mtCommonExtendVO6);
                //????????????
                WmsObjectTransactionRequestVO wmsObjectTransactionRequestVO = new WmsObjectTransactionRequestVO();
                wmsObjectTransactionRequestVO.setTransactionTypeCode("WMS_MATERIAL_RETURN");
                wmsObjectTransactionRequestVO.setEventId(eventId);
                wmsObjectTransactionRequestVO.setMaterialLotId(mtInstructionActualDetail.getMaterialLotId());
                wmsObjectTransactionRequestVO.setMaterialId(wmsProductionReturnInstructionVO.getMaterialId());
                wmsObjectTransactionRequestVO.setTransactionQty(BigDecimal.valueOf(mtInstructionActualDetail.getActualQty()));
                qty = qty + mtInstructionActualDetail.getActualQty();
                wmsObjectTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());
                wmsObjectTransactionRequestVO.setTransferLotNumber(mtMaterialLot.getLot());
                wmsObjectTransactionRequestVO.setTransactionUom(mtUomRepository.uomPropertyGet(tenantId,wmsProductionReturnInstructionVO.getUomId()).getUomCode());
                wmsObjectTransactionRequestVO.setTransactionTime(currentTimeGet());
                wmsObjectTransactionRequestVO.setTransactionReasonCode("??????????????????");
                wmsObjectTransactionRequestVO.setPlantId(mtMaterialLot.getSiteId());
                wmsObjectTransactionRequestVO.setTransferWarehouseId(wmsProductionReturnInstructionVO.getToLocatorId());
                wmsObjectTransactionRequestVO.setTransferWarehouseCode(mtModLocatorRepository.locatorBasicPropertyGet(tenantId,wmsProductionReturnInstructionVO.getToLocatorId()).getLocatorCode());
                wmsObjectTransactionRequestVO.setTransferLocatorId(mtInstructionActualDetail.getToLocatorId());
                wmsObjectTransactionRequestVO.setLocatorId(mtInstructionActualDetail.getToLocatorId());
                wmsObjectTransactionRequestVO.setLocatorCode(mtModLocatorRepository.locatorBasicPropertyGet(tenantId,mtInstructionActualDetail.getToLocatorId()).getLocatorCode());
                wmsObjectTransactionRequestVO.setWarehouseId(wmsProductionReturnInstructionVO.getToLocatorId());
                wmsObjectTransactionRequestVO.setSourceDocType(wmsProductionReturnVO.getInstructionDocType());
                wmsObjectTransactionRequestVO.setSourceDocId(wmsProductionReturnVO.getInstructionDocId());
                wmsObjectTransactionRequestVO.setSourceDocLineId(wmsProductionReturnInstructionVO.getInstructionId());
                WmsTransactionTypeDTO wmsTransactionTypeDTO = wmsTransactionTypeRepository.getTransactionType(tenantId,"WMS_MATERIAL_RETURN");
                wmsObjectTransactionRequestVO.setMoveType(wmsTransactionTypeDTO.getMoveType());
                if(StringUtils.isNotBlank(workOrderNum)){
                    wmsObjectTransactionRequestVO.setWorkOrderNum(workOrderNum);
                }
                if("PL01".equals(wmsProductionReturnVO.getInstructionDocType()) || "PT01".equals(wmsProductionReturnVO.getInstructionDocType())){
                    if(StringUtils.isNotBlank(bomReserveNum)){
                        wmsObjectTransactionRequestVO.setBomReserveNum(bomReserveNum);
                    }else{
                        wmsObjectTransactionRequestVO.setBomReserveNum("");
                    }
                    if(StringUtils.isNotBlank(bomReserveLineNum)){
                        wmsObjectTransactionRequestVO.setBomReserveLineNum(bomReserveLineNum);
                    }else{
                        wmsObjectTransactionRequestVO.setBomReserveLineNum("");
                    }
                }
                if("E".equals(flag)){
                    if(StringUtils.isNotBlank(soNum)){
                        wmsObjectTransactionRequestVO.setSoNum(soNum);
                    }
                    if(StringUtils.isNotBlank(soLineNum)){
                        wmsObjectTransactionRequestVO.setSoLineNum(soLineNum);
                    }
                }
                wmsObjectTransactionRequestVOS.add(wmsObjectTransactionRequestVO);
            }
            itfProductionPickingIfaceDTO.setActualQty(qty);
            dtoList.add(itfProductionPickingIfaceDTO);
        }
        mtMaterialLotVO42.setMaterialLotList(materialLotList);
        //??????????????????????????????
        mtMaterialLotRepository.materialLotAccumulate(tenantId,mtMaterialLotVO42);
        //????????????????????????
        mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId,"mt_material_lot_attr",eventId,attrPropertyList);
        //????????????
        List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, wmsObjectTransactionRequestVOS);
        //????????????
        itfObjectTransactionIfaceService.sendSapMaterialMove(tenantId, wmsObjectTransactionResponseVOS);
        //??????????????????
        String instructionDocStatus = "";
        //??????????????????????????????
        MtInstruction mtInstruction = new MtInstruction();
        mtInstruction.setTenantId(tenantId);
        mtInstruction.setSourceDocId(wmsProductionReturnVO.getInstructionDocId());
        List<MtInstruction> mtInstructionList = mtInstructionRepository.select(mtInstruction);
        for(MtInstruction mtInstruction1:mtInstructionList){
            if(mtInstruction1.getInstructionStatus().equals("EXECUTE")){
                instructionDocStatus = "EXECUTE";
            }
        }
        Set<String> instructionstatusSet = mtInstructionList.stream().map(MtInstruction::getInstructionStatus).collect(Collectors.toSet());
        List<String> instructionstatusList = new ArrayList<>(instructionstatusSet);
        if(instructionstatusList.size()==1){
            if(instructionstatusList.get(0).equals("COMPLETED")){
                instructionDocStatus = "COMPLETED";
            }
            if(instructionstatusList.get(0).equals("RELEASED")){
                instructionDocStatus = "RELEASED";
            }
        }else{
            instructionDocStatus = "EXECUTE";
        }
        MtInstructionDocDTO2 mtInstructionDocDTO2 = new MtInstructionDocDTO2();
        mtInstructionDocDTO2.setInstructionDocId(wmsProductionReturnVO.getInstructionDocId());
        mtInstructionDocDTO2.setInstructionDocStatus(instructionDocStatus);
        mtInstructionDocDTO2.setEventId(eventId);
        mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDocDTO2, WmsConstant.CONSTANT_N);
        //??????????????????????????????????????????
        itfProductionPickingIfaceService.itfProductionPickingIface(tenantId,dtoList);
    }

    @Override
    public WmsProductExitVO exitJudge(Long tenantId, WmsProductionReturnVO wmsProductionReturnVO) {
        //????????????
        WmsProductExitVO wmsProductExitVO = new WmsProductExitVO();
        String msg = "";
        String msg1 = "";
        String msg2 = "";
        for(WmsProductionReturnInstructionVO wmsProductionReturnInstructionVO:wmsProductionReturnVO.getInstructionList()){
            //?????????????????????????????????
            List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId,wmsProductionReturnInstructionVO.getInstructionId());
            String locatormsg = "";
            String qtymsg = "";
            if(!CollectionUtils.isEmpty(mtInstructionActualList)){
                //????????????????????????
                MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
                mtInstructionActualDetail.setTenantId(tenantId);
                mtInstructionActualDetail.setActualId(mtInstructionActualList.get(0).getActualId());
                List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository.select(mtInstructionActualDetail);
                for(MtInstructionActualDetail mtInstructionActualDetail1:mtInstructionActualDetails){
                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId,mtInstructionActualDetail1.getMaterialLotId());
                    if(StringUtils.isBlank(mtInstructionActualDetail1.getToLocatorId())){
                        locatormsg = locatormsg+"["+mtMaterialLot.getMaterialLotCode()+"]"+"-";
                    }
                    if(mtInstructionActualDetail1.getActualQty().equals(Double.NaN)){
                        qtymsg = qtymsg +"["+mtMaterialLot.getMaterialLotCode()+"]"+"-";
                    }
                }
            }
            if(StringUtils.isNotBlank(locatormsg)){
                msg1 = msg1 + locatormsg;
            }
            if(StringUtils.isNotBlank(qtymsg)){
                msg2 = msg2 + qtymsg;
            }
        }
        if(StringUtils.isNotBlank(msg1)){
            msg = msg+ mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX.WMS_PRODUCTION_0018", "WMS", msg1,"??????");
        }
        if(StringUtils.isNotBlank(msg2)){
            msg = msg+ mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX.WMS_PRODUCTION_0018", "WMS", msg2,"??????");
        }
        if(!StringUtils.isBlank(msg)){
            wmsProductExitVO.setMsg(msg);
        }
        return wmsProductExitVO;
    }

    @Override
    public void exitDelete(Long tenantId, WmsProductionReturnVO wmsProductionReturnVO) {
        for(WmsProductionReturnInstructionVO wmsProductionReturnInstructionVO:wmsProductionReturnVO.getInstructionList()){
            //?????????????????????????????????
            List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId,wmsProductionReturnInstructionVO.getInstructionId());
            if(!CollectionUtils.isEmpty(mtInstructionActualList)){
                //????????????????????????
                MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
                mtInstructionActualDetail.setTenantId(tenantId);
                mtInstructionActualDetail.setActualId(mtInstructionActualList.get(0).getActualId());
                List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository.select(mtInstructionActualDetail);
                //????????????Id
                MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
                mtEventCreateVO.setEventTypeCode("MATERIALLOT_RE_SCAN_CANCEL");
                String eventId = mtEventRepository.eventCreate(tenantId,mtEventCreateVO);
                //????????????????????????
                Double qty = 0d;
                for(MtInstructionActualDetail mtInstructionActualDetail1:mtInstructionActualDetails){
                    if(StringUtils.isBlank(mtInstructionActualDetail1.getToLocatorId()) || (mtInstructionActualDetail1.getActualQty().equals(Double.NaN))){
                        qty = qty + mtInstructionActualDetail1.getActualQty();
                        mtInstructionActualDetailMapper.deleteByMaterialLotId(tenantId,mtInstructionActualList.get(0).getActualId(),mtInstructionActualDetail1.getMaterialLotId());
                        //????????????
                        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                        mtMaterialLotVO2.setMaterialLotCode(mtMaterialLotRepository.materialLotPropertyGet(tenantId,mtInstructionActualDetail1.getMaterialLotId()).getMaterialLotCode());
                        mtMaterialLotVO2.setEventId(eventId);
                        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
                        //????????????????????????
                        MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
                        mtExtendVO10.setKeyId(mtInstructionActualDetail1.getMaterialLotId());
                        mtExtendVO10.setEventId(eventId);
                        List<MtExtendVO5> attrs = new ArrayList<>();
                        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                        mtExtendVO5.setAttrName("STATUS");
                        mtExtendVO5.setAttrValue("NEW");
                        attrs.add(mtExtendVO5);
                        mtExtendVO10.setAttrs(attrs);
                        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
                    }
                }
                //??????????????????
                MtInstructionVO mtInstructionVO = new MtInstructionVO();
                mtInstructionVO.setInstructionId(wmsProductionReturnInstructionVO.getInstructionId());
                mtInstructionVO.setEventId(eventId);
                String instructionStatus = "";
                if(mtInstructionActualList.get(0).getActualQty()-qty== 0){
                    instructionStatus = "RELEASED";
                }
                mtInstructionVO.setInstructionStatus(instructionStatus);
                mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, WmsConstant.CONSTANT_N);
                mtInstructionActualMapper.updateActualQty(tenantId,mtInstructionActualList.get(0).getActualQty()-qty,wmsProductionReturnInstructionVO.getInstructionId());
            }
        }
    }

    /***
     * @Description: ??????????????????
     */
    public static Date currentTimeGet() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(currentTime.format(formatter), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }
}
