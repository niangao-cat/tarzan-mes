package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.vo.HmeInterceptObjectVO;
import com.ruike.hme.infra.mapper.HmeInterceptObjectMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeInterceptObject;
import com.ruike.hme.domain.repository.HmeInterceptObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 拦截对象表 资源库实现
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:08
 */
@Component
public class HmeInterceptObjectRepositoryImpl extends BaseRepositoryImpl<HmeInterceptObject> implements HmeInterceptObjectRepository {

    @Autowired
    private HmeInterceptObjectMapper hmeInterceptObjectMapper;

    @Override
    public List<HmeInterceptObjectVO> selectMaterialType(Long tenantId, String siteId, String interceptId) {
        return hmeInterceptObjectMapper.selectMaterialType(tenantId , siteId , interceptId);
    }
}
