package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeOperationInstruction;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 作业指导应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-10-20 16:07:50
 */
public interface HmeOperationInstructionService {

    /**
     * 作业指导查询
     *
     * @param tenantId
     * @param insHeaderId
     * @param pageRequest
     * @author jiangling.zheng@hand-china.com 2020/10/20 17:13
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeOperationInsDTO>
     */
    Page<HmeOperationInstruction> listForUi(Long tenantId, String insHeaderId, PageRequest pageRequest);

    /**
     * 作业指导创建
     *
     * @param tenantId
     * @param hmeOperationInsList
     * @author jiangling.zheng@hand-china.com 2020/10/20 17:28
     * @return com.ruike.hme.domain.entity.HmeOperationInstruction
     */
    List<HmeOperationInstruction> saveForUi(Long tenantId, List<HmeOperationInstruction> hmeOperationInsList);

}
