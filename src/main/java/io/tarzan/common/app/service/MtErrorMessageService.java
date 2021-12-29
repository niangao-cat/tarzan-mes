package io.tarzan.common.app.service;


import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtErrorMessageDTO3;
import io.tarzan.common.api.dto.MtErrorMessageDTO4;
import io.tarzan.common.domain.vo.MtErrorMessageVO;
import io.tarzan.common.domain.vo.MtErrorMessageVO2;

/**
 * 应用服务
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtErrorMessageService {

    /**
     * 获取消息列表
     * @author xiao.tang02@hand-china.com 2019年8月13日下午1:37:55
     * @param tenantId
     * @param condition
     * @param pageRequest
     * @return
     * @return List<MtErrorMessage>
     */
    Page<MtErrorMessageVO> listErrorMessageForUi(Long tenantId, MtErrorMessageDTO3 condition, PageRequest pageRequest);

    /**
     * 新增&更新消息
     * @author xiao.tang02@hand-china.com 2019年8月13日下午1:48:40
     * @param tenantId
     * @param dto
     * @return void
     */
    String saveErrorMessageForUi(Long tenantId, MtErrorMessageDTO4 dto);

    /**
     * 删除消息
     * @author xiao.tang02@hand-china.com 2019年8月13日下午1:48:40
     * @param tenantId
     * @param list
     * @return void
     */
    void deleteErrorMessageForUi(Long tenantId, List<MtErrorMessageVO2> list);
    
}
