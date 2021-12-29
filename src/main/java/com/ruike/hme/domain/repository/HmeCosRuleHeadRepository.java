package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeCosRuleDTO;
import com.ruike.hme.api.dto.HmeCosRuleHeadDto;
import com.ruike.hme.domain.entity.HmeCosRuleHead;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 芯片规则头表资源库
 *
 * @author wenzhang.yu@hand-china.com 2020-08-10 16:16:46
 */
public interface HmeCosRuleHeadRepository extends BaseRepository<HmeCosRuleHead> {

    /**
     *@description 查询芯片规则头表
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/10 19:26
     *@param tenantId
     *@param dto
     *@return java.util.List<com.ruike.hme.domain.entity.HmeCosRuleHead>
     **/
    List<HmeCosRuleHeadDto> cosRuleQuery(Long tenantId, HmeCosRuleDTO dto);
}
