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
 * 物料转移应用服务层实现
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
        // 获取当前用户站点信息
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        // 最新一次扫描记录(本次)90
        HmeMaterialTransferDTO dtoMax =
                        dtoList.stream().max(Comparator.comparing(HmeMaterialTransferDTO::getInputTimes))
                                        .orElse(new HmeMaterialTransferDTO());
        // 第一次扫描记录(缓存记录内第一条物料批)
        HmeMaterialTransferDTO dtoMin =
                        dtoList.stream().min(Comparator.comparing(HmeMaterialTransferDTO::getInputTimes))
                                        .orElse(new HmeMaterialTransferDTO());
        String materialLotCode = dtoMax.getMaterialLotCode();
        String firstMaterialLotCode = dtoMin.getMaterialLotCode();
        HmeMaterialTransferDTO2 returnDto = new HmeMaterialTransferDTO2();
        HmeMaterialTransferDTO7 materialLotDto = new HmeMaterialTransferDTO7();
        if (StringUtils.isBlank(dtoMax.getMaterialLotId())) {
            MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, materialLotCode);
            // 校验存在性
            if (materialLot == null) {
                throw new MtException("MT_MATERIAL_TFR_0002", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_TFR_0002", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            //2020-11-27 add by chaonan.hu for yiwei.zhou 增加盘点停用标识的校验
            if (YES.equals(materialLot.getStocktakeFlag())) {
                throw new MtException("WMS_COST_CENTER_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0034", "WMS", materialLotCode));
            }
            //2021-09-07 add by sanfeng.zhang for peng.zhao 冻结条码不允许转移
            if (YES.equals(materialLot.getFreezeFlag())) {
                throw new MtException("MT_MATERIAL_TFR_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_TFR_0026", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            //2021-09-07 add by sanfeng.zhang for peng.zhao 无效条码不允许转移
            if (!YES.equals(materialLot.getEnableFlag())) {
                throw new MtException("MT_MATERIAL_TFR_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_TFR_0025", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            //2021-09-07 add by sanfeng.zhang for peng.zhao 数量为0 的不允许转移
            if (BigDecimal.valueOf(materialLot.getPrimaryUomQty()).compareTo(BigDecimal.ZERO) == 0) {
                throw new MtException("MT_MATERIAL_TFR_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_TFR_0028", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            //2021-09-07 add by sanfeng.zhang for peng.zhao SN条码数量为1 的不允许转移
            String materialType = hmeEoJobSnRepository.getMaterialType(tenantId, materialLot.getSiteId(), materialLot.getMaterialId());
            if (HmeConstants.ConstantValue.SN.equals(materialType) && BigDecimal.valueOf(materialLot.getPrimaryUomQty()).compareTo(BigDecimal.ONE) == 0) {
                throw new MtException("MT_MATERIAL_TFR_0027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_TFR_0027", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            //2020-11-30 add by chaonan.hu for yiwei.zhou 增加对原始条码状态的校验
            this.materialLotStatusVerify(tenantId, materialLot);

            //2020-12-01 add by chaonan.hu for zhenyong.ban 增加对原始条码预装标识的校验
            wmsMaterialLotSplitRepository.materialLotVfVerify(tenantId, materialLot);

            //校验在制品
            wmsMaterialLotSplitRepository.materialLotMfFlagVerify(tenantId, materialLot);

            //校验芯片装载信息
            this.materialLotLoadInVerify(tenantId, materialLot);

            //校验条码进站
            this.materialLotSiteInVerify(tenantId, materialLot);

            //校验仓库
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
                //20210329 add by sanfeng.zhang for zhenyong.ban 校验用户是否有对该条码的操作权限
                Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
                List<String> privilegeIdList = hmeMaterialTransferMapper.queryDocPrivilegeByWarehouse(tenantId, userId, warehouse.getLocatorId());
                if (CollectionUtils.isEmpty(privilegeIdList)) {
                    throw new MtException("WMS_COST_CENTER_0067", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0067", "WMS", warehouse.getLocatorCode()));
                }
            }

            //校验货位类型
            List<LovValueDTO> locatorTypeList = lovAdapter.queryLovValue("HME.TRANSFERABLE_LOCATOR_TYPE", tenantId);
            String locatorType = mtModLocator != null ? mtModLocator.getLocatorType() : "";
            Optional<LovValueDTO> locatorTypeOpt = locatorTypeList.stream().filter(dto -> StringUtils.equals(dto.getValue(), locatorType)).findFirst();
            if(!locatorTypeOpt.isPresent()){
                MtGenType genType = mtGenTypeRepository.getGenType(tenantId, "MODELING", "LOCATOR_TYPE", locatorType);
                throw new MtException("MT_MATERIAL_TFR_0023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_TFR_0023", "HME", genType != null ? genType.getDescription() : ""));

            }

            // 校验有效性
            if (!YES.equals(materialLot.getEnableFlag())) {
                throw new MtException("MT_MATERIAL_TFR_0003", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "MT_MATERIAL_TFR_0003", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            // 校验物料批站点与当前用户站点是否一致
            if (!materialLot.getSiteId().equals(siteId)) {
                throw new MtException("MT_MATERIAL_TFR_0004", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_TFR_0004", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            // 校验冻结标记
            if (YES.equals(materialLot.getFreezeFlag())) {
                throw new MtException("MT_MATERIAL_TFR_0005", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "MT_MATERIAL_TFR_0005", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            // 校验盘点停用标识
            if (YES.equals(materialLot.getStocktakeFlag())) {
                throw new MtException("MT_MATERIAL_TFR_0006", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "MT_MATERIAL_TFR_0006", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            // 校验物料批物料是否为时效物料
            String itemProdType = hmeMaterialTransferMapper.materialSiteAttrGet(tenantId, materialLot.getSiteId(), materialLot.getMaterialId());
            if (StringUtils.equals(itemProdType, HmeConstants.AttrName.TIME)) {
                throw new MtException("MT_MATERIAL_TFR_0016", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "MT_MATERIAL_TFR_0016", HmeConstants.ConstantValue.HME, materialLotCode));
            }
            // 增加校验条码没有绑定在工位上 绑定则报错
            this.materialLotBindStationVerify(tenantId, materialLot);
            // 若不是第一次扫描的条码，则与缓存物料批数据第一条进行比较
            if (dtoList.size() > 1) {
                MtMaterialLot firstMaterialLot =
                                hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, firstMaterialLotCode);
                validateAttr(tenantId, materialLot, firstMaterialLot);
                // 校验物料是否一致
                if (!materialLot.getMaterialId().equals(firstMaterialLot.getMaterialId())) {
                    throw new MtException("MT_MATERIAL_TFR_0007", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_TFR_0007", HmeConstants.ConstantValue.HME, materialLotCode));
                }
                // 校验库位ID是否一致
                if (!Optional.ofNullable(materialLot.getLocatorId()).orElse("")
                                .equals(Optional.ofNullable(firstMaterialLot.getLocatorId()).orElse(""))) {
                    throw new MtException("MT_MATERIAL_TFR_0008", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_TFR_0008", HmeConstants.ConstantValue.HME, materialLotCode));
                }
                // 校验批次是否一致
                if (!Optional.ofNullable(materialLot.getLot()).orElse("")
                                .equals(Optional.ofNullable(firstMaterialLot.getLot()).orElse(""))) {
                    throw new MtException("MT_MATERIAL_TFR_0009", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_TFR_0009", HmeConstants.ConstantValue.HME, materialLotCode));
                }
                // 校验所有者类型是否一致
                if (!Optional.ofNullable(materialLot.getOwnerType()).orElse("")
                                .equals(Optional.ofNullable(firstMaterialLot.getOwnerType()).orElse(""))) {
                    throw new MtException("MT_MATERIAL_TFR_0010", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_TFR_0010", HmeConstants.ConstantValue.HME, materialLotCode));
                }
                // 校验所有者ID是否一致
                if (!Optional.ofNullable(materialLot.getOwnerId()).orElse("")
                                .equals(Optional.ofNullable(firstMaterialLot.getOwnerId()).orElse(""))) {
                    throw new MtException("MT_MATERIAL_TFR_0011", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_TFR_0011", HmeConstants.ConstantValue.HME, materialLotCode));
                }
                // 校验供应商ID是否一致
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
        // 条码总数
        Double totalQty = dtoList.stream().map(HmeMaterialTransferDTO::getQuantity).filter(Objects::nonNull)
                        .collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).summaryStatistics()
                        .getSum();

        // 待转移总数
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
        // 第一次扫描记录(缓存记录内第一条物料批)
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
     * 转移前校验
     *
     * @param tenantId 租户
     * @param materialLot 条码号
     * @param siteId 站点ID
     * @param targetMaterialLotCode 目标条码编码
     * @param firstMaterialLot 来源条码
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/20 10:23:57
     */
    private void validateBeforeTransfer(Long tenantId, MtMaterialLot materialLot, String siteId,
                    String targetMaterialLotCode, MtMaterialLot firstMaterialLot) {
        // 20210719 modify by sanfeng.zhang for wenxin.zhang 无效 校验物料、批次、供应商、销单是否一致
        // 校验有效性
        if (!Objects.isNull(materialLot)) {
            if (YES.equals(materialLot.getEnableFlag())) {
                // 校验物料批站点与当前用户站点是否一致
                if (!materialLot.getSiteId().equals(siteId)) {
                    throw new MtException("MT_MATERIAL_TFR_0004",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0004",
                                    HmeConstants.ConstantValue.HME, targetMaterialLotCode));
                }
                //校验在制品
                wmsMaterialLotSplitRepository.materialLotMfFlagVerify(tenantId, materialLot);
                // 校验冻结标记
                if (YES.equals(materialLot.getFreezeFlag())) {
                    throw new MtException("MT_MATERIAL_TFR_0005",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0005",
                                    HmeConstants.ConstantValue.HME, targetMaterialLotCode));
                }
                // 校验盘点停用标识
                if (YES.equals(materialLot.getStocktakeFlag())) {
                    throw new MtException("MT_MATERIAL_TFR_0006",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0006",
                                    HmeConstants.ConstantValue.HME, targetMaterialLotCode));
                }
                // 校验库位ID是否一致
                if (!Optional.ofNullable(materialLot.getLocatorId()).orElse("")
                        .equals(Optional.ofNullable(firstMaterialLot.getLocatorId()).orElse(""))) {
                    throw new MtException("MT_MATERIAL_TFR_0008",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0008",
                                    HmeConstants.ConstantValue.HME, targetMaterialLotCode));
                }
                // 校验所有者类型是否一致
                if (!Optional.ofNullable(materialLot.getOwnerType()).orElse("")
                        .equals(Optional.ofNullable(firstMaterialLot.getOwnerType()).orElse(""))) {
                    throw new MtException("MT_MATERIAL_TFR_0010",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0010",
                                    HmeConstants.ConstantValue.HME, targetMaterialLotCode));
                }
                // 校验所有者ID是否一致
                if (!Optional.ofNullable(materialLot.getOwnerId()).orElse("")
                        .equals(Optional.ofNullable(firstMaterialLot.getOwnerId()).orElse(""))) {
                    throw new MtException("MT_MATERIAL_TFR_0011",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0011",
                                    HmeConstants.ConstantValue.HME, targetMaterialLotCode));
                }
                // 校验质量状态是否一致
                if (!StringCommonUtils.equalsIgnoreBlank(materialLot.getQualityStatus(),
                        firstMaterialLot.getQualityStatus())) {
                    throw new MtException("MT_MATERIAL_TFR_0015",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0015",
                                    HmeConstants.ConstantValue.HME, targetMaterialLotCode));
                }
            }
            // 校验物料是否一致
            if (!materialLot.getMaterialId().equals(firstMaterialLot.getMaterialId())) {
                throw new MtException("MT_MATERIAL_TFR_0007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0007",
                                                HmeConstants.ConstantValue.HME, targetMaterialLotCode));
            }
            // 校验批次是否一致
            if (!Optional.ofNullable(materialLot.getLot()).orElse("")
                            .equals(Optional.ofNullable(firstMaterialLot.getLot()).orElse(""))) {
                throw new MtException("MT_MATERIAL_TFR_0009",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0009",
                                                HmeConstants.ConstantValue.HME, targetMaterialLotCode));
            }
            // 校验供应商ID是否一致
            if (!Optional.ofNullable(materialLot.getSupplierId()).orElse("")
                            .equals(Optional.ofNullable(firstMaterialLot.getSupplierId()).orElse(""))) {
                throw new MtException("MT_MATERIAL_TFR_0012",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0012",
                                                HmeConstants.ConstantValue.HME, targetMaterialLotCode));
            }
            // 查询来源条码拓展属性
            MtExtendVO sourceExtendAttrQuery = new MtExtendVO();
            sourceExtendAttrQuery.setKeyId(firstMaterialLot.getMaterialLotId());
            sourceExtendAttrQuery.setTableName(ATTR_TABLE);
            List<MtExtendAttrVO> sourceExtendAttrList =
                            extendSettingsRepository.attrPropertyQuery(tenantId, sourceExtendAttrQuery);
            Map<String, String> sourceMap = sourceExtendAttrList.stream()
                            .collect(Collectors.toMap(MtExtendAttrVO::getAttrName, MtExtendAttrVO::getAttrValue));

            // 查询目标条码拓展属性
            MtExtendVO extendAttrQuery = new MtExtendVO();
            extendAttrQuery.setKeyId(materialLot.getMaterialLotId());
            extendAttrQuery.setTableName(ATTR_TABLE);
            List<MtExtendAttrVO> extendAttrList = extendSettingsRepository.attrPropertyQuery(tenantId, extendAttrQuery);
            // 无效只校验SO_NUM, SO_LINE_NUM
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
     * 校验扩展字段
     *
     * @param tenantId
     * @param materialLot
     * @param firstMaterialLot
     * @author jiangling.zheng@hand-china.com 2020/10/27 11:41
     * @return void
     */
    private void validateAttr(Long tenantId, MtMaterialLot materialLot, MtMaterialLot firstMaterialLot){
        // 查询第一条扩展属性
        MtExtendVO firstAttrQuery = new MtExtendVO();
        firstAttrQuery.setKeyId(firstMaterialLot.getMaterialLotId());
        firstAttrQuery.setTableName(ATTR_TABLE);
        List<MtExtendAttrVO> firstAttrList = extendSettingsRepository.attrPropertyQuery(tenantId, firstAttrQuery);
        // 查询当前条码扩展属性
        MtExtendVO currAttrQuery = new MtExtendVO();
        currAttrQuery.setKeyId(materialLot.getMaterialLotId());
        currAttrQuery.setTableName(ATTR_TABLE);
        List<MtExtendAttrVO> currAttrList = extendSettingsRepository.attrPropertyQuery(tenantId, currAttrQuery);
        Optional<MtExtendAttrVO> firstSoNum = firstAttrList.stream().filter(c ->
                StringUtils.equals(c.getAttrName(), "SO_NUM")).findFirst();
        Optional<MtExtendAttrVO> currSoNum = currAttrList.stream().filter(c ->
                StringUtils.equals(c.getAttrName(), "SO_NUM")).findFirst();
        //校验销售订单号
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
        //校验销售订单行号
        boolean isDiffSoLineNum = (firstSoLineNum.isPresent() && !currSoLineNum.isPresent()) ||
                (!firstSoLineNum.isPresent() && currSoLineNum.isPresent()) || (firstSoLineNum.isPresent() && currSoLineNum.isPresent() &&
                !StringUtils.equals(firstSoLineNum.get().getAttrValue(), currSoLineNum.get().getAttrValue()));
        if (isDiffSoLineNum) {
            throw new MtException("MT_MATERIAL_TFR_0020", mtErrorMessageRepository.getErrorMessageWithModule(
                    tenantId, "MT_MATERIAL_TFR_0020", HmeConstants.ConstantValue.HME));
        }
    }

    /**
     * 更新拓展字段
     *
     * @param tenantId 租户
     * @param sourceMaterialLotId 来源物料批ID
     * @param materialLotId 生成物料批ID
     * @param eventId 事件ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/20 10:32:56
     */
    private void updateMaterialLotExtendAttr(Long tenantId, String sourceMaterialLotId, String materialLotId,String supplierLot,
                    String eventId) {
        // 查询原属性
        MtExtendVO extendAttrQuery = new MtExtendVO();
        extendAttrQuery.setKeyId(sourceMaterialLotId);
        extendAttrQuery.setTableName(ATTR_TABLE);
        List<MtExtendAttrVO> extendAttrList = extendSettingsRepository.attrPropertyQuery(tenantId, extendAttrQuery);

        // 覆盖目标属性
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
        // 获取当前用户站点信息
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        List<HmeMaterialTransferDTO> dtoList = targetDto.getDtoList();
        String targetMaterialLotCode = targetDto.getTargetMaterialLotCode();

        // 待转移总数
        Double totalTransferQty = dtoList.stream().map(HmeMaterialTransferDTO::getTransferQuantity)
                        .filter(Objects::nonNull).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue)
                        .summaryStatistics().getSum();
        if (targetDto.getTargetQty().compareTo(totalTransferQty) > 0) {
            throw new MtException("MT_MATERIAL_TFR_0013",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0013",
                                            HmeConstants.ConstantValue.HME, targetDto.getTargetQty().toString(),
                                            totalTransferQty.toString()));
        }
        // 根据扫描顺序排序
        dtoList.sort(Comparator.comparing(HmeMaterialTransferDTO::getInputTimes));
        // 第一次扫描记录(缓存记录内第一条物料批)
        HmeMaterialTransferDTO firstDto =
                        dtoList.stream().min(Comparator.comparing(HmeMaterialTransferDTO::getInputTimes))
                                        .orElse(new HmeMaterialTransferDTO());
        String firstMaterialLotCode = firstDto.getMaterialLotCode();
        MtMaterialLot firstMaterialLot =
                        hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, firstMaterialLotCode);
        // 获取目标条码信息
        MtMaterialLot materialLot = new MtMaterialLot();
        if (StringUtils.isNotBlank(targetMaterialLotCode)) {
            materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, targetMaterialLotCode);
            this.validateBeforeTransfer(tenantId, materialLot, siteId, targetMaterialLotCode, firstMaterialLot);
        }
        // 创建请求事件
        String requestId = commonServiceComponent.generateEventRequest(tenantId,
                        HmeConstants.EventType.MATERIAL_TRANSFER_REQUISTION);
        // 创建物料转移扣减事件
        String outEventId = commonServiceComponent.generateEvent(tenantId, HmeConstants.EventType.MATERIAL_TRANSFER_OUT,
                        requestId);
        // 剩余转移数量
        Double targetQty = targetDto.getTargetQty();
        // 依次进行数量转移
        for (HmeMaterialTransferDTO dto : dtoList) {
            //校验条码数量一致
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
            // 扣减转移物料批数量
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
            // 卸载容器
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
        // 创建物料转移新增事件
        String inEventId = commonServiceComponent.generateEvent(tenantId, HmeConstants.EventType.MATERIAL_TRANSFER_IN,
                        requestId);
        MtMaterialLotVO2 targetMaterialLotVo = new MtMaterialLotVO2();
        if (StringUtils.isBlank(targetMaterialLotCode)) {
            // 创建目标物料批信息
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
                //无效条码 数量置为0 2020/10/15 add by sanfeng.zhang for banzhenyong
                materialLot.setPrimaryUomQty(0D);
                materialLotRepository.updateByPrimaryKeySelective(materialLot);
                //校验条码进站
                this.materialLotSiteInVerify(tenantId, materialLot);
                copyPropertiesIgnoreNullAndTableFields(firstMaterialLot, targetMaterialLotVo);
                targetMaterialLotVo.setEventId(inEventId);
                targetMaterialLotVo.setMaterialLotId(materialLot.getMaterialLotId());
                targetMaterialLotVo.setMaterialLotCode(materialLot.getMaterialLotCode());
                targetMaterialLotVo.setEnableFlag(HmeConstants.ConstantValue.YES);
                targetMaterialLotVo.setPrimaryUomQty(targetDto.getTargetQty());
            } else if (HmeConstants.ConstantValue.YES.equals(materialLot.getEnableFlag())) {
                //校验条码进站
                this.materialLotSiteInVerify(tenantId, materialLot);
                targetMaterialLotVo.setEventId(inEventId);
                targetMaterialLotVo.setMaterialLotId(materialLot.getMaterialLotId());
                targetMaterialLotVo.setMaterialLotCode(materialLot.getMaterialLotCode());
                targetMaterialLotVo.setPrimaryUomQty(BigDecimal.valueOf(materialLot.getPrimaryUomQty()).add(BigDecimal.valueOf(targetDto.getTargetQty())).doubleValue());
            }
        }

        // 创建后目标条码
        MtMaterialLotVO13 vo = materialLotRepository.materialLotUpdate(tenantId, targetMaterialLotVo,
                        HmeConstants.ConstantValue.NO);
        // 更新目标条码拓展字段
        if (!(!Objects.isNull(materialLot) && HmeConstants.ConstantValue.YES.equals(materialLot.getEnableFlag()))) {
            this.updateMaterialLotExtendAttr(tenantId, firstMaterialLot.getMaterialLotId(), vo.getMaterialLotId(), targetDto.getSupplierLot(),
                            inEventId);
        }else {
            //更新有效条码的供应商批次和原始条码
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
        // 刷新界面待转移数据
        HmeMaterialTransferDTO2 materialLotDto =
                        hmeMaterialTransferMapper.materialLotGet(tenantId, firstMaterialLotCode);
        // 条码总数
        Double totalQty = dtoList.stream().map(HmeMaterialTransferDTO::getQuantity).filter(Objects::nonNull)
                        .collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).summaryStatistics()
                        .getSum();

        // 待转移总数
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
        // 获取当前用户站点信息
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        // 待转移区域
        List<HmeMaterialTransferDTO> dtoList = targetDto.getDtoList();
        // 目标区域
        List<HmeMaterialTransferDTO6> targetDtoList = targetDto.getTargetDtoList();
        // 目标数量不能为空
        long count = targetDtoList.stream().filter(c -> c.getTargetQty() == null).count();
        if (count != 0 ) {
            throw new MtException("MT_MATERIAL_TFR_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0001", HmeConstants.ConstantValue.HME, "targetQty"));
        }
        // 待转移总数
        Double totalTransferQty = dtoList.stream().map(HmeMaterialTransferDTO::getTransferQuantity)
                .filter(Objects::nonNull).mapToDouble(Double::doubleValue)
                .summaryStatistics().getSum();
        // 转移总数
        Double totalTargetQty = targetDtoList.stream().map(HmeMaterialTransferDTO6::getTargetQty).mapToDouble(Double::doubleValue)
                .summaryStatistics().getSum();
        if (totalTargetQty.compareTo(totalTransferQty) > 0) {
            throw new MtException("MT_MATERIAL_TFR_0013",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_TFR_0013",
                            HmeConstants.ConstantValue.HME, totalTargetQty.toString(),
                            totalTransferQty.toString()));
        }
        // 根据扫描顺序排序
        dtoList.sort(Comparator.comparing(HmeMaterialTransferDTO::getInputTimes));
        // 第一次扫描记录(缓存记录内第一条物料批)
        HmeMaterialTransferDTO firstDto =
                dtoList.stream().min(Comparator.comparing(HmeMaterialTransferDTO::getInputTimes))
                        .orElse(new HmeMaterialTransferDTO());
        String firstMaterialLotCode = firstDto.getMaterialLotCode();
        MtMaterialLot firstMaterialLot =
                hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, firstMaterialLotCode);
        // 创建请求事件
        String requestId = commonServiceComponent.generateEventRequest(tenantId,
                HmeConstants.EventType.MATERIAL_TRANSFER_REQUISTION);

        // 更新来源条码
        List<MtMaterialLotVO20> updateMaterialLotList = new ArrayList<>();
        // 创建物料转移扣减事件
        String outEventId = commonServiceComponent.generateEvent(tenantId, HmeConstants.EventType.MATERIAL_TRANSFER_OUT,
                requestId);
        // 创建物料转移新增事件
        String inEventId = commonServiceComponent.generateEvent(tenantId, HmeConstants.EventType.MATERIAL_TRANSFER_IN,
                requestId);
        // 更新目标条码(存在 且有效和无效)
        List<MtMaterialLotVO20> updateTargetMaterialLotList = new ArrayList<>();
        // 更新目标条码扩展字段(存在 且有效和无效)
        List<MtCommonExtendVO7> updateTargetAttrList = new ArrayList<>();
        for (HmeMaterialTransferDTO6 targetMaterialLotDto : targetDtoList) {
            String targetMaterialLotCode = targetMaterialLotDto.getTargetMaterialLotCode();
            // 获取目标条码信息
            MtMaterialLot materialLot = null;
            if (StringUtils.isNotBlank(targetMaterialLotCode)) {
                materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, targetMaterialLotCode);
                this.validateBeforeTransfer(tenantId, materialLot, siteId, targetMaterialLotCode, firstMaterialLot);
            }
            // 剩余转移数量
            Double targetQty = targetMaterialLotDto.getTargetQty();
            // 依次进行数量转移
            for (HmeMaterialTransferDTO dto : dtoList) {
                //校验条码数量一致
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
                // 扣减转移物料批数量
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
                // 创建目标物料批信息
                copyPropertiesIgnoreNullAndTableFields(firstMaterialLot, targetMaterialLotVo);
                targetMaterialLotVo.setEventId(inEventId);
                targetMaterialLotVo.setMaterialLotId(null);
                targetMaterialLotVo.setMaterialLotCode(null);
                //2020-11-01 add by sanfeng.zhang for zhenyong 字段置空
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
                    //2020-11-01 add by sanfeng.zhang for zhenyong 字段置空
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
                    //无效条码 数量置为0 2020/10/15 add by sanfeng.zhang for banzhenyong
                    materialLot.setPrimaryUomQty(0D);
                    materialLotRepository.updateByPrimaryKeySelective(materialLot);
                    //校验条码进站
                    this.materialLotSiteInVerify(tenantId, materialLot);
                    //校验芯片装载信息
                    this.materialLotLoadInVerify(tenantId, materialLot);
                    MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                    copyPropertiesIgnoreNullAndTableFields(firstMaterialLot, mtMaterialLotVO20);
                    mtMaterialLotVO20.setMaterialLotId(materialLot.getMaterialLotId());
                    mtMaterialLotVO20.setMaterialLotCode(materialLot.getMaterialLotCode());
                    mtMaterialLotVO20.setEnableFlag(YES);
                    mtMaterialLotVO20.setPrimaryUomQty(targetMaterialLotDto.getTargetQty());
                    updateTargetMaterialLotList.add(mtMaterialLotVO20);
                } else if (YES.equals(materialLot.getEnableFlag())) {
                    //校验条码进站
                    this.materialLotSiteInVerify(tenantId, materialLot);
                    //校验芯片装载信息
                    this.materialLotLoadInVerify(tenantId, materialLot);
                    MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                    mtMaterialLotVO20.setMaterialLotId(materialLot.getMaterialLotId());
                    mtMaterialLotVO20.setMaterialLotCode(materialLot.getMaterialLotCode());
                    mtMaterialLotVO20.setPrimaryUomQty(BigDecimal.valueOf(materialLot.getPrimaryUomQty())
                            .add(BigDecimal.valueOf(targetMaterialLotDto.getTargetQty())).doubleValue());
                    updateTargetMaterialLotList.add(mtMaterialLotVO20);
                }
            }

            // 创建后目标条码
            if (Objects.isNull(materialLot)) {
                MtMaterialLotVO13 vo = materialLotRepository.materialLotUpdate(tenantId, targetMaterialLotVo, HmeConstants.ConstantValue.NO);
                this.updateMaterialLotExtendAttr(tenantId, firstMaterialLot.getMaterialLotId(), vo.getMaterialLotId(),
                        targetMaterialLotDto.getSupplierLot(), inEventId);
                // 返回新增的条码
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, vo.getMaterialLotId());
                targetMaterialLotDto.setTargetMaterialLotId(mtMaterialLot.getMaterialLotId());
                targetMaterialLotDto.setTargetMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            } else {
                if (YES.equals(materialLot.getEnableFlag())){
                    // 有效和无效条码的扩展字段更新
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
        // 最终的Quantity则为来源条码的变化后数量
        for (HmeMaterialTransferDTO transferDTO : dtoList) {
            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotId(transferDTO.getMaterialLotId());
            mtMaterialLotVO20.setPrimaryUomQty(transferDTO.getQuantity());
            mtMaterialLotVO20.setEnableFlag(transferDTO.getQuantity().compareTo(0.0) == 0 ? HmeConstants.ConstantValue.NO : YES);
            updateMaterialLotList.add(mtMaterialLotVO20);
            // 数量为0 则卸载容器
            // 卸载容器
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
        // 更新来源条码
        if (CollectionUtils.isNotEmpty(updateMaterialLotList)) {
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, updateMaterialLotList, outEventId, HmeConstants.ConstantValue.NO);
        }
        // 更新目标条码
        if (CollectionUtils.isNotEmpty(updateTargetMaterialLotList)) {
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, updateTargetMaterialLotList, inEventId, HmeConstants.ConstantValue.NO);
        }
        // 更新目标条码扩展字段
        if (CollectionUtils.isNotEmpty(updateTargetAttrList)) {
            extendSettingsRepository.attrPropertyBatchUpdateNew(tenantId, "mt_material_lot_attr", inEventId, updateTargetAttrList);
        }

        // 刷新界面待转移数据
        HmeMaterialTransferDTO7 materialLotDto =
                hmeMaterialTransferMapper.materialLotGet(tenantId, firstMaterialLotCode);
        // 条码总数
        Double totalQty = dtoList.stream().map(HmeMaterialTransferDTO::getQuantity).filter(Objects::nonNull)
                .collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).summaryStatistics()
                .getSum();

        // 待转移总数
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
        // 覆盖目标属性
        MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
        mtCommonExtendVO7.setKeyId(targetCodeId);
        List<MtCommonExtendVO4> attrList = new ArrayList<>();

        // 查询原属性
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
        // 校验条码是否绑定工位
        List<HmeEoJobLotMaterial> hmeEoJobLotMaterials = hmeMaterialTransferMapper.queryEoJobLotMaterialList(tenantId, materialLot.getMaterialLotId());
        if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterials)) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeEoJobLotMaterials.get(0).getWorkcellId());
            throw new MtException("MT_MATERIAL_TFR_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0024", HmeConstants.ConstantValue.HME, mtModWorkcell != null ? mtModWorkcell.getWorkcellCode() : ""));
        }
    }

    public static <E, T> void copyPropertiesIgnoreNullAndTableFields(E src, T target) {
        // 对象值转换时屏蔽表字段：ID，创建更新人等信息
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
