package com.ruike.itf.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanLotDetailIface;

import java.util.List;

/**
 * 拣配批次明细接口表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2021-07-09 16:58:10
 */
public interface ItfSoDeliveryChanLotDetailIfaceRepository extends BaseRepository<ItfSoDeliveryChanLotDetailIface> {

    void myBatchInsert(List<ItfSoDeliveryChanLotDetailIface> lotDetailIfaceList);

    void myBatchUpdateSelective(List<ItfSoDeliveryChanLotDetailIface> lotDetailIfaceList);
}
