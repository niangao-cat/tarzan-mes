package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.TagCheckRuleHeaderDTO;
import com.ruike.hme.domain.entity.TagCheckRuleHeader;
import com.ruike.hme.domain.entity.TagCheckRuleLine;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 数据项展示规则维护头表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021-08-25 16:23:18
 */
public interface TagCheckRuleHeaderService {

    /**
     * 数据项展示规则维护头表查询参数
     *
     * @param tenantId           租户ID
     * @param checkRuleHeaderDTO 查询条件
     * @param pageRequest        分页
     * @return 数据项展示规则维护头表列表
     */
    Page<TagCheckRuleHeader> list(Long tenantId, TagCheckRuleHeaderDTO checkRuleHeaderDTO, PageRequest pageRequest);

    /**
     * TagCheckRuleHeader 数据保存
     *
     * @param tenantId
     * @param tagCheckRuleHeader
     * @return
     */
    void save(Long tenantId, TagCheckRuleHeader tagCheckRuleHeader);

    /**
     * 根据头id查询行表数据
     *
     * @param tenantId    租户id
     * @param headerId    头表id
     * @param pageRequest 分页
     * @return
     */
    Page<TagCheckRuleLine> queryById(Long tenantId, String headerId, PageRequest pageRequest);

}
