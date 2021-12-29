package com.ruike.itf.infra.repository.impl;

import com.ruike.itf.domain.entity.ItfWoSnRelIface;
import com.ruike.itf.domain.repository.ItfWoSnRelIfaceRepository;
import com.ruike.itf.infra.mapper.ItfWoSnRelIfaceMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Sn同步接口资源库实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/24 4:40 下午
 */
@Component
public class ItfWoSnRelIfaceRepositoryImpl extends BaseRepositoryImpl<ItfWoSnRelIface> implements ItfWoSnRelIfaceRepository {
    private final ItfWoSnRelIfaceMapper itfWoSnRelIfaceMapper;

    @Autowired
    public ItfWoSnRelIfaceRepositoryImpl(ItfWoSnRelIfaceMapper itfWoSnRelIfaceMapper) {
        this.itfWoSnRelIfaceMapper = itfWoSnRelIfaceMapper;
    }

    @Override
    public List<ItfWoSnRelIface> selectInterfaceData(Long tenantId) {
        return itfWoSnRelIfaceMapper.selectInterfaceData(tenantId);
    }

    /**
     *
     * @Description 回写接口表时查询原有数据
     *
     * @author yuchao.wang
     * @date 2020/7/30 17:19
     * @param woSnRelIface
     * @return com.ruike.itf.domain.entity.ItfWoSnRelIface
     *
     */
    @Override
    public ItfWoSnRelIface queryIfaceForReturnWrite(ItfWoSnRelIface woSnRelIface) {
        return itfWoSnRelIfaceMapper.queryIfaceForReturnWrite(woSnRelIface);
    }
}
