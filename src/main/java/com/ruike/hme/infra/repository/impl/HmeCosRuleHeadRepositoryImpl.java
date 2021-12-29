package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeCosRuleDTO;
import com.ruike.hme.api.dto.HmeCosRuleHeadDto;
import com.ruike.hme.domain.entity.HmeCosRuleHead;
import com.ruike.hme.domain.repository.HmeCosRuleHeadRepository;
import com.ruike.hme.infra.mapper.HmeCosRuleHeadMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 芯片规则头表 资源库实现
 *
 * @author wenzhang.yu@hand-china.com 2020-08-10 16:16:46
 */
@Component
public class HmeCosRuleHeadRepositoryImpl extends BaseRepositoryImpl<HmeCosRuleHead> implements HmeCosRuleHeadRepository {

    @Autowired
    private HmeCosRuleHeadMapper hmeCosRuleHeadMapper;

    /**
     *@description 查询芯片规则头表
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/10 19:26
     *@param tenantId
     *@param dto
     *@return java.util.List<com.ruike.hme.domain.entity.HmeCosRuleHead>
     **/
    @Override
    public List<HmeCosRuleHeadDto> cosRuleQuery(Long tenantId, HmeCosRuleDTO dto) {

        return hmeCosRuleHeadMapper.cosRuleQuery(tenantId,dto);
    }
}
