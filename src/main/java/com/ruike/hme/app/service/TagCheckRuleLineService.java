package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.TagCheckRuleLine;

/**
 * 数据项展示规则维护行表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021-08-25 16:23:20
 */
public interface TagCheckRuleLineService {

    /**
     * TagCheckRuleLine 行表基础数据保存
     *
     * @param tenantId         租户id
     * @param tagCheckRuleLine 保存数据
     * @author wengang.qiang
     */
    void save(Long tenantId, TagCheckRuleLine tagCheckRuleLine);

}
