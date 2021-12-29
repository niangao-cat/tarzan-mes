package tarzan.method.app.service;

import java.util.List;

import tarzan.method.api.dto.MtNcValidOperDTO;

/**
 * 不良代码工艺分配应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
public interface MtNcValidOperService {

    /**
     * UI根据对象查询不良代码工艺
     * 
     * @author benjamin
     * @date 2019/9/10 1:16 PM
     * @param tenantId 租户Id
     * @param ncObjectId 对象Id
     * @param ncObjectType 对象类型
     * @return list
     */
    List<MtNcValidOperDTO> queryNcValidOperListForUi(Long tenantId, String ncObjectId, String ncObjectType);

    /**
     * UI保存不良代码工艺
     * 
     * @author benjamin
     * @date 2019/9/10 2:09 PM
     * @param tenantId 租户Id
     * @param ncObjectId 对象Id
     * @param ncObjectType 对象类型
     * @param dtoList List<MtNcValidOperDTO>
     */
    void saveNcValidOperListForUi(Long tenantId, String ncObjectId, String ncObjectType,
                                  List<MtNcValidOperDTO> dtoList);

    /**
     * UI删除不良代码工艺
     * 
     * @author benjamin
     * @date 2019/9/10 2:21 PM
     * @param tenantId 租户Id
     * @param ncValidOperId 不良代码工艺Id
     */
    void removeNcValidOperForUi(Long tenantId, String ncValidOperId);
}
