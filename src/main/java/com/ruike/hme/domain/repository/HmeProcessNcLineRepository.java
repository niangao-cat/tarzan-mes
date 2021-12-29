package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeProcessNcLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeProcessNcLine;

/**
 * 工序不良行表资源库
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
public interface HmeProcessNcLineRepository extends BaseRepository<HmeProcessNcLine> {

    /**
     * 获取工序不良行表信息
     *
     * @param tenantId
     * @param headerId
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */
    Page<HmeProcessNcLineVO> selectLine(Long tenantId, String headerId, PageRequest pageRequest);

    /**
     * 删除工序不良行表信息
     *
     * @param tenantId
     * @param headerId
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */
    void deleteLineByHeader(Long tenantId, String headerId);
}
