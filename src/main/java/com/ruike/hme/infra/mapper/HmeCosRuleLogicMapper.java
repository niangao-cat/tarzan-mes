package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosRuleLogicDTO;
import com.ruike.hme.domain.entity.HmeCosRuleLogic;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 芯片规则逻辑Mapper
 *
 * @author wenzhang.yu@hand-china.com 2020-08-10 16:16:47
 */
public interface HmeCosRuleLogicMapper extends BaseMapper<HmeCosRuleLogic> {


    List<HmeCosRuleLogicDTO > cosRuleLogicQuery(@Param("tenantId") Long tenantId, @Param("cosRuleId") String cosRuleId);
}
