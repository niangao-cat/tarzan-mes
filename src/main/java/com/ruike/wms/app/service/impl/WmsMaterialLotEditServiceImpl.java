package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsMaterialLotEditDTO;
import com.ruike.wms.api.dto.WmsMaterialLotEditResponseDTO;
import com.ruike.wms.api.dto.WmsMaterialLotEditUomDTO;
import com.ruike.wms.app.service.WmsCommonApiService;
import com.ruike.wms.app.service.WmsMaterialLotEditService;
import com.ruike.wms.domain.vo.WmsMaterialLotAttrViewVO;
import com.ruike.wms.infra.mapper.WmsMaterialLotEditMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.vo.MtCommonExtendVO4;
import io.tarzan.common.domain.vo.MtCommonExtendVO7;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtContainerLoadDetail;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.entity.MtMaterialLotHis;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotHisRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.tarzan.common.domain.util.MtBaseConstants.NO;

/**
 * @author kun.zhou
 * @Classname MaterialLotEditServiceImpl
 * @Description ????????????SERVICE
 * @Date 2020-03-17 10:36
 */
@Service
@Slf4j
public class WmsMaterialLotEditServiceImpl implements WmsMaterialLotEditService {

    @Autowired
    private WmsMaterialLotEditMapper materialLotEditMapper;
    @Autowired
    private WmsCommonApiService commonApiService;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtMaterialLotHisRepository mtMaterialLotHisRepository;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private CustomSequence customSequence;
//    @Autowired
//    private ZMaterialBinRepository zMaterialBinRepository;

    private static String MT_MATERIAL_LOT_ATTR_TABLE = "mt_material_lot_attr";
    private static String MATERIAL_LOT_INIT = "MATERIAL_LOT_INIT";
    private static String INSTRUCTION_ID = "INSTRUCTION_ID";
    private static String GRADE_CODE = "GRADE_CODE";
    private static String PRODUCT_DATE = "PRODUCT_DATE";
    private static String OVERDUE_INSPECTION_DATE = "OVERDUE_INSPECTION_DATE";
    private static String WO_ISSUE_DATE = "WO_ISSUE_DATE";
    private static String COLOR_BIN = "COLOR_BIN";
    private static String LIGHT_BIN = "LIGHT_BIN";
    private static String VOLTAGE_BIN = "VOLTAGE_BIN";
    private static String STICKER_NUMBER = "STICKER_NUMBER";
    private static String PRINTING = "PRINTING";
    private static String HUMIDITY_LEVEL = "HUMIDITY_LEVEL";
    private static String EXPANSION_COEFFICIENTS = "EXPANSION_COEFFICIENTS";
    private static String PO_NUM = "PO_NUM";
    private static String PO_LINE_NUM = "PO_LINE_NUM";
    private static String PO_LINE_LOCATION_NUM = "PO_LINE_LOCATION_NUM";
    private static String SO_NUM = "SO_NUM";
    private static String SO_LINE_NUM = "SO_LINE_NUM";
    private static String WBS_NUM = "WBS_NUM";
    private static String REMARK = "REMARK";
    private static String STATUS = "STATUS";
    private static String MATERIAL_VERSION = "MATERIAL_VERSION";
    private static String ENABLE_DATE = "ENABLE_DATE";
    private static String DEADLINE_DATE = "DEADLINE_DATE";
    private static String LAB_CODE = "LAB_CODE";
    private static String LAB_REMARK = "LAB_REMARK";


