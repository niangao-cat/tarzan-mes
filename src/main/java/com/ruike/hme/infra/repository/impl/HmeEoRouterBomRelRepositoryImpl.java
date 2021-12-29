package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.vo.HmeEoRouterBomRelVO;
import com.ruike.hme.infra.mapper.HmeEoRouterBomRelMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeEoRouterBomRel;
import com.ruike.hme.domain.repository.HmeEoRouterBomRelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * EO 工艺路线 装配清单关系表 资源库实现
 *
 * @author yuchao.wang@hand-china.com 2020-12-29 20:14:41
 */
@Component
public class HmeEoRouterBomRelRepositoryImpl extends BaseRepositoryImpl<HmeEoRouterBomRel> implements HmeEoRouterBomRelRepository {

    @Autowired
    private HmeEoRouterBomRelMapper hmeEoRouterBomRelMapper;

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
    @Override
    public HmeEoRouterBomRelVO queryLastRelByEoId(Long tenantId, String eoId) {
        return hmeEoRouterBomRelMapper.queryLastRelByEoId(tenantId, eoId);
    }
}
