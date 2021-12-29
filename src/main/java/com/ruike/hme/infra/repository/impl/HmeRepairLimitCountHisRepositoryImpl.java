package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeRepairLimitCountHis;
import com.ruike.hme.domain.repository.HmeRepairLimitCountHisRepository;
import com.ruike.hme.domain.vo.HmeRepairLimitCountHisVO;
import com.ruike.hme.infra.mapper.HmeRepairLimitCountHisMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 返修进站限制次数历史表表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2021-09-13 16:41:22
 */
@Component
public class HmeRepairLimitCountHisRepositoryImpl extends BaseRepositoryImpl<HmeRepairLimitCountHis> implements HmeRepairLimitCountHisRepository {

    @Autowired
    private HmeRepairLimitCountHisMapper mapper;

    @Override
    @ProcessLovValue
    public Page<HmeRepairLimitCountHisVO> list(Long tenantId, PageRequest pageRequest, String repairLimitCountId) {
        return PageHelper.doPage(pageRequest, ()->mapper.list(tenantId, repairLimitCountId));
    }
}
