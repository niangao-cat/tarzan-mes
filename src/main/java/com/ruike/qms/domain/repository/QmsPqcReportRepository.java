package com.ruike.qms.domain.repository;

import com.ruike.qms.api.dto.QmsPqcReportDTO;
import com.ruike.qms.domain.vo.QmsPqcReportVO;
import com.ruike.qms.domain.vo.QmsPqcReportVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 巡检报表资源库
 *
 * @author chaonan.hu@hand-china.com 2020/12/11 15:13:23
 */
public interface QmsPqcReportRepository extends AopProxy<QmsPqcReportRepository> {

    /**
     * 巡检报表-头部数据分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/11 15:13:21
     * @return io.choerodon.core.domain.Page<com.ruike.qms.domain.vo.QmsPqcReportVO>
     */
    Page<QmsPqcReportVO> pqcReportHeadDataQuery(Long tenantId, QmsPqcReportDTO dto, PageRequest pageRequest);

    /**
     * 巡检报表-明细数据分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/12 01:34:22
     * @return io.choerodon.core.domain.Page<com.ruike.qms.domain.vo.QmsPqcReportVO2>
     */
    Page<QmsPqcReportVO2> pgcReportDetailDataQuery(Long tenantId,  QmsPqcReportDTO dto, PageRequest pageRequest);
}
