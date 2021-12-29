package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeCosRuleLogicDTO;
import com.ruike.hme.domain.entity.HmeCosRuleLogic;
import com.ruike.hme.domain.repository.HmeCosRuleLogicRepository;
import com.ruike.hme.infra.mapper.HmeCosRuleLogicMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 芯片规则逻辑 资源库实现
 *
 * @author wenzhang.yu@hand-china.com 2020-08-10 16:16:47
 */
@Component
public class HmeCosRuleLogicRepositoryImpl extends BaseRepositoryImpl<HmeCosRuleLogic> implements HmeCosRuleLogicRepository {

    @Autowired
    private HmeCosRuleLogicMapper hmeCosRuleLogicMapper;

    @Override
    public List<HmeCosRuleLogicDTO> cosRuleLogicQuery(Long tenantId, String cosRuleId) {

        return hmeCosRuleLogicMapper.cosRuleLogicQuery(tenantId,cosRuleId);
    }
}
