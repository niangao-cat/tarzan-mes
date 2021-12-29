package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeTagPassRateHeaderAndLineHisService;
import com.ruike.hme.domain.vo.HmeTagPassRateHeaderAndLineHisVO;
import com.ruike.hme.infra.mapper.HmeTagPassRateHeaderAndLineHisMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 偏振度和发散角良率维护头历史表应用服务默认实现
 *
 * @author wengang.qiang@hand-china.com 2021/09/14 14:09
 */
@Service
public class HmeTagPassRateHeaderAndLineHisServiceImpl implements HmeTagPassRateHeaderAndLineHisService {

    private final HmeTagPassRateHeaderAndLineHisMapper tagPassRateHeaderAndLineHisMapper;
    private final MtUserClient mtUserClient;

    public HmeTagPassRateHeaderAndLineHisServiceImpl(HmeTagPassRateHeaderAndLineHisMapper tagPassRateHeaderAndLineHisMapper, MtUserClient mtUserClient) {
        this.tagPassRateHeaderAndLineHisMapper = tagPassRateHeaderAndLineHisMapper;
        this.mtUserClient = mtUserClient;
    }

    @Override
    @ProcessLovValue
    public Page<HmeTagPassRateHeaderAndLineHisVO> queryTagPassRateHeaderAndLineHis(Long tenantId, PageRequest pageRequest, String heardId) {
        Page<HmeTagPassRateHeaderAndLineHisVO> page = PageHelper.doPage(pageRequest, () -> tagPassRateHeaderAndLineHisMapper.queryTagPassRateHeaderAndLineHis(tenantId, heardId));
        List<Long> userId = new ArrayList<>();
        page.getContent().forEach(hmeTagPassRateHeaderAndLineHisVO -> {
            userId.add(hmeTagPassRateHeaderAndLineHisVO.getLastUpdatedBy());
        });
        List<Long> userIdList = userId.stream().distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userIdList)) {
            userInfoMap = mtUserClient.userInfoBatchGet(tenantId, userIdList);
        }
        for (HmeTagPassRateHeaderAndLineHisVO hmeTagPassRateHeaderAndLineHisVO : page.getContent()) {
            hmeTagPassRateHeaderAndLineHisVO.setLastUpdatedByName(userInfoMap.getOrDefault(hmeTagPassRateHeaderAndLineHisVO.getLastUpdatedBy(),
                    new MtUserInfo()).getRealName());
        }

        return page;
    }
}
