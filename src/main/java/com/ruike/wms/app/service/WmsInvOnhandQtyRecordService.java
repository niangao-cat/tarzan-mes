package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsInvOnhandQtyRecordDTO2;
import com.ruike.wms.api.dto.WmsInvOnhandQtyRecordDTO4;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 仓库物料每日进销存表应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-11-18 09:53:55
 */
public interface WmsInvOnhandQtyRecordService {

    /**
     * 查询
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @author jiangling.zheng@hand-china.com 2020/11/20 17:34
     * @return io.choerodon.core.domain.Page<com.ruike.wms.api.dto.WmsInvOnhandQtyRecordDTO2>
     */
    Page<WmsInvOnhandQtyRecordDTO2> listForUi(Long tenantId, WmsInvOnhandQtyRecordDTO4 dto, PageRequest pageRequest);

    /**
     * 导出
     *
     * @param tenantId
     * @param dto
     * @param response
     * @author jiangling.zheng@hand-china.com 2020/11/20 17:34
     * @return void
     */

    void export(Long tenantId, WmsInvOnhandQtyRecordDTO4 dto, HttpServletResponse response);

}
