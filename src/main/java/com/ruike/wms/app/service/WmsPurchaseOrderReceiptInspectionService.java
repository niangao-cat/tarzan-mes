package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsPurchaseOrderReceiptInspectionDTO;
import com.ruike.wms.domain.vo.WmsPurchaseOrderReceiptInspectionVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/09 13:42
 */
public interface WmsPurchaseOrderReceiptInspectionService {

    Page<WmsPurchaseOrderReceiptInspectionVO> queryList(Long tenantId, WmsPurchaseOrderReceiptInspectionDTO dto, PageRequest pageRequest);

    List<WmsPurchaseOrderReceiptInspectionVO> export(Long tenantId, WmsPurchaseOrderReceiptInspectionDTO dto, ExportParam exportParam);
}
