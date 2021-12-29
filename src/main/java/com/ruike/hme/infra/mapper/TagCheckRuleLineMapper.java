package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.TagCheckRuleLine;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据项展示规则维护行表Mapper
 *
 * @author wengang.qiang@hand-china.com 2021-08-25 16:23:20
 */
public interface TagCheckRuleLineMapper extends BaseMapper<TagCheckRuleLine> {
    /**
     * 批量插入头表
     *
     * @param domains TagCheckRuleLine集合
     * @author wengang.qiang
     */
    void myBatchInsert(@Param("domains") List<TagCheckRuleLine> domains);

}
