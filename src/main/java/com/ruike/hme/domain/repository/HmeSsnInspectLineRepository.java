package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeSsnInspectLine;
import com.ruike.hme.domain.vo.HmeSsnInspectLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.ibatis.annotations.Param;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 标准件检验标准行资源库
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:10
 */
public interface HmeSsnInspectLineRepository extends BaseRepository<HmeSsnInspectLine> {

    /**
     * 获取标准件检验标准行表信息
     *
     * @param tenantId
     * @param ssnInspectHeaderId
     * @author li.zhang13@hand-china.com
     * @date 2021-02-01 10:45:10
     */
    Page<HmeSsnInspectLineVO> selectLine(Long tenantId, PageRequest pageRequest, String ssnInspectHeaderId);

    /**
     * 根据头ID删除标准件检验标准行表信息
     *
     * @param tenantId
     * @param ssnInspectHeaderId
     * @author li.zhang13@hand-china.com
     * @date 2021-02-01 10:45:10
     */
    void deleteLineByHeade(Long tenantId, String ssnInspectHeaderId);

    /**
     * 标准件检验标准行历史
     *
     * @param tenantId
     * @param ssnInspectLineId
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeSsnInspectLineVO>
     * @author sanfeng.zhang@hand-china.com 2021/4/8 14:18
     */
    Page<HmeSsnInspectLineVO> ssnInspectLineHisQuery(Long tenantId, String ssnInspectLineId, PageRequest pageRequest);

}
