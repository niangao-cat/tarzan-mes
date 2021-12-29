package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeWoDispatchRecode;
import com.ruike.hme.domain.repository.HmeWoDispatchRecodeRepository;
import com.ruike.hme.domain.vo.HmeWoDispatchWkcVO;
import com.ruike.hme.infra.mapper.HmeWoDispatchRecodeMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 工单派工记录表 资源库实现
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */
@Component
public class HmeWoDispatchRecodeRepositoryImpl extends BaseRepositoryImpl<HmeWoDispatchRecode> implements HmeWoDispatchRecodeRepository {

    private final HmeWoDispatchRecodeMapper hmeWoDispatchRecodeMapper;

    public HmeWoDispatchRecodeRepositoryImpl(HmeWoDispatchRecodeMapper hmeWoDispatchRecodeMapper) {
        this.hmeWoDispatchRecodeMapper = hmeWoDispatchRecodeMapper;
    }

    @Override
    public List<HmeWoDispatchWkcVO> selectDispatchDetailList(Long tenantId, String topSiteId, String prodLineId, Long userId, List<String> workOrderIdList) {
        return hmeWoDispatchRecodeMapper.selectDispatchDetailList(tenantId, topSiteId, prodLineId, userId, workOrderIdList);
    }
}
