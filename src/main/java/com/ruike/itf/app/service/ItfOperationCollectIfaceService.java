package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.OperationCollectItfDTO;

import java.util.List;

/**
 * @ClassName itfOperationCollectIfaceService
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/17 19:46
 * @Version 1.0
 **/
public interface ItfOperationCollectIfaceService {

    List<OperationCollectItfDTO> invoke(Long tenantId, List<OperationCollectItfDTO> collectList);
}
