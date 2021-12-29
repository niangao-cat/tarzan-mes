package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfLbpCollectIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * lbp数据采集接口Mapper
 *
 * @author wenzhang.yu@hand-china.com 2020-09-04 16:35:53
 */
public interface ItfLbpCollectIfaceMapper extends BaseMapper<ItfLbpCollectIface> {

    void insertIface(@Param("domains")List<ItfLbpCollectIface> domains);

    void updateIface(@Param("domains")List<ItfLbpCollectIface> domains);
}
