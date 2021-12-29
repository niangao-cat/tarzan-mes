package tarzan.modeling.app.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.modeling.api.dto.MtModSiteDTO;
import tarzan.modeling.api.dto.MtModSiteDTO2;
import tarzan.modeling.api.dto.MtModSiteDTO3;
import tarzan.modeling.api.dto.MtModSiteDTO4;
import tarzan.modeling.app.service.MtModSiteService;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModSiteManufacturing;
import tarzan.modeling.domain.entity.MtModSiteSchedule;
import tarzan.modeling.domain.repository.MtModSiteManufacturingRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtModSiteScheduleRepository;
import tarzan.modeling.domain.vo.MtModSiteManufacturingVO2;
import tarzan.modeling.domain.vo.MtModSiteScheduleVO2;
import tarzan.modeling.domain.vo.MtModSiteVO2;
import tarzan.modeling.domain.vo.MtModSiteVO4;
import tarzan.modeling.infra.mapper.MtModSiteMapper;

/**
 * 站点应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Service
public class MtModSiteServiceImpl implements MtModSiteService {

    private static final String MT_MOD_SITE_ATTR = "mt_mod_site_attr";

    @Autowired
    private MtModSiteMapper mtModSiteMapper;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtModSiteManufacturingRepository mtModSiteManufacturingRepository;

    @Autowired
    private MtModSiteScheduleRepository mtModSiteScheduleRepository;

    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;

    @Override
    public Page<MtModSiteVO2> queryForUi(Long tenantId, MtModSiteDTO2 dto, PageRequest pageRequest) {
        MtModSite mtModSite = new MtModSite();
        mtModSite.setTenantId(tenantId);
        mtModSite.setSiteCode(dto.getSiteCode());
        mtModSite.setSiteName(dto.getSiteName());
        mtModSite.setSiteType(dto.getSiteType());
        mtModSite.setEnableFlag(dto.getEnableFlag());

        Criteria criteria = new Criteria(mtModSite);
        List<WhereField> whereFields = new ArrayList<WhereField>();

        whereFields.add(new WhereField(MtModSite.FIELD_TENANT_ID, Comparison.EQUAL));

        if (dto.getSiteCode() != null) {
            whereFields.add(new WhereField(MtModSite.FIELD_SITE_CODE, Comparison.LIKE));
        }
        if (dto.getSiteName() != null) {
            whereFields.add(new WhereField(MtModSite.FIELD_SITE_NAME, Comparison.LIKE));
        }
        if (dto.getSiteType() != null) {
            whereFields.add(new WhereField(MtModSite.FIELD_SITE_TYPE, Comparison.EQUAL));
        }
        if (dto.getEnableFlag() != null) {
            whereFields.add(new WhereField(MtModSite.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        }

        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

        Page<MtModSite> sites =
                        PageHelper.doPageAndSort(pageRequest, () -> this.mtModSiteMapper.selectOptional(mtModSite, criteria));
        List<MtGenType> siteTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "ORGANIZATION_REL_TYPE");

        List<MtModSiteVO2> list = new ArrayList<MtModSiteVO2>();
        MtModSiteVO2 mtModSiteVO2 = null;
        for (MtModSite site : sites) {
            mtModSiteVO2 = new MtModSiteVO2();
            BeanUtils.copyProperties(site, mtModSiteVO2);
            String siteType = mtModSiteVO2.getSiteType();
            if (StringUtils.isNotEmpty(mtModSiteVO2.getSiteType())) {
                Optional<MtGenType> optional =
                                siteTypes.stream().filter(t -> t.getTypeCode().equals(siteType)).findFirst();
                if (optional.isPresent()) {
                    mtModSiteVO2.setSiteTypeDesc(optional.get().getDescription());
                }
            }
            list.add(mtModSiteVO2);
        }

        Page<MtModSiteVO2> result = new Page<MtModSiteVO2>();
        result.setNumber(sites.getNumber());
        result.setSize(sites.getSize());
        result.setTotalElements(sites.getTotalElements());
        result.setTotalPages(sites.getTotalPages());
        result.setNumberOfElements(sites.getNumberOfElements());
        result.setContent(list);

        return result;
    }

    @Override
    public MtModSiteDTO3 queryInfoForUi(Long tenantId, String siteId) {
        if (StringUtils.isEmpty(siteId)) {
            return null;
        }

        MtModSiteDTO3 dto = new MtModSiteDTO3();
        MtModSite mtModSite = this.mtModSiteRepository.siteBasicPropertyGet(tenantId, siteId);
        if (null == mtModSite) {
            return dto;
        }

        MtModSiteVO4 mtModSiteVO4 = new MtModSiteVO4();
        BeanUtils.copyProperties(mtModSite, mtModSiteVO4);
        if (StringUtils.isNotEmpty(mtModSiteVO4.getSiteType())) {
            List<MtGenType> siteTypes =
                            this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "ORGANIZATION_REL_TYPE");
            Optional<MtGenType> siteType = siteTypes.stream()
                            .filter(t -> t.getTypeCode().equals(mtModSiteVO4.getSiteType())).findFirst();
            if (siteType.isPresent()) {
                mtModSiteVO4.setSiteTypeDesc(siteType.get().getDescription());
            }
        }
        dto.setSite(mtModSiteVO4);

        MtModSiteManufacturing mtModSiteManufacturing =
                        this.mtModSiteManufacturingRepository.siteManufacturingPropertyGet(tenantId, siteId);
        if (null != mtModSiteManufacturing) {
            MtModSiteManufacturingVO2 mtModSiteManufacturingVO2 = new MtModSiteManufacturingVO2();
            BeanUtils.copyProperties(mtModSiteManufacturing, mtModSiteManufacturingVO2);
            if (StringUtils.isNotEmpty(mtModSiteManufacturingVO2.getAttritionCalculateStrategy())) {
                List<MtGenType> strategys =
                                this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "ATTRITION_STRATEGY");
                Optional<MtGenType> strategy = strategys.stream().filter(
                                t -> t.getTypeCode().equals(mtModSiteManufacturingVO2.getAttritionCalculateStrategy()))
                                .findFirst();
                if (strategy.isPresent()) {
                    mtModSiteManufacturingVO2.setAttritionCalculateStrategyDesc(strategy.get().getDescription());
                }
            }
            dto.setSiteManufacturing(mtModSiteManufacturingVO2);
        }

        MtModSiteSchedule mtModSiteSchedule =
                        this.mtModSiteScheduleRepository.siteSchedulePropertyGet(tenantId, siteId);
        if (null != mtModSiteSchedule) {
            MtModSiteScheduleVO2 mtModSiteScheduleVO2 = new MtModSiteScheduleVO2();
            BeanUtils.copyProperties(mtModSiteSchedule, mtModSiteScheduleVO2);
            dto.setSiteSchedule(mtModSiteScheduleVO2);
        }

        List<MtExtendAttrDTO> siteAttrs = mtExtendSettingsService.attrQuery(tenantId, siteId, MT_MOD_SITE_ATTR);
        dto.setSiteAttrs(siteAttrs);

        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveForUi(Long tenantId, MtModSiteDTO4 dto) {
        if (null == dto || null == dto.getSite()) {
            return null;
        }

        MtModSite mtModSite = new MtModSite();
        BeanUtils.copyProperties(dto.getSite(), mtModSite);
        if(dto.getSite()!=null&&dto.getSite().get_tls()!=null){
            mtModSite.set_tls(dto.getSite().get_tls());
        }
        String siteId = mtModSiteRepository.siteBasicPropertyUpdate(tenantId, mtModSite, "Y");
        if (CollectionUtils.isNotEmpty(dto.getSiteAttrs())) {
            mtExtendSettingsService.attrSave(tenantId, MT_MOD_SITE_ATTR, siteId, null, dto.getSiteAttrs());
        }

        if (null != dto.getSiteManufacturing()) {
            dto.getSiteManufacturing().setSiteId(siteId);
            mtModSiteManufacturingRepository.siteManufacturingPropertyUpdate(tenantId, dto.getSiteManufacturing(), "Y");
        }

        if (null != dto.getSiteSchedule()) {
            dto.getSiteSchedule().setSiteId(siteId);
            mtModSiteScheduleRepository.siteSchedulePropertyUpdate(tenantId, dto.getSiteSchedule(), "Y");
        }
        return siteId;
    }

    @Override
    public List<MtModSiteDTO> queryUserSiteForUi(Long tenantId, Long userId, String siteType) {
        if (StringUtils.isEmpty(siteType)) {
            return Collections.emptyList();
        }

        MtUserOrganization mtUserOrganization = new MtUserOrganization();
        mtUserOrganization.setUserId(userId == null ? DetailsHelper.getUserDetails().getUserId() : userId);
        mtUserOrganization.setOrganizationType("SITE");
        List<MtUserOrganization> userOrgList =
                mtUserOrganizationRepository.userOrganizationPermissionQuery(tenantId, mtUserOrganization);
        if (CollectionUtils.isEmpty(userOrgList)) {
            return Collections.emptyList();
        }

        List<MtModSite> modSiteList = mtModSiteMapper.selectSitesLimitType(tenantId,
                userOrgList.stream().map(MtUserOrganization::getOrganizationId).collect(Collectors.toList()),
                siteType);

        List<MtModSiteDTO> resultList = new ArrayList<>(modSiteList.size());
        modSiteList.forEach(s -> {
            MtModSiteDTO dto = new MtModSiteDTO();
            BeanUtils.copyProperties(s, dto);
            resultList.add(dto);
        });

        return resultList;
    }

}
