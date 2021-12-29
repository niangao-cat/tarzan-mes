package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmePumpModPositionLine;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 泵浦源模块位置行表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2021-08-25 17:34:37
 */
public interface HmePumpModPositionLineMapper extends BaseMapper<HmePumpModPositionLine> {

    /**
     * 删除行数据
     *
     * @param positionLineIds
     * @author sanfeng.zhang@hand-china.com 2021/8/31 15:59
     * @return void
     */
    void myBatchDelete(@Param("tenantId") Long tenantId, @Param("positionLineIds") List<String> positionLineIds);

    /**
     * 查询EO下所有的位置
     *
     * @param tenantId
     * @param eoId
     * @author sanfeng.zhang@hand-china.com 2021/8/31 16:08
     * @return java.util.List<java.lang.String>
     */
    List<String> queryPumpPositionLineByEoId(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);
}
