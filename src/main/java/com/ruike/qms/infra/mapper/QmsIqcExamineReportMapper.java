package com.ruike.qms.infra.mapper;


import com.ruike.qms.api.dto.QmsIqcExamineReportDTO;
import com.ruike.qms.domain.vo.QmsIqcExamineReportVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * ICQ检验报表Mapper
 *
 * @author chaonan.hu@hand-china.com 2020-12-10 09:56:23
 */
public interface QmsIqcExamineReportMapper {

    /**
     * 分页查询IQC检验报表数据
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/10 11:15:06
     * @return java.util.List<com.ruike.qms.domain.vo.QmsIqcExamineReportVO>
     */
    List<QmsIqcExamineReportVO> iqcExamineReportQuery(@Param("tenantId") Long tenantId, @Param("dto") QmsIqcExamineReportDTO dto);

    /**
     * 饼状图-检验总数查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/10 14:41:10
     * @return java.math.BigDecimal
     */
    BigDecimal totalNumQuery(@Param("tenantId") Long tenantId, @Param("dto") QmsIqcExamineReportDTO dto);

    /**
     * 饼状图-合格数查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/10 14:43:18
     * @return java.math.BigDecimal
     */
    BigDecimal okNumQuery(@Param("tenantId") Long tenantId, @Param("dto") QmsIqcExamineReportDTO dto);

    /**
     * 饼状图-不合格数查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/10 14:43:18
     * @return java.math.BigDecimal
     */
    BigDecimal ngNumQuery(@Param("tenantId") Long tenantId, @Param("dto") QmsIqcExamineReportDTO dto);
}
