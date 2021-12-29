package tarzan.material.infra.repository.impl;

import com.google.common.collect.Lists;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.*;
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
import tarzan.material.domain.entity.MtMaterialCategorySite;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtPfepManufacturing;
import tarzan.material.domain.entity.MtPfepManufacturingCategory;
import tarzan.material.domain.repository.MtMaterialCategoryAssignRepository;
import tarzan.material.domain.repository.MtMaterialCategorySiteRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtPfepManufacturingRepository;
import tarzan.material.domain.trans.MtPfepManufacturingTransMapper;
import tarzan.material.domain.vo.*;
import tarzan.material.infra.mapper.MtMaterialSiteMapper;
import tarzan.material.infra.mapper.MtPfepManufacturingCategoryMapper;
import tarzan.material.infra.mapper.MtPfepManufacturingMapper;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.repository.MtBomRepository;
import tarzan.method.domain.repository.MtRouterRepository;
import tarzan.method.domain.vo.MtBomVO7;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 物料生产属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Component
public class MtPfepManufacturingRepositoryImpl extends BaseRepositoryImpl<MtPfepManufacturing>
                implements MtPfepManufacturingRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepo;

    @Autowired
    private MtMaterialCategorySiteRepository mtMaterialCategorySiteRepo;

    @Autowired
    private MtMaterialCategoryAssignRepository mtMaterialCategoryAssignRepo;

    @Autowired
    private MtPfepManufacturingMapper mtPfepManufacturingMapper;

    @Autowired
    private MtPfepManufacturingCategoryMapper mtPfepManufacturingCategoryMapper;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtMaterialSiteMapper mtMaterialSiteMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtPfepManufacturingTransMapper transMapper;

    @Autowired
    private ThreadPoolTaskExecutor poolExecutor;

    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;

    @Autowired
    private MtMaterialCategoryAssignRepository mtMaterialCategoryAssignRepository;

    @Autowired
    private MtMaterialCategorySiteRepository mtMaterialCategorySiteRepository;


    @Override
    public String pfepDefaultBomGet(Long tenantId, MtPfepInventoryVO dto) {
        return getManufacturing(tenantId, dto, "bom", "【API:pfepDefaultBomGet】").getDefaultBomId();
    }

    @Override
    public String pfepDefaultRouterGet(Long tenantId, MtPfepInventoryVO dto) {
        return getManufacturing(tenantId, dto, "router", "【API:pfepDefaultRouterGet】").getDefaultRoutingId();
    }

    @Override
    public MtPfepManufacturing pfepManufacturingIssueControlGet(Long tenantId, MtPfepInventoryVO dto) {
        return getManufacturing(tenantId, dto, "issue", "【API:pfepManufacturingIssueControlnGet】");
    }

    @Override
    public MtPfepManufacturing pfepManufacturingCompleteControlGet(Long tenantId, MtPfepInventoryVO dto) {
        return getManufacturing(tenantId, dto, "complete", "【API:pfepManufacturingCompleteControlnGet】");

    }

    @Override
    public MtPfepManufacturing pfepManufacturingAttritionControlGet(Long tenantId, MtPfepInventoryVO dto) {
        return getManufacturing(tenantId, dto, "attrition", "【API:pfepManufacturingAttritionControlGet】");
    }

    private MtPfepManufacturing getManufacturing(Long tenantId, MtPfepInventoryVO dto, String type, String api) {
        MtPfepManufacturing result = new MtPfepManufacturing();

        if (StringUtils.isEmpty(dto.getOrganizationId()) != StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_MATERIAL_0004", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0004", "MATERIAL", api));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", api));

        }
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "siteId", api));
        }

        MtMaterialSite materialSite = new MtMaterialSite();
        materialSite.setMaterialId(dto.getMaterialId());
        materialSite.setSiteId(dto.getSiteId());
        String materialSiteId = mtMaterialSiteRepo.materialSiteLimitRelationGet(tenantId, materialSite);

        MtMaterialCategoryAssignVO assignVO = new MtMaterialCategoryAssignVO();
        assignVO.setMaterialId(dto.getMaterialId());
        assignVO.setSiteId(dto.getSiteId());
        assignVO.setDefaultType("MANUFACTURING");
        String materialId = mtMaterialCategoryAssignRepo.defaultSetMaterialAssignCategoryGet(tenantId, assignVO);

        String materialCategorySiteId = "";
        if (StringUtils.isNotEmpty(materialId)) {
            MtMaterialCategorySite categorySite = new MtMaterialCategorySite();
            categorySite.setMaterialCategoryId(materialId);
            categorySite.setSiteId(dto.getSiteId());
            materialCategorySiteId =
                            mtMaterialCategorySiteRepo.materialCategorySiteLimitRelationGet(tenantId, categorySite);
        }

        boolean existsNull = true;

        if (StringUtils.isNotEmpty(dto.getOrganizationId())) {

            MtPfepManufacturing manufacturing = new MtPfepManufacturing();
            manufacturing.setTenantId(tenantId);
            manufacturing.setMaterialSiteId(materialSiteId);
            manufacturing.setEnableFlag("Y");
            manufacturing.setOrganizationId(dto.getOrganizationId());
            manufacturing.setOrganizationType(dto.getOrganizationType());
            manufacturing = mtPfepManufacturingMapper.selectOne(manufacturing);
            if (manufacturing != null) {
                existsNull = setFromMaterial(result, manufacturing, type);
            }

            if (existsNull && StringUtils.isNotEmpty(materialCategorySiteId)) {
                MtPfepManufacturingCategory manufacturingCategory = new MtPfepManufacturingCategory();
                manufacturingCategory.setTenantId(tenantId);
                manufacturingCategory.setMaterialCategorySiteId(materialCategorySiteId);
                manufacturingCategory.setEnableFlag("Y");
                if (StringUtils.isNotEmpty(dto.getOrganizationId())) {
                    manufacturingCategory.setOrganizationId(dto.getOrganizationId());
                    manufacturingCategory.setOrganizationType(dto.getOrganizationType());
                }
                manufacturingCategory = mtPfepManufacturingCategoryMapper.selectOne(manufacturingCategory);
                if (manufacturingCategory != null) {
                    existsNull = setFromCategory(result, manufacturingCategory, type);
                }
            }
        }
        if (existsNull) {
            MtPfepManufacturing manufacturing1 = new MtPfepManufacturing();
            manufacturing1.setTenantId(tenantId);
            manufacturing1.setMaterialSiteId(materialSiteId);
            manufacturing1.setEnableFlag("Y");
            manufacturing1.setOrganizationId("");
            manufacturing1.setOrganizationType("");
            manufacturing1 = mtPfepManufacturingMapper.selectOne(manufacturing1);
            if (manufacturing1 != null) {
                existsNull = setFromMaterial(result, manufacturing1, type);
            }
        }
        if (existsNull && StringUtils.isNotEmpty(materialCategorySiteId)) {
            MtPfepManufacturingCategory manufacturingCategory1 = new MtPfepManufacturingCategory();
            manufacturingCategory1.setTenantId(tenantId);
            manufacturingCategory1.setMaterialCategorySiteId(materialCategorySiteId);
            manufacturingCategory1.setEnableFlag("Y");
            manufacturingCategory1.setOrganizationId("");
            manufacturingCategory1.setOrganizationType("");
            manufacturingCategory1 = mtPfepManufacturingCategoryMapper.selectOne(manufacturingCategory1);
            if (manufacturingCategory1 != null) {
                setFromCategory(result, manufacturingCategory1, type);
            }
        }
        return result;
    }

    private boolean setFromMaterial(MtPfepManufacturing source, MtPfepManufacturing target, String type) {
        boolean existsNull = false;
        switch (type) {
            case "bom":
                if (StringUtils.isEmpty(source.getDefaultBomId())) {
                    source.setDefaultBomId(target.getDefaultBomId());
                }
                if (StringUtils.isEmpty(source.getDefaultBomId())) {
                    existsNull = true;
                }
                break;
            case "router":
                if (StringUtils.isEmpty(source.getDefaultRoutingId())) {
                    source.setDefaultRoutingId(target.getDefaultRoutingId());
                }
                if (StringUtils.isEmpty(source.getDefaultRoutingId())) {
                    existsNull = true;
                }
                break;
            case "issue":
                if (StringUtils.isEmpty(source.getIssueControlType())) {
                    source.setIssueControlType(target.getIssueControlType());
                }
                if (source.getIssueControlQty() == null) {
                    source.setIssueControlQty(target.getIssueControlQty());
                }
                if (StringUtils.isEmpty(source.getIssueControlType()) || source.getIssueControlQty() == null) {
                    existsNull = true;
                }
                if (existsNull) {
                    source.setIssueControlType("");
                    source.setIssueControlQty(null);
                }
                break;
            case "complete":
                if (StringUtils.isEmpty(source.getCompleteControlType())) {
                    source.setCompleteControlType(target.getCompleteControlType());
                }
                if (source.getCompleteControlQty() == null) {
                    source.setCompleteControlQty(target.getCompleteControlQty());
                }
                if (StringUtils.isEmpty(source.getCompleteControlType()) || source.getCompleteControlQty() == null) {
                    existsNull = true;
                }
                if (existsNull) {
                    source.setCompleteControlType("");
                    source.setCompleteControlQty(null);
                }
                break;
            case "attrition":
                if (StringUtils.isEmpty(source.getAttritionControlType())) {
                    source.setAttritionControlType(target.getAttritionControlType());
                }
                if (source.getAttritionControlQty() == null) {
                    source.setAttritionControlQty(target.getAttritionControlQty());
                }
                if (StringUtils.isEmpty(source.getAttritionControlType()) || source.getAttritionControlQty() == null) {
                    existsNull = true;
                }
                if (existsNull) {
                    source.setAttritionControlType("");
                    source.setAttritionControlQty(null);
                }
                break;
            default:
                break;
        }

        return existsNull;
    }

    private boolean setFromCategory(MtPfepManufacturing source, MtPfepManufacturingCategory target, String type) {
        boolean existsNull = false;
        switch (type) {
            case "bom":
                if (StringUtils.isEmpty(source.getDefaultBomId())) {
                    source.setDefaultBomId(target.getDefaultBomId());
                }
                if (StringUtils.isEmpty(source.getDefaultBomId())) {
                    existsNull = true;
                }
                break;
            case "router":
                if (StringUtils.isEmpty(source.getDefaultRoutingId())) {
                    source.setDefaultRoutingId(target.getDefaultRoutingId());
                }
                if (StringUtils.isEmpty(source.getDefaultRoutingId())) {
                    existsNull = true;
                }
                break;
            case "issue":
                if (StringUtils.isEmpty(source.getIssueControlType())) {
                    source.setIssueControlType(target.getIssueControlType());
                }
                if (source.getIssueControlQty() == null) {
                    source.setIssueControlQty(target.getIssueControlQty());
                }
                if (StringUtils.isEmpty(source.getIssueControlType()) || source.getIssueControlQty() == null) {
                    existsNull = true;
                }
                if (existsNull) {
                    source.setIssueControlType("");
                    source.setIssueControlQty(null);
                }
                break;
            case "complete":
                if (StringUtils.isEmpty(source.getCompleteControlType())) {
                    source.setCompleteControlType(target.getCompleteControlType());
                }
                if (source.getCompleteControlQty() == null) {
                    source.setCompleteControlQty(target.getCompleteControlQty());
                }
                if (StringUtils.isEmpty(source.getCompleteControlType()) || source.getCompleteControlQty() == null) {
                    existsNull = true;
                }
                if (existsNull) {
                    source.setCompleteControlType("");
                    source.setCompleteControlQty(null);
                }
                break;
            case "attrition":
                if (StringUtils.isEmpty(source.getAttritionControlType())) {
                    source.setAttritionControlType(target.getAttritionControlType());
                }
                if (source.getAttritionControlQty() == null) {
                    source.setAttritionControlQty(target.getAttritionControlQty());
                }
                if (StringUtils.isEmpty(source.getAttritionControlType()) || source.getAttritionControlQty() == null) {
                    existsNull = true;
                }
                if (existsNull) {
                    source.setAttritionControlType("");
                    source.setAttritionControlQty(null);
                }
                break;
            default:
                break;
        }
        return existsNull;
    }

    @Override
    public List<MtPfepInventoryVO> pfepRouterLimitMaterialQuery(Long tenantId, String defaultRoutingId) {
        if (StringUtils.isEmpty(defaultRoutingId)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "defaultRoutingId", "【API:pfepRouterLimitMaterialQuery】"));
        }
        return mtPfepManufacturingMapper.selectByRouterId(tenantId, defaultRoutingId);
    }

    @Override
    public List<MtPfepInventoryVO2> pfepRouterLimitMaterialCategoryQuery(Long tenantId, String defaultRoutingId) {
        if (StringUtils.isEmpty(defaultRoutingId)) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "defaultRoutingId", "【API:pfepRouterLimitMaterialCategoryQuery】"));
        }
        return mtPfepManufacturingCategoryMapper.selectByRouterId(tenantId, defaultRoutingId);
    }

    @Override
    public List<MtPfepInventoryVO> pfepBomLimitMaterialQuery(Long tenantId, String defaultBomId) {
        if (StringUtils.isEmpty(defaultBomId)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "defaultBomId", "【API:pfepBomLimitMaterialQuery】"));

        }
        return this.mtPfepManufacturingMapper.selectByBomId(tenantId, defaultBomId);
    }

    @Override
    public List<MtPfepInventoryVO2> pfepBomLimitMaterialCategoryQuery(Long tenantId, String defaultBomId) {
        if (StringUtils.isEmpty(defaultBomId)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "defaultBomId", "【API:pfepBomLimitMaterialCategoryQuery】"));

        }
        return mtPfepManufacturingCategoryMapper.selectByBomId(tenantId, defaultBomId);
    }

    @Override
    public String pfepOperationAssembleFlagGet(Long tenantId, MtPfepInventoryVO condtion) {
        if (StringUtils.isEmpty(condtion.getMaterialId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:pfepOperationAssembleFlagGet】"));
        }
        if (StringUtils.isEmpty(condtion.getSiteId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "siteId", "【API:pfepOperationAssembleFlagGet】"));
        }
        if (StringUtils.isEmpty(condtion.getOrganizationType()) && StringUtils.isNotEmpty(condtion.getOrganizationId())
                        || StringUtils.isNotEmpty(condtion.getOrganizationType())
                                        && StringUtils.isEmpty(condtion.getOrganizationId())) {
            throw new MtException("MT_MATERIAL_0004", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0004", "MATERIAL", "【API:pfepOperationAssembleFlagGet】"));
        }

        if (StringUtils.isNotEmpty(condtion.getOrganizationType())
                        && StringUtils.isNotEmpty(condtion.getOrganizationId())) {

            // 首先根据输入的物料ID、站点ID、组织类型、组织ID，从MT_PFEP_MANUFACTURING获取默认是否按工序装配标识OPERATION_ASSEMBLE_FLAG
            MtMaterialSite mtMaterialSite = new MtMaterialSite();
            mtMaterialSite.setMaterialId(condtion.getMaterialId());
            mtMaterialSite.setSiteId(condtion.getSiteId());
            String materialSiteId = this.mtMaterialSiteRepo.materialSiteLimitRelationGet(tenantId, mtMaterialSite);

            MtPfepManufacturing mtPfepManufacturing = new MtPfepManufacturing();
            mtPfepManufacturing.setTenantId(tenantId);
            mtPfepManufacturing.setMaterialSiteId(materialSiteId);
            mtPfepManufacturing.setOrganizationType(condtion.getOrganizationType());
            mtPfepManufacturing.setOrganizationId(condtion.getOrganizationId());
            mtPfepManufacturing.setEnableFlag("Y");
            mtPfepManufacturing = this.mtPfepManufacturingMapper.selectOne(mtPfepManufacturing);
            if (null != mtPfepManufacturing && StringUtils.isNotEmpty(mtPfepManufacturing.getOperationAssembleFlag())) {
                return mtPfepManufacturing.getOperationAssembleFlag();
            }

            // 若通过物料ID+站点ID+组织类型+组织ID查询到MT_PFEP_MANUFACTURING中默认是否按工序装配标识数据为空值，则针对获取物料站点下默认类别集的物料类别，然后通过物料类别ID+站点ID+组织类型+组织ID获取MT_PFEP_MANUFACTURING_CATEGORY中默认是否按工序装配标识OPERATION_ASSEMBLE_FLAG数据
            MtMaterialCategoryAssignVO assignVO = new MtMaterialCategoryAssignVO();
            assignVO.setMaterialId(condtion.getMaterialId());
            assignVO.setSiteId(condtion.getSiteId());
            assignVO.setDefaultType("MANUFACTURING");
            String materialCategoryId =
                            this.mtMaterialCategoryAssignRepo.defaultSetMaterialAssignCategoryGet(tenantId, assignVO);

            String materialCategorySiteId = "";
            if (StringUtils.isNotEmpty(materialCategoryId)) {
                MtMaterialCategorySite mtMaterialCategorySite = new MtMaterialCategorySite();
                mtMaterialCategorySite.setMaterialCategoryId(materialCategoryId);
                mtMaterialCategorySite.setSiteId(condtion.getSiteId());
                materialCategorySiteId = this.mtMaterialCategorySiteRepo.materialCategorySiteLimitRelationGet(tenantId,
                                mtMaterialCategorySite);

                MtPfepManufacturingCategory mtPfepManufacturingCategory = new MtPfepManufacturingCategory();
                mtPfepManufacturingCategory.setTenantId(tenantId);
                mtPfepManufacturingCategory.setMaterialCategorySiteId(materialCategorySiteId);
                mtPfepManufacturingCategory.setOrganizationType(condtion.getOrganizationType());
                mtPfepManufacturingCategory.setOrganizationId(condtion.getOrganizationId());
                mtPfepManufacturingCategory.setEnableFlag("Y");
                mtPfepManufacturingCategory =
                                this.mtPfepManufacturingCategoryMapper.selectOne(mtPfepManufacturingCategory);
                if (null != mtPfepManufacturingCategory
                                && StringUtils.isNotEmpty(mtPfepManufacturingCategory.getOperationAssembleFlag())) {
                    return mtPfepManufacturingCategory.getOperationAssembleFlag();
                }
            }

            // 若通过物料或物料类别、站点ID、组织类型、组织ID均获取到PFEP中默认是否按工序装配标识属性为空值，则将输入的物料ID、站点ID作为参数，同时限定组织类型、组织ID为空，从MT_PFEP_MANUFACTURING获取默认是否按工序装配标识OPERATION_ASSEMBLE_FLAG
            mtPfepManufacturing = new MtPfepManufacturing();
            mtPfepManufacturing.setTenantId(tenantId);
            mtPfepManufacturing.setMaterialSiteId(materialSiteId);
            mtPfepManufacturing.setOrganizationType("");
            mtPfepManufacturing.setOrganizationId("");
            mtPfepManufacturing.setEnableFlag("Y");
            mtPfepManufacturing = this.mtPfepManufacturingMapper.selectOne(mtPfepManufacturing);
            if (null != mtPfepManufacturing && StringUtils.isNotEmpty(mtPfepManufacturing.getOperationAssembleFlag())) {
                return mtPfepManufacturing.getOperationAssembleFlag();
            }

            // 若通过物料或者物料类别、站点ID、组织类型、组织ID均获取到PFEP属性为空值，同时通过物料ID、站点ID、限定组织类型、组织ID为空依然获取到默认是否按工序装配标识属性为空值，则在获取物料站点下默认类别集的物料类别，然后通过物料类别ID+站点ID、限定组织类型、组织ID为空，从MT_PFEP_MANUFACUTRING_CATEGORY中获取默认是否按工序装配标识OPERATION_ASSEMBLE_FLAG
            MtPfepManufacturingCategory mtPfepManufacturingCategory = new MtPfepManufacturingCategory();
            mtPfepManufacturingCategory.setTenantId(tenantId);
            mtPfepManufacturingCategory.setMaterialCategorySiteId(materialCategorySiteId);
            mtPfepManufacturingCategory.setOrganizationType("");
            mtPfepManufacturingCategory.setOrganizationId("");
            mtPfepManufacturingCategory.setEnableFlag("Y");
            mtPfepManufacturingCategory = this.mtPfepManufacturingCategoryMapper.selectOne(mtPfepManufacturingCategory);
            if (null != mtPfepManufacturingCategory
                            && StringUtils.isNotEmpty(mtPfepManufacturingCategory.getOperationAssembleFlag())) {
                return mtPfepManufacturingCategory.getOperationAssembleFlag();
            }
            return "";
        } else {
            // 将输入的物料ID、站点ID作为参数，同时限定组织类型、组织ID为空，从MT_PFEP_MANUFACTURING获取默认是否按工序装配标识DEFAULR_BOM_ID
            MtMaterialSite mtMaterialSite = new MtMaterialSite();
            mtMaterialSite.setMaterialId(condtion.getMaterialId());
            mtMaterialSite.setSiteId(condtion.getSiteId());
            String materialSiteId = this.mtMaterialSiteRepo.materialSiteLimitRelationGet(tenantId, mtMaterialSite);

            MtPfepManufacturing mtPfepManufacturing = new MtPfepManufacturing();
            mtPfepManufacturing.setTenantId(tenantId);
            mtPfepManufacturing.setMaterialSiteId(materialSiteId);
            mtPfepManufacturing.setOrganizationType("");
            mtPfepManufacturing.setOrganizationId("");
            mtPfepManufacturing.setEnableFlag("Y");
            mtPfepManufacturing = this.mtPfepManufacturingMapper.selectOne(mtPfepManufacturing);
            if (null != mtPfepManufacturing && StringUtils.isNotEmpty(mtPfepManufacturing.getOperationAssembleFlag())) {
                return mtPfepManufacturing.getOperationAssembleFlag();
            }

            // 若通过物料ID、站点ID、限定组织类型、组织ID为空获取到默认是否按工序装配标识未空值，则在获取物料站点下默认类别集的物料类别，然后通过物料类别ID+站点ID、限定组织类型+组织ID为空，从MT_PFEP_MANUFACUTRING_CATEGORY获取默认是否按工序装配标识OPERATION_ASSEMBLE_FLAG
            MtMaterialCategoryAssignVO assignVO = new MtMaterialCategoryAssignVO();
            assignVO.setMaterialId(condtion.getMaterialId());
            assignVO.setSiteId(condtion.getSiteId());
            assignVO.setDefaultType("MANUFACTURING");
            String materialCategoryId =
                            this.mtMaterialCategoryAssignRepo.defaultSetMaterialAssignCategoryGet(tenantId, assignVO);

            MtMaterialCategorySite mtMaterialCategorySite = new MtMaterialCategorySite();
            mtMaterialCategorySite.setMaterialCategoryId(materialCategoryId);
            mtMaterialCategorySite.setSiteId(condtion.getSiteId());
            String materialCategorySiteId = this.mtMaterialCategorySiteRepo
                            .materialCategorySiteLimitRelationGet(tenantId, mtMaterialCategorySite);

            MtPfepManufacturingCategory mtPfepManufacturingCategory = new MtPfepManufacturingCategory();
            mtPfepManufacturingCategory.setTenantId(tenantId);
            mtPfepManufacturingCategory.setMaterialCategorySiteId(materialCategorySiteId);
            mtPfepManufacturingCategory.setOrganizationType("");
            mtPfepManufacturingCategory.setOrganizationId("");
            mtPfepManufacturingCategory.setEnableFlag("Y");
            mtPfepManufacturingCategory = this.mtPfepManufacturingCategoryMapper.selectOne(mtPfepManufacturingCategory);
            if (null != mtPfepManufacturingCategory
                            && StringUtils.isNotEmpty(mtPfepManufacturingCategory.getOperationAssembleFlag())) {
                return mtPfepManufacturingCategory.getOperationAssembleFlag();
            }
            return "";
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String materialPfepManufacturingUpdate(Long tenantId, MtPfepManufacturingVO3 dto, String fullUpdate) {
        if (StringUtils.isNotEmpty(dto.getDefaultBomId())) {
            MtBomVO7 mtBomVO7 = this.mtBomRepository.bomBasicGet(tenantId, dto.getDefaultBomId());
            if (null == mtBomVO7) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                                "defaultBomId", "【API:materialPfepManufacturingUpdate】"));
            }
        }
        if (StringUtils.isNotEmpty(dto.getDefaultRoutingId())) {
            MtRouter mtRouter = this.mtRouterRepository.routerGet(tenantId, dto.getDefaultRoutingId());
            if (null == mtRouter) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                                "defaultRoutingId", "【API:materialPfepManufacturingUpdate】"));
            }
        }
        if (StringUtils.isNotEmpty(dto.getOrganizationType())) {
            MtGenTypeVO2 condition = new MtGenTypeVO2();
            condition.setModule("MATERIAL");
            condition.setTypeGroup("PFEP_ORGANIZATION_TYPE");
            List<String> mtGenTypeCodes = this.mtGenTypeRepository.groupLimitTypeQuery(tenantId, condition).stream()
                            .map(MtGenType::getTypeCode).distinct().collect(Collectors.toList());
            if (!mtGenTypeCodes.contains(dto.getOrganizationType())) {
                throw new MtException("MT_MATERIAL_0070",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0070", "MATERIAL",
                                                "organizationType", "{" + mtGenTypeCodes.toString() + "}",
                                                "【API:materialPfepManufacturingUpdate】"));
            }
        }
        if (!(StringUtils.isNotEmpty(dto.getIssueControlType()) && null != dto.getIssueControlQty())) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            "issueControlType、issueControlQty",
                                            "【API:materialPfepManufacturingUpdate】"));
        }
        if (!(StringUtils.isNotEmpty(dto.getCompleteControlType()) && null != dto.getCompleteControlQty())) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            "completeControlType、completeControlQty",
                                            "【API:materialPfepManufacturingUpdate】"));
        }
        if (!(StringUtils.isNotEmpty(dto.getAttritionControlType()) && null != dto.getAttritionControlQty())) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            "attritionControlType、attritionControlQty",
                                            "【API:materialPfepManufacturingUpdate】"));
        }
        if (!(StringUtils.isNotEmpty(dto.getOrganizationType()) && StringUtils.isNotEmpty(dto.getOrganizationId()))) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            "organizationType、organizationId",
                                            "【API:materialPfepManufacturingUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getMaterialSiteId())) {
            throw new MtException("MT_MATERIAL_0076",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0076", "MATERIAL",
                                            "materialId", "materialSiteId", "【API:materialPfepManufacturingUpdate】"));
        }

        MtGenTypeVO2 condition = new MtGenTypeVO2();
        condition.setModule("MATERIAL");
        condition.setTypeGroup("CONTROL_TYPE");
        List<String> mtGenTypeCodes = this.mtGenTypeRepository.groupLimitTypeQuery(tenantId, condition).stream()
                        .map(MtGenType::getTypeCode).distinct().collect(Collectors.toList());
        if (StringUtils.isNotEmpty(dto.getAttritionControlType())
                        && !mtGenTypeCodes.contains(dto.getAttritionControlType())) {
            throw new MtException("MT_MATERIAL_0070",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0070", "MATERIAL",
                                            "attritionControlType", "{" + mtGenTypeCodes.toString() + "}",
                                            "【API:materialPfepManufacturingUpdate】"));
        }
        if (StringUtils.isNotEmpty(dto.getCompleteControlType())
                        && !mtGenTypeCodes.contains(dto.getCompleteControlType())) {
            throw new MtException("MT_MATERIAL_0070",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0070", "MATERIAL",
                                            "completeControlType", "{" + mtGenTypeCodes.toString() + "}",
                                            "【API:materialPfepManufacturingUpdate】"));
        }
        if (StringUtils.isNotEmpty(dto.getIssueControlType()) && !mtGenTypeCodes.contains(dto.getIssueControlType())) {
            throw new MtException("MT_MATERIAL_0070",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0070", "MATERIAL",
                                            "issueControlType", "{" + mtGenTypeCodes.toString() + "}",
                                            "【API:materialPfepManufacturingUpdate】"));
        }
        if ((StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isNotEmpty(dto.getSiteId()))
                        || (StringUtils.isNotEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getSiteId()))) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            "materialId、siteId", "【API:materialPfepManufacturingUpdate】"));
        }
        if (StringUtils.isNotEmpty(dto.getSiteId())) {
            MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
            if (null == mtModSite || !"MANUFACTURING".equals(mtModSite.getSiteType())) {
                throw new MtException("MT_MATERIAL_0079", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0079", "MATERIAL", "【API:materialPfepManufacturingUpdate】"));
            }
        }
        if (StringUtils.isNotEmpty(dto.getMaterialSiteId()) && StringUtils.isNotEmpty(dto.getMaterialId())
                        && StringUtils.isNotEmpty(dto.getSiteId())) {
            MtMaterialSite mtMaterialSite = new MtMaterialSite();
            mtMaterialSite.setMaterialId(dto.getMaterialId());
            mtMaterialSite.setSiteId(dto.getSiteId());
            String tempMaterialSiteId = this.mtMaterialSiteRepo.materialSiteLimitRelationGet(tenantId, mtMaterialSite);
            if (!dto.getMaterialSiteId().equals(tempMaterialSiteId)) {
                throw new MtException("MT_MATERIAL_0077",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0077", "MATERIAL",
                                                "materialId", "siteId", "materialSiteId",
                                                "【API:materialPfepManufacturingUpdate】"));
            }
        }

        String materialSiteId = null;
        if (StringUtils.isNotEmpty(dto.getMaterialSiteId())) {
            MtMaterialSite mtMaterialSite = new MtMaterialSite();
            mtMaterialSite.setTenantId(tenantId);
            mtMaterialSite.setMaterialSiteId(dto.getMaterialSiteId());
            mtMaterialSite.setEnableFlag("Y");
            mtMaterialSite = mtMaterialSiteRepo.selectOne(mtMaterialSite);
            if (null == mtMaterialSite) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                                "materialSiteId", "【API:materialPfepManufacturingUpdate】"));
            }
            MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, mtMaterialSite.getSiteId());
            if (null == mtModSite || !"MANUFACTURING".equals(mtModSite.getSiteType())) {
                throw new MtException("MT_MATERIAL_0079", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0079", "MATERIAL", "【API:materialPfepManufacturingUpdate】"));
            }
            materialSiteId = dto.getMaterialSiteId();
        }

        if (StringUtils.isNotEmpty(dto.getMaterialId()) && StringUtils.isNotEmpty(dto.getSiteId())) {
            MtMaterialSite mtMaterialSite = new MtMaterialSite();
            mtMaterialSite.setSiteId(dto.getSiteId());
            mtMaterialSite.setMaterialId(dto.getMaterialId());
            String searchMaterialSiteId =
                            this.mtMaterialSiteRepo.materialSiteLimitRelationGet(tenantId, mtMaterialSite);
            if (StringUtils.isEmpty(searchMaterialSiteId)) {
                throw new MtException("MT_MATERIAL_0074", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0074", "MATERIAL", "【API:materialPfepManufacturingUpdate】"));
            }
            materialSiteId = searchMaterialSiteId;
        }

        String result = null;
        if (StringUtils.isNotEmpty(dto.getPfepManufacturingId())) {
            MtPfepManufacturing mtPfepManufacturing = new MtPfepManufacturing();
            mtPfepManufacturing.setTenantId(tenantId);
            mtPfepManufacturing.setPfepManufacturingId(dto.getPfepManufacturingId());
            mtPfepManufacturing = this.mtPfepManufacturingMapper.selectOne(mtPfepManufacturing);
            if (null == mtPfepManufacturing) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                                "pefpManufacturingId", "【API:materialPfepManufacturingUpdate】"));
            }

            if (null != dto.getEnableFlag() && "".equals(dto.getEnableFlag())) {
                throw new MtException("MT_MATERIAL_0071", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0071", "MATERIAL", "enableFlag", "【API:materialPfepManufacturingUpdate】"));
            }

            if ("Y".equalsIgnoreCase(fullUpdate)) {
                mtPfepManufacturing.setMaterialSiteId(materialSiteId);
                mtPfepManufacturing.setOrganizationType(
                                dto.getOrganizationType() == null ? "" : dto.getOrganizationType());
                mtPfepManufacturing.setOrganizationId(dto.getOrganizationId() == null ? "" : dto.getOrganizationId());
                mtPfepManufacturing.setDefaultBomId(dto.getDefaultBomId() == null ? "" : dto.getDefaultBomId());
                mtPfepManufacturing.setDefaultRoutingId(
                                dto.getDefaultRoutingId() == null ? "" : dto.getDefaultRoutingId());
                mtPfepManufacturing.setIssueControlType(
                                dto.getIssueControlType() == null ? "" : dto.getIssueControlType());
                mtPfepManufacturing.setIssueControlQty(dto.getIssueControlQty());
                mtPfepManufacturing.setCompleteControlType(
                                dto.getCompleteControlType() == null ? "" : dto.getCompleteControlType());
                mtPfepManufacturing.setCompleteControlQty(dto.getCompleteControlQty());
                mtPfepManufacturing.setAttritionControlType(
                                dto.getAttritionControlType() == null ? "" : dto.getAttritionControlType());
                mtPfepManufacturing.setAttritionControlQty(dto.getAttritionControlQty());
                mtPfepManufacturing.setOperationAssembleFlag(
                                dto.getOperationAssembleFlag() == null ? "" : dto.getOperationAssembleFlag());
                mtPfepManufacturing.setEnableFlag(dto.getEnableFlag());
                mtPfepManufacturing.setTenantId(tenantId);
                self().updateByPrimaryKey(mtPfepManufacturing);
            } else {
                if (null != materialSiteId) {
                    mtPfepManufacturing.setMaterialSiteId(materialSiteId);
                }
                if (null != dto.getOrganizationType()) {
                    mtPfepManufacturing.setOrganizationType(dto.getOrganizationType());
                }
                if (null != dto.getOrganizationId()) {
                    mtPfepManufacturing.setOrganizationId(dto.getOrganizationId());
                }
                if (null != dto.getDefaultBomId()) {
                    mtPfepManufacturing.setDefaultBomId(dto.getDefaultBomId());
                }
                if (null != dto.getDefaultRoutingId()) {
                    mtPfepManufacturing.setDefaultRoutingId(dto.getDefaultRoutingId());
                }
                if (null != dto.getIssueControlType()) {
                    mtPfepManufacturing.setIssueControlType(dto.getIssueControlType());
                }
                if (null != dto.getIssueControlQty()) {
                    mtPfepManufacturing.setIssueControlQty(dto.getIssueControlQty());
                }
                if (null != dto.getCompleteControlType()) {
                    mtPfepManufacturing.setCompleteControlType(dto.getCompleteControlType());
                }
                if (null != dto.getCompleteControlQty()) {
                    mtPfepManufacturing.setCompleteControlQty(dto.getCompleteControlQty());
                }
                if (null != dto.getAttritionControlType()) {
                    mtPfepManufacturing.setAttritionControlType(dto.getAttritionControlType());
                }
                if (null != dto.getAttritionControlQty()) {
                    mtPfepManufacturing.setAttritionControlQty(dto.getAttritionControlQty());
                }
                if (null != dto.getOperationAssembleFlag()) {
                    mtPfepManufacturing.setOperationAssembleFlag(dto.getOperationAssembleFlag());
                }
                if (null != dto.getEnableFlag()) {
                    mtPfepManufacturing.setEnableFlag(dto.getEnableFlag());
                }
                mtPfepManufacturing.setTenantId(tenantId);
                self().updateByPrimaryKeySelective(mtPfepManufacturing);
            }
            result = dto.getPfepManufacturingId();
        } else {
            MtPfepManufacturing mtPfepManufacturing = new MtPfepManufacturing();
            mtPfepManufacturing.setTenantId(tenantId);
            mtPfepManufacturing.setMaterialSiteId(materialSiteId);
            mtPfepManufacturing.setOrganizationType(dto.getOrganizationType() == null ? "" : dto.getOrganizationType());
            mtPfepManufacturing.setOrganizationId(dto.getOrganizationId() == null ? "" : dto.getOrganizationId());
            mtPfepManufacturing = this.mtPfepManufacturingMapper.selectOne(mtPfepManufacturing);
            if (null == mtPfepManufacturing) {
                if (StringUtils.isEmpty(dto.getEnableFlag())) {
                    throw new MtException("MT_MATERIAL_0071",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0071",
                                                    "MATERIAL", "enableFlag", "【API:materialPfepManufacturingUpdate】"));
                }

                MtPfepManufacturing newPfepManufacturing = new MtPfepManufacturing();
                newPfepManufacturing.setMaterialSiteId(materialSiteId);
                newPfepManufacturing.setOrganizationType(dto.getOrganizationType());
                newPfepManufacturing.setOrganizationId(dto.getOrganizationId());
                newPfepManufacturing.setDefaultBomId(dto.getDefaultBomId());
                newPfepManufacturing.setDefaultRoutingId(dto.getDefaultRoutingId());
                newPfepManufacturing.setIssueControlType(dto.getIssueControlType());
                newPfepManufacturing.setIssueControlQty(dto.getIssueControlQty());
                newPfepManufacturing.setCompleteControlType(dto.getCompleteControlType());
                newPfepManufacturing.setCompleteControlQty(dto.getCompleteControlQty());
                newPfepManufacturing.setAttritionControlType(dto.getAttritionControlType());
                newPfepManufacturing.setAttritionControlQty(dto.getAttritionControlQty());
                newPfepManufacturing.setOperationAssembleFlag(dto.getOperationAssembleFlag());
                newPfepManufacturing.setEnableFlag(dto.getEnableFlag());
                newPfepManufacturing.setTenantId(tenantId);
                self().insertSelective(newPfepManufacturing);

                result = newPfepManufacturing.getPfepManufacturingId();
            } else {
                if (null != dto.getEnableFlag() && "".equals(dto.getEnableFlag())) {
                    throw new MtException("MT_MATERIAL_0071",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0071",
                                                    "MATERIAL", "enableFlag", "【API:materialPfepManufacturingUpdate】"));
                }

                if ("Y".equalsIgnoreCase(fullUpdate)) {
                    mtPfepManufacturing.setMaterialSiteId(materialSiteId);
                    mtPfepManufacturing.setOrganizationType(
                                    dto.getOrganizationType() == null ? "" : dto.getOrganizationType());
                    mtPfepManufacturing
                                    .setOrganizationId(dto.getOrganizationId() == null ? "" : dto.getOrganizationId());
                    mtPfepManufacturing.setDefaultBomId(dto.getDefaultBomId() == null ? "" : dto.getDefaultBomId());
                    mtPfepManufacturing.setDefaultRoutingId(
                                    dto.getDefaultRoutingId() == null ? "" : dto.getDefaultRoutingId());
                    mtPfepManufacturing.setIssueControlType(
                                    dto.getIssueControlType() == null ? "" : dto.getIssueControlType());
                    mtPfepManufacturing.setIssueControlQty(dto.getIssueControlQty());
                    mtPfepManufacturing.setCompleteControlType(
                                    dto.getCompleteControlType() == null ? "" : dto.getCompleteControlType());
                    mtPfepManufacturing.setCompleteControlQty(dto.getCompleteControlQty());
                    mtPfepManufacturing.setAttritionControlType(
                                    dto.getAttritionControlType() == null ? "" : dto.getAttritionControlType());
                    mtPfepManufacturing.setAttritionControlQty(dto.getAttritionControlQty());
                    mtPfepManufacturing.setOperationAssembleFlag(
                                    dto.getOperationAssembleFlag() == null ? "" : dto.getOperationAssembleFlag());
                    mtPfepManufacturing.setEnableFlag(dto.getEnableFlag());
                    mtPfepManufacturing.setTenantId(tenantId);
                    self().updateByPrimaryKey(mtPfepManufacturing);
                } else {
                    if (null != materialSiteId) {
                        mtPfepManufacturing.setMaterialSiteId(materialSiteId);
                    }
                    if (null != dto.getOrganizationType()) {
                        mtPfepManufacturing.setOrganizationType(dto.getOrganizationType());
                    }
                    if (null != dto.getOrganizationId()) {
                        mtPfepManufacturing.setOrganizationId(dto.getOrganizationId());
                    }
                    if (null != dto.getDefaultBomId()) {
                        mtPfepManufacturing.setDefaultBomId(dto.getDefaultBomId());
                    }
                    if (null != dto.getDefaultRoutingId()) {
                        mtPfepManufacturing.setDefaultRoutingId(dto.getDefaultRoutingId());
                    }
                    if (null != dto.getIssueControlType()) {
                        mtPfepManufacturing.setIssueControlType(dto.getIssueControlType());
                    }
                    if (null != dto.getIssueControlQty()) {
                        mtPfepManufacturing.setIssueControlQty(dto.getIssueControlQty());
                    }
                    if (null != dto.getCompleteControlType()) {
                        mtPfepManufacturing.setCompleteControlType(dto.getCompleteControlType());
                    }
                    if (null != dto.getCompleteControlQty()) {
                        mtPfepManufacturing.setCompleteControlQty(dto.getCompleteControlQty());
                    }
                    if (null != dto.getAttritionControlType()) {
                        mtPfepManufacturing.setAttritionControlType(dto.getAttritionControlType());
                    }
                    if (null != dto.getAttritionControlQty()) {
                        mtPfepManufacturing.setAttritionControlQty(dto.getAttritionControlQty());
                    }
                    if (null != dto.getOperationAssembleFlag()) {
                        mtPfepManufacturing.setOperationAssembleFlag(dto.getOperationAssembleFlag());
                    }
                    if (null != dto.getEnableFlag()) {
                        mtPfepManufacturing.setEnableFlag(dto.getEnableFlag());
                    }
                    mtPfepManufacturing.setTenantId(tenantId);
                    self().updateByPrimaryKeySelective(mtPfepManufacturing);
                }
                result = mtPfepManufacturing.getPfepManufacturingId();
            }
        }

        return result;
    }

    @Override
    public MtPfepManufacturingVO5 propertyLimitPfepManufacturingGet(Long tenantId, MtPfepManufacturingVO4 dto) {
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
                                            "organizationType、organizationId",
                                            "【API:propertyLimitPfepManufacturingGet】"));

        }
        if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isNotEmpty(dto.getSiteId())
                        || StringUtils.isNotEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            "materialId、siteId", "【API:propertyLimitPfepManufacturingGet】"));

        }
        if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getMaterialSiteId())) {
            throw new MtException("MT_MATERIAL_0076",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0076", "MATERIAL",
                                            "materialId", "materialSiteId ",
                                            "【API:propertyLimitPfepManufacturingGet】"));
        }
        if (StringUtils.isNotEmpty(dto.getMaterialId()) && StringUtils.isNotEmpty(dto.getMaterialSiteId())
                        && StringUtils.isNotEmpty(dto.getSiteId())) {
            MtMaterialSite materialSite = new MtMaterialSite();
            materialSite.setMaterialId(dto.getMaterialId());
            materialSite.setSiteId(dto.getSiteId());
            String materialSiteId = mtMaterialSiteRepo.materialSiteLimitRelationGet(tenantId, materialSite);
            if (!materialSiteId.equals(dto.getMaterialSiteId())) {
                throw new MtException("MT_MATERIAL_0077",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0077", "MATERIAL",
                                                "materialId", "siteId", "materialSiteId ",
                                                "【API:propertyLimitPfepManufacturingGet】"));
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
                throw new MtException("MT_MATERIAL_0055 ",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055 ", "MATERIAL",
                                                "materialSiteId ", "【API:propertyLimitPfepManufacturingGet】"));
            }
            MATERIAL_ID = site.getMaterialId();
            SITE_ID = site.getSiteId();
        }

        if (StringUtils.isNotEmpty(dto.getMaterialId()) && StringUtils.isNotEmpty(dto.getSiteId())) {
            MtMaterialSite categorySite = new MtMaterialSite();
            categorySite.setSiteId(dto.getSiteId());
            categorySite.setMaterialId(dto.getMaterialId());
            String materialId = mtMaterialSiteRepo.materialSiteLimitRelationGet(tenantId, categorySite);
            if (StringUtils.isEmpty(materialId)) {
                throw new MtException("MT_MATERIAL_0075", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0075", "MATERIAL", "【API:propertyLimitPfepManufacturingGet】"));
            }
            paramMaterialSiteId = materialId;
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
            MtPfepManufacturing pfepManufacturing = new MtPfepManufacturing();
            pfepManufacturing.setTenantId(tenantId);
            pfepManufacturing.setMaterialSiteId(paramMaterialSiteId);
            pfepManufacturing.setOrganizationType(dto.getOrganizationType());
            pfepManufacturing.setOrganizationId(dto.getOrganizationId());
            pfepManufacturing.setEnableFlag("Y");
            MtPfepManufacturing mtPfepManufacturing = mtPfepManufacturingMapper.selectOne(pfepManufacturing);
            MtPfepManufacturingVO5 manufacturingVO5 = new MtPfepManufacturingVO5();
            if (null != mtPfepManufacturing) {
                manufacturingVO5.setTableName("MT_PFEP_MANUFACTURING");
                manufacturingVO5.setKeyId(mtPfepManufacturing.getPfepManufacturingId());
                return manufacturingVO5;
            } else {
                // 获取物料站点下默认类别集的物料类别，然后通过物料类别ID+站点ID+组织类型+组织ID获取
                MtMaterialCategoryAssignVO assignVO = new MtMaterialCategoryAssignVO();
                assignVO.setMaterialId(paramMaterialId);
                assignVO.setSiteId(paramSiteId);
                assignVO.setDefaultType("MANUFACTURING");
                String materialCategoryId =
                                mtMaterialCategoryAssignRepo.defaultSetMaterialAssignCategoryGet(tenantId, assignVO);

                MtMaterialCategorySite categorySite = new MtMaterialCategorySite();
                categorySite.setSiteId(paramSiteId);
                categorySite.setMaterialCategoryId(materialCategoryId);
                String materialCategorySiteId =
                                mtMaterialCategorySiteRepo.materialCategorySiteLimitRelationGet(tenantId, categorySite);

                MtPfepManufacturingCategory pfepManufacturingCategory = new MtPfepManufacturingCategory();
                pfepManufacturingCategory.setMaterialCategorySiteId(materialCategorySiteId);
                pfepManufacturingCategory.setOrganizationType(dto.getOrganizationType());
                pfepManufacturingCategory.setOrganizationId(dto.getOrganizationId());
                pfepManufacturingCategory.setEnableFlag("Y");
                pfepManufacturingCategory.setTenantId(tenantId);
                MtPfepManufacturingCategory mtPfepManufacturingCategory =
                                mtPfepManufacturingCategoryMapper.selectOne(pfepManufacturingCategory);
                if (null != mtPfepManufacturingCategory) {
                    manufacturingVO5.setTableName("MT_PFEP_MANUFACTURING_CATEGORY");
                    manufacturingVO5.setKeyId(mtPfepManufacturingCategory.getPfepManufacturingCategoryId());
                    return manufacturingVO5;
                }

                // 根据P1(materialSiteId)作为参数，同时限定组织类型、组织ID为空，从MT_PFEP_INVENTORY获取PFEP_MANUFACTURING_ID
                pfepManufacturing.setTenantId(tenantId);
                pfepManufacturing.setMaterialSiteId(paramMaterialSiteId);
                pfepManufacturing.setOrganizationType(null);
                pfepManufacturing.setOrganizationId(null);
                pfepManufacturing.setEnableFlag("Y");
                MtPfepManufacturing mtPfepManufacturing2 = mtPfepManufacturingMapper.selectOne(pfepManufacturing);
                if (null != mtPfepManufacturing2) {
                    manufacturingVO5.setTableName("MT_PFEP_MANUFACTURING");
                    manufacturingVO5.setKeyId(mtPfepManufacturing2.getPfepManufacturingId());
                    return manufacturingVO5;
                }

                // 获取物料站点下默认类别集的物料类别，然后通过物料类别ID+站点ID、限定组织类型、组织ID为空，获取MT_PFEP_INVENTORY_CATEGORY中PFEP_MANUFACTURING_CATEGORY_ID
                pfepManufacturingCategory.setMaterialCategorySiteId(materialCategorySiteId);
                pfepManufacturingCategory.setOrganizationType(null);
                pfepManufacturingCategory.setOrganizationId(null);
                pfepManufacturingCategory.setEnableFlag("Y");
                pfepManufacturingCategory.setTenantId(tenantId);
                MtPfepManufacturingCategory mtPfepInventoryCategory2 =
                                mtPfepManufacturingCategoryMapper.selectOne(pfepManufacturingCategory);
                if (null != mtPfepInventoryCategory2) {
                    manufacturingVO5.setTableName("MT_PFEP_MANUFACTURING_CATEGORY");
                    manufacturingVO5.setKeyId(mtPfepInventoryCategory2.getPfepManufacturingCategoryId());
                    return manufacturingVO5;
                } else {
                    return null;
                }


            }
        }

        // b) 若组织类型和组织ID同时无值 获取物料pfep属性主键
        if (StringUtils.isEmpty(dto.getOrganizationId()) && StringUtils.isEmpty(dto.getOrganizationType())) {
            MtPfepManufacturing pfepManufacturing = new MtPfepManufacturing();
            pfepManufacturing.setTenantId(tenantId);
            pfepManufacturing.setMaterialSiteId(paramMaterialSiteId);
            pfepManufacturing.setEnableFlag("Y");
            MtPfepManufacturing mtPfepManufacturing = mtPfepManufacturingMapper.selectOne(pfepManufacturing);
            MtPfepManufacturingVO5 manufacturingVO5 = new MtPfepManufacturingVO5();
            if (null != mtPfepManufacturing) {
                manufacturingVO5.setTableName("MT_PFEP_MANUFACTURING");
                manufacturingVO5.setKeyId(mtPfepManufacturing.getPfepManufacturingId());
                return manufacturingVO5;
            }

            MtMaterialCategoryAssignVO assignVO = new MtMaterialCategoryAssignVO();
            assignVO.setMaterialId(paramMaterialId);
            assignVO.setSiteId(paramSiteId);
            assignVO.setDefaultType("MANUFACTURING");
            String materialCategoryId =
                            mtMaterialCategoryAssignRepo.defaultSetMaterialAssignCategoryGet(tenantId, assignVO);

            MtMaterialCategorySite categorySite = new MtMaterialCategorySite();
            categorySite.setSiteId(paramSiteId);
            categorySite.setMaterialCategoryId(materialCategoryId);
            String materialCategorySiteId =
                            mtMaterialCategorySiteRepo.materialCategorySiteLimitRelationGet(tenantId, categorySite);

            MtPfepManufacturingCategory pfepManufacturingCategory = new MtPfepManufacturingCategory();
            pfepManufacturingCategory.setMaterialCategorySiteId(materialCategorySiteId);
            pfepManufacturingCategory.setEnableFlag("Y");
            pfepManufacturingCategory.setTenantId(tenantId);
            MtPfepManufacturingCategory mtPfepManufacturingCategory =
                            mtPfepManufacturingCategoryMapper.selectOne(pfepManufacturingCategory);
            if (null != mtPfepManufacturingCategory) {
                manufacturingVO5.setTableName("MT_PFEP_MANUFACTURING_CATEGORY");
                manufacturingVO5.setKeyId(mtPfepManufacturingCategory.getPfepManufacturingCategoryId());
                return manufacturingVO5;
            }
        }

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pfepManufacturingAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10) {
        if (StringUtils.isEmpty(mtExtendVO10.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId ", "【API：pfepManufacturingAttrPropertyUpdate】"));
        }
        MtPfepManufacturing pfepManufacturing = new MtPfepManufacturing();
        pfepManufacturing.setTenantId(tenantId);
        pfepManufacturing.setPfepManufacturingId(mtExtendVO10.getKeyId());
        pfepManufacturing = mtPfepManufacturingMapper.selectOne(pfepManufacturing);
        if (null == pfepManufacturing) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            mtExtendVO10.getKeyId(), "mt_pfep_manufacturing",
                                            "【API:pfepManufacturingAttrPropertyUpdate】"));
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_pfep_manufacturing_attr", mtExtendVO10.getKeyId(),
                        mtExtendVO10.getEventId(), mtExtendVO10.getAttrs());

    }

    @Override
    public List<MtPfepManufacturing> selectpfepManufacturingByMaterialSiteId(Long tenantId,
                                                                             List<String> materialSIteIds) {
        if (CollectionUtils.isEmpty(materialSIteIds)) {
            return Collections.emptyList();
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("p.MATERIAL_SITE_ID", materialSIteIds, 1000);
        return mtPfepManufacturingMapper.selectByMaterialSiteId(tenantId, whereInValuesSql);
    }

    @Override
    public List<MtPfepManufacturingVO11> pfepManufacturingBatchGet(Long tenantId, List<MtPfepManufacturingVO1> voList,
                                                                   List<String> fields) {
        final String apiName = "【API:pfepManufacturingBatchGet】";
        return pfepManufacturingBatchGetCommon(tenantId, voList, fields, apiName);
    }

    private List<MtPfepManufacturingVO11> pfepManufacturingBatchGetCommon(Long tenantId,
                                                                          List<MtPfepManufacturingVO1> voList, List<String> fields, String apiName) {
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
        List<String> allFields = Arrays.asList(MtPfepManufacturing.FIELD_DEFAULT_BOM_ID,
                MtPfepManufacturing.FIELD_DEFAULT_ROUTING_ID, MtPfepManufacturing.FIELD_ISSUE_CONTROL_TYPE,
                MtPfepManufacturing.FIELD_ISSUE_CONTROL_QTY, MtPfepManufacturing.FIELD_COMPLETE_CONTROL_TYPE,
                MtPfepManufacturing.FIELD_COMPLETE_CONTROL_QTY,
                MtPfepManufacturing.FIELD_ATTRITION_CONTROL_TYPE,
                MtPfepManufacturing.FIELD_ATTRITION_CONTROL_QTY,
                MtPfepManufacturing.FIELD_OPERATION_ASSEMBLE_FLAG);
        List<String> inputFields;
        if (CollectionUtils.isEmpty(fields)) {
            inputFields = allFields;
        } else {
            if (fields.stream().anyMatch(t -> !allFields.contains(t))) {
                List<String> unKnow = fields.stream().filter(t -> !allFields.contains(t)).collect(toList());
                StringBuilder info = new StringBuilder(MtBaseConstants.STRING_SPECIAL);
                for (String ever : unKnow) {
                    info.append(ever).append(",");
                }
                info.deleteCharAt(info.length() - 1);
                throw new MtException("MT_MATERIAL_0107", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_0107","MATERIAL", info.toString(), apiName));

            }
            inputFields = fields;
        }
        // 将null置为空或者0
        voList.forEach(t -> {
            if (StringUtils.isEmpty(t.getOrganizationType())) {
                t.setOrganizationType(MtBaseConstants.STRING_SPECIAL);
            }
            if (MtIdHelper.isIdNull(t.getOrganizationId())) {
                t.setOrganizationId("");
            }
        });

        List<MtMaterialSiteVO3> mtMaterialSite = new ArrayList<>();
        List<MtMaterialCategoryAssignVO5> assignCondition = new ArrayList<>();

        // 批量获取站点物料关系
        for (MtPfepManufacturingVO1 vo6 : voList) {
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
        List<MtPfepManufacturingVO1> inputList = voList.stream().distinct().collect(Collectors.toList());
        // 先准备好结果数据
        List<MtPfepManufacturingVO11> result = transMapper.manufacturingVO1ToManufacturingVO11List(inputList);

        List<MtPfepManufacturingVO1> removeList = new ArrayList<>();

        // 第一次查询，根据物料和组织
        List<MtPfepManufacturing> materialList = baseMaterialPfepBatchGet(tenantId, inputList.stream()
                .filter(t -> StringUtils.isNotEmpty(t.getOrganizationType())
                        && MtIdHelper.isIdNotNull(t.getOrganizationId()))
                .collect(Collectors.toList()), inputFields, materialSiteIdMap, false);
        // 第一次拼接
        if (CollectionUtils.isNotEmpty(materialList)) {
            for (MtPfepManufacturingVO1 input : inputList) {
                if (MtIdHelper.isIdNotNull(input.getOrganizationId())
                        && StringUtils.isNotEmpty(input.getOrganizationType())) {
                    Optional<MtPfepManufacturingVO11> resultOp = result.stream()
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
                        MtPfepManufacturingVO11 one = resultOp.get();
                        // 物料站点关系
                        String materialSiteId = materialSiteIdMap
                                .get(input.getMaterialId() + MtBaseConstants.SPLIT_CHAR + input.getSiteId());
                        Optional<MtPfepManufacturing> pfepOp = materialList.stream()
                                .filter(t -> MtIdHelper.isSame(materialSiteId, t.getMaterialSiteId())
                                        && MtFieldsHelper.isSame(one.getOrganizationType(),
                                        t.getOrganizationType())
                                        && MtIdHelper.isSame(one.getOrganizationId(),
                                        t.getOrganizationId()))
                                .findFirst();
                        if (pfepOp.isPresent()) {
                            MtPfepManufacturingVO record = transMapper.manufacturingToManufacturingVO(pfepOp.get());
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
        List<MtPfepManufacturingCategory> materialCategoryList = baseMaterialCategoryPfepBatchGet(tenantId,
                inputList.stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getOrganizationType())
                                && MtIdHelper.isIdNotNull(t.getOrganizationId()))
                        .collect(Collectors.toList()),
                inputFields, categorySiteIdMap, false);
        // 第二次拼接
        if (CollectionUtils.isNotEmpty(materialCategoryList)) {
            for (MtPfepManufacturingVO1 input : inputList) {
                if (MtIdHelper.isIdNotNull(input.getOrganizationId())
                        && StringUtils.isNotEmpty(input.getOrganizationType())) {
                    Optional<MtPfepManufacturingVO11> resultOp = result.stream()
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
                        MtPfepManufacturingVO11 one = resultOp.get();
                        // 获取物料类别站点ID,有可能找不到
                        String materialCategorySiteId = categorySiteIdMap
                                .get(input.getMaterialId() + MtBaseConstants.SPLIT_CHAR + input.getSiteId());
                        Optional<MtPfepManufacturingCategory> pfepOp = materialCategoryList.stream().filter(
                                t -> MtIdHelper.isSame(materialCategorySiteId, t.getMaterialCategorySiteId())
                                        && MtFieldsHelper.isSame(one.getOrganizationType(),
                                        t.getOrganizationType())
                                        && MtIdHelper.isSame(one.getOrganizationId(),
                                        t.getOrganizationId()))
                                .findFirst();
                        if (pfepOp.isPresent()) {
                            // 先保留上面步骤就获取的值,作为临时变量
                            MtPfepManufacturingVO record = transMapper.manufacturingVO11ToManufacturingVO(one);
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
        List<MtPfepManufacturing> materialList2 =
                baseMaterialPfepBatchGet(tenantId, inputList, inputFields, materialSiteIdMap, true);
        // 第三次拼接
        if (CollectionUtils.isNotEmpty(materialList2)) {
            for (MtPfepManufacturingVO1 input : inputList) {

                Optional<MtPfepManufacturingVO11> resultOp = result.stream()
                        .filter(t -> MtIdHelper.isSame(input.getMaterialId(), t.getMaterialId())
                                && MtIdHelper.isSame(input.getSiteId(), t.getSiteId())
                                && MtFieldsHelper.isSame(input.getOrganizationType(),
                                t.getOrganizationType())
                                && MtIdHelper.isSame(input.getOrganizationId(), t.getOrganizationId()))
                        .findFirst();
                // 这里正常不可能找不到
                if (resultOp.isPresent()) {
                    MtPfepManufacturingVO11 one = resultOp.get();
                    // 物料站点关系
                    String materialSiteId = materialSiteIdMap
                            .get(input.getMaterialId() + MtBaseConstants.SPLIT_CHAR + input.getSiteId());
                    Optional<MtPfepManufacturing> pfepOp = materialList2.stream()
                            .filter(t -> MtIdHelper.isSame(materialSiteId, t.getMaterialSiteId())).findFirst();
                    if (pfepOp.isPresent()) {
                        // 先保留上面步骤就获取的值,作为临时变量
                        MtPfepManufacturingVO record = transMapper.manufacturingVO11ToManufacturingVO(one);
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
        List<MtPfepManufacturingCategory> materialCategoryList2 =
                baseMaterialCategoryPfepBatchGet(tenantId, inputList, inputFields, categorySiteIdMap, true);
        // 第四次拼接
        if (CollectionUtils.isNotEmpty(materialCategoryList2)) {
            for (MtPfepManufacturingVO1 input : inputList) {
                Optional<MtPfepManufacturingVO11> resultOp = result.stream()
                        .filter(t -> MtIdHelper.isSame(input.getMaterialId(), t.getMaterialId())
                                && MtIdHelper.isSame(input.getSiteId(), t.getSiteId())
                                && MtFieldsHelper.isSame(input.getOrganizationType(),
                                t.getOrganizationType())
                                && MtIdHelper.isSame(input.getOrganizationId(), t.getOrganizationId()))
                        .findFirst();
                // 这里正常不可能找不到
                if (resultOp.isPresent()) {
                    MtPfepManufacturingVO11 one = resultOp.get();
                    // 获取物料类别站点ID,有可能找不到
                    String materialCategorySiteId = categorySiteIdMap
                            .get(input.getMaterialId() + MtBaseConstants.SPLIT_CHAR + input.getSiteId());
                    Optional<MtPfepManufacturingCategory> pfepOp = materialCategoryList2.stream().filter(
                            t -> MtIdHelper.isSame(materialCategorySiteId, t.getMaterialCategorySiteId()))
                            .findFirst();
                    if (pfepOp.isPresent()) {
                        // 先保留上面步骤就获取的值,作为临时变量
                        MtPfepManufacturingVO record = transMapper.manufacturingVO11ToManufacturingVO(one);
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

    private List<MtPfepManufacturing> baseMaterialPfepBatchGet(Long tenantId, List<MtPfepManufacturingVO1> inputList,
                                                               List<String> inputFields, Map<String, String> materialSiteIdMap, boolean ignoreOrganization) {
        if (CollectionUtils.isNotEmpty(inputList)) {
            List<String> list = new ArrayList<>(inputFields);
            list.add(MtPfepManufacturing.FIELD_MATERIAL_SITE_ID);
            list.add(MtPfepManufacturing.FIELD_ORGANIZATION_TYPE);
            list.add(MtPfepManufacturing.FIELD_ORGANIZATION_ID);

            // 改为以分治方式查询
            List<List<MtPfepManufacturingVO1>> batchList =
                    Lists.partition(inputList.stream().distinct().collect(toList()), 1000);

            List<Future<List<MtPfepManufacturing>>> futureList = new ArrayList<>();
            List<MtPfepManufacturing> result = new ArrayList<>();
            CustomUserDetails details = DetailsHelper.getUserDetails();
            for (List<MtPfepManufacturingVO1> ever : batchList) {

                Condition.Builder builder =
                        Condition.builder(MtPfepManufacturing.class).select(list.toArray(new String[0]));
                for (MtPfepManufacturingVO1 input : ever) {
                    // 物料站点关系
                    String materialSiteId = materialSiteIdMap
                            .get(input.getMaterialId() + MtBaseConstants.SPLIT_CHAR + input.getSiteId());
                    if (MtIdHelper.isIdNotNull(materialSiteId)) {
                        Sqls sqls = Sqls.custom().andEqualTo(MtPfepManufacturing.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtPfepManufacturing.FIELD_MATERIAL_SITE_ID, materialSiteId)
                                .andEqualTo(MtPfepManufacturing.FIELD_ENABLE_FLAG, MtBaseConstants.YES);
                        if (ignoreOrganization) {
                            sqls.andEqualTo(MtPfepManufacturing.FIELD_ORGANIZATION_TYPE, MtBaseConstants.STRING_SPECIAL)
                                    .andEqualTo(MtPfepManufacturing.FIELD_ORGANIZATION_ID,
                                            MtBaseConstants.LONG_SPECIAL);
                        } else {
                            sqls.andEqualTo(MtPfepManufacturing.FIELD_ORGANIZATION_TYPE, input.getOrganizationType())
                                    .andEqualTo(MtPfepManufacturing.FIELD_ORGANIZATION_ID,
                                            input.getOrganizationId());
                        }
                        builder.orWhere(sqls);
                    }
                }
                Future<List<MtPfepManufacturing>> one = poolExecutor.submit(() -> {
                    DetailsHelper.setCustomUserDetails(details);
                    SecurityTokenHelper.close();
                    return mtPfepManufacturingMapper.selectByCondition(builder.build());
                });
                futureList.add(one);
                // 最大开启10个线程
                if (futureList.size() > 10) {
                    for (Future<List<MtPfepManufacturing>> oneFuture : futureList) {
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
            for (Future<List<MtPfepManufacturing>> oneFuture : futureList) {
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

    private List<MtPfepManufacturingCategory> baseMaterialCategoryPfepBatchGet(Long tenantId,
                                                                               List<MtPfepManufacturingVO1> inputList, List<String> inputFields,
                                                                               Map<String, String> categorySiteIdMap, boolean ignoreOrganization) {
        if (CollectionUtils.isNotEmpty(inputList)) {
            List<String> list = new ArrayList<>(inputFields);
            list.add(MtPfepManufacturingCategory.FIELD_MATERIAL_CATEGORY_SITE_ID);
            list.add(MtPfepManufacturingCategory.FIELD_ORGANIZATION_TYPE);
            list.add(MtPfepManufacturingCategory.FIELD_ORGANIZATION_ID);

            // 改为以分治方式查询
            List<List<MtPfepManufacturingVO1>> batchList =
                    Lists.partition(inputList.stream().distinct().collect(toList()), 1000);

            List<Future<List<MtPfepManufacturingCategory>>> futureList = new ArrayList<>();
            List<MtPfepManufacturingCategory> result = new ArrayList<>();
            CustomUserDetails details = DetailsHelper.getUserDetails();
            for (List<MtPfepManufacturingVO1> ever : batchList) {
                Condition.Builder builder = Condition.builder(MtPfepManufacturingCategory.class)
                        .select(list.toArray(new String[0]));
                for (MtPfepManufacturingVO1 input : ever) {
                    String materialCategorySiteId = categorySiteIdMap
                            .get(input.getMaterialId() + MtBaseConstants.SPLIT_CHAR + input.getSiteId());
                    if (MtIdHelper.isIdNotNull(materialCategorySiteId)) {
                        Sqls sqls = Sqls.custom()
                                .andEqualTo(MtPfepManufacturingCategory.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtPfepManufacturingCategory.FIELD_MATERIAL_CATEGORY_SITE_ID,
                                        materialCategorySiteId)
                                .andEqualTo(MtPfepManufacturingCategory.FIELD_ENABLE_FLAG, MtBaseConstants.YES);
                        if (ignoreOrganization) {
                            sqls.andEqualTo(MtPfepManufacturingCategory.FIELD_ORGANIZATION_TYPE,
                                    MtBaseConstants.STRING_SPECIAL)
                                    .andEqualTo(MtPfepManufacturingCategory.FIELD_ORGANIZATION_ID,
                                            MtBaseConstants.LONG_SPECIAL);
                        } else {
                            sqls.andEqualTo(MtPfepManufacturingCategory.FIELD_ORGANIZATION_TYPE,
                                    input.getOrganizationType())
                                    .andEqualTo(MtPfepManufacturingCategory.FIELD_ORGANIZATION_ID,
                                            input.getOrganizationId());
                        }
                        builder.orWhere(sqls);
                    }
                }
                Future<List<MtPfepManufacturingCategory>> one = poolExecutor.submit(() -> {
                    DetailsHelper.setCustomUserDetails(details);
                    SecurityTokenHelper.close();
                    return mtPfepManufacturingCategoryMapper.selectByCondition(builder.build());
                });
                futureList.add(one);
                // 最大开启10个线程
                if (futureList.size() > 10) {
                    for (Future<List<MtPfepManufacturingCategory>> oneFuture : futureList) {
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
            for (Future<List<MtPfepManufacturingCategory>> oneFuture : futureList) {
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
