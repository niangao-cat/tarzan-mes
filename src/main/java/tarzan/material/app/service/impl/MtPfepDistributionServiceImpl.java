package tarzan.material.app.service.impl;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.material.api.dto.MtPfepDistributionAllDTO1;
import tarzan.material.api.dto.MtPfepDistributionDTO1;
import tarzan.material.api.dto.MtPfepDistributionDTO2;
import tarzan.material.api.dto.MtPfepDistributionDTO3;
import tarzan.material.api.dto.MtPfepDistributionDTO4;
import tarzan.material.api.dto.MtPfepDistributionDTO5;
import tarzan.material.api.dto.MtPfepDistributionDTO6;
import tarzan.material.api.dto.MtPfepDistributionDTO7;
import tarzan.material.app.service.MtPfepDistributionService;
import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.entity.MtMaterialCategorySite;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtPfepDistribution;
import tarzan.material.domain.entity.MtPfepDistributionCategory;
import tarzan.material.domain.repository.MtMaterialCategoryRepository;
import tarzan.material.domain.repository.MtMaterialCategorySiteRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtPfepDistributionCategoryRepository;
import tarzan.material.domain.repository.MtPfepDistributionRepository;
import tarzan.material.domain.vo.MtMaterialVO4;
import tarzan.material.domain.vo.MtMaterialVO5;
import tarzan.material.domain.vo.MtPfepDistributionVO;
import tarzan.material.domain.vo.MtPfepInventoryCategoryVO2;
import tarzan.material.infra.mapper.MtPfepDistributionCategoryMapper;
import tarzan.material.infra.mapper.MtPfepDistributionMapper;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

