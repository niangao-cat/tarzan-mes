package com.ruike.hme.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmePumpModPositionLine;

import java.util.List;

/**
 * 泵浦源模块位置行表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2021-08-25 17:34:37
 */
public interface HmePumpModPositionLineRepository extends BaseRepository<HmePumpModPositionLine> {

    /**
     * 查询EO下所有的位置
     *
     * @param tenantId
     * @param eoId
     * @author sanfeng.zhang@hand-china.com 2021/8/31 16:06
     * @return java.util.List<java.lang.String>
     */
    List<String> queryPumpPositionLineByEoId(Long tenantId, String eoId);
}
