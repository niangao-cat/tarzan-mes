package com.ruike.qms.infra.mapper;

import com.ruike.qms.domain.entity.QmsOqcLine;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 出库检行表Mapper
 *
 * @author yuchao.wang@hand-china.com 2020-08-28 14:18:08
 */
public interface QmsOqcLineMapper extends BaseMapper<QmsOqcLine> {

    /**
     *
     * @Description 批量更新行数据
     *
     * @author yuchao.wang
     * @date 2020/8/29 17:48
     * @param userId 用户ID
     * @param lineList 行数据列表
     * @return void
     *
     */
    void batchUpdateResult(@Param(value = "userId") Long userId,
                           @Param(value = "lineList") List<QmsOqcLine> lineList);
}
