package com.ruike.hme.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine;

import java.util.List;

/**
 * 质量文件行表资源库
 *
 * @author chaonan.hu@hand-china.com 2021-01-19 11:10:06
 */
public interface HmeQuantityAnalyzeLineRepository extends BaseRepository<HmeQuantityAnalyzeLine> {
    /**
     * 批量新增
     *
     * @param quantityAnalyzeLineList
     * @author penglin.sui
     * @date 2021-06-29 15:27
     */
    void batchInsertAnalyzeLine(List<HmeQuantityAnalyzeLine> quantityAnalyzeLineList);
}
