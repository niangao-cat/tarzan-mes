package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsLibraryAgeReportDTO;
import com.ruike.wms.api.dto.WmsLibraryAgeReportDTO2;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO2;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO4;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 库龄报表应用服务
 *
 * @author: chaonan.hu@hand-china.com 2020-11-18 14:24:34
 **/
public interface WmsLibraryAgeReportService {

    /**
     * 库龄报表数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/18 15:45:10
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsLibraryAgeReportVO>
     */
    Page<WmsLibraryAgeReportVO> libraryAgeReportQuery(Long tenantId, WmsLibraryAgeReportDTO dto, PageRequest pageRequest);

    /**
     * 库龄分组查询
     * 
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/18 07:16:41 
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsLibraryAgeReportVO2>
     */
    Page<WmsLibraryAgeReportVO2> libraryAgeGroupQuery(Long tenantId, WmsLibraryAgeReportDTO2 dto, PageRequest pageRequest);

    /**
     * 库龄报表导出
     * 
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/19 09:47:35 
     * @return java.util.List<com.ruike.wms.domain.vo.WmsLibraryAgeReportVO4>
     */
    List<WmsLibraryAgeReportVO4> libraryAgeExport(Long tenantId, WmsLibraryAgeReportDTO dto);
}
