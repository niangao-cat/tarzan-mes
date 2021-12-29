package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmePumpModPositionHeader;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 泵浦源模块位置头表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2021-08-25 17:34:37
 */
public interface HmePumpModPositionHeaderMapper extends BaseMapper<HmePumpModPositionHeader> {


    /**
     * 批量插入
     *
     * @param headerList
     * @author sanfeng.zhang@hand-china.com 2021/8/31 14:17
     * @return void
     */
    void myBatchInsert(@Param("headerList") List<HmePumpModPositionHeader> headerList);

    /**
     * 批量更新EO
     *
     * @param tenantId
     * @param userId
     * @param headerList
     * @author sanfeng.zhang@hand-china.com 2021/9/22 23:13
     * @return void
     */
    void myBatchUpdateEo(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("headerList") List<HmePumpModPositionHeader> headerList);
}
