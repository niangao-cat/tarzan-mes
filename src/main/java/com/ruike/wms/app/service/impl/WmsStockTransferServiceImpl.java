package com.ruike.wms.app.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsStockTransferService;
import com.ruike.wms.domain.entity.WmsStockAllocateSetting;
import com.ruike.wms.domain.repository.WmsDocPrivilegeRepository;
import com.ruike.wms.domain.repository.WmsStockAllocateSettingRepository;
import com.ruike.wms.domain.vo.WmsInstructionVO2;
import com.ruike.wms.domain.vo.WmsStockTransferVO;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.CommonQRCodeUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsCostCenterPickReturnMapper;
import com.ruike.wms.infra.mapper.WmsStockTransferMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtExtendVO5;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.MtInstructionActualVO;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.instruction.domain.vo.MtInstructionVO6;
import tarzan.inventory.domain.entity.MtInvOnhandQuantity;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO;
import tarzan.modeling.domain.vo.MtModLocatorVO9;
import com.ruike.wms.api.dto.WmsInstructionReturnDTO;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class WmsStockTransferServiceImpl implements WmsStockTransferService {
    private static final String INSTRUCTION_DOC_STATUS = "CLOSED";
    private static final String DOC_STATUS_CANCEL = "CANCEL";
    private static final String DOC_STATUS_APPROVED = "APPROVED";

    @Resource
    MtInstructionDocRepository mtInstructionDocRepository;

    @Autowired
    MtInstructionRepository mtInstructionRepository;

    @Autowired
    MtExtendSettingsMapper mtExtendSettingMapper;

    @Autowired
    private WmsStockTransferMapper wmsStockTransferMapper;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    MtUserOrganizationRepository mtUserOrganizationRepository;

    @Autowired
    MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    MtEventRepository mtEventRepository;

    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;

    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private WmsStockAllocateSettingRepository wmsStockAllocateSettingRepository;

    @Autowired
    private MtUserClient mtUserClient;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private WmsCostCenterPickReturnMapper wmsCostCenterPickReturnMapper;

    @Autowired
    private WmsDocPrivilegeRepository wmsDocPrivilegeRepository;


    @Override
    @ProcessLovValue
    public WmsInstructionReturnDTO putInstruction(Long tenantId,WmsStockTransferHeadDTO wmsStockTransferHeadDTO) {
        WmsInstructionReturnDTO wmsInstructionReturnDTO = new WmsInstructionReturnDTO();
        // ?????????????????????
        if(StringUtils.isEmpty(wmsStockTransferHeadDTO.getInstructionDocId())){
            wmsInstructionReturnDTO.setProcessDate(DateUtil.date());
            wmsInstructionReturnDTO.setProcessStatus("E");
            wmsInstructionReturnDTO.setProcessMessage("????????????!");
            return wmsInstructionReturnDTO;
        }
        // ??????API : eventCreate ?????? eventId
        String eventTypeCode = "STOCK_ALLOCATION_CLOSED";
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode(eventTypeCode);
        String eventId  = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        // ?????? API : instructionDocUpdate (??????????????????)
        MtInstructionDocDTO2 mtInstructionDocDTO2 = new MtInstructionDocDTO2();
        mtInstructionDocDTO2.setInstructionDocId(wmsStockTransferHeadDTO.getInstructionDocId());
        mtInstructionDocDTO2.setInstructionDocStatus(INSTRUCTION_DOC_STATUS);
        mtInstructionDocDTO2.setEventId(eventId);
        mtInstructionDocRepository.instructionDocUpdate(tenantId,mtInstructionDocDTO2,"N");
        // ??????????????????
        List<MtInstructionVO> mtInstructionVOS = wmsStockTransferMapper.selectInstruction(wmsStockTransferHeadDTO.getInstructionDocId());
        // ?????? API : instructionUpdate  (??????????????????)
        for (MtInstructionVO item:mtInstructionVOS) {
            item.setInstructionStatus(INSTRUCTION_DOC_STATUS);
            item.setEventId(eventId);
            mtInstructionRepository.instructionUpdate(tenantId,item,"N");
        }
        // ??????????????????
        wmsInstructionReturnDTO.setProcessDate(DateUtil.date());
        wmsInstructionReturnDTO.setProcessStatus("S");
        wmsInstructionReturnDTO.setProcessMessage("200");
        return wmsInstructionReturnDTO;
    }

    @Override
    @ProcessLovValue
    public List<WmsStockTransferHeadDTO> queryHeadData(WmsStockTransferDTO dto, Long tenantId, PageRequest pageRequest) {
        Page<WmsStockTransferHeadDTO> base = PageHelper.doPageAndSort(pageRequest,
                () -> wmsStockTransferMapper.selectStockTransferData(tenantId, dto));
        return base;
    }

    @Override
    @ProcessLovValue
    public List<WmsStockTransferLineDTO> queryLineDetailByHeadId(String sourceInstructionId, Long tenantId, PageRequest pageRequest) {

        Page<WmsStockTransferLineDTO> base = PageHelper.doPageAndSort(pageRequest,
                () -> wmsStockTransferMapper.selectInstructionLine(tenantId, sourceInstructionId));

//        List<WmsStockTransferLineDTO> wmsStockTransferLineDTOS
//                = wmsStockTransferMapper.selectInstructionLine(tenantId, dto.getSourceDocId());

        for (WmsStockTransferLineDTO wmsStockTransferLineDTO : base) {
            if (!StringUtils.isEmpty(wmsStockTransferLineDTO.getFromWarehouseId())) {
                String fromWarehouseCode = mtModLocatorRepository.
                        locatorBasicPropertyGet(tenantId, wmsStockTransferLineDTO.getFromWarehouseId()).getLocatorCode();
                wmsStockTransferLineDTO.setFromWarehouseCode(fromWarehouseCode);
            }
            if (!StringUtils.isEmpty(wmsStockTransferLineDTO.getToWarehouseId())) {
                String toWarehouseCode = mtModLocatorRepository.
                        locatorBasicPropertyGet(tenantId, wmsStockTransferLineDTO.getToWarehouseId()).getLocatorCode();
                wmsStockTransferLineDTO.setToWarehouseCode(toWarehouseCode);
            }

            //???????????????
            wmsStockTransferLineDTO.setOnhandQuantity(BigDecimal.ZERO);
            List<String> locatorList = new ArrayList<>();
            if(StringUtils.isNotBlank(wmsStockTransferLineDTO.getFromLocatorId())){
                locatorList.add(wmsStockTransferLineDTO.getFromLocatorId());
            }else {
                List<MtModLocator> mtModLocatorList = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class)
                        .andWhere(Sqls.custom().andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtModLocator.FIELD_ENABLE_FLAG,WmsConstant.CONSTANT_Y)
                                .andEqualTo(MtModLocator.FIELD_PARENT_LOCATOR_ID, wmsStockTransferLineDTO.getFromWarehouseId())).build());
                locatorList = mtModLocatorList.stream().map(MtModLocator::getLocatorId).collect(Collectors.toList());
            }
            if(CollectionUtils.isNotEmpty(locatorList)){
                List<MtInvOnhandQuantity> mtInvOnhandQuantities = mtInvOnhandQuantityRepository.selectByCondition(Condition.builder(MtInvOnhandQuantity.class).andWhere(Sqls.custom()
                        .andEqualTo(MtInvOnhandQuantity.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtInvOnhandQuantity.FIELD_MATERIAL_ID, wmsStockTransferLineDTO.getMaterialId())
                        .andIn(MtInvOnhandQuantity.FIELD_LOCATOR_ID, locatorList)).build());
                Double sum = mtInvOnhandQuantities.stream().map(MtInvOnhandQuantity::getOnhandQuantity).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                if(sum != null){
                    wmsStockTransferLineDTO.setOnhandQuantity(BigDecimal.valueOf(sum));
                }
            }
        }

        return base;
    }

    @Override
    @ProcessLovValue
    public List<WmsStockTransferLineDTO> listStockUpdateTransferLineForUi(String sourceInstructionId, Long tenantId) {
        List<WmsStockTransferLineDTO> lineDTOList = wmsStockTransferMapper.selectInstructionLine(tenantId, sourceInstructionId);
        for (WmsStockTransferLineDTO wmsStockTransferLineDTO : lineDTOList) {
            if (!StringUtils.isEmpty(wmsStockTransferLineDTO.getFromWarehouseId())) {
                String fromWarehouseCode = mtModLocatorRepository.
                        locatorBasicPropertyGet(tenantId, wmsStockTransferLineDTO.getFromWarehouseId()).getLocatorCode();
                wmsStockTransferLineDTO.setFromWarehouseCode(fromWarehouseCode);
            }
            if (!StringUtils.isEmpty(wmsStockTransferLineDTO.getToWarehouseId())) {
                String toWarehouseCode = mtModLocatorRepository.
                        locatorBasicPropertyGet(tenantId, wmsStockTransferLineDTO.getToWarehouseId()).getLocatorCode();
                wmsStockTransferLineDTO.setToWarehouseCode(toWarehouseCode);
            }

            //???????????????
            wmsStockTransferLineDTO.setOnhandQuantity(BigDecimal.ZERO);
            List<String> locatorList = new ArrayList<>();
            if(StringUtils.isNotBlank(wmsStockTransferLineDTO.getFromLocatorId())){
                locatorList.add(wmsStockTransferLineDTO.getFromLocatorId());
            }else {
                List<MtModLocator> mtModLocatorList = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class)
                        .andWhere(Sqls.custom().andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtModLocator.FIELD_ENABLE_FLAG,WmsConstant.CONSTANT_Y)
                                .andEqualTo(MtModLocator.FIELD_PARENT_LOCATOR_ID, wmsStockTransferLineDTO.getFromWarehouseId())).build());
                locatorList = mtModLocatorList.stream().map(MtModLocator::getLocatorId).collect(Collectors.toList());
            }
            if(CollectionUtils.isNotEmpty(locatorList)){
                List<MtInvOnhandQuantity> mtInvOnhandQuantities = mtInvOnhandQuantityRepository.selectByCondition(Condition.builder(MtInvOnhandQuantity.class).andWhere(Sqls.custom()
                        .andEqualTo(MtInvOnhandQuantity.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtInvOnhandQuantity.FIELD_MATERIAL_ID, wmsStockTransferLineDTO.getMaterialId())
                        .andIn(MtInvOnhandQuantity.FIELD_LOCATOR_ID, locatorList)).build());
                Double sum = mtInvOnhandQuantities.stream().map(MtInvOnhandQuantity::getOnhandQuantity).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                if(sum != null){
                    wmsStockTransferLineDTO.setOnhandQuantity(BigDecimal.valueOf(sum));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(lineDTOList)) {
            MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(sourceInstructionId);
            Long userId = DetailsHelper.getUserDetails().getUserId();
            // ??????????????????
            if (StringUtils.isNotBlank(lineDTOList.get(0).getFromWarehouseId())) {
                wmsDocPrivilegeRepository.isWarehousePrivileged(tenantId,WmsWarehousePrivilegeQueryDTO.builder()
                        .userId(userId)
                        .locatorId(lineDTOList.get(0).getFromWarehouseId())
                        .docType(mtInstructionDoc.getInstructionDocType())
                        .operationType(WmsConstant.OperationType.CREATE)
                        .locationType(WmsConstant.LocatorType.FROM_LOCATOR)
                        .build());
            }
            // ??????????????????
            if (StringUtils.isNotBlank(lineDTOList.get(0).getToWarehouseId())) {
                wmsDocPrivilegeRepository.isWarehousePrivileged(tenantId,WmsWarehousePrivilegeQueryDTO.builder()
                        .userId(userId)
                        .locatorId(lineDTOList.get(0).getToWarehouseId())
                        .docType(mtInstructionDoc.getInstructionDocType())
                        .operationType(WmsConstant.OperationType.CREATE)
                        .locationType(WmsConstant.LocatorType.TO_LOCATOR)
                        .build());
            }
        }
        return lineDTOList;
    }

    /**
     * @param dto      1
     * @param tenantId 2
     * @return : tarzan.instruction.domain.entity.MtInstructionDoc
     * @Description: ???????????? ??????/??????
     * @author: Modified By Deng Xu
     * @date 2020/6/8 10:41
     * @version 1.0
     */
    @Override
    public MtInstructionDoc save(WmsStockTransferSaveDTO dto, Long tenantId) {
        if (StringUtils.isEmpty(dto.getInstructionDocType())) {
            throw new MtException("WMS_INV_TRANSFER_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_INV_TRANSFER_0045", "WMS", dto.getInstructionDocNum()));
        }
        MtInstructionDocDTO2 mtInstructionDocVO = new MtInstructionDocDTO2();
        BeanUtils.copyProperties(dto, mtInstructionDocVO);
        //???????????????
        if (StringUtils.isNotEmpty(mtInstructionDocVO.getInstructionDocId())) {
            //?????????ID???????????????????????????NEW
            MtInstructionDoc queryDoc = new MtInstructionDoc();
            queryDoc.setInstructionDocId(mtInstructionDocVO.getInstructionDocId());
            queryDoc = mtInstructionDocRepository.selectByPrimaryKey(queryDoc);
            if (null == queryDoc || StringUtils.isEmpty(queryDoc.getInstructionDocId()) || !WmsConstant.InstructionStatus.NEW.equals(queryDoc.getInstructionDocStatus())) {
                throw new MtException("WMS_INV_TRANSFER_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0044", "WMS", dto.getInstructionDocNum()));
            }
            //??????API???eventRequestCreate
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "STOCK_ALLOCATION_UPDATE");
            //??????API???eventCreate
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("STOCK_ALLOCATION_UPDATE");
            eventCreateVO.setEventRequestId(eventRequestId);
            // ????????????
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
            mtInstructionDocVO.setEventId(eventId);
        }
        mtInstructionDocVO.setInstructionDocStatus("NEW");
