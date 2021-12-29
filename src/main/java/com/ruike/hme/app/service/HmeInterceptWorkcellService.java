package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeInterceptWorkcellVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 拦截工序表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:11
 */
public interface HmeInterceptWorkcellService {

    /**
     * 拦截工序表查询参数
     *
     * @param tenantId    租户id
     * @param pageRequest 分页参数
     * @param interceptId 拦截信息表主键
     * @return
     */
    Page<HmeInterceptWorkcellVO> queryInterceptWorkcell(Long tenantId, PageRequest pageRequest, String interceptId);

    /**
     * 拦截工序表保存
     *
     * @param tenantId                   租户id
     * @param hmeInterceptWorkcellVOList 保存数据
     * @param interceptId                信息表id
     */
    void saveInterceptWorkcell(Long tenantId, List<HmeInterceptWorkcellVO> hmeInterceptWorkcellVOList, String interceptId);

    /**
     * 拦截工序放行
     *
     * @param tenantId                   租户id
     * @param interceptId                信息表id
     * @param hmeInterceptWorkcellVOList 放行数据
     */
    void passInterceptWorkcell(Long tenantId, String interceptId, List<HmeInterceptWorkcellVO> hmeInterceptWorkcellVOList);
}
