package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCenterKanbanHeaderDTO;
import com.ruike.hme.api.dto.HmeCenterKanbanHeaderDTO2;
import com.ruike.hme.api.dto.HmeCenterKanbanLineDTO;
import com.ruike.hme.domain.entity.HmeCenterKanbanHeader;
import com.ruike.hme.domain.entity.HmeCenterKanbanLine;
import com.ruike.hme.domain.vo.HmeCenterKanbanHeaderVO;
import com.ruike.hme.domain.vo.HmeCenterKanbanLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 制造中心看板信息头表应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-05-28 13:49:51
 */
public interface HmeCenterKanbanHeaderService {

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

    /**
     * 分页查询制造中心看板信息头表
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/28 03:42:39
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeCenterKanbanHeaderVO>
     */
    Page<HmeCenterKanbanHeaderVO> headPageQuery(Long tenantId, HmeCenterKanbanHeaderDTO2 dto, PageRequest pageRequest);

    /**
     * 分页查询制造中心看板信息行表
     *
     * @param tenantId 租户ID
     * @param centerKanbanHeaderId 头ID
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/28 04:43:48
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeCenterKanbanLineVO>
     */
    Page<HmeCenterKanbanLineVO> linePageQuery(Long tenantId, String centerKanbanHeaderId, PageRequest pageRequest);

}
