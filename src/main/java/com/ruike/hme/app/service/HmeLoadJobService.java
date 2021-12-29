package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeLoadJobDTO;
import com.ruike.hme.api.dto.HmeLoadJobDTO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.ibatis.annotations.Param;

/**
 * 装载信息作业记录表应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-02-01 11:09:48
 */
public interface HmeLoadJobService {

    /**
     * 分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/3 16:53:00
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeLoadJobDTO>
     */
    Page<HmeLoadJobDTO> pageList(Long tenantId, HmeLoadJobDTO2 dto, PageRequest pageRequest);
}
