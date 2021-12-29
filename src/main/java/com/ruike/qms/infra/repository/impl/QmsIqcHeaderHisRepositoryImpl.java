package com.ruike.qms.infra.repository.impl;

import com.ruike.qms.infra.mapper.QmsIqcHeaderHisMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.qms.domain.entity.QmsIqcHeaderHis;
import com.ruike.qms.domain.repository.QmsIqcHeaderHisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 质检单头历史表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-06 11:43:28
 */
@Component
public class QmsIqcHeaderHisRepositoryImpl extends BaseRepositoryImpl<QmsIqcHeaderHis> implements QmsIqcHeaderHisRepository {

    @Autowired
    private QmsIqcHeaderHisMapper qmsIqcHeaderHisMapper;

    @Override
    public void createQmsIqcHeaderHis(QmsIqcHeaderHis qmsIqcHeaderHis) {
        self().insertSelective(qmsIqcHeaderHis);
    }

    @Override
    public List<String> queryMaterialLotIdByLine(Long tenantId, String instructionId) {
        return qmsIqcHeaderHisMapper.queryMaterialLotIdByLine(tenantId,instructionId);
    }
}
