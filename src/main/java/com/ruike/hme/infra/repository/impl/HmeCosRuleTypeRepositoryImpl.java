package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeCosRuleTypeDTO;
import com.ruike.hme.domain.entity.HmeCosRuleType;
import com.ruike.hme.domain.repository.HmeCosRuleTypeRepository;
import com.ruike.hme.infra.mapper.HmeCosRuleTypeMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 芯片规则类型 资源库实现
 *
 * @author wenzhang.yu@hand-china.com 2020-08-10 16:16:46
 */
@Component
public class HmeCosRuleTypeRepositoryImpl extends BaseRepositoryImpl<HmeCosRuleType> implements HmeCosRuleTypeRepository {

    @Autowired
    private HmeCosRuleTypeMapper hmeCosRuleTypeMapper;

    @Override
    public List<HmeCosRuleTypeDTO> cosRuleTypeQuery(Long tenantId, String cosRuleId) {
        return hmeCosRuleTypeMapper.cosRuleQuery(tenantId,cosRuleId);
    }
}
