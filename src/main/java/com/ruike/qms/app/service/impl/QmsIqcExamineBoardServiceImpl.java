package com.ruike.qms.app.service.impl;

import com.ruike.qms.api.dto.*;
import com.ruike.qms.app.service.QmsIqcExamineBoardService;
import com.ruike.qms.domain.repository.QmsIqcExamineBoardRepository;
import com.ruike.qms.infra.mapper.QmsIqcExamineBoardMapper;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.util.PageUtil;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * ICQ检验看板应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-05-06 10:37:10
 */
@Service
public class QmsIqcExamineBoardServiceImpl implements QmsIqcExamineBoardService {

    @Autowired
    private QmsIqcExamineBoardMapper qmsIqcExamineBoardMapper;

    @Autowired
    private QmsIqcExamineBoardRepository qmsIqcExamineBoardRepository;

    @Override
    @ProcessLovValue
    public Page<QmsIqcExamineBoardDTO> selectIqcExamineBoardForUi(Long tenantId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> qmsIqcExamineBoardRepository.selectIqcExamineBoardForUi(tenantId));
    }

    @Override
    public List<QmsIqcCalSumDTO> selectIqcDayForUi(Long tenantId) {
        return qmsIqcExamineBoardMapper.selectIqcDays(tenantId);
    }

    @Override
    public List<QmsIqcCalSumDTO> selectIqcMonthForUi(Long tenantId) {
        return qmsIqcExamineBoardMapper.selectIqcMonths(tenantId);
    }


}
