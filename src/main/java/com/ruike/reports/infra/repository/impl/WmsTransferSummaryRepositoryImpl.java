package com.ruike.reports.infra.repository.impl;

import com.ruike.reports.api.dto.WmsTransferSummaryQueryDTO;
import com.ruike.reports.api.dto.WmsTransferSummaryQueryDTO2;
import com.ruike.reports.domain.repository.WmsTransferSummaryRepository;
import com.ruike.reports.domain.vo.WmsTransferSummaryVO;
import com.ruike.reports.domain.vo.WmsTransferSummaryVO2;
import com.ruike.reports.infra.mapper.WmsTransferSummaryMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 调拨汇总报表 资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/23 15:22
 */
@Component
public class WmsTransferSummaryRepositoryImpl implements WmsTransferSummaryRepository {
    private WmsTransferSummaryMapper wmsTransferSummaryMapper;

    public WmsTransferSummaryRepositoryImpl(WmsTransferSummaryMapper wmsTransferSummaryMapper) {
        this.wmsTransferSummaryMapper = wmsTransferSummaryMapper;
    }

    @Override
    @ProcessLovValue
    public Page<WmsTransferSummaryVO> pageList(Long tenantId, WmsTransferSummaryQueryDTO dto, PageRequest pageRequest) {
        Page<WmsTransferSummaryVO> page = PageHelper.doPage(pageRequest, () -> wmsTransferSummaryMapper.selectList(tenantId, dto));
        AtomicInteger sequenceCreator = new AtomicInteger(pageRequest.getPage() * pageRequest.getSize());
        page.forEach(rec -> {rec.setSequence(sequenceCreator.incrementAndGet()); rec.setWaitAllocationQty(rec.getQuantity().subtract(rec.getActualQuantity()));});
        return page;
    }

    @Override
    @ProcessLovValue
    public Page<WmsTransferSummaryVO2> selectDetailList(Long tenantId, WmsTransferSummaryQueryDTO2 dto, PageRequest pageRequest) {
        Page<WmsTransferSummaryVO2> page = PageHelper.doPage(pageRequest, () -> wmsTransferSummaryMapper.selectDetailList(tenantId, dto));
        AtomicInteger sequenceCreator = new AtomicInteger(pageRequest.getPage() * pageRequest.getSize());
        page.forEach(rec -> {rec.setSequence(sequenceCreator.incrementAndGet()); rec.setWaitAllocationQty(rec.getQuantity().subtract(rec.getActualQuantity()));});
        return page;
    }
}
