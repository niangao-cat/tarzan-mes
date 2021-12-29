package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.vo.HmeNcCodeRouterRelHisVO;
import com.ruike.hme.infra.mapper.HmeNcCodeRouterRelHisMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeNcCodeRouterRelHis;
import com.ruike.hme.domain.repository.HmeNcCodeRouterRelHisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 不良代码工艺路线关系历史表 资源库实现
 *
 * @author penglin.sui@hand-china.com 2021-03-30 15:50:41
 */
@Component
public class HmeNcCodeRouterRelHisRepositoryImpl extends BaseRepositoryImpl<HmeNcCodeRouterRelHis> implements HmeNcCodeRouterRelHisRepository {

    @Autowired
    private HmeNcCodeRouterRelHisMapper hmeNcCodeRouterRelHisMapper;

    @Override
    public Page<HmeNcCodeRouterRelHisVO> ncCodeRouterRelHisList(Long tenantId, String ncCodeRouterRelId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeNcCodeRouterRelHisMapper.ncCodeRouterRelHisQuery(tenantId,ncCodeRouterRelId));
    }
}
