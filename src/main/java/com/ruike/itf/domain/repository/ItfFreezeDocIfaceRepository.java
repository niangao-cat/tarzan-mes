package com.ruike.itf.domain.repository;

import com.ruike.itf.domain.entity.ItfFreezeDocIface;
import org.hzero.mybatis.base.BaseRepository;

/**
 * 条码冻结接口表资源库
 *
 * @author yonghui.zhu@hand-china.com 2021-03-03 10:08:00
 */
public interface ItfFreezeDocIfaceRepository extends BaseRepository<ItfFreezeDocIface> {

    @Override
    int updateByPrimaryKeySelective(ItfFreezeDocIface record);

    int save(ItfFreezeDocIface record);
}
