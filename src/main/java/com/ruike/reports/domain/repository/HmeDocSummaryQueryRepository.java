package com.ruike.reports.domain.repository;

import com.ruike.reports.api.dto.HmeDocSummaryQueryDTO;
import com.ruike.reports.domain.vo.HmeDocSummaryQueryVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

public interface HmeDocSummaryQueryRepository {

    Page<HmeDocSummaryQueryVO> pageList(Long tenantId, HmeDocSummaryQueryDTO dto, PageRequest pageRequest);
}
