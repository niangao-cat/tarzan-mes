package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfInstructionAddrIfaceDTO;

import java.util.List;

/**
 * 送货单的文件地址表应用服务
 *
 * @author kejin.liu@hand-china.com 2020-10-27 15:24:39
 */
public interface ItfInstructionAddrIfaceService {

    /**
     * SRM系统-送货单地址创建Rest接口
     *
     * @param ifaceSyncDTOS
     * @return
     */
    List<ItfInstructionAddrIfaceDTO> srmInstructionAddrCreate(List<ItfInstructionAddrIfaceDTO> ifaceSyncDTOS);
}
