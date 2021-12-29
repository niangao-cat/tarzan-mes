package com.ruike.hme.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmePumpModPositionHeader;

import java.util.List;

/**
 * 泵浦源模块位置头表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2021-08-25 17:34:37
 */
public interface HmePumpModPositionHeaderRepository extends BaseRepository<HmePumpModPositionHeader> {

    /**
     * 批量插入
     *
     * @param headerList
     * @author sanfeng.zhang@hand-china.com 2021/8/31 11:54
     * @return void
     */
    void myBatchInsert(List<HmePumpModPositionHeader> headerList);
}
