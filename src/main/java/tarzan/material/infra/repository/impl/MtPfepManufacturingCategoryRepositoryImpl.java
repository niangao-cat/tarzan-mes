package tarzan.material.infra.repository.impl;

import java.util.List;
import java.util.stream.Collectors;

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
import tarzan.material.domain.entity.MtPfepManufacturingCategory;
import tarzan.material.domain.repository.MtMaterialCategorySiteRepository;
import tarzan.material.domain.repository.MtPfepManufacturingCategoryRepository;
import tarzan.material.domain.vo.MtPfepManufactureCateVO1;
import tarzan.material.infra.mapper.MtPfepManufacturingCategoryMapper;
import tarzan.method.domain.entity.MtBom;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.repository.MtBomRepository;
import tarzan.method.domain.repository.MtRouterRepository;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

/**
 * 物料类别生产属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Component
public class MtPfepManufacturingCategoryRepositoryImpl extends BaseRepositoryImpl<MtPfepManufacturingCategory>
                implements MtPfepManufacturingCategoryRepository {
    private static final String TABLE_NAME = "mt_pfep_manufacturing_category";
    private static final String ATTR_TABLE_NAME = "mt_pfep_mfg_catg_attr";

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

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
    private MtMaterialCategorySiteRepository mtMaterialCategorySiteRepository;

    /**
     * materialCategoryPfepManufacturingUpdate-物料类别生产属性新增&更新
     *
     * @param tenantId
     * @param dto
     * @param fullUpdate
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/9/18
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String materialCategoryPfepManufacturingUpdate(Long tenantId, MtPfepManufactureCateVO1 dto,
                    String fullUpdate) {
        // 1. 验证参数有效性
        if (StringUtils.isNotEmpty(dto.getDefaultBomId())) {
            MtBom mtBom = mtBomRepository.bomBasicGet(tenantId, dto.getDefaultBomId());
            if (mtBom == null) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                                "defaultBomId:" + dto.getDefaultBomId(),
                                                "【API:materialCategoryPfepManufacturingUpdate】"));
            }
        }
        if (StringUtils.isNotEmpty(dto.getDefaultRoutingId())) {
            MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, dto.getDefaultRoutingId());
            if (mtRouter == null) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                                "defaultRoutingId:" + dto.getDefaultBomId(),
                                                "【API:materialCategoryPfepManufacturingUpdate】"));
            }
        }
        if (StringUtils.isNotEmpty(dto.getOrganizationType())) {
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("MATERIAL");
            mtGenTypeVO2.setTypeGroup("PFEP_ORGANIZATION_TYPE");
            List<MtGenType> mtGenTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
            if (CollectionUtils.isEmpty(mtGenTypes)) {
                throw new MtException("MT_MATERIAL_0070",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0070", "MATERIAL",
                                                "organizationType:" + dto.getOrganizationType(), "",
                                                "【API:materialCategoryPfepManufacturingUpdate】"));
            }

            List<String> typeCodes = mtGenTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            if (!typeCodes.contains(dto.getOrganizationType())) {
                throw new MtException("MT_MATERIAL_0070",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0070", "MATERIAL",
                                                "organizationType:" + dto.getOrganizationType(), typeCodes.toString(),
                                                "【API:materialCategoryPfepManufacturingUpdate】"));
            }
        }

        MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
        mtGenTypeVO2.setModule("MATERIAL");
        mtGenTypeVO2.setTypeGroup("CONTROL_TYPE");
        List<MtGenType> mtGenTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
        if (CollectionUtils.isEmpty(mtGenTypes)) {
            throw new MtException("MT_MATERIAL_0070",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0070", "MATERIAL",
                                            "IssueControlType:" + dto.getIssueControlType(), "",
                                            "【API:materialCategoryPfepManufacturingUpdate】"));
        }

        List<String> controlTypes = mtGenTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());

        // controlType 校验
        String errorMessage = "issueControlType:" + dto.getIssueControlType();
        controlTypeContainCheck(tenantId, dto.getIssueControlType(), controlTypes, errorMessage);

        errorMessage = "completeControlType:" + dto.getCompleteControlType();
        controlTypeContainCheck(tenantId, dto.getCompleteControlType(), controlTypes, errorMessage);

        errorMessage = "attritionControlType:" + dto.getAttritionControlType();
        controlTypeContainCheck(tenantId, dto.getAttritionControlType(), controlTypes, errorMessage);

        // type - qty 同时输入校验
        errorMessage = "issueControlType、issueControlQty";
        doubleDataEmptyCheck(tenantId, dto.getIssueControlType(), dto.getIssueControlQty(), errorMessage);

        errorMessage = "completeControlType、completeControlQty";
        doubleDataEmptyCheck(tenantId, dto.getCompleteControlType(), dto.getCompleteControlQty(), errorMessage);

        errorMessage = "attritionControlType、attritionControlQty";
        doubleDataEmptyCheck(tenantId, dto.getAttritionControlType(), dto.getAttritionControlQty(), errorMessage);

        errorMessage = "organizationType、organizationId";
        doubleDataEmptyCheck(tenantId, dto.getOrganizationType(), dto.getOrganizationId(), errorMessage);

        if (StringUtils.isEmpty(dto.getMaterialCategoryId()) && StringUtils.isEmpty(dto.getMaterialCategorySiteId())) {
            throw new MtException("MT_MATERIAL_0076",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0076", "MATERIAL",
                                            "materialCategoryId", "materialCategorySiteId ",
                                            "【API:materialCategoryPfepManufacturingUpdate】"));
        }

        errorMessage = "materialCategoryId、siteId";
        doubleDataEmptyCheck(tenantId, dto.getMaterialCategoryId(), dto.getSiteId(), errorMessage);

        String materialCategorySiteId = dto.getMaterialCategorySiteId();

        // 当输入了 materialCategorySiteId
        if (StringUtils.isNotEmpty(materialCategorySiteId)) {
            // 获取 MtMaterialCategorySite 数据
            MtMaterialCategorySite mtMaterialCategorySite = mtMaterialCategorySiteRepository
                            .relationLimitMaterialCategorySiteGet(tenantId, materialCategorySiteId);
            if (mtMaterialCategorySite == null || StringUtils.isEmpty(mtMaterialCategorySite.getSiteId())) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                                "materialCategorySiteId:" + materialCategorySiteId,
                                                "【API:materialCategoryPfepManufacturingUpdate】"));
            }

            // 校验站点类型
            MtModSite mtModSite =
                            mtModSiteRepository.siteBasicPropertyGet(tenantId, mtMaterialCategorySite.getSiteId());
            if (mtModSite == null || !"MANUFACTURING".equals(mtModSite.getSiteType())) {
                throw new MtException("MT_MATERIAL_0078", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0078", "MATERIAL", "【API:materialCategoryPfepManufacturingUpdate】"));
            }

        }

        if (StringUtils.isNotEmpty(dto.getSiteId())) {
            MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
            if (mtModSite == null || !"MANUFACTURING".equals(mtModSite.getSiteType())) {
                throw new MtException("MT_MATERIAL_0078", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0078", "MATERIAL", "【API:materialCategoryPfepManufacturingUpdate】"));
            }
        }
        if (StringUtils.isNotEmpty(dto.getMaterialCategoryId())
                        && StringUtils.isNotEmpty(dto.getMaterialCategorySiteId())
                        && StringUtils.isNotEmpty(dto.getSiteId())) {
            // 则根据条件查询已存在的关系id
            MtMaterialCategorySite mtMaterialCategorySite = new MtMaterialCategorySite();
            mtMaterialCategorySite.setTenantId(tenantId);
            mtMaterialCategorySite.setMaterialCategoryId(dto.getMaterialCategoryId());
            mtMaterialCategorySite.setSiteId(dto.getSiteId());
            String alreadyMaterialCategorySiteId = mtMaterialCategorySiteRepository
                            .materialCategorySiteLimitRelationGet(tenantId, mtMaterialCategorySite);

            if (!alreadyMaterialCategorySiteId.equals(materialCategorySiteId)) {
                throw new MtException("MT_MATERIAL_0077",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0077", "MATERIAL",
                                                "materialCategoryId", "siteId", "materialCategorySiteId",
                                                "【API:materialCategoryPfepManufacturingUpdate】"));
            }
        }
        // 则根据条件查询已存在的关系id
        MtMaterialCategorySite mtMaterialCategorySite = new MtMaterialCategorySite();
        mtMaterialCategorySite.setTenantId(tenantId);
        mtMaterialCategorySite.setMaterialCategoryId(dto.getMaterialCategoryId());
        mtMaterialCategorySite.setSiteId(dto.getSiteId());
        String alreadyMaterialCategorySiteId = mtMaterialCategorySiteRepository
                        .materialCategorySiteLimitRelationGet(tenantId, mtMaterialCategorySite);
        if (StringUtils.isEmpty(alreadyMaterialCategorySiteId)) {
            throw new MtException("MT_MATERIAL_0075", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0075", "MATERIAL", "【API:materialCategoryPfepManufacturingUpdate】"));
        }

        // 判断 materialCategorySiteId 是否有输入
        if (StringUtils.isEmpty(materialCategorySiteId)) {
            materialCategorySiteId = alreadyMaterialCategorySiteId;
        } else {
            if (!alreadyMaterialCategorySiteId.equals(materialCategorySiteId)) {
                throw new MtException("MT_MATERIAL_0075", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0075", "MATERIAL", "【API:materialCategoryPfepManufacturingUpdate】"));
            }
        }

        MtPfepManufacturingCategory mtPfepManufacturingCategory;

        // 2. 根据传入参数判断新增或更新模式
        if (StringUtils.isNotEmpty(dto.getPfepManufacturingCategoryId())) {
            mtPfepManufacturingCategory =
                            mtPfepManufacturingCategoryMapper.selectByPrimaryKey(dto.getPfepManufacturingCategoryId());
            if (mtPfepManufacturingCategory == null) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                                "pefpManufacturingCategoryId :" + dto.getPfepManufacturingCategoryId(),
                                                "【API:materialCategoryPfepManufacturingUpdate】"));
            }
        } else {
            mtPfepManufacturingCategory = new MtPfepManufacturingCategory();
            mtPfepManufacturingCategory.setTenantId(tenantId);
            mtPfepManufacturingCategory.setMaterialCategorySiteId(materialCategorySiteId);
            mtPfepManufacturingCategory.setOrganizationType(
                            StringUtils.isEmpty(dto.getOrganizationType()) ? "" : dto.getOrganizationType());
            mtPfepManufacturingCategory.setOrganizationId(
                            StringUtils.isEmpty(dto.getOrganizationId()) ? "" : dto.getOrganizationId());
            mtPfepManufacturingCategory = mtPfepManufacturingCategoryMapper.selectOne(mtPfepManufacturingCategory);
        }

        if (mtPfepManufacturingCategory == null) {
            if (StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("MT_MATERIAL _0071",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL _0071", "MATERIAL",
                                                "enableFlag", "【API:materialCategoryPfepManufacturingUpdate】"));
            }

            // 新增模式
            mtPfepManufacturingCategory = new MtPfepManufacturingCategory();
            BeanUtils.copyProperties(dto, mtPfepManufacturingCategory);
            mtPfepManufacturingCategory.setTenantId(tenantId);
            mtPfepManufacturingCategory.setMaterialCategorySiteId(materialCategorySiteId);

            self().insertSelective(mtPfepManufacturingCategory);
        } else {
            if (StringUtils.isNotEmpty(dto.getEnableFlag())) {
                mtPfepManufacturingCategory.setOrganizationType(dto.getOrganizationType());
                mtPfepManufacturingCategory.setOrganizationId(dto.getOrganizationId());
                mtPfepManufacturingCategory.setDefaultBomId(dto.getDefaultBomId());
                mtPfepManufacturingCategory.setDefaultRoutingId(dto.getDefaultRoutingId());
                mtPfepManufacturingCategory.setIssueControlType(dto.getIssueControlType());
                mtPfepManufacturingCategory.setCompleteControlType(dto.getCompleteControlType());
                mtPfepManufacturingCategory.setAttritionControlType(dto.getAttritionControlType());
                mtPfepManufacturingCategory.setOperationAssembleFlag(dto.getOperationAssembleFlag());
                mtPfepManufacturingCategory.setEnableFlag(dto.getEnableFlag());

                mtPfepManufacturingCategory.setMaterialCategorySiteId(materialCategorySiteId);
                mtPfepManufacturingCategory.setIssueControlQty(dto.getIssueControlQty());
                mtPfepManufacturingCategory.setCompleteControlQty(dto.getCompleteControlQty());
                mtPfepManufacturingCategory.setAttritionControlQty(dto.getAttritionControlQty());

                if ("Y".equals(fullUpdate)) {
                    mtPfepManufacturingCategory = (MtPfepManufacturingCategory) ObjectFieldsHelper
                                    .setStringFieldsEmpty(mtPfepManufacturingCategory);
                    self().updateByPrimaryKey(mtPfepManufacturingCategory);
                } else {
                    self().updateByPrimaryKeySelective(mtPfepManufacturingCategory);
                }
            }
        }

        return mtPfepManufacturingCategory.getPfepManufacturingCategoryId();
    }

    /**
     * xxControlType输入是否满足要求检验
     *
     * @param tenantId
     * @param controlTypes
     * @param errorMessage
     * @return void
     * @author chuang.yang
     * @date 2019/9/18
     */
    public void controlTypeContainCheck(Long tenantId, String controlType, List<String> controlTypes,
                    String errorMessage) {
        if (StringUtils.isNotEmpty(controlType) && !controlTypes.contains(controlType)) {
            throw new MtException("MT_MATERIAL_0070",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0070", "MATERIAL",
                                            errorMessage, controlTypes.toString(),
                                            "【API:materialCategoryPfepManufacturingUpdate】"));
        }
    }

    /**
     * String-Double 值对同时输入、同时不输入校验
     *
     * @param tenantId
     * @param type
     * @param qty
     * @param errorMessage
     * @return void
     * @author chuang.yang
     * @date 2019/9/18
     */
    public void doubleDataEmptyCheck(Long tenantId, String type, Double qty, String errorMessage) {
        if (StringUtils.isNotEmpty(type) && qty == null || StringUtils.isEmpty(type) && qty != null) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            errorMessage, "【API:materialCategoryPfepManufacturingUpdate】"));
        }
    }

    /**
     * String-String 值对同时输入、同时不输入校验
     *
     * @param tenantId
     * @param str1
     * @param str2
     * @param errorMessage
     * @return void
     * @author chuang.yang
     * @date 2019/9/18
     */
    public void doubleDataEmptyCheck(Long tenantId, String str1, String str2, String errorMessage) {
        if (StringUtils.isNotEmpty(str1) && StringUtils.isEmpty(str2)
                        || StringUtils.isEmpty(str1) && StringUtils.isNotEmpty(str2)) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            errorMessage, "【API:materialCategoryPfepManufacturingUpdate】"));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pfepMfgCatgAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId", "【API:pfepMfgCatgAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtPfepManufacturingCategory mtPfepManufacturingCategory = new MtPfepManufacturingCategory();
        mtPfepManufacturingCategory.setTenantId(tenantId);
        mtPfepManufacturingCategory.setPfepManufacturingCategoryId(dto.getKeyId());
        mtPfepManufacturingCategory = mtPfepManufacturingCategoryMapper.selectOne(mtPfepManufacturingCategory);
        if (mtPfepManufacturingCategory == null) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            dto.getKeyId(), TABLE_NAME, "【API:pfepMfgCatgAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, ATTR_TABLE_NAME, dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }
}
