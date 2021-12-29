package com.ruike.reports.domain.repository;

import com.ruike.reports.api.dto.QmsIqcInspectionKanbanQueryDTO;
import com.ruike.reports.api.dto.QmsSupplierQualityQueryDTO;
import com.ruike.reports.domain.vo.ChartsSquareResultVO;
import com.ruike.reports.domain.vo.QmsIqcInspectionKanbanVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * <p>
 * IQC检验看板 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 10:53
 */
public interface QmsIqcInspectionKanbanRepository {

    /**
     * 分页查询看板数据
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return java.util.List<com.ruike.reports.domain.vo.QmsIqcInspectionKanbanVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 10:43:31
     */
    Page<QmsIqcInspectionKanbanVO> pagedKanbanList(Long tenantId,
                                                   QmsIqcInspectionKanbanQueryDTO dto,
                                                   PageRequest pageRequest);

    /**
     * 查询看板列表
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.reports.domain.vo.QmsIqcInspectionKanbanVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 10:43:31
     */
    List<QmsIqcInspectionKanbanVO> kanbanListGet(Long tenantId,
                                                 QmsIqcInspectionKanbanQueryDTO dto);

    /**
     * 供应商来料质量数据查询
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return java.util.List<com.ruike.reports.domain.vo.QmsIqcInspectionKanbanVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 11:14:34
     */
    Page<QmsIqcInspectionKanbanVO> pagedQualityList(Long tenantId,
                                                    QmsSupplierQualityQueryDTO dto,
                                                    PageRequest pageRequest);

    /**
     * 供应商来料质量数据查询
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.reports.domain.vo.QmsIqcInspectionKanbanVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 11:14:34
     */
    List<QmsIqcInspectionKanbanVO> qualityListGet(Long tenantId,
                                                  QmsSupplierQualityQueryDTO dto);

    /**
     * 供应商来料质量图表map获取
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.reports.domain.vo.QmsIqcInspectionKanbanVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 11:14:34
     */
    ChartsSquareResultVO qualityChartMapGet(Long tenantId,
                                            QmsSupplierQualityQueryDTO dto);

}
