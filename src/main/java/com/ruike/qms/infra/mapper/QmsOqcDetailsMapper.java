package com.ruike.qms.infra.mapper;

import com.ruike.qms.domain.entity.QmsOqcDetails;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 出库检明细表Mapper
 *
 * @author yuchao.wang@hand-china.com 2020-08-28 14:18:07
 */
public interface QmsOqcDetailsMapper extends BaseMapper<QmsOqcDetails> {

    /**
     *
     * @Description 根据头ID删除明细数据
     *
     * @author yuchao.wang
     * @date 2020/8/29 17:23
     * @param tenantId 租户ID
     * @param headerId 头ID
     * @return int
     *
     */
    int deleteByHeaderId(@Param("tenantId") Long tenantId, @Param("headerId") String headerId);
}
