package com.ruike.hme.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeWoSnRel;

import java.util.*;

/**
 * 生产订单SN码关系表资源库
 *
 * @author penglin.sui@hand-china.com 2020-08-14 17:31:42
 */
public interface HmeWoSnRelRepository extends BaseRepository<HmeWoSnRel> {

    /**
     * 
     * @Description 批量新增
     * 
     * @author yuchao.wang
     * @date 2020/11/6 16:43
     * @param insertList 新增数据列表
     * @return void
     * 
     */
    void myBatchInsert(List<HmeWoSnRel> insertList);
}
