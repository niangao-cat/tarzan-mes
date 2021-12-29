package com.ruike.qms.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.qms.domain.entity.QmsIqcLineHis;

/**
 * 质检单行历史表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-06 11:43:28
 */
public interface QmsIqcLineHisRepository extends BaseRepository<QmsIqcLineHis>, AopProxy<QmsIqcLineHisRepository> {

    /**
     * 质检单行历史
     *
     * @param qmsIqcLineHis
     * @author sanfeng.zhang@hand-china.com 2020/8/6 15:26
     * @return void
     */
    void createQmsIqcLineHis(QmsIqcLineHis qmsIqcLineHis);

}