//        mtInstructionDocVO.setPersonId(-1L); // ?????????
        mtInstructionDocVO.setPersonId(DetailsHelper.getUserDetails().getUserId());
        MtInstructionDocVO3 mtInstructionDocVO3 = mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDocVO, "");
        //???????????????
        //??????API???eventRequestCreate
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "STOCK_ALLOCATION_CREATION");
        //??????API???eventCreate
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("STOCK_ALLOCATION_CREATION");
        eventCreateVO.setEventRequestId(eventRequestId);
        // ????????????
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        //?????? ?????? ?????????????????????
        Map<String, List<WmsStockTransferSaveDTO.LineDTO>> lineMapList = dto.getLineDTOList().stream().collect(Collectors.groupingBy(line -> line.getMaterialId() + "_" + line.getMaterialVersion()));
        for (Map.Entry<String, List<WmsStockTransferSaveDTO.LineDTO>> lineMap : lineMapList.entrySet()){
            List<WmsStockTransferSaveDTO.LineDTO> lineDTOList = lineMap.getValue();
            if(CollectionUtils.isNotEmpty(lineDTOList) && lineDTOList.size() > 1){
                throw new MtException("WMS_INV_TRANSFER_0039", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_INV_TRANSFER_0039", "WMS", lineDTOList.get(0).getInstructionLineNum().toString(), lineDTOList.get(1).getInstructionLineNum().toString()));
            }
        }

        //2020-11-02 add by chaonan.hu for jian.zhang ????????????????????????????????????????????????????????????????????????
        //???????????????????????????????????????,???????????????????????????????????????????????????????????????????????????
        List<WmsStockTransferSaveDTO.LineDTO> versionNullList = dto.getLineDTOList().stream().filter(item -> StringUtils.isEmpty(item.getMaterialVersion())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(versionNullList)){
            //?????????????????????????????????Id
            List<String> versionNullMaterialList = versionNullList.stream().map(WmsStockTransferSaveDTO.LineDTO::getMaterialId).collect(Collectors.toList());
            for (String materialId:versionNullMaterialList) {
                //???????????????????????????????????????????????????????????????????????????????????????????????????
                List<WmsStockTransferSaveDTO.LineDTO> lineDTOList = dto.getLineDTOList().stream().filter(item -> materialId.equals(item.getMaterialId()) && StringUtils.isNotEmpty(item.getMaterialVersion())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(lineDTOList)){
                    MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialId);
                    throw new MtException("WMS_INV_TRANSFER_0050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_INV_TRANSFER_0050", "WMS", mtMaterial.getMaterialCode()));
                }
            }
        }
        if(StringUtils.isNotEmpty(dto.getInstructionDocId())){
            if(CollectionUtils.isNotEmpty(dto.getLineDTOList())){
                //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                List<String> materialIds = dto.getLineDTOList().stream().map(WmsStockTransferSaveDTO.LineDTO::getMaterialId).distinct().collect(Collectors.toList());
                List<WmsPickReturnLineReceiveVO> instructionsForUnique = wmsCostCenterPickReturnMapper.queryInstructionsForUnique(tenantId, dto.getInstructionDocId(), materialIds);
                if (CollectionUtils.isNotEmpty(instructionsForUnique)) {
                    instructionsForUnique.forEach(item -> {
                        dto.getLineDTOList().forEach(para -> {
                            if (StringUtils.trimToEmpty(item.getMaterialId()).equals(StringUtils.trimToEmpty(para.getMaterialId()))
                                    && StringUtils.trimToEmpty(item.getMaterialVersion()).equals(StringUtils.trimToEmpty(para.getMaterialVersion()))) {
                                throw new MtException("WMS_COST_CENTER_0023",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0023", "WMS"));
                            }
                            //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                            if(StringUtils.trimToEmpty(item.getMaterialId()).equals(StringUtils.trimToEmpty(para.getMaterialId()))
                                    && ((StringUtils.isNotEmpty(item.getMaterialVersion()) && StringUtils.isEmpty(para.getMaterialVersion()))
                                    || (StringUtils.isEmpty(item.getMaterialVersion()) && StringUtils.isNotEmpty(para.getMaterialVersion())))){
                                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(item.getMaterialId());
                                throw new MtException("WMS_INV_TRANSFER_0050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WMS_INV_TRANSFER_0050", "WMS", mtMaterial.getMaterialCode()));
                            }
                        });
                    });
                }
            }
        }

        for (WmsStockTransferSaveDTO.LineDTO lineDto : dto.getLineDTOList()) {
            if (Objects.isNull(lineDto.getInstructionLineNum())) {
                throw new RuntimeException("??????????????????");
            }
            Long num = lineDto.getInstructionLineNum();
            //??????????????????ID?????????????????????????????????
            if (StringUtils.isNotEmpty(lineDto.getInstructionId())) {
                continue;
            }
            if (StringUtils.isEmpty(lineDto.getMaterialId())) {
                throw new MtException("WMS_INV_TRANSFER_0040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0039", "WMS", String.valueOf(num)));
            }
            if (lineDto.getQuantity() == null) {
                throw new MtException("WMS_INV_TRANSFER_0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0040", "WMS", String.valueOf(num)));
            }
            if (StringUtils.isEmpty(lineDto.getFromWarehouseId())) {
                throw new MtException("WMS_INV_TRANSFER_0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0042", "WMS", String.valueOf(num)));
            }
            //??????????????????+??????+???????????????????????????
            WmsInstructionVO2 vo2 = new WmsInstructionVO2();
            vo2.setSourceDocId(mtInstructionDocVO3.getInstructionDocId());
            vo2.setFromLocatorId(lineDto.getFromWarehouseId());
            vo2.setMaterialId(lineDto.getMaterialId());
            vo2.setMaterialVersion(lineDto.getMaterialVersion());
            int i = wmsStockTransferMapper.verifyData(vo2);
            if (i > 0) {
                throw new MtException("WMS_INV_TRANSFER_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0043", "WMS"));
            }
            //?????????????????????
            MtInstructionVO mtInstructionVO7 = new MtInstructionVO();
            BeanUtils.copyProperties(lineDto, mtInstructionVO7);
            //????????????????????????????????????TRANSFER_OVER_LOCATOR???????????????????????????
            if (lineDto.getFromSiteId().equals(lineDto.getToSiteId())) {
                mtInstructionVO7.setInstructionType("TRANSFER_OVER_LOCATOR");
            } else {
                //???????????????????????????????????????TRANSFER_OVER_SITE??????????????????
                mtInstructionVO7.setInstructionType("TRANSFER_OVER_SITE");
            }
            mtInstructionVO7.setFromLocatorId(lineDto.getFromWarehouseId());
            mtInstructionVO7.setToLocatorId(lineDto.getToWarehouseId());
            mtInstructionVO7.setSourceDocId(mtInstructionDocVO3.getInstructionDocId());
            mtInstructionVO7.setInstructionStatus("NEW");
            mtInstructionVO7.setSiteId(dto.getSiteId());
            MtInstructionVO6 mtInstructionVO6 = mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO7, "");
            //???????????????????????????
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setKeyId(mtInstructionVO6.getInstructionId());
            List<MtExtendVO5> mtExtendVO5s = new ArrayList<>(4);
            //????????????
            MtExtendVO5 mtExtendVO51 = new MtExtendVO5();
            mtExtendVO51.setAttrName("MATERIAL_VERSION");
            mtExtendVO51.setAttrValue(lineDto.getMaterialVersion());
            mtExtendVO5s.add(mtExtendVO51);
            //????????????
            MtExtendVO5 mtExtendVO52 = new MtExtendVO5();
            mtExtendVO52.setAttrName("FROM_LOCATOR_ID");
            mtExtendVO52.setAttrValue(String.valueOf(lineDto.getFromLocatorId()));
            mtExtendVO5s.add(mtExtendVO52);
            // ????????????
            MtExtendVO5 mtExtendVO53 = new MtExtendVO5();
            mtExtendVO53.setAttrName("TO_LOCATOR_ID");
            mtExtendVO53.setAttrValue(String.valueOf(lineDto.getToLocatorId()));
            mtExtendVO5s.add(mtExtendVO53);
            // ??????
            MtExtendVO5 mtExtendVO54 = new MtExtendVO5();
            mtExtendVO54.setAttrName("INSTRUCTION_LINE_NUM");
            mtExtendVO54.setAttrValue(String.valueOf(num));
            mtExtendVO5s.add(mtExtendVO54);
            // ????????????
            MtExtendVO5 mtExtendVO55 = new MtExtendVO5();
            mtExtendVO55.setAttrName("EXCESS_SETTING");
            mtExtendVO55.setAttrValue(String.valueOf(lineDto.getExcessSetting()));
            mtExtendVO5s.add(mtExtendVO55);
            // ?????????
            MtExtendVO5 mtExtendVO56 = new MtExtendVO5();
            mtExtendVO56.setAttrName("EXCESS_VALUE");
            mtExtendVO56.setAttrValue(String.valueOf(lineDto.getExcessValue()));
            mtExtendVO5s.add(mtExtendVO56);
            //???????????????????????????
            mtExtendVO10.setAttrs(mtExtendVO5s);
            mtInstructionRepository.instructionAttrPropertyUpdate(tenantId, mtExtendVO10);
            //?????????????????????????????????????????????API???instructionActualUpdate
            MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
            BeanUtils.copyProperties(mtInstructionVO7, mtInstructionActualVO);
            mtInstructionActualVO.setInstructionId(mtInstructionVO6.getInstructionId());
            mtInstructionActualVO.setActualQty(0D);
            mtInstructionActualVO.setEventId(eventId);
            mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);
        }
        //?????????????????????????????????N
        if(StringUtils.isEmpty(mtInstructionDocVO.getInstructionDocId())){
            List<MtExtendVO5> vo5List = new ArrayList<>(1);
            MtExtendVO5 vo5 = new MtExtendVO5();
            vo5.setAttrName("PRINT_FLAG");
            vo5.setAttrValue(MtBaseConstants.NO);
            vo5List.add(vo5);

            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr",
                    mtInstructionDocVO3.getInstructionDocId() , "-1",vo5List);
        }
        //??????????????????????????????
        MtInstructionDoc instructionDoc = new MtInstructionDoc();
        instructionDoc.setInstructionDocId(mtInstructionDocVO3.getInstructionDocId());
        instructionDoc = mtInstructionDocRepository.selectByPrimaryKey(instructionDoc);
        return instructionDoc;
    }

    @Override
    public List<WmsSiteDTO> getSite(Long tenantId) {
        MtUserOrganization mtUserOrganization = new MtUserOrganization();
        List<WmsSiteDTO> siteList = new ArrayList<>();
        mtUserOrganization.setUserId(DetailsHelper.getUserDetails().getUserId());
//        mtUserOrganization.setUserId(1L);
        mtUserOrganization.setOrganizationType("SITE");
        List<MtUserOrganization> mtUserOrganizationList = mtUserOrganizationRepository.userOrganizationPermissionQuery(tenantId, mtUserOrganization);
        List<String> siteIdList = mtUserOrganizationList.stream().map(MtUserOrganization::getOrganizationId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(siteIdList)) {
            return siteList;
        }
        List<MtModSite> mtModSiteList = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, siteIdList);
        for (MtModSite mtModSite : mtModSiteList) {
            WmsSiteDTO site = new WmsSiteDTO();
            BeanUtils.copyProperties(mtModSite, site);
            site.setDescription(mtModSite.getSiteName());
            siteList.add(site);
        }
        return siteList;
    }

    @Override
    public List<WmsWarehouseDTO> getWarehouse(Long tenantId, String siteId) {
        if (StringUtils.isEmpty(siteId)) {
            return Collections.emptyList();
        }
        List<MtModLocatorOrgRelVO> mtModLocatorOrgRelVOS = mtModLocatorOrgRelRepository.organizationLimitLocatorQuery(tenantId, "SITE", siteId, "TOP");

        if (CollectionUtils.isEmpty(mtModLocatorOrgRelVOS)) {
            return Collections.emptyList();
        }
        //????????????????????????
        List<WmsWarehouseDTO> result = new ArrayList<>();
        List<MtModLocator> mtModLocators = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, mtModLocatorOrgRelVOS.stream().map(MtModLocatorOrgRelVO::getLocatorId).collect(Collectors.toList()));

        for (MtModLocator mtModLocator : mtModLocators) {
            WmsWarehouseDTO warehouseDTO = new WmsWarehouseDTO();
            warehouseDTO.setWarehouseId(mtModLocator.getLocatorId());
            warehouseDTO.setWarehouseCode(mtModLocator.getLocatorCode());
            warehouseDTO.setWarehouseDesc(mtModLocator.getLocatorName());
            result.add(warehouseDTO);
        }
        return result;
    }

    @Override
    public List<WmsLocatorDTO> getLocator(Long tenantId, String locatorId) {

        if (StringUtils.isEmpty(locatorId)) {
            return Collections.emptyList();
        }
        MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
        mtModLocatorVO9.setLocatorId(locatorId);
        mtModLocatorVO9.setQueryType("ALL");
        List<String> strings = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
        if (CollectionUtils.isEmpty(strings)) {
            return Collections.emptyList();
        }
        //????????????????????????
        List<WmsLocatorDTO> result = new ArrayList<>();
        List<MtModLocator> mtModLocators = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, strings);
        for (MtModLocator mtModLocator : mtModLocators) {
            WmsLocatorDTO wmsLocatorDTO = new WmsLocatorDTO();
            wmsLocatorDTO.setLocatorId(mtModLocator.getLocatorId());
            wmsLocatorDTO.setLocatorCode(mtModLocator.getLocatorCode());
            wmsLocatorDTO.setLocatorName(mtModLocator.getLocatorName());
            result.add(wmsLocatorDTO);
        }
        return result;
    }

    /**
     * @param tenantId      ??????ID
     * @param instructionId ????????????ID
     * @param pageRequest   ????????????
     * @return : io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsStockTransferVO>
     * @Description: ????????????-?????????ID??????????????????
     * @author: Deng Xu
     * @date 2020/6/8 13:50
     * @version 1.0
     */
    @Override
    @ProcessLovValue
    public Page<WmsStockTransferVO> listMaterialLotForUi(Long tenantId, String instructionId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> wmsStockTransferMapper.listMaterialLotForUi(tenantId, instructionId));
    }

    /**
     * @param tenantId  ??????ID
     * @param deleteDto ????????????????????????DTO??????
     * @return : void
     * @Description: ???????????? ??????????????????
     * @author: Deng Xu
     * @date 2020/6/8 15:19
     * @version 1.0
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLine(Long tenantId, WmsStockTransferSaveDTO.LineDTO deleteDto) {
        if (StringUtils.isEmpty(deleteDto.getInstructionId())) {
            throw new RuntimeException("????????????????????????????????????");
        }
        //?????????ID???????????????????????????????????????
        if (StringUtils.isNotEmpty(deleteDto.getInstructionId())) {
            //?????????ID??????????????????????????????
            MtInstructionActual queryActual = new MtInstructionActual();
            queryActual.setInstructionId(deleteDto.getInstructionId());
            List<MtInstructionActual> actualList = mtInstructionActualRepository.select(queryActual);
            mtInstructionActualRepository.batchDeleteByPrimaryKey(actualList);
            //???????????????
            mtInstructionRepository.deleteByPrimaryKey(deleteDto.getInstructionId());
        }
    }

    /**
     * ????????????
     * @param tenantId
     * @param wmsStockTransferHeadDtoList
     * @author wsg guijie.wu@hand-china.com 2020/7/30 17:58
     * @return java.util.List<com.ruike.wms.api.dto.WmsStockTransferHeadDTO>
     */
    @Override
    public List<WmsStockTransferHeadDTO> print(Long tenantId, List<WmsStockTransferHeadDTO> wmsStockTransferHeadDtoList) {
        //??????API???eventCreate???????????????
        MtEventCreateVO event = new MtEventCreateVO();
        event.setEventTypeCode("DOCUTMENT_FRINT");
        String eventId = mtEventRepository.eventCreate(tenantId, event);
        for (WmsStockTransferHeadDTO vo : wmsStockTransferHeadDtoList) {
            if (MtBaseConstants.YES.equals(vo.getPrintFlag())) {
                throw new MtException("WMS_INV_TRANSFER_0021",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_INV_TRANSFER_0021",
                                "WMS", vo.getInstructionDocNum()));
            }
            //??????????????????
            List<MtExtendVO5> vo5List = new ArrayList<>();
            MtExtendVO5 vo5 = new MtExtendVO5();
            vo5.setAttrName("PRINT_FLAG");
            vo5.setAttrValue(MtBaseConstants.YES);
            vo5List.add(vo5);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr",
                    vo.getInstructionDocId(), eventId, vo5List);
            vo.setPrintFlag(MtBaseConstants.YES);
        }
        return wmsStockTransferHeadDtoList;
    }

   /**
    * ??????
    * @param tenantId
    * @param wmsStockTransferHeadDtoList
    * @author wsg guijie.wu@hand-china.com 2020/7/30 17:58
    * @return java.util.List<com.ruike.wms.api.dto.WmsStockTransferHeadDTO>
    */
    @Override
    public List<WmsStockTransferHeadDTO> hold(Long tenantId, List<WmsStockTransferHeadDTO> wmsStockTransferHeadDtoList) {
        List<WmsStockTransferHeadDTO> oldList = wmsStockTransferHeadDtoList.stream().filter(t -> !MtBaseConstants.EO_STATUS.NEW.equals(t.getInstructionDocStatus())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(oldList)){
            List<String> numList = oldList.stream().map(WmsStockTransferHeadDTO -> WmsStockTransferHeadDTO.getInstructionDocNum()).collect(Collectors.toList());
            throw new MtException("WMS_INV_TRANSFER_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_INV_TRANSFER_0022", "WMS", StringUtils.join(numList,",")));
        }
        List<MtInstructionDoc> updateList = new ArrayList<>();
        for(WmsStockTransferHeadDTO wmsStockTransferHeadDTO: wmsStockTransferHeadDtoList){
            //??????API???eventRequestCreate
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "STOCK_ALLOCATION_UPDATE");
            //??????API???eventCreate
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("STOCK_ALLOCATION_UPDATE");
            eventCreateVO.setEventRequestId(eventRequestId);
            // ????????????
            mtEventRepository.eventCreate(tenantId, eventCreateVO);
            MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
            mtInstructionDoc.setInstructionDocId(wmsStockTransferHeadDTO.getInstructionDocId());
            mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(mtInstructionDoc);
            if(!Objects.isNull(mtInstructionDoc)){
                mtInstructionDoc.setInstructionDocStatus(MtBaseConstants.QUALITY_STATUS.PENDING);
                updateList.add(mtInstructionDoc);
            }
            wmsStockTransferHeadDTO.setInstructionDocStatus(MtBaseConstants.QUALITY_STATUS.PENDING);
        }
        mtInstructionDocRepository.batchUpdateByPrimaryKeySelective(updateList);
        return wmsStockTransferHeadDtoList;
    }

    /**
     * ????????????
     * @param tenantId
     * @param wmsStockTransferHeadDtoList
     * @author wsg guijie.wu@hand-china.com 2020/7/30 17:58
     * @return java.util.List<com.ruike.wms.api.dto.WmsStockTransferHeadDTO>
     */
    @Override
    public List<WmsStockTransferHeadDTO> holdCancel(Long tenantId, List<WmsStockTransferHeadDTO> wmsStockTransferHeadDtoList) {
        List<WmsStockTransferHeadDTO> oldList = wmsStockTransferHeadDtoList.stream().filter(t -> !MtBaseConstants.QUALITY_STATUS.PENDING.equals(t.getInstructionDocStatus())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(oldList)){
            List<String> numList = oldList.stream().map(WmsStockTransferHeadDTO -> WmsStockTransferHeadDTO.getInstructionDocNum()).collect(Collectors.toList());
            throw new MtException("WMS_INV_TRANSFER_0023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_INV_TRANSFER_0023", "WMS", StringUtils.join(numList,",")));
        }
        List<MtInstructionDoc> updateList = new ArrayList<>();
        for(WmsStockTransferHeadDTO wmsStockTransferHeadDTO: wmsStockTransferHeadDtoList){
            //??????API???eventRequestCreate
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "STOCK_ALLOCATION_UPDATE");
            //??????API???eventCreate
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("STOCK_ALLOCATION_UPDATE");
            eventCreateVO.setEventRequestId(eventRequestId);
            // ????????????
            mtEventRepository.eventCreate(tenantId, eventCreateVO);
            MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
            mtInstructionDoc.setInstructionDocId(wmsStockTransferHeadDTO.getInstructionDocId());
            mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(mtInstructionDoc);
            if(!Objects.isNull(mtInstructionDoc)){
                mtInstructionDoc.setInstructionDocStatus(MtBaseConstants.EO_STATUS.NEW);
                updateList.add(mtInstructionDoc);
            }
            wmsStockTransferHeadDTO.setInstructionDocStatus(MtBaseConstants.EO_STATUS.NEW);
        }
        mtInstructionDocRepository.batchUpdateByPrimaryKeySelective(updateList);
        return wmsStockTransferHeadDtoList;
    }

    /**
     * ????????????
     * @param tenantId
     * @param wmsStockTransferHeadDtoList
     * @author wsg guijie.wu@hand-china.com 2020/7/30 17:57
     * @return java.util.List<com.ruike.wms.api.dto.WmsStockTransferHeadDTO>
     */
    @Override
    public List<WmsStockTransferHeadDTO> cancel(Long tenantId, List<WmsStockTransferHeadDTO> wmsStockTransferHeadDtoList) {
        List<String> statusList = new ArrayList<>();
        statusList.add(MtBaseConstants.EO_STATUS.NEW);
        statusList.add(DOC_STATUS_APPROVED);
        List<WmsStockTransferHeadDTO> oldList = wmsStockTransferHeadDtoList.stream().filter(t -> !statusList.contains(t.getInstructionDocStatus())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(oldList)){
            List<String> numList = oldList.stream().map(WmsStockTransferHeadDTO -> WmsStockTransferHeadDTO.getInstructionDocNum()).collect(Collectors.toList());
            throw new MtException("WMS_INV_TRANSFER_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_INV_TRANSFER_0024", "WMS", StringUtils.join(numList,",")));
        }
        List<MtInstructionDoc> updateList = new ArrayList<>();
        for(WmsStockTransferHeadDTO wmsStockTransferHeadDTO: wmsStockTransferHeadDtoList){
            //??????API???eventRequestCreate
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "STOCK_ALLOCATION_UPDATE");
            //??????API???eventCreate
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("STOCK_ALLOCATION_UPDATE");
            eventCreateVO.setEventRequestId(eventRequestId);
            // ????????????
            mtEventRepository.eventCreate(tenantId, eventCreateVO);
            MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
            mtInstructionDoc.setInstructionDocId(wmsStockTransferHeadDTO.getInstructionDocId());
            mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(mtInstructionDoc);
            if(!Objects.isNull(mtInstructionDoc)){
                mtInstructionDoc.setInstructionDocStatus(DOC_STATUS_CANCEL);
                updateList.add(mtInstructionDoc);
            }
            wmsStockTransferHeadDTO.setInstructionDocStatus(DOC_STATUS_CANCEL);
        }
        mtInstructionDocRepository.batchUpdateByPrimaryKeySelective(updateList);
        return wmsStockTransferHeadDtoList;
    }

    /**
     * ????????????
     * @param tenantId
     * @param wmsStockTransferHeadDtoList
     * @author wsg guijie.wu@hand-china.com 2020/7/30 17:57
     * @return java.util.List<com.ruike.wms.api.dto.WmsStockTransferHeadDTO>
     */
    @Override
    public List<WmsStockTransferHeadDTO> approval(Long tenantId, List<WmsStockTransferHeadDTO> wmsStockTransferHeadDtoList) {
        validateStock(tenantId, wmsStockTransferHeadDtoList);
        List<WmsStockTransferHeadDTO> oldList = wmsStockTransferHeadDtoList.stream().filter(t -> !MtBaseConstants.EO_STATUS.NEW.equals(t.getInstructionDocStatus())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(oldList)){
            List<String> numList = oldList.stream().map(WmsStockTransferHeadDTO -> WmsStockTransferHeadDTO.getInstructionDocNum()).collect(Collectors.toList());
            throw new MtException("WMS_INV_TRANSFER_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_INV_TRANSFER_0022", "WMS", StringUtils.join(numList,",")));
        }
        List<MtInstructionDoc> updateList = new ArrayList<>();
        for(WmsStockTransferHeadDTO wmsStockTransferHeadDTO: wmsStockTransferHeadDtoList){
            //??????API???eventRequestCreate
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "STOCK_ALLOCATION_UPDATE");
            //??????API???eventCreate
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("STOCK_ALLOCATION_UPDATE");
            eventCreateVO.setEventRequestId(eventRequestId);
            // ????????????
            mtEventRepository.eventCreate(tenantId, eventCreateVO);
            MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
            mtInstructionDoc.setInstructionDocId(wmsStockTransferHeadDTO.getInstructionDocId());
            mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(mtInstructionDoc);
            if(!Objects.isNull(mtInstructionDoc)){
                mtInstructionDoc.setInstructionDocStatus(DOC_STATUS_APPROVED);
                updateList.add(mtInstructionDoc);
            }
            wmsStockTransferHeadDTO.setInstructionDocStatus(DOC_STATUS_APPROVED);
        }
        mtInstructionDocRepository.batchUpdateByPrimaryKeySelective(updateList);
        return wmsStockTransferHeadDtoList;
    }

    @Override
    public void printPdf(Long tenantId, List<String> instructionDocIdList, HttpServletResponse response) {
        //???????????????????????????
        updateprintFlag(tenantId, instructionDocIdList);
        //???????????????
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);
        String basePath = classUrl + "/templates";
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "/templates");
            if (!file.exists()) {
                if(!file.mkdir()){
                    throw new MtException("???????????????????????????!");
                }
            }
            basePath = systemPath + "/templates";
        } else {
            basePath = classUrl + "/templates";
        }
        String docNumber = "";
        String uuid = UUID.randomUUID().toString();
        String barcodePath = "";
        String qrcodePath = "";
        String content = "";
        String pdfFileName = uuid + ".pdf";
        String pdfPath = basePath + "/" + pdfFileName;
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        //List<File> barcodeImageFileList = new ArrayList<File>();
        List<File> qrcodeImageFileList = new ArrayList<File>();

        //????????????????????????????????????
        Long lineCount = 10L;
        //??????????????????????????????????????????
        //??????
        Date nowDate = new Date();
        String[] strNow = new SimpleDateFormat("yyyy-MM-dd").format(nowDate).split("-");
        String date = strNow[0] + strNow[1] + strNow[2];
        //??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        String realName = curUser.getRealName();
        for(String instructionDocId:instructionDocIdList) {
            //???????????????
            WmsStockTransferDTO2 headDto = wmsStockTransferMapper.printHeadDataQuery(tenantId, instructionDocId);
            if(StringUtils.isNotEmpty(headDto.getInstructionDocType())){
                String meaning = lovAdapter.queryLovMeaning("WMS.STOCK_ALLOCATION_DOC.TYPE", tenantId, headDto.getInstructionDocType());
                headDto.setInstructionDocTypeMeaning(meaning);
            }
            docNumber = headDto.getInstructionDocNum();
            //???????????????
            String codeUuid = UUID.randomUUID().toString();
            qrcodePath = basePath + "/" + codeUuid + "_" + docNumber + "_qrcode.png";
            File qrcodeImageFile = new File(qrcodePath);
            qrcodeImageFileList.add(qrcodeImageFile);
            content = headDto.getInstructionDocNum();
            try {
                CommonQRCodeUtil.encode(content, qrcodePath, qrcodePath, true);
                log.info("<====????????????????????????{}", qrcodePath);
            } catch (Exception e) {
                log.error("<==== WmsStockTransferServiceImpl.printPdf.encode Error", e);
                throw new MtException(e.getMessage());
            }

            //????????????
            Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            imgMap.put("qrcodeImage", qrcodePath);
            Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);

            //???????????????
            Boolean printflag = false;
            Long currentLine = 0L;
            List<WmsStockTransferDTO3> lineList = wmsStockTransferMapper.printLineDataQuery(tenantId, instructionDocId);
            for(WmsStockTransferDTO3 lineDto:lineList){
                formMap.put("lineNum"+currentLine.toString(), lineDto.getLineNum());
                formMap.put("itemCode"+currentLine.toString(), lineDto.getItemCode());
                formMap.put("itemDesc"+currentLine.toString(), lineDto.getItemDesc());
                formMap.put("qty"+currentLine.toString(), lineDto.getQty().setScale(3,BigDecimal.ROUND_DOWN));
                formMap.put("version"+currentLine.toString(), lineDto.getVersion());
                formMap.put("uom"+currentLine.toString(), lineDto.getUom());
                formMap.put("locatorName"+currentLine.toString(), lineDto.getLocatorName());
                formMap.put("fromLocatorName"+currentLine.toString(), lineDto.getFromLocatorName());
                formMap.put("remark"+currentLine.toString(), lineDto.getRemark());
                formMap.put("createdBy", realName);
                currentLine +=1L;
                printflag = false;
                if(currentLine%lineCount == 0){
                    formMap.put("instructionDocNum", headDto.getInstructionDocNum());
                    formMap.put("siteName", headDto.getSiteName());
                    formMap.put("instructionDocTypeMeaning", headDto.getInstructionDocTypeMeaning());
                    formMap.put("date", date);
                    formMap.put("supplierCode", headDto.getSupplierCode());
                    formMap.put("supplierName", headDto.getSupplierName());

                    Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    param.put("formMap", formMap);
                    param.put("imgMap", imgMap);
                    dataList.add(param);
                    currentLine = 0L;
                    formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    printflag = true;
                }
            }
            if(!printflag) {
                formMap.put("instructionDocNum", headDto.getInstructionDocNum());
                formMap.put("siteName", headDto.getSiteName());
                formMap.put("instructionDocTypeMeaning", headDto.getInstructionDocTypeMeaning());
                formMap.put("date", date);
                formMap.put("supplierCode", headDto.getSupplierCode());
                formMap.put("supplierName", headDto.getSupplierName());

                Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                param.put("formMap", formMap);
                param.put("imgMap", imgMap);
                dataList.add(param);
            }
        }
        if(dataList.size() > 0) {
            //??????PDF
            try {
                log.info("<==== ??????PDF????????????:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + "/wms_stock_transfer_print_template.pdf", pdfPath, dataList);
                log.info("<==== ??????PDF?????????{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== WmsStockTransferServiceImpl.printPdf.generatePDFFile Error", e);
                throw new MtException(e.getMessage());
            }
        }

        //?????????????????????????????????
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File pdfFile = new File(pdfPath);
        try{
            //??????????????????
            response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
            response.setHeader("Content-Disposition","attachment;filename=" + uuid + ".pdf");
            String encoding = new GetFileCharset().guestFileEncoding(pdfFile);
            if(org.apache.commons.lang.StringUtils.isNotEmpty(encoding)){
                response.setCharacterEncoding(encoding);
            }

            //?????????????????????????????????
            bis = new BufferedInputStream(new FileInputStream(pdfPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error("<==== WmsStockTransferServiceImpl.getPrintPdfUrl.outputPDFFile Error", e);
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
                log.error("<==== WmsStockTransferServiceImpl.getPrintPdfUrl.closeIO Error", e);
            }
        }

        //??????????????????
        for (File file:qrcodeImageFileList
        ) {
            if(!file.delete()){
                log.info("<==== WmsStockTransferServiceImpl.multiplePrint.qrcodeImageFileList Failed: {}", barcodePath);
            }
        }
        if(!pdfFile.delete()){
            log.info("<==== WmsStockTransferServiceImpl.multiplePrint.pdfFile Failed: {}", barcodePath);
        }
    }

    private void validateStock(Long tenantId, List<WmsStockTransferHeadDTO> wmsStockTransferHeadDtoList){
        if(CollectionUtils.isEmpty(wmsStockTransferHeadDtoList)){
            throw new MtException("WMS_INV_TRANSFER_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_INV_TRANSFER_0028", "WMS", "wmsStockTransferHeadDtoList"));
        }
        for(WmsStockTransferHeadDTO dto : wmsStockTransferHeadDtoList){
            // ?????????????????????
            MtInstruction mtInstruction = mtInstructionRepository.selectOne(new MtInstruction() {{
                setSourceDocId(dto.getInstructionDocId());
                setTenantId(tenantId);
            }});
            if(Objects.isNull(mtInstruction)) {
                throw new MtException("WMS_INV_TRANSFER_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0029", "WMS", dto.getInstructionDocNum()));
            } else {
               // ?????????????????????????????????
                WmsStockAllocateSetting setting = wmsStockAllocateSettingRepository.selectOne(new WmsStockAllocateSetting() {{
                    setSiteId(dto.getSiteId());
                    setFromLocatorId(mtInstruction.getFromLocatorId());
                    setToLocatorId(mtInstruction.getToLocatorId());
                    setApproveSetting("Y");
                    setTenantId(tenantId);
                }});
                if(Objects.isNull(setting)){
                    throw new MtException("WMS_INV_TRANSFER_0030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_INV_TRANSFER_0030", "WMS", dto.getInstructionDocNum()));
                }
            }
        }
    }

    private void updateprintFlag(Long tenantId, List<String> instructionDocIdList){
        //??????API???eventCreate???????????????
        MtEventCreateVO event = new MtEventCreateVO();
        event.setEventTypeCode("DOCUTMENT_FRINT");
        String eventId = mtEventRepository.eventCreate(tenantId, event);
        List<MtExtendVO5> vo5List = new ArrayList<>();
        MtExtendVO5 vo5 = new MtExtendVO5();
        vo5.setAttrName("PRINT_FLAG");
        vo5.setAttrValue(MtBaseConstants.YES);
        vo5List.add(vo5);
        for (String instructionDocId:instructionDocIdList) {
            //??????????????????
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr",
                    instructionDocId, eventId, vo5List);
        }
    }
}
