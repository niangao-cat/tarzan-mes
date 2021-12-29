package tarzan.modeling.infra.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.StringHelper;
import tarzan.modeling.domain.entity.*;
import tarzan.modeling.domain.repository.*;
import tarzan.modeling.domain.vo.*;
import tarzan.modeling.infra.mapper.*;

/**
 * 组织结构关系 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Component
public class MtModOrganizationRelRepositoryImpl extends BaseRepositoryImpl<MtModOrganizationRel>
        implements MtModOrganizationRelRepository {

    @Autowired
    private MtModSiteMapper mtModSiteMapper;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtModEnterpriseMapper mtModEnterpriseMapper;

    @Autowired
    private MtModOrganizationRelMapper mtModOrganizationRelMapper;

    @Autowired
    private MtModAreaMapper mtModAreaMapper;

    @Autowired
    private MtModWorkcellMapper mtModWorkcellMapper;

    @Autowired
    private MtModLocatorOrgRelMapper mtModLocatorOrgRelMapper;

    @Autowired
    private MtModProductionLineMapper mtModProductionLineMapper;

    @Autowired
    private MtModLocatorMapper mtModLocatorMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtModAreaRepository mtModAreaRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;


    /* 下面4个函数是用来verfify是用来校验是否可以在某个组织下新增组织 */
    @Override
    public String siteOrganizationRelVerify(Long tenantId, String siteId, String parentOrganizationType,
                                            String parentOrganizationId) {
        if (StringUtils.isEmpty(siteId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "siteId", "【API:siteOrganizationRelVerify】"));
        }
        if (StringUtils.isEmpty(parentOrganizationType)) {
            throw new MtException("MT_MODELING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                            "parentOrganizationType", "【API:siteOrganizationRelVerify】"));
        }
        if (StringUtils.isEmpty(parentOrganizationId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "parentOrganizationId", "【API:siteOrganizationRelVerify】"));
        }

        if (!MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE.equals(parentOrganizationType)) {
            throw new MtException("MT_MODELING_0004",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0004", "MODELING",
                            "parentOrganizationType", "【API:siteOrganizationRelVerify】"));
        }

        MtModSite mtModSite = new MtModSite();
        mtModSite.setTenantId(tenantId);
        mtModSite.setSiteId(siteId);
        mtModSite = this.mtModSiteMapper.selectOne(mtModSite);
        if (mtModSite == null || !"Y".equals(mtModSite.getEnableFlag())) {
            throw new MtException("MT_MODELING_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0005", "MODELING", "siteId", "【API:siteOrganizationRelVerify】"));
        }
        MtModEnterprise mtModEnterprise = new MtModEnterprise();
        mtModEnterprise.setTenantId(tenantId);
        mtModEnterprise.setEnterpriseId(parentOrganizationId);
        mtModEnterprise = this.mtModEnterpriseMapper.selectOne(mtModEnterprise);
        if (mtModEnterprise == null || !"Y".equals(mtModEnterprise.getEnableFlag())) {
            throw new MtException("MT_MODELING_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0005", "MODELING", "parentOrganizationId", "【API:siteOrganizationRelVerify】"));
        }

        MtModOrganizationRel mtModOrganizationRel = new MtModOrganizationRel();
        mtModOrganizationRel.setTenantId(tenantId);
        mtModOrganizationRel.setOrganizationId(siteId);
        mtModOrganizationRel.setTopSiteId(siteId);
        mtModOrganizationRel.setParentOrganizationType(parentOrganizationType);
        mtModOrganizationRel.setParentOrganizationId(parentOrganizationId);
        mtModOrganizationRel.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.SITE);

        List<MtModOrganizationRel> list = this.mtModOrganizationRelMapper.select(mtModOrganizationRel);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new MtException("MT_MODELING_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0006", "MODELING", "【API:siteOrganizationRelVerify】"));
        }
        return "Y";
    }

    @Override
    public String areaOrganizationRelVerify(Long tenantId, String areaId, String topSiteId,
                                            String parentOrganizationType, String parentOrganizationId) {
        if (StringUtils.isEmpty(areaId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "areaId", "【API:areaOrganizationRelVerify】"));
        }
        if (StringUtils.isEmpty(topSiteId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "topSiteId", "【API:areaOrganizationRelVerify】"));
        }
        if (StringUtils.isEmpty(parentOrganizationType)) {
            throw new MtException("MT_MODELING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                            "parentOrganizationType", "【API:areaOrganizationRelVerify】"));
        }
        if (StringUtils.isEmpty(parentOrganizationId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "parentOrganizationId", "【API:areaOrganizationRelVerify】"));
        }
        if (!MtBaseConstants.ORGANIZATION_TYPE.SITE.equals(parentOrganizationType)
                && !MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(parentOrganizationType)) {
            throw new MtException("MT_MODELING_0007",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0007", "MODELING",
                            "parentOrganizationType", "【API:areaOrganizationRelVerify】"));
        }

        MtModArea mtModArea = new MtModArea();
        mtModArea.setTenantId(tenantId);
        mtModArea.setAreaId(areaId);
        mtModArea = this.mtModAreaMapper.selectOne(mtModArea);
        if (mtModArea == null || !"Y".equals(mtModArea.getEnableFlag())) {
            throw new MtException("MT_MODELING_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0005", "MODELING", "areaId", "【API:areaOrganizationRelVerify】"));
        }
        MtModSite mtTopModSite = new MtModSite();
        mtTopModSite.setTenantId(tenantId);
        mtTopModSite.setSiteId(topSiteId);
        mtTopModSite = this.mtModSiteMapper.selectOne(mtTopModSite);
        if (mtTopModSite == null || !"Y".equals(mtTopModSite.getEnableFlag())) {
            throw new MtException("MT_MODELING_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0005", "MODELING", "topSiteId", "【API:areaOrganizationRelVerify】"));
        }

        if (MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(parentOrganizationType)) {
            mtModArea = new MtModArea();
            mtModArea.setTenantId(tenantId);
            mtModArea.setAreaId(parentOrganizationId);
            mtModArea = this.mtModAreaMapper.selectOne(mtModArea);
            if (mtModArea == null || !"Y".equals(mtModArea.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0005",
                                "MODELING", "parentOrganizationId", "【API:areaOrganizationRelVerify】"));
            }
            if (areaId.equals(parentOrganizationId)) {
                throw new MtException("MT_MODELING_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MODELING_0024", "MODELING", "【API:areaOrganizationRelVerify】"));
            }
        } else {
            MtModSite mtModSite = new MtModSite();
            mtModSite.setTenantId(tenantId);
            mtModSite.setSiteId(topSiteId);
            mtModSite = this.mtModSiteMapper.selectOne(mtModSite);
            if (mtModSite == null || !"Y".equals(mtModSite.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0005",
                                "MODELING", "parentOrganizationId", "【API:areaOrganizationRelVerify】"));
            }
        }

        // 查询当前区域所有父节点
        MtModOrganizationRel mtModOrganizationRel = new MtModOrganizationRel();
        mtModOrganizationRel.setTenantId(tenantId);
        mtModOrganizationRel.setOrganizationId(areaId);
        mtModOrganizationRel.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.AREA);
        List<MtModOrganizationRel> areaAllOrgRelList = this.mtModOrganizationRelMapper.select(mtModOrganizationRel);
        if (CollectionUtils.isEmpty(areaAllOrgRelList)) {
            return "Y";
        }

        // 校验
        // 1. 同一个区域，父层唯一，不能重复定义，否则报错
        Optional<MtModOrganizationRel> any = areaAllOrgRelList.stream()
                .filter(t -> t.getTopSiteId().equals(topSiteId)
                        && t.getParentOrganizationType().equals(parentOrganizationType)
                        && t.getParentOrganizationId().equals(parentOrganizationId))
                .findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MODELING_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0006", "MODELING", "【API:areaOrganizationRelVerify】"));
        }

        // 2. 同一个站点下，同一个区域只能有一个关系存在（不论是跟站点的关系，还是区域自己的嵌套关系），否则报错
        any = areaAllOrgRelList.stream().filter(t -> t.getTopSiteId().equals(topSiteId)).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MODELING_0049",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0049", "MODELING",
                            mtModArea.getAreaCode(), mtTopModSite.getSiteCode(),
                            "【API:areaOrganizationRelVerify】"));
        }

        // 3. 同一个区域，在不同站点下，站点的类型不能相同，否则报错
        List<String> topSiteIds =
                areaAllOrgRelList.stream().map(MtModOrganizationRel::getTopSiteId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(topSiteIds)) {
            List<MtModSite> mtModSiteList = this.mtModSiteMapper.selectSites(tenantId, topSiteIds);
            if (CollectionUtils.isNotEmpty(mtModSiteList)) {
                MtModSite finalMtTopModSite = mtTopModSite;
                Optional<MtModSite> resSiteTypes = mtModSiteList.stream()
                        .filter(t -> t.getSiteType().equals(finalMtTopModSite.getSiteType())).findAny();
                if (resSiteTypes.isPresent()) {
                    String siteCode = resSiteTypes.get().getSiteCode();
                    throw new MtException("MT_MODELING_0008",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0008",
                                    "MODELING", siteCode, "【API:areaOrganizationRelVerify】"));
                }
            }
        }
        return "Y";
    }

    @Override
    public String prodLineOrganizationRelVerify(Long tenantId, String prodLineId, String topSiteId,
                                                String parentOrganizationType, String parentOrganizationId) {
        if (StringUtils.isEmpty(prodLineId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "prodLineId", "【API:prodLineOrganizationRelVerify】"));
        }
        if (StringUtils.isEmpty(topSiteId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "topSiteId", "【API:prodLineOrganizationRelVerify】"));
        }
        if (StringUtils.isEmpty(parentOrganizationType)) {
            throw new MtException("MT_MODELING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                            "parentOrganizationType", "【API:prodLineOrganizationRelVerify】"));
        }
        if (StringUtils.isEmpty(parentOrganizationId)) {
            throw new MtException("MT_MODELING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                            "parentOrganizationId", "【API:prodLineOrganizationRelVerify】"));
        }

        if (!MtBaseConstants.ORGANIZATION_TYPE.SITE.equals(parentOrganizationType)
                && !MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(parentOrganizationType)) {
            throw new MtException("MT_MODELING_0007",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0007", "MODELING",
                            "parentOrganizationType", "【API:prodLineOrganizationRelVerify】"));
        }
        MtModProductionLine mtModProductionLine = new MtModProductionLine();
        mtModProductionLine.setTenantId(tenantId);
        mtModProductionLine.setProdLineId(prodLineId);
        mtModProductionLine = this.mtModProductionLineMapper.selectOne(mtModProductionLine);
        if (mtModProductionLine == null || !"Y".equals(mtModProductionLine.getEnableFlag())) {
            throw new MtException("MT_MODELING_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0005", "MODELING", "prodLineId", "【API:prodLineOrganizationRelVerify】"));
        }
        MtModSite mtTopModSite = new MtModSite();
        mtTopModSite.setTenantId(tenantId);
        mtTopModSite.setSiteId(topSiteId);
        mtTopModSite = this.mtModSiteMapper.selectOne(mtTopModSite);
        if (mtTopModSite == null || !"Y".equals(mtTopModSite.getEnableFlag())) {
            throw new MtException("MT_MODELING_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0005", "MODELING", "topSiteId", "【API:prodLineOrganizationRelVerify】"));
        }

        if (MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(parentOrganizationType)) {
            MtModArea mtModArea = new MtModArea();
            mtModArea.setTenantId(tenantId);
            mtModArea.setAreaId(parentOrganizationId);
            mtModArea = this.mtModAreaMapper.selectOne(mtModArea);
            if (mtModArea == null || !"Y".equals(mtModArea.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0005",
                                "MODELING", "parentOrganizationId",
                                "【API:prodLineOrganizationRelVerify】"));
            }
        } else {
            MtModSite mtModSite = new MtModSite();
            mtModSite.setTenantId(tenantId);
            mtModSite.setSiteId(parentOrganizationId);
            mtModSite = this.mtModSiteMapper.selectOne(mtModSite);
            if (mtModSite == null || !"Y".equals(mtModSite.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0005",
                                "MODELING", "parentOrganizationId",
                                "【API:prodLineOrganizationRelVerify】"));
            }
        }

        // 查询当前产线所有父组织
        MtModOrganizationRel mtModOrganizationRel = new MtModOrganizationRel();
        mtModOrganizationRel.setOrganizationId(prodLineId);
        mtModOrganizationRel.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);
        List<MtModOrganizationRel> prodLineAllOrgRelList = this.mtModOrganizationRelMapper.select(mtModOrganizationRel);
        if (CollectionUtils.isEmpty(prodLineAllOrgRelList)) {
            return "Y";
        }

        // 1. 同一个生产线，父层唯一，不能重复定义，否则报错
        Optional<MtModOrganizationRel> any = prodLineAllOrgRelList.stream()
                .filter(t -> t.getTopSiteId().equals(topSiteId)
                        && t.getParentOrganizationType().equals(parentOrganizationType)
                        && t.getParentOrganizationId().equals(parentOrganizationId))
                .findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MODELING_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0006", "MODELING", "【API:prodLineOrganizationRelVerify】"));
        }

        // 2. 同一个生产线，不能既存在站点下也存在区域下，否则报错
        // 2.1 筛选该生产线直接父层关系
        List<String> parentOrganizationTypeList = prodLineAllOrgRelList.stream()
                .filter(t -> t.getTopSiteId().equals(topSiteId))
                .map(MtModOrganizationRel::getParentOrganizationType).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(parentOrganizationTypeList)) {
            // 生产线的父层，要么是一个站点；要么是1个或者多个区域
            // 2.2 如果输入的当前组织的类型，跟生产线已维护关系类型不一致，违反上述限制，则报错
            String alreadyOrgType = parentOrganizationTypeList.get(0);
            if (!alreadyOrgType.equals(parentOrganizationType)) {
                throw new MtException("MT_MODELING_0049",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0049",
                                "MODELING", mtModProductionLine.getProdLineCode(),
                                mtTopModSite.getSiteCode(), "【API:prodLineOrganizationRelVerify】"));
            }
        }

        // 3. 同一个生产线，在不同站点下，站点的类型不能相同，否则报错
        List<String> topSiteIds = prodLineAllOrgRelList.stream().map(MtModOrganizationRel::getTopSiteId)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(topSiteIds)) {
            List<MtModSite> mtModSiteList = this.mtModSiteMapper.selectSites(tenantId, topSiteIds);
            if (CollectionUtils.isNotEmpty(mtModSiteList)) {
                MtModSite finalMtTopModSite = mtTopModSite;
                Optional<MtModSite> resSiteTypes = mtModSiteList.stream()
                        .filter(t -> t.getSiteType().equals(finalMtTopModSite.getSiteType())).findAny();
                if (resSiteTypes.isPresent()) {
                    String siteCode = resSiteTypes.get().getSiteCode();
                    throw new MtException("MT_MODELING_0015",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0015",
                                    "MODELING", siteCode, "【API:prodLineOrganizationRelVerify】"));
                }
            }
        }

        return "Y";
    }

    @Override
    public String workcellOrganizationRelVerify(Long tenantId, String workcellId, String topSiteId,
                                                String parentOrganizationType, String parentOrganizationId) {
        if (StringUtils.isEmpty(workcellId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "workcellId", "【API:workcellOrganizationRelVerify】"));
        }
        if (StringUtils.isEmpty(topSiteId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "topSiteId", "【API:workcellOrganizationRelVerify】"));
        }
        if (StringUtils.isEmpty(parentOrganizationType)) {
            throw new MtException("MT_MODELING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                            "parentOrganizationType", "【API:workcellOrganizationRelVerify】"));
        }
        if (StringUtils.isEmpty(parentOrganizationId)) {
            throw new MtException("MT_MODELING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                            "parentOrganizationId", "【API:workcellOrganizationRelVerify】"));
        }
        if (!MtBaseConstants.ORGANIZATION_TYPE.SITE.equals(parentOrganizationType)
                && !MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(parentOrganizationType)
                && !MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE.equals(parentOrganizationType)
                && !MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(parentOrganizationType)) {
            throw new MtException("MT_MODELING_0010",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0010", "MODELING",
                            "parentOrganizationType", "【API:workcellOrganizationRelVerify】"));
        }
        MtModWorkcell mtModWorkcell = new MtModWorkcell();
        mtModWorkcell.setTenantId(tenantId);
        mtModWorkcell.setWorkcellId(workcellId);
        mtModWorkcell = this.mtModWorkcellMapper.selectOne(mtModWorkcell);
        if (mtModWorkcell == null || !"Y".equals(mtModWorkcell.getEnableFlag())) {
            throw new MtException("MT_MODELING_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0005", "MODELING", "workcellId", "【API:workcellOrganizationRelVerify】"));
        }
        MtModSite mtTopModSite = new MtModSite();
        mtTopModSite.setTenantId(tenantId);
        mtTopModSite.setSiteId(topSiteId);
        mtTopModSite = this.mtModSiteMapper.selectOne(mtTopModSite);
        if (mtTopModSite == null || !"Y".equals(mtTopModSite.getEnableFlag())) {
            throw new MtException("MT_MODELING_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0005", "MODELING", "topSiteId", "【API:workcellOrganizationRelVerify】"));
        }

        if (MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(parentOrganizationType)) {
            MtModArea mtModArea = new MtModArea();
            mtModArea.setTenantId(tenantId);
            mtModArea.setAreaId(parentOrganizationId);
            mtModArea = this.mtModAreaMapper.selectOne(mtModArea);
            if (mtModArea == null || !"Y".equals(mtModArea.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0005",
                                "MODELING", "parentOrganizationId",
                                "【API:workcellOrganizationRelVerify】"));
            }
        } else if (MtBaseConstants.ORGANIZATION_TYPE.SITE.equals(parentOrganizationType)) {
            MtModSite mtModSite = new MtModSite();
            mtModSite.setTenantId(tenantId);
            mtModSite.setSiteId(parentOrganizationId);
            mtModSite = this.mtModSiteMapper.selectOne(mtModSite);
            if (mtModSite == null || !"Y".equals(mtModSite.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0005",
                                "MODELING", "parentOrganizationId",
                                "【API:workcellOrganizationRelVerify】"));
            }
        } else if (MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE.equals(parentOrganizationType)) {
            MtModProductionLine mtModProductionLine = new MtModProductionLine();
            mtModProductionLine.setTenantId(tenantId);
            mtModProductionLine.setProdLineId(parentOrganizationId);
            mtModProductionLine = this.mtModProductionLineMapper.selectOne(mtModProductionLine);
            if (mtModProductionLine == null || !"Y".equals(mtModProductionLine.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0005",
                                "MODELING", "parentOrganizationId",
                                "【API:workcellOrganizationRelVerify】"));
            }
        } else {
            MtModWorkcell tempModWorkcell = new MtModWorkcell();
            tempModWorkcell.setTenantId(tenantId);
            tempModWorkcell.setWorkcellId(parentOrganizationId);
            mtModWorkcell = this.mtModWorkcellMapper.selectOne(tempModWorkcell);
            if (mtModWorkcell == null || !"Y".equals(mtModWorkcell.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0005",
                                "MODELING", "parentOrganizationId",
                                "【API:workcellOrganizationRelVerify】"));
            }
            if (workcellId.equals(parentOrganizationId)) {
                throw new MtException("MT_MODELING_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MODELING_0024", "MODELING", "【API:workcellOrganizationRelVerify】"));
            }
        }

        // 查询当前Workcell所有父层组织
        MtModOrganizationRel mtModOrganizationRel = new MtModOrganizationRel();
        mtModOrganizationRel.setOrganizationId(workcellId);
        mtModOrganizationRel.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
        List<MtModOrganizationRel> workcellAllOrgRelList = this.mtModOrganizationRelMapper.select(mtModOrganizationRel);
        if (CollectionUtils.isEmpty(workcellAllOrgRelList)) {
            return "Y";
        }

        // 1. 同一个WKC，父层唯一，不能重复定义，否则报错
        Optional<MtModOrganizationRel> any = workcellAllOrgRelList.stream()
                .filter(t -> t.getTopSiteId().equals(topSiteId)
                        && t.getParentOrganizationType().equals(parentOrganizationType)
                        && t.getParentOrganizationId().equals(parentOrganizationId))
                .findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MODELING_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0006", "MODELING", "【API:prodLineOrganizationRelVerify】"));
        }

        // 3. 同一个工作单元，不能重复嵌套，否则报错
        MtModOrganizationVO2 queryVO = new MtModOrganizationVO2();
        queryVO.setTopSiteId(topSiteId);
        queryVO.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
        queryVO.setOrganizationId(parentOrganizationId);
        queryVO.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
        queryVO.setQueryType(MtBaseConstants.QUERY_TYPE.ALL);
        List<MtModOrganizationItemVO> allWorkcellParentOrgRelList = parentOrganizationRelQuery(tenantId, queryVO);
        Optional<MtModOrganizationItemVO> workcell = allWorkcellParentOrgRelList.stream()
                .filter(t -> t.getOrganizationType().equals(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL)
                        && t.getOrganizationId().equals(workcellId))
                .findAny();
        if (workcell.isPresent()) {
            throw new MtException("MT_MODELING_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0022", "MODELING", "【API:workcellOrganizationRelVerify】"));
        }

        // 4. 同一个工作单元，在不同站点下，站点的类型不能相同，否则报错
        List<String> topSiteIds = workcellAllOrgRelList.stream().map(MtModOrganizationRel::getTopSiteId)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(topSiteIds)) {
            List<MtModSite> mtModSiteList = this.mtModSiteMapper.selectSites(tenantId, topSiteIds);
            if (CollectionUtils.isNotEmpty(mtModSiteList)) {
                MtModSite finalMtTopModSite = mtTopModSite;
                Optional<MtModSite> resSiteTypes = mtModSiteList.stream()
                        .filter(t -> t.getSiteType().equals(finalMtTopModSite.getSiteType())).findAny();
                if (resSiteTypes.isPresent() && !resSiteTypes.get().getSiteId().equals(mtTopModSite.getSiteId())) {
                    String siteCode = resSiteTypes.get().getSiteCode();
                    throw new MtException("MT_MODELING_0011",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0011",
                                    "MODELING", siteCode, "【API:prodLineOrganizationRelVerify】"));
                }
            }
        }

        return "Y";
    }

    /* 下面几个函数是通用API */
    @Override
    public List<String> organizationLimitSiteQuery(Long tenantId, MtModOrganizationRelVO dto) {
        if (StringUtils.isEmpty(dto.getOrganizationId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationId", "【API:organizationLimitSiteQuery】"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationType", "【API:organizationLimitSiteQuery】"));
        }

        MtModOrganizationRel mtModOrganizationRel = new MtModOrganizationRel();
        mtModOrganizationRel.setTenantId(tenantId);
        mtModOrganizationRel.setOrganizationId(dto.getOrganizationId());
        mtModOrganizationRel.setOrganizationType(dto.getOrganizationType());
        List<MtModOrganizationRel> relList = mtModOrganizationRelMapper.select(mtModOrganizationRel);

        if (CollectionUtils.isNotEmpty(relList)) {
            List<String> topSiteIds = new ArrayList<>();
            for (MtModOrganizationRel rel : relList) {
                if (StringUtils.isNotEmpty(rel.getTopSiteId())) {
                    topSiteIds.add(rel.getTopSiteId());
                }
            }
            if (StringUtils.isNotEmpty(dto.getSiteType()) && CollectionUtils.isNotEmpty(topSiteIds)) {
                List<MtModSite> siteList = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, topSiteIds);
                List<MtModSite> resultSiteList = siteList.stream()
                        .filter(p -> dto.getSiteType().equals(p.getSiteType())).collect(Collectors.toList());
                topSiteIds.clear();
                for (MtModSite site : resultSiteList) {
                    topSiteIds.add(site.getSiteId());
                }
            }
            return topSiteIds;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<MtModOrganizationRelVO3> organizationLimitSiteBatchQuery(Long tenantId, MtModOrganizationRelVO2 dto) {
        if (CollectionUtils.isEmpty(dto.getOrganizationIdList())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationIdList", "【API:organizationLimitSiteQuery】"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_MODELING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                            "organizationType", "【API:organizationLimitSiteBatchQuery】"));
        }

        List<MtModOrganizationRel> relList = mtModOrganizationRelMapper.selectByCondition(Condition
                .builder(MtModOrganizationRel.class)
                .andWhere(Sqls.custom().andEqualTo(MtModOrganizationRel.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtModOrganizationRel.FIELD_ORGANIZATION_TYPE,
                                dto.getOrganizationType())
                        .andIn(MtModOrganizationRel.FIELD_ORGANIZATION_ID, dto.getOrganizationIdList()))
                .build());
        if (CollectionUtils.isNotEmpty(relList)) {
            if (StringUtils.isNotEmpty(dto.getSiteType())) {
                List<MtModSite> siteList = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId,
                        relList.stream().filter(t -> null != t.getTopSiteId())
                                .map(MtModOrganizationRel::getTopSiteId).collect(Collectors.toList()));
                // 获取需要筛选的站点
                List<String> siteIdList = siteList.stream().filter(t -> dto.getSiteType().equals(t.getSiteType()))
                        .map(MtModSite::getSiteId).collect(Collectors.toList());
                // 进行站点赛选
                relList = relList.stream().filter(t -> siteIdList.contains(t.getTopSiteId()))
                        .collect(Collectors.toList());
            }
            return relList.stream().map(t -> {
                MtModOrganizationRelVO3 one = new MtModOrganizationRelVO3();
                one.setOrganizationId(t.getOrganizationId());
                one.setSiteId(t.getTopSiteId());
                return one;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<MtModOrganizationItemVO> subOrganizationRelQuery(Long tenantId, MtModOrganizationVO2 dto) {
        List<MtModOrganizationVO> enterPriseVOList = new ArrayList<MtModOrganizationVO>();
        List<MtModAreaVO3> areaVOList = new ArrayList<MtModAreaVO3>();
        List<MtModSiteVO3> siteVOList = new ArrayList<MtModSiteVO3>();
        List<MtModWorkcellVO> workcellVOList = new ArrayList<MtModWorkcellVO>();
        List<MtModProductionLineVO> prodlineVOList = new ArrayList<MtModProductionLineVO>();

        if (StringUtils.isEmpty(dto.getTopSiteId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "topSiteId", "【API:subOrganizationRelQuery】"));
        }

        if (StringUtils.isEmpty(dto.getParentOrganizationType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "parentOrganizationType", "【API:subOrganizationRelQuery】"));
        }

        if (StringUtils.isEmpty(dto.getParentOrganizationId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "parentOrganizationId", "【API:subOrganizationRelQuery】"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationType", "【API:subOrganizationRelQuery】"));
        }

        MtGenType parentGenType = this.mtGenTypeRepository.getGenType(tenantId, "MODELING", "ORGANIZATION_TYPE",
                dto.getParentOrganizationType());
        if (parentGenType == null) {
            throw new MtException("MT_MODELING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0019", "MODELING", "parentOrganizationType", "【API:subOrganizationRelQuery】"));
        }

        MtGenType genType = this.mtGenTypeRepository.getGenType(tenantId, "MODELING", "ORGANIZATION_TYPE",
                dto.getOrganizationType());
        if (genType == null) {
            throw new MtException("MT_MODELING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0019", "MODELING", "organizationType", "【API:subOrganizationRelQuery】"));
        }

        List<String> rightQueryTypes = Arrays.asList(MtBaseConstants.QUERY_TYPE.TOP, MtBaseConstants.QUERY_TYPE.BOTTOM,
                MtBaseConstants.QUERY_TYPE.ALL);

        if (StringUtils.isNotEmpty(dto.getQueryType()) && !rightQueryTypes.contains(dto.getQueryType())) {
            throw new MtException("MT_MODELING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0002", "MODELING", dto.getQueryType(), "【API:subOrganizationRelQuery】"));
        }

        // 5.如果[parameter4]为AREA或WORKCELL、且[parameter5]为空，则报错：
        if ((MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(dto.getOrganizationType())
                || MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(dto.getOrganizationType()))
                && StringUtils.isEmpty(dto.getQueryType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "queryType", "【API:subOrganizationRelQuery】"));
        }

        /*
         * 遍历树结构去获取数据 只有区域和WKC存在自己嵌套自己的情况，需要考录传入参数queryType
         */
        String queryType = MtBaseConstants.QUERY_TYPE.ALL;
        if (MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(dto.getOrganizationType())
                || MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(dto.getOrganizationType())) {
            queryType = dto.getQueryType();
        }

        MtModOrganizationRel rel = new MtModOrganizationRel();
        rel.setTenantId(tenantId);

        // 当通过企业找站点的时候，不限制顶层站点
        if (!MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE.equals(dto.getParentOrganizationType())
                && MtBaseConstants.ORGANIZATION_TYPE.SITE.equals(dto.getOrganizationType())) {
            rel.setTopSiteId(dto.getTopSiteId());
        }
        List<MtModOrganizationRel> allRelList = mtModOrganizationRelMapper.select(rel);

        switch (dto.getParentOrganizationType()) {
            case MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE:
                MtModOrganizationVO enterPriseVO = getModEnterPriseSub(dto.getParentOrganizationId(),
                        dto.getTopSiteId(), "", null, queryType, dto.getOrganizationType(), allRelList);
                if (enterPriseVO != null) {
                    areaVOList = enterPriseVO.getAreaVOList();
                    siteVOList = enterPriseVO.getSiteVOList();
                    workcellVOList = enterPriseVO.getWorkcellVOList();
                    prodlineVOList = enterPriseVO.getProdlineVOList();
                    enterPriseVOList = enterPriseVO.getEnterPriseVOList();
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.SITE:
                MtModSiteVO3 siteVO = getModSiteSub(dto.getParentOrganizationId(), dto.getTopSiteId(), 1, "", null,
                        queryType, dto.getOrganizationType(), allRelList);
                areaVOList = siteVO.getAreaVOList();
                workcellVOList = siteVO.getWorkcellVOList();
                prodlineVOList = siteVO.getProdlineVOList();
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.AREA:
                MtModAreaVO3 areaVo = getModAreaSub(dto.getParentOrganizationId(), dto.getTopSiteId(), 1, "", null,
                        queryType, dto.getOrganizationType(), allRelList);
                if (areaVo != null) {
                    areaVOList = areaVo.getAreaVOList();
                    workcellVOList = areaVo.getWorkcellVOList();
                    prodlineVOList = areaVo.getProdlineVOList();
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE:
                MtModProductionLineVO prodlineVo = getModProdlineSub(dto.getParentOrganizationId(), dto.getTopSiteId(),
                        1, "", null, queryType, dto.getOrganizationType(), allRelList);
                if (prodlineVo != null) {
                    workcellVOList = prodlineVo.getWorkcellVOList();
                    prodlineVOList = prodlineVo.getProdlineVOList();
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                MtModWorkcellVO workcellVo = getModWorkcellSub(dto.getParentOrganizationId(), dto.getTopSiteId(), 1, "",
                        null, queryType, dto.getOrganizationType(), allRelList);
                if (workcellVo != null) {
                    workcellVOList = workcellVo.getWorkcellVOList();
                }
                break;
            default:
                break;
        }

        /*
         * 整理返回数据 API只查询有效的数据
         */
        List<MtModOrganizationItemVO> itemVOList = new ArrayList<MtModOrganizationItemVO>();
        switch (dto.getOrganizationType()) {
            case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                if (CollectionUtils.isNotEmpty(workcellVOList)) {
                    // 获取对应wkc信息，需要筛选有效性为Y的数据
                    List<String> wkcIds =
                            workcellVOList.stream().map(MtModWorkcellVO::getMyId).collect(Collectors.toList());
                    List<MtModWorkcell> mtModWorkcellList = mtModWorkcellMapper.selectByIdsCustom(tenantId, wkcIds);

                    if (CollectionUtils.isNotEmpty(mtModWorkcellList)) {
                        // 转换成map数据
                        Map<String, MtModWorkcell> mtModWorkcellMap = mtModWorkcellList.stream()
                                .collect(Collectors.toMap(t -> t.getWorkcellId(), t -> t));

                        for (MtModWorkcellVO workcellVO : workcellVOList) {
                            MtModWorkcell modWorkcell = mtModWorkcellMap.get(workcellVO.getMyId());
                            if (modWorkcell != null && "Y".equals(modWorkcell.getEnableFlag())) {
                                MtModOrganizationItemVO itemVO = new MtModOrganizationItemVO();
                                itemVO.setPro(workcellVO.getPro());
                                itemVO.setRelId(workcellVO.getRelId());
                                itemVO.setOrganizationId(workcellVO.getMyId());
                                itemVO.setOrganizationType(workcellVO.getType());
                                itemVO.setSequence(workcellVO.getSequence());
                                itemVOList.add(itemVO);
                            }
                        }
                    }
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.SITE:
                if (CollectionUtils.isNotEmpty(siteVOList)) {
                    // 获取对应site信息，筛选有效性为Y的数据
                    List<String> siteIds = siteVOList.stream().map(MtModSiteVO3::getMyId).collect(Collectors.toList());
                    List<MtModSite> mtModSiteList = mtModSiteMapper.selectByIdsCustom(tenantId, siteIds);

                    if (CollectionUtils.isNotEmpty(mtModSiteList)) {
                        // 转成map数据
                        Map<String, MtModSite> mtModSiteMap =
                                mtModSiteList.stream().collect(Collectors.toMap(t -> t.getSiteId(), t -> t));

                        for (MtModSiteVO3 modSiteVO3 : siteVOList) {
                            MtModSite mtModSite = mtModSiteMap.get(modSiteVO3.getMyId());

                            if (mtModSite != null && "Y".equals(mtModSite.getEnableFlag())) {
                                MtModOrganizationItemVO itemVO = new MtModOrganizationItemVO();
                                itemVO.setPro(modSiteVO3.getPro());
                                itemVO.setRelId(modSiteVO3.getRelId());
                                itemVO.setOrganizationId(modSiteVO3.getMyId());
                                itemVO.setOrganizationType(modSiteVO3.getType());
                                itemVO.setSequence(modSiteVO3.getSequence());
                                itemVOList.add(itemVO);
                            }
                        }
                    }
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE:
                if (CollectionUtils.isNotEmpty(enterPriseVOList)) {
                    // 获取对应enterPrise信息，筛选有效性为Y的数据
                    List<String> enterPriseIds = enterPriseVOList.stream().map(MtModOrganizationVO::getMyId)
                            .collect(Collectors.toList());
                    List<MtModEnterprise> mtModEnterpriseList =
                            mtModEnterpriseMapper.selectByIdsCustom(tenantId, enterPriseIds);

                    if (CollectionUtils.isNotEmpty(mtModEnterpriseList)) {
                        // 转成map数据
                        Map<String, MtModEnterprise> mtModEnterpriseMap = mtModEnterpriseList.stream()
                                .collect(Collectors.toMap(t -> t.getEnterpriseId(), t -> t));

                        for (MtModOrganizationVO enterPriseVO : enterPriseVOList) {
                            MtModEnterprise mtModEnterprise = mtModEnterpriseMap.get(enterPriseVO.getMyId());

                            if (mtModEnterprise != null && "Y".equals(mtModEnterprise.getEnableFlag())) {
                                MtModOrganizationItemVO itemVO = new MtModOrganizationItemVO();
                                itemVO.setPro(enterPriseVO.getPro());
                                itemVO.setRelId(enterPriseVO.getRelId());
                                itemVO.setOrganizationId(enterPriseVO.getMyId());
                                itemVO.setOrganizationType(enterPriseVO.getType());
                                itemVO.setSequence(enterPriseVO.getSequence());
                                itemVOList.add(itemVO);
                            }
                        }
                    }
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.AREA:
                if (CollectionUtils.isNotEmpty(areaVOList)) {
                    // 获取对应 area 信息，筛选有效性为Y的数据
                    List<String> areaIds = areaVOList.stream().map(MtModAreaVO3::getMyId).collect(Collectors.toList());
                    List<MtModArea> mtModAreaList = mtModAreaMapper.selectByIdsCustom(tenantId, areaIds);

                    if (CollectionUtils.isNotEmpty(mtModAreaList)) {
                        // 转成map数据
                        Map<String, MtModArea> mtModAreaMap =
                                mtModAreaList.stream().collect(Collectors.toMap(t -> t.getAreaId(), t -> t));
                        for (MtModAreaVO3 modAreaVO3 : areaVOList) {
                            MtModArea mtModArea = mtModAreaMap.get(modAreaVO3.getMyId());

                            if (mtModArea != null && "Y".equals(mtModArea.getEnableFlag())) {
                                MtModOrganizationItemVO itemVO = new MtModOrganizationItemVO();
                                itemVO.setPro(modAreaVO3.getPro());
                                itemVO.setRelId(modAreaVO3.getRelId());
                                itemVO.setOrganizationId(modAreaVO3.getMyId());
                                itemVO.setOrganizationType(modAreaVO3.getType());
                                itemVO.setSequence(modAreaVO3.getSequence());
                                itemVOList.add(itemVO);
                            }
                        }
                    }
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE:
                if (CollectionUtils.isNotEmpty(prodlineVOList)) {
                    // 获取对应 prodLine 信息，筛选有效性为Y的数据
                    List<String> prodLineIds = prodlineVOList.stream().map(MtModProductionLineVO::getMyId)
                            .collect(Collectors.toList());
                    List<MtModProductionLine> mtModProductionLineList =
                            mtModProductionLineMapper.selectByIdsCustom(tenantId, prodLineIds);

                    if (CollectionUtils.isNotEmpty(mtModProductionLineList)) {
                        // 转成map数据
                        Map<String, MtModProductionLine> mtModProductionLineMap = mtModProductionLineList.stream()
                                .collect(Collectors.toMap(t -> t.getProdLineId(), t -> t));
                        for (MtModProductionLineVO productionLineVO : prodlineVOList) {
                            MtModProductionLine mtModProductionLine =
                                    mtModProductionLineMap.get(productionLineVO.getMyId());

                            if (mtModProductionLine != null && "Y".equals(mtModProductionLine.getEnableFlag())) {
                                MtModOrganizationItemVO itemVO = new MtModOrganizationItemVO();
                                itemVO.setPro(productionLineVO.getPro());
                                itemVO.setRelId(productionLineVO.getRelId());
                                itemVO.setOrganizationId(productionLineVO.getMyId());
                                itemVO.setOrganizationType(productionLineVO.getType());
                                itemVO.setSequence(productionLineVO.getSequence());
                                itemVOList.add(itemVO);
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }

        // 需要遍历一下晒除掉自己
        if (itemVOList.size() <= 0) {
            return itemVOList;
        }

        List<MtModOrganizationItemVO> resultItem = new ArrayList<MtModOrganizationItemVO>();
        itemVOList.sort(Comparator.comparing(MtModOrganizationItemVO::getPro));

        resultItem = removeDuplicate(itemVOList);
        return resultItem;
    }

    @Override
    public List<MtModOrganizationItemVO> parentOrganizationRelQuery(Long tenantId, MtModOrganizationVO2 rel) {
        // 1. 校验[parameter1]至 [parameter4]是否存在为空，如果为空，
        if (StringUtils.isEmpty(rel.getTopSiteId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "topSiteId", "【API:parentOrganizationRelQuery】"));
        }

        if (StringUtils.isEmpty(rel.getParentOrganizationType())) {
            throw new MtException("MT_MODELING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                            "parentOrganizationType", "【API:parentOrganizationRelQuery】"));
        }

        if (StringUtils.isEmpty(rel.getOrganizationId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationId", "【API:parentOrganizationRelQuery】"));
        }
        if (StringUtils.isEmpty(rel.getOrganizationType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationType", "【API:parentOrganizationRelQuery】"));
        }

        // 校验在GEN_TYPE表中是否存在
        MtGenType parentGenType = this.mtGenTypeRepository.getGenType(tenantId, "MODELING", "ORGANIZATION_TYPE",
                rel.getParentOrganizationType());
        if (parentGenType == null) {
            throw new MtException("MT_MODELING_0019",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0019", "MODELING",
                            "parentOrganizationType", "【API:parentOrganizationRelQuery】"));
        }

        MtGenType genType = this.mtGenTypeRepository.getGenType(tenantId, "MODELING", "ORGANIZATION_TYPE",
                rel.getOrganizationType());
        if (genType == null) {
            throw new MtException("MT_MODELING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0019", "MODELING", "organizationType", "【API:parentOrganizationRelQuery】"));
        }

        // 4.[parameter5] 仅允许为以下三种类型：
        if (StringUtils.isNotEmpty(rel.getQueryType()) && !MtBaseConstants.QUERY_TYPE.TOP.equals(rel.getQueryType())
                && !MtBaseConstants.QUERY_TYPE.BOTTOM.equals(rel.getQueryType())
                && !MtBaseConstants.QUERY_TYPE.ALL.equals(rel.getQueryType())) {
            throw new MtException("MT_MODELING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0002", "MODELING", rel.getQueryType(), "【API:parentOrganizationRelQuery】"));
        }
        // 5. 如果[parameter4]为AREA或WORKCELL、且[parameter5]为空，报错
        if ((MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(rel.getParentOrganizationType())
                || MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(rel.getParentOrganizationType()))
                && StringUtils.isEmpty(rel.getQueryType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "queryType", "【API:parentOrganizationRelQuery】"));
        }

        String targetType = rel.getParentOrganizationType();
        List<MtModOrganizationItemVO> itemVOList = new ArrayList<MtModOrganizationItemVO>();
        List<MtModOrganizationItemVO> resultList = new ArrayList<MtModOrganizationItemVO>();

        List<MtModOrganizationParentVO> parentVOList =
                findParent(tenantId, rel.getOrganizationId(), rel.getOrganizationType(), rel.getTopSiteId(), 1);

        // 遍历出需要的节点ID
        for (MtModOrganizationParentVO mtModOrganizationParentVO : parentVOList) {
            if (mtModOrganizationParentVO.getOrganizationType().equals(targetType)) {
                MtModOrganizationItemVO t = new MtModOrganizationItemVO();
                t.setPro(mtModOrganizationParentVO.getPro());
                t.setOrganizationId(mtModOrganizationParentVO.getOrganizationId());
                t.setSequence(mtModOrganizationParentVO.getSequence());
                t.setOrganizationType(mtModOrganizationParentVO.getOrganizationType());
                itemVOList.add(t);
            }
        }

        if (itemVOList.size() <= 0) {
            return itemVOList;
        }
        itemVOList.sort(Comparator.comparing(MtModOrganizationItemVO::getPro));
        int maxpro = itemVOList.get(itemVOList.size() - 1).getPro();
        int minpro = itemVOList.get(0).getPro();

        if (MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(rel.getParentOrganizationType())
                || MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(rel.getParentOrganizationType())) {

            if (MtBaseConstants.QUERY_TYPE.BOTTOM.equals(rel.getQueryType())) {
                for (MtModOrganizationItemVO mtModOrganizationItemVO : itemVOList) {
                    if (mtModOrganizationItemVO.getPro() == minpro) {
                        resultList.add(mtModOrganizationItemVO);
                    }
                }
            } else if (MtBaseConstants.QUERY_TYPE.TOP.equals(rel.getQueryType())) {
                for (MtModOrganizationItemVO mtModOrganizationItemVO : itemVOList) {
                    if (mtModOrganizationItemVO.getPro() == maxpro) {
                        resultList.add(mtModOrganizationItemVO);
                    }
                }
            } else {
                resultList = itemVOList;
            }
        } else {
            resultList = itemVOList;
        }
        return removeDuplicate(resultList);
    }


    @Override
    public List<MtModOrganizationVO5> parentOrganizationRelBatchQuery(Long tenantId, MtModOrganizationVO4 dto) {

        List<MtModOrganizationVO5> resultList = new ArrayList<>();
        // 1.参数合规性校验
        if (StringUtils.isEmpty(dto.getTopSiteId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "topSiteId", "【API:parentOrganizationRelBatchQuery】"));
        }

        if (StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_MODELING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                            "organizationType", "【API:parentOrganizationRelBatchQuery】"));
        }

        if (CollectionUtils.isEmpty(dto.getOrganizationIdList())) {
            throw new MtException("MT_MODELING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                            "organizationIdList", "【API:parentOrganizationRelBatchQuery】"));
        }

        if (StringUtils.isEmpty(dto.getParentOrganizationType())) {
            throw new MtException("MT_MODELING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                            "parentOrganizationType", "【API:parentOrganizationRelBatchQuery】"));
        }

        // 校验organizationType、parentOrganizationType是否为GEN_TYPE表中，TYPE_CODE=ORGANIZATION_TYPE 的数据

        MtGenType genType = this.mtGenTypeRepository.getGenType(tenantId, "MODELING", "ORGANIZATION_TYPE",
                dto.getOrganizationType());
        if (genType == null) {
            throw new MtException("MT_MODELING_0019",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0019", "MODELING",
                            "organizationType", "【API:parentOrganizationRelBatchQuery】"));
        }

        MtGenType parentGenType = this.mtGenTypeRepository.getGenType(tenantId, "MODELING", "ORGANIZATION_TYPE",
                dto.getParentOrganizationType());
        if (parentGenType == null) {
            throw new MtException("MT_MODELING_0019",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0019", "MODELING",
                            "parentOrganizationType", "【API:parentOrganizationRelBatchQuery】"));
        }

        // 校验queryType仅允许为以下三种类型
        if (StringUtils.isNotEmpty(dto.getQueryType()) && !MtBaseConstants.QUERY_TYPE.TOP.equals(dto.getQueryType())
                && !MtBaseConstants.QUERY_TYPE.BOTTOM.equals(dto.getQueryType())
                && !MtBaseConstants.QUERY_TYPE.ALL.equals(dto.getQueryType())) {
            throw new MtException("MT_MODELING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0002", "MODELING", "queryType", "【API:parentOrganizationRelBatchQuery】"));
        }

        // 如果[parameter4]为AREA或WORKCELL、且[parameter5]为空
        if ((MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(dto.getParentOrganizationType())
                || MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(dto.getParentOrganizationType()))
                && StringUtils.isEmpty(dto.getQueryType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "queryType", "【API:parentOrganizationRelBatchQuery】"));
        }

        // 2查询父层组织
        String targetType = dto.getParentOrganizationType();

        for (String organizationId : dto.getOrganizationIdList()) {
            List<MtModOrganizationItemVO> itemVOList = new ArrayList<MtModOrganizationItemVO>();

            MtModOrganizationVO5 result = new MtModOrganizationVO5();
            result.setOrganizationId(organizationId);
            List<MtModOrganizationVO6> list = new ArrayList<>();

            List<MtModOrganizationParentVO> parentVOList =
                    findParent(tenantId, organizationId, dto.getOrganizationType(), dto.getTopSiteId(), 1);

            // 遍历出需要的节点ID
            for (MtModOrganizationParentVO mtModOrganizationParentVO : parentVOList) {
                if (mtModOrganizationParentVO.getOrganizationType().equals(targetType)) {
                    MtModOrganizationItemVO t = new MtModOrganizationItemVO();
                    t.setPro(mtModOrganizationParentVO.getPro());
                    t.setOrganizationId(mtModOrganizationParentVO.getOrganizationId());
                    t.setSequence(mtModOrganizationParentVO.getSequence());
                    t.setOrganizationType(mtModOrganizationParentVO.getOrganizationType());
                    itemVOList.add(t);
                }
            }


            if (CollectionUtils.isNotEmpty(itemVOList)) {
                itemVOList.sort(Comparator.comparing(MtModOrganizationItemVO::getPro));
                int maxpro = itemVOList.get(itemVOList.size() - 1).getPro();
                int minpro = itemVOList.get(0).getPro();

                if (MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(dto.getParentOrganizationType())
                        || MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(dto.getParentOrganizationType())) {

                    if (MtBaseConstants.QUERY_TYPE.BOTTOM.equals(dto.getQueryType())) {
                        for (MtModOrganizationItemVO mtModOrganizationItemVO : itemVOList) {
                            if (mtModOrganizationItemVO.getPro() == minpro) {
                                MtModOrganizationVO6 vo6 = new MtModOrganizationVO6();
                                vo6.setParentOrganizationId(mtModOrganizationItemVO.getOrganizationId());
                                vo6.setSequence(mtModOrganizationItemVO.getSequence());
                                list.add(vo6);
                            }
                        }
                    } else if (MtBaseConstants.QUERY_TYPE.TOP.equals(dto.getQueryType())) {
                        for (MtModOrganizationItemVO mtModOrganizationItemVO : itemVOList) {
                            if (mtModOrganizationItemVO.getPro() == maxpro) {
                                MtModOrganizationVO6 vo6 = new MtModOrganizationVO6();
                                vo6.setParentOrganizationId(mtModOrganizationItemVO.getOrganizationId());
                                vo6.setSequence(mtModOrganizationItemVO.getSequence());
                                list.add(vo6);
                            }
                        }
                    } else {
                        list = itemVOList.stream().map(t -> {
                            MtModOrganizationVO6 vo6 = new MtModOrganizationVO6();
                            vo6.setParentOrganizationId(t.getOrganizationId());
                            vo6.setSequence(t.getSequence());
                            return vo6;
                        }).collect(Collectors.toList());
                    }
                } else {
                    list = itemVOList.stream().map(t -> {
                        MtModOrganizationVO6 vo6 = new MtModOrganizationVO6();
                        vo6.setParentOrganizationId(t.getOrganizationId());
                        vo6.setSequence(t.getSequence());
                        return vo6;
                    }).collect(Collectors.toList());
                }

            }
            result.setParentOrganizationList(list);
            resultList.add(result);
        }

        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void organizationRelDelete(Long tenantId, MtModOrganizationRel rel) {
        // 删除REL表的数据
        List<MtModOrganizationVO> enterPriseVOList = new ArrayList<MtModOrganizationVO>();
        List<MtModAreaVO3> areaVOList = new ArrayList<MtModAreaVO3>();
        List<MtModSiteVO3> siteVOList = new ArrayList<MtModSiteVO3>();
        List<MtModWorkcellVO> workcellVOList = new ArrayList<MtModWorkcellVO>();
        List<MtModProductionLineVO> prodlineVOList = new ArrayList<MtModProductionLineVO>();
        List<String> deletelist = new ArrayList<String>();

        if (StringUtils.isEmpty(rel.getTopSiteId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "topSiteId", "【API:organizationRelDelete】"));
        }
        if (StringUtils.isEmpty(rel.getParentOrganizationType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "parentOrganizationType", "【API:organizationRelDelete】"));
        }
        if (StringUtils.isEmpty(rel.getParentOrganizationId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "parentOrganizationId", "【API:organizationRelDelete】"));
        }
        if (StringUtils.isEmpty(rel.getOrganizationType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationType", "【API:organizationRelDelete】"));
        }
        if (StringUtils.isEmpty(rel.getOrganizationId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationId", "【API:organizationRelDelete】"));
        }

        MtGenType t1 = this.mtGenTypeRepository.getGenType(tenantId, "MODELING", "ORGANIZATION_TYPE",
                rel.getParentOrganizationType());
        MtGenType t2 = this.mtGenTypeRepository.getGenType(tenantId, "MODELING", "ORGANIZATION_TYPE",
                rel.getOrganizationType());

        if (t1 == null) {
            throw new MtException("MT_MODELING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0019", "MODELING", "parentOrganizationType", "【API:organizationRelDelete】"));
        }

        if (t2 == null) {
            throw new MtException("MT_MODELING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0019", "MODELING", "organizationType", "【API:organizationRelDelete】"));
        }
        rel.setTenantId(tenantId);
        rel = mtModOrganizationRelMapper.selectOne(rel);

        // 遍历树结构去获取数据
        String queryType = MtBaseConstants.QUERY_TYPE.ALL;
        try {
            switch (rel.getOrganizationType()) {
                case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                    MtModWorkcellVO workcellVo = getModelWorkcell(tenantId, rel.getOrganizationId(), rel.getTopSiteId(),
                            1, rel.getOrganizationRelId(), rel.getSequence(), false, false, queryType,
                            rel.getParentOrganizationId(), rel.getParentOrganizationType());
                    if (workcellVo != null) {
                        workcellVOList = workcellVo.getWorkcellVOList();
                    }
                    break;
                case MtBaseConstants.ORGANIZATION_TYPE.SITE:
                    MtModSiteVO3 siteVO = getSite(tenantId, rel.getOrganizationId(), 1, rel.getOrganizationRelId(),
                            rel.getSequence(), false, false, queryType, rel.getParentOrganizationId(),
                            rel.getParentOrganizationType());
                    if (siteVO != null) {
                        siteVOList = siteVO.getSiteVOList();
                        areaVOList = siteVO.getAreaVOList();
                        workcellVOList = siteVO.getWorkcellVOList();
                        prodlineVOList = siteVO.getProdlineVOList();
                    }
                    break;
                case MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE:
                    MtModOrganizationVO enterPriseVO = getEnterPrise(tenantId, rel.getOrganizationId(),
                            rel.getOrganizationRelId(), rel.getSequence(), false, false, queryType);
                    if (enterPriseVO != null) {
                        areaVOList = enterPriseVO.getAreaVOList();
                        siteVOList = enterPriseVO.getSiteVOList();
                        workcellVOList = enterPriseVO.getWorkcellVOList();
                        prodlineVOList = enterPriseVO.getProdlineVOList();
                        enterPriseVOList = enterPriseVO.getEnterPriseVOList();
                    }
                    break;
                case MtBaseConstants.ORGANIZATION_TYPE.AREA:
                    MtModAreaVO3 areaVo = getArea(tenantId, rel.getOrganizationId(), rel.getTopSiteId(), 1,
                            rel.getOrganizationRelId(), rel.getSequence(), false, false, queryType,
                            rel.getParentOrganizationId(), rel.getParentOrganizationType());
                    if (areaVo != null) {
                        areaVOList = areaVo.getAreaVOList();
                        workcellVOList = areaVo.getWorkcellVOList();
                        prodlineVOList = areaVo.getProdlineVOList();
                    }
                    break;
                case MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE:
                    MtModProductionLineVO prodlineVo = getModelProdline(tenantId, rel.getOrganizationId(),
                            rel.getTopSiteId(), 1, rel.getOrganizationRelId(), rel.getSequence(), false, false,
                            queryType, rel.getParentOrganizationId(), rel.getParentOrganizationType());

                    if (prodlineVo != null) {
                        workcellVOList = prodlineVo.getWorkcellVOList();
                        prodlineVOList = prodlineVo.getProdlineVOList();
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (MtModOrganizationVO mtModOrganizationVO : enterPriseVOList) {
            deletelist.add(mtModOrganizationVO.getRelId());
        }
        for (MtModAreaVO3 mtModAreaVO3 : areaVOList) {
            deletelist.add(mtModAreaVO3.getRelId());
        }
        for (MtModSiteVO3 mtModSiteVO3 : siteVOList) {
            deletelist.add(mtModSiteVO3.getRelId());
        }
        for (MtModProductionLineVO mtModProductionLineVO : prodlineVOList) {
            deletelist.add(mtModProductionLineVO.getRelId());
        }
        for (MtModWorkcellVO mtModWorkcellVO : workcellVOList) {
            deletelist.add(mtModWorkcellVO.getRelId());
        }
        if (deletelist.size() > 0) {
            mtModOrganizationRelMapper.deleteBatch(tenantId, deletelist);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void organizationRelCopy(Long tenantId, MtModOrganizationCopyVO dto) {
        List<MtModOrganizationVO> enterPriseVOList = new ArrayList<MtModOrganizationVO>();
        List<MtModAreaVO3> areaVOList = new ArrayList<MtModAreaVO3>();
        List<MtModSiteVO3> siteVOList = new ArrayList<MtModSiteVO3>();
        List<MtModWorkcellVO> workcellVOList = new ArrayList<MtModWorkcellVO>();
        List<MtModProductionLineVO> prodlineVOList = new ArrayList<MtModProductionLineVO>();

        if (StringUtils.isEmpty(dto.getTopSiteId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "topSiteId", "【API:organizationRelCopy】"));
        }
        if (StringUtils.isEmpty(dto.getParentOrganizationType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "parentOrganizationType", "【API:organizationRelCopy】"));
        }
        if (StringUtils.isEmpty(dto.getParentOrganizationId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "parentOrganizationId", "【API:organizationRelCopy】"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationType", "【API:organizationRelCopy】"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "organizationId", "【API:organizationRelCopy】"));
        }
        if (StringUtils.isEmpty(dto.getTargetSiteId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "targetSiteId", "【API:organizationRelCopy】"));
        }
        if (StringUtils.isEmpty(dto.getTargetOrganizationType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "targetOrganizationType", "【API:organizationRelCopy】"));
        }
        if (StringUtils.isEmpty(dto.getTargetOrganizationId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "targetOrganizationId", "【API:organizationRelCopy】"));
        }

        // 获取组织类型数据
        List<MtGenType> mtGenTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "ORGANIZATION_TYPE");
        if (CollectionUtils.isEmpty(mtGenTypes)) {
            throw new MtException("MT_MODELING_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0018", "MODELING"));
        }

        Optional<MtGenType> any = mtGenTypes.stream()
                .filter(t -> t.getTypeCode().equals(dto.getParentOrganizationType())).findAny();
        if (!any.isPresent()) {
            throw new MtException("MT_MODELING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0019", "MODELING", "parentOrganizationType"));
        }
        any = mtGenTypes.stream().filter(t -> t.getTypeCode().equals(dto.getOrganizationType())).findAny();
        if (!any.isPresent()) {
            throw new MtException("MT_MODELING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0019", "MODELING", "parentOrganizationType"));
        }
        any = mtGenTypes.stream().filter(t -> t.getTypeCode().equals(dto.getTargetOrganizationType())).findAny();
        if (!any.isPresent()) {
            throw new MtException("MT_MODELING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0019", "MODELING", "parentOrganizationType"));
        }

        MtModOrganizationRel source = new MtModOrganizationRel();
        source.setTenantId(tenantId);
        source.setParentOrganizationId(dto.getParentOrganizationId());
        source.setParentOrganizationType(dto.getParentOrganizationType());
        source.setTopSiteId(dto.getTopSiteId());
        source.setOrganizationId(dto.getOrganizationId());
        source.setOrganizationType(dto.getOrganizationType());
        source = mtModOrganizationRelMapper.selectOne(source);

        if (source == null) {
            throw new MtException("MT_MODELING_0052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0052", "MODELING", "【API:organizationRelCopy】"));
        }
        String queryType = MtBaseConstants.QUERY_TYPE.ALL;
        // 遍历树结构去获取数据

        switch (dto.getOrganizationType()) {
            case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                MtModWorkcellVO workcellVo = getModelWorkcell(tenantId, dto.getOrganizationId(), dto.getTopSiteId(), 1,
                        source.getOrganizationRelId(), source.getSequence(), false, false, queryType,
                        source.getParentOrganizationId(), source.getParentOrganizationType());
                if (workcellVo != null) {
                    workcellVOList = workcellVo.getWorkcellVOList();
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.SITE:
                MtModSiteVO3 siteVO = getSite(tenantId, dto.getOrganizationId(), 1, source.getOrganizationRelId(),
                        source.getSequence(), false, false, queryType, source.getParentOrganizationId(),
                        source.getParentOrganizationType());
                if (siteVO != null) {
                    siteVOList = siteVO.getSiteVOList();
                    areaVOList = siteVO.getAreaVOList();
                    workcellVOList = siteVO.getWorkcellVOList();
                    prodlineVOList = siteVO.getProdlineVOList();
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE:
                MtModOrganizationVO enterPriseVO = getEnterPrise(tenantId, dto.getOrganizationId(),
                        source.getOrganizationRelId(), source.getSequence(), false, false, queryType);

                if (enterPriseVO != null) {
                    areaVOList = enterPriseVO.getAreaVOList();
                    siteVOList = enterPriseVO.getSiteVOList();
                    workcellVOList = enterPriseVO.getWorkcellVOList();
                    prodlineVOList = enterPriseVO.getProdlineVOList();
                    enterPriseVOList = enterPriseVO.getEnterPriseVOList();
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.AREA:
                MtModAreaVO3 areaVO = getArea(tenantId, dto.getOrganizationId(), dto.getTopSiteId(), 1,
                        source.getOrganizationRelId(), source.getSequence(), false, false, queryType,
                        source.getParentOrganizationId(), source.getParentOrganizationType());
                if (areaVO != null) {
                    areaVOList = areaVO.getAreaVOList();
                    workcellVOList = areaVO.getWorkcellVOList();
                    prodlineVOList = areaVO.getProdlineVOList();
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE:
                MtModProductionLineVO ProdlineVo = getModelProdline(tenantId, dto.getOrganizationId(),
                        dto.getTopSiteId(), 1, source.getOrganizationRelId(), source.getSequence(), false,
                        false, queryType, source.getParentOrganizationId(), source.getParentOrganizationType());
                if (ProdlineVo != null) {
                    workcellVOList = ProdlineVo.getWorkcellVOList();
                    prodlineVOList = ProdlineVo.getProdlineVOList();
                }
                break;
            default:
                break;
        }
        queryType = "";
        List<MtModOrganizationRel> newInsert = new ArrayList<MtModOrganizationRel>();
        List<MtModOrganizationItemVO> relitem = new ArrayList<MtModOrganizationItemVO>();
        for (int i = 0; i < enterPriseVOList.size(); i++) {
            MtModOrganizationItemVO t = new MtModOrganizationItemVO();
            t.setRelId(enterPriseVOList.get(i).getRelId());
            t.setPro(enterPriseVOList.get(i).getPro());
            relitem.add(t);
        }
        for (int i = 0; i < areaVOList.size(); i++) {
            MtModOrganizationItemVO t = new MtModOrganizationItemVO();
            t.setRelId(areaVOList.get(i).getRelId());
            t.setPro(areaVOList.get(i).getPro());
            relitem.add(t);
        }
        for (int i = 0; i < siteVOList.size(); i++) {
            MtModOrganizationItemVO t = new MtModOrganizationItemVO();
            t.setRelId(siteVOList.get(i).getRelId());
            t.setPro(siteVOList.get(i).getPro());
            relitem.add(t);
        }
        for (int i = 0; i < prodlineVOList.size(); i++) {
            MtModOrganizationItemVO t = new MtModOrganizationItemVO();
            t.setRelId(prodlineVOList.get(i).getRelId());
            t.setPro(prodlineVOList.get(i).getPro());
            relitem.add(t);
        }
        for (int i = 0; i < workcellVOList.size(); i++) {
            MtModOrganizationItemVO t = new MtModOrganizationItemVO();
            t.setRelId(workcellVOList.get(i).getRelId());
            t.setPro(workcellVOList.get(i).getPro());
            relitem.add(t);
        }

        /* 需要给插入的数据按照从上到下的顺序排序 */
        relitem.sort(Comparator.comparing(MtModOrganizationItemVO::getPro));
        List<String> rellist = relitem.stream().map(MtModOrganizationItemVO::getRelId).collect(Collectors.toList());
        /* 去除重复项 */
        rellist = removeDuplicateList(rellist);
        for (int i = 0; i < rellist.size(); i++) {
            MtModOrganizationRel r = new MtModOrganizationRel();
            r.setOrganizationRelId(rellist.get(i));
            r.setTenantId(tenantId);
            r = mtModOrganizationRelMapper.selectOne(r);
            r.setTenantId(tenantId);
            r.setOrganizationRelId(null);
            r.setTopSiteId(dto.getTargetSiteId());
            if (r.getParentOrganizationType().equals(source.getParentOrganizationType())
                    && r.getParentOrganizationId().equals(source.getParentOrganizationId())) {
                r.setParentOrganizationId(dto.getTargetOrganizationId());
                r.setParentOrganizationType(dto.getTargetOrganizationType());
            }
            newInsert.add(r);
        }

        // 插入数据
        for (int i = 0; i < newInsert.size(); i++) {
            MtModOrganizationRel t = newInsert.get(i);
            switch (t.getOrganizationType()) {
                case MtBaseConstants.ORGANIZATION_TYPE.SITE:
                    self().siteOrganizationRelVerify(tenantId, t.getOrganizationId(), t.getParentOrganizationType(),
                            t.getParentOrganizationId());
                    break;
                case MtBaseConstants.ORGANIZATION_TYPE.AREA:
                    self().areaOrganizationRelVerify(tenantId, t.getOrganizationId(), t.getTopSiteId(),
                            t.getParentOrganizationType(), t.getParentOrganizationId());
                    break;
                case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                    self().workcellOrganizationRelVerify(tenantId, t.getOrganizationId(), t.getTopSiteId(),
                            t.getParentOrganizationType(), t.getParentOrganizationId());
                    break;
                case MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE:
                    self().prodLineOrganizationRelVerify(tenantId, t.getOrganizationId(), t.getTopSiteId(),
                            t.getParentOrganizationType(), t.getParentOrganizationId());
                    break;
                default:
                    break;
            }
            newInsert.get(i).setTenantId(tenantId);
            self().insertSelective(newInsert.get(i));
        }
    }

    @Override
    public MtModOrganizationChildrenVO siteLimitOrganizationTree(Long tenantId, MtModOrganizationVO2 dto) {
        if (StringUtils.isEmpty(dto.getParentOrganizationType())) {
            dto.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.SITE);
        }
        if (StringUtils.isEmpty(dto.getTopSiteId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "topSiteId", "【API:getChildrenByParent】"));
        }
        if (StringUtils.isEmpty(dto.getParentOrganizationType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "parentOrganizationType", "【API:getChildrenByParent】"));
        }
        if (StringUtils.isEmpty(dto.getParentOrganizationType())) {
            dto.setParentOrganizationType(null);
        }
        if (StringUtils.isEmpty(dto.getParentOrganizationId())) {
            dto.setParentOrganizationId(null);
        }
        if (MtBaseConstants.ORGANIZATION_TYPE.LOCATOR.equals(dto.getParentOrganizationType())) {
            MtModLocatorVO locator = getLocator(tenantId, dto.getParentOrganizationId(), false, null, null, null);
            if (locator != null) {
                return locator.getChildren();
            } else {
                return null;
            }
        }

        // 获取组织关系
        MtModOrganizationRel rel = new MtModOrganizationRel();
        rel.setTenantId(tenantId);
        rel.setTopSiteId(dto.getTopSiteId());
        rel.setParentOrganizationType(dto.getParentOrganizationType());
        rel.setParentOrganizationId(dto.getParentOrganizationId());
        List<MtModOrganizationRel> rels = mtModOrganizationRelMapper.select(rel);
        if (CollectionUtils.isEmpty(rels)) {
            rels.add(rel);
        }
        boolean allTree;
        for (MtModOrganizationRel mtModOrganizationRel : rels) {

            switch (dto.getParentOrganizationType()) {
                case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                    allTree = true;
                    MtModWorkcellVO modelWorkcell = getModelWorkcell(tenantId, dto.getParentOrganizationId(),
                            dto.getTopSiteId(), 1, mtModOrganizationRel.getOrganizationRelId(),
                            mtModOrganizationRel.getSequence(), false, allTree, null, null, null);
                    if (modelWorkcell == null) {
                        return null;
                    }
                    return modelWorkcell.getChildren();
                case MtBaseConstants.ORGANIZATION_TYPE.SITE:
                    allTree = true;
                    MtModSiteVO3 site = getSite(tenantId, dto.getTopSiteId(), 1,
                            mtModOrganizationRel.getOrganizationRelId(), mtModOrganizationRel.getSequence(),
                            false, allTree, null, null, null);
                    if (site == null) {
                        return null;
                    }
                    return site.getChildren();
                case MtBaseConstants.ORGANIZATION_TYPE.AREA:
                    allTree = true;
                    MtModAreaVO3 area = getArea(tenantId, dto.getParentOrganizationId(), dto.getTopSiteId(), 1,
                            mtModOrganizationRel.getOrganizationRelId(), mtModOrganizationRel.getSequence(),
                            false, allTree, null, null, null);
                    if (area == null) {
                        return null;
                    }
                    return area.getChildren();
                case MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE:
                    allTree = true;
                    MtModProductionLineVO modelProdline = getModelProdline(tenantId, dto.getParentOrganizationId(),
                            dto.getTopSiteId(), 1, mtModOrganizationRel.getOrganizationRelId(),
                            mtModOrganizationRel.getSequence(), false, allTree, null, null, null);
                    if (modelProdline == null) {
                        return null;
                    }
                    return modelProdline.getChildren();

                default:
                    return null;
            }
        }
        return null;
    }

    @Override
    public MtModOrganizationChildrenVO allOrganizationTree(Long tenantId, String enterpriseId) {
        MtModOrganizationVO organization = getEnterPrise(tenantId, enterpriseId, null, null, false, true, null);
        if (organization == null) {
            return new MtModOrganizationChildrenVO();
        } else {
            return organization.getChildren();
        }
    }

    @Override
    public List<MtModOrganizationSingleChildrenVO> singleOrganizationTree(Long tenantId, String topSiteId,
                                                                          String parentOrganizationType, String parentOrganizationId, String isOnhand) {
        if (StringUtils.isEmpty(parentOrganizationType)) {
            parentOrganizationType = MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE;
        }

        boolean isOnhandType = MtBaseConstants.YES.equalsIgnoreCase(isOnhand);

        switch (parentOrganizationType) {
            case MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE:
                MtModEnterprise modEnterprise = new MtModEnterprise();
                modEnterprise.setTenantId(tenantId);
                modEnterprise.setEnableFlag(MtBaseConstants.YES);
                List<MtModEnterprise> enterprises = mtModEnterpriseMapper.select(modEnterprise);
                MtModEnterprise enterprise = null;
                if (CollectionUtils.isNotEmpty(enterprises)) {
                    enterprise = enterprises.get(0);
                }

                if (enterprise != null) {
                    if (StringUtils.isNotEmpty(parentOrganizationId)) {
                        return getSingleLayerEnterPrise(tenantId, enterprise, true);
                    } else {
                        MtModOrganizationSingleChildrenVO subChildren = new MtModOrganizationSingleChildrenVO();
                        subChildren.setCode(enterprise.getEnterpriseCode());
                        subChildren.setName(enterprise.getEnterpriseName());
                        subChildren.setId(enterprise.getEnterpriseId());
                        subChildren.setType(parentOrganizationType);
                        subChildren.setText(getOrganizationDescriptionByCode(tenantId,
                                MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE));

                        // 查询子结构
                        List<String> parentOrganizationIds = getParentOrganizationIds(tenantId,
                                Arrays.asList(subChildren.getId()), parentOrganizationType, topSiteId);
                        subChildren.setChildren(this.hasChildren(parentOrganizationIds, subChildren.getId()));
                        return Arrays.asList(subChildren);
                    }
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.SITE:
                if (StringUtils.isNotEmpty(parentOrganizationId)) {
                    if (isOnhandType) {
                        MtModSite modSite = mtModSiteMapper.selectByPrimaryKey(parentOrganizationId);

                        MtModOrganizationSingleChildrenVO subChildren = new MtModOrganizationSingleChildrenVO();
                        subChildren.setTopSiteId(modSite.getSiteId());
                        subChildren.setName(modSite.getSiteName());
                        subChildren.setCode(modSite.getSiteCode());
                        subChildren.setId(modSite.getSiteId());
                        subChildren.setType(parentOrganizationType);
                        subChildren.setText(getOrganizationDescriptionByCode(tenantId,
                                MtBaseConstants.ORGANIZATION_TYPE.SITE));
                        // 查询子结构
                        List<String> parentOrganizationIds =
                                getParentOrganizationIds(tenantId, Arrays.asList(subChildren.getId()),
                                        parentOrganizationType, modSite.getSiteId());
                        subChildren.setChildren(this.hasChildren(parentOrganizationIds, subChildren.getId()));
                        return Arrays.asList(subChildren);
                    } else {
                        return this.getSingleLayerSite(tenantId, topSiteId, parentOrganizationId,
                                parentOrganizationType, true);
                    }
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.AREA:
                if (StringUtils.isNotEmpty(parentOrganizationId)) {
                    if (isOnhandType) {
                        MtModArea modArea = mtModAreaMapper.selectByPrimaryKey(parentOrganizationId);

                        MtModOrganizationSingleChildrenVO subChildren = new MtModOrganizationSingleChildrenVO();
                        subChildren.setTopSiteId(topSiteId);
                        subChildren.setCode(modArea.getAreaCode());
                        subChildren.setName(modArea.getAreaName());
                        subChildren.setId(modArea.getAreaId());
                        subChildren.setType(parentOrganizationType);
                        subChildren.setText(getOrganizationDescriptionByCode(tenantId,
                                MtBaseConstants.ORGANIZATION_TYPE.AREA));
                        // 查询子结构
                        List<String> parentOrganizationIds = getParentOrganizationIds(tenantId,
                                Arrays.asList(subChildren.getId()), parentOrganizationType, topSiteId);
                        subChildren.setChildren(this.hasChildren(parentOrganizationIds, subChildren.getId()));
                        return Arrays.asList(subChildren);
                    } else {
                        return getSingleLayerArea(tenantId, topSiteId, parentOrganizationId, parentOrganizationType,
                                true);
                    }
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE:
                if (StringUtils.isNotEmpty(parentOrganizationId)) {
                    if (isOnhandType) {
                        MtModProductionLine modProductionLine =
                                mtModProductionLineMapper.selectByPrimaryKey(parentOrganizationId);

                        MtModOrganizationSingleChildrenVO subChildren = new MtModOrganizationSingleChildrenVO();
                        subChildren.setTopSiteId(topSiteId);
                        subChildren.setCode(modProductionLine.getProdLineCode());
                        subChildren.setName(modProductionLine.getProdLineName());
                        subChildren.setId(modProductionLine.getProdLineId());
                        subChildren.setType(parentOrganizationType);
                        subChildren.setText(getOrganizationDescriptionByCode(tenantId,
                                MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE));
                        // 查询子结构
                        List<String> parentOrganizationIds = getParentOrganizationIds(tenantId,
                                Arrays.asList(subChildren.getId()), parentOrganizationType, topSiteId);
                        subChildren.setChildren(this.hasChildren(parentOrganizationIds, subChildren.getId()));
                        return Arrays.asList(subChildren);
                    } else {
                        return getSingleLayerModelProdline(tenantId, topSiteId, parentOrganizationId,
                                parentOrganizationType, true);
                    }
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                if (StringUtils.isNotEmpty(parentOrganizationId)) {
                    if (isOnhandType) {
                        MtModWorkcell modWorkcell = mtModWorkcellMapper.selectByPrimaryKey(parentOrganizationId);

                        MtModOrganizationSingleChildrenVO subChildren = new MtModOrganizationSingleChildrenVO();
                        subChildren.setTopSiteId(topSiteId);
                        subChildren.setCode(modWorkcell.getWorkcellCode());
                        subChildren.setName(modWorkcell.getWorkcellName());
                        subChildren.setId(modWorkcell.getWorkcellId());
                        subChildren.setType(parentOrganizationType);
                        subChildren.setText(getOrganizationDescriptionByCode(tenantId,
                                MtBaseConstants.ORGANIZATION_TYPE.SITE));
                        // 查询子结构
                        List<String> parentOrganizationIds = getParentOrganizationIds(tenantId,
                                Arrays.asList(subChildren.getId()), parentOrganizationType, topSiteId);
                        subChildren.setChildren(this.hasChildren(parentOrganizationIds, subChildren.getId()));
                        return Arrays.asList(subChildren);
                    } else {
                        return getSingleLayerModelWorkcell(tenantId, topSiteId, parentOrganizationId,
                                parentOrganizationType, true);
                    }
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.LOCATOR:
                if (StringUtils.isNotEmpty(parentOrganizationId)) {
                    if (isOnhandType) {
                        MtModLocator locator = mtModLocatorMapper.selectByPrimaryKey(parentOrganizationId);

                        MtModOrganizationSingleChildrenVO subChildren = new MtModOrganizationSingleChildrenVO();
                        subChildren.setTopSiteId(topSiteId);
                        subChildren.setCode(locator.getLocatorCode());
                        subChildren.setName(locator.getLocatorName());
                        subChildren.setId(locator.getLocatorId());
                        subChildren.setType(parentOrganizationType);
                        subChildren.setText(getOrganizationDescriptionByCode(tenantId,
                                MtBaseConstants.ORGANIZATION_TYPE.LOCATOR));
                        // 查询子结构
                        List<String> parentLocatorIds =
                                getParentLocatorIds(tenantId, Arrays.asList(subChildren.getId()));
                        subChildren.setChildren(this.hasChildren(parentLocatorIds, subChildren.getId()));
                        return Arrays.asList(subChildren);
                    } else {
                        return getSingleLayerLocator(tenantId, topSiteId, parentOrganizationId, true);
                    }
                }
                break;
            default:
                return null;
        }

        return null;
    }

    /**
     * 批量获取当前组织是否为父组织，如果有则返回当前组织自己，如果没有则不返回
     *
     * @author chuang.yang
     * @date 2019/12/11
     * @param tenantId
     * @param parentOrganizationIds
     * @param parentOrganizationType
     * @param topSiteId
     * @return java.util.List<java.lang.String>
     */
    @Override
    public List<String> selectByParentOrganizationIds(Long tenantId, List<String> parentOrganizationIds,
                                                      String parentOrganizationType, String topSiteId) {
        String whereInValuesSql =
                StringHelper.getWhereInValuesSql("PARENT_ORGANIZATION_ID", parentOrganizationIds, 1000);

        return mtModOrganizationRelMapper.selectByParentOrganizationIds(tenantId, whereInValuesSql,
                parentOrganizationType, topSiteId);
    }

    /**
     * 单层查询组织关系查询的相关函数 : SingleLayer
     *
     * @author chuang.yang
     * @date 2019/10/12
     */
    private List<MtModOrganizationSingleChildrenVO> getSingleLayerEnterPrise(Long tenantId, MtModEnterprise enterprise,
                                                                             boolean queryEnable) {
        if (enterprise != null) {
            MtModOrganizationRel mtModOrganizationRel = new MtModOrganizationRel();
            mtModOrganizationRel.setTenantId(tenantId);
            mtModOrganizationRel.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE);
            mtModOrganizationRel.setParentOrganizationId(enterprise.getEnterpriseId());
            List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelMapper.select(mtModOrganizationRel);

            List<MtModOrganizationSingleChildrenVO> childrenList =
                    Collections.synchronizedList(new ArrayList<MtModOrganizationSingleChildrenVO>());

            if (mtModOrganizationRels.size() > 0) {

                // 企业下只有站点, 批量查询站点信息
                List<String> siteIds = mtModOrganizationRels.stream().map(MtModOrganizationRel::getOrganizationId)
                        .collect(Collectors.toList());
                List<MtModSite> modSites = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, siteIds);

                if (CollectionUtils.isNotEmpty(modSites)) {
                    // 获取描述配置
                    String siteDes = this.getOrganizationDescriptionByCode(tenantId,
                            MtBaseConstants.ORGANIZATION_TYPE.SITE);

                    // 转为Map数据
                    Map<String, MtModSite> modSiteMap =
                            modSites.stream().collect(Collectors.toMap(t -> t.getSiteId(), t -> t));

                    // 填充返回数据
                    mtModOrganizationRels.parallelStream().forEach(rel -> {
                        MtModSite modSite = modSiteMap.get(rel.getOrganizationId());
                        if (modSite != null) {
                            if ((queryEnable && isEnable(modSite.getEnableFlag())) || !queryEnable) {
                                MtModOrganizationSingleChildrenVO subChildren = new MtModOrganizationSingleChildrenVO();
                                subChildren.setTopSiteId(modSite.getSiteId());
                                subChildren.setCode(modSite.getSiteCode());
                                subChildren.setName(modSite.getSiteName());
                                subChildren.setText(siteDes);
                                subChildren.setId(modSite.getSiteId());
                                subChildren.setType(rel.getOrganizationType());
                                subChildren.setSequence(rel.getSequence());
                                subChildren.setParentId(enterprise.getEnterpriseId());
                                subChildren.setParentType(MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE);
                                subChildren.setChildren(this.siteHasChildren(tenantId, subChildren.getTopSiteId(),
                                        rel.getOrganizationId(), rel.getOrganizationType()));
                                childrenList.add(subChildren);
                            }
                        }
                    });
                }
            }
            return childrenList.parallelStream()
                    .sorted(Comparator.comparing(t -> t.getSequence() == null ? 0L : t.getSequence()))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    private List<MtModOrganizationSingleChildrenVO> getSingleLayerSite(Long tenantId, String topSiteId,
                                                                       String parentOrganizationId, String parentOrganizationType, boolean queryEnable) {
        MtModOrganizationRel mtModOrganizationRel = new MtModOrganizationRel();
        mtModOrganizationRel.setTenantId(tenantId);
        mtModOrganizationRel.setParentOrganizationType(parentOrganizationType);
        mtModOrganizationRel.setParentOrganizationId(parentOrganizationId);
        mtModOrganizationRel.setTopSiteId(topSiteId);
        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelMapper.select(mtModOrganizationRel);

        List<MtModOrganizationSingleChildrenVO> childrenList = new ArrayList<MtModOrganizationSingleChildrenVO>();

        if (CollectionUtils.isNotEmpty(mtModOrganizationRels)) {
            childrenList = this.getSubChildrenListForSiteOrArea(tenantId, topSiteId, parentOrganizationId,
                    parentOrganizationType, mtModOrganizationRels, queryEnable);
        }

        childrenList = childrenList.parallelStream()
                .sorted(Comparator.comparing(t -> t.getSequence() == null ? 0L : t.getSequence()))
                .collect(Collectors.toList());

        // 可能有库位关系
        List<MtModOrganizationSingleChildrenVO> locatorChildrenList = getSingleLayerLocator(tenantId, topSiteId,
                parentOrganizationId, parentOrganizationType, queryEnable);
        if (CollectionUtils.isNotEmpty(locatorChildrenList)) {
            locatorChildrenList = locatorChildrenList.parallelStream()
                    .sorted(Comparator.comparing(t -> t.getSequence() == null ? 0L : t.getSequence()))
                    .collect(Collectors.toList());

            childrenList.addAll(locatorChildrenList);
        }

        return childrenList;
    }

    private List<MtModOrganizationSingleChildrenVO> getSingleLayerArea(Long tenantId, String topSiteId,
                                                                       String parentOrganizationId, String parentOrganizationType, boolean queryEnable) {

        // 查询下是否有子节点
        MtModOrganizationRel modOrganizationRel = new MtModOrganizationRel();
        modOrganizationRel.setTenantId(tenantId);
        modOrganizationRel.setParentOrganizationType(parentOrganizationType);
        modOrganizationRel.setParentOrganizationId(parentOrganizationId);
        modOrganizationRel.setTopSiteId(topSiteId);
        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelMapper.select(modOrganizationRel);

        List<MtModOrganizationSingleChildrenVO> childrenList = new ArrayList<MtModOrganizationSingleChildrenVO>();
        if (CollectionUtils.isNotEmpty(mtModOrganizationRels)) {
            childrenList = getSubChildrenListForSiteOrArea(tenantId, topSiteId, parentOrganizationId,
                    parentOrganizationType, mtModOrganizationRels, queryEnable);
        }

        childrenList = childrenList.parallelStream()
                .sorted(Comparator.comparing(t -> t.getSequence() == null ? 0L : t.getSequence()))
                .collect(Collectors.toList());

        // 可能有库位关系
        List<MtModOrganizationSingleChildrenVO> locatorChildrenList = getSingleLayerLocator(tenantId, topSiteId,
                parentOrganizationId, parentOrganizationType, queryEnable);
        if (CollectionUtils.isNotEmpty(locatorChildrenList)) {
            locatorChildrenList = locatorChildrenList.parallelStream()
                    .sorted(Comparator.comparing(t -> t.getSequence() == null ? 0L : t.getSequence()))
                    .collect(Collectors.toList());

            childrenList.addAll(locatorChildrenList);
        }

        return childrenList;
    }

    /**
     * 查询站点、区域下的子组织：区域、产线、WKC
     *
     * @author chuang.yang
     * @date 2019/10/12
     * @param tenantId
     * @param topSiteId
     * @param mtModOrganizationRels
     * @param queryEnable
     * @return java.util.List<hmes.mod_organization_rel.view.Children>
     */
    private List<MtModOrganizationSingleChildrenVO> getSubChildrenListForSiteOrArea(Long tenantId, String topSiteId,
                                                                                    String parentOrganizationId, String parentOrganizationType,
                                                                                    List<MtModOrganizationRel> mtModOrganizationRels, boolean queryEnable) {

        List<MtModOrganizationSingleChildrenVO> childrenList =
                Collections.synchronizedList(new ArrayList<MtModOrganizationSingleChildrenVO>());

        // 按照组织类型分组
        Map<String, List<MtModOrganizationRel>> modOrganizationRelMap = mtModOrganizationRels.stream()
                .collect(Collectors.groupingBy(MtModOrganizationRel::getOrganizationType));

        // 站点、区域下有：区域、产线、WKC
        List<MtModOrganizationRel> modAreaRelList = new ArrayList<>();
        List<MtModOrganizationRel> modProdLineRelList = new ArrayList<>();
        List<MtModOrganizationRel> modWorkcellRelList = new ArrayList<>();
        for (Map.Entry<String, List<MtModOrganizationRel>> entry : modOrganizationRelMap.entrySet()) {
            switch (entry.getKey()) {
                case MtBaseConstants.ORGANIZATION_TYPE.AREA:
                    modAreaRelList = entry.getValue();
                    break;
                case MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE:
                    modProdLineRelList = entry.getValue();
                    break;
                case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                    modWorkcellRelList = entry.getValue();
                    break;
                default:
                    break;
            }
        }

        // 批量查询区域信息
        if (CollectionUtils.isNotEmpty(modAreaRelList)) {
            List<String> areaIds = modAreaRelList.stream().map(MtModOrganizationRel::getOrganizationId)
                    .collect(Collectors.toList());
            List<MtModArea> modAreas = mtModAreaRepository.areaBasicPropertyBatchGet(tenantId, areaIds);

            if (CollectionUtils.isNotEmpty(modAreas)) {
                // 批量查询站点下的子结构
                List<String> parentOrganizationIds = getParentOrganizationIds(tenantId, areaIds,
                        MtBaseConstants.ORGANIZATION_TYPE.AREA, topSiteId);

                // 获取描述配置
                String areaDes = this.getOrganizationDescriptionByCode(tenantId,
                        MtBaseConstants.ORGANIZATION_TYPE.AREA);

                // 转为Map数据
                Map<String, MtModArea> modAreaMap =
                        modAreas.stream().collect(Collectors.toMap(t -> t.getAreaId(), t -> t));

                // 填充返回数据
                mtModOrganizationRels.parallelStream().forEach(rel -> {
                    MtModArea modArea = modAreaMap.get(rel.getOrganizationId());
                    if (modArea != null) {
                        if ((queryEnable && isEnable(modArea.getEnableFlag())) || !queryEnable) {
                            MtModOrganizationSingleChildrenVO subChildren = new MtModOrganizationSingleChildrenVO();
                            subChildren.setTopSiteId(topSiteId);
                            subChildren.setCode(modArea.getAreaCode());
                            // add by ruike, 2020-05-18
                            subChildren.setName(modArea.getAreaName());
                            subChildren.setId(modArea.getAreaId());
                            subChildren.setType(rel.getOrganizationType());
                            subChildren.setText(areaDes);
                            subChildren.setSequence(rel.getSequence());
                            subChildren.setParentId(parentOrganizationId);
                            subChildren.setParentType(parentOrganizationType);
                            subChildren.setChildren(hasChildren(parentOrganizationIds, modArea.getAreaId()));
                            childrenList.add(subChildren);
                        }
                    }
                });
            }
        }

        // 批量查询产线信息
        if (CollectionUtils.isNotEmpty(modProdLineRelList)) {
            List<String> prodLineIds = modProdLineRelList.stream().map(MtModOrganizationRel::getOrganizationId)
                    .collect(Collectors.toList());
            List<MtModProductionLine> modProductionLines =
                    mtModProductionLineRepository.prodLineBasicPropertyBatchGet(tenantId, prodLineIds);

            if (CollectionUtils.isNotEmpty(modProductionLines)) {
                // 批量查询站点下的子结构
                List<String> parentOrganizationIds = getParentOrganizationIds(tenantId, prodLineIds,
                        MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE, topSiteId);

                // 获取描述配置
                String prodLineDes = this.getOrganizationDescriptionByCode(tenantId,
                        MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);

                // 转成Map数据
                Map<String, MtModProductionLine> modProductionLineMap =
                        modProductionLines.stream().collect(Collectors.toMap(t -> t.getProdLineId(), t -> t));

                // 填充返回数据
                mtModOrganizationRels.parallelStream().forEach(rel -> {
                    MtModProductionLine modProductionLine = modProductionLineMap.get(rel.getOrganizationId());
                    if (modProductionLine != null) {
                        if ((queryEnable && isEnable(modProductionLine.getEnableFlag())) || !queryEnable) {
                            MtModOrganizationSingleChildrenVO subChildren = new MtModOrganizationSingleChildrenVO();
                            subChildren.setTopSiteId(topSiteId);
                            subChildren.setCode(modProductionLine.getProdLineCode());
                            // add by ruike, 2020-05-18
                            subChildren.setName(modProductionLine.getProdLineName());
                            subChildren.setId(modProductionLine.getProdLineId());
                            subChildren.setType(rel.getOrganizationType());
                            subChildren.setText(prodLineDes);
                            subChildren.setSequence(rel.getSequence());
                            subChildren.setParentId(parentOrganizationId);
                            subChildren.setParentType(parentOrganizationType);
                            subChildren.setChildren(
                                    hasChildren(parentOrganizationIds, modProductionLine.getProdLineId()));
                            childrenList.add(subChildren);
                        }
                    }
                });
            }
        }

        // 批量查询WKC信息
        if (CollectionUtils.isNotEmpty(modWorkcellRelList)) {
            List<String> workcellIds = modWorkcellRelList.stream().map(MtModOrganizationRel::getOrganizationId)
                    .collect(Collectors.toList());
            List<MtModWorkcell> modWorkcells =
                    mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workcellIds);

            if (CollectionUtils.isNotEmpty(modWorkcells)) {
                // 批量查询站点下的子结构
                List<String> parentOrganizationIds = getParentOrganizationIds(tenantId, workcellIds,
                        MtBaseConstants.ORGANIZATION_TYPE.WORKCELL, topSiteId);

                // 获取描述配置
                String workCellDes = this.getOrganizationDescriptionByCode(tenantId,
                        MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);

                // 转为Map数据
                Map<String, MtModWorkcell> modWorkcellMap =
                        modWorkcells.stream().collect(Collectors.toMap(t -> t.getWorkcellId(), t -> t));

                // 填充返回数据
                mtModOrganizationRels.parallelStream().forEach(rel -> {
                    MtModWorkcell modWorkcell = modWorkcellMap.get(rel.getOrganizationId());
                    if (modWorkcell != null) {
                        if ((queryEnable && isEnable(modWorkcell.getEnableFlag())) || !queryEnable) {
                            MtModOrganizationSingleChildrenVO subChildren = new MtModOrganizationSingleChildrenVO();
                            subChildren.setTopSiteId(topSiteId);
                            subChildren.setCode(modWorkcell.getWorkcellCode());
                            subChildren.setName(modWorkcell.getWorkcellName());
                            subChildren.setId(modWorkcell.getWorkcellId());
                            subChildren.setType(rel.getOrganizationType());
                            subChildren.setText(workCellDes);
                            subChildren.setSequence(rel.getSequence());
                            subChildren.setParentId(parentOrganizationId);
                            subChildren.setParentType(parentOrganizationType);
                            subChildren.setChildren(hasChildren(parentOrganizationIds, modWorkcell.getWorkcellId()));
                            childrenList.add(subChildren);
                        }
                    }
                });
            }
        }

        return childrenList;
    }

    private List<MtModOrganizationSingleChildrenVO> getSingleLayerModelProdline(Long tenantId, String topSiteId,
                                                                                String parentOrganizationId, String parentOrganizationType, boolean queryEnable) {
        List<MtModOrganizationSingleChildrenVO> childrenList = new ArrayList<MtModOrganizationSingleChildrenVO>();

        MtModOrganizationRel modOrganizationRel = new MtModOrganizationRel();
        modOrganizationRel.setTenantId(tenantId);
        modOrganizationRel.setParentOrganizationType(parentOrganizationType);
        modOrganizationRel.setParentOrganizationId(parentOrganizationId);
        modOrganizationRel.setTopSiteId(topSiteId);
        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelMapper.select(modOrganizationRel);

        if (CollectionUtils.isNotEmpty(mtModOrganizationRels)) {
            childrenList = getSubChildrenListForProdLineOrWorkcell(tenantId, topSiteId, parentOrganizationId,
                    parentOrganizationType, mtModOrganizationRels, queryEnable);
        }

        childrenList = childrenList.parallelStream()
                .sorted(Comparator.comparing(t -> t.getSequence() == null ? 0L : t.getSequence()))
                .collect(Collectors.toList());

        // 可能有库位关系
        List<MtModOrganizationSingleChildrenVO> locatorChildrenList = getSingleLayerLocator(tenantId, topSiteId,
                parentOrganizationId, parentOrganizationType, queryEnable);
        if (CollectionUtils.isNotEmpty(locatorChildrenList)) {
            locatorChildrenList = locatorChildrenList.parallelStream()
                    .sorted(Comparator.comparing(t -> t.getSequence() == null ? 0L : t.getSequence()))
                    .collect(Collectors.toList());

            childrenList.addAll(locatorChildrenList);
        }

        return childrenList;
    }

    private List<MtModOrganizationSingleChildrenVO> getSingleLayerModelWorkcell(Long tenantId, String topSiteId,
                                                                                String parentOrganizationId, String parentOrganizationType, boolean queryEnable) {
        List<MtModOrganizationSingleChildrenVO> childrenList = new ArrayList<MtModOrganizationSingleChildrenVO>();

        MtModOrganizationRel modOrganizationRel = new MtModOrganizationRel();
        modOrganizationRel.setTenantId(tenantId);
        modOrganizationRel.setParentOrganizationType(parentOrganizationType);
        modOrganizationRel.setParentOrganizationId(parentOrganizationId);
        modOrganizationRel.setTopSiteId(topSiteId);
        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelMapper.select(modOrganizationRel);

        if (CollectionUtils.isNotEmpty(mtModOrganizationRels)) {
            childrenList = getSubChildrenListForProdLineOrWorkcell(tenantId, topSiteId, parentOrganizationId,
                    parentOrganizationType, mtModOrganizationRels, queryEnable);
        }

        childrenList = childrenList.parallelStream()
                .sorted(Comparator.comparing(t -> t.getSequence() == null ? 0L : t.getSequence()))
                .collect(Collectors.toList());

        // 可能有库位关系
        List<MtModOrganizationSingleChildrenVO> locatorChildrenList = getSingleLayerLocator(tenantId, topSiteId,
                parentOrganizationId, parentOrganizationType, queryEnable);
        if (CollectionUtils.isNotEmpty(locatorChildrenList)) {
            locatorChildrenList = locatorChildrenList.parallelStream()
                    .sorted(Comparator.comparing(t -> t.getSequence() == null ? 0L : t.getSequence()))
                    .collect(Collectors.toList());

            childrenList.addAll(locatorChildrenList);
        }

        return childrenList;
    }

    /**
     * 查询产线、WKC下的子组织：WKC
     *
     * @author chuang.yang
     * @date 2019/10/12
     * @param tenantId
     * @param topSiteId
     * @param mtModOrganizationRels
     * @param queryEnable
     * @return java.util.List<hmes.mod_organization_rel.view.Children>
     */
    private List<MtModOrganizationSingleChildrenVO> getSubChildrenListForProdLineOrWorkcell(Long tenantId,
                                                                                            String topSiteId, String parentOrganizationId, String parentOrganizationType,
                                                                                            List<MtModOrganizationRel> mtModOrganizationRels, boolean queryEnable) {

        List<MtModOrganizationSingleChildrenVO> childrenList =
                Collections.synchronizedList(new ArrayList<MtModOrganizationSingleChildrenVO>());

        // 产线、WKC下有：WKC, 批量查询WKC信息
        List<String> workcellIds = mtModOrganizationRels.stream().map(MtModOrganizationRel::getOrganizationId)
                .collect(Collectors.toList());
        List<MtModWorkcell> modWorkcells = mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workcellIds);

        if (CollectionUtils.isNotEmpty(modWorkcells)) {
            // 批量查询站点下的子结构
            List<String> parentOrganizationIds = getParentOrganizationIds(tenantId, workcellIds,
                    MtBaseConstants.ORGANIZATION_TYPE.WORKCELL, topSiteId);

            // 获取描述配置
            String workCellDes =
                    this.getOrganizationDescriptionByCode(tenantId, MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);

            // 转成Map数据
            Map<String, MtModWorkcell> modWorkcellMap =
                    modWorkcells.stream().collect(Collectors.toMap(t -> t.getWorkcellId(), t -> t));

            // 填充返回数据
            mtModOrganizationRels.parallelStream().forEach(rel -> {
                MtModWorkcell modWorkcell = modWorkcellMap.get(rel.getOrganizationId());
                if (modWorkcell != null) {
                    if ((queryEnable && isEnable(modWorkcell.getEnableFlag())) || !queryEnable) {
                        MtModOrganizationSingleChildrenVO subChildren = new MtModOrganizationSingleChildrenVO();
                        subChildren.setTopSiteId(topSiteId);
                        subChildren.setCode(modWorkcell.getWorkcellCode());
                        // add by ruike, 2020-05-18
                        subChildren.setName(modWorkcell.getWorkcellName());
                        subChildren.setId(modWorkcell.getWorkcellId());
                        subChildren.setType(rel.getOrganizationType());
                        subChildren.setText(workCellDes);
                        subChildren.setSequence(rel.getSequence());
                        subChildren.setParentId(parentOrganizationId);
                        subChildren.setParentType(parentOrganizationType);
                        subChildren.setChildren(hasChildren(parentOrganizationIds, rel.getOrganizationId()));
                        childrenList.add(subChildren);
                    }
                }
            });
        }

        return childrenList;
    }

    private List<MtModOrganizationSingleChildrenVO> getSingleLayerLocator(Long tenantId, String topSiteId,
                                                                          String parentOrganizationId, String parentOrganizationType, boolean queryEnable) {

        List<MtModOrganizationSingleChildrenVO> childrenList =
                Collections.synchronizedList(new ArrayList<MtModOrganizationSingleChildrenVO>());

        MtModLocatorOrgRel reltmp = new MtModLocatorOrgRel();
        reltmp.setTenantId(tenantId);
        reltmp.setOrganizationId(parentOrganizationId);
        reltmp.setOrganizationType(parentOrganizationType);
        List<MtModLocatorOrgRel> locatorOrganizationRels = mtModLocatorOrgRelRepository.select(reltmp);

        if (CollectionUtils.isNotEmpty(locatorOrganizationRels)) {
            // 批量获取库位信息
            List<String> locatorIds = locatorOrganizationRels.stream().map(MtModLocatorOrgRel::getLocatorId)
                    .collect(Collectors.toList());
            String whereInValuesSql = StringHelper.getWhereInValuesSql("t1.LOCATOR_ID", locatorIds, 1000);

            List<MtModLocator> mtModLocatorList = mtModLocatorMapper.selectByIdsCustom(tenantId, whereInValuesSql);

            if (CollectionUtils.isNotEmpty(mtModLocatorList)) {
                // 查询当前所有库位是否有子库位
                List<String> parentLocatorIds = getParentLocatorIds(tenantId, locatorIds);

                // 获取配置描述
                String locatorDes = this.getOrganizationDescriptionByCode(tenantId,
                        MtBaseConstants.ORGANIZATION_TYPE.LOCATOR);

                // 转成Map数据
                Map<String, MtModLocatorOrgRel> modLocatorOrgRelMap = locatorOrganizationRels.stream()
                        .collect(Collectors.toMap(t -> t.getLocatorId(), t -> t));

                // 填充返回数据
                mtModLocatorList.parallelStream().forEach(locator -> {
                    if ((queryEnable && isEnable(locator.getEnableFlag())) || !queryEnable) {
                        MtModOrganizationSingleChildrenVO subChildren = new MtModOrganizationSingleChildrenVO();
                        subChildren.setTopSiteId(topSiteId);
                        subChildren.setCode(locator.getLocatorCode());
                        // add by ruike, 2020-05-18
                        subChildren.setName(locator.getLocatorName());
                        subChildren.setId(locator.getLocatorId());
                        subChildren.setType(MtBaseConstants.ORGANIZATION_TYPE.LOCATOR);
                        subChildren.setText(locatorDes);
                        subChildren.setParentId(parentOrganizationId);
                        subChildren.setParentType(parentOrganizationType);
                        subChildren.setChildren(hasChildren(parentLocatorIds, locator.getLocatorId()));

                        MtModLocatorOrgRel mtModLocatorOrgRel = modLocatorOrgRelMap.get(locator.getLocatorId());
                        if (mtModLocatorOrgRel != null) {
                            subChildren.setSequence(mtModLocatorOrgRel.getSequence());
                        }

                        childrenList.add(subChildren);
                    }
                });
            }
        }

        return childrenList.parallelStream()
                .sorted(Comparator.comparing(t -> t.getSequence() == null ? 0L : t.getSequence()))
                .collect(Collectors.toList());
    }

    private List<MtModOrganizationSingleChildrenVO> getSingleLayerLocator(Long tenantId, String topSiteId,
                                                                          String parentLocatorId, boolean queryEnable) {

        List<MtModOrganizationSingleChildrenVO> childrenList =
                Collections.synchronizedList(new ArrayList<MtModOrganizationSingleChildrenVO>());

        MtModLocator locatorTemp = new MtModLocator();
        locatorTemp.setTenantId(tenantId);
        locatorTemp.setParentLocatorId(parentLocatorId);
        List<MtModLocator> mtModLocatorList = mtModLocatorMapper.select(locatorTemp);

        if (CollectionUtils.isNotEmpty(mtModLocatorList)) {
            // 查询当前所有库位是否有子库位
            List<String> locatorIds =
                    mtModLocatorList.stream().map(MtModLocator::getLocatorId).collect(Collectors.toList());
            List<String> parentLocatorIds = getParentLocatorIds(tenantId, locatorIds);

            // 获取配置描述
            String locatorDes =
                    this.getOrganizationDescriptionByCode(tenantId, MtBaseConstants.ORGANIZATION_TYPE.LOCATOR);

            // 填充返回数据
            mtModLocatorList.parallelStream().forEach(locator -> {

                if ((queryEnable && isEnable(locator.getEnableFlag())) || !queryEnable) {
                    MtModOrganizationSingleChildrenVO subChildren = new MtModOrganizationSingleChildrenVO();
                    subChildren.setTopSiteId(topSiteId);
                    subChildren.setCode(locator.getLocatorCode());
                    // add by ruike, 2020-05-18
                    subChildren.setName(locator.getLocatorName());
                    subChildren.setId(locator.getLocatorId());
                    subChildren.setType(MtBaseConstants.ORGANIZATION_TYPE.LOCATOR);
                    subChildren.setText(locatorDes);
                    subChildren.setParentId(parentLocatorId);
                    subChildren.setParentType(MtBaseConstants.ORGANIZATION_TYPE.LOCATOR);
                    subChildren.setChildren(hasChildren(parentLocatorIds, locator.getLocatorId()));
                    childrenList.add(subChildren);
                }
            });
        }

        return childrenList.parallelStream()
                .sorted(Comparator.comparing(t -> t.getSequence() == null ? 0L : t.getSequence()))
                .collect(Collectors.toList());
    }

    /**
     * 判断当前站点：是否存在子组织
     *
     * @param topSiteId
     * @param parentOrganizationId
     * @param parentOrganizationType
     * @return
     */
    private boolean siteHasChildren(Long tenantId, String topSiteId, String parentOrganizationId,
                                    String parentOrganizationType) {
        // 查询组织关系
        MtModOrganizationRel rel = new MtModOrganizationRel();
        rel.setParentOrganizationType(parentOrganizationType);
        rel.setParentOrganizationId(parentOrganizationId);
        rel.setTopSiteId(topSiteId);
        List<MtModOrganizationRel> mtModOrganizationRels =
                mtModOrganizationRelMapper.selectChildrenCount(tenantId, rel);
        if (CollectionUtils.isNotEmpty(mtModOrganizationRels)) {
            return true;
        }

        // 查询组织与库位关系
        MtModLocatorOrgRel mtModLocatorOrgRel = new MtModLocatorOrgRel();
        mtModLocatorOrgRel.setOrganizationId(parentOrganizationId);
        mtModLocatorOrgRel.setOrganizationType(parentOrganizationType);
        List<MtModLocatorOrgRel> mtModLocatorOrgRels =
                mtModLocatorOrgRelMapper.selectCountByOrganization(tenantId, mtModLocatorOrgRel);
        return CollectionUtils.isNotEmpty(mtModLocatorOrgRels);
    }

    /**
     * 判断当前组织是否存在子组织：除站点
     *
     * @author chuang.yang
     * @date 2019/10/17
     * @param parentOrganizationIds
     * @param organizationId
     * @return boolean
     */
    private boolean hasChildren(List<String> parentOrganizationIds, String organizationId) {
        if (CollectionUtils.isNotEmpty(parentOrganizationIds)) {
            return parentOrganizationIds.contains(organizationId);
        } else {
            return false;
        }
    }

    /**
     * 批量获取当前组织是否为父组织，如果有则返回当前组织自己，如果没有则不返回
     *
     * @author chuang.yang
     * @date 2019/10/17
     * @param tenantId
     * @param parentOrganizationIds
     * @return java.util.List<java.lang.String>
     */
    private List<String> getParentOrganizationIds(Long tenantId, List<String> parentOrganizationIds,
                                                  String parentOrganizationType, String topSiteId) {
        List<String> result = new ArrayList<>();

        // 站点、区域、产线、WKC，可能存在子结构和子库位
        // 1. 查询 子结构
        String whereInValuesSql =
                StringHelper.getWhereInValuesSql("PARENT_ORGANIZATION_ID", parentOrganizationIds, 1000);

        List<String> organizationList = mtModOrganizationRelMapper.selectByParentOrganizationIds(tenantId,
                whereInValuesSql, parentOrganizationType, topSiteId);
        if (CollectionUtils.isNotEmpty(organizationList)) {
            result.addAll(organizationList);
        }

        // 1. 查询 子库位
        whereInValuesSql = StringHelper.getWhereInValuesSql("ORGANIZATION_ID", parentOrganizationIds, 1000);
        List<String> locatorList = mtModLocatorOrgRelMapper.selectByParentOrganization(tenantId, parentOrganizationType,
                whereInValuesSql);
        if (CollectionUtils.isNotEmpty(locatorList)) {
            result.addAll(locatorList);
        }

        return result.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 批量获取当前库位是否为父库位，如果有则返回当前库位自己，如果没有则不返回
     *
     * @author chuang.yang
     * @date 2019/10/17
     * @param tenantId
     * @param parentLocatorIds
     * @return java.util.List<java.lang.String>
     */
    private List<String> getParentLocatorIds(Long tenantId, List<String> parentLocatorIds) {
        String whereInValuesSql = StringHelper.getWhereInValuesSql("PARENT_LOCATOR_ID", parentLocatorIds, 1000);
        return mtModLocatorMapper.selectByParentLocatorIds(tenantId, whereInValuesSql);
    }

    /**
     * 是否有效
     *
     * @author chuang.yang
     * @date 2019/10/12
     * @param enableFlag
     * @return boolean
     */
    private boolean isEnable(String enableFlag) {
        return MtBaseConstants.YES.equals(enableFlag);
    }

    /**
     * 根据code查询prompt描述
     *
     * @author chuang.yang
     * @date 2019/10/12
     * @param tenantId
     * @param code
     * @return java.lang.String
     */
    private String getOrganizationDescriptionByCode(Long tenantId, String code) {
        MtGenType genType = mtGenTypeRepository.getGenType(tenantId, MtBaseConstants.GEN_TYPE_MODULE.MODELING,
                MtBaseConstants.GEN_TYPE_GROUP.ORGANIZATION_TYPE, code);
        if (genType != null) {
            return genType.getDescription();
        } else {
            return "";
        }
    }

    private List<MtModOrganizationParentVO> findParent(Long tenantId, String id, String type, String topId, int pro) {
        List<MtModOrganizationParentVO> parentVo = new ArrayList<MtModOrganizationParentVO>();
        if (!enableCheck(tenantId, type, id)) {
            return parentVo;
        }

        MtModOrganizationRel r = new MtModOrganizationRel();
        r.setTenantId(tenantId);
        r.setOrganizationId(id);
        r.setOrganizationType(type);
        r.setTopSiteId(topId);
        List<MtModOrganizationRel> list = mtModOrganizationRelMapper.select(r);

        for (MtModOrganizationRel rel : list) {
            if (rel != null && enableCheck(tenantId, rel.getParentOrganizationType(), rel.getParentOrganizationId())) {
                MtModOrganizationParentVO p = new MtModOrganizationParentVO();
                p.setOrganizationId(rel.getParentOrganizationId());
                p.setOrganizationType(rel.getParentOrganizationType());
                p.setTopSiteId(rel.getTopSiteId());
                p.setPro(pro);
                p.setSequence(rel.getSequence());
                parentVo.add(p);
                parentVo.addAll(findParent(tenantId, rel.getParentOrganizationId(), rel.getParentOrganizationType(),
                        topId, pro + 1));
            }
        }
        return parentVo;
    }

    /**
     * 改造组织关系获取逻辑
     *
     * @author chuang.yang
     * @date 2019/6/27
     * @return
     */
    private MtModOrganizationVO getModEnterPriseSub(String organizationId, String topSiteId, String relId,
                                                    Long sequence, String queryType, String organizationType, List<MtModOrganizationRel> allRelList) {

        MtModOrganizationVO enterPriseVO = new MtModOrganizationVO();
        enterPriseVO.setMyId(organizationId);
        enterPriseVO.setType(MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE);
        enterPriseVO.setPro(1);
        enterPriseVO.setSequence(sequence);
        enterPriseVO.setRelId(relId);
        enterPriseVO.setTopSiteId(topSiteId);
        if (StringUtils.isNotEmpty(queryType)) {
            enterPriseVO.getEnterPriseVOList().add(enterPriseVO);
        }

        // 查询下是否有子节点
        List<MtModOrganizationRel> rels = allRelList.stream()
                .filter(t -> MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE.equals(t.getParentOrganizationType())
                        && organizationId.equals(t.getParentOrganizationId()))
                .collect(Collectors.toList());

        if (rels.size() > 0) {
            List<MtModSiteVO3> sitelists = new ArrayList<MtModSiteVO3>();
            for (MtModOrganizationRel rel : rels) {
                String tempOrganizationId = rel.getOrganizationId();
                String tempOrganizationType = rel.getOrganizationType();
                String organizationRelId = rel.getOrganizationRelId();
                Long seq = rel.getSequence();
                if (MtBaseConstants.ORGANIZATION_TYPE.SITE.equals(tempOrganizationType)) {
                    MtModSiteVO3 site = getModSiteSub(tempOrganizationId, topSiteId, 2, organizationRelId, seq,
                            queryType, organizationType, allRelList);
                    if (site != null) {
                        sitelists.add(site);
                        /* 企业下面只有站点，站点下面可能有站点,wkc,area,prodline */
                        enterPriseVO.getSiteVOList().addAll(site.getSiteVOList());
                        enterPriseVO.getWorkcellVOList().addAll(site.getWorkcellVOList());
                        enterPriseVO.getAreaVOList().addAll(site.getAreaVOList());
                        enterPriseVO.getProdlineVOList().addAll(site.getProdlineVOList());
                    }
                }
            }
            enterPriseVO.setSiteVO(sitelists);
        }

        return enterPriseVO;
    }

    private MtModSiteVO3 getModSiteSub(String organizationId, String topSiteId, int pro, String relId, Long sequence,
                                       String queryType, String organizationType, List<MtModOrganizationRel> allRelList) {
        if (StringUtils.isEmpty(organizationId)) {
            return null;
        }

        MtModSiteVO3 siteVO = new MtModSiteVO3();
        siteVO.setMyId(organizationId);
        siteVO.setType(MtBaseConstants.ORGANIZATION_TYPE.SITE);
        siteVO.setPro(pro);
        siteVO.setRelId(relId);
        siteVO.setTopSiteId(topSiteId);
        siteVO.setSequence(sequence);
        if (StringUtils.isNotEmpty(queryType)) {
            siteVO.getSiteVOList().add(siteVO);
        }

        // 查询下是否有子节点
        List<MtModOrganizationRel> rels = allRelList.stream()
                .filter(t -> MtBaseConstants.ORGANIZATION_TYPE.SITE.equals(t.getParentOrganizationType())
                        && organizationId.equals(t.getParentOrganizationId())
                        && topSiteId.equals(t.getTopSiteId()))
                .collect(Collectors.toList());

        if (rels.size() > 0) {
            List<MtModAreaVO3> arealist = new ArrayList<MtModAreaVO3>();
            List<MtModWorkcellVO> workcelllist = new ArrayList<MtModWorkcellVO>();
            List<MtModProductionLineVO> prodlinelist = new ArrayList<MtModProductionLineVO>();

            for (MtModOrganizationRel rel : rels) {
                String tempOrganizationId = rel.getOrganizationId();
                String tempOrganizationType = rel.getOrganizationType();
                String organizationRelId = rel.getOrganizationRelId();
                Long seq = rel.getSequence();
                if (MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(tempOrganizationType) && Arrays
                        .asList(MtBaseConstants.ORGANIZATION_TYPE.AREA,
                                MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE,
                                MtBaseConstants.ORGANIZATION_TYPE.WORKCELL)
                        .contains(organizationType)) {
                    MtModAreaVO3 area = getModAreaSub(tempOrganizationId, topSiteId, pro + 1, organizationRelId, seq,
                            queryType, organizationType, allRelList);
                    if (area != null) {
                        arealist.add(area);
                        /* area向下递归可以找到area,wck,prodline */
                        siteVO.getWorkcellVOList().addAll(area.getWorkcellVOList());
                        siteVO.getAreaVOList().addAll(area.getAreaVOList());
                        siteVO.getProdlineVOList().addAll(area.getProdlineVOList());
                    }
                } else if (MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE.equals(tempOrganizationType) && Arrays
                        .asList(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE,
                                MtBaseConstants.ORGANIZATION_TYPE.WORKCELL)
                        .contains(organizationType)) {
                    MtModProductionLineVO line = getModProdlineSub(tempOrganizationId, topSiteId, pro + 1,
                            organizationRelId, seq, queryType, organizationType, allRelList);
                    if (line != null) {
                        prodlinelist.add(line);
                        /* 生产线向下找可能找到生产线和WKC */
                        siteVO.getWorkcellVOList().addAll(line.getWorkcellVOList());
                        siteVO.getProdlineVOList().addAll(line.getProdlineVOList());
                    }
                } else if (MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(tempOrganizationType)
                        && MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(organizationType)) {
                    MtModWorkcellVO workcell = getModWorkcellSub(tempOrganizationId, topSiteId, pro + 1,
                            organizationRelId, seq, queryType, organizationType, allRelList);
                    if (workcell != null) {
                        workcelllist.add(workcell);
                        /* wkc向下递归只会找到WKC */
                        siteVO.getWorkcellVOList().addAll(workcell.getWorkcellVOList());
                    }
                }
            }
            siteVO.setAreaVO(arealist);
            siteVO.setWorkcellVO(workcelllist);
            siteVO.setProdlineVO(prodlinelist);
        }
        return siteVO;
    }

    private MtModAreaVO3 getModAreaSub(String organizationId, String topSiteId, int pro, String relId, Long sequence,
                                       String queryType, String organizationType, List<MtModOrganizationRel> allRelList) {
        if (StringUtils.isEmpty(organizationId)) {
            return null;
        }

        MtModAreaVO3 areaVO = new MtModAreaVO3();
        areaVO.setMyId(organizationId);
        areaVO.setType(MtBaseConstants.ORGANIZATION_TYPE.AREA);
        areaVO.setPro(pro);
        areaVO.setRelId(relId);
        areaVO.setTopSiteId(topSiteId);
        areaVO.setSequence(sequence);

        // 查询下是否有子节点
        List<MtModOrganizationRel> rels = allRelList.stream()
                .filter(t -> MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(t.getParentOrganizationType())
                        && organizationId.equals(t.getParentOrganizationId())
                        && topSiteId.equals(t.getTopSiteId()))
                .collect(Collectors.toList());

        if (rels.size() > 0) {
            List<MtModAreaVO3> arealist = new ArrayList<MtModAreaVO3>();
            List<MtModWorkcellVO> workcelllist = new ArrayList<MtModWorkcellVO>();
            List<MtModProductionLineVO> prodlinelist = new ArrayList<MtModProductionLineVO>();

            for (MtModOrganizationRel rel : rels) {
                String tempOrganizationId = rel.getOrganizationId();
                String tempOrganizationType = rel.getOrganizationType();
                String organizationRelId = rel.getOrganizationRelId();
                Long seq = rel.getSequence();
                if (MtBaseConstants.ORGANIZATION_TYPE.AREA.equals(tempOrganizationType) && Arrays
                        .asList(MtBaseConstants.ORGANIZATION_TYPE.AREA,
                                MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE,
                                MtBaseConstants.ORGANIZATION_TYPE.WORKCELL)
                        .contains(organizationType)) {
                    MtModAreaVO3 area = getModAreaSub(tempOrganizationId, topSiteId, pro + 1, organizationRelId, seq,
                            queryType, organizationType, allRelList);
                    if (area != null) {
                        arealist.add(area);
                        areaVO.getWorkcellVOList().addAll(area.getWorkcellVOList());
                        areaVO.getAreaVOList().addAll(area.getAreaVOList());
                        areaVO.getProdlineVOList().addAll(area.getProdlineVOList());
                    }
                } else if (MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE.equals(tempOrganizationType) && Arrays
                        .asList(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE,
                                MtBaseConstants.ORGANIZATION_TYPE.WORKCELL)
                        .contains(organizationType)) {
                    MtModProductionLineVO line = getModProdlineSub(tempOrganizationId, topSiteId, pro + 1,
                            organizationRelId, seq, queryType, organizationType, allRelList);
                    if (line != null) {
                        prodlinelist.add(line);
                        areaVO.getWorkcellVOList().addAll(line.getWorkcellVOList());
                        areaVO.getProdlineVOList().addAll(line.getProdlineVOList());
                    }
                } else if (MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(tempOrganizationType)
                        && MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(organizationType)) {
                    MtModWorkcellVO workcell = getModWorkcellSub(tempOrganizationId, topSiteId, pro + 1,
                            organizationRelId, seq, queryType, organizationType, allRelList);
                    if (workcell != null) {
                        workcelllist.add(workcell);
                        areaVO.getWorkcellVOList().addAll(workcell.getWorkcellVOList());
                    }
                }
            }

            areaVO.setAreaVO(arealist);
            areaVO.setWorkcellVO(workcelllist);
            areaVO.setProdlineVO(prodlinelist);
        }

        if (StringUtils.isNotEmpty(queryType) && pro != 1) {
            switch (queryType) {
                case MtBaseConstants.QUERY_TYPE.ALL:
                    areaVO.getAreaVOList().add(areaVO);
                    break;
                case MtBaseConstants.QUERY_TYPE.BOTTOM:
                    if (CollectionUtils.isEmpty(areaVO.getAreaVO())) {
                        areaVO.getAreaVOList().add(areaVO);
                    }
                    break;
                case MtBaseConstants.QUERY_TYPE.TOP:
                    if (typeCheck(relId, MtBaseConstants.ORGANIZATION_TYPE.AREA, pro, allRelList)) {
                        areaVO.getAreaVOList().add(areaVO);
                    }
                    break;
                default:
                    break;
            }
        }
        return areaVO;
    }

    private MtModProductionLineVO getModProdlineSub(String organizationId, String topSiteId, int pro, String relId,
                                                    Long sequence, String queryType, String organizationType, List<MtModOrganizationRel> allRelList) {
        if (StringUtils.isEmpty(organizationId)) {
            return null;
        }

        MtModProductionLineVO prodlineVO = new MtModProductionLineVO();
        prodlineVO.setMyId(organizationId);
        prodlineVO.setType(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);
        prodlineVO.setPro(pro);
        prodlineVO.setRelId(relId);
        prodlineVO.setTopSiteId(topSiteId);
        prodlineVO.setSequence(sequence);
        if (StringUtils.isNotEmpty(queryType)) {
            prodlineVO.getProdlineVOList().add(prodlineVO);
        }

        // 查询下是否有子节点
        List<MtModOrganizationRel> rels = allRelList.stream()
                .filter(t -> MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE.equals(t.getParentOrganizationType())
                        && organizationId.equals(t.getParentOrganizationId())
                        && topSiteId.equals(t.getTopSiteId()))
                .collect(Collectors.toList());

        if (rels.size() > 0) {
            List<MtModWorkcellVO> workcelists = new ArrayList<MtModWorkcellVO>();
            for (MtModOrganizationRel rel : rels) {
                String tempOrganizationId = rel.getOrganizationId();
                String tempOrganizationType = rel.getOrganizationType();
                String organizationRelId = rel.getOrganizationRelId();
                Long seq = rel.getSequence();
                if (MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(tempOrganizationType)
                        && MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(organizationType)) {
                    MtModWorkcellVO workcell = getModWorkcellSub(tempOrganizationId, topSiteId, pro + 1,
                            organizationRelId, seq, queryType, organizationType, allRelList);
                    if (workcell != null) {
                        workcelists.add(workcell);
                        prodlineVO.getWorkcellVOList().addAll(workcell.getWorkcellVOList());
                    }
                }
            }
            prodlineVO.setWorkcellVO(workcelists);
        }

        return prodlineVO;
    }

    private MtModWorkcellVO getModWorkcellSub(String organizationId, String topSiteId, int pro, String relId,
                                              Long sequence, String queryType, String organizationType, List<MtModOrganizationRel> allRelList) {
        if (StringUtils.isEmpty(organizationId)) {
            return null;
        }

        MtModWorkcellVO workcellVO = new MtModWorkcellVO();
        workcellVO.setMyId(organizationId);
        workcellVO.setType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
        workcellVO.setPro(pro);
        workcellVO.setRelId(relId);
        workcellVO.setSequence(sequence);
        workcellVO.setTopSiteId(topSiteId);

        // 查询下是否有子节点
        List<MtModOrganizationRel> rels = allRelList.stream()
                .filter(t -> MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(t.getParentOrganizationType())
                        && organizationId.equals(t.getParentOrganizationId())
                        && topSiteId.equals(t.getTopSiteId()))
                .collect(Collectors.toList());

        if (rels.size() > 0) {
            List<MtModWorkcellVO> workcelllists = new ArrayList<MtModWorkcellVO>();
            for (MtModOrganizationRel rel : rels) {
                String tempOrganizationId = rel.getOrganizationId();
                String tempOrganizationType = rel.getOrganizationType();
                String organizationRelId = rel.getOrganizationRelId();
                Long seq = rel.getSequence();
                if (MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(tempOrganizationType)
                        && MtBaseConstants.ORGANIZATION_TYPE.WORKCELL.equals(organizationType)) {
                    MtModWorkcellVO workcell = getModWorkcellSub(tempOrganizationId, topSiteId, pro + 1,
                            organizationRelId, seq, queryType, organizationType, allRelList);
                    if (workcell != null) {
                        workcelllists.add(workcell);
                        workcellVO.getWorkcellVOList().addAll(workcell.getWorkcellVOList());
                    }
                }
            }
            workcellVO.setWorkcellVO(workcelllists);
        }

        if (StringUtils.isNotEmpty(queryType) && pro != 1) {
            switch (queryType) {
                case MtBaseConstants.QUERY_TYPE.ALL:
                    workcellVO.getWorkcellVOList().add(workcellVO);
                    break;
                case MtBaseConstants.QUERY_TYPE.BOTTOM:
                    if (CollectionUtils.isEmpty(workcellVO.getWorkcellVO())) {
                        workcellVO.getWorkcellVOList().add(workcellVO);
                    }
                    break;
                case MtBaseConstants.QUERY_TYPE.TOP:
                    if (typeCheck(relId, MtBaseConstants.ORGANIZATION_TYPE.WORKCELL, pro, allRelList)) {
                        workcellVO.getWorkcellVOList().add(workcellVO);
                    }
                    break;
                default:
                    break;
            }
        }
        return workcellVO;
    }

    /* 下面5个是标准递归子函数 */
    private MtModOrganizationVO getEnterPrise(Long tenantId, String id, String relId, Long sequence,
                                              boolean queryEnable, boolean allTree, String queryType) {
        MtModEnterprise enterprise = new MtModEnterprise();
        enterprise.setTenantId(tenantId);
        enterprise.setEnterpriseId(id);
        if (queryEnable) {
            enterprise.setEnableFlag("Y");
        }

        enterprise = mtModEnterpriseMapper.selectOne(enterprise);

        if (enterprise != null) {
            MtModOrganizationVO enterPriseVO = new MtModOrganizationVO();
            enterPriseVO.setCode(enterprise.getEnterpriseCode());
            enterPriseVO.setName(enterprise.getEnterpriseName());
            enterPriseVO.setMyId(enterprise.getEnterpriseId());
            enterPriseVO.setType(MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE);
            enterPriseVO.setPro(1);
            enterPriseVO.setSequence(sequence);
            enterPriseVO.setRelId(relId);
            if (StringUtils.isNotEmpty(queryType)) {
                enterPriseVO.getEnterPriseVOList().add(enterPriseVO);
            }

            MtModOrganizationChildrenVO children = new MtModOrganizationChildrenVO();
            children.setCode(enterPriseVO.getCode());
            children.setName(enterPriseVO.getName());
            children.setId(enterPriseVO.getMyId());
            children.setType(enterPriseVO.getType());
            List<MtModOrganizationChildrenVO> childrenlist = new ArrayList<MtModOrganizationChildrenVO>();
            MtModOrganizationRel r = new MtModOrganizationRel();
            r.setTenantId(tenantId);
            r.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE);
            r.setParentOrganizationId(id);
            List<MtModOrganizationRel> rels = mtModOrganizationRelMapper.select(r);
            if (rels.size() > 0) {
                List<MtModSiteVO3> sitelists = new ArrayList<MtModSiteVO3>();
                for (MtModOrganizationRel rel : rels) {
                    String mId = rel.getOrganizationId();
                    String mType = rel.getOrganizationType();
                    String organizationRelId = rel.getOrganizationRelId();
                    Long seq = rel.getSequence();
                    switch (mType) {
                        case MtBaseConstants.ORGANIZATION_TYPE.SITE:
                            MtModSiteVO3 site = getSite(tenantId, mId, 2, organizationRelId, seq, queryEnable, allTree,
                                    queryType, rel.getParentOrganizationId(), rel.getParentOrganizationType());
                            if (site != null) {
                                childrenlist.add(site.getChildren());
                                sitelists.add(site);
                                /* 企业下面只有站点，站点下面可能有站点,wkc,area,prodline */
                                enterPriseVO.getSiteVOList().addAll(site.getSiteVOList());
                                enterPriseVO.getWorkcellVOList().addAll(site.getWorkcellVOList());
                                enterPriseVO.getAreaVOList().addAll(site.getAreaVOList());
                                enterPriseVO.getProdlineVOList().addAll(site.getProdlineVOList());
                            }
                            break;
                        default:
                            break;
                    }
                }
                enterPriseVO.setSiteVO(sitelists);
            }

            children.setChildren(sortChildrenList(childrenlist));
            enterPriseVO.setChildren(children);
            return enterPriseVO;
        } else {
            return null;
        }
    }

    private List<MtModOrganizationChildrenVO> sortChildrenList(List<MtModOrganizationChildrenVO> childrenList) {
        for (MtModOrganizationChildrenVO children : childrenList) {
            if (children.getSequence() == null) {
                children.setSequence(Long.valueOf(1L));
            }
        }
        List<MtModOrganizationChildrenVO> locatorList =
                childrenList.stream().filter(c -> MtBaseConstants.ORGANIZATION_TYPE.LOCATOR.equals(c.getType()))
                        .collect(Collectors.toList());
        List<MtModOrganizationChildrenVO> orgList = childrenList.stream()
                .filter(c -> !MtBaseConstants.ORGANIZATION_TYPE.LOCATOR.equals(c.getType()))
                .collect(Collectors.toList());

        List<MtModOrganizationChildrenVO> resultList = new ArrayList<>(childrenList.size());
        resultList.addAll(orgList.stream().sorted(Comparator.comparing(MtModOrganizationChildrenVO::getSequence))
                .collect(Collectors.toList()));
        resultList.addAll(locatorList.stream().sorted(Comparator.comparing(MtModOrganizationChildrenVO::getSequence))
                .collect(Collectors.toList()));

        for (MtModOrganizationChildrenVO children : resultList) {
            if (CollectionUtils.isNotEmpty(children.getChildren())) {
                children.setChildren(sortChildrenList(children.getChildren()));
            }
        }

        return resultList;
    }

    private MtModSiteVO3 getSite(Long tenantId, String id, int pro, String relId, Long sequence, boolean queryEnable,
                                 boolean allTree, String queryType, String parentId, String parentType) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        MtModSite modSite = new MtModSite();
        modSite.setTenantId(tenantId);
        modSite.setSiteId(id);
        if (queryEnable) {
            modSite.setEnableFlag("Y");
        }
        modSite = mtModSiteMapper.selectOne(modSite);

        String topId = id;
        if (modSite != null) {
            MtModSiteVO3 siteVO = new MtModSiteVO3();
            siteVO.setCode(modSite.getSiteCode());
            siteVO.setName(modSite.getSiteName());
            siteVO.setMyId(modSite.getSiteId());
            siteVO.setType(MtBaseConstants.ORGANIZATION_TYPE.SITE);
            siteVO.setPro(pro);
            siteVO.setRelId(relId);
            siteVO.setTopSiteId(topId);
            siteVO.setSequence(sequence);
            if (StringUtils.isNotEmpty(queryType)) {
                siteVO.getSiteVOList().add(siteVO);
            }

            MtModOrganizationChildrenVO children = new MtModOrganizationChildrenVO();
            children.setTopSiteId(siteVO.getMyId());
            children.setCode(siteVO.getCode());
            children.setName(siteVO.getName());
            children.setId(siteVO.getMyId());
            children.setType(siteVO.getType());
            children.setSequence(sequence);
            children.setParentId(parentId);
            children.setParentType(parentType);
            List<MtModOrganizationChildrenVO> childrenlist = new ArrayList<MtModOrganizationChildrenVO>();
            MtModOrganizationRel r = new MtModOrganizationRel();
            r.setTenantId(tenantId);
            r.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.SITE);
            r.setParentOrganizationId(id);
            r.setTopSiteId(topId);
            List<MtModOrganizationRel> rels = mtModOrganizationRelMapper.select(r);

            if (rels.size() > 0) {
                List<MtModAreaVO3> arealist = new ArrayList<MtModAreaVO3>();
                List<MtModWorkcellVO> workcelllist = new ArrayList<MtModWorkcellVO>();
                List<MtModProductionLineVO> prodlinelist = new ArrayList<MtModProductionLineVO>();

                for (MtModOrganizationRel rel : rels) {
                    String mId = rel.getOrganizationId();
                    String mType = rel.getOrganizationType();
                    String organizationRelId = rel.getOrganizationRelId();
                    Long seq = rel.getSequence();
                    switch (mType) {
                        case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                            MtModWorkcellVO workcell = getModelWorkcell(tenantId, mId, topId, pro + 1,
                                    organizationRelId, seq, queryEnable, allTree, queryType,
                                    rel.getParentOrganizationId(), rel.getParentOrganizationType());
                            if (workcell != null) {
                                workcelllist.add(workcell);
                                /* wkc向下递归只会找到WKC */
                                childrenlist.add(workcell.getChildren());
                                siteVO.getWorkcellVOList().addAll(workcell.getWorkcellVOList());
                            }
                            break;
                        case MtBaseConstants.ORGANIZATION_TYPE.AREA:
                            MtModAreaVO3 area = getArea(tenantId, mId, topId, pro + 1, organizationRelId, seq,
                                    queryEnable, allTree, queryType, rel.getParentOrganizationId(),
                                    rel.getParentOrganizationType());
                            if (area != null) {
                                arealist.add(area);
                                childrenlist.add(area.getChildren());
                                /* area向下递归可以找到area,wck,prodline */
                                siteVO.getWorkcellVOList().addAll(area.getWorkcellVOList());
                                siteVO.getAreaVOList().addAll(area.getAreaVOList());
                                siteVO.getProdlineVOList().addAll(area.getProdlineVOList());
                            }
                            break;
                        case MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE:
                            MtModProductionLineVO line = getModelProdline(tenantId, mId, topId, pro + 1,
                                    organizationRelId, seq, queryEnable, allTree, queryType,
                                    rel.getParentOrganizationId(), rel.getParentOrganizationType());
                            if (line != null) {
                                prodlinelist.add(line);
                                childrenlist.add(line.getChildren());
                                /* 生产线向下找可能找到生产线和WKC */
                                siteVO.getWorkcellVOList().addAll(line.getWorkcellVOList());
                                siteVO.getProdlineVOList().addAll(line.getProdlineVOList());
                            }
                            break;
                        default:
                            break;
                    }
                }
                siteVO.setAreaVO(arealist);
                siteVO.setWorkcellVO(workcelllist);
                siteVO.setProdlineVO(prodlinelist);
            }

            // 可能有库位关系
            if (allTree) {
                MtModLocatorOrgRel reltmp = new MtModLocatorOrgRel();
                reltmp.setTenantId(tenantId);
                reltmp.setOrganizationId(id);
                reltmp.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.SITE);
                List<MtModLocatorOrgRel> rellist = mtModLocatorOrgRelMapper.select(reltmp);

                if (rellist.size() > 0) {
                    List<MtModLocatorVO> locatorlist = new ArrayList<MtModLocatorVO>();
                    for (MtModLocatorOrgRel mtModLocatorOrgRel : rellist) {
                        MtModLocatorVO t = getLocator(tenantId, mtModLocatorOrgRel.getLocatorId(), queryEnable,
                                mtModLocatorOrgRel.getSequence(), mtModLocatorOrgRel.getOrganizationId(),
                                mtModLocatorOrgRel.getOrganizationType());
                        locatorlist.add(t);
                        childrenlist.add(t.getChildren());
                    }
                    siteVO.setLocatorVO(locatorlist);
                }
            }

            children.setChildren(childrenlist);
            siteVO.setChildren(children);
            return siteVO;
        } else {
            return null;
        }
    }

    private MtModAreaVO3 getArea(Long tenantId, String id, String topId, int pro, String relId, Long sequence,
                                 boolean queryEnable, boolean allTree, String queryType, String parentId, String parentType) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }

        MtModArea modArea = new MtModArea();
        modArea.setTenantId(tenantId);
        modArea.setAreaId(id);
        if (queryEnable) {
            modArea.setEnableFlag("Y");
        }
        modArea = mtModAreaMapper.selectOne(modArea);

        if (modArea != null) {
            MtModAreaVO3 areaVO = new MtModAreaVO3();
            areaVO.setCode(modArea.getAreaCode());
            // add by ruike, 2020-05-18
            areaVO.setName(modArea.getAreaName());
            areaVO.setMyId(modArea.getAreaId());
            areaVO.setType(MtBaseConstants.ORGANIZATION_TYPE.AREA);
            areaVO.setPro(pro);
            areaVO.setRelId(relId);
            areaVO.setTopSiteId(topId);
            areaVO.setSequence(sequence);

            MtModOrganizationChildrenVO children = new MtModOrganizationChildrenVO();
            children.setTopSiteId(topId);
            children.setCode(areaVO.getCode());
            // add by ruike, 2020-05-18
            children.setName(areaVO.getName());
            children.setId(areaVO.getMyId());
            children.setType(areaVO.getType());
            children.setSequence(sequence);
            children.setParentId(parentId);
            children.setParentType(parentType);
            List<MtModOrganizationChildrenVO> childrenlist = new ArrayList<MtModOrganizationChildrenVO>();

            // 查询下是否有子节点
            MtModOrganizationRel modOrganizationRel = new MtModOrganizationRel();
            modOrganizationRel.setTenantId(tenantId);
            modOrganizationRel.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.AREA);
            modOrganizationRel.setParentOrganizationId(id);
            modOrganizationRel.setTopSiteId(topId);
            List<MtModOrganizationRel> rels = mtModOrganizationRelMapper.select(modOrganizationRel);

            if (rels.size() > 0) {
                List<MtModAreaVO3> arealist = new ArrayList<MtModAreaVO3>();
                List<MtModWorkcellVO> workcelllist = new ArrayList<MtModWorkcellVO>();
                List<MtModProductionLineVO> prodlinelist = new ArrayList<MtModProductionLineVO>();

                for (MtModOrganizationRel rel : rels) {
                    String mId = rel.getOrganizationId();
                    String mType = rel.getOrganizationType();
                    String organizationRelId = rel.getOrganizationRelId();
                    Long seq = rel.getSequence();
                    switch (mType) {
                        case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                            MtModWorkcellVO workcell = getModelWorkcell(tenantId, mId, topId, pro + 1,
                                    organizationRelId, seq, queryEnable, allTree, queryType,
                                    rel.getParentOrganizationId(), rel.getParentOrganizationType());
                            if (workcell != null) {
                                childrenlist.add(workcell.getChildren());
                                workcelllist.add(workcell);
                                areaVO.getWorkcellVOList().addAll(workcell.getWorkcellVOList());
                            }
                            break;
                        case MtBaseConstants.ORGANIZATION_TYPE.AREA:
                            MtModAreaVO3 area = getArea(tenantId, mId, topId, pro + 1, organizationRelId, seq,
                                    queryEnable, allTree, queryType, rel.getParentOrganizationId(),
                                    rel.getParentOrganizationType());
                            if (area != null) {
                                arealist.add(area);
                                childrenlist.add(area.getChildren());
                                areaVO.getWorkcellVOList().addAll(area.getWorkcellVOList());
                                areaVO.getAreaVOList().addAll(area.getAreaVOList());
                                areaVO.getProdlineVOList().addAll(area.getProdlineVOList());
                            }
                            break;
                        case MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE:
                            MtModProductionLineVO line = getModelProdline(tenantId, mId, topId, pro + 1,
                                    organizationRelId, seq, queryEnable, allTree, queryType,
                                    rel.getParentOrganizationId(), rel.getParentOrganizationType());
                            if (line != null) {
                                prodlinelist.add(line);
                                childrenlist.add(line.getChildren());
                                areaVO.getWorkcellVOList().addAll(line.getWorkcellVOList());
                                areaVO.getProdlineVOList().addAll(line.getProdlineVOList());
                            }
                            break;
                        default:
                            break;
                    }
                }

                areaVO.setAreaVO(arealist);
                areaVO.setWorkcellVO(workcelllist);
                areaVO.setProdlineVO(prodlinelist);
            }

            // 可能有库位关系
            if (allTree) {
                MtModLocatorOrgRel reltmp = new MtModLocatorOrgRel();
                reltmp.setTenantId(tenantId);
                reltmp.setOrganizationId(id);
                reltmp.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.AREA);
                List<MtModLocatorOrgRel> rellist = mtModLocatorOrgRelMapper.select(reltmp);
                if (rellist.size() > 0) {
                    List<MtModLocatorVO> locatorlist = new ArrayList<MtModLocatorVO>();
                    for (MtModLocatorOrgRel mtModLocatorOrgRel : rellist) {
                        MtModLocatorVO t = getLocator(tenantId, mtModLocatorOrgRel.getLocatorId(), queryEnable,
                                mtModLocatorOrgRel.getSequence(), mtModLocatorOrgRel.getOrganizationId(),
                                mtModLocatorOrgRel.getOrganizationType());
                        locatorlist.add(t);
                        childrenlist.add(t.getChildren());
                    }
                    areaVO.setLocatorVO(locatorlist);
                }
            }

            children.setChildren(childrenlist);
            areaVO.setChildren(children);
            if (StringUtils.isNotEmpty(queryType)) {
                switch (queryType) {
                    case MtBaseConstants.QUERY_TYPE.ALL:
                        areaVO.getAreaVOList().add(areaVO);
                        break;
                    case MtBaseConstants.QUERY_TYPE.BOTTOM:
                        if (CollectionUtils.isEmpty(areaVO.getAreaVO())) {
                            areaVO.getAreaVOList().add(areaVO);
                        }
                        break;
                    case MtBaseConstants.QUERY_TYPE.TOP:
                        if (typeCheck(tenantId, relId, MtBaseConstants.ORGANIZATION_TYPE.AREA, pro)) {
                            areaVO.getAreaVOList().add(areaVO);
                        }
                        break;
                    default:
                        break;
                }
            }
            return areaVO;
        } else {
            return null;
        }
    }

    private MtModProductionLineVO getModelProdline(Long tenantId, String id, String topId, int pro, String relId,
                                                   Long sequence, boolean queryEnable, boolean allTree, String queryType, String parentId,
                                                   String parentType) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        MtModProductionLine modProductionLine = new MtModProductionLine();
        modProductionLine.setTenantId(tenantId);
        modProductionLine.setProdLineId(id);
        if (queryEnable) {
            modProductionLine.setEnableFlag("Y");
        }
        modProductionLine = mtModProductionLineMapper.selectOne(modProductionLine);

        if (modProductionLine != null) {
            MtModProductionLineVO prodlineVO = new MtModProductionLineVO();
            prodlineVO.setCode(modProductionLine.getProdLineCode());
            // add by ruike, 2020-05-18
            prodlineVO.setName(modProductionLine.getProdLineName());
            prodlineVO.setMyId(modProductionLine.getProdLineId());
            prodlineVO.setType(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);
            prodlineVO.setPro(pro);
            prodlineVO.setRelId(relId);
            prodlineVO.setTopSiteId(topId);
            prodlineVO.setSequence(sequence);
            if (StringUtils.isNotEmpty(queryType)) {
                prodlineVO.getProdlineVOList().add(prodlineVO);
            }

            MtModOrganizationChildrenVO children = new MtModOrganizationChildrenVO();
            children.setCode(prodlineVO.getCode());
            // add by ruike, 2020-05-18
            children.setName(prodlineVO.getName());
            children.setTopSiteId(topId);
            children.setId(prodlineVO.getMyId());
            children.setType(prodlineVO.getType());
            children.setSequence(sequence);
            children.setParentId(parentId);
            children.setParentType(parentType);
            List<MtModOrganizationChildrenVO> childrenlist = new ArrayList<MtModOrganizationChildrenVO>();

            MtModOrganizationRel modOrganizationRel = new MtModOrganizationRel();
            modOrganizationRel.setTenantId(tenantId);
            modOrganizationRel.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);
            modOrganizationRel.setParentOrganizationId(id);
            modOrganizationRel.setTopSiteId(topId);
            List<MtModOrganizationRel> rels = mtModOrganizationRelMapper.select(modOrganizationRel);

            if (rels.size() > 0) {
                List<MtModWorkcellVO> workcelists = new ArrayList<MtModWorkcellVO>();
                for (MtModOrganizationRel rel : rels) {
                    String mId = rel.getOrganizationId();
                    String mType = rel.getOrganizationType();
                    String organizationRelId = rel.getOrganizationRelId();
                    Long seq = rel.getSequence();
                    switch (mType) {
                        case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                            MtModWorkcellVO workcell = getModelWorkcell(tenantId, mId, topId, pro + 1,
                                    organizationRelId, seq, queryEnable, allTree, queryType,
                                    rel.getParentOrganizationId(), rel.getParentOrganizationType());
                            if (workcell != null) {
                                workcelists.add(workcell);
                                childrenlist.add(workcell.getChildren());
                                prodlineVO.getWorkcellVOList().addAll(workcell.getWorkcellVOList());
                            }
                            break;
                        default:
                            break;
                    }
                }
                prodlineVO.setWorkcellVO(workcelists);
            }

            // 可能有库位关系
            if (allTree) {
                MtModLocatorOrgRel reltmp = new MtModLocatorOrgRel();
                reltmp.setTenantId(tenantId);
                reltmp.setOrganizationId(id);
                reltmp.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE);
                List<MtModLocatorOrgRel> rellist = mtModLocatorOrgRelMapper.select(reltmp);

                if (rellist.size() > 0) {
                    List<MtModLocatorVO> locatorlist = new ArrayList<MtModLocatorVO>();
                    for (MtModLocatorOrgRel mtModLocatorOrgRel : rellist) {
                        MtModLocatorVO t = getLocator(tenantId, mtModLocatorOrgRel.getLocatorId(), queryEnable,
                                mtModLocatorOrgRel.getSequence(), mtModLocatorOrgRel.getOrganizationId(),
                                mtModLocatorOrgRel.getOrganizationType());
                        locatorlist.add(t);
                        childrenlist.add(t.getChildren());
                    }
                    prodlineVO.setLocatorVO(locatorlist);
                }
            }

            children.setChildren(childrenlist);
            prodlineVO.setChildren(children);
            return prodlineVO;
        } else {
            return null;
        }
    }

    private MtModWorkcellVO getModelWorkcell(Long tenantId, String id, String topId, int pro, String relId,
                                             Long sequence, boolean queryEnable, boolean allTree, String queryType, String parentId,
                                             String parentType) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }

        MtModWorkcell modWorkcell = new MtModWorkcell();
        modWorkcell.setTenantId(tenantId);
        modWorkcell.setWorkcellId(id);
        if (queryEnable) {
            modWorkcell.setEnableFlag("Y");
        }
        modWorkcell = mtModWorkcellMapper.selectOne(modWorkcell);

        if (modWorkcell != null) {
            MtModWorkcellVO workcellVO = new MtModWorkcellVO();
            workcellVO.setCode(modWorkcell.getWorkcellCode());
            workcellVO.setName(modWorkcell.getWorkcellName());
            workcellVO.setMyId(modWorkcell.getWorkcellId());
            workcellVO.setType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
            workcellVO.setPro(pro);
            workcellVO.setRelId(relId);
            workcellVO.setSequence(sequence);
            workcellVO.setTopSiteId(topId);

            MtModOrganizationChildrenVO children = new MtModOrganizationChildrenVO();
            children.setCode(workcellVO.getCode());
            children.setName(workcellVO.getName());
            children.setTopSiteId(topId);
            children.setId(workcellVO.getMyId());
            children.setType(workcellVO.getType());
            children.setSequence(sequence);
            children.setParentId(parentId);
            children.setParentType(parentType);
            List<MtModOrganizationChildrenVO> childrenlist = new ArrayList<MtModOrganizationChildrenVO>();

            MtModOrganizationRel modOrganizationRel = new MtModOrganizationRel();
            modOrganizationRel.setTenantId(tenantId);
            modOrganizationRel.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
            modOrganizationRel.setParentOrganizationId(id);
            modOrganizationRel.setTopSiteId(topId);
            List<MtModOrganizationRel> rels = mtModOrganizationRelMapper.select(modOrganizationRel);

            if (rels.size() > 0) {
                List<MtModWorkcellVO> workcelllists = new ArrayList<MtModWorkcellVO>();
                for (MtModOrganizationRel rel : rels) {
                    String mId = rel.getOrganizationId();
                    String mType = rel.getOrganizationType();
                    String organizationRelId = rel.getOrganizationRelId();
                    Long seq = rel.getSequence();
                    switch (mType) {
                        case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                            MtModWorkcellVO workcell = getModelWorkcell(tenantId, mId, topId, pro + 1,
                                    organizationRelId, seq, queryEnable, allTree, queryType,
                                    rel.getParentOrganizationId(), rel.getParentOrganizationType());
                            if (workcell != null) {
                                workcelllists.add(workcell);
                                childrenlist.add(workcell.getChildren());
                                /* wkc向下递归只有WKC */
                                workcellVO.getWorkcellVOList().addAll(workcell.getWorkcellVOList());
                            }
                            break;
                        default:
                            break;
                    }
                }
                workcellVO.setWorkcellVO(workcelllists);
            }

            // 可能有库位关系
            if (allTree) {
                MtModLocatorOrgRel reltmp = new MtModLocatorOrgRel();
                reltmp.setTenantId(tenantId);
                reltmp.setOrganizationId(id);
                reltmp.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
                List<MtModLocatorOrgRel> rellist = mtModLocatorOrgRelMapper.select(reltmp);
                if (rellist.size() > 0) {
                    List<MtModLocatorVO> locatorlist = new ArrayList<MtModLocatorVO>();
                    for (MtModLocatorOrgRel mtModLocatorOrgRel : rellist) {
                        MtModLocatorVO t = getLocator(tenantId, mtModLocatorOrgRel.getLocatorId(), queryEnable,
                                sequence, mtModLocatorOrgRel.getOrganizationId(),
                                mtModLocatorOrgRel.getOrganizationType());
                        locatorlist.add(t);
                        childrenlist.add(t.getChildren());

                    }
                    workcellVO.setLocatorVO(locatorlist);
                }
            }

            children.setChildren(childrenlist);
            workcellVO.setChildren(children);

            if (StringUtils.isNotEmpty(queryType)) {
                switch (queryType) {
                    case MtBaseConstants.QUERY_TYPE.ALL:
                        workcellVO.getWorkcellVOList().add(workcellVO);
                        break;
                    case MtBaseConstants.QUERY_TYPE.BOTTOM:
                        if (CollectionUtils.isEmpty(workcellVO.getWorkcellVO())) {
                            workcellVO.getWorkcellVOList().add(workcellVO);
                        }
                        break;
                    case MtBaseConstants.QUERY_TYPE.TOP:
                        if (typeCheck(tenantId, relId, MtBaseConstants.ORGANIZATION_TYPE.WORKCELL, pro)) {
                            workcellVO.getWorkcellVOList().add(workcellVO);
                        }
                        break;
                    default:
                        break;
                }
            }
            return workcellVO;
        } else {
            return null;
        }
    }

    private MtModLocatorVO getLocator(Long tenantId, String id, boolean queryEnable, Long sequence, String parentId,
                                      String parentType) {
        MtModLocator locator = new MtModLocator();
        locator.setTenantId(tenantId);
        locator.setLocatorId(id);
        if (queryEnable) {
            locator.setEnableFlag("Y");
        }
        locator = mtModLocatorMapper.selectOne(locator);

        if (locator != null) {
            MtModLocatorVO locatorVO = new MtModLocatorVO();
            locatorVO.setCode(locator.getLocatorCode());
            locatorVO.setMyId(locator.getLocatorId());
            locatorVO.setType(MtBaseConstants.ORGANIZATION_TYPE.LOCATOR);

            MtModOrganizationChildrenVO children = new MtModOrganizationChildrenVO();
            children.setTopSiteId(locatorVO.getMyId());
            children.setCode(locatorVO.getCode());
            children.setId(locatorVO.getMyId());
            children.setType(locatorVO.getType());
            children.setSequence(sequence);
            children.setParentId(parentId);
            children.setParentType(parentType);
            List<MtModOrganizationChildrenVO> childrenlist = new ArrayList<MtModOrganizationChildrenVO>();

            MtModLocator locatorTemp = new MtModLocator();
            locatorTemp.setTenantId(tenantId);
            locatorTemp.setParentLocatorId(id);
            List<MtModLocator> rels = mtModLocatorMapper.select(locatorTemp);

            if (rels.size() > 0) {
                List<MtModLocatorVO> locatorlist = new ArrayList<MtModLocatorVO>();
                for (MtModLocator rel : rels) {
                    MtModLocatorVO t = getLocator(tenantId, rel.getLocatorId(), queryEnable, sequence, id,
                            MtBaseConstants.ORGANIZATION_TYPE.LOCATOR);
                    locatorlist.add(t);
                    childrenlist.add(t.getChildren());
                }
                locatorVO.setLocatorVO(locatorlist);
            }

            children.setChildren(childrenlist);
            locatorVO.setChildren(children);
            return locatorVO;
        } else {
            return null;
        }
    }


    /* 下面5个是公用函数 */
    private static List<MtModOrganizationItemVO> removeDuplicate(List<MtModOrganizationItemVO> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getOrganizationId().equals(list.get(i).getOrganizationId())) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    private static List<String> removeDuplicateList(List<String> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    private boolean enableCheck(Long tenantId, String type, String id) {
        switch (type) {
            case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                MtModWorkcell wkcTemp = new MtModWorkcell();
                wkcTemp.setTenantId(tenantId);
                wkcTemp.setWorkcellId(id);
                wkcTemp.setEnableFlag("Y");
                wkcTemp = mtModWorkcellMapper.selectOne(wkcTemp);
                if (wkcTemp != null) {
                    return true;
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.SITE:
                MtModSite siteTemp = new MtModSite();
                siteTemp.setTenantId(tenantId);
                siteTemp.setSiteId(id);
                siteTemp.setEnableFlag("Y");
                siteTemp = mtModSiteMapper.selectOne(siteTemp);
                if (siteTemp != null) {
                    return true;
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE:
                MtModEnterprise enterpriseTemp = new MtModEnterprise();
                enterpriseTemp.setTenantId(tenantId);
                enterpriseTemp.setEnterpriseId(id);
                enterpriseTemp.setEnableFlag("Y");
                enterpriseTemp = mtModEnterpriseMapper.selectOne(enterpriseTemp);
                if (enterpriseTemp != null) {
                    return true;
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.AREA:
                MtModArea areaTemp = new MtModArea();
                areaTemp.setTenantId(tenantId);
                areaTemp.setAreaId(id);
                areaTemp.setEnableFlag("Y");
                areaTemp = mtModAreaMapper.selectOne(areaTemp);
                if (areaTemp != null) {
                    return true;
                }
                break;
            case MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE:
                MtModProductionLine productionLineType = new MtModProductionLine();
                productionLineType.setTenantId(tenantId);
                productionLineType.setProdLineId(id);
                productionLineType.setEnableFlag("Y");
                productionLineType = mtModProductionLineMapper.selectOne(productionLineType);
                if (productionLineType != null) {
                    return true;
                }
                break;
            default:
                return false;
        }
        return false;
    }

    private boolean typeCheck(String relId, String type, int pro, List<MtModOrganizationRel> allRelList) {
        if (StringUtils.isEmpty(relId)) {
            return false;
        }

        MtModOrganizationRel rel =
                allRelList.stream().filter(t -> t.getOrganizationRelId().equals(relId)).findFirst().get();
        return StringUtils.isNotEmpty(type) && !type.equals(rel.getParentOrganizationType())
                || type.equals(rel.getParentOrganizationType()) && pro == 2;
    }

    private boolean typeCheck(Long tenantId, String relId, String type, int pro) {
        if (StringUtils.isEmpty(relId)) {
            return false;
        }
        MtModOrganizationRel r = new MtModOrganizationRel();
        r.setTenantId(tenantId);
        r.setOrganizationRelId(relId);
        r = mtModOrganizationRelMapper.selectOne(r);
        if (StringUtils.isNotEmpty(type) && !type.equals(r.getParentOrganizationType())
                || type.equals(r.getParentOrganizationType()) && pro == 2) {
            return true;
        }
        return false;
    }
}
