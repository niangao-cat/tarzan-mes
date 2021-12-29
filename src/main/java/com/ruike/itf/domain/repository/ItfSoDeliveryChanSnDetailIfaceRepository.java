package com.ruike.itf.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanSnDetailIface;

import java.util.List;

/**
 * 拣配SN明细接口表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2021-07-09 16:58:09
 */
public interface ItfSoDeliveryChanSnDetailIfaceRepository extends BaseRepository<ItfSoDeliveryChanSnDetailIface> {

    void myBatchInsert(List<ItfSoDeliveryChanSnDetailIface> snDetailIfaceList);

    void myBatchUpdateSelective(List<ItfSoDeliveryChanSnDetailIface> snDetailIfaceList);
}
