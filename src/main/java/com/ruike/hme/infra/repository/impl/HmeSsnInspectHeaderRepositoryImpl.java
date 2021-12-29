package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.vo.HmeSsnInspectExportVO;
import com.ruike.hme.domain.vo.HmeSsnInspectHeaderVO;
import com.ruike.hme.domain.vo.HmeSsnInspectImportVO;
import com.ruike.hme.infra.mapper.HmeSsnInspectHeaderMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeSsnInspectHeader;
import com.ruike.hme.domain.repository.HmeSsnInspectHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 标准件检验标准头 资源库实现
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:11
 */
@Component
public class HmeSsnInspectHeaderRepositoryImpl extends BaseRepositoryImpl<HmeSsnInspectHeader> implements HmeSsnInspectHeaderRepository {

    @Autowired
    private HmeSsnInspectHeaderMapper hmeSsnInspectHeaderMapper;

    @Override
    @ProcessLovValue
    public Page<HmeSsnInspectHeaderVO> selectHeader(Long tenantId, PageRequest pageRequest, HmeSsnInspectHeader hmeSsnInspectHeader) {
        Page<HmeSsnInspectHeaderVO> page = PageHelper.doPage(pageRequest, () -> hmeSsnInspectHeaderMapper.selectHeader(tenantId, hmeSsnInspectHeader));
        return page;
    }

    @Override
    @ProcessLovValue
    public Page<HmeSsnInspectHeaderVO> ssnInspectHeaderHisQuery(Long tenantId, String ssnInspectHeaderId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeSsnInspectHeaderMapper.ssnInspectHeaderHisQuery(tenantId, ssnInspectHeaderId));
    }

    @Override
    @ProcessLovValue
    public List<HmeSsnInspectExportVO> ssnInspectExport(Long tenantId, HmeSsnInspectHeader hmeSsnInspectHeader) {
        return hmeSsnInspectHeaderMapper.ssnInspectExport(tenantId, hmeSsnInspectHeader);
    }
}
