package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeLoadJobObject;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 装载信息作业对象表Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-02-02 17:19:24
 */
public interface HmeLoadJobObjectMapper extends BaseMapper<HmeLoadJobObject> {

    /**
     * 批量新增
     *
     * @param domains
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/2 9:48
     */
    void batchInsert(@Param("domains") List<HmeLoadJobObject> domains);

}
