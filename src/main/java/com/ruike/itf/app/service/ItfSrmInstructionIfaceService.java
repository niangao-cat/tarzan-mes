package com.ruike.itf.app.service;

import com.ruike.itf.domain.entity.ItfSrmInstructionIface;
import io.choerodon.core.domain.Page;

import java.util.List;

/**
 * 送货单状态接口记录表应用服务
 *
 * @author kejin.liu01@hand-china.com 2020-09-09 10:05:04
 */
public interface ItfSrmInstructionIfaceService {

    /**
     * 送货单状态回传SRM
     *
     * @param itfSrmInstructionIface
     * @return
     * @author jiangling.zheng@hand-china.com 2020/9/9 10:36
     */

    List<ItfSrmInstructionIface> sendInstructionDocStatusSrm(List<ItfSrmInstructionIface> itfSrmInstructionIface,Long tenantId);

    /**
     * 查询送货单状态
     * @param instructionDocId
     * @param tenantId
     * @return
     */
    List<ItfSrmInstructionIface> selectMtDocStatus(String instructionDocId, Long tenantId);
}
