package com.ruike.hme.infra.mapper;

import java.util.List;

import com.ruike.hme.api.dto.HmeNcCodeRouterRelDTO;
import com.ruike.hme.domain.entity.HmeNcCodeRouterRel;
import com.ruike.hme.domain.vo.HmeNcCodeRouterRelVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 不良代码工艺路线关系表Mapper
 *
 * @author penglin.sui@hand-china.com 2021-03-30 15:50:41
 */
public interface HmeNcCodeRouterRelMapper extends BaseMapper<HmeNcCodeRouterRel> {
    /**
     * 不良代码指定工艺路线查询
     *
     * @param tenantId 租户id
     * @param dto 参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcCodeRouterRelVO>
     */
    List<HmeNcCodeRouterRelVO> ncCodeRouterRelQuery(@Param("tenantId") Long tenantId,
                                                    @Param("dto") HmeNcCodeRouterRelDTO dto);
}
