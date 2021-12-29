package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosRuleDTO;
import com.ruike.hme.api.dto.HmeCosRuleHeadDto;
import com.ruike.hme.api.dto.HmeCosRuleLogicDTO;
import com.ruike.hme.api.dto.HmeCosRuleTypeDTO;
import com.ruike.hme.domain.entity.HmeCosRuleHead;
import com.ruike.hme.domain.entity.HmeCosRuleLogic;
import com.ruike.hme.domain.entity.HmeCosRuleType;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 芯片规则头表应用服务
 *
 * @author wenzhang.yu@hand-china.com 2020-08-10 16:16:46
 */
public interface HmeCosRuleHeadService {

    /**
     *@description 查询芯片规则头表
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/11 11:35
     *@param tenantId
     *@param dto
     *@param pageRequest
     *@return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeCosRuleHeadDto>
     **/
    Page<HmeCosRuleHeadDto> cosRuleQuery(Long tenantId, HmeCosRuleDTO dto, PageRequest pageRequest);

    /**
     *@description 新增或修改芯片规则头表
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/10 19:36
     *@param tenantId
     *@param dto
     *@return com.ruike.hme.domain.entity.HmeCosRuleHead
     **/
    HmeCosRuleHead addAndUpdateCosRule(Long tenantId, HmeCosRuleHead dto);

    /**
     *@description 删除芯片规则头表
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/10 19:42
     *@param hmeCosRuleHead
     *@return void
     **/
    void deleteCosRule(HmeCosRuleHead hmeCosRuleHead);

    /**
     *@description 获取芯片规则类型
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/17 14:12
     *@param tenantId
     *@param cosRuleId
     *@param pageRequest
     *@return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeCosRuleTypeDTO>
     **/
    Page<HmeCosRuleTypeDTO> cosRuleTypeQuery(Long tenantId, String cosRuleId, PageRequest pageRequest);

    /**
     *@description 获取芯片规则逻辑
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/10 20:37
     *@param tenantId
     *@param cosRuleId
     *@param pageRequest
     *@return io.choerodon.core.domain.Page<com.ruike.hme.domain.entity.HmeCosRuleLogic>
     **/
    Page<HmeCosRuleLogicDTO> cosRuleLogicQuery(Long tenantId, String cosRuleId, PageRequest pageRequest);

    /**
     *@description 新增或修改芯片规则类型
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/10 20:34
     *@param tenantId
     *@param dto
     *@return com.ruike.hme.domain.entity.HmeCosRuleType
     **/
    HmeCosRuleType addAndUpdateCosRuleType(Long tenantId, HmeCosRuleType dto);

    /**
     *@description 新增或修改芯片规则类型
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/11 9:00
     *@param tenantId
     *@param dto
     *@return com.ruike.hme.domain.entity.HmeCosRuleLogic
     **/
    HmeCosRuleLogic addAndUpdateCosRuleLogic(Long tenantId, HmeCosRuleLogic dto);
}
