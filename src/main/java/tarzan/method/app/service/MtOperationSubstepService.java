package tarzan.method.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.method.api.dto.MtOperationSubstepDTO;

/**
 * 工艺子步骤应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:19:27
 */
public interface MtOperationSubstepService {

    /**
     * UI查询工艺子步骤列表
     *
     * @author benjamin
     * @date 2019/9/9 11:30 AM
     * @param tenantId 租户Id
     * @param operationId 工艺Id
     * @param pageRequest PageRequest
     * @return page
     */
    Page<MtOperationSubstepDTO> queryOperationSubstepListForUi(Long tenantId, String operationId,
                                                               PageRequest pageRequest);

    /**
     * UI删除工艺子步骤
     *
     * @author benjamin
     * @date 2019/9/9 2:02 PM
     * @param tenantId 租户Id
     * @param operationSubstepId 工艺子步骤Id
     */
    void removeOperationSubstepForUi(Long tenantId, String operationSubstepId);

    /**
     * UI保存工艺子步骤
     *
     * @author benjamin
     * @date 2019/9/9 4:14 PM
     * @param tenantId 租户Id
     * @param operationId 工艺Id
     * @param dtoList List<MtOperationSubstepDTO>
     * @param operationNewFlag 工艺新增标识
     */
    void saveOperationSubstepForUi(Long tenantId, String operationId, List<MtOperationSubstepDTO> dtoList,
                                   Boolean operationNewFlag);
}
