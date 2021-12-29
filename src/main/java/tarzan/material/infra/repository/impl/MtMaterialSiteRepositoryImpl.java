package tarzan.material.infra.repository.impl;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.google.common.collect.Lists;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.util.MtIdHelper;
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

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.trans.MtMaterialSiteTransMapper;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.vo.MtMaterialSiteVO;
import tarzan.material.domain.vo.MtMaterialSiteVO3;
import tarzan.material.domain.vo.MtMaterialSiteVO4;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.infra.mapper.MtMaterialSiteMapper;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

/**
 * 物料站点分配 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@Component
public class MtMaterialSiteRepositoryImpl extends BaseRepositoryImpl<MtMaterialSite>
                implements MtMaterialSiteRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtModSiteRepository modSiteRepository;

    @Autowired
    private MtMaterialSiteMapper mtMaterialSiteMapper;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private ThreadPoolTaskExecutor poolExecutor;

    @Autowired
    private MtMaterialSiteTransMapper transMapper;

    @Override
    public String materialSiteLimitRelationGet(Long tenantId, MtMaterialSite dto) {
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:materialSiteLimitRelationGet】"));
        }

        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "siteId", "【API:materialSiteLimitRelationGet】"));
        }

        MtMaterialSite materialSite = new MtMaterialSite();
        materialSite.setTenantId(tenantId);
        materialSite.setMaterialId(dto.getMaterialId());
        materialSite.setSiteId(dto.getSiteId());
        materialSite = mtMaterialSiteMapper.selectOne(materialSite);
        if (materialSite == null) {
            return "";
        }
        return materialSite.getMaterialSiteId();
    }

    @Override
    public MtMaterialSite relationLimitMaterialSiteGet(Long tenantId, String materialSiteId) {
        if (StringUtils.isEmpty(materialSiteId)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialSiteId", "【API:relationLimitMaterialSiteGet】"));
        }
        MtMaterialSite materialSite = new MtMaterialSite();
        materialSite.setTenantId(tenantId);
        materialSite.setMaterialSiteId(materialSiteId);
        return mtMaterialSiteMapper.selectOne(materialSite);
    }

    @Override
    public List<String> siteLimitMaterialQuery(Long tenantId, MtMaterialSite dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "siteId", "【API:siteLimitMaterialQuery】"));
        }
        MtMaterialSite materialSite = new MtMaterialSite();
        materialSite.setSiteId(dto.getSiteId());
        materialSite.setMaterialSiteId(dto.getMaterialSiteId());
        materialSite.setEnableFlag("Y");

        // 要求物料是有效的
        return mtMaterialSiteMapper.selectEnableMaterial(tenantId, materialSite).stream()
                        .map(MtMaterialSite::getMaterialId).collect(toList());

    }

    @Override
    public List<String> materialLimitSiteQuery(Long tenantId, MtMaterialSite dto) {
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:materialLimitSiteQuery】"));
        }

        MtMaterialSite materialSite = new MtMaterialSite();
        materialSite.setMaterialId(dto.getMaterialId());
        if (StringUtils.isNotEmpty(dto.getMaterialSiteId())) {
            materialSite.setMaterialSiteId(dto.getMaterialSiteId());
        }
        materialSite.setEnableFlag("Y");

        List<String> keys = mtMaterialSiteMapper.select(materialSite).stream().map(MtMaterialSite::getSiteId)
                        .collect(toList());
        return modSiteRepository.siteBasicPropertyBatchGet(tenantId, keys).stream()
                        .filter(t -> "Y".equals(t.getEnableFlag())).map(MtModSite::getSiteId).collect(toList());
    }

    @Override
    public String materialSitePfepExistValidate(Long tenantId, MtMaterialSite dto) {
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:materialSitePfepExistValidate】"));
        }

        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "siteId", "【API:materialSitePfepExistValidate】"));
        }

        MtMaterialSite materialSite = new MtMaterialSite();
        materialSite.setSiteId(dto.getSiteId());
        materialSite.setMaterialId(dto.getMaterialId());
        MtMaterialSite materialSite1 = mtMaterialSiteMapper.selectPfepItem(tenantId, materialSite);
        if (materialSite1 == null) {
            return "N";
        } else {
            return "Y";
        }
    }

    @Override
    public List<MtMaterialSite> queryMaterialSiteByMaterialId(Long tenantId, List<String> materialIdList) {
        if (CollectionUtils.isEmpty(materialIdList)) {
            return Collections.emptyList();
        }

        return mtMaterialSiteMapper.queryMaterialSiteByMaterialId(tenantId, materialIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String materialSiteAssign(Long tenantId, MtMaterialSiteVO vo) {
        String materialId = vo.getMaterialId();
        String siteId = vo.getSiteId();
        String materialSiteId = vo.getMaterialSiteId();
        // 参数校验
        if (StringUtils.isNotEmpty(materialId)) {
            MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, materialId);
            if (mtMaterialVO == null || !"Y".equalsIgnoreCase(mtMaterialVO.getEnableFlag())) {
                throw new MtException("MT_MATERIAL_0055", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0055", "MATERIAL", "materialId", "【API:materialSiteAssign】"));
            }
        }
        if (StringUtils.isNotEmpty(siteId)) {
            MtModSite mtModSite = modSiteRepository.siteBasicPropertyGet(tenantId, siteId);
            if (mtModSite == null || !"Y".equalsIgnoreCase(mtModSite.getEnableFlag())) {
                throw new MtException("MT_MATERIAL_0055", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0055", "MATERIAL", "materialId", "【API:materialSiteAssign】"));
            }
        }
        if ((StringUtils.isEmpty(materialId) && StringUtils.isNotEmpty(siteId))
                        || StringUtils.isNotEmpty(materialId) && StringUtils.isEmpty(siteId)) {
            throw new MtException("MT_MATERIAL_0072", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0072", "MATERIAL", "materialId、siteId", "【API:materialSiteAssign】"));
        }
        if (StringUtils.isEmpty(materialId) && StringUtils.isEmpty(materialSiteId)) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            "materialId", "materialSiteId", "【API:materialSiteAssign】"));
        }
        if (StringUtils.isNotEmpty(materialSiteId)) {
            MtMaterialSite mtMaterialSite = new MtMaterialSite();
            mtMaterialSite.setTenantId(tenantId);
            mtMaterialSite.setMaterialSiteId(materialSiteId);
            if (StringUtils.isNotEmpty(materialId)) {
                mtMaterialSite.setMaterialId(materialId);
            }
            if (StringUtils.isNotEmpty(siteId)) {
                mtMaterialSite.setSiteId(siteId);
            }
            mtMaterialSite = mtMaterialSiteMapper.selectOne(mtMaterialSite);
            if (null == mtMaterialSite) {
                throw new MtException("MT_MATERIAL_0055", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0055", "MATERIAL", "materialSiteId", "【API:materialSiteAssign】"));
            }

            if (null != vo.getSourceIdentificationId()) {
                mtMaterialSite.setSourceIdentificationId(vo.getSourceIdentificationId());
            }
            if (null != vo.getEnableFlag()) {
                mtMaterialSite.setEnableFlag(vo.getEnableFlag());
            }
            if (null != vo.getMaterialId()) {
                mtMaterialSite.setMaterialId(vo.getMaterialId());
            }
            if (null != vo.getMaterialSiteId()) {
                mtMaterialSite.setMaterialSiteId(vo.getMaterialSiteId());
            }
            if (null != vo.getSiteId()) {
                mtMaterialSite.setSiteId(vo.getSiteId());
            }
            self().updateByPrimaryKey(mtMaterialSite);
            return materialSiteId;
        }
        if (StringUtils.isEmpty(materialSiteId) && StringUtils.isNotEmpty(materialId)
                        && StringUtils.isNotEmpty(siteId)) {
            MtMaterialSite mtMaterialSite = new MtMaterialSite();
            mtMaterialSite.setMaterialId(materialId);
            mtMaterialSite.setSiteId(siteId);
            mtMaterialSite.setTenantId(tenantId);
            mtMaterialSite = mtMaterialSiteMapper.selectOne(mtMaterialSite);
            if (null == mtMaterialSite) {
                // 新增 判断enableFlag是否有输入
                if (StringUtils.isEmpty(vo.getEnableFlag())) {
                    throw new MtException("MT_MATERIAL_0071", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_MATERIAL_0071", "MATERIAL", "enableFlag", "【API:materialSiteAssign】"));
                }

                mtMaterialSite = new MtMaterialSite();
                mtMaterialSite.setTenantId(tenantId);
                mtMaterialSite.setEnableFlag(vo.getEnableFlag());
                mtMaterialSite.setMaterialId(materialId);
                mtMaterialSite.setSiteId(siteId);
                if (null != vo.getSourceIdentificationId()) {
                    mtMaterialSite.setSourceIdentificationId(vo.getSourceIdentificationId());
                }
                self().insertSelective(mtMaterialSite);
                return mtMaterialSite.getMaterialSiteId();
            }

            if (null != vo.getSourceIdentificationId()) {
                mtMaterialSite.setSourceIdentificationId(vo.getSourceIdentificationId());
            }
            if (null != vo.getEnableFlag()) {
                mtMaterialSite.setEnableFlag(vo.getEnableFlag());
            }
            self().updateByPrimaryKey(mtMaterialSite);
            return mtMaterialSite.getMaterialSiteId();
        }
        return materialSiteId;
    }

    @Override
    public List<MtMaterialSite> queryByMaterialSiteId(Long tenantId, List<String> materialSiteIds) {
        return mtMaterialSiteMapper.queryByMaterialSiteId(tenantId, materialSiteIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialSiteAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        if (StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId", "【API:materialSiteAttrPropertyUpdate】"));
        }
        // 获取主表数据
        MtMaterialSite mtMaterialSite = mtMaterialSiteMapper.selectByPrimaryKey(dto.getKeyId());
        if (mtMaterialSite == null || StringUtils.isEmpty(mtMaterialSite.getMaterialSiteId())) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            "keyId:" + dto.getKeyId(), "mt_material_site",
                                            "【API:materialSiteAttrPropertyUpdate】"));
        }

        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_site_attr", dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }

    @Override
    public List<MtMaterialSiteVO4> materialSiteLimitRelationBatchGet(Long tenantId,
                                                                     List<MtMaterialSiteVO3> materialSiteIds) {
        final String apiName = "【API:materialSiteLimitRelationBatchGet】";
        if (CollectionUtils.isEmpty(materialSiteIds)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL",
                    "materialSiteIds", apiName));
        }
        Optional<MtMaterialSiteVO3> any =
                materialSiteIds.parallelStream().filter(t -> MtIdHelper.isIdNull(t.getMaterialId())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL",
                    "materialSiteIds:materialId", apiName));
        }
        any = materialSiteIds.parallelStream().filter(t -> MtIdHelper.isIdNull(t.getSiteId())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL",
                    "materialSiteIds:siteId", apiName));
        }
        // 改为以分治方式查询
        List<List<MtMaterialSiteVO3>> batchList =
                Lists.partition(materialSiteIds.stream().distinct().collect(toList()), 1000);

        List<Future<List<MtMaterialSite>>> futureList = new ArrayList<>();
        List<MtMaterialSite> result = new ArrayList<>();
        CustomUserDetails details = DetailsHelper.getUserDetails();
        for (List<MtMaterialSiteVO3> ever : batchList) {
            Condition.Builder builder = Condition.builder(MtMaterialSite.class).select(
                    MtMaterialSite.FIELD_MATERIAL_SITE_ID, MtMaterialSite.FIELD_MATERIAL_ID,
                    MtMaterialSite.FIELD_SITE_ID, MtMaterialSite.FIELD_SOURCE_IDENTIFICATION_ID,
                    MtMaterialSite.FIELD_ENABLE_FLAG);
            for (MtMaterialSiteVO3 input : ever) {
                Sqls sql = Sqls.custom().andEqualTo(MtMaterialSite.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtMaterialSite.FIELD_MATERIAL_ID, input.getMaterialId())
                        .andEqualTo(MtMaterialSite.FIELD_SITE_ID, input.getSiteId());
                builder.orWhere(sql);
            }
            Future<List<MtMaterialSite>> one = poolExecutor.submit(() -> {
                DetailsHelper.setCustomUserDetails(details);
                SecurityTokenHelper.close();
                return mtMaterialSiteMapper.selectByCondition(builder.build());
            });
            futureList.add(one);
            if (futureList.size() > 10) {
                for (Future<List<MtMaterialSite>> oneFuture : futureList) {
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

        for (Future<List<MtMaterialSite>> oneFuture : futureList) {
            try {
                result.addAll(oneFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                oneFuture.cancel(true);
                Thread.currentThread().interrupt();
            }
        }

        return transMapper.materialSiteToMaterialSiteVO4List(result);
    }
}
