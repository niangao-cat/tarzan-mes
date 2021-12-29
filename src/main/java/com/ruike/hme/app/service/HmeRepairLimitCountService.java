package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeRepairLimitCountDTO;
import com.ruike.hme.domain.vo.HmeRepairLimitCountVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;


/**
 * @author yaqiong.zhou@raycus.com 2021/9/7 10:11
 */
public interface HmeRepairLimitCountService {
    /**
     * 查询限制返修进站的次数
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @return
     */
    Page<HmeRepairLimitCountVO> queryRepairLimitCountList(Long tenantId, PageRequest pageRequest, HmeRepairLimitCountDTO dto);

    /**
     * 删除返修进站次数的限制
     * 根据 Id 删除 hme_repair_limit_count 表数据
     * @param tenantId
     * @param list
     */
    void deleteRepairLimitCountByIds(Long tenantId, List<String> list);

    /**
     * 保存返修进站次数限制
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeRepairLimitCountDTO> createOrUpdateRepairLimitCount(Long tenantId, List<HmeRepairLimitCountDTO> dto);
}
