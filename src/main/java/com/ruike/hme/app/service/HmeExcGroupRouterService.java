package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeExcGroupRouterDTO;
import com.ruike.hme.domain.entity.HmeExcGroupRouter;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 异常收集组异常反馈路线表应用服务
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:23
 */
public interface HmeExcGroupRouterService {

    /**
     * 界面查询
     *
     * @param tenantId    租户ID
     * @param assignId    分配ID
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.entity.HmeExcGroupRouter>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/15 11:40:15
     */
    Page<HmeExcGroupRouter> listForUi(Long tenantId, String assignId, PageRequest pageRequest);

    /**
     * 界面删除
     *
     * @param tenantId 租户ID
     * @param dto      删除数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/15 11:40:42
     */
    void deleteForUi(Long tenantId, HmeExcGroupRouterDTO dto);
}
