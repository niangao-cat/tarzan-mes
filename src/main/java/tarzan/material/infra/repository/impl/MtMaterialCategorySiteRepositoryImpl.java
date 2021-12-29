package tarzan.material.infra.repository.impl;

import com.google.common.collect.Lists;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtIdHelper;
import io.tarzan.common.domain.util.StringHelper;
import io.tarzan.common.domain.vo.MtExtendVO10;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.entity.MtMaterialCategorySet;
import tarzan.material.domain.entity.MtMaterialCategorySite;
import tarzan.material.domain.repository.MtMaterialCategoryRepository;
import tarzan.material.domain.repository.MtMaterialCategorySetRepository;
import tarzan.material.domain.repository.MtMaterialCategorySiteRepository;
import tarzan.material.domain.trans.MtMaterialCategorySiteTransMapper;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO4;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO7;
import tarzan.material.infra.mapper.MtMaterialCategoryMapper;
import tarzan.material.infra.mapper.MtMaterialCategorySiteMapper;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.util.stream.Collectors.toList;

/**
 * 物料类别站点分配 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@Component
public class MtMaterialCategorySiteRepositoryImpl extends BaseRepositoryImpl<MtMaterialCategorySite>
                implements MtMaterialCategorySiteRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtMaterialCategoryRepository mtMaterialCategoryRepository;
    @Autowired
    private MtMaterialCategorySetRepository mtMaterialCategorySetRepository;

    @Autowired
    private MtMaterialCategoryMapper mtMaterialCategoryMapper;

    @Autowired
    private MtMaterialCategorySiteMapper mtMaterialCategorySiteMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private ThreadPoolTaskExecutor poolExecutor;

    @Autowired
    private MtMaterialCategorySiteTransMapper transMapper;



    @Override
    public String materialCategorySitePfepExistValidate(Long tenantId, MtMaterialCategorySite dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "siteId", "【API:materialCategorySitePfepExistValidate】"));
        }

        if (StringUtils.isEmpty(dto.getMaterialCategoryId())) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "materialCategoryId", "【API:materialCategorySitePfepExistValidate】"));
        }

        MtMaterialCategorySite categorySite = new MtMaterialCategorySite();
        categorySite.setSiteId(dto.getSiteId());
        categorySite.setMaterialCategoryId(dto.getMaterialCategoryId());
        return null != mtMaterialCategorySiteMapper.selectPfepItem(tenantId, categorySite) ? "Y" : "N";
    }

    @Override
    public String materialCategorySiteValidate(Long tenantId, MtMaterialCategorySite dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "siteId", "【API:materialCategorySiteValidate】"));
        }

        if (StringUtils.isEmpty(dto.getMaterialCategoryId())) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "materialCategoryId", "【API:materialCategorySiteValidate】"));
        }

        MtModSite site = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
        if (site == null) {
            return "N";
        }

        List<MtMaterialCategorySet> ls =
                        mtMaterialCategorySetRepository.defaultCategorySetGet(tenantId, site.getSiteType());
        if (ls.size() == 0) {
            return "N";
        }

        MtMaterialCategory mtMaterialCategory = new MtMaterialCategory();
        mtMaterialCategory.setTenantId(tenantId);
        mtMaterialCategory.setMaterialCategoryId(dto.getMaterialCategoryId());
        mtMaterialCategory.setMaterialCategorySetId(ls.get(0).getMaterialCategorySetId());
        mtMaterialCategory = mtMaterialCategoryMapper.selectOne(mtMaterialCategory);

        if (mtMaterialCategory == null) {
            return "N";
        } else {
            return "Y";
        }

    }

    @Override
    public String materialCategorySiteLimitRelationGet(Long tenantId, MtMaterialCategorySite dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "siteId", "【API:materialCategorySiteLimitRelationGet】"));
        }

        if (StringUtils.isEmpty(dto.getMaterialCategoryId())) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "materialCategoryId", "【API:materialCategorySiteLimitRelationGet】"));
        }

        MtMaterialCategorySite categorySite = new MtMaterialCategorySite();
        categorySite.setTenantId(tenantId);
        categorySite.setMaterialCategoryId(dto.getMaterialCategoryId());
        categorySite.setSiteId(dto.getSiteId());
        categorySite = mtMaterialCategorySiteMapper.selectOne(categorySite);
        if (categorySite != null) {
            return categorySite.getMaterialCategorySiteId();
        } else {
            return "";
        }
    }

    @Override
    public MtMaterialCategorySite relationLimitMaterialCategorySiteGet(Long tenantId, String materialCategorySiteId) {
        if (StringUtils.isEmpty(materialCategorySiteId)) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "materialCategorySiteId", "【API:relationLimitMaterialCategorySiteGet】"));
        }

        MtMaterialCategorySite categorySite = new MtMaterialCategorySite();
        categorySite.setTenantId(tenantId);
        categorySite.setMaterialCategorySiteId(materialCategorySiteId);
        categorySite = mtMaterialCategorySiteMapper.selectOne(categorySite);
        return categorySite;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String materialCategorySiteAssign(Long tenantId, MtMaterialCategorySiteVO4 materialCategorySiteVO) {
        if (StringUtils.isEmpty(materialCategorySiteVO.getMaterialCategoryId())
                        || StringUtils.isEmpty(materialCategorySiteVO.getSiteId())) {
            throw new MtException("MT_MATERIAL_0026", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0026", "MATERIAL", "【API:materialCategorySiteAssign】"));
        }

        MtMaterialCategory mtMaterialCategory = mtMaterialCategoryRepository.materialCategoryGet(tenantId,
                        materialCategorySiteVO.getMaterialCategoryId());
        if (null == mtMaterialCategory || !"Y".equals(mtMaterialCategory.getEnableFlag())) {
            throw new MtException("MT_MATERIAL_0055", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0055", "MATERIAL", "materialCategoryId", "【API:materialCategorySiteAssign】"));
        }
        if ("N".equals(mtMaterialCategorySetRepository.defaultCategorySetValidate(tenantId,
                        mtMaterialCategory.getMaterialCategorySetId()))) {
            throw new MtException("MT_MATERIAL_0039", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0039", "MATERIAL", "materialCategoryId", "【API:materialCategorySiteAssign】"));
        }

        MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, materialCategorySiteVO.getSiteId());
        if (null == mtModSite || !"Y".equals(mtModSite.getEnableFlag())) {
            throw new MtException("MT_MATERIAL_0055", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0055", "MATERIAL", "siteId", "【API:materialCategorySiteAssign】"));
        }

        MtMaterialCategorySite mtMaterialCategorySite = new MtMaterialCategorySite();
        mtMaterialCategorySite.setTenantId(tenantId);
        mtMaterialCategorySite.setMaterialCategoryId(materialCategorySiteVO.getMaterialCategoryId());
        mtMaterialCategorySite.setSiteId(materialCategorySiteVO.getSiteId());

        MtMaterialCategorySite originMaterialCategorySite =
                        mtMaterialCategorySiteMapper.selectOne(mtMaterialCategorySite);
        if (null != originMaterialCategorySite) {
            return originMaterialCategorySite.getMaterialCategorySiteId();
        }

        self().insertSelective(mtMaterialCategorySite);

        return mtMaterialCategorySite.getMaterialCategorySiteId();
    }

    @Override
    public List<MtMaterialCategorySite> selectByMaterialCategorySiteIds(Long tenantId,
                                                                        List<String> materialCategorySiteIds) {
        return mtMaterialCategorySiteMapper.selectByMaterialCategorySiteIds(tenantId, materialCategorySiteIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialCategorySiteAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10) {
        if (StringUtils.isEmpty(mtExtendVO10.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId ", "【API：materialCategorySiteAttrPropertyUpdate】"));
        }
        MtMaterialCategorySite categorySite = new MtMaterialCategorySite();
        categorySite.setTenantId(tenantId);
        categorySite.setMaterialCategorySiteId(mtExtendVO10.getKeyId());
        categorySite = mtMaterialCategorySiteMapper.selectOne(categorySite);
        if (null == categorySite) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            mtExtendVO10.getKeyId(), "mt_material_category_site",
                                            "【API:materialCategorySiteAttrPropertyUpdate】"));
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_category_site_attr",
                        mtExtendVO10.getKeyId(), mtExtendVO10.getEventId(), mtExtendVO10.getAttrs());

    }

    @Override
    public List<MtMaterialCategorySite> selectByMaterialCategoryIds(Long tenantId, List<String> materialCategoryIds) {
        if (CollectionUtils.isEmpty(materialCategoryIds)) {
            return Collections.emptyList();
        }
        String whereInValuesSql =
                        StringHelper.getWhereInValuesSql("t.MATERIAL_CATEGORY_SITE_ID", materialCategoryIds, 1000);
        return mtMaterialCategorySiteMapper.selectByMaterialCategoryIds(tenantId, whereInValuesSql);
    }

    @Override
    public List<MtMaterialCategorySiteVO7> materialCategorySiteLimitRelationBatchGet(Long tenantId,
                                                                                     List<MtMaterialCategorySiteVO4> materialCategorySiteIds) {
        final String apiName = "【API:materialCategorySiteLimitRelationBatchGet】";
        if (CollectionUtils.isEmpty(materialCategorySiteIds)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL",
                    "materialCategorySiteIds", apiName));
        }
        Optional<MtMaterialCategorySiteVO4> any = materialCategorySiteIds.parallelStream()
                .filter(t -> MtIdHelper.isIdNull(t.getMaterialCategoryId())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL",
                    "materialSiteIds:materialCategoryId", apiName));
        }
        any = materialCategorySiteIds.parallelStream().filter(t -> MtIdHelper.isIdNull(t.getSiteId())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL",
                    "materialSiteIds:siteId", apiName));
        }
        // 改为以分治方式查询

        List<List<MtMaterialCategorySiteVO4>> batchList =
                Lists.partition(materialCategorySiteIds.stream().distinct().collect(toList()), 1000);

        List<Future<List<MtMaterialCategorySite>>> futureList = new ArrayList<>();
        List<MtMaterialCategorySite> result = new ArrayList<>();
        CustomUserDetails details = DetailsHelper.getUserDetails();
        for (List<MtMaterialCategorySiteVO4> ever : batchList) {
            Condition.Builder builder = Condition.builder(MtMaterialCategorySite.class).select(
                    MtMaterialCategorySite.FIELD_MATERIAL_CATEGORY_SITE_ID,
                    MtMaterialCategorySite.FIELD_MATERIAL_CATEGORY_ID, MtMaterialCategorySite.FIELD_SITE_ID);
            for (MtMaterialCategorySiteVO4 input : ever) {
                Sqls sql = Sqls.custom().andEqualTo(MtMaterialCategorySite.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtMaterialCategorySite.FIELD_MATERIAL_CATEGORY_ID,
                                input.getMaterialCategoryId())
                        .andEqualTo(MtMaterialCategorySite.FIELD_SITE_ID, input.getSiteId());
                builder.orWhere(sql);
            }
            Future<List<MtMaterialCategorySite>> one = poolExecutor.submit(() -> {
                DetailsHelper.setCustomUserDetails(details);
                SecurityTokenHelper.close();
                return mtMaterialCategorySiteMapper.selectByCondition(builder.build());
            });
            futureList.add(one);
            if (futureList.size() > 10) {
                for (Future<List<MtMaterialCategorySite>> oneFuture : futureList) {
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

        for (Future<List<MtMaterialCategorySite>> oneFuture : futureList) {
            try {
                result.addAll(oneFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                oneFuture.cancel(true);
                Thread.currentThread().interrupt();
            }
        }

        return transMapper.materialCategorySiteToMaterialCategorySiteVO4List(result);
    }
}
