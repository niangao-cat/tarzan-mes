package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfConcodeReturnIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 成品出入库容器信息返回头表接口表
 *
 * @author taowen.wang@hand-china.com 2021/6/30 13:40
 */
public interface ItfConcodeReturnIfaceMapper extends BaseMapper<ItfConcodeReturnIface> {

    int insertByItfConcodeReturnIface(@Param("item") ItfConcodeReturnIface itfConcodeReturnIface);

    List<String> selectByAttrId(@Param("tenantId") Long tenantId,@Param("materialLotId") List<String> materialLotId);
}