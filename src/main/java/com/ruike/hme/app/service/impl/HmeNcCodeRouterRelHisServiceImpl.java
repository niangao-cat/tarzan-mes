package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeNcCodeRouterRelHisService;
import com.ruike.hme.domain.repository.HmeNcCodeRouterRelHisRepository;
import com.ruike.hme.domain.vo.HmeNcCodeRouterRelHisVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 不良代码工艺路线关系历史表应用服务默认实现
 *
 * @author penglin.sui@hand-china.com 2021-03-30 15:50:41
 */
@Service
public class HmeNcCodeRouterRelHisServiceImpl implements HmeNcCodeRouterRelHisService {

    @Autowired
    private HmeNcCodeRouterRelHisRepository hmeNcCodeRouterRelHisRepository;

    @Override
    public Page<HmeNcCodeRouterRelHisVO> ncCodeRouterRelHisList(Long tenantId, String ncCodeRouterRelId, PageRequest pageRequest) {
        return this.hmeNcCodeRouterRelHisRepository.ncCodeRouterRelHisList(tenantId,ncCodeRouterRelId,pageRequest);
    }
}
