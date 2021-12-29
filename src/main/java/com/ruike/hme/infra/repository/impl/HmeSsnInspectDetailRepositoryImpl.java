package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeSsnInspectLine;
import com.ruike.hme.domain.vo.HmeSsnInspectDetailVO;
import com.ruike.hme.infra.mapper.HmeSsnInspectDetailMapper;
import com.ruike.hme.infra.mapper.HmeSsnInspectHeaderMapper;
import com.ruike.hme.infra.mapper.HmeSsnInspectLineMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeSsnInspectDetail;
import com.ruike.hme.domain.repository.HmeSsnInspectDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 标准件检验标准明细 资源库实现
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:11
 */
@Component
public class HmeSsnInspectDetailRepositoryImpl extends BaseRepositoryImpl<HmeSsnInspectDetail> implements HmeSsnInspectDetailRepository {

    @Autowired
    private HmeSsnInspectDetailMapper hmeSsnInspectDetailMapper;
    @Autowired
    private HmeSsnInspectLineMapper hmeSsnInspectLineMapper;

    @Override
    public Page<HmeSsnInspectDetailVO> selectDetail(Long tenantId, PageRequest pageRequest, String ssnInspectLineId) {
        Page<HmeSsnInspectDetailVO> page = PageHelper.doPage(pageRequest, () -> hmeSsnInspectDetailMapper.selectDetail(tenantId,ssnInspectLineId));
        return page;
    }

    @Override
    public void deleteByLine(Long tenantId, String ssnInspectLineId) {
        hmeSsnInspectDetailMapper.deleteByLine(tenantId,ssnInspectLineId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDetailByHeader(Long tenantId, String ssnInspectHeaderId) {
        List<HmeSsnInspectLine> hmeSsnInspectLines = hmeSsnInspectLineMapper.selectByHeader(tenantId,ssnInspectHeaderId);
        for(HmeSsnInspectLine a : hmeSsnInspectLines){
            hmeSsnInspectDetailMapper.deleteByLine(tenantId,a.getSsnInspectLineId());
        }
    }
}
