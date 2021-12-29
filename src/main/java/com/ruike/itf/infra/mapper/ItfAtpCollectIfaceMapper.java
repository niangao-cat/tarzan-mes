package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfAtpCollectIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自动化测试接口表Mapper
 *
 * @author wenzhang.yu@hand-china 2021-01-06 11:37:08
 */
public interface ItfAtpCollectIfaceMapper extends BaseMapper<ItfAtpCollectIface> {

    void insertIface(@Param("domains") List<ItfAtpCollectIface> domains);

    void updateIface(@Param("domains") List<ItfAtpCollectIface> domains);
}
