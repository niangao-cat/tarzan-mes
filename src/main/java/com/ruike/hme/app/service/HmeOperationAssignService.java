package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeQualificationDTO;
import com.ruike.hme.api.dto.HmeQualificationDTO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 资质与工艺关系表应用服务
 *
 * @author chaonan.hu@hand-china.com 2020-06-16 19:08:35
 */
public interface HmeOperationAssignService {

    /**
     * 资质查询LOV
     *
     * @param tenantId    租户ID
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeQualificationDTO>
     */
    Page<HmeQualificationDTO2> listForUi(Long tenantId, HmeQualificationDTO2 dto, PageRequest pageRequest);
}
