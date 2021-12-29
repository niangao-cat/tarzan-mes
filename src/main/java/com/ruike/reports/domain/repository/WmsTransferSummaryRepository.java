package com.ruike.reports.domain.repository;

import com.ruike.reports.api.dto.WmsTransferSummaryQueryDTO;
import com.ruike.reports.api.dto.WmsTransferSummaryQueryDTO2;
import com.ruike.reports.domain.vo.WmsTransferSummaryVO;
import com.ruike.reports.domain.vo.WmsTransferSummaryVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;


/**
 * <p>
 * 调拨汇总报表 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/23 15:21
 */
public interface WmsTransferSummaryRepository {

    /**
     * 查询分页列表
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return java.util.List<com.ruike.reports.domain.vo.WmsTransferSummaryVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/23 02:51:08
     */
    Page<WmsTransferSummaryVO> pageList(Long tenantId,
                                        WmsTransferSummaryQueryDTO dto,
                                        PageRequest pageRequest);

    /**
     * 调拨单详情报表分页查询
     * 
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页参数
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/28 17:34:03
     * @return java.util.List<com.ruike.reports.domain.vo.WmsTransferSummaryVO2>
     */
    Page<WmsTransferSummaryVO2> selectDetailList(Long tenantId, WmsTransferSummaryQueryDTO2 dto, PageRequest pageRequest);
}
