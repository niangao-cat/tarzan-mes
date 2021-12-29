package com.ruike.wms.infra.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ruike.wms.api.dto.WmsSiteDTO;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * WmsSiteRepositoryImpl
 *
 * @author liyuan.lv@hand-china.com 2020/06/17 15:51
 */
@Component
public class WmsSiteRepositoryImpl implements WmsSiteRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Override
    public String userDefaultSite(Long tenantId) {
        // 获取用户默认站点
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        MtUserOrganization userOrganization = new MtUserOrganization();
        userOrganization.setUserId(userId);
        userOrganization.setOrganizationType("SITE");
        MtUserOrganization defaultSite =
                mtUserOrganizationRepository.userDefaultOrganizationGet(tenantId, userOrganization);
        if (null == defaultSite) {
            throw new MtException("MT_PERMISSION_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_PERMISSION_0006", "PERMISSION", "【API:defaultSiteUi】"));
        }
        return defaultSite.getOrganizationId();
    }

    @Override
    public List<WmsSiteDTO> getSite(Long tenantId) {
        MtUserOrganization mtUserOrganization = new MtUserOrganization();
        List<MtUserOrganization> mtUserOrganizationList = new ArrayList<>();
        List<String> siteIdList = new ArrayList<>();
        List<MtModSite> mtModSiteList = new ArrayList<>();
        List<WmsSiteDTO> siteList = new ArrayList<>();
        mtUserOrganization.setUserId(DetailsHelper.getUserDetails().getUserId());
        mtUserOrganization.setOrganizationType("SITE");
        mtUserOrganizationList = mtUserOrganizationRepository.userOrganizationPermissionQuery(tenantId, mtUserOrganization);
        siteIdList = mtUserOrganizationList.stream().map(MtUserOrganization::getOrganizationId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(siteIdList)) {
            return siteList;
        }
        mtModSiteList = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, siteIdList);
        for (MtModSite mtModSite : mtModSiteList) {
            WmsSiteDTO site = new WmsSiteDTO();
            BeanUtils.copyProperties(mtModSite, site);
            site.setDescription(mtModSite.getSiteName());
            siteList.add(site);
        }
        return siteList;
    }
}
