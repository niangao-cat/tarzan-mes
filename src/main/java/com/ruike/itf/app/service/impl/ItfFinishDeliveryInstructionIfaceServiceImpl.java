package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfFinishDeliveryInstructionIfaceDTO;
import com.ruike.itf.app.service.ItfFinishDeliveryInstructionIfaceService;
import com.ruike.itf.domain.repository.ItfFinishDeliveryInstructionIfaceRepository;
import com.ruike.itf.domain.vo.ItfFinishDeliveryInstructionIfaceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 成品出库指令信息接口
 *
 * @author sanfeng.zhang@hand-china.com 2021/7/16 15:40
 */
@Service
public class ItfFinishDeliveryInstructionIfaceServiceImpl implements ItfFinishDeliveryInstructionIfaceService {

    @Autowired
    private ItfFinishDeliveryInstructionIfaceRepository itfFinishDeliveryInstructionIfaceRepository;

    @Override
    public List<ItfFinishDeliveryInstructionIfaceVO> itfWCSTaskIface(Long tenantId, List<ItfFinishDeliveryInstructionIfaceDTO> dtoList) {
        return itfFinishDeliveryInstructionIfaceRepository.itfWCSTaskIface(tenantId, dtoList);
    }
}
