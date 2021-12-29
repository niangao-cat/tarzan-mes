package io.tarzan.common.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtGenStatusDTO;
import io.tarzan.common.api.dto.MtGenStatusDTO2;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.vo.MtGenStatusVO2;
import io.tarzan.common.domain.vo.MtGenStatusVO3;

/**
 * 状态应用服务
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtGenStatusService {

    List<MtGenStatus> comboBoxUi(Long tenantId, MtGenStatusVO2 condition);
    
    /**
     * 获取状态列表
     * @author xiao.tang02@hand-china.com 2019年8月13日下午7:13:07
     * @param tenantId
     * @param condition
     * @param pageRequest
     * @return
     * @return Page<MtGenStatus>
     */
    Page<MtGenStatusVO3> listGenStatusForUi(Long tenantId, MtGenStatusDTO condition, PageRequest pageRequest);

    /**
     * 新增&更新状态
     * @author xiao.tang02@hand-china.com 2019年8月13日下午7:13:29
     * @param tenantId
     * @param dto
     * @return
     * @return String
     */
    String saveGenStatusForUi(Long tenantId, MtGenStatusDTO2 dto);

    /**
     * 删除状态
     * @author xiao.tang02@hand-china.com 2019年8月13日下午7:13:29
     * @param tenantId
     * @param list
     * @return
     * @return String
     */
    void removeGenStatusForUi(Long tenantId, List<MtGenStatus> list);
}
