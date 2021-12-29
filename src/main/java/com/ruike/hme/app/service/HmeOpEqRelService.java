package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeOpEqRelDTO;
import com.ruike.hme.api.dto.HmeOpEqRelDTO2;
import com.ruike.hme.domain.entity.HmeOpEqRel;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 工艺设备类关系表应用服务
 *
 * @author chaonan.hu@hand-china.com 2020-06-22 09:42:57
 */
public interface HmeOpEqRelService {

    /**
     * 设备类查询LOV
     *
     * @param tenantId    租户id
     * @param dto         查询参数
     * @param pageRequest 分页
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeOpEqRelDTO>
     */
    Page<HmeOpEqRelDTO2> listForLov(Long tenantId, HmeOpEqRelDTO2 dto, PageRequest pageRequest);

    /**
     * 工艺设备类关系表 保存
     *
     * @param hmeOpEqRels 工艺设备类
     * @return java.util.List<com.ruike.hme.domain.entity.HmeOpEqRel>
     */
    List<HmeOpEqRel> createOrUpdate(List<HmeOpEqRel> hmeOpEqRels);

    /**
     * 根据工艺id查询工艺设备类关系
     *
     * @param tenantId    租户ID
     * @param operationId 工艺ID
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeOpEqRelDTO>
     */
    Page<HmeOpEqRelDTO> query(Long tenantId, String operationId, PageRequest pageRequest);
}
