package com.ruike.qms.app.service;

import com.ruike.qms.api.dto.*;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * ICQ检验看板 应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-05-06 10:37:10
 */
public interface QmsIqcExamineBoardService {

    /**
     * 获取ICQ检验看板信息
     *
     * @param tenantId    租户ID
     * @param pageRequest 分页参数
     * @return List<QmsIqcExamineBoardDTO>
     * @author jiangling.zheng@hand-china.com
     */
    Page<QmsIqcExamineBoardDTO> selectIqcExamineBoardForUi(Long tenantId, PageRequest pageRequest);

    /**
     * 获取30天物料量
     *
     * @param tenantId 租户ID
     * @return List<QmsIqcMonthDTO>
     * @author jiangling.zheng@hand-china.com
     */
    List<QmsIqcCalSumDTO> selectIqcDayForUi(Long tenantId);

    /**
     * 获取30天物料量
     *
     * @param tenantId 租户ID
     * @return List<QmsIqcMonthDTO>
     * @author jiangling.zheng@hand-china.com
     */
    List<QmsIqcCalSumDTO> selectIqcMonthForUi(Long tenantId);

}
