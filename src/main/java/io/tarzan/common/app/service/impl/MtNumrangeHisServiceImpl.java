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
import io.tarzan.common.api.dto.MtNumrangeHisDTO;
import io.tarzan.common.app.service.MtNumrangeHisService;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.MtNumrangeHisVO;
import io.tarzan.common.infra.mapper.MtNumrangeHisMapper;

/**
 * 号码段定义历史表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
@Service
public class MtNumrangeHisServiceImpl implements MtNumrangeHisService {
    @Autowired
    private MtNumrangeHisMapper mtNumrangeHisMapper;
    @Autowired
    private MtUserClient userClient;

    @Override
    public Page<MtNumrangeHisVO> numrangeHisQueryForUi(Long tenantId, MtNumrangeHisDTO dto, PageRequest pageRequest) {
        Page<MtNumrangeHisVO> page =
                        PageHelper.doPage(pageRequest, () -> mtNumrangeHisMapper.numrangeHisQueryForUi(tenantId, dto));
        // 调用服务批量获取用户信息
        Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId,
                        page.getContent().stream().map(MtNumrangeHisVO::getEventBy).collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            for (MtNumrangeHisVO one : page.getContent()) {
                // 将用户信息设置到对象中
                if (MapUtils.isNotEmpty(userInfoMap) && userInfoMap.containsKey(one.getEventBy())) {
                    one.setEventByName(userInfoMap.get(one.getEventBy()).getLoginName());
                }
            }
        }
        return page;
    }
}
