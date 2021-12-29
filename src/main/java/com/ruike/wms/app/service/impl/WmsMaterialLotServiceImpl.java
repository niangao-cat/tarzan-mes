package com.ruike.wms.app.service.impl;

import com.ruike.hme.domain.entity.HmeMaterialLotLabCode;
import com.ruike.hme.domain.vo.HmeSnBindEoVO2;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeMaterialLotLabCodeMapper;
import com.ruike.hme.infra.mapper.HmeCosPatchPdaMapper;
import com.ruike.hme.infra.mapper.HmeSnBindEoMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsCommonApiService;
import com.ruike.wms.app.service.WmsEventService;
import com.ruike.wms.app.service.WmsMaterialLotService;
import com.ruike.wms.domain.repository.WmsMaterialLotRepository;
import com.ruike.wms.domain.vo.WmsEventVO;
import com.ruike.wms.domain.vo.WmsMaterialLotAttrVO;
import com.ruike.wms.domain.vo.WmsMaterialLotExtendAttrVO;
import com.ruike.wms.domain.vo.WmsMaterialLotPntVO;
import com.ruike.wms.infra.barcode.CommonFreemarkerTemplate;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.CommonQRCodeUtil;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsMaterialLotMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.util.FileUtil;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.file.FileClient;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.api.dto.MtTagGroupObjectDTO3;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.general.infra.mapper.MtTagGroupObjectMapper;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.entity.MtMaterialLotHis;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotHisRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO13;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO26;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtMaterialVO1;
import tarzan.material.domain.vo.MtMaterialVO2;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.order.domain.entity.MtWorkOrder;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;
import static com.ruike.wms.infra.constant.WmsConstant.EventType.HME_PRODUCTION_VERSION_CHANGE;

/**
 * @Classname WmsMaterialLotService
 * @Description 条码功能
 * @Date 2019/9/16 14:58
 * @Created by admin
 */
@Slf4j
@Service
public class WmsMaterialLotServiceImpl implements WmsMaterialLotService {

    @Autowired
    private MtMaterialLotRepository materialLotRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private WmsMaterialLotMapper wmsMaterialLotMapper;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    WmsCommonServiceComponent wmsCommonServiceComponent;

    @Autowired
    MtMaterialRepository materialRepository;

    @Autowired
    FileClient fileClient;

    @Autowired
    private WmsCommonApiService wmsCommonApiService;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtTagGroupObjectMapper mtTagGroupObjectMapper;

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private WmsEventService wmsEventService;

    @Autowired
    private WmsMaterialLotRepository wmsMaterialLotRepository;

    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;

    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtMaterialLotHisRepository mtMaterialLotHisRepository;

    @Autowired
    private HmeMaterialLotLabCodeMapper hmeMaterialLotLabCodeMapper;

    @Autowired
    private HmeSnBindEoMapper hmeSnBindEoMapper;

    @Autowired
    private HmeCosPatchPdaMapper hmeCosPatchPdaMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public String materialLotCreate(Long tenantId, WmsMaterialLotAddDTO dto) {
        //更新条码 物料版本（暂时）
        if (StringUtils.isNotBlank(dto.getMaterialLotId())) {
            if (StringUtils.isNotBlank(dto.getMaterialVersion())) {
                MtMaterialLot mtMaterialLot = materialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
                //限制状态
                MtExtendSettings reworkAttr = new MtExtendSettings();
                reworkAttr.setAttrName("STATUS");
                List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                        "mt_material_lot_attr", "MATERIAL_LOT_ID", dto.getMaterialLotId(), Collections.singletonList(reworkAttr));
                List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.MATERIALLOT_STATUS_ENABLED", tenantId);
                List<String> statusList = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                    if (!statusList.contains(mtExtendAttrVOList.get(0).getAttrValue())) {
                        throw new MtException("HME_NC_0015", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "HME_NC_0015", "HME", mtMaterialLot.getMaterialLotCode()));
                    }
                }

                //限制货位
                List<LovValueDTO> locatorList = lovAdapter.queryLovValue("HME.MATERIALLOT_STATUS_DISABLED", tenantId);
                if (CollectionUtils.isNotEmpty(locatorList)) {
                    MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
                    if (mtModLocator != null) {
                        List<String> locatorCodeList = locatorList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                        if (!locatorCodeList.contains(mtModLocator.getLocatorCode())) {
                            throw new MtException("HME_NC_0016", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "HME_NC_0016", "HME", mtMaterialLot.getMaterialLotCode()));
                        }
                    }
                }

                // 创建数据采集实绩
                MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                eventCreateVO.setEventTypeCode("PRODUCTION_VERSION_CHANGE");
                // 创建事件
                String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                List<MtExtendVO5> attr = new ArrayList<>();
                MtExtendVO5 vo5 = new MtExtendVO5();
                vo5.setAttrName("MATERIAL_VERSION");
                vo5.setAttrValue(dto.getMaterialVersion());
                attr.add(vo5);
                mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", dto.getMaterialLotId(), eventId, attr);
            }
            return dto.getMaterialLotId();
        }

        if (StringUtils.isNotBlank(dto.getMaterialLotCode())) {
            MtMaterialLot mtMaterialLot = new MtMaterialLot();
            mtMaterialLot.setTenantId(tenantId);
            mtMaterialLot.setMaterialLotCode(dto.getMaterialLotCode());
            mtMaterialLot = mtMaterialLotMapper.selectOne(mtMaterialLot);
            if (mtMaterialLot != null) {
                throw new MtException("HME_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "HME_MATERIAL_LOT_0001", "HME", dto.getMaterialLotCode()));
            }
        }

        String eventId = wmsCommonServiceComponent.generateEvent(tenantId, WmsConstant.EVENT_REQUEST_CODE_BARCODE);
        if (StringUtils.isEmpty(eventId)) {
            log.error("<==== materialLotCreate event is null");
            throw new MtException("MT_GENERAL_0061", mtErrorMessageRepository.getErrorMessageWithModule(
                    tenantId, "MT_GENERAL_0061", "GENERAL"));
        }

        /*if (StringUtils.isEmpty(dto.getLot())) {
            log.error("<==== materialLotCreate lot is null");
            throw new WmsException("物料批次号为空");
        }*/

        /*CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        MtUserOrganization userOrganization = commonServiceComponent.getUserOrganization(tenantId, userId);
        if (userOrganization == null) {
            log.error("<==== materialLotCreate MtUserOrganization is null");
            return null;
        }*/

        Integer createCount = dto.getCreateQty();
        createCount = createCount == null || createCount == 0 ? 1 : createCount;
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < createCount; i++) {
            /*
            MtNumrangeVO2 numrangeVO2 = new MtNumrangeVO2();
            numrangeVO2.setObjectCode(WmsKeyConstant.NUM_GEN_BARCODE);
            Map<String, String> objectCodeMap = new HashMap<>(2);
            objectCodeMap.put(WmsKeyConstant.NUM_GEN_CODE_OBJECT, dto.getLot());
            objectCodeMap.put("materialCode", materialRepository.materialPropertyGet(tenantId, dto.getMaterialId()).getMaterialCode());
            numrangeVO2.setCallObjectCodeList(objectCodeMap);
            MtUserOrganizationDTO6 mtUserOrganizationDTO6 = mtUserOrganizationService.userDefaultSiteForUi(tenantId);
            numrangeVO2.setSiteId(mtUserOrganizationDTO6.getSiteId());
            if (org.apache.commons.lang3.StringUtils.isNotBlank(dto.getCreateReason())) {
                MtGenType mtGenType = mtGenTypeRepository.selectByPrimaryKey(dto.getCreateReason());
                numrangeVO2.setObjectTypeCode(mtGenType.getTypeCode());
            }

            MtNumrangeVO5 numrangeVO5 = commonServiceComponent.generateNumber(tenantId, numrangeVO2);
            if (null == numrangeVO5) {
                log.error("<==== materialLotCreate numrangeGenerate is null");
                throw new WmsException("物料批编码生成异常");
            }
            */
            //生产批次号 add by sanfeng.zhang for yiwei 2020/10/06
            if (StringUtils.isBlank(dto.getLot())) {
                MtNumrangeVO2 mtNumrange = new MtNumrangeVO2();
                mtNumrange.setObjectCode("RECEIPT_BATCH");
                MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrange);
                dto.setLot(mtNumrangeVO5 != null ? mtNumrangeVO5.getNumber() : null);
            } else {
                //效验输入的批次号 10位加数字
                if (!CommonUtils.isNumeric(dto.getLot()) && dto.getLot().length() != 10) {
                    throw new MtException("HME_MATERIAL_LOT_004", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "HME_MATERIAL_LOT_004", "HME"));
                }
            }
            // ids.add(createMaterialLot(tenantId, eventId, dto, userOrganization.getOrganizationId()));
            ids.add(createMaterialLot(tenantId, eventId, dto));
        }
        return Arrays.toString(ids.toArray());
    }

    @Override
    public MtModSite siteBasicPropertyGet(Long tenantId) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        MtUserOrganization userOrganization = wmsCommonServiceComponent.getUserOrganization(tenantId, userId);
        if (userOrganization == null) {
            log.error("<==== materialLotCreate MtUserOrganization is null");
            return null;
        }
        return mtModSiteRepository.siteBasicPropertyGet(tenantId, userOrganization.getOrganizationId());
    }

    private String createMaterialLot(Long tenantId, String eventId, WmsMaterialLotAddDTO dto) {
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        BeanUtils.copyProperties(dto, mtMaterialLotVO2);

        mtMaterialLotVO2.setEventId(eventId);
        //mtMaterialLotVO2.setMaterialLotCode(number);
        //update by sanfeng.zhang 2020/9/9 默认为N
        mtMaterialLotVO2.setEnableFlag(WmsConstant.CONSTANT_N);
        mtMaterialLotVO2.setLocatorId(dto.getLocatorId());
        mtMaterialLotVO2.setQualityStatus(dto.getQualityStatus());
        mtMaterialLotVO2.setSiteId(dto.getSiteId());
        mtMaterialLotVO2.setTenantId(tenantId);
        mtMaterialLotVO2.setPrimaryUomQty(dto.getPrimaryUomQty().doubleValue());
        mtMaterialLotVO2.setCid(Long.valueOf(customSequence.getNextKey("mt_material_lot_cid_s")));
        mtMaterialLotVO2.setSupplierId(dto.getSupplierId());
        if (StringUtils.isNotBlank(dto.getMaterialLotCode())) {
            mtMaterialLotVO2.setOutsideNum(dto.getMaterialLotCode());
        }

        /**
         * add by liyuan.lv, 2020-03-25
         */
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dto.getCreateReason())) {
            MtGenType mtGenType = mtGenTypeRepository.selectByPrimaryKey(dto.getCreateReason());
            mtMaterialLotVO2.setCreateReason(mtGenType != null ? mtGenType.getTypeCode() : "");
            mtMaterialLotVO2.setBusinessType(mtGenType.getTypeCode());
        }

        MtMaterialLotVO13 mtMaterialLotVO13 = materialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, WmsConstant.CONSTANT_N);
        String materialLotId = mtMaterialLotVO13.getMaterialLotId();
        log.info("<==== materialLotCreate materialLotId:{}", materialLotId);

        List<MtExtendVO5> attr = new ArrayList<>(10);
        MtExtendVO5 tmpVo = null;
        if (!StringUtils.isEmpty(dto.getProductDate())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("PRODUCT_DATE");
            tmpVo.setAttrValue(dto.getProductDate());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getSoNum())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("SO_NUM");
            tmpVo.setAttrValue(dto.getSoNum());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getSupplierLot())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("SUPPLIER_LOT");
            tmpVo.setAttrValue(dto.getSupplierLot());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getSoLineNum())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("SO_LINE_NUM");
            tmpVo.setAttrValue(dto.getSoLineNum());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getStatus())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName(HmeConstants.ExtendAttr.STATUS);
            tmpVo.setAttrValue(dto.getStatus());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getColorBin())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("COLOR_BIN");
            tmpVo.setAttrValue(dto.getColorBin());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getLightBin())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("LIGHT_BIN");
            tmpVo.setAttrValue(dto.getLightBin());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getVoltageBin())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("VOLTAGE_BIN");
            tmpVo.setAttrValue(dto.getVoltageBin());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getPerformanceLevel())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("PERFORMANCE_LEVEL");
            tmpVo.setAttrValue(dto.getPerformanceLevel());
            attr.add(tmpVo);
        }
