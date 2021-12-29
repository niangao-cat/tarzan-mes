package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsSummaryOfCosBarcodeProcessingDTO;
import com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * @description COS条码加工汇总表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @version 0.0.1
 * @return
 */
public interface WmsSummaryOfCosBarcodeProcessingRepository {

    /**
     * @description COS条码加工汇总表
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/18
     * @time 15:31
     * @version 0.0.1
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WorkOrderInProcessDetailsQueryReportVO>
     */
    Page<WmsSummaryOfCosBarcodeProcessingVO> list(Long tenantId, PageRequest pageRequest, WmsSummaryOfCosBarcodeProcessingDTO dto);
}
