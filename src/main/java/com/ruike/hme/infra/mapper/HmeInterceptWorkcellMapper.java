package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeInterceptWorkcell;
import com.ruike.hme.domain.vo.HmeInterceptWorkcellVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 拦截工序表Mapper
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:11
 */
public interface HmeInterceptWorkcellMapper extends BaseMapper<HmeInterceptWorkcell> {
    /**
     * 拦截工序表基础查询
     *
     * @param tenantId    租户id
     * @param interceptId 信息表主键
     * @return
     */
    List<HmeInterceptWorkcellVO> queryInterceptWorkcell(@Param("tenantId") Long tenantId,
                                                        @Param("interceptId") String interceptId
    );
}
