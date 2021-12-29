package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfSoDeliveryChanOrPostDTO;
import com.ruike.itf.app.service.ItfSoDeliveryChanOrPostIfaceService;
import com.ruike.itf.domain.repository.ItfSoDeliveryChanOrPostIfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 交货单修改过账接口头表应用服务默认实现
 *
 * @author sanfeng.zhang@hand-china.com 2021-07-09 16:58:10
 */
@Service
public class ItfSoDeliveryChanOrPostIfaceServiceImpl implements ItfSoDeliveryChanOrPostIfaceService {

    @Autowired
    private ItfSoDeliveryChanOrPostIfaceRepository itfSoDeliveryChanOrPostIfaceRepository;

    @Override
    public void soDeliveryChangeOrPostIface(Long tenantId, List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostList) {
        itfSoDeliveryChanOrPostIfaceRepository.soDeliveryChangeOrPostIface(tenantId, itfSoDeliveryChanOrPostList);
    }
}
