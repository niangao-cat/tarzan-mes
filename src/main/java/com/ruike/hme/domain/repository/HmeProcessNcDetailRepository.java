package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeProcessNcLine;
import com.ruike.hme.domain.vo.HmeProcessNcDetailVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeProcessNcDetail;

/**
 * 工序不良明细表资源库
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
public interface HmeProcessNcDetailRepository extends BaseRepository<HmeProcessNcDetail> {

    /**
     * 获取工序不良行表明细信息
     *
     * @param tenantId
     * @param lineId
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */
    Page<HmeProcessNcDetailVO> selectDetail(Long tenantId, String lineId, PageRequest pageRequest);
    /**
     * 根据行ID删除工序不良行表明细信息
     *
     * @param tenantId
     * @param hmeProcessNcLine
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */
    void deleteByLine(Long tenantId, HmeProcessNcLine hmeProcessNcLine);

    /**
     * 根据头ID删除工序不良行表明细信息
     *
     * @param tenantId
     * @param headerId
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */

    void deleteDetailByHeader(Long tenantId, String headerId);
}
