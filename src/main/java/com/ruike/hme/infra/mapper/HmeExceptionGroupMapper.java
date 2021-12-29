package com.ruike.hme.infra.mapper;

import java.util.List;

import com.ruike.hme.api.dto.HmeExceptionGroupDTO;
import com.ruike.hme.domain.entity.HmeExceptionGroup;
import io.choerodon.mybatis.common.BaseMapper;

import org.apache.ibatis.annotations.Param;

/**
 * 异常收集组基础数据表Mapper
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:24
 */
public interface HmeExceptionGroupMapper extends BaseMapper<HmeExceptionGroup> {

    /**
     * 查询
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return java.util.List<com.ruike.hme.domain.entity.HmeExceptionGroup>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/15 11:26 上午
     */
    List<HmeExceptionGroup> uiQuery(@Param(value = "tenantId") Long tenantId,
                                    @Param(value = "dto") HmeExceptionGroupDTO dto);
}
