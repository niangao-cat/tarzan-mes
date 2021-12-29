package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfZzqCollectIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 准直器耦合接口表Mapper
 *
 * @author wenzhang.yu@hand-china 2020-12-16 13:43:44
 */
public interface ItfZzqCollectIfaceMapper extends BaseMapper<ItfZzqCollectIface> {


    void insertIface(@Param("domains")List<ItfZzqCollectIface> domains);

    void updateIface(@Param("domains")List<ItfZzqCollectIface> domains);
}
