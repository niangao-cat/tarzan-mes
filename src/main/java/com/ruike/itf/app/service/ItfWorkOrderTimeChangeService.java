package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfWorkOrderTimeChangeDTO;

import java.util.List;

/**
 * @ClassName ItfWorkOrderTimeChangeService
 * @Description 工单时间变更
 * @Author lkj
 * @Date 2020/12/17
 */
public interface ItfWorkOrderTimeChangeService {

    /**
     * <strong>Title : hmeWorkOrderTimeChange</strong><br/>
     * <strong>Description : 工单时间变更 </strong><br/>
     * <strong>Create on : 2020/12/17 上午11:43</strong><br/>
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.itf.api.dto.ItfWorkOrderTimeChangeDTO
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    List<ItfWorkOrderTimeChangeDTO> hmeWorkOrderTimeChange(Long tenantId, List<ItfWorkOrderTimeChangeDTO> dto);
}
