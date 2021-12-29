package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsLibraryAgeReportDTO;
import com.ruike.wms.api.dto.WmsLibraryAgeReportDTO2;
import com.ruike.wms.api.dto.WmsLibraryAgeReportDTO3;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO2;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO4;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库龄报表Mapper
 *
 * @author chaonan.hu@hand-china.com 2020/11/18 15:50:34
 */
public interface WmsLibraryAgeReportMapper {

    /**
     * 库龄报表数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/18 15:51:02
     * @return List<WmsLibraryAgeReportVO>
     */
    List<WmsLibraryAgeReportVO> libraryAgeReportQuery(@Param(value = "tenantId") Long tenantId,
                                                      @Param(value = "dto") WmsLibraryAgeReportDTO dto);

    /**
     * 库龄分组查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/18 19:23:15
     * @return java.util.List<com.ruike.wms.domain.vo.WmsLibraryAgeReportVO2>
     */
    List<WmsLibraryAgeReportVO2> libraryAgeGroupQuery(@Param(value = "tenantId") Long tenantId,
                                                      @Param(value = "dto") WmsLibraryAgeReportDTO2 dto);

    /**
     * 库龄区间下条码数量之和查询
     * 
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param lot 批次
     * @param locatorId 货位ID
     * @param dto 库龄区间
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/18 19:47:46
     * @return java.math.BigDecimal
     */
    BigDecimal qtySumQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "materialId") String materialId,
                           @Param(value = "lot") String lot, @Param(value = "locatorId") String locatorId,
                           @Param(value = "dto") WmsLibraryAgeReportDTO3 dto);

    /**
     * 库龄报表导出数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/19 09:57:03
     * @return List<WmsLibraryAgeReportVO4>
     */
    List<WmsLibraryAgeReportVO4> libraryAgeExportQuery(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "dto") WmsLibraryAgeReportDTO dto);
}
