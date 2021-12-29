package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeEoRouterBomRelVO;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeEoRouterBomRel;

/**
 * EO 工艺路线 装配清单关系表资源库
 *
 * @author yuchao.wang@hand-china.com 2020-12-29 20:14:41
 */
public interface HmeEoRouterBomRelRepository extends BaseRepository<HmeEoRouterBomRel> {

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
    HmeEoRouterBomRelVO queryLastRelByEoId(Long tenantId, String eoId);
}
