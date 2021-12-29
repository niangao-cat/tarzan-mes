package com.ruike.itf.app.service;

import com.ruike.hme.domain.entity.HmeRepairWorkOrderCreate;

import java.util.List;

public interface ItfRepairWorkOrderCreateService {

    /**
     * <strong>Title : hmeRepairWorkOrderCreateService</strong><br/>
     * <strong>Description : 发送ERP创建返修工单 </strong><br/>
     * <strong>Create on : 2020/12/10 下午5:32</strong><br/>
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.domain.entity.HmeRepairWorkOrderCreate
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    HmeRepairWorkOrderCreate hmeRepairWorkOrderCreateService(Long tenantId, HmeRepairWorkOrderCreate dto);

}