//        if (!StringUtils.isEmpty(dto.getWarehouseId())) {
//            tmpVo = new MtExtendVO5();
//            tmpVo.setAttrName("WAREHOUSE_ID");
//            tmpVo.setAttrValue(dto.getWarehouseId());
//            attr.add(tmpVo);
//        }
        if (!StringUtils.isEmpty(dto.getMaterialVersion())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("MATERIAL_VERSION");
            tmpVo.setAttrValue(dto.getMaterialVersion());
            attr.add(tmpVo);
        }
        if (attr.size() > 0) {
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", materialLotId,
                    eventId, attr);
            log.debug("<==== materialLotCreate attrPropertyUpdate success ");
        }

        return materialLotId;
    }

    @Override
    @ProcessLovValue
    public Page<WmsMaterialLotQryResultDTO> selectBarCodeCondition(PageRequest pageRequest, WmsMaterialLotQryDTO materialLotQryDTO, Long tenantId) {
        Page<WmsMaterialLotQryResultDTO> page = PageHelper.doPage(pageRequest, () -> wmsMaterialLotMapper.selectBarCodeCondition(tenantId, materialLotQryDTO));
        MtExtendVO1 extendVO1 = new MtExtendVO1();
        extendVO1.setTableName("mt_material_lot_attr");
        List<String> materialLotIdList = page.getContent().stream().map(WmsMaterialLotQryResultDTO::getMaterialLotId).distinct().collect(Collectors.toList());
        List<MtExtendAttrVO1> extendAttrList = new ArrayList<>();
        List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(materialLotIdList)) {
            List<List<String>> splitMaterialLotIdList = CommonUtils.splitSqlList(materialLotIdList, 3000);
            for (List<String> splitMaterialLotId:splitMaterialLotIdList) {
                extendVO1.setKeyIdList(splitMaterialLotId);
                List<MtExtendAttrVO1> subExtendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, extendVO1);
                if(CollectionUtils.isNotEmpty(subExtendAttrList)){
                    extendAttrList.addAll(subExtendAttrList);
                }
                List<HmeMaterialLotLabCode> subHmeMaterialLotLabCodeList = wmsMaterialLotMapper.queryLabCodeByMaterialLotId(tenantId, splitMaterialLotId);
                if(CollectionUtils.isNotEmpty(subHmeMaterialLotLabCodeList)){
                    hmeMaterialLotLabCodeList.addAll(subHmeMaterialLotLabCodeList);
                }
            }

