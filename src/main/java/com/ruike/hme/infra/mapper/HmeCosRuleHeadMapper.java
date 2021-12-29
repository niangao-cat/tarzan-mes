package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosRuleDTO;
import com.ruike.hme.api.dto.HmeCosRuleHeadDto;
import com.ruike.hme.domain.entity.HmeCosRuleHead;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 芯片规则头表Mapper
 *
 * @author wenzhang.yu@hand-china.com 2020-08-10 16:16:46
 */
public interface HmeCosRuleHeadMapper extends BaseMapper<HmeCosRuleHead> {

    /**
     *@description 查询芯片规则头表
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/11 11:40
     *@param tenantId
     *@param dto
     *@return java.util.List<com.ruike.hme.api.dto.HmeCosRuleHeadDto>
     **/
    List<HmeCosRuleHeadDto> cosRuleQuery(@Param("tenantId")Long tenantId, @Param("dto")HmeCosRuleDTO dto);
}
