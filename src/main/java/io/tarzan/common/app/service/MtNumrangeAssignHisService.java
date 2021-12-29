package io.tarzan.common.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtNumrangeAssignHisDTO;
import io.tarzan.common.domain.vo.MtNumrangeAssignHisVO;

/**
 * 号码段分配历史表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
public interface MtNumrangeAssignHisService {

    /**
     * 获取号码段分配历史列表
     * @author xiao.tang02@hand-china.com 2019年8月16日上午11:19:49
     * @param tenantId
     * @param condition
     * @param pageRequest
     * @return
     * @return Page<MtNumrangeAssignHisVO>
     */
    Page<MtNumrangeAssignHisVO> listNumrangeAssignHisForUi(Long tenantId,
                                                           MtNumrangeAssignHisDTO condition, PageRequest pageRequest);
    
}
