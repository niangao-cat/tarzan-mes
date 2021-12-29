package com.ruike.reports.domain.repository;

import com.ruike.reports.api.dto.HmeCosWorkcellSummaryQueryDTO;
import com.ruike.reports.domain.vo.HmeCosWorkcellSummaryVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;


/**
 * <p>
 * COS工位加工汇总 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/1 16:08
 */
public interface HmeCosWorkcellSummaryRepository {

    /**
     * 根据条件查询列表
     *
     * @param tenantId    租户
     * @param dto         参数
     * @param pageRequest 分页参数
     * @return java.util.List<com.ruike.reports.domain.vo.HmeCosWorkcellSummaryVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/1 03:58:52
     */
    Page<HmeCosWorkcellSummaryVO> pageList(Long tenantId,
                                           HmeCosWorkcellSummaryQueryDTO dto,
                                           PageRequest pageRequest);

    /**
     * 根据条件查询列表
     *
     * @param tenantId 租户
     * @param dto      参数
     * @return java.util.List<com.ruike.reports.domain.vo.HmeCosWorkcellSummaryVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/1 03:58:52
     */
    List<HmeCosWorkcellSummaryVO> list(Long tenantId,
                                       HmeCosWorkcellSummaryQueryDTO dto);
}