//            extendVO1.setKeyIdList(materialLotIdList);
//            extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, extendVO1);
//            hmeMaterialLotLabCodeList = wmsMaterialLotMapper.queryLabCodeByMaterialLotId(tenantId, materialLotIdList);
        }
        // 查询实验代码
        Map<String, List<HmeMaterialLotLabCode>> labCodeMap = hmeMaterialLotLabCodeList.stream().collect(Collectors.groupingBy(HmeMaterialLotLabCode::getMaterialLotId));

        Map<String, List<MtExtendAttrVO1>> attrList = extendAttrList.stream().collect(Collectors.groupingBy(dto -> dto.getKeyId() + "_" + dto.getAttrName()));

        //在制品标识
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WMS.FLAG_YN", tenantId);

        Map<String, List<LovValueDTO>> lovValueDTOSMap = lovValueDTOS.stream().collect(Collectors.groupingBy(dto -> dto.getValue()));

        //实际货位
        List<String> actualLocatorCodeList = page.getContent().stream().map(WmsMaterialLotQryResultDTO::getActualLocatorCode).distinct().filter(Objects::nonNull).collect(Collectors.toList());

        List<MtModLocator> locatorList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(actualLocatorCodeList)) {
            locatorList = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class)
                    .andWhere(Sqls.custom().andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtModLocator.FIELD_LOCATOR_CATEGORY, "INVENTORY")
                            .andIn(MtModLocator.FIELD_LOCATOR_CODE, actualLocatorCodeList)).build());
        }

        Map<String, List<MtModLocator>> locatorListMap = locatorList.stream().collect(Collectors.groupingBy(dto -> dto.getLocatorCode()));

        //转型物料
        List<String> materialIdList = page.getContent().stream().map(WmsMaterialLotQryResultDTO::getPerformanceLevel).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<MtMaterial> mtMaterialList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(materialIdList)) {
            mtMaterialList = mtMaterialRepository.selectByIds(StringUtils.join(materialIdList, ","));
        }

        Map<String, List<MtMaterial>> mtMaterialListMap = mtMaterialList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialId()));

        //物料版本
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = userDetails != null ? userDetails.getUserId() : -1L;
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);

        List<String> mtMaterialIdList = page.getContent().stream().map(WmsMaterialLotQryResultDTO::getMaterialId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<String> materialVersionList = page.getContent().stream().map(WmsMaterialLotQryResultDTO::getMaterialVersion).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<MtTagGroupObjectDTO3> dto3List = wmsMaterialLotRepository.batchProductionVersionQuery(tenantId, defaultSiteId, mtMaterialIdList, materialVersionList);

        Map<String, List<MtTagGroupObjectDTO3>> dto3ListMap = dto3List.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialId() + "_" + dto.getProductionVersion()));

        for (WmsMaterialLotQryResultDTO wmsMaterialLotQryResultDTO : page.getContent()) {
            if(wmsMaterialLotQryResultDTO.getSapAccountFlag()!=null && wmsMaterialLotQryResultDTO.getSapAccountFlag().equals("N")){
                wmsMaterialLotQryResultDTO.setSapAccountFlag("N");
            }else {
                wmsMaterialLotQryResultDTO.setSapAccountFlag("Y");
            }
            //实际货位
            if (StringUtils.isNotBlank(wmsMaterialLotQryResultDTO.getActualLocatorCode())) {
                List<MtModLocator> mtModLocatorList = locatorListMap.get(wmsMaterialLotQryResultDTO.getActualLocatorCode());
                if (CollectionUtils.isNotEmpty(mtModLocatorList)) {
                    wmsMaterialLotQryResultDTO.setActualLocatorName(mtModLocatorList.get(0).getLocatorName());
                }
            }

            //转型物料
            if (StringUtils.isNotBlank(wmsMaterialLotQryResultDTO.getPerformanceLevel())) {
                List<MtMaterial> materialList = mtMaterialListMap.get(wmsMaterialLotQryResultDTO.getPerformanceLevel());
                if (CollectionUtils.isNotEmpty(materialList)) {
                    wmsMaterialLotQryResultDTO.setPerformanceLevelName(materialList.get(0).getMaterialCode());
                }
            }

            //物料版本
            if (StringUtils.isNotBlank(wmsMaterialLotQryResultDTO.getMaterialVersion())) {
                List<MtTagGroupObjectDTO3> mtTagGroupObjectDTO3List = dto3ListMap.get(wmsMaterialLotQryResultDTO.getMaterialId() + "_" + wmsMaterialLotQryResultDTO.getMaterialVersion());
                if (CollectionUtils.isNotEmpty(mtTagGroupObjectDTO3List)) {
                    wmsMaterialLotQryResultDTO.setMaterialVersionMeaning(mtTagGroupObjectDTO3List.get(0).getDescription());
                }
            }

            //在制品标识
            List<LovValueDTO> collect = lovValueDTOSMap.get(wmsMaterialLotQryResultDTO.getMfFlag());
            if (CollectionUtils.isNotEmpty(collect)) {
                wmsMaterialLotQryResultDTO.setMfFlagMeaning(collect.get(0).getMeaning());
            }

            // 实验代码
            List<HmeMaterialLotLabCode> materialLotLabCodeList = labCodeMap.get(wmsMaterialLotQryResultDTO.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(materialLotLabCodeList)) {
                List<String> labCodeList = materialLotLabCodeList.stream().map(HmeMaterialLotLabCode::getLabCode).collect(Collectors.toList());
                wmsMaterialLotQryResultDTO.setLabCode(StringUtils.join(labCodeList.toArray(), ","));
            }

            //获取所有扩展属性
            wmsMaterialLotQryResultDTO.setMsl(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "MSL")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "MSL").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setPrinting(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PRINTING")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PRINTING").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setExpansionCoefficients(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "EXPANSION_COEFFICIENTS")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "EXPANSION_COEFFICIENTS").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setInstructionId(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "INSTRUCTION_ID")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "INSTRUCTION_ID").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setStickerNumber(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "STICKER_NUMBER")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "STICKER_NUMBER").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setPrintTime(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PRINT_TIME")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PRINT_TIME").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setPrintReason(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PRINT_REASON")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PRINT_REASON").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setProductDate(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PRODUCT_DATE")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PRODUCT_DATE").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setOverdueInspectionDate(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "OVERDUE_INSPECTION_DATE")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "OVERDUE_INSPECTION_DATE").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setWoIssueDate(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "WO_ISSUE_DATE")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "WO_ISSUE_DATE").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setColorBin(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "COLOR_BIN")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "COLOR_BIN").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setLightBin(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "LIGHT_BIN")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "LIGHT_BIN").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setVoltageBin(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "VOLTAGE_BIN")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "VOLTAGE_BIN").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setPoLineLocationNum(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PO_LINE_LOCATION_NUM")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PO_LINE_LOCATION_NUM").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setEffectiveDate(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "EFFECTIVE_DATE")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "EFFECTIVE_DATE").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setEnableDate(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "ENABLE_DATE")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "ENABLE_DATE").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setDeadlineDate(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "DEADLINE_DATE")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "DEADLINE_DATE").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setCurrentWck(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "CURRENT_WCK")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "CURRENT_WCK").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setFinalProcessWck(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "FINAL_PROCESS_WCK")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "FINAL_PROCESS_WCK").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setReworkFlag(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "REWORK_FLAG")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "REWORK_FLAG").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setPerformanceLevel(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PERFORMANCE_LEVEL")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PERFORMANCE_LEVEL").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setDeliveryNum(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "DELIVERY_NUM")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "DELIVERY_NUM").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setDeliveryLineNum(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "DELIVERY_LINE_NUM")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "DELIVERY_LINE_NUM").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setSupplierLot(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "SUPPLIER_LOT")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "SUPPLIER_LOT").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setReplacementFlag(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "NC_SUPPLIER_REPLACEMENT")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "NC_SUPPLIER_REPLACEMENT").get(0).getAttrValue() : "");
            wmsMaterialLotQryResultDTO.setDesignedReworkFlag(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "DESIGNED_REWORK_FLAG")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "DESIGNED_REWORK_FLAG").get(0).getAttrValue() : "");
            // 如果指定工艺路线返修标识不为Y 则不显示返修工艺路线信息
            if (!WmsConstant.CONSTANT_Y.equals(wmsMaterialLotQryResultDTO.getDesignedReworkFlag())) {
                wmsMaterialLotQryResultDTO.setReworkRouterId("");
                wmsMaterialLotQryResultDTO.setReworkRouterName("");
                wmsMaterialLotQryResultDTO.setReworkRouterDesc("");
                wmsMaterialLotQryResultDTO.setReworkRouterVersion("");
            }
        }
        return page;
    }

    @Override
    @ProcessLovValue
    public Page<WmsMaterialLotHisResultDTO> selectBarCodeHis(PageRequest pageRequest, WmsMaterialLotHisQryDTO materialLotQryDTO, Long tenantId) {
        Page<WmsMaterialLotHisResultDTO> page = PageHelper.doPageAndSort(pageRequest, () -> wmsMaterialLotMapper.selectBarCodeHis(materialLotQryDTO, tenantId));
        for (WmsMaterialLotHisResultDTO wmsMaterialLotHisResultDTO : page.getContent()) {
            //实际货位
            if (StringUtils.isNotBlank(wmsMaterialLotHisResultDTO.getActualLocatorCode())) {
                MtModLocator locator = new MtModLocator();
                locator.setTenantId(tenantId);
                locator.setLocatorCategory("INVENTORY");
                locator.setLocatorCode(wmsMaterialLotHisResultDTO.getActualLocatorCode());
                List<MtModLocator> locatorList = mtModLocatorRepository.select(locator);
                if (CollectionUtils.isNotEmpty(locatorList)) {
                    wmsMaterialLotHisResultDTO.setActualLocatorName(locatorList.get(0).getLocatorName());
                }
            }

            //原始条码
            if (StringUtils.isNotBlank(wmsMaterialLotHisResultDTO.getOriginalId())) {
                MtMaterialLot mtMaterialLot = materialLotRepository.selectByPrimaryKey(wmsMaterialLotHisResultDTO.getOriginalId());
                wmsMaterialLotHisResultDTO.setOriginalCode(mtMaterialLot != null ? mtMaterialLot.getMaterialLotCode() : "");
            }

            //转型物料
            if (StringUtils.isNotBlank(wmsMaterialLotHisResultDTO.getPerformanceLevel())) {
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(wmsMaterialLotHisResultDTO.getPerformanceLevel());
                wmsMaterialLotHisResultDTO.setPerformanceLevelName(mtMaterial != null ? mtMaterial.getMaterialCode() : "");
            }

            //在制品标识
            List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WMS.FLAG_YN", tenantId);
            List<LovValueDTO> collect = lovValueDTOS.stream().filter(e -> StringUtils.equals(e.getValue(), wmsMaterialLotHisResultDTO.getMfFlag())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                wmsMaterialLotHisResultDTO.setMfFlagMeaning(collect.get(0).getMeaning());
            }
        }
        return page;
    }

    @Override
    public String queryPrintData(Long tenantId, List<String> materialLotIds) {
        List<WmsMaterialLotPntVO> materialLotList = wmsMaterialLotMapper.queryPrintData(tenantId, materialLotIds);
        log.info("<==== MaterialLotServiceImpl queryPrintData materialLotList: {}", materialLotList);
        if (CollectionUtils.isEmpty(materialLotList)) {
            return null;
        }
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);
        String basePath = classUrl + "//templates";
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "//templates");
            if (!file.exists()) {
                file.mkdir();
            }
            basePath = systemPath + "//templates";
        } else {
            basePath = classUrl + "//templates";
        }

        String pdfPath = basePath + UUID.randomUUID().toString() + ".pdf";
        java.util.List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> dataMap = null;
        List<File> fileList = new ArrayList<>(materialLotList.size());
        for (WmsMaterialLotPntVO mtl : materialLotList) {
            dataMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            if (!StringUtils.isEmpty(mtl.getSupplierName())) {
                if (mtl.getSupplierName().length() > 15) {
                    mtl.setSupplierName(mtl.getSupplierName().substring(0, 15));
                }
            }
            if (StringUtils.isEmpty(mtl.getColorBin()) && StringUtils.isEmpty(mtl.getLightBin())
                    && StringUtils.isEmpty(mtl.getVoltageBin())) {
            } else {
                StringBuilder ecs = new StringBuilder();
                if (!StringUtils.isEmpty(mtl.getColorBin())) {
                    ecs.append(mtl.getColorBin());
                }
                if (!StringUtils.isEmpty(mtl.getLightBin())) {
                    if (ecs.length() > 0) {
                        ecs.append("|");
                    }
                    ecs.append(mtl.getLightBin());
                }
                if (!StringUtils.isEmpty(mtl.getVoltageBin())) {
                    if (ecs.length() > 0) {
                        ecs.append("|");
                    }
                    ecs.append(mtl.getVoltageBin());
                }
                mtl.setExpansionCoefficients(ecs.toString());
            }
            String uuid = UUID.randomUUID().toString();
            String barCodePath = basePath + "/" + uuid + ".png";

            //生成条形码
            File barCodeFile = new File(barCodePath);
            fileList.add(barCodeFile);
            CommonQRCodeUtil.encode(mtl.getMaterialLotCode(), null, barCodePath, true);
            log.info("<====生成条形码完成！{}", barCodePath);
            mtl.setBarcodeImg(barCodePath);


            //文本内容map
            Map<String, Object> map = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            map.put("supplierName", mtl.getSupplierName());
            map.put("materialCode", mtl.getMaterialCode());
            map.put("materialName", mtl.getMaterialName());
            DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
            map.put("primaryUomQty", decimalFormat.format(mtl.getPrimaryUomQty()) + "/" + mtl.getPrimaryUomCode());
            map.put("lot", mtl.getLot());
            map.put("materialLotCode", mtl.getMaterialLotCode());
            map.put("expansionCoefficients", mtl.getExpansionCoefficients());
            map.put("so", null);
            map.put("remark", null);
            map.put("customerCode", null);

            //图片
            Map<String, Object> imgmap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            imgmap.put("barcodeImg", mtl.getBarcodeImg());

            //组装map传过去
            Map<String, Object> o = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            o.put("formMap", map);
            o.put("imgMap", imgmap);
            dataList.add(o);
        }

        String filePath = null;
        try {
            log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, dataList);
            CommonPdfTemplateUtil.multiplePage(basePath + "/materialLot_template.pdf", pdfPath, dataList);
            File pdfFile = new File(pdfPath);
            log.info("<==== 生成PDF完成！{}", pdfFile);
            String d = DateUtil.date2String(new Date(), "yyyyMMdd");
            filePath = fileClient.
                    uploadFile(tenantId, "materiallot", "pages" + "/" + d,
                            System.currentTimeMillis() + ".pdf", "application/pdf", FileUtil.File2byte(pdfFile));
            log.info("<==== pdf url: {}", filePath);
            pdfFile.delete();
            for (File file : fileList) {
                file.delete();
            }
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("erorr!", e);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return filePath;
    }

    @Override
    public String queryPrintData2(Long tenantId, List<String> materialLotIds) {
        List<WmsMaterialLotPntVO> materialLotList = wmsMaterialLotMapper.queryPrintData(tenantId, materialLotIds);
        if (CollectionUtils.isEmpty(materialLotList)) {
            return null;
        }
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);
        String basePath = classUrl + "//templates";
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "//templates");
            if (!file.exists()) {
                file.mkdir();
            }
            basePath = systemPath + "//templates";
        } else {
            basePath = classUrl + "//templates";
        }

        String htmlPath = basePath + UUID.randomUUID().toString() + ".html";
        Map<String, Object> dataMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        for (WmsMaterialLotPntVO mtl : materialLotList) {
            if (!StringUtils.isEmpty(mtl.getSupplierName())) {
                if (mtl.getSupplierName().length() > 13) {
                    mtl.setSupplierName(mtl.getSupplierName().substring(0, 13));
                }
            }
            if (StringUtils.isEmpty(mtl.getColorBin()) && StringUtils.isEmpty(mtl.getLightBin())
                    && StringUtils.isEmpty(mtl.getVoltageBin())) {
            } else {
                StringBuilder ecs = new StringBuilder();
                if (!StringUtils.isEmpty(mtl.getColorBin())) {
                    ecs.append(mtl.getColorBin());
                }
                if (!StringUtils.isEmpty(mtl.getLightBin())) {
                    if (ecs.length() > 0) {
                        ecs.append("|");
                    }
                    ecs.append(mtl.getLightBin());
                }
                if (!StringUtils.isEmpty(mtl.getVoltageBin())) {
                    if (ecs.length() > 0) {
                        ecs.append("|");
                    }
                    ecs.append(mtl.getVoltageBin());
                }
                mtl.setExpansionCoefficients(ecs.toString());
            }
            String uuid = UUID.randomUUID().toString();
            String barCodePath = basePath + "/" + uuid + ".png";

            //生成条形码
            File barCodeFile = new File(barCodePath);
            CommonQRCodeUtil.encode(mtl.getMaterialLotCode(), null, barCodePath, true);
            log.info("<====生成条形码完成！{}", barCodePath);
            String barCodeRemote = fileClient.
                    uploadFile(tenantId, "MATERIAL_LOT", "PRINT" + "/" + mtl.getMaterialLotCode(),
                            mtl.getMaterialLotCode() + ".png", FileUtil.File2byte(barCodeFile));
            mtl.setBarcodeImg(barCodeRemote);
            barCodeFile.delete();
        }
        dataMap.put("materialLotList", materialLotList);
        String filePath = null;
        try {
            CommonFreemarkerTemplate tp = new CommonFreemarkerTemplate("UTF-8");
            tp.setTemplateDirectoryPath(basePath);
            File htmlFile = new File(htmlPath);
            tp.processToFile("materiallot_tempt.html", dataMap, htmlFile);
            log.info("<==== 生成html完成！{}", htmlPath);
            String d = DateUtil.date2String(new Date(), "yyyyMMdd");
            filePath = fileClient.
                    uploadFile(tenantId, "MATERIAL_LOT", "pages" + "/" + d,
                            System.currentTimeMillis() + ".html", FileUtil.File2byte(htmlFile));
            log.info("<==== html url: {}", filePath);
            htmlFile.delete();
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("erorr!", e);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return filePath;
    }

    @Override
    public String print(Long tenantId, List<String> materialLotIds) {
        List<WmsMaterialLotPntVO> materialLotList = wmsMaterialLotMapper.queryPrintData(tenantId, materialLotIds);
        //1.供应商:
        //2.品号:
        //3.品名:
        //4.批号:
        //5.数量:
        //6.条码值:
        //7.客户条码:
        //8.系数：
        //a)	第1格：空
        //b)	第2格：空
        //9.SO:空
        //10.备注：空
        //wmsCommonApiService.commonPrint(tenantId, materialLotList);
        return "Success";
    }

    @Override
    public List<WmsMaterialLotAttrVO> batchSaveExtendAttr(Long tenantId, List<WmsMaterialLotExtendAttrVO> extendAttrList) {
        WmsEventVO event = wmsEventService.createEventOnly(tenantId, HME_PRODUCTION_VERSION_CHANGE);
        return wmsMaterialLotRepository.batchSaveExtendAttr(tenantId, event.getEventId(), extendAttrList);
    }

    @Override
    @ProcessLovValue
    public List<WmsMaterialLotHisExportResultDTO> barCodeHisExport(Long tenantId, WmsMaterialLotHisQryDTO materialLotQryDTO) {
        if (CollectionUtils.isEmpty(materialLotQryDTO.getMaterialLotIds())) {
            return Collections.EMPTY_LIST;
        }
        List<WmsMaterialLotHisResultDTO> wmsMaterialLotHisResultDTOList = wmsMaterialLotMapper.selectBarCodeHis(materialLotQryDTO, tenantId);
        List<WmsMaterialLotHisExportResultDTO> returnList = new ArrayList<>(CollectionUtils.isEmpty(wmsMaterialLotHisResultDTOList) ? 10 : wmsMaterialLotHisResultDTOList.size());
        WmsMaterialLotHisExportResultDTO wmsMaterialLotHisExportResultDTO;
        int maxRow = 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (WmsMaterialLotHisResultDTO wmsMaterialLotHisResultDTO : wmsMaterialLotHisResultDTOList) {
            //实际货位
            if (StringUtils.isNotBlank(wmsMaterialLotHisResultDTO.getActualLocatorCode())) {
                MtModLocator locator = new MtModLocator();
                locator.setTenantId(tenantId);
                locator.setLocatorCategory("INVENTORY");
                locator.setLocatorCode(wmsMaterialLotHisResultDTO.getActualLocatorCode());
                List<MtModLocator> locatorList = mtModLocatorRepository.select(locator);
                if (CollectionUtils.isNotEmpty(locatorList)) {
                    wmsMaterialLotHisResultDTO.setActualLocatorName(locatorList.get(0).getLocatorName());
                }
            }

            //原始条码
            if (StringUtils.isNotBlank(wmsMaterialLotHisResultDTO.getOriginalId())) {
                MtMaterialLot mtMaterialLot = materialLotRepository.selectByPrimaryKey(wmsMaterialLotHisResultDTO.getOriginalId());
                wmsMaterialLotHisResultDTO.setOriginalCode(mtMaterialLot != null ? mtMaterialLot.getMaterialLotCode() : "");
            }

            //转型物料
            if (StringUtils.isNotBlank(wmsMaterialLotHisResultDTO.getPerformanceLevel())) {
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(wmsMaterialLotHisResultDTO.getPerformanceLevel());
                wmsMaterialLotHisResultDTO.setPerformanceLevelName(mtMaterial != null ? mtMaterial.getMaterialCode() : "");
            }

            //在制品标识
            List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WMS.FLAG_YN", tenantId);
            List<LovValueDTO> collect = lovValueDTOS.stream().filter(e -> StringUtils.equals(e.getValue(), wmsMaterialLotHisResultDTO.getMfFlag())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                wmsMaterialLotHisResultDTO.setMfFlagMeaning(collect.get(0).getMeaning());
            }

            wmsMaterialLotHisExportResultDTO = new WmsMaterialLotHisExportResultDTO();
            BeanUtils.copyProperties(wmsMaterialLotHisResultDTO, wmsMaterialLotHisExportResultDTO);
            if (wmsMaterialLotHisExportResultDTO.getInLocatorTime() != null) {
                wmsMaterialLotHisExportResultDTO.setInLocatorTimeStr(sdf.format(wmsMaterialLotHisExportResultDTO.getInLocatorTime()));
            }
            returnList.add(wmsMaterialLotHisExportResultDTO);
            maxRow--;
            if (maxRow < 0) {
                break;
            }
        }
        return returnList;
    }

    @Override
    @ProcessLovValue
    public List<WmsMaterialLotQryExportResultDTO> barCodeQueryExport(Long tenantId, WmsMaterialLotQryDTO materialLotQryDTO) {
        List<WmsMaterialLotQryExportResultDTO> list = wmsMaterialLotMapper.selectBarCodeExportCondition(tenantId, materialLotQryDTO);
        if (CollectionUtils.isNotEmpty(list)) {
            MtExtendVO1 extendVO1 = new MtExtendVO1();
            extendVO1.setTableName("mt_material_lot_attr");
            List<String> materialLotIdList = list.stream().map(WmsMaterialLotQryExportResultDTO::getMaterialLotId).distinct().collect(Collectors.toList());
            List<MtExtendAttrVO1> extendAttrList = new ArrayList<>();
            List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(materialLotIdList)) {
                List<List<String>> splitMaterialLotIdList = CommonUtils.splitSqlList(materialLotIdList, 3000);
                    for (List<String> splitMaterialLotId:splitMaterialLotIdList) {
                        extendVO1.setKeyIdList(splitMaterialLotId);
                    List<MtExtendAttrVO1> subExtendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, extendVO1);
                    if(CollectionUtils.isNotEmpty(subExtendAttrList)){
                        extendAttrList.addAll(subExtendAttrList);
                    }
                    List<HmeMaterialLotLabCode> subHmeMaterialLotLabCodeList = wmsMaterialLotMapper.queryLabCodeByMaterialLotId(tenantId, splitMaterialLotId);
                    if(CollectionUtils.isNotEmpty(subHmeMaterialLotLabCodeList)){
                        hmeMaterialLotLabCodeList.addAll(subHmeMaterialLotLabCodeList);
                    }
                }
            }
            // 查询实验代码
            Map<String, List<HmeMaterialLotLabCode>> labCodeMap = hmeMaterialLotLabCodeList.stream().collect(Collectors.groupingBy(HmeMaterialLotLabCode::getMaterialLotId));

            Map<String, List<MtExtendAttrVO1>> attrList = extendAttrList.stream().collect(Collectors.groupingBy(dto -> dto.getKeyId() + "_" + dto.getAttrName()));

            //在制品标识
            List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WMS.FLAG_YN", tenantId);

            Map<String, List<LovValueDTO>> lovValueDTOSMap = lovValueDTOS.stream().collect(Collectors.groupingBy(dto -> dto.getValue()));

            //实际货位
            List<String> actualLocatorCodeList = list.stream().map(WmsMaterialLotQryExportResultDTO::getActualLocatorCode).distinct().filter(Objects::nonNull).collect(Collectors.toList());

            List<MtModLocator> locatorList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(actualLocatorCodeList)) {
                locatorList = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class)
                        .andWhere(Sqls.custom().andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtModLocator.FIELD_LOCATOR_CATEGORY, "INVENTORY")
                                .andIn(MtModLocator.FIELD_LOCATOR_CODE, actualLocatorCodeList)).build());
            }


            Map<String, List<MtModLocator>> locatorListMap = locatorList.stream().collect(Collectors.groupingBy(dto -> dto.getLocatorCode()));

            //转型物料
            List<String> materialIdList = list.stream().map(WmsMaterialLotQryExportResultDTO::getPerformanceLevel).distinct().filter(Objects::nonNull).collect(Collectors.toList());
            List<MtMaterial> mtMaterialList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(materialIdList)) {
                mtMaterialList = mtMaterialRepository.selectByIds(StringUtils.join(materialIdList, ","));
            }

            Map<String, List<MtMaterial>> mtMaterialListMap = mtMaterialList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialId()));

            //物料版本
            CustomUserDetails userDetails = DetailsHelper.getUserDetails();
            Long userId = userDetails != null ? userDetails.getUserId() : -1L;
            String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);

            List<String> mtMaterialIdList = list.stream().map(WmsMaterialLotQryExportResultDTO::getMaterialId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
            List<String> materialVersionList = list.stream().map(WmsMaterialLotQryExportResultDTO::getMaterialVersion).distinct().filter(Objects::nonNull).collect(Collectors.toList());
            List<MtTagGroupObjectDTO3> dto3List = wmsMaterialLotRepository.batchProductionVersionQuery(tenantId, defaultSiteId, mtMaterialIdList, materialVersionList);

            Map<String, List<MtTagGroupObjectDTO3>> dto3ListMap = dto3List.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialId() + "_" + dto.getProductionVersion()));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (WmsMaterialLotQryExportResultDTO wmsMaterialLotQryResultDTO : list) {
                if(wmsMaterialLotQryResultDTO.getSapAccountFlag()!=null && wmsMaterialLotQryResultDTO.getSapAccountFlag().equals("N")){
                    wmsMaterialLotQryResultDTO.setSapAccountFlag("N");
                }else {
                    wmsMaterialLotQryResultDTO.setSapAccountFlag("Y");
                }
                //实际货位
                if (StringUtils.isNotBlank(wmsMaterialLotQryResultDTO.getActualLocatorCode())) {
                    List<MtModLocator> mtModLocatorList = locatorListMap.get(wmsMaterialLotQryResultDTO.getActualLocatorCode());
                    if (CollectionUtils.isNotEmpty(mtModLocatorList)) {
                        wmsMaterialLotQryResultDTO.setActualLocatorName(mtModLocatorList.get(0).getLocatorName());
                    }
                }

                //转型物料
                if (StringUtils.isNotBlank(wmsMaterialLotQryResultDTO.getPerformanceLevel())) {
                    List<MtMaterial> materialList = mtMaterialListMap.get(wmsMaterialLotQryResultDTO.getPerformanceLevel());
                    if (CollectionUtils.isNotEmpty(materialList)) {
                        wmsMaterialLotQryResultDTO.setPerformanceLevelName(materialList.get(0).getMaterialCode());
                    }
                }

                //物料版本
                if (StringUtils.isNotBlank(wmsMaterialLotQryResultDTO.getMaterialVersion())) {
                    List<MtTagGroupObjectDTO3> mtTagGroupObjectDTO3List = dto3ListMap.get(wmsMaterialLotQryResultDTO.getMaterialId() + "_" + wmsMaterialLotQryResultDTO.getMaterialVersion());
                    if (CollectionUtils.isNotEmpty(mtTagGroupObjectDTO3List)) {
                        wmsMaterialLotQryResultDTO.setMaterialVersionMeaning(mtTagGroupObjectDTO3List.get(0).getDescription());
                    }
                }

                //在制品标识
                List<LovValueDTO> collect = lovValueDTOSMap.get(wmsMaterialLotQryResultDTO.getMfFlag());
                if (CollectionUtils.isNotEmpty(collect)) {
                    wmsMaterialLotQryResultDTO.setMfFlagMeaning(collect.get(0).getMeaning());
                }

                //入库时间
                if (wmsMaterialLotQryResultDTO.getInLocatorTime() != null) {
                    wmsMaterialLotQryResultDTO.setInLocatorTimeStr(sdf.format(wmsMaterialLotQryResultDTO.getInLocatorTime()));
                }
                // 实验代码
                List<HmeMaterialLotLabCode> materialLotLabCodeList = labCodeMap.get(wmsMaterialLotQryResultDTO.getMaterialLotId());
                if (CollectionUtils.isNotEmpty(materialLotLabCodeList)) {
                    List<String> labCodeList = materialLotLabCodeList.stream().map(HmeMaterialLotLabCode::getLabCode).collect(Collectors.toList());
                    wmsMaterialLotQryResultDTO.setLabCode(StringUtils.join(labCodeList.toArray(), ","));
                }
                //获取所有扩展属性
                wmsMaterialLotQryResultDTO.setMsl(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "MSL")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "MSL").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setPrinting(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PRINTING")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PRINTING").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setExpansionCoefficients(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "EXPANSION_COEFFICIENTS")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "EXPANSION_COEFFICIENTS").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setInstructionId(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "INSTRUCTION_ID")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "INSTRUCTION_ID").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setStickerNumber(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "STICKER_NUMBER")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "STICKER_NUMBER").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setPrintTime(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PRINT_TIME")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PRINT_TIME").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setPrintReason(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PRINT_REASON")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PRINT_REASON").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setProductDate(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PRODUCT_DATE")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PRODUCT_DATE").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setOverdueInspectionDate(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "OVERDUE_INSPECTION_DATE")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "OVERDUE_INSPECTION_DATE").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setWoIssueDate(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "WO_ISSUE_DATE")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "WO_ISSUE_DATE").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setColorBin(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "COLOR_BIN")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "COLOR_BIN").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setLightBin(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "LIGHT_BIN")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "LIGHT_BIN").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setVoltageBin(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "VOLTAGE_BIN")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "VOLTAGE_BIN").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setPoLineLocationNum(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PO_LINE_LOCATION_NUM")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PO_LINE_LOCATION_NUM").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setEffectiveDate(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "EFFECTIVE_DATE")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "EFFECTIVE_DATE").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setEnableDate(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "ENABLE_DATE")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "ENABLE_DATE").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setDeadlineDate(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "DEADLINE_DATE")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "DEADLINE_DATE").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setCurrentWck(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "CURRENT_WCK")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "CURRENT_WCK").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setFinalProcessWck(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "FINAL_PROCESS_WCK")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "FINAL_PROCESS_WCK").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setReworkFlag(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "REWORK_FLAG")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "REWORK_FLAG").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setPerformanceLevel(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PERFORMANCE_LEVEL")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "PERFORMANCE_LEVEL").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setDeliveryNum(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "DELIVERY_NUM")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "DELIVERY_NUM").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setDeliveryLineNum(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "DELIVERY_LINE_NUM")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "DELIVERY_LINE_NUM").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setSupplierLot(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "SUPPLIER_LOT")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "SUPPLIER_LOT").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setOutMaterialLotCode(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "OUTER_BOX")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "OUTER_BOX").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setReplacementFlag(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "NC_SUPPLIER_REPLACEMENT")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "NC_SUPPLIER_REPLACEMENT").get(0).getAttrValue() : "");
                wmsMaterialLotQryResultDTO.setDesignedReworkFlag(CollectionUtils.isNotEmpty(attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "DESIGNED_REWORK_FLAG")) ? attrList.get(wmsMaterialLotQryResultDTO.getMaterialLotId() + "_" + "DESIGNED_REWORK_FLAG").get(0).getAttrValue() : "");
                // 如果指定工艺路线返修标识不为Y 则不显示返修工艺路线信息
                if (!WmsConstant.CONSTANT_Y.equals(wmsMaterialLotQryResultDTO.getDesignedReworkFlag())) {
                    wmsMaterialLotQryResultDTO.setReworkRouterId("");
                    wmsMaterialLotQryResultDTO.setReworkRouterName("");
                    wmsMaterialLotQryResultDTO.setReworkRouterDesc("");
                    wmsMaterialLotQryResultDTO.setReworkRouterVersion("");
                }
            }
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtMaterialLotVO13 materialLotUpdate(Long tenantId, MtMaterialLotVO2 dto, String fullUpdate) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "eventId", "【API:materialLotUpdate】"));
        }

        if (dto.getPrimaryUomQty() != null && dto.getTrxPrimaryUomQty() != null) {
            throw new MtException("MT_MATERIAL_LOT_0009",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0009",
                            "MATERIAL_LOT", "primaryUomQty、trxPrimaryUomQty",
                            "【API:materialLotUpdate】"));
        }

        if (dto.getSecondaryUomQty() != null && dto.getTrxSecondaryUomQty() != null) {
            throw new MtException("MT_MATERIAL_LOT_0009",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0009",
                            "MATERIAL_LOT", "secondaryUomQty、trxSecondaryUomQty",
                            "【API:materialLotUpdate】"));
        }

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getOwnerType()) && org.apache.commons.lang3.StringUtils.isEmpty(dto.getOwnerId())) {
            throw new MtException("MT_MATERIAL_LOT_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0014", "MATERIAL_LOT", "ownerType", "ownerId", "【API:materialLotUpdate】"));
        }

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getReservedObjectType()) && org.apache.commons.lang3.StringUtils.isEmpty(dto.getReservedObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0014",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                            "MATERIAL_LOT", "reservedObjectType", "reservedObjectId",
                            "【API:materialLotUpdate】"));
        }

        MtMaterialLot mtMaterialLot = null;
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getMaterialLotId())) {
            mtMaterialLot = new MtMaterialLot();
            mtMaterialLot.setTenantId(tenantId);
            mtMaterialLot.setMaterialLotId(dto.getMaterialLotId());
            mtMaterialLot.setMaterialLotCode(dto.getMaterialLotCode());
            mtMaterialLot = mtMaterialLotMapper.selectOne(mtMaterialLot);
            if (mtMaterialLot == null) {
                throw new MtException("MT_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "MT_MATERIAL_LOT_0011", "MATERIAL_LOT", "【API:materialLotUpdate】"));
            }
        } else if (!dto.isOnlyInsert() //临时增加参数，是否不通过CODE更新 modify by yuchao.wang at 2020.10.20
                && org.apache.commons.lang3.StringUtils.isEmpty(dto.getMaterialLotId()) && org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getMaterialLotCode())) {
            mtMaterialLot = new MtMaterialLot();
            mtMaterialLot.setTenantId(tenantId);
            mtMaterialLot.setMaterialLotCode(dto.getMaterialLotCode());
            mtMaterialLot = mtMaterialLotMapper.selectOne(mtMaterialLot);
        } else {
            if (org.apache.commons.lang3.StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_MATERIAL_LOT_0002",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                "MATERIAL_LOT", "materialLotId、materialLotCode、materialId",
                                "【API:materialLotUpdate】"));
            }

            if (org.apache.commons.lang3.StringUtils.isEmpty(dto.getSiteId())) {
                throw new MtException("MT_MATERIAL_LOT_0002",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                "MATERIAL_LOT", "materialLotId、materialLotCode、siteId",
                                "【API:materialLotUpdate】"));
            }


            if (org.apache.commons.lang3.StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("MT_MATERIAL_LOT_0002",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                "MATERIAL_LOT", "materialLotId、materialLotCode、enableFlag",
                                "【API:materialLotUpdate】"));
            }


            if (org.apache.commons.lang3.StringUtils.isEmpty(dto.getQualityStatus())) {
                throw new MtException("MT_MATERIAL_LOT_0002",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                "MATERIAL_LOT", "materialLotId、materialLotCode、qualityStatus",
                                "【API:materialLotUpdate】"));
            }


            if (org.apache.commons.lang3.StringUtils.isEmpty(dto.getLocatorId())) {
                throw new MtException("MT_MATERIAL_LOT_0002",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                "MATERIAL_LOT", "materialLotId、materialLotCode、locatorId",
                                "【API:materialLotUpdate】"));
            }

            if (org.apache.commons.lang3.StringUtils.isEmpty(dto.getCreateReason())) {
                throw new MtException("MT_MATERIAL_LOT_0002",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                "MATERIAL_LOT", "materialLotId、materialLotCode、createReason",
                                "【API:materialLotUpdate】"));
            }
        }

        MtMaterialVO2 mtMaterialVO2 = null;
        MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
        if (mtMaterialLot != null) {
            //V20201103 modify by penglin.sui 新增行级锁，防止并发
            mtMaterialLot = mtMaterialLotMapper.selectForUpdate(tenantId, mtMaterialLot.getMaterialLotId());
            MtMaterialLot mtMaterialLotOld = new MtMaterialLot();
            BeanUtils.copyProperties(mtMaterialLot, mtMaterialLotOld);
            mtMaterialLot.setSiteId(Strings.isNotBlank(dto.getSiteId()) ? dto.getSiteId() : mtMaterialLot.getSiteId());
            mtMaterialLot.setEnableFlag(dto.getEnableFlag());
            mtMaterialLot.setQualityStatus(Strings.isNotBlank(dto.getQualityStatus()) ? dto.getQualityStatus() : mtMaterialLot.getQualityStatus());
            mtMaterialLot.setMaterialId(Strings.isNotBlank(dto.getMaterialId()) ? dto.getMaterialId() : mtMaterialLot.getMaterialId());

            // 验证传入单位的单位类别是否与物料主单位的单位类别一致
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getMaterialId()) || org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getPrimaryUomId())) {
                mtMaterialVO2 = new MtMaterialVO2();

                if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getMaterialId())) {
                    mtMaterialVO2.setMaterialId(dto.getMaterialId());
                } else {
                    mtMaterialVO2.setMaterialId(mtMaterialLot.getMaterialId());
                }

                if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getPrimaryUomId())) {
                    mtMaterialVO2.setPrimaryUomId(dto.getPrimaryUomId());
                } else {
                    mtMaterialVO2.setPrimaryUomId(mtMaterialLot.getPrimaryUomId());
                }
                this.mtMaterialRepository.materialUomTypeValidate(tenantId, mtMaterialVO2);
            }
            mtMaterialLot.setPrimaryUomId(dto.getPrimaryUomId());

            if (dto.getPrimaryUomQty() != null) {
                mtMaterialLot.setPrimaryUomQty(dto.getPrimaryUomQty());
            } else if (dto.getTrxPrimaryUomQty() != null) {
                mtMaterialLot.setPrimaryUomQty((BigDecimal
                        .valueOf(mtMaterialLotOld.getPrimaryUomQty() == null ? 0.0D
                                : mtMaterialLotOld.getPrimaryUomQty())
                        .add(BigDecimal.valueOf(dto.getTrxPrimaryUomQty()))).doubleValue());
            }

            if (StringUtils.isNotEmpty(dto.getMaterialId()) || StringUtils.isNotEmpty(dto.getSecondaryUomId())) {
                /*
                 * 验证物料是否启用双单位
                 */
                String isDoubleUom = this.mtMaterialRepository.materialDualUomValidate(tenantId,
                        mtMaterialLot.getMaterialId());

                if ("Y".equals(isDoubleUom)) {
                    /*
                     * 验证传入单位的单位类别是否与物料辅助单位的单位类别一致
                     */
                    mtMaterialVO2 = new MtMaterialVO2();
                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getMaterialId())) {
                        mtMaterialVO2.setMaterialId(dto.getMaterialId());
                    } else {
                        mtMaterialVO2.setMaterialId(mtMaterialLot.getMaterialId());
                    }

                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getSecondaryUomId())) {
                        mtMaterialVO2.setSecondaryUomId(dto.getSecondaryUomId());
                    } else if (org.apache.commons.lang3.StringUtils.isNotEmpty(mtMaterialLot.getSecondaryUomId())) {
                        mtMaterialVO2.setSecondaryUomId(mtMaterialLot.getSecondaryUomId());
                    } else {
                        throw new MtException("MT_MATERIAL_LOT_0064",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "MT_MATERIAL_LOT_0064", "MATERIAL_LOT",
                                        "【API:materialLotUpdate】"));
                    }

                    mtMaterialRepository.materialUomTypeValidate(tenantId, mtMaterialVO2);

                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getSecondaryUomId())) {
                        mtMaterialLot.setSecondaryUomId(dto.getSecondaryUomId());
                    }
                } else {
                    mtMaterialLot.setSecondaryUomId("");
                }
            } else {
                mtMaterialLot.setSecondaryUomId(null);
            }

            if (dto.getSecondaryUomQty() != null) {
                mtMaterialLot.setSecondaryUomQty(dto.getSecondaryUomQty());
            } else {
                if (dto.getTrxSecondaryUomQty() != null) {
                    mtMaterialLot.setSecondaryUomQty((BigDecimal
                            .valueOf(mtMaterialLotOld.getSecondaryUomQty() == null ? 0.0D
                                    : mtMaterialLotOld.getSecondaryUomQty())
                            .add(BigDecimal.valueOf(dto.getTrxSecondaryUomQty()))).doubleValue());
                } else {
                    // 二者均没输入的情况，全量模式可以更新为空
                    mtMaterialLot.setSecondaryUomQty(null);
                }
            }
            mtMaterialLot.setLocatorId(dto.getLocatorId());
            mtMaterialLot.setAssemblePointId(dto.getAssemblePointId());

            if (dto.getLoadTime() != null) {
                mtMaterialLot.setLoadTime(dto.getLoadTime());
            } else {
                if (!"Y".equals(mtMaterialLotOld.getEnableFlag()) && "Y".equals(mtMaterialLot.getEnableFlag())) {
                    mtMaterialLot.setLoadTime(new Date());
                } else if ("Y".equals(mtMaterialLotOld.getEnableFlag()) && "Y".equals(mtMaterialLot.getEnableFlag())
                        && mtMaterialLot.getPrimaryUomQty() != null) {
                    if (mtMaterialLotOld.getPrimaryUomQty() == null
                            || BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).compareTo(
                            BigDecimal.valueOf(mtMaterialLotOld.getPrimaryUomQty())) > 0) {
                        mtMaterialLot.setLoadTime(new Date());
                    } else if (BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty())
                            .compareTo(BigDecimal.valueOf(mtMaterialLotOld.getPrimaryUomQty())) == 0
                            && mtMaterialLot.getSecondaryUomQty() != null
                            && (mtMaterialLotOld.getSecondaryUomQty() == null || BigDecimal
                            .valueOf(mtMaterialLot.getSecondaryUomQty())
                            .compareTo(BigDecimal.valueOf(
                                    mtMaterialLotOld.getSecondaryUomQty())) > 0)) {
                        mtMaterialLot.setLoadTime(new Date());
                    }
                }
            }


            if (dto.getUnloadTime() != null) {
                mtMaterialLot.setUnloadTime(dto.getUnloadTime());
            } else {
                if ("Y".equals(mtMaterialLotOld.getEnableFlag()) && !"Y".equals(mtMaterialLot.getEnableFlag())) {
                    mtMaterialLot.setUnloadTime(new Date());
                } else if ("Y".equals(mtMaterialLotOld.getEnableFlag()) && "Y".equals(mtMaterialLot.getEnableFlag())
                        && mtMaterialLot.getPrimaryUomQty() != null) {
                    if (mtMaterialLotOld.getPrimaryUomQty() == null
                            || BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).compareTo(
                            BigDecimal.valueOf(mtMaterialLotOld.getPrimaryUomQty())) < 0) {
                        mtMaterialLot.setUnloadTime(new Date());
                    } else if (BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty())
                            .compareTo(BigDecimal.valueOf(mtMaterialLotOld.getPrimaryUomQty())) == 0
                            && mtMaterialLot.getSecondaryUomQty() != null
                            && (mtMaterialLotOld.getSecondaryUomQty() == null || BigDecimal
                            .valueOf(mtMaterialLot.getSecondaryUomQty())
                            .compareTo(BigDecimal.valueOf(
                                    mtMaterialLotOld.getSecondaryUomQty())) < 0)) {
                        mtMaterialLot.setUnloadTime(new Date());
                    }
                }
            }

            mtMaterialLot.setOwnerType(dto.getOwnerType());
            mtMaterialLot.setOwnerId(dto.getOwnerId());
            mtMaterialLot.setLot(dto.getLot());
            mtMaterialLot.setOvenNumber(dto.getOvenNumber());
            mtMaterialLot.setSupplierId(dto.getSupplierId());
            mtMaterialLot.setSupplierSiteId(dto.getSupplierId());
            mtMaterialLot.setCustomerId(dto.getCustomerId());
            mtMaterialLot.setCustomerSiteId(dto.getCustomerSiteId());
            mtMaterialLot.setReservedFlag(dto.getReservedFlag());
            mtMaterialLot.setReservedObjectType(dto.getReservedObjectType());
            mtMaterialLot.setReservedObjectId(dto.getReservedObjectId());
            mtMaterialLot.setCreateReason(dto.getCreateReason());
            mtMaterialLot.setIdentification(dto.getIdentification());
            mtMaterialLot.setEoId(dto.getEoId());
            mtMaterialLot.setInLocatorTime(dto.getInLocatorTime());
            mtMaterialLot.setFreezeFlag(dto.getFreezeFlag());
            mtMaterialLot.setStocktakeFlag(dto.getStocktakeFlag());
            mtMaterialLot.setInSiteTime(dto.getInSiteTime());
            mtMaterialLot.setCurrentContainerId(dto.getCurrentContainerId());
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getTopContainerId())) {
                mtMaterialLot.setTopContainerId(dto.getTopContainerId());
            } else if (null != dto.getCurrentContainerId()) {
                if ("".equals(dto.getCurrentContainerId())) {
                    mtMaterialLot.setTopContainerId("");
                } else {
                    MtContainer mtContainer = new MtContainer();
                    mtContainer.setTenantId(tenantId);
                    mtContainer.setContainerId(dto.getCurrentContainerId());
                    mtContainer = mtContainerRepository.selectOne(mtContainer);
                    if (null != mtContainer) {
                        mtMaterialLot.setTopContainerId(org.apache.commons.lang3.StringUtils.isEmpty(mtContainer.getTopContainerId())
                                ? dto.getCurrentContainerId()
                                : mtContainer.getTopContainerId());
                    } else {
                        mtMaterialLot.setTopContainerId(dto.getCurrentContainerId());
                    }
                }
            }
            mtMaterialLot.setInstructionDocId(dto.getInstructionDocId());
            mtMaterialLot.setTenantId(tenantId);

            if ("Y".equalsIgnoreCase(fullUpdate)) {
                mtMaterialLot = (MtMaterialLot) ObjectFieldsHelper.setStringFieldsEmpty(mtMaterialLot);
                mtMaterialLotMapper.updateByPrimaryKey(mtMaterialLot);
            } else {
                mtMaterialLotMapper.updateByPrimaryKeySelective(mtMaterialLot);
            }


            mtMaterialLotHis.setTrxPrimaryQty((BigDecimal
                    .valueOf(mtMaterialLot.getPrimaryUomQty() == null ? 0.0D : mtMaterialLot.getPrimaryUomQty())
                    .subtract(BigDecimal.valueOf(mtMaterialLotOld.getPrimaryUomQty() == null ? 0.0D
                            : mtMaterialLotOld.getPrimaryUomQty()))).doubleValue());
            mtMaterialLotHis.setTrxSecondaryQty((BigDecimal
                    .valueOf(mtMaterialLot.getSecondaryUomQty() == null ? 0.0D
                            : mtMaterialLot.getSecondaryUomQty())
                    .subtract(BigDecimal.valueOf(mtMaterialLotOld.getSecondaryUomQty() == null ? 0.0D
                            : mtMaterialLotOld.getSecondaryUomQty()))).doubleValue());
        } else {
            mtMaterialLot = new MtMaterialLot();

            String nextCode = "";
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getMaterialLotCode())) {
                nextCode = dto.getMaterialLotCode();
            } else {
                MtMaterialLotVO26 mtMaterialLotVO26 = new MtMaterialLotVO26();
                Map<String, String> materilaLotPropertyList = new HashMap<>(0);
                // siteCode
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getSiteId())) {
                    MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
                    if (null != mtModSite) {
                        materilaLotPropertyList.put("siteCode", mtModSite.getSiteCode());
                    }
                }

                // currentContainerCode
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getCurrentContainerId())) {
                    MtContainer mtContainer =
                            mtContainerRepository.containerPropertyGet(tenantId, dto.getCurrentContainerId());
                    if (null != mtContainer) {
                        materilaLotPropertyList.put("currentContainerCode", mtContainer.getContainerCode());
                    }
                }


                // materialCode
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getMaterialId())) {
                    MtMaterialVO materialVO = mtMaterialRepository.materialPropertyGet(tenantId, dto.getMaterialId());
                    if (null != materialVO) {
                        materilaLotPropertyList.put("materialCode", materialVO.getMaterialCode());
                    }
                }

                // ownerType
                materilaLotPropertyList.put("ownerType", dto.getOwnerType());
                // businessType
                materilaLotPropertyList.put("businessType", dto.getBusinessType());
                // lot
                materilaLotPropertyList.put("lot", dto.getLot());

                mtMaterialLotVO26.setSiteId(dto.getSiteId());
                mtMaterialLotVO26.setOutsideNum(dto.getOutsideNum());
                mtMaterialLotVO26.setMaterilaLotPropertyList(materilaLotPropertyList);
                mtMaterialLotVO26.setIncomingValueList(dto.getIncomingValueList());
                MtNumrangeVO5 mtNumrangeVO5 = mtMaterialLotRepository.materialLotNextCodeGet(tenantId, mtMaterialLotVO26);
                if (mtNumrangeVO5 != null) {
                    nextCode = mtNumrangeVO5.getNumber();
                }
            }

            mtMaterialLot.setMaterialLotCode(nextCode);
            mtMaterialLot.setSiteId(dto.getSiteId());
            mtMaterialLot.setEnableFlag(dto.getEnableFlag());
            mtMaterialLot.setQualityStatus(dto.getQualityStatus());
            mtMaterialLot.setMaterialId(dto.getMaterialId());

            if (org.apache.commons.lang3.StringUtils.isEmpty(dto.getPrimaryUomId())) {
                MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, dto.getMaterialId());
                if (mtMaterialVO1 == null) {
                    throw new MtException("MT_MATERIAL_LOT_0016", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "MT_MATERIAL_LOT_0016", "MATERIAL_LOT", "【API:materialLotUpdate】"));
                }
                mtMaterialLot.setPrimaryUomId(mtMaterialVO1.getPrimaryUomId());
            } else {
                mtMaterialVO2 = new MtMaterialVO2();
                mtMaterialVO2.setPrimaryUomId(dto.getPrimaryUomId());
                mtMaterialVO2.setMaterialId(dto.getMaterialId());
                mtMaterialRepository.materialUomTypeValidate(tenantId, mtMaterialVO2);
                mtMaterialLot.setPrimaryUomId(dto.getPrimaryUomId());
            }


            if (dto.getPrimaryUomQty() != null) {
                mtMaterialLot.setPrimaryUomQty(dto.getPrimaryUomQty());
            } else if (dto.getTrxPrimaryUomQty() != null) {
                mtMaterialLot.setPrimaryUomQty(dto.getTrxPrimaryUomQty());
            } else {
                mtMaterialLot.setPrimaryUomQty(Double.valueOf(0.0D));
            }

            if (org.apache.commons.lang3.StringUtils.isEmpty(dto.getSecondaryUomId())) {
                MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, dto.getMaterialId());
                if (null == mtMaterialVO1) {
                    throw new MtException("MT_MATERIAL_LOT_0016", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "MT_MATERIAL_LOT_0016", "MATERIAL_LOT", "【API:materialLotUpdate】"));
                }
                mtMaterialLot.setSecondaryUomId(mtMaterialVO1.getSecondaryUomId());
            } else {
                mtMaterialVO2 = new MtMaterialVO2();
                mtMaterialVO2.setSecondaryUomId(dto.getSecondaryUomId());
                mtMaterialVO2.setMaterialId(dto.getMaterialId());
                mtMaterialRepository.materialUomTypeValidate(tenantId, mtMaterialVO2);
                mtMaterialLot.setSecondaryUomId(dto.getSecondaryUomId());
            }

            if (dto.getSecondaryUomQty() != null) {
                mtMaterialLot.setSecondaryUomQty(dto.getSecondaryUomQty());
            } else if (dto.getTrxSecondaryUomQty() != null) {
                mtMaterialLot.setSecondaryUomQty(dto.getTrxSecondaryUomQty());
            }

            mtMaterialLot.setLocatorId(dto.getLocatorId());
            mtMaterialLot.setAssemblePointId(dto.getAssemblePointId());

            if (dto.getLoadTime() != null) {
                mtMaterialLot.setLoadTime(dto.getLoadTime());
            } else if ("Y".equals(dto.getEnableFlag()) && (dto.getPrimaryUomQty() != null
                    && BigDecimal.valueOf(dto.getPrimaryUomQty()).compareTo(BigDecimal.ZERO) != 0)
                    || (dto.getSecondaryUomQty() != null && BigDecimal.valueOf(dto.getSecondaryUomQty())
                    .compareTo(BigDecimal.ZERO) != 0)) {
                mtMaterialLot.setLoadTime(new Date());
            }

            if (dto.getUnloadTime() != null) {
                mtMaterialLot.setUnloadTime(dto.getUnloadTime());
            }

            mtMaterialLot.setOwnerType(dto.getOwnerType());
            mtMaterialLot.setOwnerId(dto.getOwnerId());

            if (null == dto.getLot()) {
                String nextLot = mtMaterialLotRepository.materialLotNextLotGet(tenantId);
                mtMaterialLot.setLot(nextLot);
            } else {
                mtMaterialLot.setLot(dto.getLot());
            }

            mtMaterialLot.setOvenNumber(dto.getOvenNumber());
            mtMaterialLot.setSupplierId(dto.getSupplierId());
            mtMaterialLot.setSupplierSiteId(dto.getSupplierSiteId());
            mtMaterialLot.setCustomerId(dto.getCustomerId());
            mtMaterialLot.setCustomerSiteId(mtMaterialLot.getCustomerSiteId());
            mtMaterialLot.setReservedFlag(dto.getReservedFlag());
            mtMaterialLot.setReservedObjectType(dto.getReservedObjectType());
            mtMaterialLot.setReservedObjectId(dto.getReservedObjectId());
            mtMaterialLot.setCreateReason(dto.getCreateReason());
            // add 2019-11-13
            mtMaterialLot.setCustomerSiteId(dto.getCustomerSiteId());

            if (org.apache.commons.lang3.StringUtils.isEmpty(dto.getIdentification())) {
                mtMaterialLot.setIdentification(mtMaterialLot.getMaterialLotCode());
            } else {
                mtMaterialLot.setIdentification(dto.getIdentification());

                // update by chuang.yang 2019-09-17
                // 添加唯一性校验
                MtMaterialLot verify = new MtMaterialLot();
                verify.setTenantId(tenantId);
                verify.setIdentification(dto.getIdentification());
                List<MtMaterialLot> mtMaterialLots = mtMaterialLotMapper.select(verify);
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtMaterialLots)) {
                    throw new MtException("MT_MATERIAL_LOT_0075",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0075",
                                    "MATERIAL_LOT", "MT_MATERIAL_LOT", "IDENTIFICATION",
                                    "【API:materialLotUpdate】"));
                }
            }

            mtMaterialLot.setEoId(dto.getEoId());
            mtMaterialLot.setInLocatorTime(dto.getInLocatorTime());
            mtMaterialLot.setFreezeFlag(dto.getFreezeFlag());
            mtMaterialLot.setStocktakeFlag(dto.getStocktakeFlag());
            mtMaterialLot.setInSiteTime(dto.getInSiteTime());
            mtMaterialLot.setCurrentContainerId(dto.getCurrentContainerId());
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(dto.getTopContainerId())) {
                mtMaterialLot.setTopContainerId(dto.getTopContainerId());
            } else if (null != dto.getCurrentContainerId()) {
                if ("".equals(dto.getCurrentContainerId())) {
                    mtMaterialLot.setTopContainerId("");
                } else {
                    MtContainer mtContainer = new MtContainer();
                    mtContainer.setTenantId(tenantId);
                    mtContainer.setContainerId(dto.getCurrentContainerId());
                    mtContainer = mtContainerRepository.selectOne(mtContainer);
                    if (null != mtContainer) {
                        mtMaterialLot.setTopContainerId(org.apache.commons.lang3.StringUtils.isEmpty(mtContainer.getTopContainerId())
                                ? dto.getCurrentContainerId()
                                : mtContainer.getTopContainerId());
                    } else {
                        mtMaterialLot.setTopContainerId(dto.getCurrentContainerId());
                    }
                }
            }
            mtMaterialLot.setInstructionDocId(dto.getInstructionDocId());
            mtMaterialLot.setTenantId(tenantId);
            mtMaterialLotRepository.insertSelective(mtMaterialLot);

            mtMaterialLotHis.setTrxPrimaryQty(mtMaterialLot.getPrimaryUomQty());
            mtMaterialLotHis.setTrxSecondaryQty(mtMaterialLot.getSecondaryUomQty());
        }

        // 记录历史
        // 查询对象
        MtMaterialLot lot = new MtMaterialLot();
        lot.setTenantId(tenantId);
        lot.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        mtMaterialLot = mtMaterialLotMapper.selectOne(lot);
        BeanUtils.copyProperties(mtMaterialLot, mtMaterialLotHis);
        mtMaterialLotHis.setEventId(dto.getEventId());
        mtMaterialLotHis.setTenantId(tenantId);
        mtMaterialLotHisRepository.insertSelective(mtMaterialLotHis);
        // 主表记录最新历史
        mtMaterialLot.setLatestHisId(mtMaterialLotHis.getMaterialLotHisId());
        mtMaterialLotMapper.updateByPrimaryKeySelective(mtMaterialLot);

        MtMaterialLotVO13 result = new MtMaterialLotVO13();
        result.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        result.setMaterialLotHisId(mtMaterialLotHis.getMaterialLotHisId());

        return result;
    }

    @Override
    public void updateMaterialLotStatus(Long tenantId, String eventId, String materialLotId, String status) {
        // 更新条码
        MtMaterialLotVO2 materialLotUpdate = new MtMaterialLotVO2();
        materialLotUpdate.setMaterialLotId(materialLotId);
        materialLotUpdate.setEventId(eventId);
        mtMaterialLotRepository.materialLotUpdate(tenantId, materialLotUpdate, WmsConstant.CONSTANT_N);

        // 更新拓展字段
        MtExtendVO10 extendVo = new MtExtendVO10();
        extendVo.setKeyId(materialLotId);
        extendVo.setEventId(eventId);
        MtExtendVO5 extendAttr = new MtExtendVO5();
        extendAttr.setAttrName("STATUS");
        extendAttr.setAttrValue(status);
        List<MtExtendVO5> extendList = new ArrayList<>(1);
        extendList.add(extendAttr);
        extendVo.setAttrs(extendList);
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, extendVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public String materialLotNew(Long tenantId, WmsMaterialLotAddDTO dto) {
        //更新条码 物料版本（暂时）
        if (StringUtils.isNotBlank(dto.getMaterialLotId())) {
            if (StringUtils.isNotBlank(dto.getMaterialVersion())) {
                MtMaterialLot mtMaterialLot = materialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
                //限制状态
                MtExtendSettings reworkAttr = new MtExtendSettings();
                reworkAttr.setAttrName("STATUS");
                List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                        "mt_material_lot_attr", "MATERIAL_LOT_ID", dto.getMaterialLotId(), Collections.singletonList(reworkAttr));
                List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.MATERIALLOT_STATUS_ENABLED", tenantId);
                List<String> statusList = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                    if (!statusList.contains(mtExtendAttrVOList.get(0).getAttrValue())) {
                        throw new MtException("HME_NC_0015", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "HME_NC_0015", "HME", mtMaterialLot.getMaterialLotCode()));
                    }
                }

                //限制货位
                List<LovValueDTO> locatorList = lovAdapter.queryLovValue("HME.MATERIALLOT_STATUS_DISABLED", tenantId);
                if (CollectionUtils.isNotEmpty(locatorList)) {
                    MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
                    if (mtModLocator != null) {
                        List<String> locatorCodeList = locatorList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                        if (!locatorCodeList.contains(mtModLocator.getLocatorCode())) {
                            throw new MtException("HME_NC_0016", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "HME_NC_0016", "HME", mtMaterialLot.getMaterialLotCode()));
                        }
                    }
                }

                // 创建数据采集实绩
                MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                eventCreateVO.setEventTypeCode("PRODUCTION_VERSION_CHANGE");
                // 创建事件
                String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                List<MtExtendVO5> attr = new ArrayList<>();
                MtExtendVO5 vo5 = new MtExtendVO5();
                vo5.setAttrName("MATERIAL_VERSION");
                vo5.setAttrValue(dto.getMaterialVersion());
                attr.add(vo5);
                mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", dto.getMaterialLotId(), eventId, attr);
            }
            return dto.getMaterialLotId();
        }

        if (StringUtils.isNotBlank(dto.getMaterialLotCode())) {
            MtMaterialLot mtMaterialLot = new MtMaterialLot();
            mtMaterialLot.setTenantId(tenantId);
            mtMaterialLot.setMaterialLotCode(dto.getMaterialLotCode());
            mtMaterialLot = mtMaterialLotMapper.selectOne(mtMaterialLot);
            if (mtMaterialLot != null) {
                throw new MtException("HME_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "HME_MATERIAL_LOT_0001", "HME", dto.getMaterialLotCode()));
            }
        }

        String eventId = wmsCommonServiceComponent.generateEvent(tenantId, WmsConstant.EVENT_REQUEST_CODE_BARCODE);
        if (StringUtils.isEmpty(eventId)) {
            log.error("<==== materialLotCreate event is null");
            throw new MtException("MT_GENERAL_0061", mtErrorMessageRepository.getErrorMessageWithModule(
                    tenantId, "MT_GENERAL_0061", "GENERAL"));
        }

        Integer createCount = dto.getCreateQty();
        createCount = createCount == null || createCount == 0 ? 1 : createCount;
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < createCount; i++) {
            //生产批次号 add by sanfeng.zhang for yiwei 2020/10/06
            if (StringUtils.isBlank(dto.getLot())) {
                MtNumrangeVO2 mtNumrange = new MtNumrangeVO2();
                mtNumrange.setObjectCode("RECEIPT_BATCH");
                MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrange);
                dto.setLot(mtNumrangeVO5 != null ? mtNumrangeVO5.getNumber() : null);
            } else {
                //效验输入的批次号 10位加数字
                if (!CommonUtils.isNumeric(dto.getLot()) && dto.getLot().length() != 10) {
                    throw new MtException("HME_MATERIAL_LOT_004", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "HME_MATERIAL_LOT_004", "HME"));
                }
            }
            // ids.add(createMaterialLot(tenantId, eventId, dto, userOrganization.getOrganizationId()));
            ids.add(createMaterialLotNew(tenantId, eventId, dto));
        }
        // 批量生成条码
        return Arrays.toString(ids.toArray());
    }

    private String createMaterialLotNew(Long tenantId, String eventId, WmsMaterialLotAddDTO dto) {
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        BeanUtils.copyProperties(dto, mtMaterialLotVO2);

        mtMaterialLotVO2.setEventId(eventId);
        //mtMaterialLotVO2.setMaterialLotCode(number);
        //update by sanfeng.zhang 2020/9/9 默认为N
        mtMaterialLotVO2.setEnableFlag(WmsConstant.CONSTANT_N);
        mtMaterialLotVO2.setLocatorId(dto.getLocatorId());
        mtMaterialLotVO2.setQualityStatus(dto.getQualityStatus());
        mtMaterialLotVO2.setSiteId(dto.getSiteId());
        mtMaterialLotVO2.setTenantId(tenantId);
        mtMaterialLotVO2.setPrimaryUomQty(dto.getPrimaryUomQty().doubleValue());
        mtMaterialLotVO2.setCid(Long.valueOf(customSequence.getNextKey("mt_material_lot_cid_s")));
        mtMaterialLotVO2.setSupplierId(dto.getSupplierId());
        if (StringUtils.isNotBlank(dto.getMaterialLotCode())) {
            mtMaterialLotVO2.setOutsideNum(dto.getMaterialLotCode());
        }

        /**
         * add by liyuan.lv, 2020-03-25
         */
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dto.getCreateReason())) {
            MtGenType mtGenType = mtGenTypeRepository.selectByPrimaryKey(dto.getCreateReason());
            mtMaterialLotVO2.setCreateReason(mtGenType != null ? mtGenType.getTypeCode() : "");
            mtMaterialLotVO2.setBusinessType(mtGenType.getTypeCode());
        }
        if(StringUtils.isBlank(dto.getMaterialLotCode())) {
            List<MtNumrangeVO11> mtNumrangeVO11List = new ArrayList<>();
            mtNumrangeVO11List = this.createRuleCode(tenantId,dto);
            MtNumrangeVO9 mtNumrangeVO9 = new MtNumrangeVO9();
            mtNumrangeVO9.setObjectCode("SN_NUM");
            mtNumrangeVO9.setIncomingValueList(mtNumrangeVO11List);
            mtNumrangeVO9.setObjectNumFlag(HmeConstants.ConstantValue.YES);
            mtNumrangeVO9.setNumQty(1L);
            MtNumrangeVO8 mtNumrangeVO8 = mtNumrangeRepository.numrangeBatchGenerate(tenantId, mtNumrangeVO9);
            List<String> numberList = mtNumrangeVO8.getNumberList();
            mtMaterialLotVO2.setMaterialLotCode(numberList.get(0));
        }

        MtMaterialLotVO13 mtMaterialLotVO13 = materialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, WmsConstant.CONSTANT_N);

        String materialLotId = mtMaterialLotVO13.getMaterialLotId();
        log.info("<==== materialLotCreate materialLotId:{}", materialLotId);

        List<MtExtendVO5> attr = new ArrayList<>(10);
        MtExtendVO5 tmpVo = null;
        if (!StringUtils.isEmpty(dto.getProductDate())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("PRODUCT_DATE");
            tmpVo.setAttrValue(dto.getProductDate());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getSoNum())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("SO_NUM");
            tmpVo.setAttrValue(dto.getSoNum());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getSupplierLot())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("SUPPLIER_LOT");
            tmpVo.setAttrValue(dto.getSupplierLot());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getSoLineNum())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("SO_LINE_NUM");
            tmpVo.setAttrValue(dto.getSoLineNum());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getStatus())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName(HmeConstants.ExtendAttr.STATUS);
            tmpVo.setAttrValue(dto.getStatus());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getColorBin())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("COLOR_BIN");
            tmpVo.setAttrValue(dto.getColorBin());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getLightBin())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("LIGHT_BIN");
            tmpVo.setAttrValue(dto.getLightBin());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getVoltageBin())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("VOLTAGE_BIN");
            tmpVo.setAttrValue(dto.getVoltageBin());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getPerformanceLevel())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("PERFORMANCE_LEVEL");
            tmpVo.setAttrValue(dto.getPerformanceLevel());
            attr.add(tmpVo);
        }
        if (!StringUtils.isEmpty(dto.getMaterialVersion())) {
            tmpVo = new MtExtendVO5();
            tmpVo.setAttrName("MATERIAL_VERSION");
            tmpVo.setAttrValue(dto.getMaterialVersion());
            attr.add(tmpVo);
        }
        if (attr.size() > 0) {
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", materialLotId,
                    eventId, attr);
            log.debug("<==== materialLotCreate attrPropertyUpdate success ");
        }
        return materialLotId;
    }

    private List<MtNumrangeVO11> createRuleCode(Long tenantId, WmsMaterialLotAddDTO dto) {
        List<MtNumrangeVO11> mtNumrangeVO11List = new ArrayList<>();
        List<String> siteIdList = new ArrayList<>();
        siteIdList.add(dto.getSiteId());

        //获取工厂简码
        Map<String, String> siteMap = new HashMap<>();
        List<HmeSnBindEoVO2> siteCodeList = this.modSiteAttrValueGet(tenantId, siteIdList);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(siteCodeList)) {
            siteCodeList.forEach(item -> siteMap.put(item.getSiteId(), item.getSiteCode()));
        }
        ///根据产线ID查询产线简码
        String prodLineShortName = hmeCosPatchPdaMapper.getProdLineShortName(tenantId, dto.getProdLineId());
        if (StringUtils.isEmpty(prodLineShortName)) {
            throw new MtException("HME_SN_BIND_EO_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SN_BIND_EO_002", "HME"));
        }
        //获取物料的扩展字段18
        String code = hmeSnBindEoMapper.select(tenantId,dto.getMaterialId(),dto.getSiteId());
        //当前年 后两位
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2);
        //月：1-9，A-Z 获取的月比真实少一个月
        String month = this.handleMonth(calendar.get(Calendar.MONTH) + 1);

        StringBuffer codeStr = new StringBuffer();
        //站点
        if (StringUtils.isBlank(dto.getSiteId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "siteId", "【API:createMaterialLotCode】"));
        }
        String siteAttrValue = "";
        if(siteMap.containsKey(dto.getSiteId())){
            siteAttrValue = siteMap.get(dto.getSiteId());
        }
        if (StringUtils.isBlank(siteAttrValue)) {
            throw new MtException("HME_SN_BIND_EO_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SN_BIND_EO_001", "HME", ""));
        }
        //产线
        if (StringUtils.isBlank(dto.getProdLineId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "productionLineId", "【API:createMaterialLotCode】"));
        }
        String proLineAttrValue = prodLineShortName;

        codeStr.append(siteAttrValue)
                .append(proLineAttrValue)
                .append(code)
                .append(year)
                .append(month);
        String ruleCode = codeStr.toString();

        MtNumrangeVO11 vo11 = new MtNumrangeVO11();
        vo11.setSequence(1L);
        List<String> valList = new ArrayList<>();
        valList.add(ruleCode);
        vo11.setIncomingValue(valList);
        mtNumrangeVO11List.add(vo11);
        return mtNumrangeVO11List;
    }

    public List<HmeSnBindEoVO2> modSiteAttrValueGet(Long tenantId, List<String> siteIdList) {
        //参数检验
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(siteIdList)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "siteId", "【API:modSiteAttrValueGet】"));
        }
        return hmeSnBindEoMapper.modSiteAttrValueGet(tenantId, siteIdList);
    }

    /**
     * 处理月 1-9，A-Z
     *
     * @param month
     * @return
     * @date 2020/7/6
     * @author sanfeng.zhang
     */
    public String handleMonth(int month) {
        String monthStr = "";
        int monthMax = 10;
        if (month < monthMax) {
            return String.valueOf(month);
        }
        switch (month) {
            case 10:
                monthStr = "A";
                break;
            case 11:
                monthStr = "B";
                break;
            case 12:
                monthStr = "C";
                break;
            default:
                break;
        }
        return monthStr;
    }
}
