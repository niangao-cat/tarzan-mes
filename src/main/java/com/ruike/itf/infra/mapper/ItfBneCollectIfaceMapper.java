package com.ruike.itf.infra.mapper;

import com.ruike.itf.api.dto.BneCollectItfDTO1;
import com.ruike.itf.domain.entity.ItfBneCollectIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * BNE数据采集接口表Mapper
 *
 * @author wenzhang.yu@hand-china.com 2020-09-12 13:59:43
 */
public interface ItfBneCollectIfaceMapper extends BaseMapper<ItfBneCollectIface> {

    BneCollectItfDTO1 selectWorkType(@Param(value="materialLotId") String materialLotId);

}
