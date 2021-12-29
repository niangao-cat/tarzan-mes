package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeNcDowngradeDTO;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeNcDowngrade;

/**
 * 产品降级关系维护资源库
 *
 * @author chaonan.hu@hand-china.com 2021-05-18 11:35:47
 */
public interface HmeNcDowngradeRepository extends BaseRepository<HmeNcDowngrade> {

    /**
     * 创建或更新产品降级关系维护
     *
     * @param tenantId 租户ID
     * @param dto 创建或更新信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/18 02:05:28
     * @return com.ruike.hme.domain.entity.HmeNcDowngrade
     */
    HmeNcDowngrade createOrUpdate(Long tenantId, HmeNcDowngradeDTO dto);
}
