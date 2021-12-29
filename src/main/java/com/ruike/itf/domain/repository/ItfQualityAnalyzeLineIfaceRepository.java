package com.ruike.itf.domain.repository;

import com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine;
import com.ruike.itf.domain.entity.ItfQualityAnalyzeLineIface;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 质量文件解析接口头资源库
 *
 * @author yonghui.zhu@hand-china.com 2021-04-06 10:04:45
 */
public interface ItfQualityAnalyzeLineIfaceRepository extends BaseRepository<ItfQualityAnalyzeLineIface> {

    /**
     * 保存
     *
     * @param entity 实体
     * @return int
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/6 10:20:48
     */
    int save(ItfQualityAnalyzeLineIface entity);

    /**
     * 批量新增
     *
     * @param quantityAnalyzeLineIfaceList
     * @author penglin.sui
     * @date 2021-06-29 15:44
     */
    void batchInsertAnalyzeLineIface(List<ItfQualityAnalyzeLineIface> quantityAnalyzeLineIfaceList);
}
