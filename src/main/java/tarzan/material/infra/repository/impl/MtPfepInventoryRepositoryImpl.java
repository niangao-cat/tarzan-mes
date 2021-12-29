package tarzan.material.infra.repository.impl;

import com.google.common.collect.Lists;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.MtBeanUtils;
import io.tarzan.common.domain.util.MtFieldsHelper;
import io.tarzan.common.domain.util.MtIdHelper;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.material.domain.entity.*;
import tarzan.material.domain.repository.MtMaterialCategoryAssignRepository;
import tarzan.material.domain.repository.MtMaterialCategorySiteRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtPfepInventoryRepository;
import tarzan.material.domain.trans.MtPfepInventoryTransMapper;
import tarzan.material.domain.vo.*;
import tarzan.material.infra.mapper.MtMaterialSiteMapper;
import tarzan.material.infra.mapper.MtPfepInventoryCategoryMapper;
import tarzan.material.infra.mapper.MtPfepInventoryMapper;
import tarzan.material.infra.mapper.MtUomMapper;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 物料存储属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Component
public class MtPfepInventoryRepositoryImpl extends BaseRepositoryImpl<MtPfepInventory>
                implements MtPfepInventoryRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtMaterialCategorySiteRepository iMtMaterialCategorySiteService;

    @Autowired
    private MtMaterialSiteRepository materialSiteService;

    @Autowired
    private MtMaterialCategoryAssignRepository imtMaterialCategoryAssignService;

    @Autowired
    private MtPfepInventoryMapper mtPfepInventoryMapper;

    @Autowired
    private MtPfepInventoryCategoryMapper mtPfepInventoryCategoryMapper;

    @Autowired
    private MtUomMapper mtUomMapper;

    @Autowired
    private MtModLocatorRepository modLocatorRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;

    @Autowired
    private MtMaterialSiteMapper mtMaterialSiteMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtMaterialCategoryAssignRepository mtMaterialCategoryAssignRepository;

    @Autowired
    private  MtMaterialCategorySiteRepository mtMaterialCategorySiteRepository;

    @Autowired
    private ThreadPoolTaskExecutor poolExecutor;

    @Autowired
    private MtPfepInventoryTransMapper transMapper;


    @Override
    public MtPfepInventory pfepInventoryGet(Long tenantId, MtPfepInventoryVO dto) {
        return getInventory(tenantId, dto, "normal", "【API:pfepInventoryGet】");
    }

    @Override
    public MtPfepInventory pfepDefaultManufacturingLocationGet(Long tenantId, MtPfepInventoryVO dto) {
        return getInventory(tenantId, dto, "location", "【API:pfepDefaultManufacturingLocationGet】");
    }

    private MtPfepInventory getInventory(Long tenantId, MtPfepInventoryVO dto, String type, String api) {
        MtPfepInventory inventory = new MtPfepInventory();

        inventory.setTenantId(tenantId);
        inventory.setIdentifyType("");
        inventory.setIdentifyId("");
        inventory.setStockLocatorId("");
        inventory.setMaxStockQty(null);
        inventory.setMinStockQty(null);
        inventory.setMinStockQty(null);
        inventory.setMinStockQty(null);
        inventory.setPackageWeight(null);
        inventory.setWeightUomId("");
        inventory.setPackageLength(null);
        inventory.setPackageWidth(null);
        inventory.setPackageHeight(null);
        inventory.setPackageSizeUomId("");

        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", api));
        }
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "siteId", api));
        }
        if (StringUtils.isEmpty(dto.getOrganizationId()) != StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_MATERIAL_0004",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0004", api));
        }

        MtMaterialSite materialSite = new MtMaterialSite();
        materialSite.setMaterialId(dto.getMaterialId());
        materialSite.setSiteId(dto.getSiteId());
        String materialSiteId = materialSiteService.materialSiteLimitRelationGet(tenantId, materialSite);

        MtMaterialCategoryAssignVO assignVO = new MtMaterialCategoryAssignVO();
        assignVO.setMaterialId(dto.getMaterialId());
        assignVO.setSiteId(dto.getSiteId());
        assignVO.setDefaultType("MANUFACTURING");
        String materialCategoryId = imtMaterialCategoryAssignService.defaultSetMaterialAssignCategoryGet(tenantId, assignVO);
        String materialCategorySiteId = "";
        if (StringUtils.isNotEmpty(materialCategoryId)) {
            MtMaterialCategorySite categorySite = new MtMaterialCategorySite();
            categorySite.setMaterialCategoryId(materialCategoryId);
            categorySite.setSiteId(dto.getSiteId());
            materialCategorySiteId =
                            iMtMaterialCategorySiteService.materialCategorySiteLimitRelationGet(tenantId, categorySite);

        }
        boolean existsNull = true;

        if (StringUtils.isNotEmpty(dto.getOrganizationId())) {

            MtPfepInventory inventory1 = new MtPfepInventory();
            inventory1.setTenantId(tenantId);
            inventory1.setMaterialSiteId(materialSiteId);
            inventory1.setEnableFlag("Y");
            inventory1.setOrganizationId(dto.getOrganizationId());
            inventory1.setOrganizationType(dto.getOrganizationType());
            inventory1 = mtPfepInventoryMapper.selectOne(inventory1);
            if (inventory1 != null) {
                existsNull = setFromMaterial(inventory, inventory1, type);
            }

            if (existsNull && StringUtils.isNotEmpty(materialCategorySiteId)) {
                MtPfepInventoryCategory inventoryCategory1 = new MtPfepInventoryCategory();
                inventoryCategory1.setTenantId(tenantId);
                inventoryCategory1.setMaterialCategorySiteId(materialCategorySiteId);
                inventoryCategory1.setEnableFlag("Y");
                if (StringUtils.isNotEmpty(dto.getOrganizationId())) {
                    inventoryCategory1.setOrganizationId(dto.getOrganizationId());
                    inventoryCategory1.setOrganizationType(dto.getOrganizationType());
                }
                inventoryCategory1 = mtPfepInventoryCategoryMapper.selectOne(inventoryCategory1);
                if (inventoryCategory1 != null) {
                    existsNull = setFromCategory(inventory, inventoryCategory1, type);
                }
            }
        }
        if (existsNull) {
            MtPfepInventory inventory2 = new MtPfepInventory();
            inventory2.setTenantId(tenantId);
            inventory2.setMaterialSiteId(materialSiteId);
            inventory2.setEnableFlag("Y");
            inventory2.setOrganizationId("");
            inventory2.setOrganizationType("");
            inventory2 = mtPfepInventoryMapper.selectOne(inventory2);
            if (inventory2 != null) {
                existsNull = setFromMaterial(inventory, inventory2, type);
            }
        }
        if (existsNull && StringUtils.isNotEmpty(materialCategorySiteId)) {
            MtPfepInventoryCategory inventoryCategory2 = new MtPfepInventoryCategory();
            inventoryCategory2.setTenantId(tenantId);
            inventoryCategory2.setMaterialCategorySiteId(materialCategorySiteId);
            inventoryCategory2.setEnableFlag("Y");
            inventoryCategory2.setOrganizationId("");
            inventoryCategory2.setOrganizationType("");
            inventoryCategory2 = mtPfepInventoryCategoryMapper.selectOne(inventoryCategory2);
            if (inventoryCategory2 != null) {
                setFromCategory(inventory, inventoryCategory2, type);
            }
        }
        return inventory;
    }

    private boolean setFromMaterial(MtPfepInventory source, MtPfepInventory target, String type) {
        boolean existsNull = false;
        switch (type) {
            case "location":
                if (StringUtils.isEmpty(source.getIssuedLocatorId())) {
                    source.setIssuedLocatorId(target.getIssuedLocatorId());
                }
                if (StringUtils.isEmpty(source.getCompletionLocatorId())) {
                    source.setCompletionLocatorId(target.getCompletionLocatorId());
                }
                if (StringUtils.isEmpty(source.getIssuedLocatorId())
                                || StringUtils.isEmpty(source.getCompletionLocatorId())) {
                    existsNull = true;
                }
                break;
            case "normal":
                if (StringUtils.isEmpty(source.getIdentifyType())) {
                    source.setIdentifyType(target.getIdentifyType());
                }
                if (StringUtils.isEmpty(source.getIdentifyId())) {
                    source.setIdentifyId(target.getIdentifyId());
                }
                if (StringUtils.isEmpty(source.getStockLocatorId())) {
                    source.setStockLocatorId(target.getStockLocatorId());
                }
                if (source.getMaxStockQty() == null) {
                    source.setMaxStockQty(target.getMaxStockQty());
                }
                if (source.getMinStockQty() == null) {
                    source.setMinStockQty(target.getMinStockQty());
                }
                if (source.getMinStockQty() == null) {
                    source.setMinStockQty(target.getMinStockQty());
                }
                if (source.getMinStockQty() == null) {
                    source.setMinStockQty(target.getMinStockQty());
                }
                if (source.getPackageWeight() == null) {
                    source.setPackageWeight(target.getPackageWeight());
                }
                if (StringUtils.isEmpty(source.getWeightUomId())) {
                    source.setWeightUomId(target.getWeightUomId());
                }
                if (source.getPackageLength() == null) {
                    source.setPackageLength(target.getPackageLength());
                }
                if (source.getPackageWidth() == null) {
                    source.setPackageWidth(target.getPackageWidth());
                }
                if (source.getPackageHeight() == null) {
                    source.setPackageHeight(target.getPackageHeight());
                }
                if (StringUtils.isEmpty(source.getPackageSizeUomId())) {
                    source.setPackageSizeUomId(target.getPackageSizeUomId());
                }
                if (StringUtils.isEmpty(source.getIdentifyType()) || StringUtils.isEmpty(source.getIdentifyId())
                                || StringUtils.isEmpty(source.getStockLocatorId()) || source.getMaxStockQty() == null
                                || source.getMinStockQty() == null || source.getPackageWeight() == null
                                || StringUtils.isEmpty(source.getWeightUomId()) || source.getPackageLength() == null
                                || source.getPackageWidth() == null || source.getPackageHeight() == null
                                || StringUtils.isEmpty(source.getPackageSizeUomId())) {
                    existsNull = true;
                }
                break;
            default:
                break;
        }
        return existsNull;
    }

    private boolean setFromCategory(MtPfepInventory source, MtPfepInventoryCategory target, String type) {

        boolean existsNull = false;
        switch (type) {
            case "location":
                if (StringUtils.isEmpty(source.getIssuedLocatorId())) {
                    source.setIssuedLocatorId(target.getIssuedLocatorId());
                }
                if (StringUtils.isEmpty(source.getCompletionLocatorId())) {
                    source.setCompletionLocatorId(target.getCompletionLocatorId());
                }

                if (StringUtils.isEmpty(source.getIssuedLocatorId())
                                || StringUtils.isEmpty(source.getCompletionLocatorId())) {
                    existsNull = true;
                }
                break;
            case "normal":

                if (StringUtils.isEmpty(source.getIdentifyType())) {
                    source.setIdentifyType(target.getIdentifyType());
                }

                if (StringUtils.isEmpty(source.getIdentifyId())) {
                    source.setIdentifyId(target.getIdentifyId());
                }

                if (StringUtils.isEmpty(source.getStockLocatorId())) {
                    source.setStockLocatorId(target.getStockLocatorId());
                }

                if (source.getMaxStockQty() == null) {
                    source.setMaxStockQty(target.getMaxStockQty());
                }

                if (source.getMinStockQty() == null) {
                    source.setMinStockQty(target.getMinStockQty());
                }

                if (source.getMinStockQty() == null) {
                    source.setMinStockQty(target.getMinStockQty());
                }

                if (source.getMinStockQty() == null) {
                    source.setMinStockQty(target.getMinStockQty());
                }

                if (source.getPackageWeight() == null) {
                    source.setPackageWeight(target.getPackageWeight());
                }

                if (StringUtils.isEmpty(source.getWeightUomId())) {
                    source.setWeightUomId(target.getWeightUomId());
                }

                if (source.getPackageLength() == null) {
                    source.setPackageLength(target.getPackageLength());
                }

                if (source.getPackageWidth() == null) {
                    source.setPackageWidth(target.getPackageWidth());
                }

                if (source.getPackageHeight() == null) {
                    source.setPackageHeight(target.getPackageHeight());
                }

                if (StringUtils.isEmpty(source.getPackageSizeUomId())) {
                    source.setPackageSizeUomId(target.getPackageSizeUomId());
                }

                if (StringUtils.isEmpty(source.getIdentifyType()) || StringUtils.isEmpty(source.getIdentifyId())
                                || StringUtils.isEmpty(source.getStockLocatorId()) || source.getMaxStockQty() == null
                                || source.getMinStockQty() == null || source.getPackageWeight() == null
                                || StringUtils.isEmpty(source.getWeightUomId()) || source.getPackageLength() == null
                                || source.getPackageWidth() == null || source.getPackageHeight() == null
                                || StringUtils.isEmpty(source.getPackageSizeUomId())) {
                    existsNull = true;
                }
                break;
            default:
                break;
        }

        return existsNull;
    }

    @Override
    public void materialIdentifyTypeValidate(Long tenantId, MtPfepInventoryVO3 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getOrganizationId()) != StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_MATERIAL_0004", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0004", "MATERIAL", "【API:materialIdentifyTypeValidate】"));
        }
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "siteId", "【API:materialIdentifyTypeValidate】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:materialIdentifyTypeValidate】"));
        }
        if (StringUtils.isEmpty(dto.getIdentifyType())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "identifyType", "【API:materialIdentifyTypeValidate】"));
        }

        // 判断输入类型是否为MATERIAL
        boolean isMaterial = "MATERIAL".equals(dto.getIdentifyType());

        // 获取公用参数
        MtMaterialSite mtMaterialSite = new MtMaterialSite();
        mtMaterialSite.setMaterialId(dto.getMaterialId());
        mtMaterialSite.setSiteId(dto.getSiteId());
        String materialSiteId = materialSiteService.materialSiteLimitRelationGet(tenantId, mtMaterialSite);

        MtMaterialCategoryAssignVO assignVO = new MtMaterialCategoryAssignVO();
        assignVO.setMaterialId(dto.getMaterialId());
        assignVO.setSiteId(dto.getSiteId());
        assignVO.setDefaultType("MANUFACTURING");
        String materialCategoryId =
                        imtMaterialCategoryAssignService.defaultSetMaterialAssignCategoryGet(tenantId, assignVO);

        MtMaterialCategorySite mtMaterialCategorySite = new MtMaterialCategorySite();
        mtMaterialCategorySite.setMaterialCategoryId(materialCategoryId);
        mtMaterialCategorySite.setSiteId(dto.getSiteId());
        String materialCategorySiteId = iMtMaterialCategorySiteService.materialCategorySiteLimitRelationGet(tenantId,
                        mtMaterialCategorySite);

        // 2. 获取物料站点下PFEP存储属性中的标识类型 (同时有值）
        if (StringUtils.isNotEmpty(dto.getOrganizationId()) && StringUtils.isNotEmpty(dto.getOrganizationType())) {
            /*
             * 2.1. 首先根据输入的物料ID、站点ID、组织类型、组织ID， 从MT_PFEP_INVENTORY获取物料的标识类型IDENTIFY_TYPE
             */
            MtPfepInventory mtPfepInventory = new MtPfepInventory();
            mtPfepInventory.setMaterialSiteId(materialSiteId);
            mtPfepInventory.setEnableFlag("Y");
            mtPfepInventory.setOrganizationId(dto.getOrganizationId());
            mtPfepInventory.setOrganizationType(dto.getOrganizationType());
            mtPfepInventory = mtPfepInventoryMapper.mySelectOne(tenantId, mtPfepInventory);
            if (mtPfepInventory != null && StringUtils.isNotEmpty(mtPfepInventory.getIdentifyType())) {
                if (!mtPfepInventory.getIdentifyType().equals(dto.getIdentifyType())) {
                    throw new MtException("MT_MATERIAL_0069",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0069",
                                                    "MATERIAL", mtPfepInventory.getIdentifyType(),
                                                    dto.getIdentifyType(), "【API:materialIdentifyTypeValidate】"));
                } else {
                    // 相同则检验通过
                    return;
                }
            }

            /*
             * 2.2. 获取物料站点下默认类别集的物料类别，然后通过物料类别ID+站点ID+组织类型+组织ID
             * 获取MT_PFEP_INVENTORY_CATEGORY中物料的标识类型IDENTIFY_TYPE数据（限制有效性为Y）
             */
            if (StringUtils.isNotEmpty(materialCategoryId)) {

                MtPfepInventoryCategory mtPfepInventoryCategory = new MtPfepInventoryCategory();
                mtPfepInventoryCategory.setMaterialCategorySiteId(materialCategorySiteId);
                mtPfepInventoryCategory.setEnableFlag("Y");
                mtPfepInventoryCategory.setOrganizationId(dto.getOrganizationId());
                mtPfepInventoryCategory.setOrganizationType(dto.getOrganizationType());
                mtPfepInventoryCategory = mtPfepInventoryCategoryMapper.mySelectOne(tenantId, mtPfepInventoryCategory);
                if (mtPfepInventoryCategory != null
                                && StringUtils.isNotEmpty(mtPfepInventoryCategory.getIdentifyType())) {
                    if (!mtPfepInventoryCategory.getIdentifyType().equals(dto.getIdentifyType())) {
                        throw new MtException("MT_MATERIAL_0069",
                                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0069",
                                                        "MATERIAL", mtPfepInventoryCategory.getIdentifyType(),
                                                        dto.getIdentifyType(), "【API:materialIdentifyTypeValidate】"));
                    } else {
                        // 相同则检验通过
                        return;
                    }
                }
            }

            materialIdentifyTypeValidateEmpty(tenantId, dto, materialSiteId, materialCategorySiteId, isMaterial);

        } else {
            // 3. 获取物料站点下PFEP存储属性中的标识类型 (同时无值）
            materialIdentifyTypeValidateEmpty(tenantId, dto, materialSiteId, materialCategorySiteId, isMaterial);
        }
    }

    /**
     * materialIdentifyTypeValidate-获取物料的标识管控类型 公共代码提炼，按organizationId、organizationType空查询的逻辑
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/7/23
     */
    private void materialIdentifyTypeValidateEmpty(Long tenantId, MtPfepInventoryVO3 dto, String materialSiteId,
                                                   String materialCategorySiteId, boolean isMaterial) {
        /*
         * 2.3. 将输入的物料ID、站点ID作为参数，同时限定组织类型、组织ID为空， 从MT_PFEP_INVENTORY获取物料的标识类型IDENTIFY_TYPE（限制有效性为Y）
         */
        MtPfepInventory mtPfepInventory = new MtPfepInventory();
        mtPfepInventory.setMaterialSiteId(materialSiteId);
        mtPfepInventory.setEnableFlag("Y");
        mtPfepInventory.setOrganizationId("");
        mtPfepInventory.setOrganizationType("");
        mtPfepInventory = mtPfepInventoryMapper.mySelectOne(tenantId, mtPfepInventory);
        if (mtPfepInventory != null && StringUtils.isNotEmpty(mtPfepInventory.getIdentifyType())) {
            if (!mtPfepInventory.getIdentifyType().equals(dto.getIdentifyType())) {
                throw new MtException("MT_MATERIAL_0069",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0069", "MATERIAL",
                                                mtPfepInventory.getIdentifyType(), dto.getIdentifyType(),
                                                "【API:materialIdentifyTypeValidate】"));
            } else {
                // 相同则检验通过
                return;
            }
        }

        /*
         * 获取物料站点下默认类别集的物料类别，然后通过物料类别ID+站点ID、限定组织类型、组织ID为空，
         * 从MT_PFEP_INVENTORY_CATEGORY中获取物料的标识类型IDENTIFY_TYPE（限制有效性为Y）
         */
        MtPfepInventoryCategory mtPfepInventoryCategory = new MtPfepInventoryCategory();
        mtPfepInventoryCategory.setMaterialCategorySiteId(materialCategorySiteId);
        mtPfepInventoryCategory.setEnableFlag("Y");
        mtPfepInventoryCategory.setOrganizationId("");
        mtPfepInventoryCategory.setOrganizationType("");
        mtPfepInventoryCategory = mtPfepInventoryCategoryMapper.mySelectOne(tenantId, mtPfepInventoryCategory);
        if (mtPfepInventoryCategory != null && StringUtils.isNotEmpty(mtPfepInventoryCategory.getIdentifyType())) {
            if (!mtPfepInventoryCategory.getIdentifyType().equals(dto.getIdentifyType())) {
                throw new MtException("MT_MATERIAL_0069",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0069", "MATERIAL",
                                                mtPfepInventoryCategory.getIdentifyType(), dto.getIdentifyType(),
                                                "【API:materialIdentifyTypeValidate】"));
            }
        } else {
            /*
             * 若获取到IDENTIFY_TYPE无值或值为空 输入值identifyType不为MATERIAL，返回验证不通过
             */
            if (!isMaterial) {
                throw new MtException("MT_MATERIAL_0069",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0069", "MATERIAL",
                                                "MATERIAL", dto.getIdentifyType(),
                                                "【API:materialIdentifyTypeValidate】"));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String materialPfepInventoryUpdate(Long tenantId, MtPfepInventoryVO6 dto, String fullUpdate) {
        // 1.判断参数合规性
        if (StringUtils.isNotEmpty(dto.getWeightUomId())) {
            MtUom mtUom = mtUomMapper.selectByPrimaryKey(dto.getWeightUomId());
            if (mtUom == null) {
                throw new MtException("MT_MATERIAL_0055", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0055", "MATERIAL", "weightUomId", "【API:materialPfepInventoryUpdate】"));
            }
        }
        if (StringUtils.isNotEmpty(dto.getPackageSizeUomId())) {
            MtUom mtUom = mtUomMapper.selectByPrimaryKey(dto.getPackageSizeUomId());
            if (mtUom == null) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                                "packageSizeUomId", "【API:materialPfepInventoryUpdate】"));
            }
        }
        if (StringUtils.isNotEmpty(dto.getIssuedLocatorId())) {
            MtModLocator mtModLocator = modLocatorRepository.selectByPrimaryKey(dto.getIssuedLocatorId());
            if (mtModLocator == null) {
                throw new MtException("MT_MATERIAL_0055", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0055", "MATERIAL", "issueLocatorId", "【API:materialPfepInventoryUpdate】"));
            }
        }
        if (StringUtils.isNotEmpty(dto.getCompletionLocatorId())) {
            MtModLocator mtModLocator = modLocatorRepository.selectByPrimaryKey(dto.getCompletionLocatorId());
            if (mtModLocator == null) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                                "completeLocationId", "【API:materialPfepInventoryUpdate】"));
            }
        }
        if (StringUtils.isNotEmpty(dto.getStockLocatorId())) {
            MtModLocator mtModLocator = modLocatorRepository.selectByPrimaryKey(dto.getStockLocatorId());
            if (mtModLocator == null) {
                throw new MtException("MT_MATERIAL_0055", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0055", "MATERIAL", "stockLocatorId", "【API:materialPfepInventoryUpdate】"));
            }
        }

        if(StringUtils.isNotEmpty(dto.getOrganizationType())) {
            // 获取物料库存类型
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("MATERIAL");
            mtGenTypeVO2.setTypeGroup("PFEP_ORGANIZATION_TYPE");
            List<MtGenType> mtGenTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
            if (CollectionUtils.isEmpty(mtGenTypes)
                    || mtGenTypes.stream().noneMatch(t -> t.getTypeCode().equals(dto.getOrganizationType()))) {
                throw new MtException("MT_MATERIAL_0070",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0070", "MATERIAL",
                                dto.getOrganizationType(), mtGenTypes.stream().map(MtGenType::getTypeCode)
                                        .collect(Collectors.toList()).toString(),
                                "【API:materialPfepInventoryUpdate】"));
            }
        }

        if (StringUtils.isEmpty(dto.getMaterialSiteId()) && StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_MATERIAL_0076",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0076", "MATERIAL",
                                            "materialId", "materialSiteId", "【API:materialPfepInventoryUpdate】"));
        }

        if (StringUtils.isEmpty(dto.getOrganizationId()) && StringUtils.isNotEmpty(dto.getOrganizationType())
                        || StringUtils.isNotEmpty(dto.getOrganizationId())
                                        && StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            "organizationType、organizationId", "【API:materialPfepInventoryUpdate】"));

        }
        if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isNotEmpty(dto.getSiteId())
                        || StringUtils.isNotEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0072", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0072", "MATERIAL", "materialId、siteId", "【API:materialPfepInventoryUpdate】"));

        }
        if (StringUtils.isNotEmpty(dto.getSiteId())) {
            // 若输入参数materialId和siteId值不为空时
            MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
            if (mtModSite != null && !"MANUFACTURING".equals(mtModSite.getSiteType())) {
                throw new MtException("MT_MATERIAL_0079", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0079", "MATERIAL", "【API:materialPfepInventoryUpdate】"));

            }
            // materialSiteId、materialId和siteId同时输入
            if (StringUtils.isNotEmpty(dto.getMaterialSiteId())) {
                MtMaterialSite mtMaterialSite = mtMaterialSiteRepository.relationLimitMaterialSiteGet(tenantId,
                                dto.getMaterialSiteId());
                if (mtMaterialSite == null || !"Y".equals(mtMaterialSite.getEnableFlag())) {
                    throw new MtException("MT_MATERIAL_0055",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                                    "MATERIAL", "materialSiteId", "【API:materialPfepInventoryUpdate】"));
                }
                if (!dto.getMaterialId().equals(mtMaterialSite.getMaterialId())
                                || !dto.getSiteId().equals(mtMaterialSite.getSiteId())
                                || !tenantId.equals(mtMaterialSite.getTenantId())) {
                    throw new MtException("MT_MATERIAL_0077",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0077",
                                                    "MATERIAL", "materialId", "siteId", "materialSiteId ",
                                                    "【API:materialPfepInventoryUpdate】"));


                }
            } else {
                MtMaterialSite mtMaterialSite = new MtMaterialSite();
                mtMaterialSite.setSiteId(dto.getSiteId());
                mtMaterialSite.setMaterialId(dto.getMaterialId());
                mtMaterialSite.setTenantId(tenantId);
                String materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId, mtMaterialSite);
                if (StringUtils.isEmpty(materialSiteId)) {
                    throw new MtException("MT_MATERIAL_0075", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_MATERIAL_0075", "MATERIAL", "【API:materialPfepInventoryUpdate】"));
                }
                dto.setMaterialSiteId(materialSiteId);
            }
        } else {
            // 当siteId为空时MaterialSiteId不为空
            MtMaterialSite mtMaterialSite =
                            mtMaterialSiteRepository.relationLimitMaterialSiteGet(tenantId, dto.getMaterialSiteId());
            if (mtMaterialSite == null || !"Y".equals(mtMaterialSite.getEnableFlag())) {
                throw new MtException("MT_MATERIAL_0055", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0055", "MATERIAL", "materialSiteId", "【API:materialPfepInventoryUpdate】"));
            }

            MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, mtMaterialSite.getSiteId());
            if (mtModSite != null && !"MANUFACTURING".equals(mtModSite.getSiteType())) {
                throw new MtException("MT_MATERIAL_0079", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0079", "MATERIAL", "【API:materialPfepInventoryUpdate】"));

            }

        }

        // 2.根据传入参数判断新增或更新模式
        MtPfepInventory mtPfepInventory = null;
        if (StringUtils.isNotEmpty(dto.getPfepInventoryId())) {
            mtPfepInventory = mtPfepInventoryMapper.selectByPrimaryKey(dto.getPfepInventoryId());
            if (mtPfepInventory == null) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                                "pfepInventoryId", "【API:materialPfepInventoryUpdate】"));
            }

        } else if (StringUtils.isNotEmpty(dto.getMaterialSiteId()) && StringUtils.isNotEmpty(dto.getOrganizationId())) {
            mtPfepInventory = new MtPfepInventory();
            mtPfepInventory.setOrganizationId(dto.getOrganizationId());
            mtPfepInventory.setOrganizationType(dto.getOrganizationType());
            mtPfepInventory.setMaterialSiteId(dto.getMaterialSiteId());
            mtPfepInventory.setTenantId(tenantId);
            mtPfepInventory = mtPfepInventoryMapper.selectOne(mtPfepInventory);
            if (mtPfepInventory == null && StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("MT_MATERIAL_0071", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0071", "MATERIAL", "enableFlag", "【API:materialPfepInventoryUpdate】"));
            }

        }
        // 3.更新数据
        String pfepInventoryId = null;
        if (mtPfepInventory != null) {
            if (StringUtils.isNotEmpty(dto.getEnableFlag())) {
                mtPfepInventory.setEnableFlag(dto.getEnableFlag());
            }
            mtPfepInventory.setMaxStockQty(dto.getMaxStockQty());
            mtPfepInventory.setMinStockQty(dto.getMinStockQty());
            mtPfepInventory.setPackageHeight(dto.getPackageHeight());
            mtPfepInventory.setPackageWeight(dto.getPackageWeight());
            mtPfepInventory.setPackageLength(dto.getPackageLength());
            mtPfepInventory.setPackageWidth(dto.getPackageWidth());

            if (dto.getIdentifyType() != null) {
                mtPfepInventory.setIdentifyType(dto.getIdentifyType());
            }
            if (dto.getIdentifyId() != null) {
                mtPfepInventory.setIdentifyId(dto.getIdentifyId());
            }
            if (dto.getStockLocatorId() != null) {
                mtPfepInventory.setStockLocatorId(dto.getStockLocatorId());
            }
            if (dto.getWeightUomId() != null) {
                mtPfepInventory.setWeightUomId(dto.getWeightUomId());
            }
            if (dto.getPackageSizeUomId() != null) {
                mtPfepInventory.setPackageSizeUomId(dto.getPackageSizeUomId());
            }
            if (dto.getMaterialSiteId() != null) {
                mtPfepInventory.setMaterialSiteId(dto.getMaterialSiteId());
            }
            if (dto.getOrganizationType() != null) {
                mtPfepInventory.setOrganizationType(dto.getOrganizationType());
            }
            if (dto.getOrganizationId() != null) {
                mtPfepInventory.setOrganizationId(dto.getOrganizationId());
            }
            if (dto.getIssuedLocatorId() != null) {
                mtPfepInventory.setIssuedLocatorId(dto.getIssuedLocatorId());
            }
            if (dto.getCompletionLocatorId() != null) {
                mtPfepInventory.setCompletionLocatorId(dto.getCompletionLocatorId());
            }
            if ("Y".equals(fullUpdate)) {
                self().updateByPrimaryKey(mtPfepInventory);
            } else {
                self().updateByPrimaryKeySelective(mtPfepInventory);
            }
            pfepInventoryId = mtPfepInventory.getPfepInventoryId();
        } else {
            // 4.新增数据
            mtPfepInventory = new MtPfepInventory();
            BeanUtils.copyProperties(dto, mtPfepInventory);
            self().insertSelective(mtPfepInventory);
            pfepInventoryId = mtPfepInventory.getPfepInventoryId();
        }
        return pfepInventoryId;
    }

    @Override
    public MtPfepInventoryVO8 pfepInventoryKidGet(Long tenantId, MtPfepInventoryVO7 dto) {
        String paramMaterialId; // 用来接受materialId 过程参数P1
        String paramMaterialSiteId = null; // 用来接受materialSiteId 过程参数P2
        String paramSiteId; // 过程参数P3
        String SITE_ID = null;
        String MATERIAL_ID = null;
        if (StringUtils.isEmpty(dto.getOrganizationType()) && StringUtils.isNotEmpty(dto.getOrganizationId())
                        || StringUtils.isNotEmpty(dto.getOrganizationType())
                                        && StringUtils.isEmpty(dto.getOrganizationId())) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            "organizationType、organizationId", "【API:pfepInventoryKidGet】"));

        }
        if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isNotEmpty(dto.getSiteId())
                        || StringUtils.isNotEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0072", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0072", "MATERIAL", "materialId、siteId", "【API:pfepInventoryKidGet】"));

        }
        if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getMaterialSiteId())) {
            throw new MtException("MT_MATERIAL_0076",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0076", "MATERIAL",
                                            "materialId", "materialSiteId ", "【API:pfepInventoryKidGet】"));
        }
        if (StringUtils.isNotEmpty(dto.getMaterialId()) && StringUtils.isNotEmpty(dto.getMaterialSiteId())
                        && StringUtils.isNotEmpty(dto.getSiteId())) {
            MtMaterialSite materialSite = new MtMaterialSite();
            materialSite.setMaterialId(dto.getMaterialId());
            materialSite.setSiteId(dto.getSiteId());
            String materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId, materialSite);
            if (!materialSiteId.equals(dto.getMaterialSiteId())) {
                throw new MtException("MT_MATERIAL_0077",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0077", "MATERIAL",
                                                "materialId", "siteId", "materialSiteId ",
                                                "【API:pfepInventoryKidGet】"));
            }
        }

        if (StringUtils.isNotEmpty(dto.getMaterialSiteId())) {
            paramMaterialSiteId = dto.getMaterialSiteId();
            MtMaterialSite mtMaterialSite = new MtMaterialSite();
            mtMaterialSite.setMaterialSiteId(dto.getMaterialSiteId());
            mtMaterialSite.setEnableFlag("Y");
            mtMaterialSite.setTenantId(tenantId);
            MtMaterialSite site = mtMaterialSiteMapper.selectOne(mtMaterialSite);
            if (null == site) {
                throw new MtException("MT_MATERIAL_0055", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0055", "MATERIAL", "materialSiteId ", "【API:pfepInventoryKidGet】"));
            }
            MATERIAL_ID = site.getMaterialId();
            SITE_ID = site.getSiteId();
        }

        if (StringUtils.isNotEmpty(dto.getMaterialId()) && StringUtils.isNotEmpty(dto.getSiteId())) {
            MtMaterialSite site = new MtMaterialSite();
            site.setSiteId(dto.getSiteId());
            site.setMaterialId(dto.getMaterialId());
            String materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId, site);
            if (StringUtils.isEmpty(materialSiteId)) {
                throw new MtException("MT_MATERIAL_0074", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0074", "MATERIAL", "【API:pfepInventoryKidGet】"));
            }
            paramMaterialSiteId = materialSiteId;
        }
        if (StringUtils.isNotEmpty(dto.getMaterialId())) {
            paramMaterialId = dto.getMaterialId();
        } else {
            paramMaterialId = MATERIAL_ID;
        }
        if (StringUtils.isNotEmpty(dto.getSiteId())) {
            paramSiteId = dto.getSiteId();
        } else {
            paramSiteId = SITE_ID;
        }
        // a) 若组织类型和组织ID同时有值 获取物料pfep属性主键
        if (StringUtils.isNotEmpty(dto.getOrganizationId()) && StringUtils.isNotEmpty(dto.getOrganizationType())) {
            MtPfepInventory pfepInventory = new MtPfepInventory();
            pfepInventory.setTenantId(tenantId);
            pfepInventory.setMaterialSiteId(paramMaterialSiteId);
            pfepInventory.setOrganizationType(dto.getOrganizationType());
            pfepInventory.setOrganizationId(dto.getOrganizationId());
            pfepInventory.setEnableFlag("Y");
            MtPfepInventory mtPfepInventory = mtPfepInventoryMapper.selectOne(pfepInventory);
            MtPfepInventoryVO8 pfepInventoryVO8 = new MtPfepInventoryVO8();
            if (null != mtPfepInventory) {
                pfepInventoryVO8.setTableName("MT_PFEP_INVENTORY");
                pfepInventoryVO8.setKeyId(mtPfepInventory.getPfepInventoryId());
                return pfepInventoryVO8;
            } else {
                // 获取物料站点下默认类别集的物料类别，然后通过物料类别ID+站点ID+组织类型+组织ID获取
                MtMaterialCategoryAssignVO assignVO = new MtMaterialCategoryAssignVO();
                assignVO.setMaterialId(paramMaterialId);
                assignVO.setSiteId(paramSiteId);
                assignVO.setDefaultType("MANUFACTURING");
                String materialCategoryId = imtMaterialCategoryAssignService
                                .defaultSetMaterialAssignCategoryGet(tenantId, assignVO);

                MtMaterialCategorySite categorySite = new MtMaterialCategorySite();
                categorySite.setSiteId(paramSiteId);
                categorySite.setMaterialCategoryId(materialCategoryId);
                String materialCategorySiteId = iMtMaterialCategorySiteService
                                .materialCategorySiteLimitRelationGet(tenantId, categorySite);

                MtPfepInventoryCategory pfepInventoryCategory = new MtPfepInventoryCategory();
                pfepInventoryCategory.setMaterialCategorySiteId(materialCategorySiteId);
                pfepInventoryCategory.setOrganizationType(dto.getOrganizationType());
                pfepInventoryCategory.setOrganizationId(dto.getOrganizationId());
                pfepInventoryCategory.setEnableFlag("Y");
                pfepInventoryCategory.setTenantId(tenantId);
                MtPfepInventoryCategory mtPfepInventoryCategory =
                                mtPfepInventoryCategoryMapper.selectOne(pfepInventoryCategory);
                if (null != mtPfepInventoryCategory) {
                    pfepInventoryVO8.setTableName("MT_PFEP_INVENTORY_CATEGORY");
                    pfepInventoryVO8.setKeyId(mtPfepInventoryCategory.getPfepInventoryCategoryId());
                    return pfepInventoryVO8;
                }

                // 根据P1(materialSiteId)作为参数，同时限定组织类型、组织ID为空，从MT_PFEP_INVENTORY获取PFEP_MANUFACTURING_ID
                pfepInventory.setTenantId(tenantId);
                pfepInventory.setMaterialSiteId(paramMaterialSiteId);
                pfepInventory.setOrganizationType(null);
                pfepInventory.setOrganizationId(null);
                pfepInventory.setEnableFlag("Y");
                MtPfepInventory mtPfepInventory2 = mtPfepInventoryMapper.selectOne(pfepInventory);
                if (null != mtPfepInventory2) {
                    pfepInventoryVO8.setTableName("MT_PFEP_INVENTORY");
                    pfepInventoryVO8.setKeyId(mtPfepInventory2.getPfepInventoryId());
                    return pfepInventoryVO8;
                }

                // 获取物料站点下默认类别集的物料类别，然后通过物料类别ID+站点ID、限定组织类型、组织ID为空，获取MT_PFEP_INVENTORY_CATEGORY中PFEP_MANUFACTURING_CATEGORY_ID
                pfepInventoryCategory.setMaterialCategorySiteId(materialCategorySiteId);
                pfepInventoryCategory.setOrganizationType(null);
                pfepInventoryCategory.setOrganizationId(null);
                pfepInventoryCategory.setEnableFlag("Y");
                pfepInventoryCategory.setTenantId(tenantId);
                MtPfepInventoryCategory mtPfepInventoryCategory2 =
                                mtPfepInventoryCategoryMapper.selectOne(pfepInventoryCategory);
                if (null != mtPfepInventoryCategory2) {
                    pfepInventoryVO8.setTableName("MT_PFEP_INVENTORY_CATEGORY");
                    pfepInventoryVO8.setKeyId(mtPfepInventoryCategory2.getPfepInventoryCategoryId());
                    return pfepInventoryVO8;
                } else {
                    return null;
                }


            }
        }

        // b) 若组织类型和组织ID同时无值 获取物料pfep属性主键
        if (StringUtils.isEmpty(dto.getOrganizationId()) && StringUtils.isEmpty(dto.getOrganizationType())) {
            MtPfepInventory pfepInventory = new MtPfepInventory();
            pfepInventory.setTenantId(tenantId);
            pfepInventory.setMaterialSiteId(paramMaterialSiteId);
            pfepInventory.setEnableFlag("Y");
            MtPfepInventory mtPfepInventory = mtPfepInventoryMapper.selectOne(pfepInventory);
            MtPfepInventoryVO8 pfepInventoryVO8 = new MtPfepInventoryVO8();
            if (null != mtPfepInventory) {
                pfepInventoryVO8.setTableName("MT_PFEP_INVENTORY");
                pfepInventoryVO8.setKeyId(mtPfepInventory.getPfepInventoryId());
                return pfepInventoryVO8;
            }

            MtMaterialCategoryAssignVO assignVO = new MtMaterialCategoryAssignVO();
            assignVO.setMaterialId(paramMaterialId);
            assignVO.setSiteId(paramSiteId);
            assignVO.setDefaultType("MANUFACTURING");
            String materialCategoryId =
                            imtMaterialCategoryAssignService.defaultSetMaterialAssignCategoryGet(tenantId, assignVO);

            MtMaterialCategorySite categorySite = new MtMaterialCategorySite();
            categorySite.setSiteId(paramSiteId);
            categorySite.setMaterialCategoryId(materialCategoryId);
            String materialCategorySiteId =
                            iMtMaterialCategorySiteService.materialCategorySiteLimitRelationGet(tenantId, categorySite);

            MtPfepInventoryCategory pfepInventoryCategory = new MtPfepInventoryCategory();
            pfepInventoryCategory.setMaterialCategorySiteId(materialCategorySiteId);
            pfepInventoryCategory.setEnableFlag("Y");
            pfepInventoryCategory.setTenantId(tenantId);
            MtPfepInventoryCategory mtPfepInventoryCategory =
                            mtPfepInventoryCategoryMapper.selectOne(pfepInventoryCategory);
            if (null != mtPfepInventoryCategory) {
                pfepInventoryVO8.setTableName("MT_PFEP_INVENTORY_CATEGORY");
                pfepInventoryVO8.setKeyId(mtPfepInventoryCategory.getPfepInventoryCategoryId());
                return pfepInventoryVO8;
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pfepInventoryAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10) {
        if (StringUtils.isEmpty(mtExtendVO10.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId ", "【API：pfepInventoryAttrPropertyUpdate】"));
        }
        MtPfepInventory pfepInventory = new MtPfepInventory();
        pfepInventory.setTenantId(tenantId);
        pfepInventory.setPfepInventoryId(mtExtendVO10.getKeyId());
        pfepInventory = mtPfepInventoryMapper.selectOne(pfepInventory);
        if (null == pfepInventory) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            mtExtendVO10.getKeyId(), "mt_pfep_inventory",
                                            "【API:pfepInventoryAttrPropertyUpdate】"));
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_pfep_inventory_attr", mtExtendVO10.getKeyId(),
                        mtExtendVO10.getEventId(), mtExtendVO10.getAttrs());

    }

    @Override
    public List<MtPfepInventoryVO3> pfepInventoryBatchGet(Long tenantId, List<MtPfepInventoryVO> voList, List<String> fields){
        final String apiName = "【API:pfepInventoryBatchGet】";
        return pfepInventoryBatchGetCommon(tenantId, voList, fields, apiName);
    }

    private List<MtPfepInventoryVO3> pfepInventoryBatchGetCommon(Long tenantId, List<MtPfepInventoryVO> voList,
                                                                 List<String> fields, String apiName) {
        if (CollectionUtils.isEmpty(voList)) {
            return Collections.emptyList();
        }
        // 校验
        voList.forEach(vo -> {
            if (MtIdHelper.isIdNull(vo.getOrganizationId()) != StringUtils.isEmpty(vo.getOrganizationType())) {
                throw new MtException("MT_MATERIAL_0004",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0004","MATERIAL", apiName));
            }
            if (MtIdHelper.isIdNull(vo.getMaterialId())) {
                throw new MtException("MT_MATERIAL_0054",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL", "materialId", apiName));

            }
            if (MtIdHelper.isIdNull(vo.getSiteId())) {
                throw new MtException("MT_MATERIAL_0054",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL", "siteId", apiName));
            }
        });
        // 如果没有输入，表示全查
        List<String> allFields = Arrays.asList(MtPfepInventory.FIELD_IDENTIFY_TYPE, MtPfepInventory.FIELD_IDENTIFY_ID,
                MtPfepInventory.FIELD_STOCK_LOCATOR_ID, MtPfepInventory.FIELD_PACKAGE_LENGTH,
                MtPfepInventory.FIELD_PACKAGE_WIDTH, MtPfepInventory.FIELD_PACKAGE_HEIGHT,
                MtPfepInventory.FIELD_PACKAGE_SIZE_UOM_ID, MtPfepInventory.FIELD_PACKAGE_WEIGHT,
                MtPfepInventory.FIELD_WEIGHT_UOM_ID, MtPfepInventory.FIELD_MAX_STOCK_QTY,
                MtPfepInventory.FIELD_MIN_STOCK_QTY, MtPfepInventory.FIELD_ISSUED_LOCATOR_ID,
                MtPfepInventory.FIELD_COMPLETION_LOCATOR_ID);
        List<String> inputFields;
        if (CollectionUtils.isEmpty(fields)) {
            inputFields = allFields;
        } else {
            if (fields.stream().anyMatch(t -> !allFields.contains(t))) {
                List<String> unKnow = fields.stream().filter(t -> !allFields.contains(t)).collect(toList());
                StringBuilder info = new StringBuilder("");
                for (String ever : unKnow) {
                    info.append(ever).append(",");
                }
                info.deleteCharAt(info.length() - 1);
                throw new MtException("MT_MATERIAL_0107", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_0107", "MATERIAL", info.toString(), apiName));

            }
            inputFields = fields;
        }
        // 将null置为空或者0
        voList.forEach(t -> {
            if (StringUtils.isEmpty(t.getOrganizationType())) {
                t.setOrganizationType("");
            }
            if (MtIdHelper.isIdNull(t.getOrganizationId())) {
                t.setOrganizationId("");
            }
        });

        List<MtMaterialSiteVO3> mtMaterialSite = new ArrayList<>();
        List<MtMaterialCategoryAssignVO5> assignCondition = new ArrayList<>();

        // 批量获取站点物料关系
        for (MtPfepInventoryVO vo6 : voList) {
            MtMaterialSiteVO3 site = new MtMaterialSiteVO3();
            site.setMaterialId(vo6.getMaterialId());
            site.setSiteId(vo6.getSiteId());
            MtMaterialCategoryAssignVO5 assignVO5 = new MtMaterialCategoryAssignVO5();
            assignVO5.setSiteId(vo6.getSiteId());
            assignVO5.setMaterialId(vo6.getMaterialId());
            assignVO5.setDefaultType("MANUFACTURING");

            mtMaterialSite.add(site);
            assignCondition.add(assignVO5);
        }

        // 获取物料站点ID的MAP,key是materialId : siteId
        Map<String, String> materialSiteIdMap = materialSiteIdMapGet(tenantId, mtMaterialSite);
        // 物料类别站点ID的MAP,key是materialId : siteId
        Map<String, String> categorySiteIdMap = materialCategorySiteIdMapGet(tenantId, assignCondition);

        // 去重
        List<MtPfepInventoryVO> inputList = voList.stream().distinct().collect(Collectors.toList());
        // 先准备好结果数据
        List<MtPfepInventoryVO3> result = transMapper.inventoryVOToInventoryVO3List(inputList);

        List<MtPfepInventoryVO> removeList = new ArrayList<>();

        // 第一次查询，根据物料和组织
        List<MtPfepInventory> materialList = baseMaterialPfepBatchGet(tenantId, inputList.stream()
                .filter(t -> StringUtils.isNotEmpty(t.getOrganizationType())
                        && MtIdHelper.isIdNotNull(t.getOrganizationId()))
                .collect(Collectors.toList()), inputFields, materialSiteIdMap, false);
        // 第一次拼接
        if (CollectionUtils.isNotEmpty(materialList)) {
            for (MtPfepInventoryVO input : inputList) {
                if (MtIdHelper.isIdNotNull(input.getOrganizationId())
                        && StringUtils.isNotEmpty(input.getOrganizationType())) {
                    Optional<MtPfepInventoryVO3> resultOp = result.stream()
                            .filter(t -> MtIdHelper.isSame(input.getMaterialId(), t.getMaterialId())
                                    && MtIdHelper.isSame(input.getSiteId(), t.getSiteId())
                                    && MtFieldsHelper.isSame(
                                    input.getOrganizationType(),
                                    t.getOrganizationType())
                                    && MtIdHelper.isSame(input.getOrganizationId(),
                                    t.getOrganizationId()))
                            .findFirst();
                    // 这里正常不可能找不到
                    if (resultOp.isPresent()) {
                        MtPfepInventoryVO3 one = resultOp.get();
                        // 物料站点关系
                        String materialSiteId = materialSiteIdMap
                                .get(input.getMaterialId() + MtBaseConstants.SPLIT_CHAR + input.getSiteId());
                        Optional<MtPfepInventory> pfepOp = materialList.stream()
                                .filter(t -> MtIdHelper.isSame(materialSiteId, t.getMaterialSiteId())
                                        && MtFieldsHelper.isSame(one.getOrganizationType(),
                                        t.getOrganizationType())
                                        && MtIdHelper.isSame(one.getOrganizationId(),
                                        t.getOrganizationId()))
                                .findFirst();
                        if (pfepOp.isPresent()) {
                            MtPfepInventoryVO11 record = transMapper.inventoryToInventoryVO11(pfepOp.get());
                            BeanUtils.copyProperties(record, one);
                            if (MtFieldsHelper.isSpecifyFieldNotEmpty(record, inputFields)) {
                                removeList.add(input);
                            }
                        }

                    }
                }
            }
            // 移除数据，清空结果
            inputList.removeAll(removeList);
            removeList.clear();
        }

        // 第二次查询，根据物料类别和组织
        List<MtPfepInventoryCategory> materialCategoryList = baseMaterialCategoryPfepBatchGet(tenantId,
                inputList.stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getOrganizationType())
                                && MtIdHelper.isIdNotNull(t.getOrganizationId()))
                        .collect(Collectors.toList()),
                inputFields, categorySiteIdMap, false);
        // 第二次拼接
        if (CollectionUtils.isNotEmpty(materialCategoryList)) {
            for (MtPfepInventoryVO input : inputList) {
                if (MtIdHelper.isIdNotNull(input.getOrganizationId())
                        && StringUtils.isNotEmpty(input.getOrganizationType())) {
                    Optional<MtPfepInventoryVO3> resultOp = result.stream()
                            .filter(t -> MtIdHelper.isSame(input.getMaterialId(), t.getMaterialId())
                                    && MtIdHelper.isSame(input.getSiteId(), t.getSiteId())
                                    && MtFieldsHelper.isSame(
                                    input.getOrganizationType(),
                                    t.getOrganizationType())
                                    && MtIdHelper.isSame(input.getOrganizationId(),
                                    t.getOrganizationId()))
                            .findFirst();
                    // 这里正常不可能找不到
                    if (resultOp.isPresent()) {
                        MtPfepInventoryVO3 one = resultOp.get();
                        // 获取物料类别站点ID,有可能找不到
                        String materialCategorySiteId = categorySiteIdMap
                                .get(input.getMaterialId() + MtBaseConstants.SPLIT_CHAR + input.getSiteId());
                        Optional<MtPfepInventoryCategory> pfepOp = materialCategoryList.stream().filter(t -> MtIdHelper
                                .isSame(materialCategorySiteId, t.getMaterialCategorySiteId())
                                && MtFieldsHelper.isSame(one.getOrganizationType(), t.getOrganizationType())
                                && MtIdHelper.isSame(one.getOrganizationId(), t.getOrganizationId()))
                                .findFirst();
                        if (pfepOp.isPresent()) {
                            // 先保留上面步骤就获取的值,作为临时变量
                            MtPfepInventoryVO11 record = transMapper.inventoryVO3ToInventoryVO11(one);
                            // 再把新获取的值填写进临时变量
                            MtBeanUtils.copyIsNullProperties(pfepOp.get(), record);
                            // 别忘了修改结果列表记录进去的值
                            BeanUtils.copyProperties(record, one);
                            if (MtFieldsHelper.isSpecifyFieldNotEmpty(record, inputFields)) {
                                removeList.add(input);
                            }
                        }
                    }

                }
            }
            // 移除数据，清空结果
            inputList.removeAll(removeList);
            removeList.clear();
        }

        // 第三次查询，根据物料
        List<MtPfepInventory> materialList2 =
                baseMaterialPfepBatchGet(tenantId, inputList, inputFields, materialSiteIdMap, true);
        // 第三次拼接
        if (CollectionUtils.isNotEmpty(materialList2)) {
            for (MtPfepInventoryVO input : inputList) {

                Optional<MtPfepInventoryVO3> resultOp = result.stream()
                        .filter(t -> MtIdHelper.isSame(input.getMaterialId(), t.getMaterialId())
                                && MtIdHelper.isSame(input.getSiteId(), t.getSiteId())
                                && MtFieldsHelper.isSame(input.getOrganizationType(),
                                t.getOrganizationType())
                                && MtIdHelper.isSame(input.getOrganizationId(), t.getOrganizationId()))
                        .findFirst();
                // 这里正常不可能找不到
                if (resultOp.isPresent()) {
                    MtPfepInventoryVO3 one = resultOp.get();
                    // 物料站点关系
                    String materialSiteId = materialSiteIdMap
                            .get(input.getMaterialId() + MtBaseConstants.SPLIT_CHAR + input.getSiteId());
                    Optional<MtPfepInventory> pfepOp = materialList2.stream()
                            .filter(t -> MtIdHelper.isSame(materialSiteId, t.getMaterialSiteId())).findFirst();
                    if (pfepOp.isPresent()) {
                        // 先保留上面步骤就获取的值,作为临时变量
                        MtPfepInventoryVO11 record = transMapper.inventoryVO3ToInventoryVO11(one);
                        // 再把新获取的值填写进临时变量
                        MtBeanUtils.copyIsNullProperties(pfepOp.get(), record);
                        // 别忘了修改结果列表记录进去的值
                        BeanUtils.copyProperties(record, one);
                        if (MtFieldsHelper.isSpecifyFieldNotEmpty(record, inputFields)) {
                            removeList.add(input);
                        }
                    }
                }

            }
            // 移除数据，清空结果
            inputList.removeAll(removeList);
            removeList.clear();
        }

        // 第四次查询，根据物料类别
        List<MtPfepInventoryCategory> materialCategoryList2 =
                baseMaterialCategoryPfepBatchGet(tenantId, inputList, inputFields, categorySiteIdMap, true);
        // 第四次拼接
        if (CollectionUtils.isNotEmpty(materialCategoryList2)) {
            for (MtPfepInventoryVO input : inputList) {
                Optional<MtPfepInventoryVO3> resultOp = result.stream()
                        .filter(t -> MtIdHelper.isSame(input.getMaterialId(), t.getMaterialId())
                                && MtIdHelper.isSame(input.getSiteId(), t.getSiteId())
                                && MtFieldsHelper.isSame(input.getOrganizationType(),
                                t.getOrganizationType())
                                && MtIdHelper.isSame(input.getOrganizationId(), t.getOrganizationId()))
                        .findFirst();
                // 这里正常不可能找不到
                if (resultOp.isPresent()) {
                    MtPfepInventoryVO3 one = resultOp.get();
                    // 获取物料类别站点ID,有可能找不到
                    String materialCategorySiteId = categorySiteIdMap
                            .get(input.getMaterialId() + MtBaseConstants.SPLIT_CHAR + input.getSiteId());
                    Optional<MtPfepInventoryCategory> pfepOp = materialCategoryList2.stream().filter(
                            t -> MtIdHelper.isSame(materialCategorySiteId, t.getMaterialCategorySiteId()))
                            .findFirst();
                    if (pfepOp.isPresent()) {
                        // 先保留上面步骤就获取的值,作为临时变量
                        MtPfepInventoryVO11 record = transMapper.inventoryVO3ToInventoryVO11(one);
                        // 再把新获取的值填写进临时变量
                        MtBeanUtils.copyIsNullProperties(pfepOp.get(), record);
                        // 别忘了修改结果列表记录进去的值
                        BeanUtils.copyProperties(record, one);
                        if (MtFieldsHelper.isSpecifyFieldNotEmpty(record, inputFields)) {
                            removeList.add(input);
                        }
                    }
                }
            }

        }

        return result;
    }

    private Map<String, String> materialSiteIdMapGet(Long tenantId, List<MtMaterialSiteVO3> mtMaterialSite) {
        // 物料站点批量获取
        List<MtMaterialSiteVO4> relSites =
                mtMaterialSiteRepository.materialSiteLimitRelationBatchGet(tenantId, mtMaterialSite);
        // 物料站点批量获取转map <MaterialId:SiteId , MaterialSiteId>
        Map<String, String> relSiteIdMap = new HashMap<>(relSites.size());
        if (CollectionUtils.isNotEmpty(relSites)) {
            relSiteIdMap = relSites.stream()
                    .collect(Collectors.toMap(
                            t -> t.getMaterialId() + MtBaseConstants.SPLIT_CHAR + t.getSiteId(),
                            MtMaterialSiteVO4::getMaterialSiteId));
        }
        return relSiteIdMap;
    }

    private Map<String, String> materialCategorySiteIdMapGet(Long tenantId,
                                                           List<MtMaterialCategoryAssignVO5> assignCondition) {
        // 批量获取物料指定站点下默认类别集的物料类别
        List<MtMaterialCategoryAssignVO6> assigns = mtMaterialCategoryAssignRepository
                .defaultSetMaterialAssignCategoryBatchGet(tenantId, assignCondition).stream()
                .filter(t -> MtIdHelper.isIdNotNull(t.getMaterialCategoryId())).collect(Collectors.toList());

        Map<String, String> result = new HashMap<>(assigns.size());
        // 处理获取物料指定站点下默认类别集的物料类别
        if (CollectionUtils.isNotEmpty(assigns)) {
            // list <MtMaterialCategorySiteVO4>
            List<MtMaterialCategorySiteVO4> categorySites = assigns.stream().map(a -> {
                MtMaterialCategorySiteVO4 siteVO4 = new MtMaterialCategorySiteVO4();
                siteVO4.setMaterialCategoryId(a.getMaterialCategoryId());
                siteVO4.setSiteId(a.getSiteId());
                return siteVO4;
            }).collect(Collectors.toList());
            // 根据物料类别和站点限制批量获取物料类别站点关系
            List<MtMaterialCategorySiteVO7> mtMaterialCategorySites = mtMaterialCategorySiteRepository
                    .materialCategorySiteLimitRelationBatchGet(tenantId, categorySites);
            Map<String, String> materialCategorySiteMap = new HashMap<>(assigns.size());
            if (CollectionUtils.isNotEmpty(mtMaterialCategorySites)) {
                // map <materialCategoryId : SiteId , materialCategorySiteId>
                materialCategorySiteMap = mtMaterialCategorySites.stream().collect(Collectors.toMap(
                        t -> t.getMaterialCategoryId() + MtBaseConstants.SPLIT_CHAR + t.getSiteId(),
                        MtMaterialCategorySiteVO7::getMaterialCategorySiteId));
            }

            for (MtMaterialCategoryAssignVO6 ever : assigns) {
                String categoryKey = ever.getMaterialCategoryId() + MtBaseConstants.SPLIT_CHAR + ever.getSiteId();
                if (materialCategorySiteMap.containsKey(categoryKey)) {
                    String key = ever.getMaterialId() + MtBaseConstants.SPLIT_CHAR + ever.getSiteId();
                    result.put(key, materialCategorySiteMap.get(categoryKey));
                }
            }

        }
        return result;
    }

    private List<MtPfepInventory> baseMaterialPfepBatchGet(Long tenantId, List<MtPfepInventoryVO> inputList,
                                                           List<String> inputFields, Map<String, String> materialSiteIdMap, boolean ignoreOrganization) {
        if (CollectionUtils.isNotEmpty(inputList)) {
            List<String> list = new ArrayList<>(inputFields);
            list.add(MtPfepInventory.FIELD_MATERIAL_SITE_ID);
            list.add(MtPfepInventory.FIELD_ORGANIZATION_TYPE);
            list.add(MtPfepInventory.FIELD_ORGANIZATION_ID);

            // 改为以分治方式查询
            List<List<MtPfepInventoryVO>> batchList =
                    Lists.partition(inputList.stream().distinct().collect(toList()), 1000);

            List<Future<List<MtPfepInventory>>> futureList = new ArrayList<>();
            List<MtPfepInventory> result = new ArrayList<>();
            CustomUserDetails details = DetailsHelper.getUserDetails();
            for (List<MtPfepInventoryVO> ever : batchList) {

                Condition.Builder builder =
                        Condition.builder(MtPfepInventory.class).select(list.toArray(new String[0]));
                for (MtPfepInventoryVO input : ever) {
                    // 物料站点关系
                    String materialSiteId = materialSiteIdMap
                            .get(input.getMaterialId() + MtBaseConstants.SPLIT_CHAR + input.getSiteId());
                    if (MtIdHelper.isIdNotNull(materialSiteId)) {
                        Sqls sqls = Sqls.custom().andEqualTo(MtPfepInventory.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtPfepInventory.FIELD_MATERIAL_SITE_ID, materialSiteId)
                                .andEqualTo(MtPfepInventory.FIELD_ENABLE_FLAG, MtBaseConstants.YES);
                        if (ignoreOrganization) {
                            sqls.andEqualTo(MtPfepInventory.FIELD_ORGANIZATION_TYPE, "")
                                    .andEqualTo(MtPfepInventory.FIELD_ORGANIZATION_ID,
                                            MtBaseConstants.LONG_SPECIAL);
                        } else {
                            sqls.andEqualTo(MtPfepInventory.FIELD_ORGANIZATION_TYPE, input.getOrganizationType())
                                    .andEqualTo(MtPfepInventory.FIELD_ORGANIZATION_ID,
                                            input.getOrganizationId());
                        }
                        builder.orWhere(sqls);
                    }
                }
                Future<List<MtPfepInventory>> one = poolExecutor.submit(() -> {
                    DetailsHelper.setCustomUserDetails(details);
                    SecurityTokenHelper.close();
                    return mtPfepInventoryMapper.selectByCondition(builder.build());
                });
                futureList.add(one);
                // 最大开启10个线程
                if (futureList.size() > 10) {
                    for (Future<List<MtPfepInventory>> oneFuture : futureList) {
                        try {
                            result.addAll(oneFuture.get());
                        } catch (InterruptedException | ExecutionException e) {
                            oneFuture.cancel(true);
                            Thread.currentThread().interrupt();
                        }
                    }
                    futureList = new ArrayList<>();
                }
            }
            for (Future<List<MtPfepInventory>> oneFuture : futureList) {
                try {
                    result.addAll(oneFuture.get());
                } catch (InterruptedException | ExecutionException e) {
                    oneFuture.cancel(true);
                    Thread.currentThread().interrupt();
                }
            }

            return result;
        }
        return new ArrayList<>();
    }

    private List<MtPfepInventoryCategory> baseMaterialCategoryPfepBatchGet(Long tenantId,
                                                                           List<MtPfepInventoryVO> inputList, List<String> inputFields, Map<String, String> categorySiteIdMap,
                                                                           boolean ignoreOrganization) {
        if (CollectionUtils.isNotEmpty(inputList)) {
            List<String> list = new ArrayList<>(inputFields);
            list.add(MtPfepInventoryCategory.FIELD_MATERIAL_CATEGORY_SITE_ID);
            list.add(MtPfepInventoryCategory.FIELD_ORGANIZATION_TYPE);
            list.add(MtPfepInventoryCategory.FIELD_ORGANIZATION_ID);

            // 改为以分治方式查询
            List<List<MtPfepInventoryVO>> batchList =
                    Lists.partition(inputList.stream().distinct().collect(toList()), 1000);

            List<Future<List<MtPfepInventoryCategory>>> futureList = new ArrayList<>();
            List<MtPfepInventoryCategory> result = new ArrayList<>();
            CustomUserDetails details = DetailsHelper.getUserDetails();
            for (List<MtPfepInventoryVO> ever : batchList) {
                Condition.Builder builder =
                        Condition.builder(MtPfepInventoryCategory.class).select(list.toArray(new String[0]));
                for (MtPfepInventoryVO input : ever) {
                    String materialCategorySiteId = categorySiteIdMap
                            .get(input.getMaterialId() + MtBaseConstants.SPLIT_CHAR + input.getSiteId());
                    if (MtIdHelper.isIdNotNull(materialCategorySiteId)) {
                        Sqls sqls = Sqls.custom()
                                .andEqualTo(MtPfepInventoryCategory.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtPfepInventoryCategory.FIELD_MATERIAL_CATEGORY_SITE_ID,
                                        materialCategorySiteId)
                                .andEqualTo(MtPfepInventoryCategory.FIELD_ENABLE_FLAG, MtBaseConstants.YES);
                        if (ignoreOrganization) {
                            sqls.andEqualTo(MtPfepInventoryCategory.FIELD_ORGANIZATION_TYPE,
                                    "")
                                    .andEqualTo(MtPfepInventoryCategory.FIELD_ORGANIZATION_ID,
                                            MtBaseConstants.LONG_SPECIAL);
                        } else {
                            sqls.andEqualTo(MtPfepInventoryCategory.FIELD_ORGANIZATION_TYPE,
                                    input.getOrganizationType())
                                    .andEqualTo(MtPfepInventoryCategory.FIELD_ORGANIZATION_ID,
                                            input.getOrganizationId());
                        }
                        builder.orWhere(sqls);
                    }
                }
                Future<List<MtPfepInventoryCategory>> one = poolExecutor.submit(() -> {
                    DetailsHelper.setCustomUserDetails(details);
                    SecurityTokenHelper.close();
                    return mtPfepInventoryCategoryMapper.selectByCondition(builder.build());
                });
                futureList.add(one);
                // 最大开启10个线程
                if (futureList.size() > 10) {
                    for (Future<List<MtPfepInventoryCategory>> oneFuture : futureList) {
                        try {
                            result.addAll(oneFuture.get());
                        } catch (InterruptedException | ExecutionException e) {
                            oneFuture.cancel(true);
                            Thread.currentThread().interrupt();
                        }
                    }
                    futureList = new ArrayList<>();
                }
            }
            for (Future<List<MtPfepInventoryCategory>> oneFuture : futureList) {
                try {
                    result.addAll(oneFuture.get());
                } catch (InterruptedException | ExecutionException e) {
                    oneFuture.cancel(true);
                    Thread.currentThread().interrupt();
                }
            }
            return result;
        }
        return new ArrayList<>();
    }
}
