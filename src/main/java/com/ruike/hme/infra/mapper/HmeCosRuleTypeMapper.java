package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosRuleTypeDTO;
import com.ruike.hme.api.dto.HmeCosRuleTypeDTO1;
import com.ruike.hme.domain.entity.HmeCosRuleType;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 芯片规则类型Mapper
 *
 * @author wenzhang.yu@hand-china.com 2020-08-10 16:16:46
 */
public interface HmeCosRuleTypeMapper extends BaseMapper<HmeCosRuleType> {

    List<HmeCosRuleTypeDTO> cosRuleQuery(@Param("tenantId") Long tenantId, @Param("cosRuleId") String cosRuleId);


    List<HmeCosRuleTypeDTO1> selectRule(@Param("cosRuleId")String cosRuleId);
}
