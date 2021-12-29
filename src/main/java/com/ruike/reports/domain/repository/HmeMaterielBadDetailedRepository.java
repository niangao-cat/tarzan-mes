package com.ruike.reports.domain.repository;

import com.ruike.reports.api.dto.HmeMaterielBadDetailedDTO;
import com.ruike.reports.domain.vo.HmeMaterielBadDetailedVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 材料不良明细报表
 *
 * @author wenqiang.yin@hand-china.com 2021/02/02 12:39
 */
public interface HmeMaterielBadDetailedRepository {

    /**
     * 材料不良明细报表查询
     * 
     * @param tenant
     * @param hmeMaterielBadDetailedDTO
     * @param pagerequest
     * @return io.choerodon.core.domain.Page<com.ruike.reports.domain.vo.ItfMaterielBadDetailedVO>
     * @auther wenqiang.yin@hand-china.com 2021/2/2 12:57
    */        
    Page<HmeMaterielBadDetailedVO> pageList(Long tenant, HmeMaterielBadDetailedDTO hmeMaterielBadDetailedDTO, PageRequest pagerequest);
}
