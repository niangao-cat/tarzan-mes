package tarzan.general.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.general.api.dto.MtTagGroupAssignDTO;
import tarzan.general.api.dto.MtTagGroupAssignDTO2;

/**
 * 数据收集项分配收集组表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupAssignService {

    /**
     * UI查询数据收集组分配的数据收集项
     * 
     * @author benjamin
     * @date 2019/9/17 2:11 PM
     * @param tenantId 租户Id
     * @param dto MtTagGroupAssignDTO2
     * @param pageRequest PageRequest
     * @return list
     */
    Page<MtTagGroupAssignDTO> queryTagGroupAssignForUi(Long tenantId, MtTagGroupAssignDTO2 dto,
                                                       PageRequest pageRequest);

    /**
     * UI查询数据收集组分配数据收集项
     * 
     * @author benjamin
     * @date 2019/9/17 8:46 PM
     * @param tenantId 租户Id
     * @param dto MtTagGroupAssignDTO2
     * @return list
     */
    List<MtTagGroupAssignDTO> queryTagGroupAssignForUi(Long tenantId, MtTagGroupAssignDTO2 dto);

    /**
     * UI保存数据收集组分配数据收集项
     * 
     * @author benjamin
     * @date 2019/9/17 2:31 PM
     * @param tenantId 租户Id
     * @param tagGroupId 数据收集组Id
     * @param dtoList 收集组分配数据收集项集合
     */
    void saveTagGroupAssignForUi(Long tenantId, String tagGroupId, List<MtTagGroupAssignDTO> dtoList);

    /**
     * UI删除数据收集组分配数据项
     * 
     * @author benjamin
     * @date 2019/9/17 2:34 PM
     * @param tenantId 租户Id
     * @param tagGroupAssignId 分配Id
     */
    void removeTagGroupAssignForUi(Long tenantId, String tagGroupAssignId);
}
