package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 质量文件行表Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-01-19 11:10:06
 */
public interface HmeQuantityAnalyzeLineMapper extends BaseMapper<HmeQuantityAnalyzeLine> {
    /**
     * delete by id list
     *
     * @param qaLineIdList Id List
     * @author penglin.sui
     * @date 2021-06-29 14:59
     */
    void batchDelete(@Param(value = "qaLineIdList") List<String> qaLineIdList);

    /**
     * 批量新增
     * @param domains 数据集合
     * @return
     * @author penglin.sui@hand-china.com 2021/6/29 15:31
     */
    void batchInsertAnalyzeLine(@Param("domains")List<HmeQuantityAnalyzeLine> domains);
}
