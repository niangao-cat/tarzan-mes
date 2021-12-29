package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.vo.HmeProcessNcHeaderVO;
import com.ruike.hme.domain.vo.HmeProcessNcLineVO;
import com.ruike.hme.infra.mapper.HmeProcessNcLineMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeProcessNcLine;
import com.ruike.hme.domain.repository.HmeProcessNcLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工序不良行表 资源库实现
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
@Component
public class HmeProcessNcLineRepositoryImpl extends BaseRepositoryImpl<HmeProcessNcLine> implements HmeProcessNcLineRepository {

    @Autowired
    private HmeProcessNcLineMapper hmeProcessNcLineMapper;

    @Override
    public Page<HmeProcessNcLineVO> selectLine(Long tenantId, String headerId, PageRequest pageRequest) {
        Page<HmeProcessNcLineVO> page = PageHelper.doPage(pageRequest, () -> hmeProcessNcLineMapper.selectProcessLine(tenantId,headerId));
        return page;
    }

    @Override
    public void deleteLineByHeader(Long tenantId, String headerId) {
        hmeProcessNcLineMapper.deleteLineByHeader(tenantId,headerId);
    }
}
