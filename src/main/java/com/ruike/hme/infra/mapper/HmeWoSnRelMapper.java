package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeWoSnRel;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 生产订单SN码关系表Mapper
 *
 * @author penglin.sui@hand-china.com 2020-08-14 17:31:42
 */
public interface HmeWoSnRelMapper extends BaseMapper<HmeWoSnRel> {


    /**
     *
     * @Description 批量新增
     *
     * @author yuchao.wang
     * @date 2020/11/6 16:43
     * @param insertList 新增数据列表
     * @return void
     *
     */
    void batchInsert(@Param("insertList") List<HmeWoSnRel> insertList);
}
