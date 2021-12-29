package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeInterceptReleaseVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 拦截例外放行表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:10
 */
public interface HmeInterceptReleaseService {

    /**
     * 拦截例外放行表查询参数
     *
     * @param tenantId    租户id
     * @param interceptId 拦截信息表id
     * @param pageRequest 分页参数
     * @return
     */
    Page<HmeInterceptReleaseVO> queryInterceptRelease(Long tenantId, String interceptId, PageRequest pageRequest);


    /**
     * 拦截例外数据添加
     *
     * @param tenantId                  租户id
     * @param interceptId               拦截信息头id
     * @param hmeInterceptReleaseVOList 保存数据
     */
    void saveInterceptRelease(Long tenantId, String interceptId, List<HmeInterceptReleaseVO> hmeInterceptReleaseVOList);

}
