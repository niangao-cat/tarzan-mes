package tarzan.material.infra.repository.impl;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.StringHelper;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.entity.MtMaterialCategorySet;
import tarzan.material.domain.repository.MtMaterialCategoryRepository;
import tarzan.material.domain.repository.MtMaterialCategorySetRepository;
import tarzan.material.domain.vo.MtMaterialCategoryVO;
import tarzan.material.domain.vo.MtMaterialCategoryVO4;
import tarzan.material.domain.vo.MtMaterialCategoryVO5;
import tarzan.material.domain.vo.MtMaterialCategoryVO6;
import tarzan.material.infra.mapper.MtMaterialCategoryMapper;

/**
 * 物料类别 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@Component
public class MtMaterialCategoryRepositoryImpl extends BaseRepositoryImpl<MtMaterialCategory>
                implements MtMaterialCategoryRepository {

    private static final String Y_FLAG = "Y";
    private static final String TABLE_NAME = "mt_material_category";
    private static final String ATTR_TABLE_NAME = "mt_material_category_attr";

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtMaterialCategorySetRepository iMtMaterialCategorySetService;

    @Autowired
    private MtMaterialCategoryMapper mtMaterialCategoryMapper;

    @Override
    public MtMaterialCategory materialCategoryGet(Long tenantId, String materialCategoryId) {
        if (StringUtils.isEmpty(materialCategoryId)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialCategoryId", "【API:materialCategoryGet】"));
        }

        MtMaterialCategory category = new MtMaterialCategory();
        category.setTenantId(tenantId);
        category.setMaterialCategoryId(materialCategoryId);
        return mtMaterialCategoryMapper.selectOne(category);
    }

    @Override
    public String materialCategoryEnableValidate(Long tenantId, String materialCategoryId) {
        if (StringUtils.isEmpty(materialCategoryId)) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "materialCategoryId", "【API:materialCategoryEnableValidate】"));
        }
        MtMaterialCategory category = new MtMaterialCategory();
        category.setTenantId(tenantId);
        category.setMaterialCategoryId(materialCategoryId);
        category = mtMaterialCategoryMapper.selectOne(category);
        if (category == null) {
            throw new MtException("MT_MATERIAL_0055",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                            "materialCategory", "【API:materialCategoryEnableValidate】"));
        }
        MtMaterialCategorySet categorySet = iMtMaterialCategorySetService.materialCategorySetPropertyGet(tenantId,
                        category.getMaterialCategorySetId());
        if (categorySet == null) {
            throw new MtException("MT_MATERIAL_0055",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055", "MATERIAL",
                                            "materialCategory", "【API:materialCategoryEnableValidate】"));
        }
        if (Y_FLAG.equals(categorySet.getEnableFlag()) && Y_FLAG.equals(category.getEnableFlag())) {
            return "Y";
        } else {
            return "N";
        }
    }

    @Override
    public List<String> setLimitMaterialCategoryQuery(Long tenantId, String materialCategorySetId) {
        if (StringUtils.isEmpty(materialCategorySetId)) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "materialCategorySetId", "【API:setLimitMaterialCategoryQuery】"));
        }
        MtMaterialCategory category = new MtMaterialCategory();
        category.setTenantId(tenantId);
        category.setMaterialCategorySetId(materialCategorySetId);
        category.setEnableFlag("Y");
        List<MtMaterialCategory> list = mtMaterialCategoryMapper.select(category);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(MtMaterialCategory::getMaterialCategoryId).collect(toList());
    }

    @Override
    public List<String> codeLimitMaterialCategoryQuery(Long tenantId, MtMaterialCategory dto) {
        MtMaterialCategory category = new MtMaterialCategory();
        category.setTenantId(tenantId);
        category.setCategoryCode(dto.getCategoryCode());
        category.setDescription(dto.getDescription());
        category.setEnableFlag("Y");
        List<MtMaterialCategory> list = mtMaterialCategoryMapper.select(category);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(MtMaterialCategory::getMaterialCategoryId).collect(toList());
    }

    @Override
    public List<MtMaterialCategory> materialCategoryPropertyBatchGet(Long tenantId, List<String> materialCategoryIds) {
        if (CollectionUtils.isEmpty(materialCategoryIds)) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "materialCategoryId", "【API:materialCategoryPropertyBatchGet】"));
        }

        return this.mtMaterialCategoryMapper.selectByIdsCustom(tenantId, materialCategoryIds);
    }

    @Override
    public MtMaterialCategoryVO materialCategoryCodeGet(Long tenantId, String materialCategoryId) {
        // 1. 校验参数有效性
        if (StringUtils.isEmpty(materialCategoryId)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialCategoryId", "【API:materialCategoryCodeGet】"));
        }

        // 2. 根据传入id获取MaterialCategory数据
        MtMaterialCategory mtMaterialCategory = new MtMaterialCategory();
        mtMaterialCategory.setTenantId(tenantId);
        mtMaterialCategory.setMaterialCategoryId(materialCategoryId);
        mtMaterialCategory = mtMaterialCategoryMapper.selectOne(mtMaterialCategory);
        if (mtMaterialCategory != null && StringUtils.isNotEmpty(mtMaterialCategory.getMaterialCategoryId())) {
            MtMaterialCategoryVO materialCategoryVo1 = new MtMaterialCategoryVO();
            materialCategoryVo1.setCategoryCode(mtMaterialCategory.getCategoryCode());
            materialCategoryVo1.setDescription(mtMaterialCategory.getDescription());
            return materialCategoryVo1;
        }

        return null;
    }

    @Override
    public String materialCategorySetGet(Long tenantId, String materialCategoryId) {
        // 1. 校验参数有效性
        if (StringUtils.isEmpty(materialCategoryId)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialCategoryId", "【API:materialCategorySetGet】"));
        }

        // 2. 根据传入id获取MaterialCategory数据
        MtMaterialCategory mtMaterialCategory = new MtMaterialCategory();
        mtMaterialCategory.setTenantId(tenantId);
        mtMaterialCategory.setMaterialCategoryId(materialCategoryId);
        mtMaterialCategory = mtMaterialCategoryMapper.selectOne(mtMaterialCategory);

        return mtMaterialCategory == null ? null : mtMaterialCategory.getMaterialCategorySetId();
    }

    @Override
    public List<MtMaterialCategory> queryMaterialCategoryBySetId(Long tenantId,
                                                                 List<String> materialCategorySetIdList) {
        if (CollectionUtils.isEmpty(materialCategorySetIdList)) {
            return Collections.emptyList();
        }
        String whereInValuesSql =
                        StringHelper.getWhereInValuesSql("MATERIAL_CATEGORY_SET_ID", materialCategorySetIdList, 1000);
        return mtMaterialCategoryMapper.queryMaterialCategoryBySetId(tenantId, whereInValuesSql);
    }

    @Override
    public List<MtMaterialCategory> queryMaterialCategoryByCode(Long tenantId, List<String> materialCategoryCodeList) {
        if (CollectionUtils.isEmpty(materialCategoryCodeList)) {
            return Collections.emptyList();
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("CATEGORY_CODE", materialCategoryCodeList, 1000);
        return mtMaterialCategoryMapper.queryMaterialCategoryByCode(tenantId, whereInValuesSql);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String materialCategoryUpdate(Long tenantId, MtMaterialCategoryVO4 vo) {
        // 参数校验
        if (StringUtils.isNotEmpty(vo.getMaterialCategorySetId())) {
            MtMaterialCategory mtMaterialCategory = new MtMaterialCategory();
            mtMaterialCategory.setTenantId(tenantId);
            mtMaterialCategory.setMaterialCategoryId(vo.getMaterialCategoryId());
            mtMaterialCategory.setEnableFlag("Y");
            MtMaterialCategory mtMaterialCategoryOne = mtMaterialCategoryMapper.selectOne(mtMaterialCategory);
            if (null == mtMaterialCategoryOne) {
                throw new MtException("MT_MATERIAL_0055", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0055", "MATERIAL", "materialCategoryId", "【API:materialCategoryUpdate】"));
            }
        }
        if (StringUtils.isEmpty(vo.getMaterialCategoryId()) && StringUtils.isEmpty(vo.getCategoryCode())) {
            throw new MtException("MT_MATERIAL_0065",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0065", "MATERIAL",
                                            "materialCategoryId、categoryCode", "【API:materialCategoryUpdate】"));
        }
        if (StringUtils.isNotEmpty(vo.getMaterialCategoryId())) {
            MtMaterialCategory mtMaterialCategory = new MtMaterialCategory();
            mtMaterialCategory.setTenantId(tenantId);
            mtMaterialCategory.setMaterialCategoryId(vo.getMaterialCategoryId());
            mtMaterialCategory.setCategoryCode(vo.getCategoryCode());
            mtMaterialCategory = mtMaterialCategoryMapper.selectOne(mtMaterialCategory);
            if (mtMaterialCategory == null) {
                throw new MtException("MT_MATERIAL_0055", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0055", "MATERIAL", "materialCategoryId", "【API:materialCategoryUpdate】"));
            }
            // 有数据，更新操作
            mtMaterialCategory.setTenantId(tenantId);
            if (null != vo.getDescription()) {
                mtMaterialCategory.setDescription(vo.getDescription());
            }
            if (null != vo.getMaterialCategorySetId()) {
                mtMaterialCategory.setMaterialCategorySetId(vo.getMaterialCategorySetId());
            }
            if (StringUtils.isNotEmpty(vo.getEnableFlag())) {
                mtMaterialCategory.setEnableFlag(vo.getEnableFlag());
            }
            if (null != vo.getCategoryCode()) {
                mtMaterialCategory.setCategoryCode(vo.getCategoryCode());
            }

            self().updateByPrimaryKey(mtMaterialCategory);
            return mtMaterialCategory.getMaterialCategoryId();
        }
        MtMaterialCategory mtMaterialCategory = new MtMaterialCategory();
        if (StringUtils.isEmpty(vo.getMaterialCategoryId()) && StringUtils.isNotEmpty(vo.getCategoryCode())) {
            mtMaterialCategory.setTenantId(tenantId);
            mtMaterialCategory.setCategoryCode(vo.getCategoryCode());
            mtMaterialCategory = mtMaterialCategoryMapper.selectOne(mtMaterialCategory);
            if (null == mtMaterialCategory) {
                // 判断categoryCode(上面if判断了)、materialCategorySetId、enableFlag是否均有输入
                if (StringUtils.isEmpty(vo.getMaterialCategorySetId()) || StringUtils.isEmpty(vo.getEnableFlag())) {
                    throw new MtException("MT_MATERIAL_0071",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0071",
                                                    "MATERIAL", "categoryCode（materialCategorySetId、enableFlag）",
                                                    "【API:materialCategoryUpdate】"));
                }
                // 新增模式
                MtMaterialCategory mtMaterialCategoryNew = new MtMaterialCategory();
                mtMaterialCategoryNew.setTenantId(tenantId);
                mtMaterialCategoryNew.setCategoryCode(vo.getCategoryCode());
                mtMaterialCategoryNew.setDescription(vo.getDescription());
                mtMaterialCategoryNew.setEnableFlag(vo.getEnableFlag());
                mtMaterialCategoryNew.setMaterialCategorySetId(vo.getMaterialCategorySetId());
                self().insertSelective(mtMaterialCategoryNew);
                return mtMaterialCategoryNew.getMaterialCategoryId();
            }
        }
        // 有数据，更新操作 ,其它字段已经在if里面判断了不为空
        if (null != vo.getDescription()) {
            mtMaterialCategory.setDescription(vo.getDescription());
        }
        if (null != vo.getMaterialCategorySetId()) {
            mtMaterialCategory.setMaterialCategorySetId(vo.getMaterialCategorySetId());
        }
        if (StringUtils.isNotEmpty(vo.getEnableFlag())) {
            mtMaterialCategory.setEnableFlag(vo.getEnableFlag());
        }

        self().updateByPrimaryKey(mtMaterialCategory);
        return mtMaterialCategory.getMaterialCategoryId();
    }

    @Override
    public List<MtMaterialCategoryVO6> propertyLimitMaterialCategoryPropertyQuery(Long tenantId,
                                                                                  MtMaterialCategoryVO5 dto) {
        return mtMaterialCategoryMapper.selectCondition(tenantId, dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialCategoryAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId", "【API:materialCategoryAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtMaterialCategory mtMaterialCategory = new MtMaterialCategory();
        mtMaterialCategory.setTenantId(tenantId);
        mtMaterialCategory.setMaterialCategoryId(dto.getKeyId());
        mtMaterialCategory = mtMaterialCategoryMapper.selectOne(mtMaterialCategory);
        if (mtMaterialCategory == null) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            dto.getKeyId(), TABLE_NAME, "【API:materialCategoryAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, ATTR_TABLE_NAME, dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }

    @Override
    public List<MtMaterialCategoryVO6> materialCategorySetBatchGet(Long tenantId, List<String> materialCategoryIdList) {
        final String apiName = "【API:materialCategorySetBatchGet】";
        if (CollectionUtils.isEmpty(materialCategoryIdList)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL",
                    "materialCategoryIdList", apiName));
        }

        List<MtMaterialCategory> categoryList = mtMaterialCategoryMapper.selectByCondition(Condition
                .builder(MtMaterialCategory.class)
                .select(MtMaterialCategory.FIELD_MATERIAL_CATEGORY_ID,
                        MtMaterialCategory.FIELD_MATERIAL_CATEGORY_SET_ID)
                .andWhere(Sqls.custom().andEqualTo(MtMaterialCategory.FIELD_TENANT_ID, tenantId)
                        )
                .andWhere(Sqls.custom()
                        .andIn(MtMaterialCategory.FIELD_MATERIAL_CATEGORY_ID, materialCategoryIdList)
                        )
                .build());

        List<MtMaterialCategoryVO6> resultList = new ArrayList<>();
        MtMaterialCategoryVO6 result;
        for (MtMaterialCategory category : categoryList) {
            result = new MtMaterialCategoryVO6();
            result.setMaterialCategoryId(category.getMaterialCategoryId());
            result.setMaterialCategorySetId(category.getMaterialCategorySetId());
            resultList.add(result);
        }

        return resultList;
    }
}
