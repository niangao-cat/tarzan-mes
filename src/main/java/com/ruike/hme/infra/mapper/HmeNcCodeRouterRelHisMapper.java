package com.ruike.hme.infra.mapper;

import java.util.List;

import com.ruike.hme.domain.entity.HmeNcCodeRouterRelHis;
import com.ruike.hme.domain.vo.HmeNcCodeRouterRelHisVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 不良代码工艺路线关系历史表Mapper
 *
 * @author penglin.sui@hand-china.com 2021-03-30 15:50:41
 */
public interface HmeNcCodeRouterRelHisMapper extends BaseMapper<HmeNcCodeRouterRelHis> {
    /**
     * 不良代码指定工艺路线历史查询
     *
     * @param tenantId 租户id
     * @param ncCodeRouterRelId 不良代码指定工艺路线ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcCodeRouterRelVO>
     */
    List<HmeNcCodeRouterRelHisVO> ncCodeRouterRelHisQuery(@Param("tenantId") Long tenantId,
                                                          @Param("ncCodeRouterRelId") String ncCodeRouterRelId);
}
