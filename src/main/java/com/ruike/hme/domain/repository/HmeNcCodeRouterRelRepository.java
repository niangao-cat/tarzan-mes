package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeNcCodeRouterRelDTO;
import com.ruike.hme.domain.vo.HmeNcCodeRouterRelVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeNcCodeRouterRel;

/**
 * 不良代码工艺路线关系表资源库
 *
 * @author penglin.sui@hand-china.com 2021-03-30 15:50:41
 */
public interface HmeNcCodeRouterRelRepository extends BaseRepository<HmeNcCodeRouterRel> {
    /**
     * 不良代码指定工艺路线查询
     *
     * @param tenantId    租户ID
     * @param dto         查询参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeNcCodeRouterRelVO>
     * @author penglin.sui@hand-china.com 2021/3/30 16:29:03
     */
    Page<HmeNcCodeRouterRelVO> ncCodeRouterRelList(Long tenantId, HmeNcCodeRouterRelDTO dto, PageRequest pageRequest);
}
