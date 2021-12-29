package com.ruike.qms.app.service;

import com.ruike.qms.api.dto.QmsTransitionRuleDTO;
import com.ruike.qms.api.dto.QmsTransitionRuleDTO2;
import com.ruike.qms.domain.entity.QmsTransitionRule;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.general.api.dto.MtUserOrganizationDTO6;

import java.util.List;

/**
 * 检验水平转移规则表应用服务
 *
 * @author tong.li05@hand-china.com 2020-05-11 09:54:52
 */
public interface QmsTransitionRuleService {

    /**
     * 检验水平转移基础设置查询
     *
     * @param pageRequest    分页参数
     * @param transitionRule 转移规则
     * @param tenantId       租户ID
     * @return io.choerodon.core.domain.Page<com.ruike.qms.api.dto.QmsTransitionRuleDTO>
     * @author tong.li 2020/5/11 13:59
     */
    Page<QmsTransitionRuleDTO> query(PageRequest pageRequest, QmsTransitionRule transitionRule, Long tenantId);

    /**
     * 检验水平转移基础设置  新建或者更新
     *
     * @param tenantId       租户ID
     * @param transitionRule 转移规则
     * @return com.ruike.qms.domain.entity.QmsTransitionRule
     * @author tong.li 2020/5/11 15:51
     */
    QmsTransitionRule createOrUpdate(Long tenantId, QmsTransitionRule transitionRule);

    /**
     * 删除数据
     *
     * @param tenantId             租户ID
     * @param transitionRuleIdList 转移规则ID列表
     * @author tong.li 2020/5/13 10:40
     */
    void delete(Long tenantId, List<String> transitionRuleIdList);

    /**
     * 获取默认站点
     *
     * @param tenantId 租户ID
     * @return com.ruike.qms.api.dto.QmsTransitionRuleDTO2
     * @author tong.li 2020/5/19 10:22
     */
    QmsTransitionRuleDTO2 userDefaultSiteForUi(Long tenantId);

}
