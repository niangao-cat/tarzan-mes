package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeEoRouterBomRel;
import com.ruike.hme.domain.vo.HmeEoRouterBomRelVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * EO 工艺路线 装配清单关系表Mapper
 *
 * @author yuchao.wang@hand-china.com 2020-12-29 20:14:41
 */
public interface HmeEoRouterBomRelMapper extends BaseMapper<HmeEoRouterBomRel> {

    /**
     *
     * @Description 根据EO查询最近一条指定返修关系
     *
     * @author yuchao.wang
     * @date 2020/12/31 11:06
     * @param tenantId 租户ID
     * @param eoId eoId
     * @return com.ruike.hme.domain.vo.HmeEoRouterBomRelVO
     *
     */
    HmeEoRouterBomRelVO queryLastRelByEoId(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);
}
