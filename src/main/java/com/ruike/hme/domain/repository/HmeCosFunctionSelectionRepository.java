package com.ruike.hme.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeCosFunctionSelection;

/**
 * 筛选芯片性能表资源库
 *
 * @author chaonan.hu@hand-china.com 2021-08-19 09:37:16
 */
public interface HmeCosFunctionSelectionRepository extends BaseRepository<HmeCosFunctionSelection> {

    /**
     * 筛选芯片性能数据抽取JOB
     *
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/19 10:04:36
     * @return void
     */
    void preSelectionFunctionDataJob(Long tenantId);
}
