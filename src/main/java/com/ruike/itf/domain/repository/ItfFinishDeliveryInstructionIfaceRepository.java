package com.ruike.itf.domain.repository;

import com.ruike.itf.api.dto.ItfFinishDeliveryInstructionIfaceDTO;
import com.ruike.itf.domain.vo.ItfFinishDeliveryInstructionIfaceVO;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;

import java.util.List;

/**
 * 成品出库指令信息接口
 *
 * @author sanfeng.zhang@hand-china.com 2021/7/16 15:41
 */
public interface ItfFinishDeliveryInstructionIfaceRepository {

    /**
     * 成品出库指令信息接口
     * @param tenantId
     * @param dtoList
     * @return java.util.List<com.ruike.itf.domain.vo.ItfFinishDeliveryInstructionIfaceVO>
     * @author sanfeng.zhang@hand-china.com 2021/7/16
     */
    List<ItfFinishDeliveryInstructionIfaceVO> itfWCSTaskIface(Long tenantId, List<ItfFinishDeliveryInstructionIfaceDTO> dtoList);
}
