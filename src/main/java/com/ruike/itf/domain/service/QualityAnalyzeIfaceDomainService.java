package com.ruike.itf.domain.service;

/**
 * <p>
 * 质量文件分析接口领域服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/6 10:19
 */
public interface QualityAnalyzeIfaceDomainService {

    /**
     * 调用
     *
     * @param tenantId 租户
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/6 10:25:11
     */
    void invoke(Long tenantId);
}
