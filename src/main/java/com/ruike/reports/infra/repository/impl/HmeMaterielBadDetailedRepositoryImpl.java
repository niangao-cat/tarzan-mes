package com.ruike.reports.infra.repository.impl;

import com.ruike.reports.api.dto.HmeMaterielBadDetailedDTO;
import com.ruike.reports.domain.repository.HmeMaterielBadDetailedRepository;
import com.ruike.reports.domain.vo.HmeMaterielBadDetailedVO;
import com.ruike.reports.infra.mapper.HmeMaterielBadDetailedMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 材料不良明细报表
 *
 * @author wenqiang.yin@hand-china.com 2021/02/02 12:40
 */
@Component
public class HmeMaterielBadDetailedRepositoryImpl implements HmeMaterielBadDetailedRepository {

    @Autowired
    private HmeMaterielBadDetailedMapper hmeMaterielBadDetailedMapper;
    /**
     * 材料不良明细报表 资源库查询
     * 
     * @param tenantId
     * @param hmeMaterielBadDetailedDTO
     * @param pagerequest
     * @return io.choerodon.core.domain.Page<com.ruike.reports.domain.vo.ItfMaterielBadDetailedVO>
     * @auther wenqiang.yin@hand-china.com 2021/2/2 12:56
    */        
    @Override
    @ProcessLovValue
    public Page<HmeMaterielBadDetailedVO> pageList(Long tenantId, HmeMaterielBadDetailedDTO hmeMaterielBadDetailedDTO, PageRequest pagerequest) {
        return PageHelper.doPageAndSort(pagerequest, () -> hmeMaterielBadDetailedMapper.selectListByCondition(tenantId, hmeMaterielBadDetailedDTO));
    }
}
