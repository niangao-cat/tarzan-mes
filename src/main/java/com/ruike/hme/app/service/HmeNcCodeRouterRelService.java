package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeNcCodeRouterRelDTO;
import com.ruike.hme.domain.entity.HmeNcCodeRouterRel;
import com.ruike.hme.domain.vo.HmeNcCodeRouterRelVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 不良代码工艺路线关系表应用服务
 *
 * @author penglin.sui@hand-china.com 2021-03-30 15:50:41
 */
public interface HmeNcCodeRouterRelService {
    /**
     * 不良代码指定工艺路线查询
     *
     * @param tenantId    租户ID
     * @param dto         查询参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeNcCodeRouterRelVO>
     * @author penglin.sui@hand-china.com 2021/3/30 16:19:03
     */
    Page<HmeNcCodeRouterRelVO> ncCodeRouterRelList(Long tenantId, HmeNcCodeRouterRelDTO dto, PageRequest pageRequest);

    /**
     * 保存不良代码指定工艺路线
     *
     * @param tenantId    租户ID
     * @param dto         保存参数
     * @return com.ruike.hme.domain.entity.HmeNcCodeRouterRel
     * @author penglin.sui@hand-china.com 2021/3/30 17:12
     */
    HmeNcCodeRouterRel ncCodeRouterRelSave(Long tenantId, HmeNcCodeRouterRel dto);
}