/**
 * 物料配送属性应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Service
public class MtPfepDistributionServiceImpl implements MtPfepDistributionService {

    private Logger logger = LoggerFactory.getLogger(MtPfepDistributionServiceImpl.class);

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtPfepDistributionRepository mtPfepDistributionRepository;

    @Autowired
    private MtPfepDistributionCategoryRepository mtPfepDistributionCategoryRepository;

    @Autowired
    private MtPfepDistributionMapper mtPfepDistributionMapper;

    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialCategorySiteRepository mtMaterialCategorySiteRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtMaterialCategoryRepository mtMaterialCategoryRepository;

    @Autowired
    private MtModAreaRepository mtModAreaRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtPfepDistributionCategoryMapper mtPfepDistributionCategoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Page<MtPfepDistributionDTO4> materialPfepDistributionQueryForUi(Long tenantId, MtPfepDistributionDTO1 dto,
                                                                           PageRequest pageRequest) {

        String materialCategorySiteId = null;
        String materialSiteId = null;

        // // 2.1 数据准备 获取物料工厂关系ID
        // if (StringUtils.isNotEmpty(dto.getMaterialId()) && StringUtils.isNotEmpty(dto.getSiteId())) {
        // // 输入则调用API{ materialSiteLimitRelationshipGet }，获取物料站点关系materialSiteId
        // MtMaterialSite mtMaterialSite = new MtMaterialSite();
        // mtMaterialSite.setSiteId(dto.getSiteId());
        // mtMaterialSite.setMaterialId(dto.getMaterialId());
        // materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId, mtMaterialSite);
        // if (StringUtils.isEmpty(materialSiteId)) {
        // throw new MtException("MT_MATERIAL_0055",
        // mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
        // "MATERIAL", "materialSiteId", "【API:materialDistributionPfepQuery】"));
        // }
        // }

        // // 2.2 数据准备 获取物料分类工厂关系ID
        // if (StringUtils.isNotEmpty(dto.getMaterialCategoryId()) &&
        // StringUtils.isNotEmpty(dto.getSiteId())) {
        // // 输入则调用API{ materialCategorySiteLimitRelationshipGet }，获取物料站点关系materialCategorySiteId
        // MtMaterialCategorySite site = new MtMaterialCategorySite();
        // site.setTenantId(tenantId);
        // site.setMaterialCategoryId(dto.getMaterialCategoryId());
        // site.setSiteId(dto.getSiteId());
        // materialCategorySiteId =
        // mtMaterialCategorySiteRepository.materialCategorySiteLimitRelationGet(tenantId, site);
        // if (StringUtils.isEmpty(materialCategorySiteId)) {
        // throw new MtException("MT_MATERIAL_0055",
        // mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
        // "MATERIAL", "materialCategorySiteId",
        // "【API:materialCategoryDistributionPfepQuery】"));
        // }
        //
        // }

        // 3.1 数据处理 先查询出基础数据 按分页查询，这样后续循环查询部一些编码的时候循环次数会少 所以速度可能更快
        MtPfepDistributionAllDTO1 mtPfepDistributionAllDTO1 = new MtPfepDistributionAllDTO1();
        mtPfepDistributionAllDTO1.setAreaId(dto.getAreaId());
        mtPfepDistributionAllDTO1.setLocatorId(dto.getLocatorId());
        mtPfepDistributionAllDTO1.setAreaLocatorId(dto.getAreaLocatorId());
        mtPfepDistributionAllDTO1.setEnableFlag(dto.getEnableFlag());
        mtPfepDistributionAllDTO1.setOrganizationId(dto.getWorkcellId());
        mtPfepDistributionAllDTO1.setSiteId(dto.getSiteId());
        mtPfepDistributionAllDTO1.setMaterialCategoryId(dto.getMaterialCategoryId());
        mtPfepDistributionAllDTO1.setMaterialId(dto.getMaterialId());

        if (StringUtils.isNotEmpty(dto.getWorkcellId())) {
            mtPfepDistributionAllDTO1.setOrganizationType("WORKCELL");
        } else {
            mtPfepDistributionAllDTO1.setOrganizationType(null);
        }

        Page<MtPfepDistributionAllDTO1> mtPfepDistributionAllDTO1Page =
                PageHelper.doPage(pageRequest, () -> mtPfepDistributionMapper
                        .materialDistributionPfepAllQuery(tenantId, mtPfepDistributionAllDTO1));


        if (CollectionUtils.isEmpty(mtPfepDistributionAllDTO1Page)) {
            logger.info("material category is empty");
            return new Page<MtPfepDistributionDTO4>();
        }

        // 3.2 数据处理 查询批量查找的一些数据 组织数据
        // 3.2.1 工作单元
        List<String> organizationIds = mtPfepDistributionAllDTO1Page.getContent().stream()
                .map(MtPfepDistributionAllDTO1::getOrganizationId).filter(StringUtils::isNotEmpty).distinct()
                .collect(toList());
        Map<String, MtModWorkcell> mtModWorkcellMap = new HashMap<>(0);

        if (CollectionUtils.isNotEmpty(organizationIds)) {
            List<MtModWorkcell> mtModWorkcellList =
                    mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, organizationIds);

            if (CollectionUtils.isNotEmpty(mtModWorkcellList)) {
                mtModWorkcellMap = mtModWorkcellList.stream()
                        .collect(Collectors.toMap(MtModWorkcell::getWorkcellId, t -> t));
            }
        }

        // 3.2.2 区域
        List<String> areaIds =
                mtPfepDistributionAllDTO1Page.getContent().stream().map(MtPfepDistributionAllDTO1::getAreaId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(toList());
        Map<String, MtModArea> mtModAreaMap = new HashMap<>(0);

        if (CollectionUtils.isNotEmpty(areaIds)) {
            List<MtModArea> mtModAreaList = mtModAreaRepository.areaBasicPropertyBatchGet(tenantId, areaIds);

            if (CollectionUtils.isNotEmpty(mtModAreaList)) {
                mtModAreaMap = mtModAreaList.stream().collect(Collectors.toMap(MtModArea::getAreaId, t -> t));
            }
        }

        // 3.2.3 线边库位
        List<String> locatorIds =
                mtPfepDistributionAllDTO1Page.getContent().stream().map(MtPfepDistributionAllDTO1::getLocatorId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(toList());
        Map<String, MtModLocator> mtModLocatorMap = new HashMap<>(0);

        if (CollectionUtils.isNotEmpty(locatorIds)) {
            List<MtModLocator> mtModLocatorList =
                    mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, locatorIds);

            if (CollectionUtils.isNotEmpty(mtModLocatorList)) {
                mtModLocatorMap =
                        mtModLocatorList.stream().collect(Collectors.toMap(MtModLocator::getLocatorId, t -> t));
            }

        }

        // 3.2.4 配送库位
        List<String> areaLocatorIds = mtPfepDistributionAllDTO1Page.getContent().stream()
                .map(MtPfepDistributionAllDTO1::getAreaLocatorId).filter(StringUtils::isNotEmpty).distinct()
                .collect(toList());
        Map<String, MtModLocator> mtModAreaLocatorMap = new HashMap<>(0);

        if (CollectionUtils.isNotEmpty(locatorIds)) {
            List<MtModLocator> mtModLocatorList =
                    mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, locatorIds);

            if (CollectionUtils.isNotEmpty(mtModLocatorList)) {
                mtModAreaLocatorMap =
                        mtModLocatorList.stream().collect(Collectors.toMap(MtModLocator::getLocatorId, t -> t));
            }

        }

        // 3.2 数据处理 补充返回数据
        List<MtPfepDistributionDTO4> list = new ArrayList<>();
        for (MtPfepDistributionAllDTO1 mtPfepDistributionAllDTO11 : mtPfepDistributionAllDTO1Page) {

            MtPfepDistributionDTO4 mtPfepDistributionDTO4 = new MtPfepDistributionDTO4();

            if (StringUtils.isNotEmpty(mtPfepDistributionAllDTO11.getMaterialSiteId())
                    && StringUtils.isNotEmpty(mtPfepDistributionAllDTO11.getMaterialId())
                    && StringUtils.isNotEmpty(mtPfepDistributionAllDTO11.getSiteId())) {
                // 3.2.1 根据 物料和组织关系id 找到物料ID 和 组织ID
                mtPfepDistributionDTO4.setMaterialId(mtPfepDistributionAllDTO11.getMaterialId());

                MtMaterialVO4 mtMaterialVO4 = new MtMaterialVO4();
                mtMaterialVO4.setMaterialId(mtPfepDistributionAllDTO11.getMaterialId());
                List<MtMaterialVO5> mtMaterialVO5List =
                        mtMaterialRepository.propertyLimitMaterialPropertyQuery(tenantId, mtMaterialVO4);

                if (CollectionUtils.isNotEmpty(mtMaterialVO5List) && mtMaterialVO5List.size() > 0) {
                    mtPfepDistributionDTO4.setMaterialCode(mtMaterialVO5List.get(0).getMaterialCode());
                    mtPfepDistributionDTO4.setMaterialName(mtMaterialVO5List.get(0).getMaterialName());
                } else {
                    logger.error("Item does not exist");
                }

            } else if (StringUtils.isNotEmpty(mtPfepDistributionAllDTO11.getMaterialCategorySiteId())
                    && StringUtils.isNotEmpty(mtPfepDistributionAllDTO11.getMaterialCategoryId())) {
                // 3.2.2 根据 物料类别和组织关系id 找到物料类别ID 和 组织ID
                mtPfepDistributionDTO4.setMaterialCategoryId(mtPfepDistributionAllDTO11.getMaterialCategoryId());

                MtMaterialCategory mtMaterialCategory = mtMaterialCategoryRepository.materialCategoryGet(tenantId,
                        mtPfepDistributionAllDTO11.getMaterialCategoryId());

                if (mtMaterialCategory != null) {
                    mtPfepDistributionDTO4.setMaterialCategoryCode(mtMaterialCategory.getCategoryCode());
                } else {
                    logger.error("Item Category does not exist");
                }

            }

            // ------------------------------------------------------------------------------------------------------
            // 3.2.3 工作单元 获取工作单元
            mtPfepDistributionDTO4.setOrganizationId(mtPfepDistributionAllDTO11.getOrganizationId());
            if (StringUtils.isEmpty(mtPfepDistributionAllDTO11.getOrganizationId())) {
                mtPfepDistributionDTO4.setOrganizationCode(null);
            } else {
                if (mtModWorkcellMap.containsKey(mtPfepDistributionAllDTO11.getOrganizationId())) {
                    mtPfepDistributionDTO4.setOrganizationCode(mtModWorkcellMap
                            .get(mtPfepDistributionAllDTO11.getOrganizationId()).getWorkcellCode());
                } else {
                    logger.error("Mod Enterprise does not exist");
                }
            }

            // 3.2.4 配送路线 获取区域编码 怎么获取？`
            mtPfepDistributionDTO4.setAreaId(mtPfepDistributionAllDTO11.getAreaId());
            if (StringUtils.isEmpty(mtPfepDistributionAllDTO11.getAreaId())) {
                mtPfepDistributionDTO4.setAreaCode(null);
            } else {
                if (mtModAreaMap.containsKey(mtPfepDistributionAllDTO11.getAreaId())) {
                    mtPfepDistributionDTO4.setAreaCode(
                            mtModAreaMap.get(mtPfepDistributionAllDTO11.getAreaId()).getAreaCode());
                } else {
                    logger.error("Area does not exist");
                }
            }

            // 3.2.5 线边库位 怎么获取？
            mtPfepDistributionDTO4.setLocatorId(mtPfepDistributionAllDTO11.getLocatorId());
            if (StringUtils.isEmpty(mtPfepDistributionAllDTO11.getLocatorId())) {
                mtPfepDistributionDTO4.setLocatorCode(null);
            } else {
                if (mtModLocatorMap.containsKey(mtPfepDistributionAllDTO11.getLocatorId())) {
                    mtPfepDistributionDTO4.setLocatorCode(
                            mtModLocatorMap.get(mtPfepDistributionAllDTO11.getLocatorId()).getLocatorCode());
                } else {
                    logger.error("Locator does not exist");
                }
            }

            // 3.2.6 配送库位 怎么获取？
            mtPfepDistributionDTO4.setAreaLocatorId(mtPfepDistributionAllDTO11.getAreaLocatorId());
            if (StringUtils.isEmpty(mtPfepDistributionAllDTO11.getAreaLocatorId())) {
                mtPfepDistributionDTO4.setAreaLocatorCode(null);
            } else {
                if (mtModAreaLocatorMap.containsKey(mtPfepDistributionAllDTO11.getAreaLocatorId())) {
                    mtPfepDistributionDTO4.setAreaLocatorCode(mtModAreaLocatorMap
                            .get(mtPfepDistributionAllDTO11.getAreaLocatorId()).getLocatorCode());
                } else {
                    logger.error("Locator does not exist");
                }
            }

            // 3.2.7 其他
            mtPfepDistributionDTO4.setMaterialConsumeRate(mtPfepDistributionAllDTO11.getMaterialConsumeRate());
            mtPfepDistributionDTO4.setFromScheduleRateFlag(mtPfepDistributionAllDTO11.getFromScheduleRateFlag());
            mtPfepDistributionDTO4.setBufferInventory(mtPfepDistributionAllDTO11.getBufferInventory());
            mtPfepDistributionDTO4.setBufferPeriod(mtPfepDistributionAllDTO11.getBufferPeriod());
            mtPfepDistributionDTO4.setMinInventory(mtPfepDistributionAllDTO11.getMinInventory());
            mtPfepDistributionDTO4.setMaxInventory(mtPfepDistributionAllDTO11.getMaxInventory());
            mtPfepDistributionDTO4.setPackQty(mtPfepDistributionAllDTO11.getPackQty());
            mtPfepDistributionDTO4.setMultiplesOfPackFlag(mtPfepDistributionAllDTO11.getMultiplesOfPackFlag());
            mtPfepDistributionDTO4.setEnableFlag(mtPfepDistributionAllDTO11.getEnableFlag());
            mtPfepDistributionDTO4.setPfepDistributionId(mtPfepDistributionAllDTO11.getPfepDistributionId());


            list.add(mtPfepDistributionDTO4);
        }

        // 3.2 数据处理 添加数据
        Page<MtPfepDistributionDTO4> page = new Page<MtPfepDistributionDTO4>();
        page.setContent(list);

        page.setTotalPages(mtPfepDistributionAllDTO1Page.getTotalPages());
        page.setTotalElements(mtPfepDistributionAllDTO1Page.getTotalElements());
        page.setNumberOfElements(mtPfepDistributionAllDTO1Page.getNumberOfElements());
        page.setSize(mtPfepDistributionAllDTO1Page.getSize());
        page.setNumber(mtPfepDistributionAllDTO1Page.getNumber());


        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String materialPfepDistributionSaveForUi(Long tenantId, MtPfepDistributionDTO2 dto) {

        if (dto == null) {
            return null;
        }

        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_0017", "MATERIAL", "siteId", "【API:materialPfepDistributionSaveForUi】"));
        }

        if (StringUtils.isNotEmpty(dto.getMaterialCategoryId())) {
            // 分类集不为空
            MtPfepInventoryCategoryVO2 mtPfepInventoryCategoryVO2 = new MtPfepInventoryCategoryVO2();

            mtPfepInventoryCategoryVO2.setMaterialCategorySiteId(null);
            // 输入则调用API{ materialCategorySiteLimitRelationshipGet }，获取物料站点关系materialCategorySiteId
            MtMaterialCategorySite site = new MtMaterialCategorySite();
            site.setTenantId(tenantId);
            site.setMaterialCategoryId(dto.getMaterialCategoryId());
            site.setSiteId(dto.getSiteId());
            mtPfepInventoryCategoryVO2.setMaterialCategorySiteId(
                    mtMaterialCategorySiteRepository.materialCategorySiteLimitRelationGet(tenantId, site));
            if (StringUtils.isEmpty(mtPfepInventoryCategoryVO2.getMaterialCategorySiteId())) {
                throw new MtException("MT_MATERIAL_0055",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                "MATERIAL", "materialCategorySiteId",
                                "【API:materialCategoryDistributionPfepQuery】"));
            }

            mtPfepInventoryCategoryVO2.setAreaId(dto.getAreaId());
            mtPfepInventoryCategoryVO2.setLocatorId(dto.getLocatorId());
            mtPfepInventoryCategoryVO2.setAreaLocatorId(dto.getAreaLocatorid());

            if (StringUtils.isNotEmpty(dto.getWorkcellId())) {
                mtPfepInventoryCategoryVO2.setOrganizationId(dto.getWorkcellId());
                mtPfepInventoryCategoryVO2.setOrganizationType("WORKCELL");
            }

            mtPfepInventoryCategoryVO2.setLocatorCapacity(dto.getLocatorCapacity());
            mtPfepInventoryCategoryVO2.setMinInventory(dto.getMinInventory());
            mtPfepInventoryCategoryVO2.setMaxInventory(dto.getMaxInventory());
            mtPfepInventoryCategoryVO2.setMaterialConsumeRate(dto.getMaterialConsumeRate());
            mtPfepInventoryCategoryVO2.setPackQty(dto.getPackQty());
            mtPfepInventoryCategoryVO2.setBufferInventory(dto.getBufferInventory());
            mtPfepInventoryCategoryVO2.setBufferPeriod(dto.getBufferPeriod());
            mtPfepInventoryCategoryVO2.setMultiplesOfPackFlag(dto.getMultiplesOfPackFlag());
            mtPfepInventoryCategoryVO2.setFromScheduleRateFlag(dto.getFromScheduleRateFlag());
            mtPfepInventoryCategoryVO2.setMaterialConsumeRateUomId(null);
            mtPfepInventoryCategoryVO2.setEnableFlag(dto.getEnableFlag());
            mtPfepInventoryCategoryVO2.setPfepDistributionCategoryId(dto.getPfepDistributionId());

            if (StringUtils.isEmpty(mtPfepInventoryCategoryVO2.getPfepDistributionCategoryId())) {
                // 新增 校验唯一性
                MtPfepDistributionCategory condition = new MtPfepDistributionCategory();
                condition.setMaterialCategorySiteId(mtPfepInventoryCategoryVO2.getMaterialCategorySiteId());
                condition.setOrganizationId(mtPfepInventoryCategoryVO2.getOrganizationId());
                condition.setOrganizationType(mtPfepInventoryCategoryVO2.getOrganizationType());
                condition.setLocatorId(mtPfepInventoryCategoryVO2.getLocatorId());
                condition.setTenantId(tenantId);

                List<MtPfepDistributionCategory> mtPfepDistributionCategoryList = mtPfepDistributionCategoryMapper
                        .materialCategoryDistributionPfepQueryByUnique(tenantId, condition);

                if (CollectionUtils.isNotEmpty(mtPfepDistributionCategoryList)) {
                    throw new MtException("MT_MATERIAL_0098", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0098", "MATERIAL", "key", "【API:materialPfepDistributionSaveForUi】"));
                }
            }

            String pfepDistributionCategoryId = null;
            try {
                pfepDistributionCategoryId = mtPfepDistributionCategoryRepository
                        .materialCategoryPfepDistributionUpdate(tenantId, mtPfepInventoryCategoryVO2);
            } catch (Exception ex) {
                ex.printStackTrace();
                String errorMsg = "save error:" + getMessage(ex);
                throw new MtException(errorMsg);
            }

            return pfepDistributionCategoryId;

        } else if (StringUtils.isNotEmpty(dto.getMaterialId())) {
            // 物料不为空
            MtPfepDistributionVO mtPfepDistributionVO = new MtPfepDistributionVO();

            // 输入则调用API{ materialSiteLimitRelationshipGet }，获取物料站点关系materialSiteId
            MtMaterialSite mtMaterialSite = new MtMaterialSite();
            mtMaterialSite.setSiteId(dto.getSiteId());
            mtMaterialSite.setMaterialId(dto.getMaterialId());
            mtPfepDistributionVO.setMaterialSiteId(
                    mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId, mtMaterialSite));

            if (StringUtils.isEmpty(mtPfepDistributionVO.getMaterialSiteId())) {
                throw new MtException("MT_MATERIAL_0055",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                "MATERIAL", "materialSiteId",
                                "【API:materialPfepDistributionSaveForUi】"));
            }

            mtPfepDistributionVO.setAreaId(dto.getAreaId());
            mtPfepDistributionVO.setLocatorId(dto.getLocatorId());
            mtPfepDistributionVO.setAreaLocatorId(dto.getAreaLocatorid());

            if (StringUtils.isNotEmpty(dto.getWorkcellId())) {
                mtPfepDistributionVO.setOrganizationId(dto.getWorkcellId());
                mtPfepDistributionVO.setOrganizationType("WORKCELL");
            }

            mtPfepDistributionVO.setLocatorCapacity(dto.getLocatorCapacity());
            mtPfepDistributionVO.setMinInventory(dto.getMinInventory());
            mtPfepDistributionVO.setMaxInventory(dto.getMaxInventory());
            mtPfepDistributionVO.setMaterialConsumeRate(dto.getMaterialConsumeRate());
            mtPfepDistributionVO.setPackQty(dto.getPackQty());
            mtPfepDistributionVO.setBufferInventory(dto.getBufferInventory());
            mtPfepDistributionVO.setBufferPeriod(dto.getBufferPeriod());
            mtPfepDistributionVO.setMultiplesOfPackFlag(dto.getMultiplesOfPackFlag());
            mtPfepDistributionVO.setFromScheduleRateFlag(dto.getFromScheduleRateFlag());
            mtPfepDistributionVO.setMaterialConsumeRateUomId(null);
            mtPfepDistributionVO.setEnableFlag(dto.getEnableFlag());
            mtPfepDistributionVO.setPfepDistributionId(dto.getPfepDistributionId());

            if (StringUtils.isEmpty(mtPfepDistributionVO.getPfepDistributionId())) {
                // 新增 校验唯一性
                MtPfepDistribution condition = new MtPfepDistribution();
                condition.setMaterialSiteId(mtPfepDistributionVO.getMaterialSiteId());
                condition.setOrganizationId(mtPfepDistributionVO.getOrganizationId());
                condition.setOrganizationType(mtPfepDistributionVO.getOrganizationType());
                condition.setLocatorId(mtPfepDistributionVO.getLocatorId());
                condition.setTenantId(tenantId);

                List<MtPfepDistribution> mtPfepDistributionList =
                        mtPfepDistributionMapper.materialDistributionPfepQueryByUnique(tenantId, condition);

                if (CollectionUtils.isNotEmpty(mtPfepDistributionList)) {
                    throw new MtException("MT_MATERIAL_0097", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0097", "MATERIAL", "key", "【API:materialPfepDistributionSaveForUi】"));
                }
            }

            String pfepDistributionId = null;
            try {
                pfepDistributionId = mtPfepDistributionRepository.materialPfepDistributionUpdate(tenantId,
                        mtPfepDistributionVO);
            } catch (Exception ex) {
                ex.printStackTrace();
                String errorMsg = "save error:" + getMessage(ex);
                throw new MtException(errorMsg);
            }

            return pfepDistributionId;
        } else {
            logger.error("Item Category And Item is all null");
            return null;
        }
    }

    public static String getMessage(Throwable e) {
        String msg = null;
        if (e instanceof UndeclaredThrowableException) {
            Throwable targetEx = ((UndeclaredThrowableException) e).getUndeclaredThrowable();
            if (targetEx != null) {
                msg = targetEx.getMessage();
            }
        } else {
            msg = e.getMessage();
        }
        return msg;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtPfepDistributionDTO7 materialPfepDistributionCopyForUi(Long tenantId, MtPfepDistributionDTO3 dto) {
        if (dto == null) {
            return null;
        }

        MtPfepDistributionDTO7 mtPfepDistributionDTO7 = new MtPfepDistributionDTO7();

        if (StringUtils.isNotEmpty(dto.getSourceMaterialId())) {
            mtPfepDistributionDTO7.setPfepDistributionType("material");
            // 准备数据
            MtPfepDistribution mtPfepDistribution = new MtPfepDistribution();

            // 输入则调用API{ materialSiteLimitRelationshipGet }，获取物料站点关系materialSiteId
            MtMaterialSite mtMaterialSite = new MtMaterialSite();
            mtMaterialSite.setSiteId(dto.getSourceSiteId());
            mtMaterialSite.setMaterialId(dto.getSourceMaterialId());
            mtPfepDistribution.setMaterialSiteId(
                    mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId, mtMaterialSite));

            if (StringUtils.isEmpty(mtPfepDistribution.getMaterialSiteId())) {
                throw new MtException("MT_MATERIAL_0055",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                "MATERIAL", "materialSiteId",
                                "【API:materialPfepDistributionCopyForUi】"));
            }
            mtPfepDistribution.setAreaId(dto.getSourceAreaId());
            mtPfepDistribution.setLocatorId(dto.getSourceLocatorId());
            mtPfepDistribution.setOrganizationId(dto.getSourceWorkcellId());

            List<MtPfepDistribution> mtPfepDistributionList =
                    mtPfepDistributionMapper.materialDistributionPfepQueryByCopy(tenantId, mtPfepDistribution);

            if (CollectionUtils.isEmpty(mtPfepDistributionList)) {
                logger.error("MtPfepDistributionCategory no data found");
                throw new MtException("MT_MATERIAL_0099",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0099",
                                "MATERIAL", "materialCategorySiteId",
                                "【API:materialPfepDistributionCopyForUi】"));
            }

            if (mtPfepDistributionList.size() != 1) {
                logger.error("MtPfepDistributionCategory too many rows");
                throw new MtException("MT_MATERIAL_0100",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0100",
                                "MATERIAL", "materialCategorySiteId",
                                "【API:materialPfepDistributionCopyForUi】"));
            }

            // 开始处理
            MtPfepDistribution record = mtPfepDistributionList.get(0);
            MtPfepDistributionVO mtPfepDistributionVO = new MtPfepDistributionVO();

            // 输入则调用API{ materialSiteLimitRelationshipGet }，获取物料站点关系materialSiteId
            MtMaterialSite targetMaterialSite = new MtMaterialSite();
            targetMaterialSite.setSiteId(dto.getTargetSiteId());
            targetMaterialSite.setMaterialId(dto.getTargetMaterialId());
            mtPfepDistributionVO.setMaterialSiteId(
                    mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId, targetMaterialSite));

            if (StringUtils.isEmpty(mtPfepDistributionVO.getMaterialSiteId())) {
                throw new MtException("MT_MATERIAL_0055",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                "MATERIAL", "materialSiteId",
                                "【API:materialPfepDistributionCopyForUi】"));
            }

            mtPfepDistributionVO.setAreaId(dto.getTargetAreaId());
            mtPfepDistributionVO.setLocatorId(dto.getTargetLocatorId());
            mtPfepDistributionVO.setOrganizationId(dto.getTargetWorkcellId());

            if (StringUtils.isNotEmpty(mtPfepDistributionVO.getOrganizationId())) {
                mtPfepDistributionVO.setOrganizationType("WORKCELL");
            }

            mtPfepDistributionVO.setAreaLocatorId(record.getAreaLocatorId());

            mtPfepDistributionVO.setLocatorCapacity(record.getLocatorCapacity());
            mtPfepDistributionVO.setMinInventory(record.getMinInventory());
            mtPfepDistributionVO.setMaxInventory(record.getMaxInventory());
            mtPfepDistributionVO.setMaterialConsumeRate(record.getMaterialConsumeRate());
            mtPfepDistributionVO.setPackQty(record.getPackQty());
            mtPfepDistributionVO.setBufferInventory(record.getBufferInventory());
            mtPfepDistributionVO.setBufferPeriod(record.getBufferPeriod());
            mtPfepDistributionVO.setMultiplesOfPackFlag(record.getMultiplesOfPackFlag());
            mtPfepDistributionVO.setFromScheduleRateFlag(record.getFromScheduleRateFlag());
            mtPfepDistributionVO.setMaterialConsumeRateUomId(null);
            mtPfepDistributionVO.setEnableFlag(record.getEnableFlag());
            mtPfepDistributionVO.setPfepDistributionId(record.getPfepDistributionId());

            // 新增 校验唯一性
            MtPfepDistribution condition = new MtPfepDistribution();
            condition.setMaterialSiteId(mtPfepDistributionVO.getMaterialSiteId());
            condition.setOrganizationId(mtPfepDistributionVO.getOrganizationId());
            condition.setOrganizationType(mtPfepDistributionVO.getOrganizationType());
            condition.setLocatorId(mtPfepDistributionVO.getLocatorId());
            condition.setTenantId(tenantId);

            List<MtPfepDistribution> mtPfepDistributions =
                    mtPfepDistributionMapper.materialDistributionPfepQueryByUnique(tenantId, condition);

            if (CollectionUtils.isEmpty(mtPfepDistributions)) {
                mtPfepDistributionVO.setPfepDistributionId(null);
            } else {
                mtPfepDistributionVO.setPfepDistributionId(mtPfepDistributions.get(0).getPfepDistributionId());
            }

            String pfepDistributionId = null;
            try {
                pfepDistributionId = mtPfepDistributionRepository.materialPfepDistributionUpdate(tenantId,
                        mtPfepDistributionVO);
            } catch (Exception ex) {
                ex.printStackTrace();
                String errorMsg = "save error:" + getMessage(ex);
                throw new MtException(errorMsg);
            }

            mtPfepDistributionDTO7.setPfepDistributionId(pfepDistributionId);
            return mtPfepDistributionDTO7;
        } else if (StringUtils.isNotEmpty(dto.getSourceMaterialCategoryId())) {
            mtPfepDistributionDTO7.setPfepDistributionType("category");

            MtPfepDistributionCategory mtPfepDistributionCategory = new MtPfepDistributionCategory();

            // 输入则调用API{ materialCategorySiteLimitRelationshipGet }，获取物料站点关系materialCategorySiteId
            MtMaterialCategorySite site = new MtMaterialCategorySite();
            site.setTenantId(tenantId);
            site.setMaterialCategoryId(dto.getSourceMaterialCategoryId());
            site.setSiteId(dto.getSourceSiteId());
            mtPfepDistributionCategory.setMaterialCategorySiteId(
                    mtMaterialCategorySiteRepository.materialCategorySiteLimitRelationGet(tenantId, site));
            if (StringUtils.isEmpty(mtPfepDistributionCategory.getMaterialCategorySiteId())) {
                throw new MtException("MT_MATERIAL_0055",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                "MATERIAL", "materialCategorySiteId",
                                "【API:materialPfepDistributionCopyForUi】"));
            }

            mtPfepDistributionCategory.setAreaId(dto.getSourceAreaId());
            mtPfepDistributionCategory.setLocatorId(dto.getSourceLocatorId());
            mtPfepDistributionCategory.setOrganizationId(dto.getSourceWorkcellId());

            List<MtPfepDistributionCategory> mtPfepDistributionCategoryList = mtPfepDistributionCategoryMapper
                    .materialCategoryDistributionPfepQueryByCopy(tenantId, mtPfepDistributionCategory);

            if (CollectionUtils.isEmpty(mtPfepDistributionCategoryList)) {
                logger.error("MtPfepDistributionCategory no data found");
                throw new MtException("MT_MATERIAL_0099",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0099",
                                "MATERIAL", "materialCategorySiteId",
                                "【API:materialPfepDistributionCopyForUi】"));
            }

            if (mtPfepDistributionCategoryList.size() != 1) {
                logger.error("MtPfepDistributionCategory too many rows");
                throw new MtException("MT_MATERIAL_0100",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0100",
                                "MATERIAL", "materialCategorySiteId",
                                "【API:materialPfepDistributionCopyForUi】"));
            }

            // 开始处理
            MtPfepDistributionCategory record = mtPfepDistributionCategoryList.get(0);
            MtPfepInventoryCategoryVO2 mtPfepInventoryCategoryVO2 = new MtPfepInventoryCategoryVO2();

            // 输入则调用API{ materialCategorySiteLimitRelationshipGet }，获取物料站点关系materialCategorySiteId
            MtMaterialCategorySite targetSite = new MtMaterialCategorySite();
            targetSite.setTenantId(tenantId);
            targetSite.setMaterialCategoryId(dto.getTargetMaterialCategoryId());
            targetSite.setSiteId(dto.getTargetSiteId());
            mtPfepInventoryCategoryVO2.setMaterialCategorySiteId(mtMaterialCategorySiteRepository
                    .materialCategorySiteLimitRelationGet(tenantId, targetSite));

            if (StringUtils.isEmpty(mtPfepInventoryCategoryVO2.getMaterialCategorySiteId())) {
                throw new MtException("MT_MATERIAL_0055",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                "MATERIAL", "materialCategorySiteId",
                                "【API:materialCategoryDistributionPfepQuery】"));
            }

            mtPfepInventoryCategoryVO2.setAreaId(dto.getTargetAreaId());
            mtPfepInventoryCategoryVO2.setLocatorId(dto.getTargetLocatorId());
            mtPfepInventoryCategoryVO2.setOrganizationId(dto.getTargetWorkcellId());

            if (StringUtils.isNotEmpty(mtPfepInventoryCategoryVO2.getOrganizationId())) {
                mtPfepInventoryCategoryVO2.setOrganizationType("WORKCELL");
            }

            mtPfepInventoryCategoryVO2.setAreaLocatorId(record.getAreaLocatorId());
            mtPfepInventoryCategoryVO2.setLocatorCapacity(record.getLocatorCapacity());
            mtPfepInventoryCategoryVO2.setMinInventory(record.getMinInventory());
            mtPfepInventoryCategoryVO2.setMaxInventory(record.getMaxInventory());
            mtPfepInventoryCategoryVO2.setMaterialConsumeRate(record.getMaterialConsumeRate());
            mtPfepInventoryCategoryVO2.setPackQty(record.getPackQty());
            mtPfepInventoryCategoryVO2.setBufferInventory(record.getBufferInventory());
            mtPfepInventoryCategoryVO2.setBufferPeriod(record.getBufferPeriod());
            mtPfepInventoryCategoryVO2.setMultiplesOfPackFlag(record.getMultiplesOfPackFlag());
            mtPfepInventoryCategoryVO2.setFromScheduleRateFlag(record.getFromScheduleRateFlag());
            mtPfepInventoryCategoryVO2.setEnableFlag(record.getEnableFlag());
            mtPfepInventoryCategoryVO2.setPfepDistributionCategoryId(record.getPfepDistributionCategoryId());

            // 获取主键ID
            // 新增 校验唯一性
            MtPfepDistributionCategory condition = new MtPfepDistributionCategory();
            condition.setMaterialCategorySiteId(mtPfepInventoryCategoryVO2.getMaterialCategorySiteId());
            condition.setOrganizationId(mtPfepInventoryCategoryVO2.getOrganizationId());
            condition.setOrganizationType(mtPfepInventoryCategoryVO2.getOrganizationType());
            condition.setLocatorId(mtPfepInventoryCategoryVO2.getLocatorId());
            condition.setTenantId(tenantId);

            List<MtPfepDistributionCategory> mtPfepDistributionCategorys = mtPfepDistributionCategoryMapper
                    .materialCategoryDistributionPfepQueryByUnique(tenantId, condition);

            if (CollectionUtils.isEmpty(mtPfepDistributionCategorys)) {
                // 没有新增
                mtPfepInventoryCategoryVO2.setPfepDistributionCategoryId(null);
            } else {
                mtPfepInventoryCategoryVO2.setPfepDistributionCategoryId(mtPfepDistributionCategorys.get(0).getPfepDistributionCategoryId());
            }

            String pfepDistributionCategoryId = null;
            try {
                pfepDistributionCategoryId = mtPfepDistributionCategoryRepository
                        .materialCategoryPfepDistributionUpdate(tenantId, mtPfepInventoryCategoryVO2);
            } catch (Exception ex) {
                ex.printStackTrace();
                String errorMsg = "save error:" + getMessage(ex);
                throw new MtException(errorMsg);
            }

            mtPfepDistributionDTO7.setPfepDistributionId(pfepDistributionCategoryId);
            return mtPfepDistributionDTO7;
        } else {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    /**
     * @description
     *
     *
     * @return:
     * @since: 1.0.0
     * @Author: jin.xu@hand-china.com
     * @Date:
     * @change: by jin.xu@hand-china.com for init
     */
    public MtPfepDistributionDTO6 materialPfepDistributionDetailsForUi(Long tenantId, MtPfepDistributionDTO5 dto) {

        if (dto == null) {
            logger.error("dto is null");
            return null;
        }

        if (StringUtils.isEmpty(dto.getPfepDistributionId())) {
            logger.error("query kid is null");
            return null;
        }

        if ("material".equals(dto.getPfepDistributionType())) {
            MtPfepDistribution mtPfepDistribution =
                    mtPfepDistributionRepository.selectByPrimaryKey(dto.getPfepDistributionId());

            if (mtPfepDistribution == null) {
                // 未查到任何数据
                logger.error("no data found");
                return null;
            }

            // 开始获取数据
            MtPfepDistributionDTO6 mtPfepDistributionDTO6 = new MtPfepDistributionDTO6();

            mtPfepDistributionDTO6.setPfepDistributionId(mtPfepDistribution.getPfepDistributionId());

            MtMaterialSite mtMaterialSite = mtMaterialSiteRepository.relationLimitMaterialSiteGet(tenantId,
                    mtPfepDistribution.getMaterialSiteId());
            if (mtMaterialSite != null) {
                mtPfepDistributionDTO6.setMaterialId(mtMaterialSite.getMaterialId());

                MtMaterialVO4 mtMaterialVO4 = new MtMaterialVO4();
                mtMaterialVO4.setMaterialId(mtMaterialSite.getMaterialId());
                List<MtMaterialVO5> mtMaterialVO5List =
                        mtMaterialRepository.propertyLimitMaterialPropertyQuery(tenantId, mtMaterialVO4);

                if (CollectionUtils.isNotEmpty(mtMaterialVO5List) && mtMaterialVO5List.size() > 0) {
                    mtPfepDistributionDTO6.setMaterialCode(mtMaterialVO5List.get(0).getMaterialCode());
                    mtPfepDistributionDTO6.setMaterialName(mtMaterialVO5List.get(0).getMaterialName());
                } else {
                    logger.error("Item does not exist");
                }

                mtPfepDistributionDTO6.setSiteId(mtMaterialSite.getSiteId());

                MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, mtMaterialSite.getSiteId());

                if (mtModSite != null) {
                    mtPfepDistributionDTO6.setSiteCode(mtModSite.getSiteCode());
                    mtPfepDistributionDTO6.setSiteName(mtModSite.getSiteName());
                }

            } else {
                logger.error("Item and site relationship does not exist");
            }

            mtPfepDistributionDTO6.setAreaId(mtPfepDistribution.getAreaId());
            if (StringUtils.isEmpty(mtPfepDistribution.getAreaId())) {
                mtPfepDistributionDTO6.setAreaCode(null);
            } else {
                MtModArea mtModArea =
                        mtModAreaRepository.areaBasicPropertyGet(tenantId, mtPfepDistribution.getAreaId());

                if (mtModArea == null) {
                    logger.error("Area does not exist");
                    mtPfepDistributionDTO6.setAreaCode(null);
                } else {
                    mtPfepDistributionDTO6.setAreaCode(mtModArea.getAreaCode());
                }
            }

            mtPfepDistributionDTO6.setLocatorId(mtPfepDistribution.getLocatorId());
            if (StringUtils.isEmpty(mtPfepDistribution.getLocatorId())) {
                mtPfepDistributionDTO6.setLocatorCode(null);
            } else {
                MtModLocator mtModLocator = mtModLocatorRepository.locatorBasicPropertyGet(tenantId,
                        mtPfepDistribution.getLocatorId());

                if (mtModLocator == null) {
                    mtPfepDistributionDTO6.setLocatorCode(null);
                } else {
                    mtPfepDistributionDTO6.setLocatorCode(mtModLocator.getLocatorCode());
                }
            }

            mtPfepDistributionDTO6.setAreaLocatorId(mtPfepDistribution.getAreaLocatorId());
            if (StringUtils.isEmpty(mtPfepDistribution.getAreaLocatorId())) {
                mtPfepDistributionDTO6.setAreaLocatorCode(null);
            } else {
                MtModLocator mtModLocator = mtModLocatorRepository.locatorBasicPropertyGet(tenantId,
                        mtPfepDistribution.getAreaLocatorId());

                if (mtModLocator == null) {
                    mtPfepDistributionDTO6.setAreaLocatorCode(null);
                } else {
                    mtPfepDistributionDTO6.setAreaLocatorCode(mtModLocator.getLocatorCode());
                }
            }

            mtPfepDistributionDTO6.setOrganizationId(mtPfepDistribution.getOrganizationId());
            if (StringUtils.isEmpty(mtPfepDistribution.getOrganizationId())) {
                mtPfepDistributionDTO6.setOrganizationCode(null);
            } else {
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId,
                        mtPfepDistribution.getOrganizationId());

                if (mtModWorkcell == null) {
                    mtPfepDistributionDTO6.setOrganizationCode(null);
                } else {
                    mtPfepDistributionDTO6.setOrganizationCode(mtModWorkcell.getWorkcellCode());
                }
            }

            mtPfepDistributionDTO6.setLocatorCapacity(mtPfepDistribution.getLocatorCapacity());
            mtPfepDistributionDTO6.setMinInventory(mtPfepDistribution.getMinInventory());
            mtPfepDistributionDTO6.setMaxInventory(mtPfepDistribution.getMaxInventory());
            mtPfepDistributionDTO6.setMaterialConsumeRate(mtPfepDistribution.getMaterialConsumeRate());
            mtPfepDistributionDTO6.setPackQty(mtPfepDistribution.getPackQty());
            mtPfepDistributionDTO6.setBufferInventory(mtPfepDistribution.getBufferInventory());
            mtPfepDistributionDTO6.setBufferPeriod(mtPfepDistribution.getBufferPeriod());
            mtPfepDistributionDTO6.setMultiplesOfPackFlag(mtPfepDistribution.getMultiplesOfPackFlag());
            mtPfepDistributionDTO6.setFromScheduleRateFlag(mtPfepDistribution.getFromScheduleRateFlag());
            mtPfepDistributionDTO6.setEnableFlag(mtPfepDistribution.getEnableFlag());


            return mtPfepDistributionDTO6;
        } else if ("category".equals(dto.getPfepDistributionType())) {
            MtPfepDistributionCategory mtPfepDistributionCategory =
                    mtPfepDistributionCategoryRepository.selectByPrimaryKey(dto.getPfepDistributionId());

            if (mtPfepDistributionCategory == null) {
                // 未查到任何数据
                logger.error("no data found");
                return null;
            }

            // 开始获取数据
            MtPfepDistributionDTO6 mtPfepDistributionDTO6 = new MtPfepDistributionDTO6();

            mtPfepDistributionDTO6.setPfepDistributionId(mtPfepDistributionCategory.getPfepDistributionCategoryId());

            MtMaterialCategorySite mtMaterialCategorySite =
                    mtMaterialCategorySiteRepository.relationLimitMaterialCategorySiteGet(tenantId,
                            mtPfepDistributionCategory.getMaterialCategorySiteId());

            if (mtMaterialCategorySite != null) {
                mtPfepDistributionDTO6.setMaterialCategoryId(mtMaterialCategorySite.getMaterialCategoryId());

                MtMaterialCategory mtMaterialCategory = mtMaterialCategoryRepository.materialCategoryGet(tenantId,
                        mtMaterialCategorySite.getMaterialCategoryId());

                if (mtMaterialCategory != null) {
                    mtPfepDistributionDTO6.setMaterialCategoryCode(mtMaterialCategory.getCategoryCode());
                } else {
                    logger.error("Item Category does not exist");
                }

                mtPfepDistributionDTO6.setSiteId(mtMaterialCategorySite.getSiteId());

                MtModSite mtModSite =
                        mtModSiteRepository.siteBasicPropertyGet(tenantId, mtMaterialCategorySite.getSiteId());

                if (mtModSite != null) {
                    mtPfepDistributionDTO6.setSiteCode(mtModSite.getSiteCode());
                    mtPfepDistributionDTO6.setSiteName(mtModSite.getSiteName());
                }

            } else {
                logger.error("Item Category and site relationship does not exist");
            }

            mtPfepDistributionDTO6.setAreaId(mtPfepDistributionCategory.getAreaId());
            if (StringUtils.isEmpty(mtPfepDistributionCategory.getAreaId())) {
                mtPfepDistributionDTO6.setAreaCode(null);
            } else {
                MtModArea mtModArea = mtModAreaRepository.areaBasicPropertyGet(tenantId,
                        mtPfepDistributionCategory.getAreaId());

                if (mtModArea == null) {
                    mtPfepDistributionDTO6.setAreaCode(null);
                } else {
                    mtPfepDistributionDTO6.setAreaCode(mtModArea.getAreaCode());
                }
            }

            mtPfepDistributionDTO6.setLocatorId(mtPfepDistributionCategory.getLocatorId());
            if (StringUtils.isEmpty(mtPfepDistributionCategory.getLocatorId())) {
                mtPfepDistributionDTO6.setLocatorCode(null);
            } else {
                MtModLocator mtModLocator = mtModLocatorRepository.locatorBasicPropertyGet(tenantId,
                        mtPfepDistributionCategory.getLocatorId());

                if (mtModLocator == null) {
                    mtPfepDistributionDTO6.setLocatorCode(null);
                } else {
                    mtPfepDistributionDTO6.setLocatorCode(mtModLocator.getLocatorCode());
                }
            }


            mtPfepDistributionDTO6.setAreaLocatorId(mtPfepDistributionCategory.getAreaLocatorId());
            if (StringUtils.isEmpty(mtPfepDistributionCategory.getAreaLocatorId())) {
                mtPfepDistributionDTO6.setAreaLocatorCode(null);
            } else {
                MtModLocator mtModLocator = mtModLocatorRepository.locatorBasicPropertyGet(tenantId,
                        mtPfepDistributionCategory.getAreaLocatorId());

                if (mtModLocator == null) {
                    mtPfepDistributionDTO6.setAreaLocatorCode(null);
                } else {
                    mtPfepDistributionDTO6.setAreaLocatorCode(mtModLocator.getLocatorCode());
                }
            }

            mtPfepDistributionDTO6.setOrganizationId(mtPfepDistributionCategory.getOrganizationId());
            if (StringUtils.isEmpty(mtPfepDistributionCategory.getOrganizationId())) {
                mtPfepDistributionDTO6.setOrganizationCode(null);
            } else {
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId,
                        mtPfepDistributionCategory.getOrganizationId());

                if (mtModWorkcell == null) {
                    mtPfepDistributionDTO6.setOrganizationCode(null);
                } else {
                    mtPfepDistributionDTO6.setOrganizationCode(mtModWorkcell.getWorkcellCode());
                }
            }

            mtPfepDistributionDTO6.setLocatorCapacity(mtPfepDistributionCategory.getLocatorCapacity());
            mtPfepDistributionDTO6.setMinInventory(mtPfepDistributionCategory.getMinInventory());
            mtPfepDistributionDTO6.setMaxInventory(mtPfepDistributionCategory.getMaxInventory());
            mtPfepDistributionDTO6.setMaterialConsumeRate(mtPfepDistributionCategory.getMaterialConsumeRate());
            mtPfepDistributionDTO6.setPackQty(mtPfepDistributionCategory.getPackQty());
            mtPfepDistributionDTO6.setBufferInventory(mtPfepDistributionCategory.getBufferInventory());
            mtPfepDistributionDTO6.setBufferPeriod(mtPfepDistributionCategory.getBufferPeriod());
            mtPfepDistributionDTO6.setMultiplesOfPackFlag(mtPfepDistributionCategory.getMultiplesOfPackFlag());
            mtPfepDistributionDTO6.setFromScheduleRateFlag(mtPfepDistributionCategory.getFromScheduleRateFlag());
            mtPfepDistributionDTO6.setEnableFlag(mtPfepDistributionCategory.getEnableFlag());

            return mtPfepDistributionDTO6;
        } else {
            logger.error("PfepDistributionType:" + dto.getPfepDistributionType() + " is not support");
            return null;
        }
    }

}
