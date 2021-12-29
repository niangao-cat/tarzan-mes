package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfDeliveryDocReturnDTO;
import com.ruike.itf.domain.vo.ItfDeliveryDocAndLineIfaceVO;
import com.ruike.itf.domain.vo.ItfPoDeliveryRelHandlerVO;

import java.util.List;

/**
 * 送货单接口头表应用服务
 *
 * @author yapeng.yao@hand-china.com 2020-09-04 16:29:21
 */
public interface ItfDeliveryDocIfaceService {

    List<ItfDeliveryDocReturnDTO> invoke(List<ItfDeliveryDocAndLineIfaceVO> voList) throws Exception;

    /**
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com 2020/10/20 12:22
     */
    List<ItfPoDeliveryRelHandlerVO> sendTHDeliveryDoc(List<ItfPoDeliveryRelHandlerVO> dto);
}
