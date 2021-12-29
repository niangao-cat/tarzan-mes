package com.ruike.itf.infra.repository.impl;

import com.ruike.itf.domain.entity.ItfFreezeDocIface;
import com.ruike.itf.domain.repository.ItfFreezeDocIfaceRepository;
import com.ruike.itf.infra.mapper.ItfFreezeDocIfaceMapper;
import org.apache.commons.lang.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;

/**
 * 条码冻结接口表 资源库实现
 *
 * @author yonghui.zhu@hand-china.com 2021-03-03 10:08:00
 */
@Component
public class ItfFreezeDocIfaceRepositoryImpl extends BaseRepositoryImpl<ItfFreezeDocIface> implements ItfFreezeDocIfaceRepository {
    private final ItfFreezeDocIfaceMapper mapper;

    public ItfFreezeDocIfaceRepositoryImpl(ItfFreezeDocIfaceMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public int updateByPrimaryKeySelective(ItfFreezeDocIface record) {
        return this.mapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int save(ItfFreezeDocIface record) {
        if (StringUtils.isBlank(record.getInterfaceId())) {
            return this.insertSelective(record);
        } else {
            return this.updateByPrimaryKeySelective(record);
        }
    }
}
