package tarzan.material.infra.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtMaterialCategorySite;
import tarzan.material.domain.entity.MtPfepDistributionCategory;
import tarzan.material.domain.repository.MtMaterialCategoryRepository;
import tarzan.material.domain.repository.MtMaterialCategorySiteRepository;
import tarzan.material.domain.repository.MtPfepDistributionCategoryRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtPfepInventoryCategoryVO2;
import tarzan.material.domain.vo.MtPfepInventoryCategoryVO3;
import tarzan.material.domain.vo.MtPfepInventoryCategoryVO4;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.material.infra.mapper.MtPfepDistributionCategoryMapper;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModAreaDistributionRepository;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModAreaDistributionVO;
import tarzan.modeling.domain.vo.MtModAreaDistributionVO2;

/**
 * 物料类别配送属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Component
public class MtPfepDistributionCategoryRepositoryImpl extends BaseRepositoryImpl<MtPfepDistributionCategory>
                implements MtPfepDistributionCategoryRepository {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtPfepDistributionCategoryMapper mtPfepDistributionCategoryMapper;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtMaterialCategoryRepository mtMaterialCategoryRepository;

    @Autowired
    private MtMaterialCategorySiteRepository mtMaterialCategorySiteRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtModAreaRepository mtModAreaRepository;

    @Autowired
    private MtModAreaDistributionRepository mtModAreaDistributionRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pfepDistributionCatgAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        if (StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId", "【API:pfepDistributionCatgAttrPropertyUpdate】"));
        }
        // 获取主表数据
        MtPfepDistributionCategory mtPfepDistributionCategory = new MtPfepDistributionCategory();
        mtPfepDistributionCategory.setTenantId(tenantId);
        mtPfepDistributionCategory.setPfepDistributionCategoryId(dto.getKeyId());
        mtPfepDistributionCategory = mtPfepDistributionCategoryMapper.selectOne(mtPfepDistributionCategory);
        if (mtPfepDistributionCategory == null
                        || StringUtils.isEmpty(mtPfepDistributionCategory.getPfepDistributionCategoryId())) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            "keyId:" + dto.getKeyId(), "mt_pfep_distribution_category",
                                            "【API:pfepDistributionCatgAttrPropertyUpdate】"));
        }

        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_pfep_distribution_catg_attr", dto.getKeyId(),
                        dto.getEventId(), dto.getAttrs());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String materialCategoryPfepDistributionUpdate(Long tenantId, MtPfepInventoryCategoryVO2 vo) {
        // 1.判断参数合规性
        if (StringUtils.isNotEmpty(vo.getMaterialConsumeRateUomId())) {
            MtUomVO mtUomVO = mtUomRepository.uomPropertyGet(tenantId, vo.getMaterialConsumeRateUomId());
            if (null == mtUomVO) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                                "MATERIAL", "materialConsumeRateUomId",
                                                "【API:materialCategoryPfepDistributionUpdate】"));
            }
        }

        if (StringUtils.isNotEmpty(vo.getLocatorId()) && StringUtils.isNotEmpty(vo.getAreaLocatorId())) {
            List<String> locatorIdList = Arrays.asList(vo.getLocatorId(), vo.getAreaLocatorId());
            List<MtModLocator> mtModLocators =
                            mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, locatorIdList);
            if (CollectionUtils.isEmpty(mtModLocators)) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                                "MATERIAL", " locatorId（routeLocatorId）",
                                                "【API:materialCategoryPfepDistributionUpdate】"));
            }
        }

        // 当OrganizationType不为空 且不等于WORKCELL则报错 2020年2月18日与郭王勋确认修改 by xujin
        if (StringUtils.isNotEmpty(vo.getOrganizationType()) && !"WORKCELL".equals(vo.getOrganizationType())) {
            throw new MtException("MT_MATERIAL_0089",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0089", "MATERIAL",
                                            " organizationType", "【API:materialCategoryPfepDistributionUpdate】"));
        }

        if (StringUtils.isNotEmpty(vo.getOrganizationType()) && StringUtils.isEmpty(vo.getOrganizationId())
                        || StringUtils.isEmpty(vo.getOrganizationType())
                                        && StringUtils.isNotEmpty(vo.getOrganizationId())) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            " organizationType、organizationId",
                                            "【API:materialCategoryPfepDistributionUpdate】"));
        }

        if (StringUtils.isNotEmpty(vo.getMaterialCategorySiteId())) {
            // modify by xujin for guowangxun 2020年2月25日 取消校验
//            String isTrue = mtMaterialCategoryRepository.materialCategoryEnableValidate(tenantId,
//                            vo.getMaterialCategorySiteId());
//            if ("N".equals(isTrue)) {
//                throw new MtException("MT_MATERIAL_0055",
//                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
//                                                "MATERIAL", "MaterialCategorySiteId",
//                                                "【API:materialCategoryPfepDistributionUpdate】"));
//            }
            MtMaterialCategorySite categorySite = mtMaterialCategorySiteRepository
                            .relationLimitMaterialCategorySiteGet(tenantId, vo.getMaterialCategorySiteId());

            if (null == categorySite) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                                "MATERIAL", "materialCategorySiteId",
                                                "【API:materialCategoryPfepDistributionUpdate】"));
            }
            String siteId = categorySite.getSiteId();

            MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, siteId);
            // 走到这说明查询结果不为空，且enableFlag='Y'
            if (!"MANUFACTURING".equals(mtModSite.getSiteType())) {
                throw new MtException("MT_MATERIAL_0078",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0078",
                                                "MATERIAL", "materialSiteId",
                                                "【API:materialCategoryPfepDistributionUpdate】"));
            }
        }

        // 检验必输参数是否输入
        // modify by xujin for guowangxun 2020年2月25日 取消校验
//        if (StringUtils.isEmpty(vo.getPfepDistributionCategoryId())) {
//            throw new MtException("MT_MATERIAL_0071",
//                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0071", "MATERIAL",
//                                            "PfepDistributionCategoryId",
//                                            "【API:materialCategoryPfepDistributionUpdate】"));
//        }

        if (StringUtils.isEmpty(vo.getMaterialCategorySiteId())) {
            throw new MtException("MT_MATERIAL_0071",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0071", "MATERIAL",
                                            "MaterialCategorySiteId", "【API:materialCategoryPfepDistributionUpdate】"));
        }

        if ((StringUtils.isEmpty(vo.getOrganizationId()) && StringUtils.isNotEmpty(vo.getOrganizationType()))
                        || (StringUtils.isNotEmpty(vo.getOrganizationId())
                                        && StringUtils.isEmpty(vo.getOrganizationType()))) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            " organizationType、organizationId",
                                            "【API:materialCategoryPfepDistributionUpdate】"));

        }

        if (StringUtils.isEmpty(vo.getLocatorId())) {
            throw new MtException("MT_MATERIAL_0071",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0071", "MATERIAL",
                                            "LocatorId", "【API:materialCategoryPfepDistributionUpdate】"));
        }

        if (StringUtils.isEmpty(vo.getAreaId())) {
            throw new MtException("MT_MATERIAL_0071", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0071", "MATERIAL", "RouteId", "【API:materialCategoryPfepDistributionUpdate】"));
        }

        if (StringUtils.isEmpty(vo.getEnableFlag())) {
            throw new MtException("MT_MATERIAL_0071",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0071", "MATERIAL",
                                            "EnableFlag", "【API:materialCategoryPfepDistributionUpdate】"));
        }

        // 更新或新增前唯一性校验
        if (StringUtils.isNotEmpty(vo.getMaterialCategorySiteId()) && StringUtils.isNotEmpty(vo.getOrganizationId())
                        && StringUtils.isNotEmpty(vo.getOrganizationType())) {
            MtPfepDistributionCategory category = new MtPfepDistributionCategory();
            category.setTenantId(tenantId);
            category.setMaterialCategorySiteId(vo.getMaterialCategorySiteId());
            category.setOrganizationId(vo.getOrganizationId());
            category.setOrganizationType(vo.getOrganizationType());
            category = mtPfepDistributionCategoryMapper.selectOne(category);
            if (null != category) {
                throw new MtException("MT_MATERIAL_0088",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0088",
                                                "MATERIAL", "materialCategorySiteId、organizationId、organizationType",
                                                "【API:materialCategoryPfepDistributionUpdate】"));
            }
        }


        // 2.根据传入参数判断新增或更新模式
        if (StringUtils.isNotEmpty(vo.getPfepDistributionCategoryId())) {
            // 更新
            // enableFlag，传入为空则不更新
            if ("".equals(vo.getEnableFlag())) {
                return vo.getPfepDistributionCategoryId();
            }
            MtPfepDistributionCategory category = new MtPfepDistributionCategory();
            category.setTenantId(tenantId);
            category.setPfepDistributionCategoryId(vo.getPfepDistributionCategoryId());
            MtPfepDistributionCategory distributionCategory = mtPfepDistributionCategoryMapper.selectOne(category);

            if (null == distributionCategory) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                                "MATERIAL", "pfepDistributionCategoryId",
                                                "【API:materialCategoryPfepDistributionUpdate】"));
            }

            if (null != vo.getMaterialCategorySiteId()) {
                category.setMaterialCategorySiteId(vo.getMaterialCategorySiteId());
            }
            if (null != vo.getOrganizationType()) {
                category.setOrganizationType(vo.getOrganizationType());
            }
            if (null != vo.getOrganizationId()) {
                category.setOrganizationId(vo.getOrganizationId());
            }
            if (null != vo.getLocatorId()) {
                category.setLocatorId(vo.getLocatorId());
            }
            if (null != vo.getLocatorCapacity()) {
                category.setLocatorCapacity(vo.getLocatorCapacity());
            }
            if (null != vo.getFromScheduleRateFlag()) {
                category.setFromScheduleRateFlag(vo.getFromScheduleRateFlag());
            }
            if (null != vo.getMaterialConsumeRateUomId()) {
                category.setMaterialConsumeRateUomId(vo.getMaterialConsumeRateUomId());
            }
            if (null != vo.getMaterialConsumeRate()) {
                category.setMaterialConsumeRate(vo.getMaterialConsumeRate());
            }
            if (null != vo.getBufferPeriod()) {
                category.setBufferPeriod(vo.getBufferPeriod());
            }
            if (null != vo.getBufferInventory()) {
                category.setBufferInventory(vo.getBufferInventory());
            }

            if (null != vo.getMaxInventory()) {
                category.setMaxInventory(vo.getMaxInventory());
            }
            if (null != vo.getMinInventory()) {
                category.setMinInventory(vo.getMinInventory());
            }
            if (null != vo.getPackQty()) {
                category.setPackQty(vo.getPackQty());
            }
            if (null != vo.getMultiplesOfPackFlag()) {
                category.setMultiplesOfPackFlag(vo.getMultiplesOfPackFlag());
            }
            if (null != vo.getAreaId()) {
                category.setAreaId(vo.getAreaId());
            }
            if (null != vo.getAreaLocatorId()) {
                category.setAreaLocatorId(vo.getAreaLocatorId());
            }
            if (null != vo.getEnableFlag()) {
                category.setEnableFlag(vo.getEnableFlag());
            }

            this.updateByPrimaryKey(category);

            return category.getPfepDistributionCategoryId();

        } else {
            // 新增
            MtPfepDistributionCategory category = new MtPfepDistributionCategory();
            category.setTenantId(tenantId);

            if (null != vo.getMaterialCategorySiteId()) {
                category.setMaterialCategorySiteId(vo.getMaterialCategorySiteId());
            }
            if (null != vo.getOrganizationType()) {
                category.setOrganizationType(vo.getOrganizationType());
            }
            if (null != vo.getOrganizationId()) {
                category.setOrganizationId(vo.getOrganizationId());
            }
            if (null != vo.getLocatorId()) {
                category.setLocatorId(vo.getLocatorId());
            }
            if (null != vo.getLocatorCapacity()) {
                category.setLocatorCapacity(vo.getLocatorCapacity());
            }
            if (null != vo.getFromScheduleRateFlag()) {
                category.setFromScheduleRateFlag(vo.getFromScheduleRateFlag());
            }
            if (null != vo.getMaterialConsumeRateUomId()) {
                category.setMaterialConsumeRateUomId(vo.getMaterialConsumeRateUomId());
            }
            if (null != vo.getMaterialConsumeRate()) {
                category.setMaterialConsumeRate(vo.getMaterialConsumeRate());
            }
            if (null != vo.getBufferPeriod()) {
                category.setBufferPeriod(vo.getBufferPeriod());
            }
            if (null != vo.getBufferInventory()) {
                category.setBufferInventory(vo.getBufferInventory());
            }

            if (null != vo.getMaxInventory()) {
                category.setMaxInventory(vo.getMaxInventory());
            }
            if (null != vo.getMinInventory()) {
                category.setMinInventory(vo.getMinInventory());
            }
            if (null != vo.getPackQty()) {
                category.setPackQty(vo.getPackQty());
            }
            if (null != vo.getMultiplesOfPackFlag()) {
                category.setMultiplesOfPackFlag(vo.getMultiplesOfPackFlag());
            }
            if (null != vo.getAreaId()) {
                category.setAreaId(vo.getAreaId());
            }
            if (null != vo.getAreaLocatorId()) {
                category.setAreaLocatorId(vo.getAreaLocatorId());
            }
            if (null != vo.getEnableFlag()) {
                category.setEnableFlag(vo.getEnableFlag());
            }

            this.insertSelective(category);
            return category.getPfepDistributionCategoryId();

        }
    }


    @Override
    public List<MtPfepInventoryCategoryVO4> materialCategoryDistributionPfepQuery(Long tenantId,
                                                                                  MtPfepInventoryCategoryVO3 dto) {
        List<MtPfepInventoryCategoryVO4> resultList = new ArrayList<>();
        // 第一步，判断参数合规性
        // 校验参数（organizationType和organizationId）是否同时输入/同时不输入
        if (StringUtils.isNotEmpty(dto.getOrganizationId()) && StringUtils.isEmpty(dto.getOrganizationType())
                        || StringUtils.isEmpty(dto.getOrganizationId())
                                        && StringUtils.isNotEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            "organizationType、organizationId",
                                            "【API:materialCategoryDistributionPfepQuery】"));
        }
        // 输入则校验参数organizationType等于WORKCELL
        if (StringUtils.isNotEmpty(dto.getOrganizationType()) && !"WORKCELL".equals(dto.getOrganizationType())) {
            throw new MtException("MT_MATERIAL_0089",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0089", "MATERIAL",
                                            "organizationType", "【API:materialCategoryDistributionPfepQuery】"));
        }
        // 传入参数organizationId调用API{workcellBasicPropertyGet}
        if (StringUtils.isNotEmpty(dto.getOrganizationId())) {
            MtModWorkcell mtModWorkcell =
                            mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getOrganizationId());
            if (mtModWorkcell == null) {
                throw new MtException("MT_MATERIAL_0090",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0090",
                                                "MATERIAL", "organizationId",
                                                "【API:materialCategoryDistributionPfepQuery】"));
            }
        }

        // 校验参数（materialId和siteId）是否同时输入/同时不输入
        if (StringUtils.isNotEmpty(dto.getMaterialCategoryId()) && StringUtils.isEmpty(dto.getSiteId())
                        || StringUtils.isEmpty(dto.getMaterialCategoryId())
                                        && StringUtils.isNotEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            "materialCategoryId，siteId",
                                            "【API:materialCategoryDistributionPfepQuery】"));
        }

        if (StringUtils.isNotEmpty(dto.getMaterialCategoryId()) && StringUtils.isNotEmpty(dto.getSiteId())) {
            // 输入则调用API{ materialCategorySiteLimitRelationshipGet }，获取物料站点关系materialCategorySiteId
            MtMaterialCategorySite site = new MtMaterialCategorySite();
            site.setTenantId(tenantId);
            site.setMaterialCategoryId(dto.getMaterialCategoryId());
            site.setSiteId(dto.getSiteId());
            String materialCategorySiteId =
                            mtMaterialCategorySiteRepository.materialCategorySiteLimitRelationGet(tenantId, site);
            if (StringUtils.isEmpty(materialCategorySiteId)) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                                "MATERIAL", "materialCategorySiteId",
                                                "【API:materialCategoryDistributionPfepQuery】"));
            } else {
                // 若参数materialSiteId也输入则校验是否与i中获取相同
                if (StringUtils.isNotEmpty(dto.getMaterialCategorySiteId())
                                && !materialCategorySiteId.equals(dto.getMaterialCategorySiteId())) {
                    throw new MtException("MT_MATERIAL_0074",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0074",
                                                    "MATERIAL", "【API:materialCategoryDistributionPfepQuery】"));
                } else {
                    dto.setMaterialCategorySiteId(materialCategorySiteId);
                }
            }

        }

        if (StringUtils.isNotEmpty(dto.getAreaId())) {
            MtModArea area = mtModAreaRepository.areaBasicPropertyGet(tenantId, dto.getAreaId());
            if (area == null) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                                "MATERIAL", "areaId", "【API:materialCategoryDistributionPfepQuery】"));
            }
        }

        // 按若参数全部为空则报错
        if (StringUtils.isEmpty(dto.getMaterialCategoryId()) && StringUtils.isEmpty(dto.getOrganizationId())
                        && StringUtils.isEmpty(dto.getOrganizationType()) && StringUtils.isEmpty(dto.getSiteId())
                        && StringUtils.isEmpty(dto.getAreaId())
                        && StringUtils.isEmpty(dto.getMaterialCategorySiteId())) {
            throw new MtException("MT_MATERIAL_0091", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0091", "MATERIAL", "areaId", "【API:materialCategoryDistributionPfepQuery】"));
        }

        // 第二步，根据输入参数组合获取输出参数
        List<MtPfepDistributionCategory> categorys =
                        mtPfepDistributionCategoryMapper.materialCategoryDistributionPfepQuery(tenantId, dto);
        for (MtPfepDistributionCategory category : categorys) {
            MtPfepInventoryCategoryVO4 result = new MtPfepInventoryCategoryVO4();
            result.setMaterialCategorySiteId(category.getMaterialCategorySiteId());
            result.setOrganizationType(category.getOrganizationType());
            result.setOrganizationId(category.getOrganizationId());
            result.setAreaId(category.getAreaId());
            result.setLocatorId(category.getLocatorId());
            result.setLocatorCapacity(category.getLocatorCapacity());
            result.setFromScheduleRateFlag(category.getFromScheduleRateFlag());
            result.setMaterialConsumeRateUomId(category.getMaterialConsumeRateUomId());
            result.setMaterialConsumeRate(category.getMaterialConsumeRate());
            result.setBufferInventory(category.getBufferInventory());
            result.setBufferPeriod(category.getBufferPeriod());
            result.setMinInventory(category.getMinInventory());
            result.setMaxInventory(category.getMaxInventory());
            result.setPackQty(category.getPackQty());
            result.setMultiplesOfPackFlag(category.getMultiplesOfPackFlag());
            result.setRouteLocatorId(category.getAreaLocatorId());
            // API{areaDistrubutionPropertyGet}循环获取剩余输出参数
            MtModAreaDistributionVO distributionVO = new MtModAreaDistributionVO();
            distributionVO.setAreaId(category.getAreaId());
            distributionVO.setLocatorId(category.getLocatorId());
            MtModAreaDistributionVO2 propertyGet =
                            mtModAreaDistributionRepository.areaDistributionPropertyGet(tenantId, distributionVO);
            if (propertyGet == null) {
                throw new MtException("MT_MATERIAL_0092", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0092", "MATERIAL", "【API:materialCategoryDistributionPfepQuery】"));
            }
            result.setDistributionMode(propertyGet.getDistributionMode());
            result.setPullTimeIntervalFlag(propertyGet.getPullTimeIntervalFlag());
            result.setDistributionCycle(propertyGet.getDistributionCycle());
            result.setBusinessType(propertyGet.getBusinessType());
            result.setInstructCreatedByEo(propertyGet.getInstructCreatedByEo());
            result.setSourceLocatorId(propertyGet.getSourceLocatorId());
            result.setSequence(propertyGet.getSequence());
            result.setPullToArrive(propertyGet.getPullToArrive());

            resultList.add(result);
        }
        return resultList;
    }
}
