package tarzan.method.infra.repository.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.method.domain.entity.MtNcGroup;
import tarzan.method.domain.repository.MtNcGroupRepository;
import tarzan.method.domain.vo.MtNcGroupVO;
import tarzan.method.domain.vo.MtNcGroupVO1;
import tarzan.method.infra.mapper.MtNcGroupMapper;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

/**
 * 不良代码组 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
@Component
public class MtNcGroupRepositoryImpl extends BaseRepositoryImpl<MtNcGroup> implements MtNcGroupRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtNcGroupMapper mtNcGroupMapper;

    @Autowired
    private MtModSiteRepository iMtModSiteService;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;


    @Override
    public MtNcGroup ncGroupPropertyGet(Long tenantId, String ncGroupId) {
        if (StringUtils.isEmpty(ncGroupId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncGroupId", "【API:ncGroupPropertyGet】"));

        }
        MtNcGroup group = new MtNcGroup();
        group.setNcGroupId(ncGroupId);
        group.setTenantId(tenantId);
        return mtNcGroupMapper.selectOne(group);
    }

    @Override
    public List<MtNcGroupVO1> propertyLimitNcGroupPropertyQuery(Long tenantId, MtNcGroupVO dto) {
        List<MtNcGroupVO1> voList = mtNcGroupMapper.selectCondition(tenantId, dto);
        if (CollectionUtils.isEmpty(voList)) {
            return Collections.emptyList();
        }

        List<String> siteList = voList.stream().map(MtNcGroupVO1::getSiteId).filter(StringUtils::isNotEmpty).distinct()
                        .collect(Collectors.toList());
        Map<String, MtModSite> mtModSiteMap = new HashMap<>(100);
        if (CollectionUtils.isNotEmpty(siteList)) {
            List<MtModSite> mtModSites = iMtModSiteService.siteBasicPropertyBatchGet(tenantId, siteList);
            if (CollectionUtils.isNotEmpty(mtModSites)) {
                mtModSiteMap = mtModSites.stream().collect(Collectors.toMap(t -> t.getSiteId(), t -> t));

            }
        }

        for (MtNcGroupVO1 groupVo1 : voList) {
            groupVo1.setSiteCode(null == mtModSiteMap.get(groupVo1.getSiteId()) ? null
                            : mtModSiteMap.get(groupVo1.getSiteId()).getSiteCode());
            groupVo1.setSiteName(null == mtModSiteMap.get(groupVo1.getSiteId()) ? null
                            : mtModSiteMap.get(groupVo1.getSiteId()).getSiteName());

        }
        voList.sort(Comparator.comparing(c -> new BigDecimal(c.getNcGroupId())));
        return voList;
    }

    @Override
    public List<MtNcGroup> ncGroupPropertyBatchGet(Long tenantId, List<String> ncGroupList) {
        if (CollectionUtils.isEmpty(ncGroupList)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncGroupList", "【API:ncGroupPropertyBatchGet】"));

        }
        return mtNcGroupMapper.selectByIdsCustom(tenantId, ncGroupList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ncGroupAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "keyId", "【API:ncGroupAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtNcGroup ncGroup = new MtNcGroup();
        ncGroup.setTenantId(tenantId);
        ncGroup.setNcGroupId(dto.getKeyId());
        ncGroup = mtNcGroupMapper.selectOne(ncGroup);
        if (ncGroup == null) {
            throw new MtException("MT_NC_CODE_0007",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_NC_CODE_0007", "NC_CODE",
                                            dto.getKeyId(), "mt_nc_group", "【API:ncGroupAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_nc_group_attr", dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }
}
