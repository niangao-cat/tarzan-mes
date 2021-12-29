package io.tarzan.common.app.service.impl;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.app.service.MtNumrangeRuleHisService;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.MtNumrangeRuleHisVO;
import io.tarzan.common.infra.mapper.MtNumrangeRuleHisMapper;

/**
 * 号码段定义组合规则历史表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:43
 */
@Service
public class MtNumrangeRuleHisServiceImpl implements MtNumrangeRuleHisService {
    @Autowired
    private MtNumrangeRuleHisMapper mtNumrangeRuleHisMapper;
    @Autowired
    private MtUserClient userClient;

    @Override
    public Page<MtNumrangeRuleHisVO> numrangeRuleHisQueryForUi(Long tenantId, String eventId, PageRequest pageRequest) {
        Page<MtNumrangeRuleHisVO> page = PageHelper.doPage(pageRequest,
                        () -> mtNumrangeRuleHisMapper.numrangeRuleHisQuery(tenantId, eventId));
        // 调用服务批量获取用户信息
        Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId,
                        page.getContent().stream().map(MtNumrangeRuleHisVO::getEventBy).collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            for (MtNumrangeRuleHisVO one : page.getContent()) {
                // 将用户信息设置到对象中
                if (MapUtils.isNotEmpty(userInfoMap) && userInfoMap.containsKey(one.getEventBy())) {
                    one.setEventByName(userInfoMap.get(one.getEventBy()).getLoginName());
                }
            }
        }
        return page;
    }
}
