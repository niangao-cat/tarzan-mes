package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfFsmCollectIface;
import com.ruike.itf.domain.entity.ItfSpecCollectIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 光谱仪数据采集接口表Mapper
 *
 * @author yonghui.zhu@hand-china.com 2020-07-13 18:36:00
 */
public interface ItfSpecCollectIfaceMapper extends BaseMapper<ItfSpecCollectIface> {

    void insertIface(@Param("domains")List<ItfSpecCollectIface> domains);

    void updateIface(@Param("domains")List<ItfSpecCollectIface> domains);
}
