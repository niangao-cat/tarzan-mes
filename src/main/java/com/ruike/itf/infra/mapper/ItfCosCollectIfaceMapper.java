package com.ruike.itf.infra.mapper;

import com.ruike.hme.domain.entity.HmeCosFunction;
import com.ruike.itf.domain.entity.ItfCosCollectIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * cos测试数据采集接口表Mapper
 *
 * @author wenzhang.yu@hand-china.com 2020-08-28 11:18:23
 */
public interface ItfCosCollectIfaceMapper extends BaseMapper<ItfCosCollectIface> {


     void insertFunction(@Param("domains")List<HmeCosFunction> domains);

    void insertIface(@Param("domains")List<ItfCosCollectIface> domains);
}
