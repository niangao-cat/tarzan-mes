package com.ruike.hme.infra.mapper;


import com.ruike.hme.api.dto.TagCheckRuleHeaderDTO;
import com.ruike.hme.domain.entity.TagCheckRuleHeader;
import com.ruike.hme.domain.entity.TagCheckRuleLine;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据项展示规则维护头表Mapper
 *
 * @author wengang.qiang@hand-china.com 2021-08-25 16:23:18
 */
public interface TagCheckRuleHeaderMapper extends BaseMapper<TagCheckRuleHeader> {
    /**
     * 根据头id查询行表
     *
     * @param tenantId 租户id
     * @param headerId 头表id
     * @return
     * @author wengang.qiang
     */
    List<TagCheckRuleLine> queryById(@Param("tenantId") Long tenantId, @Param("headerId") String headerId);

    /**
     * 根据头id查询行表基础数据查询
     *
     * @param tenantId           租户id
     * @param checkRuleHeaderDTO 查询条件
     * @return
     * @author wengang.qiang
     */
    List<TagCheckRuleHeader> query(@Param("tenantId") Long tenantId, @Param("dto") TagCheckRuleHeaderDTO checkRuleHeaderDTO);

    /**
     * 自定义批量插入数据
     *
     * @param domains TagCheckRuleHeader实体类集合
     * @author wengang.qiang
     */
    void myBatchInsert(@Param("domains") List<TagCheckRuleHeader> domains);

    /**
     * 数据保存校验
     *
     * @param tenantId 租户id
     * @param dto      条件
     * @return
     */
    TagCheckRuleHeader queryCheckRuleHeader(@Param("tenantId") Long tenantId, @Param("dto") TagCheckRuleHeader dto);

    /**
     * 头表更新
     *
     * @param tenantId 租户id
     * @param dto      内容
     */
    void myBatchUpdate(@Param(value = "tenantId") Long tenantId, @Param("dto") TagCheckRuleHeader dto);


}
