package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeRepairLimitCountDTO;
import com.ruike.hme.domain.entity.HmeRepairLimitCount;
import com.ruike.hme.domain.vo.HmeRepairLimitCountVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author yaqiong.zhou@raycus.com 2021/9/7 10:12
 */
public interface HmeRepairLimitCountMapper extends BaseMapper<HmeRepairLimitCount> {

    /**
     * 查询返修进站限制
     * hme_repair_limit_count
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeRepairLimitCountVO> queryRepairLimitCountList(@Param("tenantId") Long tenantId, @Param("dto") HmeRepairLimitCountDTO dto);

    /**
     * 根据 Id 删除返修进站限制
     * @param tenantId
     * @param list
     */
    void deleteRepairLimitCountByIds(@Param("tenantId") Long tenantId, @Param("list") List<String> list);
}
