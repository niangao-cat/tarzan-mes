package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeCenterKanbanHeaderDTO;
import com.ruike.hme.api.dto.HmeCenterKanbanLineDTO;
import com.ruike.hme.domain.entity.HmeCenterKanbanLine;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeCenterKanbanHeader;

/**
 * 制造中心看板信息头表资源库
 *
 * @author chaonan.hu@hand-china.com 2021-05-28 13:49:51
 */
public interface HmeCenterKanbanHeaderRepository extends BaseRepository<HmeCenterKanbanHeader> {

    /**
     * 创建或者更新制造中心看板信息头表
     *
     * @param tenantId 租户ID
     * @param dto 创建或者更新信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/28 02:11:25
     * @return com.ruike.hme.domain.entity.HmeCenterKanbanHeader
     */
    HmeCenterKanbanHeader createOrUpdateHeader(Long tenantId, HmeCenterKanbanHeaderDTO dto);

    /**
     * 创建或者更新制造中心看板信息行表
     *
     * @param tenantId 租户ID
     * @param dto 创建或者更新信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/28 03:08:03
     * @return com.ruike.hme.domain.entity.HmeCenterKanbanLine
     */
    HmeCenterKanbanLine createOrUpdateLine(Long tenantId, HmeCenterKanbanLineDTO dto);
}
