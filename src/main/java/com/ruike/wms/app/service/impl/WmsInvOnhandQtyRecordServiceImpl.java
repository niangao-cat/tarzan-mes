package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsInvOnhandQtyRecordDTO2;
import com.ruike.wms.api.dto.WmsInvOnhandQtyRecordDTO4;
import com.ruike.wms.app.service.WmsInvOnhandQtyRecordService;
import com.ruike.wms.domain.repository.WmsInvOnhandQtyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 仓库物料每日进销存表应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-11-18 09:53:55
 */
@Service
public class WmsInvOnhandQtyRecordServiceImpl implements WmsInvOnhandQtyRecordService {

    @Autowired
    private WmsInvOnhandQtyRecordRepository wmsInvOnhandQtyRecordRepository;

    @Override
    public Page<WmsInvOnhandQtyRecordDTO2> listForUi(Long tenantId, WmsInvOnhandQtyRecordDTO4 dto, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> wmsInvOnhandQtyRecordRepository.invOnhandQtyQuery(tenantId, dto));
    }

    @Override
    public void export(Long tenantId, WmsInvOnhandQtyRecordDTO4 dto, HttpServletResponse response) {
        wmsInvOnhandQtyRecordRepository.invOnhandQtyExport(tenantId, dto, response);
    }
}
