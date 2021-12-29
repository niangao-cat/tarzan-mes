package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCenterKanbanHeaderDTO2;
import com.ruike.hme.domain.entity.HmeCenterKanbanHeader;
import com.ruike.hme.domain.vo.HmeCenterKanbanHeaderVO;
import com.ruike.hme.domain.vo.HmeCenterKanbanLineVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 制造中心看板信息头表Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-05-28 13:49:51
 */
public interface HmeCenterKanbanHeaderMapper extends BaseMapper<HmeCenterKanbanHeader> {

    /**
     * 分页查询制造中心看板信息头表
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCenterKanbanHeaderVO>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/28 03:46:31
     */
    List<HmeCenterKanbanHeaderVO> headPageQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeCenterKanbanHeaderDTO2 dto);

    /**
     * 分页查询制造中心看板信息行表
     *
     * @param tenantId 租户ID
     * @param centerKanbanHeaderId 头ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/28 04:45:33
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCenterKanbanLineVO>
     */
    List<HmeCenterKanbanLineVO> linePageQuery(@Param("tenantId") Long tenantId, @Param("centerKanbanHeaderId") String centerKanbanHeaderId);
}
