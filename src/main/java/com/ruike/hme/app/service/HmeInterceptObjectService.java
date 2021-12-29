package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeInterceptObjectVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 拦截对象表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:08
 */
public interface HmeInterceptObjectService {

    /**
     * 拦截对象表查询参数
     *
     * @param tenantId    租户id
     * @param interceptId 信息表主键
     * @param pageRequest 分页参数
     * @return
     */
    Page<HmeInterceptObjectVO> queryInterceptObject(Long tenantId, String interceptId, PageRequest pageRequest);


    /**
     * 新增拦截对象
     *
     * @param tenantId                 租户id
     * @param hmeInterceptObjectVOList 保存数据
     * @param interceptId 信息表id
     */
    void saveInterceptObject(Long tenantId, List<HmeInterceptObjectVO> hmeInterceptObjectVOList,String interceptId);

    /**
     * 拦截对象放行
     *
     * @param tenantId                 租户id
     * @param interceptId              拦截信息表头id
     * @param hmeInterceptObjectVOList 要更新数据
     */
    void passInterceptObject(Long tenantId, String interceptId, List<HmeInterceptObjectVO> hmeInterceptObjectVOList);

}
