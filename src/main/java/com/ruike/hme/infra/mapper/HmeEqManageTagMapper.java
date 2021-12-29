package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeEqManageTag;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备管理项目表Mapper
 *
 * @author han.zhang03@hand-china.com 2020-06-10 14:58:50
 */
public interface HmeEqManageTagMapper extends BaseMapper<HmeEqManageTag> {

    /**
     * 批量更新
     *
     * @param tenantId
     * @param userId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/5/9 17:40
     * @return void
     */
    void batchLineUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("dto") List<HmeEqManageTag> dto);
}
