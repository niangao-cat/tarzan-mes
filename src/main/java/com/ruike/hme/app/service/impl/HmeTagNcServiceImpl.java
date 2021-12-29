package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeTagNcDTO;
import com.ruike.hme.app.service.HmeTagNcService;
import com.ruike.hme.domain.entity.HmeTagNc;
import com.ruike.hme.domain.repository.HmeTagFormulaHeadRepository;
import com.ruike.hme.domain.repository.HmeTagNcRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 数据项不良判定基础表应用服务默认实现
 *
 * @author guiming.zhou@hand-china.com 2020-09-24 16:00:30
 */
@Service
public class HmeTagNcServiceImpl implements HmeTagNcService {

    @Autowired
    private HmeTagNcRepository hmeTagNcRepository;

    @Override
    @ProcessLovValue
    public Page<HmeTagNcDTO> getTagNcList(Long tenantId, HmeTagNc hmeTagNc, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> hmeTagNcRepository.getTagNcList(tenantId, hmeTagNc, pageRequest));
    }
}
