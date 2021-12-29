package com.ruike.hme.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeAreaThroughRateDetails;

/**
 * 制造部直通率看板资源库
 *
 * @author sanfeng.zhang@hand-china.com 2021-06-24 21:17:01
 */
public interface HmeAreaThroughRateDetailsRepository extends BaseRepository<HmeAreaThroughRateDetails> {

    /**
     * 拉取直通率数据
     * @param tenantId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/6/24
     */
    void createAreaThroughRate(Long tenantId);
}
