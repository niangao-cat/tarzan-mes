package com.ruike.hme.app.service;

import java.util.List;

import com.ruike.hme.api.dto.HmeExceptionRouterDTO;
import com.ruike.hme.domain.entity.HmeExceptionRouter;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 异常反馈路线基础数据表应用服务
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:25
 */
public interface HmeExceptionRouterService {

    /**
     * 界面查询
     *
     * @param tenantId    租户ID
     * @param exceptionId 异常ID
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.entity.HmeExceptionRouter>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/15 11:35:11
     */
    Page<HmeExceptionRouter> listForUi(Long tenantId, String exceptionId, PageRequest pageRequest);

    /**
     * 界面批量保存
     *
     * @param tenantId 租户ID
     * @param dtoList  保存数据
     * @return java.util.List<com.ruike.hme.domain.entity.HmeExceptionRouter>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/15 11:35:11
     */
    List<HmeExceptionRouter> batchSaveForUi(Long tenantId, List<HmeExceptionRouter> dtoList);

    /**
     * 界面删除
     *
     * @param tenantId 租户ID
     * @param dto      删除数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/15 11:35:11
     */
    void deleteForUi(Long tenantId, HmeExceptionRouterDTO dto);
}
