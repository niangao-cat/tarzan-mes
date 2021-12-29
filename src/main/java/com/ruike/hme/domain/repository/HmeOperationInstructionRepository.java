package com.ruike.hme.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeOperationInstruction;

import java.util.List;

/**
 * 作业指导资源库
 *
 * @author jiangling.zheng@hand-china.com 2020-10-20 16:07:50
 */
public interface HmeOperationInstructionRepository extends BaseRepository<HmeOperationInstruction>, AopProxy<HmeOperationInstructionRepository> {

    /**
     * 作业指导查询
     *
     * @param tenantId
     * @param insHeaderId
     * @author jiangling.zheng@hand-china.com 2020/10/20 17:22
     * @return java.util.List<com.ruike.hme.api.dto.HmeOperationInsDTO>
     */
    List<HmeOperationInstruction> operationInsQuery(Long tenantId, String insHeaderId);

    /**
     * 作业指导创建/更新
     *
     * @param tenantId
     * @param hmeOperationInstruction
     * @author jiangling.zheng@hand-china.com 2020/10/20 17:28
     * @return com.ruike.hme.domain.entity.HmeOperationInstruction
     */
    HmeOperationInstruction operationInsUpdate(Long tenantId, HmeOperationInstruction hmeOperationInstruction);
}
