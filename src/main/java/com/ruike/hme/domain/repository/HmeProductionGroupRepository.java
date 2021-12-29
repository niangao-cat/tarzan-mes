package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeProductionGroupDTO;
import com.ruike.hme.api.dto.HmeProductionGroupDTO3;
import com.ruike.hme.domain.entity.HmeProductionGroupLine;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeProductionGroup;

/**
 * 产品组资源库
 *
 * @author chaonan.hu@hand-china.com 2021-05-27 13:47:50
 */
public interface HmeProductionGroupRepository extends BaseRepository<HmeProductionGroup> {

    /**
     * 新建或者更新产品组头
     *
     * @param tenantId 租户ID
     * @param dto 创建或更新信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/27 02:08:53
     * @return com.ruike.hme.domain.entity.HmeProductionGroup
     */
    HmeProductionGroup createOrUpdate(Long tenantId, HmeProductionGroupDTO dto);

    /**
     * 新建或者更新产品组行
     *
     * @param tenantId 租户ID
     * @param dto 创建或更新行信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/4 11:19:15
     * @return com.ruike.hme.domain.entity.HmeProductionGroup
     */
    HmeProductionGroupLine createOrUpdateLine(Long tenantId, HmeProductionGroupDTO3 dto);
}
