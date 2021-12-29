package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfInvItemReturnDTO;

import java.util.List;
import java.util.Map;

/**
 * 物料接口表应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
public interface ItfInvItemIfaceService {

    /**
     * 物料同步接口
     *
     * @param itemMap List<com.ruike.itf.api.dto.ItfInvItemSyncDTO>
     * @author jiangling.zheng@hand-china.com 2020/7/16 15:04
     * @return List<com.ruike.itf.api.dto.ItfInvItemReturnDTO>
     */
    List<ItfInvItemReturnDTO> invoke(Map<String,Object> itemMap);
}
