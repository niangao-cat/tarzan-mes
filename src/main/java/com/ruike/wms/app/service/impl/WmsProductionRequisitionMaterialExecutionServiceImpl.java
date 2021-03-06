package com.ruike.wms.app.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.api.dto.ItfLightTaskIfaceDTO;
import com.ruike.itf.api.dto.ItfProductionPickingIfaceDTO;
import com.ruike.itf.app.service.ItfLightTaskIfaceService;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.app.service.ItfProductionPickingIfaceService;
import com.ruike.itf.domain.entity.CostcenterDocIface;
import com.ruike.itf.domain.vo.ItfLightTaskIfaceVO;
import com.ruike.itf.infra.mapper.ItfLightTaskIfaceMapper;
import com.ruike.wms.api.dto.WmsProductionRequisitionMaterialExecutionDTO;
import com.ruike.wms.api.dto.WmsProductionRequisitionMaterialExecutionDetailDTO;
import com.ruike.wms.api.dto.WmsProductionRequisitionMaterialExecutionLineDTO;
import com.ruike.wms.app.service.WmsProductionRequisitionMaterialExecutionService;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsInstructionSnRelVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import com.ruike.wms.domain.vo.WmsProductPrepareLineVO;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsInstructionSnRelMapper;
import com.ruike.wms.infra.mapper.WmsOutSourceMapper;
import com.ruike.wms.infra.util.StringCommonUtils;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.netty.channel.EventLoopGroup;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtIdHelper;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtInstructionActualDetailMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;
import tarzan.instruction.domain.vo.MtInstructionDocVO4;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.vo.MtModLocatorVO16;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.*;
import static com.ruike.wms.infra.constant.WmsConstant.InstructionStatus.*;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Service
public class WmsProductionRequisitionMaterialExecutionServiceImpl implements WmsProductionRequisitionMaterialExecutionService {
    @Autowired
    MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private WmsInstructionSnRelMapper wmsInstructionSnRelMapper;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    private MtInstructionActualDetailRepository mtInstructionActualDetailRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;
    @Autowired
    private WmsOutSourceMapper wmsOutSourceMapper;
    @Autowired
    private ItfProductionPickingIfaceService itfProductionPickingIfaceService;
    @Autowired
    private MtInstructionActualDetailMapper mtInstructionActualDetailMapper;
    @Autowired
    private ItfLightTaskIfaceService itfLightTaskIfaceService;
    @Autowired
    private ItfLightTaskIfaceMapper itfLightTaskIfaceMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @ProcessLovValue
    public WmsProductionRequisitionMaterialExecutionDTO queryHead(Long tenantId, String instructionDocNum) {
        if (StringUtils.isBlank(instructionDocNum)) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0001", "WMS", instructionDocNum));
        }
        //??????API???????????????
        MtInstructionDocVO4 mtInstructionDocVO4 = new MtInstructionDocVO4();
        mtInstructionDocVO4.setTenantId(tenantId);
        mtInstructionDocVO4.setInstructionDocNum(instructionDocNum);
        //??????instructionDocIdList
        List<String> instructionDocList = mtInstructionDocRepository.propertyLimitInstructionDocQuery(tenantId, mtInstructionDocVO4);
        if (CollectionUtils.isEmpty(instructionDocList)) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0001", "WMS", instructionDocNum));
        }
        //instructionDocPropertyBatchGet
        List<MtInstructionDoc> mtInstructionDocs = mtInstructionDocRepository.instructionDocPropertyBatchGet(tenantId, instructionDocList);
        if (CollectionUtils.isNotEmpty(mtInstructionDocs)) {
            List<String> statusList = mtInstructionDocs.stream().map(MtInstructionDoc::getInstructionDocStatus).collect(Collectors.toList());
            List<String> typeList = mtInstructionDocs.stream().map(MtInstructionDoc::getInstructionDocType).collect(Collectors.toList());
            List<LovValueDTO> docType = lovAdapter.queryLovValue("WX.WMS.DOC_EXE", tenantId);
            List<String> collect = docType.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            if (!collect.contains(typeList.get(0))) {
                throw new MtException("WX_WMS_INSTOCK_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WX_WMS_INSTOCK_0001", "WMS", instructionDocNum));
            }
            statusList.forEach(status -> WmsCommonUtils.processValidateMessage(tenantId, !StringCommonUtils.contains(status, RELEASED, EXECUTE), "WMS_C/R_DOC_STATUS_0001", WMS, instructionDocNum));
        }

        WmsProductionRequisitionMaterialExecutionDTO returnDto = new WmsProductionRequisitionMaterialExecutionDTO();

        //???????????????
        List<WmsProductionRequisitionMaterialExecutionDTO> instructionDocList1 = wmsInstructionSnRelMapper.getInstructionDocList(tenantId, instructionDocList);
        if (CollectionUtils.isNotEmpty(instructionDocList1)) {
            returnDto.setInstructionDocNum(instructionDocList1.get(0).getInstructionDocNum());
            returnDto.setInstructionDocId(instructionDocList1.get(0).getInstructionDocId());
            returnDto.setSiteId(instructionDocList1.get(0).getSiteId());
            returnDto.setSiteCode(instructionDocList1.get(0).getSiteCode());
            returnDto.setSiteName(instructionDocList1.get(0).getSiteName());
            returnDto.setInstructionDocStatus(instructionDocList1.get(0).getInstructionDocStatus());
            returnDto.setInstructionDocType(instructionDocList1.get(0).getInstructionDocType());
            returnDto.setWorkOrderNum(instructionDocList1.get(0).getWorkOrderNum());
            //?????????
            List<LovValueDTO>
                    lovValueInDTOS = lovAdapter.queryLovValue("WX.WMS_INPLAN", tenantId);
            //?????????
            List<LovValueDTO> lovValueOutDTOS = lovAdapter.queryLovValue("WX.WMS_OUTPLAN", tenantId);
            if (CollectionUtils.isNotEmpty(lovValueInDTOS)) {
                List<String> collect = lovValueInDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                if (collect.contains(instructionDocList1.get(0).getInstructionDocType())) {
                    returnDto.setInOutPlan("?????????");
                }
            }
            if (CollectionUtils.isNotEmpty(lovValueOutDTOS)) {
                List<String> collect = lovValueOutDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                if (collect.contains(instructionDocList1.get(0).getInstructionDocType())) {
                    returnDto.setInOutPlan("?????????");
                }
            }
            //???????????????
            List<WmsProductionRequisitionMaterialExecutionLineDTO> instructionList = wmsInstructionSnRelMapper.getInstructionList(tenantId, instructionDocList, null);
            List<WmsProductionRequisitionMaterialExecutionLineDTO> lineList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(instructionList)) {
                instructionList.stream().forEach(instruction -> {
                    WmsProductionRequisitionMaterialExecutionLineDTO lineDTO = new WmsProductionRequisitionMaterialExecutionLineDTO();
                    BeanUtils.copyProperties(instruction, lineDTO);
                    if (StringUtils.isNotBlank(lineDTO.getSoNum()) && StringUtils.isNotBlank(lineDTO.getSoLineNum())) {
                        lineDTO.setSoNumSoLineNum(lineDTO.getSoNum() + "-" + lineDTO.getSoLineNum());
                    } else if (StringUtils.isNotBlank(lineDTO.getSoNum()) && StringUtils.isBlank(lineDTO.getSoLineNum())) {
                        lineDTO.setSoNumSoLineNum(lineDTO.getSoNum());
                    } else if (StringUtils.isBlank(lineDTO.getSoNum()) && StringUtils.isNotBlank(lineDTO.getSoLineNum())) {
                        lineDTO.setSoNumSoLineNum(lineDTO.getSoLineNum());
                    } else {
                        lineDTO.setSoNumSoLineNum("");
                    }
                    String meaning = lovAdapter.queryLovMeaning("WX.WMS_C/R_DOC_LINE_STATUS", tenantId, lineDTO.getInstructionStatus());
                    lineDTO.setInstructionStatusMeaning(meaning);
                    if (lineDTO.getActualQuantity() == null) {
                        lineDTO.setActualQuantity(0D);
                    }
                    List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository.instructionLimitActualDetailQuery(tenantId, instruction.getInstructionId());
                    List<String> collect2 = mtInstructionActualDetails.stream().map(MtInstructionActualDetail::getActualId).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(collect2)) {
                        //List<MtInstructionActualVO3> mtInstructionActualVO3s1 = mtInstructionActualRepository.instructionActualPropertyBatchGet(tenantId, collect2);
                        lineDTO.setNowBarCodeQuantity(new Double(collect2.size()));
                    } else {
                        lineDTO.setNowBarCodeQuantity(0D);
                    }
                    //??????????????????
                    if (StringUtils.isBlank(instruction.getMaterialVersion())) {
                        instruction.setMaterialVersion("");
                    }
                    List<LovValueDTO> lovValueDTOList1 = lovAdapter.queryLovValue("WX.WMS.LOCATOR_TYPE_LIMIT", tenantId);
                    MtModLocatorVO16 materialLocator = new MtModLocatorVO16();
                    if(CollectionUtils.isNotEmpty(lovValueDTOList1)){
                        List<String> locatorTypeList = lovValueDTOList1.stream().map(LovValueDTO::getValue).collect(toList());
                        materialLocator = wmsOutSourceMapper.getMaterialLocatorCodeByType(tenantId, instruction.getMaterialId(),
                                instructionDocList1.get(0).getSiteId(), instruction.getLocatorId(), instruction.getMaterialVersion(),locatorTypeList);
                    }else{
                        materialLocator = wmsOutSourceMapper.getMaterialLocatorCode(tenantId, instruction.getMaterialId(),
                                instructionDocList1.get(0).getSiteId(), instruction.getLocatorId(), instruction.getMaterialVersion());
                    }
                    if (materialLocator != null) {
                        lineDTO.setRecommendedLocatorCode(materialLocator.getLocatorCode());
                        if(StringUtils.isNotBlank(lineDTO.getRecommendedLocatorCode())){
                            //?????????????????????????????????ITF.LOCATOR_LABEL_ID???
                            List<LovValueDTO> LovValueDTOs = lovAdapter.queryLovValue("ITF.LOCATOR_LABEL_ID", tenantId);
                            List<LovValueDTO> LovValueDTOList = LovValueDTOs.stream().filter(item ->item.getValue().equals(lineDTO.getRecommendedLocatorCode())).collect(Collectors.toList());
                            if(CollectionUtils.isNotEmpty(LovValueDTOList)){
                                lineDTO.setLightFlag("Y");
                                lineDTO.setLightStatus("OFF");
                            }else{
                                lineDTO.setLightFlag("N");
                            }
                        }
                    }
                    lineList.add(lineDTO);
                });
                returnDto.setLineDTOList(lineList);
            }
        }
        return returnDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @ProcessLovValue
    public List<WmsProductionRequisitionMaterialExecutionLineDTO> queryBarcode(Long tenantId, WmsProductionRequisitionMaterialExecutionLineDTO dto) {
        if (StringUtils.isBlank(dto.getBarCode())) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", "WMS", dto.getBarCode()));
        }
        //WmsProductionRequisitionMaterialExecutionLineDTO result = new WmsProductionRequisitionMaterialExecutionLineDTO();
        List<String> materialLotIdlist;
        //???????????????????????????
        MtMaterialLotVO30 materialLotVo30 = new MtMaterialLotVO30();
        materialLotVo30.setCode(dto.getBarCode());
        materialLotVo30.setAllLevelFlag(HmeConstants.ConstantValue.YES);
        MtMaterialLotVO29 containerVO = mtMaterialLotRepository.codeOrIdentificationLimitObjectGet(tenantId, materialLotVo30);

        if (HmeConstants.LoadTypeCode.CONTAINER.equals(containerVO.getCodeType())) {
            if (MtIdHelper.isIdNotNull(containerVO.getCodeId())) {
                //????????????
                List<WmsProductionRequisitionMaterialExecutionLineDTO> lineDTOList = containerCodeCheck(tenantId, dto, containerVO.getCodeId());
                /*if (lineDTOList.size() == 1) {
                    lineDTOList.get(0).setBarCodematerialId(lineDTOList.get(0).getDetailDTOList().get(0).getMaterialId());
                    lineDTOList.get(0).setBarCodematerialCode(lineDTOList.get(0).getDetailDTOList().get(0).getMaterialCode());
                    lineDTOList.get(0).setBarCodematerialName(lineDTOList.get(0).getDetailDTOList().get(0).getMaterialName());
                    lineDTOList.get(0).setBarCodematerialVersion(lineDTOList.get(0).getDetailDTOList().get(0).getMaterialVersion());
                    lineDTOList.get(0).setBarCodequantity(lineDTOList.get(0).getDetailDTOList().get(0).getPrimaryUomQty());
                    lineDTOList.get(0).setInsertSoLineSoNum(lineDTOList.get(0).getDetailDTOList().get(0).getSoNum() + "-" + lineDTOList.get(0).getDetailDTOList().get(0).getSoLineNum());
                    lineDTOList.get(0).setBarCodeType(WmsConstant.CONTAINER);
                }*/
                return lineDTOList;
            }
        } else if (HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(containerVO.getCodeType())) {
            //???????????????
            WmsProductionRequisitionMaterialExecutionDetailDTO materialLotCodeCheck = wmsInstructionSnRelMapper.materialLotCodeCheck(tenantId, "", containerVO.getCodeId());
            //???????????????
            List<WmsProductionRequisitionMaterialExecutionLineDTO> instructionList = wmsInstructionSnRelMapper.getInstructionList(tenantId, Collections.singletonList(dto.getInstructionDocId()), null);

            //??????????????????
            List<String> instructionIds = instructionList.stream().map(WmsProductionRequisitionMaterialExecutionLineDTO::getInstructionId).collect(Collectors.toList());
            List<MtInstructionActualDetail> select = wmsInstructionSnRelMapper.instructionDetail(tenantId, materialLotCodeCheck.getMaterialLotId(), instructionIds);
            WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isNotEmpty(select), "WMS_DISTRIBUTION_0006", WMS, materialLotCodeCheck.getMaterialLotCode());

            //???????????????????????????API[objectLimitLoadingContainerQuery]
            if (StringUtils.isNotBlank(materialLotCodeCheck.getCurrentContainerId())) {
                if (StringUtils.isEmpty(dto.getUnbundingFlag())) {
                    MtContainer mtContainer1 = mtContainerRepository.selectByPrimaryKey(materialLotCodeCheck.getCurrentContainerId());
                    dto.setUnbundingFlag(WmsConstant.CONSTANT_Y);
                    dto.setMaterialLotCode(materialLotCodeCheck.getMaterialLotCode());
                    dto.setMaterialContainerCode(mtContainer1.getContainerCode());
                    return Collections.singletonList(dto);
                   /* throw new MtException("WMS_PRODUCTION_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_PRODUCTION_0012", "WMS", materialLotCodeCheck.getMaterialLotCode(), mtContainer1.getContainerCode()));*/
                } else if (StringUtils.isNotBlank(dto.getUnbundingFlag()) && StringUtils.equals(dto.getUnbundingFlag(), WmsConstant.CONSTANT_Y)) {
                    //??????????????????
                    String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "PRODUCTION_MATERIAL_PICK_UNLOAD");
                    // ??????API{ containerUnload }????????????
                    MtContainerVO25 mtContainerVO22 = new MtContainerVO25();
                    mtContainerVO22.setContainerId(materialLotCodeCheck.getCurrentContainerId());
                    mtContainerVO22.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                    mtContainerVO22.setLoadObjectId(materialLotCodeCheck.getMaterialLotId());
                    mtContainerVO22.setEventRequestId(eventRequestId);
                    mtContainerRepository.containerUnload(tenantId, mtContainerVO22);
                }
            }
            //??????????????????
            List<WmsProductionRequisitionMaterialExecutionLineDTO> lineDTOList = newMaterialCodeCheck(tenantId, dto, containerVO.getCodeId(), false);
            List<WmsProductionRequisitionMaterialExecutionDetailDTO> materialLotReturn = lineDTOList.get(0).getDetailDTOList().stream().filter(line -> StringCommonUtils.equalsIgnoreBlank(line.getMaterialLotId(), containerVO.getCodeId())).collect(Collectors.toList());
            lineDTOList.get(0).setBarCodematerialId(materialLotReturn.get(0).getMaterialId());
            lineDTOList.get(0).setBarCodematerialCode(materialLotReturn.get(0).getMaterialCode());
            lineDTOList.get(0).setBarCodematerialName(materialLotReturn.get(0).getMaterialName());
            lineDTOList.get(0).setBarCodematerialVersion(materialLotReturn.get(0).getMaterialVersion());
            lineDTOList.get(0).setBarCodequantity(materialLotReturn.get(0).getPrimaryUomQty());
            if (StringUtils.isNotEmpty(materialLotReturn.get(0).getSoNum()) && StringUtils.isNotEmpty(materialLotReturn.get(0).getSoLineNum())) {
                lineDTOList.get(0).setInsertSoLineSoNum(materialLotReturn.get(0).getSoNum() + "-" + materialLotReturn.get(0).getSoLineNum());
            } else if (StringUtils.isEmpty(materialLotReturn.get(0).getSoNum()) && StringUtils.isNotEmpty(materialLotReturn.get(0).getSoLineNum())) {
                lineDTOList.get(0).setInsertSoLineSoNum(materialLotReturn.get(0).getSoLineNum());
            } else if (StringUtils.isNotEmpty(materialLotReturn.get(0).getSoNum()) && StringUtils.isEmpty(materialLotReturn.get(0).getSoLineNum())) {
                lineDTOList.get(0).setInsertSoLineSoNum(materialLotReturn.get(0).getSoNum());
            } else {
                lineDTOList.get(0).setInsertSoLineSoNum("");
            }
            lineDTOList.get(0).setBarCodeType(WmsConstant.MATERIAL_LOT);
            return lineDTOList;
        } else {
            throw new MtException("WMS_COST_CENTER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0006", "WMS", dto.getBarCode()));
        }
        return null;
    }


    /**
     * @description:??????????????????
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/13 14:38
     */
    private List<WmsProductionRequisitionMaterialExecutionLineDTO> containerCodeCheck(Long tenantId, WmsProductionRequisitionMaterialExecutionLineDTO dto, String codeId) {
        List<WmsProductionRequisitionMaterialExecutionLineDTO> instructionList = new ArrayList<>();
        //??????API???containerLimitMaterialLotQuery?????????????????????????????????
        MtContLoadDtlVO10 mtContLoadDtlVO10 = new MtContLoadDtlVO10();
        mtContLoadDtlVO10.setContainerId(codeId);
        mtContLoadDtlVO10.setAllLevelFlag(HmeConstants.ConstantValue.YES);
        List<MtContLoadDtlVO4> mtContLoadDtlVO4s = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, mtContLoadDtlVO10);
        if (CollectionUtils.isEmpty(mtContLoadDtlVO4s)) {
            throw new MtException("WMS_DISTRIBUTION_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_DISTRIBUTION_0018", "WMS", dto.getBarCode()));
        }
        List<String> list = mtContLoadDtlVO4s.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(Collectors.toList());

        //?????????????????????????????????
        MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
        mtContLoadDtlVO5.setLoadObjectId(codeId);
        mtContLoadDtlVO5.setLoadObjectType(WmsConstant.CONTAINER);
        List<String> list1 = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, mtContLoadDtlVO5);
        if (CollectionUtils.isNotEmpty(list1)) {
            MtContainer mtContainer = mtContainerRepository.selectByPrimaryKey(codeId);
            throw new MtException("WMS_COST_CENTER_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0029", "WMS", mtContainer.getContainerCode()));
        }

        //??????????????????Id
        WmsProductionRequisitionMaterialExecutionDetailDTO ss = wmsInstructionSnRelMapper.containerCheck(tenantId, dto.getBarCode());
        if (ss != null && MtIdHelper.isIdNotNull(ss.getLocatorId())) {
            throw new MtException("WMS_DISTRIBUTION_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_DISTRIBUTION_0012", "WMS", dto.getBarCode()));
        }


        //?????????
        mtContLoadDtlVO4s.stream().forEach(materialLotCodes -> {
            //??????????????????????????????
            //???????????????
            List<WmsProductionRequisitionMaterialExecutionLineDTO> containerInstructionList = wmsInstructionSnRelMapper.getInstructionList(tenantId, Collections.singletonList(dto.getInstructionDocId()), null);

            //??????????????????
            List<String> instructionIds = containerInstructionList.stream().map(WmsProductionRequisitionMaterialExecutionLineDTO::getInstructionId).collect(Collectors.toList());
            List<MtInstructionActualDetail> select = wmsInstructionSnRelMapper.instructionDetail(tenantId, materialLotCodes.getMaterialLotId(), instructionIds);
            WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isNotEmpty(select), "WX.WMS_PRODUCTION_0014", WMS);

            List<WmsProductionRequisitionMaterialExecutionLineDTO> lineDTOList = newMaterialCodeCheck(tenantId, dto, materialLotCodes.getMaterialLotId(), true);
            instructionList.addAll(lineDTOList);
        });
        Map<String, List<WmsProductionRequisitionMaterialExecutionLineDTO>> groupList = instructionList.stream().collect(Collectors.groupingBy(WmsProductionRequisitionMaterialExecutionLineDTO::getInstructionId));

        instructionList.clear();
        for (Map.Entry<String, List<WmsProductionRequisitionMaterialExecutionLineDTO>> entry : groupList.entrySet()) {
            entry.getValue().forEach(i -> {
                i.setSize(i.getDetailDTOList().size());
            });
            List<WmsProductionRequisitionMaterialExecutionLineDTO> sortedList = entry.getValue().stream().sorted(Comparator.comparing(WmsProductionRequisitionMaterialExecutionLineDTO::getSize).reversed())
                    .collect(Collectors.toList());
            WmsProductionRequisitionMaterialExecutionLineDTO lineDTO = sortedList.get(0);
            instructionList.add(lineDTO);
        }
        //instructionList.sort(Comparator.comparing(WmsProductionRequisitionMaterialExecutionLineDTO::getInstructionLineNum));
        return instructionList;

    }


    /**
     * @description:????????????????????????
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/13 14:49
     */
    private Void materialCodeCheck(Long tenantId, WmsProductionRequisitionMaterialExecutionLineDTO dto, String codeId) {
        List<WmsProductionRequisitionMaterialExecutionLineDTO> lineDTOList = new ArrayList<>();
        //???????????????
        List<WmsProductionRequisitionMaterialExecutionLineDTO> instructionList = wmsInstructionSnRelMapper.getInstructionList(tenantId, Collections.singletonList(dto.getInstructionDocId()), null);

        //???????????????
        WmsProductionRequisitionMaterialExecutionDetailDTO materialLotCodeCheck = wmsInstructionSnRelMapper.materialLotCodeCheck(tenantId, "", codeId);
        if (materialLotCodeCheck != null) {
            //????????????
            if (CollectionUtils.isNotEmpty(instructionList)) {
                List<String> instructionStatus = instructionList.stream().map(WmsProductionRequisitionMaterialExecutionLineDTO::getInstructionStatus).collect(Collectors.toList());
                if ((!instructionStatus.contains(WmsConstant.InstructionStatus.RELEASED) && (!instructionStatus.contains(WmsConstant.InstructionStatus.EXECUTE)))) {
                    throw new MtException("WMS_STOCKTAKE_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_STOCKTAKE_001", "WMS", materialLotCodeCheck.getInstructionNum(), materialLotCodeCheck.getSoLineNum(), materialLotCodeCheck.getSoNum()));
                }
            }
            //?????????
            if (!StringUtils.equals(materialLotCodeCheck.getEnableFlag(), WmsConstant.CONSTANT_Y)) {
                throw new MtException("WMS_DISTRIBUTION_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_0005", "WMS", materialLotCodeCheck.getMaterialLotCode()));
            }
            //????????????
            if (StringUtils.equals(materialLotCodeCheck.getFreezeFlag(), WmsConstant.CONSTANT_Y)) {
                throw new MtException("WMS_COST_CENTER_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0025", "WMS", materialLotCodeCheck.getMaterialLotCode()));
            }
            //?????????????????????Y
            if (StringUtils.equals(materialLotCodeCheck.getStocktakeFlag(), WmsConstant.CONSTANT_Y)) {
                throw new MtException("WMS_COST_CENTER_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0034", "WMS", materialLotCodeCheck.getMaterialLotCode()));
            }
            //?????????
            if (StringUtils.equals(materialLotCodeCheck.getMfFlag(), WmsConstant.CONSTANT_Y)) {
                throw new MtException("WMS_DISTRIBUTION_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_0003", "WMS", materialLotCodeCheck.getMaterialLotCode()));
            }
            //???????????????????????????,??????????????????????????????????????????
            List<String> instructionIds = instructionList.stream().map(WmsProductionRequisitionMaterialExecutionLineDTO::getInstructionId).collect(Collectors.toList());
            List<MtInstructionActualDetail> select = wmsInstructionSnRelMapper.instructionDetail(tenantId, materialLotCodeCheck.getMaterialLotId(), instructionIds);
