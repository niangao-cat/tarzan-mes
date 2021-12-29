package com.ruike.wms.infra.repository.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.entity.WmsMaterialLotDocRel;
import com.ruike.wms.domain.repository.WmsInstructionSnRelRepository;
import com.ruike.wms.domain.repository.WmsInvTransferIssueRepository;
import com.ruike.wms.domain.repository.WmsMaterialLotDocRelRepository;
import com.ruike.wms.domain.vo.WmsInstructionSnRelVO;
import com.ruike.wms.domain.vo.WmsInvTransferObjectTrxVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsProductPrepareLineVO;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsCostCtrMaterialMapper;
import com.ruike.wms.infra.mapper.WmsInstructionSnRelMapper;
import com.ruike.wms.infra.mapper.WmsInvTransferIssueMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDetail;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDetailRepository;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionDetailVO3;
import tarzan.instruction.domain.vo.MtInstructionDetailVO4;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlVO10;
import tarzan.inventory.domain.vo.MtContLoadDtlVO4;
import tarzan.inventory.domain.vo.MtContainerVO13;
import tarzan.inventory.domain.vo.MtMaterialLotVO3;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-27 10:20
 */
@Component
public class WmsInvTransferIssueRepositoryImpl implements WmsInvTransferIssueRepository {

    @Autowired
    private WmsInvTransferIssueMapper wmsInvTransferIssueMapper;

    @Autowired
    MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private MtContainerLoadDetailRepository containerLoadDetailRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtInstructionDetailRepository mtInstructionDetailRepository;

    @Autowired
    private MtInstructionActualDetailRepository mtInstructionActualDetailRepository;

    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;

    @Autowired
    private WmsInstructionSnRelMapper wmsInstructionSnRelMapper;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Override
    public WmsInvTransferDTO docHeaderQuery(Long tenantId, String docBarCode) {
        return wmsInvTransferIssueMapper.selectDocCondition(tenantId, docBarCode);
    }

    @Override
    public List<WmsInvTransferDTO2> docLineQuery(Long tenantId, String sourceDocId, String instructionId, String type) {
        return wmsInvTransferIssueMapper.selectDocLineCondition(tenantId, sourceDocId, instructionId, type);
    }

    @Override
    public List<WmsCostCtrMaterialDTO3> containerOrMaterialLotQuery(Long tenantId, String type, WmsInvTransferDTO3 dto) {
        if (StringUtils.isEmpty(dto.getBarCode())) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "barCode"));
        }
