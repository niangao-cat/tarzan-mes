package com.ruike.qms.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.qms.domain.entity.QmsIqcDetailsHis;
import com.ruike.qms.domain.repository.QmsIqcDetailsHisRepository;
import org.springframework.stereotype.Component;

/**
 * 质检单明细历史表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-06 11:43:28
 */
@Component
public class QmsIqcDetailsHisRepositoryImpl extends BaseRepositoryImpl<QmsIqcDetailsHis> implements QmsIqcDetailsHisRepository {


    @Override
    public void createQmsIqcDetailsHis(QmsIqcDetailsHis qmsIqcDetailsHis) {
        self().insertSelective(qmsIqcDetailsHis);
    }
}
