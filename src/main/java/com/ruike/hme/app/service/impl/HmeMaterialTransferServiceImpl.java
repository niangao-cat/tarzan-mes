package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeMaterialTransferService;
import com.ruike.hme.domain.entity.HmeEoJobLotMaterial;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.repository.HmeMaterialLotLoadRepository;
import com.ruike.hme.domain.repository.HmeMaterialTransferRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeMaterialTransferMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.wms.app.service.impl.WmsCommonServiceComponent;
import com.ruike.wms.domain.repository.WmsMaterialLotSplitRepository;
import com.ruike.wms.infra.util.StringCommonUtils;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.NO;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * ?????????????????????????????????
 *
 * @author jiangling.zheng@hand-china.com 2020-05-09 10:01
 */
@Service
public class HmeMaterialTransferServiceImpl implements HmeMaterialTransferService {

    private final HmeMaterialTransferRepository hmeMaterialTransferRepository;

    private final MtErrorMessageRepository mtErrorMessageRepository;

    private final HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    private final HmeMaterialTransferMapper hmeMaterialTransferMapper;

    private final WmsCommonServiceComponent commonServiceComponent;

    private final MtMaterialLotRepository materialLotRepository;

    private final MtContainerRepository containerRepository;

    private final MtMaterialLotRepository mtMaterialLotRepository;

    private final MtExtendSettingsRepository extendSettingsRepository;

    private final WmsMaterialLotSplitRepository wmsMaterialLotSplitRepository;

    private final HmeEoJobSnRepository hmeEoJobSnRepository;

    private final MtModWorkcellRepository mtModWorkcellRepository;

    private final LovAdapter lovAdapter;

    private final MtGenTypeRepository mtGenTypeRepository;

    private final HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;

    private final MtModLocatorRepository mtModLocatorRepository;

    private final MtUserOrganizationRepository mtUserOrganizationRepository;

    public HmeMaterialTransferServiceImpl(HmeMaterialTransferRepository hmeMaterialTransferRepository,
                                          MtErrorMessageRepository mtErrorMessageRepository,
                                          HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper,
                                          HmeMaterialTransferMapper hmeMaterialTransferMapper,
                                          WmsCommonServiceComponent commonServiceComponent, MtMaterialLotRepository materialLotRepository,
                                          MtContainerRepository containerRepository, MtMaterialLotRepository mtMaterialLotRepository,
                                          MtExtendSettingsRepository extendSettingsRepository, WmsMaterialLotSplitRepository wmsMaterialLotSplitRepository,
                                          HmeEoJobSnRepository hmeEoJobSnRepository, MtModWorkcellRepository mtModWorkcellRepository, LovAdapter lovAdapter,
                                          HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository, MtGenTypeRepository mtGenTypeRepository, MtModLocatorRepository mtModLocatorRepository, MtUserOrganizationRepository mtUserOrganizationRepository) {
        this.hmeMaterialTransferRepository = hmeMaterialTransferRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.hmeWorkOrderManagementMapper = hmeWorkOrderManagementMapper;
        this.hmeMaterialTransferMapper = hmeMaterialTransferMapper;
        this.commonServiceComponent = commonServiceComponent;
        this.materialLotRepository = materialLotRepository;
        this.containerRepository = containerRepository;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.extendSettingsRepository = extendSettingsRepository;
        this.wmsMaterialLotSplitRepository = wmsMaterialLotSplitRepository;
        this.hmeEoJobSnRepository = hmeEoJobSnRepository;
        this.mtModWorkcellRepository = mtModWorkcellRepository;
        this.lovAdapter = lovAdapter;
        this.mtGenTypeRepository = mtGenTypeRepository;
        this.hmeMaterialLotLoadRepository = hmeMaterialLotLoadRepository;
        this.mtModLocatorRepository = mtModLocatorRepository;
        this.mtUserOrganizationRepository = mtUserOrganizationRepository;
    }


    private static final String[] IGNORE_TABLE_FIELDS = new String[]{MtMaterialLot.FIELD_IDENTIFICATION,
            AuditDomain.FIELD_OBJECT_VERSION_NUMBER, AuditDomain.FIELD_LAST_UPDATED_BY, AuditDomain.FIELD_CREATED_BY,
            AuditDomain.FIELD_CREATION_DATE, AuditDomain.FIELD_LAST_UPDATE_DATE};
    private final String ATTR_TABLE = "mt_material_lot_attr";

    private static final List<String> EQUALS_FIELDS =
                    Arrays.asList("STATUS", "MATERIAL_VERSION", "SO_NUM", "SO_LINE_NUM");

