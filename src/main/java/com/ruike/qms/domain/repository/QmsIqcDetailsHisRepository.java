package com.ruike.qms.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.qms.domain.entity.QmsIqcDetailsHis;

/**
 * 质检单明细历史表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-06 11:43:28
 */
public interface QmsIqcDetailsHisRepository extends BaseRepository<QmsIqcDetailsHis>, AopProxy<QmsIqcDetailsHisRepository> {

    /**
     * 质检单明细历史
     *
     * @param qmsIqcDetailsHis
     * @author sanfeng.zhang@hand-china.com 2020/8/6 15:30
     * @return void
     */
    void createQmsIqcDetailsHis(QmsIqcDetailsHis qmsIqcDetailsHis);
}
