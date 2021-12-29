package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeException;
import com.ruike.hme.api.dto.HmeExceptionDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 异常维护基础数据头表应用服务
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:25
 */
public interface HmeExceptionService {
    Page<HmeException> listForUi(Long tenantId, HmeExceptionDTO dto, PageRequest pageRequest);

    HmeException saveForUi(Long tenantId, HmeException dto);
}
