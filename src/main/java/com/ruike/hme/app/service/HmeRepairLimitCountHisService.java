package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeRepairLimitCountHisVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 返修进站限制次数历史表表应用服务
 *
 * @author sanfeng.zhang@hand-china.com 2021-09-13 16:41:22
 */
public interface HmeRepairLimitCountHisService {

    Page<HmeRepairLimitCountHisVO> list(Long tenantId, PageRequest pageRequest, String repairLimitCountId);
}
