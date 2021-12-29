package io.tarzan.common.app.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtNumrangeAssignHisDTO;
import io.tarzan.common.app.service.MtNumrangeAssignHisService;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.MtNumrangeAssignHisVO;
import io.tarzan.common.infra.mapper.MtNumrangeAssignHisMapper;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

/**
 * 号码段分配历史表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
@Service
public class MtNumrangeAssignHisServiceImpl implements MtNumrangeAssignHisService {

    @Autowired
    private MtNumrangeAssignHisMapper mapper;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtUserClient userClient;

    @Override
    public Page<MtNumrangeAssignHisVO> listNumrangeAssignHisForUi(Long tenantId, MtNumrangeAssignHisDTO condition,
                                                                  PageRequest pageRequest) {
        Page<MtNumrangeAssignHisVO> page =
                        PageHelper.doPage(pageRequest, () -> mapper.selectByConditionCustom(tenantId, condition));

        Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId,
                        page.getContent().stream().map(MtNumrangeAssignHisVO::getUserId).collect(Collectors.toList()));
        List<MtModSite> sites = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId,
                        page.getContent().stream().map(MtNumrangeAssignHisVO::getSiteId).collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            for (MtNumrangeAssignHisVO one : page.getContent()) {
                // 将用户信息设置到对象中
                if (MapUtils.isNotEmpty(userInfoMap) && userInfoMap.containsKey(one.getUserId())) {
                    one.setCreateBy(userInfoMap.get(one.getUserId()).getLoginName());
                }
                // 将站点信息设置到对象中
                if (CollectionUtils.isNotEmpty(sites)) {
                    Optional<MtModSite> siteOp =
                                    sites.stream().filter(t -> t.getSiteId().equals(one.getSiteId())).findFirst();
                    if (siteOp.isPresent()) {
                        one.setSite(siteOp.get().getSiteCode());
                        one.setSiteDesc(siteOp.get().getSiteName());
                    }
                }
            }
        }

        return page;
    }

}
