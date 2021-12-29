package io.tarzan.common.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.vo.MtNumrangeRuleHisVO;

/**
 * 号码段定义组合规则历史表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:43
 */
public interface MtNumrangeRuleHisService {
    /**
     * numrangeRuleHisQueryForUi-获取号码段规则修改历史信息
     *
     * @param tenantId    租户id
     * @param eventId     事件id
     * @param pageRequest 分页参数
     * @return
     */
    Page<MtNumrangeRuleHisVO> numrangeRuleHisQueryForUi(Long tenantId, String eventId, PageRequest pageRequest);

}
