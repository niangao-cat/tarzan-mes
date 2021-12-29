package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeTagNcDTO;
import com.ruike.hme.domain.entity.HmeTagNc;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 数据项不良判定基础表应用服务
 *
 * @author guiming.zhou@hand-china.com 2020-09-24 16:00:30
 */
public interface HmeTagNcService {

    /**
     * 获取不良信息
     *
     * @param tenantId
     * @param hmeTagNc
     * @param pageRequest
     * @author guiming.zhou@hand-china.com 2020/9/25 14:05
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeTagNcDTO>
     */
    Page<HmeTagNcDTO> getTagNcList(Long tenantId, HmeTagNc hmeTagNc, PageRequest pageRequest);

}