    @Override
    public HmeMaterialTransferDTO2 transferMaterialLotGet(Long tenantId, List<HmeMaterialTransferDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new MtException("MT_MATERIAL_TFR_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_TFR_0001", HmeConstants.ConstantValue.HME, "dtoList"));
        }
        // ??????????????????????????????
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        // ????????????????????????(??????)90
        HmeMaterialTransferDTO dtoMax =
                        dtoList.stream().max(Comparator.comparing(HmeMaterialTransferDTO::getInputTimes))
                                        .orElse(new HmeMaterialTransferDTO());
        // ?????????????????????(?????????????????????????????????)
        HmeMaterialTransferDTO dtoMin =
                        dtoList.stream().min(Comparator.comparing(HmeMaterialTransferDTO::getInputTimes))
                                        .orElse(new HmeMaterialTransferDTO());
        String materialLotCode = dtoMax.getMaterialLotCode();
        String firstMaterialLotCode = dtoMin.getMaterialLotCode();
        HmeMaterialTransferDTO2 returnDto = new HmeMaterialTransferDTO2();
        HmeMaterialTransferDTO7 materialLotDto = new HmeMaterialTransferDTO7();
        if (StringUtils.isBlank(dtoMax.getMaterialLotId())) {
            MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, materialLotCode);
            // ???????????????
            if (materialLot == null) {
                throw new MtException("MT_MATERIAL_TFR_0002", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_TFR_0002", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            //2020-11-27 add by chaonan.hu for yiwei.zhou ?????????????????????????????????
            if (YES.equals(materialLot.getStocktakeFlag())) {
                throw new MtException("WMS_COST_CENTER_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0034", "WMS", materialLotCode));
            }
            //2021-09-07 add by sanfeng.zhang for peng.zhao ???????????????????????????
            if (YES.equals(materialLot.getFreezeFlag())) {
                throw new MtException("MT_MATERIAL_TFR_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_TFR_0026", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            //2021-09-07 add by sanfeng.zhang for peng.zhao ???????????????????????????
            if (!YES.equals(materialLot.getEnableFlag())) {
                throw new MtException("MT_MATERIAL_TFR_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_TFR_0025", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            //2021-09-07 add by sanfeng.zhang for peng.zhao ?????????0 ??????????????????
            if (BigDecimal.valueOf(materialLot.getPrimaryUomQty()).compareTo(BigDecimal.ZERO) == 0) {
                throw new MtException("MT_MATERIAL_TFR_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_TFR_0028", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            //2021-09-07 add by sanfeng.zhang for peng.zhao SN???????????????1 ??????????????????
            String materialType = hmeEoJobSnRepository.getMaterialType(tenantId, materialLot.getSiteId(), materialLot.getMaterialId());
            if (HmeConstants.ConstantValue.SN.equals(materialType) && BigDecimal.valueOf(materialLot.getPrimaryUomQty()).compareTo(BigDecimal.ONE) == 0) {
                throw new MtException("MT_MATERIAL_TFR_0027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_TFR_0027", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            //2020-11-30 add by chaonan.hu for yiwei.zhou ????????????????????????????????????
            this.materialLotStatusVerify(tenantId, materialLot);

            //2020-12-01 add by chaonan.hu for zhenyong.ban ??????????????????????????????????????????
            wmsMaterialLotSplitRepository.materialLotVfVerify(tenantId, materialLot);

            //???????????????
            wmsMaterialLotSplitRepository.materialLotMfFlagVerify(tenantId, materialLot);

            //????????????????????????
            this.materialLotLoadInVerify(tenantId, materialLot);

            //??????????????????
            this.materialLotSiteInVerify(tenantId, materialLot);

            //????????????
            List<LovValueDTO> warehouseList = lovAdapter.queryLovValue("HME.NON_TRANSFERABLE_WAREHOUSE", tenantId);
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(materialLot.getLocatorId());
            if(mtModLocator != null) {
                MtModLocator warehouse = mtModLocatorRepository.selectByPrimaryKey(mtModLocator.getParentLocatorId());
                if (warehouse == null) {
                    throw new MtException("HME_SPLIT_RECORD_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SPLIT_RECORD_0019", "HME", mtModLocator.getLocatorCode()));
                }
                Optional<LovValueDTO> warehouseOpt = warehouseList.stream().filter(dto -> StringUtils.equals(dto.getValue(), warehouse.getLocatorCode())).findFirst();
                if (warehouseOpt.isPresent()) {
                    throw new MtException("MT_MATERIAL_TFR_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_TFR_0022", "HME", warehouse.getLocatorCode()));
                }
                //20210329 add by sanfeng.zhang for zhenyong.ban ????????????????????????????????????????????????
                Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
                List<String> privilegeIdList = hmeMaterialTransferMapper.queryDocPrivilegeByWarehouse(tenantId, userId, warehouse.getLocatorId());
                if (CollectionUtils.isEmpty(privilegeIdList)) {
                    throw new MtException("WMS_COST_CENTER_0067", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0067", "WMS", warehouse.getLocatorCode()));
                }
            }

            //??????????????????
            List<LovValueDTO> locatorTypeList = lovAdapter.queryLovValue("HME.TRANSFERABLE_LOCATOR_TYPE", tenantId);
            String locatorType = mtModLocator != null ? mtModLocator.getLocatorType() : "";
            Optional<LovValueDTO> locatorTypeOpt = locatorTypeList.stream().filter(dto -> StringUtils.equals(dto.getValue(), locatorType)).findFirst();
            if(!locatorTypeOpt.isPresent()){
                MtGenType genType = mtGenTypeRepository.getGenType(tenantId, "MODELING", "LOCATOR_TYPE", locatorType);
                throw new MtException("MT_MATERIAL_TFR_0023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_TFR_0023", "HME", genType != null ? genType.getDescription() : ""));

            }

            // ???????????????
            if (!YES.equals(materialLot.getEnableFlag())) {
                throw new MtException("MT_MATERIAL_TFR_0003", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "MT_MATERIAL_TFR_0003", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            // ??????????????????????????????????????????????????????
            if (!materialLot.getSiteId().equals(siteId)) {
                throw new MtException("MT_MATERIAL_TFR_0004", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_TFR_0004", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            // ??????????????????
            if (YES.equals(materialLot.getFreezeFlag())) {
                throw new MtException("MT_MATERIAL_TFR_0005", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "MT_MATERIAL_TFR_0005", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            // ????????????????????????
            if (YES.equals(materialLot.getStocktakeFlag())) {
                throw new MtException("MT_MATERIAL_TFR_0006", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "MT_MATERIAL_TFR_0006", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            // ??????????????????????????????????????????
            String itemProdType = hmeMaterialTransferMapper.materialSiteAttrGet(tenantId, materialLot.getSiteId(), materialLot.getMaterialId());
            if (StringUtils.equals(itemProdType, HmeConstants.AttrName.TIME)) {
                throw new MtException("MT_MATERIAL_TFR_0016", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "MT_MATERIAL_TFR_0016", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            // ?????????????????????????????????????????? ???????????????
            this.materialLotBindStationVerify(tenantId, materialLot);
            // ????????????????????????????????????????????????????????????????????????????????????
            if (dtoList.size() > 1) {
                MtMaterialLot firstMaterialLot =
                                hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, firstMaterialLotCode);
                validateAttr(tenantId, materialLot, firstMaterialLot);
                // ????????????????????????
                if (!materialLot.getMaterialId().equals(firstMaterialLot.getMaterialId())) {
                    throw new MtException("MT_MATERIAL_TFR_0007", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_TFR_0007", HmeConstants.ConstantValue.HME, materialLotCode));
                }
                // ????????????ID????????????
                if (!Optional.ofNullable(materialLot.getLocatorId()).orElse("")
                                .equals(Optional.ofNullable(firstMaterialLot.getLocatorId()).orElse(""))) {
                    throw new MtException("MT_MATERIAL_TFR_0008", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_TFR_0008", HmeConstants.ConstantValue.HME, materialLotCode));
                }
                // ????????????????????????
                if (!Optional.ofNullable(materialLot.getLot()).orElse("")
                                .equals(Optional.ofNullable(firstMaterialLot.getLot()).orElse(""))) {
                    throw new MtException("MT_MATERIAL_TFR_0009", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_TFR_0009", HmeConstants.ConstantValue.HME, materialLotCode));
                }
                // ?????????????????????????????????
                if (!Optional.ofNullable(materialLot.getOwnerType()).orElse("")
                                .equals(Optional.ofNullable(firstMaterialLot.getOwnerType()).orElse(""))) {
                    throw new MtException("MT_MATERIAL_TFR_0010", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_TFR_0010", HmeConstants.ConstantValue.HME, materialLotCode));
                }
                // ???????????????ID????????????
                if (!Optional.ofNullable(materialLot.getOwnerId()).orElse("")
                                .equals(Optional.ofNullable(firstMaterialLot.getOwnerId()).orElse(""))) {
                    throw new MtException("MT_MATERIAL_TFR_0011", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_TFR_0011", HmeConstants.ConstantValue.HME, materialLotCode));
                }
                // ???????????????ID????????????
                if (!Optional.ofNullable(materialLot.getSupplierId()).orElse("")
                                .equals(Optional.ofNullable(firstMaterialLot.getSupplierId()).orElse(""))) {
                    throw new MtException("MT_MATERIAL_TFR_0012", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_TFR_0012", HmeConstants.ConstantValue.HME, materialLotCode));
                }
            }
            materialLotDto = hmeMaterialTransferMapper.materialLotGet(tenantId, materialLotCode);
            dtoList.forEach(dto -> {
                if (dtoMax.getInputTimes().equals(dto.getInputTimes())) {
                    dto.setMaterialLotId(materialLot.getMaterialLotId());
                    dto.setQuantity(materialLot.getPrimaryUomQty());
                    dto.setTransferQuantity(materialLot.getPrimaryUomQty());
                }
            });
        }
        // ????????????
        Double totalQty = dtoList.stream().map(HmeMaterialTransferDTO::getQuantity).filter(Objects::nonNull)
                        .collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).summaryStatistics()
                        .getSum();

        // ???????????????
        Double totalTransferQty = dtoList.stream().map(HmeMaterialTransferDTO::getTransferQuantity)
                        .filter(Objects::nonNull).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue)
                        .summaryStatistics().getSum();
        materialLotDto.setTotalQty(totalQty);
        materialLotDto.setTotalTransferQty(totalTransferQty);
        BeanUtils.copyProperties(materialLotDto, returnDto);
        returnDto.setDtoList(dtoList);
        return returnDto;
    }

    @Override
    public HmeMaterialTransferDTO4 targetMaterialLotGet(Long tenantId, List<HmeMaterialTransferDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new MtException("MT_MATERIAL_TFR_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_TFR_0001", HmeConstants.ConstantValue.HME, "dtoList"));
        }
        // ?????????????????????(?????????????????????????????????)
        HmeMaterialTransferDTO firstDto =
                        dtoList.stream().min(Comparator.comparing(HmeMaterialTransferDTO::getInputTimes))
                                        .orElse(new HmeMaterialTransferDTO());
        String firstMaterialLotCode = firstDto.getMaterialLotCode();
        HmeMaterialTransferDTO7 materialLotDto =
                        hmeMaterialTransferMapper.materialLotGet(tenantId, firstMaterialLotCode);
        HmeMaterialTransferDTO4 dto = new HmeMaterialTransferDTO4();
        BeanUtils.copyProperties(materialLotDto, dto);
        return dto;
    }

    /**
     * ???????????????
     *
     * @param tenantId ??????
     * @param materialLot ?????????
     * @param siteId ??????ID
     * @param targetMaterialLotCode ??????????????????
     * @param firstMaterialLot ????????????
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/20 10:23:57
     */
    private void validateBeforeTransfer(Long tenantId, MtMaterialLot materialLot, String siteId,
                    String targetMaterialLotCode, MtMaterialLot firstMaterialLot) {
        // 20210719 modify by sanfeng.zhang for wenxin.zhang ?????? ??????????????????????????????????????????????????????
        // ???????????????
        if (!Objects.isNull(materialLot)) {
            if (YES.equals(materialLot.getEnableFlag())) {
                // ??????????????????????????????????????????????????????
                if (!materialLot.getSiteId().equals(siteId)) {
                    throw new MtException("MT_MATERIAL_TFR_0004",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0004",
                                    HmeConstants.ConstantValue.HME, targetMaterialLotCode));
                }
                //???????????????
                wmsMaterialLotSplitRepository.materialLotMfFlagVerify(tenantId, materialLot);
                // ??????????????????
                if (YES.equals(materialLot.getFreezeFlag())) {
                    throw new MtException("MT_MATERIAL_TFR_0005",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0005",
                                    HmeConstants.ConstantValue.HME, targetMaterialLotCode));
                }
                // ????????????????????????
                if (YES.equals(materialLot.getStocktakeFlag())) {
                    throw new MtException("MT_MATERIAL_TFR_0006",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0006",
                                    HmeConstants.ConstantValue.HME, targetMaterialLotCode));
                }
                // ????????????ID????????????
                if (!Optional.ofNullable(materialLot.getLocatorId()).orElse("")
                        .equals(Optional.ofNullable(firstMaterialLot.getLocatorId()).orElse(""))) {
                    throw new MtException("MT_MATERIAL_TFR_0008",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0008",
                                    HmeConstants.ConstantValue.HME, targetMaterialLotCode));
                }
                // ?????????????????????????????????
                if (!Optional.ofNullable(materialLot.getOwnerType()).orElse("")
                        .equals(Optional.ofNullable(firstMaterialLot.getOwnerType()).orElse(""))) {
                    throw new MtException("MT_MATERIAL_TFR_0010",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0010",
                                    HmeConstants.ConstantValue.HME, targetMaterialLotCode));
                }
                // ???????????????ID????????????
                if (!Optional.ofNullable(materialLot.getOwnerId()).orElse("")
                        .equals(Optional.ofNullable(firstMaterialLot.getOwnerId()).orElse(""))) {
                    throw new MtException("MT_MATERIAL_TFR_0011",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0011",
                                    HmeConstants.ConstantValue.HME, targetMaterialLotCode));
                }
                // ??????????????????????????????
                if (!StringCommonUtils.equalsIgnoreBlank(materialLot.getQualityStatus(),
                        firstMaterialLot.getQualityStatus())) {
                    throw new MtException("MT_MATERIAL_TFR_0015",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0015",
                                    HmeConstants.ConstantValue.HME, targetMaterialLotCode));
                }
            }
            // ????????????????????????
            if (!materialLot.getMaterialId().equals(firstMaterialLot.getMaterialId())) {
                throw new MtException("MT_MATERIAL_TFR_0007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0007",
                                                HmeConstants.ConstantValue.HME, targetMaterialLotCode));
            }
            // ????????????????????????
            if (!Optional.ofNullable(materialLot.getLot()).orElse("")
                            .equals(Optional.ofNullable(firstMaterialLot.getLot()).orElse(""))) {
                throw new MtException("MT_MATERIAL_TFR_0009",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0009",
                                                HmeConstants.ConstantValue.HME, targetMaterialLotCode));
            }
            // ???????????????ID????????????
            if (!Optional.ofNullable(materialLot.getSupplierId()).orElse("")
                            .equals(Optional.ofNullable(firstMaterialLot.getSupplierId()).orElse(""))) {
                throw new MtException("MT_MATERIAL_TFR_0012",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0012",
                                                HmeConstants.ConstantValue.HME, targetMaterialLotCode));
            }
            // ??????????????????????????????
            MtExtendVO sourceExtendAttrQuery = new MtExtendVO();
            sourceExtendAttrQuery.setKeyId(firstMaterialLot.getMaterialLotId());
            sourceExtendAttrQuery.setTableName(ATTR_TABLE);
            List<MtExtendAttrVO> sourceExtendAttrList =
                            extendSettingsRepository.attrPropertyQuery(tenantId, sourceExtendAttrQuery);
            Map<String, String> sourceMap = sourceExtendAttrList.stream()
                            .collect(Collectors.toMap(MtExtendAttrVO::getAttrName, MtExtendAttrVO::getAttrValue));

            // ??????????????????????????????
            MtExtendVO extendAttrQuery = new MtExtendVO();
            extendAttrQuery.setKeyId(materialLot.getMaterialLotId());
            extendAttrQuery.setTableName(ATTR_TABLE);
            List<MtExtendAttrVO> extendAttrList = extendSettingsRepository.attrPropertyQuery(tenantId, extendAttrQuery);
            // ???????????????SO_NUM, SO_LINE_NUM
            if (YES.equals(materialLot.getEnableFlag())) {
                Map<String, String> extendMap = extendAttrList.stream()
                        .filter(src -> StringCommonUtils.contains(src.getAttrName(), "STATUS", "MATERIAL_VERSION",
                                "SO_NUM", "SO_LINE_NUM"))
                        .collect(Collectors.toMap(MtExtendAttrVO::getAttrName, MtExtendAttrVO::getAttrValue));
                EQUALS_FIELDS.forEach(attr -> {
                    if (!StringCommonUtils.equalsIgnoreBlank(sourceMap.get(attr), extendMap.get(attr))) {
                        throw new MtException("MT_MATERIAL_TFR_0014",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0014",
                                        HmeConstants.ConstantValue.HME, targetMaterialLotCode, attr));
                    }
                });
            } else {
                List<String> attrNameList = Arrays.asList("SO_NUM", "SO_LINE_NUM");
                Map<String, String> attrMap = extendAttrList.stream().filter(src -> attrNameList.contains(src.getAttrName())).collect(Collectors.toMap(MtExtendAttrVO::getAttrName, MtExtendAttrVO::getAttrValue));
                attrNameList.forEach(attr -> {
                    if (!StringCommonUtils.equalsIgnoreBlank(sourceMap.get(attr), attrMap.get(attr))) {
                        throw new MtException("MT_MATERIAL_TFR_0014",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0014",
                                        HmeConstants.ConstantValue.HME, targetMaterialLotCode, attr));
                    }
                });
            }

        }
    }

    /**
     * ??????????????????
     *
     * @param tenantId
     * @param materialLot
     * @param firstMaterialLot
     * @author jiangling.zheng@hand-china.com 2020/10/27 11:41
     * @return void
     */
    private void validateAttr(Long tenantId, MtMaterialLot materialLot, MtMaterialLot firstMaterialLot){
        // ???????????????????????????
        MtExtendVO firstAttrQuery = new MtExtendVO();
        firstAttrQuery.setKeyId(firstMaterialLot.getMaterialLotId());
        firstAttrQuery.setTableName(ATTR_TABLE);
        List<MtExtendAttrVO> firstAttrList = extendSettingsRepository.attrPropertyQuery(tenantId, firstAttrQuery);
        // ??????????????????????????????
        MtExtendVO currAttrQuery = new MtExtendVO();
        currAttrQuery.setKeyId(materialLot.getMaterialLotId());
        currAttrQuery.setTableName(ATTR_TABLE);
        List<MtExtendAttrVO> currAttrList = extendSettingsRepository.attrPropertyQuery(tenantId, currAttrQuery);
        Optional<MtExtendAttrVO> firstSoNum = firstAttrList.stream().filter(c ->
                StringUtils.equals(c.getAttrName(), "SO_NUM")).findFirst();
        Optional<MtExtendAttrVO> currSoNum = currAttrList.stream().filter(c ->
                StringUtils.equals(c.getAttrName(), "SO_NUM")).findFirst();
        //?????????????????????
        boolean isDiffSoNum = (firstSoNum.isPresent() && !currSoNum.isPresent()) ||
                (!firstSoNum.isPresent() && currSoNum.isPresent()) || (firstSoNum.isPresent() && currSoNum.isPresent() &&
                !StringUtils.equals(firstSoNum.get().getAttrValue(), currSoNum.get().getAttrValue()));
        if (isDiffSoNum) {
            throw new MtException("MT_MATERIAL_TFR_0019", mtErrorMessageRepository.getErrorMessageWithModule(
                    tenantId, "MT_MATERIAL_TFR_0019", HmeConstants.ConstantValue.HME));
        }
        Optional<MtExtendAttrVO> firstSoLineNum = firstAttrList.stream().filter(c ->
                StringUtils.equals(c.getAttrName(), "SO_LINE_NUM")).findFirst();
        Optional<MtExtendAttrVO> currSoLineNum = currAttrList.stream().filter(c ->
                StringUtils.equals(c.getAttrName(), "SO_LINE_NUM")).findFirst();
        //????????????????????????
        boolean isDiffSoLineNum = (firstSoLineNum.isPresent() && !currSoLineNum.isPresent()) ||
                (!firstSoLineNum.isPresent() && currSoLineNum.isPresent()) || (firstSoLineNum.isPresent() && currSoLineNum.isPresent() &&
                !StringUtils.equals(firstSoLineNum.get().getAttrValue(), currSoLineNum.get().getAttrValue()));
        if (isDiffSoLineNum) {
            throw new MtException("MT_MATERIAL_TFR_0020", mtErrorMessageRepository.getErrorMessageWithModule(
                    tenantId, "MT_MATERIAL_TFR_0020", HmeConstants.ConstantValue.HME));
        }
    }

    /**
     * ??????????????????
     *
     * @param tenantId ??????
     * @param sourceMaterialLotId ???????????????ID
     * @param materialLotId ???????????????ID
     * @param eventId ??????ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/20 10:32:56
     */
    private void updateMaterialLotExtendAttr(Long tenantId, String sourceMaterialLotId, String materialLotId,String supplierLot,
                    String eventId) {
        // ???????????????
        MtExtendVO extendAttrQuery = new MtExtendVO();
        extendAttrQuery.setKeyId(sourceMaterialLotId);
        extendAttrQuery.setTableName(ATTR_TABLE);
        List<MtExtendAttrVO> extendAttrList = extendSettingsRepository.attrPropertyQuery(tenantId, extendAttrQuery);

        // ??????????????????
        List<MtExtendVO5> targetExtendAttrList = new ArrayList<>();
        MtExtendVO5 newAttr = null;
        Boolean mfFlag = false;
        for (MtExtendAttrVO extendAttrVO : extendAttrList) {
            if (StringUtils.equals(extendAttrVO.getAttrName(), "ORIGINAL_ID") || StringUtils.equals(extendAttrVO.getAttrName(), "SUPPLIER_LOT")) {
                continue;
            }

            if(StringUtils.equals(extendAttrVO.getAttrName(), "MF_FLAG")){
                mfFlag = true;
            }
            newAttr = new MtExtendVO5();
            newAttr.setAttrName(extendAttrVO.getAttrName());
            newAttr.setAttrValue(extendAttrVO.getAttrValue());
            targetExtendAttrList.add(newAttr);
        }

        if(!mfFlag){
            newAttr = new MtExtendVO5();
            newAttr.setAttrName("MF_FLAG");
            newAttr.setAttrValue("");
            targetExtendAttrList.add(newAttr);
        }

        newAttr = new MtExtendVO5();
        newAttr.setAttrName("ORIGINAL_ID");
        newAttr.setAttrValue(sourceMaterialLotId);
        targetExtendAttrList.add(newAttr);

        MtExtendVO5 supplierAttr = new MtExtendVO5();
        supplierAttr.setAttrName("SUPPLIER_LOT");
        supplierAttr.setAttrValue(supplierLot);
        targetExtendAttrList.add(supplierAttr);
        extendSettingsRepository.attrPropertyUpdate(tenantId, ATTR_TABLE, materialLotId, eventId, targetExtendAttrList);

    }

    /*@Transactional(rollbackFor = Exception.class)
    @Override
    public HmeMaterialTransferDTO2 targetMaterialLotConfirmForUi(Long tenantId, HmeMaterialTransferDTO3 targetDto) {
        if (CollectionUtils.isEmpty(targetDto.getDtoList())) {
            throw new MtException("MT_MATERIAL_TFR_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_TFR_0001", HmeConstants.ConstantValue.HME, "dtoList"));
        }
        if (targetDto.getTargetQty() == null) {
            throw new MtException("MT_MATERIAL_TFR_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_TFR_0001", HmeConstants.ConstantValue.HME, "targetQty"));
        }
        // ??????????????????????????????
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        List<HmeMaterialTransferDTO> dtoList = targetDto.getDtoList();
        String targetMaterialLotCode = targetDto.getTargetMaterialLotCode();

        // ???????????????
        Double totalTransferQty = dtoList.stream().map(HmeMaterialTransferDTO::getTransferQuantity)
                        .filter(Objects::nonNull).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue)
                        .summaryStatistics().getSum();
        if (targetDto.getTargetQty().compareTo(totalTransferQty) > 0) {
            throw new MtException("MT_MATERIAL_TFR_0013",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0013",
                                            HmeConstants.ConstantValue.HME, targetDto.getTargetQty().toString(),
                                            totalTransferQty.toString()));
        }
        // ????????????????????????
        dtoList.sort(Comparator.comparing(HmeMaterialTransferDTO::getInputTimes));
        // ?????????????????????(?????????????????????????????????)
        HmeMaterialTransferDTO firstDto =
                        dtoList.stream().min(Comparator.comparing(HmeMaterialTransferDTO::getInputTimes))
                                        .orElse(new HmeMaterialTransferDTO());
        String firstMaterialLotCode = firstDto.getMaterialLotCode();
        MtMaterialLot firstMaterialLot =
                        hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, firstMaterialLotCode);
        // ????????????????????????
        MtMaterialLot materialLot = new MtMaterialLot();
        if (StringUtils.isNotBlank(targetMaterialLotCode)) {
            materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, targetMaterialLotCode);
            this.validateBeforeTransfer(tenantId, materialLot, siteId, targetMaterialLotCode, firstMaterialLot);
        }
        // ??????????????????
        String requestId = commonServiceComponent.generateEventRequest(tenantId,
                        HmeConstants.EventType.MATERIAL_TRANSFER_REQUISTION);
        // ??????????????????????????????
        String outEventId = commonServiceComponent.generateEvent(tenantId, HmeConstants.EventType.MATERIAL_TRANSFER_OUT,
                        requestId);
        // ??????????????????
        Double targetQty = targetDto.getTargetQty();
        // ????????????????????????
        for (HmeMaterialTransferDTO dto : dtoList) {
            //????????????????????????
            MtMaterialLot mtMaterialLot = materialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
            if(mtMaterialLot.getPrimaryUomQty().compareTo(dto.getQuantity()) != 0){
                throw new MtException("MT_MATERIAL_TFR_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_TFR_0018", HmeConstants.ConstantValue.HME, dto.getMaterialLotCode()));
            }
            Double transferQty;
            if (targetQty >= dto.getTransferQuantity()) {
                transferQty = dto.getTransferQuantity();
                if(targetQty != null && dto.getTransferQuantity() != null){
                    targetQty = BigDecimal.valueOf(targetQty).subtract(BigDecimal.valueOf(dto.getTransferQuantity())).doubleValue();
                }
            } else {
                transferQty = targetQty;
                targetQty = 0.0;
            }
            // ???????????????????????????
            Double primaryUomQty =  BigDecimal.valueOf(dto.getQuantity()).subtract(BigDecimal.valueOf(transferQty)).doubleValue();
            MtMaterialLotVO2 mtMaterialLotVo2 = new MtMaterialLotVO2();
            mtMaterialLotVo2.setEventId(outEventId);
            mtMaterialLotVo2.setMaterialLotId(dto.getMaterialLotId());
            mtMaterialLotVo2.setPrimaryUomQty(primaryUomQty);
            mtMaterialLotVo2.setEnableFlag(primaryUomQty.compareTo(0.0) == 0 ? HmeConstants.ConstantValue.NO
                            : HmeConstants.ConstantValue.YES);
            materialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVo2, HmeConstants.ConstantValue.NO);
            MtMaterialLot transferMaterialLot =
                            hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotCode());
            // ????????????
            if (HmeConstants.ConstantValue.NO.equals(transferMaterialLot.getEnableFlag())
                            && StringUtils.isNotBlank(transferMaterialLot.getCurrentContainerId())) {
                MtContainerVO25 cnt = new MtContainerVO25();
                cnt.setContainerId(transferMaterialLot.getCurrentContainerId());
                cnt.setLoadObjectId(transferMaterialLot.getMaterialLotId());
                cnt.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                cnt.setEventRequestId(requestId);
                containerRepository.containerUnload(tenantId, cnt);
            }
            Double finalTransferQty = transferQty;
            dtoList.forEach(returnDto -> {
                if (returnDto.getInputTimes().equals(dto.getInputTimes())) {
                    returnDto.setQuantity(transferMaterialLot.getPrimaryUomQty());
                    returnDto.setTransferQuantity(BigDecimal.valueOf(dto.getTransferQuantity()).subtract(BigDecimal.valueOf(finalTransferQty)).doubleValue());
                }
            });
            if (targetQty.compareTo(0d) == 0) {
                break;
            }
        }
        // ??????????????????????????????
        String inEventId = commonServiceComponent.generateEvent(tenantId, HmeConstants.EventType.MATERIAL_TRANSFER_IN,
                        requestId);
        MtMaterialLotVO2 targetMaterialLotVo = new MtMaterialLotVO2();
        if (StringUtils.isBlank(targetMaterialLotCode)) {
            // ???????????????????????????
            copyPropertiesIgnoreNullAndTableFields(firstMaterialLot, targetMaterialLotVo);
            targetMaterialLotVo.setEventId(inEventId);
            targetMaterialLotVo.setMaterialLotId(null);
            targetMaterialLotVo.setMaterialLotCode(null);
            List<MtGenType> mtGenTypeList = mtGenTypeRepository.selectByCondition(Condition.builder(MtGenType.class)
                    .andWhere(Sqls.custom().andEqualTo(MtGenType.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtGenType.FIELD_MODULE, "MATERIAL_LOT")
                    .andEqualTo(MtGenType.FIELD_TYPE_GROUP, "CREATE_REASON")
                    .andEqualTo(MtGenType.FIELD_TYPE_CODE, "SPLIT")).build());
            if(CollectionUtils.isNotEmpty(mtGenTypeList)){
                targetMaterialLotVo.setCreateReason(mtGenTypeList.get(0).getGenTypeId());
            }
            targetMaterialLotVo.setEnableFlag(HmeConstants.ConstantValue.YES);
            targetMaterialLotVo.setPrimaryUomQty(targetDto.getTargetQty());
        } else {
            if (Objects.isNull(materialLot)) {
                copyPropertiesIgnoreNullAndTableFields(firstMaterialLot, targetMaterialLotVo);
                targetMaterialLotVo.setEventId(inEventId);
                targetMaterialLotVo.setMaterialLotId(null);
                targetMaterialLotVo.setMaterialLotCode(targetMaterialLotCode);
                List<MtGenType> mtGenTypeList = mtGenTypeRepository.selectByCondition(Condition.builder(MtGenType.class)
                        .andWhere(Sqls.custom().andEqualTo(MtGenType.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtGenType.FIELD_MODULE, "MATERIAL_LOT")
                                .andEqualTo(MtGenType.FIELD_TYPE_GROUP, "CREATE_REASON")
                                .andEqualTo(MtGenType.FIELD_TYPE_CODE, "SPLIT")).build());
                if(CollectionUtils.isNotEmpty(mtGenTypeList)){
                    targetMaterialLotVo.setCreateReason(mtGenTypeList.get(0).getGenTypeId());
                }
                targetMaterialLotVo.setEnableFlag(HmeConstants.ConstantValue.YES);
                targetMaterialLotVo.setPrimaryUomQty(targetDto.getTargetQty());
            } else if (HmeConstants.ConstantValue.NO.equals(materialLot.getEnableFlag())) {
                //???????????? ????????????0 2020/10/15 add by sanfeng.zhang for banzhenyong
                materialLot.setPrimaryUomQty(0D);
                materialLotRepository.updateByPrimaryKeySelective(materialLot);
                //??????????????????
                this.materialLotSiteInVerify(tenantId, materialLot);
                copyPropertiesIgnoreNullAndTableFields(firstMaterialLot, targetMaterialLotVo);
                targetMaterialLotVo.setEventId(inEventId);
                targetMaterialLotVo.setMaterialLotId(materialLot.getMaterialLotId());
                targetMaterialLotVo.setMaterialLotCode(materialLot.getMaterialLotCode());
                targetMaterialLotVo.setEnableFlag(HmeConstants.ConstantValue.YES);
                targetMaterialLotVo.setPrimaryUomQty(targetDto.getTargetQty());
            } else if (HmeConstants.ConstantValue.YES.equals(materialLot.getEnableFlag())) {
                //??????????????????
                this.materialLotSiteInVerify(tenantId, materialLot);
                targetMaterialLotVo.setEventId(inEventId);
                targetMaterialLotVo.setMaterialLotId(materialLot.getMaterialLotId());
                targetMaterialLotVo.setMaterialLotCode(materialLot.getMaterialLotCode());
                targetMaterialLotVo.setPrimaryUomQty(BigDecimal.valueOf(materialLot.getPrimaryUomQty()).add(BigDecimal.valueOf(targetDto.getTargetQty())).doubleValue());
            }
        }

        // ?????????????????????
        MtMaterialLotVO13 vo = materialLotRepository.materialLotUpdate(tenantId, targetMaterialLotVo,
                        HmeConstants.ConstantValue.NO);
        // ??????????????????????????????
        if (!(!Objects.isNull(materialLot) && HmeConstants.ConstantValue.YES.equals(materialLot.getEnableFlag()))) {
            this.updateMaterialLotExtendAttr(tenantId, firstMaterialLot.getMaterialLotId(), vo.getMaterialLotId(), targetDto.getSupplierLot(),
                            inEventId);
        }else {
            //???????????????????????????????????????????????????
            List<MtExtendVO5> mtExtendList = new ArrayList<>();
            MtExtendVO5 statusAttr = new MtExtendVO5();
            statusAttr.setAttrName(HmeConstants.ExtendAttr.SUPPLIER_LOT);
            statusAttr.setAttrValue(targetDto.getSupplierLot());
            mtExtendList.add(statusAttr);
            MtExtendVO5 originalAttr = new MtExtendVO5();
            originalAttr.setAttrName(HmeConstants.ExtendAttr.ORIGINAL_ID);
            originalAttr.setAttrValue(firstMaterialLot.getMaterialLotId());
            mtExtendList.add(originalAttr);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {
                {
                    setKeyId(vo.getMaterialLotId());
                    setEventId(inEventId);
                    setAttrs(mtExtendList);
                }
            });
        }

        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, vo.getMaterialLotId());
        // ???????????????????????????
        HmeMaterialTransferDTO2 materialLotDto =
                        hmeMaterialTransferMapper.materialLotGet(tenantId, firstMaterialLotCode);
        // ????????????
        Double totalQty = dtoList.stream().map(HmeMaterialTransferDTO::getQuantity).filter(Objects::nonNull)
                        .collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).summaryStatistics()
                        .getSum();

        // ???????????????
        Double totalTfrQty = dtoList.stream().map(HmeMaterialTransferDTO::getTransferQuantity).filter(Objects::nonNull)
                        .collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).summaryStatistics()
                        .getSum();
        materialLotDto.setDtoList(dtoList);
        materialLotDto.setTotalQty(totalQty);
        materialLotDto.setTotalTransferQty(totalTfrQty);
        materialLotDto.setTargetMaterialLotId(mtMaterialLot.getMaterialLotId());
        materialLotDto.setTargetMaterialLotCode(mtMaterialLot.getMaterialLotCode());
        materialLotDto.setSupplierLot(targetDto.getSupplierLot());
        return materialLotDto;
    }*/

    @Transactional(rollbackFor = Exception.class)
    @Override
    public HmeMaterialTransferDTO2 targetMaterialLotConfirmForUi(Long tenantId, HmeMaterialTransferDTO3 targetDto) {
        if (CollectionUtils.isEmpty(targetDto.getDtoList())) {
            throw new MtException("MT_MATERIAL_TFR_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0001", HmeConstants.ConstantValue.HME, "dtoList"));
        }
        if (CollectionUtils.isEmpty(targetDto.getTargetDtoList())) {
            throw new MtException("MT_MATERIAL_TFR_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0001", HmeConstants.ConstantValue.HME, "targetDtoList"));
        }
        // ??????????????????????????????
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        // ???????????????
        List<HmeMaterialTransferDTO> dtoList = targetDto.getDtoList();
        // ????????????
        List<HmeMaterialTransferDTO6> targetDtoList = targetDto.getTargetDtoList();
        // ????????????????????????
        long count = targetDtoList.stream().filter(c -> c.getTargetQty() == null).count();
        if (count != 0 ) {
            throw new MtException("MT_MATERIAL_TFR_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0001", HmeConstants.ConstantValue.HME, "targetQty"));
        }
        // ???????????????
        Double totalTransferQty = dtoList.stream().map(HmeMaterialTransferDTO::getTransferQuantity)
                .filter(Objects::nonNull).mapToDouble(Double::doubleValue)
                .summaryStatistics().getSum();
        // ????????????
        Double totalTargetQty = targetDtoList.stream().map(HmeMaterialTransferDTO6::getTargetQty).mapToDouble(Double::doubleValue)
                .summaryStatistics().getSum();
        if (totalTargetQty.compareTo(totalTransferQty) > 0) {
            throw new MtException("MT_MATERIAL_TFR_0013",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0013",
                            HmeConstants.ConstantValue.HME, totalTargetQty.toString(),
                            totalTransferQty.toString()));
        }
        // ????????????????????????
        dtoList.sort(Comparator.comparing(HmeMaterialTransferDTO::getInputTimes));
        // ?????????????????????(?????????????????????????????????)
        HmeMaterialTransferDTO firstDto =
                dtoList.stream().min(Comparator.comparing(HmeMaterialTransferDTO::getInputTimes))
                        .orElse(new HmeMaterialTransferDTO());
        String firstMaterialLotCode = firstDto.getMaterialLotCode();
        MtMaterialLot firstMaterialLot =
                hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, firstMaterialLotCode);
        // ??????????????????
        String requestId = commonServiceComponent.generateEventRequest(tenantId,
                HmeConstants.EventType.MATERIAL_TRANSFER_REQUISTION);

        // ??????????????????
        List<MtMaterialLotVO20> updateMaterialLotList = new ArrayList<>();
        // ??????????????????????????????
        String outEventId = commonServiceComponent.generateEvent(tenantId, HmeConstants.EventType.MATERIAL_TRANSFER_OUT,
                requestId);
        // ??????????????????????????????
        String inEventId = commonServiceComponent.generateEvent(tenantId, HmeConstants.EventType.MATERIAL_TRANSFER_IN,
                requestId);
        // ??????????????????(?????? ??????????????????)
        List<MtMaterialLotVO20> updateTargetMaterialLotList = new ArrayList<>();
        // ??????????????????????????????(?????? ??????????????????)
        List<MtCommonExtendVO7> updateTargetAttrList = new ArrayList<>();
        for (HmeMaterialTransferDTO6 targetMaterialLotDto : targetDtoList) {
            String targetMaterialLotCode = targetMaterialLotDto.getTargetMaterialLotCode();
            // ????????????????????????
            MtMaterialLot materialLot = null;
            if (StringUtils.isNotBlank(targetMaterialLotCode)) {
                materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, targetMaterialLotCode);
                this.validateBeforeTransfer(tenantId, materialLot, siteId, targetMaterialLotCode, firstMaterialLot);
            }
            // ??????????????????
            Double targetQty = targetMaterialLotDto.getTargetQty();
            // ????????????????????????
            for (HmeMaterialTransferDTO dto : dtoList) {
                //????????????????????????
                if(dto.getDeductionQty() == null) {
                    dto.setDeductionQty(BigDecimal.ZERO);
                }
                MtMaterialLot mtMaterialLot = materialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
                if(mtMaterialLot.getPrimaryUomQty().compareTo(BigDecimal.valueOf(dto.getQuantity()).add(dto.getDeductionQty()).doubleValue()) != 0){
                    throw new MtException("MT_MATERIAL_TFR_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_TFR_0018", HmeConstants.ConstantValue.HME, dto.getMaterialLotCode()));
                }
                dto.setCurrentContainerId(mtMaterialLot.getCurrentContainerId());
                Double transferQty;
                if (targetQty >= dto.getTransferQuantity()) {
                    transferQty = dto.getTransferQuantity();
                    if(targetQty != null && dto.getTransferQuantity() != null){
                        targetQty = BigDecimal.valueOf(targetQty).subtract(BigDecimal.valueOf(dto.getTransferQuantity())).doubleValue();
                    }
                } else {
                    transferQty = targetQty;
                    targetQty = 0.0;
                }
                // ???????????????????????????
                Double primaryUomQty =  BigDecimal.valueOf(dto.getQuantity()).subtract(BigDecimal.valueOf(transferQty)).doubleValue();
                Double finalTransferQty = transferQty;
                dtoList.forEach(returnDto -> {
                    if (returnDto.getInputTimes().equals(dto.getInputTimes())) {
                        returnDto.setQuantity(primaryUomQty);
                        returnDto.setTransferQuantity(BigDecimal.valueOf(dto.getTransferQuantity()).subtract(BigDecimal.valueOf(finalTransferQty)).doubleValue());
                        returnDto.setDeductionQty(returnDto.getDeductionQty().add(BigDecimal.valueOf(finalTransferQty)));
                    }
                });
                if (targetQty.compareTo(0d) == 0) {
                    break;
                }
            }

            MtMaterialLotVO2 targetMaterialLotVo = new MtMaterialLotVO2();
            if (StringUtils.isBlank(targetMaterialLotCode)) {
                // ???????????????????????????
                copyPropertiesIgnoreNullAndTableFields(firstMaterialLot, targetMaterialLotVo);
                targetMaterialLotVo.setEventId(inEventId);
                targetMaterialLotVo.setMaterialLotId(null);
                targetMaterialLotVo.setMaterialLotCode(null);
                //2020-11-01 add by sanfeng.zhang for zhenyong ????????????
                targetMaterialLotVo.setCurrentContainerId(null);
                targetMaterialLotVo.setTopContainerId(null);
                targetMaterialLotVo.setUnloadTime(null);
                targetMaterialLotVo.setLoadTime(new Date());
                List<MtGenType> mtGenTypeList = mtGenTypeRepository.selectByCondition(Condition.builder(MtGenType.class)
                        .andWhere(Sqls.custom().andEqualTo(MtGenType.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtGenType.FIELD_MODULE, "MATERIAL_LOT")
                                .andEqualTo(MtGenType.FIELD_TYPE_GROUP, "CREATE_REASON")
                                .andEqualTo(MtGenType.FIELD_TYPE_CODE, "SPLIT")).build());
                if(CollectionUtils.isNotEmpty(mtGenTypeList)){
                    targetMaterialLotVo.setCreateReason(mtGenTypeList.get(0).getTypeCode());
                }
                targetMaterialLotVo.setEnableFlag(YES);
                targetMaterialLotVo.setPrimaryUomQty(targetMaterialLotDto.getTargetQty());
            } else {
                if (Objects.isNull(materialLot)) {
                    copyPropertiesIgnoreNullAndTableFields(firstMaterialLot, targetMaterialLotVo);
                    targetMaterialLotVo.setEventId(inEventId);
                    targetMaterialLotVo.setMaterialLotId(null);
                    //2020-11-01 add by sanfeng.zhang for zhenyong ????????????
                    targetMaterialLotVo.setCurrentContainerId(null);
                    targetMaterialLotVo.setTopContainerId(null);
                    targetMaterialLotVo.setUnloadTime(null);
                    targetMaterialLotVo.setLoadTime(new Date());
                    List<MtGenType> mtGenTypeList = mtGenTypeRepository.selectByCondition(Condition.builder(MtGenType.class)
                            .andWhere(Sqls.custom().andEqualTo(MtGenType.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(MtGenType.FIELD_MODULE, "MATERIAL_LOT")
                                    .andEqualTo(MtGenType.FIELD_TYPE_GROUP, "CREATE_REASON")
                                    .andEqualTo(MtGenType.FIELD_TYPE_CODE, "SPLIT")).build());
                    if(CollectionUtils.isNotEmpty(mtGenTypeList)){
                        targetMaterialLotVo.setCreateReason(mtGenTypeList.get(0).getTypeCode());
                    }
                    targetMaterialLotVo.setMaterialLotCode(targetMaterialLotCode);
                    targetMaterialLotVo.setEnableFlag(YES);
                    targetMaterialLotVo.setPrimaryUomQty(targetMaterialLotDto.getTargetQty());
                } else if (HmeConstants.ConstantValue.NO.equals(materialLot.getEnableFlag())) {
                    //???????????? ????????????0 2020/10/15 add by sanfeng.zhang for banzhenyong
                    materialLot.setPrimaryUomQty(0D);
                    materialLotRepository.updateByPrimaryKeySelective(materialLot);
                    //??????????????????
                    this.materialLotSiteInVerify(tenantId, materialLot);
                    //????????????????????????
                    this.materialLotLoadInVerify(tenantId, materialLot);
                    MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                    copyPropertiesIgnoreNullAndTableFields(firstMaterialLot, mtMaterialLotVO20);
                    mtMaterialLotVO20.setMaterialLotId(materialLot.getMaterialLotId());
                    mtMaterialLotVO20.setMaterialLotCode(materialLot.getMaterialLotCode());
                    mtMaterialLotVO20.setEnableFlag(YES);
                    mtMaterialLotVO20.setPrimaryUomQty(targetMaterialLotDto.getTargetQty());
                    updateTargetMaterialLotList.add(mtMaterialLotVO20);
                } else if (YES.equals(materialLot.getEnableFlag())) {
                    //??????????????????
                    this.materialLotSiteInVerify(tenantId, materialLot);
                    //????????????????????????
                    this.materialLotLoadInVerify(tenantId, materialLot);
                    MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                    mtMaterialLotVO20.setMaterialLotId(materialLot.getMaterialLotId());
                    mtMaterialLotVO20.setMaterialLotCode(materialLot.getMaterialLotCode());
                    mtMaterialLotVO20.setPrimaryUomQty(BigDecimal.valueOf(materialLot.getPrimaryUomQty())
                            .add(BigDecimal.valueOf(targetMaterialLotDto.getTargetQty())).doubleValue());
                    updateTargetMaterialLotList.add(mtMaterialLotVO20);
                }
            }

            // ?????????????????????
            if (Objects.isNull(materialLot)) {
                MtMaterialLotVO13 vo = materialLotRepository.materialLotUpdate(tenantId, targetMaterialLotVo, HmeConstants.ConstantValue.NO);
                this.updateMaterialLotExtendAttr(tenantId, firstMaterialLot.getMaterialLotId(), vo.getMaterialLotId(),
                        targetMaterialLotDto.getSupplierLot(), inEventId);
                // ?????????????????????
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, vo.getMaterialLotId());
                targetMaterialLotDto.setTargetMaterialLotId(mtMaterialLot.getMaterialLotId());
                targetMaterialLotDto.setTargetMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            } else {
                if (YES.equals(materialLot.getEnableFlag())){
                    // ??????????????????????????????????????????
                    MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
                    mtCommonExtendVO7.setKeyId(materialLot.getMaterialLotId());
                    List<MtCommonExtendVO4> attrs = new ArrayList<>();
                    MtCommonExtendVO4 statusAttr = new MtCommonExtendVO4();
                    statusAttr.setAttrName(HmeConstants.ExtendAttr.SUPPLIER_LOT);
                    statusAttr.setAttrValue(targetMaterialLotDto.getSupplierLot());
                    attrs.add(statusAttr);
                    mtCommonExtendVO7.setAttrs(attrs);
                    updateTargetAttrList.add(mtCommonExtendVO7);
                } else if (NO.equals(materialLot.getEnableFlag())) {
                    MtCommonExtendVO7 mtCommonExtendVO7 = this.assemblyTargetCodeAttr(tenantId, firstMaterialLot.getMaterialLotId(), materialLot.getMaterialLotId(), targetMaterialLotDto.getSupplierLot());
                    updateTargetAttrList.add(mtCommonExtendVO7);
                }
                targetMaterialLotDto.setTargetMaterialLotId(materialLot.getMaterialLotId());
                targetMaterialLotDto.setTargetMaterialLotCode(materialLot.getMaterialLotCode());
            }
        }
        // ?????????Quantity????????????????????????????????????
        for (HmeMaterialTransferDTO transferDTO : dtoList) {
            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotId(transferDTO.getMaterialLotId());
            mtMaterialLotVO20.setPrimaryUomQty(transferDTO.getQuantity());
            mtMaterialLotVO20.setEnableFlag(transferDTO.getQuantity().compareTo(0.0) == 0 ? HmeConstants.ConstantValue.NO : YES);
            updateMaterialLotList.add(mtMaterialLotVO20);
            // ?????????0 ???????????????
            // ????????????
            if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(transferDTO.getQuantity())) == 0
                    && StringUtils.isNotBlank(transferDTO.getCurrentContainerId())) {
                MtContainerVO25 cnt = new MtContainerVO25();
                cnt.setContainerId(transferDTO.getCurrentContainerId());
                cnt.setLoadObjectId(transferDTO.getMaterialLotId());
                cnt.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                cnt.setEventRequestId(requestId);
                containerRepository.containerUnload(tenantId, cnt);
            }
        }
        // ??????????????????
        if (CollectionUtils.isNotEmpty(updateMaterialLotList)) {
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, updateMaterialLotList, outEventId, HmeConstants.ConstantValue.NO);
        }
        // ??????????????????
        if (CollectionUtils.isNotEmpty(updateTargetMaterialLotList)) {
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, updateTargetMaterialLotList, inEventId, HmeConstants.ConstantValue.NO);
        }
        // ??????????????????????????????
        if (CollectionUtils.isNotEmpty(updateTargetAttrList)) {
            extendSettingsRepository.attrPropertyBatchUpdateNew(tenantId, "mt_material_lot_attr", inEventId, updateTargetAttrList);
        }

        // ???????????????????????????
        HmeMaterialTransferDTO7 materialLotDto =
                hmeMaterialTransferMapper.materialLotGet(tenantId, firstMaterialLotCode);
        // ????????????
        Double totalQty = dtoList.stream().map(HmeMaterialTransferDTO::getQuantity).filter(Objects::nonNull)
                .collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).summaryStatistics()
                .getSum();

        // ???????????????
        Double totalTfrQty = dtoList.stream().map(HmeMaterialTransferDTO::getTransferQuantity).filter(Objects::nonNull)
                .collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).summaryStatistics()
                .getSum();
        materialLotDto.setTotalQty(totalQty);
        materialLotDto.setTotalTransferQty(totalTfrQty);
        materialLotDto.setSupplierLot(targetDtoList.get(0).getSupplierLot());
        HmeMaterialTransferDTO2 returnDto = new HmeMaterialTransferDTO2();
        BeanUtils.copyProperties(materialLotDto, returnDto);
        returnDto.setTargetDtoList(targetDtoList);
        returnDto.setDtoList(dtoList);
        return returnDto;
    }

    private MtCommonExtendVO7 assemblyTargetCodeAttr(Long tenantId, String sourceMaterialLotId, String targetCodeId, String supplierLot) {
        // ??????????????????
        MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
        mtCommonExtendVO7.setKeyId(targetCodeId);
        List<MtCommonExtendVO4> attrList = new ArrayList<>();

        // ???????????????
        MtExtendVO extendAttrQuery = new MtExtendVO();
        extendAttrQuery.setKeyId(sourceMaterialLotId);
        extendAttrQuery.setTableName(ATTR_TABLE);
        List<MtExtendAttrVO> extendAttrList = extendSettingsRepository.attrPropertyQuery(tenantId, extendAttrQuery);

        MtCommonExtendVO4 newAttr = null;
        Boolean mfFlag = false;
        for (MtExtendAttrVO extendAttrVO : extendAttrList) {
            if (StringUtils.equals(extendAttrVO.getAttrName(), "ORIGINAL_ID") || StringUtils.equals(extendAttrVO.getAttrName(), "SUPPLIER_LOT")) {
                continue;
            }

            if (StringUtils.equals(extendAttrVO.getAttrName(), "MF_FLAG")) {
                mfFlag = true;
            }
            newAttr = new MtCommonExtendVO4();
            newAttr.setAttrName(extendAttrVO.getAttrName());
            newAttr.setAttrValue(extendAttrVO.getAttrValue());
            attrList.add(newAttr);
        }

        if (!mfFlag) {
            newAttr = new MtCommonExtendVO4();
            newAttr.setAttrName("MF_FLAG");
            newAttr.setAttrValue("");
            attrList.add(newAttr);
        }

        newAttr = new MtCommonExtendVO4();
        newAttr.setAttrName("ORIGINAL_ID");
        newAttr.setAttrValue(sourceMaterialLotId);
        attrList.add(newAttr);

        MtCommonExtendVO4 supplierAttr = new MtCommonExtendVO4();
        supplierAttr.setAttrName("SUPPLIER_LOT");
        supplierAttr.setAttrValue(supplierLot);
        attrList.add(supplierAttr);

        mtCommonExtendVO7.setAttrs(attrList);
        return mtCommonExtendVO7;
    }

    @Override
    public void getLock(Long tenantId, String materialLotCode) {
        hmeMaterialTransferRepository.operationLock(tenantId, materialLotCode, YES);
    }

    @Override
    public void releaseLock(Long tenantId, String materialLotCode) {
        hmeMaterialTransferRepository.operationLock(tenantId, materialLotCode, HmeConstants.ConstantValue.NO);
    }

    @Override
    public HmeMaterialTransferDTO4 targetMaterialLotInfoGet(Long tenantId, String materialLotCode) {
        HmeMaterialTransferDTO4 transferDTO4 = new HmeMaterialTransferDTO4();
        MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, materialLotCode);
        if(materialLot != null){
            MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
            mtMaterialLotAttrVO2.setMaterialLotId(materialLot.getMaterialLotId());
            mtMaterialLotAttrVO2.setAttrName("SUPPLIER_LOT");
            List<MtExtendAttrVO> materialLotAttrVOList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if(CollectionUtils.isNotEmpty(materialLotAttrVOList)){
                transferDTO4.setSupplierLot(materialLotAttrVOList.get(0).getAttrValue());
            }
        }
        return transferDTO4;
    }

    private void materialLotLoadInVerify(Long tenantId, MtMaterialLot materialLot){
        List<HmeMaterialLotLoad> lotLoadList = hmeMaterialLotLoadRepository.select(new HmeMaterialLotLoad() {{
            setMaterialLotId(materialLot.getMaterialLotId());
            setTenantId(tenantId);
        }});
        if(CollectionUtils.isNotEmpty(lotLoadList)){
            throw new MtException("MT_MATERIAL_TFR_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0021", HmeConstants.ConstantValue.HME));
        }
    }


    private void materialLotSiteInVerify(Long tenantId, MtMaterialLot materialLot){
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLot.getMaterialLotId())
                        .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE)).orderByDesc(HmeEoJobSn.FIELD_SITE_IN_DATE).build());
        if(CollectionUtils.isNotEmpty(hmeEoJobSnList)){
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnList.get(0);
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeEoJobSn.getWorkcellId());

            List<LovValueDTO> valueDTOList = lovAdapter.queryLovValue("HME.JOB_TYPE", tenantId);

            String jobType = "";
            Optional<LovValueDTO> typeOptional = valueDTOList.stream().filter(dto -> StringUtils.equals(dto.getValue(), hmeEoJobSn.getJobType())).findFirst();
            if(typeOptional.isPresent()){
                jobType = typeOptional.get().getMeaning();
            }
            throw new MtException("MT_MATERIAL_TFR_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0017", HmeConstants.ConstantValue.HME, mtModWorkcell != null ? mtModWorkcell.getWorkcellName() : "", jobType));
        }
    }

    private void materialLotBindStationVerify(Long tenantId, MtMaterialLot materialLot) {
        // ??????????????????????????????
        List<HmeEoJobLotMaterial> hmeEoJobLotMaterials = hmeMaterialTransferMapper.queryEoJobLotMaterialList(tenantId, materialLot.getMaterialLotId());
        if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterials)) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeEoJobLotMaterials.get(0).getWorkcellId());
            throw new MtException("MT_MATERIAL_TFR_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0024", HmeConstants.ConstantValue.HME, mtModWorkcell != null ? mtModWorkcell.getWorkcellCode() : ""));
        }
    }

    public static <E, T> void copyPropertiesIgnoreNullAndTableFields(E src, T target) {
        // ????????????????????????????????????ID???????????????????????????
        Set<String> hashSet = new HashSet<>();
        hashSet.addAll(Arrays.asList(WmsCommonUtils.getNullPropertyNames(src)));
        hashSet.addAll(Arrays.asList(IGNORE_TABLE_FIELDS));
        String[] result = new String[hashSet.size()];
        BeanUtils.copyProperties(src, target, hashSet.toArray(result));
    }

    private void materialLotStatusVerify(Long tenantId, MtMaterialLot materialLot){
        boolean statusFlag = false;
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(materialLot.getMaterialLotId());
        mtMaterialLotAttrVO2.setAttrName("STATUS");
        List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if(CollectionUtils.isEmpty(mtExtendAttrVOS) || StringUtils.isEmpty(mtExtendAttrVOS.get(0).getAttrValue())){
            throw new MtException("WMS_MTLOT_SPLIT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MTLOT_SPLIT_0004", "WMS"));
        }
        String status = mtExtendAttrVOS.get(0).getAttrValue();
        List<LovValueDTO> transferMtlotStatusList = lovAdapter.queryLovValue("HME.TRANSFER_MTLOT_STATUS", tenantId);
        for (LovValueDTO lovValueDTO:transferMtlotStatusList) {
            if(status.equals(lovValueDTO.getValue())){
                statusFlag = true;
                break;
            }
        }
        if(!statusFlag){
            String statusMeaning = lovAdapter.queryLovMeaning("WMS.MTLOT.STATUS", tenantId, status);
            throw new MtException("WMS_DISTRIBUTION_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_DISTRIBUTION_0010", "WMS", materialLot.getMaterialLotCode(), statusMeaning));
        }
    }
}
