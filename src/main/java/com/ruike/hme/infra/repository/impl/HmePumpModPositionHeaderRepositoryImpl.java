package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.infra.mapper.HmePumpModPositionHeaderMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmePumpModPositionHeader;
import com.ruike.hme.domain.repository.HmePumpModPositionHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 泵浦源模块位置头表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2021-08-25 17:34:37
 */
@Component
public class HmePumpModPositionHeaderRepositoryImpl extends BaseRepositoryImpl<HmePumpModPositionHeader> implements HmePumpModPositionHeaderRepository {

    @Autowired
    private HmePumpModPositionHeaderMapper hmePumpModPositionHeaderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void myBatchInsert(List<HmePumpModPositionHeader> headerList) {
        hmePumpModPositionHeaderMapper.myBatchInsert(headerList);
    }
}
