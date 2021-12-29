package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeRepairLimitCountHisService;
import com.ruike.hme.domain.repository.HmeRepairLimitCountHisRepository;
import com.ruike.hme.domain.vo.HmeRepairLimitCountHisVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 返修进站限制次数历史表表应用服务默认实现
 *
 * @author sanfeng.zhang@hand-china.com 2021-09-13 16:41:22
 */
@Service
public class HmeRepairLimitCountHisServiceImpl implements HmeRepairLimitCountHisService {

    @Autowired
    private HmeRepairLimitCountHisRepository repairLimitCountHisRepository;

    @Override
    public Page<HmeRepairLimitCountHisVO> list(Long tenantId, PageRequest pageRequest, String repairLimitCountId) {
        return repairLimitCountHisRepository.list(tenantId, pageRequest, repairLimitCountId);
    }
}
