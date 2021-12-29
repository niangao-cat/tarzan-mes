package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfItemGroupReturnDTO;
import com.ruike.itf.api.dto.ItfItemGroupSyncDTO;
import com.ruike.itf.api.dto.ItfSapIfaceDTO;

import java.util.List;

/**
 * 物料组接口表应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-07-17 10:12:42
 */
public interface ItfItemGroupIfaceService {


    /**
     * 物料同步接口
     *
     * @param itemGroupList List<com.ruike.itf.api.dto.ItfItemGroupSyncDTO>
     * @author jiangling.zheng@hand-china.com 2020/7/16 15:04
     * @return List<com.ruike.itf.api.dto.ItfItemGroupReturnDTO>
     */
    List<ItfItemGroupReturnDTO> invoke(List<ItfSapIfaceDTO> itemGroupList);
}
