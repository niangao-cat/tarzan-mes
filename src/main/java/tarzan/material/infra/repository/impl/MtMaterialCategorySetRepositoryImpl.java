package tarzan.material.infra.repository.impl;

import java.util.*;

import io.tarzan.common.domain.util.MtBaseConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.StringHelper;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtMaterialCategorySet;
import tarzan.material.domain.repository.MtMaterialCategorySetRepository;
import tarzan.material.domain.vo.MtMaterialCategorySetVO;
import tarzan.material.domain.vo.MtMaterialCategorySetVO2;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO5;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO6;
import tarzan.material.infra.mapper.MtMaterialCategorySetMapper;

/**
 * 物料类别集 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@Component
public class MtMaterialCategorySetRepositoryImpl extends BaseRepositoryImpl<MtMaterialCategorySet>
                implements MtMaterialCategorySetRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtMaterialCategorySetMapper mtMaterialCategorySetMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;


    @Override
    public List<MtMaterialCategorySet> defaultCategorySetGet(Long tenantId, String type) {
        if (StringUtils.isEmpty(type)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "defaultType", "【API:defaultCategorySetGet】"));
        }

        if (!"SCHEDULE".equals(type) && !"MANUFACTURING".equals(type) && !"PURCHASE".equals(type)) {
            throw new MtException("MT_MATERIAL_0060", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0060", "MATERIAL", "【API:defaultCategorySetGet】"));
        }

        MtMaterialCategorySet materialCategorySet = new MtMaterialCategorySet();
        materialCategorySet.setTenantId(tenantId);
        switch (type) {
            case "SCHEDULE":
                materialCategorySet.setDefaultScheduleFlag("Y");
                break;
            case "MANUFACTURING":
                materialCategorySet.setDefaultManufacturingFlag("Y");
                break;
            default:
                materialCategorySet.setDefaultPurchaseFlag("Y");
                break;
        }
        return mtMaterialCategorySetMapper.select(materialCategorySet);
    }

    @Override
    public MtMaterialCategorySet materialCategorySetPropertyGet(Long tenantId, String materialCategorySetId) {

        if (StringUtils.isEmpty(materialCategorySetId)) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "materialCategorySetId", "【API:materialCategorySetPropertyGet】"));
        }

        MtMaterialCategorySet materialCategorySet = new MtMaterialCategorySet();
        materialCategorySet.setTenantId(tenantId);
        materialCategorySet.setMaterialCategorySetId(materialCategorySetId);
        return mtMaterialCategorySetMapper.selectOne(materialCategorySet);
    }

    @Override
    public String defaultCategorySetValidate(Long tenantId, String materialCategorySetId) {

        if (StringUtils.isEmpty(materialCategorySetId)) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "materialCategorySetId", "【API:defaultCategorySetValidate】"));
        }

        MtMaterialCategorySet materialCategorySet = new MtMaterialCategorySet();
        materialCategorySet.setTenantId(tenantId);
        materialCategorySet.setMaterialCategorySetId(materialCategorySetId);
        materialCategorySet = mtMaterialCategorySetMapper.selectOne(materialCategorySet);

        if (materialCategorySet == null) {
            return "N";
        }

        if ("Y".equals(materialCategorySet.getDefaultManufacturingFlag())
                        || "Y".equals(materialCategorySet.getDefaultPurchaseFlag())
                        || "Y".equals(materialCategorySet.getDefaultScheduleFlag())) {
            return "Y";
        } else {
            return "N";
        }
    }

    @Override
    public List<MtMaterialCategorySet> materialCategorySetPropertyBatchGet(Long tenantId,
                                                                           List<String> materialCategorySetIds) {
        if (CollectionUtils.isEmpty(materialCategorySetIds)) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "materialCategorySetId", "【API:materialCategorySetPropertyBatchGet】"));
        }
        return this.mtMaterialCategorySetMapper.selectByIdsCustom(tenantId, materialCategorySetIds);
    }

    @Override
    public List<MtMaterialCategorySet> queryMaterialCategorySetByCode(Long tenantId, List<String> categorySetCodeList) {
        if (CollectionUtils.isEmpty(categorySetCodeList)) {
            return Collections.emptyList();
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("CATEGORY_SET_CODE", categorySetCodeList, 1000);
        return mtMaterialCategorySetMapper.queryMaterialCategorySetByCode(tenantId, whereInValuesSql);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String materialCategorySetUpdate(Long tenantId, MtMaterialCategorySetVO dto) {

        if (StringUtils.isEmpty(dto.getMaterialCategorySetId()) && StringUtils.isEmpty(dto.getCategorySetCode())) {
            throw new MtException("MT_MATERIAL_0065",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0065", "MATERIAL",
                                            "materialCategorySetId、categorySetCode",
                                            "【API：materialCategorySetUpdate】"));
        }

        // 第一步 a)根据传入参数判断新增或更新模式
        MtMaterialCategorySet mtMaterialCategorySet = new MtMaterialCategorySet();
        List<MtMaterialCategorySet> mtMaterialCategorySets;
        // 新增更新
        mtMaterialCategorySet.setTenantId(tenantId);
        if (StringUtils.isNotEmpty(dto.getMaterialCategorySetId())) {
            mtMaterialCategorySet.setCategorySetCode(dto.getCategorySetCode());
            mtMaterialCategorySet.setMaterialCategorySetId(dto.getMaterialCategorySetId());
            mtMaterialCategorySet = mtMaterialCategorySetMapper.selectByPrimaryKey(mtMaterialCategorySet);

            if (null == mtMaterialCategorySet) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                                "materialCategorySetId", "【API：materialCategorySetUpdate】"));
            }
            // b)若获取到数据，判定为更新模式，继续第二步
            if ("Y".equalsIgnoreCase(dto.getDefaultScheduleFlag())) {
                MtMaterialCategorySet categorySet = new MtMaterialCategorySet();
                categorySet.setTenantId(tenantId);
                categorySet.setDefaultScheduleFlag("Y");
                MtMaterialCategorySet materialSet = mtMaterialCategorySetMapper.selectOne(categorySet);
                if (materialSet != null) {
                    materialSet.setDefaultScheduleFlag("N");
                    self().updateByPrimaryKeySelective(materialSet);
                }

                mtMaterialCategorySet.setDefaultScheduleFlag("Y");
            }

            if ("Y".equalsIgnoreCase(dto.getDefaultManufacturingFlag())) {
                MtMaterialCategorySet categorySet = new MtMaterialCategorySet();
                categorySet.setTenantId(tenantId);
                categorySet.setDefaultManufacturingFlag("Y");
                MtMaterialCategorySet materialSet = mtMaterialCategorySetMapper.selectOne(categorySet);
                if (materialSet != null) {
                    materialSet.setDefaultManufacturingFlag("N");
                    self().updateByPrimaryKeySelective(materialSet);
                }
                mtMaterialCategorySet.setDefaultManufacturingFlag("Y");
            }

            if ("Y".equalsIgnoreCase(dto.getDefaultPurchaseFlag())) {
                MtMaterialCategorySet categorySet = new MtMaterialCategorySet();
                categorySet.setTenantId(tenantId);
                categorySet.setDefaultPurchaseFlag("Y");
                MtMaterialCategorySet materialSet = mtMaterialCategorySetMapper.selectOne(categorySet);
                if (materialSet != null) {
                    materialSet.setDefaultPurchaseFlag("N");
                    self().updateByPrimaryKeySelective(materialSet);
                }

                mtMaterialCategorySet.setDefaultPurchaseFlag("Y");
            }
            if ("".equals(dto.getDefaultScheduleFlag())) {
                mtMaterialCategorySet.setDefaultScheduleFlag(dto.getDefaultScheduleFlag());
            }
            if ("".equals(dto.getDefaultManufacturingFlag())) {
                mtMaterialCategorySet.setDefaultManufacturingFlag(dto.getDefaultManufacturingFlag());
            }
            if ("".equals(dto.getDefaultPurchaseFlag())) {
                mtMaterialCategorySet.setDefaultPurchaseFlag(dto.getDefaultPurchaseFlag());
            }
            if (null != dto.getCategorySetCode()) {
                mtMaterialCategorySet.setCategorySetCode(dto.getCategorySetCode());
            }
            if (null != dto.getDescription()) {
                mtMaterialCategorySet.setDescription(dto.getDescription());
            }
            if (StringUtils.isNotEmpty(dto.getEnableFlag())) {
                mtMaterialCategorySet.setEnableFlag(dto.getEnableFlag());
            }
            self().updateByPrimaryKey(mtMaterialCategorySet);
            return mtMaterialCategorySet.getMaterialCategorySetId();
        }
        if (StringUtils.isEmpty(dto.getMaterialCategorySetId()) && StringUtils.isNotEmpty(dto.getCategorySetCode())) {
            mtMaterialCategorySet.setTenantId(tenantId);
            mtMaterialCategorySet.setCategorySetCode(dto.getCategorySetCode());
            mtMaterialCategorySets = mtMaterialCategorySetMapper.select(mtMaterialCategorySet);
            if (CollectionUtils.isNotEmpty(mtMaterialCategorySets)) {
                mtMaterialCategorySet = mtMaterialCategorySets.get(0);
            }

            if ("Y".equalsIgnoreCase(dto.getDefaultScheduleFlag())) {
                MtMaterialCategorySet categorySet = new MtMaterialCategorySet();
                categorySet.setTenantId(tenantId);
                categorySet.setDefaultScheduleFlag("Y");
                MtMaterialCategorySet materialSet = mtMaterialCategorySetMapper.selectOne(categorySet);
                if (materialSet != null) {
                    materialSet.setDefaultScheduleFlag("N");
                    self().updateByPrimaryKeySelective(materialSet);
                }
                mtMaterialCategorySet.setDefaultScheduleFlag("Y");
            }

            if ("Y".equalsIgnoreCase(dto.getDefaultManufacturingFlag())) {
                MtMaterialCategorySet categorySet = new MtMaterialCategorySet();
                categorySet.setTenantId(tenantId);
                categorySet.setDefaultManufacturingFlag("Y");
                MtMaterialCategorySet materialSet = mtMaterialCategorySetMapper.selectOne(categorySet);
                if (materialSet != null) {
                    materialSet.setDefaultManufacturingFlag("N");
                    self().updateByPrimaryKeySelective(materialSet);
                }
                mtMaterialCategorySet.setDefaultManufacturingFlag("Y");
            }

            if ("Y".equalsIgnoreCase(dto.getDefaultPurchaseFlag())) {
                MtMaterialCategorySet categorySet = new MtMaterialCategorySet();
                categorySet.setTenantId(tenantId);
                categorySet.setDefaultPurchaseFlag("Y");
                MtMaterialCategorySet materialSet = mtMaterialCategorySetMapper.selectOne(categorySet);
                if (materialSet != null) {
                    materialSet.setDefaultPurchaseFlag("N");
                    self().updateByPrimaryKeySelective(materialSet);
                }
                mtMaterialCategorySet.setDefaultPurchaseFlag("Y");
            }
            if (null != dto.getCategorySetCode()) {
                mtMaterialCategorySet.setCategorySetCode(dto.getCategorySetCode());
            }
            if (null != dto.getDescription()) {
                mtMaterialCategorySet.setDescription(dto.getDescription());
            }
            if (StringUtils.isNotEmpty(dto.getEnableFlag())) {
                mtMaterialCategorySet.setEnableFlag(dto.getEnableFlag());
            }

            if (CollectionUtils.isEmpty(mtMaterialCategorySets)) {
                if (StringUtils.isEmpty(dto.getEnableFlag()) && StringUtils.isEmpty(dto.getCategorySetCode())) {
                    throw new MtException("MT_MATERIAL_0071",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0071",
                                                    "MATERIAL", "categorySetCode（enableFlag）",
                                                    "【API：materialCategorySetUpdate】"));
                }

                if (StringUtils.isEmpty(dto.getDefaultScheduleFlag())) {
                    mtMaterialCategorySet.setDefaultScheduleFlag("N");
                }
                if (StringUtils.isEmpty(dto.getDefaultManufacturingFlag())) {
                    mtMaterialCategorySet.setDefaultManufacturingFlag("N");
                }
                if (StringUtils.isEmpty(dto.getDefaultPurchaseFlag())) {
                    mtMaterialCategorySet.setDefaultPurchaseFlag("N");
                }
                // 新增，进行第三步
                mtMaterialCategorySet.setMaterialCategorySetId(dto.getMaterialCategorySetId());
                self().insertSelective(mtMaterialCategorySet);
                return mtMaterialCategorySet.getMaterialCategorySetId();
            }

        }
        if ("".equals(dto.getDefaultScheduleFlag().trim())) {
            mtMaterialCategorySet.setDefaultScheduleFlag(dto.getDefaultScheduleFlag());
        }
        if ("".equals(dto.getDefaultManufacturingFlag().trim())) {
            mtMaterialCategorySet.setDefaultManufacturingFlag(dto.getDefaultManufacturingFlag());
        }
        if ("".equals(dto.getDefaultPurchaseFlag().trim())) {
            mtMaterialCategorySet.setDefaultPurchaseFlag(dto.getDefaultPurchaseFlag());
        }
        // 更新，进行第二步
        self().updateByPrimaryKey(mtMaterialCategorySet);
        return mtMaterialCategorySet.getMaterialCategorySetId();
    }

    @Override
    public List<MtMaterialCategorySiteVO6> propertyLimitMaterialCategorySetPropertyQuery(Long tenantId,
                                                                                         MtMaterialCategorySiteVO5 dto) {
        return mtMaterialCategorySetMapper.selectCondition(tenantId, dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialCategorySetAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10) {
        if (StringUtils.isEmpty(mtExtendVO10.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId ", "【API：materialCategorySetAttrPropertyUpdate】"));
        }
        MtMaterialCategorySet categorySet = new MtMaterialCategorySet();
        categorySet.setTenantId(tenantId);
        categorySet.setMaterialCategorySetId(mtExtendVO10.getKeyId());
        categorySet = mtMaterialCategorySetMapper.selectOne(categorySet);
        if (null == categorySet) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            mtExtendVO10.getKeyId(), "mt_material_category_set",
                                            "【API:materialCategorySetAttrPropertyUpdate】"));
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_category_set_attr",
                        mtExtendVO10.getKeyId(), mtExtendVO10.getEventId(), mtExtendVO10.getAttrs());

    }

    @Override
    public List<MtMaterialCategorySetVO2> defaultCategorySetBatchGet(Long tenantId, List<String> defaultTypes) {
        final String apiName = "【API:defaultCategorySetBatchGet】";
        if (CollectionUtils.isEmpty(defaultTypes)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL",
                    "defaultTypeList", apiName));
        }

        boolean flag = false;
        Sqls sqls = Sqls.custom();
        if (defaultTypes.contains("SCHEDULE")) {
            sqls.andEqualTo(MtMaterialCategorySet.FIELD_DEFAULT_SCHEDULE_FLAG, MtBaseConstants.YES);
            flag = true;
        }
        if (defaultTypes.contains("MANUFACTURING")) {
            if (flag) {
                sqls.orEqualTo(MtMaterialCategorySet.FIELD_DEFAULT_MANUFACTURING_FLAG, MtBaseConstants.YES);
            } else {
                sqls.andEqualTo(MtMaterialCategorySet.FIELD_DEFAULT_MANUFACTURING_FLAG, MtBaseConstants.YES);
            }
            flag = true;
        }
        if (defaultTypes.contains("PURCHASE")) {
            if (flag) {
                sqls.orEqualTo(MtMaterialCategorySet.FIELD_DEFAULT_PURCHASE_FLAG, MtBaseConstants.YES);
            } else {
                sqls.andEqualTo(MtMaterialCategorySet.FIELD_DEFAULT_PURCHASE_FLAG, MtBaseConstants.YES);
            }
            flag = true;
        }

        if (!flag) {
            return Collections.emptyList();
        }

        SecurityTokenHelper.close();
        List<MtMaterialCategorySet> materialCategorySetList = mtMaterialCategorySetMapper.selectByCondition(Condition
                .builder(MtMaterialCategorySet.class).andWhere(Sqls.custom()
                        .andEqualTo(MtMaterialCategorySet.FIELD_TENANT_ID, tenantId))
                .andWhere(sqls).build());
        if (CollectionUtils.isEmpty(materialCategorySetList)) {
            return Collections.emptyList();
        }

        Map<String, MtMaterialCategorySetVO2> map = new HashMap<>(3);
        if (defaultTypes.contains("SCHEDULE")) {
            MtMaterialCategorySetVO2 vo = new MtMaterialCategorySetVO2("SCHEDULE");
            Optional<MtMaterialCategorySet> scheduleCategorySet = materialCategorySetList.stream()
                    .filter(set -> MtBaseConstants.YES.equals(set.getDefaultScheduleFlag())).findAny();
            if (scheduleCategorySet.isPresent()) {
                vo.setMaterialCategorySetId(scheduleCategorySet.get().getMaterialCategorySetId());
                vo.setCategorySetCode(scheduleCategorySet.get().getCategorySetCode());
                vo.setDescription(scheduleCategorySet.get().getDescription());
            }
            map.put("SCHEDULE", vo);
        }
        if (defaultTypes.contains("MANUFACTURING")) {
            MtMaterialCategorySetVO2 vo =
                    new MtMaterialCategorySetVO2("MANUFACTURING");
            Optional<MtMaterialCategorySet> manufacturingCategorySet = materialCategorySetList.stream()
                    .filter(set -> MtBaseConstants.YES.equals(set.getDefaultManufacturingFlag())).findAny();
            if (manufacturingCategorySet.isPresent()) {
                vo.setMaterialCategorySetId(manufacturingCategorySet.get().getMaterialCategorySetId());
                vo.setCategorySetCode(manufacturingCategorySet.get().getCategorySetCode());
                vo.setDescription(manufacturingCategorySet.get().getDescription());
            }
            map.put("MANUFACTURING", vo);
        }
        if (defaultTypes.contains("PURCHASE")) {
            MtMaterialCategorySetVO2 vo = new MtMaterialCategorySetVO2("PURCHASE");
            Optional<MtMaterialCategorySet> purchaseCategorySet = materialCategorySetList.stream()
                    .filter(set -> MtBaseConstants.YES.equals(set.getDefaultPurchaseFlag())).findAny();
            if (purchaseCategorySet.isPresent()) {
                vo.setMaterialCategorySetId(purchaseCategorySet.get().getMaterialCategorySetId());
                vo.setCategorySetCode(purchaseCategorySet.get().getCategorySetCode());
                vo.setDescription(purchaseCategorySet.get().getDescription());
            }
            map.put("PURCHASE", vo);
        }

        return new ArrayList<>(map.values());
    }
}
