package tarzan.general.app.service.impl;

import static java.util.stream.Collectors.toList;

import java.util.*;

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
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.MtBaseConstants;
import tarzan.general.api.dto.*;
import tarzan.general.app.service.MtUserOrganizationService;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.vo.MtOrganizationVO;
import tarzan.general.infra.mapper.MtUserOrganizationMapper;
import tarzan.modeling.domain.entity.*;
import tarzan.modeling.domain.repository.*;

/**
 * 用户组织关系表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:40
 */
@Service
public class MtUserOrganizationServiceImpl extends BaseServiceImpl<MtUserOrganization>
                implements MtUserOrganizationService {
    private static final String YES = "Y";

    @Autowired
    private MtUserOrganizationMapper mapper;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtModEnterpriseRepository mtModEnterpriseRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtModAreaRepository mtModAreaRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Override
    public Page<MtUserOrganizationDTO4> userOrganizationQuery(PageRequest pageRequest, Long tenantId,
                    MtUserOrganizationDTO4 dto) {
        if (dto == null) {
            return null;
        }
        Map<Long, MtUserInfo> userInfoMap = userClient.userInfoAllGet(tenantId);

        List<MtUserInfo> infoList = null;
        if (StringUtils.isNotEmpty(dto.getUserName()) && StringUtils.isNotEmpty(dto.getUserDesc())) {
            // 根据登陆名和用户名联合筛选
            infoList = userInfoMap.values().stream()
                            .filter(t -> StringUtils.isNotEmpty(t.getLoginName())
                                            && t.getLoginName().toUpperCase().contains(dto.getUserName().toUpperCase())
                                            && StringUtils.isNotEmpty(t.getRealName())
                                            && t.getRealName().toUpperCase().contains(dto.getUserDesc().toUpperCase()))
                            .collect(toList());
        } else if (StringUtils.isNotEmpty(dto.getUserName())) {
            // 根据登陆名筛选
            infoList = userInfoMap.values().stream()
                            .filter(t -> StringUtils.isNotEmpty(t.getLoginName())
                                            && t.getLoginName().toUpperCase().contains(dto.getUserName().toUpperCase()))
                            .collect(toList());
        } else if (StringUtils.isNotEmpty(dto.getUserDesc())) {
            // 根据用户名筛选
            infoList = userInfoMap.values().stream()
                            .filter(t -> StringUtils.isNotEmpty(t.getRealName())
                                            && t.getRealName().toUpperCase().contains(dto.getUserDesc().toUpperCase()))
                            .collect(toList());
        } else {
            // 如果没有输入这两个条件，不进行筛选
            infoList = new ArrayList<>(userInfoMap.values());
        }

        // 判断LIST是否存在，不存在说明不满足条件
        if (CollectionUtils.isEmpty(infoList)) {
            return new Page<MtUserOrganizationDTO4>();
        }

        // 如果满足条件，根据主键进行数据查询
        List<Long> idList = infoList.stream().map(MtUserInfo::getId).collect(toList());

        // 执行数据库查询并进行数据分页
        Page<MtUserOrganizationDTO4> page =
                        PageHelper.doPage(pageRequest, () -> mapper.mtUserOrgnanizationSearch(tenantId, dto, idList));
        for (MtUserOrganizationDTO4 e : page.getContent()) {
            Optional<MtUserInfo> userOp = infoList.stream()
                            .filter(t -> t.getId() != null && t.getId().equals(e.getUserId())).findFirst();
            if (userOp.isPresent()) {
                e.setUserName(userOp.get().getLoginName());
                e.setUserDesc(userOp.get().getRealName());
            }
        }

        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtUserOrganization userOrganizationSave(Long tenantId, MtUserOrganizationDTO3 dto) {
        MtUserOrganization mtUserOrganization = new MtUserOrganization();
        BeanUtils.copyProperties(dto, mtUserOrganization);
        mtUserOrganization.setTenantId(tenantId);
        // 查询单位编码是否存在
        MtUserOrganization union = new MtUserOrganization();
        union.setTenantId(tenantId);
        union.setUserId(mtUserOrganization.getUserId());
        union.setOrganizationType(mtUserOrganization.getOrganizationType());
        union.setOrganizationId(mtUserOrganization.getOrganizationId());
        if (StringUtils.isNotEmpty(mtUserOrganization.getUserOrganizationId())) {
            union.setUserOrganizationId(mtUserOrganization.getUserOrganizationId());
        }
        Criteria criteria2 = new Criteria(union);
        List<WhereField> whereFields2 = new ArrayList<WhereField>();
        if (StringUtils.isNotEmpty(mtUserOrganization.getUserOrganizationId())) {
            whereFields2.add(new WhereField(MtUserOrganization.FIELD_USER_ORGANIZATION_ID, Comparison.NOT_EQUAL));
        }
        whereFields2.add(new WhereField(MtUserOrganization.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields2.add(new WhereField(MtUserOrganization.FIELD_USER_ID, Comparison.EQUAL));
        whereFields2.add(new WhereField(MtUserOrganization.FIELD_ORGANIZATION_TYPE, Comparison.EQUAL));
        whereFields2.add(new WhereField(MtUserOrganization.FIELD_ORGANIZATION_ID, Comparison.EQUAL));
        criteria2.where(whereFields2.toArray(new WhereField[whereFields2.size()]));
        if (mapper.selectOptional(union, criteria2).size() > 0) {
            throw new MtException("MT_PERMISSION_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PERMISSION_0005", "PERMISSION"));
        }
        // 查询同类型组织同用户是否存在多个默认
        if (YES.equals(mtUserOrganization.getDefaultOrganizationFlag())) {
            MtUserOrganization uomQuery = new MtUserOrganization();
            uomQuery.setTenantId(mtUserOrganization.getTenantId());
            uomQuery.setUserId(mtUserOrganization.getUserId());
            uomQuery.setOrganizationType(mtUserOrganization.getOrganizationType());
            uomQuery.setDefaultOrganizationFlag(YES);
            if (StringUtils.isNotEmpty(mtUserOrganization.getUserOrganizationId())) {
                uomQuery.setUserOrganizationId(mtUserOrganization.getUserOrganizationId());
            }
            Criteria criteria = new Criteria(uomQuery);
            List<WhereField> whereFields = new ArrayList<WhereField>();
            if (StringUtils.isNotEmpty(mtUserOrganization.getUserOrganizationId())) {
                whereFields.add(new WhereField(MtUserOrganization.FIELD_USER_ORGANIZATION_ID, Comparison.NOT_EQUAL));
            }
            whereFields.add(new WhereField(MtUserOrganization.FIELD_TENANT_ID, Comparison.EQUAL));
            whereFields.add(new WhereField(MtUserOrganization.FIELD_USER_ID, Comparison.EQUAL));
            whereFields.add(new WhereField(MtUserOrganization.FIELD_ORGANIZATION_TYPE, Comparison.EQUAL));
            whereFields.add(new WhereField(MtUserOrganization.FIELD_DEFAULT_ORGANIZATION_FLAG, Comparison.EQUAL));
            criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
            Optional<MtUserOrganization> first = mapper.selectOptional(uomQuery, criteria).stream().findFirst();
            Boolean isExist = false;

            // 判断对应的组织类型是否存在
            if (first.isPresent()) {
                MtUserOrganization defaultOrganization = first.get();
                switch (defaultOrganization.getOrganizationType()) {
                    case MtBaseConstants.ORGANIZATION_TYPE.ENTERPRISE:
                        MtModEnterprise mtModEnterprise = mtModEnterpriseRepository.enterpriseBasicPropertyGet(
                                        defaultOrganization.getTenantId(), defaultOrganization.getOrganizationId());
                        if (mtModEnterprise != null) {
                            isExist = true;
                        }
                        break;
                    case MtBaseConstants.ORGANIZATION_TYPE.SITE:
                        MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(
                                        defaultOrganization.getTenantId(), defaultOrganization.getOrganizationId());
                        if (mtModSite != null) {
                            isExist = true;
                        }
                        break;
                    case MtBaseConstants.ORGANIZATION_TYPE.AREA:
                        MtModArea mtModArea = mtModAreaRepository.areaBasicPropertyGet(
                                        defaultOrganization.getTenantId(), defaultOrganization.getOrganizationId());
                        if (mtModArea != null) {
                            isExist = true;
                        }
                        break;
                    case MtBaseConstants.ORGANIZATION_TYPE.PROD_LINE:
                        MtModProductionLine mtModProductionLine = mtModProductionLineRepository
                                        .prodLineBasicPropertyGet(defaultOrganization.getTenantId(),
                                                        defaultOrganization.getOrganizationId());
                        if (mtModProductionLine != null) {
                            isExist = true;
                        }
                        break;
                    case MtBaseConstants.ORGANIZATION_TYPE.WORKCELL:
                        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(
                                        defaultOrganization.getTenantId(), defaultOrganization.getOrganizationId());
                        if (mtModWorkcell != null) {
                            isExist = true;
                        }
                        break;
                    case MtBaseConstants.ORGANIZATION_TYPE.LOCATOR:
                        MtModLocator mtModLocator = mtModLocatorRepository.locatorBasicPropertyGet(
                                        defaultOrganization.getTenantId(), defaultOrganization.getOrganizationId());
                        if (mtModLocator != null) {
                            isExist = true;
                        }
                        break;
                    default:
                        break;
                }
            }
            if (isExist) {
                // 查询类型对应字段的中文描述
                MtGenType type = mtGenTypeRepository.getGenType(tenantId, "PERMISSION", "USER_ORGANIZATION_TYPE",
                                dto.getOrganizationType());
                String desc = dto.getOrganizationType();
                if (type != null && StringUtils.isNotEmpty(type.getDescription())) {
                    desc = type.getDescription();
                }
                throw new MtException("MT_PERMISSION_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_PERMISSION_0004", "PERMISSION", desc));
            }
        }
        // 判断是否存在，存在修改，不存在新增
        if (StringUtils.isEmpty(dto.getUserOrganizationId())) {
            this.insertSelective(mtUserOrganization);
        } else {
            this.updateByPrimaryKeySelective(mtUserOrganization);
        }
        return mtUserOrganization;
    }

    @Override
    public Page<MtOrganizationVO> mtOrganizationSearch(Long tenantId, PageRequest pageRequest, MtOrganizationDTO dto) {
        return PageHelper.doPage(pageRequest, () -> mapper.mtOrganizationSearch(tenantId, dto));
    }

    /**
     * UI查询用户权限组织LOV
     *
     * @author chuang.yang
     * @date 2019/12/10
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @return io.choerodon.core.domain.Page<tarzan.general.api.dto.MtUserOrganizationDTO5>
     */
    @Override
    public Page<MtUserOrganizationDTO4> userOrganizationLovForUi(Long tenantId, PageRequest pageRequest,
                    MtUserOrganizationDTO5 dto) {
        MtUserOrganizationDTO4 condition = new MtUserOrganizationDTO4();
        condition.setOrganizationType(dto.getOrganizationType());
        condition.setOrganizationCode(dto.getOrganizationCode());
        condition.setOrganizationDesc(dto.getOrganizationDesc());
        return PageHelper.doPage(pageRequest, () -> mapper.mtUserOrgnanizationSearch(tenantId, condition,
                        Arrays.asList(DetailsHelper.getUserDetails().getUserId())));
    }

    /**
     * UI用户组织对象关系查询(LOV)
     *
     * @author chuang.yang
     * @date 2019/12/11
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @return io.choerodon.core.domain.Page<tarzan.general.api.dto.MtUserOrganizationDTO4>
     */
    @Override
    public Page<MtUserOrganizationDTO4> userOrganizationRelLovForUi(Long tenantId, PageRequest pageRequest,
                    MtUserOrganizationDTO5 dto) {
        MtUserOrganizationDTO4 condition = new MtUserOrganizationDTO4();
        condition.setOrganizationType(dto.getOrganizationType());
        condition.setOrganizationCode(dto.getOrganizationCode());
        condition.setOrganizationDesc(dto.getOrganizationDesc());
        return PageHelper.doPage(pageRequest, () -> mapper.mtUserOrgnanizationRelQuery(tenantId, condition,
                        DetailsHelper.getUserDetails().getUserId(), dto.getSiteId()));
    }

    @Override
    public MtUserOrganizationDTO6 userDefaultSiteForUi(Long tenantId) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        List<MtUserOrganizationDTO6> result = mapper.userSiteListForUi(tenantId, userId, MtBaseConstants.YES);

        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public List<MtUserOrganizationDTO6> userSiteListForUi(Long tenantId) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        return mapper.userSiteListForUi(tenantId, userId, null);
    }

}
