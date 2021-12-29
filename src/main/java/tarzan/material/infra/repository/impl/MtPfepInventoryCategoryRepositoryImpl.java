package tarzan.material.infra.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.material.domain.entity.MtMaterialCategorySite;
import tarzan.material.domain.entity.MtPfepInventoryCategory;
import tarzan.material.domain.repository.MtMaterialCategorySiteRepository;
import tarzan.material.domain.repository.MtPfepInventoryCategoryRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtPfepInventoryCategoryVO;
import tarzan.material.infra.mapper.MtPfepInventoryCategoryMapper;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;

/**
 * 物料类别存储属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Component
public class MtPfepInventoryCategoryRepositoryImpl extends BaseRepositoryImpl<MtPfepInventoryCategory>
                implements MtPfepInventoryCategoryRepository {

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtMaterialCategorySiteRepository mtMaterialCategorySiteRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtPfepInventoryCategoryMapper mtPfepInventoryCategoryMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String materialCategoryPfepInventoryUpdate(Long tenantId, MtPfepInventoryCategoryVO vo, String fullUpdate) {
        List<String> uomIds = new ArrayList<>();
        List<String> identifyTypes = new ArrayList<>();
        String pfepInventoryCategoryId = "";
        if (StringUtils.isNotEmpty(vo.getWeightUomId())) {
            uomIds.add(vo.getWeightUomId());
        }
        if (StringUtils.isNotEmpty(vo.getPackageSizeUomId())) {
            uomIds.add(vo.getPackageSizeUomId());
        }
        if (uomIds.size() != 0 && CollectionUtils.isEmpty(mtUomRepository.uomPropertyBatchGet(tenantId, uomIds))) {
            throw new MtException("MT_MATERIAL_0055",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                            "weightUomId（packageSizeUomId）",
                                            "【API:materialCategoryPfepInventoryUpdate】"));
        }
        if (StringUtils.isNotEmpty(vo.getIssuedLocatorId())) {
            identifyTypes.add(vo.getIssuedLocatorId());
        }
        if (StringUtils.isNotEmpty(vo.getCompletionLocatorId())) {
            identifyTypes.add(vo.getCompletionLocatorId());
        }
        if (StringUtils.isNotEmpty(vo.getStockLocatorId())) {
            identifyTypes.add(vo.getStockLocatorId());
        }
        if (identifyTypes.size() != 0 && CollectionUtils
                        .isEmpty(mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, identifyTypes))) {
            throw new MtException("MT_MATERIAL_0055",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                            " issueLocatorId（completeLocationId或stockLocatorId）",
                                            "【API:materialCategoryPfepInventoryUpdate】"));
        }
        MtGenTypeVO2 mtGenTypeVO = new MtGenTypeVO2();
        mtGenTypeVO.setModule("MATERIAL");
        mtGenTypeVO.setTypeGroup("PFEP_ORGANIZATION_TYPE");
        List<MtGenType> mtGenTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO);
        if (mtGenTypes.stream().noneMatch(t -> vo.getOrganizationType().equalsIgnoreCase(t.getTypeCode()))) {
            throw new MtException("MT_MATERIAL_0070",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0070", "MATERIAL",
                                            "organizationType", "【API:materialCategoryPfepInventoryUpdate】"));
        }
        if (StringUtils.isNotEmpty(vo.getOrganizationType()) && StringUtils.isEmpty(vo.getOrganizationId())
                        || StringUtils.isEmpty(vo.getOrganizationType())
                                        && StringUtils.isNotEmpty(vo.getOrganizationId())) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            "（organizationType、organizationId）",
                                            "【API:materialCategoryPfepInventoryUpdate】"));
        }
        if (StringUtils.isEmpty(vo.getMaterialCategoryId()) && StringUtils.isEmpty(vo.getMaterialCategorySiteId())) {
            throw new MtException("MT_MATERIAL_0076",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0076", "MATERIAL",
                                            "（materialCategoryId", "materialCategorySiteId ",
                                            "【API:materialCategoryPfepInventoryUpdate】"));
        }
        if (StringUtils.isEmpty(vo.getMaterialCategoryId()) && StringUtils.isNotEmpty(vo.getSiteId())
                        || StringUtils.isNotEmpty(vo.getMaterialCategoryId()) && StringUtils.isEmpty(vo.getSiteId())) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            "materialCategoryId、siteId", "【API:materialCategoryPfepInventoryUpdate】"));
        }
        if (StringUtils.isNotEmpty(vo.getSiteId())) {
            MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, vo.getSiteId());
            if (!"MANUFACTURING".equalsIgnoreCase(mtModSite.getSiteType())) {
                throw new MtException("MT_MATERIAL_0078",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0078", "MATERIAL",
                                                "MANUFACTURING", "【API:materialCategoryPfepInventoryUpdate】"));
            }
        }
        String materialCategorySiteId = "";
        if (StringUtils.isNotEmpty(vo.getMaterialCategorySiteId()) && StringUtils.isNotEmpty(vo.getMaterialCategoryId())
                        && StringUtils.isNotEmpty(vo.getSiteId())) {
            MtMaterialCategorySite categorySite = new MtMaterialCategorySite();
            categorySite.setMaterialCategoryId(vo.getMaterialCategoryId());
            categorySite.setSiteId(vo.getSiteId());
            materialCategorySiteId = mtMaterialCategorySiteRepository.materialCategorySiteLimitRelationGet(tenantId,
                            categorySite);
            if (!materialCategorySiteId.equals(vo.getMaterialCategorySiteId())) {
                throw new MtException("MT_MATERIAL_0077",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0077", "MATERIAL",
                                                "materialCategoryId", "siteId", "materialCategorySiteId",
                                                "【API:materialCategoryPfepInventoryUpdate】"));
            }
        }
        if (StringUtils.isNotEmpty(vo.getMaterialCategorySiteId())) {
            MtMaterialCategorySite mtMaterialCategorySite = mtMaterialCategorySiteRepository
                            .relationLimitMaterialCategorySiteGet(tenantId, vo.getMaterialCategorySiteId());
            if (StringUtils.isEmpty(mtMaterialCategorySite.getSiteId())) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                                "materialCategorySiteId ",
                                                "【API:materialCategoryPfepInventoryUpdate】"));
            }
            MtModSite mtModSite =
                            mtModSiteRepository.siteBasicPropertyGet(tenantId, mtMaterialCategorySite.getSiteId());
            if (!"MANUFACTURING".equalsIgnoreCase(mtModSite.getSiteType())) {
                throw new MtException("MT_MATERIAL_0078",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0078", "MATERIAL",
                                                "MANUFACTURING ", "【API:materialCategoryPfepInventoryUpdate】"));
            } else {
                materialCategorySiteId = mtMaterialCategorySite.getMaterialCategorySiteId();
            }

        }
        if (StringUtils.isNotEmpty(vo.getMaterialCategoryId()) && StringUtils.isNotEmpty(vo.getSiteId())) {
            MtMaterialCategorySite categorySite = new MtMaterialCategorySite();
            categorySite.setMaterialCategoryId(vo.getMaterialCategoryId());
            categorySite.setSiteId(vo.getSiteId());
            materialCategorySiteId = mtMaterialCategorySiteRepository.materialCategorySiteLimitRelationGet(tenantId,
                            categorySite);

            if (StringUtils.isEmpty(materialCategorySiteId)) {
                throw new MtException("MT_MATERIAL_0075", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0075", "MATERIAL", "【API:materialCategoryPfepInventoryUpdate】"));
            }
        }
        if (StringUtils.isNotEmpty(vo.getPfepInventoryCategoryId())) {
            MtPfepInventoryCategory mtPfepInventoryCategory = new MtPfepInventoryCategory();
            mtPfepInventoryCategory.setTenantId(tenantId);
            mtPfepInventoryCategory.setPfepInventoryCategoryId(vo.getPfepInventoryCategoryId());
            mtPfepInventoryCategory = mtPfepInventoryCategoryMapper.selectOne(mtPfepInventoryCategory);
            if (null == mtPfepInventoryCategory) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                                "pfepInventoryCategoryId",
                                                "【API:materialCategoryPfepInventoryUpdate】"));
            }

            // 更新
            mtPfepInventoryCategory.setIdentifyType(vo.getIdentifyType());
            mtPfepInventoryCategory.setIdentifyId(vo.getIdentifyId());
            mtPfepInventoryCategory.setStockLocatorId(vo.getStockLocatorId());
            mtPfepInventoryCategory.setMaxStockQty(vo.getMaxStockQty());
            mtPfepInventoryCategory.setMinStockQty(vo.getMinStockQty());
            mtPfepInventoryCategory.setPackageWeight(vo.getPackageWeight());
            mtPfepInventoryCategory.setWeightUomId(vo.getWeightUomId());
            mtPfepInventoryCategory.setPackageLength(vo.getPackageLength());
            mtPfepInventoryCategory.setPackageWidth(vo.getPackageWidth());
            mtPfepInventoryCategory.setPackageHeight(vo.getPackageHeight());
            mtPfepInventoryCategory.setPackageSizeUomId(vo.getPackageSizeUomId());
            mtPfepInventoryCategory.setMaterialCategorySiteId(materialCategorySiteId);
            mtPfepInventoryCategory.setOrganizationType(vo.getOrganizationType());
            mtPfepInventoryCategory.setOrganizationId(vo.getOrganizationId());
            mtPfepInventoryCategory.setIssuedLocatorId(vo.getIssuedLocatorId());
            mtPfepInventoryCategory.setCompletionLocatorId(vo.getCompletionLocatorId());
            if ("Y".equalsIgnoreCase(fullUpdate)) {
                mtPfepInventoryCategory = (MtPfepInventoryCategory) ObjectFieldsHelper
                                .setStringFieldsEmpty(mtPfepInventoryCategory);
                self().updateByPrimaryKey(mtPfepInventoryCategory);
                pfepInventoryCategoryId = mtPfepInventoryCategory.getPfepInventoryCategoryId();
            } else {
                self().updateByPrimaryKeySelective(mtPfepInventoryCategory);
                pfepInventoryCategoryId = mtPfepInventoryCategory.getPfepInventoryCategoryId();
            }
        }
        if (StringUtils.isEmpty(vo.getPfepInventoryCategoryId()) && StringUtils.isNotEmpty(vo.getOrganizationId())
                        && StringUtils.isNotEmpty(vo.getOrganizationType())) {
            MtPfepInventoryCategory mtPfepInventoryCategory = new MtPfepInventoryCategory();
            mtPfepInventoryCategory.setTenantId(tenantId);
            mtPfepInventoryCategory.setOrganizationType(vo.getOrganizationType());
            mtPfepInventoryCategory.setOrganizationId(vo.getOrganizationId());
            mtPfepInventoryCategory.setMaterialCategorySiteId(materialCategorySiteId);
            mtPfepInventoryCategory = mtPfepInventoryCategoryMapper.selectOne(mtPfepInventoryCategory);

            if (null == mtPfepInventoryCategory) {
                // 新增
                if (StringUtils.isEmpty(vo.getEnableFlag())) {
                    throw new MtException("MT_MATERIAL_0071",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0071",
                                                    "MATERIAL", "enableFlag",
                                                    "【API:materialCategoryPfepInventoryUpdate】"));
                }
                MtPfepInventoryCategory pfepInventoryCategory = new MtPfepInventoryCategory();
                BeanUtils.copyProperties(vo, pfepInventoryCategory);
                self().insertSelective(pfepInventoryCategory);
                pfepInventoryCategoryId = pfepInventoryCategory.getPfepInventoryCategoryId();
            } else {
                // 更新
                if (null != vo.getEnableFlag()) {
                    mtPfepInventoryCategory.setIdentifyType(vo.getIdentifyType());
                    mtPfepInventoryCategory.setIdentifyId(vo.getIdentifyId());
                    mtPfepInventoryCategory.setStockLocatorId(vo.getStockLocatorId());
                    mtPfepInventoryCategory.setMaxStockQty(vo.getMaxStockQty());
                    mtPfepInventoryCategory.setMinStockQty(vo.getMinStockQty());
                    mtPfepInventoryCategory.setPackageWeight(vo.getPackageWeight());
                    mtPfepInventoryCategory.setWeightUomId(vo.getWeightUomId());
                    mtPfepInventoryCategory.setPackageLength(vo.getPackageLength());
                    mtPfepInventoryCategory.setPackageWidth(vo.getPackageWidth());
                    mtPfepInventoryCategory.setPackageHeight(vo.getPackageHeight());
                    mtPfepInventoryCategory.setPackageSizeUomId(vo.getPackageSizeUomId());
                    mtPfepInventoryCategory.setMaterialCategorySiteId(materialCategorySiteId);
                    mtPfepInventoryCategory.setOrganizationType(vo.getOrganizationType());
                    mtPfepInventoryCategory.setOrganizationId(vo.getOrganizationId());
                    mtPfepInventoryCategory.setIssuedLocatorId(vo.getIssuedLocatorId());
                    mtPfepInventoryCategory.setCompletionLocatorId(vo.getCompletionLocatorId());
                    if ("Y".equalsIgnoreCase(fullUpdate)) {
                        mtPfepInventoryCategory = (MtPfepInventoryCategory) ObjectFieldsHelper
                                        .setStringFieldsEmpty(mtPfepInventoryCategory);
                        self().updateByPrimaryKey(mtPfepInventoryCategory);
                        pfepInventoryCategoryId = mtPfepInventoryCategory.getPfepInventoryCategoryId();
                    } else {
                        self().updateByPrimaryKeySelective(mtPfepInventoryCategory);
                        pfepInventoryCategoryId = mtPfepInventoryCategory.getPfepInventoryCategoryId();
                    }
                }
            }
        }
        return pfepInventoryCategoryId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pfepInventoryCatgAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        if (StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId", "【API:pfepInventoryCatgAttrPropertyUpdate】"));
        }
        // 获取主表数据
        MtPfepInventoryCategory mtPfepInventoryCategory = new MtPfepInventoryCategory();
        mtPfepInventoryCategory.setTenantId(tenantId);
        mtPfepInventoryCategory.setPfepInventoryCategoryId(dto.getKeyId());
        mtPfepInventoryCategory = mtPfepInventoryCategoryMapper.selectOne(mtPfepInventoryCategory);
        if (mtPfepInventoryCategory == null
                        || StringUtils.isEmpty(mtPfepInventoryCategory.getPfepInventoryCategoryId())) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            "keyId:" + dto.getKeyId(), "mt_pfep_inventory_category",
                                            "【API:pfepInventoryCatgAttrPropertyUpdate】"));
        }

        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_pfep_inventory_catg_attr", dto.getKeyId(),
                        dto.getEventId(), dto.getAttrs());
    }
}
