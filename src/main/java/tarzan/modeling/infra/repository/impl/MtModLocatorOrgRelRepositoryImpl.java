package tarzan.modeling.infra.repository.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.modeling.domain.entity.*;
import tarzan.modeling.domain.repository.*;
import tarzan.modeling.domain.vo.*;
import tarzan.modeling.infra.mapper.MtModLocatorOrgRelMapper;

/**
 * 组织与库位结构关系 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@Component
public class MtModLocatorOrgRelRepositoryImpl extends BaseRepositoryImpl<MtModLocatorOrgRel>
        implements MtModLocatorOrgRelRepository {
    private static final String Y_FLAG = "Y";
    private static final String SITE = "SITE";
    private static final String AREA = "AREA";
    private static final String PROD_LINE = "PROD_LINE";
    private static final String TOP = "TOP";
    private static final String BOTTOM = "BOTTOM";
    private static final String ALL = "ALL";

    @Autowired
    private MtModLocatorOrgRelMapper mtModLocatorOrgRelMapper;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtModAreaRepository mtModAreaRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepo;

    @Override
    public String locatorOrganizationRelVerify(Long tenantId, String locatorId, String parentOrganizationType,
                                               String parentOrganizationId) {
        if (StringUtils.isEmpty(locatorId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "locatorId", "【API:locatorOrganizationRelVerify】"));
        }
        if (StringUtils.isEmpty(parentOrganizationType)) {
            throw new MtException("MT_MODELING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                            "parentOrganizationType", "【API:locatorOrganizationRelVerify】"));
        }
        if (StringUtils.isEmpty(parentOrganizationId)) {
            throw new MtException("MT_MODELING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                            "parentOrganizationId", "【API:locatorOrganizationRelVerify】"));
        }

        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setTenantId(tenantId);
        mtModLocator.setLocatorId(locatorId);
        mtModLocator = this.mtModLocatorRepository.selectOne(mtModLocator);

        if (mtModLocator == null || !Y_FLAG.equals(mtModLocator.getEnableFlag())) {
            throw new MtException("MT_MODELING_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0005", "MODELING", "locatorId", "【API:locatorOrganizationRelVerify】"));
        }

        if (!SITE.equals(parentOrganizationType) && !AREA.equals(parentOrganizationType)
                && !"PROD_LINE".equals(parentOrganizationType) && !"WORKCELL".equals(parentOrganizationType)) {
            throw new MtException("MT_MODELING_0010",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0010", "MODELING",
                            "parentOrganizationType", "【API:locatorOrganizationRelVerify】"));
        }

        if (AREA.equals(parentOrganizationType)) {
            MtModArea mtModArea = new MtModArea();
            mtModArea.setTenantId(tenantId);
            mtModArea.setAreaId(parentOrganizationId);
            mtModArea = this.mtModAreaRepository.selectOne(mtModArea);
            if (mtModArea == null || !Y_FLAG.equals(mtModArea.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0005",
                                "MODELING", "parentOrganizationId",
                                "【API:locatorOrganizationRelVerify】"));
            }
        } else if (SITE.equals(parentOrganizationType)) {
            MtModSite mtModSite = new MtModSite();
            mtModSite.setTenantId(tenantId);
            mtModSite.setSiteId(parentOrganizationId);
            mtModSite = this.mtModSiteRepository.selectOne(mtModSite);
            if (mtModSite == null || !Y_FLAG.equals(mtModSite.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0005",
                                "MODELING", "parentOrganizationId",
                                "【API:locatorOrganizationRelVerify】"));
            }
        } else if (PROD_LINE.equals(parentOrganizationType)) {
            MtModProductionLine mtModProductionLine = new MtModProductionLine();
            mtModProductionLine.setTenantId(tenantId);
            mtModProductionLine.setProdLineId(parentOrganizationId);
            mtModProductionLine = this.mtModProductionLineRepository.selectOne(mtModProductionLine);
            if (mtModProductionLine == null || !Y_FLAG.equals(mtModProductionLine.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0005",
                                "MODELING", "parentOrganizationId",
                                "【API:locatorOrganizationRelVerify】"));
            }
        } else {
            MtModWorkcell mtModWorkcell = new MtModWorkcell();
            mtModWorkcell.setTenantId(tenantId);
            mtModWorkcell.setWorkcellId(parentOrganizationId);
            mtModWorkcell = this.mtModWorkcellRepository.selectOne(mtModWorkcell);
            if (mtModWorkcell == null || !Y_FLAG.equals(mtModWorkcell.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0005",
                                "MODELING", "parentOrganizationId",
                                "【API:locatorOrganizationRelVerify】"));
            }
        }

        MtModLocatorOrgRel mtModLocatorOrgRel = new MtModLocatorOrgRel();
        mtModLocatorOrgRel.setTenantId(tenantId);
        mtModLocatorOrgRel.setLocatorId(locatorId);
        mtModLocatorOrgRel.setOrganizationType(parentOrganizationType);
        mtModLocatorOrgRel.setOrganizationId(parentOrganizationId);
        List<MtModLocatorOrgRel> list = this.mtModLocatorOrgRelMapper.select(mtModLocatorOrgRel);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new MtException("MT_MODELING_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0016", "MODELING", "【API:locatorOrganizationRelVerify】"));
        }

        return "Y";
    }

    @Override
    public List<MtModLocatorOrgRelVO> organizationLimitLocatorQuery(Long tenantId, String organizationType,
                                                                    String organizationId, String queryType) {
        if (StringUtils.isEmpty(organizationType)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationType", "【API:organizationLimitLocatorQuery】"));
        }
        if (StringUtils.isEmpty(organizationId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationId", "【API:organizationLimitLocatorQuery】"));
        }
        if (StringUtils.isEmpty(queryType)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "queryType", "【API:organizationLimitLocatorQuery】"));
        }

        List<MtGenType> mtGenTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "ORGANIZATION_TYPE");
        if (CollectionUtils.isEmpty(mtGenTypes)) {
            throw new MtException("MT_MODELING_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0018", "MODELING", "【API:organizationLimitLocatorQuery】"));
        }
        if (!mtGenTypes.stream().anyMatch(t -> t.getTypeCode().equals(organizationType))) {
            throw new MtException("MT_MODELING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0019", "MODELING", "organizationType", "【API:organizationLimitLocatorQuery】"));
        }

        if (!TOP.equals(queryType) && !BOTTOM.equals(queryType) && !ALL.equals(queryType)) {
            throw new MtException("MT_MODELING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0002", "MODELING", "queryType", "【API:organizationLimitLocatorQuery】"));
        }

        MtModLocatorOrgRel mtModLocatorOrgRel = new MtModLocatorOrgRel();
        mtModLocatorOrgRel.setTenantId(tenantId);
        mtModLocatorOrgRel.setOrganizationType(organizationType);
        mtModLocatorOrgRel.setOrganizationId(organizationId);
        List<MtModLocatorOrgRel> list = this.mtModLocatorOrgRelMapper.select(mtModLocatorOrgRel);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        final List<MtModLocatorOrgRelVO> result = new ArrayList<MtModLocatorOrgRelVO>();
        if (!BOTTOM.equals(queryType)) {
            list.forEach(t -> {
                MtModLocatorOrgRelVO tmpVo = new MtModLocatorOrgRelVO();
                tmpVo.setLocatorId(t.getLocatorId());
                tmpVo.setSequence(t.getSequence());
                result.add(tmpVo);
            });
        }

        if (BOTTOM.equals(queryType)) {
            for (MtModLocatorOrgRel t : list) {
                // 不会是空
                MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
                mtModLocatorVO9.setLocatorId(t.getLocatorId());
                mtModLocatorVO9.setQueryType("BOTTOM");
                List<String> ids = this.mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
                if (CollectionUtils.isNotEmpty(ids)) {
                    ids.forEach(id -> {
                        MtModLocatorOrgRelVO tmpVo = new MtModLocatorOrgRelVO();
                        tmpVo.setLocatorId(id);
                        if (id.equals(t.getLocatorId())) {
                            tmpVo.setSequence(t.getSequence());
                        }
                        result.add(tmpVo);
                    });
                } else {
                    // 第4步就是底层,
                    MtModLocatorOrgRelVO tmpVo = new MtModLocatorOrgRelVO();
                    tmpVo.setLocatorId(t.getLocatorId());
                    tmpVo.setSequence(t.getSequence());
                    result.add(tmpVo);
                }
            }
        } else if (ALL.equals(queryType)) {
            for (MtModLocatorOrgRel t : list) {
                MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
                mtModLocatorVO9.setLocatorId(t.getLocatorId());
                mtModLocatorVO9.setQueryType("ALL");
                List<String> ids = this.mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
                ids.forEach(id -> {
                    MtModLocatorOrgRelVO tmpVo = new MtModLocatorOrgRelVO();
                    tmpVo.setLocatorId(id);
                    result.add(tmpVo);
                });
            }
        }

        return result;
    }

    @Override
    public List<MtModLocatorOrgRelVO3> locatorLimitOrganizationQuery(Long tenantId, MtModLocatorOrgRelVO2 queryVO) {
        if (StringUtils.isEmpty(queryVO.getLocatorId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "locatorId", "【API:locatorLimitOrganizationQuery】"));
        }
        if (StringUtils.isEmpty(queryVO.getOrganizationType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationType", "【API:locatorLimitOrganizationQuery】"));
        }

        List<MtGenType> mtGenTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "ORGANIZATION_TYPE");
        if (CollectionUtils.isEmpty(mtGenTypes)) {
            throw new MtException("MT_MODELING_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0018", "MODELING", "【API:locatorLimitOrganizationQuery】"));
        }
        if (mtGenTypes.stream().noneMatch(t -> t.getTypeCode().equals(queryVO.getOrganizationType()))) {
            throw new MtException("MT_MODELING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0019", "MODELING", "organizationType", "【API:locatorLimitOrganizationQuery】"));
        }


        // get parent locator list
        List<String> parentLocatorIdList =
                mtModLocatorRepository.parentLocatorQuery(tenantId, queryVO.getLocatorId(), "ALL");
        parentLocatorIdList.add(queryVO.getLocatorId());
        MtModLocatorOrgRel locatorOrgRel;
        List<MtModLocatorOrgRel> locatorOrgRelList = new ArrayList<>();
        for (String locatorId : parentLocatorIdList) {
            locatorOrgRel = new MtModLocatorOrgRel();
            locatorOrgRel.setTenantId(tenantId);
            locatorOrgRel.setLocatorId(locatorId);
            List<MtModLocatorOrgRel> mtModLocatorOrgRels = mtModLocatorOrgRelMapper.select(locatorOrgRel);
            locatorOrgRelList.addAll(mtModLocatorOrgRels);
        }

        if (CollectionUtils.isEmpty(locatorOrgRelList)) {
            return Collections.emptyList();
        }

        // 筛选结果为目标组织

        MtModLocatorOrgRelVO3 locatorOrgRelVO;
        List<MtModLocatorOrgRelVO3> resultList = new ArrayList<>();

        // get org site list
        MtModOrganizationRelVO organizationRelVO;
        List<String> topSiteIdList = new ArrayList<>();
        for (MtModLocatorOrgRel rel : locatorOrgRelList) {
            organizationRelVO = new MtModOrganizationRelVO();
            organizationRelVO.setOrganizationId(rel.getOrganizationId());
            organizationRelVO.setOrganizationType(rel.getOrganizationType());
            if (StringUtils.isNotEmpty(queryVO.getSiteType())) {
                organizationRelVO.setSiteType(queryVO.getSiteType());
            }

            // 只有满足条件的组织才返回
            List<String> topSiteIds = mtModOrganizationRelRepo.organizationLimitSiteQuery(tenantId, organizationRelVO);

            if (CollectionUtils.isNotEmpty(topSiteIds)) {
                topSiteIdList.addAll(topSiteIds);

                locatorOrgRelVO = new MtModLocatorOrgRelVO3();
                locatorOrgRelVO.setOrganizationId(rel.getOrganizationId());
                locatorOrgRelVO.setOrganizationType(rel.getOrganizationType());
                resultList.add(locatorOrgRelVO);
            }
        }
        topSiteIdList = topSiteIdList.stream().distinct().collect(Collectors.toList());
        List<Tuple> tupleList =
                locatorOrgRelList.stream().map(c -> new Tuple(c.getOrganizationId(), c.getOrganizationType()))
                        .distinct().collect(Collectors.toList());

        // 查询站点时直接返回
        if (!"SITE".equalsIgnoreCase(queryVO.getOrganizationType())) {
            // get parent org list
            MtModOrganizationVO2 parentOrgVO;
            List<MtModOrganizationItemVO> orgRelList;
            for (String siteId : topSiteIdList) {
                for (Tuple rel : tupleList) {
                    parentOrgVO = new MtModOrganizationVO2();
                    parentOrgVO.setOrganizationId(rel.getOrganizationId());
                    parentOrgVO.setOrganizationType(rel.getOrganizationType());
                    parentOrgVO.setTopSiteId(siteId);
                    parentOrgVO.setParentOrganizationType(queryVO.getOrganizationType());
                    parentOrgVO.setQueryType("ALL");
                    orgRelList = mtModOrganizationRelRepo.parentOrganizationRelQuery(tenantId, parentOrgVO);
                    for (MtModOrganizationItemVO itemVO : orgRelList) {
                        locatorOrgRelVO = new MtModLocatorOrgRelVO3();
                        locatorOrgRelVO.setOrganizationId(itemVO.getOrganizationId());
                        locatorOrgRelVO.setOrganizationType(queryVO.getOrganizationType());
                        resultList.add(locatorOrgRelVO);
                    }
                }
            }
        }else{
            topSiteIdList.stream().forEach(c -> {
                MtModLocatorOrgRelVO3 vo3 = new MtModLocatorOrgRelVO3();
                vo3.setOrganizationId(c);
                vo3.setOrganizationType("SITE");
                resultList.add(vo3);
            });
        }
        return resultList.stream().filter(t -> queryVO.getOrganizationType().equals(t.getOrganizationType())).distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> organizationLimitLocatorAllQuery(Long tenantId, MtModLocatorOrgRelVO3 queryVO) {
        if (StringUtils.isEmpty(queryVO.getOrganizationId())) {
            throw new MtException("MT_MODELING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                            "organizationId", "【API:organizationLimitLocatorAllQuery】"));
        }
        if (StringUtils.isEmpty(queryVO.getOrganizationType())) {
            throw new MtException("MT_MODELING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                            "organizationType", "【API:organizationLimitLocatorAllQuery】"));
        }

        List<MtGenType> mtGenTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "ORGANIZATION_TYPE");
        if (CollectionUtils.isEmpty(mtGenTypes)) {
            throw new MtException("MT_MODELING_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0018", "MODELING", "【API:organizationLimitLocatorQuery】"));
        }
        if (mtGenTypes.stream().noneMatch(t -> t.getTypeCode().equals(queryVO.getOrganizationType()))) {
            throw new MtException("MT_MODELING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0019", "MODELING", "organizationType", "【API:organizationLimitLocatorQuery】"));
        }

        MtModOrganizationRel queryOrgRel = new MtModOrganizationRel();
        queryOrgRel.setTenantId(tenantId);
        queryOrgRel.setOrganizationId(queryVO.getOrganizationId());
        queryOrgRel.setOrganizationType(queryVO.getOrganizationType());

        Criteria criteria = new Criteria(queryOrgRel);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtModOrganizationRel.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtModOrganizationRel.FIELD_ORGANIZATION_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtModOrganizationRel.FIELD_ORGANIZATION_TYPE, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

        List<MtModOrganizationRel> orgRelList = mtModOrganizationRelRepo.selectOptional(queryOrgRel, criteria);
        if (CollectionUtils.isEmpty(orgRelList)) {
            return Collections.emptyList();
        }

        List<String> topSiteIdList =
                orgRelList.stream().map(MtModOrganizationRel::getTopSiteId).collect(Collectors.toList());
        List<String> locatorOrgRelList = new ArrayList<>();
        for (String topSiteId : topSiteIdList) {
            switch (queryVO.getOrganizationType()) {
                case "SITE":
                case "AREA":
                    locatorOrgRelList.addAll(getModLocatorOrganizationRelList(tenantId, topSiteId,
                            queryVO.getOrganizationType(), queryVO.getOrganizationId(), "AREA"));
                    locatorOrgRelList.addAll(getModLocatorOrganizationRelList(tenantId, topSiteId,
                            queryVO.getOrganizationType(), queryVO.getOrganizationId(), "PROD_LINE"));
                    locatorOrgRelList.addAll(getModLocatorOrganizationRelList(tenantId, topSiteId,
                            queryVO.getOrganizationType(), queryVO.getOrganizationId(), "WORKCELL"));
                    break;
                case "PROD_LINE":
                case "WORKCELL":
                    locatorOrgRelList.addAll(getModLocatorOrganizationRelList(tenantId, topSiteId,
                            queryVO.getOrganizationType(), queryVO.getOrganizationId(), "WORKCELL"));
                    break;
                default:
                    break;
            }
        }

        return locatorOrgRelList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 查询组织下对应的所有库位信息
     *
     * @author benjamin
     * @date 2019-09-04 19:15
     * @param tenantId 租户Id
     * @param topSiteId 站点Id
     * @param parentOrganizationType 父组织类型
     * @param parentOrganizationId 父组织Id
     * @param organizationType 查询组织类型
     * @return list
     */
    private List<String> getModLocatorOrganizationRelList(Long tenantId, String topSiteId,
                                                          String parentOrganizationType, String parentOrganizationId, String organizationType) {
        MtModOrganizationVO2 orgQueryVO = new MtModOrganizationVO2();
        orgQueryVO.setTopSiteId(topSiteId);
        orgQueryVO.setParentOrganizationType(parentOrganizationType);
        orgQueryVO.setParentOrganizationId(parentOrganizationId);
        orgQueryVO.setOrganizationType(organizationType);
        orgQueryVO.setQueryType("ALL");

        MtModLocatorOrgRel locatorOrgRel = new MtModLocatorOrgRel();
        locatorOrgRel.setTenantId(tenantId);
        locatorOrgRel.setOrganizationId(parentOrganizationId);
        locatorOrgRel.setOrganizationType(parentOrganizationType);
        List<MtModLocatorOrgRel> locatorOrgRelList = new ArrayList<>();
        locatorOrgRelList.addAll(mtModLocatorOrgRelMapper.select(locatorOrgRel));

        List<MtModOrganizationItemVO> orgList = mtModOrganizationRelRepo.subOrganizationRelQuery(tenantId, orgQueryVO);
        for (MtModOrganizationItemVO org : orgList) {
            locatorOrgRel = new MtModLocatorOrgRel();
            locatorOrgRel.setTenantId(tenantId);
            locatorOrgRel.setOrganizationId(org.getOrganizationId());
            locatorOrgRel.setOrganizationType(organizationType);
            locatorOrgRelList.addAll(mtModLocatorOrgRelMapper.select(locatorOrgRel));
        }

        List<String> resultList = new ArrayList<>();
        for (MtModLocatorOrgRel rel : locatorOrgRelList) {
            resultList.add(rel.getLocatorId());

            MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
            mtModLocatorVO9.setLocatorId(rel.getLocatorId());
            mtModLocatorVO9.setQueryType("ALL");
            resultList.addAll(mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9));
        }

        return resultList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void locatorOrganizationRelDelete(Long tenantId, MtModLocatorOrgRel dto) {
        if (StringUtils.isEmpty(dto.getOrganizationId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationId", "【API:locatorOrganizationRelDelete】"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationType", "【API:locatorOrganizationRelDelete】"));
        }
        if (StringUtils.isEmpty(dto.getLocatorId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "locatorId", "【API:locatorOrganizationRelDelete】"));
        }

        List<MtGenType> mtGenTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "ORGANIZATION_TYPE");
        if (CollectionUtils.isEmpty(mtGenTypes)) {
            throw new MtException("MT_MODELING_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0018", "MODELING", "【API:locatorOrganizationRelDelete】"));
        }
        if (!mtGenTypes.stream().anyMatch(t -> t.getTypeCode().equals(dto.getOrganizationType()))) {
            throw new MtException("MT_MODELING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0019", "MODELING", "organizationType", "【API:locatorOrganizationRelDelete】"));
        }

        MtModLocatorOrgRel tmp = new MtModLocatorOrgRel();
        tmp.setTenantId(tenantId);
        tmp.setOrganizationId(dto.getOrganizationId());
        tmp.setOrganizationType(dto.getOrganizationType());
        tmp.setLocatorId(dto.getLocatorId());
        self().delete(tmp);
    }

    private static class Tuple implements Serializable {
        private static final long serialVersionUID = -1738320358437889925L;
        private String organizationId;
        private String organizationType;

        public Tuple(String organizationId, String organizationType) {
            this.organizationId = organizationId;
            this.organizationType = organizationType;
        }

        public String getOrganizationId() {
            return organizationId;
        }

        public String getOrganizationType() {
            return organizationType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple tuple = (Tuple) o;
            return Objects.equals(organizationId, tuple.organizationId)
                    && Objects.equals(organizationType, tuple.organizationType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(organizationId, organizationType);
        }
    }
}
