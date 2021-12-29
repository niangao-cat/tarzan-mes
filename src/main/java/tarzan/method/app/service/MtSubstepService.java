package tarzan.method.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.method.api.dto.MtSubstepDTO;
import tarzan.method.api.dto.MtSubstepDTO2;

/**
 * 子步骤应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:23:13
 */
public interface MtSubstepService {
    /**
     * UI查询子步骤
     *
     * @author benjamin
     * @date 2019/9/5 3:18 PM
     * @param tenantId 租户Id
     * @param dto MtSubstepDTO
     * @param pageRequest PageRequest
     * @return page
     */
    Page<MtSubstepDTO2> querySubstepForUi(Long tenantId, MtSubstepDTO dto, PageRequest pageRequest);

    /**
     * UI保存子步骤
     *
     * @author benjamin
     * @date 2019/9/5 3:37 PM
     * @param tenantId 租户Id
     * @param dto MtSubstepDTO2
     * @return id
     */
    String saveSubstepForUi(Long tenantId, MtSubstepDTO2 dto);

    /**
     * UI删除子步骤
     *
     * @author benjamin
     * @date 2019/9/5 4:57 PM
     * @param tenantId 租户Id
     * @param substepId 子步骤Id
     */
    void removeSubstepForUi(Long tenantId, String substepId);
}
