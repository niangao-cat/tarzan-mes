package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.domain.repository.WmsDistributionSignRepository;
import com.ruike.wms.domain.vo.WmsDistributionSignDetailVO;
import com.ruike.wms.domain.vo.WmsDistributionSignLineVO;
import com.ruike.wms.infra.mapper.WmsDistributionSignMapper;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 配送签收 资源库实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 21:50
 */
@Component
public class WmsDistributionSignRepositoryImpl implements WmsDistributionSignRepository {
    private final WmsDistributionSignMapper wmsDistributionSignMapper;

    public WmsDistributionSignRepositoryImpl(WmsDistributionSignMapper wmsDistributionSignMapper) {
        this.wmsDistributionSignMapper = wmsDistributionSignMapper;
    }

    @Override
    @ProcessLovValue
    public List<WmsDistributionSignLineVO> selectSignListByDocId(Long tenantId, String instructionDocId) {
        return wmsDistributionSignMapper.selectSignListByDocId(tenantId, instructionDocId);
    }

    @Override
    public List<WmsDistributionSignDetailVO> selectPrepareSignList(Long tenantId, List<String> idList) {
        return wmsDistributionSignMapper.selectPrepareSignList(tenantId, idList);
    }
}
