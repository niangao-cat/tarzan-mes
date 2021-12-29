package tarzan.general.app.service;

import tarzan.general.api.dto.MtTagGroupObjectDTO2;

/**
 * 数据收集组关联对象表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupObjectService {

    /**
     * UI查询数据收集组关联对象
     * 
     * @author benjamin
     * @date 2019/9/17 2:45 PM
     * @param tenantId 租户Id
     * @param tagGroupId 数据收集组Id
     * @return MtTagGroupObjectDTO2
     */
    MtTagGroupObjectDTO2 queryTagGroupObjectForUi(Long tenantId, String tagGroupId);

    /**
     * UI保存数据收集组关联对象
     * 
     * @author benjamin
     * @date 2019/9/17 3:40 PM
     * @param tenantId 租户Id
     * @param tagGroupId 数据收集组Id
     * @param dto MtTagGroupObjectDTO2
     * @return String
     */
    String saveTagGroupObjectForUi(Long tenantId, String tagGroupId, MtTagGroupObjectDTO2 dto);
}
