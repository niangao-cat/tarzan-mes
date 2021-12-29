package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeNcCodeRouterRelDTO;
import com.ruike.hme.domain.vo.HmeNcCodeRouterRelVO;
import com.ruike.hme.infra.mapper.HmeNcCodeRouterRelMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeNcCodeRouterRel;
import com.ruike.hme.domain.repository.HmeNcCodeRouterRelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 不良代码工艺路线关系表 资源库实现
 *
 * @author penglin.sui@hand-china.com 2021-03-30 15:50:41
 */
@Component
public class HmeNcCodeRouterRelRepositoryImpl extends BaseRepositoryImpl<HmeNcCodeRouterRel> implements HmeNcCodeRouterRelRepository {

    @Autowired
    private HmeNcCodeRouterRelMapper hmeNcCodeRouterRelMapper;

    @Override
    @ProcessLovValue
    public Page<HmeNcCodeRouterRelVO> ncCodeRouterRelList(Long tenantId, HmeNcCodeRouterRelDTO dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeNcCodeRouterRelMapper.ncCodeRouterRelQuery(tenantId,dto));
    }
}
