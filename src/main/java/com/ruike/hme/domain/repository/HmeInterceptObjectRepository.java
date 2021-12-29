package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeInterceptObjectVO;
import org.apache.ibatis.annotations.Param;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeInterceptObject;

import java.util.List;
import java.util.Map;

/**
 * 拦截对象表资源库
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:08
 */
public interface HmeInterceptObjectRepository extends BaseRepository<HmeInterceptObject> {
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
