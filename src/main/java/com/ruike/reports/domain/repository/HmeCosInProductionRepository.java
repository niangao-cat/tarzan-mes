package com.ruike.reports.domain.repository;

import com.ruike.reports.api.dto.HmeCosInProductionDTO;
import com.ruike.reports.domain.vo.HmeCosInProductionVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * description
 *
 * @author 35113 2021/01/27 12:44
 */
public interface HmeCosInProductionRepository {

    /**
     * COS在制报表查询
     *
     * @param tenant
     * @param hmeCosInProductionDTO
     * @param pagerequest
     * @return io.choerodon.core.domain.Page<com.ruike.reports.domain.vo.HmeCosInProductionVO>
     * @auther wenqiang.yin@hand-china.com 2021/1/27 12:49
    */
    Page<HmeCosInProductionVO> pageList(Long tenant, HmeCosInProductionDTO hmeCosInProductionDTO, PageRequest pagerequest);
}