//        if (StringUtils.isEmpty(dto.getLocatorCode()) && WmsConstant.TransferType.RECEIPT.equals(type)) {
//            throw new MtException("WMS_INV_TRANSFER_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "WMS_INV_TRANSFER_0016", WmsConstant.ConstantValue.WMS));
//        }
        if (WmsConstant.TransferType.RECEIPT.equals(type) && StringUtils.isEmpty(dto.getInstructionDocType())) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "instructionDocType"));
        }
        if (CollectionUtils.isEmpty(dto.getDocLineList())) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "docLineList"));
        }
        // 获取条码类型及物料批
        WmsCostCtrMaterialDTO6 materialLotDto = materialLotInfoQuery(tenantId, dto.getBarCode());
        return materialLotQuery(tenantId, type, materialLotDto, dto);
    }

    @Override
    public WmsInvTransferDTO7 docDetailQuery(Long tenantId, String type, WmsInvTransferDTO6 dto) {
        if (dto.getDocLine() == null) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "docLine"));
        }
        if (dto.getInstructionDocType() == null) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "instructionDocType"));
        }
        // 获取行明细信息
        WmsInvTransferDTO2 docLine = dto.getDocLine();
        // 获取本次扫码条码信息
        List<WmsCostCtrMaterialDTO3> barDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getBarCodes())) {
            List<String> barCodeList = dto.getBarCodes();
            for(String barCode : barCodeList){
                WmsCostCtrMaterialDTO6 materialLotDto = materialLotInfoQuery(tenantId, barCode);
                List<String> materialLotIds = materialLotDto.getMaterialLotIds();
                if (CollectionUtils.isNotEmpty(materialLotIds)) {
                    List<WmsCostCtrMaterialDTO3> materialDtoList = wmsInvTransferIssueMapper.selectMaterialLotCondition(tenantId, materialLotIds);
                    materialDtoList.removeIf(item -> !(StringUtils.equals(docLine.getMaterialId(), item.getMaterialId()) &&
                            (StringUtils.equals(Optional.ofNullable(docLine.getMaterialVersion()).orElse(""), Optional.ofNullable(item.getMaterialVersion()).orElse("")) || StringUtils.isEmpty(docLine.getMaterialVersion()))));
                    materialDtoList.forEach(m ->{
                        m.setContainerId(materialLotDto.getContainerId());
                        m.setContainerCode(StringUtils.isEmpty(materialLotDto.getContainerId()) ? null : barCode);
                    });
                    barDtoList.addAll(materialDtoList);
                }
            }
            // 前台缓存数据标记
            barDtoList.forEach(barDto ->{
                barDto.setCacheFlag(WmsConstant.CONSTANT_Y);
            });
        }
        // 获取历史条码信息
        WmsInvTransferDTO7 returnDto = new WmsInvTransferDTO7();
        BeanUtils.copyProperties(docLine, returnDto);
        /*WmsMaterialLotDocRel lotDocRel = new WmsMaterialLotDocRel();
        lotDocRel.setTenantId(tenantId);
        lotDocRel.setInstructionDocId(dto.getInstructionDocId());
        lotDocRel.setInstructionId(docLine.getInstructionId());
        List<WmsMaterialLotDocRel> lotDocRelList = wmsMaterialLotDocRelRepository.select(lotDocRel);*/
        List<String> matLotIds = new ArrayList<>();
        // 单据类型为发出接收执行时取指令明细
        if (WmsConstant.TransferType.ISSUE.equals(type) && StringUtils.equals(dto.getInstructionDocType(), WmsConstant.DocType.SEND_RECEIVE_EXECUTE)) {
            List<MtInstructionDetailVO3> mtInstructionDetailVO3s = new ArrayList<>();
            MtInstructionDetailVO3 detailVO3 = new MtInstructionDetailVO3();
            detailVO3.setInstructionId(docLine.getInstructionId());
            mtInstructionDetailVO3s.add(detailVO3);
            List<MtInstructionDetailVO4> mtInstructionDetails = mtInstructionDetailRepository
                    .propertyLimitInstructionDetailBatchQuery(tenantId, mtInstructionDetailVO3s);
            matLotIds = mtInstructionDetails.stream().map(MtInstructionDetailVO4::getMaterialLotId).collect(Collectors.toList());
        } else {
            List<String> instructionIdList = Arrays.asList(docLine.getInstructionId());
            List<MtInstructionActualDetailVO2> detailList =
                    mtInstructionActualDetailRepository.instructionLimitActualDetailBatchQuery(tenantId, instructionIdList);
            matLotIds = detailList.stream().map(MtInstructionActualDetailVO2::getMaterialLotId).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(matLotIds)) {
            List<WmsCostCtrMaterialDTO3> materialDtoList = wmsInvTransferIssueMapper.selectMaterialLotCondition(tenantId, matLotIds);
            materialDtoList.forEach(barDto -> {
                barDto.setCacheFlag(WmsConstant.ConstantValue.NO);
            });
            barDtoList.addAll(materialDtoList);
        }
        returnDto.setBarDtoList(barDtoList);
        return returnDto;
    }

    @Override
    public WmsCostCtrMaterialDTO6 materialLotInfoQuery(Long tenantId, String barCode) {
        MtContainerVO13 containerVo13 = new MtContainerVO13();
        containerVo13.setContainerCode(barCode);
        List<String> containerIds = mtContainerRepository.propertyLimitContainerQuery(tenantId, containerVo13);
        List<String> materialLotIds = null;
        String containerId = null;
        String codeType = null;
        if (!CollectionUtils.isEmpty(containerIds)) {
            containerId = containerIds.get(0);
            // 校验扫描的容器条码是否存在顶层容器
            String finalContainerId = containerId;
            MtContainer mtContainer = mtContainerRepository.selectOne(new MtContainer() {{
                setTenantId(tenantId);
                setContainerId(finalContainerId);
            }});
            if (StringUtils.isNotEmpty(mtContainer.getTopContainerId())) {
                throw new MtException("WMS_COST_CENTER_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0029", WmsConstant.ConstantValue.WMS, barCode));
            }
            MtContLoadDtlVO10 contLoadDtlVo10 = new MtContLoadDtlVO10();
            // 获取所有层级
            contLoadDtlVo10.setAllLevelFlag(WmsConstant.ConstantValue.YES);
            contLoadDtlVo10.setContainerId(containerId);
            List<MtContLoadDtlVO4> contLoadDtls =
                    containerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, contLoadDtlVo10);
            if (CollectionUtils.isEmpty(contLoadDtls)) {
                throw new MtException("WMS_COST_CENTER_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0005", WmsConstant.ConstantValue.WMS, barCode));
            }
            codeType = HmeConstants.LoadTypeCode.CONTAINER;
            materialLotIds = contLoadDtls.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(Collectors.toList());
            //条码存在性校验
            if(CollectionUtils.isEmpty(materialLotIds)){
                throw new MtException("WMS_COST_CENTER_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0005", WmsConstant.ConstantValue.WMS, barCode));
            }
        } else {

            // 检验条码是否为物料批
            MtMaterialLotVO3 materialLotVo3 = new MtMaterialLotVO3();
            materialLotVo3.setMaterialLotCode(barCode);
            materialLotIds = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotVo3);
            if (CollectionUtils.isEmpty(materialLotIds)) {
                // 条码返回错误信息
                throw new MtException("WMS_COST_CENTER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0006", WmsConstant.ConstantValue.WMS, barCode));
            }
            // 校验扫描的物料批条码是否存在顶层容器
            String materialLotId = materialLotIds.get(0);
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotId);
            if (StringUtils.isNotEmpty(mtMaterialLot.getTopContainerId())) {
                throw new MtException("WMS_COST_CENTER_0030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0030", WmsConstant.ConstantValue.WMS, barCode));
            }
            //条码的上层容器id为空校验
            if (StringUtils.isNotBlank(mtMaterialLot.getCurrentContainerId())) {
                throw new MtException("WMS_COST_CENTER_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0035", WmsConstant.ConstantValue.WMS, barCode));
            }
            codeType = HmeConstants.LoadTypeCode.MATERIAL_LOT;
        }
        WmsCostCtrMaterialDTO6 materialLotDto = new WmsCostCtrMaterialDTO6();
        materialLotDto.setContainerId(containerId);
        materialLotDto.setCodeType(codeType);
        materialLotDto.setMaterialLotIds(materialLotIds);
        return materialLotDto;
    }

    @Override
    public void addObjectTransaction(WmsInvTransferObjectTrxVO dto,
                                     WmsCostCtrMaterialDTO3 lotDto,
                                     List<WmsObjectTransactionRequestVO> objectTransactionList) {
        objectTransactionList.add(new WmsObjectTransactionRequestVO() {{
            setTransactionId(null);
            setTransactionTypeCode(dto.getTransactionTypeCode());
            setEventId(dto.getEventId());
            setBarcode(lotDto.getMaterialLotCode());
            setMaterialLotId(lotDto.getMaterialLotId());
            setPlantCode(lotDto.getSiteCode());
            setPlantId(lotDto.getSiteId());
            setMaterialCode(lotDto.getMaterialCode());
            setMaterialId(lotDto.getMaterialId());
            setTransactionQty(dto.getTransactionQty());
            setTransferLotNumber(lotDto.getLot());
            setLotNumber(lotDto.getLot());
            setTransactionUom(lotDto.getPrimaryUomCode());
            setTransactionTime(new Date());
            setWarehouseId(lotDto.getWarehouseId());
            setWarehouseCode(lotDto.getWarehouseCode());
            setLocatorCode(lotDto.getLocatorCode());
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
            setTransactionReasonCode("库存调拨");
            setMergeFlag("N");
        }});
    }

    private List<WmsCostCtrMaterialDTO3> materialLotQuery(Long tenantId, String type, WmsCostCtrMaterialDTO6 materialLotDto,
                                                       WmsInvTransferDTO3 barCodeDto) {
        String codeType = materialLotDto.getCodeType();
        String containerId = materialLotDto.getContainerId();
        String containerCode = StringUtils.isEmpty(containerId) ? null : barCodeDto.getBarCode();
        List<String> materialLotIds = materialLotDto.getMaterialLotIds();
        if (CollectionUtils.isEmpty(materialLotIds)) {
            return null;
        }
        List<WmsInvTransferDTO2> mateLineList = new ArrayList<>();
        List<WmsInvTransferDTO2> docLineList = barCodeDto.getDocLineList();
        List<WmsCostCtrMaterialDTO3> dtoList = wmsInvTransferIssueMapper.selectMaterialLotCondition(tenantId, materialLotIds);
        String isContainterCode = HmeConstants.LoadTypeCode.CONTAINER.equals(codeType) ? WmsConstant.CONSTANT_Y : WmsConstant.CONSTANT_N;
        Map<String, BigDecimal> materialQtyMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        String docType = barCodeDto.getInstructionDocType();
        //获取行指定信息
        List<WmsInstructionSnRelVO> wmsInstructionSnRelVOS = wmsInstructionSnRelMapper.selectInstruction(tenantId);
        // 容器条码下物料批的物料相同
        for (WmsCostCtrMaterialDTO3 dto : dtoList) {
            dto.setIsContainerCode(isContainterCode);
            dto.setContainerId(containerId);
            dto.setContainerCode(containerCode);
            // 匹配对应单据行（物料+版本唯一）
            List<WmsInvTransferDTO2> docLines = docLineList.stream().filter(item -> StringUtils.equals(dto.getMaterialId(), item.getMaterialId())
                    && (
                            (StringUtils.equals(Optional.ofNullable(dto.getMaterialVersion()).orElse(""), Optional.ofNullable(item.getMaterialVersion()).orElse(""))
                            || StringUtils.isEmpty(item.getMaterialVersion()))
                    )).collect(Collectors.toList());
            // 校验条码物料是否在库存调拨单据行物料中
            // 获取当前页面单据行信息
            List<String> materialIds = docLineList.stream().map(WmsInvTransferDTO2::getMaterialId)
                    .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
            //校验物料ID
            if (!materialIds.contains(dto.getMaterialId()) && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                throw new MtException("WMS_INV_TRANSFER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0006", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
            } else if (!materialIds.contains(dto.getMaterialId()) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                throw new MtException("WMS_INV_TRANSFER_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0007", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode()));
            }
            // 校验物料版本
            if (docLines.size() == 0 && "CONTAINER".equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0013", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
            } else if (docLines.size() == 0 && "MATERIAL_LOT".equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0014", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode()));
            }
            // 校验是否为在制品
            if(WmsConstant.CONSTANT_Y.equals(dto.getMfFlag()) && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)){
                throw new MtException("WMS_INV_TRANSFER_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0036", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(),dto.getMaterialLotCode()));
            } else if (WmsConstant.CONSTANT_Y.equals(dto.getMfFlag()) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                throw new MtException("WMS_INV_TRANSFER_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0035", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode()));
            }
            WmsInvTransferDTO2 docLine = docLines.get(0);
            // modify by jiangling.zheng @20200811 去除(kang.wang)
            /*if (StringUtils.isNotBlank(docLine.getToLocatorCode()) && !barCodeDto.getLocatorCode().equals(docLine.getToLocatorCode())) {
                if ("CONTAINER".equals(codeType)) {
                    throw new MtException("WMS_INV_TRANSFER_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_INV_TRANSFER_0017", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
                } else if ("MATERIAL_LOT".equals(codeType)) {
                    throw new MtException("WMS_INV_TRANSFER_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_INV_TRANSFER_0018", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode()));
                }
            }*/
            /*String locatorId = mtModLocatorRepository.selectOne(new MtModLocator() {{
                setLocatorCode(dto.getLocatorCode());
                setTenantId(tenantId);
            }}).getLocatorId();*/
            dto.setCacheLocatorId(docLine.getToLocatorId());
            dto.setCacheLocatorCode(docLine.getToLocatorCode());
            dto.setCacheLocatorName(docLine.getToLocatorName());
            if (WmsConstant.TransferType.ISSUE.equals(type) || (WmsConstant.TransferType.RECEIPT.equals(type) && WmsConstant.InstructionStatus.RECEIVE_EXECUTE.equals(docType))) {
                // 有效性校验
                if (!WmsConstant.CONSTANT_Y.equals(dto.getEnableFlag()) && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                    throw new MtException("WMS_COST_CENTER_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0007", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
                } else if (!WmsConstant.CONSTANT_Y.equals(dto.getEnableFlag()) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                    throw new MtException("WMS_COST_CENTER_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0009", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode()));
                }
                // 是否冻结校验
                if (WmsConstant.CONSTANT_Y.equals(dto.getFreezeFlag()) && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                    throw new MtException("WMS_COST_CENTER_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0024", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
                } else if (WmsConstant.CONSTANT_Y.equals(dto.getFreezeFlag()) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                    throw new MtException("WMS_COST_CENTER_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0025", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode()));
                }
                //条码盘点停用标识校验
                if(WmsConstant.CONSTANT_Y.equals(dto.getStocktakeFlag()) && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)){
                    throw new MtException("WMS_COST_CENTER_0033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0033", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(),dto.getMaterialLotCode()));
                } else if (WmsConstant.CONSTANT_Y.equals(dto.getStocktakeFlag()) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                    throw new MtException("WMS_COST_CENTER_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0034", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
                }
                // 质量状态校验
                if (!WmsConstant.ConstantValue.OK.equals(dto.getQualityStatus()) && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                    throw new MtException("WMS_COST_CENTER_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0008", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
                } else if (!WmsConstant.ConstantValue.OK.equals(dto.getQualityStatus()) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                    throw new MtException("WMS_COST_CENTER_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0010", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode()));
                }
                // 校验条码的仓库与对应的库存调拨单行中对应的物料行的发出仓库是否一致
                long warehouseCount = docLines.stream().filter(item ->
                        StringUtils.equals(dto.getWarehouseId(), item.getFromWarehouseId())
                ).count();
                if (warehouseCount == 0 && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                    throw new MtException("WMS_INV_TRANSFER_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_INV_TRANSFER_0008", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
                } else if (warehouseCount == 0 && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                    throw new MtException("WMS_INV_TRANSFER_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_INV_TRANSFER_0009", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode(), ""));
                }
                // 校验条码的货位与对应的库存调拨单行中对应的物料行的货位是否一致
                long locatorCount = docLines.stream().filter(item ->
                        // nvl(dto.getLocatorId(),"").equals(nvl("".equals(item.getToLocatorId()) ? null : item.getToLocatorId(),nvl(dto.getLocatorId(),"")))
                        StringUtils.equals(Optional.ofNullable(dto.getLocatorId()).orElse(""),
                                Optional.ofNullable(Optional.ofNullable("".equals(item.getFromLocatorId()) ? null :item.getFromLocatorId()).orElse(dto.getLocatorId())).orElse(""))
                ).count();
                if (locatorCount == 0 && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                    throw new MtException("WMS_INV_TRANSFER_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_INV_TRANSFER_0010", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
                } else if (locatorCount == 0 && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                    throw new MtException("WMS_INV_TRANSFER_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_INV_TRANSFER_0011", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode(), ""));
                }
                //校验物料条码主单位与指令行中对应的单位是否一致
                // add by wsg 2020803 超发逻辑
                long uomCount = docLines.stream().filter(item ->
                        StringUtils.equals(dto.getPrimaryUomId(), item.getUomId())
                ).count();
                if (uomCount == 0) {
                    if (HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                        throw new MtException("WMS_INV_TRANSFER_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_INV_TRANSFER_0025", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode(), ""));
                    } else if (HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                        throw new MtException("WMS_INV_TRANSFER_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_INV_TRANSFER_0026", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
                    }
                }
                //匹配指定sn
                List<WmsInstructionSnRelVO> wmsInstructionSnRelVOList1 = wmsInstructionSnRelVOS.stream().filter(item -> item.getMaterialLotId().equals(dto.getMaterialLotId())).collect(toList());
                //条码没被指定
                if (CollectionUtils.isEmpty(wmsInstructionSnRelVOList1)) {
                    List<String> instructionIds = wmsInstructionSnRelVOS.stream().map(WmsInstructionSnRelVO::getInstructionId).collect(toList());
                    docLines = docLines.stream().filter(item -> !instructionIds.contains(item.getInstructionId())).collect(toList());
                    if(CollectionUtils.isEmpty(docLines)){
                        throw new MtException("WX_WMS_SO_DELIVERY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WX_WMS_SO_DELIVERY_0010", "WMS"));
                    }
                } else {
                    //条码被指定
                    List<String> instructionIds = wmsInstructionSnRelVOList1.stream().map(WmsInstructionSnRelVO::getInstructionId).collect(toList());
                    List<WmsInvTransferDTO2> docLines2 = docLines.stream().filter(item -> instructionIds.contains(item.getInstructionId())).collect(toList());
                    if (CollectionUtils.isEmpty(docLines2)) {
                        //条码被指定的行不在该单据下
                        //获取该条码被指定的行状态
                        List<MtInstruction> mtInstructions = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIds);
                        for (MtInstruction mtInstruction : mtInstructions) {
                            //判断行状态是否在值集中
                            List<LovValueDTO> status = lovAdapter.queryLovValue("WX.WMS.AUTO_SN_DOC_STATUS_LIMIT", tenantId);
                            List<LovValueDTO> statusList = status.stream().filter(item -> item.getValue().equals(mtInstruction.getInstructionStatus())).collect(Collectors.toList());
                            if (CollectionUtils.isEmpty(statusList)) {
                                //不存在报错
                                //获取单据num
                                MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.instructionDocPropertyGet(tenantId, mtInstruction.getSourceDocId());
                                throw new MtException("WX_WMS_SO_DELIVERY_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WX_WMS_SO_DELIVERY_0009", "WMS", dto.getMaterialLotCode(),mtInstructionDoc.getInstructionDocNum()));
                            }
                        }
                    } else {
                        //条码被指定的行在该单据下
                        docLines = docLines2;
                    }
                }
                // 条码数量+扫描累计数量+物料行已执行数量是否超过制单数量
