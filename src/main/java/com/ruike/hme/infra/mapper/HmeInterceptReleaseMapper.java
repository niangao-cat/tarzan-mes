package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeInterceptRelease;
import com.ruike.hme.domain.vo.HmeInterceptReleaseVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 拦截例外放行表Mapper
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:10
 */
public interface HmeInterceptReleaseMapper extends BaseMapper<HmeInterceptRelease> {
    /**
     * 拦截例外基础查询
     *
     * @param tenantId    租户id
     * @param interceptId 拦截信息头表id
     * @return
     */
    List<HmeInterceptReleaseVO> queryInterceptObject(@Param("tenantId") Long tenantId,
                                                     @Param("interceptId") String interceptId);

    /**
     * 根据拦截对象查询EO标识
     *
     * @param tenantId    租户id
     * @param interceptId 拦截信息头表id
     * @return java.util.List<java.lang.String>
     */
    List<String> queryIndetification(@Param("tenantId") Long tenantId,
                                     @Param("interceptId") String interceptId);
}
