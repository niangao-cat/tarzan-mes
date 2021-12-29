package com.ruike.itf.infra.mapper;

import com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine;
import com.ruike.itf.domain.entity.ItfQualityAnalyzeLineIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 质量文件解析接口头Mapper
 *
 * @author yonghui.zhu@hand-china.com 2021-04-06 10:04:45
 */
public interface ItfQualityAnalyzeLineIfaceMapper extends BaseMapper<ItfQualityAnalyzeLineIface> {
    /**
     * 批量新增
     * @param domains 数据集合
     * @return
     * @author penglin.sui@hand-china.com 2021/6/29 15:31
     */
    void batchInsertAnalyzeLineIface(@Param("domains") List<ItfQualityAnalyzeLineIface> domains);
}
