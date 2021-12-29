package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeCosTestPassRateHisService;
import com.ruike.hme.domain.vo.HmeCosTestPassRateHisVO;
import com.ruike.hme.infra.mapper.HmeCosTestPassRateHisMapper;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseAppService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * COS测试良率维护历史表应用服务默认实现
 *
 * @author wengang.qiang@hand-china.com 2021-09-06 11:44:40
 */
@Service
public class HmeCosTestPassRateHisServiceImpl extends BaseAppService implements HmeCosTestPassRateHisService {

    private final HmeCosTestPassRateHisMapper hmeCosTestPassRateHisMapper;
    private final MtUserClient mtUserClient;

    public HmeCosTestPassRateHisServiceImpl(HmeCosTestPassRateHisMapper hmeCosTestPassRateHisMapper, MtUserClient mtUserClient) {
        this.hmeCosTestPassRateHisMapper = hmeCosTestPassRateHisMapper;
        this.mtUserClient = mtUserClient;
    }


    @Override
    @ProcessLovValue
    public List<HmeCosTestPassRateHisVO> queryHmeCosTestPassRateHis(Long tenantId, String testId) {
        List<HmeCosTestPassRateHisVO> hmeCosTestPassRateHisVOList = hmeCosTestPassRateHisMapper.queryHmeCosTestPassRateHis(tenantId, testId);
        List<Long> userId = new ArrayList<>();
        hmeCosTestPassRateHisVOList.forEach(hmeCosTestPassRateHisVO -> {
            userId.add(hmeCosTestPassRateHisVO.getCreatedBy());
            userId.add(hmeCosTestPassRateHisVO.getLastUpdatedBy());
        });
        List<Long> userIdList = userId.stream().distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userIdList)) {
            userInfoMap = mtUserClient.userInfoBatchGet(tenantId, userIdList);
        }
        for (HmeCosTestPassRateHisVO hmeCosTestPassRateHisVO : hmeCosTestPassRateHisVOList) {
            hmeCosTestPassRateHisVO.setCreateByName(userInfoMap.getOrDefault(hmeCosTestPassRateHisVO.getCreatedBy(), new MtUserInfo()).getRealName());
            hmeCosTestPassRateHisVO.setLasteUpdateByName(userInfoMap.getOrDefault(hmeCosTestPassRateHisVO.getLastUpdatedBy(), new MtUserInfo()).getRealName());
        }
        return hmeCosTestPassRateHisVOList;
    }
}
