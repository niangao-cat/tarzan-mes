package com.ruike.hme.infra.mapper;

import java.util.List;

import com.ruike.hme.domain.entity.HmeException;
import com.ruike.hme.api.dto.HmeExceptionDTO;
import io.choerodon.mybatis.common.BaseMapper;

import org.apache.ibatis.annotations.Param;

/**
 * 异常维护基础数据头表Mapper
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:25
 */
public interface HmeExceptionMapper extends BaseMapper<HmeException> {
    /**
     * 查询
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.entity.HmeException>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/15 11:31 上午
     */
    List<HmeException> uiQuery(@Param(value = "tenantId") Long tenantId,
                               @Param(value = "dto") HmeExceptionDTO dto);
}