    @Override
    public Page<WmsMaterialLotEditResponseDTO> queryMaterialLotEdit(Long tenantId, WmsMaterialLotEditDTO dto,
                                                                    PageRequest pageRequest) {
        Page<WmsMaterialLotEditResponseDTO> queryPage =
                PageHelper.doPage(pageRequest, () -> materialLotEditMapper.queryMaterialLotEdit(tenantId, dto));

        if (CollectionUtils.isNotEmpty(queryPage)) {
            // ????????????????????????
            List<String> materialLotIds = queryPage.getContent().stream()
                    .map(WmsMaterialLotEditResponseDTO::getMaterialLotId).distinct().collect(Collectors.toList());
            List<WmsMaterialLotAttrViewVO> materialLotAttrViewVOList =
                    commonApiService.queryMaterialLotAttrViewData(tenantId, materialLotIds);
            Map<String, WmsMaterialLotAttrViewVO> materialLotAttrViewVOMap = null;
            // ??????map??????
            if (CollectionUtils.isNotEmpty(materialLotAttrViewVOList)) {
                materialLotAttrViewVOMap = materialLotAttrViewVOList.stream()
                        .collect(Collectors.toMap(t -> t.getMaterialLotId(), t -> t));
            }

            // ??????????????????
            List<LovValueDTO> enableFlagList =
                    commonApiService.queryLovValueList(tenantId, "Z_MTLOT_ENABLE_FLAG", null);
            Map<String, String> enableMap = null;
            if (CollectionUtils.isNotEmpty(enableFlagList)) {
                enableMap = enableFlagList.stream().collect(Collectors.toMap(t -> t.getValue(), t -> t.getMeaning()));
            }

            // ????????????
            List<LovValueDTO> statusList = commonApiService.queryLovValueList(tenantId, "Z.MTLOT.STATUS.G", null);
            Map<String, String> statusMap = null;
            if (CollectionUtils.isNotEmpty(enableFlagList)) {
                statusMap = statusList.stream().collect(Collectors.toMap(t -> t.getValue(), t -> t.getMeaning()));
            }

            // ??????????????????
            List<LovValueDTO> qualityStatusList =
                    commonApiService.queryLovValueList(tenantId, "MT.MTLOT.QUALITY_STATUS", null);
            Map<String, String> qualityStatusMap = null;
            if (CollectionUtils.isNotEmpty(enableFlagList)) {
                qualityStatusMap = qualityStatusList.stream()
                        .collect(Collectors.toMap(LovValueDTO::getValue, LovValueDTO::getMeaning));
            }

            // ????????????????????????
            List<String> warehouseIds = queryPage.getContent().stream().map(WmsMaterialLotEditResponseDTO::getWarehouseId)
                    .distinct().collect(Collectors.toList());
            // ???????????????
            Map<String, String> parentMap = null;
            // ?????????????????????
            Map<String, String> parentLocatorCodeMap = null;
            if (CollectionUtils.isNotEmpty(warehouseIds)) {
                List<MtModLocator> warehouseList =
                        mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, warehouseIds);
                if (CollectionUtils.isNotEmpty(warehouseList)) {
                    // ?????????????????????????????????
                    List<MtModLocator> hasParentLocatorList =
                            warehouseList.stream().filter(t -> StringUtils.isNotEmpty(t.getParentLocatorId()))
                                    .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(hasParentLocatorList)) {
                        parentMap = hasParentLocatorList.stream()
                                .collect(Collectors.toMap(MtModLocator::getLocatorId, MtModLocator::getParentLocatorId));

                        List<String> parentLocatorIds = hasParentLocatorList.stream()
                                .map(MtModLocator::getParentLocatorId).distinct().collect(Collectors.toList());
                        List<MtModLocator> parentLocatorList =
                                mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, parentLocatorIds);
                        if (CollectionUtils.isNotEmpty(parentLocatorList)) {
                            parentLocatorCodeMap = parentLocatorList.stream()
                                    .collect(Collectors.toMap(MtModLocator::getLocatorId, MtModLocator::getLocatorCode));
                        }
                    }
                }
            }

            // ????????????????????????
            // ????????????Id???????????????
            List<MtContainerLoadDetail> mtContainerLoadDetails = mtContainerLoadDetailRepository
                    .loadObjectLimitBatchGet(tenantId, "MATERIAL_LOT", materialLotIds);

            // ???????????????container??????
            Map<String, String> loadObjectMap = null;
            Map<String, MtContainer> mtContainerMap = null;
            if (CollectionUtils.isNotEmpty(mtContainerLoadDetails)) {
                loadObjectMap = mtContainerLoadDetails.stream()
                        .collect(Collectors.toMap(MtContainerLoadDetail::getLoadObjectId, MtContainerLoadDetail::getContainerId));

                // ??????????????????????????????
                List<String> containerIds = mtContainerLoadDetails.stream().map(MtContainerLoadDetail::getContainerId)
                        .distinct().collect(Collectors.toList());
                List<MtContainer> mtContainerList =
                        mtContainerRepository.containerPropertyBatchGet(tenantId, containerIds);
                if (CollectionUtils.isNotEmpty(mtContainerList)) {
                    mtContainerMap = mtContainerList.stream()
                            .collect(Collectors.toMap(MtContainer::getContainerId, t -> t));
                }
            }

            for (WmsMaterialLotEditResponseDTO response : queryPage.getContent()) {
                // ??????????????????
                if (materialLotAttrViewVOMap != null) {
                    WmsMaterialLotAttrViewVO materialLotAttrViewVO =
                            materialLotAttrViewVOMap.get(response.getMaterialLotId());
                    if (materialLotAttrViewVO != null) {
                        response.setStatus(materialLotAttrViewVO.getStatus());
                        response.setInstructionId(materialLotAttrViewVO.getInstructionId());
                        response.setProductDate(materialLotAttrViewVO.getProductDate());
                        response.setOverdueInspectionDate(materialLotAttrViewVO.getOverdueInspectionDate());
                        response.setWoIssueDate(materialLotAttrViewVO.getWoIssueDate());
                        response.setGradeCode(materialLotAttrViewVO.getGradeCode());
                        response.setColorBin(materialLotAttrViewVO.getColorBin());
                        response.setLightBin(materialLotAttrViewVO.getLightBin());
                        response.setVoltageBin(materialLotAttrViewVO.getVoltageBin());
                        response.setStickerNumber(materialLotAttrViewVO.getStickerNumber());
                        response.setPrinting(materialLotAttrViewVO.getPrinting());
                        response.setMsl(materialLotAttrViewVO.getMsl());
                        response.setExpansionCoefficients(materialLotAttrViewVO.getExpansionCoefficients());
                        response.setPoNum(materialLotAttrViewVO.getPoNum());
                        response.setPoLineNum(materialLotAttrViewVO.getPoLineNum());
                        response.setPoLineLocationNum(materialLotAttrViewVO.getPoLineLocationNum());
                        response.setSoNum(materialLotAttrViewVO.getSoNum());
                        response.setSoLineNum(materialLotAttrViewVO.getSoLineNum());
                        response.setWbsNum(materialLotAttrViewVO.getWbsNum());
                        response.setRemark(materialLotAttrViewVO.getRemark());
                        response.setMaterialVersion(materialLotAttrViewVO.getMaterialVersion());
                        response.setEnableDate(materialLotAttrViewVO.getEnableDate());
                        response.setDeadlineDate(materialLotAttrViewVO.getDeadlineDate());
                        response.setLabCode(materialLotAttrViewVO.getLabCode());
                        response.setLabRemark(materialLotAttrViewVO.getLabRemark());
                    }
                }

                // ??????????????????
                if (StringUtils.isNotBlank(response.getEnableFlag()) && enableMap != null) {
                    response.setEnableFlagMeaning(enableMap.get(response.getEnableFlag()));
                }
                // ????????????
                if (StringUtils.isNotBlank(response.getStatus()) && statusMap != null) {
                    response.setStatusMeaning(statusMap.get(response.getStatus()));
                }
                // ??????????????????
                if (StringUtils.isNotBlank(response.getQualityStatus()) && qualityStatusMap != null) {
                    response.setQualityStatusMeaning(qualityStatusMap.get(response.getQualityStatus()));
                }
                // ??????Warehouse codeWarehouse code
                if (StringUtils.isNotBlank(response.getWarehouseId()) && parentMap != null
                        && parentLocatorCodeMap != null) {
                    response.setWarehouseCode(parentLocatorCodeMap.get(parentMap.get(response.getWarehouseId())));
                }
                if (loadObjectMap != null && mtContainerMap != null) {
                    MtContainer mtContainer = mtContainerMap.get(loadObjectMap.get(response.getMaterialLotId()));
                    if (mtContainer != null) {
                        response.setContainerId(mtContainer.getContainerId());
                        response.setContainerCode(mtContainer.getContainerCode());
                    }
                }
            }
        }

        return queryPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Page<WmsMaterialLotEditDTO> updateMaterialLotData(Long tenantId, List<WmsMaterialLotEditResponseDTO> dtoList) {
        Page<WmsMaterialLotEditDTO> resultPage = new Page<>();
        // ??????eventRequestId
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, MATERIAL_LOT_INIT);
        // ??????eventId
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventRequestId(eventRequestId);
        eventCreate.setEventTypeCode(MATERIAL_LOT_INIT);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
        List<MtMaterialLotVO20> materialLotList = new ArrayList<>();
        List<MtCommonExtendVO7> attrList = new ArrayList<>();
        for (WmsMaterialLotEditResponseDTO response : dtoList) {
            MtMaterialLot setUpdateParam = setUpdateParam(tenantId, response);
            MtMaterialLotVO20 materialLot = new MtMaterialLotVO20();
            BeanUtils.copyProperties(setUpdateParam, materialLot);
            materialLotList.add(materialLot);
            // ????????????????????????
            attrList.add(new MtCommonExtendVO7(materialLot.getMaterialLotId(), getUpdateAttr(tenantId, response)));
        }
        mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotList, eventId, NO);
        mtExtendSettingsRepository.attrPropertyBatchUpdateNew(tenantId, "mt_material_lot_attr", eventId, attrList);
        return resultPage;
    }

    @Override
    public Page<WmsMaterialLotEditUomDTO> queryUom(Long tenantId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> materialLotEditMapper.queryUom(tenantId));
    }

    /**
     * ??????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return
     */
    public MtMaterialLot setUpdateParam(Long tenantId, WmsMaterialLotEditResponseDTO dto) {
        MtMaterialLot materialLot = new MtMaterialLot();
        materialLot.setTenantId(tenantId);
        materialLot.setMaterialLotId(dto.getMaterialLotId());
        MtMaterialLot materialLotResult = mtMaterialLotRepository.selectOne(materialLot);
        if (!dto.getMaterialLotCode().equals(materialLotResult.getMaterialLotCode())) {
            // ??????eventRequestId
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, MATERIAL_LOT_INIT);
            // ??????eventId
            MtEventCreateVO eventCreate = new MtEventCreateVO();
            eventCreate.setEventRequestId(eventRequestId);
            eventCreate.setEventTypeCode(MATERIAL_LOT_INIT);
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
            // ??????????????????
            materialLotResult.setMaterialLotCode(dto.getMaterialLotCode());
            mtMaterialLotRepository.updateByPrimaryKey(materialLotResult);
            // ????????????????????????
            MtMaterialLotHis materialLotHis = new MtMaterialLotHis();
            BeanUtils.copyProperties(materialLotResult, materialLotHis);
            materialLotHis.setMaterialLotCode(dto.getMaterialLotCode());
            materialLotHis.setEventId(eventId);
            materialLotHis.setMaterialLotHisId(customSequence.getNextKey("mt_material_lot_his_s"));
            materialLotHis.setCid(Long.valueOf(customSequence.getNextKey("mt_material_lot_his_cid_s")));
            mtMaterialLotHisRepository.insertSelective(materialLotHis);
        }
        materialLotResult = mtMaterialLotRepository.selectOne(materialLot);
        materialLotResult.setMaterialLotId(dto.getMaterialLotId());
        materialLotResult.setEnableFlag(dto.getEnableFlag());
        materialLotResult.setMaterialId(dto.getMaterialId());
        materialLotResult.setPrimaryUomQty(dto.getPrimaryUomQty());
        materialLotResult.setLot(dto.getLot());
        materialLotResult.setReservedObjectId(dto.getReservedObjectId());
        materialLotResult.setPrimaryUomId(dto.getUomId());
        materialLotResult.setLocatorId(dto.getLocatorId());
        materialLotResult.setQualityStatus(dto.getQualityStatus());
        materialLotResult.setSiteId(dto.getSiteId());
        materialLotResult.setSupplierId(dto.getSupplierId());
        return materialLotResult;

    }

    /**
     * ????????????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return
     */
    public List<MtCommonExtendVO4> getUpdateAttr(Long tenantId, WmsMaterialLotEditResponseDTO dto) {
        // ??????API[attrTableQuery]
        MtExtendVO mtExtend = new MtExtendVO();
        mtExtend.setTableName(MT_MATERIAL_LOT_ATTR_TABLE);
        mtExtend.setKeyId(dto.getMaterialLotId());
        List<MtExtendAttrVO> mtExtendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtend);
        // ??????API[attrTableUpdate]
        List<MtCommonExtendVO4> mtExtendUpdateList = new ArrayList<>();
        for (MtExtendAttrVO mtExtendAttr : mtExtendAttrList) {
            MtCommonExtendVO4 mtExtends = new MtCommonExtendVO4();
            Boolean updateFlag = GRADE_CODE.equals(mtExtendAttr.getAttrName())
                    || INSTRUCTION_ID.equals(mtExtendAttr.getAttrName())
                    || PRODUCT_DATE.equals(mtExtendAttr.getAttrName())
                    || OVERDUE_INSPECTION_DATE.equals(mtExtendAttr.getAttrName())
                    || WO_ISSUE_DATE.equals(mtExtendAttr.getAttrName())
                    || COLOR_BIN.equals(mtExtendAttr.getAttrName())
                    || LIGHT_BIN.equals(mtExtendAttr.getAttrName())
                    || VOLTAGE_BIN.equals(mtExtendAttr.getAttrName())
                    || STICKER_NUMBER.equals(mtExtendAttr.getAttrName())
                    || PRINTING.equals(mtExtendAttr.getAttrName())
                    || HUMIDITY_LEVEL.equals(mtExtendAttr.getAttrName())
                    || EXPANSION_COEFFICIENTS.equals(mtExtendAttr.getAttrName())
                    || PO_NUM.equals(mtExtendAttr.getAttrName())
                    || PO_LINE_NUM.equals(mtExtendAttr.getAttrName())
                    || PO_LINE_LOCATION_NUM.equals(mtExtendAttr.getAttrName())
                    || SO_NUM.equals(mtExtendAttr.getAttrName())
                    || SO_LINE_NUM.equals(mtExtendAttr.getAttrName())
                    || WBS_NUM.equals(mtExtendAttr.getAttrName())
                    || REMARK.equals(mtExtendAttr.getAttrName())
                    || STATUS.equals(mtExtendAttr.getAttrName())
                    || MATERIAL_VERSION.equals(mtExtendAttr.getAttrName())
                    || ENABLE_DATE.equals(mtExtendAttr.getAttrName())
                    || DEADLINE_DATE.equals(mtExtendAttr.getAttrName())
                    || LAB_CODE.equals(mtExtendAttr.getAttrName())
                    || LAB_REMARK.equals(mtExtendAttr.getAttrName());
            if (!updateFlag) {
                BeanUtils.copyProperties(mtExtendAttr, mtExtends);
                mtExtendUpdateList.add(mtExtends);
            }
        }
        List<String> exitAttrNames = mtExtendAttrList.stream().map(MtExtendAttrVO::getAttrName).collect(Collectors.toList());
        mtExtendUpdateList.addAll(setAttr(dto, exitAttrNames));
        return mtExtendUpdateList;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param dto
     * @return
     */
    public List<MtCommonExtendVO4> setAttr(WmsMaterialLotEditResponseDTO dto, List<String> exitAttrNames) {
        List<MtCommonExtendVO4> mtExtendUpdateList = new ArrayList<>();
        // ??????????????????
        subSetAttr(mtExtendUpdateList, exitAttrNames, GRADE_CODE, dto.getGradeCode());
        subSetAttr(mtExtendUpdateList, exitAttrNames, INSTRUCTION_ID, dto.getInstructionId());
        subSetAttr(mtExtendUpdateList, exitAttrNames, PRODUCT_DATE, dto.getProductDate());
        subSetAttr(mtExtendUpdateList, exitAttrNames, OVERDUE_INSPECTION_DATE, dto.getOverdueInspectionDate());
        subSetAttr(mtExtendUpdateList, exitAttrNames, WO_ISSUE_DATE, dto.getWoIssueDate());
        subSetAttr(mtExtendUpdateList, exitAttrNames, COLOR_BIN, dto.getColorBin());
        subSetAttr(mtExtendUpdateList, exitAttrNames, LIGHT_BIN, dto.getLightBin());
        subSetAttr(mtExtendUpdateList, exitAttrNames, VOLTAGE_BIN, dto.getVoltageBin());
        subSetAttr(mtExtendUpdateList, exitAttrNames, STICKER_NUMBER, dto.getStickerNumber());
        subSetAttr(mtExtendUpdateList, exitAttrNames, PRINTING, dto.getPrinting());
        subSetAttr(mtExtendUpdateList, exitAttrNames, HUMIDITY_LEVEL, dto.getMsl());
        subSetAttr(mtExtendUpdateList, exitAttrNames, EXPANSION_COEFFICIENTS, dto.getExpansionCoefficients());
        subSetAttr(mtExtendUpdateList, exitAttrNames, PO_NUM, dto.getPoNum());
        subSetAttr(mtExtendUpdateList, exitAttrNames, PO_LINE_NUM, dto.getPoLineNum());
        subSetAttr(mtExtendUpdateList, exitAttrNames, PO_LINE_LOCATION_NUM, dto.getPoLineLocationNum());
        subSetAttr(mtExtendUpdateList, exitAttrNames, SO_NUM, dto.getSoNum());
        subSetAttr(mtExtendUpdateList, exitAttrNames, SO_LINE_NUM, dto.getSoLineNum());
        subSetAttr(mtExtendUpdateList, exitAttrNames, WBS_NUM, dto.getWbsNum());
        subSetAttr(mtExtendUpdateList, exitAttrNames, REMARK, dto.getRemark());
        subSetAttr(mtExtendUpdateList, exitAttrNames, STATUS, dto.getStatus());
        subSetAttr(mtExtendUpdateList, exitAttrNames, MATERIAL_VERSION, dto.getMaterialVersion());
        subSetAttr(mtExtendUpdateList, exitAttrNames, DEADLINE_DATE, dto.getDeadlineDate());
        subSetAttr(mtExtendUpdateList, exitAttrNames, ENABLE_DATE, dto.getEnableDate());
        subSetAttr(mtExtendUpdateList, exitAttrNames, LAB_CODE, dto.getLabCode());
        subSetAttr(mtExtendUpdateList, exitAttrNames, LAB_REMARK, dto.getLabRemark());

        return mtExtendUpdateList;
    }

    private void subSetAttr(List<MtCommonExtendVO4> mtExtendUpdateList, List<String> exitAttrNames, String attrName, String attrValue) {
        if (exitAttrNames.contains(attrName)) {
            MtCommonExtendVO4 mtExtends = new MtCommonExtendVO4();
            mtExtends.setAttrName(attrName);
            mtExtends.setAttrValue(attrValue);
            mtExtendUpdateList.add(mtExtends);
        } else if (StringUtils.isNotBlank(attrValue)) {
            MtCommonExtendVO4 mtExtends = new MtCommonExtendVO4();
            mtExtends.setAttrName(attrName);
            mtExtends.setAttrValue(attrValue);
            mtExtendUpdateList.add(mtExtends);
        }
    }

}
