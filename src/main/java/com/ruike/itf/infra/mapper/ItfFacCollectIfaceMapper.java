package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfFacCollectIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * FAC数据采集接口表Mapper
 *
 * @author yonghui.zhu@hand-china.com 2020-08-04 19:51:39
 */
public interface ItfFacCollectIfaceMapper extends BaseMapper<ItfFacCollectIface> {

    void insertIface(@Param("domains")List<ItfFacCollectIface> domains);
}
