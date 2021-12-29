package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeSsnInspectDetailVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeSsnInspectDetail;

import java.util.List;

/**
 * 标准件检验标准明细资源库
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:11
 */
public interface HmeSsnInspectDetailRepository extends BaseRepository<HmeSsnInspectDetail> {

    /**
     * 获取标准件检验标准明细信息
     *
     * @param tenantId
     * @param ssnInspectLineId
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */
    Page<HmeSsnInspectDetailVO> selectDetail(Long tenantId, PageRequest pageRequest, String ssnInspectLineId);

    /**
     * 根据行id删除标准件检验标准明细信息
     *
     * @param tenantId
     * @param ssnInspectLineId
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */
    void deleteByLine(Long tenantId, String ssnInspectLineId);

    /**
     * 根据头id删除标准件检验标准明细信息
     *
     * @param tenantId
     * @param ssnInspectHeaderId
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */
    void deleteDetailByHeader(Long tenantId, String ssnInspectHeaderId);
}
