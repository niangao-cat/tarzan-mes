package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeRepairLimitCountDTO;
import com.ruike.hme.domain.entity.HmeRepairLimitCount;
import com.ruike.hme.domain.vo.HmeRepairLimitCountVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * @author yaqiong.zhou@raycus.com 2021/9/7 10:09
 */
public interface HmeRepairLimitCountRepository extends BaseRepository<HmeRepairLimitCount>, AopProxy<HmeRepairLimitCountRepository> {
    /**
     * 查询限制返修进站的次数
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @return
     */
    Page<HmeRepairLimitCountVO> queryRepairLimitCountList(Long tenantId, PageRequest pageRequest, HmeRepairLimitCountDTO dto);

    /**
     * 根据 Id 删除返修进站次数的限制
     * hme_repair_limit_count
     * @param tenantId
     * @param list
     */
    void deleteRepairLimitCountByIds(Long tenantId, List<String> list);

    /**
     * 更新返修进站次数限制
     * @param tenantId
     * @param dtoList
     * @return
     */
    List<HmeRepairLimitCountDTO> createOrUpdateRepairLimitCount(Long tenantId, List<HmeRepairLimitCountDTO> dtoList);
}
