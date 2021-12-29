package com.ruike.itf.domain.repository;

import com.ruike.itf.domain.entity.ItfQualityAnalyzeIface;
import org.hzero.mybatis.base.BaseRepository;

/**
 * 质量文件解析接口头资源库
 *
 * @author yonghui.zhu@hand-china.com 2021-04-06 10:04:45
 */
public interface ItfQualityAnalyzeIfaceRepository extends BaseRepository<ItfQualityAnalyzeIface> {

    /**
     * 保存
     *
     * @param entity 实体
     * @return int
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/6 10:20:48
     */
    int save(ItfQualityAnalyzeIface entity);
}
