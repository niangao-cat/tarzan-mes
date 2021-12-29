package com.ruike.qms.app.service.impl;

import com.ruike.qms.api.dto.QmsPqcHeaderQueryDTO;
import com.ruike.qms.app.service.QmsPqcDocService;
import com.ruike.qms.domain.vo.QmsPqcHeaderQueryVO;
import com.ruike.qms.domain.vo.QmsPqcLineDetailsVO;
import com.ruike.qms.domain.vo.QmsPqcLineQueryVO;
import com.ruike.qms.infra.mapper.QmsPqcDocMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 巡检单查询应用服务默认实现
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/05 12:17
 */
@Service
public class QmsPqcDocServiceImpl implements QmsPqcDocService {

    @Autowired
    private QmsPqcDocMapper qmsPqcDocMapper;

    @Override
    @ProcessLovValue
    public Page<QmsPqcHeaderQueryVO> qmsPqcDocQueryHeader(PageRequest pageRequest, Long tenantId, QmsPqcHeaderQueryDTO dto) {
        return PageHelper.doPage(pageRequest, () -> qmsPqcDocMapper.selectByConditionForUi(tenantId, dto));
    }

    @Override
    @ProcessLovValue
    public Page<QmsPqcLineQueryVO> qmsPqcDocQueryLine(Long tenantId, PageRequest pageRequest, String pqcHeaderId) {
        return PageHelper.doPage(pageRequest, () -> qmsPqcDocMapper.selectByIdForUi(tenantId, pqcHeaderId));
    }

    @Override
    public Page<QmsPqcLineDetailsVO> qmsPqcDocQueryLineDetails(Long tenantId, PageRequest pageRequest, String pqcLineId) {
        return PageHelper.doPage(pageRequest, () -> qmsPqcDocMapper.selectDetailsByIdForUi(tenantId, pqcLineId));
    }

    @Override
    @ProcessLovValue
    public List<QmsPqcHeaderQueryVO> listHeaderExport(Long tenantId, QmsPqcHeaderQueryDTO dto) {
        return qmsPqcDocMapper.selectByConditionForUi(tenantId, dto);
    }
}
