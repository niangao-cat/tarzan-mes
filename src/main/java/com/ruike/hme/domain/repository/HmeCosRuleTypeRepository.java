package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeCosRuleTypeDTO;
import com.ruike.hme.domain.entity.HmeCosRuleType;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 芯片规则类型资源库
 *
 * @author wenzhang.yu@hand-china.com 2020-08-10 16:16:46
 */
public interface HmeCosRuleTypeRepository extends BaseRepository<HmeCosRuleType> {


    List<HmeCosRuleTypeDTO> cosRuleTypeQuery(Long tenantId, String cosRuleId);
}