/*            MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
            mtInstructionActualDetail.setTenantId(tenantId);
            mtInstructionActualDetail.setMaterialLotId(materialLotCodeCheck.getMaterialLotId());
            List<MtInstructionActualDetail> select = mtInstructionActualDetailRepository.select(mtInstructionActualDetail);*/
            if (CollectionUtils.isNotEmpty(select)) {
                List<String> ids = new ArrayList<>();
                select.stream().forEach(id -> {
                    ids.add(id.getMaterialLotId());
                });
                if (ids.contains(materialLotCodeCheck.getMaterialLotId())) {
                    throw new MtException("WMS_DISTRIBUTION_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_DISTRIBUTION_0006", "WMS", materialLotCodeCheck.getMaterialLotCode()));
                }
            }
            if (CollectionUtils.isNotEmpty(select)) {
                throw new MtException("WMS_DISTRIBUTION_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_0006", "WMS", materialLotCodeCheck.getMaterialLotCode()));
            }

            if (CollectionUtils.isNotEmpty(instructionList)) {
                List<String> instructionMaterialId = instructionList.stream().map(WmsProductionRequisitionMaterialExecutionLineDTO::getMaterialId).collect(Collectors.toList());
                List<String> instructionUomId = instructionList.stream().map(WmsProductionRequisitionMaterialExecutionLineDTO::getUomId).collect(Collectors.toList());
                List<String> instructionMaterialVersion = instructionList.stream().map(WmsProductionRequisitionMaterialExecutionLineDTO::getMaterialVersion).collect(Collectors.toList());
                List<String> instructionLocatorId = instructionList.stream().map(WmsProductionRequisitionMaterialExecutionLineDTO::getLocatorId).collect(Collectors.toList());
                //viii.	???????????????????????????????????????????????????
                if (!instructionMaterialId.contains(materialLotCodeCheck.getMaterialId())) {
                    throw new MtException("WMS_DISTRIBUTION_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_DISTRIBUTION_0007", "WMS", materialLotCodeCheck.getMaterialLotCode(), materialLotCodeCheck.getMaterialCode()));
                }
                //xi.	????????????????????????????????????????????????
                if (!instructionUomId.contains(materialLotCodeCheck.getUomId())) {
                    throw new MtException("WMS_DISTRIBUTION_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_DISTRIBUTION_0004", "WMS", materialLotCodeCheck.getMaterialLotCode()));
                }
                //????????????????????????
                if (!instructionMaterialVersion.contains(materialLotCodeCheck.getMaterialVersion())) {
                    throw new MtException("WMS_DISTRIBUTION_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_DISTRIBUTION_0008", "WMS", materialLotCodeCheck.getMaterialLotCode(), materialLotCodeCheck.getMaterialVersion()));
                }
                //x.	???????????????????????????????????????????????????
                if (!instructionLocatorId.contains(materialLotCodeCheck.getParentLocatorId())) {
                    throw new MtException("WX.WMS_PRODUCTION_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WX.WMS_PRODUCTION_0007", "WMS"));
                }
            }

            //????????????xii.	????????????????????????????????????
            if (!StringUtils.equals(materialLotCodeCheck.getStatus(), WmsConstant.InstructionStatus.INSTOCK)) {
                throw new MtException("WMS_DISTRIBUTION_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_0010", "WMS", materialLotCodeCheck.getMaterialLotCode(), materialLotCodeCheck.getStatus()));
            }
            //xiii.	???????????????????????????????????????
            if (!StringUtils.equals(materialLotCodeCheck.getQuantityStatus(), WmsConstant.ConstantValue.OK)) {
                throw new MtException("WMS_DISTRIBUTION_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_0011", "WMS", materialLotCodeCheck.getMaterialLotCode(), materialLotCodeCheck.getQuantityStatus()));
            }
            //xiv.	????????????????????????????????????????????????????????????????????????????????????
            List<String> instructionSoNum = instructionList.stream().map(WmsProductionRequisitionMaterialExecutionLineDTO::getSoNum).collect(Collectors.toList());
            List<String> instructionSoLineNum = instructionList.stream().map(WmsProductionRequisitionMaterialExecutionLineDTO::getSoLineNum).collect(Collectors.toList());

            if (!instructionSoNum.contains(materialLotCodeCheck.getSoNum())) {
                throw new MtException("WMS_DISTRIBUTION_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_0009", "WMS", materialLotCodeCheck.getMaterialLotCode(), materialLotCodeCheck.getSoNum()));
            }
            if (!instructionSoLineNum.contains(materialLotCodeCheck.getSoLineNum())) {
                throw new MtException("WMS_DISTRIBUTION_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_0009", "WMS", materialLotCodeCheck.getMaterialLotCode(), materialLotCodeCheck.getSoLineNum()));
            }
            //LOCATOR_TYPE= 14???LOCATOR_CATEGORY= AREA?????????LOCATOR_ID??????
            if (MtIdHelper.isIdNotNull(materialLotCodeCheck.getLocatorId())) {
                throw new MtException("WMS_DISTRIBUTION_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_0012", "WMS", materialLotCodeCheck.getMaterialLotCode()));
            }
            //xvi.	??????????????????{ primaryUomQty }?????????0
            if (materialLotCodeCheck.getPrimaryUomQty() == 0) {
                throw new MtException("WMS_PUT_IN_STOCK_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_PUT_IN_STOCK_013", "WMS", materialLotCodeCheck.getMaterialLotCode()));
            }


            //???????????????????????????API[objectLimitLoadingContainerQuery]
            MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
            mtContLoadDtlVO5.setLoadObjectId(materialLotCodeCheck.getMaterialLotId());
            mtContLoadDtlVO5.setLoadObjectType(WmsConstant.StocktakeRangeObjectType.MATERIAL);
            List<String> strings = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, mtContLoadDtlVO5);
            if (CollectionUtils.isNotEmpty(strings)) {
                if (StringUtils.isEmpty(dto.getUnbundingFlag())) {
                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotCodeCheck.getMaterialLotId());
                    MtContainer mtContainer1 = mtContainerRepository.selectByPrimaryKey(strings.get(0));
//                    unLoadFlag = "?????????" + mtMaterialLot.getMaterialLotCode() + "???" + mtContainer1.getContainerCode() + "??????,?????????????????????,?????????????";
                    throw new MtException("WX.WMS_PRODUCTION_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WX.WMS_PRODUCTION_0013", "WMS", mtMaterialLot.getMaterialLotCode(), mtContainer1.getContainerCode()));
                } else if (StringUtils.isNotBlank(dto.getUnbundingFlag()) && StringUtils.equals(dto.getUnbundingFlag(), WmsConstant.CONSTANT_Y)) {
                    //??????????????????
                    String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "PRODUCTION_MATERIAL_PICK_UNLOAD");
                    // ??????API{ containerUnload }????????????
                    MtContainerVO25 mtContainerVO22 = new MtContainerVO25();
                    mtContainerVO22.setContainerId(strings.get(0));
                    mtContainerVO22.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                    mtContainerVO22.setLoadObjectId(materialLotCodeCheck.getMaterialLotId());
                    mtContainerVO22.setEventRequestId(eventRequestId);
                    mtContainerRepository.containerUnload(tenantId, mtContainerVO22);
                }
            }
        }
        //xvii.	???????????????+???????????????????????????????????????????????????????????????
        //double result = dto.getActualQuantity() + dto.getNowBarCodeQuantity();
        instructionList.stream().forEach(instruction -> {
            if (instruction.getActualQuantity() == null) {
                instruction.setActualQuantity(0D);
            }
            if (instruction.getQuantity().compareTo(instruction.getActualQuantity()) < 0) {
                throw new MtException("WMS_COST_CENTER_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0019", "WMS", materialLotCodeCheck.getMaterialLotCode(), String.valueOf(dto.getActualQuantity())));
            }
        });
        return null;
    }


    /**
     * @description:??????
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/14 10:25
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsProductionRequisitionMaterialExecutionDTO execute(Long tenantId, List<WmsProductionRequisitionMaterialExecutionLineDTO> dtoList, String workOrderNum, String instructionDocType) {
        //??????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "PRODUCTION_MATERIAL_PICK");
        //????????????,??????id
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode("PRODUCTION_MATERIAL_PICK");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(dtoList)) {
            //??????????????????????????????
            List<ItfProductionPickingIfaceDTO> itfProductionPickingIfaceDTOList = new ArrayList<>();
            //??????????????????????????????????????????????????????
            List<WmsProductionRequisitionMaterialExecutionDetailDTO> lineDetailList = new ArrayList<>();
            for (WmsProductionRequisitionMaterialExecutionLineDTO barCodeStatus : dtoList) {
                List<WmsProductionRequisitionMaterialExecutionDetailDTO> wmsProductionRequisitionMaterialExecutionDetailDTOS = materialLotCodeQuery(tenantId, barCodeStatus);
                lineDetailList.addAll(wmsProductionRequisitionMaterialExecutionDetailDTOS);
            }
            List<WmsProductionRequisitionMaterialExecutionDetailDTO> collect = lineDetailList.stream().filter(line -> line.getStatus().equals(WmsConstant.MaterialLotStatus.SCANNED)).collect(toList());
            WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(collect), "WX.WMS_PRODUCTION_0021", WMS);

            dtoList.stream().forEach(dto -> {
                List<WmsProductionRequisitionMaterialExecutionDetailDTO> wmsProductionRequisitionMaterialExecutionDetailDTOS = materialLotCodeQuery(tenantId, dto);
                wmsProductionRequisitionMaterialExecutionDetailDTOS = wmsProductionRequisitionMaterialExecutionDetailDTOS.stream().filter(line -> line.getStatus().equals(WmsConstant.MaterialLotStatus.SCANNED)).collect(Collectors.toList());
                dto.setDetailDTOList(wmsProductionRequisitionMaterialExecutionDetailDTOS);
                List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
                ItfProductionPickingIfaceDTO itfProductionPickingIfaceDTO = new ItfProductionPickingIfaceDTO();
                itfProductionPickingIfaceDTO.setInstructionDocId(dto.getInstructionDocId());
                itfProductionPickingIfaceDTO.setInstructionId(dto.getInstructionId());
                Double qty = 0d;
                for(WmsProductionRequisitionMaterialExecutionDetailDTO s:dto.getDetailDTOList()){
                    qty = qty + s.getPrimaryUomQty();
                }
                itfProductionPickingIfaceDTO.setActualQty(qty);
                itfProductionPickingIfaceDTOList.add(itfProductionPickingIfaceDTO);
                if (CollectionUtils.isNotEmpty(dto.getDetailDTOList())) {
                    dto.getDetailDTOList().stream().forEach(detail -> {
                        if (!StringUtils.equals(detail.getStatus(), WmsConstant.MaterialLotStatus.SCANNED)) {
                            throw new MtException("WMS_DISTRIBUTION_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_DISTRIBUTION_0013", "WMS"));
                        }

                        //???????????????
//                        List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
//                        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
//                        mtExtendVO5.setAttrName("STATUS");
//                        mtExtendVO5.setAttrValue("SHIPPED");
//                        mtExtendVO5List.add(mtExtendVO5);
//                        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", detail.getMaterialLotId(), eventId, mtExtendVO5List);
                        //????????????????????????
                        MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
                        mtCommonExtendVO6.setKeyId(detail.getMaterialLotId());
                        List<MtCommonExtendVO5> attrs = new ArrayList<>();
                        MtCommonExtendVO5 mtCommonExtendVO5 = new MtCommonExtendVO5();
                        mtCommonExtendVO5.setAttrName("STATUS");
                        mtCommonExtendVO5.setAttrValue("SHIPPED");
                        attrs.add(mtCommonExtendVO5);
                        mtCommonExtendVO6.setAttrs(attrs);
                        attrPropertyList.add(mtCommonExtendVO6);

                        //iii.	??????????????????
                        //??????????????????
                        List<WmsObjectTransactionRequestVO> objectTransactionList = new ArrayList<>();
                        WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
                        objectTransactionVO.setTransactionTypeCode(WmsConstant.WMS_MATERIAL_PICK);
                        objectTransactionVO.setEventId(eventId);
                        objectTransactionVO.setMaterialLotId(detail.getMaterialLotId());
                        objectTransactionVO.setMaterialId(detail.getMaterialId());
                        objectTransactionVO.setTransactionQty(BigDecimal.valueOf(detail.getPrimaryUomQty()));

                        objectTransactionVO.setLotNumber(detail.getLot());
                        objectTransactionVO.setTransferLotNumber(detail.getLot());
                        objectTransactionVO.setTransactionUom(detail.getUomCode());
                        objectTransactionVO.setTransactionTime(new Date());
                        objectTransactionVO.setTransactionReasonCode("??????????????????");
                        objectTransactionVO.setPlantId(detail.getSiteId());
                        objectTransactionVO.setWarehouseId(detail.getParentLocatorId());
                        objectTransactionVO.setLocatorId(detail.getMaterialLotLocatorId());
                        objectTransactionVO.setSourceDocType(dto.getInstructionDocType());
                        objectTransactionVO.setSourceDocId(dto.getInstructionDocId());
                        objectTransactionVO.setSourceDocLineId(dto.getInstructionId());
                        objectTransactionVO.setWorkOrderNum(workOrderNum);
                        if (StringUtils.isNotBlank(dto.getSpecStockFlag())) {
                            objectTransactionVO.setSpecStockFlag(dto.getSpecStockFlag());
                        }
                        if (StringUtils.equals(WmsConstant.KEY_IFACE_STATUS_ERROR, objectTransactionVO.getSpecStockFlag())) {
                            objectTransactionVO.setSoNum(dto.getSoNum());
                            objectTransactionVO.setSoLineNum(dto.getSoLineNum());
                        }
                        if (StringCommonUtils.equalsIgnoreBlank(instructionDocType, WmsConstant.PT01) || StringCommonUtils.equalsIgnoreBlank(instructionDocType, WmsConstant.PL01)) {
                            objectTransactionVO.setBomReserveNum(dto.getBomReserveNum());
                            objectTransactionVO.setBomReserveLineNum(dto.getBomReserveLineNum());
                        }
                        List<WmsTransactionType> moveType = wmsTransactionTypeRepository.selectByCondition(Condition.builder(WmsTransactionType.class)
                                .andWhere(Sqls.custom()
                                        .andEqualTo(WmsTransactionType.FIELD_TRANSACTION_TYPE_CODE, objectTransactionVO.getTransactionTypeCode())
                                        .andEqualTo(WmsTransactionType.FIELD_TENANT_ID, tenantId))
                                .build());
                        if (CollectionUtils.isNotEmpty(moveType)) {
                            objectTransactionVO.setMoveType(moveType.get(0).getMoveType());
                        }
                        if (StringUtils.equals(dto.getBarCodeType(), HmeConstants.LoadTypeCode.CONTAINER)) {
                            objectTransactionVO.setContainerId(detail.getContaierId());
                        }
                        //objectTransactionList.add(objectTransactionVO);
                        objectTransactionRequestList.add(objectTransactionVO);
                        //wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionList);
                        //????????????????????????Id
                        mtInstructionActualDetailMapper.updateContainer(tenantId,detail.getMaterialLotId(),detail.getActualId());
                    });
                    //c)	?????????????????????????????????
                    MtMaterialLotVO15 mtMaterialLotVO15 = new MtMaterialLotVO15();
                    mtMaterialLotVO15.setAllConsume(WmsConstant.CONSTANT_Y);
                    mtMaterialLotVO15.setInstructionDocId(dto.getInstructionDocId());
                    mtMaterialLotVO15.setEventRequestId(eventRequestId);
                    mtMaterialLotVO15.setMtMaterialLotSequenceList(dto.getDetailDTOList().stream()
                            .map(detail -> {
                                MtMaterialLotVO16 mtMaterialLotVO161 = new MtMaterialLotVO16();
                                mtMaterialLotVO161.setMaterialLotId(detail.getMaterialLotId());
                                return mtMaterialLotVO161;
                            }).collect(Collectors.toList()));
                    mtMaterialLotRepository.sequenceLimitMaterialLotBatchConsume(tenantId, mtMaterialLotVO15);
                    //?????????????????????????????????
                    mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);

                }
            });
            //??????????????????
            List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
            itfObjectTransactionIfaceService.sendSapMaterialMove(tenantId, wmsObjectTransactionResponseVOS);

            List<WmsProductionRequisitionMaterialExecutionLineDTO> instructionList = wmsInstructionSnRelMapper.getInstructionList(tenantId, singletonList(dtoList.get(0).getInstructionDocId()), null);
            List<String> statusList = instructionList.stream().map(WmsProductionRequisitionMaterialExecutionLineDTO::getInstructionStatus).collect(Collectors.toList());

            List<WmsProductionRequisitionMaterialExecutionLineDTO> complete = instructionList.stream().filter(line -> StringCommonUtils.contains(line.getInstructionStatus(), COMPLETED)).collect(Collectors.toList());
            List<WmsProductionRequisitionMaterialExecutionLineDTO> released = instructionList.stream().filter(line -> StringCommonUtils.contains(line.getInstructionStatus(), RELEASED)).collect(Collectors.toList());


            //d)	?????????????????????
            MtInstructionDocDTO2 mtInstructionDocDTO2 = new MtInstructionDocDTO2();
            mtInstructionDocDTO2.setInstructionDocId(dtoList.get(0).getInstructionDocId());
            mtInstructionDocDTO2.setEventId(eventId);
            if (new BigDecimal(statusList.size()).compareTo(new BigDecimal(complete.size())) == 0) {
                mtInstructionDocDTO2.setInstructionDocStatus(COMPLETED);
            } else if (new BigDecimal(statusList.size()).compareTo(new BigDecimal(released.size())) == 0) {
                mtInstructionDocDTO2.setInstructionDocStatus(RELEASED);
            } else {
                mtInstructionDocDTO2.setInstructionDocStatus(EXECUTE);
            }
            MtInstructionDocVO3 mtInstructionDocVO3 = mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDocDTO2, WmsConstant.KEY_IFACE_STATUS_NEW);

            //??????????????????????????????
            WmsProductionRequisitionMaterialExecutionDTO returnDto = new WmsProductionRequisitionMaterialExecutionDTO();
            returnDto.setInstructionDocStatus(mtInstructionDocDTO2.getInstructionDocStatus());
            String meaning = lovAdapter.queryLovMeaning("WX.WMS_C/R_DOC_STATUS", tenantId, returnDto.getInstructionDocStatus());
            returnDto.setInstructionDocStatusMeaning(meaning);
            returnDto.setLineDTOList(instructionList);
            //??????????????????????????????????????????
            itfProductionPickingIfaceService.itfProductionPickingIface(tenantId,itfProductionPickingIfaceDTOList);
            return returnDto;
        } else {
            throw new MtException("WMS_DISTRIBUTION_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_DISTRIBUTION_0013", "WMS"));
        }
    }


    /**
     * @description:??????
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/14 10:25
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsProductionRequisitionMaterialExecutionLineDTO barcodeDelete(Long tenantId, WmsProductionRequisitionMaterialExecutionLineDTO dto) {
        WmsProductionRequisitionMaterialExecutionLineDTO returnDto = new WmsProductionRequisitionMaterialExecutionLineDTO();
        //????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIALLOT_PICK_SCAN_CANCEL");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);


        if (CollectionUtils.isNotEmpty(dto.getDetailDTOList())) {
            //??????????????????
            double doublesum = dto.getDetailDTOList().stream().mapToDouble(WmsProductionRequisitionMaterialExecutionDetailDTO::getPrimaryUomQty).sum();
            //?????????????????????
            List<WmsProductionRequisitionMaterialExecutionDetailDTO> wmsProductionRequisitionMaterialExecutionDetailDTOS = materialLotCodeQuery(tenantId, dto);
            double detailQtysum = wmsProductionRequisitionMaterialExecutionDetailDTOS.stream().mapToDouble(WmsProductionRequisitionMaterialExecutionDetailDTO::getActualQty).sum();
            BigDecimal subtract = new BigDecimal(detailQtysum).subtract(new BigDecimal(doublesum));
            //dto.getDetailDTOList().stream().forEach(detail -> {
            List<MtInstructionActualVO> mtInstructionActualVOS = new ArrayList<>();
            //???????????????????????????
            List<String> materialLotLists = dto.getDetailDTOList().stream().map(WmsProductionRequisitionMaterialExecutionDetailDTO::getMaterialLotId).collect(toList());
            WmsProductionRequisitionMaterialExecutionLineDTO lineDTO = checkLoad(tenantId, materialLotLists, dto.getUnbundingFlag());
            if (lineDTO != null) {
                return lineDTO;
            }
            for (WmsProductionRequisitionMaterialExecutionDetailDTO detail : dto.getDetailDTOList()) {
                //ii.	???????????????
                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                mtMaterialLotVO2.setMaterialLotCode(detail.getMaterialLotCode());
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, WmsConstant.KEY_IFACE_STATUS_NEW);


                List<MtExtendVO5> attrList = new ArrayList<>();
                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                mtExtendVO5.setAttrName("STATUS");
                mtExtendVO5.setAttrValue("INSTOCK");
                attrList.add(mtExtendVO5);
                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
                    setEventId(eventId);
                    setKeyId(detail.getMaterialLotId());
                    setAttrs(attrList);
                }});
                //?????????????????????
                MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
                mtInstructionActualDetail.setTenantId(tenantId);
                mtInstructionActualDetail.setActualId(detail.getActualId());
                mtInstructionActualDetail.setMaterialLotId(detail.getMaterialLotId());
                List<MtInstructionActualDetailVO> mtInstructionActualDetailVOList = mtInstructionActualDetailRepository.propertyLimitInstructionActualDetailQuery(tenantId, mtInstructionActualDetail);
                //});
                if (CollectionUtils.isNotEmpty(mtInstructionActualDetailVOList)) {
                    mtInstructionActualDetailRepository.deleteByPrimaryKey(mtInstructionActualDetailVOList.get(0).getActualDetailId());
                }
//                MtInstructionActual mtInstructionActualVO = new MtInstructionActual();
//                mtInstructionActualVO.setInstructionId(dto.getInstructionId());
//                mtInstructionActualVO.setActualQty(detailQtysum);
//                //mtInstructionActualVO.setEventId(eventId);
//                mtInstructionActualVO.setActualId(detail.getActualId());
//                mtInstructionActualVO.setInstructionType(dto.getInstructionType());
//                mtInstructionActualVO.setMaterialId(dto.getMaterialId());
//                mtInstructionActualVO.setUomId(dto.getUomId());
//                mtInstructionActualVO.setFromSiteId(dto.getSiteId());
//                mtInstructionActualVO.setFromLocatorId(dto.getLocatorId());
//                mtInstructionActualRepository.delete(mtInstructionActualVO);
                MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
                mtInstructionActualVO.setActualId(detail.getActualId());
                mtInstructionActualVO.setEventId(eventId);
                mtInstructionActualVO.setActualQty(mtInstructionActualDetailVOList.get(0).getActualQty());
                mtInstructionActualVOS.add(mtInstructionActualVO);
            }
            MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
            mtInstructionActualVO.setActualId(mtInstructionActualVOS.get(0).getActualId());
            mtInstructionActualVO.setEventId(eventId);
            Double qty = 0d;
            for (MtInstructionActualVO mtInstructionVO : mtInstructionActualVOS) {
                qty = qty + mtInstructionVO.getActualQty();
            }
            mtInstructionActualVO.setActualQty(-qty);
            mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);
            //i.	??????????????????
            //BigDecimal subtract = new BigDecimal(detailQtysum).subtract(new BigDecimal(doublesum));
/*            MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
            mtInstructionActualVO.setInstructionId(dto.getInstructionId());
            mtInstructionActualVO.setActualQty(subtract.doubleValue());
            mtInstructionActualVO.setEventId(eventId);
            mtInstructionActualVO.setActualId(dto.getDetailDTOList().get(0).getActualId());
            mtInstructionActualVO.setInstructionType(dto.getInstructionType());
            mtInstructionActualVO.setMaterialId(dto.getMaterialId());
            mtInstructionActualVO.setUomId(dto.getUomId());
            mtInstructionActualVO.setFromSiteId(dto.getSiteId());
            mtInstructionActualVO.setFromLocatorId(dto.getLocatorId());
            mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);*/


            //iii.	??????????????????
            MtInstructionVO mtInstructionVO = new MtInstructionVO();
            mtInstructionVO.setInstructionId(dto.getInstructionId());
            mtInstructionVO.setEventId(eventId);
            mtInstructionVO.setSiteId(dto.getSiteId());
            if (BigDecimal.ZERO.compareTo(subtract) == 0) {
                mtInstructionVO.setInstructionStatus(WmsConstant.DELIVIERY_STATUS_VALID_RELEASED);
            } else if (subtract.compareTo(new BigDecimal(dto.getQuantity())) == 0) {
                mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.COMPLETED);
            } else {
                mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.EXECUTE);
            }
            mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, WmsConstant.KEY_IFACE_STATUS_NEW);
        }
        return null;
    }

    @Override
    @ProcessLovValue
    public List<WmsProductionRequisitionMaterialExecutionDetailDTO> materialLotCodeQuery(Long tenantId, WmsProductionRequisitionMaterialExecutionLineDTO dto) {
        List<WmsProductionRequisitionMaterialExecutionDetailDTO> returnList = new ArrayList<>();
        List<MtInstructionActual> mtInstructionActuals = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId, dto.getInstructionId());
        if (CollectionUtils.isNotEmpty(mtInstructionActuals)) {
            List<String> actualIds = mtInstructionActuals.stream().map(MtInstructionActual::getActualId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(actualIds)) {
                actualIds.stream().forEach(actualId -> {
                    MtInstructionActualDetail detail = new MtInstructionActualDetail();
                    detail.setActualId(actualId);
                    detail.setTenantId(tenantId);
                    List<MtInstructionActualDetailVO> mtInstructionActualDetailVOS = mtInstructionActualDetailRepository.propertyLimitInstructionActualDetailQuery(tenantId, detail);

                    if (CollectionUtils.isNotEmpty(mtInstructionActualDetailVOS)) {
                        mtInstructionActualDetailVOS.stream().forEach(mtInstructionActualDetail -> {
                            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, mtInstructionActualDetail.getMaterialLotId());
                            List<WmsProductionRequisitionMaterialExecutionDetailDTO> wmsProductionRequisitionMaterialExecutionDetailDTOS = wmsInstructionSnRelMapper.materialLotCodeDetail(tenantId, dto.getInstructionId(), singletonList(mtMaterialLot.getMaterialLotId()), new ArrayList<>());
                            WmsProductionRequisitionMaterialExecutionDetailDTO su = new WmsProductionRequisitionMaterialExecutionDetailDTO();
                            BeanUtils.copyProperties(wmsProductionRequisitionMaterialExecutionDetailDTOS.get(0), su);
                            String meaning = lovAdapter.queryLovMeaning("WMS.MTLOT.QUALITY_STATUS", tenantId, su.getQuantityStatus());
                            su.setQuantityStatusMeaning(meaning);
                            su.setActualId(mtInstructionActualDetail.getActualId());
                            su.setActualQty(mtInstructionActualDetail.getActualQty());
                            returnList.add(su);
                        });
                    }
                });

            }

        }
        return returnList;


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Void updateBarcode(Long tenantId, List<WmsProductionRequisitionMaterialExecutionLineDTO> dtoList, String barCode) {
        if (CollectionUtils.isNotEmpty(dtoList)) {
            dtoList.stream().forEach(dto -> {
                //???????????????
                //queryBarcode(tenantId, dto);

                //???????????????????????????
                MtMaterialLotVO30 materialLotVo30 = new MtMaterialLotVO30();
                materialLotVo30.setCode(barCode);
                materialLotVo30.setAllLevelFlag(HmeConstants.ConstantValue.YES);
                MtMaterialLotVO29 containerVO = mtMaterialLotRepository.codeOrIdentificationLimitObjectGet(tenantId, materialLotVo30);

                //????????????
                MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                eventCreateVO.setEventTypeCode("MATERIALLOT_PICK_SCAN");
                String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                if (StringUtils.equals(containerVO.getCodeType(), HmeConstants.LoadTypeCode.CONTAINER)) {
                    //??????????????????
                    MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
                    mtInstructionActualVO.setInstructionId(dto.getInstructionId());
                    //mtInstructionActualVO.setActualQty(dto.getActualQuantity());
                    double sum = dto.getDetailDTOList().stream().mapToDouble(WmsProductionRequisitionMaterialExecutionDetailDTO::getPrimaryUomQty).sum();
                    BigDecimal add = new BigDecimal(sum);
                    mtInstructionActualVO.setActualQty(add.doubleValue());
                    mtInstructionActualVO.setEventId(eventId);
                    mtInstructionActualVO.setMaterialId(dto.getMaterialId());
                    mtInstructionActualVO.setUomId(dto.getUomId());
                    mtInstructionActualVO.setInstructionType(dto.getInstructionType());
                    mtInstructionActualVO.setFromSiteId(dto.getSiteId());
                    mtInstructionActualVO.setFromLocatorId(dto.getLocatorId());
                    MtInstructionActualVO1 mtInstructionActualVO1 = mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);

                    //List<WmsProductionRequisitionMaterialExecutionDetailDTO> wmsProductionRequisitionMaterialExecutionDetailDTOS = materialLotCodeQuery(tenantId, dto);
                    // List<String> list = wmsProductionRequisitionMaterialExecutionDetailDTOS.stream().map(WmsProductionRequisitionMaterialExecutionDetailDTO::getMaterialLotId).collect(Collectors.toList());

                    //List<WmsProductionRequisitionMaterialExecutionDetailDTO> materialLotCodeList = wmsInstructionSnRelMapper.materialLotCodeDetail(tenantId, "", list, new ArrayList<>());
                    if (CollectionUtils.isNotEmpty(dto.getDetailDTOList())) {
                        List<Double> actualQtyList = dto.getDetailDTOList().stream().map(WmsProductionRequisitionMaterialExecutionDetailDTO::getPrimaryUomQty).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                        double doublesum = 0D;
                        if (CollectionUtils.isNotEmpty(actualQtyList)) {
                            doublesum = actualQtyList.stream().mapToDouble(Double::doubleValue).sum();
                        }
                        dto.getDetailDTOList().stream().forEach(materialLotCode -> {
                            //??????????????????
                            MtInstructionActualDetail actualDetail = new MtInstructionActualDetail();
                            actualDetail.setActualId(mtInstructionActualVO1.getActualId());
                            actualDetail.setMaterialLotId(materialLotCode.getMaterialLotId());
                            actualDetail.setUomId(materialLotCode.getUomId());
                            actualDetail.setContainerId(containerVO.getCodeId());
//                            actualDetail.setFromLocatorId(materialLotCode.getLocatorId());
                            actualDetail.setFromLocatorId(mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotCode.getMaterialLotId()).getLocatorId());
                            actualDetail.setActualQty(materialLotCode.getPrimaryUomQty());
                            mtInstructionActualDetailRepository.instructionActualDetailCreate(tenantId, actualDetail);

                            //????????????
                            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                            mtMaterialLotVO2.setMaterialLotCode(materialLotCode.getMaterialLotCode());
                            mtMaterialLotVO2.setEventId(eventId);
                            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, WmsConstant.KEY_IFACE_STATUS_NEW);

                            // ???????????????????????????
                            List<MtExtendVO5> attrList = new ArrayList<>();
                            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                            mtExtendVO5.setAttrName("STATUS");
                            mtExtendVO5.setAttrValue("SCANNED");
                            attrList.add(mtExtendVO5);
                            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
                                setEventId(eventId);
                                setKeyId(materialLotCode.getMaterialLotId());
                                setAttrs(attrList);
                            }});
                        });
                        //iii.	??????????????????
                        MtInstructionVO mtInstructionVO = new MtInstructionVO();
                        mtInstructionVO.setInstructionId(dto.getInstructionId());
                        mtInstructionVO.setEventId(eventId);
                        mtInstructionVO.setSiteId(dto.getSiteId());
                        //mtInstructionVO.setQuantity(dto.getQuantity());
                        //mtInstructionVO.setInstructionType(dto.getInstructionType());
                        if (dto.getActualQuantity() == null) {
                            dto.setActualQuantity(0d);
                        }
                        if (doublesum + dto.getActualQuantity() == 0) {
                            mtInstructionVO.setInstructionStatus(WmsConstant.DELIVIERY_STATUS_VALID_RELEASED);
                        } else if (new BigDecimal(doublesum + dto.getActualQuantity()).compareTo(new BigDecimal(dto.getQuantity())) == 0) {
                            mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.COMPLETED);
                        } else {
                            mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.EXECUTE);
                        }
                        mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, WmsConstant.KEY_IFACE_STATUS_NEW);
                    }
                } else if (StringUtils.equals(containerVO.getCodeType(), HmeConstants.LoadTypeCode.MATERIAL_LOT)) {
                    //??????????????????
                    MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
                    mtInstructionActualVO.setInstructionId(dto.getInstructionId());
                    List<WmsProductionRequisitionMaterialExecutionDetailDTO> wmsProductionRequisitionMaterialExecutionDetailDTOS = materialLotCodeQuery(tenantId, dto);
                    //???????????????
                    //double sum1 = wmsProductionRequisitionMaterialExecutionDetailDTOS.stream().mapToDouble(WmsProductionRequisitionMaterialExecutionDetailDTO::getPrimaryUomQty).sum();
                    double sum2 = wmsProductionRequisitionMaterialExecutionDetailDTOS.stream().mapToDouble(WmsProductionRequisitionMaterialExecutionDetailDTO::getActualQty).sum();
                    //??????????????????
                    double sum = dto.getDetailDTOList().stream().mapToDouble(WmsProductionRequisitionMaterialExecutionDetailDTO::getPrimaryUomQty).sum();
                    BigDecimal add1 = new BigDecimal(sum2).add(new BigDecimal(sum));
                    mtInstructionActualVO.setActualQty(sum);
                    //mtInstructionActualVO.setActualQty(dto.getActualQuantity());
                    mtInstructionActualVO.setEventId(eventId);
                    mtInstructionActualVO.setMaterialId(dto.getMaterialId());
                    mtInstructionActualVO.setUomId(dto.getUomId());
                    mtInstructionActualVO.setInstructionType(dto.getInstructionType());
                    mtInstructionActualVO.setFromSiteId(dto.getSiteId());
                    mtInstructionActualVO.setFromLocatorId(dto.getLocatorId());
                    MtInstructionActualVO1 mtInstructionActualVO1 = mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);

                    //List<WmsProductionRequisitionMaterialExecutionDetailDTO> returnDto = wmsInstructionSnRelMapper.materialLotCodeDetail(tenantId, "", singletonList(containerVO.getCodeId()), new ArrayList<>());
                    //if (CollectionUtils.isNotEmpty(returnDto)) {
                    //??????????????????
                    MtInstructionActualDetail actualDetail = new MtInstructionActualDetail();
                    actualDetail.setActualId(mtInstructionActualVO1.getActualId());
                    actualDetail.setMaterialLotId(containerVO.getCodeId());
                    actualDetail.setUomId(dto.getUomId());
                    actualDetail.setActualQty(sum);
                    //actualDetail.setActualQty(dto.getPrimaryUomQty());
                    actualDetail.setFromLocatorId(dto.getLocatorId());
                    mtInstructionActualDetailRepository.instructionActualDetailCreate(tenantId, actualDetail);

                    //????????????
                    MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                    mtMaterialLotVO2.setMaterialLotCode(barCode);
                    mtMaterialLotVO2.setEventId(eventId);
                    mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, WmsConstant.KEY_IFACE_STATUS_NEW);

                    // ???????????????????????????
                    List<MtExtendVO5> attrList = new ArrayList<>();
                    MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                    mtExtendVO5.setAttrName("STATUS");
                    mtExtendVO5.setAttrValue("SCANNED");
                    attrList.add(mtExtendVO5);
                    mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
                        setEventId(eventId);
                        setKeyId(containerVO.getCodeId());
                        setAttrs(attrList);
                    }});

                    //iii.	??????????????????
                    MtInstructionVO mtInstructionVO = new MtInstructionVO();
                    mtInstructionVO.setInstructionId(dto.getInstructionId());
                    mtInstructionVO.setEventId(eventId);
                    mtInstructionVO.setSiteId(dto.getSiteId());
                    //mtInstructionVO.setQuantity(dto.getQuantity());
                    //mtInstructionVO.setInstructionType(dto.getInstructionType());
                    //returnDto.get(0).setActualQty(returnDto.get(0).getActualQty() != null ? returnDto.get(0).getActualQty() : 0D);
                    if (BigDecimal.ZERO.compareTo(add1) == 0) {
                        mtInstructionVO.setInstructionStatus(WmsConstant.DELIVIERY_STATUS_VALID_RELEASED);
                    } else if (add1.compareTo(new BigDecimal(dto.getQuantity())) == -1) {
                        mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.EXECUTE);
                    } else if (add1.compareTo(new BigDecimal(dto.getQuantity())) == 0) {
                        mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.COMPLETED);
                    }
                    mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, WmsConstant.KEY_IFACE_STATUS_NEW);
                    //}
                }
            });
        }

        return null;
    }

    @Override
    public WmsProductionRequisitionMaterialExecutionLineDTO queryLine(Long tenantId, String instructionDocId, String instructionId) {
        WmsProductionRequisitionMaterialExecutionLineDTO returnDto = new WmsProductionRequisitionMaterialExecutionLineDTO();
        List<WmsProductionRequisitionMaterialExecutionLineDTO> instructionList = wmsInstructionSnRelMapper.getInstructionList(tenantId, singletonList(instructionDocId), instructionId);
        if (CollectionUtils.isNotEmpty(instructionList)) {
            BeanUtils.copyProperties(instructionList.get(0), returnDto);

            String meaning = lovAdapter.queryLovMeaning("WX.WMS_C/R_DOC_LINE_STATUS", tenantId, instructionList.get(0).getInstructionStatus());
            returnDto.setInstructionStatusMeaning(meaning);
            //????????????
            List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository.instructionLimitActualDetailQuery(tenantId, instructionList.get(0).getInstructionId());
            List<String> collect2 = mtInstructionActualDetails.stream().map(MtInstructionActualDetail::getActualId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect2)) {
                //List<MtInstructionActualVO3> mtInstructionActualVO3s1 = mtInstructionActualRepository.instructionActualPropertyBatchGet(tenantId, collect2);
                returnDto.setNowBarCodeQuantity(new Double(collect2.size()));
            } else {
                returnDto.setNowBarCodeQuantity(0D);
            }
            //????????????
            //??????????????????
            if (StringUtils.isBlank(instructionList.get(0).getMaterialVersion())) {
                returnDto.setMaterialVersion("");
            }
            List<LovValueDTO> LovValueDTOs = lovAdapter.queryLovValue("WX.WMS.LOCATOR_TYPE_LIMIT", tenantId);
            MtModLocatorVO16 materialLocator = new MtModLocatorVO16();
            if(CollectionUtils.isNotEmpty(LovValueDTOs)){
                List<String> locatorTypeList = LovValueDTOs.stream().map(LovValueDTO::getValue).collect(toList());
                materialLocator = wmsOutSourceMapper.getMaterialLocatorCodeByType(tenantId, instructionList.get(0).getMaterialId(),
                        instructionList.get(0).getSiteId(), instructionList.get(0).getLocatorId(), instructionList.get(0).getMaterialVersion(),locatorTypeList);
            }else{
                materialLocator = wmsOutSourceMapper.getMaterialLocatorCode(tenantId, instructionList.get(0).getMaterialId(),
                        instructionList.get(0).getSiteId(), instructionList.get(0).getLocatorId(), instructionList.get(0).getMaterialVersion());
            }
            if (materialLocator != null) {
                returnDto.setRecommendedLocatorCode(materialLocator.getLocatorCode());
            }

        }
        return returnDto;
    }


    private List<WmsProductionRequisitionMaterialExecutionLineDTO> newMaterialCodeCheck(Long tenantId, WmsProductionRequisitionMaterialExecutionLineDTO dto, String codeId, Boolean flag) {
        List<WmsProductionRequisitionMaterialExecutionLineDTO> lineDTOList = new ArrayList<>();
        //???????????????
        List<WmsProductionRequisitionMaterialExecutionLineDTO> instructionList = wmsInstructionSnRelMapper.getInstructionList(tenantId, Collections.singletonList(dto.getInstructionDocId()), null);

        //?????????????????????
        List<WmsInstructionSnRelVO> wmsInstructionSnRelVOS = wmsInstructionSnRelMapper.selectInstruction(tenantId);
        //???????????????
        WmsProductionRequisitionMaterialExecutionDetailDTO materialLotCodeCheck = wmsInstructionSnRelMapper.materialLotCodeCheck(tenantId, "", codeId);
        if (materialLotCodeCheck != null) {
            //????????????
            if (CollectionUtils.isNotEmpty(instructionList)) {
                List<WmsProductionRequisitionMaterialExecutionLineDTO> version = instructionList;
                //????????????
                //instructionList.forEach(line -> WmsCommonUtils.processValidateMessage(tenantId, !StringCommonUtils.contains(line.getInstructionStatus(), RELEASED, EXECUTE), "WMS_STOCKTAKE_001", WMS, line.getInstructionNum(), line.getSoLineNum(), line.getSoNum()));
                instructionList = instructionList.stream().filter(line -> StringCommonUtils.contains(line.getInstructionStatus(), RELEASED, EXECUTE)).collect(Collectors.toList());
                WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(instructionList), "WMS_STOCKTAKE_001", WMS, "?????????", version.stream().map(line -> line.getInstructionLineNum()).collect(Collectors.joining("-")), version.stream().map(line -> line.getInstructionStatus()).collect(Collectors.joining("-")));

                //?????????
                WmsCommonUtils.processValidateMessage(tenantId, !YES.equals(materialLotCodeCheck.getEnableFlag()), "WMS_DISTRIBUTION_0005", WMS, materialLotCodeCheck.getMaterialLotCode());
                //????????????
                WmsCommonUtils.processValidateMessage(tenantId, YES.equals(materialLotCodeCheck.getFreezeFlag()), "WMS_COST_CENTER_0025", WMS, materialLotCodeCheck.getMaterialLotCode());
                //?????????????????????Y
                WmsCommonUtils.processValidateMessage(tenantId, YES.equals(materialLotCodeCheck.getStocktakeFlag()), "WMS_COST_CENTER_0034", WMS, materialLotCodeCheck.getMaterialLotCode());
                //?????????
                WmsCommonUtils.processValidateMessage(tenantId, YES.equals(materialLotCodeCheck.getMfFlag()), "WMS_DISTRIBUTION_0003", WMS, materialLotCodeCheck.getMaterialLotCode());
                //??????????????????
                List<String> instructionIds = instructionList.stream().map(WmsProductionRequisitionMaterialExecutionLineDTO::getInstructionId).collect(Collectors.toList());
                List<MtInstructionActualDetail> select = wmsInstructionSnRelMapper.instructionDetail(tenantId, materialLotCodeCheck.getMaterialLotId(), instructionIds);
                WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isNotEmpty(select), "WMS_DISTRIBUTION_0006", WMS, materialLotCodeCheck.getMaterialLotCode());
                //????????????
                version = instructionList;
                instructionList = instructionList.stream().filter(line -> line.getMaterialId().equals(materialLotCodeCheck.getMaterialId())).collect(Collectors.toList());
                WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(instructionList), "WMS_DISTRIBUTION_0007", WMS, materialLotCodeCheck.getMaterialLotCode(), version.stream().map(line -> line.getInstructionLineNum()).collect(Collectors.joining("-")));

                //????????????
                instructionList = instructionList.stream().filter(line -> line.getUomId().equals(materialLotCodeCheck.getUomId())).collect(Collectors.toList());
                WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(instructionList), "WMS_DISTRIBUTION_0004", WMS, materialLotCodeCheck.getMaterialLotCode());

                //????????????
                version = instructionList;
                instructionList = instructionList.stream().filter(line -> StringCommonUtils.equalsIgnoreBlank(line.getMaterialVersion(), materialLotCodeCheck.getMaterialVersion())).collect(Collectors.toList());
                WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(instructionList), "WMS_DISTRIBUTION_0008", WMS, materialLotCodeCheck.getMaterialLotCode(), version.stream().map(line -> line.getInstructionLineNum()).collect(Collectors.joining("-")));

                //??????
                version = instructionList;
                instructionList = instructionList.stream().filter(line -> line.getLocatorId().equals(materialLotCodeCheck.getParentLocatorId())).collect(Collectors.toList());
                WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(instructionList), "WX.WMS_PRODUCTION_0007", WMS, materialLotCodeCheck.getMaterialLotCode(), version.stream().map(line -> line.getInstructionLineNum()).collect(Collectors.joining("-")));

                //????????????
                String meaning = lovAdapter.queryLovMeaning("WMS.MTLOT.STATUS", tenantId, materialLotCodeCheck.getStatus());
                WmsCommonUtils.processValidateMessage(tenantId, !INSTOCK.equals(materialLotCodeCheck.getStatus()), "WMS_DISTRIBUTION_0010", WMS, materialLotCodeCheck.getMaterialLotCode(), StringUtils.isNotEmpty(meaning) ? meaning : "");
                //????????????????????????
                WmsCommonUtils.processValidateMessage(tenantId, !OK.equals(materialLotCodeCheck.getQuantityStatus()), "WMS_DISTRIBUTION_0011", WMS, materialLotCodeCheck.getMaterialLotCode(), materialLotCodeCheck.getQuantityStatus());

                List<WmsProductionRequisitionMaterialExecutionLineDTO> collect = instructionList.stream().filter(line -> StringUtils.equals(line.getSpecStockFlag(), WmsConstant.KEY_IFACE_STATUS_ERROR)).collect(toList());
                List<WmsProductionRequisitionMaterialExecutionLineDTO> collect1 = instructionList.stream().filter(line -> !StringUtils.equals(line.getSpecStockFlag(), WmsConstant.KEY_IFACE_STATUS_ERROR)).collect(toList());

                if (CollectionUtils.isNotEmpty(collect)) {
                    version = instructionList;
                    List<String> instructionIdList1 = new ArrayList<>();
                    List<String> instructionIdList3 = new ArrayList<>();
                    for(WmsProductionRequisitionMaterialExecutionLineDTO wmsProductionRequisitionMaterialExecutionLineDTO:instructionList){
                        if(WmsConstant.KEY_IFACE_STATUS_ERROR.equals(wmsProductionRequisitionMaterialExecutionLineDTO.getSpecStockFlag()) && ((StringCommonUtils.equalsIgnoreBlank(wmsProductionRequisitionMaterialExecutionLineDTO.getSoNum(), materialLotCodeCheck.getSoNum()) && StringCommonUtils.equalsIgnoreBlank(wmsProductionRequisitionMaterialExecutionLineDTO.getSoLineNum(), materialLotCodeCheck.getSoLineNum())))){
                            instructionIdList1.add(wmsProductionRequisitionMaterialExecutionLineDTO.getInstructionId());
                        }
                        if(WmsConstant.KEY_IFACE_STATUS_ERROR.equals(wmsProductionRequisitionMaterialExecutionLineDTO.getSpecStockFlag())){
                            instructionIdList3.add(wmsProductionRequisitionMaterialExecutionLineDTO.getInstructionId());
                        }
                    }
                    if(CollectionUtils.isNotEmpty(instructionIdList1)){
                        instructionList = instructionList.stream().filter(item ->instructionIdList1.contains(item.getInstructionId())).collect(toList());
                    }else{
                        if(CollectionUtils.isNotEmpty(instructionIdList3)){
                            instructionList = instructionList.stream().filter(item ->!instructionIdList3.contains(item.getInstructionId())).collect(toList());
                        }
                    }
                    if(CollectionUtils.isEmpty(instructionList)){
                        WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(instructionList), "WMS_DISTRIBUTION_0009", WMS, materialLotCodeCheck.getMaterialLotCode(), version.stream().map(line -> line.getInstructionLineNum()).collect(Collectors.joining("-")));
                    }
