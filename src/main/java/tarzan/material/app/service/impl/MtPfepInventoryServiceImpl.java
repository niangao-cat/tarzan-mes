package tarzan.material.app.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtThreadPoolRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.config.ExecutorConfig;
import tarzan.material.api.dto.MtPfepInventoryDTO2;
import tarzan.material.api.dto.MtPfepInventoryDTO3;
import tarzan.material.api.dto.MtPfepInventoryDTO4;
import tarzan.material.app.service.MtMaterialSiteService;
import tarzan.material.app.service.MtPfepInventoryCategoryService;
import tarzan.material.app.service.MtPfepInventoryService;
import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.entity.MtMaterialCategorySet;
import tarzan.material.domain.entity.MtMaterialCategorySite;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtPfepInventory;
import tarzan.material.domain.entity.MtPfepInventoryCategory;
import tarzan.material.domain.repository.*;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtPfepInventoryVO4;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.material.infra.mapper.MtMaterialSiteMapper;
import tarzan.material.infra.mapper.MtPfepInventoryCategoryMapper;
import tarzan.material.infra.mapper.MtPfepInventoryMapper;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.*;

/**
 * 物料存储属性应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Service
public class MtPfepInventoryServiceImpl extends BaseServiceImpl<MtPfepInventory> implements MtPfepInventoryService {
    private static final Logger logger = LoggerFactory.getLogger(MtPfepInventoryServiceImpl.class);
    private static final String MT_PFEP_INVENTORY_ATTR = "mt_pfep_inventory_attr";
    private static final String MT_PFEP_INVENTORY_CATEGORY_ATTR = "mt_pfep_inventory_catg_attr";

    @Autowired
    private MtPfepInventoryMapper mtPfepInventoryMapper;

    @Autowired
    private MtMaterialSiteMapper mtMaterialSiteMapper;

    @Autowired
    private MtMaterialSiteService mtMaterialSiteService;

    @Autowired
    private MtPfepInventoryCategoryService categoryService;

    @Autowired
    private MtPfepInventoryCategoryMapper categoryMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;

    @Autowired
    private MtThreadPoolRepository mtThreadPoolRepository;

    @Autowired
    private MtMaterialCategorySiteRepository mtMaterialCategorySiteRepository;

    @Autowired
    private MtMaterialCategorySetRepository mtMaterialCategorySetRepository;

    @Autowired
    private ExecutorConfig executorConfig;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtModAreaRepository mtModAreaRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtMaterialCategoryRepository mtMaterialCategoryRepository;

    @Override
    public Page<MtPfepInventoryVO4> pfepInventoryDetialQuery(Long tenantId,
                                                             PageRequest pageRequest,
                                                             MtPfepInventoryDTO4 dto) {
        Page<MtPfepInventoryVO4> page = new Page<>();
        if (("material".equals(dto.getType()) &&
                StringUtils.isNotEmpty(dto.getKid()))
                || StringUtils.isNotEmpty(dto.getMaterialId()) ||
                ObjectFieldsHelper.isAllFieldNull(dto)) {
            Page<MtPfepInventory> base = PageHelper.doPage(pageRequest,
                    () ->
                            mtPfepInventoryMapper.mtPfepInventoryDetialQuery(tenantId, dto));
            page.setTotalPages(base.getTotalPages());
            page.setTotalElements(base.getTotalElements());
            page.setNumberOfElements(base.getNumberOfElements());
            page.setSize(base.getSize());
            page.setNumber(base.getNumber());
            if (CollectionUtils.isEmpty(base)) {
                return page;
            }
            // 并行流获取id列表
            List<String> packageSizeUomIds =
                    base.parallelStream().map(MtPfepInventory::getPackageSizeUomId).distinct()
                            .collect(Collectors.toList());
            List<String> weightUomIds =
                    base.parallelStream().map(MtPfepInventory::getWeightUomId).distinct()
                            .collect(Collectors.toList());
            List<String> stockLocatorIds =
                    base.parallelStream().map(MtPfepInventory::getStockLocatorId).distinct()
                            .collect(Collectors.toList());
            List<String> issuedLocatorId =
                    base.parallelStream().map(MtPfepInventory::getIssuedLocatorId).distinct()
                            .collect(Collectors.toList());
            List<String> completionLocatorIds =
                    base.parallelStream().map(MtPfepInventory::getCompletionLocatorId)
                            .distinct().collect(Collectors.toList());
            List<String> areaIds = base.parallelStream().filter(t ->
                    "AREA".equals(t.getOrganizationType()))
                    .map(MtPfepInventory::getOrganizationId).distinct().collect(Collectors.toList());
            List<String> productionLineIds = base.parallelStream()
                    .filter(t ->
                            "PRODUCTIONLINE".equals(t.getOrganizationType()))
                    .map(MtPfepInventory::getOrganizationId).distinct().collect(Collectors.toList());
            List<String> workcellIds = base.parallelStream().filter(t ->
                    "WORKCELL".equals(t.getOrganizationType()))
                    .map(MtPfepInventory::getOrganizationId).distinct().collect(Collectors.toList());
            List<String> locatorIds = base.parallelStream().filter(t ->
                    "LOCATOR".equals(t.getOrganizationType()))
                    .map(MtPfepInventory::getOrganizationId).distinct().collect(Collectors.toList());
            List<String> allUomIds = new ArrayList<>();
            allUomIds.addAll(packageSizeUomIds);
            allUomIds.addAll(weightUomIds);
            List<String> allLocatorIds = new ArrayList<>();
            allLocatorIds.addAll(stockLocatorIds);
            allLocatorIds.addAll(issuedLocatorId);
            allLocatorIds.addAll(completionLocatorIds);
            allLocatorIds.addAll(locatorIds);
            // 根据物料站点id获取物料Id和站点id
            List<String> materialSiteIds =
                    base.parallelStream().map(MtPfepInventory::getMaterialSiteId).distinct()
                            .collect(Collectors.toList());
            List<MtMaterialSite> mtMaterialSites =
                    mtMaterialSiteRepository.queryByMaterialSiteId(tenantId, materialSiteIds);
            Map<String, String> materialIds = new HashMap<>();
            Map<String, String> siteIds = new HashMap<>();
            if (CollectionUtils.isNotEmpty(mtMaterialSites)) {
                materialIds = mtMaterialSites.stream().collect(
                        Collectors.toMap(MtMaterialSite::getMaterialSiteId,
                                MtMaterialSite::getMaterialId));
                siteIds = mtMaterialSites.stream().collect(
                        Collectors.toMap(MtMaterialSite::getMaterialSiteId,
                                MtMaterialSite::getSiteId));
            }
            // 获取物料信息
            Map<String, MtMaterialVO> materialMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(materialIds.values())) {
                List<MtMaterialVO> materialVOS =
                        mtMaterialRepository.materialBasicInfoBatchGet(tenantId,
                                new ArrayList<>(materialIds.values()));
                if (CollectionUtils.isNotEmpty(materialVOS)) {
                    materialMap =
                            materialVOS.stream().collect(Collectors.toMap(MtMaterialVO::getMaterialId, t
                                    -> t));
                }
            }
            // 获取站点信息
            Map<String, MtModSite> siteMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(siteIds.values())) {
                List<MtModSite> mtModSites =
                        mtModSiteRepository.siteBasicPropertyBatchGet(tenantId,
                                new ArrayList<>(siteIds.values()));
                if (CollectionUtils.isNotEmpty(mtModSites)) {
                    siteMap =
                            mtModSites.stream().collect(Collectors.toMap(MtModSite::getSiteId, t -> t));
                }
            }
            // 获取单位信息
            Map<String, String> uomCodes = new HashMap<>();
            if (CollectionUtils.isNotEmpty(allUomIds)) {
                List<MtUomVO> mtUomVOS =
                        mtUomRepository.uomPropertyBatchGet(tenantId, allUomIds);
                if (CollectionUtils.isNotEmpty(mtUomVOS)) {
                    uomCodes =
                            mtUomVOS.stream().collect(Collectors.toMap(MtUomVO::getUomId,
                                    MtUomVO::getUomCode));
                }
            }
            // 获取区域信息
            Map<String, MtModArea> areaMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(areaIds)) {
                List<MtModArea> mtModAreas =
                        mtModAreaRepository.areaBasicPropertyBatchGet(tenantId, areaIds);
                if (CollectionUtils.isNotEmpty(mtModAreas)) {
                    areaMap =
                            mtModAreas.stream().collect(Collectors.toMap(MtModArea::getAreaId, t -> t));
                }
            }
            // 获取生产线信息
            Map<String, MtModProductionLine> productionLineMap = new HashMap<>
                    ();
            if (CollectionUtils.isNotEmpty(productionLineIds)) {
                List<MtModProductionLine> mtModProductionLines =
                        mtModProductionLineRepository
                                .prodLineBasicPropertyBatchGet(tenantId,
                                        productionLineIds);
                if (CollectionUtils.isNotEmpty(mtModProductionLines)) {
                    productionLineMap = mtModProductionLines.stream()
                            .collect(Collectors.toMap(MtModProductionLine::getProdLineId, t -> t));
                }
            }
            // 获取工作单元信息
            Map<String, MtModWorkcell> workCellMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                List<MtModWorkcell> mtModWorkcells =
                        mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId,
                                workcellIds);
                if (CollectionUtils.isNotEmpty(mtModWorkcells)) {
                    workCellMap = mtModWorkcells.stream()
                            .collect(Collectors.toMap(MtModWorkcell::getWorkcellId, t -> t));
                }
            }
            // 获取库位信息
            Map<String, MtModLocator> locatorMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(allLocatorIds)) {
                List<MtModLocator> mtModLocators =
                        mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId,
                                allLocatorIds.stream().distinct().collect(Collectors.toList()));
                if (CollectionUtils.isNotEmpty(mtModLocators)) {
                    locatorMap =
                            mtModLocators.stream().collect(Collectors.toMap(MtModLocator::getLocatorId,
                                    t -> t));
                }
            }
            // 组装数据
            List<MtPfepInventoryVO4> list = Collections.synchronizedList(new
                    ArrayList<>());
            Map<String, MtMaterialVO> finalMaterialMap = materialMap;
            Map<String, String> finalMaterialIds = materialIds;
            Map<String, MtModSite> finalSiteMap = siteMap;
            Map<String, String> finalSiteIds = siteIds;
            Map<String, MtModArea> finalAreaMap = areaMap;
            Map<String, MtModProductionLine> finalProductionLineMap =
                    productionLineMap;
            Map<String, MtModWorkcell> finalWorkCellMap = workCellMap;
            Map<String, MtModLocator> finalLocatorMap = locatorMap;
            Map<String, String> finalUomCodes = uomCodes;
            base.parallelStream().forEachOrdered(t -> {
                MtPfepInventoryVO4 vo = new MtPfepInventoryVO4();
                vo.setKid(t.getPfepInventoryId());
                vo.setKeyType("material");
                vo.setMaterialSiteId(t.getMaterialSiteId());
                vo.setMaterialId(finalMaterialIds.get(t.getMaterialSiteId()));
                MtMaterialVO materialVO =
                        finalMaterialMap.get(finalMaterialIds.get(t.getMaterialSiteId()));
                if (materialVO != null) {
                    vo.setMaterialCode(materialVO.getMaterialCode());
                    vo.setMaterialName(materialVO.getMaterialName());
                }
                vo.setCategoryCode("");
                vo.setCategoryDesc("");
                vo.setCategorySetDesc("");
                vo.setSiteId(finalSiteIds.get(t.getMaterialSiteId()));
                MtModSite mtModSite =
                        finalSiteMap.get(finalSiteIds.get(t.getMaterialSiteId()));
                if (mtModSite != null) {
                    vo.setSiteCode(mtModSite.getSiteCode());
                    vo.setSiteName(mtModSite.getSiteName());
                }
                if ("AREA".equals(t.getOrganizationType())) {
                    vo.setAreaId(t.getOrganizationId());
                    MtModArea mtModArea =
                            finalAreaMap.get(t.getOrganizationId());
                    if (mtModArea != null) {
                        vo.setAreaCode(mtModArea.getAreaCode());
                        vo.setAreaName(mtModArea.getAreaName());
                    }
                }
                if ("PRODUCTIONLINE".equals(t.getOrganizationType())) {
                    vo.setProdLineId(t.getOrganizationId());
                    MtModProductionLine productionLine =
                            finalProductionLineMap.get(t.getOrganizationId());
                    if (productionLine != null) {
                        vo.setProdLineCode(productionLine.getProdLineCode());
                        vo.setProdLineName(productionLine.getProdLineName());
                    }
                }
                if ("WORKCELL".equals(t.getOrganizationType())) {
                    vo.setWorkcellId(t.getOrganizationId());
                    MtModWorkcell workcell =
                            finalWorkCellMap.get(t.getOrganizationId());
                    if (workcell != null) {
                        vo.setWorkcellCode(workcell.getWorkcellCode());
                        vo.setWorkcellName(workcell.getWorkcellName());
                    }
                }
                MtModLocator modLocator;
                if ("LOCATOR".equals(t.getOrganizationType())) {
                    vo.setLocatorId(t.getOrganizationId());
                    modLocator = finalLocatorMap.get(t.getOrganizationId());
                    if (modLocator != null) {
                        vo.setLocatorCode(modLocator.getLocatorCode());
                        vo.setLocatorName(modLocator.getLocatorName());
                    }
                }
                vo.setIdentifyType(t.getIdentifyType());
                vo.setIdentifyId(t.getIdentifyId());
                vo.setPackageLength(t.getPackageLength());
                vo.setPackageWidth(t.getPackageWidth());
                vo.setPackageHeight(t.getPackageHeight());
                vo.setPackageSizeUomId(t.getPackageSizeUomId());
                vo.setPackageSizeUomCode(finalUomCodes.get(t.getPackageSizeUomId()));
                vo.setPackageWeight(t.getPackageWeight());
                vo.setWeightUomId(t.getWeightUomId());
                vo.setWeightUomCode(finalUomCodes.get(t.getWeightUomId()));
                vo.setMaxStockQty(t.getMaxStockQty());
                vo.setMinStockQty(t.getMinStockQty());
                vo.setStockLocatorId(t.getStockLocatorId());
                modLocator = finalLocatorMap.get(t.getStockLocatorId());
                if (modLocator != null) {
                    vo.setStockLocatorCode(modLocator.getLocatorCode());
                    vo.setStockLocatorName(modLocator.getLocatorName());
                }
                vo.setIssuedLocatorId(t.getIssuedLocatorId());
                modLocator = finalLocatorMap.get(t.getIssuedLocatorId());
                if (modLocator != null) {
                    vo.setIssuedLocatorCode(modLocator.getLocatorCode());
                    vo.setIssuedLocatorName(modLocator.getLocatorName());
                }
                vo.setCompletionLocatorId(t.getCompletionLocatorId());
                modLocator = finalLocatorMap.get(t.getCompletionLocatorId());
                if (modLocator != null) {
                    vo.setCompletionLocatorCode(modLocator.getLocatorCode());
                    vo.setCompletionLocatorName(modLocator.getLocatorName());
                }
                vo.setEnableFlag(t.getEnableFlag());
                vo.setOrganizationType(t.getOrganizationType());
                vo.setOrganizationId(t.getOrganizationId());
                vo.setOrganizationCode(StringUtils.isEmpty(vo.getAreaCode()) ?
                        StringUtils.isEmpty(vo.getProdLineCode())
                                ? StringUtils.isEmpty(vo.getWorkcellCode()) ?
                                vo.getLocatorCode() : vo.getWorkcellCode()
                                : vo.getProdLineCode() : vo.getAreaCode());
                vo.setOrganizationDesc(StringUtils.isEmpty(vo.getAreaName()) ?
                        StringUtils.isEmpty(vo.getProdLineName())
                                ? StringUtils.isEmpty(vo.getWorkcellName()) ?
                                vo.getLocatorName() : vo.getWorkcellName()
                                : vo.getProdLineName() : vo.getAreaName());
                vo.setMtExtendAttrDTOList(mtExtendSettingsService.attrQuery(tenantId,
                        t.getPfepInventoryId(),
                        MT_PFEP_INVENTORY_ATTR));
                list.add(vo);
            });
            page.setContent(list);
        } else if (("category".equals(dto.getType()) &&
                StringUtils.isNotEmpty(dto.getKid()))
                || StringUtils.isNotEmpty(dto.getCategoryId())) {
            Page<MtPfepInventoryCategory> base = PageHelper.doPage(pageRequest,
                    () ->
                            categoryMapper.mtPfepInventoryCategoryDetialQuery(tenantId, dto));
            page.setTotalPages(base.getTotalPages());
            page.setTotalElements(base.getTotalElements());
            page.setNumberOfElements(base.getNumberOfElements());
            page.setSize(base.getSize());
            page.setNumber(base.getNumber());
            if (CollectionUtils.isEmpty(base)) {
                return page;
            }
// 并行流获取id列表
            List<String> packageSizeUomIds =
                    base.parallelStream().map(MtPfepInventoryCategory::getPackageSizeUomId)
                            .distinct().collect(Collectors.toList());
            List<String> weightUomIds =
                    base.parallelStream().map(MtPfepInventoryCategory::getWeightUomId).distinct(
                    )
                            .collect(Collectors.toList());
            List<String> stockLocatorIds =
                    base.parallelStream().map(MtPfepInventoryCategory::getStockLocatorId)
                            .distinct().collect(Collectors.toList());
            List<String> issuedLocatorId =
                    base.parallelStream().map(MtPfepInventoryCategory::getIssuedLocatorId)
                            .distinct().collect(Collectors.toList());
            List<String> completionLocatorIds =
                    base.parallelStream().map(MtPfepInventoryCategory::getCompletionLocatorId).
                            distinct()
                            .collect(Collectors.toList());
            List<String> areaIds = base.parallelStream().filter(t ->
                    "AREA".equals(t.getOrganizationType()))
                    .map(MtPfepInventoryCategory::getOrganizationId).distinct().collect(Collectors.toList());
            List<String> productionLineIds = base.parallelStream()
                    .filter(t ->
                            "PRODUCTIONLINE".equals(t.getOrganizationType()))
                    .map(MtPfepInventoryCategory::getOrganizationId).distinct().collect(Collectors.toList());
            List<String> workcellIds = base.parallelStream().filter(t ->
                    "WORKCELL".equals(t.getOrganizationType()))
                    .map(MtPfepInventoryCategory::getOrganizationId).distinct().collect(Collectors.toList());
            List<String> locatorIds = base.parallelStream().filter(t ->
                    "LOCATOR".equals(t.getOrganizationType()))
                    .map(MtPfepInventoryCategory::getOrganizationId).distinct().collect(Collectors.toList());
            List<String> allUomIds = new ArrayList<>();
            allUomIds.addAll(packageSizeUomIds);
            allUomIds.addAll(weightUomIds);
            List<String> allLocatorIds = new ArrayList<>();
            allLocatorIds.addAll(stockLocatorIds);
            allLocatorIds.addAll(issuedLocatorId);
            allLocatorIds.addAll(completionLocatorIds);
            allLocatorIds.addAll(locatorIds);
// 根据物料站点id获取物料Id和站点id
            List<String> materialSiteIds =
                    base.parallelStream().map(MtPfepInventoryCategory::getMaterialCategorySiteId
                    )
                            .distinct().collect(Collectors.toList());
            List<MtMaterialCategorySite> mtMaterialSites =
                    mtMaterialCategorySiteRepository.selectByMaterialCategorySiteIds(tenantId,
                            materialSiteIds);
            Map<String, String> materialIds = new HashMap<>();
            Map<String, String> siteIds = new HashMap<>();
            if (CollectionUtils.isNotEmpty(mtMaterialSites)) {
                materialIds = mtMaterialSites.stream()
                        .collect(Collectors.toMap(MtMaterialCategorySite::getMaterialCategorySiteId,
                                MtMaterialCategorySite::getMaterialCategoryId));
                siteIds = mtMaterialSites.stream().collect(Collectors.toMap(
                        MtMaterialCategorySite::getMaterialCategorySiteId,
                        MtMaterialCategorySite::getSiteId));
            }
// 获取物料类别信息
            Map<String, MtMaterialCategory> materialMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(materialIds.values())) {
                List<MtMaterialCategory> materialCategory =
                        mtMaterialCategoryRepository
                                .materialCategoryPropertyBatchGet(tenantId, new
                                        ArrayList<>(materialIds.values()));
                if (CollectionUtils.isNotEmpty(materialCategory)) {
                    materialMap = materialCategory.stream()
                            .collect(Collectors.toMap(MtMaterialCategory::getMaterialCategoryId, t ->
                                    t));
                }
            }
// 获取站点信息
            Map<String, MtModSite> siteMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(siteIds.values())) {
                List<MtModSite> mtModSites =
                        mtModSiteRepository.siteBasicPropertyBatchGet(tenantId,
                                new ArrayList<>(siteIds.values()));
                if (CollectionUtils.isNotEmpty(mtModSites)) {
                    siteMap =
                            mtModSites.stream().collect(Collectors.toMap(MtModSite::getSiteId, t -> t));
                }
            }
// 获取单位信息
            Map<String, String> uomCodes = new HashMap<>();
            if (CollectionUtils.isNotEmpty(allUomIds)) {
                List<MtUomVO> mtUomVOS =
                        mtUomRepository.uomPropertyBatchGet(tenantId, allUomIds);
                if (CollectionUtils.isNotEmpty(mtUomVOS)) {
                    uomCodes =
                            mtUomVOS.stream().collect(Collectors.toMap(MtUomVO::getUomId,
                                    MtUomVO::getUomCode));
                }
            }
// 获取区域信息
            Map<String, MtModArea> areaMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(areaIds)) {
                List<MtModArea> mtModAreas =
                        mtModAreaRepository.areaBasicPropertyBatchGet(tenantId, areaIds);
                if (CollectionUtils.isNotEmpty(mtModAreas)) {
                    areaMap =
                            mtModAreas.stream().collect(Collectors.toMap(MtModArea::getAreaId, t -> t));
                }
            }
// 获取生产线信息
            Map<String, MtModProductionLine> productionLineMap = new HashMap<>
                    ();
            if (CollectionUtils.isNotEmpty(productionLineIds)) {
                List<MtModProductionLine> mtModProductionLines =
                        mtModProductionLineRepository
                                .prodLineBasicPropertyBatchGet(tenantId,
                                        productionLineIds);
                if (CollectionUtils.isNotEmpty(mtModProductionLines)) {
                    productionLineMap = mtModProductionLines.stream()
                            .collect(Collectors.toMap(MtModProductionLine::getProdLineId, t -> t));
                }
            }
// 获取工作单元信息
            Map<String, MtModWorkcell> workCellMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(workcellIds)) {
                List<MtModWorkcell> mtModWorkcells =
                        mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId,
                                workcellIds);
                if (CollectionUtils.isNotEmpty(mtModWorkcells)) {
                    workCellMap = mtModWorkcells.stream()
                            .collect(Collectors.toMap(MtModWorkcell::getWorkcellId, t -> t));
                }
            }
// 获取库位信息
            Map<String, MtModLocator> locatorMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(allLocatorIds)) {
                List<MtModLocator> mtModLocators =
                        mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId,
                                allLocatorIds.stream().distinct().collect(Collectors.toList()));
                if (CollectionUtils.isNotEmpty(mtModLocators)) {
                    locatorMap =
                            mtModLocators.stream().collect(Collectors.toMap(MtModLocator::getLocatorId,
                                    t -> t));
                }
            }
            List<String> materialCategorySetIds = materialMap.values().stream()
                    .map(MtMaterialCategory::getMaterialCategorySetId).collect(Collectors.toList
                            ());
            List<MtMaterialCategorySet> mtMaterialCategorySets =
                    mtMaterialCategorySetRepository
                            .materialCategorySetPropertyBatchGet(tenantId,
                                    materialCategorySetIds);
// 组装MtMaterialCategorySetDesc
            Map<String, String> materialCategorySetDescs = new HashMap<>();
            if (CollectionUtils.isNotEmpty(mtMaterialCategorySets)) {
                materialCategorySetDescs = mtMaterialCategorySets.stream()
                        .collect(Collectors.toMap(MtMaterialCategorySet::getMaterialCategorySetId,
                                MtMaterialCategorySet::getDescription));
            }
// 组装数据
            List<MtPfepInventoryVO4> list = Collections.synchronizedList(new
                    ArrayList<>());
            Map<String, MtMaterialCategory> finalMaterialMap = materialMap;
            Map<String, String> finalMaterialIds = materialIds;
            Map<String, MtModSite> finalSiteMap = siteMap;
            Map<String, String> finalSiteIds = siteIds;
            Map<String, MtModArea> finalAreaMap = areaMap;
            Map<String, MtModProductionLine> finalProductionLineMap =
                    productionLineMap;
            Map<String, MtModWorkcell> finalWorkCellMap = workCellMap;
            Map<String, MtModLocator> finalLocatorMap = locatorMap;
            Map<String, String> finalUomCodes = uomCodes;
            Map<String, String> finalMaterialCategorySetDescs =
                    materialCategorySetDescs;
            base.parallelStream().forEachOrdered(t -> {
                MtPfepInventoryVO4 vo = new MtPfepInventoryVO4();
                vo.setKid(t.getPfepInventoryCategoryId());
                vo.setKeyType("category");
                vo.setMaterialCategorySiteId(t.getMaterialCategorySiteId());
                vo.setMaterialCategoryId(finalMaterialIds.get(t.getMaterialCategorySiteId()
                ));
                MtMaterialCategory mtMaterialCategory =
                        finalMaterialMap.get(finalMaterialIds.get(t.getMaterialCategorySiteId()));
                if (mtMaterialCategory != null) {
                    vo.setCategoryCode(mtMaterialCategory.getCategoryCode());
                    vo.setCategoryDesc(mtMaterialCategory.getDescription());
                    vo.setCategorySetDesc(
                            finalMaterialCategorySetDescs.get(mtMaterialCategory.getMaterialCategorySetId()));
                }
                vo.setSiteId(finalSiteIds.get(t.getMaterialCategorySiteId()));
                MtModSite mtModSite =
                        finalSiteMap.get(finalSiteIds.get(t.getMaterialCategorySiteId()));
                if (mtModSite != null) {
                    vo.setSiteCode(mtModSite.getSiteCode());
                    vo.setSiteName(mtModSite.getSiteName());
                }
                if ("AREA".equals(t.getOrganizationType())) {
                    vo.setAreaId(t.getOrganizationId());
                    MtModArea mtModArea =
                            finalAreaMap.get(t.getOrganizationId());
                    if (mtModArea != null) {
                        vo.setAreaCode(mtModArea.getAreaCode());
                        vo.setAreaName(mtModArea.getAreaName());
                    }
                }
                if ("PRODUCTIONLINE".equals(t.getOrganizationType())) {
                    vo.setProdLineId(t.getOrganizationId());
                    MtModProductionLine productionLine =
                            finalProductionLineMap.get(t.getOrganizationId());
                    if (productionLine != null) {
                        vo.setProdLineCode(productionLine.getProdLineCode());
                        vo.setProdLineName(productionLine.getProdLineName());
                    }
                }
                if ("WORKCELL".equals(t.getOrganizationType())) {
                    vo.setWorkcellId(t.getOrganizationId());
                    MtModWorkcell workcell =
                            finalWorkCellMap.get(t.getOrganizationId());
                    if (workcell != null) {
                        vo.setWorkcellCode(workcell.getWorkcellCode());
                        vo.setWorkcellName(workcell.getWorkcellName());
                    }
                }
                MtModLocator modLocator;
                if ("LOCATOR".equals(t.getOrganizationType())) {
                    vo.setLocatorId(t.getOrganizationId());
                    modLocator = finalLocatorMap.get(t.getOrganizationId());
                    if (modLocator != null) {
                        vo.setLocatorCode(modLocator.getLocatorCode());
                        vo.setLocatorName(modLocator.getLocatorName());
                    }
                }
                vo.setIdentifyType(t.getIdentifyType());
                vo.setIdentifyId(t.getIdentifyId());
                vo.setPackageLength(t.getPackageLength());
                vo.setPackageWidth(t.getPackageWidth());
                vo.setPackageHeight(t.getPackageHeight());
                vo.setPackageSizeUomId(t.getPackageSizeUomId());
                vo.setPackageSizeUomCode(finalUomCodes.get(t.getPackageSizeUomId()));
                vo.setPackageWeight(t.getPackageWeight());
                vo.setWeightUomId(t.getWeightUomId());
                vo.setWeightUomCode(finalUomCodes.get(t.getWeightUomId()));
                vo.setMaxStockQty(t.getMaxStockQty());
                vo.setMinStockQty(t.getMinStockQty());
                vo.setStockLocatorId(t.getStockLocatorId());
                modLocator = finalLocatorMap.get(t.getStockLocatorId());
                if (modLocator != null) {
                    vo.setStockLocatorCode(modLocator.getLocatorCode());
                    vo.setStockLocatorName(modLocator.getLocatorName());
                }
                vo.setIssuedLocatorId(t.getIssuedLocatorId());
                modLocator = finalLocatorMap.get(t.getIssuedLocatorId());
                if (modLocator != null) {
                    vo.setIssuedLocatorCode(modLocator.getLocatorCode());
                    vo.setIssuedLocatorName(modLocator.getLocatorName());
                }
                vo.setCompletionLocatorId(t.getCompletionLocatorId());
                modLocator = finalLocatorMap.get(t.getCompletionLocatorId());
                if (modLocator != null) {
                    vo.setCompletionLocatorCode(modLocator.getLocatorCode());
                    vo.setCompletionLocatorName(modLocator.getLocatorName());
                }
                vo.setEnableFlag(t.getEnableFlag());
                vo.setOrganizationType(t.getOrganizationType());
                vo.setOrganizationId(t.getOrganizationId());
                vo.setOrganizationCode(StringUtils.isEmpty(vo.getAreaCode()) ?
                        StringUtils.isEmpty(vo.getProdLineCode())
                                ? StringUtils.isEmpty(vo.getWorkcellCode()) ?
                                vo.getLocatorCode() : vo.getWorkcellCode()
                                : vo.getProdLineCode() : vo.getAreaCode());
                vo.setOrganizationDesc(StringUtils.isEmpty(vo.getAreaName()) ?
                        StringUtils.isEmpty(vo.getProdLineName())
                                ? StringUtils.isEmpty(vo.getWorkcellName()) ?
                                vo.getLocatorName() : vo.getWorkcellName()
                                : vo.getProdLineName() : vo.getAreaName());
                vo.setMtExtendAttrDTOList(mtExtendSettingsService.attrQuery(tenantId,
                        t.getPfepInventoryCategoryId(),
                        MT_PFEP_INVENTORY_ATTR));
                list.add(vo);
            });
            page.setContent(list);
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtPfepInventoryDTO4 pfepInventorySave(Long tenantId, MtPfepInventoryDTO2 dto) {
        MtPfepInventoryDTO4 dto4 = new MtPfepInventoryDTO4();
        if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getMaterialCategoryId())) {
            throw new MtException("MT_MATERIAL_0086",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0086", "MATERIAL"));
        } else if (StringUtils.isNotEmpty(dto.getMaterialId()) && StringUtils.isNotEmpty(dto.getMaterialCategoryId())) {
            throw new MtException("MT_MATERIAL_0084",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0084", "MATERIAL"));
        } else if (StringUtils.isNotEmpty(dto.getMaterialId())) {
            // 基础属性赋值
            MtPfepInventory mtPfepInventory = new MtPfepInventory();
            BeanUtils.copyProperties(dto, mtPfepInventory);
            mtPfepInventory.setTenantId(tenantId);
            // 获取站点关系ID
            MtMaterialSite site = new MtMaterialSite();
            site.setMaterialId(dto.getMaterialId());
            site.setSiteId(dto.getSiteId());
            site.setTenantId(tenantId);
            String materialSiteId = mtMaterialSiteService.materialSiteLimitRelationGet(tenantId, site);
            if (StringUtils.isEmpty(materialSiteId)) {
                // 目标物料与站点关系不存在，请确认
                throw new MtException("MT_BOM_0022", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0022", "BOM", "API【saveMtPfepInventory】"));
            }
            mtPfepInventory.setMaterialSiteId(materialSiteId);
            mtPfepInventory.setPfepInventoryId(dto.getKid());
            // 校验唯一性
            Criteria criteria = new Criteria(mtPfepInventory);
            List<WhereField> whereFields2 = new ArrayList<WhereField>();
            if (StringUtils.isNotEmpty(mtPfepInventory.getPfepInventoryId())) {
                whereFields2.add(new WhereField(MtPfepInventory.FIELD_PFEP_INVENTORY_ID, Comparison.NOT_EQUAL));
            }
            whereFields2.add(new WhereField(MtPfepInventory.FIELD_TENANT_ID, Comparison.EQUAL));
            whereFields2.add(new WhereField(MtPfepInventory.FIELD_MATERIAL_SITE_ID, Comparison.EQUAL));
            whereFields2.add(new WhereField(MtPfepInventory.FIELD_ORGANIZATION_ID, Comparison.EQUAL));
            whereFields2.add(new WhereField(MtPfepInventory.FIELD_ORGANIZATION_TYPE, Comparison.EQUAL));

            criteria.where(whereFields2.toArray(new WhereField[whereFields2.size()]));
            if (mtPfepInventoryMapper.selectOptional(mtPfepInventory, criteria).size() > 0) {
                throw new MtException("MT_MATERIAL_0061",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0061", "MATERIAL"));
            }
            if (StringUtils.isEmpty(mtPfepInventory.getPfepInventoryId())) {
                this.insertSelective(mtPfepInventory);
            } else {
                this.updateByPrimaryKey(mtPfepInventory);
            }
            // 写入扩展字段
            if (CollectionUtils.isNotEmpty(dto.getMtPfepInventoryAttrs())) {
                mtExtendSettingsService.attrSave(tenantId, MT_PFEP_INVENTORY_ATTR, mtPfepInventory.getPfepInventoryId(),
                                null, dto.getMtPfepInventoryAttrs());
            }
            dto4.setKid(mtPfepInventory.getPfepInventoryId());
            dto4.setType("material");
        } else if (StringUtils.isNotEmpty(dto.getMaterialCategoryId())) {
            dto4 = categoryService.pfepInventoryCategoryUpdate(tenantId, dto);
        }
        return dto4;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtPfepInventoryDTO4 copyMtPfepInventory(Long tenantId, MtPfepInventoryDTO3 dto) {
        // 获取站点关系
        MtMaterialSite mtMaterialSite = new MtMaterialSite();
        mtMaterialSite.setTenantId(tenantId);
        mtMaterialSite.setMaterialId(dto.getSourceMaterialId());
        mtMaterialSite.setSiteId(dto.getSourceSiteId());
        mtMaterialSite = mtMaterialSiteMapper.selectOne(mtMaterialSite);

        if (mtMaterialSite == null) {
            // 目标物料与站点关系不存在，请确认
            throw new MtException("MT_BOM_0022", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_BOM_0022",
                            "BOM", "API【saveMtPfepInventory】"));
        }
        // 获取来源属性
        MtPfepInventory pfepInventory = new MtPfepInventory();
        pfepInventory.setMaterialSiteId(mtMaterialSite.getMaterialSiteId());
        pfepInventory.setOrganizationId(dto.getSourceOrgId());
        pfepInventory.setOrganizationType(dto.getSourceOrgType());
        pfepInventory.setTenantId(tenantId);

        // 目标属性赋值
        MtPfepInventory targetPfep = mtPfepInventoryMapper.selectOne(pfepInventory);
        if (targetPfep == null) {
            throw new MtException("MT_MATERIAL_0085",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0085", "MATERIAL"));
        }
        MtPfepInventoryDTO2 dto2 = new MtPfepInventoryDTO2();
        BeanUtils.copyProperties(targetPfep, dto2);
        dto2 = setPfepInventorySaveParam(tenantId, dto, dto2, targetPfep.getPfepInventoryId());

        // 保存
        return pfepInventorySave(tenantId, dto2);
    }


    @Override
    public MtPfepInventoryDTO2 setPfepInventorySaveParam(Long tenantId, MtPfepInventoryDTO3 dto,
                                                         MtPfepInventoryDTO2 dto2, String kid) {
        dto2.setMaterialId(dto.getTargetMaterialId());
        dto2.setSiteId(dto.getTargetSiteId());
        dto2.setOrganizationId(dto.getTargetOrgId());
        dto2.setOrganizationType(dto.getTargetOrgType());
        dto2.setMaterialCategoryId(dto.getTargetMaterialCategoryId());

        // 获取来源扩展属性
        List<MtExtendAttrDTO> list = mtExtendSettingsService.attrQuery(tenantId, kid,
                        StringUtils.isEmpty(dto.getSourceMaterialCategoryId()) ? MT_PFEP_INVENTORY_ATTR
                                        : MT_PFEP_INVENTORY_CATEGORY_ATTR);
        List<MtExtendAttrDTO3> extendList = new ArrayList<>();
        list.stream().forEach(e -> {
            // 写入扩展字段
            MtExtendAttrDTO3 dto3 = new MtExtendAttrDTO3();
            dto3.setAttrName(e.getAttrName());
            dto3.setAttrValue(e.getAttrValue());
            extendList.add(dto3);
        });
        dto2.setMtPfepInventoryAttrs(extendList);
        return dto2;
    }
}
