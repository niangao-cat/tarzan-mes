package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeInterceptObject;
import com.ruike.hme.domain.vo.HmeInterceptObjectVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 拦截对象表Mapper
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:08
 */
public interface HmeInterceptObjectMapper extends BaseMapper<HmeInterceptObject> {

    /**
     * 拦截对象基础数据查询
     *
     * @param tenantId    租户id
     * @param interceptId 拦截信息表主键id
     * @return
     */
    List<HmeInterceptObjectVO> queryInterceptObject(@Param("tenantId") Long tenantId,
                                                    @Param("interceptId") String interceptId);

    /**
     * 查询物料类型
     *
     * @param tenantId 租户ID
     * @param siteId 站点id
     * @param interceptId 拦截单id
     * @return java.util.List<com.ruike.hme.domain.vo.HmeInterceptObjectVO>
     */
    List<HmeInterceptObjectVO> selectMaterialType(@Param("tenantId") Long tenantId,
                                                  @Param("siteId") String siteId ,
                                                  @Param("interceptId") String interceptId);
}