package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsMaterialExchangeDocDTO;
import com.ruike.wms.api.dto.WmsMaterialExchangeDocLineDTO;
import com.ruike.wms.api.dto.WmsMaterialExchangeParamDTO;
import com.ruike.wms.app.service.WmsMaterialExchangeService;
import com.ruike.wms.domain.repository.WmsMaterialExchangeRepository;
import com.ruike.wms.domain.vo.WmsMaterialExchangeVO;
import com.ruike.wms.infra.mapper.WmsMaterialExchangeMapper;
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
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;

/**
 * 料废查询应用服务实现
 *
 * @author jiangling.zheng@hand-china.com 2020-05-18 10:30
 */
@Service
public class WmsMaterialExchangeServiceImpl implements WmsMaterialExchangeService {

    @Autowired
    private WmsMaterialExchangeMapper wmsMaterialExchangeMapper;

    @Autowired
    private WmsMaterialExchangeRepository wmsMaterialExchangeRepository;

    @Override
    @ProcessLovValue
    public Page<WmsMaterialExchangeDocDTO> listDocForUi(Long tenantId, WmsMaterialExchangeParamDTO dto, PageRequest pageRequest) {
        dto.setTenantId(tenantId);
        return PageHelper.doPage(pageRequest, () -> wmsMaterialExchangeRepository.listDocForUi(dto));
    }

    @Override
    @ProcessLovValue
    public Page<WmsMaterialExchangeDocLineDTO> listDocLineForUi(Long tenantId, String instructionDocId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> wmsMaterialExchangeMapper.selectLineByConditionForUi(tenantId, instructionDocId));
    }

    @Override
    public WmsMaterialExchangeVO lineStockTransfer(Long tenantId, WmsMaterialExchangeVO wmsMaterialExchangeVO) {
        return wmsMaterialExchangeRepository.lineStockTransfer(tenantId, wmsMaterialExchangeVO);
    }
}
