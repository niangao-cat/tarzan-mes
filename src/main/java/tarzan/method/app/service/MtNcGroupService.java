package tarzan.method.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.method.api.dto.MtNcGroupDTO;
import tarzan.method.api.dto.MtNcGroupDTO2;
import tarzan.method.api.dto.MtNcGroupDTO4;

/**
 * 不良代码组应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
public interface MtNcGroupService {

    /**
     * UI查询不良代码组
     *
     * @author benjamin
     * @date 2019/9/10 1:48 PM
     * @param tenantId 租户Id
     * @param dto MtNcGroupDTO
     * @param pageRequest PageRequest
     * @return page
     */
    Page<MtNcGroupDTO> queryNcGroupListForUi(Long tenantId, MtNcGroupDTO dto, PageRequest pageRequest);

    /**
     * UI根据不良代码组Id查询不良代码组详细信息
     *
     * @author benjamin
     * @date 2019/9/10 1:49 PM
     * @param tenantId 租户Id
     * @param ncGroupId 不良代码组Id
     * @return MtNcGroupDTO2
     */
    MtNcGroupDTO2 queryNcGroupDetailForUi(Long tenantId, String ncGroupId);

    /**
     * UI保存不良代码组
     *
     * @author benjamin
     * @date 2019/9/10 2:29 PM
     * @param tenantId 租户Id
     * @param dto MtNcCodeDTO4
     * @return String
     */
    String saveNcGroupForUi(Long tenantId, MtNcGroupDTO4 dto);
}
