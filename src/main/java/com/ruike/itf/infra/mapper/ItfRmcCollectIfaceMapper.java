package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfRmcCollectIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 反射镜数据采集接口表Mapper
 *
 * @author yonghui.zhu@hand-china.com 2020-08-04 19:51:39
 */
public interface ItfRmcCollectIfaceMapper extends BaseMapper<ItfRmcCollectIface> {

    void insertIface(@Param("domains")List<ItfRmcCollectIface> domains);
}
