package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.vo.HmeSsnInspectLineVO;
import com.ruike.hme.infra.mapper.HmeSsnInspectLineMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeSsnInspectLine;
import com.ruike.hme.domain.repository.HmeSsnInspectLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 标准件检验标准行 资源库实现
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:10
 */
@Component
public class HmeSsnInspectLineRepositoryImpl extends BaseRepositoryImpl<HmeSsnInspectLine> implements HmeSsnInspectLineRepository {

    @Autowired
    private HmeSsnInspectLineMapper hmeSsnInspectLineMapper;

    @Override
    public Page<HmeSsnInspectLineVO> selectLine(Long tenantId, PageRequest pageRequest, String ssnInspectHeaderId) {
        Page<HmeSsnInspectLineVO> page = PageHelper.doPage(pageRequest, () -> hmeSsnInspectLineMapper.selectLine(tenantId, ssnInspectHeaderId));
        return page;
    }

    @Override
    public void deleteLineByHeade(Long tenantId, String ssnInspectHeaderId) {
        hmeSsnInspectLineMapper.deleteLineByHeade(tenantId, ssnInspectHeaderId);
    }

    @Override
    @ProcessLovValue
    public Page<HmeSsnInspectLineVO> ssnInspectLineHisQuery(Long tenantId, String ssnInspectLineId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeSsnInspectLineMapper.ssnInspectLineHisQuery(tenantId, ssnInspectLineId));

    }
}
