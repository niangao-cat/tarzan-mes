package com.ruike.reports.infra.repository.impl;

import com.ruike.reports.api.dto.WmsDistributionGeneralQueryDTO;
import com.ruike.reports.domain.repository.WmsDistributionGeneralRepository;
import com.ruike.reports.domain.vo.WmsDistributionGeneralVO;
import com.ruike.reports.infra.mapper.WmsDistributionGeneralMapper;
import com.ruike.wms.domain.vo.WmsDistributionListQueryVO1;
import com.ruike.wms.infra.mapper.WmsDistributionListQueryMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.NO;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * <p>
 * 配送综合查询报表 资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 17:01
 */
@Component
public class WmsDistributionGeneralRepositoryImpl implements WmsDistributionGeneralRepository {
    private final WmsDistributionGeneralMapper wmsDistributionGeneralMapper;
    private final WmsDistributionListQueryMapper wmsDistributionListQueryMapper;

    public WmsDistributionGeneralRepositoryImpl(WmsDistributionGeneralMapper wmsDistributionGeneralMapper, WmsDistributionListQueryMapper wmsDistributionListQueryMapper) {
        this.wmsDistributionGeneralMapper = wmsDistributionGeneralMapper;
        this.wmsDistributionListQueryMapper = wmsDistributionListQueryMapper;
    }

    @Override
    @ProcessLovValue
    public Page<WmsDistributionGeneralVO> pageList(Long tenantId, WmsDistributionGeneralQueryDTO dto, PageRequest pageRequest) {
        Page<WmsDistributionGeneralVO> page = PageHelper.doPage(pageRequest, () -> wmsDistributionGeneralMapper.selectListByCondition(tenantId, dto));
        if (page.size() > 0) {
            Set<String> docIdSet = page.stream().map(WmsDistributionGeneralVO::getInstructionDocId).collect(Collectors.toSet());
            Map<String, String> suiteMap = new HashMap<>(docIdSet.size() * 2);
            // 按单据计算是否备齐
            docIdSet.forEach(id -> {
                List<WmsDistributionListQueryVO1> lineList = wmsDistributionListQueryMapper.selectDistribution(tenantId, id);
                suiteMap.put(id, lineList.stream().allMatch(rec -> rec.getAcutalQty().compareTo(rec.getQuantity()) >= 0) ? YES : NO);
            });
            page.forEach(rec -> rec.setSuiteFlag(suiteMap.get(rec.getInstructionDocId())));
        }
        return page;
    }
}
