package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeInspectorItemGroupRelDTO;
import com.ruike.hme.domain.vo.HmeInspectorItemGroupRelVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 检验员与物料组关系表应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-03-29 13:44:29
 */
public interface HmeInspectorItemGroupRelService {

    /**
     * 分页查询检验员与物料组关系
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/29 02:16:31
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeInspectorItemGroupRelVO>
     */
    Page<HmeInspectorItemGroupRelVO> relPageQuery(Long tenantId, HmeInspectorItemGroupRelDTO dto, PageRequest pageRequest);

    /**
     * 新建或者更新检验员与物料组关系
     *
     * @param tenantId 租户ID
     * @param dtoList 新建或者更新信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/29 02:37:48
     * @return java.util.List<com.ruike.hme.domain.vo.HmeInspectorItemGroupRelVO>
     */
    List<HmeInspectorItemGroupRelVO> relCreateOrUpdate(Long tenantId, List<HmeInspectorItemGroupRelVO> dtoList);
}
