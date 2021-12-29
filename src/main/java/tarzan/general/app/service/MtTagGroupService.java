package tarzan.general.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.general.api.dto.MtTagGroupDTO2;
import tarzan.general.api.dto.MtTagGroupDTO4;
import tarzan.general.api.dto.MtTagGroupDTO5;
import tarzan.general.api.dto.MtTagGroupDTO6;

/**
 * 数据收集组表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupService {

    /**
     * UI查询数据收集组列表信息
     * 
     * @author benjamin
     * @date 2019/9/17 10:13 AM
     * @param tenantId 租户Id
     * @param dto MtTagGroupDTO2
     * @param pageRequest PageRequest
     * @return page
     */
    Page<MtTagGroupDTO2> queryTagGroupListForUi(Long tenantId, MtTagGroupDTO2 dto, PageRequest pageRequest);

    /**
     * UI查询数据收集组详细信息
     * 
     * @author benjamin
     * @date 2019/9/17 11:36 AM
     * @param tenantId 租户Id
     * @param tagGroupId 数据收集组Id
     * @return String
     */
    MtTagGroupDTO4 queryTagGroupDetailForUi(Long tenantId, String tagGroupId);

    /**
     * UI保存数据收集组
     * 
     * @author benjamin
     * @date 2019/9/17 3:29 PM
     * @param tenantId 租户Id
     * @param dto MtTagGroupDTO6
     * @return String
     */
    String saveTagGroupForUi(Long tenantId, MtTagGroupDTO5 dto);

    /**
     * UI复制数据收集组
     * 
     * @author benjamin
     * @date 2019/9/17 5:53 PM
     * @param tenantId 租户Id
     * @param dto MtTagGroupDTO7
     * @return String
     */
    String copyTagGroupForUi(Long tenantId, MtTagGroupDTO6 dto);
}
