package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeCosTestMonitorRecordService;
import com.ruike.hme.domain.vo.HmeCosTestMonitorRecordVO;
import com.ruike.hme.infra.mapper.HmeCosTestMonitorRecordMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * COS测试良率监控记录表应用服务默认实现
 *
 * @author wengang.qiang@hand-china.com 2021-09-16 14:29:14
 */
@Service
public class HmeCosTestMonitorRecordServiceImpl extends BaseAppService implements HmeCosTestMonitorRecordService {

    private final HmeCosTestMonitorRecordMapper hmeCosTestMonitorRecordMapper;
    private final MtUserClient mtUserClient;

    @Autowired
    public HmeCosTestMonitorRecordServiceImpl(HmeCosTestMonitorRecordMapper hmeCosTestMonitorRecordMapper,
                                              MtUserClient mtUserClient) {
        this.hmeCosTestMonitorRecordMapper = hmeCosTestMonitorRecordMapper;
        this.mtUserClient = mtUserClient;
    }

    @Override
    @ProcessLovValue
    public Page<HmeCosTestMonitorRecordVO> queryCosTestMonitorRecord(Long tenantId, String cosMonitorHeaderId, PageRequest pageRequest) {
        Page<HmeCosTestMonitorRecordVO> page = PageHelper.doPage(pageRequest, () -> hmeCosTestMonitorRecordMapper.queryCosTestMonitorRecord(tenantId, cosMonitorHeaderId));
        List<Long> userIdList = new ArrayList<>();
        page.getContent().forEach(hmeCosTestMonitorRecordVO -> {
            userIdList.add(hmeCosTestMonitorRecordVO.getPassBy());
        });
        List<Long> distinctList = userIdList.stream().distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(distinctList)) {
            userInfoMap = mtUserClient.userInfoBatchGet(tenantId, distinctList);
        }
        for (HmeCosTestMonitorRecordVO hmeCosTestMonitorRecordVO : page.getContent()) {
            //设置放行人姓名
            hmeCosTestMonitorRecordVO.setPassByName(userInfoMap.getOrDefault(hmeCosTestMonitorRecordVO.getPassBy(), new MtUserInfo()).getRealName());
        }
        return page;
    }
}
