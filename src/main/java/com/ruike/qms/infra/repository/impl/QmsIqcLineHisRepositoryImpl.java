package com.ruike.qms.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.qms.domain.entity.QmsIqcLineHis;
import com.ruike.qms.domain.repository.QmsIqcLineHisRepository;
import org.springframework.stereotype.Component;

/**
 * 质检单行历史表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-06 11:43:28
 */
@Component
public class QmsIqcLineHisRepositoryImpl extends BaseRepositoryImpl<QmsIqcLineHis> implements QmsIqcLineHisRepository {


    @Override
    public void createQmsIqcLineHis(QmsIqcLineHis qmsIqcLineHis) {
        self().insertSelective(qmsIqcLineHis);
    }
}
