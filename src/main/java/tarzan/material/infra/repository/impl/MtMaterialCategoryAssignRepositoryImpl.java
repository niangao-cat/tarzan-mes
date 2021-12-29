package tarzan.material.infra.repository.impl;

import static java.util.stream.Collectors.toList;

import java.util.*;
import java.util.stream.Collectors;

import io.tarzan.common.domain.util.MtIdHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.StringHelper;
import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.entity.MtMaterialCategoryAssign;
import tarzan.material.domain.entity.MtMaterialCategorySet;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.repository.MtMaterialCategoryAssignRepository;
import tarzan.material.domain.repository.MtMaterialCategoryRepository;
import tarzan.material.domain.repository.MtMaterialCategorySetRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.vo.*;
import tarzan.material.infra.mapper.MtMaterialCategoryAssignMapper;
import tarzan.material.infra.mapper.MtMaterialCategoryMapper;

/**
 * 物料类别分配 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@Component
public class MtMaterialCategoryAssignRepositoryImpl extends BaseRepositoryImpl<MtMaterialCategoryAssign>
                implements MtMaterialCategoryAssignRepository {

    private static final String Y_FLAG = "Y";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepo;

    @Autowired
    private MtMaterialCategorySetRepository mtMaterialCategorySetRepo;

    @Autowired
    private MtMaterialCategoryMapper mtMaterialCategoryMapper;

    @Autowired
    private MtMaterialCategoryAssignMapper mtMaterialCategoryAssignMapper;

    @Autowired
    private MtMaterialCategorySetRepository mtMaterialCategorySetRepository;

    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;

    @Autowired
    private MtMaterialCategoryRepository mtMaterialCategoryRepository;

    @Override
    public String defaultSetMaterialAssignCategoryGet(Long tenantId, MtMaterialCategoryAssignVO dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "siteId", "【API:defaultSetMaterialAssignCategoryGet】"));
        }

        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:defaultSetMaterialAssignCategoryGet】"));
        }

        if (StringUtils.isEmpty(dto.getDefaultType())) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "defaultType", "【API:defaultSetMaterialAssignCategoryGet】"));
        }

        List<MtMaterialCategorySet> ls =
                        mtMaterialCategorySetRepo.defaultCategorySetGet(tenantId, dto.getDefaultType());
        if (CollectionUtils.isEmpty(ls)) {
            return "";
        }

        dto.setMaterialCategorySetId(ls.get(0).getMaterialCategorySetId());
        return self().setLimitMaterialAssignCategoryGet(tenantId, dto);
    }

    @Override
    public String setLimitMaterialAssignCategoryGet(Long tenantId, MtMaterialCategoryAssignVO condition) {
        MtMaterialCategoryAssignVO assignVO = new MtMaterialCategoryAssignVO();
        assignVO.setMaterialId(condition.getMaterialId());
        assignVO.setSiteId(condition.getSiteId());
        assignVO.setMaterialCategorySetId(condition.getMaterialCategorySetId());

        if (StringUtils.isEmpty(assignVO.getSiteId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "siteId", "【API:setLimitMaterialAssignCategoryGet】"));
        }

        if (StringUtils.isEmpty(assignVO.getMaterialId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:setLimitMaterialAssignCategoryGet】"));
        }

        if (StringUtils.isEmpty(assignVO.getMaterialCategorySetId())) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "materialCategorySetId", "【API:setLimitMaterialAssignCategoryGet】"));
        }

        MtMaterialSite materialSite = new MtMaterialSite();
        materialSite.setMaterialId(assignVO.getMaterialId());
        materialSite.setSiteId(assignVO.getSiteId());
        String materialSiteId = mtMaterialSiteRepo.materialSiteLimitRelationGet(tenantId, materialSite);
        if (StringUtils.isEmpty(materialSiteId)) {
            return null;
        }

        assignVO.setMaterialSiteId(materialSiteId);
        List<MtMaterialCategoryAssign> ls = mtMaterialCategoryAssignMapper.selectData(tenantId, assignVO);

        if (CollectionUtils.isEmpty(ls)) {
            return "";
        } else {
            return ls.get(0).getMaterialCategoryId();
        }

    }

    @Override
    public List<String> categorySiteLimitMaterialQuery(Long tenantId, MtMaterialCategoryAssignVO dto) {

        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "siteId", "【API:categorySiteLimitMaterialQuery】"));
        }

        if (StringUtils.isEmpty(dto.getMaterialCategoryId())) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "materialCategoryId", "【API:categorySiteLimitMaterialQuery】"));
        }

        List<MtMaterialCategoryAssignVO> ls = mtMaterialCategoryAssignMapper.selectMaterialBySiteId(tenantId, dto);

        return ls.stream().map(MtMaterialCategoryAssignVO::getMaterialId).collect(toList());
    }

    @Override
    public String materialCategoryAssignValidate(Long tenantId, MtMaterialCategoryAssignVO dto) {

        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "siteId", "【API:materialCategoryAssignValidate】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:materialCategoryAssignValidate】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialCategoryId())) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "materialCategoryId", "【API:materialCategoryAssignValidate】"));
        }

        if (mtMaterialCategoryAssignMapper.selectData(tenantId, dto).size() > 0) {
            return "Y";
        } else {
            return "N";
        }
    }

    @Override
    public String materialCategoryAssignUniqueValidate(Long tenantId, MtMaterialCategoryAssignVO condition) {
        MtMaterialCategoryAssignVO assignVO = new MtMaterialCategoryAssignVO();
        assignVO.setMaterialCategoryId(condition.getMaterialCategoryId());
        assignVO.setSiteId(condition.getSiteId());
        assignVO.setMaterialId(condition.getMaterialId());
        if (StringUtils.isNotEmpty(condition.getMaterialCategoryAssignId())) {
            assignVO.setMaterialCategoryAssignId(condition.getMaterialCategoryAssignId());
        }

        if (StringUtils.isEmpty(assignVO.getSiteId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "siteId", "【API:materialCategoryAssignUniqueValidate】"));
        }
        if (StringUtils.isEmpty(assignVO.getMaterialId())) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "materialId", "【API:materialCategoryAssignUniqueValidate】"));
        }
        if (StringUtils.isEmpty(assignVO.getMaterialCategoryId())) {
            throw new MtException("MT_MATERIAL_0054",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL",
                                            "materialCategoryId", "【API:materialCategoryAssignUniqueValidate】"));
        }

        MtMaterialCategory category = new MtMaterialCategory();
        category.setTenantId(tenantId);
        category.setMaterialCategoryId(assignVO.getMaterialCategoryId());
        category = mtMaterialCategoryMapper.selectOne(category);

        if (category == null) {
            throw new MtException("MT_MATERIAL_0030", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0030", "MATERIAL", "【API:materialCategoryAssignUniqueValidate】"));
        }

        MtMaterialCategoryAssignVO assignVo1 = new MtMaterialCategoryAssignVO();
        assignVo1.setMaterialId(assignVO.getMaterialId());
        assignVo1.setSiteId(assignVO.getSiteId());
        assignVo1.setMaterialCategorySetId(category.getMaterialCategorySetId());

        String result = null;
        try {
            result = self().setLimitMaterialAssignCategoryGet(tenantId, assignVo1);
        } catch (MtException ex) {
            result = null;
        }

        if (StringUtils.isEmpty(result)) {
            return "Y";
        } else {
            if (StringUtils.isNotEmpty(assignVO.getMaterialCategoryAssignId())) {
                MtMaterialCategoryAssignVO assignVo2 = new MtMaterialCategoryAssignVO();
                assignVo2.setMaterialId(assignVO.getMaterialId());
                assignVo2.setSiteId(assignVO.getSiteId());
                assignVo2.setMaterialCategoryId(result);
                List<MtMaterialCategoryAssign> ls = mtMaterialCategoryAssignMapper.selectData(tenantId, assignVo2);
                if (CollectionUtils.isEmpty(ls)) {
                    return "Y";
                } else {
                    return ls.get(0).getMaterialCategoryAssignId().equals(assignVO.getMaterialCategoryAssignId()) ? "Y"
                                    : "N";
                }
            } else {
                return "N";
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String materialCategoryAssign(Long tenantId, MtMaterialCategoryAssignVO2 assignVO) {
        if (StringUtils.isEmpty(assignVO.getMaterialCategoryId())) {
            throw new MtException("MT_MATERIAL_0021", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0021", "MATERIAL", "【API:materialCategoryAssign】"));
        }
        if (StringUtils.isEmpty(assignVO.getMaterialId()) && StringUtils.isEmpty(assignVO.getMaterialSiteId())) {
            throw new MtException("MT_MATERIAL_0076",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0076", "MATERIAL",
                                            "materialId", "materialSiteId", "【API:materialCategoryAssign】"));
        }
        boolean materialAndSiteEmptyFlag =
                        StringUtils.isEmpty(assignVO.getMaterialId()) && StringUtils.isEmpty(assignVO.getSiteId());
        boolean materialAndSiteNotEmptyFlag = StringUtils.isNotEmpty(assignVO.getMaterialId())
                        && StringUtils.isNotEmpty(assignVO.getSiteId());
        if (!(materialAndSiteEmptyFlag || materialAndSiteNotEmptyFlag)) {
            throw new MtException("MT_MATERIAL_0072", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0072", "MATERIAL", "materialId, siteId", "【API:materialCategoryAssign】"));
        }

        if (StringUtils.isNotEmpty(assignVO.getMaterialSiteId()) && StringUtils.isNotEmpty(assignVO.getMaterialId())
                        && StringUtils.isNotEmpty(assignVO.getSiteId())) {
            MtMaterialSite mtMaterialSite = new MtMaterialSite();
            mtMaterialSite.setSiteId(assignVO.getSiteId());
            mtMaterialSite.setMaterialId(assignVO.getMaterialId());
            String materialSiteId = mtMaterialSiteRepo.materialSiteLimitRelationGet(tenantId, mtMaterialSite);
            if (!assignVO.getMaterialSiteId().equals(materialSiteId)) {
                throw new MtException("MT_MATERIAL_0077",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0077", "MATERIAL",
                                                "materialId", "siteId", "materialSiteId",
                                                "【API:materialCategoryAssign】"));
            }
        }

        String materialSiteId = assignVO.getMaterialSiteId();
        if (StringUtils.isNotEmpty(assignVO.getMaterialSiteId())) {
            MtMaterialSite mtMaterialSite =
                            mtMaterialSiteRepo.relationLimitMaterialSiteGet(tenantId, assignVO.getMaterialSiteId());
            if (null == mtMaterialSite || !Y_FLAG.equals(mtMaterialSite.getEnableFlag())
                            || StringUtils.isEmpty(mtMaterialSite.getMaterialSiteId())) {
                throw new MtException("MT_MATERIAL_0055", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0055", "MATERIAL", "materialSiteId", "【API:materialCategoryAssign】"));
            }
        } else if (StringUtils.isNotEmpty(assignVO.getMaterialId()) && StringUtils.isNotEmpty(assignVO.getSiteId())) {
            MtMaterialSite mtMaterialSite = new MtMaterialSite();
            mtMaterialSite.setSiteId(assignVO.getSiteId());
            mtMaterialSite.setMaterialId(assignVO.getMaterialId());
            materialSiteId = mtMaterialSiteRepo.materialSiteLimitRelationGet(tenantId, mtMaterialSite);
            if (StringUtils.isEmpty(materialSiteId)) {
                throw new MtException("MT_MATERIAL_0075", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0075", "MATERIAL", "【API:materialCategoryAssign】"));
            }
        }

        MtMaterialCategoryAssign mtMaterialCategoryAssign = new MtMaterialCategoryAssign();
        mtMaterialCategoryAssign.setTenantId(tenantId);
        mtMaterialCategoryAssign.setMaterialSiteId(materialSiteId);
        mtMaterialCategoryAssign.setMaterialCategoryId(assignVO.getMaterialCategoryId());
        if (null != mtMaterialCategoryAssignMapper.selectOne(mtMaterialCategoryAssign)) {
            throw new MtException("MT_MATERIAL_0038", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0038", "MATERIAL", "【API:materialCategoryAssign】"));
        }

        self().insertSelective(mtMaterialCategoryAssign);

        return mtMaterialCategoryAssign.getMaterialCategoryAssignId();
    }

    @Override
    public List<MtMaterialCategoryAssign> materialCategoryAssignByCategoryIds(Long tenantId, List<String> categoryIds) {
        if (CollectionUtils.isEmpty(categoryIds)) {
            return Collections.emptyList();
        }
        return mtMaterialCategoryAssignMapper.selectByMaterialCategoryIds(tenantId, categoryIds);
    }

    @Override
    public List<MtMaterialCategoryAssign> materialCategoryAssignByMaterilSiteIds(Long tenantId, List<String> materialSiteIds) {
        if (CollectionUtils.isEmpty(materialSiteIds)) {
            return Collections.emptyList();
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("t.MATERIAL_SITE_ID", materialSiteIds, 1000);
        return mtMaterialCategoryAssignMapper.selectByMaterilSiteIds(tenantId, whereInValuesSql);
    }

    @Override
    public List<MtMaterialCategoryAssignVO6> defaultSetMaterialAssignCategoryBatchGet(Long tenantId,
                                                                                      List<MtMaterialCategoryAssignVO5> dto) {
        final String apiName = "【API:defaultSetMaterialAssignCategoryBatchGet】";
        // 第一步，判断须输入参数
        if (CollectionUtils.isEmpty(dto)) {
            throw new MtException("MT_MATERIAL_0054",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL", "inputList", apiName));
        }

        if (dto.stream().anyMatch(t -> MtIdHelper.isIdNull(t.getMaterialId()))) {
            throw new MtException("MT_MATERIAL_0054",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL", "materialId", apiName));
        }

        if (dto.stream().anyMatch(t -> MtIdHelper.isIdNull(t.getSiteId()))) {
            throw new MtException("MT_MATERIAL_0054",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL", "siteId", apiName));
        }

        if (dto.stream().anyMatch(t -> StringUtils.isEmpty(t.getDefaultType()))) {
            throw new MtException("MT_MATERIAL_0054",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL", "defaultType", apiName));
        }

        List<String> defaultTypeIds =
                dto.stream().map(MtMaterialCategoryAssignVO5::getDefaultType).distinct().collect(toList());
        // 第二步，获取输入参数列表的所有defaultType去重组成参数列表
        List<MtMaterialCategorySetVO2> categorySetVO2s =
                mtMaterialCategorySetRepository.defaultCategorySetBatchGet(tenantId, defaultTypeIds);


        Map<String, String> siteIdMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(categorySetVO2s)) {
            // 组装map
            siteIdMap = categorySetVO2s.stream().filter(t -> MtIdHelper.isIdNotNull(t.getMaterialCategorySetId()))
                    .collect(Collectors.toMap(MtMaterialCategorySetVO2::getDefaultType,
                            MtMaterialCategorySetVO2::getMaterialCategorySetId));

        }
        // 拼接传入参数
        List<MtMaterialCategoryAssignVO3> conditionList = new ArrayList<>();
        for (MtMaterialCategoryAssignVO5 assignDto : dto) {
            String materialCategorySetId = siteIdMap.get(assignDto.getDefaultType());
            if (MtIdHelper.isIdNotNull(materialCategorySetId)) {
                MtMaterialCategoryAssignVO3 condition = new MtMaterialCategoryAssignVO3();
                condition.setSiteId(assignDto.getSiteId());
                condition.setMaterialId(assignDto.getMaterialId());
                condition.setMaterialCategorySetId(materialCategorySetId);
                conditionList.add(condition);
            }
        }

        List<MtMaterialCategoryAssignVO4> materialCategoryIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(conditionList)) {
            materialCategoryIdList = setLimitMaterialAssignCategoryBatchGet(tenantId, conditionList);
        }

        List<MtMaterialCategoryAssignVO6> resultList = new ArrayList<>();
        for (MtMaterialCategoryAssignVO5 assignDto : dto) {
            MtMaterialCategoryAssignVO6 result = new MtMaterialCategoryAssignVO6();
            result.setSiteId(assignDto.getSiteId());
            result.setMaterialId(assignDto.getMaterialId());
            result.setDefaultType(assignDto.getDefaultType());

            String materialCategorySetId = siteIdMap.get(assignDto.getDefaultType());
            if (MtIdHelper.isIdNotNull(materialCategorySetId)) {
                Optional<MtMaterialCategoryAssignVO4> Optional = materialCategoryIdList.stream()
                        .filter(c -> c.getSiteId().compareTo(assignDto.getSiteId()) == 0
                                && c.getMaterialId().compareTo(assignDto.getMaterialId()) == 0
                                && c.getMaterialCategorySetId().compareTo(materialCategorySetId) == 0)
                        .findFirst();
                if (Optional.isPresent()) {
                    result.setMaterialCategoryId(Optional.get().getMaterialCategoryId());
                } else {
                    result.setMaterialCategoryId(null);
                }
            } else {
                result.setMaterialCategoryId(null);
            }
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public List<MtMaterialCategoryAssignVO4> setLimitMaterialAssignCategoryBatchGet(Long tenantId,
                                                                                    List<MtMaterialCategoryAssignVO3> dto) {
        final String apiName = "【API:setLimitMaterialAssignCategoryBatchGet】";
        // 数据校验
        if (CollectionUtils.isEmpty(dto)) {
            throw new MtException("MT_MATERIAL_0054",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054", "MATERIAL","inputList", apiName));
        }

        if (dto.stream().anyMatch(t -> MtIdHelper.isIdNull(t.getMaterialId()))) {
            throw new MtException("MT_MATERIAL_0054",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL", "materialId", apiName));
        }

        if (dto.stream().anyMatch(t -> MtIdHelper.isIdNull(t.getSiteId()))) {
            throw new MtException("MT_MATERIAL_0054",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL","siteId", apiName));
        }

        if (dto.stream().anyMatch(t -> MtIdHelper.isIdNull(t.getMaterialCategorySetId()))) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0054","MATERIAL",
                    "materialCategorySetId", apiName));
        }

        // 第二步，根据物料站点获取物料站点关系
        List<MtMaterialSiteVO3> siteVO3s = dto.stream().map(t -> {
            MtMaterialSiteVO3 site = new MtMaterialSiteVO3();
            site.setMaterialId(t.getMaterialId());
            site.setSiteId(t.getSiteId());
            return site;
        }).collect(toList());
        List<MtMaterialSiteVO4> mtMaterialSites =
                mtMaterialSiteRepository.materialSiteLimitRelationBatchGet(tenantId, siteVO3s);

        Map<String, MtMaterialSiteVO4> materialSiteMap = new HashMap<>(mtMaterialSites.size());

        Map<String, List<MtMaterialCategoryAssign>> categoryIdMap = new HashMap<>();

        Map<String, String> setIdMap = new HashMap<>();


        if (CollectionUtils.isNotEmpty(mtMaterialSites)) {

            List<String> mtMaterialSiteIds =
                    mtMaterialSites.stream().map(MtMaterialSiteVO4::getMaterialSiteId).collect(toList());

            List<MtMaterialCategoryAssign> categoryAssigns =
                    this.materialCategoryAssignByMaterilSiteIds(tenantId, mtMaterialSiteIds);

            if (CollectionUtils.isNotEmpty(categoryAssigns)) {
                List<String> categoryIds = categoryAssigns.stream().map(MtMaterialCategoryAssign::getMaterialCategoryId)
                        .collect(toList());

                if (CollectionUtils.isNotEmpty(categoryIds)) {
                    List<MtMaterialCategoryVO6> categories =
                            mtMaterialCategoryRepository.materialCategorySetBatchGet(tenantId, categoryIds);
                    if (CollectionUtils.isNotEmpty(categories)) {
                        setIdMap = categories.stream()
                                .collect(Collectors.toMap(MtMaterialCategoryVO6::getMaterialCategoryId,
                                        MtMaterialCategoryVO6::getMaterialCategorySetId));
                    }
                }

                categoryIdMap = categoryAssigns.stream()
                        .collect(Collectors.groupingBy(MtMaterialCategoryAssign::getMaterialSiteId));
            }

            // 以MaterialId，SiteId两个字段转Map
            materialSiteMap = mtMaterialSites.stream()
                    .collect(Collectors.toMap(t -> t.getMaterialId() + ":" + t.getSiteId(), t -> t));
        }

        // 返回结果
        List<MtMaterialCategoryAssignVO4> resultList = new ArrayList<>();
        for (MtMaterialCategoryAssignVO3 assign : dto) {
            MtMaterialCategoryAssignVO4 result = new MtMaterialCategoryAssignVO4();
            result.setMaterialId(assign.getMaterialId());
            result.setSiteId(assign.getSiteId());
            result.setMaterialCategorySetId(assign.getMaterialCategorySetId());

            String mapKey = assign.getMaterialId() + ":" + assign.getSiteId();
            MtMaterialSiteVO4 mtMaterialSite = materialSiteMap.get(mapKey);

            if (mtMaterialSite == null || MtIdHelper.isIdNull(mtMaterialSite.getMaterialSiteId())) {
                // 若materialSiteId为空，返回materialCategoryId为空
                result.setMaterialCategoryId(null);
            } else {
                result.setMaterialCategoryId(null);
                // 若materialSiteId不为空，继续第三步
                List<MtMaterialCategoryAssign> assigns = categoryIdMap.get(mtMaterialSite.getMaterialSiteId());
                // 第四步，判断物料类别是否属于指定物料类别集
                if (CollectionUtils.isNotEmpty(assigns)) {
                    for (MtMaterialCategoryAssign categoryAssign : assigns) {
                        String setId = setIdMap.get(categoryAssign.getMaterialCategoryId());
                        if (assign.getMaterialCategorySetId().equals(setId)) {
                            result.setMaterialCategoryId(categoryAssign.getMaterialCategoryId());
                        }
                    }
                }
            }
            resultList.add(result);
        }

        return resultList;
    }


}
