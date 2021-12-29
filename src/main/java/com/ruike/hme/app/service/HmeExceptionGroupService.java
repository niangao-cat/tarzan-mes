package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeExceptionGroupDTO;
import com.ruike.hme.domain.entity.HmeExceptionGroup;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 异常收集组基础数据表应用服务
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:24
 */
public interface HmeExceptionGroupService {
    Page<HmeExceptionGroup> listForUi(Long tenantId, HmeExceptionGroupDTO dto, PageRequest pageRequest);

    HmeExceptionGroup saveForUi(Long tenantId, HmeExceptionGroup dto);
}
