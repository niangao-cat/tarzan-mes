package com.ruike.reports.infra.repository.impl;

import com.ruike.reports.api.dto.HmeSsnInspectResultDTO;
import com.ruike.reports.domain.repository.HmeSsnInspectResultRepository;
import com.ruike.reports.domain.vo.HmeSsnInspectResultHeaderLinesVO;
import com.ruike.reports.domain.vo.HmeSsnInspectResultHeaderVO;
import com.ruike.reports.domain.vo.HmeSsnInspectResultLineVO;
import com.ruike.reports.infra.mapper.HmeSsnInspectResultMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 标准件检验结果查询
 *
 * @author wenqiang.yin@hand-china.com 2021/02/04 8:34
 */
@Component
@Slf4j
public class HmeSsnInspectResultRepositoryImpl implements HmeSsnInspectResultRepository {

    @Autowired
    private HmeSsnInspectResultMapper hmeSsnInspectResultMapper;


    @Override
    @ProcessLovValue
    public Page<HmeSsnInspectResultHeaderLinesVO> pageHeaderLinesList(Long tenantId, HmeSsnInspectResultDTO dto, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> hmeSsnInspectResultMapper.selectHeaderLinesByCondition(tenantId, dto));
    }

    @Override
    @ProcessLovValue
    public List<HmeSsnInspectResultHeaderLinesVO> listExport(Long tenantId , HmeSsnInspectResultDTO dto) {
        List<HmeSsnInspectResultHeaderLinesVO> list = hmeSsnInspectResultMapper.selectHeaderLinesByCondition(tenantId, dto);
        log.info("==============================>"+list.toString());
        return list;
    }
}
