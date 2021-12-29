package com.ruike.wms.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ruike.wms.api.dto.SiteDTO;
import com.ruike.wms.api.dto.WmsLocatorDTO;
import com.ruike.wms.api.dto.WmsWarehouseDTO;
import com.ruike.wms.app.service.WmsWarehouseLocatorService;
import com.ruike.wms.domain.repository.WmsWarehouseLocatorRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * WmsWarehouseLocatorServiceImpl
 *
 * @author liyuan.lv@hand-china.com 2020/04/30 23:44
 */
@Service
@Slf4j
public class WmsWarehouseLocatorServiceImpl implements WmsWarehouseLocatorService {
    @Autowired
    private WmsCommonServiceComponent wmsCommonServiceComponent;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private WmsWarehouseLocatorRepository repository;
    @Autowired
    MtUserOrganizationRepository mtUserOrganizationRepository;

    @Override
    public Page<WmsWarehouseDTO> getWarehouse(Long tenantId, WmsWarehouseDTO dto, PageRequest pageRequest) {
        List<WmsWarehouseDTO> resultList = repository.getWarehouse(tenantId, dto);
        List<WmsWarehouseDTO> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
        return new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), resultList.size());
    }

    @Override
    public Page<WmsLocatorDTO> getLocator(Long tenantId, WmsLocatorDTO dto, PageRequest pageRequest) {
        List<WmsLocatorDTO> resultList = repository.getLocator(tenantId, dto);
        List<WmsLocatorDTO> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
        return new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), resultList.size());
    }

    @Override
    public MtModSite siteBasicPropertyGet(Long tenantId) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        MtUserOrganization userOrganization = wmsCommonServiceComponent.getUserOrganization(tenantId, userId);
        if (userOrganization == null) {
            log.error("<==== materialLotCreate MtUserOrganization is null");
            return null;
        }
        return mtModSiteRepository.siteBasicPropertyGet(tenantId,userOrganization.getOrganizationId());
    }

    @Override
    public List<SiteDTO> getSite(Long tenantId) {
        MtUserOrganization mtUserOrganization = new MtUserOrganization();
        List<MtUserOrganization> mtUserOrganizationList = new ArrayList<>();
        List<String> siteIdList = new ArrayList<>();
        List<MtModSite> mtModSiteList = new ArrayList<>();
        List<SiteDTO> siteList = new ArrayList<>();
        mtUserOrganization.setUserId(DetailsHelper.getUserDetails().getUserId());
        mtUserOrganization.setOrganizationType("SITE");
        mtUserOrganizationList = mtUserOrganizationRepository.userOrganizationPermissionQuery(tenantId, mtUserOrganization);
        siteIdList = mtUserOrganizationList.stream().map(MtUserOrganization::getOrganizationId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(siteIdList)) {
            return siteList;
        }
        mtModSiteList = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, siteIdList);
        for (MtModSite mtModSite : mtModSiteList) {
            SiteDTO site = new SiteDTO();
            BeanUtils.copyProperties(mtModSite, site);
            site.setDescription(mtModSite.getSiteName());
            siteList.add(site);
        }
        return siteList;
    }
}