//                BigDecimal actualQty = docLine.getActualQty() == null ? BigDecimal.ZERO : docLine.getActualQty();
//                BigDecimal totalQty = actualQty.add(dto.getPrimaryUomQty());
//                if (totalQty.compareTo(docLine.getQuantity()) > 0 && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
//                    throw new MtException("WMS_COST_CENTER_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "WMS_COST_CENTER_0019", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(),
//                            totalQty.toString(), docLine.getQuantity().toString()));
//                } else if (totalQty.compareTo(docLine.getQuantity()) > 0 && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
//                    throw new MtException("WMS_COST_CENTER_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "WMS_COST_CENTER_0019", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode(),
//                            totalQty.toString(), docLine.getQuantity().toString()));
//                }
                // 条码数量+扫描累计数量+物料行已执行数量是否超过制单数量
                BigDecimal actualQty = Optional.ofNullable(docLine.getActualQty()).orElse(BigDecimal.ZERO);
                //当前物料已扫描未执行实物总数
                // modify by jiangling.zheng 2020-08-20 此逻辑不需要，累计扫描条码数量前端已计算，不需要后端重新计算
//                BigDecimal scannedMaterialQty = BigDecimal.ZERO;
                BigDecimal scannedMaterialQty = docLine.getCumExecuteQty();
                /*List<WmsCostCtrMaterialDTO3> scannedBarcodes = barCodeDto.getBarCodeList();
                //已扫描条码
                if (scannedBarcodes.size() > 0) {
                    //获取已扫描条码中与当前物料一致的条码数量和实物数量
                    List<WmsCostCtrMaterialDTO3> scannedMaterialCodeDtoList = scannedBarcodes.stream().filter(m ->
                            dto.getMaterialId().equals(m.getMaterialId()) && StringUtils.equals(Optional.ofNullable(dto.getMaterialVersion()).orElse(""),
                                    Optional.ofNullable(m.getMaterialVersion()).orElse(""))).collect(Collectors.toList());
                    BigDecimal primaryUomQty = CollectionUtils.isNotEmpty(scannedMaterialCodeDtoList)?
                            scannedMaterialCodeDtoList.stream().collect(CollectorsUtil.summingBigDecimal(m ->
                                    Optional.ofNullable(m.getPrimaryUomQty()).orElse(BigDecimal.ZERO))) : BigDecimal.ZERO;
                    scannedMaterialQty = scannedMaterialQty.add(primaryUomQty);
                }*/
                // end mod
                String materialQtyKey = dto.getMaterialId() + dto.getMaterialVersion();
                BigDecimal materialQty = Optional.ofNullable(materialQtyMap.get(materialQtyKey)).orElse(BigDecimal.ZERO);
                materialQtyMap.put(materialQtyKey, materialQty.add(dto.getPrimaryUomQty()));
                BigDecimal totalQty = actualQty.add(scannedMaterialQty).add(materialQty).add(dto.getPrimaryUomQty());
                //获取当前条码中所有相同物料
                // modify by jiangling.zheng 2020-08-20 此逻辑不需要，条码累计个数及数量前端已计算
                /*List<WmsCostCtrMaterialDTO3> currentSameMaterialList = dtoList.stream().filter(m ->
                        dto.getMaterialId().equals(m.getMaterialId()) && StringUtils.equals(Optional.ofNullable(dto.getMaterialVersion()).orElse(""),
                                Optional.ofNullable(m.getMaterialVersion()).orElse(""))).collect(Collectors.toList());
                int addCodeQty = currentSameMaterialList.size();
                BigDecimal addQuantity = currentSameMaterialList.stream().collect(CollectorsUtil.summingBigDecimal(m -> Optional.ofNullable(m.getPrimaryUomQty()).orElse(BigDecimal.ZERO)));
                dto.setAddCodeQty(addCodeQty);
                dto.setAddQuantity(addQuantity);*/
                // end mod
                if (totalQty.compareTo(docLine.getQuantity()) > 0) {//已执行+已扫描+当前条码数量>制单数量
                    if (actualQty.add(scannedMaterialQty).add(materialQty).compareTo(docLine.getQuantity()) > 0) {//已执行数量+已扫描数量+当前条码已统计数量>制单数量,已超发
                        if (HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                            throw new MtException("WMS_INV_TRANSFER_0027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_INV_TRANSFER_0027", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(),
                                    totalQty.toString(), docLine.getQuantity().toString()));
                        } else if (HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                            throw new MtException("WMS_INV_TRANSFER_0027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_INV_TRANSFER_0027", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode(),
                                    totalQty.toString(), docLine.getQuantity().toString(), ""));
                        }
                    } else {
                        String excessSetting = docLine.getExcessSetting();
                        // M为按实物条码超发 N 为零超发
                        if (!"M".equalsIgnoreCase(excessSetting)) {
                            if (HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                                throw new MtException("WMS_INV_TRANSFER_0027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WMS_INV_TRANSFER_0027", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(),
                                        totalQty.toString(), docLine.getQuantity().toString()));
                            } else if (HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                                throw new MtException("WMS_INV_TRANSFER_0027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WMS_INV_TRANSFER_0027", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode(),
                                        totalQty.toString(), docLine.getQuantity().toString(), ""));
                            }
                        }
                    }
                }
                // end add
                //去重
                /*if (docLineList.contains(docLine)) {
                    docLineList.removeIf(item -> item.getInstructionId().equals(docLine.getInstructionId()));
                }
                docLineList.add(docLine);*/
                mateLineList.add(docLine);
            } else if (WmsConstant.TransferType.RECEIPT.equals(type) && WmsConstant.DocType.SEND_RECEIVE_EXECUTE.equals(barCodeDto.getInstructionDocType())) {
                // 本类型只做校验
                MtInstructionDetail detail = mtInstructionDetailRepository.selectOne(new MtInstructionDetail() {{
                    setTenantId(tenantId);
                    setMaterialLotId(dto.getMaterialLotId());
                    setInstructionId(docLine.getInstructionId());
                }});
                if (ObjectUtils.isEmpty(detail) && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                    throw new MtException("WMS_INV_TRANSFER_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_INV_TRANSFER_0012", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
                } else if (ObjectUtils.isEmpty(detail) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                    throw new MtException("WMS_INV_TRANSFER_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_INV_TRANSFER_0013", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode(), ""));
                }
                //匹配指定sn
                List<WmsInstructionSnRelVO> wmsInstructionSnRelVOList1 = wmsInstructionSnRelVOS.stream().filter(item -> item.getMaterialLotId().equals(dto.getMaterialLotId())).collect(toList());
                //条码没被指定
                if (CollectionUtils.isEmpty(wmsInstructionSnRelVOList1)) {
                    List<String> instructionIds = wmsInstructionSnRelVOS.stream().map(WmsInstructionSnRelVO::getInstructionId).collect(toList());
                    docLines = docLines.stream().filter(item -> !instructionIds.contains(item.getInstructionId())).collect(toList());
                    if(CollectionUtils.isEmpty(docLines)){
                        throw new MtException("WX_WMS_SO_DELIVERY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WX_WMS_SO_DELIVERY_0010", "WMS"));
                    }
                } else {
                    //条码被指定
                    List<String> instructionIds = wmsInstructionSnRelVOList1.stream().map(WmsInstructionSnRelVO::getInstructionId).collect(toList());
                    List<WmsInvTransferDTO2> docLines2 = docLines.stream().filter(item -> instructionIds.contains(item.getInstructionId())).collect(toList());
                    if (CollectionUtils.isEmpty(docLines2)) {
                        //条码被指定的行不在该单据下
                        //获取该条码被指定的行状态
                        List<MtInstruction> mtInstructions = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIds);
                        for (MtInstruction mtInstruction : mtInstructions) {
                            //判断行状态是否在值集中
                            List<LovValueDTO> status = lovAdapter.queryLovValue("WX.WMS.AUTO_SN_DOC_STATUS_LIMIT", tenantId);
                            List<LovValueDTO> statusList = status.stream().filter(item -> item.getValue().equals(mtInstruction.getInstructionStatus())).collect(Collectors.toList());
                            if (CollectionUtils.isEmpty(statusList)) {
                                //不存在报错
                                //获取单据num
                                MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.instructionDocPropertyGet(tenantId, mtInstruction.getSourceDocId());
                                throw new MtException("WX_WMS_SO_DELIVERY_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WX_WMS_SO_DELIVERY_0009", "WMS", dto.getMaterialLotCode(),mtInstructionDoc.getInstructionDocNum()));
                            }
                        }
                    } else {
                        //条码被指定的行在该单据下
                        docLines = docLines2;
                    }
                }
            }
        }
        // add by jiangling.zheng 2020-08-20 发出功能单据类型为发出执行 及接收功能有此逻辑：当前条码（容器）匹配的所有单据行数量仓库及货位需一致
        if ((StringUtils.equals(WmsConstant.TransferType.ISSUE, type) && StringUtils.equals(WmsConstant.DocType.SEND_EXECUTE, docType)) ||
                StringUtils.equals(WmsConstant.TransferType.RECEIPT, type)) {
            // 校验目标仓库是否一致
            List<String> toWarehouseIds = mateLineList.stream().map(WmsInvTransferDTO2::getToWarehouseId).distinct().collect(Collectors.toList());
            // 校验目标货位是否一致
            List<String> toLocatorIds = mateLineList.stream().filter(item -> StringUtils.isNotEmpty(item.getToLocatorId()))
                    .map(WmsInvTransferDTO2::getToLocatorId).distinct().collect(Collectors.toList());
            if (toWarehouseIds.size() > 1) {
                throw new MtException("WMS_INV_TRANSFER_0033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0033", WmsConstant.ConstantValue.WMS));
            }
            if (toLocatorIds.size() > 1) {
                throw new MtException("WMS_INV_TRANSFER_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0034", WmsConstant.ConstantValue.WMS));
            }
        }
        // end add
        return dtoList;
    }
}
