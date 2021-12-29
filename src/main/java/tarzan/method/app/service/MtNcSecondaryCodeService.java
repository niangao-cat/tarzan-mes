package tarzan.method.app.service;

import java.util.List;

import tarzan.method.api.dto.MtNcSecondaryCodeDTO;

/**
 * 次级不良代码应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
public interface MtNcSecondaryCodeService {

    /**
     * UI根据对象查询次级不良代码
     * 
     * @author benjamin
     * @date 2019/9/10 12:57 PM
     * @param tenantId 租户Id
     * @param ncObjectId 对象Id
     * @param ncObjectType 对象类型
     * @return list
     */
    List<MtNcSecondaryCodeDTO> querySecondaryCodeListForUi(Long tenantId, String ncObjectId, String ncObjectType);

    /**
     * UI保存次级不良代码
     * 
     * @author benjamin
     * @date 2019/9/10 1:52 PM
     * @param tenantId 租户Id
     * @param ncObjectId 对象Id
     * @param ncObjectType 对象类型
     * @param dtoList List<MtNcSecondaryCodeDTO>
     */
    void saveSecondaryCodeListForUi(Long tenantId, String ncObjectId, String ncObjectType,
                                    List<MtNcSecondaryCodeDTO> dtoList);

    /**
     * UI删除次级不良代码
     * 
     * @author benjamin
     * @date 2019/9/10 2:20 PM
     * @param tenantId 租户Id
     * @param ncSecondaryCodeId 次级不良代码Id
     */
    void removeSecondaryCodeForUi(Long tenantId, String ncSecondaryCodeId);
}
