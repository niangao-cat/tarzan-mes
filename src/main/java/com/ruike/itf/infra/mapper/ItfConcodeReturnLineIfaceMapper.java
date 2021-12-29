package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfConcodeReturnLineIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 成品出入库容器信息返回行表接口表
 *
 * @author taowen.wang@hand-china.com 2021/6/30 13:40
 */
public interface ItfConcodeReturnLineIfaceMapper extends BaseMapper<ItfConcodeReturnLineIface> {

    int batchInsertItfConcodeReturnLineIface(@Param("list") List<ItfConcodeReturnLineIface> itfConcodeReturnLineIfaceList);
}