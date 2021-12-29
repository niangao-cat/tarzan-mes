package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosWipQueryDTO;
import com.ruike.hme.app.service.HmeCosWipQueryService;
import com.ruike.hme.domain.vo.HmeCosWipQueryVO;
import com.ruike.hme.infra.mapper.HmeCosWipQueryMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HmeCosWipQueryServiceImpl implements HmeCosWipQueryService {

    @Autowired
    private HmeCosWipQueryMapper hmeCosWipQueryMapper;

    @Override
    @ProcessLovValue
    public Page<HmeCosWipQueryVO> propertyCosWipQuery(Long tenantId, HmeCosWipQueryDTO dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () ->hmeCosWipQueryMapper.cosWipQuery(tenantId,dto));
    }
}
