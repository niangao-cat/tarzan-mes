package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeNonStandardDetailsVO;
import com.ruike.hme.domain.vo.HmeNonStandardReportVO;
import com.ruike.hme.domain.vo.HmeNonStandardReportVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/14 16:51
 */
public interface HmeCommonReportRepository {

    /**
     * 非标报表
     *
     * @param tenantId    租户id
     * @param dto         参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeNonStandardReportVO2>
     * @author sanfeng.zhang@hand-china.com 2020/12/14 17:19
     */
    Page<HmeNonStandardReportVO2> nonStandardProductReportQuery(Long tenantId, HmeNonStandardReportVO dto, PageRequest pageRequest);

    /**
     * 待上线数量明细
     *
     * @param tenantId    租户
     * @param workOrderId 工单
     * @param pageRequest 分页
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeNonStandardDetailsVO>
     * @author sanfeng.zhang@hand-china.com 2020/12/14 20:16
     */
    Page<HmeNonStandardDetailsVO> waitQtyDetailsQuery(Long tenantId, String workOrderId, PageRequest pageRequest);

    /**
     * 在线数量明细
     *
     * @param tenantId    租户
     * @param workOrderId 工单
     * @param pageRequest 分页
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeNonStandardDetailsVO>
     * @author sanfeng.zhang@hand-china.com 2020/12/14 20:26
     */
    Page<HmeNonStandardDetailsVO> onlineQtyDetailsQuery(Long tenantId, String workOrderId, PageRequest pageRequest);

    /**
     * 完工数量明细
     *
     * @param tenantId    租户
     * @param workOrderId 工单
     * @param pageRequest 分页
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeNonStandardDetailsVO>
     * @author sanfeng.zhang@hand-china.com 2020/12/14 20:26
     */
    Page<HmeNonStandardDetailsVO> completedQtyDetailsQuery(Long tenantId, String workOrderId, PageRequest pageRequest);


}
