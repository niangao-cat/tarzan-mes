package com.ruike.itf.infra.repository.impl;

import com.ruike.itf.domain.entity.ItfQualityAnalyzeIface;
import com.ruike.itf.domain.repository.ItfQualityAnalyzeIfaceRepository;
import com.ruike.itf.infra.mapper.ItfQualityAnalyzeIfaceMapper;
import org.apache.commons.lang.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;

/**
 * 质量文件解析接口头 资源库实现
 *
 * @author yonghui.zhu@hand-china.com 2021-04-06 10:04:45
 */
@Component
public class ItfQualityAnalyzeIfaceRepositoryImpl extends BaseRepositoryImpl<ItfQualityAnalyzeIface> implements ItfQualityAnalyzeIfaceRepository {
    private final ItfQualityAnalyzeIfaceMapper mapper;

    public ItfQualityAnalyzeIfaceRepositoryImpl(ItfQualityAnalyzeIfaceMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public int save(ItfQualityAnalyzeIface entity) {
        if (StringUtils.isNotBlank(entity.getInterfaceId())) {
            return mapper.updateByPrimaryKeySelective(entity);
        } else {
            return this.insertSelective(entity);
        }
    }
}
