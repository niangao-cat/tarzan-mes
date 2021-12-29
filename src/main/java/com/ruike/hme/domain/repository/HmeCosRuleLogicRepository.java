package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeCosRuleLogicDTO;
import com.ruike.hme.domain.entity.HmeCosRuleLogic;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 芯片规则逻辑资源库
 *
 * @author wenzhang.yu@hand-china.com 2020-08-10 16:16:47
 */
public interface HmeCosRuleLogicRepository extends BaseRepository<HmeCosRuleLogic> {


    List<HmeCosRuleLogicDTO> cosRuleLogicQuery(Long tenantId, String cosRuleId);
}
