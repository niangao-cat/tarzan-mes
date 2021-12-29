package com.ruike.itf.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.itf.domain.entity.ItfSoDeliveryLineChanIface;

import java.util.List;

/**
 * 交货单修改过账接口行表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2021-07-09 16:58:10
 */
public interface ItfSoDeliveryLineChanIfaceRepository extends BaseRepository<ItfSoDeliveryLineChanIface> {

    /**
     * 批量新增（开启新事务）
     * @param lineChanIfaceList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/7/13
     */
    void myBatchInsert(List<ItfSoDeliveryLineChanIface> lineChanIfaceList);

    void myBatchUpdateSelective(List<ItfSoDeliveryLineChanIface> lineChanIfaceList);
}