//                    //???????????????
//                    instructionList = instructionList.stream().filter(line -> StringCommonUtils.equalsIgnoreBlank(line.getSoNum(), materialLotCodeCheck.getSoNum())).collect(Collectors.toList());
//                    WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(instructionList), "WMS_DISTRIBUTION_0009", WMS, materialLotCodeCheck.getMaterialLotCode(), version.stream().map(line -> line.getInstructionLineNum()).collect(Collectors.joining("-")));
//                    //??????????????????
//                    instructionList = instructionList.stream().filter(line -> StringCommonUtils.equalsIgnoreBlank(line.getSoLineNum(), materialLotCodeCheck.getSoLineNum())).collect(Collectors.toList());
//                    WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(instructionList), "WX.WMS_PRODUCTION_0017", WMS, materialLotCodeCheck.getMaterialLotCode(), version.stream().map(line -> line.getInstructionLineNum()).collect(Collectors.joining("-")));
                }
                if(CollectionUtils.isNotEmpty(collect1)){
                    version = instructionList;
                    List<String> instructionIdList2 = new ArrayList<>();
                    for(WmsProductionRequisitionMaterialExecutionLineDTO wmsProductionRequisitionMaterialExecutionLineDTO:instructionList){
                        if(!WmsConstant.KEY_IFACE_STATUS_ERROR.equals(wmsProductionRequisitionMaterialExecutionLineDTO.getSpecStockFlag())){
                            instructionIdList2.add(wmsProductionRequisitionMaterialExecutionLineDTO.getInstructionId());
                        }
                    }
                    if(StringUtils.isBlank(materialLotCodeCheck.getSoNum()) && StringUtils.isBlank(materialLotCodeCheck.getSoLineNum())){
                        instructionList = instructionList.stream().filter(item ->instructionIdList2.contains(item.getInstructionId())).collect(toList());
                    }else{
                        instructionList = instructionList.stream().filter(item ->!instructionIdList2.contains(item.getInstructionId())).collect(toList());
                    }
                    if(CollectionUtils.isEmpty(instructionList)){
                        WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isEmpty(instructionList), "WMS_DISTRIBUTION_0009", WMS, materialLotCodeCheck.getMaterialLotCode(),version.stream().map(line -> line.getInstructionLineNum()).collect(Collectors.joining("-")));
                    }
                }
                if (CollectionUtils.isNotEmpty(collect) || CollectionUtils.isNotEmpty(collect1)) {
                    //LOCATOR_TYPE= 14???LOCATOR_CATEGORY= AREA?????????LOCATOR_ID??????
                    WmsCommonUtils.processValidateMessage(tenantId, MtIdHelper.isIdNotNull(materialLotCodeCheck.getLocatorId()), "WMS_DISTRIBUTION_0012", WMS, materialLotCodeCheck.getMaterialLotCode());
                    //????????????sn
                    List<WmsInstructionSnRelVO> wmsInstructionSnRelVOList1 = wmsInstructionSnRelVOS.stream().filter(item -> item.getMaterialLotId().equals(materialLotCodeCheck.getMaterialLotId())).collect(toList());

                    //??????????????????
                    if (CollectionUtils.isEmpty(wmsInstructionSnRelVOList1)) {
                        List<String> instructionIdList = wmsInstructionSnRelVOS.stream().map(WmsInstructionSnRelVO::getInstructionId).collect(toList());
                        instructionList = instructionList.stream().filter(item -> !instructionIdList.contains(item.getInstructionId())).collect(toList());
                        if (CollectionUtils.isEmpty(instructionList)) {
                            throw new MtException("WX_WMS_SO_DELIVERY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WX_WMS_SO_DELIVERY_0010", "WMS"));
                        }
                    } else {
                        //???????????????
                        List<String> instructionIdList = wmsInstructionSnRelVOList1.stream().map(WmsInstructionSnRelVO::getInstructionId).collect(toList());
                        List<WmsProductionRequisitionMaterialExecutionLineDTO> lineList2 = instructionList.stream().filter(item -> instructionIdList.contains(item.getInstructionId())).collect(toList());
                        if (CollectionUtils.isEmpty(lineList2)) {
                            //???????????????????????????????????????
                            //????????????????????????????????????
                            List<MtInstruction> mtInstructions = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIdList);
                            for (MtInstruction mtInstruction : mtInstructions) {
                                //?????????????????????????????????
                                List<LovValueDTO> status = lovAdapter.queryLovValue("WX.WMS.AUTO_SN_DOC_STATUS_LIMIT", tenantId);
                                List<LovValueDTO> statusList = status.stream().filter(item -> item.getValue().equals(mtInstruction.getInstructionStatus())).collect(Collectors.toList());
                                if (CollectionUtils.isEmpty(statusList)) {
                                    //???????????????
                                    //????????????num
                                    MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.instructionDocPropertyGet(tenantId, mtInstruction.getSourceDocId());
                                    throw new MtException("WX_WMS_SO_DELIVERY_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WX_WMS_SO_DELIVERY_0009", "WMS", materialLotCodeCheck.getMaterialLotCode(), mtInstructionDoc.getInstructionDocNum()));
                                }
                            }
                        } else {
                            //????????????????????????????????????
                            instructionList = lineList2;
                        }
                    }


                    //xvi.	??????????????????{ primaryUomQty }?????????0
                    WmsCommonUtils.processValidateMessage(tenantId, materialLotCodeCheck.getPrimaryUomQty() == 0, "WMS_PUT_IN_STOCK_013", WMS, materialLotCodeCheck.getMaterialLotCode());
                    //xvii.	???????????????+???????????????????????????????????????????????????????????????
                    if (materialLotCodeCheck.getPrimaryUomQty() == null) {
                        materialLotCodeCheck.setPrimaryUomQty(0D);
                    }
                    if (new BigDecimal(instructionList.get(0).getQuantity()).compareTo(new BigDecimal(materialLotCodeCheck.getPrimaryUomQty())) < 0) {
                        throw new MtException("WMS_COST_CENTER_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0019", "WMS", materialLotCodeCheck.getMaterialLotCode(), String.valueOf(materialLotCodeCheck.getPrimaryUomQty()), String.valueOf(instructionList.get(0).getQuantity())));
                    }

                }
                //???????????????????????????????????????????????????
       /*         List<WmsProductionRequisitionMaterialExecutionDetailDTO> lineMaterialLotQty = materialLotCodeQuery(tenantId, instructionList.get(0));
                if(CollectionUtils.isNotEmpty(lineMaterialLotQty)){
                    double sum = lineMaterialLotQty.mapToDouble(WmsProductionRequisitionMaterialExecutionDetailDTO::getPrimaryUomQty).sum();
                    BigDecimal subtract = new BigDecimal(instructionList.get(0).getQuantity()).subtract(new BigDecimal(sum));
                    if(subtract.compareTo(new BigDecimal(materialLotCodeCheck.getPrimaryUomQty()))==-1){
                        throw new MtException("WMS_COST_CENTER_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0019", "WMS", materialLotCodeCheck.getMaterialLotCode(), String.valueOf(materialLotCodeCheck.getPrimaryUomQty()), String.valueOf(instructionList.get(0).getQuantity())));
                    }

                }*/
                instructionList.get(0).setDetailDTOList(Collections.singletonList(materialLotCodeCheck));
                updateBarcode(tenantId, instructionList, dto.getBarCode());
                //??????
                List<WmsProductionRequisitionMaterialExecutionDetailDTO> wmsProductionRequisitionMaterialExecutionDetailDTOS = materialLotCodeQuery(tenantId, instructionList.get(0));
       /*         List<WmsProductionRequisitionMaterialExecutionDetailDTO> collect1 = wmsProductionRequisitionMaterialExecutionDetailDTOS.stream().filter(line -> StringCommonUtils.contains(line.getMaterialLotId(), materialLotCodeCheck.getMaterialLotId())).collect(Collectors.toList());
                WmsCommonUtils.processValidateMessage(tenantId, CollectionUtils.isNotEmpty(collect1), "WX.WMS_PRODUCTION_0002", WMS);
*/
                Stream<Double> doubleStream = wmsProductionRequisitionMaterialExecutionDetailDTOS.stream().map(WmsProductionRequisitionMaterialExecutionDetailDTO::getActualQty);
                double sum = doubleStream.mapToDouble(Double::doubleValue).sum();
                instructionList.get(0).setNowBarCodeQuantity(new Double(wmsProductionRequisitionMaterialExecutionDetailDTOS.size()));
                instructionList.get(0).setActualQuantity(sum);
                if (BigDecimal.valueOf(sum).compareTo(BigDecimal.valueOf(instructionList.get(0).getQuantity())) > 0) {
                    if (flag.equals("true")) {
                        throw new MtException("WMS_COST_CENTER_0079", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0079", "WMS"));
                    } else {
                        throw new MtException("WMS_COST_CENTER_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0019", "WMS", materialLotCodeCheck.getMaterialLotCode(), String.valueOf(sum), String.valueOf(instructionList.get(0).getQuantity())));
                    }
                }
                MtInstruction mtInstruction = mtInstructionRepository.selectByPrimaryKey(instructionList.get(0).getInstructionId());
                instructionList.get(0).setInstructionStatus(mtInstruction.getInstructionStatus());
                instructionList.get(0).setDetailDTOList(wmsProductionRequisitionMaterialExecutionDetailDTOS);
                String instrctionId = instructionList.get(0).getInstructionId();
                List<WmsProductionRequisitionMaterialExecutionLineDTO> wmsProductionRequisitionMaterialExecutionLineDTOS = dto.getDocLineList().stream().filter(item ->item.getInstructionId().equals(instrctionId)).collect(toList());
                if(CollectionUtils.isNotEmpty(wmsProductionRequisitionMaterialExecutionLineDTOS)){
                    if(StringUtils.isNotBlank(wmsProductionRequisitionMaterialExecutionLineDTOS.get(0).getTaskNum())){
                        List<ItfLightTaskIfaceDTO> dtoList = new ArrayList<>();
                        ItfLightTaskIfaceDTO itfLightTaskIfaceDTO = new ItfLightTaskIfaceDTO();
                        itfLightTaskIfaceDTO.setTaskNum(wmsProductionRequisitionMaterialExecutionLineDTOS.get(0).getTaskNum());
                        itfLightTaskIfaceDTO.setTaskStatus("OFF");
                        dtoList.add(itfLightTaskIfaceDTO);
                        List<ItfLightTaskIfaceVO> itfLightTaskIfaceVOS = itfLightTaskIfaceService.itfLightTaskIface(tenantId,dtoList);
                        if(CollectionUtils.isNotEmpty(itfLightTaskIfaceVOS)){
                            instructionList.get(0).setStatus(itfLightTaskIfaceVOS.get(0).getStatus());
                            instructionList.get(0).setTaskStatus("OFF");
                            instructionList.get(0).setLightStatus("OFF");
                            instructionList.get(0).setMessage(itfLightTaskIfaceVOS.get(0).getMessage());
                        }
                    }
                }
                return instructionList;
            }
        }
        return null;
    }

    //???????????????????????????
    private WmsProductionRequisitionMaterialExecutionLineDTO checkLoad(Long tenantId, List<String> materialLotIds, String unBundingFlag) {
        WmsProductionRequisitionMaterialExecutionLineDTO returnResults = new WmsProductionRequisitionMaterialExecutionLineDTO();
        for (String materialLotId : materialLotIds) {
            //???????????????????????????
            WmsProductionRequisitionMaterialExecutionDetailDTO materialLotCodeCheck = wmsInstructionSnRelMapper.materialLotCodeCheck(tenantId, "", materialLotId);
            //???????????????????????????API[objectLimitLoadingContainerQuery]
            if (StringUtils.isNotBlank(materialLotCodeCheck.getCurrentContainerId())) {
                if (StringUtils.isEmpty(unBundingFlag)) {
                    MtContainer mtContainer = new MtContainer();
                    mtContainer.setTenantId(tenantId);
                    mtContainer.setContainerId(materialLotCodeCheck.getCurrentContainerId());
                    MtContainer mtContainer1 = mtContainerRepository.selectByPrimaryKey(mtContainer);
                    returnResults.setUnbundingFlag(WmsConstant.CONSTANT_Y);
                    returnResults.setMaterialContainerCode(mtContainer1.getContainerCode());
                    returnResults.setMaterialLotCode(materialLotCodeCheck.getMaterialLotCode());
                    return returnResults;

                } else if (StringUtils.isNotBlank(unBundingFlag) && StringUtils.equals(unBundingFlag, WmsConstant.CONSTANT_Y)) {
                    //??????????????????
                    String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "PRODUCTION_MATERIAL_PICK_UNLOAD");
                    // ??????API{ containerUnload }????????????
                    MtContainerVO25 mtContainerVO22 = new MtContainerVO25();
                    mtContainerVO22.setContainerId(materialLotCodeCheck.getCurrentContainerId());
                    mtContainerVO22.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                    mtContainerVO22.setLoadObjectId(materialLotCodeCheck.getMaterialLotId());
                    mtContainerVO22.setEventRequestId(eventRequestId);
                    mtContainerRepository.containerUnload(tenantId, mtContainerVO22);
                }
            }
        }
        return null;
    }
}
