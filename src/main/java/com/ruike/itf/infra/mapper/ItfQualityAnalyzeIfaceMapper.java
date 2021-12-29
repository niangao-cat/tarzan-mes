package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfQualityAnalyzeIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 质量文件解析接口头Mapper
 *
 * @author yonghui.zhu@hand-china.com 2021-04-06 10:04:45
 */
public interface ItfQualityAnalyzeIfaceMapper extends BaseMapper<ItfQualityAnalyzeIface> {
    /**
     *
     * @Description 根据主键批量更新处理状态
     *
     * @author penglin.sui
     * @date 2021/6/29 11:44
     * @param userId 用户ID
     * @param qualityAnalyzeIfaceList qualityAnalyzeIfaceList
     * @return void
     *
     */
    void batchUpdateProcess(@Param("userId") Long userId,
                            @Param("qualityAnalyzeIfaceList") List<ItfQualityAnalyzeIface> qualityAnalyzeIfaceList);
}
