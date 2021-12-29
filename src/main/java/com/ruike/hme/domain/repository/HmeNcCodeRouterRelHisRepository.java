package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeNcCodeRouterRelHisVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeNcCodeRouterRelHis;

/**
 * 不良代码工艺路线关系历史表资源库
 *
 * @author penglin.sui@hand-china.com 2021-03-30 15:50:41
 */
public interface HmeNcCodeRouterRelHisRepository extends BaseRepository<HmeNcCodeRouterRelHis> {
    /**
     * 不良代码指定工艺路线历史查询
     *
     * @param tenantId    租户ID
     * @param ncCodeRouterRelId 不良代码指定工艺路线ID
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeNcCodeRouterRelHisVO>
     * @author penglin.sui@hand-china.com 2021/3/31 13:45
     */
    Page<HmeNcCodeRouterRelHisVO> ncCodeRouterRelHisList(Long tenantId, String ncCodeRouterRelId, PageRequest pageRequest);
}
