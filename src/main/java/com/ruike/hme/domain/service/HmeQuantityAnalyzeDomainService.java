package com.ruike.hme.domain.service;

import com.ruike.hme.api.dto.query.QualityAnalyzeQuery;
import com.ruike.hme.api.dto.representation.QualityAnalyzeRepresentation;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * <p>
 * 质量文件解析 领域服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/8 11:15
 */
public interface HmeQuantityAnalyzeDomainService {

    /**
     * 分页查询
     *
     * @param tenantId    租户
     * @param query       查询条件
     * @param pageRequest 分页条件
     * @return com.ruike.hme.api.dto.representation.QualityAnalyzeRepresentation
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/8 11:21:27
     */
    QualityAnalyzeRepresentation page(Long tenantId, QualityAnalyzeQuery query, PageRequest pageRequest);
}
