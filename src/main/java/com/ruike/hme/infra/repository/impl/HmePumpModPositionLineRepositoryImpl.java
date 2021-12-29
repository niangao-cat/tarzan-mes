package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.infra.mapper.HmePumpModPositionLineMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmePumpModPositionLine;
import com.ruike.hme.domain.repository.HmePumpModPositionLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 泵浦源模块位置行表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2021-08-25 17:34:37
 */
@Component
public class HmePumpModPositionLineRepositoryImpl extends BaseRepositoryImpl<HmePumpModPositionLine> implements HmePumpModPositionLineRepository {

    @Autowired
    private HmePumpModPositionLineMapper hmePumpModPositionLineMapper;

    @Override
    public List<String> queryPumpPositionLineByEoId(Long tenantId, String eoId) {
        return hmePumpModPositionLineMapper.queryPumpPositionLineByEoId(tenantId, eoId);
    }
}
