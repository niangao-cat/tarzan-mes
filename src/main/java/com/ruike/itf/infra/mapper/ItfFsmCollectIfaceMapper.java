package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfFsmCollectIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 熔接机数据采集接口表Mapper
 *
 * @author yonghui.zhu@hand-china.com 2020-07-13 18:36:01
 */
public interface ItfFsmCollectIfaceMapper extends BaseMapper<ItfFsmCollectIface> {

    void insertIface(@Param("domains")List<ItfFsmCollectIface> domains);

    void updateIface(@Param("domains")List<ItfFsmCollectIface> domains);
}
