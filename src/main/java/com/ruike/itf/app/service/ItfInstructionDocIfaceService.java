package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfInstructionReturnDTO;
import com.ruike.itf.api.dto.ItfInstructionSyncDTO;
import com.ruike.itf.domain.entity.ItfInstructionDocIface;

import java.text.ParseException;
import java.util.List;

/**
 * 指令单据头表应用服务
 *
 * @author kejin.liu01@hand-china.com 2020-08-11 11:24:54
 */
public interface ItfInstructionDocIfaceService {

    /**
     *
     * 采购订单同步接口
     * @param itfInstructionSyncDTO
     * @author kejin.liu01@hand-china.com 2020/8/11 20:51
     * @return
     */
    List<ItfInstructionDocIface> invoke(ItfInstructionSyncDTO itfInstructionSyncDTO);

}
