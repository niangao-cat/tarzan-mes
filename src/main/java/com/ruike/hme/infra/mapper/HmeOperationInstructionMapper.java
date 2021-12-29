package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeOperationInstruction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * 作业指导Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-10-20 16:07:50
 */
public interface HmeOperationInstructionMapper extends BaseMapper<HmeOperationInstruction> {

    /**
     * 作业指导查询
     *
     * @param tenantId
     * @param insHeaderId
     * @author jiangling.zheng@hand-china.com 2020/10/20 17:04
     * @return java.util.List<com.ruike.hme.api.dto.HmeOperationInsDTO>
     */
    List<HmeOperationInstruction> operationInsQuery(@Param("tenantId")Long tenantId,
                                                    @Param("insHeaderId")String insHeaderId);
}
